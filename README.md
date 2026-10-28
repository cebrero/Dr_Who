# Dr_Who

Android-Spielleiter für ein physisches Doctor-Who-Tabletop: Ein Handy steckt in einer
gedruckten Tür-/Portalhalterung, zeigt Animationen und stellt lokal eine Spieler-Webseite
für bis zu vier weitere Handys bereit.

**Zuerst [ONBOARDING.md](ONBOARDING.md) lesen.** Dort stehen sämtliche vereinbarten Anforderungen,
Regelideen, offenen Fragen, der tatsächliche Entwicklungsstand und die nächsten Schritte.
Sie dient ausdrücklich auch der Fortsetzung in einem neuen Chat.

## Stand 0.15.2

Neu in 0.15.2: Die ursprünglichen bewegten Energieringe aus 0.14.0 um das mittige
Charakterfoto sind vor dem Schlüsselfund wieder sichtbar. Danach bleibt das Öffnungsvideo.

Neu in 0.15.1: Neues Spielfeld beginnt mit dem Portal oben mittig. Links ausbauen mit
angepassten Koordinaten und erhaltenen Figurenplätzen. Die Karte unter dem Würfel bleibt bei
Zielauswahl und Statusabfragen ruhig; vier Client-Tabs ohne separaten Karten-Reiter. Details 8q.

Neu in 0.15.0: Portal als ein dreiseitig ummauertes Zielfeld ohne Gegenstände, bestätigte
Kartenbewegung mit erreichbaren Feldern und frei korrigierbarer Vorschau. Beide Portalziele
beenden die Partie automatisch. Öffnungsfilm und einmal TARDIS-Sound ab Schlüsselfund;
Status währenddessen auf unbeteiligten Spielerhandys. Details Abschnitt 8p.

Neu in 0.14.0: Portal-Spielansicht als animierte Zeitreise-Bühne mit Energieringen,
großer aktiver Figur, räumlichem Würfel nach Clientfreigabe, deutlicher Bewegung X→Y und
kompakter Teamleiste. Verwaltung über ⋮. Keine alten Würfel vor dem aktuellen Wurf und
keine Platzhalterwerte in der gemeinsamen Spielansicht. Details Abschnitt 8o.

Seit 0.13.0: Zielabstimmung vor der Raummeisterwahl mit Mehrheit und einmaligem Los bei
Gleichstand. Zufall ist eine zufällige eigene Stimme. Das Portal zeigt die bereitgestellte
Vollbildanimation und fehlende Stimmen. Der Portalwürfel deckt erst nach dem Spielerwürfel auf.
Eckkachel-Wandprüfung folgt der sichtbaren Nord-/West-Grafik. Alle Dossiers erzählen aus der
Spielwelt, ohne Quellen-/Bildhinweise. Details Abschnitt 8m/8n; API 8, Spielstandformat 11.
Die damals offene Siegprüfung und Zielzelle sind seit 0.15.0 umgesetzt (Abschnitt 8p).

Seit 0.12.0: Acht bestätigte Typen / elf einzelne Figuren mit neuen Bildern und Dossiers.
Duplikate sind unabhängig auswählbar; undefinierte neue Aktionen bleiben gesperrt.
Raummeister speichern und laden benannte Räume direkt im Editor, voreingestellt ist
Datum-Zeit_Charaktername. Raumvorlagen bleiben über Spielreset und Neustarts erhalten.
Details in ONBOARDING.md, Abschnitt 8l. API-Protokoll 7, Spielstandformat 10 unverändert.

Seit 0.11.0: Illustrierte scrollbare Charakterdossiers während des Aufbaus, danach auch für
Raummeister. Frühe Bedienanleitung, persönliche Fähigkeiten und Herkunft als immersive Akte aus der Spielwelt.
Recherchequellen und Bildherkunft stehen nur in ONBOARDING.md, nicht in den Dossiers.
Erst alle Gelesen-Bestätigungen geben Startfilm/Go frei. Kurze ein-/ausblendende Portal-Fakten
und neue automatische Startplätze möglichst mit Abstand zum Portal. Format 10 / Protokoll 6;
keine neuen Kampfwerte. Details in ONBOARDING.md, Abschnitt 8k.

