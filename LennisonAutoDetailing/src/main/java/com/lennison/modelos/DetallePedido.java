package com.lennison.modelos;

/**
 * Representa cada artículo o servicio individual comprado dentro de un Pedido.
 */
public class DetallePedido {

    private Integer id;
    private Integer idPedido;
    private String tipo; // "PRODUCTO" o "SERVICIO"
    private Integer idReferencia; // El ID del Producto o Servicio correspondiente
    private Integer cantidad;
    private Double precioUnitario;

    // Campos auxiliares para renderizar nombres cómodamente en las vistas sin consultas complejas
    private String nombreReferencia;

    public DetallePedido() {
    }

    public DetallePedido(Integer id, Integer idPedido, String tipo, Integer idReferencia, Integer cantidad, Double precioUnitario) {
        this.id = id;
        this.idPedido = idPedido;
        this.tipo = tipo;
        this.idReferencia = idReferencia;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getIdReferencia() {
        return idReferencia;
    }

    public void setIdReferencia(Integer idReferencia) {
        this.idReferencia = idReferencia;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getNombreReferencia() {
        return nombreReferencia;
    }

    public void setNombreReferencia(String nombreReferencia) {
        this.nombreReferencia = nombreReferencia;
    }

    public Double getSubtotal() {
        return (precioUnitario != null && cantidad != null) ? precioUnitario * cantidad : 0.0;
    }
}
