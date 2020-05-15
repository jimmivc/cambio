package com.shimmi.tipodecambio;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shimmi.tipodecambio.objects.Banco;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class BankCheckList extends Activity {
    ArrayList<Banco> bancos;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_checklist);

//        if(getArrayList("banks")){

//        }

        bancos = loadSwitches();
//        bancos.add(new Banco("BN",true));
//        bancos.add(new Banco("BCT",false));
//        bancos.add(new Banco("HSBC", false));
//        bancos.add(new Banco("Cathay",false));
//        bancos.add(new Banco("Bac",false));
//        bancos.add(new Banco("Lafise",false));
//        bancos.add(new Banco("Improsa",false));
//        bancos.add(new Banco("Scotiabank",false));
//        bancos.add(new Banco("General",true));
//        bancos.add(new Banco("BCR",false));
//        bancos.add(new Banco("Popular",false));
        CheckListAdapter adapter = new CheckListAdapter(bancos,this.getBaseContext());
        ((ListView) findViewById(R.id.lstBanks)).setAdapter(adapter);

//        banks = getArrayList("banks");
    }

    @Override
    public void onBackPressed() {
        saveArrayList(bancos);
        setResult(Activity.RESULT_OK);
        finish();
    }

    public void saveArrayList(ArrayList<Banco> banksList){
        String key = "banks";
        ArrayList<String> list = new ArrayList<>();

        for (Banco b:banksList) {
            if (b.isState()) {
                list.add(b.getNombre());
            }
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public ArrayList<Banco> loadSwitches() {
        ArrayList<Banco> bancos = new ArrayList<>();
        bancos.add(new Banco("BN",false));
        bancos.add(new Banco("BCT",false));
        bancos.add(new Banco("HSBC", false));
        bancos.add(new Banco("Cathay",false));
        bancos.add(new Banco("Bac",false));
        bancos.add(new Banco("Lafise",false));
        bancos.add(new Banco("Improsa",false));
        bancos.add(new Banco("Scotiabank",false));
        bancos.add(new Banco("General",false));
        bancos.add(new Banco("BCR",false));
        bancos.add(new Banco("Popular",false));

        for (String nombre:getArrayList("banks")) {
            for (Banco bank:bancos) {
                if(nombre.equals(bank.getNombre())){
                    bank.setState(true);
                    break;
                }
            }
        }
        return bancos;
    }

    public ArrayList<String> getArrayList(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = prefs.getString(key,"[]");
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
