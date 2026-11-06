package com.lennison.controladores;

import com.lennison.config.HashUtil;
import com.lennison.config.SessionManager;
import com.lennison.dao.UsuarioDAO;
import com.lennison.modelos.Usuario;
import com.lennison.vistas.Vista;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador de Inicio de Sesión.
 */
public class LoginController extends BaseController {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (method.equalsIgnoreCase("GET")) {
            // Si ya tiene sesión activa, redirigir al panel correspondiente
            if (obtenerSesion(exchange) != null) {
                String rol = obtenerSesion(exchange).getUsuario().getRol();
                if ("ADMIN".equalsIgnoreCase(rol)) {
                    redirect(exchange, "/admin");
                } else {
                    redirect(exchange, "/inicio-usuario");
                }
                return;
            }

            Map<String, String> marcadores = new HashMap<>();
            marcadores.put("MENSAJE_ERROR", "");
            String html = Vista.render("login.html", marcadores);
            sendHtml(exchange, html);

        } else if (method.equalsIgnoreCase("POST")) {
            Map<String, String> params = parseFormBody(exchange);
            String correo = params.get("correo");
            String clave = params.get("clave");

            Usuario usuario = usuarioDAO.buscarPorCorreo(correo);
            if (usuario != null) {
                String claveHasheada = HashUtil.hashear(clave);
                if (usuario.getContrasenaHash().equals(claveHasheada)) {
                    // Crear sesión en memoria del servidor
                    String sessionId = SessionManager.crearSesion(usuario);
                    setSessionCookie(exchange, sessionId);

                    if ("ADMIN".equalsIgnoreCase(usuario.getRol())) {
                        redirect(exchange, "/admin");
                    } else {
                        redirect(exchange, "/inicio-usuario");
                    }
                    return;
                }
            }

            // Si falla la autenticación, renderizar nuevamente con mensaje de error
            Map<String, String> marcadores = new HashMap<>();
            marcadores.put("MENSAJE_ERROR", 
                "<div style='border: 1px solid #8B0000; border-left: 5px solid #8B0000; background-color: #1a0a0a; color: #ff5555; padding: 12px; border-radius: 8px; margin-bottom: 20px; font-size: 0.9rem; text-align: center;'>" +
                "Correo o contraseña incorrectos. Por favor, intenta de nuevo." +
                "</div>"
            );
            String html = Vista.render("login.html", marcadores);
            sendHtml(exchange, html);
        }
    }
}
