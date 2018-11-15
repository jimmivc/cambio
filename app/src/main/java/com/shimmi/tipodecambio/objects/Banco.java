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
        tipoCambio = new TipoCambio();
        findTipoCambio();
    }

    public Banco(String pnombre,int plogo, Context pcontext){
        setNombre(pnombre);
        context = pcontext;
        tipoCambio = new TipoCambio();
        setLogo(plogo);
        findTipoCambio();
    }

    private void findTipoCambio() {

        Date now = Timestamp.now().toDate();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        FirebaseDatabase.getInstance().getReference(getNombre()+"/"+df.format(now)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    tipoCambio.setCompra(dataSnapshot.hasChild("compra")? dataSnapshot.child("compra").getValue(Double.class):0);
                    tipoCambio.setVenta(dataSnapshot.hasChild("venta")?dataSnapshot.child("venta").getValue(Double.class):0);
//                    setTipoCambio(dataSnapshot.getValue(TipoCambio.class));
                    if(tipoCambio.getCompra()==0 || tipoCambio.getVenta()==0)
                        tryGetTipoCambio();

                }else{
                    createTipoCambio();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void findTipoCambio(final Date date, final boolean fCompra, final boolean fVenta) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        FirebaseDatabase.getInstance().getReference(getNombre()+"/"+df.format(date)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){

                    double compra = dataSnapshot.hasChild("compra")? dataSnapshot.child("compra").getValue(Double.class):0;
                    double venta = dataSnapshot.hasChild("venta")?dataSnapshot.child("venta").getValue(Double.class):0;
//                    setTipoCambio(dataSnapshot.getValue(TipoCambio.class));
                    if (fCompra){
                        tipoCambio.setCompra(compra);
                    }

                    if(fVenta){
                        tipoCambio.setVenta(venta);
                    }



                }

                if(tipoCambio.getCompra()==0 || tipoCambio.getVenta()==0){
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DATE,-1);
                    findTipoCambio(calendar.getTime(),fCompra,fVenta);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void tryGetTipoCambio(){
        TipoCambio t = new TipoCambio(Codigos.getInstance().getCodeByBank(getNombre()),context,getNombre(),true);
        if (tipoCambio.getVenta()==0)
            tipoCambio.setVenta(t.getVenta());
        if(tipoCambio.getCompra()==0)
            tipoCambio.setCompra(t.getCompra());

        if (tipoCambio.getCompra()==0 || tipoCambio.getCompra()==0) {
            Date now = Timestamp.now().toDate();
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            findTipoCambio(now,tipoCambio.getCompra()==0,tipoCambio.getVenta()==0);
        }
    }

    private void createTipoCambio(){
        setTipoCambio(new TipoCambio(Codigos.getInstance().getCodeByBank(getNombre()),context,getNombre()));
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
