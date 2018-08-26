package com.shimmi.tipodecambio;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.shimmi.tipodecambio.objects.Banco;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //create object For SiteList class
    ArrayList<Banco> bancos = new ArrayList<Banco>();
    ViewDataBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bancos.get(0).setNombre("OMG sirvio");
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Banco b = new Banco("BN",this);
        bancos.add(b);
//        bancos.add(new Banco("Bac",this));
//        bancos.add(new Banco("BCR",this));

        requestCurrency();
        BankAdapter adapter = new BankAdapter(bancos,this.getBaseContext());
        ((ListView) findViewById(R.id.lstBanks)).setAdapter(adapter);

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                test();
//            }
//        }, 2000);   //5 seconds
    }

    private void requestCurrency(){
        bancos.add(new Banco("BCT",this));
        bancos.add(new Banco("HSBC",this));
//        bancos.add(new Banco("Citibank",this));
        bancos.add(new Banco("Cathay",this));
        bancos.add(new Banco("Bac",this));
        bancos.add(new Banco("Lafise",this));
        bancos.add(new Banco("Improsa",this));
//        bancos.add(new Banco("Interfin",this));
        bancos.add(new Banco("Scotiabank",this));
        bancos.add(new Banco("General",this));
        bancos.add(new Banco("BCR",this));
        bancos.add(new Banco("Popular",this));

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
