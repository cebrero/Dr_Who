# Hinweise für die nächste Entwicklung

1. Zuerst die komplette `ONBOARDING.md` lesen. Sie ist das zentrale Projektgedächtnis.
2. Anforderungen, bestätigte Regeln, technische Zwischenlösungen und Vorschläge unterscheiden.
   Ein Vorschlag ist erst nach Zustimmung eine feste Spielregel.
3. Vor und nach Änderungen tatsächlichen Code und Tests prüfen; ONBOARDING.md mit Stand,
   Version, offenen Fragen, Tests und nächstem konkretem Schritt aktualisieren.
4. Deutsches UI. Android ist der lokale Spielleiter; Spieler brauchen nur einen Browser.
   Kein Cloud-Zwang. Maximal vier Spielerteilnahmen, unabhängig von WLAN-Client-Limits.
5. Der Host entscheidet über Spielzustand und Zufall. Keine Admin-Funktionen per Spieler-API.
   Verbindungs-Code und geheime Wiederbeitritts-Token nicht verwechseln.
6. Keine physischen Teile, Abmessungen, Figuren oder fertigen Regeln erfinden.
   Der Nutzer liefert Inventar und Animationen später.
7. Kernregeln und Speichern netzwerkunabhängig testbar halten. Keine Java-Objektdeserialisierung
   für importierte Spielstände. Android-Gerätetest und APK-Build ehrlich als offen markieren,
   solange sie nicht tatsächlich durchgeführt wurden.
8. Dieses Repository ist die dauerhafte Quelle; keine parallelen Projektstatusdateien pflegen.
9. Bestätigt: Alle beigetretenen Spieler sind Partner, unabhängig vom Seriencharakter.
   Nur zusätzlich am Portal hinzugefügte, unbesetzte Figuren werden Gegner.
10. Seit neuestem Wunsch 0.6.0: wartende Spieler sehen ihren Steckbrief; TARDIS optional
    im Tab Spiel. Bei Gegnerbewegung Helfer zufällig am Host
    bestimmen und speichern; Angriffsansicht gezielt beim betroffenen Spieler anzeigen.
    Nicht behaupten, eine lokale Browserseite könne gesperrte Telefone fernentsperren.
11. Endgültige Ausdauer-/Kraftwerte fehlen. Elf Miniaturen aus zwei Nutzerfotos sind seit
    0.12.0 bestätigt; Katalog/IDs in ONBOARDING.md 8l. Bilder sind KI-Illustrationen.
    Keine weiteren Figuren oder Werte ohne Abstimmung erfinden.
12. Seit 0.7.0 startet jeder Spielerzug nach Popup Your move... / Go, danach W6 am eigenen Gerät. 1–4: exakt Augenzahl ziehen;
    5/6: genau diese Bewegung ODER Charakteraktion. Orthogonal, Richtungswechsel erlaubt.
    Kein freies Passen. Blockierte Wege und Feldwiederholungen sind noch offene Regeln.
13. VOR JEDEM BUILD ONBOARDING.md inhaltlich auf aktuellen Ablauf/Regeln/Stand/Version
    prüfen und aktualisieren, dann updateOnboardingStamp ausführen. verifyOnboarding
    darf nicht umgangen oder automatisch vor jedem Build neu gestempelt werden.
    Ein Quellhash ersetzt keine inhaltliche Dokumentationspflege. Testergebnisse ehrlich halten.
14. Bestätigte Designwahl: B „Zeitreise-Archiv“ für die Spielerhandys (Pergament/Petrol/Messing),
    A „TARDIS-Konsole“ für das Portal-Handy (Dunkelblau/Cyan). Nicht wieder zur Auswahl stellen.
15. Fester Nutzergrundsatz: Einrichtung/Verbinden so einfach wie möglich. Mit passenden
    Grafiken und Animationen ins Abenteuer hineinziehen. Technik und Verwaltung im Hintergrund,
    wenige klare Schritte und gut lesbare Anzeigen. Eine Animation darf keine Eingabe verdecken.