Seit 0.10.2: TARDIS-Wartefenster nach bestätigter Figurenwahl, zehn wechselnde eigene
Wartegags und Abbruch zum Figurenwechsel. Steckbriefbild antippbar als Großansicht.
Lederbeutel-Symbol und scrollbar erweiterbares Inventar mit drei Itemkästchen pro Reihe.
Details und vollständige Texte in ONBOARDING.md, Abschnitt 8j.

Seit 0.10.1: mittige fette Feldlabels und neue Kacheln nur per Ziehen. Clients zeigen Story
als scrollende Sprechblasen, einen bildgroßen Steckbrief mit Infos unten und direkt antippbaren
W6. Würfel und Kompass stehen bei Bewegung nebeneinander; darunter folgt ein vergrößerter
Kartenausschnitt der eigenen Figur. Details in ONBOARDING.md, Abschnitt 8i.

Seit 0.10.0: Themensong beim Abenteuerstart, TARDIS-Sound bei neuem Beitritt am Portal,
geheimer zufälliger erreichbarer Portalschlüssel, Fund-Popup, goldene Portalanimation und
Tasche. Portal über zwei Kacheln in der elfteiligen Palette.

Seit 0.9.2: identische lokale Kachelgrafiken auf Portal und Clients, Kompassdrehung am Portal,
Koordinaten auf jedem Teilfeld (A1/B1/A2/B2, global bis AN40), Kartenzoom per +/− und Zwei-Finger-Geste sowie
Sprung zur eigenen Figur. Aufbau-Schalter heißt Kleine Kacheln / Große Kacheln.

Seit 0.9.1: gemeinsame Portal-Startseite mit Team unter den Zugangsdaten, rotes × zum
Abmelden einzelner Spieler, direkter automatischer Spiel-WLAN-Start, Reset wahlweise ohne Sicherung.
Unterer Portalreset beendet zusätzlich den App-Hotspot und leert die Ansicht. Große unbeschnittene
Quartettbilder, gekürzter Nord-Hinweis und gleichzeitig drehbare Karte/Bewegungskompass je Client.
Einstellungen ohne Kerker-/WLAN-Einträge.

Seit 0.9.0: Name eingeben und Figur wählen, ohne Raumcode. Portal-Einstellungen bieten
benannte vollständige Spielstände, Laden, Datei-Export/Import und Reset mit vorheriger Sicherung.
Neue Kacheln enthalten vier Bewegungsfelder; zehn Typen, dicke rote Wände und gespeicherter
Kerkerzustand. Pro Handy drehbarer Himmelsrichtungskompass; Portal steht immer im Norden.
Größere verschiebbare Karte und kompakte Ansichten mit Tabs. Alte Spielstände bleiben ladbar.

Bestätigte Gestaltung: **Spielerhandys im Design B „Zeitreise-Archiv“** mit Pergament, Petrol
und Messing; **Portal-Handy im Design A „TARDIS-Konsole“** mit Dunkelblau/Cyan. Die Farb-
und Typografiegrundlage ist umgesetzt. Die Portal-Lobby zeigt einen statischen Doctor-Who-Hintergrund, vier
Spielerplätze mit Charakterporträts, Spieleranzahl und kompakte Zugangsdaten. Echte Figurenfotos
folgen noch; Doctor, Dalek und Engel besitzen realistische KI-generierte Offline-Porträts.
Starttexte wurden vereinfacht („Dr WHO“ am Portal, „Dr. Who“ bei Spielern); der zusätzliche Portalcode entfällt.
Spieler sehen schon vor dem Beitritt die aktuelle Belegung. Das App-Icon ist eine blaue TARDIS.
Die Steckbriefe lassen sich als Karten seitlich durchwischen; die Sichtkanten blenden weich aus.
Im Spiel stehen oben **Spiel / Steckbrief / Karte / Story** zur Wahl; der Verlauf lässt sich
aufklappen und eintragsweise durchblättern. Ein fester Hinweis meldet den eigenen Zug. Das vollständige Steckbriefbild bleibt
unbeschnitten. Der Client-Würfel besitzt sechs feste Seiten und landet auf dem gespeicherten
Ergebnis; nach der Animation werden keine Augen ausgetauscht.
Nach dem Spielstart zeigt das Portal Runde, aktiven Spieler, Wege von Feld zu Feld, Aktionen,
Teampositionen und letzte Ereignisse. Portal und Spieler zeigen einen 2,6 Sekunden animierten
Würfel mit Nebel und kurzem Lichtimpuls. Ergebnis und Ereignis-ID werden zuerst gespeichert;
die Animation verändert keinen Wurf und startet nicht bei jedem Polling erneut.

