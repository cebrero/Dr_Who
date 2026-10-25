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
10. Nicht beteiligte Handys zeigen TARDIS. Bei Gegnerbewegung Helfer zufällig am Host
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
