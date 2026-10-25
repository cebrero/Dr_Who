# Dr_Who – zentrales Projektgedächtnis

Stand: **25.10.2026 · Version 0.2.0 · Charakterwahl, Partner/Gegner und Gegnerphasen**
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
| Portal-Lobby | Während der Auswahl Verbindungshilfe, Beitrittscode, Fortschritt und Zuordnung Spielername → Figur anzeigen |
| Teams | Alle beigetretenen Spieler sind Partner. Übrige bekannte Figuren können danach ausschließlich am Portal als Gegner hinzugefügt werden |
| Gegnerbewegung | Wenn ein Gegner bewegt werden muss, bestimmt die App zufällig einen Spieler für die physische Ausführung |
| Warteanzeige | Handys gerade nicht beteiligter Spieler zeigen eine fliegende TARDIS |
| Angriffsanzeige | Angegriffener Spieler sieht den eigenen Steckbrief, die Attribute des Gegners und die konkreten Folgen für seinen Charakter |
| Beispiel Doctor | Schallschraubenzieher kann Türen öffnen |
| Beispiel Daleks | Schießen nur bis zu drei Felder in geraden Richtungen |
| Beispiel Weinende Engel | Gegner im Sichtfeld für zufällig ein bis drei Runden aussetzen lassen |
| Spielleitung | App speichert Aktionen, verfolgt Bewegungen und entscheidet den Zufallsteil von Aktionen |
| Darstellung | Portal- und Zwischenanimationen auf dem Host; Nutzer kann geeignete Dateien bereitstellen |

Diese Beispiele sind Nutzerwünsche, aber noch keine vollständig definierten oder ausbalancierten
Regeln. Insbesondere Sichtlinien, Trefferfolgen, Bewegungsweiten, Gegnersteuerung und genaue
Aktionsökonomie müssen gemeinsam geklärt werden.

## 3. Arbeitsentscheidungen dieser ersten Umsetzung

Technische, später änderbare Entscheidungen; nicht mit zugesagten Endfunktionen verwechseln:

- Android-App ist alleinige Instanz für gültige Züge, Zufall und Speichern.
- Standard-Android-Lokalhotspot, alternativ gemeinsames WLAN/System-Hotspot; keine Cloud und
  kein Internet während der Partie erforderlich. WLAN-Name/Passwort zunächst von Android vorgegeben.
- Maximal vier registrierte Spielerplätze wird in der App erzwungen. Die Zahl technisch möglicher
  WLAN-Clients hängt zusätzlich vom Telefon ab und ist auf echter Hardware zu prüfen.
- WLAN-Passwort und sechsstelliger Spiel-Beitrittscode sind getrennt. Jede Anmeldung erhält
  einen geheimen Wiederverbindungstoken. Keine MAC-Bindung oder persönlichen Onlinekonten.
- Bewegungen werden von Spielern eingegeben und auf dem echten Tisch nachvollzogen.
  Die App erkennt physische Figuren derzeit nicht automatisch; Kameras/Sensoren sind nicht vereinbart.
- Deutschsprachige Oberfläche. Java 17 als schlankes Android-Grundgerüst mit getrenntem Kernmodul.
- Einfache selbst gezeichnete Portalanimation als Platzhalter. Keine Serienmedien beschafft.
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

1. Spielerzüge nacheinander; Oberfläche zeigt aktive Figur und gültige Möglichkeiten.
2. Ein Spieler wählt Bewegung oder eine charakterabhängige Aktion. Reichweite und Teilaktionen
   erst gemeinsam festlegen. Denkbare spätere Alternative: zwei Aktionspunkte.
3. Host prüft Position, Wände, Türstatus, Sichtlinie, Reichweite, Status und Ressourcen.
4. Wenn nötig würfelt der Host, speichert Ergebnis und Konsequenzen und zeigt die Auflösung an.
5. Am Rundenende mögliche Gegner-/Ereignisphase und Bedrohungsanstieg.

