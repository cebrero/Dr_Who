# Dr_Who – Onboarding, Spielablauf und Spielregeln

Stand: **28.10.2026 · Version 0.15.0 · Portal-Zielfeld, Finale und bestätigte Kartenbewegung**
<!-- app-version: 0.15.0 -->
<!-- reviewed-source-sha256: d1de85c7ee358f3493636dbe4b8c3fe0004058f5fc1d0441d8d25353527d37f3 -->
Repository: https://github.com/cebrero/Dr_Who · Hauptbranch: `main`

**Neu in 0.15.0:** Portal über zwei Kacheln, aber **ein logisches Feld/eine Koordinate**, drei
rote Wandseiten und nur die offene lange Seite als Zugang. Keine Gegenstände auf Portalfläche.
Bewegung am Client durch erreichbare Kartenziele, frei korrigierbare gespeicherte Vorschau und
explizites Bestätigen des ganzen Wegs statt Richtungstasten. Beide gewählten Ziele beenden die
Partie automatisch. Ab echtem Schlüsselfund privater Portalfilm in Schleife und einmal TARDIS-Sound;
zuvor nur mittiges aktives Porträt. Statusinformationen während des Films auf unbeteiligten Clients.
Details und Interpretationen Abschnitt **8p**; aktueller Prüfstand Abschnitt 11.

**Neu in 0.14.0:** Portal-Spielansicht als lebendige Zeitreise-Bühne statt großer Textkarten:
leuchtende bewegte Energieringe/Sternenhimmel, große Charakterdarstellung bei Go, inszenierter
W6 mit bestehender Client-Freigabe, klare Bewegung X → Y, verbleibende Schritte und kompakte
Teamleiste. Verwaltung im Portal-Menü oben rechts. Details Abschnitt 8o, Tests Abschnitt 11.

**Neu in 0.13.0:** Immersive Dossiers ohne Serien-/Bildhinweise; Zielabstimmung vor der
Raummeisterwahl (Mehrheit, bei Gleichstand Host-Los), private Vollbild-Portalanimation während
Abstimmung/Ergebnis. Der Portalwürfel wartet auf die Aufdeckung am Spielerhandy. Eckkachel-Wände
entsprechen jetzt ihrer sichtbaren Grafik. Details 8m/8n; Build-/Teststatus Abschnitt 11.

**Seit 0.12.0:** Acht bestätigte Figurentypen / elf einzelne Miniaturen sind auswählbar,
mit neuen Dossiers und Bildern. Raummeister kann direkt im Editor benannte Raumvorlagen speichern
und nach Bestätigung laden. Voreinstellung Datum-Zeit_Charaktername. Details unter Abschnitt 8l.
Ausdauer/Kraft und neue Spezialwirkungen bleiben offen; nicht definierte Aktionen sind gesperrt.

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
Die beiden bestätigten Fotos zeigen acht Figurentypen mit elf Miniaturen (Abschnitt 8l).
Ein vollständiges Kachelinventar mit Stückzahlen und Abmessungen ist weiterhin offen.

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

## 4a. Spielablauf und Spielregeln – verbindlicher Stand 0.15.0

### Vor der Partie

1. Portal-Handy zeigt eine gemeinsame Startseite mit statischem Doctor-Who-Hintergrund, WLAN-Zugang,
   Spielseite und darunter dem Team. **Spiel-WLAN starten** startet direkt den Android-Lokalhotspot;
   nur erforderliche Android-Berechtigungen bleiben. WLAN-/Seiten-QR-Codes vereinfachen den Einstieg.
   Neue Registrierung am Portal mit bereitgestelltem TARDIS-Sound begrüßen; bestehende Plätze bleiben still.
2. Bis zu vier Spieler treten am eigenen Handy mit Namen ohne zusätzlichen Portalcode bei.
3. Jeder wählt eine Quartettkarte mit großem vollständigem Porträt oben und Eigenschaften darunter.
   Jede der elf einzelnen Figuren ist exklusiv reservierbar; Duplikat-Miniaturen haben eigene IDs.
   Gewählte Figuren sind für andere gesperrt. Alle beigetretenen Spieler sind Partner.
4. Portal zeigt, wie viele bereits gewählt haben, plus Spielername und zugeordnete Figur.
5. Nach der Spielerwahl kann der Host übrige bekannte Figuren als Gegner hinzufügen.
6. Host drückt Start; der bereitgestellte Themensong spielt einmal. Zuerst stimmen alle am eigenen
   Handy über eines der beiden Spielziele ab. Mehrheit, Gleichstand durch einmaliges Portal-Los;
   Zufall bestimmt die eigene Stimme. Ergebnis drei Sekunden, dann drei Sekunden TARDIS-Flug und zufällige Raummeister-Anfrage an echte
   Spieler. Nein fragt jemand anderen; nach ausschließlich Nein entscheidet das Portal verbindlich.
7. Raummeister erfasst den tatsächlichen Aufbau oder lädt eine benannte Raumvorlage direkt im Editor;
   Speichern einer eigenen Vorlage mit editierbarem Datum-Zeit_Charaktername beendet den Bau nicht.
   Manueller Aufbau weiterhin per Kachel-Editor (20×20, elf Typen einschließlich Portal über zwei Kacheln, 90°-Drehung,
   vier Bewegungsfelder pro normaler Kachel, ein Feld für das ganze Portal, Startfiguren). Portal zeigt den bestätigten Entwurf live und kurze Serienfakten.
   Andere Spieler lesen währenddessen ihr Dossier und bestätigen Gelesen. Nach dem Speichern liest
   auch der Raummeister. Erst alle Bestätigungen erlauben drei Sekunden Startfilm und Go.
   Automatische neue Plätze bevorzugen Abstand zum Portal, manuelle Plätze bleiben erhalten.
   Beim Planspeichern wird ein Portalschlüssel geheim auf
   einem freien erreichbaren Feld **außerhalb des Portals** versteckt. Erst bestätigte Bewegung sammelt ihn ein: Fund-Popup/Tasche, Portalvideo und einmal Öffnungssound.
   Aktuelle Details siehe Abschnitte 8d–8k.
8. Normale Spielerhandlungen laufen am eigenen Gerät. Bereits laufende ältere Partien bleiben
   erhalten und können ihren Plan über die Spielleitung nachtragen. Der Inventargenerator folgt später.

### Spielerzug

| Schritt | Regel | Umsetzung 0.15.0 |
| --- | --- | --- |
| 0 | Zugbeginn mit „Your move...“ und Go bestätigen | Host speichert die Bestätigung; vor Schlüsselfund mittiges Porträt am Portal, nach Fund Portalfilm; keine Aktion vor Go |
| 1 | Aktiver Spieler würfelt zuerst einen W6 am eigenen Handy | Direkt den Würfel antippen; Host speichert verbindlich. Client animiert 2,6 s und bestätigt Aufdeckung; Portal deckt erst danach auf. Kein erneutes Würfeln bei Retry |
| 2a | Bei 1, 2, 3 oder 4: genau diese Zahl Felder ziehen | Direkt zur Bewegung; dies ist die aus der Nutzerregel abgeleitete Interpretation |
| 2b | Bei 5 oder 6: Bewegung ODER Charakteraktion wählen | Zwei Auswahlmöglichkeiten; kein Bewegen plus Aktion |
| 3a | Normaler Weg genau um die Augenzahl; eine erfüllte Portal-Siegbedingung beendet sofort | Host markiert genau passende Endfelder; erreichbares Siegportal auch mit kürzerer Strecke, Rest entfällt ausschließlich beim Sieg |
| 3b | Nur orthogonale Übergänge; Richtungswechsel erlaubt | Host berechnet den ganzen legalen Weg mit Wänden/Figuren. Portalfläche ist ein Knoten mit Zugang nur über ihre offene Front |
| 3c | Erreichbares Zielfeld antippen, ggf. anderes wählen, dann Zug bestätigen | Vorschau am Portal parallel; Figur/Schritte/Schlüsselfund/Story ändern erst durch atomare Bestätigung des vollständigen Wegs. Physische Figur danach entsprechend versetzen |
| 3d | Alternativ bei 5/6: charakterabhängige Aktion | Aktionswahl protokolliert; konkrete Wirkung noch nicht umgesetzt |
| 4 | Ohne Sieg folgt nach Bewegung bzw. Aktion der nächste Spieler | Nach bestätigtem Weg bzw. Test-Aktionswahl Go/W6; bei erfülltem Ziel stattdessen dauerhaft gespeichertes Spielende |

Im Prototyp ist die 5/6-Entscheidung verbindlich, sobald „Bewegen“ gewählt wurde. Während
angefangener Bewegung kann nicht nachträglich zur Aktion gewechselt werden. Das ist eine
technische Festlegung für klare Zustände. Ab 0.15.0 ist das Bewegungsziel vor Bestätigung frei korrigierbar, ohne wieder zur Aktion zurückzukehren.
Für die drei bisherigen Typen und deren Duplikate öffnet die Taste „Aktion wählen · Techniktest“ bei den gerichteten
Fähigkeiten zuerst die Richtungsauswahl Nord/Ost/Süd/West. Erst die gesendete Richtungswahl
beendet den Zug; Abbrechen verbraucht nichts. Gespeichert wird weiterhin nur die Auswahl,
ohne eine Tür zu öffnen, Schaden zu verursachen oder einen Status anzuwenden. Das ist eine
klar gekennzeichnete Übergangslösung, keine fertig implementierte Sonderfähigkeit. Bei den fünf
neu aufgenommenen Typen ist die eigene Spezialaktion noch nicht vereinbart; Taste bleibt gesperrt,
Host weist sie ohne Zugverbrauch ab. Bei 5/6 vorerst normale Bewegung wählen.

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
- Seit 0.15.0 Ziel vor Bestätigung korrigierbar; Auswahl zwischen mehreren gleich langen Routen ist noch nicht vorgesehen (Host nimmt deterministisch einen gültigen Weg).
- Welche Charakteraktion ist bei mehreren Fähigkeiten wählbar, mit welchem Ziel und welcher Probe?
- Treffer, Schaden, Ausdauerverlust, Kraftberechnung, Schutz, Erholung und Niederlage.
- Gegnerverhalten, Gegner-Bewegungsweite, Sichtlinien, Türen und Zeitversetzung/Überspringen.
- Weitere Missionen, Startreihenfolge und gewünschte Spieldauer. Die beiden gewählten Portalziele werden ab 0.15.0 gewertet (8p).

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

**Aktueller Ausbaugrad:** Acht bestätigte Typen, elf physische Figuren: Doctor (1), Dalek (2),
Weinender Engel (2), Sontaraner (1), Cyberman (2), Schneemann (1), Adipose (1), Zygon (1).
Fotozuordnung vom Nutzer bestätigt, Zygon zusätzlich separat bestätigt. Alle sind auswählbar,
maximal vier Personen. Zweite Miniaturen besitzen eigene IDs; Partner-/Gegnerregel bleibt gleich.
Vollständige IDs, Bedienstand, Quellen und Bilder unter Abschnitt 8l.

Steckbriefe zeigen vollständige lokale KI-Porträts und Beschreibungen. Nutzerfotos belegen den
Miniaturenbestand; sie sind nicht automatisch öffentliche Auswahl-Assets. Doppelfiguren verwenden
über ihren Archetyp dasselbe Porträt/Dossier, bleiben aber einzelne Figuren. Ausdauer und Kraft
weiter ausdrücklich „Noch festzulegen“. Bisherige drei Aktionen gerichtete Techniktests; fünf
neue Typen haben bis zur gemeinsamen Regelentscheidung eine gesperrte Spezialaktion.

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
| Figuren | 11 auf zwei bestätigten Fotos | 8 Typen, Sockelgröße unbekannt | Abschnitt 8l; weitere Figuren möglich |
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

