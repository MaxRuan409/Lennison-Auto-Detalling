package com.lennison.controladores;

import com.lennison.config.SessionData;
import com.lennison.dao.ProductoDAO;
import com.lennison.modelos.Producto;
import com.lennison.vistas.GeneradorHTML;
import com.lennison.vistas.Vista;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para mostrar el catálogo de productos.
 */
public class ProductosController extends BaseController {

    private final ProductoDAO productoDAO = new ProductoDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        SessionData sd = verificarSesion(exchange);
        if (sd == null) return;

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            redirect(exchange, "/productos");
            return;
        }

        // Obtener todos los productos de la BD
        List<Producto> productos = productoDAO.listarTodos();

        // Convertir la colección de productos a HTML dinámico
        String htmlProductos = GeneradorHTML.generarCuadriculaProductos(productos);

        Map<String, String> marcadores = new HashMap<>();
        marcadores.put("CONTENIDO_PRODUCTOS", htmlProductos);
        marcadores.put("CANTIDAD_CARRITO", String.valueOf(sd.getCarrito().size()));

        String html = Vista.render("productos.html", marcadores);
        sendHtml(exchange, html);
    }
}
