package com.example.appentregasfoto;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ActivityMostraTodasHawb extends AppCompatActivity {

    private ConexaoDB conexaoDb;
    public SQLiteDatabase banco;
    TextView totalHawb;
    String soma_total_entregue;
    private String Nhawb;
    private HawbDAO dao;
    private ListView lisTodasHawb;

    List<ModeloHawb> arrayListHawb;
    ListaTodasHawbAdapter arrayAdapaterHawb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_todas_hawb);

        conexaoDb = new ConexaoDB(ActivityMostraTodasHawb.this);
        banco = conexaoDb.getWritableDatabase();

        totalHawb = findViewById(R.id.tv_total_hawb);
        lisTodasHawb = findViewById(R.id.lista_todas_hawbs);

        dao = new HawbDAO(ActivityMostraTodasHawb.this);

        MostraTotalHawb();

        lisTodasHawb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent ite = new Intent(ActivityMostraTodasHawb.this, ActivityMostraDetalheHAWB.class);
                try {
                    ite.putExtra("nhawbb", arrayListHawb.get(position).Nhawb);
                } catch (Exception e){
                    Log.e("LogHawb:", e.getMessage());
                }
                startActivity(ite);
            }
        });
    }

    public void MostraTotalHawb(){
        arrayListHawb = dao.obterTotalHawb();
        arrayAdapaterHawb = new ListaTodasHawbAdapter(getApplicationContext(), arrayListHawb);
        lisTodasHawb.setAdapter(arrayAdapaterHawb);
        try {
            soma_total_entregue = String.valueOf(lisTodasHawb.getAdapter().getCount());
            totalHawb.setText(soma_total_entregue);
        }catch (Exception e){
            Log.e("LogHawbEntregue", e.getMessage());
        }
    }
}