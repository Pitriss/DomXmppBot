package com.hfad.botserver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class NodoUpdateInfoActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY =
            "BotServer.Update.Node.REPLY";

    private Nodo actual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nodo_update_info);
        int posicion = (int)getIntent().getExtras().getSerializable("Posicion");
        ArrayList<Nodo> nodos = Nodos.getInstance().getAlmacen();

        actual = nodos.get(posicion);
        final EditText nodo = (EditText) findViewById(R.id.edNodo);
        final EditText sensor = (EditText) findViewById(R.id.edSensor);
        final EditText actuador = (EditText) findViewById(R.id.edActuador);

        nodo.setText(actual.getName());
        sensor.setText(actual.getSensorName());
        actuador.setText(actual.getActuadorName());

        Button guardarNodos = (Button) findViewById(R.id.btGuardar);

        guardarNodos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Intent intent = new Intent(MainActivity.this, ListaNodeActivity.class);
                //startActivity(intent);

                actual.setName(nodo.getText().toString());
                actual.setSensorName(sensor.getText().toString());
                actual.setActuadorName(actuador.getText().toString());

                Intent replyIntent = new Intent();
                replyIntent.putExtra(EXTRA_REPLY, "update");
                setResult(RESULT_OK,replyIntent);
                finish();

            }
        });

    }
}
