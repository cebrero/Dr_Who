# Dr_Who – Onboarding, Spielablauf und Spielregeln

Stand: **26.10.2026 · Version 0.10.1 · kompakte Spieleransicht, Story-Sprechblasen und klare Feldlabels**
<!-- app-version: 0.10.1 -->
<!-- reviewed-source-sha256: 159171dfae6e3deda37be5b8391e325f131fec602edeca507bbbe6076f2f3295 -->
Repository: https://github.com/cebrero/Dr_Who · Hauptbranch: `main`

Diese Datei ist die maßgebliche Übergabe an zukünftige Chats. Nach jeder Entwicklungsrunde
Anforderungen, Entscheidungen, tatsächlichen Stand, Teststatus und nächsten Schritt hier
aktualisieren. Keine parallelen Handoff- oder Statusdateien anlegen.

## 1. Auftrag und Vision des Nutzers

Ein Android-Handy steckt in einer selbst gedruckten Halterung, die wie eine Tür aussieht.
Das Handy wird zum sichtbaren Portal und digitalen Spielleiter eines physischen Tabletop-Spiels
im Universum der TV-Serie Doctor Who. Bis zu vier Spieler bedienen das Spiel über ihre eigenen
Handys. Nur das Host-Handy braucht eine Android-App; die anderen benötigen einen Browser.

Der Nutzer möchte die Regeln und ein sinnvolles Spielziel gemeinsam entwickeln.
Er nennt später sämtliche vorhandenen Figuren und zeigt die bereits gedruckten Spielteile.
Keine vollständige Figurenliste und kein vollständiges Teileinventar sind bisher bekannt.

## 2. Vom Nutzer ausdrücklich gewünschte Anforderungen

| Bereich | Anforderung |
| --- | --- |
| Projekt | Neues Android-Projekt mit dem Namen `Dr_Who` auf GitHub |
| Projektwissen | Alle Vereinbarungen, Zukunftspläne und Informationen dauerhaft auf GitHub dokumentieren, sodass ein neuer Chat nahtlos übernehmen kann |
| Host | Android-Handy als digitaler Spielleiter in gedruckter Tür-/Portalhalterung |
| Netzwerk | Host stellt über einen Hotspot eine lokale Website bereit |
| Spieler | Bis zu vier Personen treten auf eigenen Handys mit angezeigtem Code bei und können interagieren |
| Runden | Rundenbasierter Ablauf; je nach Charakter Bewegung oder Aktion |
| Welt | Doctor-Who-Thema und später benannte Seriencharaktere |
| Brett | Rechteckige magnetisch verbundene Felder; Räume, Wände und Türen |
| Besondere Teile | Verließ, Friedhof und Brunnen vorhanden bzw. für das Spiel genannt |
| Aufbau | Nutzer nennt Stückzahlen von Plättchen mit gerader Wand, Ecke und zwei parallelen Wänden sowie weitere Teile |
| Generator | Bei neuem Spiel logischen Aufbau aus vorhandenen Teilen vorschlagen, einschließlich zusätzlichem Raum; Spieler bauen diesen auf |
| Fortsetzung | Spielzustand und Brettaufbau speichern, später fortsetzen und physisch wieder aufbauen |
| Figuren | Unterschiedliche Fähigkeiten und Eigenschaften pro Charakter |
| Charakterwahl | Nach Beitritt Steckbrief mit Foto, Beschreibung, Ausdauer, Kraft und Sonderfähigkeiten; gewählte Figur für andere gesperrt |
| Portal-Lobby | Während der Auswahl Verbindungshilfe, Portalcode, Fortschritt und Zuordnung Spielername → Figur anzeigen |
| Teams | Alle beigetretenen Spieler sind Partner. Übrige bekannte Figuren können danach ausschließlich am Portal als Gegner hinzugefügt werden |
| Gegnerbewegung | Wenn ein Gegner bewegt werden muss, bestimmt die App zufällig einen Spieler für die physische Ausführung |
| Warteanzeige | Seit neuestem Wunsch 0.6.0: eigener Steckbrief als Standard; TARDIS optional im Tab Spiel |
| Angriffsanzeige | Angegriffener Spieler sieht den eigenen Steckbrief, die Attribute des Gegners und die konkreten Folgen für seinen Charakter |
| Beispiel Doctor | Schallschraubenzieher kann Türen öffnen |
| Beispiel Daleks | Schießen nur bis zu drei Felder in geraden Richtungen |
| Beispiel Weinende Engel | Gegner im Sichtfeld für zufällig ein bis drei Runden aussetzen lassen |
| Spielleitung | App speichert Aktionen, verfolgt Bewegungen und entscheidet den Zufallsteil von Aktionen |
| Darstellung | Portal- und Zwischenanimationen auf dem Host; Nutzer kann geeignete Dateien bereitstellen |
| Bestätigtes Design | Spielerhandys: B „Zeitreise-Archiv“. Portal-Handy: A „TARDIS-Konsole“ |

Diese Beispiele sind Nutzerwünsche, aber noch keine vollständig definierten oder ausbalancierten
Regeln. Insbesondere Sichtlinien, Trefferfolgen, Bewegungsweiten, Gegnersteuerung und genaue
Aktionsökonomie müssen gemeinsam geklärt werden.

## 3. Arbeitsentscheidungen dieser ersten Umsetzung

Technische, später änderbare Entscheidungen; nicht mit zugesagten Endfunktionen verwechseln:

- Android-App ist alleinige Instanz für gültige Züge, Zufall und Speichern.
- Seit 0.9.1 direkter automatischer Android-Lokalhotspot; SSID/Passwort werden vom System
  vergeben und tatsächlich angezeigt. Der frühere Wunsch `Dr_Who` / `drwho123` bleibt mit
  gewöhnlichen öffentlichen APIs für diesen Startweg nicht frei konfigurierbar. Die frühere
  manuelle Einrichtungsnachfrage entfällt auf Nutzerwunsch.
- Maximal vier registrierte Spielerplätze wird in der App erzwungen. Die Zahl technisch möglicher
  WLAN-Clients hängt zusätzlich vom Telefon ab und ist auf echter Hardware zu prüfen.
- Zusätzlicher Beitrittscode entfällt seit 0.9.0; das WLAN-Passwort bleibt. Jede Anmeldung erhält
  einen geheimen Wiederverbindungstoken. Keine MAC-Bindung oder persönlichen Onlinekonten.
- Bewegungen werden von Spielern eingegeben und auf dem echten Tisch nachvollzogen.
  Die App erkennt physische Figuren derzeit nicht automatisch; Kameras/Sensoren sind nicht vereinbart.
- Deutschsprachige Oberfläche. Java 17 als schlankes Android-Grundgerüst mit getrenntem Kernmodul.
- Statischer, KI-generierter Doctor-Who-Hintergrund in der Portal-Lobby; selbst gezeichnete
  TARDIS-Animation nur in der separaten Großansicht und optional im Spieltab wartender Spieler. Keine Serienmedien beschafft.
- Noch keine Entscheidung zu Hoch-/Querformat, Telefonabmessungen, Halterungsausschnitt oder Ton.

## 4. Spielziel – Vorschlag, noch NICHT bestätigt

### „Der Zeitriss“ – kooperative Mission

Die Gruppe strandet in einem durch Zeitrisse veränderten Ort. Drei verteilte Zeitanker müssen
gefunden, aktiviert und am Portal synchronisiert werden. Danach müssen die Helden rechtzeitig
zum Portal zurückkehren. Ein Bedrohungszähler steigt durch Runden und ausgewählte Ereignisse.
Bei Erreichen des Limits kollabiert der Zeitriss. Anzahl der Anker und Rundenlimit werden erst
nach Kenntnis des Brettinventars und einigen Testpartien festgelegt.

Mögliche Rollen der vorhandenen Geländeobjekte:

- **Verließ:** Gefangenen befreien oder einen eingeschlossenen Zeitanker bergen.
- **Friedhof:** Versteckte Hinweise, Engel-Bedrohung oder riskante Abkürzung.
- **Brunnen:** Quelle für eine einmalige Erholung oder Hinweis auf einen Zeittunnel.
- **Zusätzlicher Raum:** Abgeschlossener Technikraum, der einen Anker oder die Portalsteuerung enthält.
- **Tür-/Portalhalterung:** sichtbarer Ausgang, Missionsstatus und Portalöffnung im Finale.

**Bestätigt am 25.10.2026:** Alle beigetretenen Spieler spielen als Partner. Die Zuordnung richtet
sich nach der Teilnahme, nicht nach der Rolle in der Serie: Auch ein von einem Spieler gewählter
Dalek wäre ein Partner. Nicht gewählte Figuren sind zunächst nur verfügbar, nicht automatisch
aktiv. Erst durch Hinzufügen am Portal werden sie zu Gegnern. Das kooperative Missionsziel
„Der Zeitriss“ selbst ist weiterhin nur ein Vorschlag.

### Vorgeschlagener Rundenablauf

Für den verbindlichen bisherigen Ablauf gilt jetzt Abschnitt 4a. Die frühere Demo-Regel
„ein Schritt ODER W6-Test ODER passen“ ist aufgehoben. Zwei Aktionspunkte sind nicht vereinbart.
Eine vollständige Missionsschleife und automatische Kampfauflösung fehlen weiterhin.

## 4a. Spielablauf und Spielregeln – verbindlicher Stand 0.10.1

### Vor der Partie

1. Portal-Handy zeigt eine gemeinsame Startseite mit statischem Doctor-Who-Hintergrund, WLAN-Zugang,
   Spielseite und darunter dem Team. **Spiel-WLAN starten** startet direkt den Android-Lokalhotspot;
   nur erforderliche Android-Berechtigungen bleiben. WLAN-/Seiten-QR-Codes vereinfachen den Einstieg.
   Neue Registrierung am Portal mit bereitgestelltem TARDIS-Sound begrüßen; bestehende Plätze bleiben still.
2. Bis zu vier Spieler treten am eigenen Handy mit Namen ohne zusätzlichen Portalcode bei.
3. Jeder wählt eine Quartettkarte mit großem vollständigem Porträt oben und Eigenschaften darunter.
   Gewählte Figuren sind für andere gesperrt. Alle beigetretenen Spieler sind Partner.
4. Portal zeigt, wie viele bereits gewählt haben, plus Spielername und zugeordnete Figur.
5. Nach der Spielerwahl kann der Host übrige bekannte Figuren als Gegner hinzufügen.
6. Host drückt Start; der bereitgestellte Themensong spielt einmal. Drei Sekunden TARDIS-Flug, dann zufällige Raummeister-Anfrage an echte
   Spieler. Nein fragt jemand anderen; nach ausschließlich Nein entscheidet das Portal verbindlich.
7. Raummeister erfasst den tatsächlichen Aufbau per Kachel-Editor (20×20, elf Typen einschließlich Portal über zwei Kacheln, 90°-Drehung,
   vier Bewegungsfelder pro Kachel, Startfiguren). Portal zeigt den bestätigten Entwurf live. Erst Speichern,
   dann drei Sekunden Startanimation und erster Spielerzug. Vorher wird ein Portalschlüssel geheim auf
   einem freien erreichbaren Feld versteckt. Beim Betreten Fund-Popup, Portalanimation und Tasche.
   Aktuelle Details siehe Abschnitte 8d–8i.
8. Normale Spielerhandlungen laufen am eigenen Gerät. Bereits laufende ältere Partien bleiben
   erhalten und können ihren Plan über die Spielleitung nachtragen. Der Inventargenerator folgt später.

### Spielerzug

| Schritt | Regel | Umsetzung 0.10.1 |
| --- | --- | --- |
| 0 | Zugbeginn mit „Your move...“ und Go bestätigen | Host speichert die Bestätigung; bis dahin Karte des aktiven Spielers am Portal und keine Würfel-/Spielaktion |
| 1 | Aktiver Spieler würfelt zuerst einen W6 am eigenen Handy | Direkt den Würfel antippen; Host würfelt und speichert verbindlich; kein erneutes Würfeln bei Retry |
| 2a | Bei 1, 2, 3 oder 4: genau diese Zahl Felder ziehen | Direkt zur Bewegung; dies ist die aus der Nutzerregel abgeleitete Interpretation |
| 2b | Bei 5 oder 6: Bewegung ODER Charakteraktion wählen | Zwei Auswahlmöglichkeiten; kein Bewegen plus Aktion |
| 3a | Bewegung immer genau um die Augenzahl, nicht nur bis zu dieser Zahl | Reststrecke wird nach jedem gespeicherten Schritt reduziert |
| 3b | Nur gerade/orthogonale Schritte; Richtungswechsel unterwegs erlaubt | Nord/Ost/Süd/West, keine Diagonalen; z. B. 2 nach Norden, dann 3 nach Osten bei einer 5 |
| 3c | Spieler meldet seine Bewegungen am eigenen Gerät und bewegt die echte Figur passend | Schrittweise Eingabe im gespeicherten Aufbau; App kann die physische Bewegung nicht automatisch erkennen |
| 3d | Alternativ bei 5/6: charakterabhängige Aktion | Aktionswahl protokolliert; konkrete Wirkung noch nicht umgesetzt |
| 4 | Nach vollständiger Bewegung bzw. Aktion folgt der nächste Spieler | Nach exakt verbrauchter Strecke bzw. Test-Aktionswahl; nächster Spieler beginnt wieder mit Go/W6 |

Im Prototyp ist die 5/6-Entscheidung verbindlich, sobald „Bewegen“ gewählt wurde. Während
angefangener Bewegung kann nicht nachträglich zur Aktion gewechselt werden. Das ist eine
technische Festlegung für klare Zustände; ein späterer Zurück-/Planungsmodus ist noch offen.
Die aktuelle Taste „Aktion wählen · Techniktest“ öffnet bei den drei bekannten gerichteten
Fähigkeiten zuerst die Richtungsauswahl Nord/Ost/Süd/West. Erst die gesendete Richtungswahl
beendet den Zug; Abbrechen verbraucht nichts. Gespeichert wird weiterhin nur die Auswahl,
ohne eine Tür zu öffnen, Schaden zu verursachen oder einen Status anzuwenden. Das ist eine
klar gekennzeichnete Übergangslösung, keine fertig implementierte Sonderfähigkeit.

### Gegnerphase und Rundenende

1. Nach allen Partnerzügen folgen die hinzugefügten Gegner.
2. Muss eine Gegnerfigur bewegt werden, bestimmt der Host zufällig einen Spieler als Helfer.
   Dieser erhält den Auftrag am eigenen Handy und bestätigt die physische Ausführung.
3. Wird ein Spieler angegriffen, wechselt sein Handy zur Angriffsansicht: eigener Steckbrief,
   Gegnerattribute und gespeicherte konkrete Folge. Nur das Ziel bestätigt die Anzeige.
4. Unbeteiligte Handys zeigen seit 0.6.0 standardmäßig den eigenen Steckbrief.
   Die fliegende TARDIS bleibt im optional gewählten Tab „Spiel“ sichtbar.
5. Nach allen Gegnern beginnt die nächste Runde. Sind keine Gegner hinzugefügt, beginnt
   die nächste Runde unmittelbar nach dem letzten Partner.

Zielzustand: Gegnerentscheidungen und Ergebnisse automatisch durch die App. Heute muss der
Host Bewegungsbedarf, Weg/Ziel und Angriffsfolge noch vorgeben; nur Helferwahl, Übertragung,
Bestätigung, Phasenwechsel und Speichern sind bereits automatisiert. Spieler sollen im fertigen
Spiel möglichst keine Portalbedienung übernehmen. Der Host dient hauptsächlich Portal/Lobby,
Aufbau, Spielstart/Fortsetzung und Verwaltung; physisches Figurenbewegen bleibt notwendig.

### Bisher genannte Charakterregeln

- Doctor: Schallschraubenzieher kann Türen öffnen.
- Dalek: Schuss bis zu drei Felder in geraden Richtungen.
- Weinender Engel: Gegner im Sichtfeld für zufällig ein bis drei Runden aussetzen lassen.

Diese Fähigkeiten sind Nutzerwünsche. Ausdauer-/Kraftwerte, Reichweite des Schraubenziehers,
Treffer/Schaden, Sichtlinien, Statusbeginn/-ende und genaue Zielwahl sind noch nicht definiert.
Die allgemeine 5/6-Aktionsfreigabe gilt als Grundregel; charakterbezogene Ausnahmen wurden
bisher nicht vereinbart. Der Bewegungs-W6 ist nicht automatisch ein Schadenswurf.

### Noch offene Regeln – keine stillen Festlegungen

- Was passiert, wenn die volle Augenzahl wegen einer Wand, geschlossenen Tür oder anderen
  Figur nicht gelaufen werden kann? Rest verfallen lassen, Umweg, Zurückgehen oder andere Lösung?
- Darf ein Feld im selben Zug mehrfach betreten werden? Im freien Prototyp technisch möglich,
  aber als endgültige Regel noch nicht bestätigt. Andere Figuren blockieren dort vorläufig Felder.
- Können Spieler einen Weg zuerst komplett planen und dann bestätigen/noch ändern?
- Welche Charakteraktion ist bei mehreren Fähigkeiten wählbar, mit welchem Ziel und welcher Probe?
- Treffer, Schaden, Ausdauerverlust, Kraftberechnung, Schutz, Erholung und Niederlage.
- Gegnerverhalten, Gegner-Bewegungsweite, Sichtlinien, Türen und Zeitversetzung/Überspringen.
- Missionsziel, Siegbedingungen, Startreihenfolge und gewünschte Spieldauer.

Das Missionsziel „Der Zeitriss“ bleibt ein unbestätigter Vorschlag, nicht bereits festes Regelwerk.

## 5. Charaktere und offene Regelfragen

| Charakter/Typ | Nutzeridee | Vor Umsetzung klären |
| --- | --- | --- |
| Doctor | Schallschraubenzieher öffnet Türen | Reichweite; freie Sicht; welche Türen; automatisch oder Probe; Verbrauch/Abklingzeit |
| Dalek | Schuss bis drei Felder, nur gerade Richtungen | Vier orthogonale Richtungen? Zielauswahl, Deckung, Schaden, Trefferwurf, Türen/Wände blockieren? |
| Weinender Engel | Gegner im Sichtfeld setzt ein bis drei Runden aus | Definition Sichtfeld/Blickrichtung; ein Ziel oder mehrere; Schutz/Resistenz; wann zählen und enden Runden? |
| Weitere Figuren | Nutzer nennt sie später | Namen, vorhandene Anzahl und gewünschte Besonderheiten; Team ergibt sich aus Spielerwahl oder Gegner-Hinzufügen |

Engel: Die genannte Lähmungsfähigkeit beibehalten, nicht stillschweigend durch die Serienregel
„Beobachten stoppt Engel“ ersetzen. Diese wäre eine zusätzliche optionale Idee. Ein bis drei
komplette Runden Pause kann für Spieler lang sein; eventuell Hilfsaktion oder Befreiung anbieten,
aber nur als noch unbestätigten Balancing-Vorschlag behandeln.

