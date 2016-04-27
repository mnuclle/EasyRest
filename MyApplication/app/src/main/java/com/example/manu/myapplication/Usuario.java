package com.example.manu.myapplication;

import java.util.Date;

/**
 * Created by Manu on 08/04/2016.
 */
public class Usuario {

    public int idUsuario;
    public String nombreUsuario;
    public String contraseña;
    public int idRol;
    public Date fechaAlta;
    public Date fechaBaja;

    public Usuario(int idUsuario, String nombreUsuario, int idRol, Date fechaAlta, Date fechaBaja) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.idRol = idRol;
        this.fechaAlta = fechaAlta;
        this.fechaBaja = fechaBaja;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }
}
