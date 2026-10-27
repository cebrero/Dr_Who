const {JSDOM}=require('jsdom'),fs=require('node:fs'),assert=require('node:assert/strict');
let html=fs.readFileSync('app/src/main/assets/player.html','utf8');for(const file of ['room-library.js','board-art.js','character-dossiers.js'])html=html.replace('<script src="/'+file+'"></script>','<script>'+fs.readFileSync('app/src/main/assets/'+file,'utf8')+'</script>');
let count=0;const check=(v,m)=>{assert(v,m);count++},tick=()=>new Promise(r=>setImmediate(r)),clone=o=>JSON.parse(JSON.stringify(o));
let s={sessionId:'votes',revision:2,started:true,ready:true,selectedCount:2,round:1,lastRoll:0,you:'A',current:null,phase:'players',helperId:'',activeEnemy:'',attack:{pending:false},turn:{accepted:false,stage:'roll',roll:0,remaining:0},setup:{stage:'voting',id:'setup',roomMaster:'',candidate:'',forced:false,saved:false,legacy:false,dossierRequired:true,readBy:[]},vote:{choice:0,objective:0,tied:false,votedBy:[]},board:{size:20,subdivisions:2,tiles:[]},players:[{id:'A',name:'Alice',characterId:'doctor',x:0,y:0},{id:'B',name:'Bob',characterId:'dalek',x:1,y:0}],characters:[{id:'doctor',name:'Doctor',portrait:'/portraits/doctor.jpg',endurance:'Noch festzulegen',strength:'Noch festzulegen',abilities:[],owner:'A'}],enemies:[],log:[],bag:[]};
let posts=[],offline=true,ackOffline=true,timers=[],acks=[];
const dom=new JSDOM(html,{url:'http://192.168.4.1',runScripts:'dangerously',beforeParse(w){w.setInterval=()=>0;const timer=w.setTimeout.bind(w);w.setTimeout=(fn,ms,...args)=>ms===2600?(timers.push(fn),timers.length):timer(fn,ms,...args);w.localStorage.setItem('drwho.v1',JSON.stringify({token:'a',sessionId:'votes'}));w.fetch=async(path,opts)=>{
 if(path==='/api/info')return{ok:true,json:async()=>({appVersion:'0.13.0',protocolVersion:8,sessionId:'votes'})};
 if(path==='/api/command'){const f=new URLSearchParams(opts.body);if(f.get('action')==='ackRoll'){acks.push(f);if(ackOffline)throw Error('reply lost');s.rollEvent.presented=true;return{ok:true,json:async()=>clone(s)}}posts.push(f);if(offline)throw Error('offline');if(f.get('action')==='vote'){s.vote.choice=Number(f.get('direction'))||2;s.vote.votedBy=['A'];s.revision++}}
 return{ok:true,json:async()=>clone(s)};
}}});
(async()=>{const w=dom.window,d=w.document,$=id=>d.getElementById(id);try{
 await tick();await tick();check(!$('voteDialog').hidden&&$('setupArea').inert,'vote popup precedes roommaster and locks background');check($('voteOk').disabled&&!$('voteRandom').disabled,'explicit choice required for OK, random available');
 const option=d.querySelector('input[value="2"]');option.checked=true;option.dispatchEvent(new w.Event('change'));w.render(clone(s));check(option.checked&&!$('voteOk').disabled,'draft vote stable through polling');
 d.dispatchEvent(new w.KeyboardEvent('keydown',{key:'Escape',bubbles:true}));check(!$('voteDialog').hidden,'required vote is not silently dismissed');
 $('voteOk').click();await tick();const id=posts.at(-1).get('requestId');check(posts.at(-1).get('direction')==='2'&&!$('voteOk').disabled&&$('voteRandom').disabled,'lost reply freezes chosen vote with retry');
 offline=false;$('voteOk').click();await tick();await tick();check(posts.at(-1).get('requestId')===id&&$('voteDialog').hidden,'same vote retry keeps request ID and waiting player leaves modal');check($('setupSub').textContent.includes('gespeichert'),'confirmed voter waits for others');
 s.setup.stage='voteResult';s.vote.objective=2;s.vote.tied=true;s.vote.votedBy=['A','B'];s.revision++;w.render(clone(s));check($('setupSub').textContent.includes('Schlüssel')&&$('setupSub').textContent.includes('Gleichstand'),'result displays chosen goal and tie draw');
 s.setup.stage='selecting';s.revision++;w.render(clone(s));check($('setupTitle').textContent.includes('Raummeister'),'roommaster search follows vote result');
 s.setup.stage='play';s.current='A';s.turn={accepted:true,stage:'move',roll:4,remaining:4};s.rollEvent={id:'roll-one',playerId:'A',value:4,presented:false};s.revision++;w.render(clone(s));
 check(w.eval('rolling')&&acks.length===0,'no portal ACK before client animation ends');timers.shift()();await tick();check(!w.eval('rolling')&&acks.length===1,'animation completion sends portal ACK');check(w.eval('pending')===null,'ACK retry never blocks movement as a gameplay action');
 const ackId=acks[0].get('requestId');await w.poll();await tick();check(acks.at(-1).get('requestId')===ackId&&acks.length===2,'lost ACK retains original receipt ID');
 ackOffline=false;await w.poll();await tick();check(s.rollEvent.presented&&$('controls').hidden===false,'ACK confirms without hiding movement console');const total=acks.length;await w.poll();check(acks.length===total,'confirmed ACK does not replay at every poll');
 // A pending vote is also resolved by host state after a lost successful response.
 s.setup.stage='voting';s.current=null;s.vote={choice:0,objective:0,votedBy:[]};s.revision++;w.render(clone(s));$('voteRandom').click();await tick();check(posts.at(-1).get('direction')==='random','Zufall submits a host-side random own vote');
 console.log('PASS: '+count+' voting popup and client-first dice checks');
}finally{w.close()}})().catch(e=>{console.error(e);process.exitCode=1});