16. Einfaches WLAN: Dr_Who / drwho123. Die App führt zur einmaligen System-Hotspot-
    Einrichtung. Auch SDK 36 hat keine öffentlichen SSID-/Passwort-Setter für gewöhnliche
    Apps. Keine versteckten APIs/Reflection und kein frei konfigurierbares Passwort versprechen.
    drwho.local wird echt per mDNS angekündigt; IP-/QR-Ausweichweg und tatsächlichen Port zeigen.
    Namensauflösung und Hotspotverhalten erst nach echtem Gerätetest als bestätigt bezeichnen.
17. Seit 0.5.0 umgesetzt und beibehalten: blaue TARDIS als App-/Launcher-Icon.
18. Aktuelle Startbildschirme: Portal „Dr WHO“, Spieler „Dr. Who“ und „Version“, Zugang
    „Portalcode“. Auf Portal-Startseite statischer Doctor-Who-Hintergrund, keine TARDIS-Animation,
    Spielanleitung oder Abenteuer-Untertitel. Spieler sehen vor Beitritt die aktuelle reine
    Platzbelegung. Repositoryname/SSID bleiben Dr_Who; TARDIS bleibt optional im Spieltab.
19. Charakterauswahl als wischbares Karussell mit weichen Randübergängen beibehalten.
    „lokal & ohne Konto“ ist aus der Versionszeile entfernt. Porträts sind realistische
    KI-generierte JPEGs, keine echten Figuren-/Serienfotos; später austauschbar.
20. Nach Spielstart automatisch Portal-Spielübersicht zeigen: Runde, aktiver Spieler/Gegner,
    Bewegung mit Ausgangs-/Zielfeld, Aktionen, W6 und Ereignisse. Würfelanimation an Portal
    und Clients etwa 2–3 Sekunden mit Nebel/kurzem Blitz. Gespeicherten Wert einmalig aufdecken,
    Folgehandlungen währenddessen sperren; niemals für Animation neu würfeln. Stabile Wurf-ID
    aus Speicherformat 5 nutzen, Polling darf Animation nicht neu starten. Barrierearme Alternative.
21. Seit 0.5.1 Version/Protokoll und Partie-ID getrennt prüfen. Neue Partie oder echte 401:
    zurück zur Anmeldung; keine alten Aktionen übertragen, kein automatischer Platzbezug.
    Andere Softwareversion: Neuladen anbieten, gültige Anmeldung erhalten. WLAN-Aussetzer
    ist kein Logout. Alten Poll-Antworten nach neuem Beitritt keine Wirkung geben.
    CLIENT_VERSION im Browser muss zur App-Version passen; Host nutzt BuildConfig.
22. Für jeden Build ab 0.5.0 den gesicherten privaten Signierschlüssel wiederverwenden und
    Zertifikat vor APK-Ausgabe vergleichen. Normales Update ohne Deinstallation ermöglichen.
23. Seit 0.6.0 Spieler-Tabs Spiel/Steckbrief/Story (bis 0.6.0 Was passiert ist). Verlauf auf-/zuklappbar;
    Tab-/Aufklappauswahl bei Polling erhalten. Oben deutlicher Zughinweis; eigener Zug/neuer
    Auftrag öffnet Spiel, Warten standardmäßig eigenen Steckbrief. Vollständiges Bild ohne Crop.
    Würfel muss auf gespeicherter Augenzahl landen: sechs feste Seiten, passende Orientierung,
    keine anderen Augen nach Animation einblenden. Keine Deckkraftanimation auf dem 3D-Würfel.
24. Seit 0.7.0 vor jedem menschlichen Zug Go am eigenen Client verlangen und hostseitig
    speichern. Portal zeigt bis Go die Charakterkarte mit Spielername ist dran…. Nach Neustart
    keine bereits bestätigte Freigabe verlieren; nächste Person muss neu bestätigen.
25. Story mit gespeicherter Host-Ortszeit, Sprecher/Spieler, Ich-Aussage und Richtung. Dalek-
    Geräusche einmalig aus separatem kosmetischem Zufall; niemals beim Anzeigen neu auswürfeln.
    Alte Protokolle ohne Zeit ausdrücklich Archiv nennen. Keine fiktiven Räume erfinden.
