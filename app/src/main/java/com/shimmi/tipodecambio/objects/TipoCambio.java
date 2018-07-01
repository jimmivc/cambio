package com.shimmi.tipodecambio.objects;

import java.util.Date;

/**
 * Created by Jimmi on 23/08/2017.
 */

public class TipoCambio {
    private String codigoVenta;
    private String codigoCompra;
    private Date fecha;
    private double venta;
    private double compra;

    public TipoCambio(){
    }

    public TipoCambio(String codVenta, String codCompra){
        setCodigoVenta(codVenta);
        setCodigoCompra(codCompra);
    }

    public String getCodigoVenta() {
        return codigoVenta;
    }

    public void setCodigoVenta(String codigoVenta) {
        this.codigoVenta = codigoVenta;
    }

    public String getCodigoCompra() {
        return codigoCompra;
    }

    public void setCodigoCompra(String codigoCompra) {
        this.codigoCompra = codigoCompra;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getVenta() {
        return venta;
    }

    private void setVenta(double venta) {
        this.venta = venta;
    }

    public double getCompra() {
        return compra;
    }

    private void setCompra(double compra) {
        this.compra = compra;
    }

}
