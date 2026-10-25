package de.cebrero.drwho;

import android.content.Context;
import android.net.wifi.WifiManager;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

/** Advertises a real hostname, rather than just a similarly named DNS-SD service. */
final class PortalNetwork implements Closeable {
    private final ExecutorService worker = Executors.newSingleThreadExecutor();
    private final WifiManager.MulticastLock lock;
    private final Consumer<String> hostnameChanged;
    private volatile JmDNS dns;
    private volatile boolean closed;
    private volatile boolean announced;
    private volatile String requested = "";
    volatile String hostname = "", address = "", status = "WLAN noch nicht verbunden";

    PortalNetwork(Context context, Consumer<String> hostnameChanged) {
        WifiManager wifi = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        lock = wifi.createMulticastLock("drwho-mdns"); lock.setReferenceCounted(false);
        this.hostnameChanged = hostnameChanged;
    }
    void update(int port) {
        Inet4Address selected = preferredAddress();
        String next = selected == null ? "" : selected.getHostAddress();
        if (closed) return;
        if (next.equals(requested)) {
            JmDNS active=dns;
            if(announced&&active!=null){
                String actual=active.getHostName().replaceFirst("\\.$", "").toLowerCase(Locale.ROOT);
                if(!actual.equals(hostname)){hostname=actual;hostnameChanged.accept(actual);}
            }
            return;
        }
        requested = next; address = next; hostname = ""; announced = false;
        status = next.isEmpty() ? "Warte auf WLAN-Adresse" : "Lokaler Name wird gestartet …";
        worker.execute(() -> {
            stopDns(); if (closed || !next.equals(requested) || selected == null) return;
            try {
                lock.acquire();
                JmDNS created = JmDNS.create(selected, "drwho");
                if (closed || !next.equals(requested)) { created.close(); releaseLock(); return; }
                dns = created;
                dns.registerService(ServiceInfo.create("_http._tcp.local.", "Dr_Who", port, "path=/"));
                hostname = dns.getHostName().replaceFirst("\\.$", "").toLowerCase(Locale.ROOT);
                hostnameChanged.accept(hostname);
                announced = true;
                status = "Name bereit · alternativ Adresse oder QR nutzen";
            } catch (IOException | RuntimeException error) {
                stopDns(); hostname = ""; status = "Direkte Adresse und Seiten-QR verwenden";
            }
        });
    }
    private void stopDns() {
        announced = false;
        if (dns != null) { try { dns.close(); } catch (IOException ignored) { } dns = null; }
        releaseLock();
    }
    private void releaseLock() { if (lock.isHeld()) lock.release(); }
    @Override public void close() {
        if (closed) return; closed = true; worker.execute(this::stopDns); worker.shutdown();
    }
    static Inet4Address preferredAddress() {
        Inet4Address best = null; int bestScore = -1;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces != null && interfaces.hasMoreElements()) {
                NetworkInterface net = interfaces.nextElement(); String name = net.getName().toLowerCase(Locale.ROOT);
                if (!net.isUp() || net.isLoopback() || name.matches(".*(tun|tap|ppp|rmnet|ccmni).*")) continue;
                int score = name.contains("ap") || name.contains("swlan") || name.startsWith("br") ? 100 : name.contains("wlan") || name.contains("wifi") ? 80 : name.contains("eth") ? 50 : 20;
                Enumeration<InetAddress> addresses = net.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (!(address instanceof Inet4Address) || !address.isSiteLocalAddress()) continue;
                    int priority = score + (address.getHostAddress().endsWith(".1") ? 15 : 0);
                    if (priority > bestScore) { best = (Inet4Address)address; bestScore = priority; }
                }
            }
        } catch (SocketException ignored) { }
        return best;
    }
}
