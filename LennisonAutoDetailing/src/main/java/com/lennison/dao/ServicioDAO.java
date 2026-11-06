package com.lennison.dao;

import com.lennison.config.ConexionDB;
import com.lennison.modelos.Servicio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServicioDAO {

    public List<Servicio> listarTodos() {
        List<Servicio> lista = new ArrayList<>();
        String sql = "SELECT * FROM servicios";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Servicio s = new Servicio();
                s.setId(rs.getInt("id"));
                s.setNombre(rs.getString("nombre"));
                s.setDescripcion(rs.getString("descripcion"));
                s.setPrecio(rs.getDouble("precio"));
                s.setDuracionEstimadaMinutos(rs.getInt("duracion_estimada_minutos"));
                lista.add(s);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar servicios: " + e.getMessage());
        }
        return lista;
    }

    public Servicio buscarPorId(int id) {
        String sql = "SELECT * FROM servicios WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Servicio s = new Servicio();
                    s.setId(rs.getInt("id"));
                    s.setNombre(rs.getString("nombre"));
                    s.setDescripcion(rs.getString("descripcion"));
                    s.setPrecio(rs.getDouble("precio"));
                    s.setDuracionEstimadaMinutos(rs.getInt("duracion_estimada_minutos"));
                    return s;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar servicio por ID: " + e.getMessage());
        }
        return null;
    }

    public boolean insertar(Servicio s) {
        String sql = "INSERT INTO servicios (nombre, descripcion, precio, duracion_estimada_minutos) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getNombre());
            ps.setString(2, s.getDescripcion());
            ps.setDouble(3, s.getPrecio());
            ps.setInt(4, s.getDuracionEstimadaMinutos());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar servicio: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM servicios WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar servicio: " + e.getMessage());
            return false;
        }
    }
}
