package de.cebrero.drwho.core;

import java.util.*;
import java.util.regex.*;

/** Read-only projection for the local portal stage. Never writes state or generates randomness. */
public final class PortalScene {
    private PortalScene(){}
    public static String json(GameEngine.PortalState v,boolean seen,boolean reduced){
        boolean rolling=v.currentRoll&&!v.rollId.isEmpty()&&!v.rollPresented;
        String mode=v.enemyTurn?"enemy":v.awaitingGo?"go":rolling?"rolling":v.turnStage.equals("choice")?"choice":v.turnStage.equals("move")?"move":"ready";
        String title=v.awaitingGo?"Die Zeit ruft dich.":rolling?"Das Schicksal rollt …":v.enemyTurn?"Gefahr im Zeitgefüge":v.turnStage.equals("move")?"Deine Reise geht weiter.":v.turnStage.equals("choice")?"Dein nächster Schritt zählt.":"Ein Wurf. Unzählige Möglichkeiten.";
        String hint=v.awaitingGo?"Go auf deinem Handy · dann beginnt dein Zug":rolling?"Der Zeitwirbel wartet auf deinen Würfel.":v.enemyTurn?v.detail:v.turnStage.equals("move")?"Noch "+v.remaining+" von "+v.turnRoll+" Feldern · Figur auf dem Tisch bewegen.":v.turnStage.equals("choice")?(v.activeCharacter!=null&&v.activeCharacter.actionAvailable?"Bewegung oder Aktion · wähle auf deinem Handy":"Bewegung wählen · auf deinem Handy"):"Tippe den Würfel auf deinem Handy an.";
        String eventTitle=v.eventSpeaker.isEmpty()?"ZEITGEFÜGE":v.eventSpeaker.toUpperCase(Locale.GERMAN),eventText=v.eventText,eventType="story",eventId=v.eventId;
        Matcher movement=Pattern.compile("von Feld ([A-Z]+[0-9]+) zu Feld ([A-Z]+[0-9]+)").matcher(eventText);
        if(movement.find()){eventTitle=movement.group(1)+"  →  "+movement.group(2);eventType="move";eventText=v.eventSpeaker+" ist weitergezogen.";}
        else if(eventText.contains("Auswahl gespeichert")&&eventText.contains("Wirkung")){eventTitle="AKTION VORGEMERKT";eventText=v.eventSpeaker+" hat die Richtung gewählt. Die Wirkung ist noch offen.";eventType="action";}
        if(rolling){eventTitle="DAS SCHICKSAL HÄLT DEN ATEM AN";eventText="Bitte keine Abkürzung durch die Zukunft.";eventType="wait";eventId="wait:"+v.rollId;}
        else if(v.awaitingGo&&!eventType.equals("move")&&!eventType.equals("action")){eventTitle="DEIN MOMENT IM ZEITGEFÜGE";eventText="Das Universum wartet. Ausnahmsweise auf dich.";eventType="go";eventId="go:"+v.round+":"+v.activePlayerName;}
        String portrait=v.activeCharacter==null?"":v.activeCharacter.portrait;
        StringBuilder b=new StringBuilder("{\"session\":"+q(v.sessionId)+",\"round\":"+v.round+",\"mode\":"+q(mode)+",\"actor\":"+q(v.activePlayerName)+",\"character\":"+q(v.activeCharacter==null?"":v.activeCharacter.name)+",\"portrait\":"+q(portrait)+",\"field\":"+q(v.field)+",\"title\":"+q(title)+",\"hint\":"+q(hint)+",\"remaining\":"+(rolling?0:v.remaining)+",\"steps\":"+(rolling?0:v.turnRoll));
        b.append(",\"rollId\":").append(q(v.currentRoll?v.rollId:"")).append(",\"die\":").append(v.currentRoll&&v.rollPresented?v.die:0).append(",\"ready\":").append(v.rollPresented).append(",\"seen\":").append(seen).append(",\"reduced\":").append(reduced);
        b.append(",\"eventId\":").append(q(eventId)).append(",\"eventTitle\":").append(q(eventTitle)).append(",\"eventText\":").append(q(eventText)).append(",\"eventType\":").append(q(eventType)).append(",\"team\":[");
        for(int i=0;i<v.positions.size();i++){
            String[] p=v.boardPlayers.get(i);if(i>0)b.append(',');b.append("{\"name\":").append(q(p[0])).append(",\"portrait\":").append(q("/portraits/"+CharacterProfile.archetypeOf(p[1])+".jpg")).append(",\"field\":").append(q(BoardPlan.cell(Integer.parseInt(p[2]),Integer.parseInt(p[3])))).append(",\"active\":").append(v.activeCharacter!=null&&p[1].equals(v.activeCharacter.id)).append('}');
        }
        return b.append("]}").toString();
    }
    private static String q(String value){return GameEngine.quote(value==null?"":value);}
}