26. Gerichtete Aktion: erst Nord/Ost/Süd/West-Popup, vor Auswahl abbrechbar. Nach unbestätigtem
    Senden nur identischer Retry. Vorhandene Aktionswirkungen bleiben Techniktest. Seit 0.8.0 Format 7,
    alte Formate 1–6 lesen; API-Protokoll 3 erfordert Neuladen alter Spieleroberflächen.
27. drwho.local auf dem Portal-Handy ist beim Nutzer nicht auflösbar gewesen, IP funktioniert.
    Vollständige URL/Port und mDNS-/Netzroute prüfen. Ohne Gerätetest keine konkrete Ursache
    oder universelle Namensauflösung behaupten; IP-/QR-Ausweichweg erhalten.

28. Seit 0.8.0 Start zuerst Raummeisterwahl: gespeicherte 3-Sekunden-TARDIS, zufällige echte
    Spieler ohne Wiederholung fragen, Ja überträgt, alle Nein führt zur verbindlichen zufälligen
    Portalwahl (mit Informationspopup). Kein automatischer Plan ohne Inventar. Kein Zeitablauf
    für Kandidaten erfinden. Auswahl/Entwurf/Deadline über Neustarts und Retrys erhalten.
29. Nur Raummeister-Token bearbeitet 20×20-Karte (A1–T20): Ecke, Boden, parallele Wände, Tür,
    Kerker, Brunnen, Portal mit genau zwei Feldern. Touch/Maus-Drag, Tipp-Alternative, 90°-Drehung,
    Verschieben, Müll, Startfiguren. Jede Änderung zuerst am Host speichern und dann darstellen.
    Kein Feld ohne Kachel, kein Überlappen, kein Trennen des Portals, keine doppelten Startplätze.
30. Portal zeigt Aufbau live; erst nach finalem Speichern 3-Sekunden-Startfilm, dann Go/W6.
    Hinweise auf Zusage/Ablehnung/Portalwahl/Angriff groß je 3 Sekunden mit stabilen IDs zeigen.
    Altpartien bleiben auf altem Testfeld; hostbestätigtes Nachtragen des Aufbaus pausiert und
    erhält Runde/Zug. Keine ungefragte Rücksetzung oder fertige Mission behaupten.
31. Massive Kachelwände blockieren beidseitig Bewegung, leere Stellen existieren nicht. Türen
    vorläufig passierbar; Kerker/Brunnen/Portal sind noch ohne Sonderwirkung. Blockierte volle
    Würfelstrecke, Türzustände und Szenariostart sind offene Regeln. Kein neues Passen erfinden.

32. Seit 0.8.1 Codefehler inline am Codefeld, Format lokal und Richtigkeit am Host prüfen.
    Felder nach Fehler/Timeout freigeben; Name und ursprünglichen erfolgreichen Anmeldenachweis
    (`saved.registration`) trotz falscher Korrektur erhalten. Nie neuen Platz still belegen.
    Beim Rücksprung alle vier Modals, inert und Aufbau-/Editor-/Spielansichten vollständig lösen.
    Versionswechsel ebenfalls aus altem Editor befreien. Netzwerkfehler ist kein Codefehler.

33. Seit 0.9.0 neue Wünsche vor älteren Texten: Name ohne Raumcode im privaten Spiel-WLAN.
    Vollständige benannte Sicherungen/Datei-Export/-Import/Reset nur am Portal. Vor Reset/Laden
    sichern; Dateivalidierung vor Ersetzen. Nach Laden neue Sitzung und einmalige Namensübernahme.
34. Format 8 / Protokoll 4: neue Kacheln 2×2 Bewegungsfelder; alter Plan bleibt unverändert, bis
    Host bewusst bearbeitet. Zehn neue Typen, rote Außenwände, Kerkerzustand, alle Figurenpositionen.
    Keine Lava-/Heilregel erfinden. Kompass ist je Browser drehbar, Welt-Norden bleibt Portal.
35. Ganze Story ab 0.9.0 bis 8-MiB-Dateigrenze erhalten, nicht mehr 60 abschneiden. Alte verworfene
    Story nicht rekonstruieren behaupten. Captive Portal auf normalem Android-Hotspot ist nicht
    zuverlässig steuerbar; QR-Einstieg erhalten und keinen automatischen Browserstart versprechen.

