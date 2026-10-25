# Dr_Who

Android-Spielleiter für ein physisches Doctor-Who-Tabletop: Ein Handy steckt in einer
gedruckten Tür-/Portalhalterung, zeigt Animationen und stellt lokal eine Spieler-Webseite
für bis zu vier weitere Handys bereit.

**Zuerst [PROJEKT.md](PROJEKT.md) lesen.** Dort stehen sämtliche vereinbarten Anforderungen,
Regelideen, offenen Fragen, der tatsächliche Entwicklungsstand und die nächsten Schritte.
Sie dient ausdrücklich auch der Fortsetzung in einem neuen Chat.

## Stand 0.1.0

Technischer Einstieg mit Android-Host, lokalem HTTP-Server, Code-Lobby, vier Spielerplätzen,
Browser-Steuerung, automatischem lokalem Spielstand und einer eigenen einfachen Portalanimation.
Ein freies 6×6-Demofeld dient zum Testen von Zugfolge, Bewegung und W6-Zufall.
Charakterregeln, Inventarverwaltung, Brettgenerator und eigentliche Kampagne sind noch geplant.

Validiert am 25.10.2026: Android-Debug-Build erfolgreich, 39 Kern-/HTTP-Prüfungen bestanden,
Android-Lint ohne Fehler (8 Hinweise). Ein realer Vier-Spieler-Hotspottest steht noch aus.

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

## Technikdemo verwenden

1. APK auf dem Spielleiter-Handy installieren und App öffnen.
2. „Lokalen Hotspot starten“ und die Android-Berechtigung erteilen.
   Alternativ alle Geräte mit demselben WLAN oder dem manuell gestarteten System-Hotspot verbinden.
3. Spieler verbinden sich mit dem angezeigten WLAN und öffnen die angezeigte IPv4-Adresse
   samt `http://` und Port `:8080` im Browser. Es wird keine Internetverbindung benötigt.
4. Namen und sechsstelligen Beitrittscode eingeben. Das WLAN-Passwort ist davon getrennt.
5. Host startet die Technikdemo. Die Spieler bewegen sich abwechselnd um ein Feld,
   würfeln einen W6-Techniktest oder passen. Jede dieser Eingaben beendet in der Demo den Zug.
6. „Portal anzeigen“ öffnet die Animation. Antippen bringt die Host-Steuerung zurück.

App im Vordergrund geöffnet lassen. Nach Änderungen wird automatisch gespeichert;
beim erneuten Öffnen wird die vorhandene Partie geladen. Keine fertige Spielversion:
Aktuell ein lokaler Spielstand, noch kein Auswahlmenü für mehrere Partien, kein Export/Import
und kein Zurücksetzen über die Oberfläche. Browser-Anmeldungen gehören zur Host-Adresse;
ein Adresswechsel oder gelöschte Browserdaten brauchen später eine Wiederbeitrittsfunktion.

## Struktur

- `app/`: native Android-Oberfläche, Hotspot, atomisches Speichern, Portal, Browserdatei.
- `core/`: Android-unabhängiger Host-Zustand und lokaler HTTP-Server samt Tests.
- `PROJEKT.md`: zentrales Projektgedächtnis und Spielentwurf.
- `AGENTS.md`: Hinweise für zukünftige Weiterentwicklung.

Quellprojekt für ein privates Fan-Spiel; keine offiziellen Serienbilder, Videos oder Audios enthalten.