Weitere offene Grundfragen: Lebenspunkte oder zeitweilige Zustände? Ausscheiden erlaubt?
Gemeinsame Sicht oder verdeckte Informationen? Können Figuren Felder teilen? Mindestspielerzahl?
Gewünschte Dauer und Schwierigkeitsgrad? Mehrere Figuren pro Spieler?

### Bestätigter Beitritts- und Gegnerablauf (25.10.2026)

1. Name und Beitrittscode eingeben, dann Charakter-Steckbriefe ansehen und genau eine freie
   Figur wählen. Jede Figuren-ID wird ausschließlich an eine Person vergeben; gleichzeitige
   Anfragen werden am Host nacheinander verbindlich gespeichert. Eine Netzwerkunterbrechung
   oder wiederholte Anfrage erzeugt keine zweite Vergabe.
2. Portal zeigt WLAN/Adresse/Code und „gewählt / beigetreten“ sowie bis zu vier Zuordnungen.
   „Beigetreten“ und „Figur gewählt“ sind unterschiedliche Zustände.
3. Sobald alle beigetretenen Spieler gewählt haben, kann der Host übrige bekannte Figuren
   als Gegner hinzufügen. Keine automatische Aufnahme sämtlicher Reste. Partner sind nie
   gegenseitig Gegner. Eine Figur kann nicht zugleich Spielerfigur und Gegner sein.
4. Nach den Spielerzügen folgen die hinzugefügten Gegner nacheinander. Benötigt ein Gegner
   eine physische Bewegung, bestimmt der Host aus allen Partnern zufällig einen Helfer.
   Wiederholtes Anfordern desselben offenen Auftrags würfelt nicht neu. Der Helfer bestätigt
   die ausgeführte Tischbewegung. Alle übrigen Handys warten im eigenen Steckbrief
   (seit 0.6.0); die TARDIS ist zusätzlich im Tab „Spiel“ erreichbar.
5. Bei einem Angriff sieht das Ziel beide Steckbriefe und die gespeicherte Ereignisbeschreibung.
   Nur das Ziel kann diese Anzeige bestätigen. Erst nach offenen Bestätigungen kann der Host
   den Gegnerzug abschließen; nach dem letzten Gegner beginnt die nächste Runde.

**Aktueller Ausbaugrad:** Drei Katalogeinträge (Doctor, Dalek, Weinender Engel), ausschließlich
weil diese bereits vom Nutzer genannt wurden. Keine weiteren Serienfiguren erfunden. Daher
können aktuell maximal drei unterschiedliche Figuren gewählt werden, obwohl vier Spielerplätze
vorhanden sind. Für vier spielende Personen braucht es mindestens eine weitere benannte Figur;
für zusätzliche Gegner entsprechend weitere verfügbare Figuren. Mehrere Exemplare eines Typs
werden später nach tatsächlichem Inventar als separate Figuren-IDs modelliert.

Steckbriefkarten haben Bildbereich, Beschreibung, Ausdauer, Kraft und Sonderfähigkeiten.
Ab 0.4.0 sind für die drei bekannten Figuren lokale SVG-Illustrationen hinterlegt. Echte
Figurenfotos fehlen weiterhin. Native Portal-Avatare zeigen stilisierte Charakterbilder und
bevorzugen später vorhandene JPEGs unter `app/src/main/assets/portraits/<charakter-id>.jpg`.
Für die Browser-Steckbriefe wird der Bildpfad im Charakterkatalog gepflegt. Alles funktioniert offline.
Ausdauer und Kraft stehen ausdrücklich auf „Noch festzulegen“; Fähigkeitstexte geben die
bisherigen Nutzerideen wieder und sind noch keine implementierten Kampfregeln.

Gegnersteuerung ist derzeit ein moderierter Technikablauf: Der Host fordert die Bewegung an,
nennt Weg/Ziel und trägt die konkrete Angriffsfolge ein. Automatische Entscheidung, Positionen
von Gegnern, Reichweiten, Treffer/Schaden und Statusdauer folgen mit Brettmodell und Regeln.
Die Angriffsanzeige verändert bisher keine Ausdauer- oder Statuswerte von selbst. Das Wort
„aktiviert“ bezeichnet den Ansichtswechsel in einer geöffneten Webseite, nicht Fernentsperren.

Vorhandene 0.1.0-Spielstände behalten Runde, Positionen und Anmeldung. Vor Fortsetzung müssen
zuerst Figuren gewählt werden; es wird keine zufällige Figur zugewiesen. Ab 0.2.0 werden zusätzlich Gegneraufträge/Angriffsdaten gespeichert; ab 0.3.0 auch Wurf,
Entscheidungsphase und Restschritte. Beim Laden älterer Formate beginnt der aktuelle Spieler
mit dem neuen Würfelschritt; ein alter letzter Wurf wird nicht als aktuelle Zugberechtigung benutzt. Die Datei heißt aus Kompatibilitätsgründen
weiter `session-v1.drw`; maßgeblich ist die im Inhalt gespeicherte Formatnummer.

## 6. Physisches Inventar und Brettgenerator

Seit 0.8.0 ist der manuelle 20×20-Editor vor dem Spielstart umgesetzt (Abschnitt 8b).
Er erfasst vorhandene Teile ohne unbekannte Stückzahlen zu erfinden; ein Generator fehlt noch.

### Noch vom Nutzer zu erfassen

| Teil | Anzahl | Maße/Verbindungen | Status |
| --- | --- | --- | --- |
| Rechteckiges Feld ohne Wand | unbekannt | Breite/Tiefe, Magnetpositionen | erfragen |
| Feld mit einer geraden Wand | unbekannt | Wandseite, Tür möglich? | angekündigte Zählung |
| Eckfeld | unbekannt | zwei benachbarte Wände; Drehung in 90°-Schritten | im Editor vorhanden |
| Feld mit parallelen Wänden | unbekannt | Abstand, Gangbreite | angekündigte Zählung |
| Einzelwände/Türen | unbekannt | fest am Feld oder frei aufsetzbar? | erfragen |
| Verließ | unbekannt | Feldanzahl, Zugänge, Aufstellmaß | genannt |
| Friedhof | unbekannt | Feldanzahl, Zugänge, Aufstellmaß | genannt |
| Brunnen | unbekannt | blockiert Feld oder begehbar? | genannt |
| Figuren | unbekannt | Name, Anzahl, Sockelgröße | Nutzer zeigt sie später |
| Portalhalterung | unbekannt | Portal belegt zwei Rasterfelder; Telefon, sichtbare Displayfläche fehlen | zweifeldrig im Editor |

Zusätzlich klären: Entspricht jedes gedruckte Plättchen einem Bewegungsfeld oder mehreren?
90°-Drehungen sind im Editor ausdrücklich gewünscht; passt das physische Magnetraster in jeder Drehung?
Maximale Tischfläche und gewünschte Spieldauer? Müssen alle Teile benutzt werden?
Was bedeutet „extra Raum“ baulich: abgetrennt oder über einen Gang angeschlossen?

### Späterer Inventargenerator – noch nicht implementiert

- Inventar mit stabilen Typ-IDs, Stückzahlen, Abmessungen, zulässigen Drehungen und Kanten
  (offen/Wand/Tür/Anschluss) modellieren. Doppelseitige Wände und inkompatible Anschlüsse erkennen.
- Plan mit konkreter Teilezuordnung, Rasterkoordinaten, Ausrichtung, Türen, Sonderorten und
  Startpositionen erzeugen. Keine Teile erfinden oder Inventarzahlen überschreiten.
- Erreichbare Wege, zugängliche Sonderorte, Spielerstart und mindestens einen geeigneten
  Zusatzraum prüfen. Regeln für zunächst verschlossene Räume mit erreichbarem Öffnungsweg.
- Rechteckige Maße und Tischgrenze berücksichtigen. Bei unmöglichen Vorgaben verständlich
  erklären, welche Teile fehlen; keine stillschweigende ungültige Karte erzeugen.
- Generator-Seed UND den konkreten fertigen Plan speichern; Seed allein reicht bei späteren
  Generatoränderungen nicht für zuverlässigen Wiederaufbau.
- Aufbaubild mit Koordinaten, Nordpfeil, Stückliste und Schrittfolge. Spieler bestätigen Aufbau
  und Startaufstellung vor dem ersten Zug. Bestehenden manuellen Editor als zusätzliche Aufbauoption beibehalten.

## 7. Speichern und Fortsetzen – Endziel

Ein vollständiger späterer Spielstand enthält:

- Format-/Regel-/Szenarioversion, Partie-ID, Name, Zeitstempel und Missionsparameter.
- Vollständiges Inventarprofil und tatsächlich verwendeten Aufbau, jedes Teil mit Drehung und
  Koordinate; Türen, Verließ/Friedhof/Brunnen und Portalstandort.
- Figuren, Besitzer, Positionen, Blickrichtung, Eigenschaften, Inventar, Ressourcen,
  Lebens-/Statuswerte, Effektdauer und ggf. Gegnerzustand.
- Runde, Phase, aktiven Spieler, bereits verbrauchte Aktionen, Ziele und Bedrohungsstand.
- Bereits ausgewürfelte Resultate und angewandte Konsequenzen mit stabilen Ereignis-IDs;
  Zufall nicht beim Neuladen erneut ausführen. Später erweiterbares Ereignisprotokoll.
- Bei geheimen Informationen gefilterte Spieleransichten; Host behält vollständigen Zustand.
- Nach gültiger Aktion speichern, bevor Erfolg bestätigt wird. Wiederholte Netzwerkanfragen
  dürfen keine zweite Bewegung oder neuen Würfelwurf auslösen.
- Mehrere benannte Partien, Export/Import, Versionsmigration und Wiederaufbauansicht.
  Beim Laden erst Plan und Figurenpositionen anzeigen, dann Fortsetzung bestätigen.

**Heute umgesetzt:** Ein versionierter binärer lokaler Spielstand (Format 9, ältere Formate
1–8 lesbar) mit Spielern und geheimen Tokens, Beitrittscode, Partie-ID, gewählten Charakter-IDs,
Gegnerliste, Partner-/Gegnerphase, aktuellem Gegner, Bewegungshelfer und Bestätigung, gespeichertem
Angriffsereignis samt Ziel und Text, Runde, aktiver Person, Rasterkoordinaten, letztem W6,
Zugphase (würfeln/entscheiden/bewegen), aktuellem Wurf und verbleibenden Schritten,
Würfelereignis-ID und zugehöriger Spieler-ID, Go-Bestätigung, Zustandsrevision, 60 alte Protokollzeilen
und vollständige strukturierte Story-Einträge bis zur 8-MiB-Dateigrenze (Zeitstempel, gespeicherte Datumsanzeige, Sprecher, Spieler,
Aussage, Richtung), Raummeister-Auswahl/-ID/-phase/-deadline, Gefragte/Kandidat,
vollständigen 20×20-Kachelplan mit Ausrichtung, Teilfeldraster, Kerkerzuständen und Gegnerpositionen, Portalhinweise und die letzten 128 Anfragequittungen. Android `AtomicFile`
schreibt den Spielstand. Erst nach erfolgreichem Schreiben wird der neue In-Memory-Zustand
übernommen. Beschädigte/unbekannte Spielstände werden nicht automatisch überschrieben.

Benannte Sicherungen, Reset sowie Datei-Export/-Import sind in den Portal-Einstellungen verfügbar (Abschnitt 8d).
**Noch nicht:** Inventar-/Szenariogenerator, verbindliche Charakterwerte und ausgeführte Spezialregeln,
umfassende zukünftige Migrationen, vollwertiges Ereignisjournal oder Host-Wechsel.
Der gespeicherte Aufbau ist bereits am Portal und im Spiel abrufbar.
Eine Deinstallation bzw. das Löschen der App-Daten entfernt den lokalen Spielstand und interne Sicherungen;
exportierte Dateien deshalb separat aufbewahren.

## 8. Portal und Bedienidee

Host zeigt während der Partie möglichst eine beeindruckende Portalfläche. Steuerung soll gut
erreichbar sein, ohne den Portalcharakter im Spiel zu zerstören. Vorschläge: geschlossene Tür im
Wartezustand, flackernder Zeitriss bei Ereignissen, offenes Portal bei Reise/Rettung und roter
Alarm bei Gefahr. Öffnung, Würfelergebnis und Konsequenz sollen optisch zusammenpassen.

Der Nutzer kann später Zwischenanimationen bereitstellen. Dateiformat, Dauer, Schleifen,
Auflösung, sichtbare Handyfläche und Sound noch erfragen. MP4/Wiederholschleifen sind eine
mögliche technische Wahl; keine Dateien oder Lizenzrechte voraussetzen. Abspielen darf keine
Regel erneut auslösen. Das gespeicherte Ereignis ist maßgeblich, die Animation nur Darstellung.

Heute (0.5.0): Grafische Portal-Lobby mit statischem Doctor-Who-/Zeitwirbel-Hintergrund,
Spieleranzahl, vier übersichtlichen Plätzen, Spielernamen und Charakterporträts.
Auf der Startseite stehen keine Spielanleitung und kein Abenteuer-Untertitel; dort läuft keine TARDIS-Animation.
WLAN-Name, Passwort, Spielseite und Code stehen kompakt in einer Verbindungskarte.
Spielleitung und bisherige Test-/Verwaltungsfunktionen liegen auf einer separaten Seite.
Eine große TARDIS-Ansicht ist über die Spielleitung verfügbar. Noch keine Medienbibliothek/Videoimport. Nicht beteiligte Spieler sehen seit 0.6.0
standardmäßig ihren eigenen Steckbrief; im Tab „Spiel“ ist weiterhin eine eigene CSS-Animation
einer fliegenden blauen TARDIS verfügbar. Das betroffene Handy schaltet innerhalb der geöffneten Webseite zur Angriffsansicht;
ein ausgeschaltetes oder gesperrtes Telefon kann die lokale Website nicht verlässlich aufwecken.
Bildschirm-Timeout und Energieverhalten müssen am Gerät geprüft werden.

## 8a. Verbindung und Portal ab 0.4.0

**Fester Gestaltungsgrundsatz des Nutzers:** Einrichtung und Verbinden so einfach wie möglich.
Viele passende Grafiken und Animationen sollen in das Abenteuer hineinziehen. Technik im
Hintergrund halten; wenige klare Handlungsschritte, gut lesbare Anzeigen und keine Animation,
die eine notwendige Eingabe verdeckt. Bestehende Designwahl Spieler B / Portal A gilt weiter.

### WLAN

- Gewünschter einfacher Zugang: **SSID `Dr_Who`, Passwort `drwho123`** (8 Zeichen).
- Eine gewöhnliche App kann das feste Passwort nicht über die öffentliche Hotspot-API
  erzwingen: `SoftApConfiguration.Builder.setSsid/setPassphrase` sind im verwendeten
  Android-SDK 36 nicht öffentlich, trotz neuer Methode `startLocalOnlyHotspotWithConfiguration`.
  Keine Reflection oder versteckten/System-APIs als angeblich zuverlässige Lösung einbauen.
- Die Einrichtungshilfe versucht direkt die WLAN-Hotspot-Einstellungen zu öffnen und fällt
  bei Herstellervarianten auf Tethering-/Netzwerk-/allgemeine Einstellungen zurück.
  Der Nutzer richtet den System-Hotspot einmal ein und bestätigt die tatsächlichen Daten.
- Manuell bestätigte WLAN-Daten werden privat in der App gespeichert; sie sind keine von der
  App überprüfte Auskunft über den aktuellen System-Hotspot. Dies bleibt in der UI erkennbar.
- Alternativ kann Android einen Lokalhotspot automatisch mit eigenen Zugangsdaten starten.
  Dann werden ausschließlich die tatsächlichen Zugangsdaten der Reservation angezeigt.
- „WLAN-QR“ enthält die angezeigten WLAN-Daten. Spieler scannen ihn mit ihrer Kamera.

### Spielseite

- Ein echter mDNS-Hostname `drwho.local` wird mit JmDNS an der bevorzugten lokalen IPv4-
  Schnittstelle angekündigt, nicht nur ein DNS-SD-Dienst mit ähnlich klingendem Namen.
- HTTP bindet bevorzugt Port 80: dann lautet die Adresse `http://drwho.local`. Falls der Port
  nicht verfügbar ist, lautet sie `http://drwho.local:8080`. UI zeigt immer den tatsächlichen Port.
- Bei Namenskonflikten kann JmDNS den Host umbenennen; den tatsächlich angekündigten Namen
  anzeigen und als zulässigen HTTP-Host übernehmen. Nicht pauschal beliebige DNS-Namen zulassen.
- Auf manchen Hotspots/Spielergeräten kann Multicast oder `.local`-Auflösung ausfallen. Daher
  steht zusätzlich die direkte IP-Adresse bereit. „Seiten-QR“ verwendet bewusst diese direkte
  Adresse und trägt den sechsstelligen Beitrittscode als URL-Parameter ein.
- Das Öffnen eines QR-Codes meldet niemanden automatisch an: Name eingeben und beitreten.
- Netzwerk-/Namens-/QR-Funktionen sind implementiert, aber noch nicht auf den tatsächlichen
  Portal- und Spielerhandys geprüft. Keinen universell funktionierenden Hostnamen behaupten.

### Grafische Lobby

Seit 0.5.0 statischer, lokal mitgelieferter Doctor-Who-Hintergrund statt drehender TARDIS
auf der Startseite. Die eigene Canvas-TARDIS bleibt nur in der separaten Großansicht erhalten.
Vier Spielerzeilen mit Namen, gewählter Figur und Charakterbild sowie Zähler „beigetreten/bereit“.
Doctor, Dalek und Weinender Engel haben eigene stilisierte Illustrationen. Es sind keine echten
Figurenfotos und beim Doctor ist keine konkrete Schauspieler-Inkarnation festgelegt. Seit 0.5.0
werden in Steckbriefen und Avataren realistischere KI-generierte JPEGs verwendet.
Verwaltung/Gegner-Teststeuerung über „Spielleitung“ statt überladener Hauptseite.

### Verbindliche Startbildschirm-Anpassungen 0.5.0 (25.10.2026)

- Portalüberschrift exakt **Dr WHO**, ohne Unterstrich oder Bindestrich. Kein Abenteuer-Untertitel,
  keine Spielanleitung und keine TARDIS-Animation auf der Portal-Startseite. WLAN-/Seitenzugang,
  QR-Codes, Spielernamen, Charakterbilder und Spieleranzahl bleiben sichtbar.
- Spielerüberschrift und Seitentitel **Dr. Who**, Groß-/Kleinschreibung beibehalten; deutlichere
  Serifenschrift im bestätigten Archivdesign. Versionszeile nur **Version 0.5.0** statt „Technikdemo“;
  der Zusatz „lokal & ohne Konto“ wurde auf Wunsch ebenfalls entfernt.
- Auf Startbildschirmen heißt der sechsstellige Zugang **Portalcode** statt „Beitrittscode“
  oder „Code vom Spielleiter-Handy“. Repositoryname und voreingestellte SSID bleiben `Dr_Who`.
