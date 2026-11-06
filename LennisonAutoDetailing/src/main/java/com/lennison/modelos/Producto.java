package com.lennison.modelos;

/**
 * -----------------------------------------------------------------------------
 * CLASE SECUNDARIA 1: Producto (Hereda de 'ItemCatalogo' e implementa 'Descontable')
 * Cumple con el diagrama del pizarrón: 6 atributos y 6 métodos lógicos.
 * -----------------------------------------------------------------------------
 */
public class Producto extends ItemCatalogo implements Descontable {

    // =========================================================================
    // EXACTAMENTE 6 ATRIBUTOS ESPECÍFICOS DE LA CLASE
    // =========================================================================
    private Integer stock;
    private String imagenUrl;
    private double pesoKg;
    private String dimension;
    private String ubicacionAlmacen;
    private boolean requiereEnvio;

    // Constructor vacío
    public Producto() {
        super();
    }

    // Constructor completo
    public Producto(Integer id, String nombre, String descripcion, Double precio, Integer stock, String imagenUrl) {
        super(id, nombre, descripcion, precio, "", "");
        this.stock = stock;
        this.imagenUrl = imagenUrl;
        this.pesoKg = 0.5;
        this.dimension = "10x10x10";
        this.ubicacionAlmacen = "Pasillo A";
        this.requiereEnvio = true;
    }

    // =========================================================================
    // MÉTODOS SECUNDARIOS (6 Métodos de Comportamiento/Lógica del Negocio)
    // =========================================================================

    // 1. Método de Comportamiento 1: Retorna el tipo de item (Polimorfismo)
    @Override
    public String getTipo() {
        return "PRODUCTO";
    }

    // 2. Método de Comportamiento 2: Cálculo de precio con descuento aplicado
    @Override
    public double calcularPrecioFinal() {
        double pr = getPrecio() != null ? getPrecio() : 0.0;
        return pr - (pr * this.descuentoAplicable);
    }

    // 3. Método de Comportamiento 3: Generación del resumen comercial del producto
    @Override
    public String obtenerResumen() {
        return getNombre() + " [Stock: " + stock + " piezas]";
    }

    // 4. Método de Comportamiento 4 (De Interfaz 1): Aplica descuento porcentual
    @Override
    public void aplicarDescuento(double porcentaje) {
        this.descuentoAplicable = porcentaje / 100.0;
    }

    // 5. Método de Comportamiento 5: Control del stock actual
    public Integer getStock() {
        return stock;
    }

    // 6. Método de Comportamiento 6: Actualización del stock actual
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    // Getters y Setters Boilerplate adicionales para compatibilidad
    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public void setPesoKg(double pesoKg) {
        this.pesoKg = pesoKg;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getUbicacionAlmacen() {
        return ubicacionAlmacen;
    }

    public void setUbicacionAlmacen(String ubicacionAlmacen) {
        this.ubicacionAlmacen = ubicacionAlmacen;
    }

    public boolean isRequiereEnvio() {
        return requiereEnvio;
    }

    public void setRequiereEnvio(boolean requiereEnvio) {
        this.requiereEnvio = requiereEnvio;
    }
}
