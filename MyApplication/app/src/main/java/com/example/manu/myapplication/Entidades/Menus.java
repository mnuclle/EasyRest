package com.example.manu.myapplication.Entidades;

import java.io.Serializable;

/**
 * Created by Manu on 21/05/2016.
 */

public class Menus implements Serializable{
    private int idMenu;
    private int idInsumo;
    private boolean esMenu;
    private String nombreMenu;
    private double precio;
    private int idCategoria;
    private int cantidad;
    private String descripcion;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIdMenu() {
        return idMenu;
    }

    public int getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(int idInsumo) {
        this.idInsumo = idInsumo;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    public boolean isEsMenu() {
        return esMenu;
    }

    public void setEsMenu(boolean esMenu) {
        this.esMenu = esMenu;
    }

    public String getNombreMenu() {
        return nombreMenu;
    }

    public void setNombreMenu(String nombreMenu) {
        this.nombreMenu = nombreMenu;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
