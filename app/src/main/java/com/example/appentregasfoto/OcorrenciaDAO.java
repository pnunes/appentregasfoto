package com.example.appentregasfoto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class OcorrenciaDAO {

    private ConexaoDB conexaoDb;
    private SQLiteDatabase banco;

    public OcorrenciaDAO(Context context){
        conexaoDb = new ConexaoDB(context);
        banco = conexaoDb.getWritableDatabase();
    }

    public long inserir(ModeloOcorrencia modeloocorrencia){
        try {
            ContentValues values = new ContentValues();
            values.put("codigo", modeloocorrencia.getCodigo());
            values.put("descricao", modeloocorrencia.getDescricao());
            return banco.insert("ocorrencia", null, values);
        }catch (Exception e){
            Log.v("ErroOcorre",e.getMessage());
        }
        return 1;
    }

    public List<ModeloOcorrencia> obterTodas(){
        List<ModeloOcorrencia> ocorrencias = new ArrayList<>();
        Cursor cursor = banco.query("ocorrencia", new String[]{"id", "codigo", "descricao"},
                null, null, null, null, "descricao" );
        while (cursor.moveToNext()){
            ModeloOcorrencia ocor = new ModeloOcorrencia();
            ocor.setId(cursor.getInt(0));
            ocor.setCodigo(cursor.getString(1));
            ocor.setDescricao(cursor.getString(2));
            ocorrencias.add(ocor);
        }
        return ocorrencias;
    }

    public boolean limpaTabelaOcorre(String tabela){
        SQLiteDatabase db = null;

        try {
            db = this.conexaoDb.getWritableDatabase();
            db.delete(tabela, "id is not null", null);
        } catch (Exception e) {
            Log.d("OcorrenciaDAO", "Não foi Possível limpar a tabela.");
            return false;
        }
        return true;
    }
}
