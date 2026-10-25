# Dr_Who

Android-Spielleiter für ein physisches Doctor-Who-Tabletop: Ein Handy steckt in einer
gedruckten Tür-/Portalhalterung, zeigt Animationen und stellt lokal eine Spieler-Webseite
für bis zu vier weitere Handys bereit.

**Zuerst [ONBOARDING.md](ONBOARDING.md) lesen.** Dort stehen sämtliche vereinbarten Anforderungen,
Regelideen, offenen Fragen, der tatsächliche Entwicklungsstand und die nächsten Schritte.
Sie dient ausdrücklich auch der Fortsetzung in einem neuen Chat.

## Stand 0.5.1

Bestätigte Gestaltung: **Spielerhandys im Design B „Zeitreise-Archiv“** mit Pergament, Petrol
und Messing; **Portal-Handy im Design A „TARDIS-Konsole“** mit Dunkelblau/Cyan. Die Farb-
und Typografiegrundlage ist umgesetzt. Die Portal-Lobby zeigt einen statischen Doctor-Who-Hintergrund, vier
Spielerplätze mit Charakterporträts, Spieleranzahl und kompakte Zugangsdaten. Echte Figurenfotos
folgen noch; Doctor, Dalek und Engel besitzen realistische KI-generierte Offline-Porträts.
Starttexte wurden vereinfacht („Dr WHO“ am Portal, „Dr. Who“ bei Spielern, „Portalcode“).
Spieler sehen schon vor dem Beitritt die aktuelle Belegung. Das App-Icon ist eine blaue TARDIS.
Die Steckbriefe lassen sich als Karten seitlich durchwischen; die Sichtkanten blenden weich aus.
Nach dem Spielstart zeigt das Portal Runde, aktiven Spieler, Wege von Feld zu Feld, Aktionen,
Teampositionen und letzte Ereignisse. Portal und Spieler zeigen einen 2,6 Sekunden animierten
Würfel mit Nebel und kurzem Lichtimpuls. Ergebnis und Ereignis-ID werden zuerst gespeichert;
die Animation verändert keinen Wurf und startet nicht bei jedem Polling erneut.

Android-Host mit lokalem HTTP-Server, Code-Lobby und exklusiver Charakterauswahl über Steckbriefe.
Alle Spieler sind Partner. Der Host kann übrige Figuren als Gegner hinzufügen. Nach den Partnern
folgt eine Gegnerphase mit zufällig ausgewähltem Bewegungshelfer und einer Angriffsanzeige beim
betroffenen Spieler. Nicht beteiligte Spieler sehen eine fliegende TARDIS. Die Portal-Lobby zeigt
Verbindungshilfe, Namen, Figuren und Auswahlfortschritt. Alles wird lokal gespeichert.

Ein freies 6×6-Demofeld dient weiterhin zum Testen von Zugfolge, Bewegung und W6-Zufall.
Jeder Spieler würfelt zuerst am eigenen Handy. Bei 1–4 zieht er genau diese Anzahl Felder;
bei 5/6 wählt er Bewegung um genau die Augenzahl oder eine Charakteraktion. Bewegungen
verlaufen orthogonal mit Richtungswechseln. Reststrecke und Würfelzustand werden gespeichert.
Die Aktionswahl ist noch ein gekennzeichneter Techniktest ohne berechnete Charakterwirkung.
Gegnerbewegung und konkrete Angriffsfolge werden in dieser Technikdemo vom Host vorgegeben;
automatische Gegner-KI, Charakterregeln, Inventar, Brettgenerator und Kampagne sind noch geplant.
Es gibt bisher die drei vom Nutzer genannten Katalogeinträge Doctor, Dalek und Weinender Engel.
Für vier spielende Personen ist noch ein vierter Charakter zu benennen. Fotos fehlen noch;
Ausdauer und Kraft sind ausdrücklich offen. Die Karten enthalten dafür vorbereitete Felder.

Validiert: 221 Kern-/HTTP-Prüfungen und 89 DOM-Prüfungen bestanden. Aktuellen Android-Build-
und Lint-Status sowie bekannte Grenzen siehe [ONBOARDING.md](ONBOARDING.md).
Ein realer Vier-Spieler-Hotspottest steht noch aus.

## Bauen

- JDK 17, Gradle 8.13, Android SDK Platform 36 und Build Tools 35.0.0.
- Android Gradle Plugin ist auf 8.13.2 festgelegt.
- Projekt in Android Studio öffnen. SDK bei Bedarf über den SDK Manager installieren.
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
statt einer blockierenden Fehlermeldung die Anmeldung zurück zum aktuellen Portalcode. Bei einer
anderen App-Version bietet er Neuladen an; WLAN-Aussetzer behalten die Anmeldung.

