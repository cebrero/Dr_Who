# Dr_Who – Onboarding, Spielablauf und Spielregeln

Stand: **25.10.2026 · Version 0.6.0 · Spieler-Tabs, vollständiger Steckbrief und korrekte Würfellandung**
<!-- app-version: 0.6.0 -->
<!-- reviewed-source-sha256: c7711e9ea1a080e2415bcb48206fa3b46b9f70593a42439691fee123edf9b0c9 -->
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
- Einfaches Spiel-WLAN als Ziel: Name `Dr_Who`, Passwort `drwho123`. Die App führt zur
  einmaligen System-Hotspot-Einrichtung und übernimmt die manuell bestätigten Daten.
  Alternativ automatischer Android-Lokalhotspot mit tatsächlich vom System vergebenen
  Zugangsdaten. Auch API 36 erlaubt einer gewöhnlichen App keine freien SSID-/Passwort-Setter.
- Maximal vier registrierte Spielerplätze wird in der App erzwungen. Die Zahl technisch möglicher
  WLAN-Clients hängt zusätzlich vom Telefon ab und ist auf echter Hardware zu prüfen.
- WLAN-Passwort und sechsstelliger Spiel-Beitrittscode sind getrennt. Jede Anmeldung erhält
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

## 4a. Spielablauf und Spielregeln – verbindlicher Stand 0.3.0

### Vor der Partie

1. Portal-Handy zeigt eine klare Lobby mit statischem Doctor-Who-Hintergrund, WLAN-Zugang, Spielseite und
   Portalcode. WLAN-/Seiten-QR-Codes vereinfachen den Einstieg; Netzwerkdetails siehe Abschnitt 8a.
2. Bis zu vier Spieler treten am eigenen Handy mit Namen und Code bei.
3. Jeder wählt einen Steckbrief mit Foto/Beschreibung, Ausdauer, Kraft und Sonderfähigkeiten.
   Gewählte Figuren sind für andere gesperrt. Alle beigetretenen Spieler sind Partner.
4. Portal zeigt, wie viele bereits gewählt haben, plus Spielername und zugeordnete Figur.
5. Nach der Spielerwahl kann der Host übrige bekannte Figuren als Gegner hinzufügen.
6. Später: App erzeugt Aufbau aus tatsächlichem Teileinventar; Spieler bauen Plan nach und
   bestätigen Aufstellung. Derzeit gibt es stattdessen nur ein freies Testfeld.
7. Host startet bzw. setzt die Partie fort. Normale Spielerhandlungen laufen am eigenen Gerät.

### Spielerzug

| Schritt | Regel | Umsetzung 0.3.0 |
| --- | --- | --- |
| 1 | Aktiver Spieler würfelt zuerst einen W6 am eigenen Handy | Host würfelt auf Tastendruck und speichert verbindlich; kein erneutes Würfeln bei Retry |
| 2a | Bei 1, 2, 3 oder 4: genau diese Zahl Felder ziehen | Direkt zur Bewegung; dies ist die aus der Nutzerregel abgeleitete Interpretation |
| 2b | Bei 5 oder 6: Bewegung ODER Charakteraktion wählen | Zwei Auswahlmöglichkeiten; kein Bewegen plus Aktion |
| 3a | Bewegung immer genau um die Augenzahl, nicht nur bis zu dieser Zahl | Reststrecke wird nach jedem gespeicherten Schritt reduziert |
| 3b | Nur gerade/orthogonale Schritte; Richtungswechsel unterwegs erlaubt | Nord/Ost/Süd/West, keine Diagonalen; z. B. 2 nach Norden, dann 3 nach Osten bei einer 5 |
| 3c | Spieler meldet seine Bewegungen am eigenen Gerät und bewegt die echte Figur passend | Schrittweise Eingabe im Testfeld; App kann die physische Bewegung nicht automatisch erkennen |
| 3d | Alternativ bei 5/6: charakterabhängige Aktion | Aktionswahl protokolliert; konkrete Wirkung noch nicht umgesetzt |
| 4 | Nach vollständiger Bewegung bzw. Aktion folgt der nächste Spieler | Nach exakt verbrauchter Strecke bzw. Test-Aktionswahl; nächster Spieler beginnt wieder mit W6 |

Im Prototyp ist die 5/6-Entscheidung verbindlich, sobald „Bewegen“ gewählt wurde. Während
angefangener Bewegung kann nicht nachträglich zur Aktion gewechselt werden. Das ist eine
technische Festlegung für klare Zustände; ein späterer Zurück-/Planungsmodus ist noch offen.
Die aktuelle Taste „Aktion wählen · Techniktest“ beendet den Zug nach protokollierter Wahl,
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

### Noch vom Nutzer zu erfassen

| Teil | Anzahl | Maße/Verbindungen | Status |
| --- | --- | --- | --- |
| Rechteckiges Feld ohne Wand | unbekannt | Breite/Tiefe, Magnetpositionen | erfragen |
| Feld mit einer geraden Wand | unbekannt | Wandseite, Tür möglich? | angekündigte Zählung |
| Eckfeld | unbekannt | zwei benachbarte Wände? innen/außen? | Form bestätigen |
| Feld mit parallelen Wänden | unbekannt | Abstand, Gangbreite | angekündigte Zählung |
| Einzelwände/Türen | unbekannt | fest am Feld oder frei aufsetzbar? | erfragen |
| Verließ | unbekannt | Feldanzahl, Zugänge, Aufstellmaß | genannt |
| Friedhof | unbekannt | Feldanzahl, Zugänge, Aufstellmaß | genannt |
| Brunnen | unbekannt | blockiert Feld oder begehbar? | genannt |
| Figuren | unbekannt | Name, Anzahl, Sockelgröße | Nutzer zeigt sie später |
| Portalhalterung | unbekannt | Telefon, sichtbare Displayfläche, Standort | Konzept genannt |