- Vor dem Spielerbeitritt aktuelle Anzeige **N von 4 Plätzen belegt · M frei**, alle 1,5 Sekunden
  aktualisiert. Registrierte Personen zählen sofort, auch ohne gewählte Figur. Bei Verbindungsfehler
  wird die Belegung als nicht verfügbar angezeigt, keine erfundene Zahl und kein stiller Beitritt.
- `GET /api/lobby` ist ohne Anmeldung erreichbar und liefert ausschließlich `occupied` und
  `capacity`. Keine Namen, Figuren, Spielzustände, Portal- oder Wiederbeitrittscodes veröffentlichen.
  Authentisierung von `/api/state` und allen Spieleraktionen bleibt unverändert.
- Blaue TARDIS als adaptives Android-App-/Launcher-Icon umgesetzt. Das bisher abstrakte
  Portal-Symbol wurde ersetzt. App-Name im Launcher: **Dr. Who**.
- Charakterauswahl als horizontal wischbares Kartenkarussell mit Einrasten pro Karte und
  weichem Verblassen an beiden Sichtkanten. Zusätzlich Vor-/Zurück-Pfeile, Kartenposition
  und Tastaturbedienung (Pfeile/Home/End). Eine vergebene Karte bleibt gesperrt; regelmäßige
  Zustandsabfragen und Besitzänderungen erhalten die gerade betrachtete Karte. Reduzierte
  Bewegungseinstellungen werden bei der Pfeilnavigation berücksichtigt.
- Realistischere KI-generierte JPEG-Porträts für Doctor, Dalek und Weinenden Engel ersetzen
  die bisherigen SVGs in den Steckbriefen; auch Portal-Avatare verwenden diese JPEGs. Keine
  echten Figurenfotos oder offiziellen Serienfotos. Der Doctor ist eine freie Interpretation,
  keine festgelegte Schauspieler-Inkarnation. Tatsächliche Figurenfotos können später dieselben
  Bildpfade ersetzen. Die alten SVGs bleiben als bearbeitbare Illustrationen im Repository.
- Spielregeln unverändert. Speicherformat 5 ergänzt eine stabile Würfelereignis-ID und Spieler-ID;
  Format 1–4 bleibt lesbar. Fliegende TARDIS auf wartenden Spielerhandys
  und separate große TARDIS über „Spielleitung“ bleiben erhalten.

Hintergrunddatei: `app/src/main/res/drawable-nodpi/portal_background.jpg` (768×1152, offline).
Erstellt mit der integrierten Bildgenerierung; kein offizielles Serienbild. Endgültiger Hintergrund-Prompt:

> Use case: stylized-concept. Asset type: static background artwork for the Android portal start screen of a Doctor Who tabletop companion. A recognizable blue TARDIS police box stands in a sweeping cosmic time vortex of deep midnight navy, teal and restrained cyan light with tiny distant stars, cinematic painterly science fiction. Portrait composition, 1024x1536. Place the TARDIS in the upper third toward the right; the center and lower two thirds remain very dark and calm so opaque UI connection cards can sit in front. Elegant immersive atmosphere, no people, no interface mockup, no lettering, no logos, no watermark. This is a still background, not an animation.

Porträtdateien: `app/src/main/assets/portraits/{doctor,dalek,weeping-angel}.jpg` (bis 750×1000).
Erstellt mit der integrierten Bildgenerierung. Endgültige Porträt-Prompts:

- Doctor: Use case: photorealistic-natural. Asset type: realistic character dossier portrait for a Doctor Who tabletop app. The Doctor, a warmly charismatic fictional adult male Time Lord with an original face (not a specific actor), tousled brown hair, a long dark coat and subtly patterned waistcoat, holding a slender glowing sonic screwdriver. Waist-up centered portrait, direct engaging gaze, believable skin and fabric details, cinematic photographic realism, restrained warm gold key light and cool petrol-blue rim light, softly blurred TARDIS console interior. Vertical 3:4 crop, head centered with generous space above and around so it also crops to a circular avatar. One character only. No lettering, no cards, no statistics, no borders, no watermark. Match an elegant parchment, petrol and brass character-selection interface.
- Dalek: Use case: photorealistic-natural. Asset type: realistic character dossier portrait for a Doctor Who tabletop app. A single recognizable bronze Dalek from Doctor Who, detailed domed head with one long central eyestalk glowing cool blue, ribbed midsection with plunger arm and ray gun, armored tapered skirt with spherical hemispheres. Centered three-quarter view, entire upper body and most of skirt visible. Cinematic photographic realism, believable aged brass and bronze textures, warm gold key light, cool petrol-blue rim light, softly blurred dark science-fiction corridor. Vertical 3:4 composition with generous space around the centered subject for an optional circular avatar crop. Match elegant parchment, petrol and brass UI. No people, no text, no interface, no borders, no watermark.
- Engel: Use case: photorealistic-natural. Asset type: realistic character dossier portrait for a Doctor Who tabletop app. A single recognizable Weeping Angel: a weathered grey stone female angel statue with large feathered wings, wavy carved hair, long draped classical robes, both hands covering its face in the iconic weeping pose. Centered waist-up portrait, wings symmetrically framing the subject. Cinematic photographic realism, convincing eroded stone texture and subtle moss, moody cool petrol-blue lighting with a restrained warm gold edge light, softly blurred old cemetery at dusk. Atmospheric and eerie but no gore. Vertical 3:4 crop, generous room above head and around for a circular avatar crop. No lettering, no interface, no borders, no watermark.

### Portal-Spielansicht und inszenierter W6 ab 0.5.0

**Bestätigter Nutzerwunsch:** Nach Spielstart soll das Portal die wichtigsten Spielereignisse
anzeigen: aktiver Spieler, Zug von Feld X nach Y, ausgeführte Aktion und animiertes Würfelergebnis.
Auch Spielerhandys würfeln sichtbar etwa 2–3 Sekunden, bevor die Augen aus Nebel erscheinen und
kurz aufblitzen. Dies ist eine Darstellungsvorgabe und ändert keine Spielregel.

**Umgesetzt:**

- Beim Start und Fortsetzen öffnet die native Portal-Spielübersicht automatisch: Runde,
  aktueller Spieler/Charakter oder Gegner, Feld/Reststrecke bzw. Aufgabe, letzter W6, letzte fünf
  Ereignisse und Positionen des Teams. „Portal-Lobby“ und „Spielleitung“ bleiben erreichbar.
- Bewegung protokolliert nun Ursprung UND Ziel, z. B. „Alice zieht von A1 nach A2“. Aktionswahl,
  Gegnerhelfer und Angriffsfolgen stammen wie bisher aus dem gespeicherten Spielzustand.
  Eine protokollierte Test-Aktionswahl hat weiterhin noch keine berechnete Charakterwirkung.
- Native Würfelinszenierung (Canvas/ValueAnimator) und 3D-CSS-Würfel im Spielerbrowser laufen
  jeweils 2,6 Sekunden mit Drehen/Taumeln, Nebel und einem einzelnen kurzen Lichtimpuls.
  Der Spieler sieht das endgültige Ergebnis und seine Bewegungs-/Aktionswahl erst nach Ablauf.
  Währenddessen bleiben Würfel-/Bewegungs-/Aktionsknöpfe gesperrt und Ergebnistext/Log verborgen.
- Das Ergebnis wird zuerst einmalig am Host berechnet und atomisch gespeichert. Jeder neue
  Wurf erhält eine gespeicherte Ereignis-ID plus Spieler-ID (Format 5). Regelmäßiges Polling,
  Retry und Bewegung erzeugen keine neue Wurf-ID und keine erneute Würfelentscheidung.
- Spielerbrowser merkt sich vollständig gezeigte Würfelereignisse lokal pro Partie; Portal
  merkt sie sich in privaten Einstellungen. Dieselbe Ereignis-ID startet nicht bei jeder
  Aktualisierung neu. Ein neuer Wurf mit derselben Augenzahl besitzt eine neue ID und animiert.
  Unterbrechung mitten in der Animation kann die Darstellung nach Rückkehr wiederholen,
  niemals den gespeicherten Zufall. Alte Formate haben noch keine Ereignis-ID.
- Portal erhält Updates wie bisher im 1,5-Sekunden-Takt. Darstellung auf Portal und Browser
  kann dadurch zeitlich versetzt beginnen; keine exakt synchronisierten Videoeffekte behaupten.
- Reduzierte Bewegung im Browser: statische Nebelanzeige mit derselben Wartezeit, ohne Drehen
  und Blitz. Deaktivierte Android-Systemanimationen zeigen den gespeicherten Wert unmittelbar.
- Reale Handyperformance, Timing bei App-Hintergrund/Display-Sperre und native Optik bleiben
  Gerätetests. Browseroberfläche kann keine gesperrten Handys fernentsperren.

### Versionsabgleich und veraltete Client-Anmeldung ab 0.5.1

**Vom Nutzer beobachteter Fehler:** Alte APK mit begonnener Partie deinstalliert, danach 0.5.0
frisch installiert. Der Browser behielt seinen alten Token und zeigte nur „Diese Anmeldung gehört
nicht zu dieser Partie“. Der Host-Spielstand wurde durch die Deinstallation entfernt; die neue
Installation hat eine neue Partie-ID. Gleiche App-Version bedeutet nicht dieselbe Partie.

- `GET /api/info` liefert ohne Anmeldung nur `appVersion`, `protocolVersion` (aktuell 1) und
  eine zufällige, nicht geheime `sessionId`. Keine Namen, Figuren, Tokens oder Portalcodes.
  `/api/lobby` bleibt auf die reine Platzbelegung begrenzt; `/api/state` bleibt authentisiert.
- Android meldet die echte Build-Version aus `BuildConfig.VERSION_NAME`; native Versionszeile
  nutzt dieselbe Quelle. Bei Versionserhöhung auch `CLIENT_VERSION` und die sichtbare Versionszeile
  in `player.html` anpassen. Der Client prüft Portal-Metadaten beim Laden und alle zehn Sekunden.
- App- oder Protokollversion weicht ab: alte Ansichten sperren und „Spieleroberfläche neu laden“
  anbieten. Tokens und unbestätigte Anfrage-ID bleiben für einen sicheren Retry nach dem Neuladen
  erhalten. Keine gültige Anmeldung allein wegen eines Softwareupdates löschen.
- Gleiche Version, aber neue Partie-ID: nur alte Client-Anmeldung, noch offene Client-Aktion und
  Würfel-Präsentationsstatus verwerfen. Automatisch Anmeldemaske zeigen, Namen beibehalten und
  aktuelle Platzbelegung abfragen. Keine automatische Neuanmeldung, kein zusätzlicher Platzbezug.
- Auch ein tatsächlicher HTTP-401 bei Zustands- oder Aktionsanfrage führt zur Anmeldemaske statt
  in die Sackgasse. Nicht mehr gültige Aktionen werden niemals in die neue Partie übertragen.
- Ursprüngliche Beitrittsanfrage bleibt gespeichert: Wenn Name und Code unverändert sind, wird
  dieselbe Anfrage-ID erneut verwendet. Der vorhandene Host kann damit denselben Platz auch nach
  Spielstart zurückgeben; keine alleinige Identifizierung über den Spielernamen und kein Token-Bypass.
  Bei anderem Namen/Portalcode entsteht erst nach explizitem „Beitreten“ eine neue Anmeldung.
- Antworten einer alten Token-Anfrage nach erfolgreichem Neubeitritt werden ignoriert. Ein normaler
  Netzfehler löscht die Anmeldung nicht. Protokollprüfung ersetzt keine Authentisierung.
- Soforthilfe für 0.5.0: aktuelle Portal-Adresse in einem privaten/Inkognito-Tab öffnen und mit
  aktuellem Portalcode neu beitreten. Das hilft nur beim Einstieg in die neue Partie, stellt keinen
  durch Deinstallation gelöschten Host-Spielstand wieder her. Browserdaten nicht pauschal löschen.
- 0.5.1 verwendet den gesicherten Schlüssel von 0.5.0. Als Update installieren, 0.5.0 nicht vorher
  deinstallieren. Geschützter Geräte-/Adresswechsel, verlorener Browsernachweis und Spielstand-
  Export/Import bleiben eigene offene Funktionen.

### Spieler-Tabs, Steckbrief und Würfellandung ab 0.6.0

**Neuester Nutzerwunsch hat Vorrang vor der früheren reinen TARDIS-Warteanzeige:**

- Nach Spielstart drei obere Tabs **Spiel**, **Steckbrief**, **Was passiert ist**. Der Verlauf
  enthält eine auf-/zuklappbare Rubrik; der Klick auf seinen Tab öffnet sie. Gewählte Tabs und
  der Aufklappzustand bleiben bei normalen Zustandsabfragen erhalten. Tastaturbedienung über
  Pfeile/Home/End, passende ARIA-Tabrollen und sichtbare Fokusmarkierungen sind vorhanden.
- Kompakter Kopf und beim Scrollen oben bleibender Hinweis: **Du bist am Zug!** mit hervorgehobener
  Farbe oder Name des aktuell aktiven Spielers/Gegners. Der Hinweis bleibt auch beim Lesen des
  Steckbriefs/Verlaufs sichtbar. Browser kann weiterhin kein gesperrtes Handy fernentsperren.
- Während andere an der Reihe sind automatisch den eigenen Steckbrief zeigen. Beim Beginn des
  eigenen Zugs automatisch zum Spieltab wechseln. Neuer Gegnerhelfer-Auftrag oder Angriff hat
  Vorrang und öffnet ebenfalls den Spieltab mit eindeutigem Hinweis. Manuelles Nachlesen ist
  anschließend weiterhin möglich; Polling zwingt nicht ständig zum Spieltab zurück.
- Das Steckbriefbild erhält sein vollständiges 3:4-Format mit `object-fit: contain`. Keine
  beschnittenen Köpfe/Körper im eigenen Steckbrief. Auch die Auswahlkarte enthält das ganze Bild
  innerhalb ihrer kleineren Bildfläche; native Portal-Avatare dürfen weiterhin rund zugeschnitten sein.
- Client-Würfel besitzt durchgängig sechs feste nummerierte Seiten, auch während der Animation.
  Die gespeicherte Augenzahl bestimmt die Landedrehung. Nach Ablauf werden weder Würfel-DOM noch
  Augen ausgetauscht. Animation und ruhende Darstellung haben dieselbe Endtransformation.
- Der Würfel selbst bleibt vollständig deckend und behält `preserve-3d`; veränderliche Deckkraft
  auf dem rotierenden Würfel würde seine 3D-Flächen abflachen. Drehung und Nebelbewegung über
  CSS-Transformationen mit Compositor-Hinweis. Kein neuer Zufall im Browser. Dauer weiterhin
  2,6 Sekunden, Nebel/kurzer Lichtimpuls und zurückgehaltene Ergebnisbeschriftung bleiben erhalten.
  Bei reduzierter Bewegung statischer Nebel bis zur gespeicherten Aufdeckung.
- Client-/Host-Regeln, API-Protokoll 1, Anmeldung und Speicherformat 5 unverändert. Die native
  Portal-Würfelanimation wurde nicht geändert. Frame-Rate auf tatsächlichen Android-Spielerhandys
  ist weiter zu prüfen; mobiles Chromium ersetzt keinen Hardwaretest.

### Your move / Go, gerichtete Aktionen und Story ab 0.7.0 (26.10.2026)

**Neue verbindliche Nutzerwünsche:**

1. Zu Beginn jedes menschlichen Spielerzugs Popup **Your move...** mit **Go**. Auf dem Portal
   wartet die Charakterkarte (Bild, Beschreibung, Ausdauer/Kraft, Fähigkeiten) mit **Spielername
   ist dran…**. Erst nach bestätigtem Go verschwindet die Karte und beginnt die normale Zugbedienung.
2. **Was passiert ist** heißt überall **Story**, auch im Portal-Verlauf. Neue Einträge beginnen
   mit Datum/Uhrzeit und Sprecher, z. B. `20.03.2026 17:34 Uhr - Dr WHO: Ich gehe von Feld A1 zu Feld A2.`
   Charaktere sprechen in erster Person; Daleks erhalten gelegentlich zufällige Geräusche im Text.
3. Richtungsabhängige Aktionen öffnen ein weiteres Popup mit Nord/Ost/Süd/West. Vor dem Senden
   kann der Spieler abbrechen. Noch nicht vereinbarte Reichweiten, Sichtwinkel und Wirkungen werden
   dadurch nicht automatisch zu fertigen Regeln.

**Tatsächliche Umsetzung:**

- `beginTurn` ist eine authentisierte, ausschließlich für den aktuellen Spieler erlaubte Aktion.
  `turn.accepted` wird am Host vor der Antwort atomisch gespeichert. Ohne Go lehnt der Host
  Würfeln, Bewegung und Aktionswahl ab; Gegnerhilfe und Angriffsbestätigung bleiben eigene Abläufe.
  Go selbst kostet keine Bewegung/Aktion und schaltet nicht zum nächsten Spieler weiter.
- Jeder nächste menschliche Zug setzt Go zurück. Wiederholte/parallel gesendete Anfrage-IDs
  bestätigen nur einmal; kein doppelter Story-Eintrag. Bereits bestätigtes Go bleibt nach Neustart
  bestätigt. Vorhandene Spielstände mitten in Bewegung oder 5/6-Wahl werden ohne zusätzliches
  Go fortgesetzt; alte wartende Würfelzüge benötigen beim Fortsetzen erstmals die Bestätigung.
- Das Go-Popup wird nicht automatisch geschlossen, sondern folgt dem gespeicherten Hostzustand.
  Hintergrund ist gesperrt, Fokus bleibt im Popup. Bei verlorener Bestätigung kann **Go** denselben
  gespeicherten Auftrag erneut senden. Keine neue Anfrage für denselben unbestätigten Versuch.
- Doctor, Dalek und Weinender Engel haben im Katalog `actionNeedsDirection=true`. Die vier
  Richtungstasten senden die gewählte Richtung mit der gespeicherten Aktionsanfrage. Host prüft
  fehlende/ungültige Richtungen. Abbrechen/Escape vor Auswahl verändert nichts. Bei unbestätigter
  gesendeter Richtung bleibt nur Retry derselben Aktion möglich, keine zweite andere Auswahl.
  Türen, Treffer, Schaden, Reichweiten und Engelstatus sind weiterhin nicht berechnet.
- Story wird am Host erzeugt und in Speicherformat **6** persistiert: Ereignis-ID, Epoch-Millis,
  fertig formatierte Ortszeit des Portal-Handys (`dd.MM.yyyy HH:mm Uhr`), Sprecher, zugehöriger
  Spielername, Aussage und Richtung. Clients formatieren weder Datum noch Zufall neu. **Dr WHO**
  ist der Story-Sprecher des Doctors; weitere Sprecher heißen wie die bekannten Figuren oder Portal.
- Bewegungen nutzen die tatsächlich bekannten Feldnamen A1–F6. Räume/Raumnamen folgen erst
  mit Inventar/Brettmodell; keine Räume xx/yy erfinden. Aktionen benennen die gewählte Richtung
  und kennzeichnen, dass ihre eigentliche Wirkung noch aussteht.
