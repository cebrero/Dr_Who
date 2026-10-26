package de.cebrero.drwho.core;
import java.nio.file.*;import java.io.*;import java.time.*;import java.util.*;
public final class BrowserPortalFixture {
 private static LocalServer open(GameEngine e,byte[] page,int port,LocalServer.Portraits portraits)throws Exception{
  for(int attempt=0;;attempt++)try{return new LocalServer(e,page,port,portraits,"0.9.1");}catch(java.net.BindException busy){if(attempt>=20)throw busy;Thread.sleep(50);}
 }
 public static void main(String[] args)throws Exception{
  Path root=Path.of(args[0]);CoreTest.MemoryStore store=new CoreTest.MemoryStore();CoreTest.Dice dice=new CoreTest.Dice();dice.face=6;
  CoreTest.TestClock clock=new CoreTest.TestClock(Clock.fixed(Instant.parse("2026-10-26T14:34:00Z"),ZoneId.of("Europe/Berlin")));
  Random sound=new Random(){public int nextInt(int bound){return 0;}};GameEngine e=new GameEngine(store,dice,CharacterProfile.known(),clock,sound);
  byte[] page=Files.readAllBytes(root.resolve("app/src/main/assets/player.html"));LocalServer.Portraits portraits=name->Files.readAllBytes(root.resolve("app/src/main/assets/portraits/"+name));
  LocalServer server=open(e,page,0,portraits);int port=server.port();
  System.out.println("{\"port\":"+port+",\"code\":"+GameEngine.quote(e.code())+"}");System.out.flush();
  byte[] archived=null;
  try(BufferedReader input=new BufferedReader(new InputStreamReader(System.in))){String command;while((command=input.readLine())!=null){
   if(command.startsWith("REMOVE ")){String name=command.substring(7);for(String[] p:e.playersForHost())if(p[1].equals(name))e.removePlayer(p[0]);}
   if(command.equals("SAVE"))archived=e.exportGame();if(command.equals("RESET"))e.resetGame();if(command.equals("LOAD"))e.loadGame(archived);
   if(command.equals("START"))e.startDemo();if(command.equals("REBUILD"))e.rebuildBoard();if(command.equals("TICK")){clock.time+=3000;e.tick();}
   if(command.equals("RESTORE")){server.close();e=new GameEngine(store,dice,CharacterProfile.known(),clock,sound);server=open(e,page,port,portraits);}
   if(command.equals("EXIT"))break;
   GameEngine.PortalState s=e.portalState();StringBuilder b=new StringBuilder("{\"stage\":"+GameEngine.quote(s.setupStage)+",\"preparing\":"+s.preparing+",\"tiles\":"+s.tiles.size()+",\"awaitingGo\":"+s.awaitingGo+",\"headline\":"+GameEngine.quote(s.headline)+",\"setupTitle\":"+GameEngine.quote(s.setupTitle)+",\"notices\":[");
   for(int i=0;i<s.notices.size();i++){if(i>0)b.append(',');b.append(GameEngine.quote(s.notices.get(i).text));}b.append("],\"events\":[");for(int i=0;i<s.events.size();i++){if(i>0)b.append(',');b.append(GameEngine.quote(s.events.get(i)));}b.append("]}");System.out.println(b);System.out.flush();
  }}finally{server.close();}
 }
}
