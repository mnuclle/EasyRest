package com.example.manu.myapplication.Cliente;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.manu.myapplication.Entidades.DetallePedido;
import com.example.manu.myapplication.R;

import java.util.ArrayList;

public class ClienteVerCuentaActivity extends ListActivity {

    private int NroCliente;
    private int positionLista;
    private VerCuentaAdapter adapter;
    private String URLGlobal;
    private ArrayList<DetallePedido> listaMenusAConfirmar;
    private TextView totalResumenCuenta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_ver_cuenta);

        Intent intent = getIntent();
        NroCliente = (int) intent.getExtras().get("IDCLIENTE");
        URLGlobal = intent.getExtras().get("URLGlobal").toString();
        totalResumenCuenta = (TextView) findViewById(R.id.totalResumenCuenta);


        /**/

        adapter = new VerCuentaAdapter();

        setListAdapter(adapter);
        loadPedidosData();

        Typeface type = Typeface.createFromAsset(getAssets(),"segoeui.ttf");
        totalResumenCuenta.setTypeface(type);

    }

    private void loadPedidosData() {
        Double montoDetalles = 0.0;
        Intent intent = getIntent();
        adapter.clear();
        ArrayList<DetallePedido> listaDetalleVer = (ArrayList<DetallePedido>) intent.getExtras().get("LISTADETALLES");
        DetallePedido info;
        for (DetallePedido cm : listaDetalleVer
                ) {
            info = cm;
            montoDetalles = montoDetalles + cm.getTotalDetalle();
            adapter.addDetallesInfo(info);

        }
        totalResumenCuenta.setText("$" +String.format("%.2f", montoDetalles));
        adapter.notifyDataSetChanged();

    }

    class VerCuentaAdapter extends BaseAdapter {
        private ArrayList<DetallePedido> listaDetallesPedidos;

        private LayoutInflater inflater;

        public VerCuentaAdapter() {
            listaDetallesPedidos = new ArrayList<>();
            inflater = LayoutInflater.from(ClienteVerCuentaActivity.this);
        }


        public void addDetallesInfo(DetallePedido info) {
            if (info != null) {
                listaDetallesPedidos.add(info);
            }
        }


        public ArrayList<DetallePedido> getList() {
            return listaDetallesPedidos;
        }

        @Override
        public int getCount() {
            return listaDetallesPedidos.size();
        }

        @Override
        public Object getItem(int position) {
            return listaDetallesPedidos.get(position);
        }

        public DetallePedido remove(int position) {
            DetallePedido det = listaDetallesPedidos.remove(position);
            return det;
        }

        public void clear() {
            listaDetallesPedidos = new ArrayList<>();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class Holder {
            private TextView txtCantidadMenuVerCuenta;
            private TextView txtNombreMenuVerCuenta;
            private TextView txtPrecioIndivDetalleVerCuenta;
            private TextView txtMontoDetalleVerCuenta;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {

            Holder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.elemento_ver_cuenta_menu, null);
                holder = new Holder();

                holder.txtCantidadMenuVerCuenta = (TextView) convertView
                        .findViewById(R.id.txtCantidadMenuVerCuenta);
                holder.txtPrecioIndivDetalleVerCuenta = (TextView) convertView
                        .findViewById(R.id.txtPrecioIndivDetalleVerCuenta);
                holder.txtMontoDetalleVerCuenta = (TextView) convertView
                        .findViewById(R.id.txtMontoDetalleVerCuenta);
                holder.txtNombreMenuVerCuenta = (TextView) convertView
                        .findViewById(R.id.txtNombreMenuVerCuenta);
                convertView.setTag(holder);

            } else {
                holder = (Holder) convertView.getTag();
            }

            DetallePedido info = (DetallePedido) getItem(position);

            String categoria = "";
            switch (info.getIdCategoria()){
                case 1 :
                    categoria = "MINUTAS";
                    break;
                case 2 :
                    categoria = "LOMITOS";
                    break;
                case 3 :
                    categoria = "PASTAS";
                    break;
                case 4 :
                    categoria = "TABLAS";
                    break;
                case 5 :
                    categoria = "BEBIDAS";
                    break;
                case 6 :
                    categoria = "PIZZAS";
                    break;
                case 7 :
                    categoria = "POSTRES";
                    break;
                case 8 :
                    categoria = "DESAYUNO";
                    break;

            }
            holder.txtNombreMenuVerCuenta.setText(info.getNombreMenu() + " (" + categoria + ")");
            holder.txtCantidadMenuVerCuenta.setText(""+info.getCantidad());
            holder.txtMontoDetalleVerCuenta.setText("$"+String.format("%.2f", info.getTotalDetalle()));
            holder.txtPrecioIndivDetalleVerCuenta.setText("$"+String.format("%.2f",info.getPrecio()));

            String estado = "";
            if (info.getIdEstado() == 0)
                estado = "PENDIENTE DE CONFIRMACION";
            if (info.getIdEstado() == 10)
                estado = "EN PREPARACION";
            if (info.getIdEstado() == 11)
                estado = "LISTO";
            if (info.getIdEstado() == 12)
                estado = "REGISTRADO";
            if (info.getIdEstado() == 13)
                estado = "CANCELADO";
            if (info.getIdEstado() == 14)
                estado = "ANULADO";
            if (info.getIdEstado() == 15)
                estado = "ENTREGADO";
            if (info.getIdEstado() == 16)
                estado = "COBRADO";
            if (info.getIdEstado() == 25)
                estado = "COBRO PARCIAL";

            Typeface type = Typeface.createFromAsset(getAssets(),"segoeui.ttf");
            holder.txtNombreMenuVerCuenta.setTypeface(type);
            holder.txtCantidadMenuVerCuenta.setTypeface(type);
            holder.txtMontoDetalleVerCuenta.setTypeface(type);
            holder.txtPrecioIndivDetalleVerCuenta.setTypeface(type);

            return convertView;
        }

    }



}
