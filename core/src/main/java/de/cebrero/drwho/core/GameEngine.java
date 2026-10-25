package de.cebrero.drwho.core;

import java.io.*;
import java.security.SecureRandom;
import java.util.*;

/** Authoritative, deliberately small technical demo; no final character rules yet. */
public final class GameEngine {
    public interface Store { byte[] load() throws IOException; void save(byte[] bytes) throws IOException; }
    private static final int FORMAT = 4, MAGIC = 0x4457484f;
    private final Store store;
    private final SecureRandom random;
    private State state;
    private final List<CharacterProfile> catalogue;

    private static final class Player {
        String id, token, name, joinRequest;
        String characterId = "";
        int x, y;
    }
    private static final class State {
        String id, code;
        boolean started;
        int round = 1, current, lastRoll;
        long revision;
        List<Player> players = new ArrayList<>();
        List<String> enemies = new ArrayList<>();
        List<String> log = new ArrayList<>();
        LinkedHashSet<String> receipts = new LinkedHashSet<>();
        String phase = "players", helperId = "", attackId = "", attackEnemy = "", attackTarget = "", attackText = "";
        int enemyIndex;
        boolean movementDone, attackPending;
        String turnStage = "roll";
        int turnRoll, remaining;
    }

    public GameEngine(Store store) throws IOException { this(store, new SecureRandom()); }
    GameEngine(Store store, SecureRandom random) throws IOException { this(store, random, CharacterProfile.known()); }
    GameEngine(Store store, SecureRandom random, List<CharacterProfile> catalogue) throws IOException {
        this.store = store;
        this.random = random;
        this.catalogue = Collections.unmodifiableList(new ArrayList<>(catalogue));
        Set<String> ids = new HashSet<>();
        for (CharacterProfile profile : this.catalogue) if (!ids.add(profile.id)) throw new IllegalArgumentException("Doppelte Charakter-ID.");
        byte[] saved = store.load();
        if (saved == null) {
            State initial = new State();
            initial.id = UUID.randomUUID().toString();
            initial.code = String.format(Locale.ROOT, "%06d", random.nextInt(1_000_000));
            initial.log.add("Technikdemo bereit. Noch keine endgültigen Spielregeln.");
            store.save(encode(initial));
            state = initial;
        } else state = decode(saved); // Never silently replace an unreadable save.
    }

