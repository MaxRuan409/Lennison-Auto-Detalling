package com.lennison.vistas;

import com.lennison.modelos.*;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Generador dinámico de fragmentos HTML a partir de listas de objetos (POO Colecciones).
 * Genera el código HTML limpio que se inyectará en las plantillas.
 */
public class GeneradorHTML {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    /**
     * Genera la cuadrícula de productos en formato HTML.
     */
    public static String generarCuadriculaProductos(List<Producto> productos) {
        if (productos == null || productos.isEmpty()) {
            return "<p style='text-align: center; width: 100%; color: #aaaaaa;'>No hay productos disponibles.</p>";
        }

        StringBuilder sb = new StringBuilder();
        for (Producto p : productos) {
            // Demostración de uso de Interfaces y Métodos Abstractos (POO)
            p.aplicarDescuento(10.0); // 10% de descuento web (Interfaz Descontable)
            double precioFinal = p.calcularPrecioFinal(); // Método abstracto heredado
            String resumen = p.obtenerResumen(); // Método abstracto heredado

            sb.append("<div class='card-producto'>");
            sb.append("  <div class='producto-img-container'>");
            if (p.getImagenUrl() != null && !p.getImagenUrl().trim().isEmpty()) {
                sb.append("    <img src='").append(p.getImagenUrl()).append("' alt='").append(p.getNombre()).append("' class='producto-img'>");
            } else {
                sb.append("    <div class='producto-img-placeholder'><i class='fa-solid fa-box'></i></div>");
            }
            sb.append("  </div>");
            sb.append("  <div class='producto-body'>");
            sb.append("    <h3 class='producto-titulo'>").append(p.getNombre()).append("</h3>");
            sb.append("    <p class='producto-desc'>").append(p.getDescripcion()).append("</p>");
            sb.append("    <p style='font-size: 0.8rem; color: var(--color-acento); font-weight: bold; margin-top: -5px;'>").append(resumen).append("</p>");
            sb.append("    <div class='producto-footer'>");
            sb.append("      <div style='display:flex; flex-direction:column;'>");
            sb.append("        <span style='text-decoration: line-through; font-size: 0.8rem; color: #888;'>$").append(String.format("%.2f", p.getPrecio())).append("</span>");
            sb.append("        <span class='producto-precio'>$").append(String.format("%.2f", precioFinal)).append("</span>");
            sb.append("      </div>");
            if (p.getStock() > 0) {
                sb.append("      <form action='/carrito/agregar' method='POST' style='margin:0;'>");
                sb.append("        <input type='hidden' name='id' value='").append(p.getId()).append("'>");
                sb.append("        <input type='hidden' name='tipo' value='PRODUCTO'>");
                sb.append("        <button type='submit' class='btn-agregar'>");
                sb.append("          <i class='fa-solid fa-cart-plus'></i> Agregar");
                sb.append("        </button>");
                sb.append("      </form>");
            } else {
                sb.append("      <span style='color: #8B0000; font-weight: 600; font-size: 0.9rem;'>Agotado</span>");
            }
            sb.append("    </div>");
            sb.append("  </div>");
            sb.append("</div>");
        }
        return sb.toString();
    }

