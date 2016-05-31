package com.example.manu.myapplication;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.os.ResultReceiver;
import android.util.JsonReader;
import android.util.JsonToken;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server extends ListActivity implements OnItemClickListener,OnClickListener {

    private String URLGlobal;
    private static final ExecutorService THREADPOOL = Executors.newCachedThreadPool();
    private Button btnEnviarNotificacion;
    private TextView txtNotificaciones;
    private TextView txtIdPedido;
    private Button btnEnviarIdPedido;
    Socket skCliente;

    private int SocketServerPort = 47000;
    private static final String REQUEST_CONNECT_CLIENT = "request-connect-client";
    private List<String> clientIPs;
    private String respuesta = "no hay";
    SocketServerTask sst;
    actualizarEstadoPedido taskActualizarEstadoPedido;
    Thread threadListener;
    Thread thPedidos;
    Thread threadActualizadorEstadoPedido;
    Intent i;
    MyResultReceiver mReceiver;

    //lista
    private DetallePedidoAdapter detallePedidoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        URLGlobal = getIntent().getExtras().get("URLGlobal").toString();//CON ESTA LINEA SETEAS LA URLGLOBAL DESPUES USALA DONDE TE SIRVA


        txtNotificaciones = (TextView) findViewById(R.id.txtNotificaciones);
        txtIdPedido = (TextView) findViewById(R.id.txtIdPedido);
        btnEnviarIdPedido = (Button) findViewById(R.id.btnEnviarPedido);

        this.registerForContextMenu(getListView());

        detallePedidoAdapter = new DetallePedidoAdapter(Server.this);
        //cargar detalles en el adapter consultando la web api.

        thPedidos = new Thread(new Runnable() {
            @Override
            public void run() {
                obtenerDetallesPedidos();
            }
        });
        thPedidos.start();


        mReceiver = new MyResultReceiver(new Handler());

        setListAdapter(detallePedidoAdapter);

        detallePedidoAdapter.notifyDataSetChanged();


        clientIPs = new ArrayList<String>();


        btnEnviarIdPedido.setOnClickListener(this);

        txtNotificaciones.setText("Notifiaciones  /t");

        //si usamos un servicio
      i =new Intent(this, ServicioListenerPedidos.class);

        i.putExtra("receiverTag", mReceiver);

        threadListener = new Thread(new Runnable() {
            @Override
            public void run() {
               Context c = getApplicationContext();
                c.startService(i);
            }
        });
        threadListener.start();

        getListView().setOnItemClickListener(this);


        //si no usamos un servicio
        //sst = new SocketServerTask();
        //sst.execute();
    }

    private BroadcastReceiver onEvent=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent i) {
           txtNotificaciones.setText(i.getStringExtra("json"));
        }
    };

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_opciones_actualizar_pedido, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final DetallePedido detalle =(DetallePedido) detallePedidoAdapter.getItem(info.position);
        switch (item.getItemId()) {
            case R.id.pedidoListo:
                if(detalle.getIdDetallePedido() > 0)
                {
                    Thread thPedidos = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            actualizarEstadoDetallePedidoFinalizado(detalle);
                            obtenerDetallesPedidos();
                        }
                    });
                    thPedidos.start();
                return true;}
            case R.id.pedidoCancelado:
                if(detalle.getIdDetallePedido() > 0)
                {
                    Thread thPedidos = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            actualizarEstadoDetallePedidoFinalizado(detalle);
                            obtenerDetallesPedidos();
                        }
                    });
                    thPedidos.start();
                    return true;}
            default:
                return super.onContextItemSelected(item);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View convertView, int position, long arg3) {

        final DetallePedido detalle;
        detalle = (DetallePedido)detallePedidoAdapter.getItem(position);

        if(detalle.getIdDetallePedido() > 0)
        {
            Thread thPedidos = new Thread(new Runnable() {
                @Override
                public void run() {
                    actualizarEstadoDetallePedidoFinalizado(detalle);
                    obtenerDetallesPedidos();
                }
            });
            thPedidos.start();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_server, menu);
        return true;
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int idPedido = Integer.parseInt(txtIdPedido.getText().toString());
        int idEstado = 3;
        final JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("idPedido", idPedido);
            jsonData.put("idEstado", idEstado);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        taskActualizarEstadoPedido = new actualizarEstadoPedido();
        taskActualizarEstadoPedido.execute(jsonData);

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


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(onEvent);
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter f=new IntentFilter();

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(onEvent, f);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void obtenerDetallesPedidos()
    {
        //cargo el adapter con los detalles
        try {
            String response = "No se conecto";
            try {
                HttpURLConnection urlConn;
                StringBuilder result = new StringBuilder();
                URL url = new URL(URLGlobal + "pedido/detallesPedido");
                ArrayList<DetallePedido> detallesArray;
                ArrayList<Pedido> pedidosArray = new ArrayList<>();
                Pedido pedido;
                DetallePedido detalle;

                urlConn = (HttpURLConnection) url.openConnection();

                InputStream in = new BufferedInputStream(urlConn.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }


                try {
                    JsonReader reader1 = new JsonReader(new InputStreamReader(new ByteArrayInputStream(result.toString().getBytes(StandardCharsets.UTF_8))));

                    try {

                        response = "GET: ";

                        reader1.beginArray();
                        while (reader1.hasNext()) {
                            pedido = new Pedido();
                            reader1.beginObject();

                            while (reader1.hasNext()) {
                                String name = reader1.nextName();
                                switch (name) {
                                    case "idPedido":
                                        pedido.setIdPedido(reader1.nextInt());
                                        break;
                                    case "idEstado":
                                        pedido.setIdEstado(reader1.nextInt());
                                        break;
                                    case "idCuenta":

                                        if (reader1.peek() == JsonToken.NULL) {
                                            pedido.setIdCuenta(0);
                                            reader1.skipValue();
                                        } else {
                                            pedido.setIdCuenta(reader1.nextInt());
                                        }
                                        break;
                                    case "fechaAlta":
                                        pedido.setFechaAlta(null);
                                        reader1.skipValue();
                                        break;
                                    default:
                                        reader1.skipValue();
                                        break;
                                    case "arrayDetallesPedido": {
                                        if (reader1.peek() == JsonToken.NULL) {
                                            reader1.skipValue();
                                        } else {

                                            detallesArray = new ArrayList<>();
                                            reader1.beginArray();
                                            while (reader1.hasNext()) {
                                                detalle = new DetallePedido();
                                                reader1.beginObject();
                                                while (reader1.hasNext()) {
                                                    String nombre = reader1.nextName();
                                                    switch (nombre) {
                                                        case "idDetallePedido":
                                                            detalle.setIdDetallePedido(reader1.nextInt());
                                                            break;
                                                        case "idPedido":
                                                            detalle.setIdPedido(reader1.nextInt());
                                                            break;
                                                        case "nombreMenu":
                                                            if (reader1.peek() == JsonToken.NULL) {
                                                                pedido.setIdCuenta(0);
                                                                reader1.skipValue();
                                                            } else {
                                                                detalle.setNombre(reader1.nextString());
                                                            }
                                                            break;
                                                        case "nombreInsumo":
                                                            if (reader1.peek() == JsonToken.NULL) {
                                                                pedido.setIdCuenta(0);
                                                                reader1.skipValue();
                                                            } else {
                                                                detalle.setNombre(reader1.nextString());
                                                            }
                                                            break;
                                                        case "cantidad":
                                                            detalle.setCantidad(reader1.nextInt());
                                                            break;
                                                        default:
                                                            reader1.skipValue();
                                                            break;
                                                    }
                                                }
                                                detallesArray.add(detalle);
                                                reader1.endObject();
                                            }
                                            reader1.endArray();
                                            DetallePedido[] detallesVector = new DetallePedido[detallesArray.size()];
                                            for (int i = 0; i < detallesArray.size(); i++) {
                                                detallesVector[i] = new DetallePedido();
                                                detallesVector[i].setNombre(detallesArray.get(i).getNombre());
                                                detallesVector[i].setCantidad(detallesArray.get(i).getCantidad());
                                                detallesVector[i].setIdDetallePedido(detallesArray.get(i).getIdDetallePedido());
                                                detallesVector[i].setIdPedido(detallesArray.get(i).getIdPedido());
                                            }
                                            pedido.setArrayDetallesPedido(detallesVector);
                                        }

                                    }

                                }
                            }
                            response += "\nidPedido: " + pedido.getIdPedido() + " idEstado: " + pedido.getIdEstado() + " idCuenta:" + pedido.getIdCuenta() + " fechaAlta:" + pedido.getFechaAlta() + ".\n";
                            pedidosArray.add(pedido);
                            reader1.endObject();



                        }


                        reader.close();
                        in.close();
                        urlConn.disconnect();
                        ArrayList<DetallePedido> listaPedidos = new ArrayList<>();
                        for (Pedido p:pedidosArray
                                ) {
                            if(p.getArrayDetallesPedido() != null) {
                                for (DetallePedido d : p.getArrayDetallesPedido()
                                        ) {
                                    listaPedidos.add(d);
                                }
                            }
                        }
                        detallePedidoAdapter.setPedidos(listaPedidos);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                detallePedidoAdapter.notifyDataSetChanged();
                            }
                        });



                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            {

                respuesta = response;

            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void obtenerMesas() {
        try {
            String response = "No se conecto";
            try {
                HttpURLConnection urlConn;
                StringBuilder result = new StringBuilder();
                URL url = new URL(URLGlobal + "mesas/mesas2");

                urlConn = (HttpURLConnection) url.openConnection();

                InputStream in = new BufferedInputStream(urlConn.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }


                try {
                    JsonReader reader1 = new JsonReader(new InputStreamReader(new ByteArrayInputStream(result.toString().getBytes(StandardCharsets.UTF_8))));

                    try {

                        response = "GET: ";

                        reader1.beginArray();
                        while (reader1.hasNext()) {
                            int id = -1;
                            int cantSillas = -1;
                            int idCuenta = -1;
                            int posicion = -1;
                            int numeroMesa = -1;
                            reader1.beginObject();
                            while (reader1.hasNext()) {
                                String name = reader1.nextName();
                                switch (name) {
                                    case "idMesa":
                                        id = reader1.nextInt();
                                        break;
                                    case "cantSillas":
                                        cantSillas = reader1.nextInt();
                                        break;
                                    case "idCuenta":

                                        if (reader1.peek() == JsonToken.NULL) {
                                            idCuenta = 0;
                                            reader1.skipValue();
                                        } else {
                                            idCuenta = reader1.nextInt();
                                        }
                                        break;
                                    case "posicion":
                                        posicion = reader1.nextInt();
                                        break;
                                    case "numeroMesa":
                                        numeroMesa = reader1.nextInt();
                                        break;
                                    default:
                                        reader1.skipValue();
                                        break;
                                }
                            }
                            response += "\nMesa: " + id + " sillas: " + cantSillas + " cuenta:" + idCuenta + " posicion:" + posicion + " nro mesa:" + numeroMesa + ".\n";
                            reader1.endObject();
                            in.close();
                            reader.close();
                            urlConn.disconnect();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            {

                respuesta = response;

            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void actualizarTexto() {
        txtNotificaciones.setText(respuesta);
    }
    public void actualizarTexto2() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtNotificaciones.setText(respuesta + " agreago prueba /n");
            }
        });

    }

    public void actualizarEstadoDetallePedidoFinalizado(DetallePedido detalle)
    {
        HttpURLConnection urlConn = null;
        try {

            String response = "No se conecto";
            StringBuilder result = new StringBuilder();
            URL url2 = new URL(URLGlobal + "pedido/actualizarDetallePedido");

            urlConn = (HttpURLConnection) url2.openConnection();
            try {


                urlConn.setChunkedStreamingMode(0);
                urlConn.setDoOutput(true);
                urlConn.setDoInput(true);
                urlConn.setRequestProperty("Content-Type", "application/json");
                urlConn.setRequestMethod("POST");
                urlConn.connect();
                //Create JSONObject here
                JSONObject jsonParam = new JSONObject();

                jsonParam.put("idDetallePedido", detalle.getIdDetallePedido());
                jsonParam.put("nombreEstado", "LISTO");
                jsonParam.put("idPedido",detalle.getIdPedido());


                PrintWriter ow = new PrintWriter(urlConn.getOutputStream());

                ow.print(jsonParam.toString());
                // ow.flush();
                ow.close();

                int reqcoda = urlConn.getResponseCode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
        } finally {
            if (urlConn != null)
                urlConn.disconnect();
        }
    }

    public void conectarActualizarPedido(int idPedido, int idEstado) {
        HttpURLConnection urlConn = null;
        try {

            String response = "No se conecto";
            StringBuilder result = new StringBuilder();
            URL url2 = new URL(URLGlobal + "mesas/actualizarPedido");

            urlConn = (HttpURLConnection) url2.openConnection();
            try {


                urlConn.setChunkedStreamingMode(0);
                urlConn.setDoOutput(true);
                urlConn.setDoInput(true);
                urlConn.setRequestProperty("Content-Type", "application/json");
                urlConn.setRequestMethod("POST");
                urlConn.connect();
                //Create JSONObject here
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonDetallePedido;
                jsonParam.put("idPedido", idPedido);
                jsonParam.put("idEstado", idEstado);

                JSONArray jsonDetallesPedido = new JSONArray();
                for(int i=0;i<8;i++)
                {
                    jsonDetallePedido = new JSONObject();
                    jsonDetallePedido.put("idDetallePedido",i);
                    jsonDetallesPedido.put(jsonDetallePedido);
                }

                jsonParam.put("arrayDetallesPedido",jsonDetallesPedido);

                PrintWriter ow = new PrintWriter(urlConn.getOutputStream());

                ow.print(jsonParam.toString());
                // ow.flush();
                ow.close();

               int reqcoda = urlConn.getResponseCode();
            } catch (Exception e) {
            }
        } catch (Exception e) {
        } finally {
            if (urlConn != null)
                urlConn.disconnect();
        }
    }

    private class actualizarEstadoPedido extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {

            int idPedido = 0;
            int idEstado = 0;
            JSONObject jsonData = params[0];
            try {
                idPedido = jsonData.getInt("idPedido");
                idEstado = jsonData.getInt("idEstado");
            } catch (JSONException e) {
                e.printStackTrace();
            }

           conectarActualizarPedido(idPedido, idEstado);
            //obtenerMesas();
           // actualizarTexto2();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                this.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    private class SocketServerTask extends AsyncTask<JSONObject, Void, Void> {


        @Override
        protected Void doInBackground(JSONObject... params) {
            Runnable runable = new Runnable() {
                @Override
                public void run() {
                    Socket socket = null;
                    DataInputStream dataInputStream = null;
                    DataOutputStream dataOutputStream = null;
                    ServerSocket serverSocket;

                    try {
                        //Log.i(TAG, "Creating server socket");
                        serverSocket = new ServerSocket(SocketServerPort);

                        while (true) {
                            socket = serverSocket.accept();
                            skCliente = socket;
                            dataInputStream = new DataInputStream(
                                    socket.getInputStream());
                            dataOutputStream = new DataOutputStream(
                                    socket.getOutputStream());

                            String messageFromClient, messageToClient, request;

                            //If no message sent from client, this code will block the program
                            //  messageFromClient = dataInputStream.readUTF();


                            final JSONObject jsondata;
                            // try {
                            // jsondata = new JSONObject(messageFromClient);


                            // request = jsondata.getString("request");

                            // if (request.equals(REQUEST_CONNECT_CLIENT)) {
                            // String clientIPAddress = jsondata.getString("ipAddress");
                            //txtIp.setText(clientIPAddress);

                            // Add client IP to a list
                            // clientIPs.add(clientIPAddress);
                            messageToClient = "Conexion Aceptada!!!";
                            obtenerMesas();
                            //llamariamos a la web api y actualizariamos los pedidos pendientes a cocina....
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    actualizarTexto();
                                }
                            });
                            // dataOutputStream.writeUTF(messageToClient);
                            //  }
                            //else {
                            // There might be other queries, but as of now nothing.
                            // dataOutputStream.flush();
                            //}
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        //Log.e(TAG, "Unable to get request");
//                        dataOutputStream.flush();
//                    }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (socket != null) {
                            try {
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        if (dataInputStream != null) {
                            try {
                                dataInputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        if (dataOutputStream != null) {
                            try {
                                dataOutputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            };
            Thread threadListener = new Thread(null, runable, "hiloEscuchador");
            threadListener.run();

            return null;
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
            Thread thpedidos2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    thPedidos.run();
                }
            });
            thpedidos2.start();
        }

    }


}