    public synchronized String code() { return state.code; }
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
                b.append(state.turnStage.equals("roll") ? "Zuerst am eigenen Handy würfeln." : state.turnStage.equals("choice") ?
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
        if (!state.code.equals(code)) throw new IllegalArgumentException("Beitrittscode falsch.");
        name = name == null ? "" : name.trim();
        if (name.isEmpty() || name.length() > 24 || name.codePoints().anyMatch(Character::isISOControl))
            throw new IllegalArgumentException("Name: 1–24 Zeichen ohne Steuerzeichen.");
        requireRequest(requestId);
        for (Player p : state.players) if (p.joinRequest.equals(requestId)) {
            if (!p.name.equals(name)) throw new IllegalArgumentException("Anmeldung bereits verwendet.");
            return credentials(state, p);
        }
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
    private static String credentials(State s, Player p) {
        return "{\"token\":" + quote(p.token) + ",\"sessionId\":" + quote(s.id) + "}";
    }
    public synchronized void startDemo() throws IOException {
        if (state.players.isEmpty()) throw new IllegalArgumentException("Zuerst mindestens einen Spieler anmelden.");
        if (!readyForPlay()) throw new IllegalArgumentException("Alle beigetretenen Spieler müssen zuerst eine Figur wählen.");
        if (state.started) return;
        State next = copy(); next.started = true;
        addLog(next, "Spiel gestartet: zuerst würfeln; 1–4 ziehen, bei 5/6 Bewegung oder Aktion wählen.");
        commit(next);
    }
    public synchronized String snapshot(String token) {
        Player self = authenticated(state, token);
        StringBuilder b = new StringBuilder("{\"sessionId\":" + quote(state.id));
        b.append(",\"revision\":").append(state.revision).append(",\"started\":").append(state.started)
            .append(",\"ready\":").append(readyForPlay()).append(",\"selectedCount\":").append(selectedCount())
            .append(",\"round\":").append(state.round).append(",\"lastRoll\":").append(state.lastRoll)
            .append(",\"turn\":{\"stage\":").append(quote(state.turnStage)).append(",\"roll\":").append(state.turnRoll)
            .append(",\"remaining\":").append(state.remaining).append("}")
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
                .append(",\"owner\":").append(quote(owner)).append(",\"enemy\":").append(state.enemies.contains(c.id)).append(",\"abilities\":[");
            for (int j = 0; j < c.abilities.size(); j++) { if (j > 0) b.append(','); b.append(quote(c.abilities.get(j))); }
            b.append("]}");
        }
        b.append("],\"enemies\":[");
        for (int i = 0; i < state.enemies.size(); i++) {
            if (i > 0) b.append(','); b.append("{\"characterId\":").append(quote(state.enemies.get(i))).append(",\"team\":\"enemies\"}");
        }
        b.append("],\"log\":[");
        for (int i = 0; i < state.log.size(); i++) { if (i > 0) b.append(','); b.append(quote(state.log.get(i))); }
        return b.append("]}").toString();
    }
    public synchronized String command(String token, String requestId, long revision, String action, String direction) throws IOException {
        Player actor = authenticated(state, token);
        requireRequest(requestId);
        String receipt = actor.id + ":" + requestId;
        if (state.receipts.contains(receipt)) return snapshot(token); // Retry never rerolls or moves twice.
        if (revision != state.revision) throw new IllegalArgumentException("Spielstand geändert. Bitte aktualisieren.");
        if ("confirmMovement".equals(action)) {
            if (!playing() || !state.phase.equals("enemies") || !actor.id.equals(state.helperId)) throw new IllegalArgumentException("Du wurdest nicht für diese Bewegung ausgewählt.");
            State next = copy(); next.helperId = ""; next.movementDone = true;
            addLog(next, actor.name + " hat die Gegnerbewegung auf dem Tisch bestätigt."); remember(next, receipt); commit(next); return snapshot(token);
        }
        if ("ackAttack".equals(action)) {
            if (!state.attackPending || !actor.id.equals(state.attackTarget)) throw new IllegalArgumentException("Kein Angriff zur Bestätigung.");
            State next = copy(); next.attackPending = false; remember(next, receipt); commit(next); return snapshot(token);
        }
        if (!playing() || !state.phase.equals("players") || !actor.id.equals(state.players.get(state.current).id))
            throw new IllegalArgumentException("Du bist gerade nicht am Zug.");
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
                if (x < 0 || x >= 6 || y < 0 || y >= 6) throw new IllegalArgumentException("Außerhalb des Demo-Spielfelds.");
                for (Player other : next.players) if (other != p && other.x == x && other.y == y)
                    throw new IllegalArgumentException("Dieses Feld ist belegt.");
                p.x = x; p.y = y;
                next.remaining--;
                addLog(next, p.name + " → " + cell(p) + " · noch " + next.remaining + " Felder. Figur auf dem Tisch versetzen.");
                finished = next.remaining == 0;
                break;
            case "roll":
                if (!next.turnStage.equals("roll")) throw new IllegalArgumentException("Du hast in diesem Zug bereits gewürfelt.");
                next.lastRoll = random.nextInt(6) + 1;
                next.turnRoll = next.lastRoll; next.remaining = next.turnRoll;
                next.turnStage = next.turnRoll >= 5 ? "choice" : "move";
                addLog(next, p.name + " würfelt " + next.turnRoll + (next.turnRoll >= 5 ? ": Bewegung oder Aktion wählen." : ": genau so viele Felder ziehen."));
                break;
            case "chooseMove":
                if (!next.turnStage.equals("choice")) throw new IllegalArgumentException("Keine Bewegungswahl offen.");
                next.turnStage = "move";
                addLog(next, p.name + " wählt Bewegung um genau " + next.turnRoll + " Felder.");
                break;
            case "action":
                if (!next.turnStage.equals("choice") || next.turnRoll < 5) throw new IllegalArgumentException("Eine Aktion steht nur statt Bewegung bei einer 5 oder 6 zur Wahl.");
                addLog(next, p.name + " wählt eine Charakteraktion (Techniktest: Auswahl gespeichert, Wirkung noch nicht berechnet).");
                finished = true;
                break;
            default: throw new IllegalArgumentException("Unbekannte Aktion.");
        }
        remember(next, receipt);
        if (finished) endPlayerTurn(next);
        commit(next);
        return snapshot(token);
    }
    private static void endPlayerTurn(State next) {
        next.turnStage = "roll"; next.turnRoll = 0; next.remaining = 0;
        next.current = (next.current + 1) % next.players.size();
        if (next.current == 0) {
            if (next.enemies.isEmpty()) next.round++;
            else { next.phase = "enemies"; next.enemyIndex = 0; next.movementDone = false; }
        }
    }
    private static String cell(Player p) { return "" + (char)('A' + p.x) + (p.y + 1); }
    public synchronized int selectedCount() {
        int n = 0; for (Player p : state.players) if (!p.characterId.isEmpty()) n++; return n;
    }
    public synchronized boolean readyForPlay() { return !state.players.isEmpty() && selectedCount() == state.players.size(); }
    public synchronized boolean playing() { return state.started && readyForPlay(); }
    private String playerName(String id) { for (Player p : state.players) if (p.id.equals(id)) return p.name; return "Spieler"; }
    public synchronized List<String[]> playersForHost() {
        List<String[]> result = new ArrayList<>();
        for (Player p : state.players) result.add(new String[]{p.id, p.name, characterName(p.characterId)});
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
        addLog(next, characterName(next.attackEnemy) + " → " + playerName(targetId) + ": " + text); commit(next);
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
        if (playing()) throw new IllegalArgumentException("Die Charakterauswahl ist nach Spielstart geschlossen.");
        if (state.enemies.contains(characterId)) throw new IllegalArgumentException("Diese Figur ist bereits als Gegner hinzugefügt.");
        for (Player p : state.players) if (!p.id.equals(actor.id) && p.characterId.equals(characterId))
            throw new IllegalArgumentException("Diese Figur wurde gerade von einem anderen Spieler gewählt.");
        if (actor.characterId.equals(characterId)) return snapshot(token);
        State next = copy();
        for (Player p : next.players) if (p.id.equals(actor.id)) p.characterId = characterId;
        remember(next, receipt);
        addLog(next, actor.name + " wählt " + characterName(characterId) + " · Partner.");
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
        throw new SecurityException("Bitte mit Beitrittscode anmelden.");
    }
    private static void addLog(State s, String text) {
        s.log.add(text); while (s.log.size() > 60) s.log.remove(0);
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
        out.flush(); return bytes.toByteArray();
    }
    private static State decode(byte[] bytes) throws IOException {
        if (bytes.length > 262144) throw new IOException("Spielstand zu groß.");
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
            if (p.x < 0 || p.x >= 6 || p.y < 0 || p.y >= 6) throw new IOException("Ungültige Position.");
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
        boolean validTurn = s.turnStage.equals("roll") && s.turnRoll == 0 && s.remaining == 0 ||
            s.turnStage.equals("choice") && s.turnRoll >= 5 && s.turnRoll <= 6 && s.remaining == s.turnRoll ||
            s.turnStage.equals("move") && s.turnRoll >= 1 && s.turnRoll <= 6 && s.remaining >= 1 && s.remaining <= s.turnRoll;
        if (!validTurn || s.phase.equals("enemies") && !s.turnStage.equals("roll")) throw new IOException("Ungültiger Würfel-/Bewegungszustand.");
        Set<String> playerIds = new HashSet<>(); for (Player p : s.players) playerIds.add(p.id);
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
