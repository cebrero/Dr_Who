const {JSDOM}=require('jsdom');
const fs=require('node:fs'),assert=require('node:assert/strict');
let checks=0;const check=(value,message)=>{assert.ok(value,message);checks++};
const tick=()=>new Promise(resolve=>setImmediate(resolve));
const html=fs.readFileSync('app/src/main/assets/player.html','utf8');
function state(stage='lobby'){
 return {sessionId:'session',revision:1,started:stage!=='lobby',ready:true,selectedCount:1,round:1,lastRoll:0,turn:{accepted:false,stage:'roll',roll:0,remaining:0},you:'P',current:stage==='play'?'P':null,phase:'players',helperId:'',movementDone:false,activeEnemy:'',attack:{id:'',enemyId:'',targetId:'',text:'',pending:false},players:[{id:'P',name:'Alice',characterId:'doctor',x:0,y:0}],characters:[],enemies:[],log:[],story:[],setup:{stage,id:'setup',roomMaster:stage==='building'?'P':'',candidate:stage==='offering'?'P':'',forced:false,saved:stage==='play',legacy:false},board:{size:20,tiles:[{x:0,y:0,kind:'floor',rotation:0}]}};
}
function harness(options={}){
 const posts=[];let current=state(options.stage),expired=false,hold=null,network=false,version='0.8.1',held=false;
 if(options.forced)current.setup.forced=true;
 const dom=new JSDOM(html,{url:'http://192.168.4.1:8080/',runScripts:'dangerously',beforeParse(w){
  w.setInterval=()=>0;if(options.stage)w.localStorage.setItem('drwho.v1',JSON.stringify({token:'old-token',sessionId:'session',join:{name:'Alice',code:'123456',requestId:'original-proof-123456'}}));
  w.fetch=async(path,request)=>{
   if(path==='/api/info')return {ok:true,status:200,json:async()=>({appVersion:version,protocolVersion:3,sessionId:'session'})};
   if(path==='/api/lobby')return {ok:true,status:200,json:async()=>({occupied:0,capacity:4})};
   if(path==='/api/state')return expired?{ok:false,status:401,json:async()=>({error:'Bitte anmelden.'})}:{ok:true,status:200,json:async()=>current};
   assert.equal(path,'/api/join');const data=new URLSearchParams(request.body);posts.push(data);
   if(network)throw new Error('Verbindung unterbrochen.');
   if(held)await new Promise(resolve=>hold=resolve);
   if(data.get('code')!=='123456')return {ok:false,status:400,json:async()=>({error:'Portalcode falsch.',errorCode:'invalid_portal_code'})};
   current=state();expired=false;return {ok:true,status:200,json:async()=>({token:'accepted-token',sessionId:'session'})};
  };
 }});
 return {dom,posts,get w(){return dom.window},get $(){return id=>dom.window.document.getElementById(id)},expire(){expired=true},hold(){held=true},release(){held=false;hold()},network(value){network=value},mismatch(){version='0.8.2';dom.window.eval('portalCheckedAt=0')},submit(){dom.window.document.getElementById('join').dispatchEvent(new dom.window.Event('submit',{bubbles:true,cancelable:true}))}};
}
(async()=>{
 const h=harness();try{
  await tick();h.$('name').value='Alice';h.$('code').value='123';h.submit();await tick();
  check(h.posts.length===0&&!h.$('joinError').hidden,'invalid code shape stays local and visibly rejected');
  check(h.$('code').getAttribute('aria-invalid')==='true'&&h.w.document.activeElement===h.$('code'),'invalid code field marked and focused');
  h.$('code').value='654321';h.hold();h.submit();await tick();
  check(h.posts.length===1&&h.$('code').disabled&&h.$('name').disabled&&h.$('joinButton').disabled,'one pending server check disables duplicate edits/submissions');
  h.submit();await h.w.poll();check(h.posts.length===1,'pending join never produces another submission');
  h.release();await tick();
  check(!h.$('join').hidden&&!h.$('joinError').hidden&&h.$('joinError').textContent.includes('falsch'),'host-invalid code remains on editable join form');
  check(!h.$('code').disabled&&!h.$('joinButton').disabled&&h.$('name').value==='Alice','rejection restores controls and preserves name');
  check(h.w.document.activeElement===h.$('code')&&h.$('code').selectionStart===0&&h.$('code').selectionEnd===6,'wrong code selected for immediate replacement');
  check(!JSON.parse(h.w.localStorage.getItem('drwho.v1')).token,'wrong code never receives credentials');
  await h.w.poll();check(!h.$('joinError').hidden&&h.$('code').value==='654321','lobby polling preserves failed code and inline error');
  const failed=h.posts[0].get('requestId');h.$('code').value='123456';h.$('code').dispatchEvent(new h.w.Event('input'));
  check(h.$('joinError').hidden&&!h.$('code').hasAttribute('aria-invalid'),'editing clears obsolete invalid state');
  h.submit();await tick();
  check(h.posts.length===2&&h.posts[1].get('requestId')!==failed,'corrected code creates a fresh request');
  check(h.$('join').hidden&&!h.$('selection').hidden&&JSON.parse(h.w.localStorage.getItem('drwho.v1')).token==='accepted-token','correct code proceeds to real selection');
 }finally{h.dom.window.close()}
 for(const [stage,forced]of[['offering',false],['building',false],['building',true],['play',false],['direction',false]]){
  const h=harness({stage:stage==='direction'?'play':stage,forced});try{
   await tick();if(stage==='direction'){h.w.eval("state.turn.accepted=true;state.turn.stage='choice';directionPicking=true;syncModals()");check(!h.$('directionDialog').hidden,'direction popup fixture visible')}
   h.expire();await h.w.poll();
   check(!h.$('join').hidden&&h.$('setupArea').hidden,'expired '+stage+' scene returns to join');
   check(!h.w.document.body.classList.contains('building')&&!h.w.document.body.classList.contains('editing-board'),'expired '+stage+' scene releases fixed editor layout');
   check(['roomDialog','forcedDialog','turnDialog','directionDialog'].every(id=>h.$(id).hidden)&&!h.$('join').inert,'expired '+stage+' scene clears every modal and inert flag');
   check(h.$('name').value==='Alice'&&h.w.document.activeElement===h.$('code'),'expired '+stage+' scene preserves player name and focuses code');
   h.$('code').value='654321';h.submit();await tick();check(!h.$('joinError').hidden&&!h.$('code').disabled,'expired '+stage+' scene can reject and correct another code');
   h.$('code').value='123456';h.submit();await tick();check(h.posts.at(-1).get('requestId')==='original-proof-123456'&&h.$('join').hidden,'expired '+stage+' recovers original registration proof after a wrong attempt');
  }finally{h.dom.window.close()}
 }
 const recovery=harness();try{
  await tick();recovery.$('name').value='Alice';recovery.$('code').value='123456';recovery.network(true);recovery.submit();await tick();
  check(!recovery.$('joinError').hidden&&!recovery.$('code').hasAttribute('aria-invalid'),'network error does not falsely label code invalid');const proof=recovery.posts[0].get('requestId');
  recovery.network(false);recovery.submit();await tick();check(recovery.posts[1].get('requestId')===proof&&recovery.$('join').hidden,'same-name same-code retry keeps proof and can join');
 }finally{recovery.dom.window.close()}
 const mismatch=harness({stage:'building'});try{
  await tick();mismatch.mismatch();await mismatch.w.poll();check(mismatch.$('setupArea').hidden&&!mismatch.$('reloadPage').hidden&&!mismatch.$('reloadPage').inert,'version mismatch also releases old editor for reload');
 }finally{mismatch.dom.window.close()}
 console.log('PASS: '+checks+' portal-code and join recovery checks');
})().catch(error=>{console.error(error);process.exitCode=1});
