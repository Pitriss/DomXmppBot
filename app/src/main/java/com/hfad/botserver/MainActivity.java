package com.hfad.botserver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {

    BotService mService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //String ip = Server.getInstance().getIpAddress();
        //TextView label = (TextView) findViewById(R.id.ipServer);
        //label.setText(ip);

        Button verNodos = (Button) findViewById(R.id.btNodes);

        verNodos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, ListaNodeActivity.class);
                startActivity(intent);
                //stopService(new Intent(MainActivity.this, BotService.class));

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();

        if(mService == null) {

            startService(new Intent(MainActivity.this, BotService.class));
           // startForegroundService(new Intent(MainActivity.this, BotService.class));
            Intent intent = new Intent(this, BotService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }

    }

    public void onBtTestClick(View v) {
        mService.getNumberTest();
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            BotService.LocalBinder binder = (BotService.LocalBinder) service;
            mService = binder.getService();
            mService.getServerInstance().initDataBase(MainActivity.this);

            String ip = mService.getServerInstance().getIpAddress();
            TextView label = (TextView) findViewById(R.id.ipServer);
            label.setText(ip);

//            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
//            mBound = false;
        }

        @Override
        public void onNullBinding(ComponentName name)
        {
            Log.i("Service","NULL");
        }

    };


}
