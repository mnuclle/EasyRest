package com.example.manu.myapplication;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.os.ResultReceiver;
import android.util.JsonReader;
import android.util.JsonToken;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by Danielito on 12/05/2016.
 */
public class ServicioListenerPedidos extends IntentService {

    private int SocketServerPort = 47000;
    Socket skCliente;
    String respuesta = "sin respuesta";
    ResultReceiver rec;
    private String URLGlobal;
    private Notification notificacion;
    private NotificationManager notifMan;
    public ServicioListenerPedidos() {
        super("ServicioListenerPedidos");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        URLGlobal = intent.getExtras().getString("URLGlobal").toString();
         rec= intent.getParcelableExtra("receiverTag");
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

                Intent intNotif = new Intent(this,Server.class);
                intNotif.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);;
                PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                        intNotif, 0);


                notifMan = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification notification = new Notification.Builder(this)
                        .setSmallIcon(R.drawable.logo)  // the status icon
                        .setTicker("NotificacionStatus")  // the status text
                        .setWhen(System.currentTimeMillis())  // the time stamp
                        .setContentTitle(getText(R.string.app_name))  // the label of the entry
                        .setContentText("Nuevo Pedido.")  // the contents of the entry
                        .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                        .build();

                notification.flags |= Notification.FLAG_ONGOING_EVENT |Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;



                Intent in = new Intent();
                String mesas = obtenerMesas();
                Bundle b = new Bundle();
                b.putString("mesas",mesas);
                rec.send(0,b);
                in.putExtra("json",mesas);
                LocalBroadcastManager.getInstance(this)
                        .sendBroadcast(in);

                notifMan.notify(R.string.app_name,notification);

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


    public String obtenerMesas() {
        try {
            String response = "No se conecto";
            try {
                HttpURLConnection urlConn;
                StringBuilder result = new StringBuilder();
                URL url = new URL("http://192.168.1.3:8082/api/mesas/mesas2");

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

                        }
                        in.close();
                        reader.close();
                        urlConn.disconnect();


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
        return respuesta;
    }
}

