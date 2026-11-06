package com.lennison.controladores;

import com.lennison.config.SessionData;
import com.lennison.dao.PedidoDAO;
import com.lennison.dao.ProductoDAO;
import com.lennison.dao.ServicioDAO;
import com.lennison.dao.UsuarioDAO;
import com.lennison.modelos.Producto;
import com.lennison.modelos.Servicio;
import com.lennison.vistas.GeneradorHTML;
import com.lennison.vistas.Vista;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador de Administración. Agrupa todo el panel administrativo.
 */
public class AdminController extends BaseController {

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final ServicioDAO servicioDAO = new ServicioDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Verificar rol de Administrador
        SessionData sd = verificarAdmin(exchange);
        if (sd == null) return;

        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if (method.equalsIgnoreCase("GET")) {
            switch (path) {
                case "/admin":
                    mostrarDashboard(exchange);
                    break;
                case "/admin/productos":
                    mostrarProductos(exchange);
                    break;
                case "/admin/servicios":
                    mostrarServicios(exchange);
                    break;
                case "/admin/productos/agregar":
                    mostrarAgregarProducto(exchange, null);
                    break;
                case "/admin/servicios/agregar":
                    mostrarAgregarServicio(exchange, null);
                    break;
                case "/admin/pedidos":
                    mostrarPedidos(exchange);
                    break;
                default:
                    redirect(exchange, "/admin");
                    break;
            }
        } else if (method.equalsIgnoreCase("POST")) {
            switch (path) {
                case "/admin/productos/agregar":
                    procesarAgregarProducto(exchange);
                    break;
                case "/admin/servicios/agregar":
                    procesarAgregarServicio(exchange);
                    break;
                case "/admin/productos/eliminar":
                    procesarEliminarProducto(exchange);
                    break;
                case "/admin/servicios/eliminar":
                    procesarEliminarServicio(exchange);
                    break;
                case "/admin/pedidos/actualizar":
                    procesarActualizarPedido(exchange);
                    break;
                default:
                    redirect(exchange, "/admin");
                    break;
            }
        }
    }

    private void mostrarDashboard(HttpExchange exchange) throws IOException {
        int totalProductos = productoDAO.obtenerCantidadProductos();
        int totalServicios = servicioDAO.listarTodos().size();
        int totalPedidos = pedidoDAO.obtenerCantidadPedidosPendientes();
        double totalVentas = pedidoDAO.obtenerTotalVentas();
        int totalClientes = usuarioDAO.obtenerCantidadClientes();

        Map<String, String> marcadores = new HashMap<>();
        marcadores.put("TOTAL_VENTAS", String.format("%.2f", totalVentas));
        marcadores.put("TOTAL_PEDIDOS", String.valueOf(totalPedidos));
        marcadores.put("TOTAL_CLIENTES", String.valueOf(totalClientes));
        marcadores.put("TOTAL_PRODUCTOS", String.valueOf(totalProductos));
        marcadores.put("TOTAL_SERVICIOS", String.valueOf(totalServicios));

        String html = Vista.render("inicio-admin.html", marcadores);
        sendHtml(exchange, html);
    }

    private void mostrarProductos(HttpExchange exchange) throws IOException {
        List<Producto> productos = productoDAO.listarTodos();
        String tabla = GeneradorHTML.generarTablaAdminProductos(productos);

        Map<String, String> marcadores = new HashMap<>();
        marcadores.put("TABLA_PRODUCTOS", tabla);

        String html = Vista.render("ver-productos.html", marcadores);
        sendHtml(exchange, html);
    }

    private void mostrarServicios(HttpExchange exchange) throws IOException {
        List<Servicio> servicios = servicioDAO.listarTodos();
        String tabla = GeneradorHTML.generarTablaAdminServicios(servicios);

        Map<String, String> marcadores = new HashMap<>();
        marcadores.put("TABLA_SERVICIOS", tabla);

        String html = Vista.render("ver-servicios.html", marcadores);
        sendHtml(exchange, html);
    }

    private void mostrarAgregarProducto(HttpExchange exchange, String errorMsg) throws IOException {
        Map<String, String> marcadores = new HashMap<>();
        if (errorMsg != null) {
            marcadores.put("MENSAJE_FORM", 
                "<div style='border: 1px solid #8B0000; border-left: 5px solid #8B0000; background-color: #1a0a0a; color: #ff5555; padding: 12px; border-radius: 8px; margin-bottom: 20px; font-size: 0.9rem; text-align: center;'>" +
                errorMsg +
                "</div>"
            );
        } else {
            marcadores.put("MENSAJE_FORM", "");
        }

        String html = Vista.render("agregar-producto.html", marcadores);
        sendHtml(exchange, html);
    }

    private void procesarAgregarProducto(HttpExchange exchange) throws IOException {
        Map<String, String> params = parseFormBody(exchange);
        String nombre = params.get("nombre");
        String descripcion = params.get("descripcion");
        String precioStr = params.get("precio");
        String stockStr = params.get("stock");
        String imagenUrl = params.get("imagenUrl");

        if (nombre == null || nombre.trim().isEmpty() ||
            descripcion == null || descripcion.trim().isEmpty() ||
            precioStr == null || stockStr == null) {
            mostrarAgregarProducto(exchange, "Todos los campos obligatorios (*) deben completarse.");
            return;
        }

        try {
            double precio = Double.parseDouble(precioStr);
            int stock = Integer.parseInt(stockStr);
            if (precio < 0 || stock < 0) {
                mostrarAgregarProducto(exchange, "El precio y el stock no pueden ser negativos.");
                return;
            }

            Producto p = new Producto(null, nombre, descripcion, precio, stock, imagenUrl);
            boolean guardado = productoDAO.insertar(p);
            if (guardado) {
                redirect(exchange, "/admin/productos");
            } else {
                mostrarAgregarProducto(exchange, "Error al insertar el producto en la base de datos.");
            }
        } catch (NumberFormatException e) {
            mostrarAgregarProducto(exchange, "Precio y stock deben ser numéricos.");
        }
    }

    private void mostrarAgregarServicio(HttpExchange exchange, String errorMsg) throws IOException {
        Map<String, String> marcadores = new HashMap<>();
        if (errorMsg != null) {
            marcadores.put("MENSAJE_FORM", 
                "<div style='border: 1px solid #8B0000; border-left: 5px solid #8B0000; background-color: #1a0a0a; color: #ff5555; padding: 12px; border-radius: 8px; margin-bottom: 20px; font-size: 0.9rem; text-align: center;'>" +
                errorMsg +
                "</div>"
            );
        } else {
            marcadores.put("MENSAJE_FORM", "");
        }

        String html = Vista.render("agregar-servicio.html", marcadores);
        sendHtml(exchange, html);
    }

    private void procesarAgregarServicio(HttpExchange exchange) throws IOException {
        Map<String, String> params = parseFormBody(exchange);
        String nombre = params.get("nombre");
        String descripcion = params.get("descripcion");
        String precioStr = params.get("precio");
        String duracionStr = params.get("duracion");

        if (nombre == null || nombre.trim().isEmpty() ||
            descripcion == null || descripcion.trim().isEmpty() ||
            precioStr == null || duracionStr == null) {
            mostrarAgregarServicio(exchange, "Todos los campos (*) son obligatorios.");
            return;
        }

        try {
            double precio = Double.parseDouble(precioStr);
            int duracion = Integer.parseInt(duracionStr);
            if (precio < 0 || duracion <= 0) {
                mostrarAgregarServicio(exchange, "El precio no puede ser negativo y la duración debe ser mayor a 0.");
                return;
            }

            Servicio s = new Servicio(null, nombre, descripcion, precio, duracion);
            boolean guardado = servicioDAO.insertar(s);
            if (guardado) {
                redirect(exchange, "/admin/servicios");
            } else {
                mostrarAgregarServicio(exchange, "Error al insertar el servicio en la base de datos.");
            }
        } catch (NumberFormatException e) {
            mostrarAgregarServicio(exchange, "Precio y duración deben ser numéricos.");
        }
    }

    private void procesarEliminarProducto(HttpExchange exchange) throws IOException {
        Map<String, String> params = parseFormBody(exchange);
        String idStr = params.get("id");
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                productoDAO.eliminar(id);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        redirect(exchange, "/admin/productos");
    }

    private void procesarEliminarServicio(HttpExchange exchange) throws IOException {
        Map<String, String> params = parseFormBody(exchange);
        String idStr = params.get("id");
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                servicioDAO.eliminar(id);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        redirect(exchange, "/admin/servicios");
    }

    private void mostrarPedidos(HttpExchange exchange) throws IOException {
        List<com.lennison.modelos.Pedido> pedidos = pedidoDAO.listarTodos();
        String tabla = GeneradorHTML.generarTablaAdminPedidos(pedidos);

        Map<String, String> marcadores = new HashMap<>();
        marcadores.put("TABLA_PEDIDOS", tabla);

        String html = Vista.render("ver-pedidos.html", marcadores);
        sendHtml(exchange, html);
    }

    private void procesarActualizarPedido(HttpExchange exchange) throws IOException {
        Map<String, String> params = parseFormBody(exchange);
        String idStr = params.get("id");
        String nuevoEstado = params.get("estado");

        if (idStr != null && nuevoEstado != null) {
            try {
                int id = Integer.parseInt(idStr);
                pedidoDAO.actualizarEstado(id, nuevoEstado);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        redirect(exchange, "/admin/pedidos");
    }
}
