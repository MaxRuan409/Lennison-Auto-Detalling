package com.lennison.modelos;

/**
 * -----------------------------------------------------------------------------
 * CLASE SECUNDARIA 2: Servicio (Hereda de 'ItemCatalogo' e implementa 'Descontable')
 * Cumple con el diagrama del pizarrón: 6 atributos y 6 métodos lógicos.
 * -----------------------------------------------------------------------------
 */
public class Servicio extends ItemCatalogo implements Descontable {

    // =========================================================================
    // EXACTAMENTE 6 ATRIBUTOS ESPECÍFICOS DE LA CLASE
    // =========================================================================
    private Integer duracionEstimadaMinutos;
    private String dificultad;
    private String herramientasRequeridas;
    private boolean requiereCita;
    private String encargado;
    private double comisionEmpleado;

    // Constructor vacío
    public Servicio() {
        super();
    }

    // Constructor completo
    public Servicio(Integer id, String nombre, String descripcion, Double precio, Integer duracionEstimadaMinutos) {
        super(id, nombre, descripcion, precio, "", "");
        this.duracionEstimadaMinutos = duracionEstimadaMinutos;
        this.dificultad = "Media";
        this.herramientasRequeridas = "Pulidora y microfibra";
        this.requiereCita = true;
        this.encargado = "Sin asignar";
        this.comisionEmpleado = 15.0;
    }

    // =========================================================================
    // MÉTODOS SECUNDARIOS (6 Métodos de Comportamiento/Lógica del Negocio)
    // =========================================================================

    // 1. Método de Comportamiento 1: Retorna el tipo de item (Polimorfismo)
    @Override
    public String getTipo() {
        return "SERVICIO";
    }

    // 2. Método de Comportamiento 2: Cálculo de precio con descuento aplicado
    @Override
    public double calcularPrecioFinal() {
        double pr = getPrecio() != null ? getPrecio() : 0.0;
        return pr - (pr * this.descuentoAplicable);
    }

    // 3. Método de Comportamiento 3: Generación del resumen comercial del servicio
    @Override
    public String obtenerResumen() {
        return getNombre() + " [Duración: " + duracionEstimadaMinutos + " mins]";
    }

    // 4. Método de Comportamiento 4 (De Interfaz 1): Aplica descuento porcentual
    @Override
    public void aplicarDescuento(double porcentaje) {
        this.descuentoAplicable = porcentaje / 100.0;
    }

    // 5. Método de Comportamiento 5: Obtener duración estimada
    public Integer getDuracionEstimadaMinutos() {
        return duracionEstimadaMinutos;
    }

    // 6. Método de Comportamiento 6: Actualizar duración estimada
    public void setDuracionEstimadaMinutos(Integer duracionEstimadaMinutos) {
        this.duracionEstimadaMinutos = duracionEstimadaMinutos;
    }

    // Getters y Setters Boilerplate adicionales para compatibilidad
    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public String getHerramientasRequeridas() {
        return herramientasRequeridas;
    }

    public void setHerramientasRequeridas(String herramientasRequeridas) {
        this.herramientasRequeridas = herramientasRequeridas;
    }

    public boolean isRequiereCita() {
        return requiereCita;
    }

    public void setRequiereCita(boolean requiereCita) {
        this.requiereCita = requiereCita;
    }

    public String getEncargado() {
        return encargado;
    }

    public void setEncargado(String encargado) {
        this.encargado = encargado;
    }

    public double getComisionEmpleado() {
        return comisionEmpleado;
    }

    public void setComisionEmpleado(double comisionEmpleado) {
        this.comisionEmpleado = comisionEmpleado;
    }
}
