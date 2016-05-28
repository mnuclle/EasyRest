package com.example.manu.myapplication;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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


public class PedidosActivity extends ListActivity implements AdapterView.OnItemClickListener{
    private Button btnElegirPedido,btnConfirmarPedido;
    private int NroCliente;

    private PedidosAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        /*Se agrega esto*/

        Intent intent = getIntent();
        NroCliente = (int)intent.getExtras().get("IDCLIENTE");

        btnElegirPedido = (Button) findViewById(R.id.btnElegirPedido);

        btnConfirmarPedido = (Button) findViewById(R.id.btnConfirmarPedido);

        btnElegirPedido.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                startActivityCategoria();
            }

        }     );

        btnConfirmarPedido.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Object[] obj = new Object[2];
                obj[0] = adapter.getList();

                new PostTask(v.getContext()).execute(obj);
            }

        }     );


        /**/

        adapter = new PedidosAdapter();

        setListAdapter(adapter);

        getListView().setOnItemClickListener(this);

        loadPedidosData();

    }


    class PostTask extends AsyncTask<Object,String,String> {

        private Exception exception;
        private Context contexto;
        private int error = 0;
        String response = "No se conecto";
        ArrayList<DetallePedido> lista ;

        public PostTask(Context context)
        {
            this.contexto=context;
        }
        protected String doInBackground(Object... params) {
            try {

                StringBuilder result = new StringBuilder();
                URL url2 = new URL("http://172.16.0.2:8082/api/pedido/enviarPedido");
                HttpURLConnection urlConn = (HttpURLConnection)url2.openConnection();
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
                        if(det.getIdEstado() == 0) {
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
                }
                finally {
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

    private void startActivityCategoria()
    {
        Intent intent = new Intent(this,CategoriaMenuActivity.class);
        intent.putExtra("IDCLIENTE",NroCliente);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Intent intent = data;
                NroCliente = (int)intent.getExtras().get("IDCLIENTE");
                Bundle bundle = intent.getExtras();

                ArrayList<DetallePedido> listadoMenus = (ArrayList<DetallePedido>) bundle.getSerializable("listadoMenus");
                Object[] obj = new Object[2];
                obj[0] = NroCliente;
                if(listadoMenus.size() > 0)
                    obj[1] = listadoMenus;
                new GetTask().execute(obj);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }


    class GetTask extends AsyncTask<Object,String,String> {

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
        ArrayList<DetallePedido> listaDetalle = (ArrayList<DetallePedido>)intent.getExtras().get("LISTADETALLES");
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


        public ArrayList<DetallePedido> getList()
        {
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


        public void clear()
        {
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


            DetallePedido info = (DetallePedido) getItem(position);

            if(info.getIdInsumo() == 0)
            {
                holder.imageMenu.setImageResource((ti.obtenerImagen(info.getIdMenu(), true)).getIdImagen());
            }
            else
            {
                holder.imageMenu.setImageResource((ti.obtenerImagen(info.getIdInsumo(),false)).getIdImagen());
            }

            holder.txtNombreMenu.setText(info.getNombreMenu());
            String cantidad = "" + info.getCantidad();
            holder.txtCantidad.setText(cantidad);
            holder.txtMontoDetalle.setText("$" + info.getTotalDetalle());
            if(info.getIdEstado() == 0)
            {
                int color = Color.RED;
                holder.txtNombreMenu.setTextColor(color);
                holder.txtCantidad.setTextColor(color);
                holder.txtMontoDetalle.setTextColor(color);
            }
            else
            {
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

        DetallePedido cm = (DetallePedido)getListAdapter().getItem(position);

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