- Dalek-Geräusche werden mit einem eigenen kosmetischen Zufallsgenerator bei einem Viertel
  seiner Aussagen aus drei Varianten gewählt. Dieser Generator verändert weder W6 noch Helferwahl.
  Bereits gewählte Aussage und Uhrzeit werden gespeichert und bleiben bei Retry/Neuladen identisch.
- Alle bis zu 60 gespeicherten Story-Einträge sind im aufklappbaren Story-Tab abrufbar, neueste
  zuerst. Der sichtbare Verlauf wird bei unverändertem Inhalt nicht ständig neu aufgebaut.
  Während der Würfelinszenierung wird keine neue Ergebnis-Aussage vorzeitig aufgedeckt.
- Frühere Protokolle hatten keine Uhrzeiten. Beim Lesen von Format 1–5 bleiben diese ehrlich als
  **Zeit nicht erfasst - Archiv** erhalten; keine rückdatierten oder erfundenen Zeitstempel.
  Spielstanddatei bleibt `session-v1.drw`, nun Inhaltformat 6. Format 1–5 bleibt lesbar.
- API-Protokoll ist jetzt **2**, App-Version **0.7.0 / Code 9**. Alte Seiten bekommen den bereits
  vorhandenen Neuladen-Hinweis. Browser-Anmeldung und Spielstand bleiben bei normalem APK-Update
  erhalten. Die private Signierung von 0.5.0 wird wiederverwendet, nicht deinstallieren.

### Beobachtung drwho.local am Portal-Handy (26.10.2026)

Der Nutzer konnte den Client auf dem APK-Handy selbst per IP öffnen, jedoch nicht per
`drwho.local`. Vollständige eingegebene Adresse, Port, Android-Version und aktive Netzroute sind
nicht bekannt; **Ursache nicht abschließend diagnostiziert**. Kein angeblich behobenes DNS-Problem
behaupten. IP-Erreichbarkeit bestätigt allein nicht die Namensauflösung.

- App bindet Port 80 oder 8080 und kündigt den lokalen Namen per JmDNS auf der lokalen
  Schnittstelle an. Vollständiges `http://` und tatsächlich angezeigten Port verwenden;
  bei 8080 also `http://drwho.local:8080`, nicht den nackten Namen ohne Port.
- Android löst `.local` per mDNS auf; Mobilfunk und VPN sind laut AOSP davon ausgeschlossen.
  Auf dem Hotspot-Handy kann die Resolver-/Netzroute deshalb relevant sein. Das ist eine mögliche,
  keine am Nutzergerät nachgewiesene Ursache. Multicast/Hotspot-/Browserverhalten bleibt Gerätetest.
- Auf demselben Handy ist `http://127.0.0.1` mit dem tatsächlichen Port ein Test-Ausweichweg;
  auf fremden Spielerhandys stattdessen die am Portal angezeigte IP oder den Seiten-QR verwenden.
- Browseranmeldungen gehören zu ihrem Ursprung: Zwischen Name, IP und Loopback wechseln kann
  einen weiteren Spielerplatz benötigen. Für eine laufende Partie dieselbe Adresse beibehalten.
- Referenz geprüft 26.10.2026: https://source.android.com/docs/core/ota/modular-system/dns-resolver

## 8b. Raummeister und physisches Raumgefüge ab 0.8.0 (26.10.2026)

**Verbindlicher neuer Nutzerwunsch:** Vor dem ersten Zug nach Start eine fliegende TARDIS mit
**der Raummeister wird gerade ausgewählt...**. Nur echte beigetretene Spieler werden zufällig
gefragt, ob sie das Raumgefüge wiederherstellen möchten. Nein fragt eine noch nicht gefragte
Person, Ja überträgt den Aufbau. Nach ausschließlich Nein entscheidet das Portal. Der Raummeister
zeichnet den tatsächlichen Tisch in ein **20×20-Raster**; währenddessen ist derselbe Aufbau am
Portal grafisch sichtbar. Speichern startet eine weitere Portalanimation und danach die Partie.
Große Ereignisanzeigen am Portal sollen 2–4 Sekunden dauern.

**Technische Interpretation der Portalentscheidung:** Wenn alle ablehnen, bestimmt der Host
zufällig einen der echten Spieler verbindlich zum Raummeister. Dieser erhält ein Informations-
Popup mit **Aufbau öffnen**. Es wird kein unbekannter physischer Aufbau automatisch erfunden.
Diese Interpretation ist in der Umsetzung verwendet und dem Nutzer mitgeteilt; falls mit
„Portal entscheidet“ stattdessen ein automatischer Brettgenerator gemeint war, gemeinsam ändern.
Inventar und Stückzahlen für einen solchen Generator fehlen weiterhin.

### Tatsächlicher Ablauf und Bedienung

1. Host startet erst, wenn alle angemeldeten Spieler Figuren gewählt haben. Beitritt und
   Figurenwechsel schließen dann; der Spielzug beginnt noch nicht.
2. Gespeicherte Phase `selecting` und Host-Deadline: **3 Sekunden** TARDIS-Flug mit exaktem Text.
   Danach eine zufällige Person als Kandidat speichern und am eigenen Client Ja/Nein fragen.
   Die Frage läuft nicht nach einer erfundenen Frist ab. Offline-Kandidat wartet auf Wiederverbindung.
3. Nein wird atomisch gespeichert; aus den noch nicht gefragten Partnern zufällig weiterwählen.
   Kein Kandidat wird in derselben Auswahl zweimal gefragt. Nach dem letzten Nein verbindliche
   Zufallswahl aus allen Partnern. Wiederholte Anfrage-ID oder Neustart lost nicht erneut aus.
4. Ja bzw. verbindliche Wahl: Phase `building`. Nur dieser geheime Browser-Token darf Kacheln
   und Startfelder ändern. Andere Clients sehen den Namen und warten; das Portal zeigt den Plan live.
5. Palette unten: **Ecke** (zwei benachbarte Wände), **Ohne Wand**, **Gang** (zwei gegenüberliegende
   Wände), **Tür** auf einer Seite, **Kerker**, **Brunnen**, **Portal** über genau zwei Rasterfelder.
   Ausgangsausrichtung Ecke Nord/Ost, Gang Nord/Süd und Tür Nord. Vier 90°-Drehungen.
   Friedhof und einzelnes gerades Wandstück bleiben späteres Inventar, keine zusätzlich erfundenen
   Palettenelemente. Mengen-/Magnet-/Maßbegrenzungen sind noch nicht bekannt.
6. Kacheln mit Maus oder Touch aus der Palette ziehen. Ein farbiger Vorschauumriss zeigt Ziel
   und Kollision; die sichtbare Karte übernimmt erst die bestätigte Speicherung am Host.
   Antippen einer platzierten Kachel dreht 90°. Ziehen innerhalb der Matrix verschiebt sie;
   in den Mülleimer ziehen entfernt sie. Portal immer als ein Teil über beide Felder behandeln.
   Rand, Überlappungen und versperrte Drehungen lehnt der Host ohne Änderung ab.
7. Seit 0.10.1 keine Kachelplatzierung per Antippen eines leeren Felds; nur Ziehen aus der Palette. Eine platzierte
   Kachel antippen und per Mülleimer-Taste entfernen; auf fokussierter Kachel auch Entf/Rücktaste.
   Seit 0.10.1 verschieben Pfeiltasten keine Kacheln mehr. Raster ist scrollbar, am Rand nach kurzem Halten beim Ziehen
   mitlaufend; **Ganzes Raster / Vergrößern** wechselt die Übersicht. Keine diagonalen Spielzüge.
8. Startfiguren bekommen zunächst die ersten freien vorhandenen Felder als sichtbare Vorschläge.
   **Startfiguren setzen** erlaubt dem Raummeister pro Spieler ein anderes belegtes Feld. Die
   Nummern entsprechen der Teamliste; keine zwei Figuren auf einem Feld. Das ist ein manueller
   Aufbauvorschlag, keine vereinbarte Szenario-Startregel. Figuren auf dem Tisch entsprechend aufstellen.
9. **Speichern & starten** verlangt mindestens ein eigenes belegtes Feld pro Spieler. Speichert
   den kompletten Plan mit Koordinaten, Typ, Ausrichtung und Figurenpositionen; dann **3 Sekunden**
   TARDIS-Flug „Das Raumgefüge steht. Das Abenteuer beginnt!“. Erst danach erster Zug mit Go/W6.
10. Jede bestätigte Bauänderung ist bereits als Entwurf gespeichert, nicht erst der Abschluss.
    Neustart während Anfrage, Bau oder Startfilm setzt an dieser Stelle fort. Ein während der
    Abwesenheit abgelaufener Film wird beim nächsten Host-Tick abgeschlossen, nicht als neues
    Spielereignis wiederholt. Jede neue Aufbauauswahl besitzt eine eigene stabile ID.

### Karte im Spiel und spätere Fortsetzung

- Koordinaten **A1 bis T20** folgen dem Raster. Leere Stellen sind **keine Spielfelder**. Ein Portal
  belegt zwei benachbarte Felder; beide können Figuren tragen, ohne Stapeln auf demselben Feld.
- Bewegung prüft nun vorhandene Zielfelder und massive Wände beider angrenzenden Kacheln.
  Richtungswechsel und exakt gewürfelte Strecke gelten weiter. Eine abgelehnte Bewegung kostet
  keinen Schritt. Was bei insgesamt nicht erfüllbarer Strecke passiert, bleibt eine offene Regel.
- Türfelder sind **vorläufig Durchgänge**. Es gibt noch keinen offenen/geschlossenen Türzustand
  oder ausgeführte Schraubenzieher-Wirkung. Kerker und Brunnen sind Gelände-Markierungen ohne
  neue Kampf-/Heilregeln. Portalreisen, Sichtlinien, Zusammenhängigkeit und Mission fehlen noch.
  Ein isolierter Raum wird nicht automatisch mit unbekannten zusätzlichen Teilen verbunden.
- Clients zeigen im Spiel den gespeicherten tatsächlichen Plan; am Portal ist er unter
  **Raumgefüge anzeigen** mit aktuellen Figurenpositionen wieder abrufbar. Der native Plan vergrößert
  den belegten Ausschnitt für Lesbarkeit, ohne Koordinaten zu ändern. Das 20×20-Modell bleibt erhalten.
- Vorhandene bereits laufende Spielstände aus Format 1–6 bleiben auf ihrem bisherigen freien
  6×6-Testfeld, damit ein Update keine Partie zurücksetzt. In **Spielleitung → Raumgefüge erfassen /
  bearbeiten** kann man eine laufende Partie ausdrücklich pausieren und die Auswahl nachholen.
  Nach Bestätigung bleibt ein vorhandener Plan als Entwurf erhalten; Runde, Zug, Würfel und
  Go-Status bleiben gespeichert. Alte Testfeldpositionen werden durch sichtbare Aufbauplätze ersetzt.
- Eine lokale Partie und ihr Plan sind über App-Neustarts und korrekt signierte Updates dauerhaft
  gespeichert. Weiterhin kein Menü für mehrere Partien, kein Export/Import und kein Schutz vor
  Deinstallation oder Löschen der App-Daten. Keine Sicherung auf GitHub der privaten Spielerdaten.

### Portalereignisse und Technik

- Großflächige Portalhinweise für Zusage, Ablehnung, verbindliche Portalwahl und gemeldete Angriffe.
  Jeder Hinweis hat eine gespeicherte ID. Native Ansicht zeigt neue Hinweise nacheinander jeweils
  **3 Sekunden**; danach Live-Aufbau oder normale Spielansicht. Gesehene Hinweise werden pro Partie
  in privaten Einstellungen gemerkt. Go-Karte wartet weiterhin bis zur tatsächlichen Bestätigung.
- Auswahl- und Startfilm haben gespeicherte Host-Deadlines; sie sind keine zweiten Zufallsquellen.
  Host-Oberfläche aktualisiert alle 500 ms, Clients wie bisher etwa alle 1,5 Sekunden. Keine perfekte
  Synchronität behaupten. Native TARDIS fliegt/rotiert mit lokal gezeichnetem Zeitwirbel ohne Medien-
  Download. Androids abgeschaltete Systemanimationen und Browser-Reduced-Motion beachten.
- **Speicherformat 7**, ältere Formate **1–6** lesbar. Enthält Auswahl-ID/-phase/-deadline, Kandidat,
  bereits Gefragte, Raummeister, verbindliche Wahl, vollständigen Kachelplan und Portalhinweise.
  Das Kopieren/Validieren des Zustands prüft Rand, Kollisionen und belegte Figurenpositionen.
- **API-Protokoll 3 / App 0.8.0 / Code 10**. Aufbauaktionen laufen authentisiert über bestehendes
  `/api/command`, weiterhin kein entfernter Host-Administrationsendpunkt. Rollen-, Revisions- und
  Quittungsprüfung am Host. Verlorene Antwort nur mit gleicher Anfrage-ID erneut senden. Keine
  gesamte Karte aus dem Browser blind übernehmen. Größenlimits gelten weiter.

## 8c. Falscher Portalcode und Rückkehr zur Anmeldung ab 0.8.1

Nutzer meldet: Nach einem falsch eingegebenen Portalcode kommt der Client nicht zurück und
muss den Code prüfen können. Der Host hat falsche Codes schon zuvor vor Vergabe eines Platzes
abgewiesen. Die bisherige Oberfläche meldete Fehler nur allgemein; der Rücksprung aus 0.8.0
räumte außerdem Aufbauansicht und feste Editorhöhe nicht auf. Das konnte die Anmeldung verdecken.

- Vor dem Senden Namen und genau sechs Ziffern prüfen. Fehlender/zu kurzer Code wird direkt
  am Codefeld erklärt. Während der Hostprüfung genau eine Anfrage, Felder vorübergehend gesperrt.
- Falscher sechsstelliger Code: Hostantwort HTTP 400 mit `errorCode=invalid_portal_code`, weiterhin
  keine Vergabe eines Spielerplatzes und keine Ausgabe des richtigen Codes. Anmeldedrosselung bleibt.
- Client bleibt bei der Anmeldung, markiert das Codefeld und wählt den Inhalt zum Überschreiben.
  Name bleibt erhalten, alle Felder und Beitreten werden wieder bedienbar. Sichtbare Fehlermeldung
  direkt im Formular; normale Belegungsabfragen löschen sie nicht. Bearbeiten entfernt alte Markierung.
- Abgelaufene Anmeldung räumt alle vier Popups, Aufbauansicht, feste Editor-/Spielklassen und
  sämtliche Hintergrundsperren auf. Auch ein Versionswechsel verlässt den alten Editor vollständig,
  bevor der Neuladen-Hinweis erscheint. Keine History-Zurück-Navigation und keine Partie zurücksetzen.
- Erfolgreiche Anmeldeanfrage zusätzlich als `saved.registration` behalten; bestehende Browser
  übernehmen ihren bisherigen Nachweis. Falsche Korrekturversuche überschreiben diesen Nachweis
  nicht. Stimmen Name und richtiger Code wieder, wird der ursprüngliche Anfrage-Nachweis benutzt:
  derselbe registrierte Platz kann auch nach Spielstart wieder geöffnet werden. Kein Token-Bypass.
- Netzwerkfehler melden Verbindung/Übertragung statt fälschlich einen ungültigen Code zu markieren.
  Wiederholung derselben unbestätigten Anmeldung verwendet weiterhin dieselbe Anfrage-ID.
- App/Client **0.8.1 / Code 11**; API-Protokoll **3** und Speicherformat **7** bleiben gleich.
  Keine Spielregeländerung. Bestehende Partien, Raumgefüge, Figuren und Züge werden erhalten.

## 8d. Vierer-Kacheln, vollständige Spielstände und einfacher Beitritt ab 0.9.0

Historischer Ausbau; Änderungen in Abschnitten 8e/8f haben Vorrang, insbesondere das globale Koordinatenraster.

Neue verbindliche Nutzerwünsche: Reset am Portalstart und in Einstellungen während der Partie;
vollständiges Speichern/Laden einschließlich Planung, aller Figuren und Story; wesentlich
weniger Anmeldung; kompakte Bildschirmseiten; größere Kacheln mit verschiebbarem Ausschnitt;
automatisches Einpassen des Portal-Plans; je Spieler drehbarer Himmelsrichtungskompass.
**Das Portal steht immer im Norden. Jede neue Kachel enthält 2×2 Bewegungsfelder.**

### Beitritt und Bedienung

- Neuer Client braucht **nur den Namen**, danach Figur wählen. Kein zusätzlicher Raum-/Portalcode.
  WLAN-Passwort bleibt. QR für WLAN und QR für Spielseite; die Spielseite enthält keinen Code mehr.
  Alte API-Anfragen mit Code werden aus Kompatibilitätsgründen weiterhin geprüft; neue lassen
  `code` weg. Spielertokens, maximal vier Plätze, exklusive Figuren und Hostverwaltung bleiben.
- Diese Vereinfachung vertraut auf das private Spiel-WLAN. Keine Freigabe ins Internet. Namen
  müssen eindeutig sein. Normale laufende Partie lässt keine neue fremde Person nachrücken.
- Clientseiten sind als feste Handyansichten angelegt: Auswahlkarussell, Spiel, Steckbrief,
  separate Karte und Story. Seit 0.10.1 zeigt Story alle Einträge als scrollende Sprechblasen; lange Texte und Dialoge dürfen
  intern scrollen. Die Brettansicht selbst ist ausdrücklich verschiebbar. Keine vollständige
  Scrollfreiheit für beliebige Schriftvergrößerung/kleinste Geräte behaupten.
- Portal-Lobby mit Umschaltung **WLAN & Zugang / Spieler** statt aller langen Karten übereinander;
  Einstellungen als Menüliste, kompaktere Spielleitung und Statusansicht. Realgerätetest ausstehend.
- Pro Browser Kompass mit links/rechts-Drehtasten in 90°-Schritten. Orientierung bleibt lokal nach
  Neuladen erhalten. N/O/S/W bewegen sich optisch passend zum Sitzplatz, gesendet werden immer
  dieselben Welt-Richtungen. Aktionsrichtung und Gegnerhelfer-Kompass verwenden diese Orientierung.
- Raummeister erhält vor dem Aufbau ein verpflichtendes Hinweis-Popup: **Das Portal steht im
  Norden!** Nord ist im Plan oben. Die Bestätigung gilt pro Partie und Aufbauauswahl; nach Laden
  mit neuer Partie-Epoche erneut bestätigen. Am Portal ist Norden ebenfalls sichtbar benannt.

### Kacheln und Bewegung

Die neue Palette enthält genau zehn Typen: glatte Steinplatten, Eckwände, einseitige Wand,
gegenüberliegende Wände, Kerker, Friedhof, Kompassfeld, Lava, Lava/Stein (zwei/zwei), Brunnen.
Alle belegen eine Kachel und vier Bewegungsfelder. Großflächige Symbole für Friedhof, Kompass
und Brunnen belegen grafisch die ganze Kachel. **Fette rote Außenbalken** markieren Wände,
innere dünne Teilung markiert die vier Schritte. Lava/Stein dreht mit der Kachel.

