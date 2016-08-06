package com.example.manu.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Danielito on 26/06/2016.
 */
public class DialogMenuListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<String> listaTextos;

    public DialogMenuListAdapter()
    {
        listaTextos = new ArrayList<>();
        listaTextos.add("Prueba1");
        listaTextos.add("Prueba2");
    }
    @Override
    public int getCount() {
        return listaTextos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaTextos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public String getItemTexto(int position)
    {
        return listaTextos.get(position);
    }

    class Holder {
        private TextView textoItemDialog;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Holder holder;

        if (convertView == null) {
            holder = new Holder();
            convertView = inflater.inflate(R.layout.detalle_pedido, parent, false);
            holder.textoItemDialog = (TextView) convertView
                    .findViewById(R.id.textoItemDialog);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        holder.textoItemDialog.setText(getItemTexto(position));

        return convertView;
    }
}
