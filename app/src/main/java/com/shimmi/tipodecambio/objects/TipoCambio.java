package com.shimmi.tipodecambio.objects;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.databinding.library.baseAdapters.BR;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shimmi.tipodecambio.utils.Codigos;
import com.shimmi.tipodecambio.xml.MyXMLHandler;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Jimmi on 23/08/2017.
 */

public class TipoCambio extends BaseObservable {
    private String codigoVenta;
    private String codigoCompra;
    private Date fecha;
    private double venta;
    private double compra;
    private String banco;
    private Context context;

    public TipoCambio(){

    }

    public TipoCambio(String[] ventacompra, Context pcontext,String pbanco){
        setCodigoVenta(ventacompra[0]);
        setCodigoCompra(ventacompra[1]);
        setFecha(Timestamp.now().toDate());
        banco = pbanco;
        context = pcontext;
//        getDataFromApi(pcontext);
        findTipoCambio();
    }

    private void findTipoCambio() {

        Date now = Timestamp.now().toDate();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        FirebaseDatabase.getInstance().getReference(banco+"/"+df.format(now)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    setCompra(dataSnapshot.hasChild("compra")? dataSnapshot.child("compra").getValue(Double.class):0);
                    setVenta(dataSnapshot.hasChild("venta")?dataSnapshot.child("venta").getValue(Double.class):0);
//                    setTipoCambio(dataSnapshot.getValue(TipoCambio.class));
                    if(getCompra()==0 || getVenta()==0)
                        getDataFromApi(true);
                }else{
                    getDataFromApi(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void tryGetTipoCambio(boolean findCompra){

        if (getCompra()==0 || getCompra()==0) {
            Date now = Timestamp.now().toDate();
//            findTipoCambio(now,getCompra()==0,getVenta()==0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.DATE,-1);
            findTipoCambio(calendar.getTime(),findCompra,!findCompra);
        }
    }

    private void findTipoCambio(final Date date, final boolean fCompra, final boolean fVenta) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        FirebaseDatabase.getInstance().getReference(banco+"/"+df.format(date)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){

                    double compra = dataSnapshot.hasChild("compra")? dataSnapshot.child("compra").getValue(Double.class):0;
                    double venta = dataSnapshot.hasChild("venta")?dataSnapshot.child("venta").getValue(Double.class):0;
//                    setTipoCambio(dataSnapshot.getValue(TipoCambio.class));
                    if (fCompra){
                        setCompra(compra);
                    }
                    if(fVenta){
                        setVenta(venta);
                    }
                }

                if(getCompra()==0 || getVenta()==0){
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

    private void getDataFromApi(final boolean secondAttempt){
        RequestQueue queue = Volley.newRequestQueue(context);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        String fecha = df.format(getFecha());
        String urlCompra = "http://indicadoreseconomicos.bccr.fi.cr/indicadoreseconomicos/WebServices/wsIndicadoresEconomicos.asmx/ObtenerIndicadoresEconomicos?tcIndicador="+codigoCompra+"&tcFechaInicio="+fecha+"&tcFechaFinal="+fecha+"&tcNombre=string&tnSubNiveles=S";
        String urlVenta = "http://indicadoreseconomicos.bccr.fi.cr/indicadoreseconomicos/WebServices/wsIndicadoresEconomicos.asmx/ObtenerIndicadoresEconomicos?tcIndicador="+codigoVenta+"&tcFechaInicio="+fecha+"&tcFechaFinal="+fecha+"&tcNombre=string&tnSubNiveles=S";

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                Log.d(banco+" compra: ",getCompra()+"");
                Log.d(banco+" venta: ",getVenta()+"");
            }
        });

        StringRequest stringRequestCompra = new StringRequest(Request.Method.GET, urlCompra,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
//                        mTextView.setText("Response is: "+ response.substring(0,500));
//                        Log.d("Para que",response);

                        try {
                            SAXParserFactory spf = SAXParserFactory.newInstance();
                            SAXParser sp = spf.newSAXParser();
                            XMLReader xr = sp.getXMLReader();
                            MyXMLHandler myXMLHandler = new MyXMLHandler(codigoVenta,codigoCompra);
                            xr.setContentHandler(myXMLHandler);
                            xr.parse(new InputSource(new StringReader(response)));
                            setCompra(myXMLHandler.getTipoCambio().getCompra());

                        } catch (Exception e) {
                            Log.d("ERROR","XML Pasing Excpetion = " + e);
                        }
                        if(getCompra()==0)
                            tryGetTipoCambio(true);
                        else
                            saveToDatabase();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mTextView.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequestCompra);

        StringRequest stringRequestVenta = new StringRequest(Request.Method.GET, urlVenta,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
//                        mTextView.setText("Response is: "+ response.substring(0,500));
//                        Log.d("Para que",response);

                        try {

                            //handle XML
                            SAXParserFactory spf = SAXParserFactory.newInstance();
                            SAXParser sp = spf.newSAXParser();
                            XMLReader xr = sp.getXMLReader();

                            //URL to parse XML Tags
//                            URL sourceUrl = new URL(
//                                    "http://indicadoreseconomicos.bccr.fi.cr/indicadoreseconomicos/WebServices/wsIndicadoresEconomicos.asmx/ObtenerIndicadoresEconomicos?tcIndicador=3151&tcFechaInicio=11/10/2017&tcFechaFinal=11/10/2017&tcNombre=string&tnSubNiveles=S");
                            //Create handler to handle XML Tags ( extends DefaultHandler )
                            MyXMLHandler myXMLHandler = new MyXMLHandler(codigoVenta,codigoCompra);
                            /////dddd
                            //////dddd
                            xr.setContentHandler(myXMLHandler);
                            xr.parse(new InputSource(new StringReader(response)));
                            setVenta(myXMLHandler.getTipoCambio().getVenta());
//                            Log.d("MAMAMIA",myXMLHandler.getBanco().getTipoCambio().getVenta()+"");
                        } catch (Exception e) {
                            Log.d("ERROR","XML Pasing Excpetion = " + e);
                        }

                        if(getVenta()==0)
                            tryGetTipoCambio(false);
                        else
                            saveToDatabase();                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mTextView.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequestVenta);

    }

    private void saveToDatabase(){
        if(getVenta()!=0 || getCompra()!=0 ){
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy",Locale.US);
            Map<String, Object> data = new HashMap<>();
            if(getVenta()!=0)
                data.put("compra", getVenta());
            if (getCompra()!=0)
                data.put("venta", getCompra());

//          FirebaseFirestore.getInstance().collection(banco).document(df.format(now)).set(docData);
            FirebaseDatabase.getInstance().getReference(banco+"/"+df.format(getFecha())).setValue(data);
        }
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

    @Bindable
    public double getVenta() {
        return venta;
    }

    public void setVenta(double venta) {
        this.venta = venta;
        notifyPropertyChanged(BR.venta);
    }

    @Bindable
    public double getCompra() {
        return compra;
    }

    public void setCompra(double compra) {
        this.compra = compra;
        notifyPropertyChanged(BR.compra);

    }

}
