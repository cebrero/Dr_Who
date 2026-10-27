package de.cebrero.drwho.core;

import java.util.*;

/** Presentation edges only; existing players and restored games are a silent baseline. */
public final class PortalAudioEvents {
    public enum Cue { NONE, JOIN, THEME }
    private String session,stage;
    private final Set<String> seen=new HashSet<>();
    public PortalAudioEvents(String session,String stage,Collection<String> players){reset(session,stage,players);}
    public void reset(String session,String stage,Collection<String> players){this.session=session;this.stage=stage;seen.clear();seen.addAll(players);}
    public Cue observe(String session,String stage,Collection<String> players){
        if(!this.session.equals(session)){reset(session,stage,players);return Cue.NONE;}
        boolean joined=false;for(String id:players)joined|=seen.add(id);
        boolean starting=this.stage.equals("lobby")&&(stage.equals("voting")||stage.equals("selecting"));this.stage=stage;
        return starting?Cue.THEME:joined&&stage.equals("lobby")?Cue.JOIN:Cue.NONE;
    }
}
