package com.shimmi.tipodecambio;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shimmi.tipodecambio.objects.Banco;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class BankCheckList extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_checklist);

//        if(getArrayList("banks")){

//        }

        ArrayList<Banco> bancos = new ArrayList<>();
        bancos.add(new Banco("BN",R.mipmap.bn,this));
        bancos.add(new Banco("BCT",R.mipmap.bct,this));
        bancos.add(new Banco("HSBC",R.mipmap.hsbc,this));
        bancos.add(new Banco("Cathay",R.mipmap.cathay,this));
        bancos.add(new Banco("Bac",R.mipmap.bac,this));
        bancos.add(new Banco("Lafise",R.mipmap.lafise,this));
        bancos.add(new Banco("Improsa",R.mipmap.improsa,this));
        bancos.add(new Banco("Scotiabank",R.mipmap.scot,this));
        bancos.add(new Banco("General",R.mipmap.general,this));
        bancos.add(new Banco("BCR",R.mipmap.bcr,this));
        bancos.add(new Banco("Popular",R.mipmap.popular,this));
        CheckListAdapter adapter = new CheckListAdapter(bancos,this.getBaseContext());
        ((ListView) findViewById(R.id.lstBanks)).setAdapter(adapter);
    }

    public void saveArrayList(ArrayList<String> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public ArrayList<String> getArrayList(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = prefs.getString(key,null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
