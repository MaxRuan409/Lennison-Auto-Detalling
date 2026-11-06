package com.lennison.modelos;

/**
 * -----------------------------------------------------------------------------
 * CLASE SECUNDARIA 4: PedidoPresencial (Hereda de 'Pedido' e implementa 'Facturable')
 * Cumple con el diagrama del pizarrón: 6 atributos y 6 métodos lógicos.
 * -----------------------------------------------------------------------------
 */
public class PedidoPresencial extends Pedido implements Facturable {

    // =========================================================================
    // EXACTAMENTE 6 ATRIBUTOS ESPECÍFICOS DE LA CLASE
    // =========================================================================
    private String numeroMesaOSucursal;
    private String nombreAtendedor;
    private double propina;
    private boolean requiereFacturaFisica;
    private String metodoConfirmacion;
    private String tiempoEsperaEstimado;

    // Constructor vacío
    public PedidoPresencial() {
        super();
    }

    // Constructor completo
    public PedidoPresencial(String numeroMesaOSucursal, String nombreAtendedor, double propina) {
        super();
        this.numeroMesaOSucursal = numeroMesaOSucursal;
        this.nombreAtendedor = nombreAtendedor;
        this.propina = propina;
        this.requiereFacturaFisica = false;
        this.metodoConfirmacion = "Firma física";
        this.tiempoEsperaEstimado = "15 minutos";
    }

    // =========================================================================
    // MÉTODOS SECUNDARIOS (6 Métodos de Comportamiento/Lógica de Negocio)
    // =========================================================================

    // 1. Método de Comportamiento 1 (De Interfaz 2): Calcula el IVA para atención en sucursal (tasa fronteriza reducida)
    @Override
    public double calcularImpuesto() {
        double t = getTotal() != null ? getTotal() : 0.0;
        return t * 0.08;
    }

    // 2. Método de Comportamiento 2 (De Interfaz 2): Emite la factura física impresa en caja
    @Override
    public String generarFactura() {
        return "FACTURA IMPRESA SUCURSAL | Caja: " + numeroMesaOSucursal + " | Atendió: " + nombreAtendedor;
    }

    // 3. Método de Comportamiento 3: Obtener número de mesa o sucursal
    public String getNumeroMesaOSucursal() {
        return numeroMesaOSucursal;
    }

    // 4. Método de Comportamiento 4: Modificar número de mesa o sucursal
    public void setNumeroMesaOSucursal(String numeroMesaOSucursal) {
        this.numeroMesaOSucursal = numeroMesaOSucursal;
    }

    // 5. Método de Comportamiento 5: Obtener propina sugerida
    public double getPropina() {
        return propina;
    }

    // 6. Método de Comportamiento 6: Asignar propina del servicio
    public void setPropina(double propina) {
        this.propina = propina;
    }

    // Getters y Setters Boilerplate adicionales para compatibilidad
    public String getNombreAtendedor() {
        return nombreAtendedor;
    }

    public void setNombreAtendedor(String nombreAtendedor) {
        this.nombreAtendedor = nombreAtendedor;
    }

    public boolean isRequiereFacturaFisica() {
        return requiereFacturaFisica;
    }

    public void setRequiereFacturaFisica(boolean requiereFacturaFisica) {
        this.requiereFacturaFisica = requiereFacturaFisica;
    }

    public String getMetodoConfirmacion() {
        return metodoConfirmacion;
    }

    public void setMetodoConfirmacion(String metodoConfirmacion) {
        this.metodoConfirmacion = metodoConfirmacion;
    }

    public String getTiempoEsperaEstimado() {
        return tiempoEsperaEstimado;
    }

    public void setTiempoEsperaEstimado(String tiempoEsperaEstimado) {
        this.tiempoEsperaEstimado = tiempoEsperaEstimado;
    }
}
