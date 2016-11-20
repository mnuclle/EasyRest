package com.example.manu.myapplication.Cliente;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.JsonReader;
import android.util.JsonToken;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manu.myapplication.DialogObservaciones;
import com.example.manu.myapplication.Entidades.DetallePedido;
import com.example.manu.myapplication.Entidades.DetallesPedido;
import com.example.manu.myapplication.Entidades.Images;
import com.example.manu.myapplication.Entidades.Pedidos;
import com.example.manu.myapplication.Entidades.TodasImages;
import com.example.manu.myapplication.R;
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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;


public class ClientePedidoActivity extends ListActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, DialogObservaciones.NoticeDialogListener {
    private FloatingActionButton btnElegirPedido, btnConfirmarPedido;
    private int NroCliente;
    private int NroOpcion = -1;
    private int positionLista;
    private PedidosAdapter adapter;
    private String URLGlobal;
    private ArrayList<DetallePedido> listaMenusAConfirmar;
    private TextView montoTotalPedido;
    private TextView cuentaPedido;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_pedido);

        /*Se agrega esto*/

        Intent intent = getIntent();
        NroCliente = (int) intent.getExtras().get("IDCLIENTE");
        URLGlobal = intent.getExtras().get("URLGlobal").toString();


        cuentaPedido = (TextView) findViewById(R.id.cuentaPedidoCliente);
        cuentaPedido.setText(intent.getExtras().get("CUENTAPEDIDO").toString());
        montoTotalPedido = (TextView) findViewById(R.id.montoTotalPedidoCliente);
        btnElegirPedido = (FloatingActionButton) findViewById(R.id.btnElegirPedidoCliente);

        Typeface type = Typeface.createFromAsset(getAssets(),"segoeui.ttf");
        cuentaPedido.setTypeface(type);
        montoTotalPedido.setTypeface(type);

        btnConfirmarPedido = (FloatingActionButton) findViewById(R.id.btnConfirmarPedidoCliente);

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
                obj[1] = URLGlobal;

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
        private String URLGlobal;

        public PostTask(Context context) {
            this.contexto = context;
        }

        protected String doInBackground(Object... params) {
            URLGlobal = params[1].toString();
            try {

                StringBuilder result = new StringBuilder();
                URL url2 = new URL(URLGlobal+"pedido/enviarPedido");
                HttpURLConnection urlConn = (HttpURLConnection) url2.openConnection();
                try {
                    lista = (ArrayList<DetallePedido>) params[0];
                    //urlConn.setChunkedStreamingMode(0);
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
                            detalles.setObservacion(det.getObservacion());

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
                            jsonObjectArray.put("observacion",(detalles.getObservacion() == null || detalles.getObservacion().trim().isEmpty()) ? null : detalles.getObservacion().trim());
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
        Intent intent = new Intent(this, ClienteCategoriaMenuActivity.class);
        intent.putExtra("IDCLIENTE", NroCliente);
        intent.putExtra("URLGlobal",URLGlobal);
        listaMenusAConfirmar = new ArrayList<>();
        for (Iterator<DetallePedido> it = adapter.getList().iterator(); it.hasNext();)
        {
            DetallePedido auxDetalle = it.next();
            if(auxDetalle.getIdEstado() == 0)
                listaMenusAConfirmar.add(auxDetalle);
        }


        Bundle bundle = new Bundle();
        bundle.putSerializable("LISTAACONDFIRMAR",listaMenusAConfirmar);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = data;
                NroCliente = (int) intent.getExtras().get("IDCLIENTE");
                URLGlobal = intent.getExtras().get("URLGlobal").toString();
                Bundle bundle = intent.getExtras();

                ArrayList<DetallePedido> listadoMenus = (ArrayList<DetallePedido>) bundle.getSerializable("listadoMenus");
                Object[] obj = new Object[3];
                obj[0] = NroCliente;
                if (listadoMenus.size() > 0)
                    obj[1] = listadoMenus;
                else {
                    listadoMenus = new ArrayList<>();
                    obj[1] = listadoMenus;
                }
                obj[2] = URLGlobal;
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
        private String URLGlobal;
        ArrayList<DetallePedido> listaMenus;

        protected String doInBackground(Object... params) {
            URLGlobal = params[2].toString();
            try {
                String response = "No se conecto";
                try {
                    listaMenus = (ArrayList<DetallePedido>) params[1];
                    HttpURLConnection urlConn;
                    StringBuilder result = new StringBuilder();
                    URL url = new URL(URLGlobal + "pedido/obtenerPedido/" + ((int) params[0]));

                    urlConn = (HttpURLConnection) url.openConnection();

                    InputStream in = new BufferedInputStream(urlConn.getInputStream());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    try {
                        JsonReader reader1 = new JsonReader(new InputStreamReader(new ByteArrayInputStream(result.toString().getBytes(Charset.forName("UTF-8")))));
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
                            URL url = new URL(URLGlobal + "pedido/obtenerDetallesPedido/" + pedido.getIdPedido());

                            urlConn = (HttpURLConnection) url.openConnection();

                            InputStream in = new BufferedInputStream(urlConn.getInputStream());

                            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                            String line;
                            while ((line = reader.readLine()) != null) {
                                result.append(line);
                            }
                            try {
                                JsonReader reader1 = new JsonReader(new InputStreamReader(new ByteArrayInputStream(result.toString().getBytes(Charset.forName("UTF-8")))));
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
                                    String observacion = "";
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
                                            case "observacion":
                                                if (reader1.peek() == JsonToken.NULL) {
                                                    reader1.skipValue();
                                                } else {
                                                    observacion = reader1.nextString();
                                                }
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
                                    detallePed.setObservacion(observacion);
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
                                    URL url = new URL(URLGlobal + "pedido/obtenerMenuDetallePedido/" + det.getIdMenu());

                                    urlConn = (HttpURLConnection) url.openConnection();

                                    InputStream in = new BufferedInputStream(urlConn.getInputStream());

                                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                                    String line;
                                    while ((line = reader.readLine()) != null) {
                                        result.append(line);
                                    }
                                    try {
                                        JsonReader reader1 = new JsonReader(new InputStreamReader(new ByteArrayInputStream(result.toString().getBytes(Charset.forName("UTF-8")))));
                                        response = "GET: ";

                                        reader1.beginArray();
                                        while (reader1.hasNext()) {
                                            int idMenu = -1;
                                            String nombre = "";
                                            double precio = -1;
                                            int idCategoria = -1;
                                            String descripcion ="";
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
                                                    case "descripcion":
                                                        descripcion = reader1.nextString();
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
                                            det.setDescripcion(descripcion);
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
                                    URL url = new URL(URLGlobal + "pedido/obtenerInsumoDetallePedido/" + det.getIdInsumo());

                                    urlConn = (HttpURLConnection) url.openConnection();

                                    InputStream in = new BufferedInputStream(urlConn.getInputStream());

                                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                                    String line;
                                    while ((line = reader.readLine()) != null) {
                                        result.append(line);
                                    }
                                    try {
                                        JsonReader reader1 = new JsonReader(new InputStreamReader(new ByteArrayInputStream(result.toString().getBytes(Charset.forName("UTF-8")))));
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
                                            det.setDescripcion(descripcion);
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
        Double montoDetalles = 0.0;
        Intent intent = getIntent();
        adapter.clear();
        ArrayList<DetallePedido> listaDetalle = (ArrayList<DetallePedido>) intent.getExtras().get("LISTADETALLES");
        DetallePedido info;

        for (DetallePedido cm : listaDetalle
                ) {
            info = cm;
            if (cm.getIdEstado() == 11 || cm.getIdEstado() == 12 || cm.getIdEstado() == 10 || cm.getIdEstado() == 16  || cm.getIdEstado() == 15)
                montoDetalles = montoDetalles + cm.getTotalDetalle();
            adapter.addDetallesInfo(info);
        }
        montoTotalPedido.setText("$" + String.format("%.2f",montoDetalles));
        adapter.notifyDataSetChanged();

    }

    private void loadPedidosData(ArrayList<DetallePedido> listaDetalle, ArrayList<DetallePedido> listaMenus) {
        Double montoDetalles = 0.0;
        DetallePedido info;
        adapter.clear();
        for (DetallePedido cm : listaDetalle
                ) {
            info = cm;
            if (cm.getIdEstado() == 11 || cm.getIdEstado() == 12 || cm.getIdEstado() == 10 || cm.getIdEstado() == 16 || cm.getIdEstado() == 15)
                montoDetalles = montoDetalles + cm.getTotalDetalle();
            adapter.addDetallesInfo(info);
        }

        for (DetallePedido cm : listaMenus
                ) {
            info = cm;
            if (cm.getIdEstado() == 11 || cm.getIdEstado() == 12 || cm.getIdEstado() == 10 || cm.getIdEstado() == 16 || cm.getIdEstado() == 15)
                montoDetalles = montoDetalles + cm.getTotalDetalle();
            adapter.addDetallesInfo(info);
        }
        montoTotalPedido.setText("$" + String.format("%.2f",montoDetalles));
        adapter.notifyDataSetChanged();

    }

    private void loadPedidosData(ArrayList<DetallePedido> listaDetalle) {

        ArrayList<DetallePedido> listadoMenus = new ArrayList<DetallePedido>();
        Object[] obj = new Object[3];
        obj[0] = NroCliente;
        if (listadoMenus.size() > 0)
            obj[1] = listadoMenus;
        else {
            listadoMenus = new ArrayList<>();
            obj[1] = listadoMenus;
        }
        obj[2] = URLGlobal;

        new GetTask().execute(obj);

        Toast.makeText(this, "Pedido registrado", Toast.LENGTH_SHORT).show();

    }

    private void loadPedidosData(ArrayList<DetallePedido> listaDetalle, int idDetallePedido) {

        ArrayList<DetallePedido> listadoMenus = new ArrayList<DetallePedido>();
        Object[] obj = new Object[3];
        obj[0] = NroCliente;
        if (listadoMenus.size() > 0)
            obj[1] = listadoMenus;
        else {
            listadoMenus = new ArrayList<>();
            obj[1] = listadoMenus;
        }
        obj[2] = URLGlobal;

        new GetTask().execute(obj);

        Toast.makeText(this, "Menú anulado.", Toast.LENGTH_SHORT).show();

    }

    private void loadPedidosDataActEstado(ArrayList<DetallePedido> listaDetalle, int idDetallePedido) {

        ArrayList<DetallePedido> listadoMenus = new ArrayList<DetallePedido>();
        Object[] obj = new Object[3];
        obj[0] = NroCliente;
        if (listadoMenus.size() > 0)
            obj[1] = listadoMenus;
        else {
            listadoMenus = new ArrayList<>();
            obj[1] = listadoMenus;
        }
        obj[2] = URLGlobal;

        new GetTask().execute(obj);

        Toast.makeText(this, "Pedido entregado.", Toast.LENGTH_SHORT).show();

    }

    class PedidosAdapter extends BaseAdapter {
        private ArrayList<DetallePedido> listaDetallesPedidos;

        private LayoutInflater inflater;
        private TodasImages ti;
        private Images im;

        public PedidosAdapter() {

            listaDetallesPedidos = new ArrayList<>();

            inflater = LayoutInflater.from(ClientePedidoActivity.this);
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
            private TextView txtObservaciones;
            private TextView txtEstadoMenu;
            private TextView txtDescripcion;
            private TextView txtCategoria;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {

            PedidosAdapter.Holder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.elemento_lista_pedido, null);
                holder = new PedidosAdapter.Holder();
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
                holder.txtObservaciones = (TextView) convertView
                        .findViewById(R.id.observaciones);
                holder.txtDescripcion = (TextView) convertView
                        .findViewById(R.id.descripciones);
                holder.txtCategoria = (TextView) convertView
                        .findViewById(R.id.categoriaNombre);
                convertView.setTag(holder);

            } else {
                holder = (PedidosAdapter.Holder) convertView.getTag();
            }


            DetallePedido info = (DetallePedido) getItem(position);

            if (info.getIdInsumo() == 0) {
                im = (ti.obtenerImagen(info.getIdMenu(), true));
                holder.imageMenu.setImageBitmap(decodeSampledBitmapFromResource(getResources(), im.getIdImagen(),100,100));
                //holder.imageMenu.setImageResource((ti.obtenerImagen(info.getIdMenu(), true)).getIdImagen());
            } else {
                im = (ti.obtenerImagen(info.getIdInsumo(), false));
                holder.imageMenu.setImageBitmap(decodeSampledBitmapFromResource(getResources(), im.getIdImagen(),100,100));
            //    holder.imageMenu.setImageResource((ti.obtenerImagen(info.getIdInsumo(), false)).getIdImagen());
            }

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
            holder.txtCategoria.setText(categoria);
            holder.txtNombreMenu.setText(info.getNombreMenu());
            holder.txtDescripcion.setText("(" + info.getDescripcion()+ ")");
            String cantidad = "" + info.getCantidad();
            holder.txtCantidad.setText(cantidad);
            holder.txtMontoDetalle.setText("$" + String.format("%.2f",info.getTotalDetalle()));
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

            int color;
            if (info.getIdEstado() == 0) {
                color = Color.parseColor("#F38129");
            }
            else
            {
                if (info.getIdEstado() == 14 || info.getIdEstado() == 13)
                    color = Color.GRAY;
                else
                    color = Color.WHITE;
            }
            holder.txtNombreMenu.setTextColor(color);
            holder.txtCantidad.setTextColor(color);
            holder.txtMontoDetalle.setTextColor(color);
            holder.txtObservaciones.setTextColor(color);
            holder.txtEstadoMenu.setTextColor(color);
            holder.txtDescripcion.setTextColor(color);
            holder.txtCategoria.setTextColor(color);

            String observ = info.getObservacion();
            if (info.getIdEstado() == 0) {
                if (observ == null) {
                    holder.txtObservaciones.setText("Toca para agregar una observación al menú.");
                } else {
                    if (observ.trim().isEmpty())
                        holder.txtObservaciones.setText("Toca para agregar una observación al menú.");
                    else
                        holder.txtObservaciones.setText(info.getObservacion());
                }
            }
            else {
                if (observ == null) {
                    holder.txtObservaciones.setText("Sin observaciones.");
                } else {


                    if (observ.trim().isEmpty())
                        holder.txtObservaciones.setText("Sin observaciones.");
                    else
                        holder.txtObservaciones.setText(info.getObservacion());
                }
            }

            Typeface type = Typeface.createFromAsset(getAssets(),"segoeui.ttf");
            holder.txtObservaciones.setTypeface(type);
            holder.txtNombreMenu.setTypeface(type);
            holder.txtCantidad.setTypeface(type);
            holder.txtMontoDetalle.setTypeface(type);
            holder.txtObservaciones.setTypeface(type);
            holder.txtEstadoMenu.setTypeface(type);
            holder.txtDescripcion.setTypeface(type);
            holder.txtCategoria.setTypeface(type);

            return convertView;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {

        DetallePedido cm = (DetallePedido) getListAdapter().getItem(position);
        if(cm.getIdEstado() == 0) {
            DialogObservaciones dialog = new DialogObservaciones();
            dialog.setItem(cm);
            dialog.show(getFragmentManager(), "DialogObservaciones");
        }

    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button

    }

    private void anularMenuPedido(int position)
    {
        Object[] obj = new Object[3];
        obj[1] = URLGlobal;
        DetallePedido detalle = (DetallePedido) adapter.getItem(position);

        obj[0] = detalle.getIdDetallePedido();
        obj[2] = adapter.getList();




        new AnularDetalleTask().execute(obj);



        // adapter.remove(position);
        //  adapter.notifyDataSetChanged();


    }

    private void actualizarMenu(int position)
    {
        Object[] obj = new Object[3];
        obj[1] = URLGlobal;
        DetallePedido detalle = (DetallePedido) adapter.getItem(position);

        obj[0] = detalle.getIdDetallePedido();
        obj[2] = adapter.getList();

        new ActualizarMenuTask().execute(obj);
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
            det.setTotalDetalle(det.getPrecio()*det.getCantidad());
            Toast.makeText(this, "Menú eliminado.", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();


    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        DetallePedido detalle = (DetallePedido) adapter.getItem(position);

        if(detalle.getIdEstado() == 0) {

            final Dialog dialog = new Dialog(view.getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_eliminar_uno_todos);
            Button dialogButtonEliminarUno = (Button) dialog.findViewById(R.id.dialogButtonEliminarUno);
            Button dialogButtonEliminarTodos = (Button) dialog.findViewById(R.id.dialogButtonEliminarTodos);
            TextView txtTituloDialog = (TextView) dialog.findViewById(R.id.txtTituloDialog) ;

            Typeface type = Typeface.createFromAsset(getAssets(),"segoeui.ttf");
            dialogButtonEliminarUno.setTypeface(type);
            dialogButtonEliminarTodos.setTypeface(type);
            txtTituloDialog.setTypeface(type);

            dialog.setTitle("ACCIONES");

            // if button is clicked, close the custom dialog
            dialogButtonEliminarUno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    restarUno(position);
                    dialog.cancel();
                }
            });
            dialogButtonEliminarTodos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removePedidosData(position);
                    dialog.cancel();
                }
            });

            dialog.show();
        }
        else
        {
            Toast.makeText(this, "Sin acciones para menú.", Toast.LENGTH_SHORT).show();
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



    class AnularDetalleTask extends AsyncTask<Object, String, String> {

        private String URLGlobal;
        private int idDetallePedido;
        ArrayList<DetallePedido> listaDetalle;
        protected String doInBackground(Object... params) {
            URLGlobal = params[1].toString();
            listaDetalle = (ArrayList<DetallePedido>)(params[2]);
            idDetallePedido = (int) params[0];
            try {
                String response = "No se conecto";

                try {
                    HttpURLConnection urlConn;
                    StringBuilder result = new StringBuilder();
                    URL url = new URL(URLGlobal + "pedido/anularDetallePedido");
                    urlConn = (HttpURLConnection) url.openConnection();
                    try {

                       // urlConn.setChunkedStreamingMode(0);
                        urlConn.setDoOutput(true);
                        urlConn.setDoInput(true);
                        urlConn.setRequestProperty("Content-Type", "application/json");
                        urlConn.setRequestMethod("POST");
                        urlConn.connect();
                        //Create JSONObject here
                        JSONObject jsonParam = new JSONObject();
                        jsonParam.put("idDetallePedido",  + ((int) params[0]));

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


                    }
                    catch (Exception e ) {
                        e.printStackTrace();
                    }
                    finally {
                        urlConn.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            } catch (Exception e) {
                e.printStackTrace();

            }

            return null;
        }

        protected void onPostExecute(String st) {
            loadPedidosData(listaDetalle,idDetallePedido);
        }
    }




    class ActualizarMenuTask extends AsyncTask<Object, String, String> {

        private String URLGlobal;
        private int idDetallePedido;
        ArrayList<DetallePedido> listaDetalle;
        protected String doInBackground(Object... params) {
            URLGlobal = params[1].toString();
            listaDetalle = (ArrayList<DetallePedido>)(params[2]);
            idDetallePedido = (int) params[0];
            try {
                String response = "No se conecto";

                try {
                    HttpURLConnection urlConn;
                    StringBuilder result = new StringBuilder();
                    URL url = new URL(URLGlobal + "pedido/actualizarDetallePedido");
                    urlConn = (HttpURLConnection) url.openConnection();
                    try {

                       // urlConn.setChunkedStreamingMode(0);
                        urlConn.setDoOutput(true);
                        urlConn.setDoInput(true);
                        urlConn.setRequestProperty("Content-Type", "application/json");
                        urlConn.setRequestMethod("POST");
                        urlConn.connect();
                        //Create JSONObject here
                        JSONObject jsonParam = new JSONObject();
                        jsonParam.put("idDetallePedido",  + ((int) params[0]));
                        jsonParam.put("idEstado",  "15" ) ;
                        jsonParam.put("nombreEstado",  "ENTREGADO" ) ;


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


                    }
                    catch (Exception e ) {
                        e.printStackTrace();
                    }
                    finally {
                        urlConn.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            } catch (Exception e) {
                e.printStackTrace();

            }

            return null;
        }

        protected void onPostExecute(String st) {
            loadPedidosDataActEstado(listaDetalle,idDetallePedido);
        }
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}
