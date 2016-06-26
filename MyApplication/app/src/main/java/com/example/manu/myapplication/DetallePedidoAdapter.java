package com.example.manu.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Danielito on 23/05/2016.
 */
public class DetallePedidoAdapter extends BaseAdapter {

    private ArrayList<DetallePedido> listaPedidos;
    private LayoutInflater inflater;

    public DetallePedidoAdapter() {
        listaPedidos = new ArrayList<DetallePedido>();
    }

    public DetallePedidoAdapter(ArrayList<DetallePedido> pedidos)
    {
        listaPedidos = pedidos;
    }

    public DetallePedidoAdapter(Context c)
    {
        listaPedidos = new ArrayList<DetallePedido>();
        inflater = LayoutInflater.from(c);
    }


    public ArrayList<DetallePedido> getPedidos()
    {
        return listaPedidos;}

    public void setPedidos(ArrayList<DetallePedido> pedidos)
    {
        listaPedidos = pedidos;
    }


    public void addPedido(DetallePedido p)
    {
        if (p != null)
            listaPedidos.add(p);
    }

    @Override
    public int getCount() {
        return listaPedidos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaPedidos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void remove(int position)
    {
        listaPedidos.remove(position);
    }


    class Holder {
        private TextView txtNombrePedido;
        private TextView txtCantidadPedido;
        private TextView txtEstadoPedido;
        private TextView txtPreferenciaPedido;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        final Holder holder;

        if (convertView == null) {
            holder = new Holder();
            convertView = inflater.inflate(R.layout.detalle_pedido,arg2,false);
            holder.txtNombrePedido = (TextView) convertView
                    .findViewById(R.id.txtNombrePedido);
            holder.txtCantidadPedido = (TextView) convertView
                    .findViewById(R.id.txtCantidadPedido);
            holder.txtEstadoPedido = (TextView) convertView
                    .findViewById(R.id.txtEstadoPedido);
            holder.txtPreferenciaPedido = (TextView) convertView
                    .findViewById(R.id.txtPreferenciaPedido);


            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        DetallePedido item = (DetallePedido) this.getItem(position);


        holder.txtNombrePedido.setText(item.getNombre());
        holder.txtCantidadPedido.setText(""+item.getCantidad());
        holder.txtEstadoPedido.setText(""+item.getNombreEstado());
        holder.txtPreferenciaPedido.setText(""+item.getObservacion());

        return convertView;
    }

}

