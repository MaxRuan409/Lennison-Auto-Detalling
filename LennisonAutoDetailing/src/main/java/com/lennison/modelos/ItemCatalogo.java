package com.lennison.modelos;

/**
 * -----------------------------------------------------------------------------
 * CLASE PRINCIPAL 1: ItemCatalogo (Equivalente a 'Animal' en el pizarrón)
 * Muestra: Abstracción (clase abstracta y métodos abstractos) y Encapsulamiento.
 * -----------------------------------------------------------------------------
 */
public abstract class ItemCatalogo {
    
    // =========================================================================
    // 12 ATRIBUTOS EN TOTAL (6 Privados y 6 Protected)
    // =========================================================================
    
    // 6 Atributos Privados (Encapsulamiento estricto)
    private Integer id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private String codigoBarras;
    private String marca;
    
    // 6 Atributos Protected (Visibles para clases hijas / Herencia)
    protected String categoria;
    protected boolean disponible;
    protected String fechaAlta;
    protected double costoBase;
    protected String proveedor;
    protected double descuentoAplicable;

    // Constructor vacío
    public ItemCatalogo() {
        this.disponible = true;
        this.descuentoAplicable = 0.0;
    }

    // Constructor completo
    public ItemCatalogo(Integer id, String nombre, String descripcion, Double precio, String codigoBarras, String marca) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.codigoBarras = codigoBarras;
        this.marca = marca;
        this.disponible = true;
        this.descuentoAplicable = 0.0;
    }

    // =========================================================================
    // 6 MÉTODOS SOLICITADOS (3 Abstractos, 2 Privados, 1 Público)
    // =========================================================================

    // 1. Método Abstracto 1: Tipo de elemento
    public abstract String getTipo();

    // 2. Método Abstracto 2: Calcular el precio final con descuentos
    public abstract double calcularPrecioFinal();

    // 3. Método Abstracto 3: Obtener resumen del artículo
    public abstract String obtenerResumen();

    // 4. Método Privado 1: Validación interna del precio
    private void validarPrecio(double p) {
        if (p < 0) {
            registrarLog("Precio inválido detectado.");
        }
    }

    // 5. Método Privado 2: Registro de logs internos de auditoría
    private void registrarLog(String msg) {
        System.out.println("[AUDITORÍA CATALOGO]: " + msg);
    }

    // 6. Método Público 1: Comprobar si tiene algún descuento activo
    public boolean tieneDescuento() {
        validarPrecio(this.precio != null ? this.precio : 0.0);
        return this.descuentoAplicable > 0.0;
    }

    // =========================================================================
    // GETTERS Y SETTERS (Para el encapsulamiento de atributos privados)
    // =========================================================================
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        validarPrecio(precio != null ? precio : 0.0);
        this.precio = precio;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }
}