Noch keine implementierte Missionsschleife. In der Technikdemo gilt nur eine Eingabe pro Zug.

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
   die ausgeführte Tischbewegung. Alle übrigen Handys bleiben in der TARDIS-Warteanzeige.
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
Fotos fehlen noch: Die Ansicht zeigt „FOTO FOLGT“ und unterstützt lokale JPEG-Dateien unter
`app/src/main/assets/portraits/<charakter-id>.jpg`. Server und Browser benötigen dafür kein Internet.
Ausdauer und Kraft stehen ausdrücklich auf „Noch festzulegen“; Fähigkeitstexte geben die
bisherigen Nutzerideen wieder und sind noch keine implementierten Kampfregeln.

Gegnersteuerung ist derzeit ein moderierter Technikablauf: Der Host fordert die Bewegung an,
nennt Weg/Ziel und trägt die konkrete Angriffsfolge ein. Automatische Entscheidung, Positionen
von Gegnern, Reichweiten, Treffer/Schaden und Statusdauer folgen mit Brettmodell und Regeln.
Die Angriffsanzeige verändert bisher keine Ausdauer- oder Statuswerte von selbst. Das Wort
„aktiviert“ bezeichnet den Ansichtswechsel in einer geöffneten Webseite, nicht Fernentsperren.

Vorhandene 0.1.0-Spielstände behalten Runde, Positionen und Anmeldung. Vor Fortsetzung müssen
zuerst Figuren gewählt werden; es wird keine zufällige Figur zugewiesen. 0.2.0 speichert zusätzlich
alle oben genannten Gegneraufträge/Angriffsdaten. Die Datei heißt aus Kompatibilitätsgründen
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

**Heute umgesetzt:** Ein versionierter binärer lokaler Demo-Spielstand (Format 3, ältere Formate
1 und 2 lesbar) mit Spielern und geheimen Tokens, Beitrittscode, Partie-ID, gewählten Charakter-IDs,
Gegnerliste, Partner-/Gegnerphase, aktuellem Gegner, Bewegungshelfer und Bestätigung, gespeichertem
Angriffsereignis samt Ziel und Text, Runde, aktiver Person, Demo-Koordinaten, letztem W6,
Zustandsrevision, 60 Protokollzeilen und den letzten 128 Anfragequittungen. Android `AtomicFile`
schreibt den Spielstand. Erst nach erfolgreichem Schreiben wird der neue In-Memory-Zustand
übernommen. Beschädigte/unbekannte Spielstände werden nicht automatisch überschrieben.

**Noch nicht:** echte Karte/Inventar, verbindliche Charakterwerte und ausgeführte Spezialregeln, Auswahl mehrerer Partien, Export/Import,
Wiederaufbauansicht, neue Partie im UI, Migration, vollwertiges Ereignisjournal oder Host-Wechsel.
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

Heute: Während der Auswahl zeigt die Portalansicht Verbindungsdaten und Auswahlfortschritt
samt Spielernamen und Figuren. Nach Spielstart Canvas-Ringe in Cyan/Blau, Rundenüberschrift
und letzter W6 als eigener Platzhalter. Antippen öffnet Host-Steuerung. Noch keine Medienbibliothek
oder Videoimport. Nicht beteiligte Spieler sehen eine eigene CSS-Animation einer fliegenden blauen
TARDIS. Das betroffene Handy schaltet innerhalb der geöffneten Webseite zur Angriffsansicht;
ein ausgeschaltetes oder gesperrtes Telefon kann die lokale Website nicht verlässlich aufwecken.
Bildschirm-Timeout und Energieverhalten müssen am Gerät geprüft werden.

## 9. Technische Grundlage 0.2.0

