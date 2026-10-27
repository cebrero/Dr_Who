'use strict';
let archiveMode='',archiveData=null,archiveName='',archiveSelected=null,archiveLoading=false,archiveError='',archiveGeneration=0;
const room$=id=>document.getElementById(id);
function roomMasterNow(){return !!(state?.started&&state.setup?.stage==='building'&&state.setup.roomMaster===state.you&&!compatibilityBlocked)}
function resetRoomArchive(){archiveGeneration++;archiveMode='';archiveData=null;archiveSelected=null;archiveLoading=false;archiveError='';archiveName='';room$('archiveList').replaceChildren();delete room$('archiveList').dataset.signature}
function archiveIsOpen(){
 if(!roomMasterNow()){if(archiveMode)resetRoomArchive();return false;}
 if(!archiveMode&&['roomSave','roomLoad'].includes(pending?.action)){
  archiveMode=pending.action==='roomSave'?'save':'confirm';archiveName=pending.action==='roomSave'?pending.direction:'';
  if(pending.action==='roomLoad')archiveSelected={id:pending.direction,name:'Gewählter Raum'};
 }
 return !!archiveMode;
}
async function openRoomArchive(mode){
 if(!canBuild())return;room$(mode==='save'?'openRoomSave':'openRoomLoad').focus();resetRoomArchive();archiveMode=mode;archiveLoading=true;syncModals();await fetchRoomArchive();
}
async function fetchRoomArchive(){
 if(!archiveIsOpen()||busy||pending)return;const generation=++archiveGeneration,access=token,identity=state.sessionId+':'+state.setup.id;
 archiveLoading=true;archiveError='';syncModals();
 try{
  const data=await api('/api/rooms');if(generation!==archiveGeneration||token!==access||!roomMasterNow()||identity!==state.sessionId+':'+state.setup.id)return;
  archiveData=data;if(archiveMode==='save'&&!archiveName)archiveName=data.defaultName;
 }catch(e){if(generation!==archiveGeneration||token!==access)return;if(e.status===401){returnToJoin('Deine Anmeldung ist nicht mehr gültig.');return}archiveError=e.message||'Räume konnten nicht geladen werden.';}
 finally{if(generation===archiveGeneration){archiveLoading=false;syncModals();if(archiveMode==='save'&&!pending)room$('roomName').focus()}}
}
function closeRoomArchive(){if(busy||pending)return;resetRoomArchive();syncModals()}
function roomArchiveFocus(){return archiveMode==='save'&&!archiveLoading?room$('roomName'):archiveMode==='confirm'?room$('archiveLoadConfirm'):room$('archiveList').querySelector('button')||room$('archiveClose')}
function renderRoomArchive(){
 if(!archiveIsOpen())return;
 room$('archiveTitle').textContent=archiveMode==='save'?'Raum speichern':archiveMode==='confirm'?'Diesen Raum laden?':'Gespeicherte Räume';
 room$('archiveSaveForm').hidden=archiveMode!=='save';room$('archiveList').hidden=archiveMode!=='list';room$('archiveConfirm').hidden=archiveMode!=='confirm';
 room$('archiveIntro').textContent=archiveMode==='save'?'Deinen Aufbau für spätere Abenteuer aufbewahren.':archiveMode==='confirm'?'Der aktuelle Aufbau wird durch diesen Raum ersetzt.':'Wähle einen gespeicherten Aufbau.';
 const savePending=pending?.action==='roomSave',loadPending=pending?.action==='roomLoad';
 if(room$('roomName').value!==archiveName)room$('roomName').value=archiveName;
 room$('roomName').disabled=busy||!!pending||archiveLoading;
 room$('archiveSave').textContent=savePending?'Dieselbe Speicherung erneut senden':'Raum speichern';
 room$('archiveSave').disabled=busy||archiveLoading||!!pending&&!savePending||!savePending&&(!archiveData||!archiveName.trim());
 room$('archiveLoadConfirm').textContent=loadPending?'Dasselbe Laden erneut senden':'Raum laden';room$('archiveLoadConfirm').disabled=busy||!!pending&&!loadPending||!archiveSelected;
 room$('archiveClose').disabled=busy||!!pending;room$('archiveBack').disabled=busy||!!pending;
 room$('archiveSelectedName').textContent=archiveSelected?.name||'';
 room$('archiveStatus').textContent=busy?'Wird am Portal gespeichert …':pending?'Bestätigung fehlt. Bitte dieselbe Anfrage erneut senden.':archiveLoading?'Räume werden geladen …':archiveError||modalError||
  (archiveData?.unreadable?archiveData.unreadable+' Raumdatei(en) konnten nicht gelesen werden.':archiveMode==='list'&&archiveData&&!archiveData.rooms.length?'Noch keine Räume gespeichert.':'');
 room$('archiveRefresh').hidden=archiveLoading||!!pending||!archiveError;room$('archiveRefresh').disabled=busy;
 if(archiveMode==='list'&&archiveData){
  const signature=JSON.stringify(archiveData.rooms);if(room$('archiveList').dataset.signature!==signature){room$('archiveList').dataset.signature=signature;room$('archiveList').replaceChildren();
   for(const room of archiveData.rooms){
    const button=document.createElement('button');button.type='button';button.className='saved-room';button.dataset.roomId=room.id;
    const name=document.createElement('strong');name.textContent=room.name;const detail=document.createElement('small');detail.textContent=room.tiles+' Kacheln · '+room.fields+' Felder · '+new Date(room.created).toLocaleString('de-DE');button.append(name,detail);
    button.onclick=()=>{if(busy||pending)return;archiveSelected=room;archiveMode='confirm';syncModals();room$('archiveLoadConfirm').focus()};room$('archiveList').append(button);
   }
  }
 }
}
function roomArchiveCommandSucceeded(request){
 if(!['roomSave','roomLoad'].includes(request.action))return '';resetRoomArchive();
 if(request.action==='roomLoad'){selectedTile=null;spawnMode=false;return 'Raum geladen. Prüfe die Startfelder und stelle eure Figuren entsprechend auf.';}
 return 'Raum gespeichert.';
}
room$('openRoomSave').onclick=()=>openRoomArchive('save');room$('openRoomLoad').onclick=()=>openRoomArchive('list');
room$('archiveClose').onclick=closeRoomArchive;room$('archiveRefresh').onclick=fetchRoomArchive;
room$('archiveBack').onclick=()=>{if(!busy&&!pending){archiveMode='list';syncModals();roomArchiveFocus().focus()}};
room$('roomName').oninput=()=>{archiveName=room$('roomName').value;archiveError='';modalError='';renderRoomArchive()};
room$('archiveSaveForm').onsubmit=event=>{
 event.preventDefault();if(busy||!roomMasterNow())return;if(pending?.action==='roomSave'){send();return}if(pending)return;
 const name=archiveName.trim();if(!name||name.length>80||/[\u0000-\u001f\u007f-\u009f]/.test(name)){archiveError='Bitte einen Raumnamen mit 1–80 Zeichen eingeben.';renderRoomArchive();room$('roomName').focus();return;}
 archiveName=name;command('roomSave',name);
};
room$('archiveLoadConfirm').onclick=()=>{if(busy||!roomMasterNow())return;if(pending?.action==='roomLoad')send();else if(!pending&&archiveSelected)command('roomLoad',archiveSelected.id)};
