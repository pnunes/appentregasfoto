package com.example.appentregasfoto;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ActivityMostraDetalheHAWB extends AppCompatActivity {
    private EditText n_hawb;
    private EditText destino;
    private EditText rua;
    private EditText numero;
    private EditText bairro;
    private EditText cidade;
    private EditText nomerecebe;
    private EditText docrecebe;
    private EditText ocorre;
    private EditText dataentrega;
    private EditText hr_entrega;
    private EditText enviaBase;
    private EditText imgBase;
    private Button GravaAlteracao;
    private Button BuscaHawb;
    private HawbDAO dao;
    private ConexaoDB conexaoDb;
    private SQLiteDatabase banco;
    private String nu_hawb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_detalhe_hawb);

        conexaoDb = new ConexaoDB(ActivityMostraDetalheHAWB.this);
        banco = conexaoDb.getWritableDatabase();

        destino = findViewById(R.id.editText_destino_hawb);
        rua     = findViewById(R.id.editText_rua_entrega);
        numero  = findViewById(R.id.editText_numero_entrega);
        bairro  = findViewById(R.id.editText_bairro_entrega);
        cidade  = findViewById(R.id.editText_cidade_entrega);
        nomerecebe = findViewById(R.id.editText_nome_recebedor);
        docrecebe = findViewById(R.id.editText_doc_recebedor);
        ocorre = findViewById(R.id.editText_codigo_ocorre);
        dataentrega = findViewById(R.id.editText_data_entrega);
        n_hawb =  findViewById(R.id.editText_numero_hawb);
        hr_entrega =  findViewById(R.id.editText_hora_entrega);
        enviaBase =  findViewById(R.id.editTextSituacaoHawb);
        imgBase = findViewById(R.id.editText_img_base);
        dao = new HawbDAO(ActivityMostraDetalheHAWB.this);

        GravaAlteracao = findViewById(R.id.btn_grava_alteracao_hawb);
        BuscaHawb = findViewById(R.id.bt_buscaHawb);

        // Pega o numero da hawb passado pela activityhawb
        Intent intent = getIntent();
        String nume_hawb = (String) intent.getSerializableExtra("nhawbb");
        //coloca o valor da hawb recebido e insere no edittext nhawb
        n_hawb.setText(nume_hawb);

        pegaDadosHawb(nume_hawb);

        BuscaHawb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                nu_hawb = n_hawb.getText().toString();
                pegaDadosHawb(nu_hawb);
            }
        });
    }

    public void pegaDadosHawb(String nume_hawb){

        Cursor cursor;
        try {
            cursor = banco.query("movihawb", new String[]{"Nhawb", "nomedestino", "rua", "numero", "bairro", "cidade", "recebedor", "documento", "dtentrega", "ocorrencia","hrentrega","envibase","imgBase" },
                    "Nhawb = ?", new String[]{nume_hawb}, null, null, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                destino.setText(cursor.getString(1));
                rua.setText(cursor.getString(2));
                numero.setText(cursor.getString(3));
                bairro.setText(cursor.getString(4));
                cidade.setText(cursor.getString(5));
                nomerecebe.setText(cursor.getString(6));
                docrecebe.setText(cursor.getString(7));
                if(cursor.getString(8).equals("1000-01-01")){
                    dataentrega.setText("A ENTREGAR");
                } else {
                    dataentrega.setText(convertDateToShow(cursor.getString(8)));
                }
                ocorre.setText(cursor.getString(9));
                hr_entrega.setText(cursor.getString(10));
                enviaBase.setText(cursor.getString(11));
                imgBase.setText(cursor.getString(12));
            } else {
                Toast.makeText(ActivityMostraDetalheHAWB.this, "HAWB não localizada! Verifique", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Log.e("LogPegahawb", e.getMessage());
        }
    }

    public void gravarAlteracaoHawb(View view) throws ParseException {
        ModeloHawb hwb = new ModeloHawb();
        hwb.setNhawb(n_hawb.getText().toString());
        hwb.setNomeDestino(destino.getText().toString());
        hwb.setRua(rua.getText().toString());
        hwb.setNumero(numero.getText().toString());
        hwb.setBairro(bairro.getText().toString());
        hwb.setCidade(cidade.getText().toString());
        hwb.setRecebedor(nomerecebe.getText().toString());
        hwb.setDocumento(docrecebe.getText().toString());
        hwb.setDtEntrega(dataentrega.getText().toString());
        hwb.setOcorrencia(ocorre.getText().toString());
        hwb.setHrEntrega(hr_entrega.getText().toString());
        hwb.setEnvibase(enviaBase.getText().toString());
        hwb.setImgBase(imgBase.getText().toString());
        long id = dao.AlteraHawb(hwb);
        Toast.makeText(ActivityMostraDetalheHAWB.this, "HAWB Alterada com sucesso :" + id, Toast.LENGTH_SHORT).show();

        //limpando os campos da activity
        hwb.setNhawb(null);
        hwb.setNomeDestino(null);
        hwb.setRua(null);
        hwb.setNumero(null);
        hwb.setBairro(null);
        hwb.setCidade(null);
        hwb.setRecebedor(null);
        hwb.setDocumento(null);
        hwb.setDtEntrega(null);
        hwb.setOcorrencia(null);
        hwb.setHrEntrega(null);
        hwb.setEnvibase(null);
        hwb.setImgBase(null);

        n_hawb.setText("");
        destino.setText("");
        rua.setText("");
        numero.setText("");
        bairro.setText("");
        cidade.setText("");
        nomerecebe.setText("");
        docrecebe.setText("");
        dataentrega.setText("");
        ocorre.setText("");
        hr_entrega.setText("");
        enviaBase.setText("");
        imgBase.setText("");
        finish();
    }

    //Metodo para formatar datas
    public String convertDateToShow(String strDate){
        // formato de entrada deve ser 2017-01-17
        SimpleDateFormat dateFormatIn = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        // sem argumentos, pega o formato do sistema para exibir a data
        SimpleDateFormat dateFormatOut = new SimpleDateFormat();
        // com argumentos formato e locale a saida e sempre a mesma
        //  SimpleDateFormat dateFormatOut = new SimpleDateFormat("dd-MM-yy", Locale.US);
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormatIn.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return stringToSpace(String.valueOf(dateFormatOut.format(convertedDate)));
    }
    // stringToSpace() retorna a string a esquerda do primeiro espaço da string passada.
    // necessario porque a conversao retorna dd/MM/yy 12:00 hs
    private String stringToSpace(String string){

        int spaceIndex = string.indexOf(" ");
        if(spaceIndex > 0) {
            string = string.substring(0, spaceIndex);
        }
        return string;
    }
}