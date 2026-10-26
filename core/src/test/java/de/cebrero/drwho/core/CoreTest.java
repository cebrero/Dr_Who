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
        return engine(store, dice, catalogue);
    }
    static String ready(GameEngine e, String name, String character) throws Exception {
        String token = join(e,name); e.chooseCharacter(token,id(),character); return token;
    }
    static void begin(GameEngine e,String token)throws Exception{e.command(token,id(),revision(e.snapshot(token)),"beginTurn","");}
    static final Map<GameEngine,MemoryStore> testStores=new IdentityHashMap<>();
    static final Map<GameEngine,TestClock> testClocks=new IdentityHashMap<>();
    static final class TestClock extends Clock {
        long time; final ZoneId zone;
        TestClock(Clock source){time=source.millis();zone=source.getZone();}
        public ZoneId getZone(){return zone;}
        public Clock withZone(ZoneId value){return Clock.fixed(instant(),value);}
        public Instant instant(){return Instant.ofEpochMilli(time);}
    }
    static GameEngine engine(MemoryStore store)throws IOException{return engine(store,new Dice());}
    static GameEngine engine(MemoryStore store,SecureRandom dice)throws IOException{return engine(store,dice,CharacterProfile.known());}
    static GameEngine engine(MemoryStore store,SecureRandom dice,List<CharacterProfile> catalogue)throws IOException{return engine(store,dice,catalogue,Clock.systemDefaultZone(),new Random());}
    static GameEngine engine(MemoryStore store,SecureRandom dice,List<CharacterProfile> catalogue,Clock clock,Random flavour)throws IOException{
        TestClock ticking=new TestClock(clock);GameEngine e=new GameEngine(store,dice,catalogue,ticking,flavour);testClocks.put(e,ticking);testStores.put(e,store);return e;
    }
    static Map<String,String> tokens(GameEngine e)throws Exception{
        DataInputStream in=new DataInputStream(new ByteArrayInputStream(testStores.get(e).data));
        in.readInt();in.readInt();in.readUTF();in.readUTF();in.readBoolean();in.readInt();in.readInt();in.readInt();in.readLong();int n=in.readInt();
        Map<String,String> result=new LinkedHashMap<>();for(int i=0;i<n;i++){String id=in.readUTF(),token=in.readUTF();in.readUTF();in.readUTF();in.readInt();in.readInt();in.readUTF();result.put(id,token);}return result;
    }
    static String cmd(GameEngine e,String token,String action,String payload)throws Exception{return e.command(token,id(),revision(e.snapshot(token)),action,payload);}
    static void advance(GameEngine e)throws Exception{testClocks.get(e).time+=3000;e.tick();}
    /** Baseline rule tests use an actual saved, open 6×6 plan through the new setup API. */
    static void start(GameEngine e)throws Exception{
        e.startDemo();advance(e);Map<String,String> tokens=tokens(e);String any=tokens.values().iterator().next();
        String master=tokens.get(text(e.snapshot(any),"candidate"));cmd(e,master,"roomYes","");
        for(int y=0;y<6;y++)for(int x=0;x<6;x++)cmd(e,master,"tilePlace",x+","+y+",floor,0");
        int index=0;for(String player:tokens.keySet())cmd(e,master,"setSpawn",player+","+(index++)+",0");
        cmd(e,master,"boardSave","");advance(e);
        // Setup has its own random draw; historical helper checks count only the gameplay phase.
        java.lang.reflect.Field field=GameEngine.class.getDeclaredField("random");field.setAccessible(true);SecureRandom rng=(SecureRandom)field.get(e);if(rng instanceof Dice)((Dice)rng).helperDraws=0;
    }
    public static void main(String[] args) throws Exception {
        lobbyAndTurns(); diceRules(); persistence(); concurrentRetry(); characterRace(); enemyPhase(); migration(); presentation(); turnStartAndStory(); directedActions(); roomSetupAndBoard(); completeSaveAndQuartered(); actualFormatSeven(); playerRemoval(); sharedAssets(); http(); connections();
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
        start(e);
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
            GameEngine e = engine(store,dice); String a = ready(e,"A","doctor"),b=ready(e,"B","dalek");start(e);begin(e,a);
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
                    GameEngine restored=engine(store,dice);check(restored.snapshot(a).equals(moved),"mid-movement restart exact");
                    rejects(IllegalArgumentException.class,()->e.command(a,id(),revision(e.snapshot(a)),"action",""));
                }
            }
            check(e.snapshot(a).contains("\"current\":"+GameEngine.quote(text(e.snapshot(b),"you"))),"turn ends after exact die count");
            check(e.snapshot(a).contains("\"stage\":\"roll\""),"next player starts with a new die roll");
        }
        MemoryStore store=new MemoryStore();Dice dice=new Dice();dice.face=5;GameEngine e=engine(store,dice);
        String a=ready(e,"A","doctor");start(e);begin(e,a);e.command(a,id(),revision(e.snapshot(a)),"roll","");
        GameEngine restored=engine(store,dice);check(restored.snapshot(a).contains("\"stage\":\"choice\""),"5/6 choice survives reload without reroll");
        restored.command(a,id(),revision(restored.snapshot(a)),"action","N");
        check(restored.snapshot(a).contains("\"round\":2"),"action replaces movement and ends turn");
        check(restored.snapshot(a).contains("Wirkung noch nicht berechnet"),"unfinished character effects explicitly marked");
    }
    static void persistence() throws Exception {
        MemoryStore store = new MemoryStore(); Dice dice = new Dice(); GameEngine e = engine(store, dice);
        String a = ready(e, "A", "doctor"); start(e);begin(e,a); String request = id(); long rev = revision(e.snapshot(a));
        e.command(a,request,rev,"roll","");
        String snapshot = e.snapshot(a), code = e.code();
        GameEngine restored = engine(store, dice);
        check(snapshot.equals(restored.snapshot(a)) && code.equals(restored.code()), "full restart preserves game and identity");
        restored.command(a,request,rev,"roll",""); check(dice.rolls == 1, "retry survives restart");
        store.fail = true;
        rejects(IOException.class, () -> restored.command(a,id(),revision(restored.snapshot(a)),"move","S"));
        check(snapshot.equals(restored.snapshot(a)), "failed save leaves authoritative state unchanged");
        store.fail = false; store.data = new byte[]{1,2,3};
        rejects(IOException.class, () -> engine(store));
        check(Arrays.equals(store.data,new byte[]{1,2,3}), "corrupt save never silently reset");
        check(GameEngine.quote("\"\\\n").equals("\"\\\"\\\\\\u000a\""), "JSON escaping");
    }
    static void concurrentRetry() throws Exception {
        MemoryStore store = new MemoryStore(); Dice dice = new Dice(); GameEngine e = engine(store,dice);
        String a = ready(e,"A","doctor"); start(e);begin(e,a); String req = id(); long rev = revision(e.snapshot(a));
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
        MemoryStore store = new MemoryStore(); GameEngine e = engine(store);
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
        check(engine(store).snapshot(a).equals(before),"character assignments and enemy list survive restart");
        start(e);rejects(IllegalArgumentException.class,()->e.chooseCharacter(winner,id(),"doctor"));
    }
    static void enemyPhase() throws Exception {
        MemoryStore store=new MemoryStore();Dice dice=new Dice();GameEngine e=engine(store,dice);
        dice.face=5;
        String a=ready(e,"Alice","doctor"),b=ready(e,"Bob","dalek");e.addEnemy("weeping-angel");start(e);
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
        GameEngine restored=engine(store,dice);check(attacked.equals(restored.snapshot(a)),"pending attack and phase survive restart");
        restored.command(a,id(),revision(restored.snapshot(a)),"ackAttack","");restored.finishEnemyTurn();
        check(restored.snapshot(a).contains("\"round\":2")&&restored.snapshot(a).contains("\"phase\":\"players\""),"next round begins after acknowledged enemy turn");
    }
    static void migration() throws Exception {
        for(int format:new int[]{1,2,3,4,5,6}){
            MemoryStore store=new MemoryStore();ByteArrayOutputStream bytes=new ByteArrayOutputStream();DataOutputStream out=new DataOutputStream(bytes);
            out.writeInt(0x4457484f);out.writeInt(format);out.writeUTF("old-game");out.writeUTF("123456");out.writeBoolean(true);
            out.writeInt(7);out.writeInt(0);out.writeInt(4);out.writeLong(10);out.writeInt(1);
            out.writeUTF("old-player");out.writeUTF("old-token");out.writeUTF("Alice");out.writeUTF(id());out.writeInt(2);out.writeInt(3);
            if(format>=2){out.writeUTF("doctor");out.writeInt(1);out.writeUTF("dalek");}
            out.writeInt(1);out.writeUTF("Ein alter Eintrag ohne Zeitstempel.");out.writeInt(0);
            if(format>=3){out.writeUTF("players");out.writeInt(0);out.writeUTF("");out.writeBoolean(false);out.writeUTF("");out.writeUTF("");out.writeUTF("");out.writeUTF("");out.writeBoolean(false);}
            if(format>=4){out.writeUTF(format==5?"move":"roll");out.writeInt(format==5?4:0);out.writeInt(format==5?2:0);}
            if(format>=5){out.writeUTF(id());out.writeUTF("old-player");}
            if(format>=6){out.writeBoolean(false);out.writeInt(0);}
            store.data=bytes.toByteArray();GameEngine e=engine(store);
            if(format==1){check(!e.readyForPlay(),"v1 requires character before resuming");e.chooseCharacter("old-token",id(),"doctor");}
            check(e.playing(),"old game resumes after setup");check(e.snapshot("old-token").contains("\"round\":7"),"migration keeps round");
            check(e.snapshot("old-token").contains("\"x\":2,\"y\":3"),"migration keeps position");
            if(format<6)check(e.snapshot("old-token").contains("\"speaker\":\"Archiv\"")&&e.snapshot("old-token").contains("\"timestampMillis\":0"),"old logs remain honest undated archive");
            if(format==5)check(e.snapshot("old-token").contains("\"accepted\":true")&&e.snapshot("old-token").contains("\"remaining\":2"),"mid-move v5 save resumes without another Go or reroll");
        }
    }
    static void presentation() throws Exception {
        MemoryStore store=new MemoryStore();Dice dice=new Dice();dice.face=5;GameEngine e=engine(store,dice);
        String token=ready(e,"Alice","doctor");start(e);
        check(e.portalState().headline.contains("Alice")&&e.portalState().detail.contains("Go")&&e.portalState().awaitingGo,"portal waits for active player Go");
        begin(e,token);
        String request=id();long before=revision(e.snapshot(token));e.command(token,request,before,"roll","");
        check(!e.sessionId().equals(engine(new MemoryStore()).sessionId()),"fresh installation has different party identity even with same app version");
        GameEngine.PortalState rolled=e.portalState();
        check(!rolled.rollId.isEmpty()&&rolled.die==5&&rolled.rollPlayer.equals("Alice"),"portal receives stable committed die event");
        check(e.snapshot(token).contains("\"rollEvent\":{\"id\":"+GameEngine.quote(rolled.rollId)),"authenticated clients receive same dice event");
        e.command(token,request,before,"roll","");check(e.portalState().rollId.equals(rolled.rollId)&&dice.rolls==1,"retry does not create presentation event");
        e=engine(store,dice);check(e.sessionId().equals(rolled.sessionId),"party identity survives normal restart and update");check(e.portalState().rollId.equals(rolled.rollId),"die presentation identity survives restart");
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
        GameEngine e=engine(store,dice,CharacterProfile.known(),clock,new Random(2));
        String a=ready(e,"Alice","doctor"),b=ready(e,"Bob","dalek");start(e);
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
        GameEngine restored=engine(store,dice,CharacterProfile.known(),clock,new Random(3));
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
        Dice rules=new Dice();rules.face=5;GameEngine dalek=engine(dalekStore,rules,CharacterProfile.known(),clock,noise);
        String d=ready(dalek,"Metal","dalek");start(dalek);begin(dalek,d);dalek.command(d,id(),revision(dalek.snapshot(d)),"roll","");
        String action=id();long rev=revision(dalek.snapshot(d));String done=dalek.command(d,action,rev,"action","E");
        check(done.contains("Knarz … grrr.")&&rules.rolls==1&&rules.helperDraws==0,"Dalek flavour does not consume game dice or helper randomness");
        GameEngine again=engine(dalekStore,rules,CharacterProfile.known(),clock,new Random(90));
        check(again.snapshot(d).equals(done)&&again.command(d,action,rev,"action","E").equals(done),"Dalek sentence and timestamp survive restart/retry without resampling");
        for(int i=0;i<45;i++){begin(again,d);again.command(d,id(),revision(again.snapshot(d)),"roll","");again.command(d,id(),revision(again.snapshot(d)),"action","W");}
        check(again.portalState().events.size()>100,"complete Story is retained beyond sixty entries");
    }
    static void directedActions() throws Exception {
        for(String character:Arrays.asList("doctor","dalek","weeping-angel"))for(String direction:Arrays.asList("N","E","S","W")){
            MemoryStore store=new MemoryStore();Dice dice=new Dice();dice.face=6;GameEngine e=engine(store,dice);
            String token=ready(e,"Actor",character);start(e);begin(e,token);e.command(token,id(),revision(e.snapshot(token)),"roll","");
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
    static void roomSetupAndBoard() throws Exception {
        MemoryStore store=new MemoryStore();Dice dice=new Dice();GameEngine e=engine(store,dice);
        String a=ready(e,"Alice","doctor"),b=ready(e,"Bob","dalek"),c=ready(e,"Cora","weeping-angel");
        e.startDemo();String selecting=e.snapshot(a);check(selecting.contains("\"stage\":\"selecting\"")&&!e.playing(),"Start enters selection rather than first turn");
        check(e.portalState().preparing&&e.portalState().setupTitle.equals("der Raummeister wird gerade ausgewählt..."),"host gets exact large selection title");
        rejects(IllegalArgumentException.class,()->join(e,"Late"));rejects(IllegalArgumentException.class,()->e.chooseCharacter(a,id(),"doctor"));rejects(IllegalArgumentException.class,()->e.addEnemy("doctor"));
        rejects(IllegalArgumentException.class,()->cmd(e,a,"beginTurn",""));rejects(IllegalArgumentException.class,()->cmd(e,a,"roomYes",""));
        testClocks.get(e).time+=2999;e.tick();check(selecting.equals(e.snapshot(a)),"selection animation lasts at least three seconds without rerolling");testClocks.get(e).time++;e.tick();
        Map<String,String> byId=tokens(e);String first=text(e.snapshot(a),"candidate"),firstToken=byId.get(first),other=firstToken.equals(a)?b:a;
        check(byId.containsKey(first),"candidate is an authenticated real player");String offered=e.snapshot(a);GameEngine restored=engine(store,dice);restored.tick();check(offered.equals(restored.snapshot(a)),"pending offer stable across restart and poll");
        rejects(IllegalArgumentException.class,()->cmd(e,other,"roomNo",""));
        String noRequest=id();long noRevision=revision(e.snapshot(a));store.fail=true;
        rejects(IOException.class,()->e.command(firstToken,noRequest,noRevision,"roomNo",""));check(offered.equals(e.snapshot(a)),"failed rejection save preserves candidate");store.fail=false;
        e.command(firstToken,noRequest,noRevision,"roomNo","");String second=text(e.snapshot(a),"candidate");check(!first.equals(second),"declining asks another player without replacement");
        String afterNo=e.snapshot(a);e.command(firstToken,noRequest,noRevision,"roomNo","");check(afterNo.equals(e.snapshot(a)),"decline retry never asks a third candidate");
        cmd(e,byId.get(second),"roomNo","");String third=text(e.snapshot(a),"candidate");check(!third.equals(first)&&!third.equals(second),"third offer is remaining player");cmd(e,byId.get(third),"roomNo","");
        String forced=e.snapshot(a);check(forced.contains("\"stage\":\"building\"")&&forced.contains("\"forced\":true"),"all declines cause a persisted portal decision");
        String masterId=text(forced,"roomMaster"),master=byId.get(masterId),outsider=master.equals(a)?b:a;
        check(e.portalState().notices.get(e.portalState().notices.size()-1).text.contains("Alle haben abgelehnt"),"large portal decision has durable notice");
        check(engine(store,dice).snapshot(a).equals(forced),"forced roommaster persists across restart");
        rejects(IllegalArgumentException.class,()->cmd(e,outsider,"tilePlace","0,0,floor,0"));rejects(IllegalArgumentException.class,()->cmd(e,outsider,"boardSave",""));
        rejects(IllegalArgumentException.class,()->cmd(e,master,"boardSave",""));
        for(String payload:Arrays.asList("20,0,floor,0","0,20,floor,0","-1,0,floor,0","0,0,unknown,0","0,0,floor,4","0,0,floor,0,extra","19,0,portal,0"))rejects(IllegalArgumentException.class,()->cmd(e,master,"tilePlace",payload));
        cmd(e,master,"tilePlace","0,0,corner,0");cmd(e,master,"tilePlace","1,0,floor,0");cmd(e,master,"tilePlace","2,0,parallel,1");
        rejects(IllegalArgumentException.class,()->cmd(e,master,"tilePlace","0,0,floor,0"));
        cmd(e,master,"tilePlace","5,5,portal,0");String plan=e.snapshot(a);check(plan.contains("\"kind\":\"portal\",\"rotation\":0"),"portal footprint persisted");
        rejects(IllegalArgumentException.class,()->cmd(e,master,"tilePlace","6,5,well,0"));
        String rotateReq=id();long rotateRev=revision(e.snapshot(master));e.command(master,rotateReq,rotateRev,"tileRotate","6,5");String turned=e.snapshot(a);e.command(master,rotateReq,rotateRev,"tileRotate","6,5");check(turned.equals(e.snapshot(a))&&turned.contains("\"kind\":\"portal\",\"rotation\":1"),"second portal half rotates whole tile once, including retry");
        cmd(e,master,"tilePlace","6,5,well,0");rejects(IllegalArgumentException.class,()->cmd(e,master,"tileRotate","5,5"));check(e.snapshot(a).contains("\"kind\":\"portal\",\"rotation\":1"),"blocked rotation leaves old portal intact");
        cmd(e,master,"tileRemove","5,6");check(!e.snapshot(a).contains("\"kind\":\"portal\""),"deleting second half deletes complete portal");
        cmd(e,master,"tilePlace","18,19,portal,0");rejects(IllegalArgumentException.class,()->cmd(e,master,"tileRotate","18,19"));
        cmd(e,master,"tileMove","19,19,4,4");check(e.snapshot(a).contains("\"x\":4,\"y\":4,\"kind\":\"portal\""),"moving second half normalizes to tile anchor");
        cmd(e,master,"tilePlace","7,5,dungeon,0");cmd(e,master,"tilePlace","8,5,door,3");
        rejects(IllegalArgumentException.class,()->cmd(e,master,"setSpawn",masterId+",19,19"));
        cmd(e,master,"setSpawn",masterId+",8,8");String otherId=text(e.snapshot(outsider),"you");rejects(IllegalArgumentException.class,()->cmd(e,master,"setSpawn",otherId+",8,8"));
        cmd(e,master,"tileRemove","0,0");check(!e.snapshot(a).contains("\"x\":0,\"y\":0,\"kind\""),"removed tile becomes a nonexistent field");
        String beforeSave=e.snapshot(a);store.fail=true;rejects(IOException.class,()->cmd(e,master,"boardSave",""));check(beforeSave.equals(e.snapshot(a)),"failed final save cannot start game");store.fail=false;
        String saveReq=id();long saveRev=revision(beforeSave);e.command(master,saveReq,saveRev,"boardSave","");check(!e.playing()&&e.snapshot(a).contains("\"stage\":\"launching\""),"successful save starts three-second launch animation");
        rejects(IllegalArgumentException.class,()->cmd(e,master,"tileRemove","4,4"));String launch=e.snapshot(a);e.command(master,saveReq,saveRev,"boardSave","");check(launch.equals(e.snapshot(a)),"save retry doesn't replay launch or revise board");
        GameEngine launchRestored=engine(store,dice,CharacterProfile.known(),testClocks.get(e),new Random());check(launchRestored.snapshot(a).equals(launch),"launch and full map survive restart");advance(launchRestored);check(launchRestored.playing()&&launchRestored.portalState().awaitingGo,"first turn begins only after launch, with Go");
        rejects(IllegalArgumentException.class,()->cmd(launchRestored,master,"tileRotate","4,4"));
        // Independent acceptance branch and minimal board use the real workflow too.
        MemoryStore acceptedStore=new MemoryStore();GameEngine yes=engine(acceptedStore,new Dice());String y=ready(yes,"Builder","doctor");yes.startDemo();advance(yes);
        String yesReq=id();long yesRev=revision(yes.snapshot(y));yes.command(y,yesReq,yesRev,"roomYes","");String accepted=yes.snapshot(y);yes.command(y,yesReq,yesRev,"roomYes","");check(accepted.equals(yes.snapshot(y))&&!accepted.contains("\"forced\":true"),"volunteer accepted exactly once");
        for(String kind:Arrays.asList("corner","floor","parallel","door","dungeon","well","portal")){
            BoardPlan board=new BoardPlan();board.place(10,10,kind,0);for(int i=0;i<4;i++){check(board.at(10,10).rotation==i,"all tile types rotate through four orientations");board.rotate(10,10);}check(board.at(10,10).rotation==0,"four taps restore original orientation");
        }
        BoardPlan walls=new BoardPlan(false);walls.place(1,1,"corner",0);walls.place(2,1,"floor",0);walls.place(1,2,"door",0);walls.place(0,1,"parallel",0);
        check(!walls.canStep(1,1,2,1)&&!walls.canStep(2,1,1,1),"corner wall blocks both directions");check(walls.canStep(1,1,1,2),"door is provisional passage");check(walls.canStep(1,1,0,1),"unwalled side is open");check(!walls.canStep(1,1,1,0)&&!walls.canStep(1,1,2,2),"missing fields and diagonals never exist");
        BoardPlan portal=new BoardPlan(false);portal.place(0,0,"portal",0);check(portal.cells()==2&&portal.canStep(0,0,1,0),"portal has exactly two connected cells");portal.rotate(1,0);check(!portal.exists(1,0)&&portal.exists(0,1),"portal rotation updates occupied cells");
        // Production move rejection on sparse plans, with no loss of rolled distance.
        cmd(yes,y,"tilePlace","10,10,floor,0");cmd(yes,y,"tilePlace","11,10,corner,0");cmd(yes,y,"tilePlace","11,9,floor,0");cmd(yes,y,"setSpawn",text(yes.snapshot(y),"you")+",21,20");cmd(yes,y,"boardSave","");advance(yes);begin(yes,y);cmd(yes,y,"roll","");
        String beforeMove=yes.snapshot(y);rejects(IllegalArgumentException.class,()->cmd(yes,y,"move","N"));check(beforeMove.equals(yes.snapshot(y)),"missing field rejected without consuming movement");cmd(yes,y,"move","E");String inside=yes.snapshot(y);rejects(IllegalArgumentException.class,()->cmd(yes,y,"move","N"));check(inside.equals(yes.snapshot(y)),"actual wall between existing neighbors does not consume distance");
        String boardJson=portal.json();check(boardJson.contains("\"size\":20"),"serialized board includes explicit size");
        int round=yes.portalState().round;String roll=yes.portalState().rollId;yes.rebuildBoard();check(!yes.playing()&&yes.portalState().tiles.size()==3,"host-confirmed rebuild retains plan and pauses game");
        advance(yes);cmd(yes,y,"roomYes","");cmd(yes,y,"boardSave","");advance(yes);check(yes.playing()&&yes.portalState().round==round&&yes.portalState().rollId.equals(roll),"rebuild preserves current round and rolled result");
        check(yes.snapshot(y).contains("\"remaining\":3")&&yes.snapshot(y).contains("\"accepted\":true"),"rebuild resumes the saved in-progress turn");
    }

    static void completeSaveAndQuartered()throws Exception{
        BoardPlan q=new BoardPlan();q.place(0,0,"floor",0);check(q.cells()==4,"one new tile has four steps");
        for(int y=0;y<2;y++)for(int x=0;x<2;x++)check(q.exists(x,y),"all four subfields exist");check(!q.exists(2,0),"empty tile has no subfields");
        check(q.canStep(0,0,1,0)&&q.canStep(1,0,1,1)&&!q.canStep(0,0,1,1),"subfield movement is orthogonal and one step");
        check(q.fieldName(1,1).equals("B2"),"every movement field has a simple global coordinate");check(q.fieldName(0,0).equals("A1")&&q.fieldName(1,0).equals("B1")&&q.fieldName(0,1).equals("A2"),"top left tile uses A1 B1 A2 B2");check(q.fieldName(25,39).equals("Z40")&&q.fieldName(26,39).equals("AA40")&&q.fieldName(39,39).equals("AN40"),"global columns continue beyond Z");check(q.tileName(0,0).equals("A1–B2"),"tile is described by movement-coordinate range");q.place(1,0,"single",3);check(!q.canStep(1,0,2,0)&&!q.canStep(2,0,1,0),"outer red wall blocks both ways");check(q.canStep(2,0,3,0),"outer wall does not divide a tile internally");
        BoardPlan dungeon=new BoardPlan();dungeon.place(0,0,"dungeon",0);dungeon.place(1,0,"floor",0);check(!dungeon.canStep(1,0,2,0),"closed dungeon blocks boundary");dungeon.toggleDungeon(0,0);check(dungeon.canStep(1,0,2,0),"open dungeon allows boundary");dungeon.rotate(0,0);check(!dungeon.at(0,0).closed,"rotation preserves dungeon condition");
        for(String kind:Arrays.asList("floor","corner","single","parallel","dungeon","cemetery","compass","lava","lava-stone","well")){BoardPlan b=new BoardPlan();b.place(19,19,kind,3);check(b.cells()==4&&b.exists(39,39),"new tile available at outermost subfield: "+kind);}
        MemoryStore store=new MemoryStore();Dice dice=new Dice();dice.face=6;GameEngine e=engine(store,dice);String a=ready(e,"Alice","doctor"),b=ready(e,"Bob","dalek");e.addEnemy("weeping-angel");e.startDemo();advance(e);Map<String,String> ts=tokens(e);String master=ts.get(text(e.snapshot(a),"candidate"));cmd(e,master,"roomYes","");
        cmd(e,master,"tilePlace","4,4,floor,0");cmd(e,master,"tilePlace","5,4,lava-stone,2");cmd(e,master,"tilePlace","4,5,dungeon,1");cmd(e,master,"setSpawn","enemy:weeping-angel,10,8");
        cmd(e,master,"setSpawn",text(e.snapshot(a),"you")+",8,8");cmd(e,master,"setSpawn",text(e.snapshot(b),"you")+",9,8");cmd(e,master,"boardSave","");advance(e);begin(e,a);cmd(e,a,"roll","");cmd(e,a,"action","E");
        String snapshot=e.snapshot(b);byte[] saved=e.exportGame();check(snapshot.contains("\"x\":10,\"y\":8")&&snapshot.contains("\"closed\":true"),"save contains enemy position and dungeon state");String id=e.sessionId();int entries=e.portalState().events.size();
        store.fail=true;rejects(IOException.class,()->e.resetGame());check(e.sessionId().equals(id)&&e.snapshot(b).equals(snapshot),"failed reset changes nothing");store.fail=false;
        e.resetGame();check(e.lobbyStatus().contains("\"occupied\":0")&&!e.sessionId().equals(id)&&e.portalState().tiles.isEmpty(),"reset clears only running game and invalidates old clients");String resetId=e.sessionId();
        rejects(IOException.class,()->e.loadGame(new byte[]{1,2,3}));check(e.sessionId().equals(resetId),"invalid import cannot damage current game");store.fail=true;rejects(IOException.class,()->e.loadGame(saved));check(e.sessionId().equals(resetId),"failed import write preserves current game");store.fail=false;
        e.loadGame(saved);check(!e.sessionId().equals(id)&&!e.sessionId().equals(resetId),"loading gets a fresh client epoch");rejects(SecurityException.class,()->e.snapshot(a));String a2=text(e.joinLocal("Alice",id()),"token"),request=id(),b2=text(e.joinLocal("Bob",request),"token");
        check(text(e.joinLocal("Bob",request),"token").equals(b2),"rejoin after load idempotent");rejects(IllegalArgumentException.class,()->e.joinLocal("Bob",id()));
        String loaded=e.snapshot(b2);check(loaded.contains("\"remaining\":0")&&loaded.contains("\"current\":"+GameEngine.quote(text(loaded,"you")))&&e.portalState().events.size()==entries+2,"load preserves turn and Story, adding only rejoin records");
        check(loaded.contains("\"x\":10,\"y\":8")&&loaded.contains("\"kind\":\"lava-stone\",\"rotation\":2")&&loaded.contains("\"closed\":true"),"full plan, all figure positions and tile states restored");
        e.toggleDungeon(4,5);check(e.snapshot(a2).contains("\"kind\":\"dungeon\",\"rotation\":1,\"closed\":false"),"host can change dungeon during game");
        GameEngine restored=engine(store,dice);check(restored.snapshot(b2).equals(e.snapshot(b2)),"full loaded game survives app restart");
        MemoryStore anonStore=new MemoryStore();GameEngine anon=engine(anonStore,new Dice());String credentials=anon.joinLocal("Nora",id());check(!credentials.contains(anon.code())&&anon.lobbyStatus().contains("\"occupied\":1"),"name-only join needs no secret room code");rejects(IllegalArgumentException.class,()->anon.joinLocal("nora",id()));
    }

    static void playerRemoval()throws Exception{
        MemoryStore store=new MemoryStore();GameEngine e=engine(store);String a=ready(e,"Alice","doctor"),b=ready(e,"Bob","dalek"),aid=text(e.snapshot(a),"you");
        String before=e.snapshot(b);store.fail=true;rejects(IOException.class,()->e.removePlayer(aid));check(before.equals(e.snapshot(b))&&e.snapshot(a).contains("Alice"),"failed removal keeps roster and credentials");store.fail=false;
        e.removePlayer(aid);rejects(SecurityException.class,()->e.snapshot(a));check(e.playersForHost().size()==1&&e.snapshot(b).contains("Bob")&&!e.snapshot(b).contains("\"name\":\"Alice\""),"only selected registration removed");
        e.chooseCharacter(b,id(),"doctor");check(e.snapshot(b).contains("\"characterId\":\"doctor\""),"removed player's character is immediately free");String once=e.snapshot(b);e.removePlayer(aid);check(once.equals(e.snapshot(b)),"repeated removal harmless");check(engine(store).snapshot(b).equals(once),"removed seat stays revoked after restart");
        try(LocalServer server=new LocalServer(e,new byte[0],0)){
            check(call(server,"/api/state",null,a,false).status==401&&call(server,"/api/state",null,b,false).status==200,"HTTP revokes only removed player's token");
            check(call(server,"/api/remove","playerId="+text(e.snapshot(b),"you"),b,true).status==404,"no player-accessible removal endpoint");
        }
        String newA=text(e.joinLocal("Alice",id()),"token");check(!newA.equals(a)&&e.playersForHost().size()==2,"removed player can explicitly rejoin free lobby");

        MemoryStore turns=new MemoryStore();Dice dice=new Dice();dice.face=6;GameEngine game=engine(turns,dice);String x=ready(game,"A","doctor"),y=ready(game,"B","dalek"),z=ready(game,"C","weeping-angel");start(game);begin(game,x);cmd(game,x,"roll","");cmd(game,x,"action","E");begin(game,y);cmd(game,y,"roll","");cmd(game,y,"chooseMove","");
        String bid=text(game.snapshot(y),"you"),cid=text(game.snapshot(z),"you");game.removePlayer(text(game.snapshot(x),"you"));String moving=game.snapshot(y);
        check(text(moving,"current").equals(bid)&&moving.contains("\"remaining\":6")&&moving.contains("\"accepted\":true"),"removing earlier waiting player preserves active player's roll and Go");
        game.removePlayer(bid);rejects(SecurityException.class,()->game.snapshot(y));String next=game.snapshot(z);check(text(next,"current").equals(cid)&&next.contains("\"remaining\":0")&&next.contains("\"accepted\":false"),"removing active player advances to next Go without leftover moves");check(engine(turns).snapshot(z).equals(next),"turn after removal remains loadable");

        MemoryStore enemies=new MemoryStore();GameEngine fight=fourCharacterFixture(enemies,dice);String p=ready(fight,"P","doctor"),q=ready(fight,"Q","dalek"),r=ready(fight,"R","test-only");fight.addEnemy("weeping-angel");start(fight);
        for(String t:new String[]{p,q}){begin(fight,t);cmd(fight,t,"roll","");cmd(fight,t,"action","E");}fight.removePlayer(text(fight.snapshot(r),"you"));check(fight.snapshot(p).contains("\"phase\":\"enemies\""),"removing last active human enters scheduled enemy phase");
        fight.requestEnemyMovement();fight.removePlayer(text(fight.snapshot(q),"you"));check(fight.snapshot(p).contains("\"helperId\":\"\""),"removed enemy helper cannot block phase");check(fight.requestEnemyMovement().equals("P"),"remaining player can receive enemy movement");cmd(fight,p,"confirmMovement","");fight.reportAttack(text(fight.snapshot(p),"you"),"Testangriff");fight.removePlayer(text(fight.snapshot(p),"you"));check(!fight.playing()&&!fight.preparing()&&fight.playersForHost().isEmpty(),"last player removal returns to empty lobby");check(engine(enemies).lobbyStatus().contains("\"occupied\":0"),"cleared enemy attack and empty lobby survive restart");

        MemoryStore setup=new MemoryStore();GameEngine board=engine(setup,dice);String i=ready(board,"I","doctor"),j=ready(board,"J","dalek"),k=ready(board,"K","weeping-angel");board.startDemo();advance(board);board.removePlayer(text(board.snapshot(k),"you"));check(text(board.snapshot(i),"candidate").equals(text(board.snapshot(j),"you")),"removed roommaster candidate is replaced");cmd(board,j,"roomYes","");cmd(board,j,"tilePlace","0,0,floor,0");board.removePlayer(text(board.snapshot(j),"you"));check(board.portalState().tiles.size()==1&&text(board.snapshot(i),"candidate").equals(text(board.snapshot(i),"you")),"lost roommaster preserves draft and offers remaining player");cmd(board,i,"roomYes","");cmd(board,i,"boardSave","");board.removePlayer(text(board.snapshot(i),"you"));check(!board.preparing()&&board.portalState().tiles.size()==1,"last player removed during launch preserves physical draft");check(engine(setup).playersForHost().isEmpty(),"empty draft remains loadable");
    }

    static void actualFormatSeven()throws Exception{
        MemoryStore store=new MemoryStore();store.data=Base64.getDecoder().decode("RFdITwAAAAcAJDlhNjkwZjQ1LTI3MTAtNDgyNS05ZmEzLTAyZjA4ZWE5M2RlZAAGNDcyODk3AQAAAAEAAAAAAAAAAAAAAAAAAAAIAAAAAQAkMjZiM2M2MzktZTFiYS00ZGRiLWI5ZmYtMmIzYjNjYTU2ZjAyAEhmY2M1OTc3MS05NDZjLTQ1MzMtYjY0Mi1lZTY4Y2U0ZDEzMmQ0ZDhiYjMxMS02NjQ4LTRmZmYtOTAzYS0zYzU4M2FjMmY3MDgABkxlZ2FjeQAkNmVmZWE5MzgtYzhiYi00YzY2LThjMWEtNTk2NGY4M2ZmNmFlAAAABAAAAAQABmRvY3RvcgAAAAAAAAAHADhUZWNobmlrZGVtbyBiZXJlaXQuIE5vY2gga2VpbmUgZW5kZ8O8bHRpZ2VuIFNwaWVscmVnZWxuLgAXTGVnYWN5IGlzdCBiZWlnZXRyZXRlbi4AJExlZ2FjeSB3w6RobHQgRGVyIERvY3RvciDCtyBQYXJ0bmVyLgBQRGFzIFBvcnRhbCBzdWNodCBkZW4gUmF1bW1laXN0ZXIuIERhcyBTcGllbCB3YXJ0ZXQgYXVmIGRlbiBnZXNwZWljaGVydGVuIEF1ZmJhdS4AKUxlZ2FjeSBzdGVsbHQgZGFzIFJhdW1nZWbDvGdlIHdpZWRlciBoZXIuAGFMZWdhY3kgaGF0IGRhcyBSYXVtZ2Vmw7xnZSBnZXNwZWljaGVydDogMSBLYWNoZWxuLCAxIEZlbGRlci4gU3RhcnRmaWd1cmVuIHdpZSBpbSBQbGFuIGF1ZnN0ZWxsZW4uADREYXMgQWJlbnRldWVyIGJlZ2lubnQuIEJlc3TDpHRpZ2UgZGVpbmVuIFp1ZyBtaXQgR28uAAAABABQMjZiM2M2MzktZTFiYS00ZGRiLWI5ZmYtMmIzYjNjYTU2ZjAyOmNob29zZTplODBjZDA0YS0wZWE4LTQ2Y2UtOGI0Yi1mYTJlYzA2YWQwMzMASTI2YjNjNjM5LWUxYmEtNGRkYi1iOWZmLTJiM2IzY2E1NmYwMjo0NjdjMDUyYS0xYjc1LTQ0ODgtYmJmMi00YmE2NTNjMmViNTMASTI2YjNjNjM5LWUxYmEtNGRkYi1iOWZmLTJiM2IzY2E1NmYwMjo4NWEyN2NkYi03MmY0LTRjMjMtYjBhNi1jMWQ0ZDZhMmYzOTQASTI2YjNjNjM5LWUxYmEtNGRkYi1iOWZmLTJiM2IzY2E1NmYwMjowMDQyNWRlYy02NTMyLTQ3MTktOTI3ZS01ODgzZjM4NzI2MDMAB3BsYXllcnMAAAAAAAAAAAAAAAAAAAAAAARyb2xsAAAAAAAAAAAAAAAAAAAAAAcAJDkzZWU4ZTEzLTBkZmEtNDQzYi05NDlhLTQ5NTBmM2VjNzhlYwAAAaMYXFAAABQxNS4wMS4yMDI3IDA4OjAwIFVocgAGUG9ydGFsAAAAOFRlY2huaWtkZW1vIGJlcmVpdC4gTm9jaCBrZWluZSBlbmRnw7xsdGlnZW4gU3BpZWxyZWdlbG4uAAAAJGYwMWQ5MjFlLTljNWYtNDJkOS1iMWZkLTU0NmZlYzExODBhZgAAAaMYXFAAABQxNS4wMS4yMDI3IDA4OjAwIFVocgAGUG9ydGFsAAAAF0xlZ2FjeSBpc3QgYmVpZ2V0cmV0ZW4uAAAAJDY5NWViODFmLWY5ZDYtNDBjMC1iNTI1LTVjNDE1ZTI5ZDkwOQAAAaMYXFAAABQxNS4wMS4yMDI3IDA4OjAwIFVocgAGRHIgV0hPAAZMZWdhY3kAJUljaCBiZWdsZWl0ZSBMZWdhY3kgYXVmIGRpZXNlciBSZWlzZS4AAAAkMzE3N2Q1YWEtYjRhYy00OGNiLThjYmItNTMyZjU0YTUzNTc5AAABoxhcUAAAFDE1LjAxLjIwMjcgMDg6MDAgVWhyAAZQb3J0YWwAAABQRGFzIFBvcnRhbCBzdWNodCBkZW4gUmF1bW1laXN0ZXIuIERhcyBTcGllbCB3YXJ0ZXQgYXVmIGRlbiBnZXNwZWljaGVydGVuIEF1ZmJhdS4AAAAkYTI4MTI0NGUtMDUzOS00N2FlLWI3MzAtYzYwZjJjN2ViMWU2AAABoxhcW7gAFDE1LjAxLjIwMjcgMDg6MDAgVWhyAAZQb3J0YWwAAAApTGVnYWN5IHN0ZWxsdCBkYXMgUmF1bWdlZsO8Z2Ugd2llZGVyIGhlci4AAAAkOGE4NGMwYTctMjY0Yy00MTEzLTg4MTUtN2E0ZDY3MjlhNDE0AAABoxhcW7gAFDE1LjAxLjIwMjcgMDg6MDAgVWhyAAZQb3J0YWwAAABhTGVnYWN5IGhhdCBkYXMgUmF1bWdlZsO8Z2UgZ2VzcGVpY2hlcnQ6IDEgS2FjaGVsbiwgMSBGZWxkZXIuIFN0YXJ0ZmlndXJlbiB3aWUgaW0gUGxhbiBhdWZzdGVsbGVuLgAAACQxOTVlYjIzMi04YzAzLTQyY2EtODMyOC03YzE3MjZmMTUwNTQAAAGjGFxncAAUMTUuMDEuMjAyNyAwODowMCBVaHIABlBvcnRhbAAAADREYXMgQWJlbnRldWVyIGJlZ2lubnQuIEJlc3TDpHRpZ2UgZGVpbmVuIFp1ZyBtaXQgR28uAAAABHBsYXkAJDBlNzZlMmFmLTYxN2YtNGExYS05ZjVmLTAyYjY3NjVkYWJkYwAkMjZiM2M2MzktZTFiYS00ZGRiLWI5ZmYtMmIzYjNjYTU2ZjAyAAAAAAAAAAAAAAABAAAAAAEAJDI2YjNjNjM5LWUxYmEtNGRkYi1iOWZmLTJiM2IzY2E1NmYwMgAAAAEAAAAEAAAABAAFZmxvb3IAAAAAAAAAAQAkNTBjMTY0NWItOTM2NC00MDhmLThlZDEtOGUxMzQ1NGI1ZWUyAChMZWdhY3kgaGF0IHp1Z2VzYWd0IHVuZCBpc3QgUmF1bW1laXN0ZXIh");
        GameEngine e=engine(store);DataInputStream in=new DataInputStream(new ByteArrayInputStream(store.data));in.readInt();in.readInt();in.readUTF();in.readUTF();in.readBoolean();in.readInt();in.readInt();in.readInt();in.readLong();in.readInt();in.readUTF();String token=in.readUTF();
        check(e.playing()&&e.snapshot(token).contains("\"subdivisions\":1")&&e.snapshot(token).contains("\"x\":4,\"y\":4"),"real 0.8.1 save keeps its original single-cell positions and rules");
        e.rebuildBoard();advance(e);cmd(e,token,"roomYes","");check(e.snapshot(token).contains("\"subdivisions\":2")&&e.snapshot(token).contains("\"x\":8,\"y\":8"),"explicit board rebuild upgrades old coordinates exactly once");
    }
    static void sharedAssets()throws Exception{
        byte[] js="window.BoardArt={};".getBytes(StandardCharsets.UTF_8),css=".field-label{color:green}".getBytes(StandardCharsets.UTF_8);
        try(LocalServer server=new LocalServer(engine(new MemoryStore()),new byte[0],0,name->null,"0.9.2",Map.of("/board-art.js",js,"/board-art.css",css))){
            js[0]='X';check(call(server,"/board-art.js",null,null,false).body.equals("window.BoardArt={};"),"shared vector script is immutable and available offline");
            check(call(server,"/board-art.css",null,null,false).body.equals(new String(css,StandardCharsets.UTF_8)),"shared field styling served");
            check(call(server,"/board-art.js","",null,true).status==404,"shared assets read-only");
            check(call(server,"/board-art.js/../session-v1.drw",null,null,false).status==404,"asset paths cannot expose private files");
            HttpURLConnection c=(HttpURLConnection)new URL("http://127.0.0.1:"+server.port()+"/board-art.js").openConnection();
            check(c.getHeaderField("Content-Type").startsWith("application/javascript"),"shared script has correct MIME");String policy=c.getHeaderField("Content-Security-Policy");check(policy.contains("script-src 'self'")&&policy.contains("style-src 'self'")&&!policy.contains("https:"),"CSP permits bundled scripts and styles without external origins");c.disconnect();
        }
    }

    static void http() throws Exception {
        GameEngine e = engine(new MemoryStore());
        try(LocalServer server=new LocalServer(e,"<h1>Dr_Who</h1>".getBytes(StandardCharsets.UTF_8),0,name->null,"0.5.1")){
            check(call(server,"/",null,null,false).body.contains("Dr_Who"),"player page served");
            check(call(server,"/api/lobby",null,null,false).body.equals("{\"occupied\":0,\"capacity\":4}"),"public lobby exposes only empty occupancy");
            Response info=call(server,"/api/info",null,null,false);
            check(info.status==200&&info.body.equals("{\"appVersion\":\"0.5.1\",\"protocolVersion\":4,\"sessionId\":"+GameEngine.quote(e.sessionId())+"}"),"public info exposes only version, protocol and non-secret party identity");
            check(call(server,"/api/info","",null,true).status==404,"metadata endpoint is read only");
            check(call(server,"/api/state",null,null,false).status==401,"unauthenticated state blocked");
            check(call(server,"/api/admin",null,null,false).status==404,"no remote host endpoint");
            Response wrong=call(server,"/api/join","name=A&code=654321&requestId="+id(),null,true);
            check(wrong.status==400&&wrong.body.contains("\"errorCode\":\"invalid_portal_code\""),"wrong code has explicit machine-readable error");
            check(!wrong.body.contains(e.code())&&call(server,"/api/lobby",null,null,false).body.contains("\"occupied\":0"),"wrong code reveals no correct code and occupies no player slot");
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
            start(e);
            Response rejectedRetry=call(server,"/api/join",form.replace(e.code(),"654321"),null,true);
            check(rejectedRetry.status==400,"wrong code cannot reclaim even with a known join proof");
            Response reclaimed=call(server,"/api/join",form,null,true);
            check(reclaimed.status==200&&text(reclaimed.body,"token").equals(token)&&call(server,"/api/lobby",null,null,false).body.contains("\"occupied\":1"),"corrected proof reclaims same existing slot during started game");
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
        GameEngine engine=engine(new MemoryStore());
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
