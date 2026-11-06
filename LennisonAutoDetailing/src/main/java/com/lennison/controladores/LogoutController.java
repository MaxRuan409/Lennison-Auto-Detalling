package com.lennison.controladores;

import com.lennison.config.SessionManager;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

/**
 * Controlador de Cierre de Sesión.
 */
public class LogoutController extends BaseController {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String sessionId = getSessionId(exchange);
        if (sessionId != null) {
            // Cerrar y eliminar la sesión en el servidor
            SessionManager.cerrarSesion(sessionId);
        }
        
        // Limpiar cookie en el cliente
        setSessionCookie(exchange, null);
        
        // Redirigir al Login
        redirect(exchange, "/login");
    }
}
