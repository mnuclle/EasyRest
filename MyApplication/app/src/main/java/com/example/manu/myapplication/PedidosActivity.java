package com.example.manu.myapplication;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonToken;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manu.myapplication.Entidades.DetallePedido;
import com.example.manu.myapplication.Entidades.DetallesPedido;
import com.example.manu.myapplication.Entidades.Pedidos;
import com.example.manu.myapplication.Entidades.TodasImages;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class PedidosActivity extends ListActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private Button btnElegirPedido, btnConfirmarPedido;
    private int NroCliente;
    private int NroOpcion = -1;
    private int positionLista;
    private PedidosAdapter adapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        /*Se agrega esto*/

        Intent intent = getIntent();
        NroCliente = (int) intent.getExtras().get("IDCLIENTE");

        btnElegirPedido = (Button) findViewById(R.id.btnElegirPedido);

        btnConfirmarPedido = (Button) findViewById(R.id.btnConfirmarPedido);

        btnElegirPedido.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                startActivityCategoria();
            }

        });

        btnConfirmarPedido.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Object[] obj = new Object[2];
                obj[0] = adapter.getList();

                new PostTask(v.getContext()).execute(obj);
            }

        });


        /**/

        adapter = new PedidosAdapter();

        setListAdapter(adapter);

        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);

        loadPedidosData();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Pedidos Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://host/path"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    class PostTask extends AsyncTask<Object, String, String> {

        private Exception exception;
        private Context contexto;
        private int error = 0;
        String response = "No se conecto";
        ArrayList<DetallePedido> lista;

        public PostTask(Context context) {
            this.contexto = context;
        }

        protected String doInBackground(Object... params) {
            try {

                StringBuilder result = new StringBuilder();
                URL url2 = new URL("http://172.16.0.2:8082/api/pedido/enviarPedido");
                HttpURLConnection urlConn = (HttpURLConnection) url2.openConnection();
                try {
                    lista = (ArrayList<DetallePedido>) params[0];
                    urlConn.setChunkedStreamingMode(0);
                    urlConn.setDoOutput(true);
                    urlConn.setDoInput(true);
                    urlConn.setRequestProperty("Content-Type", "application/json");
                    urlConn.setRequestMethod("POST");
                    urlConn.connect();


                    //Create JSONObject PEDIDO
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("idPedido", null);
                    jsonParam.put("fechaAlta", null);
                    jsonParam.put("fechaBaja", null);
                    jsonParam.put("idEstado", null);
                    jsonParam.put("idCuenta", NroCliente);

                    //create JSON ARRAY
                    JSONArray ja = new JSONArray();
                    for (DetallePedido det : lista
                            ) {
                        if (det.getIdEstado() == 0) {
                            DetallesPedido detalles = new DetallesPedido();
                            detalles.setIdMenu(det.getIdMenu());
                            detalles.setTotalDetalle(det.getTotalDetalle());
                            detalles.setNombreMenu(det.getNombreMenu());
                            detalles.setIdInsumo(det.getIdInsumo());
                            detalles.setCantidad(det.getCantidad());
                            detalles.setIdEstado(12);
                            detalles.setNombreInsumo(det.getNombreMenu());

                            JSONObject jsonObjectArray = new JSONObject();
                            jsonObjectArray.put("idDetallePedido", null);
                            jsonObjectArray.put("cantidad", detalles.getCantidad());
                            jsonObjectArray.put("idPedido", null);
                            jsonObjectArray.put("idEstado", detalles.getIdEstado());
                            jsonObjectArray.put("nombreEstado", "");
                            jsonObjectArray.put("idMenu", detalles.getIdMenu() != 0 ? detalles.getIdMenu() : null);
                            jsonObjectArray.put("nombreMenu", detalles.getNombreMenu());
                            jsonObjectArray.put("totalDetalle", detalles.getTotalDetalle());
                            jsonObjectArray.put("idInsumo", detalles.getIdInsumo() != 0 ? detalles.getIdInsumo() : null);
                            jsonObjectArray.put("nombreInsumo", detalles.getNombreInsumo());
                            ja.put(jsonObjectArray);
                        }
                    }

                    jsonParam.put("arrayDetallesPedido", ja);

                    PrintWriter ow = new PrintWriter(urlConn.getOutputStream());

                    ow.print(jsonParam.toString());
                    // ow.flush();
                    ow.close();


                    int reqcoda = urlConn.getResponseCode();

                    InputStream in = new BufferedInputStream(urlConn.getInputStream());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));


                    String line;
                    boolean leyo = false;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                        leyo = true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    error = 1;
                } finally {
                    urlConn.disconnect();
                }


            } catch (Exception e) {
                this.exception = e;
            }


            return null;
        }


        protected void onPostExecute(String st) {
            loadPedidosData(lista);
        }
    }

    private void startActivityCategoria() {
        Intent intent = new Intent(this, CategoriaMenuActivity.class);
        intent.putExtra("IDCLIENTE", NroCliente);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = data;
                NroCliente = (int) intent.getExtras().get("IDCLIENTE");
                Bundle bundle = intent.getExtras();

                ArrayList<DetallePedido> listadoMenus = (ArrayList<DetallePedido>) bundle.getSerializable("listadoMenus");
                Object[] obj = new Object[2];
                obj[0] = NroCliente;
                if (listadoMenus.size() > 0)
                    obj[1] = listadoMenus;
                new GetTask().execute(obj);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }


    class GetTask extends AsyncTask<Object, String, String> {

        private Exception exception;
        private Pedidos pedido = new Pedidos();
        private ArrayList<DetallePedido> listaDetalles = new ArrayList<>();
        ArrayList<DetallePedido> listaMenus;

        protected String doInBackground(Object... params) {
            try {
                String response = "No se conecto";
                try {
                    listaMenus = (ArrayList<DetallePedido>) params[1];
                    HttpURLConnection urlConn;
                    StringBuilder result = new StringBuilder();
                    URL url = new URL("http://172.16.0.2:8082/api/pedido/obtenerPedido/" + ((int) params[0]));

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
                            String fechaAlta = "";
                            String fechaBaja = "";
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
                } catch (Exception e) {
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
                                    DetallePedido detallePed = new DetallePedido();
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
                        for (DetallePedido det :
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


                            } else {
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


                    }
                }

            } catch (Exception e) {
                this.exception = e;

            }

            return null;
        }

        protected void onPostExecute(String st) {
            loadPedidosData(listaDetalles, listaMenus);
        }
    }

    private void loadPedidosData() {

        Intent intent = getIntent();
        adapter.clear();
        ArrayList<DetallePedido> listaDetalle = (ArrayList<DetallePedido>) intent.getExtras().get("LISTADETALLES");
        DetallePedido info;

        for (DetallePedido cm : listaDetalle
                ) {
            info = cm;
            adapter.addDetallesInfo(info);
        }
        adapter.notifyDataSetChanged();

    }

    private void loadPedidosData(ArrayList<DetallePedido> listaDetalle, ArrayList<DetallePedido> listaMenus) {

        DetallePedido info;
        adapter.clear();
        for (DetallePedido cm : listaDetalle
                ) {
            info = cm;
            adapter.addDetallesInfo(info);
        }
        for (DetallePedido cm : listaMenus
                ) {
            info = cm;
            adapter.addDetallesInfo(info);
        }

        adapter.notifyDataSetChanged();

    }

    private void loadPedidosData(ArrayList<DetallePedido> listaDetalle) {

        DetallePedido info;
        adapter.clear();
        for (DetallePedido cm : listaDetalle
                ) {
            if (cm.getIdEstado() == 0)
                cm.setIdEstado(12);
            info = cm;
            adapter.addDetallesInfo(info);
        }

        adapter.notifyDataSetChanged();

        Toast.makeText(this, "Pedido registrado", Toast.LENGTH_SHORT).show();

    }

    /*private ArrayList<DetallesPedido> loadMenusInDetalles(ArrayList<Menus> listadoMenus)
    {
        boolean primeraVez = true;
        ArrayList<DetallesPedido> listaDetallesMenu = new ArrayList<>();
        for (Menus menu: listadoMenus
             ) {

            DetallesPedido det = new DetallesPedido();
            det.setIdMenu(menu.getIdMenu());
            det.setIdInsumo(menu.getIdInsumo());
            det.setIdCategoria(menu.getIdCategoria());
            det.setCantidad(1);
            det.setPrecio(menu.getPrecio());
            det.setNombreMenu(menu.getNombreMenu());
            det.setIdEstado(0);
            det.setTotalDetalle((det.getCantidad()*det.getPrecio()));

            if(primeraVez) {
                listaDetallesMenu.add(det);
                primeraVez = false;
            }
            else {
                for(Iterator<DetallesPedido> it = listaDetallesMenu.iterator(); it.hasNext();)
                {
                    DetallesPedido detalle = it.next();
                    //ConcurrentModificationException
                    if (((detalle.getIdInsumo() == det.getIdInsumo()) && detalle.getIdInsumo() != 0) || ((detalle.getIdMenu() == det.getIdMenu()) && detalle.getIdMenu() != 0)) {
                        detalle.setCantidad(detalle.getCantidad() + 1);
                        detalle.setTotalDetalle(detalle.getCantidad()*detalle.getPrecio());
                        break;
                    } else {
                        listaDetallesMenu.add(det);
                    }
                }




            }

        }

        return listaDetallesMenu;
    }
*/


    class PedidosAdapter extends BaseAdapter {
        private ArrayList<DetallePedido> listaDetallesPedidos;

        private LayoutInflater inflater;
        private TodasImages ti;

        public PedidosAdapter() {

            listaDetallesPedidos = new ArrayList<>();

            inflater = LayoutInflater.from(PedidosActivity.this);
            ti = new TodasImages();
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
            private TextView txtNombreMenu;
            private TextView txtCantidad;
            private TextView txtMontoDetalle;
            private ImageView imageMenu;
            private TextView txtEstadoMenu;
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
                holder.txtEstadoMenu = (TextView) convertView
                        .findViewById(R.id.estadoDetalle);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }


            DetallePedido info = (DetallePedido) getItem(position);

            if (info.getIdInsumo() == 0) {
                holder.imageMenu.setImageResource((ti.obtenerImagen(info.getIdMenu(), true)).getIdImagen());
            } else {
                holder.imageMenu.setImageResource((ti.obtenerImagen(info.getIdInsumo(), false)).getIdImagen());
            }

            holder.txtNombreMenu.setText(info.getNombreMenu());
            String cantidad = "" + info.getCantidad();
            holder.txtCantidad.setText(cantidad);
            holder.txtMontoDetalle.setText("$" + info.getTotalDetalle());
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
            holder.txtEstadoMenu.setText(estado);
            if (info.getIdEstado() == 0) {
                int color = Color.RED;
                holder.txtNombreMenu.setTextColor(color);
                holder.txtCantidad.setTextColor(color);
                holder.txtMontoDetalle.setTextColor(color);
            } else {
                int color = Color.BLACK;
                holder.txtNombreMenu.setTextColor(color);
                holder.txtCantidad.setTextColor(color);
                holder.txtMontoDetalle.setTextColor(color);
            }

            return convertView;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {

        DetallePedido cm = (DetallePedido) getListAdapter().getItem(position);

        String nombre = cm.getNombreMenu();
        Toast.makeText(this, "nombre menu:" + nombre, Toast.LENGTH_LONG).show();


    }

    private void removePedidosData(int position) {

        adapter.remove(position);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Menú eliminado.", Toast.LENGTH_SHORT).show();

    }

    private void restarUno(int position) {
        DetallePedido det = (DetallePedido) adapter.getItem(position);
        int cantidad = det.getCantidad();
        if (cantidad == 1)
        {
            removePedidosData(position);
        }
        else
        {
            det.setCantidad(cantidad-1);
            Toast.makeText(this, "Menú eliminado.", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();


    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        DetallePedido detalle = (DetallePedido) adapter.getItem(position);

        if(detalle.getIdEstado() == 0) {

            final CharSequence[] items = {
                    "Eliminar todos", "Eliminar uno", "Cancelar"
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("ACCIONES");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    // Do something with the selection
                    if (item == 0) // Eliminar todos
                    {
                        removePedidosData(position);
                    } else {
                        if (item == 1) // Eliminar uno
                        {
                            restarUno(position);
                        } else//item == 2 // Cancelar
                        {
                            ;
                        }
                    }
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else
        {
            Toast.makeText(this, "Pedido ya registrado.", Toast.LENGTH_LONG).show();
        }
        return true;
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
