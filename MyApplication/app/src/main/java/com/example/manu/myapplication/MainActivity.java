package com.example.manu.myapplication;

import android.annotation.TargetApi;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.util.JsonReader;
import android.util.JsonToken;
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
    private TextView lblContraseña;
    private Spinner cmbTipoUsuario;
    private int tipoUsr = 0; /*si es 1 es cliente si es 2 es Empleado*/
    private Usuario us;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtContraseña = (EditText) findViewById(R.id.txtContraseña);
        lblContraseña = (TextView) findViewById(R.id.lblContraseña);
        btnIniciarSesion = (Button) findViewById(R.id.btnIniciarSesion);
        cmbTipoUsuario = (Spinner) findViewById(R.id.cmbTipoUsuario);
        loadSpinner();
        txtUsuario.setText("DAMIANCERRO");
        txtContraseña.setText("DCERRO1");

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
                        new PostTask(v.getContext()).execute(usa);
                    }
                    else if(tipoUsr == 2)
                    {
                        new PostTaskEmp(v.getContext()).execute(usa);

                    }
                    else
                    {
                        Toast.makeText(v.getContext(),"No selecciono tipo empleado",Toast.LENGTH_LONG).show();
                    }
            }

        }
        );




                                           /************ POST *********
                                            new PostTask().execute();
                                            */





    }

    private void loadSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.arrayTipoUsuario, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
                    lblContraseña.setVisibility(View.INVISIBLE);
                    txtContraseña.setVisibility(View.INVISIBLE);
                }
                if (empleado.equals(text)) {
                    tipoUsr = 2;
                    lblContraseña.setVisibility(View.VISIBLE);
                    txtContraseña.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    /*
    class GetTask extends AsyncTask<String,String,String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                String response = "No se conecto";
                try {
                    HttpURLConnection urlConn;
                    StringBuilder result = new StringBuilder();
                    URL url = new URL("http://172.16.0.2:8082/api/mesas");

                    urlConn = (HttpURLConnection)url.openConnection();

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
            while(reader1.hasNext()) {
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
                            }
                            else {
                                idCuenta = reader1.nextInt();
                            }
                            break;
                        case "posicion":
                            posicion =  reader1.nextInt();
                            break;
                        case "numeroMesa":
                            numeroMesa =  reader1.nextInt();
                            break;
                        default:
                            reader1.skipValue();
                            break;
                    }
                }
                response += "\nMesa: " + id + " " + cantSillas + " " + idCuenta + " " + posicion + " " + numeroMesa + ".\n";
                reader1.endObject();
            }




                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                {

                    Intent intent = new Intent(MainActivity.this, SaludarActivity.class);

                    Bundle b = new Bundle();
                    b.putString("USUARIO", response);

                    intent.putExtras(b);
                    startActivity(intent);

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

*/
    class PostTask extends AsyncTask<Usuario,String,String> {

        private Exception exception;
        private Context contexto;
        private int error = 0;
        String response = "No se conecto";
        public PostTask(Context context)
        {
            this.contexto=context;
        }
        protected String doInBackground(Usuario... params) {
            try {

                StringBuilder result = new StringBuilder();
                URL url2 = new URL("http://172.16.0.2:8082/api/usuario/validarUsuarioCliente");
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
                                int idUsuario = -1;
                                String nombreUsuario = "";
                                int idRol = -1;
                                String fechaAlta = "";
                                String fechaBaja = "";

                                while (reader1.hasNext()) {
                                    String name = reader1.nextName();
                                    switch (name) {
                                        case "idUsuario":
                                            idUsuario = reader1.nextInt();
                                            break;
                                        case "nombreUsuario":
                                            nombreUsuario = reader1.nextString();
                                            break;
                                        case "fechaBaja":

                                            if (reader1.peek() == JsonToken.NULL) {
                                                fechaBaja = "";
                                                reader1.skipValue();
                                            }
                                            else {
                                                fechaBaja = reader1.nextString();
                                            }
                                            break;
                                        case "idRol":
                                            idRol =  reader1.nextInt();
                                            break;
                                        case "fechaAlta":
                                            fechaAlta =  reader1.nextString();
                                            break;
                                        default:
                                            reader1.skipValue();
                                            break;
                                    }
                                }
                                response += "\nCliente: " + idUsuario + " " + nombreUsuario + " " + idRol + " " + fechaAlta + " " + fechaBaja + ".";
                                reader1.endObject();
                    }
                    else
                    {
                        response = "Usuario y contraseña incorrectos.";
                    }

                    Intent intent = new Intent(MainActivity.this, ListaCuentas.class);
                    Bundle b = new Bundle();
                    b.putString("USUARIO", response);

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
        public PostTaskEmp(Context contexto)
        {
            this.contexto = contexto;
        }
        protected String doInBackground(Usuario... params){
            try {

                StringBuilder result = new StringBuilder();
                URL url2 = new URL("http://172.16.0.2:8082/api/usuario/validarUsuarioEmpleado");
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
                                int idUsuario = -1;
                                String nombreUsuario = "";
                                int idRol = -1;
                                int idEmpleado = -1;

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
                                reader1.endObject();


                    }
                    else
                    {
                        response = "USUARIO Y CONTRASEÑA NO VALIDOS";
                    }

                    //validarmos tipo empleado
                    String tipoEmpleado ="";
                    HttpURLConnection urlConnnValidarTipoEmpleado;
                    URL urlValidarTipoEmpleado = new URL("http://172.16.0.2:8082/api/usuario/validarTipoEmpleado");
                    StringBuilder result2 = new StringBuilder();
                    urlConnnValidarTipoEmpleado = (HttpURLConnection)urlValidarTipoEmpleado.openConnection();

                    InputStream isValidarTipoEmpleado = new BufferedInputStream(urlConnnValidarTipoEmpleado.getInputStream());

                    BufferedReader readerValidarTipoEmpleado = new BufferedReader(new InputStreamReader(isValidarTipoEmpleado));

                    String lineValidarTipoEmpleado;
                    while ((lineValidarTipoEmpleado = readerValidarTipoEmpleado.readLine()) != null) {
                        result2.append(lineValidarTipoEmpleado);
                    }


                    try {
                        JsonReader jsonReader2 = new JsonReader(new InputStreamReader(new ByteArrayInputStream(result2.toString().getBytes(StandardCharsets.UTF_8))));

                        try {
                            response = "GET: ";

                            jsonReader2.beginArray();
                            while (jsonReader2.hasNext()) {

                                jsonReader2.beginObject();
                                while (jsonReader2.hasNext()) {
                                    String name = jsonReader2.nextName();
                                    switch (name) {
                                        case "tipoEmpleado":
                                            tipoEmpleado = jsonReader2.nextString();
                                            break;
                                        default:
                                            jsonReader2.skipValue();
                                            break;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            urlConnnValidarTipoEmpleado.disconnect();
                        }

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }


                    if(tipoEmpleado.equals("MOZO"))
                    {
                        ArrayList<CuentasMesa> lista = new ArrayList<>();
                        HttpURLConnection urlConn1;
                        StringBuilder result1 = new StringBuilder();
                        URL url = new URL("http://172.16.0.2:8082/api/mesas/obtenerTodasCuentasMozo/"+b.getInt("IDEMPLEADO"));

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
                        //verificar si es mozo o cocinero

                        Intent intent = new Intent(MainActivity.this, ListaCuentas.class);
                        intent.putExtras(b);
                        intent.putExtra("LISTA",lista);
                        startActivity(intent);

                    }
                    else
                    {
                        if(tipoEmpleado.equals("COCINERO"))
                        {
                            Intent intent = new Intent(MainActivity.this,PedidosActivity.class);
                        }
                        else
                        {

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
