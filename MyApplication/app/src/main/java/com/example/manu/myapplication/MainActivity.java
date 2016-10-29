package com.example.manu.myapplication;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manu.myapplication.Cliente.ClienteOpcionesActivity;
import com.example.manu.myapplication.Entidades.CuentasMesa;

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

public class MainActivity extends Activity {

    private EditText txtUsuario;
    private EditText txtContraseña;
    private Button btnIniciarSesion;
    private TextView NombreApp;
    private Spinner cmbTipoUsuario;
    private int tipoUsr = 0; /*si es 1 es cliente si es 2 es Empleado*/
    private Usuario us;
    private String URLGlobal = "http://172.16.0.2:8082/api/";
    //private String URLGlobal = "http://192.168.1.3:8082/api/"; //ACA PONE TU URL DANI - creo que es esa que puse.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtContraseña = (EditText) findViewById(R.id.txtContraseña);
        btnIniciarSesion = (Button) findViewById(R.id.btnIniciarSesion);
        cmbTipoUsuario = (Spinner) findViewById(R.id.cmbTipoUsuario);
        NombreApp = (TextView) findViewById(R.id.NombreApp);
        loadSpinner();

        txtUsuario.setGravity(Gravity.CENTER_HORIZONTAL);
        txtContraseña.setGravity(Gravity.CENTER_HORIZONTAL);
        txtUsuario.setText("DAMIANCERRO");
        txtContraseña.setText("DCERRO1");
//        txtUsuario.setText("PEDROLOPEZ");
//        txtContraseña.setText("PLOPEZ1");

        Typeface type = Typeface.createFromAsset(getAssets(),"segoeui.ttf");
        txtUsuario.setTypeface(type);
        txtContraseña.setTypeface(type);
        btnIniciarSesion.setTypeface(type);
        NombreApp.setTypeface(type);

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                us = new Usuario();
                us.setContraseña(txtContraseña.getText().toString().trim());
                us.setNombreUsuario(txtUsuario.getText().toString().trim());

                Usuario[] usa = new Usuario[1];
                usa[0] = us;

