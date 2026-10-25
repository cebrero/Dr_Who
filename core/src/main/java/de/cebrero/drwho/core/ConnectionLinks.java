package de.cebrero.drwho.core;

/** Connection strings shared by the portal and its QR codes. */
public final class ConnectionLinks {
    public static final String SSID = "Dr_Who", PASSWORD = "drwho123", HOSTNAME = "drwho.local";
    private ConnectionLinks() { }
    public static String site(String host, int port) {
        if (host == null || !host.matches("(?:[0-9]{1,3}\\.){3}[0-9]{1,3}|[a-z0-9-]+\\.local") || port < 1 || port > 65535)
            throw new IllegalArgumentException("Ungültige lokale Adresse.");
        return "http://" + host + (port == 80 ? "" : ":" + port);
    }
    public static String join(String host, int port, String code) {
        if (code == null || !code.matches("[0-9]{6}")) throw new IllegalArgumentException("Ungültiger Beitrittscode.");
        return site(host, port) + "/?code=" + code;
    }
    public static String wifiQr(String ssid, String password) {
        if (ssid == null || ssid.isEmpty() || password == null || password.isEmpty()) throw new IllegalArgumentException("WLAN noch nicht bereit.");
        return "WIFI:T:WPA;S:" + escape(ssid) + ";P:" + escape(password) + ";;";
    }
    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace(";", "\\;").replace(",", "\\,").replace(":", "\\:").replace("\"", "\\\"");
    }
}
