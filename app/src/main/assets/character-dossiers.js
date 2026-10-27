/* Offline lore summaries. Sources and the distinction from tabletop rules travel with the app. */
'use strict';
window.CharacterDossiers=(()=>{
 const lore={
  doctor:{tag:'REISENDER DURCH RAUM UND ZEIT',species:'Time Lord',origin:'Gallifrey · die bekannte Heimat',
   intro:'Ein neugieriger Kopf, ein unmögliches Raumschiff und meistens ein Plan, der erst unterwegs entsteht. Der Doctor begegnet dem Unbekannten mit Einfallsreichtum.',
   ability:'Dein Sonic',
   control:'Bei einer 5 oder 6 kannst du statt der Bewegung die Aktion wählen. Tippe auf „Aktion wählen · Techniktest“ und danach auf die Himmelsrichtung der Tür. Vor dem Senden kannst du abbrechen. Die Richtungswahl beendet derzeit deinen Zug; die App öffnet noch keine Tür.',
   plan:'Für unser Spiel vorgesehen: Der Schallschraubenzieher öffnet Türen. Reichweite, zulässige Türen und eine mögliche Probe legen wir noch gemeinsam fest.',
   sections:[['Heimat unter fremden Himmeln','Gallifrey ist die bekannte Heimat des Doctors und der Time Lords. Doch die Geschichte des Timeless Child zeigt: Die früheste Herkunft des Doctors reicht über dieses vertraute Bild hinaus. Gallifrey ist deshalb keine vollständige Antwort auf die Frage nach dem Ursprung.'],
    ['Mehr als ein Gesicht','Regeneration kann Aussehen und Persönlichkeit des Doctors verändern. Das macht verschiedene Inkarnationen möglich. Im Tabletop gibt es dadurch noch keine automatische Heilung, Wiederbelebung oder zusätzliche Figur.'],
    ['Ein Werkzeug für unmögliche Tage','In der Serie arbeitet der Sonic mit Schall und hilft beim Untersuchen von Technik oder beim Öffnen von Schlössern. Holz und Deadlock-Verschlüsse setzen ihm Grenzen. Diese Serienbesonderheiten werden erst nach gemeinsamer Regelentscheidung Teil unseres Spiels.']],
   scene:'doctor-scene.jpg',caption:'Ein Sonic an einer Zeitreisekonsole · freie KI-Illustration',sketch:'doctor.svg',sketchCaption:'Archivskizze · der Doctor',
   sources:[['Time Lords','https://www.doctorwho.tv/news-and-features/what-is-a-time-lord'],['Gallifrey','https://www.doctorwho.tv/news-and-features/13-chapters-in-the-history-of-gallifrey'],['Timeless Child','https://www.doctorwho.tv/news-and-features/history-of-the-timeless-child'],['Sonic Screwdriver','https://www.doctorwho.tv/news-and-features/what-is-a-sonic-screwdriver']]},
  dalek:{tag:'PANZERUNG AUSSEN. MUTANT INNEN.',species:'Dalek · mutierter Kaled',origin:'Skaro',
   intro:'Unverwechselbare Silhouette, ein einzelnes Auge und eine Stimme, die selten flüstert. Hinter dem Metall steckt ein Lebewesen, kein gewöhnlicher Roboter.',
   ability:'Dein geradliniger Fernangriff',
   control:'Bei einer 5 oder 6 kannst du statt des Ziehens „Aktion wählen · Techniktest“ antippen. Wähle danach die Himmelsrichtung für deinen Schuss. Vor dem Senden ist Abbrechen möglich. Die bestätigte Richtung beendet aktuell deinen Zug; Treffer oder Schaden berechnet die App noch nicht.',
   plan:'Für unser Spiel vorgesehen: Schießen bis zu 3 Felder weit in einer geraden Richtung. Zielauswahl, Deckung, Treffer und Schaden sind noch offen. Der Bewegungswurf ist kein Schadenswurf.',
   sections:[['Von Skaro ins Universum','Auf Skaro entstanden die Daleks aus mutierten Kaleds. Davros setzte die veränderten Lebewesen in gepanzerte Maschinen. Ihre äußere Hülle ist also ein Kriegsgerät, das seinen organischen Bewohner umschließt.'],
    ['Eine Hülle mit Wiedererkennungswert','Augenstiel, Kuppel und gepanzerter Körper machen den Dalek sofort erkennbar. In den Geschichten wird aus dieser Silhouette eine Bedrohung, die ganze Welten verfolgt. Die Panzerung bekommt bei uns noch keinen erfundenen Schutzwert.'],
    ['Feinde in der Serie, Partner am Tisch','Daleks bekämpfen in der Serie den Doctor und führten Krieg gegen die Time Lords. Für eure Partie gilt aber die Teilnahme: Wenn du diese Figur gewählt hast, bist du Partner der anderen Spieler. Nur am Portal hinzugefügte Figuren sind Gegner.']],
   scene:'dalek-scene.jpg',caption:'Dalek auf einer freien Skaro-Interpretation · KI-Illustration',sketch:'dalek.svg',sketchCaption:'Archivskizze · die Panzerhülle',
   sources:[['Daleks','https://www.doctorwho.tv/characters/daleks'],['Davros','https://www.doctorwho.tv/characters/davros']]},
  'weeping-angel':{tag:'DIE STILLE ZWISCHEN ZWEI AUGENBLICKEN',species:'Weinender Engel',origin:'Ursprung unbekannt',
   intro:'Eine Statue am Wegrand. Ein kurzer Blick zur Seite. Und plötzlich steht sie näher. Die Weinenden Engel gehören zu den rätselhaftesten Wesen der Serie.',
   ability:'Dein Eingriff in die Zeit',
   control:'Würfle wie alle anderen zuerst. Bei einer 5 oder 6 wählst du statt Bewegung „Aktion wählen · Techniktest“ und anschließend die Himmelsrichtung deines beabsichtigten Ziels. Abbrechen geht vor dem Senden. Die Richtungswahl beendet derzeit deinen Zug; die App setzt noch niemanden automatisch aus.',
   plan:'Für unser Spiel vorgesehen: Gegner im Sichtfeld setzen zufällig 1–3 Runden aus. Sichtfeld, Zielzahl und Beginn sowie Ende des Zustands sind noch zu klären. Diese Wirkung wird noch nicht ausgewürfelt oder angewendet.',
   sections:[['Eine Herkunft im Dunkeln','Die offizielle Serienbeschreibung kennt die Engel als uralte Wesen, deren Ursprung und Kultur kaum bekannt sind. Einen gesicherten Heimatplaneten gibt sie nicht an. Unser Archiv lässt diese Lücke bewusst als Geheimnis stehen.'],
    ['Stein, solange jemand hinsieht','Beobachtete Engel sind in der Serie quantenverriegelt und wirken wie Stein. Unbeobachtet bewegen sie sich äußerst schnell. Das ist Serienwissen: Eine Blick- oder Erstarrungsregel für euren Tisch ist noch nicht vereinbart.'],
    ['Gestohlene Lebenszeit','Die Berührung eines Engels kann ein Opfer in die Vergangenheit schicken. Die Engel nähren sich von der Energie der dadurch ungelebten Zeit. Unser gewünschtes Aussetzen für 1–3 Runden ist eine eigene Tabletop-Idee und kein bereits ausgeführter Serienmechanismus.']],
   scene:'weeping-angel-scene.jpg',caption:'Engel im nebligen Friedhof · freie KI-Illustration',sketch:'weeping-angel.svg',sketchCaption:'Archivskizze · die steinerne Gestalt',
   sources:[['Weeping Angels','https://www.doctorwho.tv/characters/weeping-angels'],['Geschichte der Engel','https://www.doctorwho.tv/news-and-features/a-brief-history-of-the-weeping-angels']]}
 };
 function node(tag,text,cls){const e=document.createElement(tag);if(text!==undefined)e.textContent=text;if(cls)e.className=cls;return e}
 function image(src,alt,cls){const img=node('img',undefined,cls);img.src='/portraits/'+src;img.alt=alt;img.loading='lazy';img.decoding='async';img.addEventListener('error',()=>{const note=node('span','Illustration gerade nicht verfügbar.','dossier-image-error');img.replaceWith(note)});return img}
 function section(number,title){const s=node('section',undefined,'archive-section');s.append(node('small','AKTE / '+number,'archive-kicker'),node('h3',title));return s}
 function render(c,onPortrait){
  const d=lore[c.id]||{tag:'DEINE REISE BEGINNT',species:'Noch zu beschreiben',origin:'Noch zu beschreiben',intro:c.description,ability:'Deine Spezialfähigkeiten',control:'Bestätige deinen Zug mit Go und tippe zum Würfeln auf den Würfel. Bei 5 oder 6 kannst du statt der Bewegung eine Aktion wählen.',plan:(c.abilities||[]).join(' '),sections:[],sources:[]};
  const article=node('article',undefined,'archive-dossier');article.setAttribute('aria-label',c.name+' · Charakterdossier');
  const hero=node('div',undefined,'archive-hero'),photo=node('button',undefined,'archive-portrait');photo.type='button';photo.setAttribute('aria-label',c.name+' · Bild vergrößern');
  if(/^\/portraits\/[a-z0-9-]{1,64}\.(jpg|svg)$/.test(c.portrait)){photo.append(image(c.portrait.slice(11),c.name+' · Charakterporträt'));photo.onclick=()=>onPortrait(c)}else photo.append(node('span','Porträt folgt'));
  const title=node('div',undefined,'archive-title');title.append(node('small',d.tag,'archive-kicker'),node('h2',c.name),node('p',d.intro));hero.append(photo,title);article.append(hero);
  const facts=node('dl',undefined,'archive-facts');for(const [k,v] of [['Spezies',d.species],['Herkunft',d.origin],['Ausdauer',c.endurance],['Kraft',c.strength]]){const item=node('div');item.append(node('dt',k),node('dd',v));facts.append(item)}article.append(facts);
  const how=section('01','So spielst du deine Figur');
  const steps=node('ol');for(const [title,text] of [
   ['Go, dann würfeln','Wenn „Your move...“ erscheint, tippe Go. Tippe danach direkt auf den Würfel und warte auf das Ergebnis.'],
   ['Deine Bewegung','Bei 1–4 ziehst du genau so viele kleine Felder. Bei 5 oder 6 wählst du Bewegung um genau die Augenzahl ODER deine Aktion. Kein zusätzlicher Wurf und kein freies Passen.'],
   ['Richtung und Tisch','Das Portal steht im Norden. Drehe deinen Bewegungskompass passend zu deinem Sitzplatz; deine Karten drehen sich mit. Ziehe mit Nord, Ost, Süd oder West. Richtungswechsel sind erlaubt, Diagonalen nicht.'],
   ['Jeden Schritt nachvollziehen','Die App speichert jeden bestätigten Schritt. Bewege die echte Figur entsprechend mit. Unten siehst du dein Umfeld; im Tab Karte kannst du zoomen und Koordinaten lesen. Wände, fehlende Felder und belegte Felder blockieren. Bei abgelehntem Schritt bleibt deine Reststrecke erhalten.']
  ]){const li=node('li');li.append(node('strong',title),node('p',text));steps.append(li)}how.append(steps);article.append(how);
  const ability=section('02',d.ability);ability.classList.add('archive-ability');ability.append(node('p',d.control),node('p',d.plan,'archive-plan'));
  const limit=node('p','Aktueller Spielstand: Die Aktionswahl ist ein Techniktest. Es werden noch keine Türen geöffnet, Treffer berechnet oder Statusrunden angewendet. Ausdauer und Kraft erhalten erst später verbindliche Werte.','archive-rule-note');ability.append(limit);article.append(ability);
  for(let i=0;i<d.sections.length;i++){
   const [heading,text]=d.sections[i],s=section(String(i+3).padStart(2,'0'),heading);
   if(i===0&&d.scene){const figure=node('figure',undefined,'archive-scene');figure.append(image(d.scene,d.caption),node('figcaption',d.caption));s.append(figure)}
   if(i>0&&d.sketch){const figure=node('figure',undefined,'archive-sketch '+(i%2?'sketch-right':'sketch-left'));figure.append(image(d.sketch,d.sketchCaption),node('figcaption',d.sketchCaption));s.append(figure)}
   s.append(node('p',text));article.append(s);
  }
  const bag=section('06','Karte, Tasche und Story');bag.append(node('p','Läufst du über den geheimen Portalschlüssel, meldet sich das Portal. Bestätige den Fund; der Schlüssel liegt danach in deiner Tasche. Benutzen oder Tauschen ist noch nicht freigegeben. Im Tab Story findest du die neuesten Ereignisse oben. Alle beigetretenen Spieler sind Partner, unabhängig von ihrer Serienrolle.'));article.append(bag);
  const sources=node('details',undefined,'archive-sources');sources.append(node('summary','Serienquellen & Bildhinweise'));sources.append(node('p','Hintergrundwissen frei auf Deutsch zusammengefasst aus offiziellen Doctor-Who-Quellen, geprüft am 27.10.2026. Das sind keine zusätzlichen Spielregeln. Porträts und Szenen sind KI-Illustrationen; Skizzen sind eigene Zeichnungen.'));
  for(const [label,url] of d.sources){const a=node('a',label);a.href=url;a.target='_blank';a.rel='noopener noreferrer';sources.append(a)}article.append(sources);
  return article;
 }
 return {render};
})();
