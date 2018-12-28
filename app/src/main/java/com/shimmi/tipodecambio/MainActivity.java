package com.shimmi.tipodecambio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shimmi.tipodecambio.objects.Banco;

import java.lang.reflect.Type;
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
                startActivityForResult(new Intent(MainActivity.this,BankCheckList.class),19);
            }
        });
        loadBanks();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 19){
            if(resultCode== RESULT_OK){
                loadBanks();
            }
        }
    }

    public ArrayList<String> getArrayList(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = prefs.getString(key,"[]");
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    private void loadBanks(){
        bancos.clear();
        for (String b:getArrayList("banks")){
            bancos.add(new Banco(b,this));
        }
        if(bancos.size()!=0) {
            findViewById(R.id.lstBanks).setVisibility(View.VISIBLE);
            findViewById(R.id.txtAddMessage).setVisibility(View.GONE);
            BankAdapter adapter = new BankAdapter(bancos, this.getBaseContext());
            ((ListView) findViewById(R.id.lstBanks)).setAdapter(adapter);
        }else{
            findViewById(R.id.lstBanks).setVisibility(View.GONE);
            findViewById(R.id.txtAddMessage).setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void requestCurrency(){
        bancos.add(new Banco("BN",this));
        bancos.add(new Banco("BCT",this));
        bancos.add(new Banco("HSBC",this));
        bancos.add(new Banco("Cathay",this));
        bancos.add(new Banco("Bac",this));
        bancos.add(new Banco("Lafise",this));
        bancos.add(new Banco("Improsa",this));
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