- Standard-Editor-Kachel 64 px statt 36 px; 20×20 Kachelplätze bleiben zur Abwärtskompatibilität
  erhalten. Der Ausschnitt lässt sich scrollen; **10 Kacheln breit / Große Kacheln** schaltet die
  Übersicht um. Das ist keine Verkleinerung/Abschneidung alter Pläne auf 10×10.
- Neue Bewegungskoordinaten 0–39 je Achse, Kachelkoordinaten weiter 0–19. Feldname z. B.
  **A1·1, A1·2, A1·3, A1·4** (oben links, oben rechts, unten links, unten rechts). Ein orthogonaler
  Teilfeldwechsel kostet genau einen gewürfelten Schritt. Keine Änderung der W6-/5/6-/Go-Regeln.
- Außenwände blockieren den Übergang zwischen Kacheln beidseitig, nicht die inneren Teilfelder.
  Kein Feld ohne platzierte Kachel. Figuren teilen weiterhin kein einzelnes Bewegungsfeld.
- Kerkerzustand offen/geschlossen wird gespeichert. Geschlossener Kerker sperrt seine Außenkanten;
  offene Kanten sind begehbar. Raummeister kann den Zustand im Editor mit eigener Taste ändern,
  während der Partie die Spielleitung über Einstellungen. Kein ungefragt erfundener Charakter-
  Aktionspreis oder neue Reichweite des Schallschraubenziehers.
- Lava-Schaden, Erholung am Brunnen, Friedhofseffekte und spezielle Missionen sind **noch offen**.
  Farben/Gelände sind vorhanden, aber nicht als fertige Schadens-/Heilregel ausgeben.
- Portal und Tür aus älteren Plänen bleiben ladbar, sind auf ausdrücklichen neuen Palettenwunsch
  nicht mehr in der neuen Auswahl. Früheres zweifeldriges Portal bleibt ein zusammenhängendes Teil.
- Alle Figuren einschließlich hinzugefügter Gegner erhalten gespeicherte Positionen. Raummeister
  kann auch deren Startfelder setzen. Der gewählte Gegnerhelfer kann physische orthogonale Schritte
  am eigenen Kompass melden und anschließend bestätigen. Gegnerweite/KI bleibt moderiert, nicht
  ungefragt neu festgelegt. Nicht positionierte alte Gegner müssen zunächst im Aufbau gesetzt werden.
- Portal zeichnet den gesamten belegten Ausschnitt automatisch passend zur verfügbaren Fläche;
  beim Anwachsen des Aufbaus ändern sich Ausschnitt/Maßstab ohne manuelles Zoomen.

### Speichern, Laden und Reset

- Portalstart: **Zurücksetzen**; laufende Partie: **Einstellungen**. Reset erst nach Bestätigung.
  Vorher vollständige automatische Sicherung **Vor_Reset**. Die neue Partie hat neue Identität,
  keine Spieler/Figuren/Karte und einen neuen Storybeginn. Frühere Sicherungsdateien bleiben erhalten.
- **Spiel speichern** fragt einen Namen ab und schreibt einen eigenen datierten atomischen
  Spielstand im privaten App-Speicher. **Spiel laden** zeigt vorhandene Sicherungen; davor wird
  der aktuelle Stand als **Vor_Laden** gesichert. Kein Überschreiben aller bisherigen Sicherungen.
- **Exportieren / Importieren** verwenden Androids Dateiauswahl und `.drwho`-Dateien. Darin sind
  komplette Planung/Ausrichtung/Kerkerzustände, alle bekannten Figurenpositionen, Runde, Phase,
  Go/Wurf/Reststrecke, Gegnerauftrag/Angriff, Story und Anmeldedaten enthalten. Private Spielstände
  gehören nicht ins öffentliche GitHub-Repository. Export separat aufbewahren, wenn App gelöscht wird.
- Laden validiert die ganze Datei vor Ersetzung und speichert zuerst erfolgreich. Kaputte,
  unbekannte oder zu große Dateien beschädigen die laufende Partie nicht. Größenlimit **8 MiB**.
- Laden erhält den Spielzustand, erneuert aber die Sitzungsidentität und Browser-Tokens. Dadurch
  werden alte Anfragen und niedrigere geladene Revisionen nicht mit dem laufenden Client vermischt.
  Spieler treten einmal mit ihrem gespeicherten Namen erneut bei. Nur nach explizitem Laden werden
  gespeicherte Plätze einmalig zur Namensübernahme freigegeben; nach Übernahme kein zweites
  Übernehmen desselben Platzes. Das ist eine bewusste Vereinfachung im vertrauenswürdigen Spiel-WLAN.
- Story wird ab jetzt **nicht mehr bei 60 Einträgen abgeschnitten**. Vollständiger Verlauf bleibt bis
  zur Dateigrößengrenze erhalten. Vor 0.9.0 bereits verworfene Alt-Einträge sind nicht rekonstruierbar.
- **Speicherformat 8**, Formate 1–7 lesbar. Echte 0.8.1-Datei in Regression geprüft. Alte laufende
  Pläne behalten ihre bisherigen Ein-Feld-Kacheln und Positionen. Erst bewusstes **Raumgefüge
  bearbeiten** stellt auf vier Teilfelder um und verdoppelt die Koordinaten genau einmal.
- App **0.9.0 / Code 12**, API-Protokoll **4**. Signierung ab 0.5.0 wiederverwenden. Als Update
  installieren und Clients neu laden. Kein Verlust der aktuellen Partie durch normales Update.

### Automatisch öffnen beim Verbinden mit WLAN (Nutzerfrage 26.10.2026)

Gewünscht ist eine automatische WLAN-Anmeldeseite (Captive Portal). Android unterstützt dafür
u. a. DHCP-Option 114 und Captive-Portal-Erkennung über Verbindungsproben. Unsere gewöhnliche
App kann beim normalen Android-Hotspot den DHCP-/DNS-/Umleitungsdienst nicht frei konfigurieren;
**kein zuverlässig automatisch öffnendes Captive Portal implementiert oder versprochen**.
Vorerst zwei einfache QR-Schritte. Später mit separat steuerbarem Access Point untersuchen.
Referenzen: https://developer.android.com/about/versions/11/features/captive-portal und
https://developer.android.com/develop/connectivity/wifi/localonlyhotspot (geprüft 26.10.2026).

## 8e. Portal vereinfachen, Quartettkarten und synchrone Karte ab 0.9.1

Neue verbindliche Wünsche vom 26.10.2026, umgesetzt:

- **Reset bietet drei gut lesbare Optionen untereinander:** Zurück, **Zurücksetzen (ohne
  Speicherung)**, Sichern und zurücksetzen. Nur die letzte legt `Vor_Reset` an. Bereits vorhandene
  Sicherungen bleiben in beiden Resetvarianten erhalten. Laufender Zustand wird erst nach
  erfolgreichem Speichern ersetzt; bei Speicherfehler bleibt die laufende Partie erhalten.
- **Unterer Reset auf der Portal-Startseite** setzt zusätzlich den von der App erzeugten Hotspot,
  dessen laufenden Startversuch, angezeigte SSID/Passwort/Adresse, mDNS-Instanz und gespeicherte
  Anzeigepräferenzen zurück. Ansicht springt nach oben; leeres Team, neue Partie und wieder
  **Spiel-WLAN starten**, wie beim ersten Start. Benannte Spielstandsdateien bleiben erhalten.
  Spät eintreffende alte Hotspot-/DNS-Rückmeldungen dürfen den zurückgesetzten Zustand nicht
  wiederherstellen. Ein fremder, in Android manuell gestarteter Hotspot ist nicht fernsteuerbar.
- **Reset unter Einstellungen** betrifft die Partie; der App-Hotspot bleibt für die nächste
  Anmeldung bestehen. Kein automatisches Archiv bei ausdrücklich gewähltem Reset ohne Speicherung.
- Portal-Lobby wieder **eine Seite**: WLAN und Zugang oben, Team direkt darunter. Keine Buttons
  **WLAN & Zugang / Spieler**, kein Text **Nur Name eingeben · keinen Portalcode nötig**.
  Es werden belegte Plätze mit Porträt/Name/Figur, Anzahl und bei leerer Lobby ein kurzer Hinweis gezeigt.
- Hinten an jedem belegten Spieler ein **rotes ×** mit zugänglicher Beschriftung. Entfernen wird
  zuerst dauerhaft gespeichert; nur dessen Token/Platz wird ungültig und die Figur freigegeben,
  sie wird nicht automatisch Gegner. Der Client kehrt bei der nächsten Abfrage auf seine Startseite
  zurück; keine automatische Wiederanmeldung. Andere Spieler bleiben angemeldet.
- Entfernen während Zug/Aufbau bleibt konsistent: aktiver Zug wird abgeschlossen und nächster
  regulärer Zug erhält Go; ein anderer laufender Zug bleibt samt Würfel/Reststrecke bestehen.
  Entfernte Kandidaten/Raummeister werden ersetzt, vorhandener Entwurf bleibt. Entfernte Gegnerhelfer
  und Angriffsziele blockieren keine Bestätigung mehr. Nach letztem Spieler Lobby ohne Teilnehmer;
  bestehender Entwurf/Story bleiben bis explizitem Reset erhalten. Nur nativer Host, keine neue Admin-API.
- **Spiel-WLAN starten** ruft sofort den automatischen Lokalhotspot auf, ohne App-Auswahl oder
  Einrichtungsdialog. Falls noch nötig, zeigt Android seine Systemberechtigung. Tatsächliche, von
  Android vergebene SSID/Passwort anzeigen. Manueller Einrichtungsweg und dessen alte Dialoge entfallen.
  Netzwerkadresse/QR werden erst nach aktivem App-Hotspot angeboten. Kein automatischer Browserstart.
- Einstellungen enthält **weder Kerker öffnen / schließen noch WLAN / Verbindung**. Kerkerwirkung
  kommt später in den Spielablauf. Bestehender Zustand und Zustandswahl beim Aufbau bleiben gespeichert.
- **Charakterwahl als Quartettkarte:** großes Bild oben nutzt den freien Kartenbereich, darunter
  Name, Beschreibung, Ausdauer/Kraft und Sonderfähigkeiten im bestätigten Design B. `object-fit:contain`
  zeigt das komplette Bild, auch wenn dafür seitlich Hintergrund sichtbar bleibt. Eigenschaften und
  Wahlknopf passen auf die geprüften Handygrößen. Wischen/Randverblassen und exklusive Wahl bleiben.
- Nord-Popup enthält nur Portal-Nord-Hinweis und Bestätigung. Der Zusatz **Jede Kachel enthält vier
  Schritte: oben links 1, oben rechts 2, unten links 3, unten rechts 4** ist auf Wunsch entfernt.
- **Karte und Bewegungskompass drehen je Client synchron** in allen vier 90°-Lagen. Orientierung
  bleibt pro Browser nach Neuladen erhalten und beeinflusst andere Clients nicht. Rechteckige
  belegte Kartenfläche wird ohne leere Außenränder in den verschiebbaren Rahmen eingepasst;
  Labels und Figurennummern bleiben lesbar. Wände/Gelände drehen optisch mit, Weltkoordinaten und
  gespeicherte Ausrichtung verändern sich nicht. Alter 6×6-Plan unterstützt dieselbe Drehung.
  Portalansicht und Aufbau-Editor behalten Norden oben. Kartensignatur berücksichtigt Gegnerbewegungen.
- **0.9.1 / Code 13, Format 8 / Protokoll 4.** Normal über Versionen ab 0.5.0 installieren und
  Spielerwebseiten neu laden. Spielstände kompatibel. Spielziel und Wirkungsregeln bleiben offen.

## 8f. Gemeinsame Grafik, Feldkoordinaten und Zoom ab 0.9.2

Verbindlicher Nutzerwunsch und Umsetzung vom 26.10.2026:

- Aufbau-Schalter **Kleine Kacheln**, nach Klick **Große Kacheln**. Der frühere Text **10 Kacheln
  breit** entfällt. Verhalten bleibt verschiebbarer 20×20-Plan mit kleiner/großer Darstellung.
- **Portal und Clients nutzen dieselbe lokale Vektorgrafik** für alle Kacheltypen, gespeicherte
  Drehungen, roten Wände, Lava, Brunnen, Kerkerzustände und Kompass. Die alte getrennte Canvas-
  Nachzeichnung ist ersetzt. Damit dreht der Portal-Kompass mit der gespeicherten Kacheldrehung.
- Gemeinsame Dateien `app/src/main/assets/board-art.js` und `board-art.css`. Am Portal zeigt
  `BoardView` eine rein lokale, nicht bedienbare WebView mit `portal-board.html`: kein JavaScript-
  Bridge, keine Netzwerklasten, kein Zugriff auf beliebige Dateien/Content-Provider. Java übergibt
  nur den bestätigten Plan; unveränderte Daten werden nicht alle 500 ms neu gezeichnet.
  Gesamter belegter Plan passt sich an Größe und Drehung der Portalansicht an.
- Die gleichen JS-/CSS-Dateien werden über **GET /board-art.js /board-art.css** an Clients geliefert.
  Nur diese beiden Pfade sind erlaubt, keine freie Dateiauslieferung. CSP erlaubt eigene Ressourcen;
  Spiel bleibt offline, kein CDN. Die nativen und Browser-Assets müssen im APK-Abgleich enthalten sein.
- **Jedes tatsächlich vorhandene Bewegungsfeld trägt seine eigene Koordinate**, in Editor,
  Portalplan und Spielerkarte. Nach weiterer Nutzerpräzisierung **durchgehendes globales Raster**:
  erste Kachel oben A1/B1, darunter A2/B2. Nächste Kachel rechts C1/D1 und C2/D2. Nach Z folgen
  AA, AB usw., bis **AN40**. Kachelplätze werden als Bereich **A1–B2**, **C1–D2** usw. benannt.
  Das frühere A1·1..4-Schema aus 0.9.0/0.9.1 ist damit für aktuelle Anzeigen abgelöst.
- Intern gespeicherte x/y, Kacheln und alle Figurenpositionen bleiben unverändert, ebenso die
  Bewegungsregeln. Neue Storyeinträge und Host-/Clientanzeigen verwenden die globalen Feldnamen.
  Bereits gespeicherte Storytexte mit alten Namen bleiben als damalige Aussagen unverändert erhalten.
  Koordinaten hängen am Weltfeld und ändern sich nicht durch Kachel- oder Sitzplatzdrehung.
- Zweifeldrige alte Portale besitzen acht beschriftete Teilfelder; alle 1.600 möglichen Teilfelder
  des 20×20-Kachelrasters haben eindeutige Namen von A1 bis AN40. Leere Stellen existieren weiterhin
  nicht. Ein-Feld-Altpläne behalten ihre ursprünglichen x/y und Namen, bis bewusst umgestellt wird.
- Geschlossene Kerker zeigen ihre blockierenden Außenkanten auch in der gemeinsamen Grafik rot;
  offene Zustände verwenden das grüne offene Tor. Keine neue Geländewirkung.
- Client-Karte im Tab **Karte**: **− / Zoomprozent / + / Dein Feld …**. Vergrößern von **50 bis
  300 Prozent**, einzelne Tasten ändern um 25 Prozentpunkte. Zwei Finger erlauben stufenloses
  Zoomen, ein Finger verschiebt die Karte. **Dein Feld** zeigt die eigene aktuelle Koordinate und
  springt zur Figur, auch bei gedrehter Karte. Zoom vergrößert Grafik und Koordinaten, keine ganze
  Webseite; Beschriftungen und Figurennummern bleiben aufrecht lesbar.
- Zoom/Kompass sind lokale Ansichten pro Browser, über Neuladen erhalten. Beide verändern keine
  Weltposition oder Kachelausrichtung; Netzwerk-Polling setzt den Zoom nicht zurück. Karte und
  Bewegungskreuz bleiben synchron gedreht. Aktuelle eigene Koordinate folgt jedem bestätigten Schritt.
- **0.9.2 / Code 14**, Format 8 / Protokoll 4 unverändert. Normales Update ab 0.5.0 und Clients
  neu laden. Keine neue Spielregel, Inventarmenge oder Geländewirkung erfunden.
- Build-Prüfung berücksichtigt die neuen JS-/CSS-Produktionsquellen und normalisiert wie bei
  Java/HTML auch deren Zeilenenden. Die manuelle Inhaltsprüfung und der explizite Prüfstempel bleiben.

## 8g. Nutzer-Audio am Portal ab 0.10.0

Nutzerdateien vom 26.10.2026, unverändert in die private APK eingebunden:

| Datei | Format | Dauer | SHA-256 |
| --- | --- | --- | --- |
| theme_music_12.ogg | OGG/Vorbis, 32 kHz, mono | 38,860 s | 2d4ee018831f2e71618d22485f17149c5d1535616d85408c13bb058f042fa63f |
| tardis_sound.ogg | OGG/Vorbis, 32 kHz, mono | 16,056 s | 2bfa67606f5b441fc3ce8b206a09c731ba8f1fef95d7ee055b0dbdb42c0904be |

- **Abenteuer starten** nach erfolgreichem Speichern des Starts: Themensong einmal vollständig,
  während der Raummeister-Auswahl. Kein Loop und kein zweiter Start nach dem Aufbau oder Laden.
- **Neue Registrierung in der Einrichtungs-/Portal-Lobby:** TARDIS-Sound sofort. Neue Spieler-ID
  erkennen, nicht bloß die Anzahl; Entfernen und neuer Beitritt bei gleicher Anzahl funktioniert.
  Wiederbeitritt mit ursprünglichem Nachweis, Figurenwahl, Polling und bestehende Plätze beim
  Wiederöffnen erzeugen keinen Sound. Mehrere Beitritte in einer Statusabfrage bilden einen Cue.
- Eine Spur zur Zeit: neuer Beitritt startet den TARDIS-Sound von vorn. Abenteuerstart beendet
  einen noch laufenden Beitrittssound und spielt den Themensong. Kein Überlagern von vier Handys:
  Wiedergabe ausschließlich am Android-Portal über Medienlautstärke, ohne Browser-Autoplay.
- `PortalAudioEvents` hält nur die Anzeigehistorie. `PortalAudio` lädt lokal mit MediaPlayer/
  AudioAttributes und temporärem Audiofokus. Vorbereitung asynchron; alte Rückmeldungen dürfen
  keine neuere Wiedergabe stoppen/starten. Abschluss/Fehler/Reset/Laden/Hintergrund/Fokusverlust
  geben Player und Audiofokus frei. Zurückkehren spielt keine verpassten alten Begrüßungen nach.
- Spielzustand und Zufall hängen nicht vom Erfolg der Tonwiedergabe ab. Echter Lautsprecher-,
  Systemlautstärke- und Audiofokustest am Telefon steht noch aus.

### Private Dateien für den nächsten Build wiederherstellen