Android-Host mit lokalem HTTP-Server, Namens-Lobby und exklusiver Charakterauswahl über Steckbriefe.
Alle Spieler sind Partner. Der Host kann übrige Figuren als Gegner hinzufügen. Nach den Partnern
folgt eine Gegnerphase mit zufällig ausgewähltem Bewegungshelfer und einer Angriffsanzeige beim
betroffenen Spieler. Nicht beteiligte Spieler sehen standardmäßig ihren eigenen Steckbrief;
die fliegende TARDIS bleibt optional im Spieltab. Die Portal-Lobby zeigt
Verbindungshilfe, Namen, Figuren und Auswahlfortschritt. Alles wird lokal gespeichert.

Nach Start fliegt die TARDIS drei Sekunden während der Raummeister-Auswahl. Echte Spieler
werden zufällig mit Ja/Nein gefragt. Nach allen Nein wählt das Portal verbindlich einen Spieler.
Der Raummeister erfasst den Tisch per **20×20-Kacheleditor** mit vier Bewegungsfeldern pro Kachel:
Stein, Ecke, einseitige/gegenüberliegende Wände, Kerker, Friedhof, Kompass, Lava, Lava/Stein, Brunnen und Portal über zwei Kacheln. Kacheln ziehen, antippen zum Drehen, verschieben und in den
Mülleimer ziehen. Neue Kacheln nur per Drag platzieren; Tippen auf vorhandene Kacheln dreht sie.
Startfiguren sind sichtbar und im eigenen Modus versetzbar.
Jede Änderung wird gespeichert und am Portal live gezeigt. Raumvorlagen lassen sich direkt
im Editor benennen, speichern und nach Bestätigung laden. Andere Spieler lesen während des
Aufbaus ihr Dossier, danach auch der Raummeister. Erst alle Gelesen-Bestätigungen und der
Startfilm öffnen die erste Spielrunde. Große Portalereignisse erscheinen je drei Sekunden. Leere Rasterstellen
existieren nicht; massive rote Außenwände blockieren Bewegungen. Kerker lassen sich öffnen/schließen;
Lava-, Brunnen- und andere Geländewirkungen sind noch nicht festgelegt.
Altpartien behalten ihr 6×6-Testfeld; unter Spielleitung lässt sich der reale Plan nachtragen,
ohne Runde und Zug zu verwerfen. Der Plan bleibt zum Wiederaufbau abrufbar.
Jeder Spieler bestätigt zuerst **Your move... / Go** am eigenen Handy und würfelt danach. Bei 1–4 zieht er genau diese Anzahl Felder;
bei 5/6 wählt er Bewegung um genau die Augenzahl oder eine freigegebene Charakteraktion.
Für die fünf neuen Typen bleiben noch nicht vereinbarte Spezialaktionen gesperrt. Bewegungen
verlaufen orthogonal mit Richtungswechseln. Reststrecke und Würfelzustand werden gespeichert.
Bei der gerichteten Aktionswahl erscheint ein Nord/Ost/Süd/West-Popup; Abbrechen verbraucht
nichts. Die eigentliche Wirkung ist weiterhin ein gekennzeichneter Techniktest. Bis Go zeigt das
Portal die Charakterkarte mit „Spielername ist dran…“. Die Story speichert Host-Datum/Uhrzeit,
Sprecher und Ich-Aussagen; gelegentliche Dalek-Geräusche bleiben beim Neuladen identisch.
Alte Aussagen ohne gespeicherten Zeitpunkt erscheinen als Archiv.
Gegnerbewegung und konkrete Angriffsfolge werden in dieser Technikdemo vom Host vorgegeben;
automatische Gegner-KI, verbindliche Charakter-/Gegenstandswirkungen, Brettgenerator und Kampagne sind noch geplant.
Es gibt elf bestätigte Miniaturen aus acht Typen: Doctor, Daleks, Weinende Engel, Sontaraner,
Cybermen, Schneemann, Adipose und Zygon. Mehrfachfiguren sind einzeln auswählbar.
Vier Personen können aus dem bestätigten Bestand wählen. Lokale KI-Porträts und Dossiers sind
vorhanden; Ausdauer und Kraft bleiben ausdrücklich offen.