36. Seit 0.9.1 neuester Wunsch: drei Resetoptionen, explizit OHNE Sicherung zulassen; nur
    Sichern und zurücksetzen archiviert. Unterer Lobbyreset beendet App-Hotspot samt altem
    Startversuch und leert Ansicht/WLAN-Daten; Einstellungsreset erhält WLAN. Alte Callbacks sperren.
37. Gemeinsame Portal-Lobby, Team unter Zugangsdaten, rotes Spieler-×, Tokenentzug und Client
    zurück zum Start. Zug/Aufbau/Helper/Angriff reparieren, keine Admin-HTTP-Route. Direkter
    automatischer WLAN-Start ohne App-Nachfrage; nur nötige Systemberechtigung. Keine Kerker-/WLAN-Menüs.
38. Auswahl als große Quartettkarte: ganzes Bild mit contain, Eigenschaften unten im Design B.
    Nord-Popup ohne Vier-Schritte-Zusatz. Clientkarte und Kompass gemeinsam drehen, Labels lesbar,
    je Browser gespeichert; Weltpositionen, Portal- und Aufbauausrichtung unverändert.

39. Seit 0.9.2 Kachelgrafiken und Feldlabels zentral in board-art.js/.css, am Portal lokaler
    read-only WebView-Renderer ohne Bridge/Netzwerk. Kompass immer mit gespeicherter Rotation.
    Neue gemeinsame Assets im Server nur explizit freigeben und im APK-Abgleich prüfen.
40. Jede Teilzelle im globalen Raster beschriften (oben A1/B1, darunter A2/B2, bis AN40).
    Neuer Nutzerwunsch ersetzt A1·1..4. x/y und alte Storytexte behalten, neue Anzeigen umbenennen;
    Namen bleiben bei Kachel-/Sitzdrehung am selben Weltfeld.
    Spielerkarte: Zoom 50–300 %, Tasten und Zwei-Finger-Geste, lokale Speicherung, eigene Figur
    auffindbar. Keine Weltposition durch Ansicht ändern. Editor-Schalter Kleine/Große Kacheln.

41. Ab 0.10.0 Nutzer-Audios nur privat unter app/src/privateAssets/audio/ (Git-ignoriert), aus
    persönlichen Dateien per exaktem Namen wiederherstellen. verifyAudioAssets + Quellhash nicht
    umgehen; OGG unkomprimiert im APK und bytegleich prüfen. Theme einmal bei Abenteuerstart,
    TARDIS bei neuer Spieler-ID nur Lobby, kein Replay bei Polling/Laden/Resume. Audiofokus freigeben.
42. Format 9 / Protokoll 5: geheimer Portalschlüssel beim ersten Planspeichern auf freiem erreichbarem
    Feld. Niemals vor Fund in Snapshot/Portal/Story verraten. Bewegung sammelt atomar auf, eigener
    bag + ackKey, persistentes Ereignis/Pending-Popup und Portalanimation. Kein Benutzen erfinden.
    Alte gestartete Partien behalten Legacy-Zustand ohne neuen Schlüssel; neue Partie erforderlich.
43. Portal wieder in neuer Palette (11 Typen, 5/5/1), zwei Kacheln, zusammenhängend drehen/verschieben.
    Inventar-/Schlüssel-Recovery bei Spielerentfernung und Umbau erhalten; echte Telefon-Audio- und
    native Animationstests offen halten, solange nur Build/Browser/Core geprüft wurden.

44. Seit 0.10.1 Feldlabels mittig/groß/fett ohne Fußstreifen, Figurenmarker klein in der Ecke.
    Neue Kacheln nur durch Drag platzieren; keine Leerfeld-Tipp-/Pfeiltasten-Positionierung.
    Bestehende Kacheln weiter antippen zum Drehen; Startfiguren im expliziten Modus per Tipp.
45. Story alle Einträge als scrollende Sprechblasen neueste zuerst. Eigener Steckbrief hohe
    Bildkarte, Infos in unterer Hälfte. Aktiver Client nach Go ohne redundante Zugbox; Würfel
    selbst ist Schaltfläche, kein separater Würfeln-Button. Würfel/Kompass nebeneinander,
    mitlaufender vergrößerter Kartenausschnitt darunter, dieselbe lokale Sitzorientierung.

