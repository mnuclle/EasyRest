package com.example.manu.myapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.manu.myapplication.Entidades.DetallesPedido;
import com.example.manu.myapplication.Entidades.TodasImages;
import java.util.ArrayList;






public class PedidosActivity extends ListActivity implements AdapterView.OnItemClickListener{

    private PedidosAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);


        adapter = new PedidosAdapter();

        setListAdapter(adapter);

        getListView().setOnItemClickListener(this);

        loadPedidosData();

    }
    private void loadPedidosData() {

        Intent intent = getIntent();
        ArrayList<DetallesPedido> listaDetalle = (ArrayList<DetallesPedido>)intent.getExtras().get("LISTADETALLES");
        DetallesPedido info;

        for (DetallesPedido cm : listaDetalle
                ) {
            info = cm;
            adapter.addDetallesInfo(info);
        }
        adapter.notifyDataSetChanged();

    }


    class PedidosAdapter extends BaseAdapter {
        private ArrayList<DetallesPedido> listaDetallesPedidos;
        private LayoutInflater inflater;
        private TodasImages ti;

        public PedidosAdapter() {
            listaDetallesPedidos = new ArrayList<DetallesPedido>();
            inflater = LayoutInflater.from(PedidosActivity.this);
            ti = new TodasImages();
        }

        public void addDetallesInfo(DetallesPedido info) {
            if (info != null) {
                listaDetallesPedidos.add(info);
            }
        }

        @Override
        public int getCount() {
            return listaDetallesPedidos.size();
        }

        @Override
        public Object getItem(int position) {
            return listaDetallesPedidos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class Holder {
            private TextView txtNombreMenu;
            private TextView txtCantidad;
            private TextView txtMontoDetalle;
            private ImageView imageMenu;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {

            Holder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.elemento_lista_pedido, null);
                holder = new Holder();
                holder.imageMenu = (ImageView) convertView
                        .findViewById(R.id.imagenMenu);
                holder.txtNombreMenu = (TextView) convertView
                        .findViewById(R.id.nombreMenu);
                holder.txtCantidad = (TextView) convertView
                        .findViewById(R.id.cantidadMenu);
                holder.txtMontoDetalle = (TextView) convertView
                        .findViewById(R.id.montoDetalle);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            DetallesPedido info = (DetallesPedido) getItem(position);
            if(info.getIdInsumo() == 0)
            {
                holder.imageMenu.setImageResource((ti.obtenerImagen(info.getIdMenu(), true)).getIdImagen());
            }
            else
            {
                holder.imageMenu.setImageResource((ti.obtenerImagen(info.getIdInsumo(),false)).getIdImagen());
            }

            holder.txtNombreMenu.setText(info.getNombreMenu()+"sadasdasdasdsadasd asd sa dasd sad asddasdasdasdadasdfdsfs sdf sdfsdfsdf dsfs dsfsdfsdfsd ds fdsfsdfsdfsdd");
            String cantidad = "" + info.getCantidad();
            holder.txtCantidad.setText(cantidad);
            holder.txtMontoDetalle.setText("$" + info.getTotalDetalle());

            return convertView;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {
        DetallesPedido cm = (DetallesPedido)getListAdapter().getItem(position);
        String nombre = cm.getNombreMenu();
        Toast.makeText(this, "nombre menu:" + nombre, Toast.LENGTH_LONG).show();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pedidos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }






}
