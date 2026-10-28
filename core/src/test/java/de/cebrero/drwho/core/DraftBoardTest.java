package de.cebrero.drwho.core;
import static de.cebrero.drwho.core.CoreTest.*;
import java.io.*;
/** Real setup, automatic portal, reversible draft expansion and durable coordinates. */
final class DraftBoardTest {
 static void run()throws Exception{
  MemoryStore disk=new MemoryStore();GameEngine e=engine(disk);String a=ready(e,"Builder","doctor");startVoted(e);advance(e);cmd(e,a,"roomYes","");
  check(e.portalState().tiles.size()==1&&e.portalState().tiles.get(0).kind.equals("portal"),"new roommaster receives one automatic portal");check(e.portalState().tiles.get(0).rotation==0&&e.fieldName(0,0).equals("A1"),"portal faces south and starts at A1");
  check(engine(disk).snapshot(a).equals(e.snapshot(a)),"automatic portal is durable before any manual tile");
  cmd(e,a,"tilePlace","0,1,floor,0");String id=text(e.snapshot(a),"you");cmd(e,a,"setSpawn",id+",0,2");
  String before=e.snapshot(a),request=id();long rev=revision(before);disk.fail=true;rejects(IOException.class,()->e.command(a,request,rev,"tilePlace","-1,1,corner,2"));check(e.snapshot(a).equals(before),"failed expansion preserves every coordinate");disk.fail=false;
  e.command(a,request,rev,"tilePlace","-1,1,corner,2");String shifted=e.snapshot(a);check(e.portalState().tiles.stream().anyMatch(t->t.kind.equals("portal")&&t.x==1&&t.y==0),"left insertion shifts the entire draft once");check(e.fieldName(2,0).equals("C1")&&e.fieldName(0,2).equals("A3"),"coordinates are defined from the new leftmost tile");check(e.snapshot(a).contains("\"x\":2,\"y\":2"),"manual player placement shifts together with the real tile");e.command(a,request,rev,"tilePlace","-1,1,corner,2");check(shifted.equals(e.snapshot(a)),"retry never shifts coordinates twice");
  check(engine(disk).snapshot(a).equals(shifted),"normalized coordinates survive host restart");
  String saved=e.snapshot(a);rejects(IllegalArgumentException.class,()->cmd(e,a,"tilePlace","-19,2,floor,0"));check(saved.equals(e.snapshot(a)),"physical twenty-column limit rejects expansion atomically");rejects(IllegalArgumentException.class,()->cmd(e,a,"tilePlace","-1,1,portal,0"));check(saved.equals(e.snapshot(a)),"overlapping left insertion rolls back the whole translation");
  cmd(e,a,"tileMove","0,1,-1,2");check(e.portalState().tiles.stream().anyMatch(t->t.kind.equals("corner")&&t.x==0&&t.y==2),"moving a tile into the left margin also normalizes the complete board");
  cmd(e,a,"boardSave","");advance(e);check(e.playing(),"expanded room starts through the ordinary dossier gate");e.rebuildBoard();advance(e);cmd(e,a,"roomYes","");String running=e.snapshot(a);rejects(IllegalArgumentException.class,()->cmd(e,a,"tilePlace","-1,3,floor,0"));check(e.snapshot(a).equals(running),"running game coordinates remain fixed during later repairs");
  GameEngine forced=engine(new MemoryStore());String f=ready(forced,"Only","doctor");startVoted(forced);advance(forced);cmd(forced,f,"roomNo","");check(forced.portalState().tiles.size()==1&&forced.portalState().tiles.get(0).kind.equals("portal"),"forced roommaster receives the same starting portal");
 }
}
