package com.example.manu.myapplication;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;
import android.util.JsonReader;
import android.util.JsonToken;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manu.myapplication.Entidades.CuentasMesa;
import com.example.manu.myapplication.Entidades.DetallePedido;
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


public class ListaCuentas extends ListActivity implements
        AdapterView.OnItemClickListener, DialogSalir.NoticeDialogListener
{
    private CuentasAdapter adapter;
    private String URLGlobal;
    private int idEmpleado;
    private boolean primerIngreso = true;
    boolean checkTodasCuentas = true;
    MyResultReceiver mReceiver;
    Thread thPedidos;
    Thread threadListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_cuentas);
        URLGlobal = getIntent().getExtras().get("URLGlobal").toString();
        idEmpleado = (int)getIntent().getExtras().get("IDEMPLEADO");
        adapter = new CuentasAdapter();

        setListAdapter(adapter);

        //si usamos un servicio
        mReceiver = new MyResultReceiver(new Handler());
        final Intent i =new Intent(this, ServicioListenerMozo.class);

        i.putExtra("URLGlobal",URLGlobal);
        i.putExtra("receiverTag", mReceiver);

        threadListener = new Thread(new Runnable() {
            @Override
            public void run() {
                Context c = getApplicationContext();
                c.startService(i);
            }
        });
        threadListener.start();

        /**/

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
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private void loadCuentasData(ArrayList<CuentasMesa> arrayList) {

        Intent intent = getIntent();
        ArrayList<CuentasMesa> listaCuentas = arrayList;
        adapter.clear();
        CuentasMesa info;
        for (CuentasMesa cm : listaCuentas
                ) {
            info = cm;
            adapter.addCuentasInfo(info);
        }
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
        public void clear() {
            cuentasMesa = new ArrayList<>();
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
            private TextView txtNombreMesas;
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
                holder.txtNombreMesas = (TextView) convertView
                        .findViewById(R.id.nombreMesas);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            CuentasMesa info = (CuentasMesa) getItem(position);

            holder.txtCuenta.setText(info.getNombreCuenta() + " - " + info.getNumeroDocumento());
            holder.txtMesas.setText(info.getNumeroMesa());
            holder.txtMontoPedido.setText("$" + String.format("%.2f",info.getMontoPedido()));
            holder.txtNombreMesas.setText("Mesas");

            return convertView;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!primerIngreso) {
            new PostTaskEmp(URLGlobal, idEmpleado, checkTodasCuentas).execute();
        }
        else {
            primerIngreso = false;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {
        CuentasMesa cm = (CuentasMesa)getListAdapter().getItem(position);
        int idCliente = cm.getIdCliente();
        Object[] listaObjetos = new Object[5];
        listaObjetos[0] = idCliente;
        listaObjetos[1] = URLGlobal;
        listaObjetos[2] = "MESA: " + cm.getNumeroMesa() + " - CLIENTE: " + cm.getNumeroDocumento() + ", " + cm.getNombreCuenta() ;
        new GetTask().execute(listaObjetos);
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_ver_todos:
                if (checked)
                    checkTodasCuentas = false;
                else
                    checkTodasCuentas = true;
                break;
        }
        new PostTaskEmp(URLGlobal, idEmpleado, checkTodasCuentas).execute();
    }

    class GetTask extends AsyncTask<Object,String,String> {

        private Exception exception;
        private Pedidos pedido = new Pedidos();
        private String URLGlobal;
        private ArrayList<DetallePedido> listaDetalles = new ArrayList<>();

        protected String doInBackground(Object... params) {
            URLGlobal = params[1].toString();
            try {
                String response = "No se conecto";
                try {

                        HttpURLConnection urlConn;
                        StringBuilder result = new StringBuilder();
                        URL url = new URL(URLGlobal + "pedido/obtenerPedido/" +((int) params[0]));

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
                            URL url = new URL(URLGlobal + "pedido/obtenerDetallesPedido/" + pedido.getIdPedido());

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
                                        JsonReader reader1 = new JsonReader(new InputStreamReader(new ByteArrayInputStream(result.toString().getBytes(StandardCharsets.UTF_8))));
                                        response = "GET: ";

                                        reader1.beginArray();
                                        while (reader1.hasNext()) {
                                            int idMenu = -1;
                                            String nombre = "";
                                            double precio = -1;
                                            String descripcion = "";
                                            int idCategoria = -1;
                                            int stock = 0;
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


                            }
                            else
                            {
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

                        Intent intent = new Intent(ListaCuentas.this, PedidosActivity.class);
                        intent.putExtra("LISTADETALLES", listaDetalles);
                        intent.putExtra("IDCLIENTE",((int) params[0]));
                        intent.putExtra("URLGlobal", URLGlobal);
                        intent.putExtra("CUENTAPEDIDO",params[2].toString());
                        intent.putExtra("IDEMPLEADO",idEmpleado);
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



    class PostTaskEmp extends AsyncTask<Object,String,String> {

        private Exception exception;
        private int error = 0;
        private Context contexto;
        String response = "No se conecto";
        String URLGlobal;
        int idEmpleado;
        boolean cuentasDeMozo; //si es true son cuentas de mozo, si es false son todas las cuentas.

        ArrayList<CuentasMesa> lista = new ArrayList<>();
        public PostTaskEmp(String url,int idEmpleado, boolean cuentasDeMozo)
        {
            this.URLGlobal = url;
            this.idEmpleado = idEmpleado;
            this.cuentasDeMozo = cuentasDeMozo;
        }
        protected String doInBackground(Object... params){
            try {


                        HttpURLConnection urlConn1;
                        StringBuilder result1 = new StringBuilder();

                        String urlString;
                        if (cuentasDeMozo)
                            urlString = "mesas/obtenerTodasCuentasMozo/" + idEmpleado;
                        else
                            urlString = "mesas/obtenerTodasCuentas";

                        URL url = new URL(URLGlobal + urlString);

                        urlConn1 = (HttpURLConnection)url.openConnection();

                        InputStream in1 = new BufferedInputStream(urlConn1.getInputStream());

                        BufferedReader reader1 = new BufferedReader(new InputStreamReader(in1));

                        String line1;
                        while ((line1 = reader1.readLine()) != null) {
                            result1.append(line1);
                        }


                        try {
                            JsonReader reader2 = new JsonReader(new InputStreamReader(new ByteArrayInputStream(result1.toString().getBytes(StandardCharsets.UTF_8))));

                            try {
                                response = "GET: ";

                                reader2.beginArray();
                                while(reader2.hasNext()) {
                                    int idCliente = -1;
                                    int nro_documento = -1;
                                    String n_cliente = "";
                                    int idEstadoCuenta = -1;
                                    String mesas = "";
                                    double montoPedido = 0;

                                    reader2.beginObject();
                                    while (reader2.hasNext()) {
                                        String name = reader2.nextName();
                                        switch (name) {
                                            case "id_cliente":
                                                idCliente = reader2.nextInt();
                                                break;
                                            case "mesas":
                                                mesas = reader2.nextString();
                                                break;
                                            case "nro_documento":
                                                nro_documento = reader2.nextInt();
                                                break;
                                            case "n_cliente":
                                                n_cliente =  reader2.nextString();
                                                break;
                                            case "montoPedido":
                                                montoPedido =  reader2.nextDouble();
                                                break;
                                            case "idEstadoCuenta":
                                                idEstadoCuenta =  reader2.nextInt();
                                                break;
                                            default:
                                                reader2.skipValue();
                                                break;
                                        }
                                    }

                                    CuentasMesa cm = new CuentasMesa();
                                    cm.setIdCliente(idCliente);
                                    cm.setNumeroMesa(mesas);
                                    cm.setNumeroDocumento(nro_documento);
                                    cm.setNombreCuenta(n_cliente);
                                    cm.setMontoPedido(montoPedido);
                                    cm.setIdEstado(idEstadoCuenta);

                                    lista.add(cm);

                                    // response += "\nMesa: " + id + " " + cantSillas + " " + idCuenta + " " + posicion + " " + numeroMesa + ".\n";
                                    reader2.endObject();
                                }




                            } catch (Exception e) {
                                e.printStackTrace();
                                urlConn1.disconnect();
                            }

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }



                } catch (Exception e) {
                e.printStackTrace();
                error = 1;
            }
            return null;
        }

        protected void onPostExecute(String st) {
            loadCuentasData(lista);
        }
    }



    @SuppressLint("ParcelCreator")
    public class MyResultReceiver extends ResultReceiver {

        public MyResultReceiver(Handler handler) {
            super(handler);
            // TODO Auto-generated constructor stub
        }


        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            //asi funciono pero me parece que esto a la larga consume mucha memoria porque
            // podria estar abriendo muchos hilos por cada actualizacion de pedidos

            String data = resultData.toString();
        }

    }
    private BroadcastReceiver onEvent=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent i) {

        }
    };

    @Override
    public void onBackPressed() {
        DialogSalir dialog = new DialogSalir();
        dialog.show(getFragmentManager(), "DialogSalir");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        ;

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button

    }
}

