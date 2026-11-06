package com.lennison.config;

import com.lennison.modelos.Usuario;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Gestor en memoria de las sesiones de los usuarios.
 * Reemplaza la necesidad de usar localStorage o cookies complejas con JS.
 */
public class SessionManager {
    
    // Asocia un ID de sesión (UUID) con los datos de sesión en memoria
    private static final Map<String, SessionData> sesiones = new HashMap<>();

    /**
     * Crea una nueva sesión y devuelve su ID único.
     */
    public static String crearSesion(Usuario usuario) {
        String sessionId = UUID.randomUUID().toString();
        sesiones.put(sessionId, new SessionData(usuario));
        return sessionId;
    }

    /**
     * Recupera los datos de la sesión activa a partir de su ID.
     */
    public static SessionData obtenerSesion(String sessionId) {
        if (sessionId == null) return null;
        return sesiones.get(sessionId);
    }

    /**
     * Elimina la sesión especificada.
     */
    public static void cerrarSesion(String sessionId) {
        if (sessionId != null) {
            sesiones.remove(sessionId);
        }
    }
}
