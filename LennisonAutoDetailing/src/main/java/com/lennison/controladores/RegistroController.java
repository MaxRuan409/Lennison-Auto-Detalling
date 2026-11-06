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
 * Controlador de Registro de Usuarios.
 */
public class RegistroController extends BaseController {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (method.equalsIgnoreCase("GET")) {
            Map<String, String> marcadores = new HashMap<>();
            marcadores.put("MENSAJE_REGISTRO", "");
            String html = Vista.render("registro.html", marcadores);
            sendHtml(exchange, html);

        } else if (method.equalsIgnoreCase("POST")) {
            Map<String, String> params = parseFormBody(exchange);
            String nombre = params.get("nombre");
            String telefono = params.get("telefono");
            String correo = params.get("correo");
            String clave = params.get("clave");
            String confirmarClave = params.get("confirmarClave");

            String errorMsg = null;

            if (nombre == null || nombre.trim().isEmpty() ||
                telefono == null || telefono.trim().isEmpty() ||
                correo == null || correo.trim().isEmpty() ||
                clave == null || clave.isEmpty()) {
                errorMsg = "Todos los campos son obligatorios.";
            } else if (clave.length() < 6) {
                errorMsg = "La contraseña debe tener al menos 6 caracteres.";
            } else if (!clave.equals(confirmarClave)) {
                errorMsg = "Las contraseñas no coinciden.";
            } else if (usuarioDAO.existeCorreo(correo)) {
                errorMsg = "El correo electrónico ya está registrado.";
            }

            if (errorMsg != null) {
                // Volver a mostrar el registro con el error
                Map<String, String> marcadores = new HashMap<>();
                marcadores.put("MENSAJE_REGISTRO", 
                    "<div style='border: 1px solid #8B0000; border-left: 5px solid #8B0000; background-color: #1a0a0a; color: #ff5555; padding: 12px; border-radius: 8px; margin-bottom: 20px; font-size: 0.9rem; text-align: center;'>" +
                    errorMsg +
                    "</div>"
                );
                String html = Vista.render("registro.html", marcadores);
                sendHtml(exchange, html);
                return;
            }

            // Registrar usuario
            Usuario nuevo = new Usuario();
            nuevo.setNombre(nombre);
            nuevo.setTelefono(telefono);
            nuevo.setCorreo(correo);
            nuevo.setContrasenaHash(HashUtil.hashear(clave));
            nuevo.setRol("USUARIO");

            boolean registrado = usuarioDAO.registrar(nuevo);
            if (registrado) {
                // Redirigir al login con un parámetro de éxito (opcional)
                redirect(exchange, "/login");
            } else {
                Map<String, String> marcadores = new HashMap<>();
                marcadores.put("MENSAJE_REGISTRO", 
                    "<div style='border: 1px solid #8B0000; border-left: 5px solid #8B0000; background-color: #1a0a0a; color: #ff5555; padding: 12px; border-radius: 8px; margin-bottom: 20px; font-size: 0.9rem; text-align: center;'>" +
                    "Ocurrió un error en el servidor al intentar registrar. Inténtalo de nuevo." +
                    "</div>"
                );
                String html = Vista.render("registro.html", marcadores);
                sendHtml(exchange, html);
            }
        }
    }
}
