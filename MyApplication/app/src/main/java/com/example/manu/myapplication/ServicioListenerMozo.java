package com.example.manu.myapplication;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.os.ResultReceiver;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Created by Danielito on 02/09/2016.
 */
public class ServicioListenerMozo  extends IntentService {


    private int SocketServerPort = 47000;
    Socket skCliente;
    String respuesta = "sin respuesta";
    ResultReceiver rec;
    private String URLGlobal;
    private Notification notificacion;
    private NotificationManager notifMan;
    public ServicioListenerMozo() {
        super("ServicioListenerMozo");
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

                PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);

                //VER DONDE VA EST PARA RECIBIR LA INFO DE LA CUENTA QUE TE LLAMA

                String infoCuenta = "";
                String text ="";


                byte[] resultBuff = new byte[0];
                byte[] buff = new byte[1024];
                int k = -1;
                while((k = socket.getInputStream().read(buff, 0, buff.length)) > -1) {
                    byte[] tbuff = new byte[resultBuff.length + k]; // temp buffer size = bytes already read + bytes last read
                    System.arraycopy(resultBuff, 0, tbuff, 0, resultBuff.length); // copy previous bytes
                    System.arraycopy(buff, 0, tbuff, resultBuff.length, k);  // copy current lot
                    resultBuff = tbuff; // call the temp buffer as your result buff
                }

                String rtdo = new String(resultBuff);
                infoCuenta = rtdo;
                //FIN VER

                String messageFromClient, messageToClient, request;

                Intent intNotif = new Intent(this,ListaCuentas.class);


                intNotif.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);;
                PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                        intNotif, 0);
                Notification notification = null;
                if(!infoCuenta.contains("A$;")){
                    String nombreCuenta = infoCuenta.substring(0,infoCuenta.indexOf(";"));
                    String mesasSolicitantes = infoCuenta.substring((infoCuenta.indexOf(";")+1));
                    notifMan = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                   notification = new Notification.Builder(this)
                            .setSmallIcon(R.drawable.logo)  // the status icon
                            .setTicker("NotificacionStatus")  // the status text
                            .setWhen(System.currentTimeMillis())  // the time stamp
                            .setContentTitle(getText(R.string.app_name))  // the label of the entry
                            .setContentText("El cliente " + nombreCuenta +" lo requiere!! Mesas: " + mesasSolicitantes)  // the contents of the entry
                            .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                            .build();

                    notification.flags |=  Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
                }
                else
                {   infoCuenta = infoCuenta.substring((infoCuenta.indexOf("$;")+2));
                    String nombreCuenta = infoCuenta.substring(0,infoCuenta.indexOf(";"));
                    String mesasSolicitantes = infoCuenta.substring((infoCuenta.indexOf(";")+1));
                    notifMan = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notification = new Notification.Builder(this)
                            .setSmallIcon(R.drawable.logo)  // the status icon
                            .setTicker("NotificacionStatus")  // the status text
                            .setWhen(System.currentTimeMillis())  // the time stamp
                            .setContentTitle(getText(R.string.app_name))  // the label of the entry
                            .setContentText("El pedido de la cuenta " + nombreCuenta +" está listo!! Mesas: " + mesasSolicitantes)  // the contents of the entry
                            .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                            .build();

                    notification.flags |=  Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
                }




                Intent in = new Intent();
                String mesas = "Prueba";
                Bundle b = new Bundle();
                b.putString("mesas",mesas);
                rec.send(0,b);
                in.putExtra("json",mesas);
                LocalBroadcastManager.getInstance(this)
                        .sendBroadcast(in);
                ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                String nombreClase = taskInfo.get(0).topActivity.getClassName();
                if(nombreClase.contains("ListaCuentas") || nombreClase.contains("PedidosActivity"))
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
}
