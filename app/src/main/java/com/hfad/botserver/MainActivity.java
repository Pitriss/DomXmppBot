package com.hfad.botserver;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.Policy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dk.schaumburgit.stillsequencecamera.camera2.CaptureManager;

public class MainActivity extends AppCompatActivity {

    private BotService mService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button verNodos = (Button) findViewById(R.id.btNodes);
        Button bt_take = (Button) findViewById(R.id.btn_takepicture);

        verNodos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, ListaNodeActivity.class);
                startActivity(intent);
                //stopService(new Intent(MainActivity.this, BotService.class));

            }
        });



        bt_take.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //photo.takePicture();
                //Intent intent = new Intent(MainActivity.this, PhotoActivity.class);
                //startActivity(intent);
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

            Server.setInstance(mService.getServerInstance());
            XMPPCliente.setInstance(mService.getXMPPInstance());
            Nodos.setInstance(mService.getNodosInstance());

            mService.getServerInstance().initDataBase(MainActivity.this,MainActivity.this);

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
