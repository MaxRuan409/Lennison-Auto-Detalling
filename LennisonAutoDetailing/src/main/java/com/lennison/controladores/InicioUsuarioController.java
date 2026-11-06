package com.lennison.controladores;

import com.lennison.config.SessionData;
import com.lennison.vistas.Vista;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para la pantalla principal del cliente (Panel de Usuario).
 */
public class InicioUsuarioController extends BaseController {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Verificar que el usuario esté logueado
        SessionData sd = verificarSesion(exchange);
        if (sd == null) return; // Ya se redirigió al login

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            redirect(exchange, "/inicio-usuario");
            return;
        }

        Map<String, String> marcadores = new HashMap<>();
        marcadores.put("USUARIO_NOMBRE", sd.getUsuario().getNombre());
        
        // Contar ítems en el carrito de compras del usuario
        int itemsCarritoCount = sd.getCarrito().size();
        marcadores.put("CANTIDAD_CARRITO", String.valueOf(itemsCarritoCount));

        String html = Vista.render("inicio-usuario.html", marcadores);
        sendHtml(exchange, html);
    }
}
