package com.example.appentregasfoto;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;


public class ActivityBaixaHawbFoto extends AppCompatActivity {

    ImageButton bt_tirar_foto;
    Button btn_Salvar;
    String nume_hawb;
    public String codocorre;
    ImageView iv_mostra_foto;
    private TextView nhawb;
    private static final int CAPTURA_IMAGEM = 1;
    private final int PERMISSAO_REQUEST = 2;
    private static final String EXTENSAO_IMAGEM = ".jpg";
    String currentPhotoPath;
    private Spinner splistaocorre;
    private HawbDAO daohw;
    private OcorrenciaDAO dao;
    private List<ModeloOcorrencia> lstaoco;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baixa_hawb_foto);

        nhawb =  findViewById(R.id.tv_hawb_numero);
        bt_tirar_foto = findViewById(R.id.ImgBtn_tira_foto);
        btn_Salvar = findViewById(R.id.btn_salvar);
        iv_mostra_foto = findViewById(R.id.iv_mostra_foto);
        splistaocorre = findViewById(R.id.spi_ocorrencias);

        // Pega o numero da hawb passado pela activityhawb
        Intent intent = getIntent();
        String nu_hawb = (String) intent.getSerializableExtra("nhawbv");
        // colocando o numero na hawb numa variavel string para compor o nomo imagem da hawb
        nume_hawb = (String) intent.getSerializableExtra("nhawbv");
        //coloca o valor da hawb recebido e insere no edittext nhawb
        nhawb.setText(nu_hawb);
        daohw = new HawbDAO(this);

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
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

        bt_tirar_foto.setOnClickListener(View ->{
            tirarFotoH();
        });

    }
   /* private void tirarFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = criaArquivoImagem();
            } catch (IOException ex) {
                // se ocorrer erro durante a criação do arquivo
            }
            // Continua somente se a criação do arquivo foi bem sucedida
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,"meu_provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }

    }*/

    private void tirarFotoH() {
        Intent intentCapturaImagem = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File diretorioDestino = new File(getExternalFilesDir(null), "imghawb");
        if (!diretorioDestino.exists()) {
            // se não existir o diretorio e criado
            if (!diretorioDestino.mkdirs()) { // <- mkdirs()
                Toast.makeText(this, diretorioDestino + " não pode ser criada.", Toast.LENGTH_SHORT).show();
            }


        }
        startActivityForResult(intentCapturaImagem, CAPTURA_IMAGEM);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURA_IMAGEM && resultCode == RESULT_OK) {
           // if (resultCode == ItemPodActivity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                try {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] bytes = stream.toByteArray();
                    String nomeFinalDoArquivo = nume_hawb + EXTENSAO_IMAGEM;
                    FileOutputStream fos = new FileOutputStream(
                            getExternalFilesDir(null) + "/imghawb/" + nomeFinalDoArquivo);

                    //Pegando o caminho e o nome da imagem para recuperar e mostrar na tela
                   // String currentPhotoPath = Environment.getExternalStorageDirectory() + "/imghawb/" + nomeFinalDoArquivo;
                    //Salvando a imagem na pasta
                    iv_mostra_foto.setImageBitmap(photo);
                    fos.write(bytes);
                    fos.close();
                } catch (Exception e) {
                    Log.e("ErSalvaImg",e.getMessage());
                }
                //exibirImagem();
           // }
        }
    }
  /*  @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA && resultCode == RESULT_OK) {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.fromFile(arquivoFoto)));
        }
        exibirImagem();
    }*/
  /*  private File criaArquivoImagem() throws IOException {
        // cria o arquivo com a foto
        String imageFileName = nume_hawb;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName+".", ".jpg", storageDir);
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }*/
    private void exibirImagem() {
        // Get the dimensions of the View
        int targetW = iv_mostra_foto.getWidth();
        int targetH = iv_mostra_foto.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        iv_mostra_foto.setImageBitmap(bitmap);

    }

    public void Gravar(View view){
        ModeloHawb hw = new ModeloHawb();

        hw.setNhawb(nhawb.getText().toString());
        hw.setOcorrencia(codocorre.toString());
        long id = daohw.Gravar(hw);

        Intent it = new Intent(ActivityBaixaHawbFoto.this, ActivityHAWB.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(it);

        //Limpando o numero da hawb da tela
        hw.setNhawb(null);
        nhawb.setText("");

        finish();
    }
}