Das Spiel ist privat. **Die Originalaudios werden nicht ins Quellrepository hochgeladen.** Sie
liegen bereits als persönliche ChatGPT-Dateien mit obigen exakten Namen vor. Für einen neuen
Checkout beide Dateien daraus wiederherstellen und nach
`app/src/privateAssets/audio/theme_music_12.ogg` sowie
`app/src/privateAssets/audio/tardis_sound.ogg` kopieren. Bei Anhängen mit bereitgestellten lokalen
Pfaden diese verwenden; Originaldateien nicht überschreiben. Obige Hashes vor Verwendung prüfen.

`app/src/privateAssets/` ist Git-ignoriert, wird von Gradle als zusätzliche APK-Assetquelle geladen.
Die OGGs müssen unkomprimiert im ZIP liegen, damit AssetFileDescriptor funktioniert. `verifyAudioAssets`
verhindert Builds mit fehlenden/veränderten Dateien. Der Onboarding-Quellhash umfasst auch diese
privaten Bytes. Nach Wiederherstellung auf unverändertem Checkout ist kein neuer Stempel nötig;
bei absichtlicher Audioänderung Anleitung/Prüfhashes inhaltlich aktualisieren und neu stempeln.
Kein fehlendes Audio durch eine leere Datei ersetzen. APK bleibt private Download-Datei.

## 8h. Geheimer Portalschlüssel, Tasche und Portal ab 0.10.0

Neuester verbindlicher Nutzerwunsch, umgesetzt:

- Baupalette enthält wieder **Portal · 2 Kacheln**. Elf Typen auf drei Seiten (5/5/1), horizontale
  Grundausrichtung mit genau zwei Kacheln, 90°-Drehung ergibt vertikale Ausrichtung. Acht beschriftete
  Bewegungsteilfelder. Überlappung/Rasterrand weiter validieren, als ein zusammenhängendes Teil speichern.
- Beim finalen Speichern eines **neuen** Aufbaus vor erster Runde versteckt der Host zufällig
  **einen Portalschlüssel** auf einem freien, von mindestens einer Startfigur erreichbaren Feld.
  BFS verwendet vorhandene Kacheln und massive Wände, keine erfundenen Lava-/Brunnenregeln.
  Startplätze von Partnern/Gegnern ausschließen; unerreichbare Inseln ausschließen. Fehlt Platz,
  verständlicher Fehler im Aufbau statt unwiederbringlich verlorenem Schlüssel.
- Versteck und Status nur im privaten Host-Spielstand. Keine versteckten x/y, Eigentümer-Markierung
  oder Fundnotiz in `/api/state`, Portalansicht, Karte oder Story vor dem Fund. Kein erneutes
  Zufallsziehen durch Polling, Neustart oder Speichern/Laden. Bei bewusstem Umbau gültiges Versteck
  behalten; nur wenn gelöscht, unerreichbar oder durch Startfigur belegt, beim finalen Speichern neu wählen.
- Menschlicher **Bewegungsschritt auf das Feld**, auch mitten in einem gewürfelten Weg: Schlüssel
  sofort dauerhaft in dessen Tasche legen, Fundereignis und Story speichern. Ein identischer Retry
  gibt weder zweiten Schlüssel noch zweite Fundanimation. Gegner sammeln ihn nicht ein.
- Finder erhält **Portalschlüssel gefunden!** mit Feld und **In die Tasche · weiter**. Pop-up hat
  Vorrang vor neuem Go; bestätigt ohne Bewegungs-/Aktionskosten. Netzfehler erlaubt identischen Retry
  innerhalb des Pop-ups. Offene Bestätigung wird gespeichert und nach Neustart/Laden fortgesetzt.
- Portal zeigt für drei Sekunden **animierten goldenen Schlüssel** und **Spielername hat den
  Portalschlüssel gefunden!**, mit derselben gespeicherten Ereignis-ID. Kein Schlüsselbild am
  unbekannten Feld. Beim expliziten Laden alte Portalhinweise als bereits gesehen markieren.
- Neuer Client-Reiter **Tasche**, fünf Tabs. Finder sieht Portalschlüssel mit Grafik. Andere Spieler
  erhalten nicht dessen Inventar in ihrem Snapshot. Noch kein Benutzen-, Übergeben-, Verbrauchs-
  oder Siegmechanismus; diese Spielregel legt der Nutzer später fest.
- Technischer Schutz beim Entfernen des Trägers: Schlüssel bleibt auf dessen letztem gültigen Feld
  zurück und kann mit neuem Ereignis wieder aufgenommen werden. Fehlt das Feld während eines Umbaus,
  Versteck beim nächsten Planspeichern neu setzen. Nach letztem Spieler/neuer Partie alles leeren.
  Dies ist Recovery-Verhalten, keine fertig entwickelte Weitergabe-/Inventarregel.
- **Format 9 / Protokoll 5 / Version 0.10.0 / Code 15**. Alte Formate 1–8 bleiben ladbar. Laufende
  Altpartien erhalten keinen plötzlich eingeblendeten Schlüssel; in alten noch nicht gestarteten
  Lobbys wird er beim neuen Aufbau angelegt. Aktuelle Figurenpositionen und Feldnamen bleiben erhalten.
  Für den neuen Schlüssel eine neue Partie starten; Clientseiten nach APK-Update neu laden.

## 8i. Kompakter Client und gut lesbare Kacheln ab 0.10.1

Neue verbindliche Wünsche vom 26.10.2026, umgesetzt:

- **Feldkoordinaten mittig in jedem Teilfeld**, größer und fett (Gewicht 900), ohne deckenden
  Fußstreifen. Die roten Wände bleiben auch an der unteren Kante sichtbar. Gemeinsames CSS
  gilt für Aufbau, Portal und alle Spielerkarten. Figurennummern kleiner in der Teilfeldecke.
- **Neue Kacheln ausschließlich ins Raster ziehen.** Auswahl einer Palettenkachel und anschließendes
  Antippen eines leeren Felds platziert nichts. Zoom/Scrollen erzeugen keine Kacheln. Bestehende
  Kacheln weiterhin durch Antippen um 90° drehen, durch Ziehen verschieben/entfernen. Entf/Backspace
  und Mülleimer-Taste erhalten; Pfeiltasten verschieben keine Kacheln. Expliziter Modus Startfiguren
  bleibt per Feldtippen bedienbar. Palettennavigation/Kerker-Taste reagieren auch direkt nach Drag.
- **Story** enthält alle gespeicherten Einträge als Sprechblasen, neueste oben. Innerhalb der
  Story nach unten scrollen, keine Einzel-Eintrag-Seiten mehr. Datum/Uhrzeit/Sprecher bleiben am
  Anfang, eigene Beiträge anders getönt. Unverändertes Polling behält DOM/Scrollposition; neue
  Einträge erhalten beim Lesen älterer Beiträge den sichtbaren Ausschnitt. Während der Wurfanimation
  keine neue Ergebnis-Story verraten. Der Tab öffnet den weiterhin aufklappbaren Verlauf.
- **Eigener Steckbrief als hohe Bildkarte:** Porträt nutzt den gesamten Kartenbereich, vollständiges
  Bild mit contain und Ausrichtung oben; Informationen liegen in der unteren Hälfte auf hellem
  Pergament. Lange Eigenschaften scrollen in dieser Hälfte. Kein zusätzliches Bild beschaffen,
  keine Attribute erfinden. Auswahlkarussell und Angriffssteckbriefe bleiben eigenständige Karten.
- Nach bestätigtem **Go** entfällt beim aktiven Client der redundante obere Hinweis „Du bist am
  Zug / Würfle am eigenen Handy“. Im Spiel keine zweite Spielerüberschrift/Teamzeile. Wartende,
  angegriffene und helfende Spieler behalten ihren gezielten Hinweis und bisherigen Ablauf.
- **Direkt auf den Würfel tippen** (auch Tastatur/Screenreader über echte Schaltfläche). Separater
  Würfeln-Button entfällt. Nur im erlaubten Wurfstadium aktiv; während Übertragung/2,6-Sekunden-
  Animation und danach gesperrt. Ergebnis bleibt hostgespeichert, keine neue Zufallsentscheidung.
- Nach Aufdeckung der Bewegung **Würfel links und Bewegungskompass rechts** in einer kompakten
  Reihe. Die gespeicherten Augen bleiben sichtbar; bei 5/6 zuerst Bewegung oder Aktion wählen.
  Nebel/Blitz und feste sechs Würfelseiten mit passender Landung bleiben unverändert.
- Darunter **Dein Umfeld** als vergrößerter, verschiebbarer Kartenausschnitt um die eigene Figur.
  Vierer-Kacheln 128 px / Teilfelder 64 px, mit lesbaren Koordinaten und eigener Feldanzeige.
  Folgt bestätigten Schritten, Sitzdrehung und Größenwechsel; auch Randfelder zentrierbar. Kein
  Tippen zum Bewegen auf der Karte. Separater Tab Karte behält seinen unabhängigen Zoom 50–300 %.
  Kompass, Ausschnitt und große Karte verwenden dieselbe lokale Orientierung; Weltpositionen bleiben.
- **App 0.10.1 / Code 16, Speicherformat 9 / Protokoll 5 unverändert.** Normales Update mit
  bisherigem Signierschlüssel und Clientseiten neu laden. Keine neue Partie für diese UI-Änderungen nötig.

## 9. Technische Grundlage 0.10.1

- `app`: native Activity, minSdk 26 (Android 8), target/compileSdk 36, Java 17.
- Build: AGP 8.13.2, Gradle 8.13, JDK 17. Ab 0.4.0 JmDNS 3.6.3 für den lokalen Hostnamen,
  SLF4J-NOP 2.0.17 und ZXing Core 3.5.3 zum Erzeugen von QR-Codes. Keine Online-Laufzeitabhängigkeit.
- `core`: unabhängige Java-Regellogik und HTTP-Server. Zuerst Port 80 versuchen, bei fehlender
  Verfügbarkeit auf 8080 ausweichen. Tatsächlich gebundenen Port stets in UI/QR/Annonce verwenden.
- Spieleroberfläche in `app/src/main/assets/player.html`, ohne CDNs oder Internetabhängigkeiten.
- REST-artige API: POST `/api/join`, GET `/api/state`, POST `/api/command`; kein Websocket
  nötig für den ersten Stand. Zusätzlich POST `/api/character` für die exklusive Auswahl.
  Browser fragt Zustand alle 1,5 Sekunden ab.
- Spieler melden sich mit Namen ohne zusätzlichen Code an und wählen eine Figur. Erst nach Auswahl aller
  beigetretenen Spieler kann der Spielleiter starten. Charakterwechsel vor Start ist möglich;
  die vorherige Figur wird erst nach erfolgreichem Speichern der neuen Wahl freigegeben.
- Neue Partien: 20×20-Kachelplätze mit je 2×2 Bewegungsfeldern, globale Feldnamen A1–AN40. Nur vorhandene Felder, feste Wände
  blockieren, kein Stapeln. Bereits laufende Altpartien behalten ihr freies 6×6-Feld, bis der Host
  über die Spielleitung ausdrücklich den Aufbau nachtragen lässt. Raummeister vor erstem Go.
- Jeder menschliche Zug wartet zunächst auf gespeichertes Go, danach folgt der W6. Bei 1–4 direkt bewegen, bei 5/6
  Bewegung oder Aktionswahl. Bewegung endet erst nach genau der gewürfelten Schrittzahl.
- Kein freies Passen und kein zweiter Wurf innerhalb desselben Zugs. Die Aktionswahl ersetzt
  die Bewegung; derzeit wird nur die Wahl protokolliert, keine Wirkung berechnet. Keine Missionswertung.
- Zufall entsteht ausschließlich am Host; Browser präsentiert gespeichertes Ergebnis.
- Authentisierte Zustandsabfrage, separate Tokens, kein Host-Steuerungsendpunkt.
- Längenlimits, begrenzter Threadpool, Anmeldedrosselung, keine CORS-Freigabe,
  Same-Origin/Custom-Header-Prüfung und numerische IPv4-Hostnamen.
- HTTP im lokalen WLAN; keine TLS-Infrastruktur. Nicht ins Internet weiterleiten.
- Vordergrundprototyp: App im Vordergrund und Bildschirm eingeschaltet lassen. Kein
  Foreground-Service. Android-/Herstellerverhalten und Energiesparen müssen real getestet werden.
- Automatischer Hotspot kann nach Neustart andere Zugangsdaten/IP haben. QR-Codes und
  mDNS-Hostname sind nun vorhanden; Browser-Token bleiben jedoch an den Seitenursprung gebunden.
  Beim Wechsel zwischen IP-Adresse und Hostnamen kann deshalb eine zweite Anmeldung entstehen.
  Spielerplatz freigeben, Adress-/Gerätewechsel und gesicherter Wiederbeitritt bleiben noch offen.

## 10. Entwicklungsreihenfolge

1. **Jetzt:** Projekt und Anforderungen sichern, technischen Prototyp bauen und testen.
2. **Gemeinsam konkretisieren:** Inventarfotos/-zahlen, Spielziel, Rollen, Größe/Dauer und
   genaue Bedeutung von Bewegung/Feldern klären. Keine gesamte Figurenliste erfinden.
3. **Aufbau:** Inventarmodell, Kartenvalidierung, Generator, verständliche Aufbauanzeige und
   Speichern des tatsächlichen Plans. Zuerst eine kleine Beispielmission.
4. **Spielregeln:** bestätigte Charaktere, Türen, Sichtlinien, Dalek-Reichweite, Engelstatus,
   Ereignisse, Ziele, Gegnerphase und Sieg/Niederlage.
5. **Fortsetzung:** vorhandene benannte Partien/Export/Import am echten Gerät prüfen; Wiederaufbau,
   Wiederbeitritt bei Adresswechsel und Recovery weiterentwickeln.
6. **Atmosphäre:** auf Halterung abgestimmtes Portal, gelieferte Medien, Soundoptionen und Feinschliff.

### Nächster konkreter Schritt mit dem Nutzer

Raummeister und manueller Aufbau sind seit 0.8.0 der nächste Testschwerpunkt: echte Touch-
Bedienung, Portal-Lesbarkeit und Wiederaufbau am Tisch mit realem Inventar prüfen. Die noch
fehlenden Stückzahlen/Abmessungen dienen danach dem Generator; keine unbekannten Teile ergänzen.

Der vorgemerkte TARDIS-Icon-Wunsch ist mit 0.5.0 umgesetzt. Die konkreten Startbildschirm-
Anpassungen sind in Abschnitt 8a dokumentiert und bei weiteren Änderungen zu bewahren.

Die kooperative Rollenverteilung ist geklärt. Als Nächstes Figurenliste und Fotos sowie
Ausdauer/Kraft und gewünschte Sonderfähigkeiten erfassen; danach den Zielvorschlag „Der Zeitriss“
konkretisieren und Gelände aufnehmen. Als kleinster sinnvoller Inventarschritt
reichen zunächst ein Foto von jeder Plättchenform, Stückzahl und Breite×Tiefe sowie die Angabe,
ob Wände/Türen fest oder aufsteckbar sind. Noch keine vollständige Datenerhebung erzwingen.

Parallel nach verfügbarer APK die Android-Version und das Host-Telefon notieren und einen
echten Vier-Spieler-Hotspottest durchführen. Das Host-Telefon ist bislang nicht benannt.

## 11. Tests und bekannte Grenzen

Für 0.10.1 am 26.10.2026:

- DOM-/Grafikprüfungen: **213 bestanden**; direkt antippbarer W6 mit Go-/Wurf-/Retry-Sperren,
  entfernte Zugbox, Würfel/Kompass-Reihe, eigener Kartenausschnitt, Sprechblasen/Neueste-zuerst,
  keine Kachelplatzierung bei leerem Feldtippen und unverändertes Drehen bestehender Kacheln.
- **532 Kern-/HTTP-Prüfungen bestanden**; Regellogik unverändert.
- Zwei mobile Browser-Kontexte (390×844, 320×740) am echten Java-Host: keine Kacheln durch
  Leerfeld-Tipp, echte Touch-Drag-Platzierung einschließlich Zweikachel-Portal; direkter Würfel,
  Folgehandlungen nach Animation, eigener Kartenausschnitt/Feldname nach Bewegung, synchrone
  Karten-/Kompassdrehung, Zoom/Pinch/Pan/Reload, Reset/Laden und Schlüssel-Fund weiterhin bestanden.
- Steckbriefhöhe, Bildfläche, untere Infohälfte und Story mit 30 sichtbaren Sprechblasen,
  neuester zuerst, tatsächlich scrollbarem Innenbereich ohne Dokument-Seitenlauf geprüft.
  Screenshots von Zug, Steckbrief und Story visuell geprüft. Native Telefonprüfung bleibt offen.
- Android `:app:assembleDebug :app:lintDebug`: **BUILD SUCCESSFUL**, Lint **0 Fehler / 20 Warnungen**.
  App **0.10.1 / Code 16**, bestehender Signierschlüssel seit 0.5.0 per Zertifikat bestätigt.
  Finale Anleitung und alle gemeinsamen Grafik-/Client-Assets sowie beide unkomprimierten
  Originalaudios vor Ausgabe bytegenau mit der APK abgleichen. Kein echter Telefon-/WLAN-Test.

Für 0.10.0 am 26.10.2026:

- **532 Kern-/HTTP-Prüfungen bestanden**. Neu Audio-Cues ohne Replay, neue ID bei gleicher
  Platzanzahl, Hintergrund-/Ladebaseline, fehlgeschlagener Start; geheimer erreichbarer Fund,
  keine Vorab-Leaks, atomarer Speicherfehler/Retry, eigene Tasche, fremde Bestätigung abgewiesen,
  Ereignis-ID, Save/Load/Ack, Rückfall nach Entfernen und Rückwärtslesen eines Format-8-Präfixes.
- **199 bisherige + 10 neue = 209 DOM-/Grafikprüfungen bestanden**: Fund-Popup/Fokus/Inert,
  exakter Feldname, Tasche/Grafik, identischer Ack-Retry, keine doppelte Ausgabe, kein vorzeitiges
  Benutzen, Rückkehr zur Anmeldung bei entferntem Finder aus offenem Pop-up.
- Bisheriger mobiler Chromium-Ablauf weiterhin bestanden. Zusätzlich echte Einzelspieler-Partie:
  Portal auf dritter Palettenseite per Touch, zweifache Breite, getrennte Karteninsel; Schlüssel nur
  im erreichbaren Startbereich, Bewegung zu B2, Finder-Popup, Portal-Fundereignis und Tasche,
  vollständiges Speichern/Reset/Laden mit erhaltener Tasche und ohne erneutes Pop-up nach Ack.
  Popup und Tasche per Screenshots visuell geprüft; 390×844 ohne Dokument-Seitenlauf.
- Beide Audio-Dateien mit ffprobe identifiziert und vollständig per ffmpeg decodiert. Negativprüfung:
  `verifyAudioAssets` lehnt fehlende und manipulierte Audiodateien mit vorgesehener Meldung ab.
