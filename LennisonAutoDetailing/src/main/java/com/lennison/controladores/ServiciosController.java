package com.lennison.controladores;

import com.lennison.config.SessionData;
import com.lennison.dao.ServicioDAO;
import com.lennison.modelos.Servicio;
import com.lennison.vistas.GeneradorHTML;
import com.lennison.vistas.Vista;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para mostrar el catálogo de servicios de detallado.
 */
public class ServiciosController extends BaseController {

    private final ServicioDAO servicioDAO = new ServicioDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        SessionData sd = verificarSesion(exchange);
        if (sd == null) return;

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            redirect(exchange, "/servicios");
            return;
        }

        List<Servicio> servicios = servicioDAO.listarTodos();
        String htmlServicios = GeneradorHTML.generarCuadriculaServicios(servicios);

        Map<String, String> marcadores = new HashMap<>();
        marcadores.put("CONTENIDO_SERVICIOS", htmlServicios);
        marcadores.put("CANTIDAD_CARRITO", String.valueOf(sd.getCarrito().size()));

        String html = Vista.render("servicios.html", marcadores);
        sendHtml(exchange, html);
    }
}
