package de.cebrero.drwho.core;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

/** No test dependencies: runs on JDK 17 and in Gradle via :core:smokeTest. */
public final class CoreTest {
    static int checks;
    static final class MemoryStore implements GameEngine.Store {
        byte[] data; boolean fail;
        public byte[] load() { return data == null ? null : data.clone(); }
        public void save(byte[] value) throws IOException { if (fail) throw new IOException("disk full"); data = value.clone(); }
    }
    static final class Dice extends SecureRandom {
        int rolls;
        @Override public int nextInt(int bound) { if (bound == 6) { rolls++; return 3; } return 123456; }
    }
    static void check(boolean test, String message) { checks++; if (!test) throw new AssertionError(message); }
    interface Throwing { void run() throws Exception; }
    static void rejects(Class<? extends Exception> type, Throwing run) throws Exception {
        try { run.run(); throw new AssertionError("Expected " + type); }
        catch (Exception e) { check(type.isInstance(e), "Wrong failure: " + e); }
    }
    static String text(String json, String key) {
        Matcher m = Pattern.compile("\"" + key + "\":\"([^\"]*)\"").matcher(json);
        if (!m.find()) throw new AssertionError("Missing " + key); return m.group(1);
    }
    static long revision(String json) {
        Matcher m = Pattern.compile("\"revision\":([0-9]+)").matcher(json); if (!m.find()) throw new AssertionError("revision"); return Long.parseLong(m.group(1));
    }
    static String id() { return UUID.randomUUID().toString(); }
    static String join(GameEngine e, String name) throws Exception { return text(e.join(name, e.code(), id()), "token"); }
    public static void main(String[] args) throws Exception {
        lobbyAndTurns(); persistence(); concurrentRetry(); http();
        System.out.println("PASS: " + checks + " checks (lobby, turns, persistence, retries, HTTP)");
    }
    static void lobbyAndTurns() throws Exception {
        MemoryStore store = new MemoryStore(); Dice dice = new Dice(); GameEngine e = new GameEngine(store, dice);
        rejects(IllegalArgumentException.class, () -> e.join("A", "wrong", id()));
        rejects(IllegalArgumentException.class, e::startDemo);
        String request = id(); String a = text(e.join("Doctor", e.code(), request), "token");
        check(a.equals(text(e.join("Doctor", e.code(), request), "token")), "join retry reserves same slot");
        String b = join(e, "Companion"); join(e, "Player 3"); join(e, "Player 4");
        rejects(IllegalArgumentException.class, () -> join(e, "Player 5"));
        rejects(SecurityException.class, () -> e.snapshot("invalid"));
        String snapshot = e.snapshot(a);
        check(!snapshot.contains(a) && !snapshot.contains(e.code()), "public snapshot hides tokens and join code");
        e.startDemo();
        rejects(IllegalArgumentException.class, () -> e.command(b,id(),revision(e.snapshot(b)),"roll",""));
        rejects(IllegalArgumentException.class, () -> e.command(a,id(),revision(e.snapshot(a)),"move","N"));
        rejects(IllegalArgumentException.class, () -> e.command(a,id(),revision(e.snapshot(a)),"move","E"));
        long before = revision(e.snapshot(a));
        String command = id(); String result = e.command(a,command,before,"roll","");
        check(result.contains("\"lastRoll\":4"), "host rolls die");
        check(result.equals(e.command(a,command,before,"roll","")), "duplicate does not mutate");
        check(dice.rolls == 1, "no reroll on retry");
        rejects(IllegalArgumentException.class, () -> e.command(b,id(),before,"pass",""));
        e.command(b,id(),revision(e.snapshot(b)),"move","S");
        check(e.snapshot(b).contains("\"x\":1,\"y\":1"), "movement recorded");
    }
    static void persistence() throws Exception {
        MemoryStore store = new MemoryStore(); Dice dice = new Dice(); GameEngine e = new GameEngine(store, dice);
        String a = join(e, "A"); e.startDemo(); String request = id(); long rev = revision(e.snapshot(a));
        e.command(a,request,rev,"roll","");
        String snapshot = e.snapshot(a), code = e.code();
        GameEngine restored = new GameEngine(store, dice);
        check(snapshot.equals(restored.snapshot(a)) && code.equals(restored.code()), "full restart preserves game and identity");
        restored.command(a,request,rev,"roll",""); check(dice.rolls == 1, "retry survives restart");
        store.fail = true;
        rejects(IOException.class, () -> restored.command(a,id(),revision(restored.snapshot(a)),"move","S"));
        check(snapshot.equals(restored.snapshot(a)), "failed save leaves authoritative state unchanged");
        store.fail = false; store.data = new byte[]{1,2,3};
        rejects(IOException.class, () -> new GameEngine(store));
        check(Arrays.equals(store.data,new byte[]{1,2,3}), "corrupt save never silently reset");
        check(GameEngine.quote("\"\\\n").equals("\"\\\"\\\\\\u000a\""), "JSON escaping");
    }
    static void concurrentRetry() throws Exception {
        MemoryStore store = new MemoryStore(); Dice dice = new Dice(); GameEngine e = new GameEngine(store,dice);
        String a = join(e,"A"); e.startDemo(); String req = id(); long rev = revision(e.snapshot(a));
        ExecutorService pool = Executors.newFixedThreadPool(4);
        try {
            List<Callable<String>> jobs = new ArrayList<>();
            for(int i=0;i<8;i++) jobs.add(() -> e.command(a,req,rev,"roll",""));
            List<Future<String>> results = pool.invokeAll(jobs);
            String first = results.get(0).get(); for(Future<String> r:results) check(first.equals(r.get()),"concurrent retry stable");
            check(dice.rolls == 1,"one random event under concurrent requests");
        } finally { pool.shutdownNow(); }
    }
    static final class Response {
        int status; String body;
        Response(int status,String body) { this.status=status;this.body=body; }
    }
    static Response call(LocalServer server, String path, String body, String token, boolean customHeader) throws IOException {
        HttpURLConnection c = (HttpURLConnection)new URL("http://127.0.0.1:" + server.port() + path).openConnection();
        c.setConnectTimeout(3000); c.setReadTimeout(3000);
        if(token!=null)c.setRequestProperty("Authorization","Bearer "+token);
        if(body!=null){
            c.setRequestMethod("POST");c.setDoOutput(true);c.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            if(customHeader)c.setRequestProperty("X-Dr-Who","1");
            c.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));
        }
        int status=c.getResponseCode();
        try(InputStream in=status>=400?c.getErrorStream():c.getInputStream()){
            return new Response(status,new String(in.readAllBytes(),StandardCharsets.UTF_8));
        }finally{c.disconnect();}
    }
    static void http() throws Exception {
        GameEngine e = new GameEngine(new MemoryStore());
        try(LocalServer server=new LocalServer(e,"<h1>Dr_Who</h1>".getBytes(StandardCharsets.UTF_8),0)){
            check(call(server,"/",null,null,false).body.contains("Dr_Who"),"player page served");
            check(call(server,"/api/state",null,null,false).status==401,"unauthenticated state blocked");
            check(call(server,"/api/admin",null,null,false).status==404,"no remote host endpoint");
            String form="name=A&code="+e.code()+"&requestId="+id();
            check(call(server,"/api/join",form,null,false).status==401,"cross-site form blocked");
            Response joined=call(server,"/api/join",form,null,true);check(joined.status==200,"HTTP join");
            String token=text(joined.body,"token");
            check(call(server,"/api/state",null,token,false).status==200,"authenticated snapshot");
            e.startDemo();
            String cmd="action=roll&revision="+revision(e.snapshot(token))+"&requestId="+id();
            Response roll=call(server,"/api/command",cmd,token,true);check(roll.status==200,"HTTP command");
            check(call(server,"/api/command",cmd,token,true).body.equals(roll.body),"HTTP retry idempotent");
            for(int i=0;i<8;i++)call(server,"/api/join","name=B&code=wrong&requestId="+id(),null,true);
            check(call(server,"/api/join",form,null,true).status==429,"bad-code attempts throttled");
        }
    }
}
