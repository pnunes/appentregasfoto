package com.example.appentregasfoto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityMostraArquivoImgHawb extends AppCompatActivity {

    private HawbDAO dao;
    private static String peganume;
    List<ModeloHawb> numeroHawb;
    Button btn_lista_hawbs;
    //Button btn_envia_img_base;
    TextView tituloPg;
    public static String nomenovo;
    public static String filePath;
    public static Bitmap imgorigem;
    private Bitmap bitmap;
    String sonumehawb;
    String arquivoHawb;
    long numehawb;
    private String imagem_hawb;
    public static String currentPhotoPath;
    public static ListView lv_rela_hawbs;
    public static final int PERMISSAO_REQUEST = 61;
    public static final String cami_abso = "/storage/emulated/0/android/data/com.example.appentregasfoto/files/imghawb/";
    public static final String PATH = "/android/data/com.example.appentregasfoto/files/imghawb";
    private static final String ROOT_URL = "https://www.flypost.com.br/sis_entregas/rotinas_app/file-upload.php";
    private List<String> strings;
    List<ModeloHawb> arraynumeHawb;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);

        dao = new HawbDAO(ActivityMostraArquivoImgHawb.this);
        arraynumeHawb = dao.BuscarNumeroHawb();

        setContentView(R.layout.activity_mostra_arquivo_img_hawb);

        btn_lista_hawbs = findViewById(R.id.bt_lista_arquivos);
        //btn_envia_img_base = findViewById(R.id.btn_envia_img_base);
        lv_rela_hawbs = findViewById(R.id.lv_rela_arquivos);
        tituloPg = findViewById(R.id.tv_arqui_imagem);
        tituloPg.setBackgroundColor(Color.parseColor("#0000CD"));

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSAO_REQUEST);
            }
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSAO_REQUEST);
            }
        }

        // Pega as HAWBs cujas imagens ainda não foram enviadas e mostra num listview
        arraynumeHawb = dao.BuscarNumeroHawb();
        ArrayAdapter<ModeloHawb> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arraynumeHawb);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lv_rela_hawbs.setAdapter(adaptador);

        // Controla o click sobre a imagem a ser enviada e manda o numero da HAWB para o
        // método enviaImgBase
        lv_rela_hawbs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick ( AdapterView<?> parent, View view, int position, long id ) {
                sonumehawb = String.valueOf(arraynumeHawb.get(position).Nhawb);
                enviaImgBase(sonumehawb);
            }
        });
    }

    //Metodo que usa a bilbioteca voley para enviar imagens para o servidor (Usa a classe Volley MultipartRequest)
    private void uploadBitmap(final Bitmap bitmap) {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ROOT_URL,
        new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse( NetworkResponse response) {
                try {
                    JSONObject obj = new JSONObject(new String(response.data));
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    // Atualiza a tabela movimento colocando S no campo imgBase
                    dao.atualizaEnvioImg(peganume);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse( VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("GotError",""+error.getMessage());
            }
        }) {

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = Long.parseLong(peganume);
                params.put("image", new DataPart(imagename + ".jpg", getFileDataFromDrawable(bitmap)));

                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    //Este metodo recebe o numero da HAWB, acrescenta a extensão jpg, localiza o arquivo en envia
    // para o metodo UptloadBitmap
    public void enviaImgBase( String sonumehawb ){
        final String nomeArquivoComExtensao = sonumehawb +".jpg";
        peganume = sonumehawb;
        Uri uriDestino = Uri
                .fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + PATH, nomeArquivoComExtensao));
        final String sourceFileUri = uriDestino.getPath();
        Bitmap  bitmap = BitmapFactory.decodeFile(sourceFileUri);
        uploadBitmap(bitmap);
    }
}
