package com.example.manu.myapplication;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Danielito on 23/05/2016.
 */
public class Pedido implements Serializable {

    private int idPedido;
    private Date fechaAlta;
    private Date fechaBaja;
    private int idEstado;
    private int idCuenta;
    private DetallePedido[] arrayDetallesPedido;

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
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

    public int getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public DetallePedido[] getArrayDetallesPedido() {
        return arrayDetallesPedido;
    }

    public void setArrayDetallesPedido(DetallePedido[] arrayDetallesPedido) {
        this.arrayDetallesPedido = arrayDetallesPedido;
    }


}