    /**
     * Genera la cuadrícula de servicios en formato HTML.
     */
    public static String generarCuadriculaServicios(List<Servicio> servicios) {
        if (servicios == null || servicios.isEmpty()) {
            return "<p style='text-align: center; width: 100%; color: #aaaaaa;'>No hay servicios disponibles.</p>";
        }

        StringBuilder sb = new StringBuilder();
        for (Servicio s : servicios) {
            // Demostración de uso de Interfaces y Métodos Abstractos (POO)
            s.aplicarDescuento(15.0); // 15% de descuento web (Interfaz Descontable)
            double precioFinal = s.calcularPrecioFinal(); // Método abstracto heredado
            String resumen = s.obtenerResumen(); // Método abstracto heredado

            sb.append("<div class='card-producto'>");
            sb.append("  <div class='producto-img-container'>");
            sb.append("    <div class='producto-img-placeholder'><i class='fa-solid fa-screwdriver-wrench'></i></div>");
            sb.append("  </div>");
            sb.append("  <div class='producto-body'>");
            sb.append("    <h3 class='producto-titulo'>").append(s.getNombre()).append("</h3>");
            sb.append("    <p class='producto-desc'>").append(s.getDescripcion()).append("</p>");
            sb.append("    <p style='font-size: 0.8rem; color: var(--color-acento); font-weight: bold; margin-top: -5px;'>").append(resumen).append("</p>");
            sb.append("    <p style='font-size: 0.85rem; color: #aaaaaa; margin-top: -5px;'>");
            sb.append("      <i class='fa-regular fa-clock'></i> Duración estimada: ").append(s.getDuracionEstimadaMinutos()).append(" min");
            sb.append("    </p>");
            sb.append("    <div class='producto-footer'>");
            sb.append("      <div style='display:flex; flex-direction:column;'>");
            sb.append("        <span style='text-decoration: line-through; font-size: 0.8rem; color: #888;'>$").append(String.format("%.2f", s.getPrecio())).append("</span>");
            sb.append("        <span class='producto-precio'>$").append(String.format("%.2f", precioFinal)).append("</span>");
            sb.append("      </div>");
            sb.append("      <form action='/carrito/agregar' method='POST' style='margin:0;'>");
            sb.append("        <input type='hidden' name='id' value='").append(s.getId()).append("'>");
            sb.append("        <input type='hidden' name='tipo' value='SERVICIO'>");
            sb.append("        <button type='submit' class='btn-agregar'>");
            sb.append("          <i class='fa-solid fa-cart-plus'></i> Agregar");
            sb.append("        </button>");
            sb.append("      </form>");
            sb.append("    </div>");
            sb.append("  </div>");
            sb.append("</div>");
        }
        return sb.toString();
    }

