# Dr_Who

Android-Spielleiter für ein physisches Doctor-Who-Tabletop: Ein Handy steckt in einer
gedruckten Tür-/Portalhalterung, zeigt Animationen und stellt lokal eine Spieler-Webseite
für bis zu vier weitere Handys bereit.

**Zuerst [PROJEKT.md](PROJEKT.md) lesen.** Dort stehen sämtliche vereinbarten Anforderungen,
Regelideen, offenen Fragen, der tatsächliche Entwicklungsstand und die nächsten Schritte.
Sie dient ausdrücklich auch der Fortsetzung in einem neuen Chat.

## Stand 0.2.0

Android-Host mit lokalem HTTP-Server, Code-Lobby und exklusiver Charakterauswahl über Steckbriefe.
Alle Spieler sind Partner. Der Host kann übrige Figuren als Gegner hinzufügen. Nach den Partnern
folgt eine Gegnerphase mit zufällig ausgewähltem Bewegungshelfer und einer Angriffsanzeige beim
betroffenen Spieler. Nicht beteiligte Spieler sehen eine fliegende TARDIS. Die Portal-Lobby zeigt
Verbindungshilfe, Namen, Figuren und Auswahlfortschritt. Alles wird lokal gespeichert.

Ein freies 6×6-Demofeld dient weiterhin zum Testen von Zugfolge, Bewegung und W6-Zufall.
Gegnerbewegung und konkrete Angriffsfolge werden in dieser Technikdemo vom Host vorgegeben;
automatische Gegner-KI, Charakterregeln, Inventar, Brettgenerator und Kampagne sind noch geplant.
Es gibt bisher die drei vom Nutzer genannten Katalogeinträge Doctor, Dalek und Weinender Engel.
Für vier spielende Personen ist noch ein vierter Charakter zu benennen. Fotos fehlen noch;
Ausdauer und Kraft sind ausdrücklich offen. Die Karten enthalten dafür vorbereitete Felder.

Aktuellen Teststatus und bekannte Grenzen siehe [PROJEKT.md](PROJEKT.md).
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

Kern- und HTTP-Tests ohne Android SDK:

```sh
bash scripts/test-core.sh
```

Optionale Tests der Spieleransicht mit Node.js 22: `npm ci --ignore-scripts` und `npm test`.
Diese Entwicklungsabhängigkeiten werden weder in der App noch auf den Spielerhandys benötigt.

## Technikdemo verwenden

1. APK auf dem Spielleiter-Handy installieren und App öffnen.
2. „Lokalen Hotspot starten“ und die Android-Berechtigung erteilen.
   Alternativ alle Geräte mit demselben WLAN oder dem manuell gestarteten System-Hotspot verbinden.
3. Spieler verbinden sich mit dem angezeigten WLAN und öffnen die angezeigte IPv4-Adresse
   samt `http://` und Port `:8080` im Browser. Es wird keine Internetverbindung benötigt.
4. Namen und sechsstelligen Beitrittscode eingeben. Das WLAN-Passwort ist davon getrennt.
5. Jeder Spieler wählt eine freie Figur. Erst nach Auswahl aller beigetretenen Spieler kann
   der Host übrige Figuren als Gegner hinzufügen und die Technikdemo starten.
6. Die Spieler bewegen sich abwechselnd um ein Feld,
   würfeln einen W6-Techniktest oder passen. Jede dieser Eingaben beendet in der Demo den Zug.
7. Danach folgen vorhandene Gegner. Der Host kann einen Bewegungshelfer bestimmen lassen,
   eine Angriffsfolge beim Ziel anzeigen und anschließend den Gegnerzug abschließen.
   Offene Bewegungen bestätigt nur der Helfer, Angriffe nur das Ziel am eigenen Handy.
8. „Portal anzeigen“ öffnet die Lobbyübersicht bzw. Animation. Antippen bringt die Host-Steuerung zurück.

App im Vordergrund geöffnet lassen. Nach Änderungen wird automatisch gespeichert;
beim erneuten Öffnen wird die vorhandene Partie geladen. Keine fertige Spielversion:
Aktuell ein lokaler Spielstand, noch kein Auswahlmenü für mehrere Partien, kein Export/Import
und kein Zurücksetzen über die Oberfläche. Browser-Anmeldungen gehören zur Host-Adresse;
ein Adresswechsel oder gelöschte Browserdaten brauchen später eine Wiederbeitrittsfunktion.
Die Website wechselt bei Beteiligung zur aktiven Ansicht; gesperrte Handys werden damit nicht
fernentsperrt. Bildschirm-Timeout und Energiesparen am realen Gerät berücksichtigen.

Fotos später als JPEG in `app/src/main/assets/portraits/` ergänzen: `doctor.jpg`, `dalek.jpg`
und `weeping-angel.jpg`. Ohne Foto erscheint ein gekennzeichneter Platzhalter.

## Struktur

- `app/`: native Android-Oberfläche, Hotspot, atomisches Speichern, Portal, Browserdatei.
- `core/`: Android-unabhängiger Host-Zustand und lokaler HTTP-Server samt Tests.
- `PROJEKT.md`: zentrales Projektgedächtnis und Spielentwurf.
- `AGENTS.md`: Hinweise für zukünftige Weiterentwicklung.

Quellprojekt für ein privates Fan-Spiel; keine offiziellen Serienbilder, Videos oder Audios enthalten.
