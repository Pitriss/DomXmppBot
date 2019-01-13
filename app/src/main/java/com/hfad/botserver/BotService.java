package com.hfad.botserver;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class BotService extends Service {

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    // Random number generator
    private final Random mGenerator = new Random();

    private int numberTest = 0;
    //MyTask myTask = null;

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        BotService getService() {
            // Return this instance of LocalService so clients can call public methods
            return BotService.this;
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(numberTest == 0) {
            numberTest++;
            Thread check = new Thread(new CheckAlive());
            check.start();
            Toast.makeText(this, "Servicio creado!", Toast.LENGTH_SHORT).show();
        }


        /*if(myTask == null) {
            Toast.makeText(this, "Servicio creado!", Toast.LENGTH_SHORT).show();
            myTask = new MyTask();
            myTask.execute();
        }*/

        return START_STICKY;//super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Servicio destruído!", Toast.LENGTH_SHORT).show();
        //myTask.cancel(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        //throw new UnsupportedOperationException("Not yet implemented");
        return mBinder;
    }

    private class CheckAlive extends Thread {
        @Override
        public void run() {

            Server.getInstance();//.initDataBase(this);
            XMPPCliente.getInstance().runLoggin("lamulita","Portal06","404.city");

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(BotService.this,"1234")
                            .setSmallIcon(android.R.drawable.stat_notify_sync)
                            .setContentTitle("Mensaje de Alerta")
                            .setContentText("Ejemplo de notificación.");
                            //.setOngoing(true);



            //Intent notIntent = intent;

            //PendingIntent contIntent =
            //        PendingIntent.getActivity(
            //                this, 0, notIntent, 0);

            //mBuilder.setContentIntent(contIntent);

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            Notification notification = mBuilder.build();
            mNotificationManager.notify(1234, notification);

            startForeground(1234,notification);

            /*while(true) {
                try {
                    //Server.getInstance();
                    //XMPPCliente.getInstance();
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/
        }
    }



    public int getNumberTest()
    {
        return 0;//myTask.increseNumber();
    }

    public Server getServerInstance(){
        return Server.getInstance();
    }
    public XMPPCliente getXMPPInstance()
    {
        return XMPPCliente.getInstance();
    }

    public Nodos getNodosInstance()
    {
        return Nodos.getInstance();
    }
}
