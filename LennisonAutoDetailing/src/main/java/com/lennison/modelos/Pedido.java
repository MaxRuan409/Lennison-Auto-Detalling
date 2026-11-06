package com.lennison.modelos;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * -----------------------------------------------------------------------------
 * CLASE PRINCIPAL 2: Pedido (Equivalente a 'Ventas' en el pizarrón)
 * Muestra: Encapsulamiento y asociación/composición con DetallePedido.
 * -----------------------------------------------------------------------------
 */
public class Pedido {

    // =========================================================================
    // 6 ATRIBUTOS PRINCIPALES (Pizarrón: Ventas tiene 6 atributos y 6 métodos)
    // =========================================================================
    private Integer id;
    private Integer idUsuario;
    private Double total;
    private String estado;
    private Timestamp fechaPedido;
    private String metodoPago;

    // Atributos de compatibilidad con base de datos y detalles del negocio
    protected Timestamp fechaServicio;
    protected String vehiculoMarca;
    protected String vehiculoModelo;
    protected String vehiculoPlaca;
    protected String vehiculoColor;
    protected List<DetallePedido> detalles = new ArrayList<>();

    // Constructor vacío
    public Pedido() {
        this.estado = "PENDIENTE";
        this.metodoPago = "TRANSFERENCIA";
    }

    // =========================================================================
    // 6 MÉTODOS PRINCIPALES DE COMPORTAMIENTO / LÓGICA (Ventas en el pizarrón)
    // =========================================================================

    // 1. Método de Control 1: Cambiar el estado del pedido
    public void cambiarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }

    // 2. Método de Control 2: Comprobar si el estado es pendiente
    public boolean esPendiente() {
        return "PENDIENTE".equalsIgnoreCase(this.estado);
    }

    // 3. Método de Control 3: Calcular el total incluyendo impuesto del 16%
    public double calcularTotalConImpuesto() {
        double t = this.total != null ? this.total : 0.0;
        return t * 1.16;
    }

    // 4. Método de Control 4: Obtener resumen textual de la venta
    public String obtenerDetalleCompleto() {
        return "Pedido #" + id + " | Usuario: " + idUsuario + " | Total: $" + total;
    }

    // 5. Método de Control 5: Determinar si el pedido requiere atención inmediata
    public boolean esUrgente() {
        return "PENDIENTE".equalsIgnoreCase(this.estado) && this.fechaServicio != null;
    }

    // 6. Método de Control 6: Registrar la venta en los logs de auditoría
    public void registrarPedido() {
        System.out.println("[AUDITORÍA VENTAS]: Registro de Pedido #" + this.id + " por total de $" + this.total);
    }

    // =========================================================================
    // GETTERS Y SETTERS BOILERPLATE
    // =========================================================================

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Timestamp getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Timestamp fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Timestamp getFechaServicio() {
        return fechaServicio;
    }

    public void setFechaServicio(Timestamp fechaServicio) {
        this.fechaServicio = fechaServicio;
    }

    public String getVehiculoMarca() {
        return vehiculoMarca;
    }

    public void setVehiculoMarca(String vehiculoMarca) {
        this.vehiculoMarca = vehiculoMarca;
    }

    public String getVehiculoModelo() {
        return vehiculoModelo;
    }

    public void setVehiculoModelo(String vehiculoModelo) {
        this.vehiculoModelo = vehiculoModelo;
    }

    public String getVehiculoPlaca() {
        return vehiculoPlaca;
    }

    public void setVehiculoPlaca(String vehiculoPlaca) {
        this.vehiculoPlaca = vehiculoPlaca;
    }

    public String getVehiculoColor() {
        return vehiculoColor;
    }

    public void setVehiculoColor(String vehiculoColor) {
        this.vehiculoColor = vehiculoColor;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }
}