Validiert: 734 Kern-/HTTP-Prüfungen und 342 DOM-/Grafikprüfungen bestanden. Aktuellen Android-Build-
und Lint-Status sowie bekannte Grenzen siehe [ONBOARDING.md](ONBOARDING.md).
Ein realer Vier-Spieler-Hotspottest steht noch aus.

## Bauen

- JDK 17, Gradle 8.13, Android SDK Platform 36 und Build Tools 35.0.0.
- Android Gradle Plugin ist auf 8.13.2 festgelegt.
- Projekt in Android Studio öffnen. SDK bei Bedarf über den SDK Manager installieren.
- Vor dem Build die beiden privaten Audiodateien nach `app/src/privateAssets/audio/` wiederherstellen;
  exakte Namen/Hashes siehe ONBOARDING Abschnitt 8g. Zusätzlich private Portalanimation unter
  `app/src/privateAssets/video/portal_blue.mp4` wiederherstellen (Abschnitt 8n).
  Diese Originalmedien sind nicht Teil des Quellrepositorys.
- Im Projektordner (Linux/macOS):

```sh
./gradlew :core:smokeTest :app:assembleDebug
```

Die Debug-APK liegt anschließend in `app/build/outputs/apk/debug/app-debug.apk`.
Unter Windows denselben Befehl mit `gradlew.bat` ausführen.
Der Standard-Gradle-Wrapper wird mit dem Projekt mitgeführt.
Erster Build benötigt Internet für Build-Werkzeuge; die Partie selbst arbeitet lokal.

Vor jedem APK-Build wird die zentrale `ONBOARDING.md` gegen App-Version und Produktionsquellen
geprüft. Nach Codeänderungen zuerst die Anleitung inhaltlich aktualisieren und dann
`./gradlew updateOnboardingStamp` ausführen. Auf unverändertem Checkout ist kein neuer Stempel
nötig. Die geprüfte Anleitung wird der APK als Asset beigelegt.

Kern- und HTTP-Tests ohne Android SDK:

```sh
bash scripts/test-core.sh
```

Optionale Tests der Spieleransicht mit Node.js 22: `npm ci --ignore-scripts` und `npm test`.
Diese Entwicklungsabhängigkeiten werden weder in der App noch auf den Spielerhandys benötigt.

Seit 0.5.1 prüft der Client App-Version, Protokoll und Partie-ID. Nach einer neuen Partie führt
statt einer blockierenden Fehlermeldung die Anmeldung zurück zur aktuellen Partie. Bei einer
anderen App-Version bietet er Neuladen an; WLAN-Aussetzer behalten die Anmeldung.

## Installationshinweis 0.15.0

**Über Versionen ab 0.5.0 als normales Update installieren – die App vorher nicht deinstallieren.**
Der Signierschlüssel ist identisch. Für den Wechsel von 0.4.0 gilt die folgende Ausnahme:

### Ältere Version 0.4.0

Der alte temporäre Signierschlüssel ist nicht mehr verfügbar. Diese APK hat deshalb eine
andere Signatur als 0.4.0 und lässt sich nicht direkt darüber installieren. Eine Deinstallation
löscht den bisherigen Spielstand: eine vorhandene Partie vorerst in der alten App behalten.
Details und Schlüssel-Fortsetzung stehen in [ONBOARDING.md](ONBOARDING.md).

## Technikdemo verwenden

1. APK auf dem Spielleiter-Handy installieren und App öffnen.
2. **Spiel-WLAN starten** wählen. Die App startet direkt den Android-Lokalhotspot. Nur eine noch
   fehlende Android-Berechtigung muss bestätigt werden. Die tatsächlichen, von Android vergebenen
   WLAN-Zugangsdaten erscheinen am Portal.
3. Spieler scannen zuerst „WLAN-QR“, danach „Seiten-QR“. Der Seiten-QR enthält die direkte
   Adresse ohne zusätzlichen Portalcode. Alternativ die angezeigte `http://drwho.local`-
   Adresse öffnen; wenn Port 80 nicht verfügbar ist, erscheint zusätzlich `:8080`.
   Die direkte IP bleibt als Ausweichweg sichtbar. `.local` muss am echten Gerät geprüft werden.
