package com.lennison.controladores;

import com.lennison.config.HashUtil;
import com.lennison.dao.UsuarioDAO;
import com.lennison.modelos.Usuario;
import com.lennison.vistas.Vista;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para la recuperación de contraseñas.
 */
public class RecuperarClaveController extends BaseController {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (method.equalsIgnoreCase("GET")) {
            Map<String, String> marcadores = new HashMap<>();
            marcadores.put("MENSAJE_ALERTA", "");
            String html = Vista.render("recuperar-clave.html", marcadores);
            sendHtml(exchange, html);

        } else if (method.equalsIgnoreCase("POST")) {
            Map<String, String> params = parseFormBody(exchange);
            String correo = params.get("correo");
            String nuevaClave = params.get("nuevaClave");
            String confirmarClave = params.get("confirmarClave");

            String errorMsg = null;
            String successMsg = null;

            if (correo == null || correo.trim().isEmpty() ||
                nuevaClave == null || nuevaClave.isEmpty() ||
                confirmarClave == null || confirmarClave.isEmpty()) {
                errorMsg = "Todos los campos son obligatorios.";
            } else if (nuevaClave.length() < 6) {
                errorMsg = "La nueva contraseña debe tener al menos 6 caracteres.";
            } else if (!nuevaClave.equals(confirmarClave)) {
                errorMsg = "Las contraseñas no coinciden.";
            } else {
                Usuario u = usuarioDAO.buscarPorCorreo(correo);
                if (u == null) {
                    errorMsg = "El correo electrónico ingresado no está registrado.";
                } else {
                    boolean actualizado = usuarioDAO.actualizarContrasena(u.getId(), HashUtil.hashear(nuevaClave));
                    if (actualizado) {
                        successMsg = "Contraseña restablecida con éxito. Ya puedes iniciar sesión.";
                    } else {
                        errorMsg = "Ocurrió un error al actualizar la contraseña.";
                    }
                }
            }

            Map<String, String> marcadores = new HashMap<>();
            if (errorMsg != null) {
                marcadores.put("MENSAJE_ALERTA", 
                    "<div style='border: 1px solid #8B0000; border-left: 5px solid #8B0000; background-color: #1a0a0a; color: #ff5555; padding: 12px; border-radius: 8px; margin-bottom: 20px; font-size: 0.9rem; text-align: center;'>" +
                    errorMsg +
                    "</div>"
                );
            } else {
                marcadores.put("MENSAJE_ALERTA", 
                    "<div style='border: 2px solid #008000; background-color: #0d1a0d; color: #55ff55; padding: 12px; border-radius: 8px; margin-bottom: 20px; font-size: 0.9rem; text-align: center;'>" +
                    successMsg +
                    "</div>"
                );
            }

            String html = Vista.render("recuperar-clave.html", marcadores);
            sendHtml(exchange, html);
        }
    }
}