- Android `:core:smokeTest :app:assembleDebug :app:lintDebug`: **BUILD SUCCESSFUL**, erneut
  **532 Kernprüfungen**, Lint **0 Fehler / 20 Warnungen**. App **0.10.0 / Code 15**; bestehender
  Signierschlüssel seit 0.5.0. Finale Anleitung, gemeinsame Grafiken und beide unkomprimiert
  eingebundenen Audiodateien vor Übergabe bytegenau gegen APK-Assets/Originale abgleichen.
- Tatsächliche MediaPlayer-Ausgabe, System-Audiofokus und die native Schlüsselanimation müssen
  weiterhin am echten Telefon geprüft werden.

Für 0.9.2 am 26.10.2026:

- **490 Kern-/HTTP-Prüfungen bestanden**, darunter neue unveränderliche gemeinsame Assets,
  richtige Inhaltstypen/CSP, nur lesende Pfade, keine private Dateiauslieferung und globale
  Bewegungsfeldnamen A1/B1/A2/B2, Z/AA/AN sowie Kachelbereiche.
- **180 bisherige DOM- plus 19 neue Grafik-/Koordinatenprüfungen = 199 Prüfungen bestanden**.
  Alle vier Teilfelder, 1.600 eindeutige Namen, Kartenrand, gedrehte Kacheln, alte ein-/zweifeldrige
  Pläne, Spalten nach Z, vier gespeicherte Kompassdrehungen und sichtbare Kerker-Außenwände.
- Zwei mobile Chromium-Kontexte am echten Java-HTTP-Server: Kleine/Große-Kacheln-Schalter,
  Koordinaten in jedem Teilfeld, Zoom per Tasten und echten Zwei-Finger-Ereignissen, Textgröße bei
  200 Prozent, Ein-Finger-Verschieben direkt auf Kacheln, Zoom/Kompass nach Reload, unveränderte
  Weltpositionen und bisheriger kompletter Ablauf.
- Portal-Dokument direkt aus lokalen Assets in Chromium geladen: Kompass-SVG und Koordinaten
  identisch zum Client; automatisches Einpassen in 390×360. Editor, Portalplan und vergrößerte,
  gedrehte Clientkarte per Screenshot betrachtet. Android-WebView/Telefonprüfung ist damit nicht ersetzt.
- Negativprüfung: veralteter Prüfstempel wird von `verifyOnboarding` wie vorgesehen abgelehnt;
  anschließend inhaltliche Anleitung geprüft und Prüfstempel ausdrücklich erneuert.
- Android `:core:smokeTest :app:assembleDebug :app:lintDebug`: **BUILD SUCCESSFUL**, erneut
  **490 Kernprüfungen**, Lint **0 Fehler / 18 Warnungen**. App **0.9.2 / Code 14**; bestehender
  Signierschlüssel seit 0.5.0. Finale Anleitung, Spielerdatei und alle drei gemeinsamen Portal-/
  Grafikdateien vor Übergabe gegen APK-Assets abgleichen.
- Neuer lokaler Portalrenderer muss am echten Telefon mit dessen installierter Android System
  WebView geprüft werden; Chrome-Datei-/Browserprüfung ersetzt diesen Test nicht.

Für 0.9.1 am 26.10.2026:

- **481 Kern-/HTTP-Prüfungen bestanden**, darunter neu atomisches Entfernen, Tokenentzug,
  freier Charakter, erneuter expliziter Lobbybeitritt, unveränderter fremder Zug, nächster aktiver
  Zug, Gegnerhelfer/Angriff, Raummeisterwechsel, letzter Teilnehmer und Neustartpersistenz.
- **180 DOM-Prüfungen bestanden.**
- Zwei echte mobile Chromium-Kontexte (390×844 und 320×740) am Java-HTTP-Server: große vollständig
  sichtbare Charakterbilder, vorhandene Auswahl, gekürzter Nord-Hinweis, vier synchrone Karten-/
  Kompassdrehungen mit korrekten rechteckigen Grenzen, unabhängige Clients, Reload, unveränderte
  Weltrichtung/Positionen, kompletter Reset/Laden und entfernter Client zurück zur Anmeldung.
  Screenshots der Karten und gedrehten Karte visuell geprüft, keine Dokument-Scrollleiste.
- Android `:core:smokeTest :app:assembleDebug :app:lintDebug`: **BUILD SUCCESSFUL**, erneut
  **481 Kernprüfungen**, Lint **0 Fehler / 17 Warnungen**. App **0.9.1 / Code 13**; gleicher
  Signierschlüssel seit 0.5.0. Finale Anleitung und Spielerdatei vor Übergabe mit APK-Assets abgleichen.
- Native Portal-Lobby, drei Resetoptionen und Hotspot-Start/Abbruch/Neustart benötigen weiterhin
  echte Telefonprüfung; kein realer WLAN-/Herstellertest behauptet.

Für 0.9.0 am 26.10.2026:

- **458 Kern-/HTTP-Prüfungen bestanden**, einschließlich vier Teilfelder, Wandkanten, Kerkerzustand,
  aller neuen Typen, ganzer Spielstand, Fehler beim Laden/Reset, Wiederbeitritt, Figuren/Gegner und
  echter Format-7-Datei mit erhaltener alter Bewegung sowie bewusster Rasterumstellung.
- **155 bisherige + 25 angepasste Beitrittsprüfungen = 180 DOM-Prüfungen bestanden.**
- Zwei mobile Chromium-Ansichten mit echtem Java-HTTP-Server: Name ohne Code, Charakterwahl,
  Raummeister/Nord-Popup, zehn Kacheln, vier Teilfelder, Startfigur per Touch, Kompassdrehung über
  Reload, gleichbleibende Weltrichtung, Spielreset und vollständiges Laden mit Wiederbeitritt.
  390×844 und 320×740 ohne Dokument-Seitenlauf geprüft; Screenshots des Editors/Plans betrachtet.
- Android `:core:smokeTest :app:assembleDebug :app:lintDebug`: **BUILD SUCCESSFUL**, erneut
  **458 Kernprüfungen**, Lint **0 Fehler / 17 Warnungen** (u. a. feste UI-Texte, Updates, Icon und
  ungenutzte Altressource). App **0.9.0 / Code 12**, Signatur identisch seit 0.5.0; finale Anleitung
  und Spielerdatei werden vor Übergabe gegen die APK-Assets abgeglichen.
- Native Dateidialoge, Portaloptik und reales WLAN sind weiterhin auf einem echten Telefon
  zu prüfen. Import akzeptiert auch Dateien mit unbekanntem MIME-Typ; Inhalt wird unabhängig
  davon validiert. Portal-Marker einschließlich Nummern skalieren mit dem Teilfeldraster.

Für 0.8.1 am 26.10.2026:

- Kern-/HTTP-Prüfung: **415 Prüfungen bestanden**; neu eindeutiger Codefehler, keine Preisgabe des
  richtigen Codes, kein belegter Platz bei falschem Code sowie korrigierter Wiederbeitritt während
  einer bereits gestarteten Partie mit demselben ursprünglichen Nachweis.
- `npm test`: **155 bisherige + 46 neue = 201 DOM-Prüfungen bestanden**. Lokales Format, echte
  Hostablehnung, Fokus/Markierung, erhaltene Eingaben, keine doppelten Anfragen, sichere Korrektur,
  Netzwerk-Retry, Anmelderückkehr aus Raummeister-/erzwungenem-/Go-/Richtungspopup und Editor,
  erhaltene Wiederbeitritts-Nachweise trotz zwischenzeitlich falschem Code sowie Versionswechsel.
- Zwei echte mobile Chromium-Browser am Java-HTTP-Server: vor beiden Beitritten falscher Code,
  sofort korrigierbares Formular, unveränderte Platzbelegung und danach erfolgreiche Anmeldung.
  Zusätzlich alte Anmeldung aus offenem Raummeister-Popup verworfen, falscher Code, richtiger
  Code und derselbe Spielerplatz mit ursprünglichem Nachweis während gestarteter Partie.
  Anschließend kompletter bisheriger Aufbau-/Go-/Zugablauf weiterhin bestanden.
- Android `:core:smokeTest :app:assembleDebug :app:lintDebug`: **BUILD SUCCESSFUL**, nochmals
  **415 Kernprüfungen**, Lint **0 Fehler / 16 Warnungen**. App 0.8.1 / Code 11; Signatur und finale
  eingebettete Anleitung vor Übergabe abgleichen. Echter Telefon-/WLAN-Test weiterhin offen.

Für 0.8.0 am 26.10.2026:

- `bash scripts/test-core.sh`: **411 Prüfungen bestanden**. Auswahl ohne Wiederholung, fehlende
  Berechtigungen, Speicherfehler, Retry/Neustart, alle Nein, verbindliche Portalwahl, sieben
  Kacheltypen in allen Drehungen, Rand/Kollisionen, Portal über zwei Felder, Verschieben/Entfernen,
  Startfiguren, Abschluss/Startdeadline, sparse Bewegung/Wände und Migration Formate 1–6.
- `npm test`: **155 DOM-Prüfungen bestanden**. Raummeister-Popups, Ja/Nein-Retry, erzwungene Wahl,
  Hintergrund/Fokus, 400 Rasterstellen, sieben Typen, Tipp-Alternative/Drehen/Mülleimer, Startfiguren,
  Bestätigungsfehler, Erhalt unveränderter Kacheln und gespeicherte Karte vor erstem Go.
- Zwei echte mobile Chromium-Clients am echten Java-HTTP-Server: Auswahl/Ablehnung/Zusage,
  Maus- und echte Touch-Pointer-Ziehbewegung, Drehen, Mülleimer, alle sieben Typen, Live-Hostzustand,
  fremde Bearbeitung abgewiesen, 390/320 px ohne Seitenüberlauf, Reload/Host-Neustart mit Plan,
  Startfilm vor Go, gerichteter erster Zug und erneuter Aufbau samt Alle-Nein-Portalentscheidung.
  Zweite Portalhälfte als Startfeld sowie Platzieren/Entfernen im äußersten Feld T20 geprüft.
  Den gefundenen Touch-Tap-Fehler bei Pointer-Capture durch explizite Touch-Tap-Verarbeitung und
  Unterdrücken doppelter Kompatibilitätsklicks behoben, einschließlich leerer Tipp-Ziele
  und Startfiguren auf der zweiten Portalhälfte. Screenshots visuell geprüft; Editor als fester Handybildschirm mit eigener Palettenzeile,
  damit die Palette keine unteren Rasterfelder überdeckt. Fester Meldungsplatz verhindert,
  dass eine verschwindende Speicherbestätigung das Raster während des Ziehens verschiebt.
  Polling mitten in Touch-Ziehbewegungen sowie Verschieben/Kollisionen werden im Systemtest geprüft.
- Android `:core:smokeTest :app:assembleDebug :app:lintDebug`: **BUILD SUCCESSFUL**, nochmals
  **411 Kernprüfungen**, Lint **0 Fehler / 15 Warnungen**. APK-Version 0.8.0 / Code 10, Zertifikat
  identisch zu 0.5.x–0.7.0. Nach abschließender Touch-/Auswahlkorrektur erneut gebaut und geprüft.
  Native Portaloptik und reales WLAN/mDNS bleiben Gerätetests.
- Reproduzierbarer Systemtest eingecheckt: `scripts/test-browser.cjs` und ausschließlich im Test-
  Quellbaum liegende `BrowserPortalFixture.java`. Optional `npm run test:browser` nach Kerntest-
  Kompilierung, vorhandenes Playwright/Chromium vorausgesetzt. Kein neuer Laufzeitdienst und
  kein entfernter Host-Administrationsendpunkt. Screenshots bleiben interne temporäre Testdateien.

Für 0.7.0 am 26.10.2026:

- `bash scripts/test-core.sh`: **309 Prüfungen bestanden**. Neue Fälle: Go vor Spielhandlungen,
  falscher Spieler, Speicherfehler, Go-Retry/Neustart, nächste Person wartet, Formate 1–5 inklusive
  fortgesetzter Bewegung, feste Host-Ortszeit, archivierte Altzeilen ohne erfundene Zeit, separater
  kosmetischer Zufall, gespeicherter Dalek-Text, Story-Begrenzung sowie alle 4 Richtungen für die
  3 bekannten Charaktere einschließlich ungültiger Eingaben und idempotenter Wiederholung.
- `npm test`: **125 DOM-Prüfungen bestanden**. Neu: exakter Go-Popuptext, Fokus/Hintergrundsperre,
  kein automatischer Zugstart, Retry im Popup, Story-Präfix und sichere Textausgabe, archivierte
  Aussagen, Richtungsauswahl/Abbrechen/Escape, kein Verbrauch vor Richtung, unbestätigte Aktion
  ohne Richtungswechsel und gezielter Go-Hinweis nur beim aktuellen Spieler.
- Zwei echte mobile Chromium-Clients mit dem echten Java-HTTP-Server getestet: exklusive Figuren,
  Portal-Kartenstatus vor/nach Go, Server blockiert Würfeln vor Go, Go über Server-Neustart und
  Browser-Neuladen erhalten, animierter gespeicherter W6, gerichtete Doctor-Aktion samt Abbruch,
  erster-Person-Story mit Zeitstempel, Dalek-Geräusche, sechs Bewegungen und nächster Go-Dialog.
  Screenshots der beiden Popups und Story betrachtet, keine JavaScript-Fehler.
- Android `:app:clean :core:smokeTest :app:assembleDebug :app:lintDebug`: **BUILD SUCCESSFUL**,
  erneut **309 Kernprüfungen bestanden**, Lint **0 Fehler / 15 Warnungen**. App 0.7.0 / Code 9.
- Bisherigen privaten Signierschlüssel aus der gesicherten Datei wiederhergestellt; APK-Zertifikat
  entspricht **0.5.x/0.6.0**, direktes Update ohne Deinstallation möglich. Anleitung vor Übergabe
  erneut als APK-Asset übernehmen. Native Portal-Karte und reales WLAN/mDNS noch nicht auf einem
  echten Telefon getestet.

Für 0.6.0 am 25.10.2026:

- `npm test`: **103 DOM-Prüfungen bestanden**. Neu: obere Tabs/Tastatur, aufklappbarer Verlauf,
  Erhalt der Auswahl beim Polling, eigene Zuganzeige, Steckbrief beim Warten, Aufgaben-/Angriffs-
  Priorität sowie derselbe Würfel mit unveränderten Augen vor/während/nach Aufdeckung.
- Mobiler Chromium-Test bei 390×844 und 320 Pixel Breite: vollständiges 3:4-Porträt mit contain,
  Tabs/Verlauf, automatischer Ansichtswechsel und sechs animierte Würfe tatsächlich geprüft.
  Für jede Augenzahl 1–6 ist die größte nach vorn gerichtete Fläche das gespeicherte Ergebnis;
  Matrixdifferenz zwischen Animationsende und ruhendem Würfel **0 für alle sechs Werte**.
  Gleicher Würfel/gleiche Augen bleiben erhalten. Reduzierte Bewegung und schmale Ansicht ohne
  horizontales Seitenüberlaufen geprüft; keine JavaScript-Fehler.
- Android `:core:smokeTest :app:assembleDebug :app:lintDebug`: **BUILD SUCCESSFUL**,
  **221 Kernprüfungen bestanden**, Lint **0 Fehler / 12 Warnungen**. App 0.6.0 / Versionscode 8.
- APK-Signatur mit 0.5.1 verglichen: **identisch**, direktes Update ohne Deinstallation möglich.
  Finale Anleitung wird vor Ausgabe erneut als APK-Asset übernommen. Keine Regellogik oder
  Speicherdaten geändert. Kein echter Telefon-/WLAN-Test; Bildrate auf Android weiterhin offen.

Für 0.5.1 am 25.10.2026:

- `npm test`: **89 DOM-Prüfungen bestanden**. Neue Fälle: abgelaufene Anmeldung, erneuter
  Beitritt ohne alte Aktionen, Name/QR-Code erhalten, ursprüngliche Anfrage-ID wiederverwenden,
  keine stille Neuanmeldung, verspätete 401 nach erfolgreicher Anmeldung, WLAN-Unterbrechung,
  gleiche Version/neue Partie, Versions-/Protokollabweichung mit Neuladen-Schaltfläche sowie
  gesperrte alte Momentaufnahme nach einer Versionsabweichung.
- `bash scripts/test-core.sh`: **221 Prüfungen bestanden**. Neue Fälle prüfen öffentliche,
  schreibgeschützte Metadaten ohne Zugangsdaten, unterschiedliche neue Partie-IDs sowie stabile
  Partie-ID nach Wiederherstellung. Spielregeln und Speicherformat 5 unverändert.
- Android `:app:clean :core:smokeTest :app:assembleDebug :app:lintDebug`: **BUILD SUCCESSFUL**,
  erneut 221 Kernprüfungen. Lint **0 Fehler / 12 Warnungen**. Versionsmetadaten 0.5.1 / Code 7.
- Mobiler Chromium-Test: alte Anmeldung/neue Partie führt zum Formular; genau ein expliziter
  Beitritt; simuliertes Versionsupdate fordert Neuladen und erhält dabei die gültige Anmeldung
  ohne zweiten Platz. Keine JavaScript-Fehler festgestellt.
- APK-Zertifikat mit 0.5.0 verglichen: **identisch**, direktes Update möglich. Spielstand nicht
  durch Deinstallation entfernen. Kein realer Android-Gerätetest durchgeführt.

Für 0.5.0 am 25.10.2026:

- `npm test`: **54 DOM-Prüfungen bestanden**. Neue Belegungsanzeige bei 0/3/4 Spielern,
  Netzfehler und Wiederverbindung; Eingaben bleiben erhalten, Polling meldet niemanden an.
  Dazu Karussellnavigation, Tastatur, Randbegrenzung, Kartenerhalt nach Besitzwechsel sowie
  Würfel-Ergebnis erst nach Timer, gesperrte Folgehandlungen, kein Animationsneustart bei Polling,
  gemerkte Aufdeckung und neuer Effekt bei neuem Wurf mit gleicher Augenzahl.
- `bash scripts/test-core.sh`: **217 Prüfungen bestanden**, einschließlich öffentlicher Belegung
  ohne Veröffentlichung von Namen, Codes oder Spielzustand; Format-4-Migration, persistente
  Wurf-ID, Ursprung/Ziel im Bewegungslog und neue ID auch bei gleicher Augenzahl.
- Abschließend `:app:clean :core:smokeTest :app:assembleDebug :app:lintDebug`:
  **BUILD SUCCESSFUL**, erneut 217 Kernprüfungen, Lint **0 Fehler / 12 Warnungen**.
  Shader der nativen Würfelanimation sind vorab gecacht; letzter W6 alter Formate wird ohne
  neue Animation angezeigt. Nach abschließendem Testbericht nochmals APK-Asset aktualisieren.
  Bei doppelten generierten R-Klassen aus alten Build-Ausgaben hilft ein sauberer App-Build.
