package com.lennison.modelos;

/**
 * -----------------------------------------------------------------------------
 * CLASE SECUNDARIA 3: PedidoOnline (Hereda de 'Pedido' e implementa 'Facturable')
 * Cumple con el diagrama del pizarrón: 6 atributos y 6 métodos lógicos.
 * -----------------------------------------------------------------------------
 */
public class PedidoOnline extends Pedido implements Facturable {

    // =========================================================================
    // EXACTAMENTE 6 ATRIBUTOS ESPECÍFICOS DE LA CLASE
    // =========================================================================
    private String direccionEnvio;
    private String paqueteria;
    private String numeroSeguimiento;
    private double costoEnvio;
    private String fechaEnvio;
    private String estadoEntrega;

    // Constructor vacío
    public PedidoOnline() {
        super();
    }

    // Constructor completo
    public PedidoOnline(String direccionEnvio, String paqueteria, double costoEnvio) {
        super();
        this.direccionEnvio = direccionEnvio;
        this.paqueteria = paqueteria;
        this.costoEnvio = costoEnvio;
        this.numeroSeguimiento = "PENDIENTE";
        this.fechaEnvio = "PENDIENTE";
        this.estadoEntrega = "PREPARACIÓN";
    }

    // =========================================================================
    // MÉTODOS SECUNDARIOS (6 Métodos de Comportamiento/Lógica de Negocio)
    // =========================================================================

    // 1. Método de Comportamiento 1 (De Interfaz 2): Calcula el IVA para compras en línea
    @Override
    public double calcularImpuesto() {
        double t = getTotal() != null ? getTotal() : 0.0;
        return t * 0.16;
    }

    // 2. Método de Comportamiento 2 (De Interfaz 2): Emite la factura electrónica online
    @Override
    public String generarFactura() {
        return "FACTURA ELECTRÓNICA ONLINE | Pedido #" + getId() + " | Envío a: " + direccionEnvio;
    }

    // 3. Método de Comportamiento 3: Obtener dirección de envío
    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    // 4. Método de Comportamiento 4: Modificar dirección de envío
    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    // 5. Método de Comportamiento 5: Obtener número de guía de paquetería
    public String getNumeroSeguimiento() {
        return numeroSeguimiento;
    }

    // 6. Método de Comportamiento 6: Asignar número de guía de paquetería
    public void setNumeroSeguimiento(String numeroSeguimiento) {
        this.numeroSeguimiento = numeroSeguimiento;
    }

    // Getters y Setters Boilerplate adicionales para compatibilidad
    public String getPaqueteria() {
        return paqueteria;
    }

    public void setPaqueteria(String paqueteria) {
        this.paqueteria = paqueteria;
    }

    public double getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(double costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    public String getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(String fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getEstadoEntrega() {
        return estadoEntrega;
    }

    public void setEstadoEntrega(String estadoEntrega) {
        this.estadoEntrega = estadoEntrega;
    }
}
