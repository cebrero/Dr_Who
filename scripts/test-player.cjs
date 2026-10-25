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
 let next=copy(base),requests=[],fail=false,unauthorized=false,networkState=false,revealTimer,revealStarts=0;
 const html=fs.readFileSync('app/src/main/assets/player.html','utf8');
 const dom=new JSDOM(html,{url:'http://192.168.4.1:8080/?code=123456',runScripts:'dangerously',beforeParse(w){
  const nativeTimeout=w.setTimeout.bind(w);w.setTimeout=(callback,ms)=>ms===2600?(revealTimer=callback,revealStarts++,999):nativeTimeout(callback,ms);
  w.localStorage.setItem('drwho.v1',JSON.stringify({token:'test-token'}));w.setInterval=()=>0;
  w.fetch=async(path,options)=>{
   if(path==='/api/info')return {ok:true,status:200,json:async()=>({appVersion:'0.5.1',protocolVersion:1,sessionId:'session'})};
   if(path!=='/api/state')requests.push({path,body:new URLSearchParams(String(options.body))});
   if(fail&&path!=='/api/state'||networkState&&path==='/api/state')throw new Error('offline');
   if(unauthorized&&path==='/api/command')return {ok:false,status:401,json:async()=>({error:'Bitte mit Portalcode anmelden.'})};
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
  networkState=true;await w.poll();check($('join').hidden&&JSON.parse(w.localStorage.getItem('drwho.v1')).token==='test-token','temporary network failure retains valid login');networkState=false;w.render(second);
  unauthorized=true;$('actionChoiceButton').click();await tick();
  check(!$('join').hidden&&$('game').hidden&&$('selection').hidden,'command authorization failure returns to join form');
  check($('retry').hidden&&JSON.parse(w.localStorage.getItem('drwho.v1')).pending===null,'expired command is discarded rather than replayed into next game');
  console.log('PASS: '+checks+' player-page behavior checks');
 }finally{w.close()}
 // The join screen must show real occupancy without authenticating or registering a player.
 let occupied=0,offline=false,lobbyPoll,joins=0;
 const guest=new JSDOM(html,{url:'http://192.168.4.1:8080/',runScripts:'dangerously',beforeParse(w){
  w.setInterval=callback=>{lobbyPoll=callback;return 0};
  w.fetch=async(path)=>{
   if(path==='/api/info')return {ok:true,status:200,json:async()=>({appVersion:'0.5.1',protocolVersion:1,sessionId:'session'})};
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
 // Fresh host installation rejects the token left behind in the client browser.
 for(const newParty of [false,true]){
  let recoveryPoll,valid=false;const posts=[],held=[];
  const originalJoin={name:'Alice',code:'123456',requestId:'original-join-request-1234'};
  const recovery=new JSDOM(html,{url:'http://192.168.4.1:8080/?code='+(newParty?'654321':'123456'),runScripts:'dangerously',beforeParse(w){
   w.localStorage.setItem('drwho.v1',JSON.stringify({token:'obsolete-token',sessionId:'old-session',join:originalJoin,pending:{requestId:'old-move',action:'move'},revealedRoll:'old-roll'}));
   w.setInterval=callback=>{recoveryPoll=callback;return 0};
   w.fetch=async(path,options)=>{
    if(path==='/api/info')return {ok:true,status:200,json:async()=>({appVersion:'0.5.1',protocolVersion:1,sessionId:'old-session'})};
    if(path==='/api/state'){
     if(!valid)return await new Promise(resolve=>held.push(resolve));
     return {ok:true,status:200,json:async()=>copy(base)};
    }
    if(path==='/api/lobby')return {ok:true,status:200,json:async()=>({occupied:newParty?0:1,capacity:4})};
    assert.equal(path,'/api/join');posts.push({body:new URLSearchParams(String(options.body)),headers:options.headers});valid=true;
    return {ok:true,status:200,json:async()=>({token:'current-token',sessionId:'session'})};
   };
  }});
  try{
   const w=recovery.window,$=id=>w.document.getElementById(id);
   // Keep a second old poll in flight so a late 401 can arrive after successful login.
   await tick();const late=recoveryPoll();await tick();
   held[0]({ok:false,status:401,json:async()=>({error:'Bitte mit Portalcode anmelden.'})});await tick();
   check(!$('join').hidden&&['game','selection','idle','helper','battle'].every(id=>$(id).hidden),'stale token never traps client on old game screen');
   const stored=JSON.parse(w.localStorage.getItem('drwho.v1'));
   check(stored.token===''&&stored.pending===null&&stored.join.requestId===originalJoin.requestId,'invalid access cleared while original join proof remains');
   check($('name').value==='Alice'&&$('code').value===(newParty?'654321':'123456'),'name retained and current QR code available');
   check(posts.length===0,'expired token does not silently join or occupy another seat');
   await recoveryPoll();check($('joinOccupancy').textContent.startsWith((newParty?'0':'1')+' von 4'),'fresh lobby occupancy available after rejection');
   $('join').dispatchEvent(new w.Event('submit',{bubbles:true,cancelable:true}));await tick();
   check(posts.length===1&&!posts[0].headers.Authorization,'explicit new login sends no rejected bearer token');
   check((posts[0].body.get('requestId')===originalJoin.requestId)===!newParty,'same name/code reuses join proof; changed portal code creates fresh request');
   check($('join').hidden&&!$('selection').hidden&&JSON.parse(w.localStorage.getItem('drwho.v1')).token==='current-token','client proceeds to current character selection');
   held[1]({ok:false,status:401,json:async()=>({error:'old response'})});await late;
   check($('join').hidden&&JSON.parse(w.localStorage.getItem('drwho.v1')).token==='current-token','late old 401 cannot sign out new valid registration');
  }finally{recovery.window.close()}
 }
 for(const mismatch of ['party','version','protocol']){
  let stateRequests=0,postRequests=0;
  const guarded=new JSDOM(html,{url:'http://192.168.4.1:8080/',runScripts:'dangerously',beforeParse(w){
   w.localStorage.setItem('drwho.v1',JSON.stringify({token:'known-token',sessionId:'session',join:{name:'Alice',code:'123456',requestId:'original-join-request-1234'},pending:{requestId:'pending-command',action:'move'}}));w.setInterval=()=>0;
   w.fetch=async(path,options)=>{
    if(options.method==='POST')postRequests++;
    if(path==='/api/info')return {ok:true,status:200,json:async()=>({appVersion:mismatch==='version'?'0.5.2':'0.5.1',protocolVersion:mismatch==='protocol'?2:1,sessionId:mismatch==='party'?'new-party':'session'})};
    if(path==='/api/state')stateRequests++;
    return {ok:true,status:200,json:async()=>({occupied:0,capacity:4})};
   };
  }});
  try{
   await tick();const $=id=>guarded.window.document.getElementById(id),stored=JSON.parse(guarded.window.localStorage.getItem('drwho.v1'));
   if(mismatch==='party'){
    check(!$('join').hidden&&$('reloadPage').hidden,'same app version with new party returns directly to login');
    check(stored.token===''&&stored.pending===null,'party change discards old access and commands');
   }else{
    check(!$('reloadPage').hidden&&$('join').hidden,'version/protocol mismatch offers reload instead of false login failure');
    check(stored.token==='known-token'&&stored.pending.requestId==='pending-command','software update preserves same-party credentials and request receipt');
    guarded.window.render(copy(base));check($('selection').hidden&&!$('reloadPage').hidden,'late old snapshot cannot reopen UI after compatibility block');
   }
   check(stateRequests===0&&postRequests===0,'metadata mismatch blocks stale state and automatic registration');
   check(['game','selection','idle','helper','battle'].every(id=>$(id).hidden),'mismatch never leaves an interactive old view visible');
  }finally{guarded.window.close()}
 }
 console.log('PASS: '+checks+' total player-page behavior checks including expired registrations');
}
main().catch(error=>{console.error(error);process.exitCode=1});
