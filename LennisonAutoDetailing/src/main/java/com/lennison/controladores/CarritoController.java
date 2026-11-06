package com.lennison.controladores;

import com.lennison.config.SessionData;
import com.lennison.dao.ProductoDAO;
import com.lennison.dao.ServicioDAO;
import com.lennison.modelos.DetallePedido;
import com.lennison.modelos.Producto;
import com.lennison.modelos.Servicio;
import com.lennison.vistas.GeneradorHTML;
import com.lennison.vistas.Vista;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador de Carrito de Compras (GET y POST).
 */
public class CarritoController extends BaseController {

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final ServicioDAO servicioDAO = new ServicioDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        SessionData sd = verificarSesion(exchange);
        if (sd == null) return;

        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if (method.equalsIgnoreCase("GET") && path.equals("/carrito")) {
            mostrarCarrito(exchange, sd);
        } else if (method.equalsIgnoreCase("POST") && path.equals("/carrito/agregar")) {
            agregarAlCarrito(exchange, sd);
        } else if (method.equalsIgnoreCase("POST") && path.equals("/carrito/eliminar")) {
            eliminarDelCarrito(exchange, sd);
        } else if (method.equalsIgnoreCase("POST") && path.equals("/carrito/vaciar")) {
            vaciarCarrito(exchange, sd);
        } else {
            redirect(exchange, "/carrito");
        }
    }

    private void mostrarCarrito(HttpExchange exchange, SessionData sd) throws IOException {
        double total = 0.0;
        for (DetallePedido item : sd.getCarrito()) {
            total += item.getSubtotal();
        }

        // Generar la vista del carrito
        String htmlCarritoVista;
        if (sd.getCarrito().isEmpty()) {
            htmlCarritoVista = 
                "<div class='carrito-vacio'>" +
                "  <div class='vacio-icon'><i class='fa-solid fa-basket-shopping'></i></div>" +
                "  <h3>Tu carrito está vacío</h3>" +
                "  <div style='display: flex; justify-content: center; gap: 15px; margin-top: 20px;'>" +
                "    <a href='/productos' class='btn-principal' style='text-decoration: none;'>Ver Productos</a>" +
                "    <a href='/servicios' class='btn-principal' style='background-color: var(--color-input-bg); border: 1px solid var(--color-border); text-decoration: none;'>Ver Servicios</a>" +
                "  </div>" +
                "</div>";
        } else {
            String tablaRows = GeneradorHTML.generarTablaCarrito(sd.getCarrito());
            htmlCarritoVista = 
                "<div>" +
                "  <div style='overflow-x: auto;'>" +
                "    <table class='tabla-carrito'>" +
                "      <thead>" +
                "        <tr>" +
                "          <th style='width: 45%;'>Artículo</th>" +
                "          <th style='width: 15%;'>Precio Unitario</th>" +
                "          <th style='width: 15%;'>Cantidad</th>" +
                "          <th style='width: 15%;'>Subtotal</th>" +
                "          <th style='width: 10%; text-align: center;'>Quitar</th>" +
                "        </tr>" +
                "      </thead>" +
                "      <tbody>" +
                tablaRows +
                "      </tbody>" +
                "    </table>" +
                "  </div>" +
                "  <div class='carrito-footer'>" +
                "    <div>" +
                "      <span class='carrito-total-texto'>TOTAL A PAGAR:</span>" +
                "      <span class='carrito-total-valor'>$" + String.format("%.2f", total) + "</span>" +
                "    </div>" +
                "    <div style='display: flex; gap: 15px;'>" +
                "      <form action='/carrito/vaciar' method='POST' style='margin: 0;'>" +
                "        <button type='submit' class='btn-logout' style='height: 100%; border-color: #8B0000; color: #ff5555;'>" +
                "          <i class='fa-solid fa-trash-can'></i> Vaciar Carrito" +
                "        </button>" +
                "      </form>" +
                "      <a href='/confirmar-orden' class='btn-principal' style='text-decoration: none; display: inline-flex; align-items: center; gap: 8px;'>" +
                "        Proceder al Pago <i class='fa-solid fa-chevron-right'></i>" +
                "      </a>" +
                "    </div>" +
                "  </div>" +
                "</div>";
        }

        Map<String, String> marcadores = new HashMap<>();
        marcadores.put("CARRITO_VISTA", htmlCarritoVista);
        marcadores.put("CANTIDAD_CARRITO", String.valueOf(sd.getCarrito().size()));

        String html = Vista.render("carrito.html", marcadores);
        sendHtml(exchange, html);
    }

    private void agregarAlCarrito(HttpExchange exchange, SessionData sd) throws IOException {
        Map<String, String> params = parseFormBody(exchange);
        String idStr = params.get("id");
        String tipo = params.get("tipo");

        if (idStr != null && tipo != null) {
            try {
                int id = Integer.parseInt(idStr);
                if ("PRODUCTO".equalsIgnoreCase(tipo)) {
                    Producto p = productoDAO.buscarPorId(id);
                    if (p != null && p.getStock() > 0) {
                        // Verificar si ya existe en el carrito
                        boolean existe = false;
                        for (DetallePedido item : sd.getCarrito()) {
                            if ("PRODUCTO".equalsIgnoreCase(item.getTipo()) && item.getIdReferencia() == id) {
                                if (item.getCantidad() < p.getStock()) {
                                    item.setCantidad(item.getCantidad() + 1);
                                }
                                existe = true;
                                break;
                            }
                        }
                        if (!existe) {
                            DetallePedido d = new DetallePedido(null, null, "PRODUCTO", id, 1, p.getPrecio());
                            d.setNombreReferencia(p.getNombre());
                            sd.getCarrito().add(d);
                        }
                    }
                } else if ("SERVICIO".equalsIgnoreCase(tipo)) {
                    Servicio s = servicioDAO.buscarPorId(id);
                    if (s != null) {
                        // Verificar si ya existe en el carrito (servicios solo 1 por orden)
                        boolean existe = false;
                        for (DetallePedido item : sd.getCarrito()) {
                            if ("SERVICIO".equalsIgnoreCase(item.getTipo()) && item.getIdReferencia() == id) {
                                existe = true;
                                break;
                            }
                        }
                        if (!existe) {
                            DetallePedido d = new DetallePedido(null, null, "SERVICIO", id, 1, s.getPrecio());
                            d.setNombreReferencia(s.getNombre());
                            sd.getCarrito().add(d);
                        }
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // Redirigir de vuelta al catálogo según el tipo agregado
        if ("SERVICIO".equalsIgnoreCase(tipo)) {
            redirect(exchange, "/servicios");
        } else {
            redirect(exchange, "/productos");
        }
    }

    private void eliminarDelCarrito(HttpExchange exchange, SessionData sd) throws IOException {
        Map<String, String> params = parseFormBody(exchange);
        String indexStr = params.get("index");
        if (indexStr != null) {
            try {
                int idx = Integer.parseInt(indexStr);
                if (idx >= 0 && idx < sd.getCarrito().size()) {
                    sd.getCarrito().remove(idx);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        redirect(exchange, "/carrito");
    }

    private void vaciarCarrito(HttpExchange exchange, SessionData sd) throws IOException {
        sd.getCarrito().clear();
        redirect(exchange, "/carrito");
    }
}
