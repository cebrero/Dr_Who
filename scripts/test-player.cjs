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
const base={sessionId:'session',revision:1,started:false,ready:true,selectedCount:2,round:1,lastRoll:0,turn:{stage:'roll',roll:0,remaining:0},you:'A',current:null,phase:'players',helperId:'',movementDone:false,activeEnemy:'',attack:{id:'',enemyId:'',targetId:'',text:'',pending:false},players:[{id:'A',name:'Alice',characterId:'doctor',team:'partners',x:0,y:0},{id:'B',name:'Bob',characterId:'dalek',team:'partners',x:1,y:0}],characters,enemies:[],log:[]};
const copy=o=>JSON.parse(JSON.stringify(o));
const tick=()=>new Promise(r=>setImmediate(r));
async function main(){
 let next=copy(base),requests=[],fail=false,revealTimer,revealStarts=0;
 const html=fs.readFileSync('app/src/main/assets/player.html','utf8');
 const dom=new JSDOM(html,{url:'http://192.168.4.1:8080/?code=123456',runScripts:'dangerously',beforeParse(w){
  const nativeTimeout=w.setTimeout.bind(w);w.setTimeout=(callback,ms)=>ms===2600?(revealTimer=callback,revealStarts++,999):nativeTimeout(callback,ms);
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
  check($('code').value==='123456','connection QR prefills code');
  check(requests.length===0,'QR code never joins a player without entering a name');
  check(!$('selection').hidden,'lobby shows selection');
  check($('characters').children.length===3,'three known dossiers');
  const buttons=[...w.document.querySelectorAll('.choose')];
  check(buttons[0].disabled&&buttons[1].disabled,'reserved cards cannot be selected again');
  check(!buttons[2].disabled,'unused character selectable');
  check($('characters').textContent.includes('Ausdauer')&&$('characters').textContent.includes('Kraft'),'attributes shown');
  check($('selectionStatus').textContent.includes('2 / 2'),'selection progress visible');
  const photo=$('characters').querySelector('img');photo.dispatchEvent(new w.Event('error'));
  check(!photo.isConnected&&$('characters').textContent.includes('FOTO FOLGT'),'missing photo degrades to labelled placeholder');
  const carousel=$('characters');
  Object.defineProperty(carousel,'clientWidth',{configurable:true,value:328});
  Object.defineProperty(w.HTMLElement.prototype,'offsetLeft',{configurable:true,get(){return this.parentElement===carousel?22+Array.from(carousel.children).indexOf(this)*300:0}});
  Object.defineProperty(w.HTMLElement.prototype,'offsetWidth',{configurable:true,get(){return this.parentElement===carousel?284:0}});
  carousel.scrollTo=({left})=>{carousel.scrollLeft=left;carousel.dispatchEvent(new w.Event('scroll'))};
  check($('previousCard').disabled&&!$('nextCard').disabled,'carousel starts with a previous boundary');
  $('nextCard').click();check($('cardPosition').textContent.includes('2 / 3 · Dalek'),'next arrow browses second dossier');
  w.render(copy(base));check($('cardPosition').textContent.includes('Dalek'),'unchanged polling does not reset browsed card');
  carousel.dispatchEvent(new w.KeyboardEvent('keydown',{key:'End',bubbles:true}));
  check($('nextCard').disabled&&$('cardPosition').textContent.includes('3 / 3'),'keyboard reaches final card and boundary');
  carousel.dispatchEvent(new w.KeyboardEvent('keydown',{key:'Home',bubbles:true}));
  check($('cardPosition').textContent.includes('1 / 3'),'keyboard returns to first card');
  $('nextCard').click();$('nextCard').click();
  // Retry a character selection after an unconfirmed network request.
  fail=true;buttons[2].click();await tick();
  check(requests.at(-1).path==='/api/character','character button uses authenticated choice route');
  check(!$('retry').hidden,'selection can be retried even outside game panel');
  const selectionRequest=requests.at(-1).body.get('requestId');
  fail=false;next.revision=2;next.players[0].characterId='weeping-angel';next.characters[0].owner='';next.characters[2].owner='A';
  $('retry').click();await tick();
  check(requests.at(-1).body.get('requestId')===selectionRequest,'selection retry reuses request identity');
  check($('retry').hidden,'confirmed choice clears retry');
  check($('cardPosition').textContent.includes('3 / 3 · Weinender Engel'),'updated character ownership preserves browsed card');
  const active=copy(base);active.revision=3;active.started=true;active.current='A';w.render(active);
  check(!$('game').hidden&&$('idle').hidden,'active partner sees controls');
  check(!$('rollButton').disabled,'active partner may act');
  check($('controls').hidden&&$('turnChoices').hidden,'first roll required before moving or acting');
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
  const choice=copy(active);choice.revision=11;choice.turn={stage:'choice',roll:5,remaining:5};w.render(choice);
  check(!$('turnChoices').hidden&&$('rollButton').hidden&&$('controls').hidden,'five exposes exactly movement/action choice');
  check($('moveChoiceButton').textContent==='5 Felder ziehen','movement option uses actual die count');
  const moving=copy(choice);moving.revision=12;moving.turn={stage:'move',roll:5,remaining:3};w.render(moving);
  check(!$('controls').hidden&&$('turnChoices').hidden,'choosing movement closes action choice');
  check($('turnPrompt').textContent.includes('3 von 5'),'remaining distance shown');
  const revealing=copy(choice);revealing.revision=13;revealing.rollEvent={id:'roll-1',playerId:'A',value:5};revealing.log=['Alice würfelt 5'];w.render(revealing);
  check($('roll').classList.contains('rolling')&&!$('roll').getAttribute('aria-label').includes('5'),'result stays hidden while dice tumble');
  check($('turnChoices').hidden&&$('moveChoiceButton').disabled&&!$('log').textContent.includes('würfelt 5'),'movement, action and result log wait for reveal');
  w.render(revealing);check(revealStarts===1,'polling same saved die does not restart animation');
  revealTimer();check($('rollResult').textContent==='Dein Wurf: 5'&&!$('turnChoices').hidden&&!$('moveChoiceButton').disabled,'after 2.6 seconds saved result and choices become available');
  check(JSON.parse(w.localStorage.getItem('drwho.v1')).revealedRoll==='session:roll-1','completed reveal remembered across reconnect');
  w.render(revealing);check(revealStarts===1,'same event does not animate again after reveal');
  const second=copy(revealing);second.revision=14;second.rollEvent.id='roll-2';w.render(second);
  check(revealStarts===2&&$('roll').classList.contains('rolling'),'a new event with same die value animates again');revealTimer();
  console.log('PASS: '+checks+' player-page behavior checks');
 }finally{w.close()}
 // The join screen must show real occupancy without authenticating or registering a player.
 let occupied=0,offline=false,lobbyPoll,joins=0;
 const guest=new JSDOM(html,{url:'http://192.168.4.1:8080/',runScripts:'dangerously',beforeParse(w){
  w.setInterval=callback=>{lobbyPoll=callback;return 0};
  w.fetch=async(path)=>{
   if(path==='/api/join')joins++;
   assert.equal(path,'/api/lobby');
   if(offline)throw new Error('offline');
   return {ok:true,status:200,json:async()=>({occupied,capacity:4})};
  };
 }});
 try{
  const $=id=>guest.window.document.getElementById(id);
  await tick();check($('joinOccupancy').textContent==='0 von 4 Plätzen belegt · 4 frei','empty lobby occupancy fetched before joining');
  $('name').value='Martha';$('code').value='123456';occupied=3;await lobbyPoll();
  check($('joinOccupancy').textContent==='3 von 4 Plätzen belegt · 1 frei','occupancy updates as other players join');
  occupied=4;await lobbyPoll();check($('joinOccupancy').textContent==='4 von 4 Plätzen belegt · 0 frei','full lobby displayed accurately');
  offline=true;await lobbyPoll();check($('joinOccupancy').textContent.includes('nicht verfügbar'),'network failure does not leave stale occupancy presented as current');
  offline=false;await lobbyPoll();check($('joinOccupancy').textContent.startsWith('4 von 4'),'occupancy recovers after network interruption');
  check($('name').value==='Martha'&&$('code').value==='123456'&&joins===0,'occupancy polling preserves input and never registers guest');
  console.log('PASS: '+checks+' total player-page behavior checks');
 }finally{guest.window.close()}
}
main().catch(error=>{console.error(error);process.exitCode=1});
