package de.cebrero.drwho.core;

import java.io.*;
import java.security.SecureRandom;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.nio.charset.StandardCharsets;

/** Authoritative, deliberately small technical demo; no final character rules yet. */
public final class GameEngine {
    public static final class InvalidPortalCodeException extends IllegalArgumentException {
        public InvalidPortalCodeException(){super("Portalcode falsch.");}
    }
    public interface Store { byte[] load() throws IOException; void save(byte[] bytes) throws IOException; }
    private static final int FORMAT = 8, MAGIC = 0x4457484f;
    private final Store store;
    private final SecureRandom random;
    private final Clock clock;
    private final Random flavourRandom;
    private State state;
    private final List<CharacterProfile> catalogue;

    private static final class Player {
        String id, token, name, joinRequest;
        String characterId = "";
        int x, y;
        boolean reclaimable;
    }
    private static final class StoryEntry {
        String id, dateTime, speaker, playerName, text, direction;
        long timestampMillis;
        String display(){return (dateTime.isEmpty()?"Zeit nicht erfasst":dateTime)+" - "+speaker+": "+text;}
    }
    private static final class State {
        String id, code;
        boolean started;
        int round = 1, current, lastRoll;
        long revision;
        List<Player> players = new ArrayList<>();
        List<String> enemies = new ArrayList<>();
        Map<String,int[]> enemyPositions=new LinkedHashMap<>();
        List<String> log = new ArrayList<>();
        List<StoryEntry> story = new ArrayList<>();
        boolean turnAccepted;
        String setupStage="lobby", setupId="", roomMaster="", candidate="";
        long setupDeadline;
        boolean forcedRoomMaster, boardSaved, legacyBoard;
        List<String> asked=new ArrayList<>();
        BoardPlan board=new BoardPlan();
        List<PortalNotice> notices=new ArrayList<>();
        LinkedHashSet<String> receipts = new LinkedHashSet<>();
        String phase = "players", helperId = "", attackId = "", attackEnemy = "", attackTarget = "", attackText = "";
        int enemyIndex;
        boolean movementDone, attackPending;
        String turnStage = "roll";
        int turnRoll, remaining;
        String rollEventId = "", rollPlayerId = "";
    }

    public GameEngine(Store store) throws IOException { this(store, new SecureRandom()); }
    GameEngine(Store store, SecureRandom random) throws IOException { this(store, random, CharacterProfile.known()); }
    GameEngine(Store store, SecureRandom random, List<CharacterProfile> catalogue) throws IOException {
        this(store,random,catalogue,Clock.systemDefaultZone(),new Random());
    }
    GameEngine(Store store, SecureRandom random, List<CharacterProfile> catalogue, Clock clock, Random flavourRandom) throws IOException {
        this.store = store;this.random = random;this.clock = clock;this.flavourRandom = flavourRandom;
        this.catalogue = Collections.unmodifiableList(new ArrayList<>(catalogue));
        Set<String> ids = new HashSet<>();
        for (CharacterProfile profile : this.catalogue) if (!ids.add(profile.id)) throw new IllegalArgumentException("Doppelte Charakter-ID.");
        byte[] saved = store.load();
        if (saved == null) {
            State initial = new State();
            initial.id = UUID.randomUUID().toString();
            initial.code = String.format(Locale.ROOT, "%06d", random.nextInt(1_000_000));
            addLog(initial,"Technikdemo bereit. Noch keine endgültigen Spielregeln.");
            store.save(encode(initial));
            state = initial;
        } else state = decode(saved); // Never silently replace an unreadable save.
    }

