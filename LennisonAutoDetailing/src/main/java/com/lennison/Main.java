package com.lennison;

import com.lennison.controladores.*;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) {
        try {
            // Puerto de escucha local: 8080
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            // Rutas de inicio de sesión y registro
            server.createContext("/login", new LoginController());
            server.createContext("/registro", new RegistroController());
            server.createContext("/recuperar-clave", new RecuperarClaveController());
            
            // Rutas principales del panel de usuario
            server.createContext("/inicio-usuario", new InicioUsuarioController());
            server.createContext("/productos", new ProductosController());
            server.createContext("/servicios", new ServiciosController());
            
            // Rutas del carrito de compras (GET y POST)
            server.createContext("/carrito", new CarritoController());
            server.createContext("/carrito/agregar", new CarritoController());
            server.createContext("/carrito/eliminar", new CarritoController());
            server.createContext("/carrito/vaciar", new CarritoController());
            
            // Rutas de finalización de compra
            server.createContext("/confirmar-orden", new ConfirmarOrdenController());
            server.createContext("/orden/guardar", new ConfirmarOrdenController());
            
            // Perfil de usuario y su historial de pedidos
            server.createContext("/perfil", new PerfilController());
            server.createContext("/perfil/actualizar", new PerfilController());
            server.createContext("/perfil/actualizar-clave", new PerfilController());
            server.createContext("/historial", new HistorialController());
            
            // Rutas administrativas (GET y POST)
            server.createContext("/admin", new AdminController());
            server.createContext("/admin/productos", new AdminController());
            server.createContext("/admin/servicios", new AdminController());
            server.createContext("/admin/productos/agregar", new AdminController());
            server.createContext("/admin/servicios/agregar", new AdminController());
            server.createContext("/admin/productos/eliminar", new AdminController());
            server.createContext("/admin/servicios/eliminar", new AdminController());
            server.createContext("/admin/pedidos", new AdminController());
            server.createContext("/admin/pedidos/actualizar", new AdminController());
            
            // Cerrar sesión
            server.createContext("/logout", new LogoutController());

            // Raíz de archivos estáticos (debe ser la última en evaluarse)
            server.createContext("/", new StaticFileHandler());

            server.setExecutor(null);
            server.start();

            System.out.println("====================================================");
            System.out.println("  Lennison Auto Detailing - Servidor Web Iniciado");
            System.out.println("  Dirección local: http://localhost:8080");
            System.out.println("====================================================");

        } catch (IOException e) {
            System.err.println("Error grave al arrancar el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
