package com.shimmi.tipodecambio.objects;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shimmi.tipodecambio.xml.MyXMLHandler;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ConcurrentModificationException;
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

    public TipoCambio(){

    }

    public TipoCambio(String[] ventacompra, Context pcontext,String pbanco){
        setCodigoVenta(ventacompra[0]);
        setCodigoCompra(ventacompra[1]);
        setFecha(Timestamp.now().toDate());
        banco = pbanco;
        getDataFromApi(pcontext);
    }

    public TipoCambio(String[] ventacompra, Context pcontext,String pbanco,boolean async){
        setCodigoVenta(ventacompra[0]);
        setCodigoCompra(ventacompra[1]);
        setFecha(Timestamp.now().toDate());
        banco = pbanco;
//        getDataFromApi(pcontext);

        RequestQueue queue = Volley.newRequestQueue(pcontext);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        String fecha = df.format(getFecha());
        String urlCompra = "http://indicadoreseconomicos.bccr.fi.cr/indicadoreseconomicos/WebServices/wsIndicadoresEconomicos.asmx/ObtenerIndicadoresEconomicos?tcIndicador="+codigoCompra+"&tcFechaInicio="+fecha+"&tcFechaFinal="+fecha+"&tcNombre=string&tnSubNiveles=S";
        String urlVenta = "http://indicadoreseconomicos.bccr.fi.cr/indicadoreseconomicos/WebServices/wsIndicadoresEconomicos.asmx/ObtenerIndicadoresEconomicos?tcIndicador="+codigoVenta+"&tcFechaInicio="+fecha+"&tcFechaFinal="+fecha+"&tcNombre=string&tnSubNiveles=S";

        RequestFuture<String> future = RequestFuture.newFuture();
        StringRequest stringRequestCompra = new StringRequest(Request.Method.GET, urlCompra,future, future);
//        StringRequest stringRequestVenta = new StringRequest(Request.Method.GET, urlVenta,future,future);

        queue.add(stringRequestCompra);
//        queue.add(stringRequestVenta);

        try {
            String resCompra = future.get();
            System.out.println(resCompra);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    private void getDataFromApi(Context pcontext){
        RequestQueue queue = Volley.newRequestQueue(pcontext);
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

                        saveToDatabase();
                    }
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
                data.put("compra", getCompra());
            if (getCompra()!=0)
                data.put("venta", getVenta());

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
