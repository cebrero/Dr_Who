/* Shared offline vector art and field labels for portal and players. */
(()=>{
'use strict';
function tileGraphic(kind,rotation=0,closed=true,subdivisions=2){
 const svg=document.createElementNS('http://www.w3.org/2000/svg','svg');svg.setAttribute('viewBox','0 0 100 100');svg.setAttribute('aria-hidden','true');if(kind==='portal')svg.setAttribute('preserveAspectRatio','none');
 const drawings={
  corner:'<path d="M5 94V6H94" fill="none" stroke="#28494c" stroke-width="11"/><path d="M5 94V6H94" fill="none" stroke="#d1c39a" stroke-width="3"/>',
  floor:'<path d="M12 14l12-2m48 58l12 2" stroke="#84795555" stroke-width="2"/>',
  single:'<path d="M0 6H100" stroke="#e12e2e" stroke-width="12"/>',
  cemetery:'<path d="M28 80V35h20V18h12v17h18v14H60v31" fill="#61716b" stroke="#293d38" stroke-width="3"/>',
  compass:'<path d="M50 8L66 66L50 55L34 66Z" fill="#d33427"/><text x="50" y="92" text-anchor="middle" font-size="23" fill="#123c40">N</text>',
  lava:'<rect width="100" height="100" fill="#a62e12"/><path d="M5 30L30 15L45 55L70 35L95 75M25 95L40 70L15 55" fill="none" stroke="#ffb72b" stroke-width="9"/>',
  'lava-stone':'<path d="M0 0H50V100H0Z" fill="#b43a15"/><path d="M8 12L35 30L12 55L40 85" fill="none" stroke="#ffbd31" stroke-width="8"/>',
  parallel:'<path d="M0 6H100M0 94H100" stroke="#28494c" stroke-width="11"/><path d="M0 6H100M0 94H100" stroke="#d1c39a" stroke-width="3"/>',
  door:'<path d="M0 6H27M73 6H100" stroke="#28494c" stroke-width="11"/><path d="M28 7H72V20H28Z" fill="#947149" stroke="#463e30" stroke-width="3"/><path d="M32 13h36" stroke="#f7d39a" stroke-width="2"/><path d="M28 25L68 46M28 25A43 43 0 0 1 72 25" fill="none" stroke="#806a41" stroke-width="2" stroke-dasharray="4 3"/>',
  dungeon:'<path d="M23 80V32a27 27 0 0 1 54 0v48z" fill="#213d44" stroke="#b2955f" stroke-width="5"/><path d="M36 22v57M50 17v62M64 22v57M24 49h52" stroke="#95abad" stroke-width="4"/>',
  well:'<ellipse cx="50" cy="69" rx="32" ry="18" fill="#8e825e" stroke="#4e695f" stroke-width="4"/><path d="M18 44v25m64-25v25" stroke="#577166" stroke-width="5"/><ellipse cx="50" cy="44" rx="32" ry="18" fill="#366976" stroke="#b8aa7a" stroke-width="9"/><ellipse cx="50" cy="44" rx="21" ry="9" fill="#255064"/><path d="M38 42q12-7 24 0" fill="none" stroke="#a2e2df" stroke-width="2"/>',
  portal:'<ellipse cx="50" cy="49" rx="35" ry="44" fill="#16445a" stroke="#d9bc72" stroke-width="4"/><ellipse cx="50" cy="49" rx="27" ry="35" fill="#277382" stroke="#8de1df" stroke-width="3"/><path d="M48 14q39 27 1 65q-31-20 0-52q24 10 1 36" fill="none" stroke="#b6eeea" stroke-width="4"/><path d="M20 87h60" stroke="#d9bc72" stroke-width="4"/>'
 };
 if(kind==='dungeon'&&closed)drawings.dungeon+='<path d="M6 6H94V94H6Z" fill="none" stroke="#e12e2e" stroke-width="12"/>';
 if(kind==='dungeon'&&!closed)drawings.dungeon='<path d="M23 80V32a27 27 0 0 1 54 0v48" fill="none" stroke="#347366" stroke-width="9"/><path d="M25 30L6 50V92L25 80" fill="#52857a"/>';
 if(kind==='portal')drawings.portal+='<path d="M6 94V6H94V94" fill="none" stroke="#e12e2e" stroke-width="12"/>';
 svg.innerHTML='<g transform="rotate('+rotation*90+' 50 50)">'+(drawings[kind]||drawings.floor).replace(/<path[^>]*stroke="#d1c39a"[^>]*\/>/g,'').replaceAll('#28494c','#e12e2e').replaceAll('stroke-width="11"','stroke-width="13"')+'</g>'+(subdivisions===2&&kind!=='portal'?'<path d="M50 0V100M0 50H100" stroke="#34483ecc" stroke-width="2.5"/>':'');return svg;
}
function fieldName(s,x,y){const q=s?.board?.subdivisions||1,t=s?.board?.tiles?.find(t=>t.kind==='portal'&&x>=t.x*q&&x<(t.x+tileSize(t)[0])*q&&y>=t.y*q&&y<(t.y+tileSize(t)[1])*q);if(t){x=t.x*q;y=t.y*q}if(x<0||y<0)return 'noch nicht aufgestellt';let column='',at=x;do{column=String.fromCharCode(65+at%26)+column;at=Math.floor(at/26)-1}while(at>=0);return column+(y+1)}
function fieldRange(s,t){const q=s.board?.subdivisions||1,[w,h]=tileSize(t),first=fieldName(s,t.x*q,t.y*q),last=t.kind==='portal'?first:fieldName(s,(t.x+w)*q-1,(t.y+h)*q-1);return first===last?first:first+'–'+last}
function tileSize(t){return [t.kind==='portal'&&t.rotation%2===0?2:1,t.kind==='portal'&&t.rotation%2?2:1]}
function decorateTile(element,tile,subdivisions=2){
 element.append(tileGraphic(tile.kind,tile.rotation,tile.closed,0));
 const [w,h]=tileSize(tile),cols=tile.kind==='portal'?1:w*subdivisions,rows=tile.kind==='portal'?1:h*subdivisions;
 for(let y=0;y<rows;y++)for(let x=0;x<cols;x++){
  const field=document.createElement('span');field.className='board-subfield';field.style.left=(x/cols*100)+'%';field.style.top=(y/rows*100)+'%';field.style.width=(100/cols)+'%';field.style.height=(100/rows)+'%';
  const label=document.createElement('span');label.className='field-label';label.dataset.field=fieldName({board:{subdivisions}},tile.x*subdivisions+x,tile.y*subdivisions+y);label.textContent=label.dataset.field;field.append(label);element.append(field);
 }
}
window.BoardArt={tileGraphic,fieldName,fieldRange,tileSize,decorateTile};
})();