**Heute umgesetzt:** Zusätzlich eine dauerhafte benannte Raumsammlung direkt beim Raummeister
(Abschnitt 8l), getrennt von vollständigen Spielständen. Ein versionierter binärer lokaler Spielstand (Format 11, ältere Formate
1–10 lesbar) mit Spielern und geheimen Tokens, Beitrittscode, Partie-ID, gewählten Charakter-IDs,
Gegnerliste, Partner-/Gegnerphase, aktuellem Gegner, Bewegungshelfer und Bestätigung, gespeichertem
Angriffsereignis samt Ziel und Text, Runde, aktiver Person, Rasterkoordinaten, letztem W6,
Zugphase (würfeln/entscheiden/bewegen), aktuellem Wurf und verbleibenden Schritten,
Würfelereignis-ID und zugehöriger Spieler-ID, Go-Bestätigung, Zustandsrevision, 60 alte Protokollzeilen
und vollständige strukturierte Story-Einträge bis zur 8-MiB-Dateigrenze (Zeitstempel, gespeicherte Datumsanzeige, Sprecher, Spieler,
Aussage, Richtung), Raummeister-Auswahl/-ID/-phase/-deadline, Gefragte/Kandidat,
vollständigen 20×20-Kachelplan mit Ausrichtung, Teilfeldraster, Kerkerzuständen und Gegnerpositionen, Portalhinweise, Lesepflicht/-bestätigungen, neue/manuelle Aufstellungsmerker, Interaktionsrevision und die letzten 128 Anfragequittungen. Android `AtomicFile`
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

### Weiterer Verbindungstest des Nutzers (27.10.2026), ungelöst

Zwei Spielerhandys sind im WLAN verbunden. Eines öffnet die Spielseite per IP, das andere lädt
endlos ohne sichtbaren Fehler. `drwho.local` funktioniert weiterhin nicht. Angefragter Vergleich
`http://<tatsächliche-IP>:<Port>/api/info` am betroffenen Handy noch ohne Ergebnis; Modell/Browser
und tatsächliche URL fehlen. Ursache nicht als bestätigt oder behoben behaupten. Der UI-Ausbau
0.14.0 enthält keinen Netzwerkfix. QR-/IP-Ausweichweg bleibt erhalten.

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
   Ausgangsausrichtung der Grafik Ecke Nord/West (Bewegungsprüfung seit 0.13.0 angeglichen),
   Gang Nord/Süd und Tür Nord. Vier 90°-Drehungen.
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

## 8j. Abholung, Bildvergrößerung und Inventarraster ab 0.10.2

Verbindliche Wünsche vom 27.10.2026, umgesetzt:

- Nach **hostbestätigter** Figurenwahl vor Spielstart öffnet am Client das Wartefenster
  **Die TARDIS ist unterwegs!**, mit gewählter Figur, animierter TARDIS und einem von zehn
  humorvollen Texten. Kein Fenster bei bloßem Klick oder fehlgeschlagener Auswahl.
- Texte wechseln alle **10 Sekunden** zufällig, alle zehn einmal pro Durchlauf, ohne direkte
  Wiederholung am Zykluswechsel. Reines Browser-Kosmetiklos; kein Einfluss auf Spielzufall,
  Reservierung oder Start. Polling würfelt den Text nicht neu. Letzten Text, Restvorrat und
  Abbruch je Partie/Spieler/Figur im Browser merken; Neuladen erzeugt keine neue Teilnahme.
- **Abbruch · Figur wechseln** bzw. Escape schließt das Fenster und gibt die Karten frei.
  Die aktuelle Figur bleibt am Host reserviert, bis eine andere erfolgreich gewählt ist.
  Keine stille Freigabe/Abmeldung, kein neuer API-Endpunkt. Abbruch bleibt über Polling/Reload
  erhalten. **Auf die TARDIS warten** öffnet freiwillig wieder. Eine neue bestätigte Figur
  öffnet erneut. Start am Portal beendet das Warten automatisch für Raummeister-/Go-Ablauf. Der Abbruchknopf
  bleibt auch in der kompakten Warteansicht sichtbar; lange Texte dürfen intern scrollen.
