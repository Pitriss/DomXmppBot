package com.hfad.botserver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

        Button verNodos = (Button) findViewById(R.id.btNodes);

        verNodos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, ListaNodeActivity.class);
                startActivity(intent);
            }
        });

    }


}
