package de.cebrero.drwho.core;
import java.nio.file.*;import java.io.*;import java.time.*;import java.util.*;
public final class BrowserPortalFixture {
 private static LocalServer open(GameEngine e,byte[] page,int port,LocalServer.Portraits portraits,Path root)throws Exception{
  for(int attempt=0;;attempt++)try{return new LocalServer(e,page,port,portraits,"0.13.0",Map.of("/room-library.js",Files.readAllBytes(root.resolve("app/src/main/assets/room-library.js")),"/room-library.css",Files.readAllBytes(root.resolve("app/src/main/assets/room-library.css")),"/character-dossiers.js",Files.readAllBytes(root.resolve("app/src/main/assets/character-dossiers.js")),"/character-dossiers.css",Files.readAllBytes(root.resolve("app/src/main/assets/character-dossiers.css")),"/board-art.js",Files.readAllBytes(root.resolve("app/src/main/assets/board-art.js")),"/board-art.css",Files.readAllBytes(root.resolve("app/src/main/assets/board-art.css"))));}catch(java.net.BindException busy){if(attempt>=20)throw busy;Thread.sleep(50);}
 }
 public static void main(String[] args)throws Exception{
  Path root=Path.of(args[0]);CoreTest.MemoryStore store=new CoreTest.MemoryStore();CoreTest.Dice dice=new CoreTest.Dice();dice.face=6;
  CoreTest.TestClock clock=new CoreTest.TestClock(Clock.fixed(Instant.parse("2026-10-26T14:34:00Z"),ZoneId.of("Europe/Berlin")));
  Random sound=new Random(){public int nextInt(int bound){return 0;}};RoomLibrary.MemoryStore roomStore=new RoomLibrary.MemoryStore();GameEngine e=new GameEngine(store,dice,CharacterProfile.known(),clock,sound,new RoomLibrary(roomStore));
  CoreTest.testStores.put(e,store);CoreTest.testClocks.put(e,clock);
  byte[] page=Files.readAllBytes(root.resolve("app/src/main/assets/player.html"));LocalServer.Portraits portraits=name->Files.readAllBytes(root.resolve("app/src/main/assets/portraits/"+name));
  LocalServer server=open(e,page,0,portraits,root);int port=server.port();
  System.out.println("{\"port\":"+port+",\"code\":"+GameEngine.quote(e.code())+"}");System.out.flush();
  byte[] archived=null;
  try(BufferedReader input=new BufferedReader(new InputStreamReader(System.in))){String command;while((command=input.readLine())!=null){
   if(command.startsWith("REMOVE ")){String name=command.substring(7);for(String[] p:e.playersForHost())if(p[1].equals(name))e.removePlayer(p[0]);}
   if(command.equals("SAVE"))archived=e.exportGame();if(command.equals("RESET"))e.resetGame();if(command.equals("LOAD"))e.loadGame(archived);
   if(command.startsWith("ENEMY "))e.addEnemy(command.substring(6));if(command.equals("START"))CoreTest.startVoted(e);if(command.equals("VOTE_START"))e.startDemo();if(command.equals("REBUILD"))e.rebuildBoard();if(command.equals("TICK")){clock.time+=3000;e.tick();}
   if(command.equals("RESTORE")){server.close();e=new GameEngine(store,dice,CharacterProfile.known(),clock,sound,new RoomLibrary(roomStore));server=open(e,page,port,portraits,root);CoreTest.testStores.put(e,store);CoreTest.testClocks.put(e,clock);}
   if(command.equals("EXIT"))break;
   GameEngine.PortalState s=e.portalState();StringBuilder b=new StringBuilder("{\"stage\":"+GameEngine.quote(s.setupStage)+",\"rollId\":"+GameEngine.quote(s.rollId)+",\"die\":"+s.die+",\"rollPresented\":"+s.rollPresented+",\"objectiveText\":"+GameEngine.quote(s.objectiveText)+",\"voteStatus\":"+GameEngine.quote(s.voteStatus)+",\"preparing\":"+s.preparing+",\"tiles\":"+s.tiles.size()+",\"awaitingGo\":"+s.awaitingGo+",\"headline\":"+GameEngine.quote(s.headline)+",\"setupTitle\":"+GameEngine.quote(s.setupTitle)+",\"keyEvent\":"+GameEngine.quote(s.keyEvent)+",\"notices\":[");
   for(int i=0;i<s.notices.size();i++){if(i>0)b.append(',');b.append(GameEngine.quote(s.notices.get(i).text));}b.append("],\"events\":[");for(int i=0;i<s.events.size();i++){if(i>0)b.append(',');b.append(GameEngine.quote(s.events.get(i)));}b.append("]}");System.out.println(b);System.out.flush();
  }}finally{server.close();}
 }
}
