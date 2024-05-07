package com.example.appentregasfoto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class EntregadorDAO {

    private ConexaoDB conexaoDb;
    private SQLiteDatabase banco;

    public EntregadorDAO(Context context){
        conexaoDb = new ConexaoDB(context);
        banco = conexaoDb.getWritableDatabase();
    }

    public long inserir(ModeloEntregador modeloentregador){
        try {
            ContentValues values = new ContentValues();
            values.put("cnpjcpf", modeloentregador.getCnpjcpf());
            values.put("nome", modeloentregador.getNome());
            values.put("usuario", modeloentregador.getUsuario());
            values.put("senha", modeloentregador.getSenha());
            return banco.insert("entregador", null, values);
        }catch (Exception e){
            Log.v("ErroEntregador",e.getMessage());
        }
        return 1;
    }

    public List<ModeloEntregador> obterTotal(){
        List<ModeloEntregador> entregas = new ArrayList<>();
        Cursor cursor = banco.query("entregador", new String[]{"id", "cnpjcpf", "nome"},
                null, null, null, null, null );
        while (cursor.moveToNext()){
            ModeloEntregador ent = new ModeloEntregador();
            ent.setId(cursor.getInt(0));
            ent.setCnpjcpf(cursor.getString(1));
            ent.setNome(cursor.getString(2));
            entregas.add(ent);
        }
        return entregas;
    }

    public boolean limpaTabelaEntregador(String tabela){
        SQLiteDatabase db = null;
        try {
            db = this.conexaoDb.getWritableDatabase();
            db.delete(tabela, "id is not null", null);
        } catch (Exception e) {
            Log.d("EntregadorDAO", "Não foi Possível limpar a tabela.");
            return false;
        }
        return true;
    }

    public boolean validarLogin(String usuario, String senha){
        Cursor c = banco.rawQuery("SELECT usuario,senha,nome FROM entregador WHERE usuario=? and senha=?", new String[] {usuario,senha});
        if(c.getCount()>0) {
            return true;
        }else {
            return false;
        }
    }
}