4. Nur den eigenen Namen eingeben; ein Portalcode ist nicht mehr nötig.
5. Jeder Spieler wählt eine freie Figur. Erst nach Auswahl aller beigetretenen Spieler kann
   der Host übrige Figuren als Gegner hinzufügen und die Raummeister-Auswahl starten.
6. Raummeister-Anfrage am ausgewählten Handy beantworten. Aufbau im Editor erfassen, Startfiguren
   wie angezeigt aufstellen und **Speichern & starten**. Das Portal zeigt den Plan und danach
   den Startfilm.
7. Der aktive Spieler bestätigt das Popup **Your move...** mit **Go**, danach würfelt er am eigenen Gerät. Bei 1–4 zieht er genau diese Anzahl
   Felder, bei 5/6 wählt er Bewegung oder Aktion. Jeden Bewegungsschritt am eigenen Handy
   eingeben und auf dem Tisch nachvollziehen. Nach der vollen Strecke folgt der nächste Spieler.
8. Danach folgen vorhandene Gegner. Der Host kann einen Bewegungshelfer bestimmen lassen,
   eine Angriffsfolge beim Ziel anzeigen und anschließend den Gegnerzug abschließen.
   Offene Bewegungen bestätigt nur der Helfer, Angriffe nur das Ziel am eigenen Handy.
9. „Einstellungen“ am Portal enthält benanntes Speichern/Laden, Datei-Export/-Import, Reset,
   Raumgefüge und weitere Spielleitung. Reset bietet Zurück / ohne Speicherung / Sichern und
   zurücksetzen. Beim Laden wird vorher automatisch gesichert; der Reset unten am Portal beendet
   zusätzlich den App-Hotspot und leert die Startansicht.

App im Vordergrund geöffnet lassen. Nach Änderungen wird automatisch gespeichert;
beim erneuten Öffnen wird die vorhandene Partie geladen. Keine fertige Spielversion:
Der aktuelle Stand wird automatisch gesichert, benannte Sicherungen lassen sich separat laden oder
exportieren. Beim Laden treten Spieler mit ihrem gespeicherten Namen erneut bei. Browser-Anmeldungen
gehören zur Host-Adresse; ein Adresswechsel während laufender Partie benötigt weiterhin Recovery.
Die Website wechselt bei Beteiligung zur aktiven Ansicht; gesperrte Handys werden damit nicht
fernentsperrt. Bildschirm-Timeout und Energiesparen am realen Gerät berücksichtigen.

Echte Figurenfotos später als JPEG in `app/src/main/assets/portraits/` ergänzen: `doctor.jpg`, `dalek.jpg`
und `weeping-angel.jpg`. Die nativen Avatare bevorzugen diese Fotos. Die Browser-Steckbriefe und nativen Avatare
verwenden dieselben mitgelieferten, realistisch generierten JPEGs; Bildpfade stehen im Charakterkatalog.

## Optionale Browserprüfung

Nach `bash scripts/test-core.sh` kann `npm run test:browser` zwei getrennte mobile Browser
mit dem echten Java-HTTP-Server prüfen. Dafür werden zusätzlich Playwright und Chromium benötigt;
sie gehören nicht zur ausgelieferten App. `JAVA_HOME`, optional `PLAYWRIGHT_MODULE_PATH` und
`CHROMIUM_EXECUTABLE_PATH` wählen vorhandene lokale Werkzeuge. Der Test legt interne Screenshots
im temporären Verzeichnis ab. Sein Host-Testzugang läuft ausschließlich über Prozess-stdin,
nicht über einen zusätzlichen HTTP-Administrationsendpunkt.

## Struktur

- `app/`: native Android-Oberfläche, Hotspot, atomisches Speichern, Portal, Browserdatei.
- `core/`: Android-unabhängiger Host-Zustand und lokaler HTTP-Server samt Tests.
- `ONBOARDING.md`: zentrales Projektgedächtnis und Spielentwurf.
- `AGENTS.md`: Hinweise für zukünftige Weiterentwicklung.

Quellprojekt für ein privates Fan-Spiel. Die vom Nutzer bereitgestellten Originalaudios bleiben
private Build-Beigaben und werden nur in die private APK eingebunden; Details in ONBOARDING.md.
