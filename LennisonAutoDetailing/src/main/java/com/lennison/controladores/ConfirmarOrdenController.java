package com.lennison.controladores;

import com.lennison.config.SessionData;
import com.lennison.dao.PedidoDAO;
import com.lennison.dao.ProductoDAO;
import com.lennison.modelos.DetallePedido;
import com.lennison.modelos.Pedido;
import com.lennison.modelos.PedidoOnline;
import com.lennison.modelos.PedidoPresencial;
import com.lennison.modelos.Producto;
import com.lennison.modelos.Facturable;
import com.lennison.vistas.Vista;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para la Confirmación de Pedidos y Finalización de Compra.
 */
public class ConfirmarOrdenController extends BaseController {

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        SessionData sd = verificarSesion(exchange);
        if (sd == null) return;

        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if (method.equalsIgnoreCase("GET") && path.equals("/confirmar-orden")) {
            mostrarConfirmacion(exchange, sd, null);
        } else if (method.equalsIgnoreCase("POST") && path.equals("/orden/guardar")) {
            procesarOrden(exchange, sd);
        } else {
            redirect(exchange, "/carrito");
        }
    }

    private void mostrarConfirmacion(HttpExchange exchange, SessionData sd, String errorMsg) throws IOException {
        if (sd.getCarrito().isEmpty()) {
            redirect(exchange, "/carrito");
            return;
        }

        double total = 0.0;
        boolean tieneServicios = false;
        for (DetallePedido item : sd.getCarrito()) {
            total += item.getSubtotal();
            if ("SERVICIO".equalsIgnoreCase(item.getTipo())) {
                tieneServicios = true;
            }
        }

        Map<String, String> marcadores = new HashMap<>();
        marcadores.put("TOTAL_CARRITO", String.format("%.2f", total));
        marcadores.put("CANTIDAD_CARRITO", String.valueOf(sd.getCarrito().size()));

        StringBuilder sb = new StringBuilder();
        for (DetallePedido item : sd.getCarrito()) {
            sb.append("<div style='display:flex; justify-content:space-between; margin-bottom:10px; font-size:0.9rem;'>")
              .append("<span>").append(item.getNombreReferencia()).append(" x").append(item.getCantidad()).append("</span>")
              .append("<span>$").append(String.format("%.2f", item.getSubtotal())).append("</span>")
              .append("</div>");
        }
        marcadores.put("LISTA_RESUMEN", sb.toString());
        
        // Estilo del mensaje de error
        if (errorMsg != null) {
            marcadores.put("MENSAJE_ALERTA", 
                "<div style='border: 1px solid #8B0000; border-left: 5px solid #8B0000; background-color: #1a0a0a; color: #ff5555; padding: 12px; border-radius: 8px; margin-bottom: 20px; font-size: 0.9rem; text-align: center;'>" +
                errorMsg +
                "</div>"
            );
        } else {
            marcadores.put("MENSAJE_ALERTA", "");
        }

        // Mostrar sección de vehículos/fecha solo si tiene servicios contratados
        if (tieneServicios) {
            marcadores.put("REQUISITOS_SERVICIO", 
                "<div class='grupo-input'>" +
                "  <label for='fecha-servicio'>Fecha y Hora del Servicio *</label>" +
                "  <input type='datetime-local' id='fecha-servicio' name='fechaServicio' required style='width: 100%; padding: 12px 16px; background-color: var(--color-bg); border: 1px solid var(--color-border); color: var(--color-texto); border-radius: var(--radius-md); font-family: \"Inter\", sans-serif; box-sizing: border-box;'>" +
                "</div>" +
                "<div style='display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-bottom: 20px;'>" +
                "  <div class='grupo-input'>" +
                "    <label for='marca'>Marca del Vehículo *</label>" +
                "    <input type='text' id='marca' name='marca' placeholder='Ej: Honda' required style='width: 100%; padding: 12px 16px; background-color: var(--color-bg); border: 1px solid var(--color-border); color: var(--color-texto); border-radius: var(--radius-md); font-family: \"Inter\", sans-serif; box-sizing: border-box;'>" +
                "  </div>" +
                "  <div class='grupo-input'>" +
                "    <label for='modelo'>Modelo *</label>" +
                "    <input type='text' id='modelo' name='modelo' placeholder='Ej: Civic' required style='width: 100%; padding: 12px 16px; background-color: var(--color-bg); border: 1px solid var(--color-border); color: var(--color-texto); border-radius: var(--radius-md); font-family: \"Inter\", sans-serif; box-sizing: border-box;'>" +
                "  </div>" +
                "</div>" +
                "<div style='display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-bottom: 25px;'>" +
                "  <div class='grupo-input'>" +
                "    <label for='placa'>Placa *</label>" +
                "    <input type='text' id='placa' name='placa' placeholder='Ej: ABC-1234' required style='width: 100%; padding: 12px 16px; background-color: var(--color-bg); border: 1px solid var(--color-border); color: var(--color-texto); border-radius: var(--radius-md); font-family: \"Inter\", sans-serif; box-sizing: border-box;'>" +
                "  </div>" +
                "  <div class='grupo-input'>" +
                "    <label for='color'>Color *</label>" +
                "    <input type='text' id='color' name='color' placeholder='Ej: Negro' required style='width: 100%; padding: 12px 16px; background-color: var(--color-bg); border: 1px solid var(--color-border); color: var(--color-texto); border-radius: var(--radius-md); font-family: \"Inter\", sans-serif; box-sizing: border-box;'>" +
                "  </div>" +
                "</div>"
            );
        } else {
            // Opcional o no requerido para productos
            marcadores.put("REQUISITOS_SERVICIO", 
                "<p style='color: #aaaaaa; font-style: italic; margin-bottom: 25px;'>Estás comprando únicamente productos físicos. No se requiere programar cita de detallado.</p>"
            );
        }

        String html = Vista.render("confirmar-orden.html", marcadores);
        sendHtml(exchange, html);
    }

    private void procesarOrden(HttpExchange exchange, SessionData sd) throws IOException {
        if (sd.getCarrito().isEmpty()) {
            redirect(exchange, "/carrito");
            return;
        }

        Map<String, String> params = parseFormBody(exchange);
        String marca = params.get("marca");
        String modelo = params.get("modelo");
        String placa = params.get("placa");
        String color = params.get("color");
        String fechaServicioStr = params.get("fechaServicio");

        double total = 0.0;
        boolean tieneServicios = false;
        for (DetallePedido item : sd.getCarrito()) {
            total += item.getSubtotal();
            if ("SERVICIO".equalsIgnoreCase(item.getTipo())) {
                tieneServicios = true;
            }
        }

        Timestamp sqlFechaServicio = null;

        // Validaciones de negocio en el Controlador (MVC)
        if (tieneServicios) {
            if (marca == null || marca.trim().isEmpty() ||
                modelo == null || modelo.trim().isEmpty() ||
                placa == null || placa.trim().isEmpty() ||
                color == null || color.trim().isEmpty() ||
                fechaServicioStr == null || fechaServicioStr.trim().isEmpty()) {
                mostrarConfirmacion(exchange, sd, "Todos los datos de vehículo y fecha/hora son obligatorios cuando hay servicios contratados.");
                return;
            }

            try {
                // Formato HTML local: YYYY-MM-DDTHH:MM -> Formato SQL: YYYY-MM-DD HH:MM:00
                String formattedDate = fechaServicioStr.replace("T", " ") + ":00";
                sqlFechaServicio = Timestamp.valueOf(formattedDate);
                
                // Validar que la fecha sea futura
                if (sqlFechaServicio.before(new java.util.Date())) {
                    mostrarConfirmacion(exchange, sd, "La fecha programada para el servicio debe ser posterior a la fecha actual.");
                    return;
                }
            } catch (IllegalArgumentException e) {
                mostrarConfirmacion(exchange, sd, "Formato de fecha inválido.");
                return;
            }
        }

        // Crear objeto Pedido utilizando Polimorfismo e Instanciando Clases Hijas (Herencia)
        String metodoPago = params.get("metodoPago");
        Pedido p;
        if ("SUCURSAL".equalsIgnoreCase(metodoPago)) {
            p = new PedidoPresencial("Sucursal Principal", sd.getUsuario().getNombre(), 0.0);
        } else {
            p = new PedidoOnline("Dirección del Perfil", "Envío local", 0.0);
        }
        
        p.setMetodoPago(metodoPago != null ? metodoPago : "TRANSFERENCIA");
        p.setIdUsuario(sd.getUsuario().getId());
        p.setFechaServicio(sqlFechaServicio);
        p.setTotal(total);
        p.setEstado("PENDIENTE");
        p.setVehiculoMarca(marca);
        p.setVehiculoModelo(modelo);
        p.setVehiculoPlaca(placa);
        p.setVehiculoColor(color);

        // Guardar pedido en base de datos
        int pedidoId = pedidoDAO.crearPedido(p, sd.getCarrito());

        // Demostración de uso de Interfaces y Polimorfismo en el flujo Web (Facturable)
        if (p instanceof Facturable) {
            Facturable facturable = (Facturable) p;
            double impuesto = facturable.calcularImpuesto();
            String factura = facturable.generarFactura();
            System.out.println("[AUDITORÍA FACTURACIÓN - WEB]: " + factura);
            System.out.println("[AUDITORÍA FACTURACIÓN - WEB]: Impuesto calculado: $" + String.format("%.2f", impuesto));
        }

        if (pedidoId > 0) {
            // Actualizar stock de los productos adquiridos
            for (DetallePedido item : sd.getCarrito()) {
                if ("PRODUCTO".equalsIgnoreCase(item.getTipo())) {
                    Producto prod = productoDAO.buscarPorId(item.getIdReferencia());
                    if (prod != null) {
                        int nuevoStock = prod.getStock() - item.getCantidad();
                        if (nuevoStock < 0) nuevoStock = 0;
                        productoDAO.actualizarStock(prod.getId(), nuevoStock);
                    }
                }
            }

            // Vaciar el carrito de la sesión
            sd.getCarrito().clear();

            // Redirigir al historial de pedidos
            redirect(exchange, "/historial");
        } else {
            mostrarConfirmacion(exchange, sd, "Hubo un error interno al intentar guardar el pedido. Inténtalo de nuevo.");
        }
    }
}
