package com.example.appentregasfoto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class HawbDAO {
    ModeloHawb modeloHawb = new ModeloHawb();

    private ConexaoDB conexaoDb;
    private SQLiteDatabase banco;
    private String cnpjcpf;

    public HawbDAO(Context context){
        conexaoDb = new ConexaoDB(context);
        banco = conexaoDb.getWritableDatabase();
    }
    public long inserir(ModeloHawb modeloHawb){
        try {
            ContentValues values = new ContentValues();
            values.put("nhawb", modeloHawb.getNhawb());
            values.put("nomedestino", modeloHawb.getNomeDestino());
            values.put("rua", modeloHawb.getRua());
            values.put("numero", modeloHawb.getNumero());
            values.put("bairro", modeloHawb.getBairro());
            values.put("cidade", modeloHawb.getCidade());
            values.put("dtentrega", modeloHawb.getDtEntrega());
            return banco.insert("movihawb", null, values);
        }catch (Exception e){
            Log.v("Errohawb",e.getMessage());
        }
        return 1;
    }
    public List<ModeloHawb> obterTodasHawb(){
        List<ModeloHawb> lihawb = new ArrayList<>();
        Cursor cursor;
        cursor = banco.query("movihawb", new String[]{"id", "Nhawb", "nomedestino", "rua", "numero", "bairro", "cidade","dtentrega" },
                "dtentrega = ?", new String[]{"1000-01-01"}, null, null, null );
        while (cursor.moveToNext()){
            ModeloHawb ent = new ModeloHawb();
            ent.setId(cursor.getInt(0));
            ent.setNhawb(cursor.getString(1));
            ent.setNomeDestino(cursor.getString(2));
            ent.setRua(cursor.getString(3));
            ent.setNumero(cursor.getString(4));
            ent.setBairro(cursor.getString(5));
            ent.setCidade(cursor.getString(6));
            ent.setDtEntrega(convertDateToShow(cursor.getString(7)));
            lihawb.add(ent);
        }
        return lihawb;
    }

    public List<ModeloHawb> obterHawbEntregues(){
        List<ModeloHawb> lihawb = new ArrayList<>();
        Cursor cursor;
        cursor = banco.query("movihawb", new String[]{"id", "Nhawb", "nomedestino", "rua", "numero", "bairro", "cidade","dtentrega" },
                "dtentrega != ? and envibase = ?", new String[]{"1000-01-01","N"}, null, null, null );
        while (cursor.moveToNext()){
            ModeloHawb ent = new ModeloHawb();
            ent.setId(cursor.getInt(0));
            ent.setNhawb(cursor.getString(1));
            ent.setNomeDestino(cursor.getString(2));
            ent.setRua(cursor.getString(3));
            ent.setNumero(cursor.getString(4));
            ent.setBairro(cursor.getString(5));
            ent.setCidade(cursor.getString(6));
            ent.setDtEntrega(convertDateToShow(cursor.getString(7)));
            lihawb.add(ent);
        }
        return lihawb;
    }
    public List<ModeloHawb> obterTotalHawb(){
        List<ModeloHawb> lihawb = new ArrayList<>();
        Cursor cursor;
        cursor = banco.query("movihawb", new String[]{"id", "Nhawb", "nomedestino", "rua", "numero", "bairro", "cidade","dtentrega" },
                "dtentrega != ?", new String[]{"null"}, null, null, null );
        while (cursor.moveToNext()){
            ModeloHawb ent = new ModeloHawb();
            ent.setId(cursor.getInt(0));
            ent.setNhawb(cursor.getString(1));
            ent.setNomeDestino(cursor.getString(2));
            ent.setRua(cursor.getString(3));
            ent.setNumero(cursor.getString(4));
            ent.setBairro(cursor.getString(5));
            ent.setCidade(cursor.getString(6));
            if(cursor.getString(7).equals("1000-01-01")){
                ent.setDtEntrega("A ENTREGAR");
            }else {
                ent.setDtEntrega(convertDateToShow(cursor.getString(7)));
            }
            lihawb.add(ent);
        }
        return lihawb;
    }
    public String pegaCnpjCpf(){
        Cursor c = banco.rawQuery("SELECT cnpjcpf FROM entregador", null);
        if(c.getCount()>0) {
            c.moveToFirst();
            cnpjcpf = c.getString(0);
            return cnpjcpf;
        }else {
            return null;
        }
    }

    public long Gravar(ModeloHawb modeloHawb){
        String[] nume_hawb = {String.valueOf(modeloHawb.getNhawb())};
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
        ContentValues values = new ContentValues();
        SimpleDateFormat formataData = new SimpleDateFormat("yyyy-MM-dd", new Locale("pt", "BR"));
        Date databaixa = new Date();
        String dtBaixaFormatada = formataData.format(databaixa);
        values.put("dtentrega",dtBaixaFormatada);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", new Locale("pt", "BR"));
        Date hora = Calendar.getInstance().getTime();
        String hrBaixaFormatada = sdf.format(hora);
        values.put("hrentrega",hrBaixaFormatada);
        return banco.update("movihawb", values,"Nhawb =? ",nume_hawb);
    }

    public long AlteraHawb(ModeloHawb modeloHawb) throws ParseException {
        String dataentra;
        Date dataOri;
        String[] nume_hawb = {String.valueOf(modeloHawb.getNhawb())};
        ContentValues values = new ContentValues();
        values.put("nomeDestino",modeloHawb.getNomeDestino());
        values.put("rua",modeloHawb.getRua());
        values.put("numero",modeloHawb.getNumero());
        values.put("bairro",modeloHawb.getBairro());
        values.put("cidade",modeloHawb.getCidade());
        values.put("recebedor",modeloHawb.getRecebedor());
        values.put("documento",modeloHawb.getDocumento());
        if(modeloHawb.getDtEntrega().equals("")){
            values.put("dtentrega", "1000-01-01");
        } else {
            values.put("dtentrega",convertDateToShow(modeloHawb.getDtEntrega()));
        }
        values.put("ocorrencia",modeloHawb.getOcorrencia());
        values.put("hrentrega",modeloHawb.getHrEntrega());
        values.put("envibase",modeloHawb.getEnvibase());

        return banco.update("movihawb", values,"Nhawb =? ",nume_hawb);
    }

    public long atualizaEnvio(String hawb){
        String[] nume_hawb = {String.valueOf(hawb)};
        String estatus = String.valueOf('S');
        ContentValues values = new ContentValues();
        values.put("envibase",estatus);
        return banco.update("movihawb", values,"Nhawb =? ",nume_hawb);
    }

    public long atualizaEnvioImg(String hawb){
        String[] nume_hawb = {String.valueOf(hawb)};
        String enviaimg = String.valueOf('S');
        ContentValues values = new ContentValues();
        values.put("imgBase",enviaimg);
        return banco.update("movihawb", values,"Nhawb =? ",nume_hawb);
    }
    public boolean LimpaHawb(String tabela){
        SQLiteDatabase db = null;
        try {
            db = this.conexaoDb.getWritableDatabase();
            db.delete(tabela, "dtentrega != ? and envibase = ? ", new String[]{"1000-01-01","S"});
        } catch (Exception e) {
            Log.d("HawbDAO", "Não foi Possível limpar a tabela.");
            return false;
        }
        return true;
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

    public List<ModeloHawb> BuscarNumeroHawb(){
        List<ModeloHawb> lisnuhawb = new ArrayList<>();
        Cursor cursor;
        cursor = banco.query("movihawb", new String[]{"Nhawb"},
                "dtentrega != ? and imgBase = ?", new String[]{"1000-01-01","N"}, null, null, null );
        while (cursor.moveToNext()){
            ModeloHawb peg = new ModeloHawb();
            peg.setNhawb(cursor.getString(0));
            lisnuhawb.add(peg);
        }
        return lisnuhawb;
    }
}