- Zehn **eigens formulierte** Wartegags, keine behaupteten Filmzitate. Inspiration: offizielle
  Serienmotive TARDIS, Raumzeitreise, großer Innenraum, eigenwillige Navigation und defekter
  Tarnschaltkreis (https://www.doctorwho.tv/characters/the-tardis, geprüft 27.10.2026).
  Keine neuen Figuren/Regeln aus den Gags ableiten. Vollständiger Wortlaut unten.
- Im eigenen **Steckbrief** das Bild antippen: große modale Ansicht mit vollständigem Bild
  (`contain`), Charaktername und **Schließen ×**; auch Escape/Hintergrundtap schließen.
  Echte Bildtaste mit Tastaturbedienung, Fokus im Dialog und Rückkehr zur Bildtaste. Nur
  bestehende lokale Porträtpfade. Bildfehler mit verständlichem Hinweis statt kaputtem Bild.
  Eigenes Go/Angriff/Fund, Gegnerhelfer-Auftrag und Sitzungswechsel haben Vorrang; kein
  veralteter Bilddialog blockiert.
- **Tasche** hat am Tab und Titel einen kleinen braunen Lederbeutel als lokale Vektorgrafik.
  Gesammelte Items in Kästchen mit **genau drei Spalten**, weitere Reihen entstehen dynamisch;
  der Inventarbereich scrollt nach unten. Name/Bild pro Item; Portalschlüssel erhält vorhandene
  Schlüsselgrafik. Für noch unbekannte Itembilder nur neutrales Symbol, keine erfundenen Items.
  Unverändertes Polling behält Kästchen-DOM/Scrollposition. Lange Namen umbrechen.
- Nur tatsächlich im eigenen Snapshot gesammelte Gegenstände anzeigen. Keine Benutzen-/Tausch-
  oder Siegfunktion ergänzen. Große Inventare werden ausschließlich mit Test-Fixtures geprüft.
- **App 0.10.2 / Code 17**, Speicherformat 9 und Protokoll 5 unverändert. Bestehender Signierschlüssel,
  normales Update und Clientseiten neu laden. Keine neue Partie für diese Oberflächenänderungen.

### Zehn Wartegags (eigene Texte)

1. Du hast die TARDIS gerufen. Sie kommt dich abholen – nur die richtige Minute muss sie noch finden. Bitte etwas Geduld!
2. Die TARDIS ist innen größer. Leider gilt das auch für den Weg vom Kleiderschrank zur Eingangstür. Dein Abholservice ist unterwegs!
3. Der Doctor prüft die Raumzeit-Koordinaten. Ein Zahlendreher, und wir starten beim Urknall. Warten ist gerade die angenehmere Option.
4. Die TARDIS hält noch an einer blauen Polizeibox. Es könnte ihr Spiegelbild sein. Sobald sie das geklärt hat, holt sie dich ab.
5. Der Schallschraubenzieher summt schon. Jetzt sucht der Doctor nur noch den Knopf, der nichts explodieren lässt. Bitte kurz warten.
6. Die TARDIS wollte dich pünktlich abholen. Dann hat sie bemerkt, dass Zeit relativ ist. Bleib bereit – sie arbeitet an ihrer Ausrede.
7. Die Abholung verzögert sich: Im Zeitwirbel ist gerade viel Vergangenheit unterwegs. Bitte nicht vordrängeln, besonders nicht vor dir selbst.
8. Der Tarnschaltkreis wird getestet. Ergebnis: immer noch blaue Polizeibox. Ein bewährtes Design! Gleich geht die Reise los.
9. Der Doctor macht noch einen Sicherheitscheck: Tür zu, Begleiter dabei, Universum noch da. Fast fertig. Hab einen Moment Geduld!
10. Deine Figur ist bereit, die TARDIS fast auch. Falls du dich selbst aus der Zukunft warten siehst: freundlich winken und nichts verraten.

## 8k. Charakterdossiers, Lesebestätigung und Portal-Fakten ab 0.11.0

Verbindliche Wünsche vom 27.10.2026, umgesetzt. „Setze alles neue um“ hebt die frühere
Wartebitte für diesen Ausbau auf. Neueste Präzisierung: **während der Raummeister baut**,
nicht während seiner Suche. Werte und Spezialwirkungen bleiben weiterhin offen.

### Dossier und Startfreigabe

- In `building` lesen alle anderen Spieler das eigene Dossier; der Raummeister behält den
  Editor. Großes vollständiges Porträt, Spezies/Herkunft, Ausdauer/Kraft, frühe Bedienanleitung,
  persönliche Spezialfähigkeit, kurze Hintergrundabschnitte, zusätzliche Szene und links/rechts
  wechselnde Skizzen. Eigenes Bild auch hier antippbar als Großansicht.
- Archivdesign B, vertikal scrollbarer Innenbereich und feste Taste **Gelesen · ich bin bereit**.
  Polling/Bauänderungen erhalten DOM und Leseposition. Keine Mindestlesezeit, keine automatische
  Bestätigung durch Scrollen. Bestätigung wird nur für den authentisierten eigenen Spieler gespeichert.
- Anleitung erklärt Go, Würfel antippen, genau gewürfelte Strecke, 5/6 Bewegung ODER Aktion,
  Kompass/Sitzdrehung, Welt-Norden am Portal, orthogonale Schritte/Richtungswechsel, physische
  Figurenbewegung, Karte/Zoom, Tasche und Story. Richtungswahl und Abbrechen der Aktion erklärt.
- Doctor: bekannte Heimat Gallifrey, früheste Herkunft durch Timeless Child komplexer.
  Dalek: mutierter Kaled in Panzerhülle, Skaro. Engel: Ursprung/Heimat nicht gesichert.
  Serienwissen bleibt ausdrücklich Hintergrund, keine zusätzliche Tabletop-Regel.
- Fähigkeitstexte unterscheiden Wunsch und aktuellen Stand: Sonic öffnet noch keine Türen,
  Dalek berechnet keinen Treffer/Schaden und Engel setzt niemanden aus. Ausdauer/Kraft bleiben
  „Noch festzulegen“. Keine Heilung durch Regeneration, Blickregel, Panzerungswerte oder Items erfunden.
- Nach **Speichern & starten** liest auch der Raummeister. Neue Phase `briefing`: gespeicherte
  Karte, noch kein Zug. Erst **alle Gelesen** starten genau einmal den bisherigen Drei-Sekunden-Film,
  danach normales Go/W6. Kein Timeout oder stiller Bypass. Nach Bestätigung darf weitergelesen werden,
  solange der Aufbau/die anderen Bestätigungen noch laufen.
- Portal zeigt nach Aufbau Karte und **Gelesen N / M** sowie Namen/Charaktere, die noch lesen.
  Entfernte Teilnehmer zählen nicht mehr; verlorener Raummeister verwendet die bestehende
  Ersatzauswahl bei erhaltenem Entwurf. Kein neuer Fernverwaltungsendpunkt.
- Bestätigungen und Zwischenphase über Neustart/benanntes Speichern/Laden erhalten; neue Partie
  leert sie. Alte laufende Partien werden beim Update nicht unterbrochen. Unfertige alte
  Auswahl-/Bauphasen erhalten die Lesepflicht. Umbau einer laufenden Partie verlangt kein neues Lesen.
- Verlorene Antwort: identischer Retry; bestätigter eigener Lesestatus im Snapshot löst Pending
  ebenfalls auf, damit später Go nicht blockiert. Gleichzeitiges Lesen invalidiert keine Bauaktion
  oder andere Lesebestätigung. Echte Plan-/Rollen-/Phasenänderung behält die Revisionssperre;
  zuletzt relevante Interaktionsrevision wird mitgespeichert.

### Kurze Fakten am Portal

- Nur während `building`: zwölf kurze eigene deutsche Fakten mit humorvollen Ergänzungen.
  Live-Aufbau bleibt sichtbar, darunter kontrastreiche Fläche mit kräftiger zentrierter weißer
  Schrift (18–24 sp, bis vier Zeilen). Wechsel alle acht Sekunden; 0,5 Sekunden einblenden,
  nach 6,5 Sekunden 0,5 Sekunden ausblenden. Keine Neustarts durch 500-ms-Aktualisierung.
- Bei abgeschalteten Systemanimationen statischer Wechsel. Nach Planspeicherung enden Fakten,
  Lesestatus hat Vorrang. Hintergrund/Ende beendet ausstehende Überblendung. Keine zusätzlichen
  Pop-ups, Töne, Serienzitate, Spieleraktionen oder Spielzufälle; vollständig offline.
- Alle zwölf Texte stehen in `core/.../PortalTrivia.java`. Es wird kein Spielzufall verbraucht.
  Tatsächliche native Lesbarkeit und Sitzrichtungen bleiben am echten Portal-Handy zu prüfen.

### Startplätze mit Abstand zum Portal

Die für den nächsten Build vorgemerkte Anforderung ist jetzt umgesetzt. Bei neuer Aufstellung
automatische Spielerplätze nach größtem kürzestem begehbaren Abstand zum nächsten Portal
bevorzugen (BFS mit vorhandenen Teilfeldern/massiven Wänden). Gleichstand: stabile Rasterreihenfolge.
Keine feste Mindestdistanz vereinbart, kein zusätzliches Los und kein Missionsziel abgeleitet.

Erreichbare entfernte Felder zuerst; sind diese nicht verfügbar, andere vorhandene Nicht-Portal-
Felder vor Portalplätzen als letztem Ausweichweg. Getrennte Inseln werden nicht automatisch
verbunden; Raummeister muss sinnvollen Aufbau erfassen. Neue Bauänderungen berechnen automatische
Vorschläge neu. Gültige **manuelle** Plätze sowie gültige laufende/alte Positionen bleiben erhalten.
Keine Stapelung, Gegnerplätze bleiben belegt. Entferntes Feld bekommt wie bisher Ersatzvorschlag.

### Quellen und Grafiken

Offizielle Quellen, am **27.10.2026** geprüft und frei auf Deutsch zusammengefasst:

- https://www.doctorwho.tv/news-and-features/what-is-a-time-lord
- https://www.doctorwho.tv/news-and-features/13-chapters-in-the-history-of-gallifrey
- https://www.doctorwho.tv/news-and-features/history-of-the-timeless-child
- https://www.doctorwho.tv/news-and-features/what-is-a-sonic-screwdriver
- https://www.doctorwho.tv/characters/daleks
- https://www.doctorwho.tv/characters/davros
- https://www.doctorwho.tv/characters/weeping-angels
- https://www.doctorwho.tv/news-and-features/a-brief-history-of-the-weeping-angels
- https://www.doctorwho.tv/characters/the-tardis

Bestehende Porträts/eigene SVGs ergänzt um drei neue **KI-Szenen**, keine offiziellen Serienfotos,
keine heruntergeladenen Fremdbilder. Offline unter `app/src/main/assets/portraits/`:
`doctor-scene.jpg`, `dalek-scene.jpg`, `weeping-angel-scene.jpg`, je 1000×667.
Bildherkunft nur hier dokumentiert; keine Bildhinweise im Dossier (neuer Wunsch Abschnitt 8m).
Integrierte Bildgenerierung verwendet, keine CLI/API-Ausweichlösung.
Finale Prompts:

- `doctor-scene.jpg`: Use case: photorealistic-natural. Asset type: secondary landscape photograph-like illustration in a German Doctor Who tabletop character dossier. A close-up of a sonic screwdriver with softly glowing pale green emitter resting on a worn dark teal TARDIS console, brass circular controls and a glimpse of a blue police box door behind, rich cinematic photographic detail. Landscape 3:2 composition. No people, no letters, no text, no logo, no watermark. Elegantly complement parchment, petrol and brass UI. Original fan-art interpretation, not a reproduction of a TV still.
- `dalek-scene.jpg`: Use case: photorealistic-natural. Asset type: secondary landscape illustration in a Doctor Who tabletop character dossier. A recognizable bronze Dalek in profile in a ruined alien landscape on Skaro, close and complete, eye stalk softly glowing blue, armor hemispheres and ray gun visible, distant decaying futuristic towers and orange dusty sky. Cinematic photographic realism, aged brass, charcoal shadows and warm amber. Landscape 3:2. No letters, no captions, no logo, no watermark. Original fan art, not a reproduction of a TV still.
- `weeping-angel-scene.jpg`: Use case: photorealistic-natural. Asset type: secondary landscape illustration in a Doctor Who tabletop character dossier. A weathered grey stone Weeping Angel statue with hands covering its face and full wings, standing at the edge of a misty old cemetery at dusk. Side three-quarter view, entire statue visible, damp stone details, quiet eerie atmosphere without gore or threat toward camera. Petrol-blue shadows and faint warm distant lamplight, cinematic photographic realism, landscape 3:2. No letters, no text, no logo, no watermark. Original fan art, not a reproduction of a TV still.

**Technik:** App **0.11.0 / Code 18**, **Format 10 / Protokoll 6**, Formate 1–9 lesbar.
Neue Lesepflicht/-IDs, Aufstellungs-/manuelle Startmerker und Interaktionsrevision streng validieren.
`character-dossiers.js/.css` explizit in Asset-Allowlist, keine freie Dateiauslieferung.
Private Originalaudios und Signierschlüssel unverändert. Normales Update ab 0.5.0; Clientseiten
anschließend neu laden. Bestehende laufende Partien erhalten ihre Züge/Positionen und beginnen
nicht ungefragt von vorn. Neue Aufstellungs-/Dossierphase beim nächsten neuen Spiel testen.

**Weiter offen:** Abstimmungs-Auswertung, verbindliche Mission/Sieg-/Finalphasentrigger sowie
Charakterwerte, Gegenstandswirkungen und Kampfregeln. Vorhandene Wünsche werden hier erklärt,
keine neuen Werte/Wirkungen festgelegt. Spätere Erzähl-/Angebotsidee bleibt offen.


## 8l. Bestätigte Figuren und benannte Raumvorlagen ab 0.12.0

Neue Wünsche vom **27.10.2026**, umgesetzt. Der Nutzer bestätigte die erkannten Figuren
aus zwei Fotos ausdrücklich als auswählbar. Der zusätzlich zugeordnete **Zygon** wurde
in einer separaten Auswahl ebenfalls ausdrücklich bestätigt. Keine Originalfotos öffentlich hochgeladen.

### Acht Typen, elf einzelne Figuren

| Typ | Anzahl auf den bestätigten Fotos | Katalog-IDs |
| --- | --- | --- |
| Der Doctor | 1 | `doctor` |
| Dalek | 2 | `dalek`, `dalek-2` |
| Weinender Engel | 2 | `weeping-angel`, `weeping-angel-2` |
| Sontaraner | 1 | `sontaran` |
| Cyberman | 2 | `cyberman`, `cyberman-2` |
| Schneemann | 1 | `snowman` |
| Adipose | 1 | `adipose` |
| Zygon | 1 | `zygon` |

- Jede physische Miniatur hat eine eigene exklusive ID. Zwei Spieler können unterschiedliche
  Daleks, Engel oder Cybermen wählen. Eine einzelne ID bleibt exklusiv; keine Doppelvergabe.
  Erste drei IDs und bestehende Partien bleiben unverändert. Maximal **vier Menschen**.
- Alle beigetretenen Spieler bleiben Partner; unbesetzte einzelne Figuren werden nur durch
  Hinzufügen am Portal Gegner. Keine automatische Aufnahme aller elf Figuren.
- Sontaraner wird allgemein geführt. Die Ähnlichkeit zu Strax reicht nicht, um eine konkrete
  Serienperson zwingend festzuschreiben. Doctor-Inkarnation weiterhin nicht verbindlich bestimmt.
- Neue fünf Typen erhalten große Porträts, zusätzliche Szenen, Beschreibungen und vollständige
  scrollbare Dossiers mit früher Bedienung und Herkunft/Spezies aus Sicht der Spielwelt
  (aktuelle Textvorgabe Abschnitt 8m).
  Fez/Schleife des Adipose greifen die Miniatur auf und sind kein Bonus.
- Doppelminiaturen teilen Hintergrund/Porträt ihres `archetype`, behalten aber eigenen Namen,
  Besitzer, Position und Gegnerstatus. Auch native Avatare sowie Dalek-Stimme nutzen den Typ.
- **Keine Werte oder Spezialwirkungen erfunden.** Ausdauer/Kraft aller Figuren weiter offen.
  Bei neuen fünf Typen ist `actionAvailable=false`: Taste **Spezialaktion noch offen** gesperrt,
  Host weist einen unzulässigen Aktionsversuch ohne Zugverbrauch ab. Go, W6 und normale Bewegung
  funktionieren. Bei 5/6 vorerst Bewegung wählen; kein freies Passen. Vorhandene gerichtete
  Techniktest-Aktionen der drei bisherigen Typen gelten auch für deren zweite Miniatur.
- Serienmerkmale (Klonkrieger-Ausrüstung, Cyberhülle/Umwandlung, belebter Schnee, Adipose-
  Entstehung, Zygon-Gestaltwandlung) sind **keine** bestätigten Tabletop-Wirkungen. Kosten,
  Reichweiten, Ziele und konkrete Bedienung eigener Fähigkeiten werden gemeinsam festgelegt.

### Räume direkt beim Raummeister speichern und laden

- Editor hat **Raum speichern** und **Raum laden**, zusätzlich zum bisherigen **Speichern &
  starten**. Vorlagen speichern beendet den Aufbau nicht; Laden startet ebenfalls keine Partie.
- Speicherfenster mit editierbarem Namen. Host-Voreinstellung nach gewünschtem Muster
  Datum-Zeit_Charaktername, konkret **dd.MM.yyyy-HH.mm.ss_Charaktername**; Leerzeichen des
  Charakternamens werden Unterstriche. Beispiel `27.10.2026-18.35.20_Der_Doctor`.
  Host-Zeitzone maßgeblich; beim Öffnen vorbelegen, nicht beim Polling überschreiben.
- Name 1–80 Zeichen ohne Steuerzeichen. Dateiname ist eine interne UUID, niemals der Name.
  Ein bereits vergebener Name wird mit verständlichem Fehler abgewiesen; anderen Namen wählen.
  Kein stilles Überschreiben bestehender Räume.
- Ladeauswahl zeigt Namen, Speicherzeit, Kacheln und Bewegungsfelder. Auswahl verlangt danach
  **Raum laden** als Bestätigung, dass der aktuelle Entwurf ersetzt wird. Zurück möglich.
- Raumvorlage enthält Kacheln, Drehungen, 2×2-/Altraster, zusammenhängendes Portal und Kerker-
  zustände. Keine Spieleranmeldungen, Story, Würfel, Taschen, geheimen Schlüssel oder alten
  Figurenbesitzer übernehmen. Neue Startaufstellung wird für die aktuelle Besetzung vorgeschlagen;
  bewusst geladene Vorlage ersetzt deren bisherige manuelle Startvorschläge. Beim Umbau einer
  laufenden Partie gültige aktuelle Positionen erhalten, ungültige wie bisher reparieren.
- Gespeicherte Räume bleiben über App-Neustart und Spielreset sowie vollständiges Spiel-Laden
  erhalten. Aktiver Plan weiterhin Bestandteil vollständiger Spielstände/Exporte; die zusätzliche
  Raumsammlung wird getrennt im privaten App-Speicher gehalten. Raumexport/-import, Umbenennen
  und Löschen sind noch keine Oberfläche dieser Version.
- Nur authentisierter aktueller Raummeister in `building` darf listen/speichern/laden.
  `GET /api/rooms` liefert Liste und Namensvorgabe, Schreibvorgänge als `roomSave/roomLoad`
  über bestehende authentisierte Command-Route. Keine allgemeine Administrations-API.
- `RoomLibrary` testbar ohne Android, eigene streng geprüfte Binärdateien **Raumformat 1**.
  Native `RoomFiles` schreibt privat atomar unter `saved-rooms/<UUID>.drwroom`. Fehlerhafte
  Dateien bleiben unangetastet; gültige Räume weiter verfügbar, Zahl nicht lesbarer Dateien anzeigen.
- Speichern erfordert vorhandene Kachel. Host speichert seinen bestätigten Plan, keine freie
  Karte aus dem Browser. Speicherversuch erhält stabile aus Partie/Spieler/Anfrage abgeleitete
  UUID als dauerhafte Quittung. Verlorene Antwort/Neustart/Retry erzeugt keine zweite Vorlage
  und übernimmt nicht einen inzwischen geänderten Aufbau. Neue veraltete Speicheranfrage ablehnen.
- Ladefehler lassen den laufenden Entwurf unverändert. Erst persistenter Spielzustand wird
  bestätigt, identischer Retry fügt keinen zweiten Storyeintrag hinzu. Ladeauswahl/Name behalten
  Eingaben beim Polling; unbestätigte Anfragen frieren Name und Ziel ein und bieten Retry im Dialog.
  Hintergrund gesperrt, Tastatur/Fokus/Abbrechen, Anmelde- und Versionswechsel sauber aufräumen.
- Neue Raum-JS/CSS explizit in Server-Allowlist; kleine Editorhöhe 320×568 mit kompakter Palette
  und eigener Rasterbewegung, kein erforderlicher Dokument-Seitenlauf im geprüften Browser.

### Neue Quellen und Bilder

Offizielle Serienquellen am **27.10.2026** geprüft, frei auf Deutsch zusammengefasst:

- https://www.doctorwho.tv/characters/sontarans
- https://www.doctorwho.tv/characters/cybermen
- https://www.doctorwho.tv/stories/the-snowmen
- https://www.doctorwho.tv/characters/the-great-intelligence
- https://www.doctorwho.tv/characters/adipose
- https://www.doctorwho.tv/characters/zygons
- Visueller Zygon-Vergleich: https://www.doctorwho.tv/news-and-features/new-doctor-who-miniatures-games-from-warlord

Je ein neues Porträt und eine neue Szene für Sontaraner, Cyberman, Schneemann, Adipose und Zygon
unter `app/src/main/assets/portraits/`. Insgesamt zehn lokale JPEGs aus integrierter Bildgenerierung,
keine CLI/API-Ausweichlösung und keine offiziellen Serienfotos. Originale als PNG erzeugt, für
Offline-App proportional verkleinert und als JPEG gespeichert. Keine ursprünglichen Bilder ersetzen.
Recherche und Bildherkunft nur in der Entwicklungsdokumentation; der frühere aufklappbare
Quellen-/Bildhinweisbereich ist entfernt (Abschnitt 8m). Finale Prompts:

- `sontaran.jpg`: Use case: photorealistic-natural. Asset type: offline character dossier portrait in Doctor Who tabletop Android game. A recognizable Doctor Who Sontaran, short stocky adult alien clone warrior, bulbous wrinkled ochre potato-shaped bald head, stern expressive face, dark blue-purple segmented military armour, helmet removed, complete full-body centered portrait with generous space around head and feet, cinematic photographic realism, petrol-blue shadows and warm brass edge light, softly blurred alien military corridor. Vertical 3:4 composition. One character only. No text, no captions, no watermark, no interface, no logos. Original fan-art illustration, not a reproduction of a TV frame.
- `sontaran-scene.jpg`: Use case: photorealistic-natural. Asset type: secondary landscape image for a Doctor Who tabletop archive dossier. A recognizable Doctor Who Sontaran, short stocky adult alien clone warrior, bulbous wrinkled ochre potato-shaped bald head, stern expressive face, dark blue-purple segmented military armour, helmet removed, complete full-body centered portrait with generous space around head and feet, cinematic photographic realism, petrol-blue shadows and warm brass edge light, softly blurred alien military corridor. Recompose as a distinct wide environmental scene with the subject at one side and more of the setting visible, landscape 3:2, entire character visible. No text, no captions, no watermark, no interface, no logos. Original fan-art illustration, not a reproduction of a TV frame.
- `cyberman.jpg`: Use case: photorealistic-natural. Asset type: offline character dossier portrait in Doctor Who tabletop Android game. A recognizable Doctor Who Cyberman, silver armoured humanoid with iconic handles at sides of domed helmet, two black eye apertures and rectangular mouth slit, articulated silver limbs, circular cool blue chest unit, complete full body centered with generous space around all edges, cinematic photographic realism, weathered metal, softly blurred dark technological corridor, restrained petrol-blue rim light and warm brass reflections. Vertical 3:4 composition. One character only. No text, no captions, no watermark, no interface, no logos. Original fan-art illustration, not a reproduction of a TV frame.
- `cyberman-scene.jpg`: Use case: photorealistic-natural. Asset type: secondary landscape image for a Doctor Who tabletop archive dossier. A recognizable Doctor Who Cyberman, silver armoured humanoid with iconic handles at sides of domed helmet, two black eye apertures and rectangular mouth slit, articulated silver limbs, circular cool blue chest unit, complete full body centered with generous space around all edges, cinematic photographic realism, weathered metal, softly blurred dark technological corridor, restrained petrol-blue rim light and warm brass reflections. Recompose as a distinct wide environmental scene with the subject at one side and more of the setting visible, landscape 3:2, entire character visible. No text, no captions, no watermark, no interface, no logos. Original fan-art illustration, not a reproduction of a TV frame.
- `zygon.jpg`: Use case: photorealistic-natural. Asset type: offline character dossier portrait in Doctor Who tabletop Android game. A recognizable Doctor Who Zygon, reddish-brown organic humanoid shapeshifter, elongated domed ridged head continuous with broad shoulders, small dark eyes, rows of circular suction cups down head and chest, ribbed fleshy torso and clawed hands, complete full-body centered portrait with generous space around head and feet, cinematic photographic realism, softly blurred organic alien interior, petrol-blue shadow and warm amber side light, no blood or gore. Vertical 3:4 composition. One character only. No text, no captions, no watermark, no interface, no logos. Original fan-art illustration, not a reproduction of a TV frame.
- `zygon-scene.jpg`: Use case: photorealistic-natural. Asset type: secondary landscape image for a Doctor Who tabletop archive dossier. A recognizable Doctor Who Zygon, reddish-brown organic humanoid shapeshifter, elongated domed ridged head continuous with broad shoulders, small dark eyes, rows of circular suction cups down head and chest, ribbed fleshy torso and clawed hands, complete full-body centered portrait with generous space around head and feet, cinematic photographic realism, softly blurred organic alien interior, petrol-blue shadow and warm amber side light, no blood or gore. Recompose as a distinct wide environmental scene with the subject at one side and more of the setting visible, landscape 3:2, entire character visible. No text, no captions, no watermark, no interface, no logos. Original fan-art illustration, not a reproduction of a TV frame.
- `snowman.jpg`: Use case: photorealistic-natural. Asset type: offline character dossier portrait in Doctor Who tabletop Android game. A recognizable sinister sentient snowman from Doctor Who The Snowmen, white rounded snow head with two dark angled eye holes and wide jagged black smiling mouth, simple tapered packed-snow body, no hat, no scarf, no nose carrot, no stick arms, complete snow creature centered with generous space around edges, cinematic photographic realism, Victorian street at blue winter dusk softly blurred behind, snow texture and gentle warm lamplight, eerie playful not gory. Vertical 3:4 composition. One character only. No text, no captions, no watermark, no interface, no logos. Original fan-art illustration, not a reproduction of a TV frame.
- `snowman-scene.jpg`: Use case: photorealistic-natural. Asset type: secondary landscape image for a Doctor Who tabletop archive dossier. A recognizable sinister sentient snowman from Doctor Who The Snowmen, white rounded snow head with two dark angled eye holes and wide jagged black smiling mouth, simple tapered packed-snow body, no hat, no scarf, no nose carrot, no stick arms, complete snow creature centered with generous space around edges, cinematic photographic realism, Victorian street at blue winter dusk softly blurred behind, snow texture and gentle warm lamplight, eerie playful not gory. Recompose as a distinct wide environmental scene with the subject at one side and more of the setting visible, landscape 3:2, entire character visible. No text, no captions, no watermark, no interface, no logos. Original fan-art illustration, not a reproduction of a TV frame.
- `adipose.jpg`: Use case: photorealistic-natural. Asset type: offline character dossier portrait in Doctor Who tabletop Android game. A recognizable Doctor Who Adipose, small plump pear-shaped white alien with little arms and feet, tiny black eyes, cheerful little smiling mouth and one tooth, wearing a small red fez and orange bowtie matching a homemade tabletop miniature, complete creature centered with generous headroom and space at feet, cinematic photographic realism, softly blurred petrol-blue cosmic room with warm brass highlights, endearing smooth soft white skin, no other characters. Vertical 3:4 composition. One character only. No text, no captions, no watermark, no interface, no logos. Original fan-art illustration, not a reproduction of a TV frame.
- `adipose-scene.jpg`: Use case: photorealistic-natural. Asset type: secondary landscape image for a Doctor Who tabletop archive dossier. A recognizable Doctor Who Adipose, small plump pear-shaped white alien with little arms and feet, tiny black eyes, cheerful little smiling mouth and one tooth, wearing a small red fez and orange bowtie matching a homemade tabletop miniature, complete creature centered with generous headroom and space at feet, cinematic photographic realism, softly blurred petrol-blue cosmic room with warm brass highlights, endearing smooth soft white skin, no other characters. Recompose as a distinct wide environmental scene with the subject at one side and more of the setting visible, landscape 3:2, entire character visible. No text, no captions, no watermark, no interface, no logos. Original fan-art illustration, not a reproduction of a TV frame.

**Version:** App **0.12.0 / Code 19**, **API-Protokoll 7**. Spielstandformat **10 unverändert**,
ältere Formate 1–9 weiterhin lesbar. Raumdateien haben unabhängiges Format 1. APK mit bisherigem
Schlüssel bauen; als normales Update installieren und Clients neu laden. Keine Deinstallation.
Private Originalaudios bleiben bytegleich. Bekannte Android-/Hotspot-/Audio-/native Gerätetests
nicht durch Browserprüfungen als erledigt behaupten.


## 8m. Immersive Dossiers ab 0.13.0

**Bestätigter Nutzerwunsch vom 27.10.2026, im Quellstand umgesetzt:** Figuren so beschreiben,
als existierten sie wirklich. Keine Formulierungen wie „in der Serie“, keine Referenzen und
keine Rubriken für Serienhinweise oder Bildhinweise in Steckbriefen/Dossiers.

- Alle acht Hintergrunddossiers überarbeitet: Herkunft, Spezies, Geschichte und Eigenheiten
  sind Aktenwissen aus dem Universum. Ungeklärte Herkunft bleibt ein Geheimnis; keine Heimat
  erfinden. Strax wird als eigenständiger Sontaraner erwähnt, nicht als Identität der Spielerfigur.
- Bildunterschriften beschreiben Motive/Orte; keine KI-, Fanart- oder Produktionshinweise.
  Früheren Quellen-/Bildhinweisbereich samt Links, Daten und ungenutztem CSS entfernt.
- Auch kurze Auswahl-/Spiel-Steckbriefe aus dem gemeinsamen Charakterkatalog enthalten keine
  „Serienwissen“-Vorsätze oder Kommentare über Miniaturen. Aussagekräftige Bildalternativtexte.
- Früh zugängliche Bedienanleitung, aktuelle Aktionssperren, Go/W6, Richtung/Abbruch und ehrlicher
  Hinweis auf noch nicht ausgeführte Wirkungen bleiben erhalten. Die erzählte Biologie/Geschichte
  schaltet keine neue Fähigkeit frei. Werte, Reichweiten und Kosten nicht neu festgelegt.
- Recherchequellen und Bildgenerierungsnachweise bleiben nur in dieser Entwicklungsdokumentation
  (8k/8l), ohne Quellenrubrik in der Spieloberfläche. Historische Texte zu deren Anzeige sind durch
  diesen neuesten Nutzerwunsch ersetzt. Bilder, Scrollen, Großansicht und Gelesen-Gate unverändert.
- Bestandteil von 0.13.0. Version, Update und Tests stehen in Abschnitt 8n/11.

## 8n. Zielabstimmung, Portalwürfel und Eckkachelkorrektur ab 0.13.0

Verbindlich bestätigt am **27.10.2026**: **Portal wartet auf den Spielerwürfel.**
**Abstimmung: Mehrheit, bei Gleichstand Los.** Bei **Zufall** zieht der Host die eigene Stimme
zufällig aus den beiden angebotenen Zielen. Keine neue Kampf-, Kosten- oder Siegfeldregel erfunden.

### Neue Partie: Abstimmung zuerst

- Nach Start am Portal zunächst Phase `voting`, vor Raummeistersuche und Aufbau.
  Frage **Ziel des Spiels**, Optionen unverändert:
  1. Wer als erstes vor dem Portal steht gewinnt.
  2. Wer den Schlüssel gefunden hat und vor dem Portal ist gewinnt.
- Jedes Spielerhandy erhält verpflichtendes Pop-up mit Auswahl und **OK** oder **Zufall**.
  Auswahl vor dem Senden veränderbar, über Polling/Reload erhalten. Erst Bestätigung zählt.
  Nach verlorener Antwort dieselbe eingefrorene Stimme/Anfrage erneut senden. Bereits gespeicherte
  eigene Stimme im Snapshot löst eine verlorene Bestätigung ebenfalls auf. Kein zweites Stimmenlos.
- Portal zeigt die Frage und beide Ziele als große weiße Schrift vor der privaten Vollbild-
  `portal_blue.mp4`, dazu Anzahl und Namen/Charaktere fehlender Stimmen. Keine fremden Einzelstimmen
  öffentlich im Snapshot; jeder sieht die eigene gespeicherte Stimme und die Liste der Fertigen.
- Alle aktuellen Spieler müssen abstimmen, kein stiller Timeout/automatische Ersatzstimme für
  Offline-Spieler. Host kann wie bisher einen abwesenden Spieler entfernen; dessen Stimme entfällt.
  Wenn damit alle verbleibenden Spieler abgestimmt haben, auswerten. Nach Ergebnis keine Neuwertung
  durch spätere Spielerentfernung. Letzter Spieler weg: Lobby, kein Ziel/keine Stimmen.
- Mehrheit zählt abgegebene Stimmen; Gleichstand entscheidet genau ein Host-Los. Ergebnis mit
  Hinweis auf Mehrheit bzw. Gleichstand drei Sekunden (`voteResult`), dann bisherige drei Sekunden
  Raummeister-Flug und normale Auswahl. Bestätigte Stimmen, Ziel und Losergebnis atomisch speichern.
  Reine parallele Stimmabgaben dürfen sich nicht allein durch unterschiedliche Revisionen blockieren.
- Neustart/Spiel speichern/laden erhalten Phase und Stimmen. Bewusstes Laden erneuert wie bisher
  Sitzung/Tokens; gespeicherte Spielerplätze einmal mit Namen übernehmen. Neue Partie stimmt neu ab.
  Raumumbau und Fortsetzungen bereits laufender alter Partien erzwingen keine neue Abstimmung.
- Das Ziel ist gewählt und gespeichert; die **automatische Siegprüfung fehlt weiterhin**.
  „Vor dem Portal“ als konkrete Teilzelle, Finder/Träger bei späterem Tausch und Finalphasentrigger
  sind noch gemeinsam festzulegen. Keine anderen Spieler zu Gegnern machen.

### Portal deckt nach dem Spielerwürfel auf

- W6 weiterhin einmal am Host speichern; Client animiert denselben Wert 2,6 Sekunden.
  Erst nach diesem Ende und zwei Browser-Zeichenframes für die sichtbare Endfläche sendet ausschließlich der betroffene Spieler `ackRoll` mit stabiler Wurf-ID.
- Portal erhält bis dahin keinen anzuzeigenden Zahlenwert (`die=0`) und bleibt im drehenden
  Wartezustand. Keine Ergebniszeile/Story am Portal vor Freigabe. Danach kurzer 650-ms-Abschluss
  statt der bisherigen unabhängig gestarteten 2,6 Sekunden. Es wird nicht nochmals gewürfelt.
- Aufdeckungsstatus ist gespeichert. ACK ist kosmetisch, erhöht keine Spielrevision, verbraucht
  keine Bewegung und invalidiert keinen gleichzeitig gesendeten Zug. Falscher Spieler/falsches
  Ereignis abweisen. Kein alter Wurf darf einen neueren freigeben.
- Bei verlorener ACK-Antwort identischer separater Retry über Polling, ohne normale Züge zu blockieren.
  Eine gültige anschließende Bewegung/Bewegungswahl/Aktion bestätigt ebenfalls das Aufdecken,
  da diese Bedienelemente erst nach der Spieleranimation frei sind. So bleibt kein altes Portal-
  Warten stehen, wenn der Spieler seinen Zug bereits beendet hat. Kein willkürlicher Zeit-Timeout.
- Ausfall vor der Aufdeckung: Portal wartet, Spieler lädt sein gespeichertes Ereignis erneut.
  Alter Format-10-Wurf gilt als bereits aufgedeckt, damit ein Update keine nachträgliche ACK verlangt.
  Reduzierte Animationen bleiben statisch; Freigabereihenfolge wird dennoch eingehalten.

### Eckkacheln und Schlüssel

- **Fehler gefunden:** Grafik zeigte bei Drehung 0 Nord/West, Regellogik sperrte Nord/Ost.
  Bewegungsprüfung an die bereits gezeigte/physisch aufgebaute Kachel angepasst. Drehung 0
  also Nord/West, Drehung 1 Nord/Ost, Drehung 2 Ost/Süd, Drehung 3 Süd/West.
- Keine gespeicherte Kachel drehen/verschieben, kein Reset erforderlich. Auch geladene Vorlagen
  verwenden ihre sichtbare Wandlage. Beide Bewegungsrichtungen prüfen dieselbe Kante.
- A1/B1/A2/B2 innerhalb einer Eckkachel bleiben untereinander orthogonal begehbar; nur äußere
  rote Wände sperren den Wechsel zur Nachbarkachel. Figurenbelegung bleibt ein Hindernis.
- Der versteckte Portalschlüssel **blockiert kein Feld**. Betreten sammelt ihn atomar ein,
  einschließlich Retry/Popup/Tasche wie bisher. Keine Bewegungssperre wegen des Schlüssels ergänzt.

### Private Animation und Technik

- `PortalVideo` spielt die bereitgestellte MP4 lokal, stumm und in Schleife während `voting`/
  `voteResult`. Vollbild passend beschnitten, dunkler Kontrast über dem Video, große Texte.
  Weder Videoende noch Schleife führen Aktionen aus. Hintergrund/Phasenende gibt MediaPlayer frei.
  Abspielfehler lässt dunklen lesbaren Hintergrund stehen. Finale weiterhin nicht implementiert.
- Original unter `app/src/privateAssets/video/portal_blue.mp4`, Git-ignoriert und unverändert.
  SHA-256 `e160ac9eadc8b27007cf577def1d9643e755320fede2fe5a4d83cc213c6ecade`,
  43.388.010 Bytes. `verifyPortalVideo` prüft vor Stempel/Build; MP4 wie OGG unkomprimiert im APK.
  Wiederherstellung aus gleichnamiger persönlicher Datei; niemals privat bereitgestellte Medien
  oder Signierschlüssel öffentlich hochladen. Noch keine Videobearbeitung/Finalziel-Logik.
- App **0.13.0 / Code 20**, **API-Protokoll 8**, **Spielstandformat 11**, Formate **1–10** lesbar.
  Format 11 ergänzt Aufdeckungsstatus, Stimmen, Ziel und Gleichstandsmerker. Raumformat **1** bleibt.
  Übliches Update ab 0.5.0 ohne Deinstallation; Spielerwebseiten neu laden. Neue Abstimmung erscheint
  beim **nächsten neuen Spiel**, bestehende laufende Partie wird nicht zurückgesetzt.


## 8o. Portal-Spielbühne ab 0.14.0

**Nutzerwunsch vom 27.10.2026:** Die drei eingereichten Portal-Screenshots wirken trocken und
wie ein Verwaltungsformular. Mehr Spielgefühl, Grafik und Bewegung statt großer Textkästen und
unbenutzter Fläche. Bestätigter Portalstil A (Dunkelblau/Cyan) bleibt, ergänzt um warme Messinglichter.
Keine weitere Designauswahl verlangt; bestehende Spielregeln nicht verändern.

### Darstellung

- Vollflächige lokale Sternen-/Nebelszene mit langsam wandernden Lichtbahnen, umlaufenden
  Energieringen und abstrakten Ornamenten. CSS/Vektorgeometrie und vorhandene Porträts, keine
  neue Medienquelle, kein Download während der Partie und kein zusätzlicher Sound.
- Go: Spielername, Charakter und großes vollständiges Porträt im Portal, bis der Client Go
  bestätigt. Kurze Einladung statt langer Attribut-/Wartekarte. Nicht festgelegte Ausdauer/Kraft
  werden auf der gemeinsamen Spielbühne nicht als Platzhalter ausgebreitet; Dossiers bleiben.
- Nach Go zunächst Einladung zum Würfeln am eigenen Handy. **Kein alter W6 aus einem früheren
  Zug** in dieser Phase. Neue native Projektion kennt aktuelles Feld, aktuelle Zugphase und
  ob der Wurf zum laufenden Zug gehört. Zweite Figuren nutzen ihr kanonisches Bild.
- Während des Wurfs dreht sich ein großer räumlicher W6 im Energiering. Zahlen, Restschritte
  und Ergebnis-Story bleiben bis zur vorhandenen Client-ACK verborgen; auch die an den Renderer
  gegebene Projektion enthält dort keine Restschritte/Ergebniszahl. Danach 650-ms-Landung auf
  einer von sechs festen nummerierten Seiten; gespeicherter Wert, kein neuer Zufall.
- Aktuelle Bewegung als große Koordinaten X → Y; verbleibende gewürfelte Schritte als Text
  und leuchtende Teilstrecke. Letzter abgeschlossener Schritt bleibt auch beim anschließenden
  Go des nächsten Spielers sichtbar. Der vollständige gespeicherte Storyverlauf wird nicht gekürzt.
- Aktionsauswahl ohne implementierte Wirkung bleibt **Aktion vorgemerkt · Wirkung noch offen**,
  kein erfundener Treffer, Schaden oder Erfolg. Zusätzliche kurze Portaltexte sind feste kosmetische
  Inszenierung, verbrauchen keinen Spielzufall und behaupten keine neuen Ereignisse.
- Gegnerphase mit eigener warmer Alarmfarbe, richtigem Gegnerporträt und gespeichertem Auftrag.
  Team unten als kompakte Bild-/Namens-/Koordinatenleiste, aktiver Charakter hervorgehoben.
- Drei große graue Verwaltungsbuttons entfallen aus der Spielbühne. Native Taste **⋮** oben
  rechts öffnet **Portal-Menü**: Raumgefüge & Team, Portal-Lobby, Einstellungen, Spielleitung.
  Bestehende Save/Load-/Reset-/Gegnerfunktionen bleiben über diese Wege erhalten.
- Unverändertes Polling baut Porträt/Team nicht neu auf und startet Wurf/Einblendung nicht neu.
  Übergänge nur bei neuer gespeicherter Ereignis-/Wurfidentität. Reduzierte Systembewegung:
  statische Ringe, Fragezeichen während Warten und direktes echtes Ergebnis erst nach ACK.
  Bei Hintergrund/ausgeblendeter Spielansicht dekorative Bewegung pausieren.

### Technik und Grenzen

- `PortalScene` ist eine reine Projektion des vorhandenen Hostzustands, keine zweite Spiellogik.
  Neue Felder ausschließlich in `PortalState` für die lokale Darstellung, nicht in privaten
  Binärdateien oder dem Spieler-API. Spielstandformat **11**, Raumformat **1**, Protokoll **8** bleiben.
- `PortalPlayView` ist eine lokale, lesende WebView ohne JavaScript-Bridge, Content-Zugriff oder
  Netzwerklasten. Native Activity liefert JSON; ausschließlich feste gebündelte Stage-HTML/JS/CSS
  und streng begrenzte lokale JPEG-Porträtpfade freigeben. Navigation blockieren, Texte ausschließlich
  als Text setzen. Die drei Stage-Dateien werden **nicht über Spieler-HTTP veröffentlicht**.
- Native Activity liest nur die abgeschlossene Wurf-ID aus dem Renderer zurück, prüft die aktuelle
  Identität und merkt sie wie bisher in ihren privaten Anzeigepräferenzen. Keine Spieleraktion,
  Zufallszahl oder Speicher-/Administrationsfunktion ist aus der WebView aufrufbar.
- Das bestehende native `DiceView` bleibt im Quellbestand, wird in der neuen Spielbühne durch den
  testbaren lokalen Würfelrenderer ersetzt. Sechs feste Seiten und endgültige Orientierung prüfen.
- App **0.14.0 / Code 21**. Normal über APK ab 0.5.0 installieren, nicht deinstallieren; Clients
  wegen der Versionsprüfung neu laden. Keine neue Partie für diese Darstellung erforderlich.
  Aufbau-Livekarte, Portal-Fakten, Abstimmungsvideo und vorhandene Schlüssel-/Angriffshinweise
  bleiben eigenständige Phasen. Das geplante Finale und persönliche Angebote bleiben unimplementiert.
- Keine Behauptung eines echten Android-WebView-/Telefon-/Hotspot-/Sitzrichtungs-/Leistungstests.
  Native Menübedienung, Pause/Resume und Anzeige in der gedruckten Halterung bleiben Gerätetests.

## 8p. Portal-Zielfeld, Spielende und Kartenbewegung ab 0.15.0

**Neueste Nutzerwünsche vom 28.10.2026** haben Vorrang vor früheren Richtungsbuttons, acht
Portal-Teilfeldern und dem alten Portal-Statusbildschirm. Charakterfähigkeiten aus der vorherigen
Ideenliste sind weiterhin **unbestätigte Vorschläge**; keine dieser Fähigkeiten eingebaut.

### Portalgeometrie und Gegenstände

- Physische Fläche bleibt **zwei Kacheln**, Drehung in 90°-Schritten und gemeinsame Grafik auf
  Editor, Portal-Liveplan und Spielerkarte. Zwei kurze Seiten und eine lange Rückseite als dicke
  rote Wände. Grundausrichtung 0: Nord/Ost/West geschlossen, Süd als offene lange Seite;
  90°: West offen, 180°: Nord offen, 270°: Ost offen. Nachbarkachelwände blockieren ebenfalls.
- Die gesamte Portalfläche ist **ein einziger Bewegungsknoten mit einer Matrixkoordinate**,
  dem globalen oberen linken Ankerfeld (z. B. A1). Keine acht Labels oder inneren Schrittlinien.
  Alle vier Teilfelder vor der offenen langen Seite sind mögliche Zugänge zum selben Knoten.
  Betreten zählt einen Schritt; außerhalb bleiben normale 2×2-Kacheln unverändert.
- Interpretation des bisherigen „vor dem Portal“: **das eine Portal-Zielfeld betreten**.
  Das wurde dem Nutzer vor der Umsetzung so beschrieben; keine zusätzliche unsichtbare Zielzelle.
- Startaufstellung außerhalb des Portals. Verstecken und Zurücklegen von Gegenständen sind
  auf **der gesamten Portalfläche** ausgeschlossen, nicht nur auf dem beschrifteten Anker.
  Schlüsselgenerator und Recovery beim Entfernen des Trägers verwenden dieselbe Feldprüfung.
  Weitere Items besitzen noch keine Ablagefunktion; diese muss künftig dieselbe Prüfung nutzen.
- Alte Raumvorlagen (Raumformat 1) bleiben vollständig ladbar und erhalten automatisch die neue
  Darstellung/Geometrie. Alte Portalpositionen auf den Anker zusammenführen; einen dort früher
  versteckten Schlüssel beim validierten Laden deterministisch auf ein freies erreichbares
  Nicht-Portal-Feld verlegen. Kein neues Würfellos bei jedem Polling oder Neustart.
  Haben mehrere alte Figuren im Portal gestanden, bleibt eine am Anker und die übrigen erhalten
  freie Außenfelder. Reicht die Fläche nicht oder gibt es kein gültiges Schlüsselversteck, pausiert
  die erhaltene Partie im Aufbau beim ersten Spieler; große Portalmeldung erklärt die nötige
  Ergänzung. Unplatzierte Figuren neu setzen, dann normal speichern. Keine Behauptung, alle
  hätten die Raummeisterrolle abgelehnt, und kein stiller Spielreset.

### Ziel und echtes Spielende

- Ziel 1: Erster menschlicher Spieler, der das Portal betritt, gewinnt; kein Schlüssel nötig.
- Ziel 2: Spieler mit **Portalschlüssel in der eigenen Tasche** betritt das Portal und gewinnt.
  Ohne Schlüssel keine Wertung. Es gibt weiterhin keine Übergabe-/Tauschfunktion; Finder und
  Träger sind dadurch im normalen Ablauf dieselbe Person. Menschen bleiben Partner.
- Sobald die Siegbedingung auf dem bestätigten Weg erfüllt ist, endet die Partie sofort;
  verbleibende Würfel-Schritte entfallen nur dabei. Kein zweiter Sieger, kein weiterer Wurf,
  kein weiteres Go und keine neue Gegneraktion. Gewinnername/Figur/Feld und Ergebnis-ID speichern.
- Vollständiges Speichern/Laden und App-Neustart erhalten das Ergebnis und die gesamte Story.
  Kein automatischer Reset. Alte geladene Partie mit bereits erfüllter gewählter Portalbedingung
  erhält den fehlenden Abschluss; sonst an der vorhandenen Stelle fortsetzen.
- Bei neuem zielbasiertem Aufbau muss ein Portal vorhanden sein; kein Brettgenerator und keine
  unbekannten Wände/Teile erfinden. Zusammenhang des gesamten Bretts bleibt Aufgabe des Aufbaus.

### Antippen, korrigieren und bestätigen

- **Keine Bewegungs-Richtungstasten** mehr, auch nicht beim Gegnerhelfer. Die gerichteten
  Charakteraktionen behalten ihr eigenes Nord/Ost/Süd/West-Popup; das sind keine Bewegungstasten.
- Unter dem Würfel dieselbe tatsächliche Karte mit Zoomtasten, Zwei-Finger-Zoom und Ein-Finger-
  Verschieben. **Karte ↶ / ↷** dreht die lokale Ansicht weiter synchron; Weltkoordinaten bleiben.
  Eigener Kartenzoom und Sitzorientierung über Browser-Neuladen erhalten. Keine Bewegung durch Pan/Zoom.
- Nach Aufdeckung des W6 (und bei 5/6 nach Bewegung wählen) markiert der Host **exakt passende
  Endfelder**. Mauern, nicht vorhandene Felder und andere Figuren blockieren. Keine diagonalen
  Übergänge. Umwege und bereits technisch erlaubte Wiederholungen von Feldern bleiben möglich;
  daraus keine neue endgültige Anti-Schleifenregel ableiten. Ein bereits qualifiziertes Siegportal
  wird auch bei kürzerem Weg markiert. Keine geheimen Schlüsselkoordinaten in der Zielberechnung.
- Feld antippen speichert nur den gewählten Weg als **Vorschau**. Zielfeld am Client hervorheben,
  Wegpunkte zeichnen und am Portal Ausgang → Ziel mit Hinweis **vorgemerkt** anzeigen. Anderes
  erreichbares Feld antippen korrigiert frei. Figur, Tasche, Story und Reststrecke ändern dabei nicht.
- **Zug bestätigen** rechts neben dem nach links ausgewichenen Würfel führt den ganzen Weg
  atomar am Host aus. Der Client sendet keine frei erfundene Karte oder Schritte. Host berechnet
  und prüft Route erneut. Schlüssel auf durchlaufenen Feldern aufnehmen; nicht nur am Endfeld suchen.
  Erst erfolgreiches Speichern bestätigt Bewegung und Spielende. Danach echte Figur passend versetzen.
- Identischer Retry nach Antwortverlust bewegt/öffnet/wertet niemals doppelt. Eine verlorene
  Zugantwort, die inzwischen einen Fund auslöst, kann **im Funddialog** erneut abgeglichen werden;
  kein unerreichbarer Retryknopf hinter dem Popup. Vorschau über Neustart/Sichern/Laden erhalten.
- Gegnerhelfer markiert jeweils einen legalen nächsten Schritt auf derselben Karte und bestätigt
  ihn. Danach ggf. nächsten Schritt wählen, abschließend **Figur bewegt · bestätigen**. Kein neuer
  Gegner-W6 oder neue Reichweite/KI festgelegt; bisher moderierter Auftrag bleibt bestehen.

### Portalbild und wartende Spieler

- Während regulärem Spiel **vor dem ersten Schlüsselfund** nur das vollständige mittige Porträt
  der aktiven Figur auf dunklem Hintergrund. Keine alte Kartenwand, W6- oder Teamverwaltung.
  Das kleine native ⋮-Menü bleibt erreichbar. Vorschau erscheint als große weiße Zielmeldung.
- **Erster wirklicher Fund**, niemals Vorschau: vorhandene private `portal_blue.mp4` im Hintergrund
  in Schleife, einmal `tardis_sound.ogg` als Öffnungssound. Das Originalvideo hat keinen Audiostream.
  Keine neue Medienquelle und kein über Git veröffentlichtes Nutzeroriginal. Film bleibt nach Fund
  offen, auch wenn der Träger später entfernt wird. Sieg ohne Schlüssel öffnet das Portal ebenfalls.
- Gewählte Interpretation von „nur in dieser Phase“: **während der offenen Filmphase** erscheint
  die bisherige Portal-Statusinformation im Spieltab der gerade **unbeteiligten** Clients:
  Figur/Bild/Runde/Feld, Wurf nach Client-ACK, aktuelle Entscheidung/Story und Zielvorschau.
  Eigenen Zug, angegriffene Person und aktiven Gegnerhelfer ausnehmen. Automatisch einmal in
  diese Ansicht wechseln; Steckbrief/Story bleiben anschließend freiwillig anwählbar.
- Portalfilm wird durch alte Schlüssel-/Angriffskarten nicht überdeckt; diese Hinweise laufen
  in den vorgesehenen Clientansichten. Zielvorschau und dauerhaftes Siegerergebnis groß weiß über
  dem Film. Aufbau-Livekarte/Fakten, Abstimmung und Startphasen bleiben eigene bestehende Ansichten.
- Öffnungs-ID speichern; Polling/Retry/Resume/Laden spielt den Sound nicht neu. Hintergrund/
  ausgeblendete Ansicht gibt Videoplayer und Audiofokus frei, Rückkehr setzt nur den Film fort.
  Film/Audio sind Darstellung und dürfen niemals Spielaktionen oder Zufall erzeugen.

### Technik, Migration und Grenzen

- App **0.15.0 / Code 22**, **Protokoll 9**, **Spielstandformat 12**, Formate 1–11 lesbar;
  benannte Raumdateien behalten Format 1. Normal über APK ab 0.5.0 installieren, nicht deinstallieren.
  Clientseiten wegen neuer Befehle/Metadaten neu laden. Keine neue Partie für Darstellung nötig.
- Format 12 ergänzt Öffnungs-ID, vollständiges Ergebnis und gespeicherte Vorschau. Lesen prüft
  Route, Berechtigung, Reichweite und Zustand. Öffentliche Lobby bleibt reine Platzbelegung;
  Status für wartende Handys nur über deren authentisierten Snapshot, kein neuer Admin-Endpunkt.
- Keine neue Charakterwirkung, Ausdauer-/Kraftwerte, Gegenstandskosten, Angebote oder Kampflogik.
  Der bereits dokumentierte Hotspot-/mDNS-Ladefehler wird durch diesen Ausbau nicht als behoben erklärt.
- Automatische Tests und APK-Prüfung siehe Abschnitt 11; echte Android-Video-/Audio-/Touch-/
  Halterungs-/Mehrgeräteprüfung bleibt offen, solange sie nicht tatsächlich am Telefon erfolgt ist.

## 9. Technische Grundlage 0.15.0

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
  die Bewegung; derzeit wird nur die Wahl protokolliert, keine Wirkung berechnet. Wertung der beiden gewählten Portalziele seit 0.15.0; keine weiteren Missionswirkungen.
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

### Zielabstimmung – seit 0.13.0 umgesetzt

Zwei bestätigte Zieloptionen, Mehrheit und einmaliges Gleichstands-Los sowie Zufall als eigene
zufällige Stimme sind umgesetzt (Abschnitt 8n). Weitere Fragen folgen nach Vorgaben des Nutzers.
Seit 0.15.0: ein Portal-Zielfeld, Betreten als Erreichen, beim Schlüsselziel eigener Taschenbesitz;
automatische Siegprüfung und Öffnung bei Fund umgesetzt (8p). Weitere Zielabstimmungen bleiben offen. Die persönliche Siegbedingung
macht beigetretene Spieler nicht zu Gegnern; Partner-/Gegnerzuordnung bleibt unverändert.

**Spätere Atmosphäre und Erzählung – merken und zur passenden Entwicklungsphase ansprechen
(Nutzerwunsch vom 27.10.2026, noch nicht umgesetzt):**

- Das Spiel soll mitreißen. Das Portal soll als inszenierte „höhere Macht“ Zufälle,
  Entscheidungen und Folgen verkünden: grafisches Highlight, humorvolle unterhaltsame Texte,
  rund um den Tisch gut lesbar. Lesbarkeit aus mehreren Sitzrichtungen bei der Gestaltung
  berücksichtigen; konkrete Darstellung/Animationen erst später gemeinsam festlegen.
- Spieleraktionen für die anderen am Portal erst anzeigen, wenn sie „getroffen“ wurden.
  Vorläufig als erst feststehende/aufgelöste Aktionen und Entscheidungen verstehen, keine
  vorzeitigen persönlichen Auswahlmöglichkeiten zeigen. Der genaue Wortlaut ist noch zu
  klären: bestätigte Entscheidung oder erfolgreicher Treffer; keine Trefferregel daraus ableiten.
- **Idee, noch keine feste Spielregel:** Eine unbekannte Stimme/Nachricht aus dem All begleitet
  das Abenteuer mit Pop-ups und macht einzelnen Spielern persönliche „unmoralische Angebote“.
  Beispiel: hilfreichen Gegenstand erhalten und dafür Ausdauer oder Kraft abgeben.
  Absender, Geheimhaltung, Werte, Kosten, Häufigkeit, Folgen und Balancing sind noch offen.
  Keine Zahlen, automatischen Abzüge, Spielziele oder neue Gegenstände vorweg implementieren.
- **Erinnerung einmal erfolgt am 27.10.2026:** Mit der verbindlichen Festlegung der
  Abstimmungs-/Szenarioregeln wurde Mario an alle drei Punkte erinnert. Die zugehörige Aufgabe
  `6ae0259330a48191a9dc04941777cbf8` wurde danach pausiert. **Nicht erneut erinnern.**
  Idee bleibt offen, nicht implementieren. Ursprünglicher Auslöser war: sobald die Entwicklung zur konkreten Festlegung
  von Ausdauer, Kraft, Gegenstandswirkungen und Ereignis-/Szenarioregeln kommt, bevor Angebote
  oder öffentlich/private Ereignisansichten umgesetzt werden. Weitere Ausarbeitung nur im passenden Entwicklungsgespräch; die einmalige Erinnerung ist erledigt.
- Nur als spätere Planung speichern. Dieser Wunsch löst weder Implementierung noch neuen
  APK-Build aus; die damals verschobene Startaufstellung ist inzwischen in 0.11.0 umgesetzt (Abschnitt 8k).

### Finalphase: bereitgestellte Portalanimation – ab 0.15.0 konkretisiert

Historischer Wunsch vom 27.10.2026. **Neueste Konkretisierung und Umsetzung am 28.10.2026
siehe 8p**: erster echter Schlüsselfund öffnet den Portalfilm dauerhaft, einmal mit vorhandenem
TARDIS-Sound; davor mittiges aktives Porträt. Die folgenden Punkte dokumentieren die frühere Planung:

- Wenn kurz vor dem Ende alle übrigen Zielbedingungen erfüllt sind und nur noch der passende
  Spieler das Portal erreichen muss, soll **portal_blue.mp4** als durchgehender Hintergrund
  am Portal laufen. Wer der passende Spieler ist und welche Zielbedingungen dazugehören,
  werden erst mit der Mission festgelegt; nicht allein aufgrund der Portaldistanz auslösen.
- In dieser Ansicht normale Statuskarten durch kurze Ereignis-/Aktionsmeldungen als
  **große, fette weiße Schrift über dem Video** ersetzen. Vorbeilaufender oder ausblendender
  Text ist gewünscht. Vorschlag: Ein-/Ausblenden als gut lesbarer Standard, Laufschrift als
  Alternative; konkretes Timing später testen. Wegen des hellen Hintergrunds kräftiger dunkler
  Textschatten bzw. dezenter Kontrastverlauf, ohne das Video durch große Karten zu verdecken.
- Zufälle und Entscheidungen weiterhin ausschließlich vom gespeicherten Hostzustand übernehmen.
  Animation/Schleife lösen keine zweite Aktion aus; persönliche Angebote oder noch nicht
  feststehende Spielerentscheidungen nicht auf dem gemeinsamen Portal verraten.
- Hintergrund soll während dieser Endphase weiterlaufen, auch zwischen Texteinblendungen.
  Schleifenübergang, tatsächliche Telefonleistung, Ausrichtung und Lesbarkeit rund um den Tisch
  später am Gerät prüfen. Noch keine nahtlose Schleife oder fertige Siegbedingung behaupten.
- **Original unverändert als persönliche Datei vorhanden:** `portal_blue.mp4`. Für eine spätere
  Integration daraus wiederherstellen; wie die Audios nicht ins öffentliche GitHub-Repository
  hochladen. Private Build-Kopie seit 0.13.0 unter
  `app/src/privateAssets/video/portal_blue.mp4`, lokaler Player für Abstimmung vorhanden.
  Bei Bedarf effiziente Wiedergabekopie erstellen, Original behalten. Noch kein
  Finalphasentrigger ergänzt; Video-/Buildintegration der Abstimmung siehe 8n.

| Merkmal | Geprüftes Original |
| --- | --- |
| Datei | portal_blue.mp4 |
| Bild | H.264 Main, yuv420p, 720×1280, Hochformat 9:16, 30 Bilder/s |
| Dauer | 15,033 Sekunden |
| Ton | Kein Audiostream |
| Größe | 43.388.010 Bytes |
| SHA-256 | e160ac9eadc8b27007cf577def1d9643e755320fede2fe5a4d83cc213c6ecade |
| Prüfung | Metadaten gelesen, vollständige Videodecodierung ohne Fehler, mehrere Frames visuell betrachtet |

### Startaufstellung mit Abstand zum Portal – seit 0.11.0 umgesetzt

Die Vormerkung vom 27.10.2026 wurde mit dem nächsten ausdrücklich angeforderten Build umgesetzt.
Automatische neue Plätze bevorzugen Abstand; manuelle und laufende Positionen bleiben erhalten.
Auswahlstrategie, Grenzen und weiterhin fehlende feste Mindestdistanz stehen in Abschnitt 8k.

Raummeister und manueller Aufbau sind seit 0.8.0 der nächste Testschwerpunkt: echte Touch-
Bedienung, Portal-Lesbarkeit und Wiederaufbau am Tisch mit realem Inventar prüfen. Die noch
fehlenden Stückzahlen/Abmessungen dienen danach dem Generator; keine unbekannten Teile ergänzen.

Der vorgemerkte TARDIS-Icon-Wunsch ist mit 0.5.0 umgesetzt. Die konkreten Startbildschirm-
Anpassungen sind in Abschnitt 8a dokumentiert und bei weiteren Änderungen zu bewahren.

Die Rollenverteilung und elf Figuren aus zwei Fotos sind geklärt. Als Nächstes Ausdauer/Kraft
und konkrete neue Sonderfähigkeiten gemeinsam festlegen; danach den Zielvorschlag „Der Zeitriss“
konkretisieren und Gelände aufnehmen. Als kleinster sinnvoller Inventarschritt
reichen zunächst ein Foto von jeder Plättchenform, Stückzahl und Breite×Tiefe sowie die Angabe,
ob Wände/Türen fest oder aufsteckbar sind. Noch keine vollständige Datenerhebung erzwingen.

Parallel nach verfügbarer APK die Android-Version und das Host-Telefon notieren und einen
echten Vier-Spieler-Hotspottest durchführen. Das Host-Telefon ist bislang nicht benannt.

## 11. Tests und bekannte Grenzen

Für 0.15.0 am 28.10.2026:

- **839 Kern-/HTTP-Prüfungen bestanden**: vier Portalrotationen mit genau einer Koordinate,
  beidseitige Frontzugänge, geschlossene Seiten, Itemverbot auf ganzer Fläche, exakt erreichbare
  Ziele, korrigierbare Vorschau ohne Bewegung/Story/Fund, Berechtigung, Speicherfehler, atomare
  Bestätigung, identischer Retry, Schlüssel auch auf Zwischenfeldern, beide Ziele und dauerhaftes
  Spielende, Neustart/Laden, Gegnerhelfer und Migration alter Portalpositionen/Verstecke.
  Ein alter nur aus Portal bestehender voller Aufbau wird zur Korrektur pausiert und bleibt ladbar.
- **342 DOM-/Grafikprüfungen bestanden**; vorhandene Registrierung, Wartefenster, Tasche,
  Dossiers, Raumvorlagen, Abstimmung und Client-ACK-Würfel bleiben geprüft.
- **Sieben Browserabläufe bestanden** mit echtem Java-HTTP-Host und getrennten mobilen
  Browserkontexten: bestehender Spielablauf/Kartenorientierung, Schlüssel/Sicherung, Dossiers,
  Raumvorlagen, Abstimmung/Eckkachel, neue Portalansicht und neue Kartenbewegung bis zum Sieg.
  Der neue Zwei-Client-Test prüft Zielkorrektur ohne Verschieben, öffentliche Vorschau, echten
  Touch-Zoom und Ein-Finger-Pan ohne Fehl-Auswahl, Drehung/Neuladen, verlorene Bestätigungsantwort
  mit Retry im Funddialog, Status nur auf unbeteiligtem Handy und erhaltenen Sieg nach Host-Neustart.
  Portalansicht zusätzlich bei 390×844, 320×568, 691×1477 und 844×390 geprüft, einschließlich
  vollständigem Porträt, weißer Vorschau, transparenter Filmebene und Siegertext.
- Android **assembleDebug / lintDebug / core:smokeTest** erfolgreich; vor Ausgabe des finalen
  Pakets erneut mit aktualisierter Dokumentation und Prüfmarkierung ausführen. Ausgabeprüfung:
  Code 22 / Version 0.15.0, bekanntes Signierzertifikat, sämtliche gebündelten Quellassets und
  ONBOARDING.md bytegleich; beide OGGs und private MP4 bytegleich und unkomprimiert.
- Dossier-Bedienung auf Karte/Zielvorschau/Bestätigung und tatsächliche Schlüsselzielwertung
  aktualisiert. Keine neuen Spezialfähigkeiten oder Charakterwerte beschlossen.
- Echte Telefone, native Video-/Tonwiedergabe, Hotspot und Lesbarkeit in der Halterung bleiben offen.

Für 0.14.0 am 27.10.2026:

- **734 Kern-/HTTP-Prüfungen bestanden** (719 bisherige + 15 neue): reine Portalprojektion ohne
  Zustandsänderung, echte Go-/Wurf-/Bewegungs-/Gegnerphasen, kanonische Doppelbilder, Ergebnis
  einschließlich Reststrecke vor ACK verborgen, kein alter Würfel vor neuem Wurf, wirkliche
  Ausgangs-/Zielkoordinaten, letzter Schritt während nächstem Go und keine erfundenen Treffer.
- **342 DOM-/Grafikprüfungen bestanden** (317 bisherige + 25 neue Stageprüfungen): feste sechs
  Seiten, gespeicherte Landung/identische Polls, Ergebnis-Story erst nach Landung, sichere Namen,
  blockierte externe Bilder, Bewegungsgrafik, reduzierte Bewegung und Sitzungswechsel.
- Alle **sechs mobilen Browserabläufe bestanden**: die fünf bestehenden Abläufe und die neue
  lokale Stage bei **390×844, 320×568, 691×1477 und 844×390** (Hoch-/Querformat), geladene
  Porträts/Team, kein abgeschnittener Seitenfuß in Go und Bewegung, Screenshot-Sichtprüfung,
  Warten/ACK-Landung und Reduced Motion. Die per echter 3D-Matrix ermittelte vorderste
  Würfelseite entspricht für alle Augen 1–6 dem gespeicherten Wert.
- Android-Debug-APK **0.14.0 / Code 21** mit JDK 17 / Gradle 8.13 / SDK 36 gebaut;
  `:core:smokeTest`, `:app:assembleDebug` und `:app:lintDebug` bestanden, keine Lintfehler.
  **33 gebündelte Quellassets** und eingebettete aktuelle ONBOARDING.md bytegleich;
  zwei Nutzer-OGGs und Portalfilm unverändert und unkomprimiert. Signaturzertifikat weiter
  `2940936793e828d87a8e2178dc62223c24bd9ebc66ab114241c71f52d1231b40`, normales APK-Update.
- Reale Android-WebView-/Menü-/Lebenszyklus-/Performanceprüfung bleibt offen. Browserprüfungen
  sind kein Test des nativen Geräts; insbesondere keine bestätigte Hotspot-/mDNS-Reparatur.


Für 0.13.0 am 27.10.2026:

- **719 Kern-/HTTP-Prüfungen bestanden** (658 bisherige + 61 neue): echte Startabstimmung,
  parallele Revisionen, eigene/fremde Stimmen, Mehrheits-/Gleichstandsergebnis und Zufall am Host,
  identische Retrys/Neustart ohne neues Los, Speicherausfälle, Entfernen fehlender/letzter Spieler,
  Ergebnis vor Raummeister, Aufdeckung nur vom richtigen Wurfbesitzer, ACK bei Schreibfehler,
  identische Spielrevision trotz kosmetischer Bestätigung, ACK-Recovery durch Folgeschritt,
  laufenden Format-10-Spielstand ohne nachträgliche Abstimmung lesen, alle Eckkachel-Drehungen
  mit beiden Kantenrichtungen und frei begehbaren inneren Teilfeldern. Schlüssel-Fund weiter bestanden.
- **317 DOM-/Grafikprüfungen bestanden** (301 angepasste bisherige + 16 neue): elf immersive
  Dossiers ohne Referenzen/Serien-/Bildhinweise, Abstimmungspopup, Auswahl/Polling/Fokus/Escape,
  zufällige Stimme, verlorene Antwort mit identischer Anfrage, Ergebnis und Reihenfolge,
  kein ACK vor Ende der Spieleranimation, ACK-Retry ohne blockierten Spielzug.
- Alle vier bisherigen mobilen Browserabläufe weiterhin bestanden. Neuer echter Zwei-Client-
  Ablauf: verpflichtende Abstimmung vor Suche, Auswahl über Polling/Reload erhalten, erfolgreiche
  Stimmantwort absichtlich verloren, Host-Neustart mit Teilstimmen und mit Ergebnis, Gleichstands-
  Los und Zufall, Ergebnis vor Raummeister/normalem Aufbau. 320×568-Abstimmung mit beiden Optionen
  und OK/Zufall ohne internes Scrollen, Screenshot geprüft. Erster W6: Portalwert vor Ende der
  2,6-s-Animation verborgen; Freigabe erst nach Endbild-Zeichenframes. Danach sechs Augen unverändert.
  Tatsächliche Bewegung über offene Ostkante durch B1/A1/A2/B2 der Eckkachel bestanden.
- Android `updateOnboardingStamp :core:smokeTest :app:clean :app:assembleDebug :app:lintDebug`:
  **BUILD SUCCESSFUL**, 719 Kernprüfungen, Lint **0 Fehler / 19 Warnungen**. Nach letzter Prüfung
  finale Texte/Endbild-Bestätigung erneut stempeln und `:app:clean :app:assembleDebug` ausführen.
  App 0.13.0 / Code 20; finale Anleitung sowie alle 30 lokalen Client-/Grafik-/Audio-/Videoassets
  vor Ausgabe bytegleich mit APK prüfen, beide OGGs und MP4 unkomprimiert. Signatur muss dem
  bisherigen Zertifikat seit 0.5.0 entsprechen. Private Video-Prüfung hat einen unvollständigen
  Build-Input tatsächlich zurückgewiesen; Original vollständig wiederhergestellt und verifiziert.
- **Weiter offen:** reale Android-Telefone/Hotspot, native Videowiedergabe/Portal-Endanimation,
  Audiofokus und Lesbarkeit aus allen Sitzrichtungen. Browserprüfungen ersetzen diese nicht.
  Automatische Siegprüfung und konkrete Zielzelle vor dem Portal bleiben unimplementiert.


Vorabprüfung nur der immersiven Textüberarbeitung am 27.10.2026 (historischer Zwischenstand vor obigem vollständigem 0.13.0-Test):

- `npm test`: **301 DOM-/Grafikprüfungen bestanden**. Bisherige Quellenlink-Prüfung durch
  Prüfung aller elf Figuren ersetzt: keine Referenzlinks, Quellen-/Bildhinweisrubrik oder
  Serien-/Illustrationskommentare im gerenderten Dossier einschließlich Bildalternativtexten.
- `node --check app/src/main/assets/character-dossiers.js` und `git diff --check` bestanden.
- Ausschließlich Texte, Quellenrubrik und deren Styling geändert; keine Regellogik/Migration.
  Neue Kern-, Android-, native und Hardwaretests nicht durchgeführt. Folgende 0.12.0-Buildwerte
  gehören zur bereits ausgelieferten APK, nicht zu dieser nachfolgenden Quelländerung.


Für 0.12.0 am 27.10.2026:

- **658 Kern-/HTTP-Prüfungen bestanden** (568 bisherige + 90 neue): benannte Vorlagen,
  Berechtigungen, privater Inhalt, Datum/Charakter-Vorgabe, Zustand bei Schreibfehler, dauerhafte
  Retrys, veraltete Erstanfrage, Neustart/Reset, vollständige Kacheln/Drehungen/Kerker/Portal,
  beschädigte Datei ohne Datenverlust, elf IDs/acht Typen, vier echte Auswahlfiguren, Duplikate,
  Gegnertrennung, Save/Load und gesperrte undefinierte Aktionen bei unveränderter Bewegung.
- **301 DOM-/Grafikprüfungen bestanden** (271 bisherige + 30 neue): Editor-Tasten, Namensvorgabe,
  stabile Eingabe, Fokus/Escape, Bestätigungsdialog, sichere Namen, verlorene Antworten und
  identische Save/Load-Retry, Rückkehr bei entfernter Anmeldung, fünf neue bebilderte Dossiers,
  keine Wirkung/kein Zugverbrauch durch undefinierte Aktion.
- Bisherige drei mobile Browserabläufe bestanden. Neuer Zwei-Client-Test am echten Java-Host:
  elf Auswahlkarten und geladene Porträts, Dalek-2-Dossier, eigene Raumliste/Name, absichtlich
  verlorene erfolgreiche Speicherantwort ohne Doppeldaten, Host-Neustart, bestätigtes Laden,
  Vorlagen nach Spielreset wiederverwenden, 320×568-Editor ohne Dokumentlauf, fünf neue Dossiers
  visuell kontrolliert, normales Gelesen/Go/W6 und Bewegung mit gesperrter neuer Aktion.
- Android `:core:smokeTest :app:clean :app:assembleDebug :app:lintDebug`: **BUILD SUCCESSFUL**,
  erneut 658 Kernprüfungen; Lint **0 Fehler / 19 Warnungen**. Version 0.12.0 / Code 19.
  Signatur identisch seit 0.5.0; 29 lokale Grafik-/Client-/Audioassets und Anleitung bytegleich
  gegen APK geprüft, beide Original-OGGs unkomprimiert. Finale Anleitung vor Ausgabe nochmals
  einbetten und abgleichen. Native Raumdateien und reale Telefon-/Hotspot-/Audio-/Sitzrichtungs-
  prüfung bleiben Gerätetests.

Für 0.11.0 am 27.10.2026:

- **568 Kern-/HTTP-Prüfungen bestanden**, darunter Lesegate, Speicherausfälle, identische Retrys,
  parallele Bau-/Lesebestätigungen, letzter Leser, Drei-Sekunden-Start, Entfernen/Wiederbeitritt,
  manuelle Startplätze/Portalabstand und echte exportierte Format-9-Dateien aus 0.10.2.
- **271 DOM-/Grafikprüfungen bestanden** (243 bestehende + 28 neue Dossierprüfungen).
- Bisheriger mobiler Browser-Spiel-/Schlüsselablauf am echten Java-Host bestanden. Neuer
  Zwei-Client-Test: Dossier parallel zum Editor, Raummeister liest danach, Host-Neustart mit
  Teilbestätigung und normales Go nach letzter Bestätigung. Echtes Touchscrollen, stabile
  Leseposition bei Bauänderungen, 390×844, 320×740 und 320×568 sowie größere Schrift geprüft.
  Alle drei Dossiers visuell mit geladenen Szenenbildern kontrolliert; kurzer Titel und
  Umbruch verhindern horizontales Abschneiden. Read-Taste bleibt sichtbar.
- Android `:core:smokeTest :app:clean :app:assembleDebug :app:lintDebug`: **BUILD SUCCESSFUL**.
  Lint **0 Fehler / 19 Warnungen**. Signaturzertifikat identisch seit 0.5.0;
  Version **0.11.0 / Code 18** bestätigt. Alle 17
  lokalen Client-/Grafik-/Audioassets samt ONBOARDING bytegleich gegen APK geprüft; beide
  Original-OGGs unkomprimiert. Finale Dokumentationsfassung anschließend erneut einbetten.
  Native Faktenanimation, reale Telefon-/Hotspot-/Audio-/Sitzrichtungsprüfung weiterhin offen.

Für 0.10.2 am 27.10.2026:

- **243 DOM-/Grafikprüfungen bestanden** (213 bestehende + 30 neue). Neu: hostbestätigte Wahl,
  zehn Texte ohne Wiederholung, stabiles Polling, Abbruch/Reload, fehlgeschlagener Wechsel und
  identischer Retry, Start/Go-Vorrang, Bildtaste/Großansicht/Fokus/Escape, sichere Itemnamen,
  30 Inventarkästchen ohne erfundene Aktionen und stabile Ansicht beim Polling, Sitzungsreset,
  fremder Angriff ohne Unterbrechung des Bilds und eigener Helferauftrag mit Vorrang.
- **532 Kern-/HTTP-Prüfungen bestanden**; keine Regellogik oder Datenmigration geändert.
- Zwei mobile Chromium-Kontexte 390×844 / 320×740 am echten Java-HTTP-Host: Wartedialog,
  Zufallstextwechsel, Abbruch/Polling, tatsächlicher Figurenwechsel und erneute Abholung,
  Hoststart mit Raummeister-/Go-Vorrang, bisheriger Aufbau/Zoom/Bewegung/Save/Load bestanden.
- Zusätzlich 320×568: Abbruchknopf sichtbar. In 390×844 eigenes Porträt per echter Touch-Geste
  groß mit contain und Fokus-Rückgabe; 36 Testitems in exakt drei Spalten, neue Zeile ab viertem Item,
  Inventar scrollbar ohne Dokument-Seitenlauf. Screenshot-Sichtprüfung durchgeführt. Schlüssel-
  Fund/Tasche/Save/Load weiterhin bestanden. Testitems werden nicht in den Produktkatalog aufgenommen.
- Android `:app:clean :app:assembleDebug :app:lintDebug`: **BUILD SUCCESSFUL**, Lint
  **0 Fehler / 19 Warnungen**. Version **0.10.2 / Code 17**, gleiches Signaturzertifikat
  seit 0.5.0 bestätigt. Anleitung/Client/Grafikassets und beide unkomprimierten Originalaudios
  vor Übergabe bytegenau gegen die finale APK abgleichen.
- Reale Telefon-/Hotspot-/Audio-Prüfung weiterhin offen.

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
| 27.10.2026 | 0.10.2 | Bestätigte Figurenwahl mit abbrechbarem TARDIS-Wartefenster und zehn wechselnden eigenen Gags, eigenes Porträt als Großansicht, Lederbeutel-Icon und scrollendes Inventar mit drei Spalten |

| 27.10.2026 | 0.11.0 | Scrollbare illustrierte Charakterdossiers während Aufbau, frühe Bedienung/persönliche Fähigkeiten und offizielle Hintergrundquellen, persistentes Gelesen-Gate vor Startfilm/Go, kurze ausblendende Portal-Fakten, neue automatische Startplätze mit Portalabstand; Format 10 / Protokoll 6 |

| 27.10.2026 | 0.12.0 | Bestätigte acht Typen/elf Miniaturen mit eigenen Auswahl-IDs und neuen Offline-Porträts/Dossiers, undefinierte Aktionen gesperrt; dauerhafte benannte Raumvorlagen direkt beim Raummeister, editierbare Datum-Zeit_Charaktername-Vorgabe, bestätigtes Laden und persistente Retrys; Protokoll 7, Spielstandformat 10 unverändert, Raumformat 1 |

| 27.10.2026 | 0.14.0 | Portal-Spielbühne mit Energieringen, großer Figur, W6 nach Clientfreigabe, Bewegungsgrafik, Teamleiste und kleinem Portal-Menü; Darstellung ersetzt Formularansicht, Formate/Protokoll unverändert |

| 27.10.2026 | 0.13.0 | Immersive Dossiers ohne Serien-/Bildhinweise, Zielabstimmung mit Mehrheit/Gleichstands-Los und privater Vollbildanimation, Portalwürfel nach Clientaufdeckung, Eckwände an sichtbare Grafik angepasst; Format 11/Protokoll 8 |

| 28.10.2026 | 0.15.0 | Dreiseitiges Portal als ein Zielfeld, keine Gegenstände darauf; Kartenbewegung mit erreichbaren Zielen, persistenter korrigierbarer Vorschau und atomarer Bestätigung, beide Zielwertungen/Spielende; Öffnungsfilm/Sound ab Fund und Status auf unbeteiligten Clients; Format 12/Protokoll 9 |

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
