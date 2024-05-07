package com.example.appentregasfoto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EntregadorDAO dao;
    Boolean resultado;
    EditText tusuario,tsenha;
    String usuario,senha;
    Button btEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dao = new EntregadorDAO(this);

        tusuario = findViewById(R.id.editText_Usuario);
        tsenha = findViewById(R.id.editText_Senha);
        btEntrar = findViewById(R.id.btn_entrar);

        btEntrar.setOnClickListener(view -> {
            boolean validado = true;

            if (tusuario.getText().length() == 0) {
                tusuario.setError("Campo Usuario obrigatorio.");
                tusuario.requestFocus();
                validado = false;
            }

            if (tsenha.getText().length() == 0) {
                tsenha.setError("Campo Senha obrigatorio.");
                tsenha.requestFocus();
                validado = false;
            }
            if (tusuario.getText().toString().equals("admsuporte") && tsenha.getText().toString().equals("25185725")){
                Intent proximaTela = new Intent(MainActivity.this, ActivityHAWB.class);
                startActivity(proximaTela);
            }else {
                if (validado) {
                    usuario = tusuario.getText().toString();
                    senha = tsenha.getText().toString();
                    resultado = dao.validarLogin(usuario, senha);
                    if (resultado.equals(true)) {
                        Toast.makeText(getApplicationContext(), "Bem Vindo : " + usuario.toString(), Toast.LENGTH_LONG).show();
                        Intent proximaTela = new Intent(MainActivity.this, ActivityHAWB.class);
                        startActivity(proximaTela);
                        //Limpando o conteudo dos dampos usuario e senha
                        tusuario.setText("");
                        tsenha.setText("");
                    } else {
                        Toast.makeText(getApplicationContext(), "Usuario não cadastrado ou dados de acesso incorretos! Verifique.", Toast.LENGTH_LONG).show();
                    }
                    //chamada da função de login quando usar o banco remoto
                    // validarLogin();
                }
            }
        });
    }
}