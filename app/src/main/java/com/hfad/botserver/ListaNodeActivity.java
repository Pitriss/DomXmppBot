package com.hfad.botserver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ListaNodeActivity extends AppCompatActivity {

    public static final int TEXT_REQUEST = 1;
    private AdapterNodoItem adapter;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_node);

        //ArrayList<Nodo> nodos = Nodos.getInstance().getAlmacen();

        lv = (ListView) findViewById(R.id.list_nodos);
        adapter = new AdapterNodoItem(this, Nodos.getInstance().getAlmacen());
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                Intent intent = new Intent(ListaNodeActivity.this, NodoUpdateInfoActivity.class);
                intent.putExtra("Posicion",position);
                startActivityForResult(intent,TEXT_REQUEST);//CODIGO AQUI

            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {

        if (requestCode == TEXT_REQUEST) {
            lv.setAdapter(adapter);
        }

    }
}
