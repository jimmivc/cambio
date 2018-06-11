package com.shimmi.tipodecambio.xml;

import android.util.Log;

import com.shimmi.tipodecambio.objects.Banco;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Jimmi on 22/08/2017.
 */

public class MyXMLHandler extends DefaultHandler {

    public MyXMLHandler(Banco banco){
        super();
        this.banco = banco;
    }

    String codigo = null;
    Boolean currentElement = false;
    String currentValue = null;

    private Banco banco;

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }



//    public static void setProgramList(ProgramList programList) {
//        MyXMLHandler.programList = programList;
//    }

    //called when tag starts ( ex:- <tblPrograms>diffgr:id="tblPrograms1" msdata:rowOrder="0"</tblPrograms>  -- <tblPrograms> )
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        currentElement = true;

        //set up for hierarchy scan to place data within category
        if (localName.equals("DataSet"))
        {
            /** Start */
            Log.d("INICIANDO","IZI");

//            bancos = new ArrayList<Banco>();

        }
//        else if (localName.equals("INGC011_CAT_INDICADORECONOMIC")){//"NewDataSet" - DOES NOT WORK - F.C.
//            /** Get attribute value */
//            String attr = attributes.getValue(0);
//            String attr2 = attributes.getValue(1);
//
//
////            programList.setTable(attr);
////            programList.setRowOrder(attr2);
//            Log.d("ka","IZI");
//
//        }

    }

    //called when tag closing ( ex:- <Program>Ancillary</Program>  -- </Program> )
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        currentElement = false;

        /** set value */
        if (localName.equalsIgnoreCase("COD_INDICADORINTERNO")){
            codigo = currentValue;
        }
        if (localName.equalsIgnoreCase("DES_FECHA")){
//                banco.getTipoCambio().setFecha(new Date(currentValue));
        }
        if (localName.equalsIgnoreCase("NUM_VALOR")){
            if (codigo.equals(banco.getTipoCambio().getCodigoVenta())){
                banco.getTipoCambio().setVenta(Double.parseDouble(currentValue));
            }else {
                banco.getTipoCambio().setCompra(Double.parseDouble(currentValue));
            }
        }

        //programList.setProgram(currentValue);
        //else if (localName.equalsIgnoreCase("tblPrograms"))
        //programList.setWebsite(currentValue);

    }

    //called to get tag characters ( ex:- <Program>Ancillary</Program> -- to get Ancillary character )
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

        if (currentElement) {
            currentValue = new String(ch, start, length);
            currentElement = false;
        }
    }
}
