const {JSDOM}=require('jsdom'),fs=require('node:fs'),assert=require('node:assert/strict');let checks=0;const check=(value,message)=>{assert(value,message);checks++};
const dom=new JSDOM('<body></body>',{runScripts:'outside-only'}),w=dom.window;w.eval(fs.readFileSync('app/src/main/assets/board-art.js','utf8'));const art=w.BoardArt;
const labels=(x,y,kind='floor',rotation=0,q=2)=>{const tile=w.document.createElement('div');art.decorateTile(tile,{x,y,kind,rotation,closed:true},q);return Array.from(tile.querySelectorAll('.field-label'),e=>e.textContent)};
try{
 check(labels(0,0).join(',')==='A1,B1,A2,B2','each movement subfield has its own coordinate');
 check(labels(19,19).join(',')==='AM39,AN39,AM40,AN40','coordinates at outer edge');
 check(labels(0,0,'floor',3).join(',')===labels(0,0).join(','),'rotating terrain never renames world fields');
 check(labels(0,0,'portal',0).join(',')==='A1','horizontal portal has one goal coordinate');
 check(labels(0,0,'portal',1).join(',')==='A1','vertical portal keeps one goal coordinate');
 check(labels(4,4,'floor',0,1).join(',')==='E5','old single-field saves keep old names');
 const all=[];for(let y=0;y<20;y++)for(let x=0;x<20;x++)all.push(...labels(x,y));check(new Set(all).size===1600,'1600 movement fields have unique names');
 for(const r of [0,1,2,3]){const tile=w.document.createElement('div');art.decorateTile(tile,{x:3,y:1,kind:'compass',rotation:r},2);check(tile.querySelector('svg g').getAttribute('transform')==='rotate('+r*90+' 50 50)','compass art uses saved quarter turn '+r);check(tile.querySelectorAll('.field-label').length===4,'four coordinates remain visible when compass rotates');}
 const closed=art.tileGraphic('dungeon',0,true).outerHTML,open=art.tileGraphic('dungeon',0,false).outerHTML;check(closed!==open,'shared artwork preserves dungeon state');check(art.tileGraphic('dungeon',0,true).querySelector('[stroke="#e12e2e"]')&&!art.tileGraphic('dungeon',0,false).querySelector('[stroke="#e12e2e"]'),'closed dungeon red boundary agrees with blocking walls');
 check(art.fieldName({},25,0)==='Z1'&&art.fieldName({},26,0)==='AA1'&&art.fieldName({},39,39)==='AN40','continuous global columns beyond Z');check(art.fieldRange({board:{subdivisions:2}},{x:1,y:0,kind:'floor',rotation:0})==='C1–D2','next tile uses C/D columns');
 console.log('PASS: '+checks+' shared tile-art and movement-coordinate checks');
}finally{w.close()}