46. Seit 0.10.2 nach bestätigter Lobby-Figurenwahl TARDIS-Wartefenster mit zehn eigenen Gags,
    alle zehn Sekunden zufällig ohne Wiederholung im Zyklus. Abbruch ist nur Anzeigeabbruch;
    Figur bleibt reserviert bis bestätigter Wechsel. Polling/Reload dürfen es nicht erneut öffnen,
    Hoststart beendet es. Texte kosmetisch, keine Filmzitate/Spielregeln daraus erfinden.
47. Eigenes Steckbriefbild per echter Bildtaste als modale contain-Großansicht; Fokus/Schließen/
    Escape, reale Spielhinweise vorrangig. Tasche: brauner Lederbeutel, genau drei Itemkästchen
    pro Reihe, dynamische scrollende Reihen. Unverändertes Polling erhält DOM/Scrollposition;
    keine Benutzen-/Tauschfunktion ohne Vereinbarung. Nur tatsächlich gesammelte eigene Items.

48. Seit 0.11.0 während building lesen andere Spieler ihr scrollbares illustriertes Dossier,
    Raummeister erst nach Planspeicherung. Persistentes Gelesen für alle vor launching/Go;
    keine automatische Bestätigung/Timeout. Alte laufende Spiele nicht in Briefing zwingen.
49. Format 10 / Protokoll 6: Lese-IDs und neue/manuelle Aufstellungsmerker speichern.
    Reine Lesebestätigungen dürfen parallel abgesendete Bauaktionen nicht invalidieren;
    echte Plan-/Rollen-/Phasenänderung weiterhin revisionieren. Dossier-Assets explizit erlauben.
50. Neue automatische Startfelder bevorzugen begehbaren Abstand zum Portal; gültige manuelle
    sowie alte/laufende Plätze erhalten. Keine Mindestdistanz/Mission erfinden. Portal-Fakten
    nur kosmetisch während Aufbau; Serienwissen ist keine neue Tabletop-Wirkung.

51. Seit 0.12.0 elf einzelne Figuren aus acht bestätigten Typen. Duplikate eigene IDs,
    Archetyp für Dossier/Porträt/Sprachstil. Neue fünf Typen haben keine vereinbarte Aktion:
    actionAvailable=false, kein Zugverbrauch bei Versuch. Keine Werte/Wirkungen erfinden.
52. Raummeister in building speichert/lädt benannte Vorlagen. Datum-Zeit_Charaktername
    am Host vorgeben, Eingabe bei Polling erhalten. Räume getrennt von Partien speichern,
    keine Tokens/Story/Items/Schlüssel importieren. Reset erhält Raumsammlung. Laden bestätigen.
53. Raumformat 1, Spielstandformat 10 unverändert, Protokoll 7. Dauerhafte Save-Quittung
    verhindert Duplikate nach Antwortverlust/Neustart. Erst neue passende Revision speichern;
    bestehende Quittung darf keinen späteren Plan übernehmen. Kaputte Originale erhalten.

54. Neuester Wunsch nach APK 0.12.0: Steckbriefe/Dossiers als Akten über wirklich existierende
    Wesen erzählen. Keine „in der Serie“-Kommentare, Referenzlinks oder Quellen-/Bildhinweisrubrik
    in der Spielansicht. Recherche/Bildherkunft nur in ONBOARDING.md. Bedienung/aktuelle Sperren
    ehrlich erklären; Erzählwissen erfindet keine neuen Werte oder ausführbaren Fähigkeiten.

55. Seit 0.13.0 Start zuerst voting: beide bekannten Ziele, Mehrheit, Gleichstand einmal am Host
    auslosen; Zufall ist zufällige eigene Stimme. Persistente Abstimmung/Ergebnis vor Raummeister.
    Keine Zeit-Ersatzstimme. Offline-Spieler nur explizit am Host entfernen. Keine Siegfeldregel erfinden.
56. Portalwürfel wartet auf gespeichertes ackRoll des aktiven Wurfbesitzers, erst nach 2,6-s-Client-
    Animation. Dann 650-ms-Abschluss; Ergebnisse/Story vorher nicht anzeigen. Kosmetische ACK erhöht
    keine Spielrevision; nächste gültige Folgeaktion bestätigt ebenfalls, falls ACK verloren ging.
