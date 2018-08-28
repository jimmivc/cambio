package com.shimmi.tipodecambio;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.shimmi.tipodecambio.objects.Banco;

import java.util.ArrayList;

public class BankCheckList extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_checklist);

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
        BankAdapter adapter = new BankAdapter(bancos,this.getBaseContext());
        ((ListView) findViewById(R.id.lstBanks)).setAdapter(adapter);
    }
}
