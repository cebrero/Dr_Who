package de.cebrero.drwho;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.net.wifi.WifiManager;
import android.os.*;
import android.util.AtomicFile;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.*;
import android.widget.*;
import de.cebrero.drwho.core.*;
import java.io.*;
import java.net.*;
import java.util.*;

/** Foreground-only prototype: keep the host app open during a session. */
public class MainActivity extends Activity {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private GameEngine engine;
    private LocalServer server;
    private WifiManager.LocalOnlyHotspotReservation reservation;
    private boolean hotspotPending, destroyed;
    private TextView connections, gameStatus, networkStatus;
    private FrameLayout root;
    private ScrollView console;
    private PortalView portal;
    private final Runnable refresh = new Runnable() {
        @Override public void run() {
            if (engine != null) {
                gameStatus.setText(engine.hostText()); connections.setText(addresses());
                portal.caption = engine.hostText().split("\n")[0];
                portal.result = engine.hostText();
                portal.waiting = !engine.playing();
                portal.updateLobby("VERBINDEN & FIGUR WÄHLEN\n\n" + networkStatus.getText() + "\n\n" + addresses() +
                    "\nBEITRITTSCODE  " + engine.code() + "\n\n" + engine.lobbyText() +
                    "\nWLAN verbinden → Webseite öffnen → Namen und Code eingeben → Figur wählen.\n\nAntippen · Spielleiter");
                portal.invalidate();
            }
            handler.postDelayed(this, 1000);
        }
    };
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        root = new FrameLayout(this); root.setBackgroundColor(Color.rgb(7,13,27));
        root.setOnApplyWindowInsetsListener((view, insets) -> {
            view.setPadding(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(),
                insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom()); return insets;
        });
        setContentView(root);
        try {
            engine = new GameEngine(new AndroidStore(new File(getFilesDir(), "session-v1.drw")));
            byte[] page;
            try (InputStream in = getAssets().open("player.html"); ByteArrayOutputStream b = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[4096]; int n;
                while ((n = in.read(buffer)) != -1) b.write(buffer, 0, n);
                page = b.toByteArray();
            }
            server = new LocalServer(engine, page, 8080, id -> {
                try (InputStream in = getAssets().open("portraits/" + id + ".jpg"); ByteArrayOutputStream bytes = new ByteArrayOutputStream()) {
                    byte[] buffer = new byte[4096]; int n;
                    while ((n = in.read(buffer)) != -1) bytes.write(buffer, 0, n);
                    return bytes.toByteArray();
                } catch (FileNotFoundException e) { return null; }
            });
        } catch (IOException e) {
            TextView error = text("Start fehlgeschlagen. Der vorhandene Spielstand wurde nicht überschrieben.\n" + e.getMessage(), 20);
            root.addView(error); return;
        }
        console = new ScrollView(this); LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL); panel.setPadding(28,24,28,24); console.addView(panel); root.addView(console);
        panel.addView(text("DR_WHO", 34));
        panel.addView(text("LOKALER SPIELLEITER · 0.3.1", 13));
        panel.addView(text("Charakterwahl & Gegnerzüge · Werte, Fotos und Kampfregeln folgen.", 15));
        panel.addView(text("BEITRITTSCODE  " + engine.code(), 28));
        networkStatus = text("Hotspot starten oder alle Handys mit demselben WLAN verbinden.", 16); panel.addView(networkStatus);
        button(panel, "Lokalen Hotspot starten", this::requestHotspot);
        button(panel, "Hotspot beenden", () -> {
            if (reservation != null) { reservation.close(); reservation = null; }
            networkStatus.setText("Hotspot beendet. Bestehender Spielstand bleibt erhalten.");
        });
        connections = text(addresses(), 18); connections.setTextIsSelectable(true); panel.addView(connections);
        panel.addView(text("Spieler: WLAN verbinden → IP-Adresse im Browser öffnen → Name und Code eingeben. Bei mehreren Adressen die WLAN-/Hotspot-Adresse verwenden. Ohne Internet im WLAN bleiben.", 15));
        gameStatus = text(engine.hostText(), 18); panel.addView(gameStatus);
        button(panel, "Übrige Figur als Gegner hinzufügen", () -> {
            java.util.List<CharacterProfile> available = engine.availableEnemies();
            if (available.isEmpty()) { toast(engine.readyForPlay() ? "Keine übrige bekannte Figur verfügbar." : "Zuerst müssen alle Spieler eine Figur wählen."); return; }
            String[] names = new String[available.size()]; for (int i = 0; i < names.length; i++) names[i] = available.get(i).name;
            new AlertDialog.Builder(this).setTitle("Gegner hinzufügen").setItems(names, (dialog, which) -> {
                try { engine.addEnemy(available.get(which).id); gameStatus.setText(engine.hostText()); }
                catch (IOException | IllegalArgumentException e) { toast(e.getMessage()); }
            }).setNegativeButton("Abbrechen", null).show();
        });
        button(panel, "Technikdemo starten / fortsetzen", () -> {
            try { engine.startDemo(); gameStatus.setText(engine.hostText()); }
            catch (IOException | IllegalArgumentException e) { toast(e.getMessage()); }
        });
        button(panel, "Portal anzeigen", () -> { console.setVisibility(View.GONE); portal.setVisibility(View.VISIBLE); });
        panel.addView(text("GEGNERPHASE · TECHNIKTEST", 16));
        panel.addView(text("Nach allen Partnern folgen hinzugefügte Gegner. Ziel, Weg und konkrete Angriffsfolgen legt vorerst der Spielleiter fest. Automatische Gegnerregeln folgen.", 15));
        button(panel, "Bewegungshelfer zufällig bestimmen", () -> {
            try { toast(engine.requestEnemyMovement() + " bewegt die Figur auf dem Tisch."); }
            catch (IOException | IllegalArgumentException e) { toast(e.getMessage()); }
        });
        button(panel, "Angriff auf Spieler anzeigen", this::attackDialog);
        button(panel, "Gegnerzug abschließen", () -> {
            try { engine.finishEnemyTurn(); gameStatus.setText(engine.hostText()); }
            catch (IOException | IllegalArgumentException e) { toast(e.getMessage()); }
        });
        panel.addView(text("Automatisches Speichern nach jeder Änderung. Host-App geöffnet lassen. Portal antippen, um hierher zurückzukehren.", 15));
        portal = new PortalView(); portal.setVisibility(View.GONE); root.addView(portal);
        portal.setOnClickListener(v -> { portal.setVisibility(View.GONE); console.setVisibility(View.VISIBLE); });
    }
    private TextView text(String value, int size) {
        TextView v = new TextView(this); v.setText(value); v.setTextSize(size);
        v.setTextColor(Color.rgb(210,235,245)); v.setPadding(0,10,0,12); return v;
    }
    private void button(LinearLayout panel, String label, Runnable action) {
        Button button = new Button(this); button.setText(label); button.setAllCaps(false);
        button.setOnClickListener(v -> action.run()); panel.addView(button);
    }
    private void toast(String message) { Toast.makeText(this, message, Toast.LENGTH_LONG).show(); }
    private void attackDialog() {
        java.util.List<String[]> players = engine.playersForHost();
        if (players.isEmpty()) { toast("Zuerst Spieler anmelden."); return; }
        LinearLayout content = new LinearLayout(this); content.setOrientation(LinearLayout.VERTICAL); content.setPadding(24,12,24,12);
        Spinner target = new Spinner(this); String[] names = new String[players.size()];
        for (int i = 0; i < names.length; i++) names[i] = players.get(i)[1] + " · " + players.get(i)[2];
        target.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, names)); content.addView(target);
        EditText outcome = new EditText(this); outcome.setHint("Was ist mit dem Charakter passiert?"); outcome.setMinLines(3);
        outcome.setFilters(new android.text.InputFilter[]{new android.text.InputFilter.LengthFilter(400)}); content.addView(outcome);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Angriffsfolge anzeigen").setView(content)
            .setPositiveButton("Beim Ziel anzeigen", null).setNegativeButton("Abbrechen", null).create();
        dialog.setOnShowListener(v -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(button -> {
            try { engine.reportAttack(players.get(target.getSelectedItemPosition())[0], outcome.getText().toString()); dialog.dismiss(); }
            catch (IOException | IllegalArgumentException e) { toast(e.getMessage()); }
        })); dialog.show();
    }
    private void requestHotspot() {
        if (reservation != null || hotspotPending) { toast("Hotspot läuft oder wird gestartet."); return; }
        String permission = Build.VERSION.SDK_INT >= 33 ? Manifest.permission.NEARBY_WIFI_DEVICES : Manifest.permission.ACCESS_FINE_LOCATION;
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(Build.VERSION.SDK_INT >= 33 ? new String[]{permission} :
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 7);
            return;
        }
        hotspotPending = true;
        try {
            WifiManager wifi = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
            wifi.startLocalOnlyHotspot(new WifiManager.LocalOnlyHotspotCallback() {
                @Override public void onStarted(WifiManager.LocalOnlyHotspotReservation r) {
                    hotspotPending = false;
                    if (destroyed) { r.close(); return; }
                    reservation = r;
                    String ssid, password;
                    if (Build.VERSION.SDK_INT >= 30) { ssid = r.getSoftApConfiguration().getSsid(); password = r.getSoftApConfiguration().getPassphrase(); }
                    else { ssid = r.getWifiConfiguration().SSID; password = r.getWifiConfiguration().preSharedKey; }
                    networkStatus.setText("WLAN: " + ssid + "\nWLAN-Passwort: " + password + "\nDanach Webseite öffnen und Beitrittscode eingeben.");
                }
                @Override public void onStopped() { hotspotPending = false; reservation = null; networkStatus.setText("Hotspot gestoppt. Zum Fortsetzen erneut starten."); }
                @Override public void onFailed(int reason) {
                    hotspotPending = false; networkStatus.setText("Hotspot nicht verfügbar (" + reason + "). Gemeinsames WLAN oder Android-Hotspot aus den Systemeinstellungen verwenden. Bei Android 8–12 ggf. Standortfunktion einschalten.");
                }
            }, handler);
        } catch (SecurityException | IllegalStateException | UnsupportedOperationException e) {
            hotspotPending = false; toast("Hotspot nicht möglich: " + e.getMessage());
        }
    }
    @Override public void onRequestPermissionsResult(int request, String[] permissions, int[] results) {
        super.onRequestPermissionsResult(request, permissions, results);
        if (request == 7) {
            String permission = Build.VERSION.SDK_INT >= 33 ? Manifest.permission.NEARBY_WIFI_DEVICES : Manifest.permission.ACCESS_FINE_LOCATION;
            if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) requestHotspot();
            else toast("Ohne Hotspot-Berechtigung bitte gemeinsames WLAN oder System-Hotspot nutzen.");
        }
    }
    private String addresses() {
        StringBuilder b = new StringBuilder("SPIELER-WEBSEITE\n");
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces != null && interfaces.hasMoreElements()) {
                NetworkInterface net = interfaces.nextElement(); if (!net.isUp()) continue;
                Enumeration<InetAddress> addresses = net.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress a = addresses.nextElement();
                    if (a instanceof Inet4Address && a.isSiteLocalAddress() && !a.isLoopbackAddress())
                        b.append("http://").append(a.getHostAddress()).append(":8080\n");
                }
            }
        } catch (SocketException e) { b.append("Adresse noch nicht verfügbar.\n"); }
        return b.toString();
    }
    @Override protected void onStart() { super.onStart(); if (engine != null && gameStatus != null) handler.post(refresh); }
    @Override protected void onStop() { handler.removeCallbacks(refresh); super.onStop(); }
    @Override protected void onDestroy() {
        destroyed = true; handler.removeCallbacks(refresh);
        if (reservation != null) reservation.close(); if (server != null) server.close(); super.onDestroy();
    }
    private static final class AndroidStore implements GameEngine.Store {
        private final AtomicFile file;
        AndroidStore(File path) { file = new AtomicFile(path); }
        @Override public byte[] load() throws IOException {
            try { return file.readFully(); } catch (FileNotFoundException e) { return null; }
        }
        @Override public void save(byte[] data) throws IOException {
            FileOutputStream out = file.startWrite();
            try { out.write(data); file.finishWrite(out); }
            catch (IOException e) { file.failWrite(out); throw e; }
        }
    }
    private final class PortalView extends View {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final int[] colors = new int[18];
        private final TextPaint lobbyPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        private StaticLayout lobbyLayout;
        private int lobbyWidth;
        String caption = "PORTAL BEREIT", result = "";
        String lobby = "Verbindung wird vorbereitet …";
        boolean waiting = true;
        PortalView() {
            super(MainActivity.this);
            for (int i = 0; i < colors.length; i++) colors[i] = Color.HSVToColor(110+i*7, new float[]{185+i*2, .75f, 1f});
            lobbyPaint.setColor(Color.rgb(190,237,249));
            lobbyPaint.setTextSize(17*getResources().getDisplayMetrics().scaledDensity);
            setContentDescription("Animiertes Portal. Antippen öffnet Spielleitersteuerung.");
        }
        void updateLobby(String value) {
            int width = Math.max(1, getWidth()-48);
            if (lobbyLayout != null && value.equals(lobby) && lobbyWidth == width) return;
            lobby = value; lobbyWidth = width;
            lobbyLayout = StaticLayout.Builder.obtain(lobby,0,lobby.length(),lobbyPaint,width)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL).setLineSpacing(4,1).build();
        }
        @Override protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
            super.onSizeChanged(width,height,oldWidth,oldHeight); updateLobby(lobby);
        }
        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas); canvas.drawColor(Color.rgb(3,7,18));
            if (waiting) {
                if (lobbyLayout == null) return;
                float scale = Math.min(1, (getHeight()-48f)/Math.max(1,lobbyLayout.getHeight()));
                canvas.save(); canvas.translate(24,24); canvas.scale(scale,scale); lobbyLayout.draw(canvas); canvas.restore();
                return;
            }
            float cx = getWidth()/2f, cy = getHeight()/2f;
            float radius = Math.min(getWidth()*.42f, getHeight()*.32f);
            float time = SystemClock.uptimeMillis()/35f;
            paint.setStyle(Paint.Style.STROKE);
            for (int i = 0; i < 18; i++) {
                float r = radius*(.16f+i*.047f); paint.setStrokeWidth(2+i*.35f);
                paint.setColor(colors[i]);
                canvas.drawArc(cx-r,cy-r,cx+r,cy+r,time*(i%2 == 0 ? 1 : -1)+i*21,245,false,paint);
            }
            paint.setStyle(Paint.Style.FILL); paint.setColor(Color.rgb(126,237,255));
            paint.setTextAlign(Paint.Align.CENTER); paint.setTextSize(23*getResources().getDisplayMetrics().scaledDensity);
            canvas.drawText("DR_WHO",cx,cy-radius-35,paint);
            paint.setTextSize(15*getResources().getDisplayMetrics().scaledDensity);
            canvas.drawText(caption,cx,cy+radius+38,paint);
            String roll = "";
            for (String line : result.split("\n")) if (line.startsWith("Letzter W6:")) roll = line;
            canvas.drawText(roll,cx,cy+10,paint);
            paint.setTextSize(12*getResources().getDisplayMetrics().scaledDensity);
            canvas.drawText("Antippen · Spielleiter",cx,getHeight()-30,paint);
            if (getWindowVisibility() == VISIBLE && getVisibility() == VISIBLE) postInvalidateDelayed(40);
        }
    }
}
