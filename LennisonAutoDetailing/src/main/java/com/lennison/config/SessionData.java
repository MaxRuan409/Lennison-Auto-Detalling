package com.lennison.config;

import com.lennison.modelos.DetallePedido;
import com.lennison.modelos.Usuario;
import java.util.ArrayList;
import java.util.List;

/**
 * Almacena los datos de la sesión activa de un usuario en el servidor.
 * Incluye la información del usuario y su carrito de compras actual.
 */
public class SessionData {
    private final Usuario usuario;
    private final List<DetallePedido> carrito;

    public SessionData(Usuario usuario) {
        this.usuario = usuario;
        this.carrito = new ArrayList<>(); // Colección en memoria del carrito
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public List<DetallePedido> getCarrito() {
        return carrito;
    }
}