57. Eckkachel-Grundlage Nord/West passend zur vorhandenen Grafik; frühere Logik Nord/Ost war falsch.
    Alle vier Drehungen und innere vier Teilfelder prüfen. Versteckter Schlüssel blockiert niemals.
58. Format 11 / Protokoll 8; Formate 1–10 lesbar, Raumformat 1 unverändert. Private MP4 wie Audios
    wiederherstellen und bytegleich/uncompressed im APK prüfen; lokal nur bei Abstimmung abspielen.
59. Erzählidee am 27.10.2026 einmal erinnert, Automation danach pausiert. Nicht nochmals erinnern;
    Kosten/Geheimhaltung/Balancing weiterhin gemeinsam entwerfen, keine Angebote ungefragt bauen.

60. Ab 0.14.0 lokale Portal-Spielbühne statt Formular-Karten. Stil A, große Figur bis Go,
    Energieringe, räumlicher W6 erst nach Client-ACK, aktueller Zug ohne alten Würfel, große
    Bewegung X→Y/Reststrecke, Teamleiste. Portal-Menü ⋮ enthält Verwaltung; Clientdesign bleibt B.
61. PortalScene ist reine Hostprojektion. PortalPlayView nur gebündelte Stage-Dateien und lokale
    Porträts, kein Netzwerk/JS-Bridge/Admin-API. Polling darf Darstellung nicht neu starten;
    gelandete Wurf-ID nativ merken, Hintergrund pausieren. Protokoll 8/Format 11 bleiben gleich.
62. Netzwerkbeobachtung 27.10.2026: beide im WLAN, einer lädt IP-Seite endlos, anderer kommt hinein;
    .local weiter unzuverlässig. /api/info-Gegentest noch offen. Nicht als mit UI-Ausbau behoben darstellen.

63. Ab 0.15.0 neuester Wunsch: Portal über zwei Kacheln als EIN Zielfeld/eine Ankerkoordinate,
    zwei kurze und eine lange Seite geschlossen, lange Front offen; Gegenstände auf ganzer Fläche
    verboten. Normale Kacheln behalten vier Felder. Ziele jetzt automatisch werten/Partie stoppen.
64. Bewegung nur auf Karte: Host markiert exakt erreichbare Endfelder, Vorschau korrigierbar und
    am Portal sichtbar, erst Zug bestätigen verschiebt/sammelt/wertet atomar. Keine Richtungsbuttons;
    Kartendrehung/Zoom/Pan erhalten. Gegnerhelfer ebenfalls Karte, weiterhin moderierter Einzel-Schritt.
65. Vor Fund mittiges aktives Porträt, ab echtem Fund private MP4 dauerhaft mit einmal TARDIS-Sound.
    Status während dieser offenen Phase nur auf unbeteiligten Clients; eigene Züge/Angriffe/Hilfe
    haben Vorrang. Audio/Film/Retry dürfen keinen zweiten Spielzug auslösen. Details 8p.
66. Format 12/Protokoll 9, Raumformat 1. Alte verbotene Portal-Verstecke/Teilpositionen migrieren,
    keine Nutzerdateien veröffentlichen. Fähigkeiten-Vorschläge aus dem Gespräch unbestätigt lassen.

67. Ab 0.15.1 neuer leerer Entwurf startet beim Raummeister mit gespeichertem Portal A1, im Editor
    oben mittig. Linkes Anlegen verschiebt nur neue Entwürfe samt Figuren atomar und definiert die
    Koordinaten neu; 20×20 behalten. Keine laufenden Story-/Itemkoordinaten umnummerieren. Details 8q.
68. Client hat vier Tabs ohne Karte; einziges Umfeld unter W6 behält Zoom/Pan/Fokus bei Vorschau,
    Statusabfragen und Größenänderung. Vorhandene Zielbuttons erhalten. Ab 0.15.2 ausdrücklich die
    originalen Energieringe mit mittigem Foto vor Schlüsselfund wieder anzeigen; danach Film. Details 8r.
