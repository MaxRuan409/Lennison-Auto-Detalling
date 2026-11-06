package com.lennison.controladores;

import com.lennison.config.SessionData;
import com.lennison.dao.PedidoDAO;
import com.lennison.modelos.Pedido;
import com.lennison.vistas.GeneradorHTML;
import com.lennison.vistas.Vista;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para mostrar el historial de pedidos de un cliente.
 */
public class HistorialController extends BaseController {

    private final PedidoDAO pedidoDAO = new PedidoDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        SessionData sd = verificarSesion(exchange);
        if (sd == null) return;

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            redirect(exchange, "/historial");
            return;
        }

        // Obtener pedidos del usuario logueado
        List<Pedido> pedidos = pedidoDAO.listarPorUsuario(sd.getUsuario().getId());
        String tablaHistorialHtml = GeneradorHTML.generarTablaHistorial(pedidos);

        Map<String, String> marcadores = new HashMap<>();
        marcadores.put("CONTENIDO_HISTORIAL", tablaHistorialHtml);
        marcadores.put("CANTIDAD_CARRITO", String.valueOf(sd.getCarrito().size()));

        String html = Vista.render("historial-pedidos.html", marcadores);
        sendHtml(exchange, html);
    }
}
