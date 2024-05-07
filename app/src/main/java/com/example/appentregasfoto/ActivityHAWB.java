package com.example.appentregasfoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class ActivityHAWB extends AppCompatActivity {

    private AlertDialog alerta;

    TextView qtdade_hawb;
    String total_hawb;

    private ConexaoDB conexaoDb;
    private SQLiteDatabase banco;
    public String cnpjcpf;
    JSONArray jasonArray = null;
    JSONObject jsonObject = null;
    List<ModeloHawb> arrayListHawb;
    ListaHawbAdapter arrayAdapaterHawb;

    public void HawbDAO(Context context){
        conexaoDb = new ConexaoDB(ActivityHAWB.this);
        banco = conexaoDb.getWritableDatabase();
    }
    // Pega hawbs no meu micro
    //String urlhawbs = "http://192.168.0.158/N_entregas/rotinas_app/PegaHawbs.php";

    // Pega hawbs no servidor internet
    String urlhawbs = "https://flypost.com.br/sis_entregas/rotinas_app/PegaHawbs.php";

    StringRequest stringRequest;
    RequestQueue requestQueue;

    private Button btnImportahawb;
    private HawbDAO dao;
    private ListView listahawb;

    // ListaHawbAdapter arrayAdapaterHawb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hawb);

        btnImportahawb = findViewById(R.id.btn_importa_hawb);
        qtdade_hawb = findViewById(R.id.tv_quantidade_de_hawbs);
        listahawb = findViewById(R.id.List_Hawb);

        dao = new HawbDAO(this);
        requestQueue = Volley.newRequestQueue(this);

        MostraLista();

        listahawb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(ActivityHAWB.this, ActivityBaixaHawbFoto.class);
                // Toast.makeText(getApplicationContext(), "Item :" + position + " - " + hawbes.get(position).Nhawb, Toast.LENGTH_LONG).show();
                it.putExtra("nhawbv", arrayListHawb.get(position).Nhawb);
                startActivity(it);
                finish();
            }
        });

        btnImportahawb.setOnClickListener(view -> {
            cnpjcpf = dao.pegaCnpjCpf();
            BuscaHawbs();
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pg_hawb, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.itemEntregador:
                startActivity(new Intent(this, ActivityEntregador.class));
                break;
            case R.id.itemOcorrencia:
                startActivity(new Intent(this, ActivityOcorrencia.class));
                break;
            case R.id.itemConsultaHawb:
                startActivity(new Intent(this, ActivityMostraHawbEntregue.class));
                break;
            case R.id.item_todas_hawb:
                startActivity(new Intent(this, ActivityMostraTodasHawb.class));
                break;
            case R.id.itemLimpataHawb:
                //Cria o gerador do AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                //define o titulo
                builder.setTitle("Controle HAWBs");
                //define a mensagem
                builder.setMessage("Esta operação vai excluir todas as HAWbs do sistema! Deseja excluir ?");
                //define um botão como positivo
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        // Toast.makeText(ActivityHawb.this, "sim=" + arg1, Toast.LENGTH_SHORT).show();
                        dao.LimpaHawb("movihawb");

                        MostraLista();
                    }
                }).setNegativeButton("Nâo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(ActivityHAWB.this, "Ok. Operacão cancelada!", Toast.LENGTH_SHORT).show();
                    }
                });
                alerta = builder.create();
                alerta.show();
                break;

            case R.id.item_imagem_hawb:
                startActivity(new Intent(this, ActivityMostraArquivoImgHawb.class));
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    public void BuscaHawbs(){

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST,urlhawbs,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.v("LogHawb:", response);

                        try {
                            // aqui eu pego o resultado que veio da conexa~na forma de um array
                            jasonArray = new JSONArray(response);

                            if(jasonArray.equals(null)){
                                Toast.makeText(getApplicationContext(), "Não ha movimento para importação! Verifique.", Toast.LENGTH_LONG).show();
                            }else {
                                ModeloHawb oc = new ModeloHawb();
                                for(int i = 0; i < jasonArray.length(); i++) {
                                    jsonObject = jasonArray.getJSONObject(i);
                                    oc.setNhawb(jsonObject.getString("nhawb"));
                                    oc.setNomeDestino(jsonObject.getString("nome"));
                                    oc.setRua(jsonObject.getString("rua"));
                                    oc.setNumero(jsonObject.getString("numero"));
                                    oc.setBairro(jsonObject.getString("bairro"));
                                    oc.setCidade(jsonObject.getString("cidade"));
                                    oc.setDtEntrega(jsonObject.getString("dtentrega"));
                                    long id = dao.inserir(oc);
                                }
                                MostraLista();
                            }
                        }catch (Exception e){
                            Log.v("LogHawb:", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LogHawb", error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("cnpjcpf", cnpjcpf.toString());
                return params;
            }

        };

        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest);
    }

    public void MostraLista(){
        arrayListHawb = dao.obterTodasHawb();
        if(arrayListHawb.isEmpty()){
            Toast.makeText(ActivityHAWB.this, "Sem HAWB´s para Entrega!", Toast.LENGTH_LONG).show();
        }else {
            arrayAdapaterHawb = new ListaHawbAdapter(getApplicationContext(), arrayListHawb);
            listahawb.setAdapter(arrayAdapaterHawb);
            // Mostra a quanyidade de hawbs para entrega
            try {
                total_hawb = String.valueOf(listahawb.getAdapter().getCount());
                qtdade_hawb.setText(total_hawb);
            }catch (Exception e){
                Log.e("LogLista", e.getMessage());
            }
        }
    }
}