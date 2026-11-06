package com.lennison.modelos;

import java.sql.Timestamp;

/**
 * Representa un usuario registrado en el sistema.
 */
public class Usuario {

    private Integer id;
    private String nombre;
    private String correo;
    private String contrasenaHash;
    private String telefono;
    private String rol; // "ADMIN" o "USUARIO"
    private Timestamp fechaRegistro;

    public Usuario() {
    }

    public Usuario(Integer id, String nombre, String correo, String contrasenaHash, String telefono, String rol, Timestamp fechaRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasenaHash = contrasenaHash;
        this.telefono = telefono;
        this.rol = rol;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y Setters
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenaHash() {
        return contrasenaHash;
    }

    public void setContrasenaHash(String contrasenaHash) {
        this.contrasenaHash = contrasenaHash;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString() {
        return "Usuario [ID: " + id + ", Nombre: " + nombre + ", Correo: " + correo + ", Rol: " + rol + "]";
    }
}
