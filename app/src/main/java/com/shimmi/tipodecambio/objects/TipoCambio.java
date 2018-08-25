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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shimmi.tipodecambio.xml.MyXMLHandler;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.StringReader;
import java.util.Date;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Jimmi on 23/08/2017.
 */

public class TipoCambio extends BaseObservable {
    public String codigoVenta;
    public String codigoCompra;
    private Date fecha;
    private double venta;
    private double compra;

    public TipoCambio(){
    }

    public TipoCambio(String[] ventacompra, Context pcontext){
        setCodigoVenta(ventacompra[0]);
        setCodigoCompra(ventacompra[1]);
        getDataFromApi(pcontext);
    }

    private void getDataFromApi(Context pcontext){
        RequestQueue queue = Volley.newRequestQueue(pcontext);

        String urlCompra = "http://indicadoreseconomicos.bccr.fi.cr/indicadoreseconomicos/WebServices/wsIndicadoresEconomicos.asmx/ObtenerIndicadoresEconomicos?tcIndicador="+codigoCompra+"&tcFechaInicio=30/06/2018&tcFechaFinal=30/06/2018&tcNombre=string&tnSubNiveles=S";
        String urlVenta = "http://indicadoreseconomicos.bccr.fi.cr/indicadoreseconomicos/WebServices/wsIndicadoresEconomicos.asmx/ObtenerIndicadoresEconomicos?tcIndicador="+codigoVenta+"&tcFechaInicio=30/06/2018&tcFechaFinal=30/06/2018&tcNombre=string&tnSubNiveles=S";

        FirebaseFirestore.getInstance();

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                Log.d("LDMSMDLSMDLMSDLMSLMDM",getCompra()+"");
                Log.d("LDMSMDLSMDLMSDLMSLMDM",getVenta()+"");
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
