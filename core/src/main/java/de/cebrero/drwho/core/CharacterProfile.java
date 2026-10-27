package de.cebrero.drwho.core;

import java.util.*;

/** Catalogue data, independent from allegiance or a player's current values. */
public final class CharacterProfile {
    public final String id, name, description, endurance, strength, portrait,archetype;
    public final List<String> abilities;
    public final boolean actionNeedsDirection,actionAvailable;

    public CharacterProfile(String id, String name, String description, String endurance,
            String strength, String portrait, String... abilities) {
        this(id,name,description,endurance,strength,portrait,false,abilities);
    }
    public CharacterProfile(String id,String name,String description,String endurance,String strength,String portrait,boolean actionNeedsDirection,String... abilities){
        this(id,name,description,endurance,strength,portrait,actionNeedsDirection,true,abilities);
    }
    public CharacterProfile(String id,String name,String description,String endurance,String strength,String portrait,boolean actionNeedsDirection,boolean actionAvailable,String... abilities){
        this.actionNeedsDirection=actionNeedsDirection;this.actionAvailable=actionAvailable;
        if (!id.matches("[a-z0-9-]{1,64}")) throw new IllegalArgumentException("Ungültige Charakter-ID.");
        this.id = id; this.name = name; this.description = description;
        this.archetype=archetypeOf(id);
        this.endurance = endurance; this.strength = strength; this.portrait = portrait;
        this.abilities = Collections.unmodifiableList(Arrays.asList(abilities.clone()));
    }
    public static List<CharacterProfile> known() {
        List<CharacterProfile> characters=new ArrayList<>(Arrays.asList(
            new CharacterProfile("doctor", "Der Doctor", "Löst Probleme mit Einfallsreichtum und seinem Schallschraubenzieher.",
                "Noch festzulegen", "Noch festzulegen", "/portraits/doctor.jpg",true, "Schallschraubenzieher: Türen öffnen. Reichweite und Probe noch offen."),
            new CharacterProfile("dalek", "Dalek", "Ein gepanzerter Charakter mit einem geradlinigen Fernangriff.",
                "Noch festzulegen", "Noch festzulegen", "/portraits/dalek.jpg",true, "Schießen: bis zu 3 Felder in geraden Richtungen. Trefferwirkung noch offen."),
            new CharacterProfile("weeping-angel", "Weinender Engel", "Kann ein Ziel im Sichtfeld zeitweilig aus dem Geschehen nehmen.",
                "Noch festzulegen", "Noch festzulegen", "/portraits/weeping-angel.jpg",true, "Gegner im Sichtfeld setzen 1–3 Runden aus. Sicht- und Statusregeln noch offen.")
        ));
        characters.add(new CharacterProfile("sontaran","Sontaraner","Ein entschlossener Klonkrieger von Sontar in schwerer Rüstung.","Noch festzulegen","Noch festzulegen","/portraits/sontaran.jpg",false,false,"Militärische Ausbildung und Ausrüstung. Spezialaktion noch nicht freigegeben; Reichweite und Wirkung stehen noch nicht fest."));
        characters.add(new CharacterProfile("cyberman","Cyberman","Ein kybernetisch umgebautes Wesen in einer metallenen Hülle.","Noch festzulegen","Noch festzulegen","/portraits/cyberman.jpg",false,false,"Cyberhülle und Umwandlung. Spezialaktion noch nicht freigegeben; Schutzwerte und Wirkungen stehen noch nicht fest."));
        characters.add(new CharacterProfile("snowman","Schneemann","Lebendiger Schnee unter dem Einfluss der Großen Intelligenz.","Noch festzulegen","Noch festzulegen","/portraits/snowman.jpg",false,false,"Vom Einfluss der Großen Intelligenz geformter Schnee. Spezialaktion und Wirkung noch nicht freigegeben."));
        characters.add(new CharacterProfile("adipose","Adipose","Ein kleines weißes außerirdisches Wesen mit rotem Fez und orangefarbener Schleife.","Noch festzulegen","Noch festzulegen","/portraits/adipose.jpg",false,false,"Adipose entstehen aus Körperfett. Spezialaktion noch nicht freigegeben; zusätzliche Fähigkeiten stehen noch nicht fest."));
        characters.add(new CharacterProfile("zygon","Zygon","Ein außerirdischer Gestaltwandler mit organischer Technologie.","Noch festzulegen","Noch festzulegen","/portraits/zygon.jpg",false,false,"Gestaltwandlung. Spezialaktion noch nicht freigegeben; Zielwahl, Kosten und Wirkung stehen noch nicht fest."));
        for(String[] copy:new String[][]{{"dalek","dalek-2","Dalek 2"},{"weeping-angel","weeping-angel-2","Weinender Engel 2"},{"cyberman","cyberman-2","Cyberman 2"}}){
            CharacterProfile source=characters.stream().filter(c->c.id.equals(copy[0])).findFirst().get();
            characters.add(new CharacterProfile(copy[1],copy[2],source.description,source.endurance,source.strength,source.portrait,source.actionNeedsDirection,source.actionAvailable,source.abilities.toArray(new String[0])));
        }
        return Collections.unmodifiableList(characters);
    }
    public static String archetypeOf(String id){switch(id){case "dalek-2":return "dalek";case "weeping-angel-2":return "weeping-angel";case "cyberman-2":return "cyberman";default:return id;}}
}
