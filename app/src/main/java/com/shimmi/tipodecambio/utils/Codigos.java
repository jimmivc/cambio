package com.shimmi.tipodecambio.utils;

import com.shimmi.tipodecambio.objects.Banco;
import com.shimmi.tipodecambio.objects.TipoCambio;

import java.util.HashMap;
import java.util.Map;

public class Codigos {

    static private Codigos codigos;
    private HashMap<String,String[]> codigosBanco;

    private Codigos(){
        codigosBanco = new HashMap<>();
        //venta,compra

        codigosBanco.put("BN",new String[]{"3208","3151"});
        codigosBanco.put("BCT",new String[]{"3210","3151"});
        codigosBanco.put("HSBC",new String[]{"3211","3152"});
//        codigosBanco.put("Citibank",new String[]{"3153","3212"});
        codigosBanco.put("Cathay",new String[]{"3214","3180"});
        codigosBanco.put("Bac",new String[]{"3215","3181"});
        codigosBanco.put("Lafise",new String[]{"3217","3183"});
        codigosBanco.put("Improsa",new String[]{"3218","3184"});
//        codigosBanco.put("Interfin",new String[]{"3185","3220"});
        codigosBanco.put("Scotiabank",new String[]{"3221","3186"});
        codigosBanco.put("General",new String[]{"3568","3187"});
        codigosBanco.put("BCR",new String[]{"3207","3148"});
        codigosBanco.put("Popular",new String[]{"3209","3179"});


    }

    public String[] getCodeByBank(String name){
        return codigosBanco.get(name);
    }

    static public Codigos getInstance(){
        if(codigos!=null)
            return codigos;
        else
            codigos = new Codigos();

        return codigos;
    }
}
