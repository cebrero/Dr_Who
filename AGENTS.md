# Hinweise für die nächste Entwicklung

1. Zuerst die komplette `PROJEKT.md` lesen. Sie ist das zentrale Projektgedächtnis.
2. Anforderungen, bestätigte Regeln, technische Zwischenlösungen und Vorschläge unterscheiden.
   Ein Vorschlag ist erst nach Zustimmung eine feste Spielregel.
3. Vor und nach Änderungen tatsächlichen Code und Tests prüfen; PROJEKT.md mit Stand,
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
