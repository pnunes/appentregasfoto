package com.example.appentregasfoto;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.Transliterator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityMostraHawbEntregue extends AppCompatActivity {
    private ConexaoDB conexaoDb;
    public SQLiteDatabase banco;

    private String dados;
    private String data_entrega;
    TextView totalHawbEntregue;
    String total_entregue;
    private Button enviadadosmysql;

    private HawbDAO dao;
    private ProgressBar BarraProgresso;
    private ListView lisHawbEntregue;

    List<ModeloHawb> arrayListHawbEntregue;
    ListaHawbAdapter arrayAdapaterHawb;

    //Elementos para conexao com php e banco mysql
    JSONArray jasonArray = null;
    JSONObject jsonObject = null;

    StringRequest stringRequest;
    //Fim elementos conexao mysql

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_hawb_entregue);

        conexaoDb = new ConexaoDB(ActivityMostraHawbEntregue.this);
        banco = conexaoDb.getWritableDatabase();

        totalHawbEntregue = findViewById(R.id.tv_total_hawb_entregue);
        lisHawbEntregue = findViewById(R.id.lista_hawbs_entregues);
        enviadadosmysql = findViewById(R.id.btn_envia_dados_servidor);

        dao = new HawbDAO(ActivityMostraHawbEntregue.this);

        MostraListaEntregue();

        lisHawbEntregue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent ite = new Intent(ActivityMostraHawbEntregue.this, ActivityMostraDetalheHAWB.class);
                try {
                    ite.putExtra("nhawbb", arrayListHawbEntregue.get(0).Nhawb);
                } catch (Exception e){
                    Log.e("LogHawb:", e.getMessage());
                }
                startActivity(ite);
            }
        });

        enviadadosmysql.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EnviaDadosServidor();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void MostraListaEntregue(){
        arrayListHawbEntregue = dao.obterHawbEntregues();
        arrayAdapaterHawb = new ListaHawbAdapter(getApplicationContext(), arrayListHawbEntregue);
        lisHawbEntregue.setAdapter(arrayAdapaterHawb);
        try {
            total_entregue = String.valueOf(lisHawbEntregue.getAdapter().getCount());
            totalHawbEntregue.setText(total_entregue);
        }catch (Exception e){
            Log.e("LogHawbEntregue", e.getMessage());
        }
    }

    //Metodo para enviar dados para o servidor php
    public void EnviaDadosServidor() throws JSONException {

        JSONArray dados = new JSONArray();

        Cursor cursor;
        cursor = banco.query("movihawb", new String[]{"Nhawb", "dtentrega", "hrentrega","ocorrencia"},
                "dtentrega != ? and envibase = ?", new String[]{"1000-01-01","N"}, null, null, null);
        while (cursor.moveToNext()) {
            JSONObject registro = null;
            try {
                registro = new JSONObject();
                registro.put("n_hawb", cursor.getString(0));
                registro.put("recebedor", "FOTO HAWB");
                registro.put("documento", "FOTO HAWB");
                registro.put("dt_entrega", cursor.getString(1));
                registro.put("dt_baixa",cursor.getString(1));
                registro.put("hr_entrega",cursor.getString(2));
                registro.put("ocorrencia", cursor.getString(3));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dao.atualizaEnvio(cursor.getString(0));
            dados.put(registro);
        }

        // url para onde enviar os dados no meu micro
        //String urlEnviaHawb = "http://192.168.0.158/N_entregas/rotinas_app/EnviaDadosParaBase.php";

        // url para onde enviar os dados no servidor internet
        String urlEnviaHawb = "https://flypost.com.br/sis_entregas/rotinas_app/EnviaDadosParaBase.php";

        // criando uma varivel para o request queue
        RequestQueue queue = Volley.newRequestQueue(ActivityMostraHawbEntregue.this);
        StringRequest request = new StringRequest(Request.Method.POST, urlEnviaHawb, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Informanfo o sucesso da operação num Toast.
                Toast.makeText(ActivityMostraHawbEntregue.this, "" + response, Toast.LENGTH_LONG).show();
                MostraListaEntregue();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(ActivityMostraHawbEntregue.this, "Falha no processo = " + error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //abaixo estamos criando um mapa para armazenar nossos valores no par chave e valor.
                Map<String, String> params = new HashMap<String, String>();
                params.put("dados", String.valueOf(dados));
                return params;
            }
        };
        queue.add(request);
    }
}