- `app`: native Activity, minSdk 26 (Android 8), target/compileSdk 36, Java 17.
- Build: AGP 8.13.2, Gradle 8.13, JDK 17. Keine Drittanbieter-Laufzeitbibliotheken.
- `core`: unabhängige Java-Regellogik und HTTP-Server auf Port 8080.
- Spieleroberfläche in `app/src/main/assets/player.html`, ohne CDNs oder Internetabhängigkeiten.
- REST-artige API: POST `/api/join`, GET `/api/state`, POST `/api/command`; kein Websocket
  nötig für den ersten Stand. Zusätzlich POST `/api/character` für die exklusive Auswahl.
  Browser fragt Zustand alle 1,5 Sekunden ab.
- Spieler melden sich mit Namen und Code an und wählen eine Figur. Erst nach Auswahl aller
  beigetretenen Spieler kann der Spielleiter starten. Charakterwechsel vor Start ist möglich;
  die vorherige Figur wird erst nach erfolgreichem Speichern der neuen Wahl freigegeben.
- Freies 6×6-Technikfeld A1–F6: orthogonale Einzelschritte, kein Stapeln, keine Wände.
- Eine Bewegung ODER ein W6-Test ODER Passen beendet den Zug. Keine Missionswertung.
- Zufall entsteht ausschließlich am Host; Browser präsentiert gespeichertes Ergebnis.
- Authentisierte Zustandsabfrage, separate Tokens, kein Host-Steuerungsendpunkt.
- Längenlimits, begrenzter Threadpool, Anmeldedrosselung, keine CORS-Freigabe,
  Same-Origin/Custom-Header-Prüfung und numerische IPv4-Hostnamen.
- HTTP im lokalen WLAN; keine TLS-Infrastruktur. Nicht ins Internet weiterleiten.
- Vordergrundprototyp: App im Vordergrund und Bildschirm eingeschaltet lassen. Kein
  Foreground-Service. Android-/Herstellerverhalten und Energiesparen müssen real getestet werden.
- Hotspot kann nach Neustart andere Zugangsdaten/IP haben. Token im Browser ist derzeit an
  dessen Seitenadresse gebunden. Spielerplatz freigeben, Wiederbeitritt per neuem Gerät und
  robuste Adresse/QR-Codes sind noch geplant; sie sind keine fertig gelösten Funktionen.

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

Die kooperative Rollenverteilung ist geklärt. Als Nächstes Figurenliste und Fotos sowie
Ausdauer/Kraft und gewünschte Sonderfähigkeiten erfassen; danach den Zielvorschlag „Der Zeitriss“
konkretisieren und Gelände aufnehmen. Als kleinster sinnvoller Inventarschritt
reichen zunächst ein Foto von jeder Plättchenform, Stückzahl und Breite×Tiefe sowie die Angabe,
ob Wände/Türen fest oder aufsteckbar sind. Noch keine vollständige Datenerhebung erzwingen.

Parallel nach verfügbarer APK die Android-Version und das Host-Telefon notieren und einen
echten Vier-Spieler-Hotspottest durchführen. Das Host-Telefon ist bislang nicht benannt.

## 11. Tests und bekannte Grenzen

Für 0.2.0 am 25.10.2026 tatsächlich ausgeführt:

- `bash scripts/test-core.sh` und Gradle `:core:smokeTest`: **81 Prüfungen bestanden**.
- `npm test`: **28 DOM-Verhaltenstests bestanden**, darunter exklusive Auswahlzustände,
  Retry nach Abbruch, eigene/andere Züge, TARDIS-Warteansicht, Helferauftrag, Doppeltippen,
  gezielte Angriffsansicht, sicheres Anzeigen des Ergebnistextes und veraltete Zustandsantworten.
- Gradle 8.13 `:core:smokeTest :app:assembleDebug :app:lintDebug`: **BUILD SUCCESSFUL**.
  Nach Optimierung der Portal-Lobby erneut `:app:assembleDebug :app:lintDebug` erfolgreich.
- Android-Lint: **0 Fehler, 8 Warnungen** (deutsch fest eingetragene UI-Texte und auf älteren
  Android-Versionen ignorierte Manifestattribute). Kompatibilitätsaufrufe teils deprecated.
