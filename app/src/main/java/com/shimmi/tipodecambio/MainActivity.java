package com.shimmi.tipodecambio;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shimmi.tipodecambio.objects.Banco;
import com.shimmi.tipodecambio.objects.TipoCambio;
import com.shimmi.tipodecambio.xml.MyXMLHandler;
import com.shimmi.tipodecambio.xml.ProgramList;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {

    //create object For SiteList class
    ArrayList<Banco> bancos = new ArrayList<Banco>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



    }

    private void requestCurrency(){
        bancos.add(new Banco("BCT",new TipoCambio("3151","3210")));
        bancos.add(new Banco("HSBC",new TipoCambio("3152","3211")));
        bancos.add(new Banco("Citibank",new TipoCambio("3153","3212")));
        bancos.add(new Banco("Cathay",new TipoCambio("3180","3214")));
        bancos.add(new Banco("Bac",new TipoCambio("3181","3215")));
        bancos.add(new Banco("Lafise",new TipoCambio("3183","3217")));
        bancos.add(new Banco("Improsa",new TipoCambio("3184","3218")));
        bancos.add(new Banco("Interfin",new TipoCambio("3185","3220")));
        bancos.add(new Banco("Scotiabank",new TipoCambio("3186","3221")));
        bancos.add(new Banco("General",new TipoCambio("3187","3568")));
        bancos.add(new Banco("BCR",new TipoCambio("3148","3207")));
        bancos.add(new Banco("BN",new TipoCambio("3149","3208")));
        bancos.add(new Banco("Popular",new TipoCambio("4179","3209")));


        RequestQueue queue = Volley.newRequestQueue(this);


        String url = "http://indicadoreseconomicos.bccr.fi.cr/indicadoreseconomicos/WebServices/wsIndicadoresEconomicos.asmx/ObtenerIndicadoresEconomicos?tcIndicador=3151&tcFechaInicio=11/10/2017&tcFechaFinal=11/10/2017&tcNombre=string&tnSubNiveles=S";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
//                        mTextView.setText("Response is: "+ response.substring(0,500));
                        Log.d("Para que",response);

                        try {

                            //handle XML
                            SAXParserFactory spf = SAXParserFactory.newInstance();
                            SAXParser sp = spf.newSAXParser();
                            XMLReader xr = sp.getXMLReader();

                            //URL to parse XML Tags
//                            URL sourceUrl = new URL(
//                                    "http://indicadoreseconomicos.bccr.fi.cr/indicadoreseconomicos/WebServices/wsIndicadoresEconomicos.asmx/ObtenerIndicadoresEconomicos?tcIndicador=3151&tcFechaInicio=11/10/2017&tcFechaFinal=11/10/2017&tcNombre=string&tnSubNiveles=S");

                            //Create handler to handle XML Tags ( extends DefaultHandler )
                            MyXMLHandler myXMLHandler = new MyXMLHandler(bancos.get(0));

                            /////dddd

                            //////dddd
                            xr.setContentHandler(myXMLHandler);

                            xr.parse(new InputSource(new StringReader(response)));

                            Log.d("MAMAMIA",myXMLHandler.getBanco().getTipoCambio().getVenta()+"");

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
        queue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
