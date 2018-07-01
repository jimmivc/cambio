package com.shimmi.tipodecambio.objects;

import android.content.Context;

import com.shimmi.tipodecambio.utils.Codigos;

import java.util.Map;

/**
 * Created by Jimmi on 23/08/2017.
 */

public class Banco {
    private String nombre;
    private TipoCambio tipoCambio;

    public Banco(String pnombre, Context pcontext){
        setNombre(pnombre);
        findTipoCambio(pnombre,pcontext);
    }

    private void findTipoCambio(String pnombre, Context pcontext) {

//        setTipoCambio(new TipoCambio());
        if(tipoCambio==null){
            setTipoCambio(new TipoCambio(Codigos.getInstance().getCodeByBank(pnombre),pcontext));
        }
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