    public synchronized String fieldName(int x,int y){return state.legacyBoard?BoardPlan.cell(x,y):state.board.fieldName(x,y);}
    public synchronized String code() { return state.code; }
    public synchronized String sessionId() { return state.id; }
    /** Public pre-join occupancy only; no player names, codes, tokens or game state. */
    public synchronized String lobbyStatus() { return "{\"occupied\":" + state.players.size() + ",\"capacity\":4}"; }
    /** Consistent host-only presentation data; never exposed as a public HTTP route. */
    public static final class PortalNotice {
        public String id,text;
        PortalNotice(String id,String text){this.id=id;this.text=text;}
    }
    public static final class PortalState {
        public String sessionId, rollId, rollPlayer, headline, detail;
        public int round, die, subdivisions;
        public String setupStage, setupTitle;
        public boolean preparing, boardSaved, legacyBoard;
        public List<BoardPlan.Tile> tiles;
        public List<PortalNotice> notices;
        public List<String[]> boardPlayers;
        public boolean awaitingGo;
        public String activePlayerName="";
        public CharacterProfile activeCharacter;
        public List<String> events = new ArrayList<>(), positions = new ArrayList<>();
    }
    public synchronized PortalState portalState() {
        PortalState view = new PortalState();
        view.setupStage=state.setupStage;view.preparing=state.started&&!state.setupStage.equals("play");
        view.subdivisions=state.board.scale();view.boardSaved=state.boardSaved;view.legacyBoard=state.legacyBoard;view.tiles=state.board.tiles();
        view.notices=new ArrayList<>(state.notices);view.boardPlayers=new ArrayList<>();
        for(Player p:state.players)view.boardPlayers.add(new String[]{p.name,p.characterId,String.valueOf(p.x),String.valueOf(p.y)});
        for(String enemy:state.enemies){int[] at=state.enemyPositions.getOrDefault(enemy,new int[]{-1,-1});view.boardPlayers.add(new String[]{characterName(enemy),enemy,String.valueOf(at[0]),String.valueOf(at[1])});}
        view.setupTitle=state.setupStage.equals("selecting")?"der Raummeister wird gerade ausgewählt...":
            state.setupStage.equals("offering")?playerName(state.candidate)+", möchtest du das Raumgefüge wiederherstellen?":
            state.setupStage.equals("building")?playerName(state.roomMaster)+" stellt das Raumgefüge wieder her…":
            state.setupStage.equals("launching")?"Das Raumgefüge steht. Das Abenteuer beginnt!":"";
        view.sessionId=state.id;view.rollId=state.rollEventId;view.rollPlayer=playerName(state.rollPlayerId);
        view.round=state.round;view.die=state.lastRoll;for(StoryEntry entry:state.story)view.events.add(entry.display());
        for(Player p:state.players)view.positions.add(p.name+" · "+characterName(p.characterId)+" · Feld "+cell(p));
        if(!playing()){view.headline="Dein Team sammelt sich";view.detail="Charaktere wählen und Abenteuer starten.";}
        else if(state.phase.equals("players")){
            Player p=state.players.get(state.current);view.activePlayerName=p.name;view.awaitingGo=!state.turnAccepted;
            for(CharacterProfile c:catalogue)if(c.id.equals(p.characterId))view.activeCharacter=c;
            view.headline=view.awaitingGo?p.name+" ist dran…":p.name+" · "+characterName(p.characterId);
            view.detail=view.awaitingGo?"Wartet auf Go am Spielerhandy.":"Feld "+cell(p)+" · "+(state.turnStage.equals("roll")?"Zuerst am eigenen Handy würfeln.":state.turnStage.equals("choice")?"Bewegung oder Charakteraktion wählen.":"Noch "+state.remaining+" Felder ziehen.");
        }else{
            view.headline=characterName(state.enemies.get(state.enemyIndex))+" · Gegnerzug";
            view.detail=state.attackPending?"Angriff auf "+playerName(state.attackTarget)+": "+state.attackText:!state.helperId.isEmpty()?playerName(state.helperId)+" bewegt die Gegnerfigur.":"Gegneraktion an der Spielleitung festlegen.";
        }
        return view;
    }
    public synchronized String lobbyText() {
        StringBuilder b = new StringBuilder(selectedCount() + " / " + state.players.size() + " haben gewählt · " + state.players.size() + " / 4 beigetreten\n");
        for (Player p : state.players) b.append(p.name).append(" → ").append(p.characterId.isEmpty() ? "wählt noch …" : characterName(p.characterId)).append('\n');
        b.append(readyForPlay() ? "Alle bereit. Übrige Figuren können als Gegner hinzugefügt werden.\n" : "Alle Spieler sind Partner.\n");
        return b.toString();
    }
    public synchronized String hostText() {
        StringBuilder b = new StringBuilder(playing() ? "Runde " + state.round : "Charakterauswahl");
        b.append(" · ").append(state.players.size()).append(" / 4 Spieler\n");
        b.append(selectedCount()).append(" / ").append(state.players.size()).append(" haben eine Figur gewählt\n");
        for (Player p : state.players) b.append(p.name).append(" → ").append(p.characterId.isEmpty() ? "wählt noch …" : characterName(p.characterId))
            .append(" · Partner").append(playing() ? " · " + cell(p) : "").append('\n');
        for (String enemy : state.enemies) b.append("Gegner: ").append(characterName(enemy)).append('\n');
        if (playing()) {
            if (state.phase.equals("players")) {
                b.append("Am Zug: ").append(state.players.get(state.current).name).append('\n');
                b.append(!state.turnAccepted ? "Wartet auf Go am Spielerhandy." : state.turnStage.equals("roll") ? "Zuerst am eigenen Handy würfeln." : state.turnStage.equals("choice") ?
                    "Wurf " + state.turnRoll + ": Bewegung oder Aktion wählen." : "Noch " + state.remaining + " Felder ziehen.").append('\n');
            }
            else {
                b.append("Gegnerzug: ").append(characterName(state.enemies.get(state.enemyIndex))).append('\n');
                if (!state.helperId.isEmpty()) b.append("Bewegung ausführen: ").append(playerName(state.helperId)).append('\n');
                if (state.attackPending) b.append("Angriff bei ").append(playerName(state.attackTarget)).append(": ").append(state.attackText).append('\n');
            }
        }
        if (state.lastRoll > 0) b.append("Letzter W6: ").append(state.lastRoll).append('\n');
        for (int i = Math.max(0, state.log.size() - 3); i < state.log.size(); i++) b.append(state.log.get(i)).append('\n');
        return b.toString();
    }
    public synchronized String join(String name, String code, String requestId) throws IOException {
        if (!state.code.equals(code)) throw new InvalidPortalCodeException();
        name = name == null ? "" : name.trim();
        if (name.isEmpty() || name.length() > 24 || name.codePoints().anyMatch(Character::isISOControl))
            throw new IllegalArgumentException("Name: 1–24 Zeichen ohne Steuerzeichen.");
        requireRequest(requestId);
        for (Player p : state.players) if (p.joinRequest.equals(requestId)) {
            if (!p.name.equals(name)) throw new IllegalArgumentException("Anmeldung bereits verwendet.");
            return credentials(state, p);
        }
        for(Player existing:state.players)if(existing.name.equalsIgnoreCase(name))throw new IllegalArgumentException("Dieser Name ist schon belegt. Verwende deinen bisherigen Browser oder einen anderen Namen.");
        if (state.started) throw new IllegalArgumentException("Partie läuft bereits. Vorhandene Anmeldung verwenden.");
        if (state.players.size() >= 4) throw new IllegalArgumentException("Alle vier Spielerplätze sind belegt.");
        State next = copy();
        Player p = new Player();
        p.id = UUID.randomUUID().toString();
        p.token = UUID.randomUUID().toString() + UUID.randomUUID();
        p.joinRequest = requestId; p.name = name;
        p.x = next.players.size(); p.y = 0;
        next.players.add(p); addLog(next, name + " ist beigetreten.");
        commit(next);
        return credentials(state, p);
    }
    public synchronized String joinLocal(String name,String requestId)throws IOException{
        name=name==null?"":name.trim();requireRequest(requestId);
        for(Player p:state.players)if(p.reclaimable&&p.name.equalsIgnoreCase(name)){
            State next=copy();Player target=next.players.get(state.players.indexOf(p));target.reclaimable=false;target.joinRequest=requestId;
            addLog(next,target.name+" hat den gespeicherten Spielerplatz übernommen.");commit(next);return credentials(state,target);
        }
        return join(name,state.code,requestId);
    }
    /** Complete private save; called only by the native host, never an HTTP administration route. */
    public synchronized byte[] exportGame()throws IOException{return encode(state);}
    public synchronized void loadGame(byte[] bytes)throws IOException{
        if(bytes==null)throw new IOException("Spielstand fehlt.");
        State next=decode(bytes); // Validate before touching current state or durable storage.
        next.id=UUID.randomUUID().toString();next.revision=state.revision;
        for(Player p:next.players){p.token=UUID.randomUUID().toString()+UUID.randomUUID();p.joinRequest="";p.reclaimable=true;}
        if(next.setupStage.equals("selecting")||next.setupStage.equals("launching"))next.setupDeadline=clock.millis()+3000;
        commit(next);
    }
    public synchronized void resetGame()throws IOException{
        State next=new State();next.id=UUID.randomUUID().toString();next.code=String.format(Locale.ROOT,"%06d",random.nextInt(1_000_000));next.revision=state.revision;
        addLog(next,"Neue Partie bereit. Portal steht im Norden.");commit(next);
    }
    /** Native host only. Revoke this seat without disturbing other registrations. */
    public synchronized void removePlayer(String playerId)throws IOException{
        int index=-1;for(int i=0;i<state.players.size();i++)if(state.players.get(i).id.equals(playerId))index=i;
        if(index<0)return;
        State next=copy();Player removed=next.players.get(index);
        if(next.started&&next.phase.equals("players")&&next.current==index&&next.players.size()>1)endPlayerTurn(next);
        next.players.remove(index);
        if(next.current>index)next.current--;if(next.current>=next.players.size())next.current=0;
        next.asked.remove(playerId);next.receipts.removeIf(r->r.startsWith(playerId+":"));
        if(next.helperId.equals(playerId)){next.helperId="";next.movementDone=false;}
        if(next.attackTarget.equals(playerId)){next.attackId="";next.attackEnemy="";next.attackTarget="";next.attackText="";next.attackPending=false;}
        if(next.rollPlayerId.equals(playerId)){next.rollPlayerId="";next.rollEventId="";next.lastRoll=0;}
        boolean lostMaster=next.roomMaster.equals(playerId),lostCandidate=next.candidate.equals(playerId);
        if(lostMaster)next.roomMaster="";if(lostCandidate)next.candidate="";
        if(next.players.isEmpty()){
            next.started=false;next.setupStage="lobby";next.setupId="";next.setupDeadline=0;
            next.roomMaster="";next.candidate="";next.asked.clear();next.forcedRoomMaster=false;next.boardSaved=false;
            next.phase="players";next.enemyIndex=0;next.helperId="";next.movementDone=false;
            next.turnStage="roll";next.turnRoll=0;next.remaining=0;next.turnAccepted=false;
        }else if((lostCandidate&&next.setupStage.equals("offering"))||(lostMaster&&Arrays.asList("building","launching").contains(next.setupStage))){
            next.setupId=UUID.randomUUID().toString();next.forcedRoomMaster=false;next.boardSaved=false;offerNext(next);
        }
        addLog(next,removed.name+" wurde am Portal aus der Partie entfernt.");commit(next);
    }
    public synchronized void toggleDungeon(int x,int y)throws IOException{State next=copy();next.board.toggleDungeon(x,y);addLog(next,"Kerker "+BoardPlan.cell(x,y)+(next.board.at(x,y).closed?" geschlossen.":" geöffnet."));commit(next);}
    public synchronized List<String[]> dungeonStates(){List<String[]> list=new ArrayList<>();for(BoardPlan.Tile t:state.board.tiles())if(t.kind.equals("dungeon"))list.add(new String[]{""+t.x,""+t.y,BoardPlan.cell(t.x,t.y)+(t.closed?" · geschlossen":" · offen")});return list;}
    private static String credentials(State s, Player p) {
        return "{\"token\":" + quote(p.token) + ",\"sessionId\":" + quote(s.id) + "}";
    }
    public synchronized void startDemo() throws IOException {
        if (state.players.isEmpty()) throw new IllegalArgumentException("Zuerst mindestens einen Spieler anmelden.");
        if (!readyForPlay()) throw new IllegalArgumentException("Alle beigetretenen Spieler müssen zuerst eine Figur wählen.");
        if (state.started) return;
        State next = copy(); next.started = true;next.setupStage="selecting";next.setupId=UUID.randomUUID().toString();next.setupDeadline=clock.millis()+3000;
        for(Player p:next.players){p.x=-1;p.y=-1;}
        addLog(next,"Das Portal sucht den Raummeister. Das Spiel wartet auf den gespeicherten Aufbau.");
        commit(next);
    }
    /** Host-only: preserve an existing adventure while recording or correcting its physical plan. */
    public synchronized void rebuildBoard() throws IOException {
        if(!playing())throw new IllegalArgumentException("Zuerst Partie starten oder laufenden Aufbau abschließen.");
        State next=copy();next.setupStage="selecting";next.setupId=UUID.randomUUID().toString();next.setupDeadline=clock.millis()+3000;
        next.roomMaster="";next.candidate="";next.asked.clear();next.forcedRoomMaster=false;next.boardSaved=false;
        if(next.legacyBoard)for(Player p:next.players){p.x=-1;p.y=-1;}
        if(!next.board.subdivided()){next.board.upgrade();for(Player p:next.players)if(p.x>=0){p.x*=2;p.y*=2;}}
        next.legacyBoard=false;addLog(next,"Die Partie pausiert zum Erfassen des Raumgefüges. Runde und Zug bleiben erhalten.");commit(next);
    }
    public synchronized String snapshot(String token) {
        Player self = authenticated(state, token);
        StringBuilder b = new StringBuilder("{\"sessionId\":" + quote(state.id));
        b.append(",\"revision\":").append(state.revision).append(",\"started\":").append(state.started)
            .append(",\"ready\":").append(readyForPlay()).append(",\"selectedCount\":").append(selectedCount())
            .append(",\"setup\":{\"stage\":").append(quote(state.setupStage)).append(",\"id\":").append(quote(state.setupId)).append(",\"roomMaster\":").append(quote(state.roomMaster))
            .append(",\"candidate\":").append(quote(state.candidate)).append(",\"forced\":").append(state.forcedRoomMaster)
            .append(",\"saved\":").append(state.boardSaved).append(",\"legacy\":").append(state.legacyBoard).append("}")
            .append(",\"board\":").append(state.board.json())
            .append(",\"round\":").append(state.round).append(",\"lastRoll\":").append(state.lastRoll)
            .append(",\"rollEvent\":{\"id\":").append(quote(state.rollEventId)).append(",\"playerId\":").append(quote(state.rollPlayerId)).append(",\"value\":").append(state.lastRoll).append("}")
            .append(",\"turn\":{\"stage\":").append(quote(state.turnStage)).append(",\"roll\":").append(state.turnRoll)
            .append(",\"remaining\":").append(state.remaining).append(",\"accepted\":").append(state.turnAccepted).append("}")
            .append(",\"you\":").append(quote(self.id)).append(",\"phase\":").append(quote(state.phase))
            .append(",\"helperId\":").append(quote(state.helperId)).append(",\"movementDone\":").append(state.movementDone)
            .append(",\"activeEnemy\":").append(quote(state.phase.equals("enemies") ? state.enemies.get(state.enemyIndex) : ""))
            .append(",\"attack\":{\"id\":").append(quote(state.attackId)).append(",\"enemyId\":").append(quote(state.attackEnemy))
            .append(",\"targetId\":").append(quote(state.attackTarget)).append(",\"text\":").append(quote(state.attackText))
            .append(",\"pending\":").append(state.attackPending).append("},\"current\":")
            .append(playing() && state.phase.equals("players") ? quote(state.players.get(state.current).id) : "null").append(",\"players\":[");
        for (int i = 0; i < state.players.size(); i++) {
            Player p = state.players.get(i); if (i > 0) b.append(',');
            b.append("{\"id\":").append(quote(p.id)).append(",\"name\":").append(quote(p.name))
                .append(",\"characterId\":").append(quote(p.characterId)).append(",\"team\":\"partners\"")
                .append(",\"x\":").append(p.x).append(",\"y\":").append(p.y).append('}');
        }
        b.append("],\"characters\":[");
        for (int i = 0; i < catalogue.size(); i++) {
            CharacterProfile c = catalogue.get(i); if (i > 0) b.append(',');
            String owner = ""; for (Player p : state.players) if (p.characterId.equals(c.id)) owner = p.id;
            b.append("{\"id\":").append(quote(c.id)).append(",\"name\":").append(quote(c.name))
                .append(",\"description\":").append(quote(c.description)).append(",\"endurance\":").append(quote(c.endurance))
                .append(",\"strength\":").append(quote(c.strength)).append(",\"portrait\":").append(quote(c.portrait))
                .append(",\"actionNeedsDirection\":").append(c.actionNeedsDirection).append(",\"owner\":").append(quote(owner)).append(",\"enemy\":").append(state.enemies.contains(c.id)).append(",\"abilities\":[");
            for (int j = 0; j < c.abilities.size(); j++) { if (j > 0) b.append(','); b.append(quote(c.abilities.get(j))); }
            b.append("]}");
        }
        b.append("],\"enemies\":[");
        for (int i = 0; i < state.enemies.size(); i++) {
            if (i > 0) b.append(','); b.append("{\"characterId\":").append(quote(state.enemies.get(i))).append(",\"team\":\"enemies\"");int[] at=state.enemyPositions.getOrDefault(state.enemies.get(i),new int[]{-1,-1});b.append(",\"x\":").append(at[0]).append(",\"y\":").append(at[1]).append("}");
        }
        b.append("],\"log\":[");
        for (int i = 0; i < state.log.size(); i++) { if (i > 0) b.append(','); b.append(quote(state.log.get(i))); }
        b.append("],\"story\":[");
        for(int i=0;i<state.story.size();i++){
            StoryEntry e=state.story.get(i);if(i>0)b.append(',');
            b.append("{\"id\":").append(quote(e.id)).append(",\"timestampMillis\":").append(e.timestampMillis)
                .append(",\"dateTime\":").append(quote(e.dateTime)).append(",\"speaker\":").append(quote(e.speaker))
                .append(",\"playerName\":").append(quote(e.playerName)).append(",\"text\":").append(quote(e.text))
                .append(",\"direction\":").append(quote(e.direction)).append('}');
        }
        return b.append("]}").toString();
    }
    public synchronized String command(String token, String requestId, long revision, String action, String direction) throws IOException {
        Player actor = authenticated(state, token);
        requireRequest(requestId);
        String receipt = actor.id + ":" + requestId;
        if (state.receipts.contains(receipt)) return snapshot(token); // Retry never rerolls or moves twice.
        if (revision != state.revision) throw new IllegalArgumentException("Spielstand geändert. Bitte aktualisieren.");
        if(Arrays.asList("roomYes","roomNo","tilePlace","tileMove","tileRotate","tileRemove","tileState","setSpawn","boardSave").contains(action))
            return setupCommand(actor,token,receipt,action,direction);
        if("moveEnemy".equals(action)){
            if(!playing()||!state.phase.equals("enemies")||!actor.id.equals(state.helperId))throw new IllegalArgumentException("Kein Bewegungsauftrag für dich.");
            State next=copy();String enemy=next.enemies.get(next.enemyIndex);int[] at=next.enemyPositions.get(enemy);if(at==null)throw new IllegalArgumentException("Gegnerposition zuerst im Aufbau setzen.");
            int d="NESW".indexOf(direction==null?"":direction);if(d<0||direction.length()!=1)throw new IllegalArgumentException("Richtung fehlt.");int x=at[0]+(d==1?1:d==3?-1:0),y=at[1]+(d==2?1:d==0?-1:0);
            if(!next.board.canStep(at[0],at[1],x,y))throw new IllegalArgumentException("Weg blockiert.");for(Player p:next.players)if(p.x==x&&p.y==y)throw new IllegalArgumentException("Feld belegt.");for(Map.Entry<String,int[]> other:next.enemyPositions.entrySet())if(!other.getKey().equals(enemy)&&other.getValue()[0]==x&&other.getValue()[1]==y)throw new IllegalArgumentException("Feld belegt.");
            next.enemyPositions.put(enemy,new int[]{x,y});addSpeech(next,characterName(enemy)+" zieht nach "+next.board.fieldName(x,y),enemy,actor.name,"Ich ziehe zu Feld "+next.board.fieldName(x,y)+".",direction);remember(next,receipt);commit(next);return snapshot(token);
        }
        if ("confirmMovement".equals(action)) {
            if (!playing() || !state.phase.equals("enemies") || !actor.id.equals(state.helperId)) throw new IllegalArgumentException("Du wurdest nicht für diese Bewegung ausgewählt.");
            State next = copy(); next.helperId = ""; next.movementDone = true;
            addSpeech(next,actor.name+" hat die Gegnerbewegung auf dem Tisch bestätigt.",actor.characterId,actor.name,"Ich habe die Gegnerfigur auf dem Tisch bewegt.",""); remember(next, receipt); commit(next); return snapshot(token);
        }
        if ("ackAttack".equals(action)) {
            if (!state.attackPending || !actor.id.equals(state.attackTarget)) throw new IllegalArgumentException("Kein Angriff zur Bestätigung.");
            State next = copy(); next.attackPending = false; remember(next, receipt); commit(next); return snapshot(token);
        }
        if (!playing() || !state.phase.equals("players") || !actor.id.equals(state.players.get(state.current).id))
            throw new IllegalArgumentException("Du bist gerade nicht am Zug.");
        if("beginTurn".equals(action)){
            if(!state.turnStage.equals("roll"))throw new IllegalArgumentException("Dieser Zug hat bereits begonnen.");
            if(state.turnAccepted)return snapshot(token);
            State next=copy();next.turnAccepted=true;Player p=next.players.get(next.current);
            addSpeech(next,p.name+" bestätigt Go.",p.characterId,p.name,"Ich bin bereit. Meine Reise geht weiter.","");
            remember(next,receipt);commit(next);return snapshot(token);
        }
        if(!state.turnAccepted)throw new IllegalArgumentException("Zuerst mit Go deinen Zug beginnen.");
        State next = copy(); Player p = next.players.get(next.current);
        boolean finished = false;
        switch (action == null ? "" : action) {
            case "move":
                if (!next.turnStage.equals("move") || next.remaining < 1) throw new IllegalArgumentException("Zuerst würfeln und gegebenenfalls Bewegung wählen.");
                int dx = 0, dy = 0;
                switch (direction == null ? "" : direction) {
                    case "N": dy = -1; break;
                    case "E": dx = 1; break;
                    case "S": dy = 1; break;
                    case "W": dx = -1; break;
                    default: throw new IllegalArgumentException("Ungültige Richtung.");
                }
                int x = p.x + dx, y = p.y + dy;
                if(next.legacyBoard){if(x<0||x>=6||y<0||y>=6)throw new IllegalArgumentException("Außerhalb des bisherigen Testfelds.");}
                else if(!next.board.exists(x,y))throw new IllegalArgumentException("Hier existiert kein Spielfeld.");
                else if(!next.board.canStep(p.x,p.y,x,y))throw new IllegalArgumentException("Eine Wand versperrt diesen Weg.");
                for(int[] at:next.enemyPositions.values())if(at[0]==x&&at[1]==y)throw new IllegalArgumentException("Dieses Feld ist durch einen Gegner belegt.");
                for (Player other : next.players) if (other != p && other.x == x && other.y == y)
                    throw new IllegalArgumentException("Dieses Feld ist belegt.");
                String from = cell(p); p.x = x; p.y = y;
                next.remaining--;
                addSpeech(next, p.name + " zieht von " + from + " nach " + cell(p) + " · noch " + next.remaining + " Felder. Figur auf dem Tisch versetzen.",
                    p.characterId,p.name,(p.characterId.equals("dalek")?"Ich rolle":"Ich gehe")+" von Feld "+from+" zu Feld "+cell(p)+". Noch "+next.remaining+" Felder liegen vor mir.",direction);
                finished = next.remaining == 0;
                break;
            case "roll":
                if (!next.turnStage.equals("roll")) throw new IllegalArgumentException("Du hast in diesem Zug bereits gewürfelt.");
                next.lastRoll = random.nextInt(6) + 1;
                next.rollEventId = UUID.randomUUID().toString(); next.rollPlayerId = p.id;
                next.turnRoll = next.lastRoll; next.remaining = next.turnRoll;
                next.turnStage = next.turnRoll >= 5 ? "choice" : "move";
                addSpeech(next, p.name + " würfelt " + next.turnRoll + (next.turnRoll >= 5 ? ": Bewegung oder Aktion wählen." : ": genau so viele Felder ziehen."),
                    p.characterId,p.name,"Ich würfle eine "+next.turnRoll+". "+(next.turnRoll>=5?"Ich kann Bewegung oder Aktion wählen.":"Ich ziehe genau so viele Felder."),"");
                break;
            case "chooseMove":
                if (!next.turnStage.equals("choice")) throw new IllegalArgumentException("Keine Bewegungswahl offen.");
                next.turnStage = "move";
                addSpeech(next,p.name+" wählt Bewegung um genau "+next.turnRoll+" Felder.",p.characterId,p.name,"Ich entscheide mich für "+next.turnRoll+" Felder Bewegung.","");
                break;
            case "action":
                if (!next.turnStage.equals("choice") || next.turnRoll < 5) throw new IllegalArgumentException("Eine Aktion steht nur statt Bewegung bei einer 5 oder 6 zur Wahl.");
                CharacterProfile acting=profile(p.characterId);
                String selectedDirection=direction==null?"":direction;
                if(acting.actionNeedsDirection||!selectedDirection.isEmpty())directionName(selectedDirection);
                String actionText=p.characterId.equals("doctor")?"Ich richte meinen Schallschraubenzieher":p.characterId.equals("dalek")?"Ich richte meinen Strahl":p.characterId.equals("weeping-angel")?"Ich richte meine Fähigkeit":"Ich wähle eine Charakteraktion";
                if(!selectedDirection.isEmpty())actionText+=" nach "+directionName(selectedDirection);
                addSpeech(next,p.name+" wählt eine Charakteraktion"+(selectedDirection.isEmpty()?"":" nach "+directionName(selectedDirection))+" (Techniktest: Auswahl gespeichert, Wirkung noch nicht berechnet).",
                    p.characterId,p.name,actionText+". Auswahl gespeichert; die Wirkung ist noch nicht festgelegt.",selectedDirection);
                finished = true;
                break;
            default: throw new IllegalArgumentException("Unbekannte Aktion.");
        }
        remember(next, receipt);
        if (finished) endPlayerTurn(next);
        commit(next);
        return snapshot(token);
    }
    /** Progress only host-clock presentation stages; the committed choice is never rerolled by polling. */
    public synchronized void tick() throws IOException {
        if((state.setupStage.equals("selecting")||state.setupStage.equals("launching"))&&clock.millis()>=state.setupDeadline){
            State next=copy();
            if(next.setupStage.equals("selecting"))offerNext(next);
            else{next.setupStage="play";next.setupDeadline=0;addLog(next,"Das Abenteuer beginnt. Bestätige deinen Zug mit Go.");}
            commit(next);
        }
    }
    public synchronized boolean preparing(){return state.started&&!state.setupStage.equals("play");}
    private void offerNext(State next){
        List<Player> choices=new ArrayList<>();for(Player p:next.players)if(!next.asked.contains(p.id))choices.add(p);
        next.setupDeadline=0;
        if(choices.isEmpty()){
            Player p=next.players.get(random.nextInt(next.players.size()));next.roomMaster=p.id;next.candidate="";
            next.forcedRoomMaster=true;next.setupStage="building";
            notice(next,"Alle haben abgelehnt. Das Portal entscheidet: "+p.name+" ist Raummeister.");
            addLog(next,"Das Portal bestimmt "+p.name+" zum Raummeister, nachdem alle abgelehnt haben.");
        }else{
            Player p=choices.get(random.nextInt(choices.size()));next.candidate=p.id;next.asked.add(p.id);next.setupStage="offering";
        }
    }
    private void notice(State next,String text){next.notices.add(new PortalNotice(UUID.randomUUID().toString(),text));while(next.notices.size()>40)next.notices.remove(0);}
    private String setupCommand(Player actor,String token,String receipt,String action,String payload)throws IOException{
        State next=copy();
        if(action.equals("roomYes")||action.equals("roomNo")){
            if(!next.setupStage.equals("offering")||!actor.id.equals(next.candidate))throw new IllegalArgumentException("Gerade wird ein anderer Spieler gefragt.");
            if(action.equals("roomYes")){
                next.roomMaster=actor.id;next.candidate="";next.setupStage="building";
                notice(next,actor.name+" hat zugesagt und ist Raummeister!");addLog(next,actor.name+" stellt das Raumgefüge wieder her.");
            }else{
                notice(next,actor.name+" hat abgelehnt. Das Portal sucht weiter…");addLog(next,actor.name+" lehnt die Raummeister-Rolle ab.");offerNext(next);
            }
        }else{
            if(!next.setupStage.equals("building")||!actor.id.equals(next.roomMaster))throw new IllegalArgumentException("Nur der Raummeister kann jetzt den Aufbau bearbeiten.");
            String[] args=(payload==null?"":payload).split(",",-1);
            try{
                switch(action){
                    case "tilePlace":requireArgs(args,4);next.board.place(number(args[0]),number(args[1]),args[2],number(args[3]));break;
                    case "tileMove":requireArgs(args,4);next.board.move(number(args[0]),number(args[1]),number(args[2]),number(args[3]));break;
                    case "tileRotate":requireArgs(args,2);next.board.rotate(number(args[0]),number(args[1]));break;
                    case "tileState":requireArgs(args,2);next.board.toggleDungeon(number(args[0]),number(args[1]));break;
                    case "tileRemove":requireArgs(args,2);next.board.remove(number(args[0]),number(args[1]));break;
                    case "setSpawn":
                        requireArgs(args,3);Player target=null;for(Player p:next.players)if(p.id.equals(args[0]))target=p;
                        int x=number(args[1]),y=number(args[2]);if(target==null&&args[0].startsWith("enemy:")&&next.enemies.contains(args[0].substring(6))){
                            if(!next.board.exists(x,y))throw new IllegalArgumentException("Kein vorhandenes Feld.");
                            for(Player other:next.players)if(other.x==x&&other.y==y)throw new IllegalArgumentException("Feld belegt.");
                            for(Map.Entry<String,int[]> other:next.enemyPositions.entrySet())if(!other.getKey().equals(args[0].substring(6))&&other.getValue()[0]==x&&other.getValue()[1]==y)throw new IllegalArgumentException("Feld belegt.");
                            next.enemyPositions.put(args[0].substring(6),new int[]{x,y});break;
                        }
                        if(target==null||!next.board.exists(x,y))throw new IllegalArgumentException("Startfeld muss auf einer Kachel liegen.");
                        for(Player other:next.players)if(other!=target&&other.x==x&&other.y==y)throw new IllegalArgumentException("Dieses Startfeld ist bereits belegt.");
                        for(int[] at:next.enemyPositions.values())if(at[0]==x&&at[1]==y)throw new IllegalArgumentException("Feld belegt.");target.x=x;target.y=y;break;
                    case "boardSave":
                        if(next.board.cells()<next.players.size()+next.enemies.size())throw new IllegalArgumentException("Für jeden Spieler wird mindestens ein eigenes Startfeld benötigt.");
                        repairSpawns(next);
                        for(Player p:next.players)if(!next.board.exists(p.x,p.y))throw new IllegalArgumentException("Zuerst für alle Spieler ein Startfeld platzieren.");
                        next.boardSaved=true;next.setupStage="launching";next.setupDeadline=clock.millis()+3000;
                        addLog(next,actor.name+" hat das Raumgefüge gespeichert: "+next.board.tiles().size()+" Kacheln, "+next.board.cells()+" Felder. Startfiguren wie im Plan aufstellen.");break;
                    default:throw new IllegalArgumentException("Unbekannte Aufbauaktion.");
                }
            }catch(NumberFormatException e){throw new IllegalArgumentException("Ungültige Rasterkoordinate.");}
            repairSpawns(next);
        }
        remember(next,receipt);commit(next);return snapshot(token);
    }
    private static void requireArgs(String[] args,int n){if(args.length!=n)throw new IllegalArgumentException("Unvollständige Aufbauaktion.");}
    private static int number(String value){if(!value.matches("[0-9]{1,2}"))throw new NumberFormatException();return Integer.parseInt(value);}
    private static void repairSpawns(State s){
        Set<String> occupied=new HashSet<>();
        for(Player p:s.players){
            if(!s.board.exists(p.x,p.y)||!occupied.add(p.x+","+p.y)){p.x=-1;p.y=-1;}
        }
        for(Map.Entry<String,int[]> entry:s.enemyPositions.entrySet()){int[] a=entry.getValue();if(s.board.exists(a[0],a[1])&&occupied.add(a[0]+","+a[1]))continue;entry.setValue(new int[]{-1,-1});}
        for(Player p:s.players)if(p.x<0){
            outer:for(int y=0;y<20*s.board.scale();y++)for(int x=0;x<20*s.board.scale();x++)if(s.board.exists(x,y)&&occupied.add(x+","+y)){p.x=x;p.y=y;break outer;}
        }
        for(String enemy:s.enemies){int[] a=s.enemyPositions.get(enemy);if(a!=null&&a[0]>=0)continue;
            outer:for(int y=0;y<20*s.board.scale();y++)for(int x=0;x<20*s.board.scale();x++)if(s.board.exists(x,y)&&occupied.add(x+","+y)){s.enemyPositions.put(enemy,new int[]{x,y});break outer;}
        }
        s.enemyPositions.entrySet().removeIf(e->e.getValue()[0]<0);
    }
    private static void validateSetup(State s,Set<String> ids)throws IOException{
        if(!Arrays.asList("lobby","selecting","offering","building","launching","play").contains(s.setupStage)||
            (!s.started)!=s.setupStage.equals("lobby")||s.setupDeadline<0||
            (!s.setupId.isEmpty()&&!s.setupId.matches("[a-f0-9-]{36}"))||
            (!s.candidate.isEmpty()&&!s.setupStage.equals("offering"))||
            (Arrays.asList("selecting","launching").contains(s.setupStage)!=(s.setupDeadline>0))||
            (!s.roomMaster.isEmpty()&&!ids.contains(s.roomMaster))||(!s.candidate.isEmpty()&&!ids.contains(s.candidate))||
            new HashSet<>(s.asked).size()!=s.asked.size()||!ids.containsAll(s.asked)||
            (s.setupStage.equals("offering")&&(s.candidate.isEmpty()||!s.asked.contains(s.candidate)))||
            (Arrays.asList("building","launching").contains(s.setupStage)&&s.roomMaster.isEmpty())||
            (s.boardSaved&&(!Arrays.asList("launching","play").contains(s.setupStage)||s.board.cells()<s.players.size()))||
            (s.setupStage.equals("play")&&!s.legacyBoard&&!s.boardSaved))throw new IOException("Ungültiger Aufbauzustand.");
        Set<String> occupied=new HashSet<>();
        for(Player p:s.players){
            if(p.x>=0&&!occupied.add(p.x+","+p.y))throw new IOException("Doppelte Figurenposition.");
            if(s.boardSaved&&!s.board.exists(p.x,p.y))throw new IOException("Figur steht außerhalb des Aufbaus.");
            if(s.legacyBoard&&(p.x<0||p.x>=6||p.y>=6))throw new IOException("Ungültige alte Position.");
        }
    }
    private static void endPlayerTurn(State next) {
        next.turnStage = "roll"; next.turnRoll = 0; next.remaining = 0; next.turnAccepted = false;
        next.current = (next.current + 1) % next.players.size();
        if (next.current == 0) {
            if (next.enemies.isEmpty()) next.round++;
            else { next.phase = "enemies"; next.enemyIndex = 0; next.movementDone = false; }
        }
    }
    private String cell(Player p) { return state.legacyBoard?BoardPlan.cell(p.x,p.y):state.board.fieldName(p.x,p.y); }
    public synchronized int selectedCount() {
        int n = 0; for (Player p : state.players) if (!p.characterId.isEmpty()) n++; return n;
    }
    public synchronized boolean readyForPlay() { return !state.players.isEmpty() && selectedCount() == state.players.size(); }
    public synchronized boolean playing() { return state.started && state.setupStage.equals("play") && readyForPlay(); }
    private String playerName(String id) { for (Player p : state.players) if (p.id.equals(id)) return p.name; return "Spieler"; }
    public synchronized List<String[]> playersForHost() {
        List<String[]> result = new ArrayList<>();
        for (Player p : state.players) result.add(new String[]{p.id, p.name, characterName(p.characterId), p.characterId});
        return result;
    }
    private void requireEnemyTurn() {
        if (!playing() || !state.phase.equals("enemies")) throw new IllegalArgumentException("Gerade ist kein Gegner am Zug.");
    }
    public synchronized String requestEnemyMovement() throws IOException {
        requireEnemyTurn();
        if (!state.helperId.isEmpty()) return playerName(state.helperId);
        if (state.movementDone) throw new IllegalArgumentException("Bewegung für diesen Gegnerzug bereits bestätigt.");
        if (state.attackPending) throw new IllegalArgumentException("Zuerst den Angriff bestätigen lassen.");
        State next = copy(); next.helperId = next.players.get(random.nextInt(next.players.size())).id;
        addLog(next, playerName(next.helperId) + " wurde zufällig für die Gegnerbewegung ausgewählt."); commit(next);
        return playerName(state.helperId);
    }
    public synchronized void reportAttack(String targetId, String outcome) throws IOException {
        requireEnemyTurn();
        if (!state.helperId.isEmpty()) throw new IllegalArgumentException("Zuerst die Bewegung bestätigen lassen.");
        if (state.attackPending) throw new IllegalArgumentException("Der vorherige Angriff wird noch angezeigt.");
        boolean found = false; for (Player p : state.players) if (p.id.equals(targetId)) found = true;
        if (!found) throw new IllegalArgumentException("Unbekanntes Ziel.");
        String text = outcome == null ? "" : outcome.trim();
        if (text.isEmpty() || text.length() > 400 || text.codePoints().anyMatch(c -> Character.isISOControl(c) && c != '\n')) throw new IllegalArgumentException("Angriffsfolge: 1–400 Zeichen.");
        State next = copy(); next.attackId = UUID.randomUUID().toString(); next.attackEnemy = next.enemies.get(next.enemyIndex);
        next.attackTarget = targetId; next.attackText = text; next.attackPending = true;
        notice(next,characterName(next.attackEnemy)+" greift "+playerName(targetId)+" an!\n"+text);
        addSpeech(next,characterName(next.attackEnemy)+" → "+playerName(targetId)+": "+text,next.attackEnemy,"","Ich greife "+playerName(targetId)+" an. "+text,""); commit(next);
    }
    public synchronized void finishEnemyTurn() throws IOException {
        requireEnemyTurn();
        if (!state.helperId.isEmpty() || state.attackPending) throw new IllegalArgumentException("Bewegung oder Angriff wartet noch auf Bestätigung.");
        State next = copy(); next.enemyIndex++; next.movementDone = false;
        if (next.enemyIndex >= next.enemies.size()) { next.enemyIndex = 0; next.phase = "players"; next.round++; }
        commit(next);
    }
    private CharacterProfile profile(String id) {
        for (CharacterProfile p : catalogue) if (p.id.equals(id)) return p;
        throw new IllegalArgumentException("Unbekannter Charakter.");
    }
    private String characterName(String id) {
        for (CharacterProfile p : catalogue) if (p.id.equals(id)) return p.name;
        return id; // Older saves keep their identity even if a later catalogue changes.
    }
    public synchronized String chooseCharacter(String token, String requestId, String characterId) throws IOException {
        Player actor = authenticated(state, token); requireRequest(requestId); profile(characterId);
        String receipt = actor.id + ":choose:" + requestId;
        if (state.receipts.contains(receipt)) return snapshot(token);
        if (state.started && !(state.legacyBoard && actor.characterId.isEmpty())) throw new IllegalArgumentException("Die Charakterauswahl ist nach Spielstart geschlossen.");
        if (state.enemies.contains(characterId)) throw new IllegalArgumentException("Diese Figur ist bereits als Gegner hinzugefügt.");
        for (Player p : state.players) if (!p.id.equals(actor.id) && p.characterId.equals(characterId))
            throw new IllegalArgumentException("Diese Figur wurde gerade von einem anderen Spieler gewählt.");
        if (actor.characterId.equals(characterId)) return snapshot(token);
        State next = copy();
        for (Player p : next.players) if (p.id.equals(actor.id)) p.characterId = characterId;
        remember(next, receipt);
        addSpeech(next,actor.name+" wählt "+characterName(characterId)+" · Partner.",characterId,actor.name,"Ich begleite "+actor.name+" auf dieser Reise.","");
        commit(next); return snapshot(token);
    }
    public synchronized List<CharacterProfile> availableEnemies() {
        List<CharacterProfile> result = new ArrayList<>();
        if (!readyForPlay()) return result;
        for (CharacterProfile c : catalogue) {
            boolean used = state.enemies.contains(c.id);
            for (Player p : state.players) if (p.characterId.equals(c.id)) used = true;
            if (!used) result.add(c);
        }
        return result;
    }
    /** Host-only call. Intentionally not exposed as an HTTP route. */
    public synchronized void addEnemy(String characterId) throws IOException {
        profile(characterId);
        if(state.started)throw new IllegalArgumentException("Gegner vor dem Start hinzufügen.");
        if (!readyForPlay()) throw new IllegalArgumentException("Zuerst wählen alle Spieler ihre Figuren.");
        if (state.enemies.contains(characterId)) return;
        for (Player p : state.players) if (p.characterId.equals(characterId)) throw new IllegalArgumentException("Diese Figur gehört einem Partner.");
        State next = copy(); next.enemies.add(characterId);
        addLog(next, characterName(characterId) + " wurde am Portal als Gegner hinzugefügt."); commit(next);
    }
    private static void remember(State next, String receipt) {
        next.receipts.add(receipt);
        while (next.receipts.size() > 128) next.receipts.remove(next.receipts.iterator().next());
    }
    private static void requireRequest(String id) {
        if (id == null || !id.matches("[A-Za-z0-9-]{16,80}")) throw new IllegalArgumentException("Ungültige Anfrage-ID.");
    }
    private static Player authenticated(State s, String token) {
        if (token != null) for (Player p : s.players) if (p.token.equals(token)) return p;
        throw new SecurityException("Bitte erneut anmelden.");
    }
    private static String directionName(String value){
        switch(value){case "N":return "Norden";case "E":return "Osten";case "S":return "Süden";case "W":return "Westen";default:throw new IllegalArgumentException("Bitte eine Richtung wählen: Nord, Ost, Süd oder West.");}
    }
    private void addLog(State s,String text){addSpeech(s,text,"","",text,"");}
    private void addSpeech(State s,String legacy,String characterId,String playerName,String text,String direction){
        s.log.add(legacy);while(s.log.size()>60)s.log.remove(0);
        StoryEntry e=new StoryEntry();e.id=UUID.randomUUID().toString();e.timestampMillis=clock.millis();
        e.dateTime=DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm 'Uhr'",Locale.GERMAN).format(Instant.ofEpochMilli(e.timestampMillis).atZone(clock.getZone()));
        e.speaker=characterId.isEmpty()?"Portal":characterId.equals("doctor")?"Dr WHO":characterName(characterId);e.playerName=playerName;e.direction=direction;
        if(characterId.equals("dalek")&&flavourRandom.nextInt(4)==0){String[] sounds={"Knarz … grrr.","Grrr … knarz.","Krrr … grrr."};text+=" "+sounds[flavourRandom.nextInt(sounds.length)];}
        e.text=text;s.story.add(e);
    }
    private State copy() throws IOException { return decode(encode(state)); }
    private void commit(State next) throws IOException {
        next.revision++; store.save(encode(next)); state = next;
    }
    private static byte[] encode(State s) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bytes);
        out.writeInt(MAGIC); out.writeInt(FORMAT); out.writeUTF(s.id); out.writeUTF(s.code);
        out.writeBoolean(s.started); out.writeInt(s.round); out.writeInt(s.current);
        out.writeInt(s.lastRoll); out.writeLong(s.revision); out.writeInt(s.players.size());
        for (Player p : s.players) {
            out.writeUTF(p.id); out.writeUTF(p.token); out.writeUTF(p.name); out.writeUTF(p.joinRequest);
            out.writeInt(p.x); out.writeInt(p.y);
            out.writeUTF(p.characterId);
        }
        out.writeInt(s.enemies.size()); for (String id : s.enemies) out.writeUTF(id);
        out.writeInt(s.log.size()); for (String entry : s.log) out.writeUTF(entry);
        out.writeInt(s.receipts.size()); for (String id : s.receipts) out.writeUTF(id);
        out.writeUTF(s.phase); out.writeInt(s.enemyIndex); out.writeUTF(s.helperId); out.writeBoolean(s.movementDone);
        out.writeUTF(s.attackId); out.writeUTF(s.attackEnemy); out.writeUTF(s.attackTarget); out.writeUTF(s.attackText); out.writeBoolean(s.attackPending);
        out.writeUTF(s.turnStage); out.writeInt(s.turnRoll); out.writeInt(s.remaining);
        out.writeUTF(s.rollEventId); out.writeUTF(s.rollPlayerId);
        out.writeBoolean(s.turnAccepted);out.writeInt(s.story.size());
        for(StoryEntry e:s.story){out.writeUTF(e.id);out.writeLong(e.timestampMillis);out.writeUTF(e.dateTime);out.writeUTF(e.speaker);out.writeUTF(e.playerName);out.writeUTF(e.text);out.writeUTF(e.direction);}
        out.writeUTF(s.setupStage);out.writeUTF(s.setupId);out.writeUTF(s.roomMaster);out.writeUTF(s.candidate);out.writeLong(s.setupDeadline);
        out.writeBoolean(s.forcedRoomMaster);out.writeBoolean(s.boardSaved);out.writeBoolean(s.legacyBoard);
        out.writeInt(s.asked.size());for(String id:s.asked)out.writeUTF(id);s.board.write(out);
        out.writeInt(s.notices.size());for(PortalNotice notice:s.notices){out.writeUTF(notice.id);out.writeUTF(notice.text);}
        out.writeInt(s.players.size());for(Player p:s.players)out.writeBoolean(p.reclaimable);
        out.writeInt(s.enemyPositions.size());for(Map.Entry<String,int[]> e:s.enemyPositions.entrySet()){out.writeUTF(e.getKey());out.writeInt(e.getValue()[0]);out.writeInt(e.getValue()[1]);}
        out.flush();if(bytes.size()>8*1024*1024)throw new IOException("Spielstand zu groß. Bitte sichern und neue Partie beginnen.");return bytes.toByteArray();
    }
    private static State decode(byte[] bytes) throws IOException {
        if (bytes.length > 8*1024*1024) throw new IOException("Spielstand zu groß.");
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
        if (in.readInt() != MAGIC) throw new IOException("Unbekanntes Spielstandformat.");
        int format = in.readInt();
        if (format < 1 || format > FORMAT) throw new IOException("Unbekanntes Spielstandformat.");
        State s = new State(); s.id = in.readUTF(); s.code = in.readUTF(); s.started = in.readBoolean();
        s.round = in.readInt(); s.current = in.readInt(); s.lastRoll = in.readInt(); s.revision = in.readLong();
        int n = count(in, 4);
        for (int i = 0; i < n; i++) {
            Player p = new Player(); p.id = in.readUTF(); p.token = in.readUTF(); p.name = in.readUTF();
            p.joinRequest = in.readUTF(); p.x = in.readInt(); p.y = in.readInt();
            if (format >= 2) p.characterId = in.readUTF();
            if (p.x < (format>=7?-1:0) || p.x >= (format>=8?40:format>=7?20:6) || p.y < (format>=7?-1:0) || p.y >= (format>=8?40:format>=7?20:6) || (p.x==-1)!=(p.y==-1)) throw new IOException("Ungültige Position.");
            s.players.add(p);
        }
        if (format >= 2) { int enemies = count(in, 256); for (int i = 0; i < enemies; i++) s.enemies.add(in.readUTF()); }
        Set<String> assigned = new HashSet<>();
        for (Player p : s.players) if (!p.characterId.isEmpty() && (!p.characterId.matches("[a-z0-9-]{1,64}") || !assigned.add(p.characterId)))
            throw new IOException("Doppelte oder ungültige Charaktervergabe.");
        for (String id : s.enemies) if (!id.matches("[a-z0-9-]{1,64}") || !assigned.add(id)) throw new IOException("Ungültige Gegnervergabe.");
        int logs = count(in, 60); for (int i = 0; i < logs; i++) s.log.add(in.readUTF());
        int receipts = count(in, 128); for (int i = 0; i < receipts; i++) s.receipts.add(in.readUTF());
        if (format >= 3) {
            s.phase = in.readUTF(); s.enemyIndex = in.readInt(); s.helperId = in.readUTF(); s.movementDone = in.readBoolean();
            s.attackId = in.readUTF(); s.attackEnemy = in.readUTF(); s.attackTarget = in.readUTF(); s.attackText = in.readUTF(); s.attackPending = in.readBoolean();
        }
        if (format >= 4) { s.turnStage = in.readUTF(); s.turnRoll = in.readInt(); s.remaining = in.readInt(); }
        if (format >= 5) { s.rollEventId = in.readUTF(); s.rollPlayerId = in.readUTF(); }
        if(format>=6){
            s.turnAccepted=in.readBoolean();int entries=count(in,format>=8?100000:60);
            for(int i=0;i<entries;i++){
                StoryEntry e=new StoryEntry();e.id=in.readUTF();e.timestampMillis=in.readLong();e.dateTime=in.readUTF();e.speaker=in.readUTF();e.playerName=in.readUTF();e.text=in.readUTF();e.direction=in.readUTF();
                if(!e.id.matches("[a-f0-9-]{36}")||e.timestampMillis<0||e.dateTime.length()>80||e.speaker.length()>128||e.playerName.length()>24||e.text.length()>4000||!e.direction.matches("[NESW]?"))throw new IOException("Ungültiger Story-Eintrag.");
                s.story.add(e);
            }
        }else{
            s.turnAccepted=!s.turnStage.equals("roll");
            for(int i=0;i<s.log.size();i++){
                StoryEntry e=new StoryEntry();e.id=UUID.nameUUIDFromBytes((s.id+":"+i+":"+s.log.get(i)).getBytes(StandardCharsets.UTF_8)).toString();e.timestampMillis=0;e.dateTime="";e.speaker="Archiv";e.playerName="";e.text=s.log.get(i);e.direction="";s.story.add(e);
            }
        }
        if(format>=7){
            s.setupStage=in.readUTF();s.setupId=in.readUTF();s.roomMaster=in.readUTF();s.candidate=in.readUTF();s.setupDeadline=in.readLong();
            s.forcedRoomMaster=in.readBoolean();s.boardSaved=in.readBoolean();s.legacyBoard=in.readBoolean();
            int asked=count(in,4);for(int i=0;i<asked;i++)s.asked.add(in.readUTF());s.board=BoardPlan.read(in,format>=8);
            int notices=count(in,40);for(int i=0;i<notices;i++){String id=in.readUTF(),text=in.readUTF();if(!id.matches("[a-f0-9-]{36}")||text.length()>800)throw new IOException("Ungültige Portalanzeige.");s.notices.add(new PortalNotice(id,text));}
        }else{s.setupStage=s.started?"play":"lobby";s.legacyBoard=s.started;}
        if(format>=8){int claims=count(in,4);if(claims!=s.players.size())throw new IOException("Ungültige Spielerplätze.");for(Player p:s.players)p.reclaimable=in.readBoolean();int positions=count(in,256);for(int i=0;i<positions;i++){String id=in.readUTF();int x=in.readInt(),y=in.readInt();if(!s.enemies.contains(id)||x<0||y<0||x>=40||y>=40||s.enemyPositions.put(id,new int[]{x,y})!=null)throw new IOException("Ungültige Gegnerposition.");}}
        Set<String> spots=new HashSet<>();for(Player p:s.players)if(p.x>=0)spots.add(p.x+","+p.y);for(int[] a:s.enemyPositions.values())if(!s.board.exists(a[0],a[1])||!spots.add(a[0]+","+a[1]))throw new IOException("Ungültige Figurenposition.");
        if((!s.turnAccepted&&!s.turnStage.equals("roll"))||(s.phase.equals("enemies")&&s.turnAccepted))throw new IOException("Ungültige Zugbestätigung.");
        boolean validTurn = s.turnStage.equals("roll") && s.turnRoll == 0 && s.remaining == 0 ||
            s.turnStage.equals("choice") && s.turnRoll >= 5 && s.turnRoll <= 6 && s.remaining == s.turnRoll ||
            s.turnStage.equals("move") && s.turnRoll >= 1 && s.turnRoll <= 6 && s.remaining >= 1 && s.remaining <= s.turnRoll;
        if (!validTurn || s.phase.equals("enemies") && !s.turnStage.equals("roll")) throw new IOException("Ungültiger Würfel-/Bewegungszustand.");
        Set<String> playerIds = new HashSet<>(); for (Player p : s.players) playerIds.add(p.id);
        if ((!s.rollEventId.isEmpty() && (!s.rollEventId.matches("[a-f0-9-]{36}") || !playerIds.contains(s.rollPlayerId) || s.lastRoll < 1)) ||
            (s.rollEventId.isEmpty() && !s.rollPlayerId.isEmpty())) throw new IOException("Ungültiges Würfelereignis.");
        validateSetup(s,playerIds);
        if (!(s.phase.equals("players") || s.phase.equals("enemies")) || s.enemyIndex < 0 ||
            (s.phase.equals("enemies") && (!s.started || s.enemyIndex >= s.enemies.size())) ||
            (!s.helperId.isEmpty() && (!s.phase.equals("enemies") || !playerIds.contains(s.helperId) || s.movementDone)) ||
            (s.attackPending && (!s.phase.equals("enemies") || !playerIds.contains(s.attackTarget) || !s.enemies.contains(s.attackEnemy) || s.attackId.isEmpty())))
            throw new IOException("Ungültige Gegnerphase.");
        if (in.available() != 0 || s.round < 1 || s.revision < 0 || s.current < 0 ||
            s.current >= Math.max(1, n) || (s.started && n == 0) || s.lastRoll < 0 || s.lastRoll > 6 || !s.code.matches("[0-9]{6}"))
            throw new IOException("Spielstand beschädigt.");
        return s;
    }
    private static int count(DataInputStream in, int max) throws IOException {
        int n = in.readInt(); if (n < 0 || n > max) throw new IOException("Ungültige Eintragszahl."); return n;
    }
    public static String quote(String text) {
        StringBuilder b = new StringBuilder("\"");
        for (char c : text.toCharArray()) {
            if (c == '\\' || c == '"') b.append('\\').append(c);
            else if (c < 32) b.append(String.format(Locale.ROOT, "\\u%04x", (int)c));
            else b.append(c);
        }
        return b.append('"').toString();
    }
}