Zusätzlich klären: Entspricht jedes gedruckte Plättchen einem Bewegungsfeld oder mehreren?
Dürfen die rechteckigen Teile um 90° gedreht werden? Passt das Magnetraster in jeder Drehung?
Maximale Tischfläche und gewünschte Spieldauer? Müssen alle Teile benutzt werden?
Was bedeutet „extra Raum“ baulich: abgetrennt oder über einen Gang angeschlossen?

### Zielarchitektur – noch nicht implementiert

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
  und Startaufstellung vor dem ersten Zug. Später manueller Editor als Option.

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

**Heute umgesetzt:** Ein versionierter binärer lokaler Demo-Spielstand (Format 5, ältere Formate
1, 2, 3 und 4 lesbar) mit Spielern und geheimen Tokens, Beitrittscode, Partie-ID, gewählten Charakter-IDs,
Gegnerliste, Partner-/Gegnerphase, aktuellem Gegner, Bewegungshelfer und Bestätigung, gespeichertem
Angriffsereignis samt Ziel und Text, Runde, aktiver Person, Demo-Koordinaten, letztem W6,
Zugphase (würfeln/entscheiden/bewegen), aktuellem Wurf und verbleibenden Schritten,
Würfelereignis-ID und zugehöriger Spieler-ID, Zustandsrevision, 60 Protokollzeilen und den letzten 128 Anfragequittungen. Android `AtomicFile`
schreibt den Spielstand. Erst nach erfolgreichem Schreiben wird der neue In-Memory-Zustand
übernommen. Beschädigte/unbekannte Spielstände werden nicht automatisch überschrieben.

**Noch nicht:** echte Karte/Inventar, verbindliche Charakterwerte und ausgeführte Spezialregeln, Auswahl mehrerer Partien, Export/Import,
Wiederaufbauansicht, neue Partie im UI, umfassende zukünftige Migrationen, vollwertiges Ereignisjournal oder Host-Wechsel.
Eine Deinstallation bzw. das Löschen der App-Daten entfernt bisher den lokalen Spielstand.

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

## 9. Technische Grundlage 0.6.0

- `app`: native Activity, minSdk 26 (Android 8), target/compileSdk 36, Java 17.
- Build: AGP 8.13.2, Gradle 8.13, JDK 17. Ab 0.4.0 JmDNS 3.6.3 für den lokalen Hostnamen,
  SLF4J-NOP 2.0.17 und ZXing Core 3.5.3 zum Erzeugen von QR-Codes. Keine Online-Laufzeitabhängigkeit.
- `core`: unabhängige Java-Regellogik und HTTP-Server. Zuerst Port 80 versuchen, bei fehlender
  Verfügbarkeit auf 8080 ausweichen. Tatsächlich gebundenen Port stets in UI/QR/Annonce verwenden.
- Spieleroberfläche in `app/src/main/assets/player.html`, ohne CDNs oder Internetabhängigkeiten.
- REST-artige API: POST `/api/join`, GET `/api/state`, POST `/api/command`; kein Websocket
  nötig für den ersten Stand. Zusätzlich POST `/api/character` für die exklusive Auswahl.
  Browser fragt Zustand alle 1,5 Sekunden ab.
- Spieler melden sich mit Namen und Code an und wählen eine Figur. Erst nach Auswahl aller
  beigetretenen Spieler kann der Spielleiter starten. Charakterwechsel vor Start ist möglich;
  die vorherige Figur wird erst nach erfolgreichem Speichern der neuen Wahl freigegeben.
- Freies 6×6-Technikfeld A1–F6: orthogonale Einzelschritte, kein Stapeln, keine Wände.
- Jeder Zug beginnt mit einem gespeicherten W6. Bei 1–4 direkt bewegen, bei 5/6
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
5. **Fortsetzung:** mehrere Partien, Export/Import, Wiederaufbau, Wiederbeitritt und Recovery.
6. **Atmosphäre:** auf Halterung abgestimmtes Portal, gelieferte Medien, Soundoptionen und Feinschliff.

### Nächster konkreter Schritt mit dem Nutzer

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
Charakterfähigkeiten, Gegner-KI, echte Karte und eine vollständige Mission sind noch offen.

### APK-Signierung und Fortsetzung ab 0.5.0

Der ursprüngliche temporäre Debug-Schlüssel für APK 0.4.0 ist in der aktuellen Umgebung nicht
mehr vorhanden. 0.5.0 wurde mit einem neuen Debug-Schlüssel signiert. Deshalb **kein direktes
Update über 0.4.0 möglich**. Bestehende Installation nicht ohne Sicherung entfernen: Android
löscht dabei ihren Spielstand; eine komfortable Export-/Importfunktion ist noch offen.

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

Diese Quellen betreffen Technik; die vorgeschlagenen Spielregeln sind eigene Entwürfe und keine
offiziellen Doctor-Who-Spielregeln.
