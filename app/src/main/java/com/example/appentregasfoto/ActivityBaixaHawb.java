package com.example.appentregasfoto;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ActivityBaixaHawb extends AppCompatActivity {

    private EditText nhawb;
    private EditText nomerecebe;
    private EditText docrecebe;
    public String codocorre;
    private HawbDAO daohw;

    private OcorrenciaDAO dao;
    private Spinner splistaocorre;
    private List<ModeloOcorrencia> lstaoco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baixa_hawb);

        nhawb =  findViewById(R.id.editText_Numero_hawb);
        nomerecebe =  findViewById(R.id.editText_Nome_recebedor);
        docrecebe =  findViewById(R.id.editText_documento_recebedor);
        splistaocorre = findViewById(R.id.spn_ocorrencias);
        // Pega o numero da hawb passado pela activityhawb
        Intent intent = getIntent();
        String nu_hawb = (String) intent.getSerializableExtra("nhawbv");
        //coloca o valor da hawb recebido e insere no edittext nhawb
        nhawb.setText(nu_hawb);

        //mostra a relação de ocorrencia num spiner
        //inicio spinner
        dao = new OcorrenciaDAO(this);

        lstaoco = dao.obterTodas();
        ArrayAdapter<ModeloOcorrencia> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lstaoco);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        splistaocorre.setAdapter(adaptador);
        //final spinner

        try {
            //pegar a linha do spinner que foi selecionada
            splistaocorre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   codocorre = String.valueOf(lstaoco.get(position).codigo);
                  // Toast.makeText(ActivityBaixaHawb.this,"Ocorrencia = " + codocorre, Toast.LENGTH_LONG ).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }catch (Exception e){
            Log.e("ErroSpn",e.getMessage());
        }
        //final spinner

        daohw = new HawbDAO(this);

    }
    public void Gravar(View view){
        ModeloHawb hw = new ModeloHawb();
        hw.setRecebedor(nomerecebe.getText().toString());
        hw.setDocumento(docrecebe.getText().toString());
        hw.setNhawb(nhawb.getText().toString());
        hw.setOcorrencia(codocorre.toString());
        long id = daohw.Gravar(hw);

        Intent it = new Intent(ActivityBaixaHawb.this, ActivityHAWB.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(it);

        //Limando a tela de registro dos dados de baixa
        hw.setNhawb(null);
        hw.setRecebedor(null);
        hw.setDocumento(null);
        nhawb.setText("");
        nomerecebe.setText("");
        docrecebe.setText("");

        finish();
    }
}