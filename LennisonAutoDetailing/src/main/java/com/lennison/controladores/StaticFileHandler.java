package com.lennison.controladores;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * Servidor de archivos estáticos (imágenes, CSS, etc.) bajo la carpeta 'web/'.
 * Bloquea el acceso directo a archivos .html redirigiendo a los controladores MVC.
 */
public class StaticFileHandler implements HttpHandler {

    private static final String WEB_ROOT = "./web";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        if (!method.equalsIgnoreCase("GET")) {
            enviarRespuestaError(exchange, 405, "Método no permitido.");
            return;
        }

        // Si entran a la raíz '/', redirigir al Login
        if (path.equals("/") || path.equals("/index.html")) {
            exchange.getResponseHeaders().set("Location", "/login");
            exchange.sendResponseHeaders(302, -1);
            return;
        }

        // Si intentan entrar a un archivo .html directamente, redirigir a su ruta sin extensión (MVC)
        if (path.endsWith(".html")) {
            String rutaMvc = path.substring(0, path.length() - 5);
            exchange.getResponseHeaders().set("Location", rutaMvc);
            exchange.sendResponseHeaders(302, -1);
            return;
        }

        // Construir la ruta al recurso físico
        File archivo = new File(WEB_ROOT + path);

        if (!archivo.exists() || archivo.isDirectory()) {
            enviarRespuestaError(exchange, 404, "Recurso no encontrado.");
            return;
        }

        // Configurar tipo MIME y enviar bytes del archivo
        String contentType = getContentType(path);
        exchange.getResponseHeaders().set("Content-Type", contentType);

        byte[] bytesArchivo = Files.readAllBytes(archivo.toPath());
        exchange.sendResponseHeaders(200, bytesArchivo.length);
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytesArchivo);
        }
    }

    private String getContentType(String path) {
        if (path.endsWith(".css")) return "text/css; charset=UTF-8";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".ico")) return "image/x-icon";
        if (path.endsWith(".json")) return "application/json; charset=UTF-8";
        return "application/octet-stream";
    }

    private void enviarRespuestaError(HttpExchange exchange, int statusCode, String mensaje) throws IOException {
        byte[] bytes = ("<h1>Error " + statusCode + "</h1><p>" + mensaje + "</p>").getBytes();
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
