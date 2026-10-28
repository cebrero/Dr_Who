package de.cebrero.drwho.core;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

/** Small HTTP/1.1 demo server. No remote administration and no external assets. */
public final class LocalServer implements Closeable {
    public interface Portraits { byte[] load(String filename) throws IOException; }
    private final GameEngine engine;
    private final byte[] page;
    private final Map<String,byte[]> boardAssets=new HashMap<>();
    private final Portraits portraits;
    private final String appVersion;
    private final ServerSocket listener;
    private final ThreadPoolExecutor workers = new ThreadPoolExecutor(4, 4, 0, TimeUnit.SECONDS,
        new ArrayBlockingQueue<>(16), r -> { Thread t = new Thread(r, "drwho-client"); t.setDaemon(true); return t; });
    private final Map<String, long[]> failures = new LinkedHashMap<>();
    private final Set<Socket> sockets = ConcurrentHashMap.newKeySet();
    private volatile boolean closed;
    private volatile String localHostname = ConnectionLinks.HOSTNAME;
    public void setLocalHostname(String hostname) {
        String value = hostname == null ? "" : hostname.toLowerCase(Locale.ROOT);
        if (!value.matches("[a-z0-9-]+\\.local")) throw new IllegalArgumentException("Ungültiger lokaler Hostname.");
        localHostname = value;
    }

