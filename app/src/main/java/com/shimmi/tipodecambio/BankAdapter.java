package com.shimmi.tipodecambio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shimmi.tipodecambio.objects.Banco;

import java.util.ArrayList;
import java.util.List;

public class BankAdapter extends BaseAdapter {

    private ArrayList<Banco> banks;
//    private View view;
    private Context context;

    public BankAdapter(ArrayList<Banco> pbanks, Context pcontext){
        banks = pbanks;
        context = pcontext;
    }

    @Override
    public int getCount() {
        return banks.size();
    }

    @Override
    public Object getItem(int position) {
        return banks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        if (view == null)
//            view = convertView.inflate(context,R.layout.bank_item,null);


        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.bank_item,null);
        }

        ((TextView)convertView.findViewById(R.id.textView)).setText(banks.get(position).getNombre());

        return convertView;
    }
}