                    if(tipoUsr == 1)
                    {
                        new PostTask(v.getContext(),URLGlobal).execute(usa);
                    }
                    else if(tipoUsr == 2)
                    {
                        new PostTaskEmp(v.getContext(),URLGlobal).execute(usa);

                    }
                    else
                    {
                        Toast.makeText(v.getContext(),"No selecciono tipo empleado",Toast.LENGTH_LONG).show();
                    }
            }

        }
        );


    }

    private void loadSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.arrayTipoUsuario, /*android.R.layout.simple_spinner_item*/R.layout.element_list_usuario);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(/*android.R.layout.simple_spinner_dropdown_item*/ R.layout.spinner_ddown_item);
        // Apply the adapter to the spinner
        this.cmbTipoUsuario.setAdapter(adapter);

        // This activity implements the AdapterView.OnItemSelectedListener
        this.cmbTipoUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = cmbTipoUsuario.getSelectedItem().toString();
                String cliente = "CLIENTE";
                String empleado = "EMPLEADO";
                if (cliente.equals(text)) {
                    tipoUsr = 1;
                    txtContraseña.setVisibility(View.INVISIBLE);
                }
                if (empleado.equals(text)) {
                    tipoUsr = 2;
                    txtContraseña.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    class PostTask extends AsyncTask<Usuario,String,String> {

        private Exception exception;
        private Context contexto;
        private int error = 0;
        String response = "No se conecto";
        private String URLGlobal;
        private int idUsuario;
        private String nombre;
        CuentasMesa cm = new CuentasMesa();
        public PostTask(Context context,String url)
        {
            this.URLGlobal = url;
            this.contexto=context;
        }
        protected String doInBackground(Usuario... params) {
            try {

                StringBuilder result = new StringBuilder();
                URL url2 = new URL(URLGlobal + "usuario/validarUsuarioCliente");
                HttpURLConnection urlConn = (HttpURLConnection)url2.openConnection();
                try {
                    Usuario us = params[0];
                    urlConn.setChunkedStreamingMode(0);
                    urlConn.setDoOutput(true);
                    urlConn.setDoInput(true);
                    urlConn.setRequestProperty("Content-Type", "application/json");
                    urlConn.setRequestMethod("POST");
                    urlConn.connect();
                    //Create JSONObject here
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("nombreUsuario", us.nombreUsuario);


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

                    if(leyo = true)
                    {
                        JsonReader reader1 = new JsonReader(new InputStreamReader(new ByteArrayInputStream(result.toString().getBytes(StandardCharsets.UTF_8))));

                            response = "POST: ";

                                reader1.beginObject();

                                int idCliente = -1;
                                String numeroMesa = "";
                                int numeroDocumento =0;
                                String nombreCuenta = "";
                                int idEstado = -1;
                                Double montoPedido = 0.0;

                                while (reader1.hasNext()) {
                                    String name = reader1.nextName();
                                    switch (name) {
                                        case "id_cliente" :
                                            idCliente = reader1.nextInt();
                                            break;
                                        case "mesas" :
                                            numeroMesa = reader1.nextString();
                                            break;
                                        case "nro_documento" :
                                            numeroDocumento = reader1.nextInt();
                                            break;
                                        case "n_cliente":
                                            nombreCuenta = reader1.nextString();
                                            break;
                                        case "idEstadoCuenta":
                                            idEstado = reader1.nextInt();
                                            break;
                                        case "montoPedido":
                                            montoPedido = reader1.nextDouble();
                                            break;
                                        default:
                                            reader1.skipValue();
                                            break;
                                    }
                                }
                        cm.setIdCliente(idCliente);
                        cm.setIdEstado(idEstado);
                        cm.setMontoPedido(montoPedido);
                        cm.setNombreCuenta(nombreCuenta);
                        cm.setNumeroDocumento(numeroDocumento);
                        cm.setNumeroMesa(numeroMesa);
                        reader1.endObject();

                    }
                    else
                    {
                        response = "Usuario y contraseña incorrectos.";
                    }

                    Intent intent = new Intent(MainActivity.this, ClienteOpcionesActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("IDCLIENTE", cm.getIdCliente());
                    b.putString("URLGlobal", URLGlobal);
                    b.putString("CUENTAPEDIDO","MESA: " + cm.getNumeroMesa() + " - CLIENTE: " + cm.getNumeroDocumento() + ", " + cm.getNombreCuenta());

                    intent.putExtras(b);
                    startActivity(intent);

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
            if (error == 1)
            {
                Toast.makeText(contexto,response,Toast.LENGTH_LONG).show();
            }
        }
    }

    class PostTaskEmp extends AsyncTask<Usuario,String,String> {

        private Exception exception;
        private int error = 0;
        private Context contexto;
        String response = "No se conecto";
        String URLGlobal;
        public PostTaskEmp(Context contexto,String url)
        {
            this.URLGlobal = url;
            this.contexto = contexto;
        }
        protected String doInBackground(Usuario... params){
            try {

                StringBuilder result = new StringBuilder();
                URL url2 = new URL(URLGlobal + "usuario/validarUsuarioEmpleado");
                HttpURLConnection urlConn = (HttpURLConnection)url2.openConnection();
                Usuario us = params[0];
                Bundle b = new Bundle();
                try {

                    urlConn.setChunkedStreamingMode(0);
                    urlConn.setDoOutput(true);
                    urlConn.setDoInput(true);
                    urlConn.setRequestProperty("Content-Type", "application/json");
                    urlConn.setRequestMethod("POST");
                    urlConn.connect();
                    //Create JSONObject here
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("nombreUsuario", us.getNombreUsuario());
                    jsonParam.put("contraseña", us.getContraseña());

                    PrintWriter ow = new PrintWriter(urlConn.getOutputStream());

                    ow.print(jsonParam.toString());
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

                    if(leyo = true)
                    {

                            JsonReader reader1 = new JsonReader(new InputStreamReader(new ByteArrayInputStream(result.toString().getBytes(StandardCharsets.UTF_8))));
                            response = "POST: ";
                            reader1.beginObject();
                                int idUsuario = -1;
                                String nombreUsuario = "";
                                int idRol = -1;
                                int idEmpleado = -1;
                                int idTipoEmpleado = -1;
                                while (reader1.hasNext()) {
                                    String name = reader1.nextName();
                                    switch (name) {
                                        case "idUsuario":
                                            idUsuario = reader1.nextInt();
                                            break;
                                        case "nombreUsuario":
                                            nombreUsuario = reader1.nextString();
                                            break;
                                        case "idRol":
                                            idRol =  reader1.nextInt();
                                            break;
                                        case "idEmpleado":
                                            idEmpleado =  reader1.nextInt();
                                            break;
                                        case "idTipoEmpleado":
                                            idTipoEmpleado = reader1.nextInt();
                                            break;
                                        default:
                                            reader1.skipValue();
                                            break;
                                    }
                                }
                                response += "\nEmpleado: " + idUsuario + " " + nombreUsuario + " " + idRol + " " + idEmpleado + ".";
                                b.putInt("IDUSUARIO", idUsuario);
                                b.putString("NOMBREUSUARIO", nombreUsuario);
                                b.putInt("IDROL", idRol);
                                b.putInt("IDEMPLEADO", idEmpleado);
                                b.putString("USUARIO", response);
                                b.putString("URLGlobal", URLGlobal);
                                b.putSerializable("IDTIPOEMPLEADO",idTipoEmpleado);
                                reader1.endObject();


                    }
                    else
                    {
                        response = "USUARIO Y CONTRASEÑA NO VALIDOS";
                    }

                    if (b.getInt("IDTIPOEMPLEADO") == 1) // 1 es el mozo
                    {
                        ArrayList<CuentasMesa> lista = new ArrayList<>();
                        HttpURLConnection urlConn1;
                        StringBuilder result1 = new StringBuilder();
                        URL url = new URL(URLGlobal + "mesas/obtenerTodasCuentasMozo/"+b.getInt("IDEMPLEADO"));

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

                        Intent intent = new Intent(MainActivity.this, ListaCuentas.class);
                        intent.putExtras(b);
                        intent.putExtra("LISTA",lista);
                        intent.putExtra("URLGlobal", URLGlobal);
                        intent.putExtra("IDEMPLEADO",b.getInt("IDEMPLEADO"));
                        startActivity(intent);

                    }
                    else
                    {
                        if (b.getInt("IDTIPOEMPLEADO") == 3) //COCINERO
                        {
                            Intent intent = new Intent(MainActivity.this, Server.class);
                            intent.putExtra("URLGlobal", URLGlobal);
                            startActivity(intent);
                        }

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
           if (error == 1)
           {
               Toast.makeText(contexto,response,Toast.LENGTH_LONG).show();
           }
        }
    }

    @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.menu_main, menu);
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
