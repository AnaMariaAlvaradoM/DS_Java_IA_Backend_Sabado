package com.devsenior.atiende.compartido;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;

public final class DebugLog {

    private static final HttpClient HTTP = HttpClient.newHttpClient();
    private static final Path[] RUTAS = {
            Path.of("C:/Users/anaa_/Desktop/EstudiantesDevSenior/JavaIASabados/debug-16c1de.log"),
            Path.of(System.getProperty("user.dir"), "debug-16c1de.log")
    };

    private DebugLog() {
    }

    public static void escribir(String hypothesisId, String location, String message, String dataJson) {
        // #region agent log
        String linea = "{\"sessionId\":\"16c1de\",\"hypothesisId\":\"" + hypothesisId
                + "\",\"location\":\"" + location + "\",\"message\":\"" + message
                + "\",\"data\":" + dataJson + ",\"timestamp\":" + Instant.now().toEpochMilli() + "}";
        System.out.println("DEBUG-16c1de " + linea);
        for (Path ruta : RUTAS) {
            try {
                Files.writeString(ruta, linea + "\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (Exception ex) {
                System.err.println("DEBUG-16c1de write fail " + ruta + " " + ex.getMessage());
            }
        }
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:7425/ingest/05425bb5-4a2a-4aa3-a245-4051b92e014b"))
                    .header("Content-Type", "application/json")
                    .header("X-Debug-Session-Id", "16c1de")
                    .POST(HttpRequest.BodyPublishers.ofString(linea))
                    .build();
            HTTP.send(req, HttpResponse.BodyHandlers.discarding());
        } catch (Exception ignored) {
        }
        // #endregion
    }
}
