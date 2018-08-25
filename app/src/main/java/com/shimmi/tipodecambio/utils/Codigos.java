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
        codigosBanco.put("BN",new String[]{"3149","3208"});
        codigosBanco.put("BCT",new String[]{"3151","3210"});
        codigosBanco.put("HSBC",new String[]{"3152","3211"});
        codigosBanco.put("Citibank",new String[]{"3153","3212"});
        codigosBanco.put("Cathay",new String[]{"3180","3214"});
        codigosBanco.put("Bac",new String[]{"3181","3215"});
        codigosBanco.put("Lafise",new String[]{"3183","3217"});
        codigosBanco.put("Improsa",new String[]{"3184","3218"});
//        codigosBanco.put("Interfin",new String[]{"3185","3220"});
        codigosBanco.put("Scotiabank",new String[]{"3186","3221"});
        codigosBanco.put("General",new String[]{"3187","3568"});
        codigosBanco.put("BCR",new String[]{"3148","3207"});
        codigosBanco.put("Popular",new String[]{"4179","3209"});
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
