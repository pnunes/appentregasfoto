package com.example.appentregasfoto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class ActivityOcorrencia extends AppCompatActivity {
    private ConexaoDB conexaoDb;
    private SQLiteDatabase banco;

    JSONArray jasonArray = null;
    JSONObject jsonObject = null;

    private String codigo;
    private String descricao;

    public void OcorrenciaDAO(Context context){
        conexaoDb = new ConexaoDB(context);
        banco = conexaoDb.getWritableDatabase();
    }

    //endereco no meu micro
    //String urlOcorre = "http://192.168.0.158/N_entregas/rotinas_app/PegaOcorrencias.php";

    // url para onde enviar os dados no servidor internet
    String urlOcorre = "https://flypost.com.br/sis_entregas/rotinas_app/PegaOcorrencias.php";

    StringRequest stringRequest;
    RequestQueue requestQueue;

    private Button btnImportaOcorrencia;
    private OcorrenciaDAO dao;
    private ListView listaocorre;
    private List<ModeloOcorrencia> ocorrencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocorrencia);

        btnImportaOcorrencia = findViewById(R.id.btn_importa_ocorre);
        dao = new OcorrenciaDAO(this);

        requestQueue = Volley.newRequestQueue(this);

        listaocorre = findViewById(R.id.Lista_ocorrencia);
        ocorrencias = dao.obterTodas();
        ArrayAdapter<ModeloOcorrencia> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ocorrencias);
        listaocorre.setAdapter(adaptador);

        btnImportaOcorrencia.setOnClickListener(view -> {
            dao.limpaTabelaOcorre("ocorrencia");
            BuscaOcorrencias();
        });

    }

    public void BuscaOcorrencias(){
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST,urlOcorre,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.v("LogBuscaOcorre:", response);

                        try {
                            // aqui eu pego o resultado que veio da conexa~na forma de um array
                            jasonArray = new JSONArray(response);

                            if(jasonArray.equals(null)){
                                Toast.makeText(getApplicationContext(), "Tabela Esta vazia! Verifique.", Toast.LENGTH_LONG).show();
                            }else {
                                //Toast.makeText(getApplicationContext(), "Tudo Ok." + jsonObject.length(), Toast.LENGTH_LONG).show();
                                ModeloOcorrencia oc = new ModeloOcorrencia();
                                for(int i = 0; i < jasonArray.length(); i++) {
                                    jsonObject = jasonArray.getJSONObject(i);
                                    oc.setCodigo(jsonObject.getString("codigo"));
                                    oc.setDescricao(jsonObject.getString("descricao"));
                                    long id = dao.inserir(oc);
                                }
                                listaocorre = findViewById(R.id.Lista_ocorrencia);
                                ocorrencias = dao.obterTodas();
                                ArrayAdapter<ModeloOcorrencia> adaptador = new ArrayAdapter<ModeloOcorrencia>(ActivityOcorrencia.this, android.R.layout.simple_list_item_1, ocorrencias);
                                listaocorre.setAdapter(adaptador);
                            }
                        }catch (Exception e){
                            Log.v("LogBuscaOcorre:", e.getMessage());
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
                //params.put("usuario", tusuario.toString());
               // params.put("senha", tsenha.getText().toString());
                return params;
            }

        };

        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest);
    }

}