    /**
     * Genera la tabla detallada del carrito de compras.
     */
    public static String generarTablaCarrito(List<DetallePedido> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < detalles.size(); i++) {
            DetallePedido d = detalles.get(i);
            sb.append("<tr>");
            sb.append("  <td>");
            sb.append("    <div style='font-weight: 600;'>").append(d.getNombreReferencia()).append("</div>");
            sb.append("    <div style='font-size: 0.8rem; color: #aaaaaa;'>").append(d.getTipo()).append("</div>");
            sb.append("  </td>");
            sb.append("  <td>$").append(String.format("%.2f", d.getPrecioUnitario())).append("</td>");
            sb.append("  <td>").append(d.getCantidad()).append("</td>");
            sb.append("  <td>$").append(String.format("%.2f", d.getSubtotal())).append("</td>");
            sb.append("  <td style='text-align: center;'>");
            sb.append("    <form action='/carrito/eliminar' method='POST' style='margin: 0;'>");
            sb.append("      <input type='hidden' name='index' value='").append(i).append("'>");
            sb.append("      <button type='submit' style='background: none; border: none; color: #8B0000; cursor: pointer; font-size: 1.1rem;'>");
            sb.append("        <i class='fa-solid fa-trash-can'></i>");
            sb.append("      </button>");
            sb.append("    </form>");
            sb.append("  </td>");
            sb.append("</tr>");
        }
        return sb.toString();
    }

    /**
     * Genera las filas de la tabla de historial de pedidos.
     */
    public static String generarTablaHistorial(List<Pedido> pedidos) {
        if (pedidos == null || pedidos.isEmpty()) {
            return "<tr><td colspan='6' style='text-align: center; color: #aaaaaa;'>Aún no has realizado ningún pedido.</td></tr>";
        }

        StringBuilder sb = new StringBuilder();
        for (Pedido p : pedidos) {
            String fechaPStr = p.getFechaPedido() != null ? DATE_FORMAT.format(p.getFechaPedido()) : "-";
            String fechaSStr = p.getFechaServicio() != null ? DATE_FORMAT.format(p.getFechaServicio()) : "Solo Productos";
            String vehiculo = "-";
            if (p.getVehiculoMarca() != null && !p.getVehiculoMarca().isEmpty()) {
                vehiculo = p.getVehiculoMarca() + " " + (p.getVehiculoModelo() != null ? p.getVehiculoModelo() : "") + 
                           " (" + (p.getVehiculoPlaca() != null ? p.getVehiculoPlaca() : "") + ")";
            }

            sb.append("<tr>");
            sb.append("  <td>#").append(p.getId()).append("</td>");
            sb.append("  <td>").append(fechaPStr).append("</td>");
            sb.append("  <td>").append(fechaSStr).append("</td>");
            sb.append("  <td><strong>$").append(String.format("%.2f", p.getTotal())).append("</strong></td>");
            
            // Estilos del estado
            String colorEstado = "#8B0000"; // Rojo para PENDIENTE
            if ("COMPLETADO".equalsIgnoreCase(p.getEstado())) {
                colorEstado = "#008000"; // Verde
            } else if ("CANCELADO".equalsIgnoreCase(p.getEstado())) {
                colorEstado = "#555555"; // Gris
            }
            sb.append("  <td><span style='background-color: ").append(colorEstado).append("; padding: 4px 8px; border-radius: 4px; font-size: 0.8rem; font-weight: bold;'>")
              .append(p.getEstado()).append("</span></td>");
            
            sb.append("  <td>").append(vehiculo).append("</td>");
            sb.append("</tr>");
        }
        return sb.toString();
    }

    /**
     * Genera la lista de productos para la vista de administración.
     */
    public static String generarTablaAdminProductos(List<Producto> productos) {
        if (productos == null || productos.isEmpty()) {
            return "<tr><td colspan='6' style='text-align: center; color: #aaaaaa;'>No hay productos en inventario.</td></tr>";
        }

        StringBuilder sb = new StringBuilder();
        for (Producto p : productos) {
            sb.append("<tr>");
            sb.append("  <td>").append(p.getId()).append("</td>");
            sb.append("  <td>").append(p.getNombre()).append("</td>");
            sb.append("  <td style='max-width: 300px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;'>").append(p.getDescripcion()).append("</td>");
            sb.append("  <td>$").append(String.format("%.2f", p.getPrecio())).append("</td>");
            sb.append("  <td>").append(p.getStock()).append("</td>");
            sb.append("  <td>");
            sb.append("    <form action='/admin/productos/eliminar' method='POST' style='margin:0;'>");
            sb.append("      <input type='hidden' name='id' value='").append(p.getId()).append("'>");
            sb.append("      <button type='submit' style='background-color: #8B0000; color: white; border: none; padding: 6px 12px; border-radius: 4px; cursor: pointer;'>");
            sb.append("        <i class='fa-solid fa-trash'></i> Eliminar");
            sb.append("      </button>");
            sb.append("    </form>");
            sb.append("  </td>");
            sb.append("</tr>");
        }
        return sb.toString();
    }

    /**
     * Genera la lista de servicios para la vista de administración.
     */
    public static String generarTablaAdminServicios(List<Servicio> servicios) {
        if (servicios == null || servicios.isEmpty()) {
            return "<tr><td colspan='5' style='text-align: center; color: #aaaaaa;'>No hay servicios en catálogo.</td></tr>";
        }

        StringBuilder sb = new StringBuilder();
        for (Servicio s : servicios) {
            sb.append("<tr>");
            sb.append("  <td>").append(s.getId()).append("</td>");
            sb.append("  <td>").append(s.getNombre()).append("</td>");
            sb.append("  <td style='max-width: 300px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;'>").append(s.getDescripcion()).append("</td>");
            sb.append("  <td>$").append(String.format("%.2f", s.getPrecio())).append("</td>");
            sb.append("  <td>").append(s.getDuracionEstimadaMinutos()).append(" min</td>");
            sb.append("  <td>");
            sb.append("    <form action='/admin/servicios/eliminar' method='POST' style='margin:0;'>");
            sb.append("      <input type='hidden' name='id' value='").append(s.getId()).append("'>");
            sb.append("      <button type='submit' style='background-color: #8B0000; color: white; border: none; padding: 6px 12px; border-radius: 4px; cursor: pointer;'>");
            sb.append("        <i class='fa-solid fa-trash'></i> Eliminar");
            sb.append("      </button>");
            sb.append("    </form>");
            sb.append("  </td>");
            sb.append("</tr>");
        }
        return sb.toString();
    }

    public static String generarTablaAdminPedidos(List<Pedido> pedidos) {
        if (pedidos == null || pedidos.isEmpty()) {
            return "<tr><td colspan='8' style='text-align: center; color: #aaaaaa;'>No hay pedidos registrados.</td></tr>";
        }

        StringBuilder sb = new StringBuilder();
        com.lennison.dao.UsuarioDAO usuarioDAO = new com.lennison.dao.UsuarioDAO();
        for (Pedido p : pedidos) {
            String fechaPStr = p.getFechaPedido() != null ? DATE_FORMAT.format(p.getFechaPedido()) : "-";
            String fechaSStr = p.getFechaServicio() != null ? DATE_FORMAT.format(p.getFechaServicio()) : "Solo Productos";
            String vehiculo = "-";
            if (p.getVehiculoMarca() != null && !p.getVehiculoMarca().isEmpty()) {
                vehiculo = p.getVehiculoMarca() + " " + (p.getVehiculoModelo() != null ? p.getVehiculoModelo() : "") + 
                           " (" + (p.getVehiculoPlaca() != null ? p.getVehiculoPlaca() : "") + ")";
            }

            com.lennison.modelos.Usuario cliente = usuarioDAO.buscarPorId(p.getIdUsuario());
            String infoCliente = cliente != null ? cliente.getNombre() + " (" + cliente.getCorreo() + ")" : "Usuario #" + p.getIdUsuario();

            sb.append("<tr>");
            sb.append("  <td style='padding: 12px 10px;'>#").append(p.getId()).append("</td>");
            sb.append("  <td style='padding: 12px 10px;'>").append(infoCliente).append("</td>");
            sb.append("  <td style='padding: 12px 10px;'>").append(fechaPStr).append("</td>");
            sb.append("  <td style='padding: 12px 10px;'>").append(fechaSStr).append("</td>");
            sb.append("  <td style='padding: 12px 10px;'><strong>$").append(String.format("%.2f", p.getTotal())).append("</strong></td>");
            sb.append("  <td style='padding: 12px 10px;'>").append(vehiculo).append("</td>");
            
            // Estilos del estado actual
            String colorEstado = "#8B0000"; // Rojo para PENDIENTE
            if ("COMPLETADO".equalsIgnoreCase(p.getEstado())) {
                colorEstado = "#008000"; // Verde
            } else if ("CANCELADO".equalsIgnoreCase(p.getEstado())) {
                colorEstado = "#555555"; // Gris
            } else if ("EN PROCESO".equalsIgnoreCase(p.getEstado())) {
                colorEstado = "#0055ff"; // Azul
            }
            sb.append("  <td style='padding: 12px 10px;'><span style='background-color: ").append(colorEstado).append("; padding: 4px 8px; border-radius: 4px; font-size: 0.8rem; font-weight: bold;'>")
              .append(p.getEstado()).append("</span></td>");

            // Selector de cambio de estado
            sb.append("  <td style='padding: 12px 10px;'>");
            sb.append("    <form action='/admin/pedidos/actualizar' method='POST' style='margin:0; display:flex; gap:5px; align-items:center;'>");
            sb.append("      <input type='hidden' name='id' value='").append(p.getId()).append("'>");
            sb.append("      <select name='estado' style='background-color: #212121; color: white; border: 1px solid #333; padding: 6px; border-radius: 4px; font-family: sans-serif; font-size: 0.85rem;'>");
            
            String[] estados = {"PENDIENTE", "EN PROCESO", "COMPLETADO", "CANCELADO"};
            for (String est : estados) {
                sb.append("        <option value='").append(est).append("'");
                if (est.equalsIgnoreCase(p.getEstado())) {
                    sb.append(" selected");
                }
                sb.append(">").append(est).append("</option>");
            }
            sb.append("      </select>");
            sb.append("      <button type='submit' style='background-color: #8B0000; color: white; border: none; padding: 6px 12px; border-radius: 4px; cursor: pointer; font-size: 0.85rem; font-weight: bold;'>");
            sb.append("        Guardar");
            sb.append("      </button>");
            sb.append("    </form>");
            sb.append("  </td>");
            sb.append("</tr>");
        }
        return sb.toString();
    }
}
