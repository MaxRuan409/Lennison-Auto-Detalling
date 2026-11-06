package com.lennison.controladores;

import com.lennison.config.HashUtil;
import com.lennison.config.SessionData;
import com.lennison.dao.UsuarioDAO;
import com.lennison.modelos.Usuario;
import com.lennison.vistas.Vista;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador de Perfil de Usuario (Actualización de datos y contraseña).
 */
public class PerfilController extends BaseController {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        SessionData sd = verificarSesion(exchange);
        if (sd == null)
            return;

        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if (method.equalsIgnoreCase("GET") && path.equals("/perfil")) {
            mostrarPerfil(exchange, sd, null, null);
        } else if (method.equalsIgnoreCase("POST") && path.equals("/perfil/actualizar")) {
            actualizarDatos(exchange, sd);
        } else if (method.equalsIgnoreCase("POST") && path.equals("/perfil/actualizar-clave")) {
            actualizarClave(exchange, sd);
        } else {
            redirect(exchange, "/perfil");
        }
    }

    private void mostrarPerfil(HttpExchange exchange, SessionData sd, String msgPerfil, String msgClave)
            throws IOException {
        Usuario u = sd.getUsuario();

        Map<String, String> marcadores = new HashMap<>();
        marcadores.put("USUARIO_NOMBRE", u.getNombre());
        marcadores.put("USUARIO_CORREO", u.getCorreo());
        marcadores.put("USUARIO_TELEFONO", u.getTelefono());
        marcadores.put("CANTIDAD_CARRITO", String.valueOf(sd.getCarrito().size()));

        // Mensaje de éxito/error de perfil
        if (msgPerfil != null) {
            marcadores.put("MENSAJE_PERFIL", msgPerfil);
        } else {
            marcadores.put("MENSAJE_PERFIL", "");
        }

        // Mensaje de éxito/error de contraseña
        if (msgClave != null) {
            marcadores.put("MENSAJE_CLAVE", msgClave);
        } else {
            marcadores.put("MENSAJE_CLAVE", "");
        }

        String html = Vista.render("perfil.html", marcadores);
        sendHtml(exchange, html);
    }

    private void actualizarDatos(HttpExchange exchange, SessionData sd) throws IOException {
        Map<String, String> params = parseFormBody(exchange);
        String nombre = params.get("nombre");
        String correo = params.get("correo");
        String telefono = params.get("telefono");

        String msg;

        if (nombre == null || nombre.trim().isEmpty() ||
                correo == null || correo.trim().isEmpty() ||
                telefono == null || telefono.trim().isEmpty()) {
            msg = "<div style='color:#ff5555; font-size:0.9rem; font-weight:bold; margin-bottom:10px;'>Todos los campos son obligatorios.</div>";
        } else {
            Usuario u = sd.getUsuario();

            // Validar si cambia de correo y si ya existe
            if (!correo.equalsIgnoreCase(u.getCorreo()) && usuarioDAO.existeCorreo(correo)) {
                msg = "<div style='color:#ff5555; font-size:0.9rem; font-weight:bold; margin-bottom:10px;'>El correo ya está registrado por otro usuario.</div>";
            } else {
                boolean actualizado = usuarioDAO.actualizarPerfil(u.getId(), nombre, telefono, correo);
                if (actualizado) {
                    // Actualizar el objeto usuario en la sesión del servidor
                    u.setNombre(nombre);
                    u.setCorreo(correo);
                    u.setTelefono(telefono);
                    msg = "<div style='color:#55ff55; font-size:0.9rem; font-weight:bold; margin-bottom:10px;'>Datos actualizados con éxito.</div>";
                } else {
                    msg = "<div style='color:#ff5555; font-size:0.9rem; font-weight:bold; margin-bottom:10px;'>Error interno al actualizar datos.</div>";
                }
            }
        }

        mostrarPerfil(exchange, sd, msg, null);
    }

    private void actualizarClave(HttpExchange exchange, SessionData sd) throws IOException {
        Map<String, String> params = parseFormBody(exchange);
        String claveActual = params.get("claveActual");
        String nuevaClave = params.get("nuevaClave");
        String confirmarClave = params.get("confirmarClave");

        String msg;

        if (claveActual == null || claveActual.isEmpty() ||
                nuevaClave == null || nuevaClave.isEmpty() ||
                confirmarClave == null || confirmarClave.isEmpty()) {
            msg = "<div style='color:#ff5555; font-size:0.9rem; font-weight:bold; margin-bottom:10px;'>Todos los campos son obligatorios.</div>";
        } else if (nuevaClave.length() < 6) {
            msg = "<div style='color:#ff5555; font-size:0.9rem; font-weight:bold; margin-bottom:10px;'>La nueva contraseña debe tener mínimo 6 caracteres.</div>";
        } else if (!nuevaClave.equals(confirmarClave)) {
            msg = "<div style='color:#ff5555; font-size:0.9rem; font-weight:bold; margin-bottom:10px;'>Las contraseñas no coinciden.</div>";
        } else {
            Usuario u = sd.getUsuario();
            String hasheadaActual = HashUtil.hashear(claveActual);

            if (!u.getContrasenaHash().equals(hasheadaActual)) {
                msg = "<div style='color:#ff5555; font-size:0.9rem; font-weight:bold; margin-bottom:10px;'>La contraseña actual es incorrecta.</div>";
            } else {
                String nuevaHasheada = HashUtil.hashear(nuevaClave);
                boolean actualizado = usuarioDAO.actualizarContrasena(u.getId(), nuevaHasheada);
                if (actualizado) {
                    u.setContrasenaHash(nuevaHasheada);
                    msg = "<div style='color:#55ff55; font-size:0.9rem; font-weight:bold; margin-bottom:10px;'>Contraseña actualizada correctamente.</div>";
                } else {
                    msg = "<div style='color:#ff5555; font-size:0.9rem; font-weight:bold; margin-bottom:10px;'>Error interno al actualizar contraseña.</div>";
                }
            }
        }

        mostrarPerfil(exchange, sd, null, msg);
    }
}
