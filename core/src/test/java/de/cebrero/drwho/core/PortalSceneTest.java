package de.cebrero.drwho.core;
import static de.cebrero.drwho.core.CoreTest.*;
final class PortalSceneTest {
 static void run()throws Exception{
  MemoryStore disk=new MemoryStore();Dice dice=new Dice();dice.face=6;GameEngine e=engine(disk,dice);String a=ready(e,"Odin","weeping-angel"),b=ready(e,"Bob","dalek-2");start(e);
  String before=e.snapshot(a),go=PortalScene.json(e.portalState(),false,false);
  check(go.contains("\"mode\":\"go\"")&&go.contains("\"actor\":\"Odin\"")&&go.contains("weeping-angel.jpg"),"Go stage has real active character and player");
  check(go.contains("\"rollId\":\"\"")&&go.contains("\"die\":0"),"new turn never displays a previous player's die");
  check(!go.contains("Noch festzulegen")&&!go.contains("timestampMillis"),"shared portal has no placeholder stats or raw chronology");
  check(before.equals(e.snapshot(a)),"projecting portal animation never mutates game state");
  begin(e,a);String ready=PortalScene.json(e.portalState(),false,false);check(ready.contains("\"mode\":\"ready\"")&&ready.contains("Tippe den Würfel"),"ready stage invites roll on client");
  cmd(e,a,"roll","");String waiting=PortalScene.json(e.portalState(),false,false);
  check(waiting.contains("\"mode\":\"rolling\"")&&waiting.contains("\"die\":0")&&waiting.contains("\"steps\":0")&&waiting.contains("\"remaining\":0"),"all visible numeric result channels masked until player completion");
  check(!waiting.contains("Ich würfle eine 6"),"raw result speech not passed to waiting renderer");
  String roll=e.portalState().rollId;cmd(e,a,"ackRoll",roll);String result=PortalScene.json(e.portalState(),false,false);
  check(result.contains("\"die\":6")&&result.contains("\"mode\":\"choice\"")&&result.contains("\"seen\":false"),"portal can reveal committed result after matching ACK");
  check(PortalScene.json(e.portalState(),true,true).contains("\"seen\":true,\"reduced\":true"),"native presentation preferences reach renderer without state change");
  cmd(e,a,"chooseMove","");cmd(e,a,"move","S");String move=PortalScene.json(e.portalState(),false,false);
  check(move.contains("A1  →  A2")&&move.contains("\"remaining\":5")&&move.contains("Noch 5 von 6"),"movement renders actual origin destination and exact steps left");
  for(int i=0;i<5;i++)cmd(e,a,"move","S");String next=PortalScene.json(e.portalState(),false,false);
  check(next.contains("\"actor\":\"Bob\"")&&next.contains("/portraits/dalek.jpg")&&next.contains("\"rollId\":\"\""),"next duplicate character uses canonical portrait and no old roll");
  check(next.contains("A6  →  A7"),"last movement remains publicly visible while next player waits for Go");
  begin(e,b);cmd(e,b,"roll","");cmd(e,b,"action","N");String action=PortalScene.json(e.portalState(),false,false);
  GameEngine opposing=engine(new MemoryStore(),dice);String actor=ready(opposing,"Doctor","doctor");opposing.addEnemy("sontaran");start(opposing);begin(opposing,actor);cmd(opposing,actor,"roll","");cmd(opposing,actor,"action","N");String enemy=PortalScene.json(opposing.portalState(),false,false);
  check(enemy.contains("\"mode\":\"enemy\"")&&enemy.contains("sontaran.jpg")&&enemy.contains("\"actor\":\"Sontaraner\""),"enemy stage gets real character art and no human roll");
  check(action.contains("AKTION VORGEMERKT")&&action.contains("Wirkung ist noch offen"),"technical action selection never announced as a successful hit");
  check(!enemy.contains("keyX")&&!enemy.contains("token")&&!enemy.contains("code"),"no secrets included in local presentation projection");
 }
}
