package com.example.manu.myapplication;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private EditText txtUsuario;
    private EditText txtContraseña;
    private Button btnIniciarSesion;
    private  Button btnPost;
    private String nombreUsuario;
    private String contraseña;
    private Spinner cmbTipoUsuario;
    private int tipoUsr = 0;
    /*si es 1 es cliente si es 2 es Empleado*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtContraseña = (EditText) findViewById(R.id.txtContraseña);
        btnIniciarSesion = (Button) findViewById(R.id.btnIniciarSesion);
        btnPost = (Button) findViewById(R.id.btnPost);
        cmbTipoUsuario = (Spinner) findViewById(R.id.cmbTipoUsuario);

        loadSpinner();

        nombreUsuario = txtUsuario.getText().toString().trim();
        contraseña = txtContraseña.getText().toString().trim();

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                int corePoolSize = 60;
                int maximumPoolSize = 80;
                int keepAliveTime = 10;

                BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<Runnable>(maximumPoolSize);
                Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue) ;

                    if(tipoUsr == 1)
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            new PostTask().executeOnExecutor(threadPoolExecutor);
                        } else {
                            new PostTask().execute();
                        }

                    }
                    else if(tipoUsr == 2)
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            new PostTaskEmp().executeOnExecutor(threadPoolExecutor);
                        } else {
                            new PostTaskEmp().execute();
                        }
                    }
                    else
                    {
                        Toast.makeText(v.getContext(),"No selecciono tipo empleado",Toast.LENGTH_LONG).show();
                    }


                    /************ GET **********
                    new GetTask().execute();
                     */
            }

        }
        );

        btnPost.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {


                /************ POST *********
                new PostTask().execute();
                 */

            }

        }
        );



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
                }
                if (empleado.equals(text)) {
                    tipoUsr = 2;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void executePostCliente()
    {
        new PostTask().execute();
    }

    private void executePostEmpleado()
    {
        new PostTaskEmp().execute();
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
    class PostTask extends AsyncTask<String,String,String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                String response = "No se conecto";
                StringBuilder result = new StringBuilder();
                URL url2 = new URL("http://172.16.0.2:8082/api/usuario/validarUsuarioCliente");
                HttpURLConnection urlConn = (HttpURLConnection)url2.openConnection();
                try {

                    urlConn.setChunkedStreamingMode(0);
                    urlConn.setDoOutput(true);
                    urlConn.setDoInput(true);
                    urlConn.setRequestProperty("Content-Type", "application/json");
                    urlConn.setRequestMethod("POST");
                    urlConn.connect();
                    //Create JSONObject here
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("documento", "34555333");

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
                        try {
                            JsonReader reader1 = new JsonReader(new InputStreamReader(new ByteArrayInputStream(result.toString().getBytes(StandardCharsets.UTF_8))));

                            try {

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





                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        response = "USUARIO Y CONTRASEÑA NO VALIDOS";
                    }



                } catch (Exception e) {
                    e.printStackTrace();

                }
                finally {
                    urlConn.disconnect();



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

    class PostTaskEmp extends AsyncTask<String,String,String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                String response = "No se conecto";
                StringBuilder result = new StringBuilder();
                URL url2 = new URL("http://172.16.0.2:8082/api/usuario/validarUsuarioEmpleado");
                HttpURLConnection urlConn = (HttpURLConnection)url2.openConnection();
                try {

                    urlConn.setChunkedStreamingMode(0);
                    urlConn.setDoOutput(true);
                    urlConn.setDoInput(true);
                    urlConn.setRequestProperty("Content-Type", "application/json");
                    urlConn.setRequestMethod("POST");
                    urlConn.connect();
                    //Create JSONObject here
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("nombreUsuario", "PEDROLOPEZ");
                    jsonParam.put("contraseña", "PLOPEZ1");

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
                        try {
                            JsonReader reader1 = new JsonReader(new InputStreamReader(new ByteArrayInputStream(result.toString().getBytes(StandardCharsets.UTF_8))));

                            try {

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
                                response += "\nEmpleado: " + idUsuario + " " + nombreUsuario + " " + idRol + " " + fechaAlta + " " + fechaBaja + ".";
                                reader1.endObject();





                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        response = "USUARIO Y CONTRASEÑA NO VALIDOS";
                    }



                } catch (Exception e) {
                    e.printStackTrace();

                }
                finally {
                    urlConn.disconnect();



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
