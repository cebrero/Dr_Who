package de.cebrero.drwho.core;
import java.util.*;import static de.cebrero.drwho.core.CoreTest.*;
final class CharacterInventoryTest {
 static void run()throws Exception{
  List<CharacterProfile> all=CharacterProfile.known();Set<String> ids=new HashSet<>(),types=new HashSet<>();Map<String,Integer> count=new HashMap<>();
  for(CharacterProfile p:all){check(ids.add(p.id),"every physical miniature has one unique ID");types.add(p.archetype);count.merge(p.archetype,1,Integer::sum);check(p.endurance.equals("Noch festzulegen")&&p.strength.equals("Noch festzulegen"),"new catalogue does not invent attributes");check(p.portrait.equals("/portraits/"+p.archetype+".jpg"),"duplicate figures reuse canonical local portrait");}
  check(all.size()==11&&types.size()==8,"confirmed eight types and eleven physical figures");check(count.get("dalek")==2&&count.get("weeping-angel")==2&&count.get("cyberman")==2,"photo duplicates are individually represented");
  MemoryStore store=new MemoryStore();Dice dice=new Dice();dice.face=6;GameEngine e=engine(store,dice);String d1=ready(e,"D1","dalek"),d2=ready(e,"D2","dalek-2"),c1=ready(e,"C1","cyberman"),c2=ready(e,"C2","cyberman-2");
  check(e.readyForPlay()&&e.snapshot(d1).contains("\"enemies\":[]"),"four real catalogue figures can join as partners with no automatic enemies");rejects(IllegalArgumentException.class,()->e.chooseCharacter(c1,id(),"dalek-2"));e.addEnemy("weeping-angel");e.addEnemy("weeping-angel-2");check(e.snapshot(d1).contains("\"characterId\":\"weeping-angel-2\",\"team\":\"enemies\""),"unused duplicate can become an independent enemy");
  start(e);begin(e,d1);cmd(e,d1,"roll","");cmd(e,d1,"action","N");begin(e,d2);cmd(e,d2,"roll","");cmd(e,d2,"action","N");check(e.snapshot(d2).contains("Ich richte meinen Strahl"),"second Dalek retains the Dalek action voice");check(engine(store,dice).snapshot(d2).equals(e.snapshot(d2)),"new IDs and four real seats survive save migration-free");
  for(String character:Arrays.asList("sontaran","cyberman","snowman","adipose","zygon")){
   GameEngine one=engine(new MemoryStore(),dice);String token=ready(one,"Actor",character);start(one);begin(one,token);cmd(one,token,"roll","");String before=one.snapshot(token);rejects(IllegalArgumentException.class,()->cmd(one,token,"action",""));check(before.equals(one.snapshot(token)),"undefined new ability cannot consume turn");cmd(one,token,"chooseMove","");cmd(one,token,"move","S");check(one.snapshot(token).contains("\"remaining\":5"),"new figure uses ordinary exact-step movement");
  }
 }
}
