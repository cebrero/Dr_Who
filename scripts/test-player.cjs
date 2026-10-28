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
const base={sessionId:'session',revision:1,started:false,ready:true,selectedCount:2,round:1,lastRoll:0,turn:{accepted:true,stage:'roll',roll:0,remaining:0},you:'A',current:null,phase:'players',helperId:'',movementDone:false,activeEnemy:'',attack:{id:'',enemyId:'',targetId:'',text:'',pending:false},players:[{id:'A',name:'Alice',characterId:'doctor',team:'partners',x:0,y:0},{id:'B',name:'Bob',characterId:'dalek',team:'partners',x:1,y:0}],characters,enemies:[],log:[]};
const copy=o=>JSON.parse(JSON.stringify(o));
const tick=()=>new Promise(r=>setImmediate(r));
async function main(){
 let next=copy(base),requests=[],fail=false,unauthorized=false,networkState=false,revealTimer,revealStarts=0;
 const html=fs.readFileSync('app/src/main/assets/player.html','utf8').replace('<script src="/room-library.js"></script>','<script>'+fs.readFileSync('app/src/main/assets/room-library.js','utf8')+'</script>').replace('<script src="/character-dossiers.js"></script>','<script>'+fs.readFileSync('app/src/main/assets/character-dossiers.js','utf8')+'</script>').replace('<script src="/board-art.js"></script>','<script>'+fs.readFileSync('app/src/main/assets/board-art.js','utf8')+'</script>');
 const dom=new JSDOM(html,{url:'http://192.168.4.1:8080/?code=123456',runScripts:'dangerously',beforeParse(w){
  const nativeTimeout=w.setTimeout.bind(w);w.setTimeout=(callback,ms)=>ms===2600?(revealTimer=callback,revealStarts++,999):nativeTimeout(callback,ms);
  w.localStorage.setItem('drwho.v1',JSON.stringify({token:'test-token'}));w.setInterval=()=>0;
  w.fetch=async(path,options)=>{
   if(path==='/api/info')return {ok:true,status:200,json:async()=>({appVersion:'0.15.0',protocolVersion:9,sessionId:'session'})};
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
  check(!$('playerArea').hidden&&!$('playPanel').hidden&&$('tabPlay').getAttribute('aria-selected')==='true','own turn opens play tab');
  check($('turnNotice').hidden&&!$('roll').disabled,'accepted own turn removes redundant banner and enables the die');
  $('tabProfile').click();check(!$('profilePanel').hidden&&$('playPanel').hidden&&$('myProfile').textContent.includes('Der Doctor'),'profile tab exposes own dossier during own turn');
  $('tabHistory').click();check(!$('historyPanel').hidden&&$('historyDetails').open,'history tab opens collapsible event log');
  $('historyDetails').open=false;w.render(copy(active));check(!$('historyPanel').hidden&&!$('historyDetails').open,'polling preserves selected tab and collapsed history');
  $('tabHistory').dispatchEvent(new w.KeyboardEvent('keydown',{key:'Home',bubbles:true}));
  check(!$('playPanel').hidden&&w.document.activeElement===$('tabPlay'),'tab keyboard navigation selects and focuses play');
  check(!$('roll').disabled,'active partner may act');
  check($('controls').hidden&&$('turnChoices').hidden,'first roll required before moving or acting');
  const waiting=copy(active);waiting.revision=4;waiting.current='B';w.render(waiting);
  check(!$('idle').hidden&&$('game').hidden,'uninvolved partner gets waiting status');
  check(!$('profilePanel').hidden&&$('myProfile').textContent.includes('Der Doctor'),'waiting partner sees own dossier by default');
  check($('roll').disabled,'uninvolved controls disabled');
  check($('turnNoticeTitle').textContent==='Bob ist am Zug.','notice identifies active partner');
  $('tabHistory').click();w.render(copy(waiting));check(!$('historyPanel').hidden,'waiting player may inspect history without polling forcing a tab');
  const enemy=copy(waiting);enemy.revision=5;enemy.current=null;enemy.phase='enemies';enemy.activeEnemy='weeping-angel';enemy.helperId='B';w.render(enemy);
  check(!$('idle').hidden&&$('helper').hidden,'unassigned player waits in enemy phase');
  enemy.revision=6;enemy.helperId='A';w.render(enemy);
  check(!$('helper').hidden&&$('idle').hidden,'selected helper sees movement task');
  check(!$('playPanel').hidden&&$('turnNoticeTitle').textContent.includes('Gegnerfigur bewegen'),'new helper task opens play tab and notice');
  next=copy(enemy);next.revision=7;next.helperId='';next.movementDone=true;
  const count=requests.length;$('confirmMovement').click();$('confirmMovement').click();await tick();
  check(requests.length===count+1,'double tap sends one movement confirmation');
  check(requests.at(-1).body.get('action')==='confirmMovement','helper confirms distinct action');
  const attack=copy(next);attack.revision=8;attack.attack={id:'attack-1',enemyId:'weeping-angel',targetId:'A',text:'Für 2 Runden gebannt. <img src=x onerror=alert(1)>',pending:true};w.render(attack);
  check(!$('battle').hidden&&$('idle').hidden,'attacked player gets active battle view');
  check(!$('playPanel').hidden&&$('turnNoticeTitle').textContent==='Du wirst angegriffen','attack takes priority over waiting profile');
  check($('defenderProfile').textContent.includes('Der Doctor'),'own dossier visible');
  check($('attackerProfile').textContent.includes('Weinender Engel'),'attacker dossier visible');
  check($('outcome').textContent===attack.attack.text&&!$('outcome').querySelector('img'),'outcome rendered as safe exact text');
  next=copy(attack);next.revision=9;next.attack.pending=false;$('ackAttack').click();await tick();
  check(requests.at(-1).body.get('action')==='ackAttack','target can acknowledge attack');
  check(!$('idle').hidden&&$('battle').hidden,'acknowledged target returns to TARDIS');
  const other=copy(attack);other.revision=10;other.attack.targetId='B';w.render(other);
  check($('battle').hidden&&!$('idle').hidden,'attack on someone else leaves player idle');
  w.render(active);check($('game').hidden,'late stale snapshot cannot reactivate wrong view');
  const choice=copy(active);choice.revision=11;choice.turn={accepted:true,stage:'choice',roll:5,remaining:5};w.render(choice);
  check(!$('turnChoices').hidden&&$('roll').disabled&&$('controls').hidden,'five exposes exactly movement/action choice');
  check($('moveChoiceButton').textContent==='5 Felder ziehen','movement option uses actual die count');
  const moving=copy(choice);moving.revision=12;moving.turn={accepted:true,stage:'move',roll:5,remaining:3};w.render(moving);
  check(!$('controls').hidden&&$('turnChoices').hidden,'choosing movement closes action choice');check($('turnConsole').classList.contains('with-compass')&&$('controls').parentElement===$('roll').closest('.turn-console'),'die and compass share compact row');check($('turnMapField').textContent==='A1','personal map identifies own current field');
  check($('turnPrompt').textContent.includes('3 von 5'),'remaining distance shown');
  const cube=$('roll').querySelector('.dice-cube'),fixedFaces=cube.innerHTML;
  const revealing=copy(choice);revealing.revision=13;revealing.rollEvent={id:'roll-1',playerId:'A',value:5};revealing.log=['Alice würfelt 5'];w.render(revealing);
  check($('roll').classList.contains('rolling')&&!$('roll').getAttribute('aria-label').includes('5'),'result stays hidden while dice tumble');
  check($('roll').querySelector('.dice-cube')===cube&&cube.innerHTML===fixedFaces,'animation uses existing cube with fixed numbered faces');
  check([...cube.children].map(face=>face.dataset.value).sort().join('')==='123456','all six numbered faces exist throughout animation');
  check($('roll').dataset.face==='5','animation targets committed value before reveal');
  check($('turnChoices').hidden&&$('moveChoiceButton').disabled&&!$('log').textContent.includes('würfelt 5'),'movement, action and result log wait for reveal');
  w.render(revealing);check(revealStarts===1,'polling same saved die does not restart animation');
  revealTimer();check($('rollResult').textContent==='Dein Wurf: 5'&&!$('turnChoices').hidden&&!$('moveChoiceButton').disabled,'after 2.6 seconds saved result and choices become available');
  check($('roll').querySelector('.dice-cube')===cube&&cube.innerHTML===fixedFaces,'reveal never replaces die or changes pips');
  check(JSON.parse(w.localStorage.getItem('drwho.v1')).revealedRoll==='session:roll-1','completed reveal remembered across reconnect');
  w.render(revealing);check(revealStarts===1,'same event does not animate again after reveal');
  const second=copy(revealing);second.revision=14;second.rollEvent.id='roll-2';w.render(second);
  check(revealStarts===2&&$('roll').classList.contains('rolling'),'a new event with same die value animates again');revealTimer();
  const goWaiting=copy(active);goWaiting.revision=15;goWaiting.round=2;goWaiting.turn={accepted:false,stage:'roll',roll:0,remaining:0};
  const goCount=requests.length;w.render(goWaiting);
  check(!$('turnDialog').hidden&&$('yourMoveTitle').textContent==='Your move...'&&$('goButton').textContent==='Go','new own turn opens exact Your move... / Go popup');
  check($('roll').disabled&&$('playerArea').inert&&w.document.activeElement===$('goButton'),'Go popup blocks and focuses before any roll');
  w.document.dispatchEvent(new w.KeyboardEvent('keydown',{key:'Escape',bubbles:true,cancelable:true}));check(!$('turnDialog').hidden,'Go cannot be dismissed without beginning turn');
  w.render(copy(goWaiting));check(requests.length===goCount,'polling Go prompt never begins turn automatically');
  fail=true;$('goButton').click();await tick();const goRequest=requests.at(-1).body.get('requestId');
  check(requests.at(-1).body.get('action')==='beginTurn'&&!$('turnDialog').hidden,'Go is an authenticated saved command');
  check(!$('goButton').disabled&&$('goStatus').textContent.includes('Bestätigung fehlt'),'unconfirmed Go can be retried from inside popup');
  next=copy(goWaiting);next.revision=16;next.turn.accepted=true;fail=false;$('goButton').click();await tick();
  check(requests.at(-1).body.get('requestId')===goRequest,'Go retry reuses request identity');
  check($('turnDialog').hidden&&!$('playerArea').inert&&!$('roll').disabled,'confirmed Go releases own turn controls');
  w.render(copy(next));check($('turnDialog').hidden,'accepted turn never reopens Go on polling');
  const story=copy(next);story.revision=17;story.story=[{id:'story-1',timestampMillis:1774024440000,dateTime:'20.03.2026 17:34 Uhr',speaker:'Dr WHO',playerName:'Alice',text:'Ich gehe von Feld A1 zu Feld A2. <img src=x onerror=alert(1)>',direction:'S'}];w.render(story);
  $('tabHistory').click();const text=$('log').textContent;
  check($('tabHistory').textContent==='Story'&&$('historyDetails').querySelector('summary').textContent==='Story','history renamed to Story in tab and foldout');
  check(text.startsWith('20.03.2026 17:34 Uhr - Dr WHO:')&&text.includes('Ich gehe von Feld A1'),'Story starts with host date/time and speaker');
  check(!$('log').querySelector('img')&&text.includes('<img src=x'),'Story speech uses safe text rendering');
  w.render(copy(story));check($('log').textContent===text,'Story time and speech never change on repeated rendering');check($('log').firstChild.classList.contains('story-bubble')&&$('log').firstChild.classList.contains('own-story'),'own story is a speech bubble');const history=copy(story);history.story.push({...story.story[0],id:'story-2',text:'Neuester Eintrag'});w.render(history);check($('log').children.length===2&&$('log').firstChild.textContent.includes('Neuester Eintrag')&&Array.from($('log').children).every(e=>!e.hidden),'all Story bubbles scroll in newest-first order without paging');
  const aiming=copy(story);aiming.revision=18;aiming.turn={accepted:true,stage:'choice',roll:6,remaining:6};aiming.characters[0].actionNeedsDirection=true;w.render(aiming);$('tabPlay').click();
  const beforeAim=requests.length;$('actionChoiceButton').click();
  check(!$('directionDialog').hidden&&w.document.querySelectorAll('[data-action-dir]').length===4,'directional action opens four compass buttons');
  check(requests.length===beforeAim&&$('playerArea').inert,'opening direction picker sends no action and blocks background');
  $('cancelDirection').click();check($('directionDialog').hidden&&requests.length===beforeAim&&!$('turnChoices').hidden,'cancel consumes no action or movement');
  $('actionChoiceButton').click();w.document.dispatchEvent(new w.KeyboardEvent('keydown',{key:'Escape',bubbles:true,cancelable:true}));
  check($('directionDialog').hidden&&requests.length===beforeAim,'Escape safely cancels unsubmitted direction');
  $('actionChoiceButton').click();fail=true;w.document.querySelector('[data-action-dir="E"]').click();await tick();const actionRequest=requests.at(-1).body.get('requestId');
  check(requests.at(-1).body.get('action')==='action'&&requests.at(-1).body.get('direction')==='E','selected action direction goes to host');
  check(!$('directionDialog').hidden&&!$('actionRetry').hidden&&$('cancelDirection').disabled&&[...w.document.querySelectorAll('[data-action-dir]')].every(b=>b.disabled),'unconfirmed directed action stays retryable without changing its direction');
  next=copy(aiming);next.revision=19;next.current='B';next.turn={accepted:false,stage:'roll',roll:0,remaining:0};fail=false;$('actionRetry').click();await tick();
  check(requests.at(-1).body.get('requestId')===actionRequest&&requests.at(-1).body.get('direction')==='E','directed action retry preserves request and original direction');
  check($('directionDialog').hidden&&$('turnDialog').hidden&&!$('profilePanel').hidden,'next partner Go does not appear on uninvolved client');
  const archived=copy(next);archived.revision=20;archived.story=[{id:'old',timestampMillis:0,dateTime:'',speaker:'Archiv',text:'Alte Aussage ohne gespeicherte Zeit.'}];w.render(archived);
  check($('log').textContent.startsWith('Zeit nicht erfasst - Archiv:'),'undated old saves do not get invented timestamps');
  const lastChoice=copy(second);lastChoice.revision=21;lastChoice.round=3;w.render(lastChoice);
  networkState=true;await w.poll();check($('join').hidden&&JSON.parse(w.localStorage.getItem('drwho.v1')).token==='test-token','temporary network failure retains valid login');networkState=false;w.render(lastChoice);
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
   if(path==='/api/info')return {ok:true,status:200,json:async()=>({appVersion:'0.15.0',protocolVersion:9,sessionId:'session'})};
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
    if(path==='/api/info')return {ok:true,status:200,json:async()=>({appVersion:'0.15.0',protocolVersion:9,sessionId:'old-session'})};
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
   check(!$('join').hidden&&['playerArea','game','selection','idle','helper','battle'].every(id=>$(id).hidden),'stale token never traps client on old game screen');
   const stored=JSON.parse(w.localStorage.getItem('drwho.v1'));
   check(stored.token===''&&stored.pending===null&&stored.join.requestId===originalJoin.requestId,'invalid access cleared while original join proof remains');
   check($('name').value==='Alice'&&$('code').value===(newParty?'654321':'123456'),'name retained and current QR code available');
   check(posts.length===0,'expired token does not silently join or occupy another seat');
   await recoveryPoll();check($('joinOccupancy').textContent.startsWith((newParty?'0':'1')+' von 4'),'fresh lobby occupancy available after rejection');
   $('join').dispatchEvent(new w.Event('submit',{bubbles:true,cancelable:true}));await tick();
   check(posts.length===1&&!posts[0].headers.Authorization,'explicit new login sends no rejected bearer token');
   check(posts[0].body.get('requestId')===originalJoin.requestId,'same name keeps original proof without an extra code');
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
    if(path==='/api/info')return {ok:true,status:200,json:async()=>({appVersion:mismatch==='version'?'0.10.3':'0.15.0',protocolVersion:mismatch==='protocol'?10:9,sessionId:mismatch==='party'?'new-party':'session'})};
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
   check(['playerArea','game','selection','idle','helper','battle'].every(id=>$(id).hidden),'mismatch never leaves an interactive old view visible');
  }finally{guarded.window.close()}
 }
 // Roommaster negotiation and editor on an authenticated client.
 {
  let current=copy(base),offline=false,bad=false;const posts=[];current.started=true;current.current=null;current.turn.accepted=false;
  current.setup={stage:'selecting',id:'selection-one',roomMaster:'',candidate:'',forced:false,saved:false,legacy:false};current.board={size:20,tiles:[]};current.players.forEach(p=>{p.x=-1;p.y=-1});
  const editor=new JSDOM(html,{url:'http://192.168.4.1:8080/',runScripts:'dangerously',beforeParse(w){
   w.setInterval=()=>0;w.localStorage.setItem('drwho.v1',JSON.stringify({token:'test-token',sessionId:'session'}));
   w.fetch=async(path,options)=>{if(path==='/api/info')return {ok:true,status:200,json:async()=>({appVersion:'0.15.0',protocolVersion:9,sessionId:'session'})};
    if(options.method==='POST'){posts.push(new URLSearchParams(String(options.body)));if(offline)throw new Error('offline');if(bad)return {ok:false,status:400,json:async()=>({error:'Dort liegt bereits eine Kachel.'})};}
    return {ok:true,status:200,json:async()=>copy(current)};};
  }});
  try{
   const w=editor.window,$=id=>w.document.getElementById(id);await tick();
   check(!$('setupArea').hidden&&$('selection').hidden&&$('playerArea').hidden,'preparation replaces lobby and blocks normal game');
   check($('setupTitle').textContent==='der Raummeister wird gerade ausgewählt...'&&$('turnDialog').hidden,'selection has exact text and no premature Go');
   current.revision++;current.setup.stage='offering';current.setup.candidate='B';w.render(copy(current));check($('roomDialog').hidden,'noncandidate sees no roommaster question');
   current.revision++;current.setup.candidate='A';w.render(copy(current));check(!$('roomDialog').hidden&&$('setupArea').inert&&w.document.activeElement===$('roomYes'),'only candidate gets modal with focus and inert background');
   w.document.dispatchEvent(new w.KeyboardEvent('keydown',{key:'Escape',bubbles:true,cancelable:true}));check(!$('roomDialog').hidden,'roommaster question requires explicit yes or no');
   offline=true;$('roomNo').click();await tick();const noId=posts.at(-1).get('requestId');check($('roomYes').disabled&&!$('roomNo').disabled,'lost no confirmation only permits same answer');
   current.revision++;current.setup.candidate='B';offline=false;$('roomNo').click();await tick();check(posts.at(-1).get('requestId')===noId&&$('roomDialog').hidden,'decline retries same ID and closes only on host state');
   current.revision++;current.setup.stage='building';current.setup.candidate='';current.setup.roomMaster='B';w.render(copy(current));check($('boardEditor').hidden&&$('setupTitle').textContent.includes('Bob'),'nonmaster waits for the named builder');
   current.revision++;current.setup.roomMaster='A';current.setup.forced=true;w.render(copy(current));check(!$('forcedDialog').hidden&&$('forcedDialog').textContent.includes('Alle haben abgelehnt'),'forced player sees portal decision popup');
   $('openBoard').click();$('northConfirm').click();check($('forcedDialog').hidden&&!$('boardEditor').hidden,'forced roommaster opens editor explicitly');w.render(copy(current));check($('forcedDialog').hidden,'polling does not repeat acknowledged forced popup');
   check($('tilePalette').children.length===11&&$('editorGrid').querySelectorAll('.board-empty').length===400,'eleven tile types and exactly twenty by twenty empty locations');
   $('tilePalette').querySelector('[data-kind="corner"]').click();check($('boardStatus').textContent.includes('Eckwände gewählt'),'palette selection describes drag placement');
   current.revision++;current.board.tiles=[{x:0,y:0,kind:'corner',rotation:0}];current.players[0].x=0;current.players[0].y=0;
   const beforeEmptyTap=posts.length;$('editorGrid').querySelector('[data-cell="0,0"]').click();await tick();check(posts.length===beforeEmptyTap,'empty tap cannot place selected tile');w.render(copy(current));
   check($('editorGrid').querySelectorAll('[data-tile]').length===1&&$('boardLegend').textContent.includes('Alice · A1'),'committed tile and automatic start field visible');
   current.revision++;current.board.tiles[0].rotation=1;$('editorGrid').querySelector('[data-tile]').click();await tick();check(posts.at(-1).get('action')==='tileRotate'&&posts.at(-1).get('direction')==='0,0','tile tap requests exact 90 degree turn');
   const displayed=$('editorGrid').querySelector('[data-tile]');check(displayed.querySelector('g').getAttribute('transform')==='rotate(90 50 50)','rotation uses saved orientation');
   w.render(copy(current));check(displayed===$('editorGrid').querySelector('[data-tile]'),'unchanged polling preserves focused tile DOM');
   const realTouchClock=w.performance.now.bind(w.performance),touchTime=realTouchClock();w.performance.now=()=>touchTime;
   const beforeTouch=posts.length;current.revision++;current.board.tiles[0].rotation=2;const touchTile=$('editorGrid').querySelector('[data-tile]');
   for(const type of ['pointerdown','pointerup']){const event=new w.MouseEvent(type,{bubbles:true,clientX:10,clientY:10});Object.defineProperties(event,{pointerType:{value:'touch'},pointerId:{value:9}});touchTile.dispatchEvent(event)}await tick();
   check(posts.length===beforeTouch+1&&posts.at(-1).get('action')==='tileRotate','touch pointer tap rotates without waiting for compatibility click');
   w.performance.now=()=>touchTime+100;$('editorGrid').querySelector('[data-tile]').dispatchEvent(new w.MouseEvent('click',{bubbles:true,detail:1}));await tick();check(posts.length===beforeTouch+1,'delayed compatibility click cannot rotate twice');w.performance.now=realTouchClock;

   current.revision++;current.board.tiles.push({x:4,y:4,kind:'portal',rotation:1});w.render(copy(current));const portal=$('editorGrid').querySelector('[data-cell="4,4"]');check(portal.style.height.includes('* 2')&&!$('editorGrid').querySelector('[data-cell="4,5"]'),'portal visually occupies two cells and removes covered empty target');
   $('spawnMode').click();check(!$('spawnList').hidden&&$('boardStatus').textContent.includes('Startfeld'),'explicit mode avoids confusing start placement with tile rotation');
   current.revision++;current.players[0].x=4;current.players[0].y=4;$('editorGrid').querySelector('[data-cell="4,4"]').click();await tick();check(posts.at(-1).get('action')==='setSpawn'&&posts.at(-1).get('direction')==='A,4,4','start marker set on selected player cell');
   $('clearPalette').click();const existing=$('editorGrid').querySelector('[data-cell="0,0"]');current.revision++;current.board.tiles[0].rotation=2;existing.click();await tick();current.revision++;current.board.tiles.shift();$('boardTrash').click();await tick();check(posts.at(-1).get('action')==='tileRemove'&&posts.at(-1).get('direction')==='0,0','trash button provides accessible removal alternative');
   current.revision++;current.setup.forced=false;w.render(copy(current));offline=true;w.command('tilePlace','2,2,well,0');await tick();const placeId=posts.at(-1).get('requestId');check(!$('retry').hidden&&$('saveBoard').disabled,'unconfirmed placement blocks saving and allows retry');
   offline=false;current.revision++;current.board.tiles.push({x:2,y:2,kind:'well',rotation:0});$('retry').click();await tick();check(posts.at(-1).get('requestId')===placeId&&$('editorGrid').querySelector('[data-cell="2,2"][data-tile]'),'retry confirms same tile without duplicate placement');
   bad=true;$('editorGrid').querySelector('[data-cell="2,2"]').click();await tick();check($('boardStatus').textContent.includes('bereits eine Kachel')&&$('retry').hidden,'host rejection visible without speculative board change');bad=false;
   current.revision++;current.setup.stage='launching';current.setup.saved=true;$('saveBoard').click();await tick();check(posts.at(-1).get('action')==='boardSave'&&$('boardEditor').hidden&&$('setupTitle').textContent.includes('Abenteuer beginnt'),'save starts launch screen rather than immediate die controls');
   current.revision++;current.setup.stage='play';current.current='A';w.render(copy(current));check($('setupArea').hidden&&!$('turnDialog').hidden&&!$('gameMapViewport').hidden&&$('grid').hidden,'saved real map and Go appear only when setup completes');
   check($('gameBoard').querySelectorAll('.board-tile').length===2,'game uses persisted sparse map instead of inventing missing fields');
  }finally{editor.window.close()}
 }

 console.log('PASS: '+checks+' total player-page behavior checks including roommaster and editor');
}
main().catch(error=>{console.error(error);process.exitCode=1});
