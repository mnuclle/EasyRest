package com.example.manu.myapplication.Entidades;

import android.widget.ImageView;

/**
 * Created by Manu on 07/05/2016.
 */
public class Images {
    private int idMenu;
    private int idImagen;
    private boolean esMenu;
    private int id;

    public Images(int idMenu, int idImagen,boolean esMenu, int id) {
        this.idMenu = idMenu;
        this.idImagen = idImagen;

        this.esMenu = esMenu;
        this.id=id;
    }

    public int getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(int idImagen) {
        this.idImagen = idImagen;
    }

    public int getIdMenu() {
        return idMenu;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
