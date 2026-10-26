package de.cebrero.drwho.core;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;
import java.time.*;

/** No test dependencies: runs on JDK 17 and in Gradle via :core:smokeTest. */
public final class CoreTest {
    static int checks;
    static final class MemoryStore implements GameEngine.Store {
        byte[] data; boolean fail;
        public byte[] load() { return data == null ? null : data.clone(); }
        public void save(byte[] value) throws IOException { if (fail) throw new IOException("disk full"); data = value.clone(); }
    }
    static final class Dice extends SecureRandom {
        int rolls, helperDraws, face = 4;
        @Override public int nextInt(int bound) {
            if (bound == 6) { rolls++; return face-1; }
            if (bound <= 4) { helperDraws++; return bound-1; }
            return 123456;
        }
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
    static GameEngine fourCharacterFixture(MemoryStore store, Dice dice) throws IOException {
        List<CharacterProfile> catalogue = new ArrayList<>(CharacterProfile.known());
        catalogue.add(new CharacterProfile("test-only", "Testfigur", "Nur Testdaten, keine Serienfigur", "offen", "offen", ""));
        return new GameEngine(store, dice, catalogue);
    }
    static String ready(GameEngine e, String name, String character) throws Exception {
        String token = join(e,name); e.chooseCharacter(token,id(),character); return token;
    }
    static void begin(GameEngine e,String token)throws Exception{e.command(token,id(),revision(e.snapshot(token)),"beginTurn","");}
    public static void main(String[] args) throws Exception {
        lobbyAndTurns(); diceRules(); persistence(); concurrentRetry(); characterRace(); enemyPhase(); migration(); presentation(); turnStartAndStory(); directedActions(); http(); connections();
        System.out.println("PASS: " + checks + " checks (characters, enemies, migration, persistence, retries, HTTP)");
    }
    static void lobbyAndTurns() throws Exception {
        MemoryStore store = new MemoryStore(); Dice dice = new Dice(); GameEngine e = fourCharacterFixture(store, dice);
        rejects(IllegalArgumentException.class, () -> e.join("A", "wrong", id()));
        rejects(IllegalArgumentException.class, e::startDemo);
        String request = id(); String a = text(e.join("Doctor", e.code(), request), "token");
        check(a.equals(text(e.join("Doctor", e.code(), request), "token")), "join retry reserves same slot");
        String b = join(e, "Companion"), c = join(e, "Player 3"), d = join(e, "Player 4");
        rejects(IllegalArgumentException.class, () -> join(e, "Player 5"));
        rejects(SecurityException.class, () -> e.snapshot("invalid"));
        String snapshot = e.snapshot(a);
        check(!snapshot.contains(a) && !snapshot.contains(e.code()), "public snapshot hides tokens and join code");
        rejects(IllegalArgumentException.class, e::startDemo);
        e.chooseCharacter(a,id(),"doctor"); e.chooseCharacter(b,id(),"dalek");
        e.chooseCharacter(c,id(),"weeping-angel"); e.chooseCharacter(d,id(),"test-only");
        e.startDemo();
        rejects(IllegalArgumentException.class, () -> e.command(b,id(),revision(e.snapshot(b)),"roll",""));
        rejects(IllegalArgumentException.class, () -> e.command(a,id(),revision(e.snapshot(a)),"move","N"));
        rejects(IllegalArgumentException.class, () -> e.command(a,id(),revision(e.snapshot(a)),"move","E"));
        begin(e,a);long before = revision(e.snapshot(a));
        String command = id(); String result = e.command(a,command,before,"roll","");
        check(result.contains("\"lastRoll\":4"), "host rolls die");
        check(result.equals(e.command(a,command,before,"roll","")), "duplicate does not mutate");
        check(dice.rolls == 1, "no reroll on retry");
        rejects(IllegalArgumentException.class, () -> e.command(a,id(),revision(e.snapshot(a)),"roll",""));
        rejects(IllegalArgumentException.class, () -> e.command(a,id(),revision(e.snapshot(a)),"action",""));
        rejects(IllegalArgumentException.class, () -> e.command(b,id(),before,"pass",""));
        for (int step = 0; step < 4; step++) e.command(a,id(),revision(e.snapshot(a)),"move","S");
        begin(e,b);e.command(b,id(),revision(e.snapshot(b)),"roll","");
        e.command(b,id(),revision(e.snapshot(b)),"move","S");
        check(e.snapshot(b).contains("\"x\":1,\"y\":1"), "movement recorded");
    }
    static void diceRules() throws Exception {
        for (int face = 1; face <= 6; face++) {
            MemoryStore store = new MemoryStore(); Dice dice = new Dice(); dice.face = face;
            GameEngine e = new GameEngine(store,dice); String a = ready(e,"A","doctor"),b=ready(e,"B","dalek");e.startDemo();begin(e,a);
            rejects(IllegalArgumentException.class,()->e.command(a,id(),revision(e.snapshot(a)),"action",""));
            e.command(a,id(),revision(e.snapshot(a)),"roll","");
            String rolled=e.snapshot(a);check(rolled.contains("\"roll\":"+face),"die value saved for each face");
            if(face>=5){
                check(rolled.contains("\"stage\":\"choice\""),"5/6 enable movement/action choice");
                rejects(IllegalArgumentException.class,()->e.command(a,id(),revision(e.snapshot(a)),"move","S"));
                e.command(a,id(),revision(e.snapshot(a)),"chooseMove","");
            } else check(rolled.contains("\"stage\":\"move\""),"1–4 require movement");
            for(int step=0;step<face;step++){
                // S then E then S... exercises right-angle changes without diagonal movement.
                String direction=step%2==0?"S":"E",request=id();long rev=revision(e.snapshot(a));
                String moved=e.command(a,request,rev,"move",direction);
                check(moved.equals(e.command(a,request,rev,"move",direction)),"movement retry never consumes two steps");
                if(step+1<face) {
                    check(moved.contains("\"remaining\":"+(face-step-1)),"remaining steps exact");
                    GameEngine restored=new GameEngine(store,dice);check(restored.snapshot(a).equals(moved),"mid-movement restart exact");
                    rejects(IllegalArgumentException.class,()->e.command(a,id(),revision(e.snapshot(a)),"action",""));
                }
            }
            check(e.snapshot(a).contains("\"current\":"+GameEngine.quote(text(e.snapshot(b),"you"))),"turn ends after exact die count");
            check(e.snapshot(a).contains("\"stage\":\"roll\""),"next player starts with a new die roll");
        }
        MemoryStore store=new MemoryStore();Dice dice=new Dice();dice.face=5;GameEngine e=new GameEngine(store,dice);
        String a=ready(e,"A","doctor");e.startDemo();begin(e,a);e.command(a,id(),revision(e.snapshot(a)),"roll","");
        GameEngine restored=new GameEngine(store,dice);check(restored.snapshot(a).contains("\"stage\":\"choice\""),"5/6 choice survives reload without reroll");
        restored.command(a,id(),revision(restored.snapshot(a)),"action","N");
        check(restored.snapshot(a).contains("\"round\":2"),"action replaces movement and ends turn");
        check(restored.snapshot(a).contains("Wirkung noch nicht berechnet"),"unfinished character effects explicitly marked");
    }
    static void persistence() throws Exception {
        MemoryStore store = new MemoryStore(); Dice dice = new Dice(); GameEngine e = new GameEngine(store, dice);
        String a = ready(e, "A", "doctor"); e.startDemo();begin(e,a); String request = id(); long rev = revision(e.snapshot(a));
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
        String a = ready(e,"A","doctor"); e.startDemo();begin(e,a); String req = id(); long rev = revision(e.snapshot(a));
        ExecutorService pool = Executors.newFixedThreadPool(4);
        try {
            List<Callable<String>> jobs = new ArrayList<>();
            for(int i=0;i<8;i++) jobs.add(() -> e.command(a,req,rev,"roll",""));
            List<Future<String>> results = pool.invokeAll(jobs);
            String first = results.get(0).get(); for(Future<String> r:results) check(first.equals(r.get()),"concurrent retry stable");
            check(dice.rolls == 1,"one random event under concurrent requests");
        } finally { pool.shutdownNow(); }
    }
    static void characterRace() throws Exception {
        MemoryStore store = new MemoryStore(); GameEngine e = new GameEngine(store);
        String a=join(e,"Alice"),b=join(e,"Bob");
        rejects(IllegalArgumentException.class,()->e.addEnemy("dalek"));
        rejects(SecurityException.class,()->e.chooseCharacter("fake",id(),"doctor"));
        rejects(IllegalArgumentException.class,()->e.chooseCharacter(a,id(),"not-known"));
        ExecutorService pool=Executors.newFixedThreadPool(2); CountDownLatch gate=new CountDownLatch(1);
        List<Future<Boolean>> results=new ArrayList<>();
        try {
            for(String token:Arrays.asList(a,b)) results.add(pool.submit(()->{gate.await();try{e.chooseCharacter(token,id(),"doctor");return true;}catch(IllegalArgumentException taken){return false;}}));
            gate.countDown();int successes=0;for(Future<Boolean> f:results)if(f.get())successes++;
            check(successes==1,"simultaneous character pick has exactly one winner");
        } finally {pool.shutdownNow();}
        String state=e.snapshot(a);check(state.contains("\"selectedCount\":1"),"one selected character");
        String doctorOwner=text(state.substring(state.indexOf("\"characters\"")),"owner");
        String aliceId=text(state,"you");String winner=doctorOwner.equals(aliceId)?a:b,loser=winner.equals(a)?b:a;
        rejects(IllegalArgumentException.class,()->e.chooseCharacter(loser,id(),"doctor"));
        e.chooseCharacter(loser,id(),"dalek");check(e.readyForPlay(),"all ready after exclusive choices");
        check(e.availableEnemies().size()==1,"only unused character can be added");
        rejects(IllegalArgumentException.class,()->e.addEnemy("doctor"));
        e.addEnemy("weeping-angel");String before=e.snapshot(a);e.addEnemy("weeping-angel");
        check(before.equals(e.snapshot(a)),"adding enemy twice is idempotent");
        rejects(IllegalArgumentException.class,()->e.chooseCharacter(loser,id(),"weeping-angel"));
        check(before.contains("\"team\":\"partners\"")&&before.contains("\"team\":\"enemies\""),"allegiance stored independent from character");
        check(before.contains("Noch festzulegen"),"unknown attributes not invented");
        check(new GameEngine(store).snapshot(a).equals(before),"character assignments and enemy list survive restart");
        e.startDemo();rejects(IllegalArgumentException.class,()->e.chooseCharacter(winner,id(),"doctor"));
    }
    static void enemyPhase() throws Exception {
        MemoryStore store=new MemoryStore();Dice dice=new Dice();GameEngine e=new GameEngine(store,dice);
        dice.face=5;
        String a=ready(e,"Alice","doctor"),b=ready(e,"Bob","dalek");e.addEnemy("weeping-angel");e.startDemo();
        rejects(IllegalArgumentException.class,e::requestEnemyMovement);
        for(String token:Arrays.asList(a,b)){begin(e,token);e.command(token,id(),revision(e.snapshot(token)),"roll","");e.command(token,id(),revision(e.snapshot(token)),"action","N");}
        check(e.snapshot(a).contains("\"phase\":\"enemies\""),"enemy phase follows all partners");
        check(e.snapshot(a).contains("\"round\":1"),"round ends after enemies");
        String helper=e.requestEnemyMovement();check(helper.equals("Bob"),"uniform bounded draw selects helper");
        e.requestEnemyMovement();check(dice.helperDraws==1,"repeated host request does not choose again");
        check(text(e.snapshot(a),"helperId").equals(text(e.snapshot(b),"you")),"only assigned helper involved");
        rejects(IllegalArgumentException.class,()->e.command(a,id(),revision(e.snapshot(a)),"confirmMovement",""));
        rejects(IllegalArgumentException.class,e::finishEnemyTurn);
        String move=id();long rev=revision(e.snapshot(b));e.command(b,move,rev,"confirmMovement","");
        String afterMove=e.snapshot(b);e.command(b,move,rev,"confirmMovement","");check(afterMove.equals(e.snapshot(b)),"movement confirmation retry stable");
        rejects(IllegalArgumentException.class,e::requestEnemyMovement);
        e.reportAttack(text(e.snapshot(a),"you"),"Der Engel hat dich für 2 Runden gebannt (Testmeldung).");
        String attacked=e.snapshot(a);check(attacked.contains("\"pending\":true")&&attacked.contains("für 2 Runden"),"target receives exact recorded outcome");
        rejects(IllegalArgumentException.class,()->e.command(b,id(),revision(e.snapshot(b)),"ackAttack",""));
        rejects(IllegalArgumentException.class,e::finishEnemyTurn);
        GameEngine restored=new GameEngine(store,dice);check(attacked.equals(restored.snapshot(a)),"pending attack and phase survive restart");
        restored.command(a,id(),revision(restored.snapshot(a)),"ackAttack","");restored.finishEnemyTurn();
        check(restored.snapshot(a).contains("\"round\":2")&&restored.snapshot(a).contains("\"phase\":\"players\""),"next round begins after acknowledged enemy turn");
    }
    static void migration() throws Exception {
        for(int format:new int[]{1,2,3,4,5}){
            MemoryStore store=new MemoryStore();ByteArrayOutputStream bytes=new ByteArrayOutputStream();DataOutputStream out=new DataOutputStream(bytes);
            out.writeInt(0x4457484f);out.writeInt(format);out.writeUTF("old-game");out.writeUTF("123456");out.writeBoolean(true);
            out.writeInt(7);out.writeInt(0);out.writeInt(4);out.writeLong(10);out.writeInt(1);
            out.writeUTF("old-player");out.writeUTF("old-token");out.writeUTF("Alice");out.writeUTF(id());out.writeInt(2);out.writeInt(3);
            if(format>=2){out.writeUTF("doctor");out.writeInt(1);out.writeUTF("dalek");}
            out.writeInt(1);out.writeUTF("Ein alter Eintrag ohne Zeitstempel.");out.writeInt(0);
            if(format>=3){out.writeUTF("players");out.writeInt(0);out.writeUTF("");out.writeBoolean(false);out.writeUTF("");out.writeUTF("");out.writeUTF("");out.writeUTF("");out.writeBoolean(false);}
            if(format>=4){out.writeUTF(format==5?"move":"roll");out.writeInt(format==5?4:0);out.writeInt(format==5?2:0);}
            if(format>=5){out.writeUTF(id());out.writeUTF("old-player");}
            store.data=bytes.toByteArray();GameEngine e=new GameEngine(store);
            if(format==1){check(!e.readyForPlay(),"v1 requires character before resuming");e.chooseCharacter("old-token",id(),"doctor");}
            check(e.playing(),"old game resumes after setup");check(e.snapshot("old-token").contains("\"round\":7"),"migration keeps round");
            check(e.snapshot("old-token").contains("\"x\":2,\"y\":3"),"migration keeps position");
            check(e.snapshot("old-token").contains("\"speaker\":\"Archiv\"")&&e.snapshot("old-token").contains("\"timestampMillis\":0"),"old logs remain honest undated archive");
            if(format==5)check(e.snapshot("old-token").contains("\"accepted\":true")&&e.snapshot("old-token").contains("\"remaining\":2"),"mid-move v5 save resumes without another Go or reroll");
        }
    }
    static void presentation() throws Exception {
        MemoryStore store=new MemoryStore();Dice dice=new Dice();dice.face=5;GameEngine e=new GameEngine(store,dice);
        String token=ready(e,"Alice","doctor");e.startDemo();
        check(e.portalState().headline.contains("Alice")&&e.portalState().detail.contains("Go")&&e.portalState().awaitingGo,"portal waits for active player Go");
        begin(e,token);
        String request=id();long before=revision(e.snapshot(token));e.command(token,request,before,"roll","");
        check(!e.sessionId().equals(new GameEngine(new MemoryStore()).sessionId()),"fresh installation has different party identity even with same app version");
        GameEngine.PortalState rolled=e.portalState();
        check(!rolled.rollId.isEmpty()&&rolled.die==5&&rolled.rollPlayer.equals("Alice"),"portal receives stable committed die event");
        check(e.snapshot(token).contains("\"rollEvent\":{\"id\":"+GameEngine.quote(rolled.rollId)),"authenticated clients receive same dice event");
        e.command(token,request,before,"roll","");check(e.portalState().rollId.equals(rolled.rollId)&&dice.rolls==1,"retry does not create presentation event");
        e=new GameEngine(store,dice);check(e.sessionId().equals(rolled.sessionId),"party identity survives normal restart and update");check(e.portalState().rollId.equals(rolled.rollId),"die presentation identity survives restart");
        e.command(token,id(),revision(e.snapshot(token)),"chooseMove","");
        e.command(token,id(),revision(e.snapshot(token)),"move","S");
        check(e.portalState().events.get(e.portalState().events.size()-1).contains("Dr WHO: Ich gehe von Feld A1 zu Feld A2"),"movement event records origin and destination");
        check(e.portalState().rollId.equals(rolled.rollId),"movement does not replay dice event");
        for(int i=0;i<4;i++)e.command(token,id(),revision(e.snapshot(token)),"move","S");
        begin(e,token);e.command(token,id(),revision(e.snapshot(token)),"roll","");
        check(e.portalState().die==5&&!e.portalState().rollId.equals(rolled.rollId),"equal consecutive dice values still have different events");
    }
    static void turnStartAndStory() throws Exception {
        MemoryStore store=new MemoryStore();Dice dice=new Dice();dice.face=5;
        Clock clock=Clock.fixed(Instant.parse("2026-03-20T16:34:00Z"),ZoneId.of("Europe/Berlin"));
        GameEngine e=new GameEngine(store,dice,CharacterProfile.known(),clock,new Random(2));
        String a=ready(e,"Alice","doctor"),b=ready(e,"Bob","dalek");e.startDemo();
        check(e.portalState().awaitingGo&&e.portalState().activeCharacter.id.equals("doctor")&&e.portalState().headline.equals("Alice ist dran…"),"portal presents active character until Go");
        String before=e.snapshot(a);
        rejects(IllegalArgumentException.class,()->e.command(a,id(),revision(e.snapshot(a)),"roll",""));
        rejects(IllegalArgumentException.class,()->e.command(a,id(),revision(e.snapshot(a)),"action","N"));
        rejects(IllegalArgumentException.class,()->e.command(b,id(),revision(e.snapshot(b)),"beginTurn",""));
        check(before.equals(e.snapshot(a))&&dice.rolls==0,"no game action or random draw before correct player Go");
        store.fail=true;rejects(IOException.class,()->e.command(a,id(),revision(e.snapshot(a)),"beginTurn",""));
        check(before.equals(e.snapshot(a))&&e.portalState().awaitingGo,"failed Go save keeps portal waiting and story unchanged");store.fail=false;
        String go=id();long revision=revision(e.snapshot(a));String accepted=e.command(a,go,revision,"beginTurn","");
        check(accepted.contains("\"accepted\":true")&&!e.portalState().awaitingGo,"Go is authoritative and removes portal waiting card");
        check(accepted.equals(e.command(a,go,revision,"beginTurn","")),"Go retry does not add duplicate Story or advance turn");
        GameEngine restored=new GameEngine(store,dice,CharacterProfile.known(),clock,new Random(3));
        check(restored.snapshot(a).equals(accepted)&&!restored.portalState().awaitingGo,"Go survives app restart");
        e.command(a,id(),revision(e.snapshot(a)),"roll","");e.command(a,id(),revision(e.snapshot(a)),"chooseMove","");e.command(a,id(),revision(e.snapshot(a)),"move","S");
        String line=e.portalState().events.get(e.portalState().events.size()-1);
        check(line.startsWith("20.03.2026 17:34 Uhr - Dr WHO: Ich gehe von Feld A1 zu Feld A2."),"Story uses host date, time, character speaker and first person");
        check(e.snapshot(a).contains("\"playerName\":\"Alice\"")&&e.snapshot(a).contains("\"direction\":\"S\""),"Story preserves player association and chosen direction");
        for(int i=0;i<4;i++)e.command(a,id(),revision(e.snapshot(a)),"move","S");
        check(e.portalState().awaitingGo&&e.portalState().activePlayerName.equals("Bob")&&e.snapshot(b).contains("\"accepted\":false"),"next partner must confirm own Go");
        rejects(IllegalArgumentException.class,()->e.command(a,id(),revision(e.snapshot(a)),"beginTurn",""));
        // Cosmetic randomness is a separate generator and its chosen text is stored once.
        MemoryStore dalekStore=new MemoryStore();Random noise=new Random(){int draws;@Override public int nextInt(int bound){draws++;return 0;}};
        Dice rules=new Dice();rules.face=5;GameEngine dalek=new GameEngine(dalekStore,rules,CharacterProfile.known(),clock,noise);
        String d=ready(dalek,"Metal","dalek");dalek.startDemo();begin(dalek,d);dalek.command(d,id(),revision(dalek.snapshot(d)),"roll","");
        String action=id();long rev=revision(dalek.snapshot(d));String done=dalek.command(d,action,rev,"action","E");
        check(done.contains("Knarz … grrr.")&&rules.rolls==1&&rules.helperDraws==0,"Dalek flavour does not consume game dice or helper randomness");
        GameEngine again=new GameEngine(dalekStore,rules,CharacterProfile.known(),clock,new Random(90));
        check(again.snapshot(d).equals(done)&&again.command(d,action,rev,"action","E").equals(done),"Dalek sentence and timestamp survive restart/retry without resampling");
        for(int i=0;i<45;i++){begin(again,d);again.command(d,id(),revision(again.snapshot(d)),"roll","");again.command(d,id(),revision(again.snapshot(d)),"action","W");}
        check(again.portalState().events.size()==60,"Story stays bounded with log retention");
    }
    static void directedActions() throws Exception {
        for(String character:Arrays.asList("doctor","dalek","weeping-angel"))for(String direction:Arrays.asList("N","E","S","W")){
            MemoryStore store=new MemoryStore();Dice dice=new Dice();dice.face=6;GameEngine e=new GameEngine(store,dice);
            String token=ready(e,"Actor",character);e.startDemo();begin(e,token);e.command(token,id(),revision(e.snapshot(token)),"roll","");
            String before=e.snapshot(token);
            rejects(IllegalArgumentException.class,()->e.command(token,id(),revision(e.snapshot(token)),"action",""));
            rejects(IllegalArgumentException.class,()->e.command(token,id(),revision(e.snapshot(token)),"action","NE"));
            check(before.equals(e.snapshot(token)),"missing/invalid action direction never consumes turn");
            String request=id();long rev=revision(e.snapshot(token));String result=e.command(token,request,rev,"action",direction);
            check(result.contains("\"direction\":"+GameEngine.quote(direction))&&result.contains("\"round\":2")&&result.contains("\"accepted\":false"),"direction stored and action ends exactly one turn");
            check(e.command(token,request,rev,"action",direction).equals(result),"directional action retry never applies twice");
        }
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
        try(LocalServer server=new LocalServer(e,"<h1>Dr_Who</h1>".getBytes(StandardCharsets.UTF_8),0,name->null,"0.5.1")){
            check(call(server,"/",null,null,false).body.contains("Dr_Who"),"player page served");
            check(call(server,"/api/lobby",null,null,false).body.equals("{\"occupied\":0,\"capacity\":4}"),"public lobby exposes only empty occupancy");
            Response info=call(server,"/api/info",null,null,false);
            check(info.status==200&&info.body.equals("{\"appVersion\":\"0.5.1\",\"protocolVersion\":2,\"sessionId\":"+GameEngine.quote(e.sessionId())+"}"),"public info exposes only version, protocol and non-secret party identity");
            check(call(server,"/api/info","",null,true).status==404,"metadata endpoint is read only");
            check(call(server,"/api/state",null,null,false).status==401,"unauthenticated state blocked");
            check(call(server,"/api/admin",null,null,false).status==404,"no remote host endpoint");
            String form="name=A&code="+e.code()+"&requestId="+id();
            check(call(server,"/api/join",form,null,false).status==401,"cross-site form blocked");
            Response joined=call(server,"/api/join",form,null,true);check(joined.status==200,"HTTP join");
            String token=text(joined.body,"token");
            Response lobby=call(server,"/api/lobby",null,null,false);
            check(lobby.status==200&&lobby.body.equals("{\"occupied\":1,\"capacity\":4}"),"live lobby count includes registered players without exposing identity");
            check(call(server,"/api/state",null,token,false).status==200,"authenticated snapshot");
            check(call(server,"/api/character","characterId=doctor&requestId="+id(),null,true).status==401,"choice requires authentication");
            check(call(server,"/api/character","characterId=doctor&requestId="+id(),token,true).status==200,"HTTP character choice");
            check(call(server,"/api/enemy","characterId=dalek",token,true).status==404,"enemy administration unavailable to player API");
            check(call(server,"/portraits/doctor.jpg",null,null,false).status==404,"missing portrait returns placeholder status");
            e.startDemo();
            check(call(server,"/api/command","action=roll&revision="+revision(e.snapshot(token))+"&requestId="+id(),token,true).status==400,"HTTP roll requires Go first");
            check(call(server,"/api/command","action=beginTurn&revision="+revision(e.snapshot(token))+"&requestId="+id(),token,true).status==200,"HTTP active player starts turn with Go");
            String cmd="action=roll&revision="+revision(e.snapshot(token))+"&requestId="+id();
            Response roll=call(server,"/api/command",cmd,token,true);check(roll.status==200,"HTTP command");
            check(call(server,"/api/command",cmd,token,true).body.equals(roll.body),"HTTP retry idempotent");
            for(int i=0;i<8;i++)call(server,"/api/join","name=B&code=wrong&requestId="+id(),null,true);
            check(call(server,"/api/join",form,null,true).status==429,"bad-code attempts throttled");
        }
    }
    static Response raw(LocalServer server,String host,String path)throws IOException{
        try(Socket socket=new Socket("127.0.0.1",server.port())){
            socket.setSoTimeout(3000);
            socket.getOutputStream().write(("GET "+path+" HTTP/1.1\r\nHost: "+host+"\r\nConnection: close\r\n\r\n").getBytes(StandardCharsets.US_ASCII));
            String response=new String(socket.getInputStream().readAllBytes(),StandardCharsets.UTF_8);
            return new Response(Integer.parseInt(response.split(" ")[1]),response.substring(response.indexOf("\r\n\r\n")+4));
        }
    }
    static void connections()throws Exception{
        check(ConnectionLinks.site("drwho.local",80).equals("http://drwho.local"),"default HTTP port omitted");
        check(ConnectionLinks.site("drwho.local",8080).equals("http://drwho.local:8080"),"fallback port explicit");
        check(ConnectionLinks.join("192.168.43.1",80,"123456").equals("http://192.168.43.1/?code=123456"),"direct QR includes prefill code");
        check(ConnectionLinks.wifiQr("Dr_Who","drwho123").equals("WIFI:T:WPA;S:Dr_Who;P:drwho123;;"),"Wi-Fi QR payload");
        check(ConnectionLinks.wifiQr("a;b:c","p;q:r").contains("a\\;b\\:c;P:p\\;q\\:r"),"QR separators escaped");
        rejects(IllegalArgumentException.class,()->ConnectionLinks.site("example.com",80));
        rejects(IllegalArgumentException.class,()->ConnectionLinks.join("drwho.local",80,"<code>"));
        GameEngine engine=new GameEngine(new MemoryStore());
        try(LocalServer server=new LocalServer(engine,"Player".getBytes(StandardCharsets.UTF_8),0,name->name.equals("doctor.svg")?"<svg/>".getBytes(StandardCharsets.UTF_8):null)){
            check(raw(server,"drwho.local","/").status==200,"advertised hostname accepted by HTTP server");
            check(raw(server,"evil.local","/").status==400,"other DNS names rejected");
            check(raw(server,"drwho.local.evil","/").status==400,"hostname suffix attack rejected");
            check(raw(server,"drwho.local","/?code=123456").body.equals("Player"),"QR URL serves player page");
            check(raw(server,"drwho.local","/?code=invalid").status==404,"unexpected query rejected");
            check(raw(server,"drwho.local","/portraits/doctor.svg").body.equals("<svg/>"),"local SVG portrait served");
            check(raw(server,"drwho.local","/portraits/../session-v1.drw").status==404,"portrait traversal rejected");
            server.setLocalHostname("drwho-2.local");
            check(raw(server,"drwho-2.local","/").status==200,"renamed hostname after conflict accepted");
            check(raw(server,"drwho.local","/").status==400,"old hostname no longer accepted after rename");
            rejects(IllegalArgumentException.class,()->server.setLocalHostname("other.example.com"));
        }
    }
}
