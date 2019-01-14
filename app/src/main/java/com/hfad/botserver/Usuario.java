package com.hfad.botserver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Usuario extends AppCompatActivity {

    private EditText usuario;
    private EditText password;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        usuario = (EditText)findViewById(R.id.editUser);
        password = (EditText)findViewById(R.id.editPassword);
        save = (Button) findViewById(R.id.btSalvar);

        usuario.setText(Server.getInstance().getDb().getUsuario());
        password.setText(Server.getInstance().getDb().getPassword());

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Server.getInstance().getDb().updateUsuario(usuario.getText(),password.getText());

                Intent returnIntent = new Intent();
                setResult(Usuario.RESULT_OK,returnIntent);

                finish();

            }
        });

    }
}
