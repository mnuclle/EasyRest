package com.example.manu.myapplication.Cliente;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.JsonReader;
import android.util.JsonToken;
import android.view.View;
import android.widget.Toast;

import com.example.manu.myapplication.Entidades.DetallePedido;
import com.example.manu.myapplication.Entidades.Pedidos;
import com.example.manu.myapplication.R;

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

public class ClienteOpcionesActivity extends Activity {

    private int NroCliente;
    private String URLGlobal;
    private FloatingActionButton btnVerCuenta, btnLlamarMozo,btnVerPedido;
    private String cuentaPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_opciones);

        Intent intent = getIntent();
        NroCliente = (int) intent.getExtras().get("IDCLIENTE");
        URLGlobal = intent.getExtras().get("URLGlobal").toString();
        cuentaPedido = intent.getExtras().get("CUENTAPEDIDO").toString();

        btnVerCuenta = (FloatingActionButton) findViewById(R.id.btnVerCuenta);
        btnLlamarMozo = (FloatingActionButton) findViewById(R.id.btnLlamarMozo);
        btnVerPedido = (FloatingActionButton) findViewById(R.id.btnVerPedido);


        btnVerPedido.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Object[] listaObjetos = new Object[5];
                listaObjetos[0] = NroCliente;
                listaObjetos[1] = URLGlobal;
                listaObjetos[2] = cuentaPedido;
                listaObjetos[3] = 1;
                new GetTask().execute(listaObjetos);
            }

        });

        btnVerCuenta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Object[] listaObjetos = new Object[5];
                listaObjetos[0] = NroCliente;
                listaObjetos[1] = URLGlobal;
                listaObjetos[2] = cuentaPedido;
                listaObjetos[3] = 2;
                new GetTask().execute(listaObjetos);
            }

        });


        btnLlamarMozo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object[] obj = new Object[2];
                obj[0] = NroCliente;
                obj[1] = URLGlobal;

                new PostTask(v.getContext()).execute(obj);
            }
        });

    }
    class PostTask extends AsyncTask<Object, String, String> {

        private Exception exception;
        private Context contexto;
        private int error = 0;
        String response = "No se conecto";
        ArrayList<DetallePedido> lista;
        private String URLGlobal;
        private int NroCliente;

        public PostTask(Context context) {
            this.contexto = context;
        }

        protected String doInBackground(Object... params) {
            URLGlobal = params[1].toString();
            NroCliente = (int)params[0];
            try {

                StringBuilder result = new StringBuilder();
                URL url = new URL(URLGlobal+"mozo/llamarMozo");
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                try {
                    urlConn.setChunkedStreamingMode(0);
                    urlConn.setDoOutput(true);
                    urlConn.setDoInput(true);
                    urlConn.setRequestProperty("Content-Type", "application/json");
                    urlConn.setRequestMethod("POST");
                    urlConn.connect();


                    //Create JSONObject PEDIDO
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("idCuenta", NroCliente);
                    jsonParam.put("fechaAlta", null);
                    jsonParam.put("fechaBaja", null);
                    jsonParam.put("nombreCuenta", null);
                    jsonParam.put("numeroDocumento", null);
                    jsonParam.put("idEmpleado", null);
                    jsonParam.put("idUsuario", null);
                    jsonParam.put("telefono", null);
                    jsonParam.put("idTipoDocumento", null);

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
            Toast.makeText(contexto, "Se hizo un llamado al mozo. Espere y el mismo se acercará a la mesa.", Toast.LENGTH_LONG).show();
        }
    }
    class GetTask extends AsyncTask<Object,String,String> {

        private Exception exception;
        private Pedidos pedido = new Pedidos();
        private String URLGlobal;
        private ArrayList<DetallePedido> listaDetalles = new ArrayList<>();
        private ArrayList<DetallePedido> listaDetallesVer = new ArrayList<>();

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

                        try {
                            HttpURLConnection urlConn;
                            StringBuilder result = new StringBuilder();
                            URL url = new URL(URLGlobal + "pedido/obtenerDetalleCuentaDePedido/" + pedido.getIdPedido());

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
                                    int cantidad = -1;
                                    int idMenu = -1;
                                    int idInsumo = -1;
                                    String nombre = "";
                                    double totalDetalle = -1;
                                    double precio = -1;
                                    int idCategoria = -1;
                                    reader1.beginObject();
                                    while (reader1.hasNext()) {
                                        String name = reader1.nextName();
                                        switch (name) {
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
                                            case "nombre":
                                                nombre = reader1.nextString();
                                                break;
                                            case "idCategoria":
                                                idCategoria = reader1.nextInt();
                                                break;
                                            case "precio":
                                                precio = reader1.nextDouble();
                                                break;
                                            default:
                                                reader1.skipValue();
                                                break;
                                        }
                                    }
//                                response += "\nMesa: " + id + " " + cantSillas + " " + idCuenta + " " + posicion + " " + numeroMesa + ".\n";
                                    reader1.endObject();
                                    detallePed.setPrecio(precio);
                                    detallePed.setIdCategoria(idCategoria);
                                    detallePed.setCantidad(cantidad);
                                    detallePed.setNombreMenu(nombre);
                                    detallePed.setIdInsumo(idInsumo);
                                    detallePed.setIdMenu(idMenu);
                                    detallePed.setTotalDetalle(totalDetalle);
                                    listaDetallesVer.add(detallePed);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                urlConn.disconnect();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }




                        Intent intent;
                        if ((int)params[3] == 1) {
                            intent = new Intent(ClienteOpcionesActivity.this, ClientePedidoActivity.class);
                            intent.putExtra("LISTADETALLES", listaDetalles);
                        }
                        else
                        {
                            intent = new Intent(ClienteOpcionesActivity.this, ClienteVerCuentaActivity.class);
                            intent.putExtra("LISTADETALLES", listaDetallesVer);
                        }

                        intent.putExtra("IDCLIENTE",((int) params[0]));
                        intent.putExtra("URLGlobal", URLGlobal);
                        intent.putExtra("CUENTAPEDIDO",params[2].toString());
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


}
