package de.cebrero.drwho.core;
import java.util.*;import java.io.*;import java.time.*;
import static de.cebrero.drwho.core.CoreTest.*;
final class RoomLibraryTest {
 static final class RoomDisk extends RoomLibrary.MemoryStore {boolean fail;public void save(String id,byte[] b)throws IOException{if(fail)throw new IOException("room disk full");super.save(id,b);}}
 static GameEngine open(MemoryStore game,RoomDisk disk)throws Exception{
  TestClock clock=new TestClock(Clock.fixed(Instant.parse("2026-10-27T17:35:20Z"),ZoneId.of("Europe/Berlin")));
  GameEngine e=new GameEngine(game,new Dice(),CharacterProfile.known(),clock,new Random(0),new RoomLibrary(disk));testStores.put(e,game);testClocks.put(e,clock);return e;
 }
 static String board(String snapshot){return snapshot.substring(snapshot.indexOf("\"board\":"),snapshot.indexOf(",\"round\":"));}
 static void run()throws Exception{
  MemoryStore game=new MemoryStore();RoomDisk disk=new RoomDisk();GameEngine e=open(game,disk);String a=ready(e,"A","doctor"),b=ready(e,"B","zygon");
  rejects(SecurityException.class,()->e.roomLibrary("bad"));rejects(IllegalArgumentException.class,()->e.roomLibrary(a));e.startDemo();advance(e);cmd(e,b,"roomYes","");
  rejects(IllegalArgumentException.class,()->e.roomLibrary(a));check(e.roomLibrary(b).contains("27.10.2026-18.35.23_Zygon"),"room default uses host date/time and builder character");
  rejects(IllegalArgumentException.class,()->cmd(e,b,"roomSave","Empty"));
  cmd(e,b,"tilePlace","0,0,floor,0");cmd(e,b,"tilePlace","1,0,corner,2");cmd(e,b,"tilePlace","0,1,dungeon,3");cmd(e,b,"tileState","0,1");cmd(e,b,"tilePlace","4,4,portal,1");
  cmd(e,a,"readDossier","");String plan=board(e.snapshot(a)),before=e.snapshot(b),request=id();long rev=revision(before);disk.fail=true;
  rejects(IOException.class,()->e.command(b,request,rev,"roomSave","Mein Raum"));check(disk.loadAll().isEmpty()&&e.snapshot(b).equals(before),"failed template write changes neither library nor game");disk.fail=false;
  e.command(b,request,rev,"roomSave","Mein Raum");check(e.snapshot(b).equals(before),"saving a reusable layout does not alter current game, read flags, revision or story");
  String listing=e.roomLibrary(b),roomId=text(listing,"id");check(listing.contains("\"tiles\":4")&&listing.contains("Mein Raum"),"template metadata contains real name and tile count");
  check(!listing.contains("token")&&!listing.contains("story")&&!listing.contains("bag")&&!listing.contains("keyX"),"layout library never exposes game secrets");
  cmd(e,b,"tileRemove","1,0");String changed=e.snapshot(b);e.command(b,request,rev,"roomSave","Mein Raum");check(disk.loadAll().size()==1&&e.snapshot(b).equals(changed),"retry cannot capture a changed board or create a second file");
  rejects(IllegalArgumentException.class,()->e.command(b,request,rev,"roomSave","Changed intent"));
  rejects(IllegalArgumentException.class,()->cmd(e,b,"roomSave","mein raum"));for(String name:new String[]{""," ","a\nb","x".repeat(81)})rejects(IllegalArgumentException.class,()->cmd(e,b,"roomSave",name));
  rejects(IllegalArgumentException.class,()->e.command(b,id(),rev,"roomSave","Stale first save"));check(disk.loadAll().size()==1,"stale first save cannot silently capture later edits");
  GameEngine restored=open(game,disk);check(restored.roomLibrary(b).contains(roomId),"host restart reloads persistent room index");restored.command(b,request,rev,"roomSave","Mein Raum");check(disk.loadAll().size()==1,"save retry is durable across restart");
  rejects(IllegalArgumentException.class,()->cmd(restored,a,"roomLoad",roomId));rejects(IllegalArgumentException.class,()->cmd(restored,b,"roomLoad","../../session-v1.drw"));
  String loadRequest=id();long loadRev=revision(restored.snapshot(b));game.fail=true;String beforeLoad=restored.snapshot(b);
  rejects(IOException.class,()->restored.command(b,loadRequest,loadRev,"roomLoad",roomId));check(beforeLoad.equals(restored.snapshot(b)),"failed room load leaves active draft untouched");game.fail=false;
  restored.command(b,loadRequest,loadRev,"roomLoad",roomId);check(board(restored.snapshot(b)).equals(plan),"named layout restores every tile, orientation, dungeon state and both portal halves");
  check(restored.portalState().setupStage.equals("building")&&restored.snapshot(a).contains("\"readBy\":["+GameEngine.quote(text(restored.snapshot(a),"you"))+ "]"),"room load stays in editor and preserves participants and reading progress");
  String after=restored.snapshot(b);restored.command(b,loadRequest,loadRev,"roomLoad",roomId);check(after.equals(restored.snapshot(b)),"load retry does not replay story or revise positions twice");
  try(LocalServer server=new LocalServer(restored,new byte[0],0)){
   check(call(server,"/api/rooms",null,null,false).status==401,"room index requires authentication");check(call(server,"/api/rooms",null,a,false).status==400,"other player cannot list room library");check(call(server,"/api/rooms",null,b,false).status==200,"roommaster can read library");check(call(server,"/api/rooms","name=bad",b,true).status==404,"no extra administration write route");
  }
  restored.resetGame();String again=ready(restored,"Next","doctor");restored.startDemo();advance(restored);cmd(restored,again,"roomYes","");check(restored.roomLibrary(again).contains(roomId),"game reset keeps reusable rooms");cmd(restored,again,"roomLoad",roomId);check(board(restored.snapshot(again)).equals(plan),"another game can reuse complete saved geometry");
  byte[] bytes=disk.loadAll().values().iterator().next();String bad=UUID.randomUUID().toString();disk.save(bad,Arrays.copyOf(bytes,bytes.length-4));RoomLibrary library=new RoomLibrary(disk);check(library.json("default").contains("\"unreadable\":1")&&disk.loadAll().size()==2,"corrupt original room is reported and preserved without losing valid rooms");
 }
}
