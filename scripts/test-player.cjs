// Optional DOM behavior tests; the shipped Android/browser app has no npm dependency.
const {JSDOM}=require('jsdom');
const fs=require('node:fs');
const assert=require('node:assert/strict');
let checks=0;
function check(value,message){assert.ok(value,message);checks++}
const characters=[
 {id:'doctor',name:'Der Doctor',description:'Einfallsreich',endurance:'Noch festzulegen',strength:'Noch festzulegen',portrait:'/portraits/doctor.jpg',abilities:['Türen öffnen'],owner:'A',enemy:false},
 {id:'dalek',name:'Dalek',description:'Gepanzert',endurance:'Noch festzulegen',strength:'Noch festzulegen',portrait:'/portraits/dalek.jpg',abilities:['Schuss bis 3 Felder'],owner:'B',enemy:false},
 {id:'weeping-angel',name:'Weinender Engel',description:'Zeitversetzung',endurance:'Noch festzulegen',strength:'Noch festzulegen',portrait:'/portraits/weeping-angel.jpg',abilities:['1–3 Runden aussetzen'],owner:'',enemy:false}
];
const base={sessionId:'session',revision:1,started:false,ready:true,selectedCount:2,round:1,lastRoll:0,you:'A',current:null,phase:'players',helperId:'',movementDone:false,activeEnemy:'',attack:{id:'',enemyId:'',targetId:'',text:'',pending:false},players:[{id:'A',name:'Alice',characterId:'doctor',team:'partners',x:0,y:0},{id:'B',name:'Bob',characterId:'dalek',team:'partners',x:1,y:0}],characters,enemies:[],log:[]};
const copy=o=>JSON.parse(JSON.stringify(o));
const tick=()=>new Promise(r=>setImmediate(r));
async function main(){
 let next=copy(base),requests=[],fail=false;
 const html=fs.readFileSync('app/src/main/assets/player.html','utf8');
 const dom=new JSDOM(html,{url:'http://192.168.4.1:8080',runScripts:'dangerously',beforeParse(w){
  w.localStorage.setItem('drwho.v1',JSON.stringify({token:'test-token'}));w.setInterval=()=>0;
  w.fetch=async(path,options)=>{
   if(path!=='/api/state')requests.push({path,body:new URLSearchParams(String(options.body))});
   if(fail&&path!=='/api/state')throw new Error('offline');
   return {ok:true,status:200,json:async()=>copy(next)};
  };
 }});
 const w=dom.window,$=id=>w.document.getElementById(id);
 try{
  await tick();
  check(!$('selection').hidden,'lobby shows selection');
  check($('characters').children.length===3,'three known dossiers');
  const buttons=[...w.document.querySelectorAll('.choose')];
  check(buttons[0].disabled&&buttons[1].disabled,'reserved cards cannot be selected again');
  check(!buttons[2].disabled,'unused character selectable');
  check($('characters').textContent.includes('Ausdauer')&&$('characters').textContent.includes('Kraft'),'attributes shown');
  check($('selectionStatus').textContent.includes('2 / 2'),'selection progress visible');
  const photo=$('characters').querySelector('img');photo.dispatchEvent(new w.Event('error'));
  check(!photo.isConnected&&$('characters').textContent.includes('FOTO FOLGT'),'missing photo degrades to labelled placeholder');
  // Retry a character selection after an unconfirmed network request.
  fail=true;buttons[2].click();await tick();
  check(requests.at(-1).path==='/api/character','character button uses authenticated choice route');
  check(!$('retry').hidden,'selection can be retried even outside game panel');
  const selectionRequest=requests.at(-1).body.get('requestId');
  fail=false;next.revision=2;next.players[0].characterId='weeping-angel';next.characters[0].owner='';next.characters[2].owner='A';
  $('retry').click();await tick();
  check(requests.at(-1).body.get('requestId')===selectionRequest,'selection retry reuses request identity');
  check($('retry').hidden,'confirmed choice clears retry');
  const active=copy(base);active.revision=3;active.started=true;active.current='A';w.render(active);
  check(!$('game').hidden&&$('idle').hidden,'active partner sees controls');
  check(!$('rollButton').disabled,'active partner may act');
  const waiting=copy(active);waiting.revision=4;waiting.current='B';w.render(waiting);
  check(!$('idle').hidden&&$('game').hidden,'uninvolved partner sees TARDIS');
  check($('idle').querySelector('.tardis'),'TARDIS animation exists');
  check($('rollButton').disabled,'uninvolved controls disabled');
  const enemy=copy(waiting);enemy.revision=5;enemy.current=null;enemy.phase='enemies';enemy.activeEnemy='weeping-angel';enemy.helperId='B';w.render(enemy);
  check(!$('idle').hidden&&$('helper').hidden,'unassigned player waits in enemy phase');
  enemy.revision=6;enemy.helperId='A';w.render(enemy);
  check(!$('helper').hidden&&$('idle').hidden,'selected helper sees movement task');
  next=copy(enemy);next.revision=7;next.helperId='';next.movementDone=true;
  const count=requests.length;$('confirmMovement').click();$('confirmMovement').click();await tick();
  check(requests.length===count+1,'double tap sends one movement confirmation');
  check(requests.at(-1).body.get('action')==='confirmMovement','helper confirms distinct action');
  const attack=copy(next);attack.revision=8;attack.attack={id:'attack-1',enemyId:'weeping-angel',targetId:'A',text:'Für 2 Runden gebannt. <img src=x onerror=alert(1)>',pending:true};w.render(attack);
  check(!$('battle').hidden&&$('idle').hidden,'attacked player gets active battle view');
  check($('defenderProfile').textContent.includes('Der Doctor'),'own dossier visible');
  check($('attackerProfile').textContent.includes('Weinender Engel'),'attacker dossier visible');
  check($('outcome').textContent===attack.attack.text&&!$('outcome').querySelector('img'),'outcome rendered as safe exact text');
  next=copy(attack);next.revision=9;next.attack.pending=false;$('ackAttack').click();await tick();
  check(requests.at(-1).body.get('action')==='ackAttack','target can acknowledge attack');
  check(!$('idle').hidden&&$('battle').hidden,'acknowledged target returns to TARDIS');
  const other=copy(attack);other.revision=10;other.attack.targetId='B';w.render(other);
  check($('battle').hidden&&!$('idle').hidden,'attack on someone else leaves player idle');
  w.render(active);check($('game').hidden,'late stale snapshot cannot reactivate wrong view');
  console.log('PASS: '+checks+' player-page behavior checks');
 }finally{w.close()}
}
main().catch(error=>{console.error(error);process.exitCode=1});
