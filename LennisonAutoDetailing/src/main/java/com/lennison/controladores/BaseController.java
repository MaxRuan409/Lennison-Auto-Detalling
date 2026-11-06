package com.lennison.controladores;

import com.lennison.config.SessionData;
import com.lennison.config.SessionManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase base para todos los controladores de la aplicación (Abstracción y Herencia).
 * Provee herramientas comunes para manejar peticiones HTTP sin librerías externas.
 */
public abstract class BaseController implements HttpHandler {

    /**
     * Parsea el cuerpo de una petición POST enviada por un formulario HTML (application/x-www-form-urlencoded).
     */
    protected Map<String, String> parseFormBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> params = new HashMap<>();
        if (body.isEmpty()) return params;
        
        for (String pair : body.split("&")) {
            String[] parts = pair.split("=", 2);
            if (parts.length > 0) {
                String key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
                String value = parts.length > 1 ? URLDecoder.decode(parts[1], StandardCharsets.UTF_8) : "";
                params.put(key, value);
            }
        }
        return params;
    }

    /**
     * Obtiene el ID de sesión (SESSIONID) desde la cabecera Cookie de la petición.
     */
    protected String getSessionId(HttpExchange exchange) {
        String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");
        if (cookieHeader != null) {
            for (String cookie : cookieHeader.split(";")) {
                String[] parts = cookie.trim().split("=");
                if (parts.length == 2 && parts[0].equals("SESSIONID")) {
                    return parts[1];
                }
            }
        }
        return null;
    }

    /**
     * Recupera los datos de la sesión del usuario actual.
     */
    protected SessionData obtenerSesion(HttpExchange exchange) {
        return SessionManager.obtenerSesion(getSessionId(exchange));
    }

    /**
     * Realiza una redirección HTTP (Status 302) a la URL especificada.
     */
    protected void redirect(HttpExchange exchange, String url) throws IOException {
        exchange.getResponseHeaders().set("Location", url);
        exchange.sendResponseHeaders(302, -1);
    }

    /**
     * Envía una respuesta HTML de vuelta al cliente.
     */
    protected void sendHtml(HttpExchange exchange, String html) throws IOException {
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    /**
     * Escribe la cookie de sesión en las cabeceras HTTP del cliente.
     */
    protected void setSessionCookie(HttpExchange exchange, String sessionId) {
        if (sessionId == null) {
            // Eliminar cookie
            exchange.getResponseHeaders().set("Set-Cookie", "SESSIONID=; Path=/; Max-Age=0; HttpOnly");
        } else {
            // Configurar cookie de sesión
            exchange.getResponseHeaders().set("Set-Cookie", "SESSIONID=" + sessionId + "; Path=/; HttpOnly");
        }
    }

    /**
     * Verifica si hay una sesión activa, si no la hay, redirige al Login.
     */
    protected SessionData verificarSesion(HttpExchange exchange) throws IOException {
        SessionData sd = obtenerSesion(exchange);
        if (sd == null) {
            redirect(exchange, "/login");
            return null;
        }
        return sd;
    }

    /**
     * Verifica si la sesión es de Administrador. Si no, redirige correspondientemente.
     */
    protected SessionData verificarAdmin(HttpExchange exchange) throws IOException {
        SessionData sd = verificarSesion(exchange);
        if (sd == null) return null;
        
        if (!"ADMIN".equalsIgnoreCase(sd.getUsuario().getRol())) {
            redirect(exchange, "/inicio-usuario");
            return null;
        }
        return sd;
    }
}
