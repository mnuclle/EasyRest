package com.example.manu.myapplication.Entidades;

import android.os.Parcelable;

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