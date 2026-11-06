package com.lennison.dao;

import com.lennison.config.ConexionDB;
import com.lennison.modelos.DetallePedido;
import com.lennison.modelos.Pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public int crearPedido(Pedido p, List<DetallePedido> detalles) {
        String sqlPedido = "INSERT INTO pedidos (id_usuario, fecha_servicio, total, estado, vehiculo_marca, vehiculo_modelo, vehiculo_placa, vehiculo_color) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_pedido (id_pedido, tipo, id_referencia, cantidad, precio_unitario) VALUES (?, ?, ?, ?, ?)";

        Connection con = null;
        try {
            con = ConexionDB.getConexion();
            con.setAutoCommit(false); // Iniciar transacción manual
            int idGenerado = -1;

            try (PreparedStatement ps = con.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, p.getIdUsuario());
                
                if (p.getFechaServicio() != null) {
                    ps.setTimestamp(2, p.getFechaServicio());
                } else {
                    ps.setNull(2, java.sql.Types.TIMESTAMP); 
                }
                
                ps.setDouble(3, p.getTotal());
                ps.setString(4, p.getEstado() == null ? "PENDIENTE" : p.getEstado());
                
                ps.setString(5, (p.getVehiculoMarca() == null || p.getVehiculoMarca().trim().isEmpty()) ? null : p.getVehiculoMarca());
                ps.setString(6, (p.getVehiculoModelo() == null || p.getVehiculoModelo().trim().isEmpty()) ? null : p.getVehiculoModelo());
                ps.setString(7, (p.getVehiculoPlaca() == null || p.getVehiculoPlaca().trim().isEmpty()) ? null : p.getVehiculoPlaca());
                ps.setString(8, (p.getVehiculoColor() == null || p.getVehiculoColor().trim().isEmpty()) ? null : p.getVehiculoColor());
                
                ps.executeUpdate();
                
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGenerado = rs.getInt(1);
                    }
                }
            }

            try (PreparedStatement psD = con.prepareStatement(sqlDetalle)) {
                for (DetallePedido d : detalles) {
                    psD.setInt(1, idGenerado);
                    psD.setString(2, d.getTipo().toUpperCase());
                    psD.setInt(3, d.getIdReferencia());
                    psD.setInt(4, d.getCantidad());
                    psD.setDouble(5, d.getPrecioUnitario());
                    psD.executeUpdate();
                }
            }
            
            con.commit(); // Confirmar cambios
            return idGenerado;
            
        } catch (SQLException e) {
            System.err.println("Error en la transacción del pedido: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback(); // Deshacer cambios en caso de error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return -1;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close(); 
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public List<Pedido> listarPorUsuario(int idUsuario) {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE id_usuario = ? ORDER BY fecha_pedido DESC";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pedido p = new Pedido();
                    p.setId(rs.getInt("id"));
                    p.setIdUsuario(rs.getInt("id_usuario"));
                    p.setFechaPedido(rs.getTimestamp("fecha_pedido"));
                    p.setFechaServicio(rs.getTimestamp("fecha_servicio"));
                    p.setTotal(rs.getDouble("total"));
                    p.setEstado(rs.getString("estado"));
                    p.setVehiculoMarca(rs.getString("vehiculo_marca"));
                    p.setVehiculoModelo(rs.getString("vehiculo_modelo"));
                    p.setVehiculoPlaca(rs.getString("vehiculo_placa"));
                    p.setVehiculoColor(rs.getString("vehiculo_color"));
                    
                    // Cargar detalles asociados
                    p.setDetalles(obtenerDetalles(con, p.getId()));
                    lista.add(p);
                }
            }
        } catch (SQLException e) { 
            System.err.println("Error al listar pedidos por usuario: " + e.getMessage());
        }
        return lista;
    }

    public List<Pedido> listarTodos() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidos ORDER BY fecha_pedido DESC";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pedido p = new Pedido();
                p.setId(rs.getInt("id"));
                p.setIdUsuario(rs.getInt("id_usuario"));
                p.setFechaPedido(rs.getTimestamp("fecha_pedido"));
                p.setFechaServicio(rs.getTimestamp("fecha_servicio"));
                p.setTotal(rs.getDouble("total"));
                p.setEstado(rs.getString("estado"));
                p.setVehiculoMarca(rs.getString("vehiculo_marca"));
                p.setVehiculoModelo(rs.getString("vehiculo_modelo"));
                p.setVehiculoPlaca(rs.getString("vehiculo_placa"));
                p.setVehiculoColor(rs.getString("vehiculo_color"));
                
                // Cargar detalles
                p.setDetalles(obtenerDetalles(con, p.getId()));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar todos los pedidos: " + e.getMessage());
        }
        return lista;
    }

    public int obtenerCantidadPedidosPendientes() {
        String sql = "SELECT COUNT(*) FROM pedidos WHERE estado = 'PENDIENTE'";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al contar pedidos pendientes: " + e.getMessage());
        }
        return 0;
    }

    public double obtenerTotalVentas() {
        String sql = "SELECT SUM(total) FROM pedidos WHERE estado != 'CANCELADO'";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al calcular total de ventas: " + e.getMessage());
        }
        return 0.0;
    }

    private List<DetallePedido> obtenerDetalles(Connection con, int idPedido) throws SQLException {
        List<DetallePedido> detalles = new ArrayList<>();
        String sql = "SELECT dp.*, " +
                     "CASE WHEN dp.tipo = 'PRODUCTO' THEN p.nombre ELSE s.nombre END AS nombre_referencia " +
                     "FROM detalle_pedido dp " +
                     "LEFT JOIN productos p ON dp.tipo = 'PRODUCTO' AND dp.id_referencia = p.id " +
                     "LEFT JOIN servicios s ON dp.tipo = 'SERVICIO' AND dp.id_referencia = s.id " +
                     "WHERE dp.id_pedido = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetallePedido d = new DetallePedido();
                    d.setId(rs.getInt("id"));
                    d.setIdPedido(rs.getInt("id_pedido"));
                    d.setTipo(rs.getString("tipo"));
                    d.setIdReferencia(rs.getInt("id_referencia"));
                    d.setCantidad(rs.getInt("cantidad"));
                    d.setPrecioUnitario(rs.getDouble("precio_unitario"));
                    d.setNombreReferencia(rs.getString("nombre_referencia"));
                    detalles.add(d);
                }
            }
        }
        return detalles;
    }

    public boolean actualizarEstado(int idPedido, String nuevoEstado) {
        String sql = "UPDATE pedidos SET estado = ? WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idPedido);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el estado del pedido: " + e.getMessage());
            return false;
        }
    }
}