- Mobiler Chromium-Test bei 390×844: JPEGs laden, echte Touch-Wischgeste erreicht nächste Karte,
  Pfeilnavigation funktioniert, kein seitliches Seitenüberlaufen, Ergebnis während Würfeln verborgen
  und nach Ablauf wählbare Folgehandlung. Screenshots für Sichtprüfung betrachtet.
- Kein echter Android-Gerätetest durchgeführt; insbesondere native Portal-Spielansicht und
  Würfelanimation, WLAN/QR/mDNS und reale Performance bleiben Hardwareprüfungen.

Für 0.4.0 am 25.10.2026:

- `bash scripts/test-core.sh`: **204 Prüfungen bestanden**. `npm test`: **35 DOM-Tests bestanden**.
- Neue Prüfungen: QR-Verbindungsdaten/Escaping, Code-Vorbelegung ohne automatischen Beitritt,
  lokaler HTTP-Hostname und Namenswechsel, fremde Hostnamen, sichere Portraitpfade/SVG-Ausgabe.
- Nach Korrektur der nicht öffentlichen SSID-Setter-API: Android `:app:assembleDebug :app:lintDebug`
  **BUILD SUCCESSFUL**, Lint **0 Fehler / 7 Warnungen**. Nach Verfeinerung von Einrichtungsdialog,
  UTF-8-QR-Codes und Hostnamenpflege erneut `:core:smokeTest :app:assembleDebug :app:lintDebug`
  erfolgreich (204 Kernprüfungen). Letzte APK mit aktuellem Onboarding wird ausgeliefert.
- Stilisierte Porträts, XML und Spieler-JavaScript geparst; HTTP-Portraitpfad korrigiert und
  durch den neuen SVG-Ausgabetest abgedeckt. Keine Behauptung eines realen Gerätetests.
- Reale Hotspot-Einrichtung, `.local` auf verschiedenen Handys, Port-80-Bindung auf Android,
  QR-Scan mit Systemkamera, native Lobbydarstellung und Animation bleiben Gerätetests.


Für 0.3.1 am 25.10.2026:

- `npm test`: **33 DOM-Verhaltenstests bestanden** nach Umstellung auf Design B.
- Keine neue Regellogik oder Änderung am Speicherformat; die 187 Kernprüfungen aus 0.3.0
  bleiben der zuletzt ausgeführte Regellogik-Teststand.
- Android-Build `:app:assembleDebug :app:lintDebug`: **BUILD SUCCESSFUL**, Lint **0 Fehler / 8 Warnungen**.
  Würfelfläche im Archivstil auf eine einzelne große Zahl begrenzt; W6-Beschreibung bleibt
  für assistive Bedienung als zugänglicher Text erhalten. Danach erneut UI-Test und APK-Build.


Für 0.3.0 am 25.10.2026 tatsächlich ausgeführt:

- `bash scripts/test-core.sh`: **187 Prüfungen bestanden**. Zusätzlich zu bisherigen Tests
  sind alle sechs Würfelwerte, zuerst würfeln, 5/6-Auswahl, exakte Weglänge, Richtungswechsel,
  keine Doppelbewegung bei Retry, kein Wechsel zur Aktion nach Bewegungsbeginn, Reststrecke
  nach Neustart und Lesen der Spielstandformate 1/2/3 geprüft.
- `npm test`: **33 DOM-Verhaltenstests bestanden**. Neu: Würfeln vor Bewegung, 5er-Wahl,
  geschlossene Aktionswahl nach Bewegungsentscheidung und sichtbare Reststrecke.
- JavaScript-Syntax mit `node --check` und `git diff --check` geprüft.
- Negative Prüfung der Build-Sperre: `verifyOnboarding` lehnt einen nicht zum Quellstand
  passenden Prüfstempel mit der vorgesehenen Dokumentationsmeldung ab.
- Nach inhaltlicher Prüfung und ausdrücklichem Aktualisieren des Prüfstempels:
  Gradle `:core:smokeTest :app:assembleDebug :app:lintDebug` **BUILD SUCCESSFUL**.
  Dabei erneut 187 Kernprüfungen bestanden; Android-Lint **0 Fehler / 8 Warnungen**.
  Die abschließende APK enthält den aktuellen geprüften Anleitungsstand.

Vorheriger Stand 0.2.0: 81 Kern-/HTTP-Prüfungen, 28 DOM-Prüfungen, APK-Build erfolgreich,
Android-Lint 0 Fehler / 8 Warnungen. Dieses historische Ergebnis ersetzt nicht den aktuellen Build.

Kernprüfungen umfassen weiterhin vier Plätze, Code/Token-Schutz, Zugreihenfolge, Grenzen,
belegte Felder, Revisionen, idempotente und parallele Anfragen, Neustart und Speicherfehler.
Außerdem: gleichzeitige Figurenwahl, Partner-/Gegnertrennung, Host-exklusive Gegnerverwaltung,
Gegnerphase, genau einmal bestimmter Bewegungshelfer, berechtigte Bestätigungen, Rundenwechsel
nach Gegnern und gespeicherte Angriffe. Der vierte Katalogeintrag im Vier-Spieler-Test ist reine
Testdaten-Fixture und keine zusätzliche Figur im Produktkatalog.

Build-Hinweis: SDK-Plattform und Build Tools sind lokal vorhanden. Nicht installierte Platform-Tools
führen in dieser Umgebung zu einer Warnung, verhindern den APK-Build aber nicht.
Die optionalen npm-Abhängigkeiten dienen nur den DOM-Tests, nicht dem Offline-Spielbetrieb.

Noch ausstehend: echter Telefon-/Hotspottest mit vier Geräten, visuelle und Energie-/Lebenszyklus-
Prüfung am Host, Bildschirm-Timeouts der Spieler, App-Abbruch und Wiederbeitritt bei geänderter IP.
DOM-Tests ersetzen keine Sichtprüfung auf realen Handys. Fotos, verbindliche Attribute, wirkende
Charakterfähigkeiten, Gegner-KI, Inventargenerator und eine vollständige Mission sind noch offen.

### APK-Signierung und Fortsetzung ab 0.5.0

Der ursprüngliche temporäre Debug-Schlüssel für APK 0.4.0 ist in der aktuellen Umgebung nicht
mehr vorhanden. 0.5.0 wurde mit einem neuen Debug-Schlüssel signiert. Deshalb **kein direktes
Update über 0.4.0 möglich**. Bestehende Installation nicht ohne Sicherung entfernen: Android
löscht dabei ihren Spielstand; Export/Import steht ab 0.9.0 zur Verfügung.

- Bisheriges Zertifikat SHA-256: `e0e73387152fbcc397aa5790717d35790a95d95c59845778e05df1494de7ade5`.
- Zertifikat ab 0.5.0 SHA-256: `2940936793e828d87a8e2178dc62223c24bd9ebc66ab114241c71f52d1231b40`.
- Für die Fortsetzung den privaten Debug-Schlüssel `Dr_Who_debug_signing_0.5.0.jks`
  aus den persönlichen ChatGPT-Dateien abrufen. Er gehört außerhalb des öffentlichen Repositorys.
  Vor weiteren Builds genau diesen wiederverwenden,
  damit Versionen ab 0.5.0 installierbare Updates bleiben. Keine Schlüssel ins Repository stellen.
- Aktuelle lokale Datei liegt außerhalb des Repositorys unter
  `../build-tools/android-user/debug.keystore` (`ANDROID_USER_HOME` für diese Builds).
  Standard-Android-Debug-Alias/-Kennwort; kein Produktions-Signiersystem.
- APK-Signatur und Versionsmetadaten vor Übergabe geprüft. Ein verlorener privater Schlüssel
  kann aus einer bereits signierten APK nicht zurückgewonnen werden.

## 12. Änderungshistorie

| Datum | Version | Änderung |
| --- | --- | --- |
| 25.10.2026 | 0.1.0 | Repository angelegt, Originalanforderungen und offene Fragen dokumentiert; kooperatives Missionsziel vorgeschlagen; Android-/Browser-/Host-Grundlage als Technikdemo erstellt |
| 25.10.2026 | 0.2.0 | Exklusive Steckbrief-Auswahl, Portal-Lobby, Partner-/Gegnerzuordnung, Gegnerphase mit zufälligem Bewegungshelfer, TARDIS-Warteanzeige, Angriffsansicht und rückwärts lesbares Speicherformat 3 |
| 25.10.2026 | 0.3.0 | W6 zu Beginn jedes Spielerzugs, exakte Reststrecke, Richtungswechsel, 5/6-Wahl, Speicherformat 4; zentrale ONBOARDING.md mit Spielregeln und Build-Prüfung; drei visuelle Designvorschläge |
| 25.10.2026 | 0.3.1 | Nutzer wählt Spielerdesign B und Portaldesign A; Spieler-CSS auf Pergament/Petrol/Messing und Serifenschrift umgestellt; Regeln unverändert |
| 25.10.2026 | 0.4.0 | Grafische Portal-Lobby mit drehender TARDIS/Charakterporträts; einfache WLAN-Einrichtung, echter mDNS-Hostname, Port-80-Versuch mit 8080-Fallback, WLAN-/Seiten-QR und Code-Vorbelegung |
| 25.10.2026 | 0.5.0 | Statischer Doctor-Who-Hintergrund statt Lobby-Animation; aufgeräumte Starttexte, Portalcode, Dr. Who/Version auf Spielerhandys, öffentliche reine Belegungsanzeige, wischbares Steckbrief-Karussell, realistischere generierte Porträts, TARDIS-Launcher-Icon, automatische Portal-Spielübersicht und 2,6 Sekunden Würfelinszenierung mit persistenten Ereignissen (Format 5) |
| 25.10.2026 | 0.5.1 | Automatischer Versions-/Protokoll-/Partieabgleich; ungültige Anmeldung führt zurück zum Beitritt, Metadaten ohne Token, kein Übertragen alter Aktionen und Schutz vor verspäteten alten Antworten; gleicher Signierschlüssel wie 0.5.0 |
| 25.10.2026 | 0.6.0 | Drei Spieler-Tabs und aufklappbarer Verlauf; sichtbare Zughinweise, Steckbrief als Warteansicht mit vollständigem Bild; sechs feste Würfelseiten und Landedrehung passend zum gespeicherten Ergebnis ohne Zahlentausch |
| 26.10.2026 | 0.7.0 | Gespeicherter Zugstart Your move... / Go mit Portal-Charakterkarte, gerichtete Aktionen per Popup, Story mit Host-Zeitstempeln und Sprecher in erster Person sowie einmalig gespeicherter Dalek-Klangtext; Format 6 / Protokoll 2 |
| 26.10.2026 | 0.8.0 | Raummeister-Auswahl mit Ja/Nein und verbindlicher Portalwahl, gespeicherter 20×20-Kacheleditor mit zweifeldrigem Portal/Startfiguren, Live-Aufbau am Portal, Startfilm, sparse Karte und Wandprüfung, große 3-Sekunden-Ereignisse, Altpartien-Aufbau nachtragen; Format 7 / Protokoll 3 |
| 26.10.2026 | 0.8.1 | Eindeutige Portalcode-Prüfung mit Inlinefehler/Fokus, Korrektur ohne Sackgasse, vollständig aufgeräumte alte Popups und Editoransicht, erhaltene ursprüngliche Anmeldenachweise für sicheren Wiederbeitritt; Spielregeln/Format/Protokoll unverändert |
| 26.10.2026 | 0.9.0 | Name ohne Raumcode, Reset mit Sicherung, komplette benannte Spielstände und Datei-Export/Import, Vierer-Kacheln/rote Wände/zehn Typen/Kerkerzustand, drehbarer Spielerkompass und Nord-Popup, volle Story, Format 8/Protokoll 4 und Migration alter Pläne |
| 26.10.2026 | 0.9.1 | Drei Resetoptionen/ohne Archiv, vollständiger Portal-Hotspotreset, gemeinsame Lobby und rotes Spieler-×, direkter WLAN-Start, reduzierte Einstellungen, große Quartettbilder ohne Beschnitt, kurzer Nord-Hinweis, je Client synchrone Karten-/Kompassrotation |
| 26.10.2026 | 0.9.2 | Kleine/Große Kacheln, identische lokale Vektorgrafiken auf Portal/Client samt Kompassdrehung, globale Koordinaten A1/B1/A2/B2 bis AN40 an jedem Teilfeld, Client-Kartenzoom per Tasten und Zwei-Finger-Geste sowie Sprung zur eigenen Figur, Format/Protokoll kompatibel |
| 26.10.2026 | 0.10.0 | Private Portal-Musik/Beitrittssound mit einmaligen Cues, geheimer zufälliger erreichbarer Portalschlüssel, Finder-Popup, native Schlüsselanimation, gespeicherte Tasche, Zweikachel-Portal wieder in Palette; Format 9 / Protokoll 5 |
| 26.10.2026 | 0.10.1 | Mittige fette Teilfeldkoordinaten, neue Kacheln nur per Drag, scrollende Story-Sprechblasen, hohe Bild-Steckbriefkarte mit Infos unten, direkt antippbarer W6 und Kompass daneben, mitlaufender vergrößerter Kartenausschnitt |

## 13. Fortsetzung in einem anderen Chat

Der Nutzer kann schreiben:

> Bitte entwickle mein Android-Tabletop-Projekt weiter: https://github.com/cebrero/Dr_Who.
> Lies zuerst AGENTS.md und die komplette ONBOARDING.md sowie den aktuellen Code und Git-Stand.
> Berücksichtige bestätigte Anforderungen und offene Vorschläge und setze beim nächsten Schritt an.

Der neue Chat soll zuerst tatsächlichen Remote-Stand lesen, nicht diese Übergabe aus dem Gedächtnis
rekonstruieren. Bei Zugriffsschwierigkeiten benennen, welcher Zugriff fehlt. Vor neuen Annahmen
die Abschnitte 2, 5, 6, 10 und 11 prüfen und nach jeder Änderung diese Datei aktualisieren.

## 14. Anleitung bei jedem Build aktuell halten

**Verbindlicher Entwicklungsablauf:** Vor jedem Build diese Datei mit Spielablauf, geltenden
Regeln, Grenzen, Vereinbarungen, App-Version und Tests abgleichen. Tatsächlich geänderte
Regeln vollständig hier beschreiben; Vorschläge bleiben als solche markiert. Dann den
Quellstand mit `./gradlew updateOnboardingStamp` zuordnen und den Build ausführen.

- `verifyOnboarding` läuft vor jedem APK-Build und vor Gradle-Kerntests.
- Es prüft die App-Version und einen SHA-256 über Produktionsquellen und Buildkonfiguration.
  Geänderter Code ohne aktualisierte Prüfmarkierung blockiert den Build.
- `updateOnboardingStamp` aktualisiert nur die Prüfmarkierung. Es schreibt keine Spielregeln
  und ersetzt keine inhaltliche Prüfung. Die Anleitung muss zuvor tatsächlich geprüft werden.
- Textdatei-Zeilenenden werden für diese Prüfung normalisiert, damit Windows/Linux übereinstimmen.
- Jeder APK-Build enthält eine Kopie der geprüften ONBOARDING.md unter den App-Assets.
- Neue Tests/Codeänderungen und spätere Builds: Ergebnisse hier aktuell halten. Keine automatisch
  erfundenen Testergebnisse und keine pauschale Behauptung, ein Hash prüfe die Bedeutung des Texts.
- `PROJEKT.md` bleibt nur als Verweis für frühere Links bestehen. Keine zweite Regelquelle pflegen.

## 15. Bestätigte Gestaltung – Spieler B, Portal A

**Nutzerentscheidung vom 25.10.2026:** Design B gefällt dem Nutzer für die Spielerhandys
am besten; Design A ist für das Portal-Handy bestätigt. Nicht erneut nach A/B/C fragen.

- **Spieler: B „Zeitreise-Archiv“** – helle Pergamentflächen, Petrol-Schaltflächen,
  Messing-/Goldakzente und Serifenschrift für Überschriften. Gilt für Anmeldung, Auswahl,
  Steckbriefe, Würfel-/Bewegungsansicht und Angriffsansicht. Die TARDIS-Warteanimation bleibt
  als optionale Weltraumszene im Tab „Spiel“ innerhalb des Archivrahmens erhalten;
  seit 0.6.0 ist der eigene Steckbrief die Standard-Warteansicht.
- **Portal: A „TARDIS-Konsole“** – dunkelblau, Cyan, klar erkennbare Verbindungs-/Spieldaten
  und leuchtende Portalwirkung. Die vorhandene dunkle Android-Portalbasis bleibt dafür bestehen.
- **C „Zeitwirbel“** wurde nicht als Gestaltung ausgewählt.

In 0.3.1 wurde die Spieleroberfläche entsprechend mit lokalem CSS umgestellt. Es werden keine
externen Fonts oder Texturen geladen. Große Bedienelemente und sichtbare Fokusmarkierungen
bleiben erhalten. Spielregeln und Speicherformat 4 sind gegenüber 0.3.0 unverändert.

Die zuvor generierten Bilder sind weiterhin Konzeptvorlagen, keine echten App-Screenshots.
Fotos, aufwendige Illustrationen, dekorative Bildbestandteile und Portalvideos daraus sind noch
nicht als App-Assets übernommen. Der aktuelle Ausbau übernimmt Farben, Typografie und
Bedienflächen; eine pixelgleiche Nachbildung des Bildes wird nicht behauptet. Charakterfotos
und endgültige Werte folgen nach den tatsächlichen Figuren des Nutzers.

Ab 0.4.0 wurde die Portal-Lobby auch strukturell überarbeitet und zunächst die drehende TARDIS sowie
stilisierte Charakterbilder ergänzt. Der Nutzer verlangt dauerhaft einen einfachen, immersiven
Einstieg mit vielen passenden Grafiken/Animationen; diese sollen Orientierung und Atmosphäre
unterstützen, ohne die Bedienung zu überladen. Seit 0.5.0 ersetzt auf ausdrücklichen Wunsch
ein statischer Hintergrund die Animation auf der Portal-Startseite; der neue Wunsch hat Vorrang.

## 16. Technische Referenzen

- Hotspot-API (SSID-/Passwort-Setter bleiben System-API): https://developer.android.com/reference/android/net/wifi/WifiManager#startLocalOnlyHotspotWithConfiguration(android.net.wifi.SoftApConfiguration,%20java.util.concurrent.Executor,%20android.net.wifi.WifiManager.LocalOnlyHotspotCallback)
- JmDNS: https://github.com/jmdns/jmdns
- ZXing: https://github.com/zxing/zxing
- Android-Lokalhotspot und Berechtigungen: https://developer.android.com/develop/connectivity/wifi/localonlyhotspot
- AGP 8.13-Kompatibilität: https://developer.android.com/build/releases/agp-8-13-0-release-notes
- Gradle 8.13: https://docs.gradle.org/8.13/release-notes.html

- Android-mDNS-Resolver: https://source.android.com/docs/core/ota/modular-system/dns-resolver

Diese Quellen betreffen Technik; die vorgeschlagenen Spielregeln sind eigene Entwürfe und keine
offiziellen Doctor-Who-Spielregeln.
