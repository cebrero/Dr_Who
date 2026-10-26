package de.cebrero.drwho.core;

import java.util.*;

/** Catalogue data, independent from allegiance or a player's current values. */
public final class CharacterProfile {
    public final String id, name, description, endurance, strength, portrait;
    public final List<String> abilities;
    public final boolean actionNeedsDirection;

    public CharacterProfile(String id, String name, String description, String endurance,
            String strength, String portrait, String... abilities) {
        this(id,name,description,endurance,strength,portrait,false,abilities);
    }
    public CharacterProfile(String id,String name,String description,String endurance,String strength,String portrait,boolean actionNeedsDirection,String... abilities){
        this.actionNeedsDirection=actionNeedsDirection;
        if (!id.matches("[a-z0-9-]{1,64}")) throw new IllegalArgumentException("Ungültige Charakter-ID.");
        this.id = id; this.name = name; this.description = description;
        this.endurance = endurance; this.strength = strength; this.portrait = portrait;
        this.abilities = Collections.unmodifiableList(Arrays.asList(abilities.clone()));
    }
    public static List<CharacterProfile> known() {
        return Arrays.asList(
            new CharacterProfile("doctor", "Der Doctor", "Löst Probleme mit Einfallsreichtum und seinem Schallschraubenzieher.",
                "Noch festzulegen", "Noch festzulegen", "/portraits/doctor.jpg",true, "Schallschraubenzieher: Türen öffnen. Reichweite und Probe noch offen."),
            new CharacterProfile("dalek", "Dalek", "Ein gepanzerter Charakter mit einem geradlinigen Fernangriff.",
                "Noch festzulegen", "Noch festzulegen", "/portraits/dalek.jpg",true, "Schießen: bis zu 3 Felder in geraden Richtungen. Trefferwirkung noch offen."),
            new CharacterProfile("weeping-angel", "Weinender Engel", "Kann ein Ziel im Sichtfeld zeitweilig aus dem Geschehen nehmen.",
                "Noch festzulegen", "Noch festzulegen", "/portraits/weeping-angel.jpg",true, "Gegner im Sichtfeld setzen 1–3 Runden aus. Sicht- und Statusregeln noch offen.")
        );
    }
}
