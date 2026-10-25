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
11. Charakterfotos und endgültige Ausdauer-/Kraftwerte fehlen noch. Platzhalter klar
    kennzeichnen; keine zusätzlichen Serienfiguren oder Spielwerte ohne Abstimmung erfinden.
12. Jeder Spielerzug beginnt mit W6 am eigenen Gerät. 1–4: exakt Augenzahl ziehen;
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
23. Seit 0.6.0 Spieler-Tabs Spiel/Steckbrief/Was passiert ist. Verlauf auf-/zuklappbar;
    Tab-/Aufklappauswahl bei Polling erhalten. Oben deutlicher Zughinweis; eigener Zug/neuer
    Auftrag öffnet Spiel, Warten standardmäßig eigenen Steckbrief. Vollständiges Bild ohne Crop.
    Würfel muss auf gespeicherter Augenzahl landen: sechs feste Seiten, passende Orientierung,
    keine anderen Augen nach Animation einblenden. Keine Deckkraftanimation auf dem 3D-Würfel.
