package com.example.manu.myapplication.Entidades;

import java.io.Serializable;

/**
<<<<<<< HEAD
 * Created by Manu on 28/05/2016.
 */

public class DetallesPedido implements Serializable {
    public int idDetallePedido ;
    public int cantidad ;
    public int idPedido ;
    public int idEstado;
    public String nombreEstado;
    public int idMenu ;
    public String nombreMenu;
    public double totalDetalle;
    public int idInsumo;
    public String nombreInsumo;
=======
 * Created by Manu on 08/05/2016.
 */
public class DetallesPedido implements Serializable {
    private int idDetallePedido;
    private int idPedido;
    private int idMenu;
    private int idInsumo;
    private int idEstado;
    private double totalDetalle;
    private int cantidad;
    private String nombreMenu;
    private double precio;
    private int idCategoria;


    public DetallesPedido() {
    }
>>>>>>> origin/master

    public int getIdDetallePedido() {
        return idDetallePedido;
    }

    public void setIdDetallePedido(int idDetallePedido) {
        this.idDetallePedido = idDetallePedido;
    }

<<<<<<< HEAD
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

=======
>>>>>>> origin/master
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

<<<<<<< HEAD
=======
    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    public int getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(int idInsumo) {
        this.idInsumo = idInsumo;
    }

>>>>>>> origin/master
    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }
<<<<<<< HEAD

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
=======
    public double getTotalDetalle() {
        return totalDetalle;
    }

    public void setTotalDetalle(double totalDetalle) {
        this.totalDetalle = totalDetalle;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
>>>>>>> origin/master
    }

    public String getNombreMenu() {
        return nombreMenu;
    }

    public void setNombreMenu(String nombreMenu) {
        this.nombreMenu = nombreMenu;
    }

<<<<<<< HEAD
    public double getTotalDetalle() {
        return totalDetalle;
    }

    public void setTotalDetalle(double totalDetalle) {
        this.totalDetalle = totalDetalle;
    }

    public int getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(int idInsumo) {
        this.idInsumo = idInsumo;
    }

    public String getNombreInsumo() {
        return nombreInsumo;
    }

    public void setNombreInsumo(String nombreInsumo) {
        this.nombreInsumo = nombreInsumo;
=======
    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
>>>>>>> origin/master
    }
}
