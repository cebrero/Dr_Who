const {JSDOM}=require('jsdom'),fs=require('node:fs'),assert=require('node:assert/strict');
let checks=0;const check=(v,m)=>{assert(v,m);checks++},tick=()=>new Promise(r=>setImmediate(r)),clone=o=>JSON.parse(JSON.stringify(o));
let html=fs.readFileSync('app/src/main/assets/player.html','utf8');
for(const file of ['room-library.js','board-art.js','character-dossiers.js'])html=html.replace('<script src="/'+file+'"></script>','<script>'+fs.readFileSync('app/src/main/assets/'+file,'utf8')+'</script>');
const profiles=[['doctor','Der Doctor'],['dalek','Dalek'],['weeping-angel','Weinender Engel']].map(([id,name],i)=>({id,name,portrait:'/portraits/'+id+'.jpg',description:'Charakter',endurance:'Noch festzulegen',strength:'Noch festzulegen',abilities:['Noch offen'],actionNeedsDirection:true,owner:['A','B','C'][i],enemy:false}));
let current={sessionId:'briefing',revision:1,started:true,ready:true,selectedCount:3,round:1,lastRoll:0,you:'A',current:null,phase:'players',helperId:'',activeEnemy:'',attack:{pending:false},turn:{accepted:false,stage:'roll',roll:0,remaining:0},setup:{stage:'building',id:'setup',roomMaster:'B',candidate:'',forced:false,saved:false,legacy:false,dossierRequired:true,readBy:[]},board:{size:20,subdivisions:2,tiles:[{x:0,y:0,kind:'floor',rotation:0}]},players:[{id:'A',name:'Alice',characterId:'doctor',x:0,y:0},{id:'B',name:'Bob',characterId:'dalek',x:1,y:0},{id:'C',name:'Cora',characterId:'weeping-angel',x:0,y:1}],characters:profiles,enemies:[],log:[],bag:[]};
let posts=[],offline=false,lostReply=false,expired=false;
function page(){return new JSDOM(html,{url:'http://192.168.4.1/',runScripts:'dangerously',beforeParse(w){w.setInterval=()=>0;w.localStorage.setItem('drwho.v1',JSON.stringify({token:'token',sessionId:'briefing',northSetup:'briefing:setup'}));w.fetch=async(path,opts)=>{
 if(expired&&path==='/api/state')return{ok:false,status:401,json:async()=>({error:'Entfernt'})};
 if(path==='/api/info')return{ok:true,json:async()=>({appVersion:'0.14.0',protocolVersion:8,sessionId:'briefing'})};
 if(path==='/api/command'){
  const form=new URLSearchParams(opts.body);posts.push(form);if(offline)throw Error('offline');
  if(form.get('action')==='readDossier'){if(!current.setup.readBy.includes(current.you))current.setup.readBy.push(current.you);current.revision++;if(lostReply){lostReply=false;throw Error('reply lost')}}
 }
 return{ok:true,json:async()=>clone(current)};
}}})}
(async()=>{const dom=page(),w=dom.window,d=w.document,$=id=>d.getElementById(id);try{
 await tick();await tick();
 check(!$('setupDossier').hidden&&$('boardEditor').hidden,'other player reads during construction');
 check($('setupWait').hidden&&$('playerArea').hidden,'reading replaces plain waiting screen');
 check($('dossierScroll').querySelector('h2').textContent==='Der Doctor','own dossier is selected');
 check($('dossierScroll').querySelector('.archive-portrait img').src.endsWith('/portraits/doctor.jpg'),'uncropped main portrait uses known local photo');
 check($('dossierScroll').querySelector('ol').textContent.includes('Go')&&$('dossierScroll').querySelector('ol').textContent.includes('genau'),'early simple instructions retain exact movement rule');
 check($('dossierScroll').querySelector('.archive-ability').textContent.includes('noch keine Tür'),'unimplemented abilities stay honest');
 check($('dossierScroll').textContent.includes('Gallifrey')&&$('dossierScroll').textContent.includes('Timeless Child'),'researched Doctor origins include nuance');
 check($('dossierScroll').querySelectorAll('.archive-scene img').length===1&&$('dossierScroll').querySelectorAll('.archive-sketch').length===2,'extra scene and alternating sketches');
 const cast=['doctor','dalek','weeping-angel','sontaran','cyberman','snowman','adipose','zygon','dalek-2','weeping-angel-2','cyberman-2'];
 check(cast.every(id=>{const archetype=id.replace(/-2$/,''),article=w.CharacterDossiers.render({id,archetype,name:id,portrait:'/portraits/'+archetype+'.jpg',endurance:'Noch festzulegen',strength:'Noch festzulegen',actionAvailable:['doctor','dalek','weeping-angel'].includes(archetype)},()=>{});return !article.querySelector('a,details,.archive-sources')&&!/Serie|Bildhinweis|Quellen|Referenzen|KI-Illustration|Miniatur|Tabletop|The Snowmen|Partners in Crime/i.test(article.textContent)&&Array.from(article.querySelectorAll('img')).every(img=>!/Serie|KI-|Illustration/i.test(img.alt));}),'all eleven dossiers stay in-world without references or image notices');
 const content=$('dossierScroll').firstChild;$('dossierScroll').scrollTop=230;await w.eval('poll()');
 check($('dossierScroll').firstChild===content&&$('dossierScroll').scrollTop===230,'poll preserves dossier DOM and scroll');
 $('dossierScroll').dispatchEvent(new w.Event('scroll'));check(posts.length===0,'scrolling does not silently confirm');
 $('dossierScroll').querySelector('.archive-portrait').click();check(!$('portraitDialog').hidden,'portrait opens while reading');
 d.dispatchEvent(new w.KeyboardEvent('keydown',{key:'Escape',bubbles:true}));check($('portraitDialog').hidden&&d.activeElement.classList.contains('archive-portrait'),'portrait closes and restores focus');
 offline=true;$('readDossier').click();await tick();const request=posts.at(-1).get('requestId');
 check($('readDossier').textContent.includes('erneut')&&!$('readDossier').disabled,'failed confirmation offers usable retry');
 offline=false;$('readDossier').click();await tick();await tick();check(posts.at(-1).get('requestId')===request,'retry keeps identical request ID');
 check($('readDossier').disabled&&$('readDossier').textContent.includes('bestätigt'),'only confirmed host reply marks reader done');
 check(current.setup.stage==='building'&&!$('setupDossier').hidden,'reader stays in dossier until construction and others done');
 current.you='C';current.setup.readBy=['A'];current.revision++;await w.eval('poll()');
 check($('dossierScroll').querySelector('h2').textContent==='Weinender Engel'&&$('dossierScroll').textContent.includes('Ursprung unbekannt'),'Angel origin is not invented');
 check($('dossierScroll').textContent.includes('1–3 Runden')&&$('dossierScroll').textContent.includes('noch nicht'),'Angel desired ability and execution limits');
 lostReply=true;$('readDossier').click();await tick();check(!$('readDossier').disabled,'lost successful reply remains retryable initially');await w.eval('poll()');
 check(w.eval('pending')===null&&$('readDossier').disabled,'host read state resolves lost response without blocking Go');
 current.you='B';current.revision++;await w.eval('poll()');
 check($('setupDossier').hidden&&!$('boardEditor').hidden,'roommaster builds instead of reading');
 current.setup.stage='briefing';current.setup.saved=true;current.revision++;await w.eval('poll()');
 check(!$('setupDossier').hidden&&$('boardEditor').hidden,'roommaster reads after saving');
 check($('dossierScroll').querySelector('h2').textContent==='Dalek'&&$('dossierScroll').textContent.includes('mutierter Kaled'),'Dalek dossier includes species');
 check($('dossierScroll').textContent.includes('bis zu 3 Felder')&&$('dossierScroll').textContent.includes('Himmelsrichtung'),'personal Dalek controls and range');
 $('dossierScroll').querySelector('.archive-portrait').click();current.setup.stage='launching';current.setup.readBy=['A','B','C'];current.revision++;await w.eval('poll()');
 check($('setupDossier').hidden&&$('portraitDialog').hidden,'launch interrupts dossier portrait cleanly');
 current.setup.stage='play';current.current='A';current.revision++;await w.eval('poll()');check(!$('playerArea').hidden&&$('turnDialog').hidden,'waiting player returns to normal profile');
 current.setup.stage='briefing';current.revision++;await w.eval('poll()');expired=true;await w.eval('poll()');
 check(!$('join').hidden&&$('setupArea').hidden&&!d.body.classList.contains('reading-dossier'),'removed player returns to clean join screen');
 console.log('PASS: '+checks+' dossier and pregame confirmation checks');
}finally{w.close()}})().catch(e=>{console.error(e);process.exitCode=1});
