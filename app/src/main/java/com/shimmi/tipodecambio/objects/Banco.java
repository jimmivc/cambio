package com.shimmi.tipodecambio.objects;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import androidx.databinding.library.baseAdapters.BR;
import com.shimmi.tipodecambio.R;
import com.shimmi.tipodecambio.utils.Codigos;

/**
 * Created by Jimmi on 23/08/2017.
 */

public class Banco extends BaseObservable {

    private String nombre;
    private TipoCambio tipoCambio;
    private int logo;
    private Context context;
    private boolean state;

    public Banco(String pnombre, Context pcontext){
        setNombre(pnombre);
        context = pcontext;
        setLogo(findLogo());
        tipoCambio = new TipoCambio(Codigos.getInstance().getCodeByBank(getNombre()),context,getNombre());
//        findTipoCambio();
    }

    public Banco(String pnombre, boolean pstate){
        setNombre(pnombre);
        setLogo(findLogo());
        setState(pstate);
    }

//    public Banco(String pnombre,int plogo, Context pcontext){
//        setNombre(pnombre);
//        context = pcontext;
//        tipoCambio = new TipoCambio(Codigos.getInstance().getCodeByBank(getNombre()),context,getNombre());
//        setLogo(plogo);
////        findTipoCambio();
//    }

    private int findLogo(){
        switch (getNombre()){
            case "BN":
                return R.mipmap.bn;
            case "BCT":
                return R.mipmap.bct;
            case "HSBC":
                return R.mipmap.hsbc;
            case "Bac":
                return R.mipmap.bac;
            case "Lafise":
                return R.mipmap.lafise;
            case "Cathay":
                return R.mipmap.cathay;
            case "Improsa":
                return R.mipmap.improsa;
            case "Scotiabank":
                return R.mipmap.scot;
            case "General":
                return R.mipmap.general;
            case "BCR":
                return R.mipmap.bcr;
            case "Popular":
                return R.mipmap.popular;
        }
        return 0;
    }

    @Bindable
    public TipoCambio getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(TipoCambio tipoCambio) {
        this.tipoCambio = tipoCambio;
        notifyPropertyChanged(BR.tipoCambio);
    }

    @Bindable
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
        notifyPropertyChanged(BR.nombre);
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    @Bindable
    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
