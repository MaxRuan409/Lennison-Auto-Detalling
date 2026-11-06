package com.lennison.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión a la base de datos de MySQL.
 */
public class ConexionDB {

    private static final String URL = "jdbc:mysql://localhost:3306/lennison_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USUARIO = "lennison_app";
    private static final String PASSWORD = "123456";

    /**
     * Obtiene una conexión activa a la base de datos MySQL.
     */
    public static Connection getConexion() {
        Connection conexion = null;
        try {
            // Cargar el driver JDBC de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver de MySQL no encontrado en el classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error de SQL al intentar conectar con la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
        return conexion;
    }
}
