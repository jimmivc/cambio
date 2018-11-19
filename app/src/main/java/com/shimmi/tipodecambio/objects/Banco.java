package com.shimmi.tipodecambio.objects;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.android.databinding.library.baseAdapters.BR;
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shimmi.tipodecambio.R;
import com.shimmi.tipodecambio.utils.Codigos;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Jimmi on 23/08/2017.
 */

public class Banco extends BaseObservable {
    private String nombre;
    private TipoCambio tipoCambio;
    private int logo;
    private Context context;

    public Banco(String pnombre, Context pcontext){
        setNombre(pnombre);
        context = pcontext;
        tipoCambio = new TipoCambio(Codigos.getInstance().getCodeByBank(getNombre()),context,getNombre());
//        findTipoCambio();
    }

    public Banco(String pnombre,int plogo, Context pcontext){
        setNombre(pnombre);
        context = pcontext;
        tipoCambio = new TipoCambio(Codigos.getInstance().getCodeByBank(getNombre()),context,getNombre());
        setLogo(plogo);
//        findTipoCambio();
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
}
