'use strict';
window.PortalStage=(()=>{
 const $=id=>document.getElementById(id),landing={1:[0,0],2:[0,-90],3:[-90,0],4:[90,0],5:[0,90],6:[0,180]},pips={1:[5],2:[1,9],3:[1,5,9],4:[1,3,7,9],5:[1,3,5,7,9],6:[1,3,4,6,7,9]};
 let session='',roll='',completed='',timer=0,landingRoll='',lastEvent='',model=null,paused=false;
 for(let value=1;value<=6;value++){const face=document.createElement('div');face.className='face face-'+value;for(const position of pips[value]){const pip=document.createElement('i');pip.className='pip';pip.style.gridArea=Math.ceil(position/3)+' / '+((position-1)%3+1);face.append(pip)}$('cube').append(face)}
 function text(id,value){if($(id).textContent!==String(value))$(id).textContent=value}
 function reset(){clearTimeout(timer);timer=0;roll='';completed='';landingRoll='';lastEvent='';$('dieScene').className='die-scene';}
 function paintTeam(s){const key=JSON.stringify(s.team);if($('team').dataset.signature===key)return;$('team').dataset.signature=key;$('team').replaceChildren();for(const p of s.team){const item=document.createElement('div');item.className='traveller'+(p.active?' active':'');const img=document.createElement('img');img.alt='';img.src=safePortrait(p.portrait);img.onerror=()=>img.hidden=true;const labels=document.createElement('div'),name=document.createElement('strong'),at=document.createElement('small');name.textContent=p.name;at.textContent=p.field;labels.append(name,at);item.append(img,labels);$('team').append(item)}}
 function safePortrait(path){return /^\/portraits\/[a-z0-9-]{1,64}\.jpg$/.test(path)?path.slice(1):''}
 function event(s,conceal){const key=conceal?'reveal:'+s.rollId:s.eventId,heading=conceal?'DER ZEITWIRBEL ENTSCHEIDET':s.eventTitle,body=conceal?'Einen Augenblick. Selbst die Zeit braucht manchmal Zeit.':s.eventText; text('eventLabel',conceal?'DAS PORTAL HÄLT DEN ATEM AN':s.eventType==='move'?'BEWEGUNG IM ZEITGEFÜGE':'DAS PORTAL SPRICHT');text('eventTitle',heading);text('eventText',body);if(key!==lastEvent){lastEvent=key;const el=$('announcement');el.classList.remove('reveal');void el.offsetWidth;el.classList.add('reveal')}}
 function paint(s){
  const revealing=!!s.rollId&&s.ready&&completed!==s.rollId&&!s.seen;
  document.body.dataset.mode=s.mode;document.body.classList.toggle('has-die',!!s.rollId);document.body.classList.toggle('reduced',s.reduced);document.body.classList.toggle('paused',paused);
  text('round',s.round);text('actor',s.actor);text('character',s.character);text('eyebrow',s.mode==='go'?'DIE ZEIT RUFT':s.mode==='enemy'?'GEGNERPHASE':'IM ZEITGEFÜGE UNTERWEGS');
  const src=safePortrait(s.portrait);if($('portrait').getAttribute('src')!==src){$('portrait').src=src;$('portrait').hidden=!src}$('portrait').alt=s.character;$('portrait').onerror=()=>{$('portrait').hidden=true};
  text('field',s.field||'—');text('title',revealing?'Das Schicksal enthüllt sich …':s.title);text('hint',revealing?'Der Spielerwürfel steht. Nun antwortet das Portal.':s.hint);
  $('steps').hidden=s.mode!=='move'||revealing;const stepKey=[s.steps,s.remaining].join(':');if($('steps').dataset.signature!==stepKey){$('steps').dataset.signature=stepKey;$('steps').replaceChildren();for(let i=0;i<s.steps;i++){const p=document.createElement('i');if(i>=s.remaining)p.className='used';$('steps').append(p)}$('steps').setAttribute('aria-label',s.remaining+' von '+s.steps+' Schritten verbleiben')}
  event(s,revealing);paintTeam(s);
 }
 function update(s){
  if(session!==s.session){reset();session=s.session}model=s;
  const id=s.rollId||'';if(roll!==id){clearTimeout(timer);timer=0;landingRoll='';roll=id;completed='';$('dieScene').className='die-scene';}
  $('dieScene').hidden=!id;
  if(id){
   if(!s.ready){$('dieScene').className='die-scene waiting';text('dieCaption','DER SPIELER WÜRFELT');$('dieScene').setAttribute('aria-label','Würfel in Bewegung. Ergebnis noch verborgen.');}
   else if(Number.isInteger(s.die)&&landing[s.die]){
    const [x,y]=landing[s.die];$('cube').style.setProperty('--landing-x',x+'deg');$('cube').style.setProperty('--landing-y',y+'deg');
    const reduced=s.reduced||window.matchMedia?.('(prefers-reduced-motion: reduce)').matches;
    if(s.seen||reduced){clearTimeout(timer);timer=0;completed=id;$('dieScene').className='die-scene finished';}
    else if(completed!==id&&landingRoll!==id){
     landingRoll=id;$('dieScene').className='die-scene landing';
     timer=setTimeout(()=>{timer=0;if(!model||model.session!==s.session||model.rollId!==id)return;completed=id;$('dieScene').className='die-scene finished';update(model)},650);
    }
    text('dieCaption',completed===id?'WURF · '+s.die:'DAS SCHICKSAL ENTHÜLLT SICH');$('dieScene').setAttribute('aria-label',completed===id?'Würfelergebnis: '+s.die:'Würfel enthüllt sich.');
   }
  }
  paint(s);return completed;
 }
 function pause(value){paused=!!value;document.body.classList.toggle('paused',paused)}
 document.addEventListener('visibilitychange',()=>pause(document.hidden));
 return {update,pause};
})();