- Spieler-JavaScript mit `node --check` geprüft; Android-XML geparst; `git diff --check` sauber.

Kernprüfungen umfassen weiterhin vier Plätze, Code/Token-Schutz, Zugreihenfolge, Grenzen,
belegte Felder, Revisionen, idempotente und parallele Anfragen, Neustart und Speicherfehler.
Neu geprüft: zwei gleichzeitige Bewerber um dieselbe Figur, Partner-/Gegnertrennung,
Host-exklusive Gegnerverwaltung, Auswahl vor Spielstart, Gegnerphase, genau einmal bestimmter
Bewegungshelfer, berechtigte Bestätigungen, Rundenwechsel nach Gegnern, gespeicherter Angriff
und Lesen alter Spielstandformate 1/2. Der vierte Katalogeintrag im Vier-Spieler-Test ist reine
Testdaten-Fixture und keine zusätzliche Figur im Produktkatalog.

Build-Hinweis: SDK-Plattform und Build Tools sind lokal vorhanden. Nicht installierte Platform-Tools
führen in dieser Umgebung zu einer Warnung, verhindern den erfolgreichen APK-Build aber nicht.
Die optionalen npm-Abhängigkeiten dienen nur den DOM-Tests, nicht dem Offline-Spielbetrieb.

Noch ausstehend: echter Telefon-/Hotspottest mit vier Geräten, visuelle und Energie-/Lebenszyklus-
Prüfung am Host, Bildschirm-Timeouts der Spieler, App-Abbruch und Wiederbeitritt bei geänderter IP.
DOM-Tests ersetzen keine Sichtprüfung auf realen Handys. Fotos, verbindliche Attribute, wirkende
Charakterfähigkeiten, Gegner-KI, echte Karte und eine vollständige Mission sind noch offen.

## 12. Änderungshistorie

| Datum | Version | Änderung |
| --- | --- | --- |
| 25.10.2026 | 0.1.0 | Repository angelegt, Originalanforderungen und offene Fragen dokumentiert; kooperatives Missionsziel vorgeschlagen; Android-/Browser-/Host-Grundlage als Technikdemo erstellt |
| 25.10.2026 | 0.2.0 | Exklusive Steckbrief-Auswahl, Portal-Lobby, Partner-/Gegnerzuordnung, Gegnerphase mit zufälligem Bewegungshelfer, TARDIS-Warteanzeige, Angriffsansicht und rückwärts lesbares Speicherformat 3 |

## 13. Fortsetzung in einem anderen Chat

Der Nutzer kann schreiben:

> Bitte entwickle mein Android-Tabletop-Projekt weiter: https://github.com/cebrero/Dr_Who.
> Lies zuerst AGENTS.md und die komplette PROJEKT.md sowie den aktuellen Code und Git-Stand.
> Berücksichtige bestätigte Anforderungen und offene Vorschläge und setze beim nächsten Schritt an.

Der neue Chat soll zuerst tatsächlichen Remote-Stand lesen, nicht diese Übergabe aus dem Gedächtnis
rekonstruieren. Bei Zugriffsschwierigkeiten benennen, welcher Zugriff fehlt. Vor neuen Annahmen
die Abschnitte 2, 5, 6, 10 und 11 prüfen und nach jeder Änderung diese Datei aktualisieren.

## 14. Technische Referenzen

- Android-Lokalhotspot und Berechtigungen: https://developer.android.com/develop/connectivity/wifi/localonlyhotspot
- AGP 8.13-Kompatibilität: https://developer.android.com/build/releases/agp-8-13-0-release-notes
- Gradle 8.13: https://docs.gradle.org/8.13/release-notes.html

Diese Quellen betreffen Technik; die vorgeschlagenen Spielregeln sind eigene Entwürfe und keine
offiziellen Doctor-Who-Spielregeln.