## Installationshinweis 0.5.1

**Über 0.5.0 als normales Update installieren – die App vorher nicht deinstallieren.**
Der Signierschlüssel ist identisch. Für den Wechsel von 0.4.0 gilt die folgende Ausnahme:

### Ältere Version 0.4.0

Der alte temporäre Signierschlüssel ist nicht mehr verfügbar. Diese APK hat deshalb eine
andere Signatur als 0.4.0 und lässt sich nicht direkt darüber installieren. Eine Deinstallation
löscht den bisherigen Spielstand: eine vorhandene Partie vorerst in der alten App behalten.
Details und Schlüssel-Fortsetzung stehen in [ONBOARDING.md](ONBOARDING.md).

## Technikdemo verwenden

1. APK auf dem Spielleiter-Handy installieren und App öffnen.
2. „Spiel-WLAN einrichten“ wählen. Für das einfache Passwort einmal den Android-System-Hotspot
   mit **Dr_Who / drwho123** einrichten und die tatsächlichen Daten in der App bestätigen.
   Alternativ „Automatisch starten“: Android vergibt dafür eigene Zugangsdaten.
3. Spieler scannen zuerst „WLAN-QR“, danach „Seiten-QR“. Der Seiten-QR enthält die direkte
   Adresse mit vorausgefülltem Portalcode. Alternativ die angezeigte `http://drwho.local`-
   Adresse öffnen; wenn Port 80 nicht verfügbar ist, erscheint zusätzlich `:8080`.
   Die direkte IP bleibt als Ausweichweg sichtbar. `.local` muss am echten Gerät geprüft werden.
4. Namen und sechsstelligen Portalcode eingeben. Das WLAN-Passwort ist davon getrennt.
5. Jeder Spieler wählt eine freie Figur. Erst nach Auswahl aller beigetretenen Spieler kann
   der Host übrige Figuren als Gegner hinzufügen und die Technikdemo starten.
6. Der aktive Spieler würfelt zuerst am eigenen Gerät. Bei 1–4 zieht er genau diese Anzahl
   Felder, bei 5/6 wählt er Bewegung oder Aktion. Jeden Bewegungsschritt am eigenen Handy
   eingeben und auf dem Tisch nachvollziehen. Nach der vollen Strecke folgt der nächste Spieler.
7. Danach folgen vorhandene Gegner. Der Host kann einen Bewegungshelfer bestimmen lassen,
   eine Angriffsfolge beim Ziel anzeigen und anschließend den Gegnerzug abschließen.
   Offene Bewegungen bestätigt nur der Helfer, Angriffe nur das Ziel am eigenen Handy.
8. Verwaltung ist hinter „Spielleitung“ verborgen. Dort öffnet „TARDIS groß anzeigen“ die
   große Animation; Antippen bringt die Portal-Lobby zurück.

App im Vordergrund geöffnet lassen. Nach Änderungen wird automatisch gespeichert;
beim erneuten Öffnen wird die vorhandene Partie geladen. Keine fertige Spielversion:
Aktuell ein lokaler Spielstand, noch kein Auswahlmenü für mehrere Partien, kein Export/Import
und kein Zurücksetzen über die Oberfläche. Browser-Anmeldungen gehören zur Host-Adresse;
ein Adresswechsel oder gelöschte Browserdaten brauchen später eine Wiederbeitrittsfunktion.
Die Website wechselt bei Beteiligung zur aktiven Ansicht; gesperrte Handys werden damit nicht
fernentsperrt. Bildschirm-Timeout und Energiesparen am realen Gerät berücksichtigen.

Echte Figurenfotos später als JPEG in `app/src/main/assets/portraits/` ergänzen: `doctor.jpg`, `dalek.jpg`
und `weeping-angel.jpg`. Die nativen Avatare bevorzugen diese Fotos. Die Browser-Steckbriefe und nativen Avatare
verwenden dieselben mitgelieferten, realistisch generierten JPEGs; Bildpfade stehen im Charakterkatalog.

## Struktur

- `app/`: native Android-Oberfläche, Hotspot, atomisches Speichern, Portal, Browserdatei.
- `core/`: Android-unabhängiger Host-Zustand und lokaler HTTP-Server samt Tests.
- `ONBOARDING.md`: zentrales Projektgedächtnis und Spielentwurf.
- `AGENTS.md`: Hinweise für zukünftige Weiterentwicklung.

Quellprojekt für ein privates Fan-Spiel; keine offiziellen Serienbilder, Videos oder Audios enthalten.