    public LocalServer(GameEngine engine, byte[] page, int port) throws IOException {
        this(engine, page, port, id -> null);
    }
    public LocalServer(GameEngine engine, byte[] page, int port, Portraits portraits) throws IOException {
        this(engine,page,port,portraits,"dev");
    }
    public LocalServer(GameEngine engine, byte[] page, int port, Portraits portraits, String appVersion) throws IOException {
        this(engine,page,port,portraits,appVersion,Collections.emptyMap());
    }
    public LocalServer(GameEngine engine,byte[] page,int port,Portraits portraits,String appVersion,Map<String,byte[]> assets)throws IOException{
        for(Map.Entry<String,byte[]> asset:assets.entrySet()){
            if(!Arrays.asList("/board-art.js","/board-art.css","/character-dossiers.js","/character-dossiers.css","/room-library.js","/room-library.css").contains(asset.getKey()))throw new IllegalArgumentException("Unbekanntes Seiten-Asset.");
            boardAssets.put(asset.getKey(),asset.getValue().clone());
        }
        this.appVersion = appVersion;
        this.engine = engine; this.page = page.clone();
        this.portraits = portraits;
        listener = new ServerSocket(); listener.setReuseAddress(true);
        try { listener.bind(new InetSocketAddress("0.0.0.0", port), 16); }
        catch (IOException error) { listener.close(); workers.shutdownNow(); throw error; }
        Thread acceptor = new Thread(this::accept, "drwho-listener"); acceptor.setDaemon(true); acceptor.start();
    }
    public int port() { return listener.getLocalPort(); }
    private void accept() {
        while (!closed) {
            try {
                Socket socket = listener.accept(); socket.setSoTimeout(4000); sockets.add(socket);
                try { workers.execute(() -> serve(socket)); }
                catch (RejectedExecutionException busy) { sockets.remove(socket); socket.close(); }
            } catch (IOException e) { if (!closed) close(); }
        }
    }
    private void serve(Socket socket) {
        try (Socket client = socket) {
            InputStream in = client.getInputStream(); OutputStream out = client.getOutputStream();
            try {
                String request = line(in, 2048);
                String[] parts = request.split(" ");
                if (parts.length != 3 || !parts[2].equals("HTTP/1.1")) throw new IllegalArgumentException("Ungültige HTTP-Anfrage.");
                Map<String, String> headers = new HashMap<>(); int bytes = request.length();
                for (;;) {
                    String h = line(in, 2048); bytes += h.length();
                    if (bytes > 8192) throw new IllegalArgumentException("Header zu groß.");
                    if (h.isEmpty()) break;
                    int colon = h.indexOf(':'); if (colon < 1) throw new IllegalArgumentException("Ungültiger Header.");
                    String key = h.substring(0, colon).toLowerCase(Locale.ROOT);
                    if (headers.put(key, h.substring(colon + 1).trim()) != null) throw new IllegalArgumentException("Doppelter Header.");
                }
                String host = headers.getOrDefault("host", "");
                // Numeric addresses or our exact advertised local hostname, never arbitrary DNS names.
                String hostname = host.replaceFirst(":[0-9]{1,5}$", "").toLowerCase(Locale.ROOT);
                if (!host.matches("(?:[0-9]{1,3}\\.){3}[0-9]{1,3}(?::[0-9]{1,5})?") &&
                    !(hostname.equals(localHostname) && host.matches("(?i)[a-z0-9-]+\\.local(?::[0-9]{1,5})?")))
                    throw new IllegalArgumentException("Bitte die am Portal angezeigte Adresse öffnen.");
                if (headers.containsKey("transfer-encoding")) throw new IllegalArgumentException("Transfer-Encoding nicht unterstützt.");
                String origin = headers.get("origin");
                if (origin != null && !origin.equals("http://" + host)) throw new SecurityException("Fremder Ursprung.");
                String method = parts[0], path = parts[1];
                if (method.equals("GET") && (path.equals("/") || path.matches("/\\?code=[0-9]{6}"))) { reply(out, 200, "text/html; charset=utf-8", page); return; }
                if(method.equals("GET")&&boardAssets.containsKey(path)){reply(out,200,path.endsWith(".js")?"application/javascript; charset=utf-8":"text/css; charset=utf-8",boardAssets.get(path));return;}
                if (method.equals("GET") && path.matches("/portraits/[a-z0-9-]{1,64}\\.(jpg|svg)")) {
                    byte[] photo = portraits.load(path.substring("/portraits/".length()));
                    if (photo == null) json(out, 404, "{\"error\":\"Foto noch nicht hinterlegt.\"}");
                    else reply(out, 200, path.endsWith(".svg") ? "image/svg+xml" : "image/jpeg", photo);
                    return;
                }
                if (method.equals("GET") && path.equals("/api/lobby")) {
                    json(out, 200, engine.lobbyStatus()); return;
                }
                if (method.equals("GET") && path.equals("/api/info")) {
                    json(out, 200, "{\"appVersion\":" + GameEngine.quote(appVersion) + ",\"protocolVersion\":9,\"sessionId\":" + GameEngine.quote(engine.sessionId()) + "}"); return;
                }
                String authorization = headers.getOrDefault("authorization", "");
                String token = authorization.startsWith("Bearer ") ? authorization.substring(7) : "";
                if (method.equals("GET") && path.equals("/api/state")) {
                    engine.snapshot(token); engine.tick(); json(out, 200, engine.snapshot(token)); return;
                }
                if(method.equals("GET")&&path.equals("/api/rooms")){json(out,200,engine.roomLibrary(token));return;}
                if (!method.equals("POST") || !path.equals("/api/join") && !path.equals("/api/command") && !path.equals("/api/character")) {
                    json(out, 404, "{\"error\":\"Nicht gefunden.\"}"); return;
                }
                if (!"1".equals(headers.get("x-dr-who"))) throw new SecurityException("Spieler-Anfrage erforderlich.");
                if (!headers.getOrDefault("content-type", "").startsWith("application/x-www-form-urlencoded"))
                    throw new IllegalArgumentException("Ungültiges Datenformat.");
                int size = Integer.parseInt(headers.getOrDefault("content-length", "-1"));
                if (size < 0 || size > 4096) throw new IllegalArgumentException("Anfrage zu groß oder unvollständig.");
                byte[] body = new byte[size]; new DataInputStream(in).readFully(body);
                Map<String, String> form = form(new String(body, StandardCharsets.UTF_8));
                if (path.equals("/api/join")) {
                    String ip = client.getInetAddress().getHostAddress();
                    if (limited(ip)) { json(out, 429, "{\"error\":\"Zu viele Anmeldeversuche. Eine Minute warten.\"}"); return; }
                    try { json(out, 200, (form.containsKey("code")?engine.join(form.get("name"),form.get("code"),form.get("requestId")):engine.joinLocal(form.get("name"),form.get("requestId")))); }
                    catch (IllegalArgumentException e) { failed(ip); throw e; }
                } else if (path.equals("/api/character")) json(out, 200, engine.chooseCharacter(token, form.get("requestId"), form.get("characterId")));
                else json(out, 200, engine.command(token, form.get("requestId"), Long.parseLong(form.getOrDefault("revision", "-1")), form.get("action"), form.get("direction")));
            } catch (SecurityException e) { json(out, 401, "{\"error\":" + GameEngine.quote(e.getMessage()) + "}"); }
            catch (GameEngine.InvalidPortalCodeException e) { json(out, 400, "{\"error\":\"Portalcode falsch.\",\"errorCode\":\"invalid_portal_code\"}"); }
            catch (IllegalArgumentException e) { json(out, 400, "{\"error\":" + GameEngine.quote(e.getMessage() == null ? "Ungültige Eingabe." : e.getMessage()) + "}"); }
            catch (IOException e) { json(out, 503, "{\"error\":\"Speichern oder Übertragung fehlgeschlagen. Gleiche Anfrage erneut senden.\"}"); }
        } catch (IOException ignored) { /* A disconnect never rolls back an already committed action. */ }
        finally { sockets.remove(socket); }
    }
    private synchronized boolean limited(String ip) {
        long[] entry = failures.get(ip);
        return entry != null && System.currentTimeMillis() - entry[0] < 60_000 && entry[1] >= 8;
    }
    private synchronized void failed(String ip) {
        long now = System.currentTimeMillis(); long[] entry = failures.get(ip);
        if (entry == null || now - entry[0] >= 60_000) { entry = new long[]{now, 0}; failures.put(ip, entry); }
        entry[1]++;
        if (failures.size() > 128) failures.remove(failures.keySet().iterator().next());
    }
    private static String line(InputStream in, int limit) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream(); int previous = -1;
        for (int c; (c = in.read()) >= 0;) {
            if (previous == '\r' && c == '\n') {
                byte[] bytes = b.toByteArray(); return new String(bytes, 0, bytes.length - 1, StandardCharsets.US_ASCII);
            }
            if (b.size() >= limit) throw new IOException("HTTP-Zeile zu lang.");
            b.write(c); previous = c;
        }
        throw new EOFException("Verbindung beendet.");
    }
    private static Map<String, String> form(String body) throws UnsupportedEncodingException {
        Map<String, String> result = new HashMap<>();
        for (String pair : body.split("&")) {
            String[] kv = pair.split("=", 2);
            String key = URLDecoder.decode(kv[0], "UTF-8");
            if (result.put(key, kv.length == 2 ? URLDecoder.decode(kv[1], "UTF-8") : "") != null)
                throw new IllegalArgumentException("Doppeltes Eingabefeld.");
        }
        return result;
    }
    private static void json(OutputStream out, int code, String value) throws IOException {
        reply(out, code, "application/json; charset=utf-8", value.getBytes(StandardCharsets.UTF_8));
    }
    private static void reply(OutputStream out, int code, String type, byte[] bytes) throws IOException {
        String status;
        switch (code) { case 200: status = "OK"; break; case 400: status = "Bad Request"; break;
            case 401: status = "Unauthorized"; break; case 429: status = "Too Many Requests"; break;
            case 503: status = "Service Unavailable"; break; default: status = "Not Found"; }
        String headers = "HTTP/1.1 " + code + " " + status + "\r\nContent-Type: " + type +
            "\r\nContent-Length: " + bytes.length + "\r\nCache-Control: no-store\r\nConnection: close" +
            "\r\nX-Content-Type-Options: nosniff\r\nReferrer-Policy: no-referrer" +
            "\r\nContent-Security-Policy: default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; connect-src 'self'; frame-ancestors 'none'; form-action 'self'\r\n\r\n";
        out.write(headers.getBytes(StandardCharsets.US_ASCII)); out.write(bytes); out.flush();
    }
    @Override public void close() {
        closed = true; try { listener.close(); } catch (IOException ignored) { }
        workers.shutdownNow();
        for (Socket socket : sockets) try { socket.close(); } catch (IOException ignored) { }
        sockets.clear();
    }
}
