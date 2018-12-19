package com.hfad.botserver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        server = new Server(this);
        XMPPCliente.getInstance().runLoggin("lamulita","Portal06","404.city");
        String ip = server.getIpAddress();
        TextView label = (TextView) findViewById(R.id.ipServer);
        label.setText(ip);
    }
}
