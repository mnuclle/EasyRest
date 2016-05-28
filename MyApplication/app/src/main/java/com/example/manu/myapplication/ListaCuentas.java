package com.example.manu.myapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
<<<<<<< HEAD
=======
import android.support.v7.app.AppCompatActivity;
>>>>>>> origin/master
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonToken;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
<<<<<<< HEAD
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manu.myapplication.Entidades.CuentasMesa;
import com.example.manu.myapplication.Entidades.DetallePedido;
=======
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manu.myapplication.Entidades.CuentasMesa;
import com.example.manu.myapplication.Entidades.DetallesPedido;
>>>>>>> origin/master
import com.example.manu.myapplication.Entidades.Pedidos;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
<<<<<<< HEAD
=======
import java.util.Date;
>>>>>>> origin/master

public class ListaCuentas extends ListActivity implements
        AdapterView.OnItemClickListener
{
    private CuentasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_cuentas);

        adapter = new CuentasAdapter();

        setListAdapter(adapter);

        getListView().setOnItemClickListener(this);

        loadCuentasData();


    }

    private void loadCuentasData() {

        Intent intent = getIntent();
        ArrayList<CuentasMesa> listaCuentas = (ArrayList<CuentasMesa>)intent.getExtras().get("LISTA");
        CuentasMesa info;
        for (CuentasMesa cm : listaCuentas
             ) {
            info = cm;
            adapter.addCuentasInfo(info);
        }

        /*
        info.setNombreCuenta("Juan Perez");
        info.setNumeroDocumento(35966806);
        info.setNumeroMesa("2-3-4");
        info.setMontoPedido(0);
        adapter.addCuentasInfo(info);

        info = new CuentasMesa();
        info.setNombreCuenta("Manuel Calle");
        info.setNumeroDocumento(34555888);
        info.setNumeroMesa("5-6-7");
        info.setMontoPedido(250);
        adapter.addCuentasInfo(info);

        info = new CuentasMesa();
        info.setNombreCuenta("Daniel Peres");
        info.setNumeroDocumento(32654987);
        info.setNumeroMesa("1");
        info.setMontoPedido(360.50);
        adapter.addCuentasInfo(info);


        info = new CuentasMesa();
        info.setNombreCuenta("Karen Valverde");
        info.setNumeroDocumento(39658896);
        info.setNumeroMesa("9");
        info.setMontoPedido(0);
        adapter.addCuentasInfo(info);


        info = new CuentasMesa();
        info.setNombreCuenta("Catherine Huguet");
        info.setNumeroDocumento(45678912);
        info.setNumeroMesa("8");
        info.setMontoPedido(1205);
        adapter.addCuentasInfo(info);

        info = new CuentasMesa();
        info.setNombreCuenta("Daniel Peres");
        info.setNumeroDocumento(32654987);
        info.setNumeroMesa("1");
        info.setMontoPedido(360.50);
        adapter.addCuentasInfo(info);


        info = new CuentasMesa();
        info.setNombreCuenta("Karen Valverde");
        info.setNumeroDocumento(39658896);
        info.setNumeroMesa("9");
        info.setMontoPedido(0);
        adapter.addCuentasInfo(info);


        info = new CuentasMesa();
        info.setNombreCuenta("Catherine Huguet");
        info.setNumeroDocumento(45678912);
        info.setNumeroMesa("8");
        info.setMontoPedido(1205);
        adapter.addCuentasInfo(info);
*/
        adapter.notifyDataSetChanged();

    }


    class CuentasAdapter extends BaseAdapter {
        private ArrayList<CuentasMesa> cuentasMesa;
        private LayoutInflater inflater;

        public CuentasAdapter() {
            cuentasMesa = new ArrayList<CuentasMesa>();
            inflater = LayoutInflater.from(ListaCuentas.this);
        }

        public void addCuentasInfo(CuentasMesa info) {
            if (info != null) {
                cuentasMesa.add(info);
            }
        }

        @Override
        public int getCount() {
            return cuentasMesa.size();
        }

        @Override
        public Object getItem(int position) {
            return cuentasMesa.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class Holder {
            private TextView txtCuenta;
            private TextView txtMesas;
            private TextView txtMontoPedido;
            private ImageView imgIcono;
                    }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            Holder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.elemento_lista, null);
                holder = new Holder();
                holder.imgIcono = (ImageView) convertView
                        .findViewById(R.id.icono);
                holder.txtCuenta = (TextView) convertView
                        .findViewById(R.id.cuenta);
                holder.txtMesas = (TextView) convertView
                        .findViewById(R.id.mesas);
                holder.txtMontoPedido = (TextView) convertView
                        .findViewById(R.id.montoPedido);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            CuentasMesa info = (CuentasMesa) getItem(position);

            holder.txtCuenta.setText(info.getNombreCuenta() + " - " + info.getNumeroDocumento());
            holder.txtMesas.setText(info.getNumeroMesa());
            holder.txtMontoPedido.setText("$" + info.getMontoPedido());

            return convertView;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {
        CuentasMesa cm = (CuentasMesa)getListAdapter().getItem(position);
        int idCliente = cm.getIdCliente();
        Object[] listaObjetos = new Object[5];
        listaObjetos[0] = idCliente;

        new GetTask().execute(listaObjetos);

    }


    class GetTask extends AsyncTask<Object,String,String> {

        private Exception exception;
        private Pedidos pedido = new Pedidos();
<<<<<<< HEAD
        private ArrayList<DetallePedido> listaDetalles = new ArrayList<>();
=======
        private ArrayList<DetallesPedido> listaDetalles = new ArrayList<>();
>>>>>>> origin/master

        protected String doInBackground(Object... params) {
            try {
                String response = "No se conecto";
                try {

                        HttpURLConnection urlConn;
                        StringBuilder result = new StringBuilder();
                        URL url = new URL("http://172.16.0.2:8082/api/pedido/obtenerPedido/" +((int) params[0]));

                        urlConn = (HttpURLConnection) url.openConnection();

                        InputStream in = new BufferedInputStream(urlConn.getInputStream());

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }
                    try {
                        JsonReader reader1 = new JsonReader(new InputStreamReader(new ByteArrayInputStream(result.toString().getBytes(StandardCharsets.UTF_8))));
                        response = "GET: ";

                        reader1.beginArray();
                        while (reader1.hasNext()) {
                            int idPedido = -1;
                            String fechaAlta="";
                            String fechaBaja="";
                            int idEstado = -1;
                            int idCuenta = -1;
                            reader1.beginObject();
                            while (reader1.hasNext()) {
                                String name = reader1.nextName();
                                switch (name) {
                                    case "idPedido":
                                        idPedido = reader1.nextInt();
                                        break;
                                    case "fechaAlta":
                                        fechaAlta = reader1.nextString();
                                        break;
                                    case "fechaBaja":

                                        if (reader1.peek() == JsonToken.NULL) {
                                            fechaBaja = "";
                                            reader1.skipValue();
                                        } else {
                                            fechaBaja = reader1.nextString();
                                        }
                                        break;
                                    case "idEstado":
                                        idEstado = reader1.nextInt();
                                        break;
                                    case "idCuenta":
                                        idCuenta = reader1.nextInt();
                                        break;
                                    default:
                                        reader1.skipValue();
                                        break;
                                }
                            }
                            pedido.setIdPedido(idPedido);
                            pedido.setIdCuenta(idCuenta);
                            pedido.setIdEstado(idEstado);
//                                response += "\nMesa: " + id + " " + cantSillas + " " + idCuenta + " " + posicion + " " + numeroMesa + ".\n";
                            reader1.endObject();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        urlConn.disconnect();
                    }
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
                {
                    if (pedido.getIdPedido() != -1) {
                        try {
                            HttpURLConnection urlConn;
                            StringBuilder result = new StringBuilder();
                            URL url = new URL("http://172.16.0.2:8082/api/pedido/obtenerDetallesPedido/" + pedido.getIdPedido());

                            urlConn = (HttpURLConnection) url.openConnection();

                            InputStream in = new BufferedInputStream(urlConn.getInputStream());

                            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                            String line;
                            while ((line = reader.readLine()) != null) {
                                result.append(line);
                            }
                            try {
                                JsonReader reader1 = new JsonReader(new InputStreamReader(new ByteArrayInputStream(result.toString().getBytes(StandardCharsets.UTF_8))));
                                response = "GET: ";

                                reader1.beginArray();
                                while (reader1.hasNext()) {
<<<<<<< HEAD
                                    DetallePedido detallePed = new DetallePedido();
=======
                                    DetallesPedido detallePed = new DetallesPedido();
>>>>>>> origin/master
                                    int idDetallePedido = -1;
                                    int idPedido = -1;
                                    int cantidad = -1;
                                    int idMenu = -1;
                                    int idEstado = -1;
                                    int idInsumo = -1;
                                    double totalDetalle = -1;
                                    reader1.beginObject();
                                    while (reader1.hasNext()) {
                                        String name = reader1.nextName();
                                        switch (name) {
                                            case "idDetallePedido":
                                                idDetallePedido = reader1.nextInt();
                                                break;
                                            case "idPedido":
                                                idPedido = reader1.nextInt();
                                                break;
                                            case "idMenu":
                                                if (reader1.peek() == JsonToken.NULL) {
                                                    idMenu = 0;
                                                    reader1.skipValue();
                                                } else {
                                                    idMenu = reader1.nextInt();
                                                }
                                                break;
                                            case "idInsumo":
                                                if (reader1.peek() == JsonToken.NULL) {
                                                    idInsumo = 0;
                                                    reader1.skipValue();
                                                } else {
                                                    idInsumo = reader1.nextInt();
                                                }
                                                break;
                                            case "cantidad":
                                                cantidad = reader1.nextInt();
                                                break;
                                            case "totalDetalle":
                                                totalDetalle = reader1.nextDouble();
                                                break;
                                            case "idEstado":
                                                idEstado = reader1.nextInt();
                                                break;
                                            default:
                                                reader1.skipValue();
                                                break;
                                        }
                                    }
//                                response += "\nMesa: " + id + " " + cantSillas + " " + idCuenta + " " + posicion + " " + numeroMesa + ".\n";
                                    reader1.endObject();
                                    detallePed.setIdPedido(idPedido);
                                    detallePed.setCantidad(cantidad);
                                    detallePed.setIdDetallePedido(idDetallePedido);
                                    detallePed.setIdInsumo(idInsumo);
                                    detallePed.setIdMenu(idMenu);
                                    detallePed.setIdEstado(idEstado);
                                    detallePed.setTotalDetalle(totalDetalle);

                                    listaDetalles.add(detallePed);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                urlConn.disconnect();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
<<<<<<< HEAD
                        for (DetallePedido det :
=======
                        for (DetallesPedido det :
>>>>>>> origin/master
                                listaDetalles) {
                            if (det.getIdInsumo() == 0) {
                                try {
                                    HttpURLConnection urlConn;
                                    StringBuilder result = new StringBuilder();
                                    URL url = new URL("http://172.16.0.2:8082/api/pedido/obtenerMenuDetallePedido/" + det.getIdMenu());

                                    urlConn = (HttpURLConnection) url.openConnection();

                                    InputStream in = new BufferedInputStream(urlConn.getInputStream());

                                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                                    String line;
                                    while ((line = reader.readLine()) != null) {
                                        result.append(line);
                                    }
                                    try {
                                        JsonReader reader1 = new JsonReader(new InputStreamReader(new ByteArrayInputStream(result.toString().getBytes(StandardCharsets.UTF_8))));
                                        response = "GET: ";

                                        reader1.beginArray();
                                        while (reader1.hasNext()) {
                                            int idMenu = -1;
                                            String nombre = "";
                                            double precio = -1;
                                            int idCategoria = -1;
                                            reader1.beginObject();
                                            while (reader1.hasNext()) {
                                                String name = reader1.nextName();
                                                switch (name) {
                                                    case "idMenu":
                                                        idMenu = reader1.nextInt();
                                                        break;
                                                    case "nombre":
                                                        nombre = reader1.nextString();
                                                        break;
                                                    case "precio":
                                                        precio = reader1.nextDouble();
                                                        break;
                                                    case "idCategoria":
                                                        idCategoria = reader1.nextInt();
                                                        break;
                                                    default:
                                                        reader1.skipValue();
                                                        break;
                                                }
                                            }
//                                response += "\nMesa: " + id + " " + cantSillas + " " + idCuenta + " " + posicion + " " + numeroMesa + ".\n";
                                            reader1.endObject();
                                            det.setNombreMenu(nombre);
                                            det.setPrecio(precio);
                                            det.setIdCategoria(idCategoria);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        urlConn.disconnect();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                            else
                            {
                                try {
                                    HttpURLConnection urlConn;
                                    StringBuilder result = new StringBuilder();
                                    URL url = new URL("http://172.16.0.2:8082/api/pedido/obtenerInsumoDetallePedido/" + det.getIdInsumo());

                                    urlConn = (HttpURLConnection) url.openConnection();

                                    InputStream in = new BufferedInputStream(urlConn.getInputStream());

                                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                                    String line;
                                    while ((line = reader.readLine()) != null) {
                                        result.append(line);
                                    }
                                    try {
                                        JsonReader reader1 = new JsonReader(new InputStreamReader(new ByteArrayInputStream(result.toString().getBytes(StandardCharsets.UTF_8))));
                                        response = "GET: ";

                                        reader1.beginArray();
                                        while (reader1.hasNext()) {
                                            int idInsumo = -1;
                                            String nombre = "";
                                            String descripcion = "";
                                            double precio = -1;
                                            int idCategoria = -1;
                                            reader1.beginObject();
                                            while (reader1.hasNext()) {
                                                String name = reader1.nextName();
                                                switch (name) {
                                                    case "idInsumo":
                                                        idInsumo = reader1.nextInt();
                                                        break;
                                                    case "nombre":
                                                        nombre = reader1.nextString();
                                                        break;
                                                    case "descripcion":
                                                        descripcion = reader1.nextString();
                                                        break;
                                                    case "precio":
                                                        precio = reader1.nextDouble();
                                                        break;
                                                    case "idCategoria":
                                                        idCategoria = reader1.nextInt();
                                                        break;
                                                    default:
                                                        reader1.skipValue();
                                                        break;
                                                }
                                            }
//                                response += "\nMesa: " + id + " " + cantSillas + " " + idCuenta + " " + posicion + " " + numeroMesa + ".\n";
                                            reader1.endObject();
                                            det.setNombreMenu(nombre);
                                            det.setPrecio(precio);
                                            det.setIdCategoria(idCategoria);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        urlConn.disconnect();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        Intent intent = new Intent(ListaCuentas.this, PedidosActivity.class);
                        intent.putExtra("LISTADETALLES", listaDetalles);
<<<<<<< HEAD
                        intent.putExtra("IDCLIENTE",((int) params[0]));
=======
>>>>>>> origin/master
                        startActivity(intent);
                    }
                }

            } catch (Exception e) {
                this.exception = e;

            }
            return null;
        }

        protected void onPostExecute(String st) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_cuentas, menu);
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

