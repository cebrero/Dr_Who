package de.cebrero.drwho.core;

import java.io.*;
import java.security.SecureRandom;
import java.util.*;

/** Authoritative, deliberately small technical demo; no final character rules yet. */
public final class GameEngine {
    public interface Store { byte[] load() throws IOException; void save(byte[] bytes) throws IOException; }
    private static final int FORMAT = 1, MAGIC = 0x4457484f;
    private final Store store;
    private final SecureRandom random;
    private State state;

    private static final class Player {
        String id, token, name, joinRequest;
        int x, y;
    }
    private static final class State {
        String id, code;
        boolean started;
        int round = 1, current, lastRoll;
        long revision;
        List<Player> players = new ArrayList<>();
        List<String> log = new ArrayList<>();
        LinkedHashSet<String> receipts = new LinkedHashSet<>();
    }

    public GameEngine(Store store) throws IOException { this(store, new SecureRandom()); }
    GameEngine(Store store, SecureRandom random) throws IOException {
        this.store = store;
        this.random = random;
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
    public synchronized String hostText() {
        StringBuilder b = new StringBuilder(state.started ? "Runde " + state.round : "Lobby");
        b.append(" · ").append(state.players.size()).append(" / 4 Spieler\n");
        for (Player p : state.players) b.append(p.name).append(" · ").append(cell(p)).append('\n');
        if (state.started) b.append("Am Zug: ").append(state.players.get(state.current).name).append('\n');
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
        if (state.started) return;
        if (state.players.isEmpty()) throw new IllegalArgumentException("Zuerst mindestens einen Spieler anmelden.");
        State next = copy(); next.started = true;
        addLog(next, "Technikdemo gestartet: pro Zug eine Bewegung ODER ein W6-Test ODER passen.");
        commit(next);
    }
    public synchronized String snapshot(String token) {
        Player self = authenticated(state, token);
        StringBuilder b = new StringBuilder("{\"sessionId\":" + quote(state.id));
        b.append(",\"revision\":").append(state.revision).append(",\"started\":").append(state.started)
            .append(",\"round\":").append(state.round).append(",\"lastRoll\":").append(state.lastRoll)
            .append(",\"you\":").append(quote(self.id)).append(",\"current\":")
            .append(state.started ? quote(state.players.get(state.current).id) : "null").append(",\"players\":[");
        for (int i = 0; i < state.players.size(); i++) {
            Player p = state.players.get(i); if (i > 0) b.append(',');
            b.append("{\"id\":").append(quote(p.id)).append(",\"name\":").append(quote(p.name))
                .append(",\"x\":").append(p.x).append(",\"y\":").append(p.y).append('}');
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
        if (!state.started || !actor.id.equals(state.players.get(state.current).id))
            throw new IllegalArgumentException("Du bist gerade nicht am Zug.");
        State next = copy(); Player p = next.players.get(next.current);
        switch (action == null ? "" : action) {
            case "move":
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
                addLog(next, p.name + " → " + cell(p) + ". Figur auf dem Tisch entsprechend versetzen.");
                break;
            case "roll":
                next.lastRoll = random.nextInt(6) + 1;
                addLog(next, p.name + " würfelt " + next.lastRoll + " (W6-Techniktest, noch keine Ereignistabelle).");
                break;
            case "pass": addLog(next, p.name + " passt."); break;
            default: throw new IllegalArgumentException("Unbekannte Aktion.");
        }
        next.receipts.add(receipt);
        while (next.receipts.size() > 128) next.receipts.remove(next.receipts.iterator().next());
        next.current = (next.current + 1) % next.players.size();
        if (next.current == 0) next.round++;
        commit(next);
        return snapshot(token);
    }
    private static String cell(Player p) { return "" + (char)('A' + p.x) + (p.y + 1); }
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
        }
        out.writeInt(s.log.size()); for (String entry : s.log) out.writeUTF(entry);
        out.writeInt(s.receipts.size()); for (String id : s.receipts) out.writeUTF(id);
        out.flush(); return bytes.toByteArray();
    }
    private static State decode(byte[] bytes) throws IOException {
        if (bytes.length > 262144) throw new IOException("Spielstand zu groß.");
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
        if (in.readInt() != MAGIC || in.readInt() != FORMAT) throw new IOException("Unbekanntes Spielstandformat.");
        State s = new State(); s.id = in.readUTF(); s.code = in.readUTF(); s.started = in.readBoolean();
        s.round = in.readInt(); s.current = in.readInt(); s.lastRoll = in.readInt(); s.revision = in.readLong();
        int n = count(in, 4);
        for (int i = 0; i < n; i++) {
            Player p = new Player(); p.id = in.readUTF(); p.token = in.readUTF(); p.name = in.readUTF();
            p.joinRequest = in.readUTF(); p.x = in.readInt(); p.y = in.readInt();
            if (p.x < 0 || p.x >= 6 || p.y < 0 || p.y >= 6) throw new IOException("Ungültige Position.");
            s.players.add(p);
        }
        int logs = count(in, 60); for (int i = 0; i < logs; i++) s.log.add(in.readUTF());
        int receipts = count(in, 128); for (int i = 0; i < receipts; i++) s.receipts.add(in.readUTF());
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
