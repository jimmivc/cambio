package com.shimmi.tipodecambio.objects;

import java.util.Map;

/**
 * Created by Jimmi on 23/08/2017.
 */

public class Banco {
    private String nombre;
    private TipoCambio tipoCambio;


    public Banco(String pnombre){

    }

    public TipoCambio getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(TipoCambio tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
