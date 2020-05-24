package com.shimmi.tipodecambio.objects;

import android.content.Context;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.annotation.NonNull;
import android.util.Log;

import androidx.databinding.library.baseAdapters.BR;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Jimmi on 23/08/2017.
 */

public class TipoCambio extends BaseObservable {

    private final DateFormat localDateFormat = new SimpleDateFormat("dd-MM-yy", Locale.US);

    private String codigoVenta;
    private String codigoCompra;
    private Date fecha;
    private String fechaVenta;
    private String fechaCompra;
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
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        FirebaseDatabase.getInstance().getReference(banco+"/"+df.format(now)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    setCompra(dataSnapshot.hasChild("compra") ? dataSnapshot.child("compra").getValue(Double.class) : 0);
                    setFechaCompra(getFecha());
                    setVenta(dataSnapshot.hasChild("venta") ? dataSnapshot.child("venta").getValue(Double.class) : 0);
                    setFechaVenta(getFecha());
//                    setTipoCambio(dataSnapshot.getValue(TipoCambio.class));
                    if ( getCompra() == 0 )
                        getCompraFromAPI();
                    if ( getVenta() == 0 )
                        getVentaFromApi();
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

        Date now = Timestamp.now().toDate();
//      findTipoCambio(now,getCompra()==0,getVenta()==0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE,-1);

        findTipoCambio(calendar.getTime(),findCompra,!findCompra);
    }

    private void findTipoCambio(final Date date, final boolean fCompra, final boolean fVenta) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        FirebaseDatabase.getInstance().getReference(banco+"/"+df.format(date)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){

                    double compra = dataSnapshot.hasChild("compra")? dataSnapshot.child("compra").getValue(Double.class):0;
                    double venta = dataSnapshot.hasChild("venta")?dataSnapshot.child("venta").getValue(Double.class):0;
//                    setTipoCambio(dataSnapshot.getValue(TipoCambio.class));
                    if (fCompra){
                        setCompra(compra);
                        setFechaCompra(date);
                    }
                    if(fVenta){
                        setVenta(venta);
                        setFechaVenta(date);
                    }
                }

                if(getCompra()==0 || getVenta()==0){
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DATE,-1);
                    findTipoCambio(calendar.getTime(), fCompra, fVenta);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getDataFromApi(final boolean secondAttempt){
       getCompraFromAPI();
       getVentaFromApi();
    }

    private void saveToDatabase(){
        if( getVenta()!=0 || getCompra()!=0 ){
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
            Map<String, Object> data = new HashMap<>();
            if (getVenta()!=0)
                data.put("venta", getVenta());
            if (getCompra()!=0)
                data.put("compra", getCompra());

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

    private Date getFecha() {
        return fecha;
    }

    private void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setFechaVenta(Date fechaVenta){
        this.fechaVenta = localDateFormat.format(fechaVenta);
        notifyPropertyChanged(BR.fechaVenta);
    }

    public void setFechaVenta(String fechaVenta){
        this.fechaVenta = fechaVenta;
        notifyPropertyChanged(BR.fechaVenta);
    }

    @Bindable
    public String getFechaVenta(){
        return fechaVenta;
    }

    public void setFechaCompra(String fechaCompra){
        this.fechaCompra = fechaCompra;
        notifyPropertyChanged(BR.fechaCompra);
    }

    public void setFechaCompra(Date fechaCompra){
        this.fechaCompra = localDateFormat.format(fechaCompra);
        notifyPropertyChanged(BR.fechaCompra);
    }

    @Bindable
    public String getFechaCompra(){
        return fechaCompra;
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

    private void getCompraFromAPI(){
        RequestQueue queue = Volley.newRequestQueue(context);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        final String fecha = df.format(getFecha());
        String token = "20MIV559IJ";
        String getURL = "/Indicadores/Suscripciones/WS/wsindicadoreseconomicos.asmx/ObtenerIndicadoresEconomicos";
        String host = "https://gee.bccr.fi.cr";
        String urlCompra = host+getURL+"?Indicador="+codigoCompra+"&FechaInicio="+fecha+"&FechaFinal="+fecha+"&Nombre=string&SubNiveles=S&CorreoElectronico=jimmivco@gmail.com&Token="+token;

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                Log.d(banco+" compra: ",getCompra()+"");
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
                            setFechaCompra(getFecha());
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
    }

    private void getVentaFromApi(){
        RequestQueue queue = Volley.newRequestQueue(context);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        String fecha = df.format(getFecha());
        String token = "20MIV559IJ";
        String getURL = "/Indicadores/Suscripciones/WS/wsindicadoreseconomicos.asmx/ObtenerIndicadoresEconomicos";
        String host = "https://gee.bccr.fi.cr";
        String urlVenta = host+getURL+"?Indicador="+codigoVenta+"&FechaInicio="+fecha+"&FechaFinal="+fecha+"&Nombre=string&SubNiveles=S&CorreoElectronico=jimmivco@gmail.com&Token="+token;

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                Log.d(banco+" compra: ",getCompra()+"");
                Log.d(banco+" venta: ",getVenta()+"");
            }
        });

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
                            setFechaVenta(getFecha());
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

}
