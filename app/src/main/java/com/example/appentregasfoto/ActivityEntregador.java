package com.example.appentregasfoto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityEntregador extends AppCompatActivity {
    private ConexaoDB conexaoDb;
    private SQLiteDatabase banco;

    JSONArray jasonArray = null;
    JSONObject jsonObject = null;

    private String cnpjcpf;
    private String nome;
    private String usuario;
    private String senha;

    EditText tentregador;

    //endereco no meu micro
    //String urlEntregador = "http://192.168.0.158/N_entregas/rotinas_app/PegaEntregador.php";

    // url para onde enviar os dados no servidor internet
    String urlEntregador = "https://flypost.com.br/sis_entregas/rotinas_app/PegaEntregador.php";

    StringRequest stringRequest;
    RequestQueue requestQueue;

    private Button btnImportaEntregador;
    private EntregadorDAO dao;
    private ListView listaentregador;
    private List<ModeloEntregador> entregadores;

    public void EntregadorDAO(Context context){
        conexaoDb = new ConexaoDB(context);
        banco = conexaoDb.getWritableDatabase();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entregador);

        btnImportaEntregador = findViewById(R.id.btn_importa_entregador);
        tentregador = findViewById(R.id.editText_cnpj_cpf_entrega);

        dao = new EntregadorDAO(this);
        requestQueue = Volley.newRequestQueue(this);

        listaentregador = findViewById(R.id.Lista_entregador);
        entregadores = dao.obterTotal();
        ArrayAdapter<ModeloEntregador> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, entregadores);
        listaentregador.setAdapter(adaptador);

        btnImportaEntregador.setOnClickListener(view -> {
            dao.limpaTabelaEntregador("entregador");
            BuscaEntregadores();
        });
    }

    public void BuscaEntregadores(){

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST,urlEntregador,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.v("LogEntregador:", response);

                        try {
                            // aqui eu pego o resultado que veio da conexão na forma de um array
                            jasonArray = new JSONArray(response);

                            if(jasonArray.equals(null)){
                                Toast.makeText(getApplicationContext(), "Tabela Esta vazia! Verifique.", Toast.LENGTH_LONG).show();
                            }else {
                                ModeloEntregador oc = new ModeloEntregador();
                                for(int i = 0; i < jasonArray.length(); i++) {
                                    jsonObject = jasonArray.getJSONObject(i);
                                    oc.setCnpjcpf(jsonObject.getString("cnpjcpf"));
                                    oc.setNome(jsonObject.getString("nome"));
                                    oc.setUsuario(jsonObject.getString("usuario"));
                                    oc.setSenha(jsonObject.getString("senha"));
                                    long id = dao.inserir(oc);
                                }
                                listaentregador = findViewById(R.id.Lista_entregador);
                                entregadores = dao.obterTotal();
                                ArrayAdapter<ModeloEntregador> adaptador = new ArrayAdapter<ModeloEntregador>(ActivityEntregador.this, android.R.layout.simple_list_item_1, entregadores);
                                listaentregador.setAdapter(adaptador);
                                tentregador.setText("");
                            }
                        }catch (Exception e){
                            Log.v("LogEntregador:", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LogBuscaOcorre", error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("entregador", tentregador.getText().toString());
                return params;
            }

        };

        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest);
    }
}