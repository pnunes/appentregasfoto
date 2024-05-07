package com.example.appentregasfoto;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class ConexaoDB extends SQLiteOpenHelper {
    private static final String nomeDB = "EntregasDAO";
    private static final int versaoDB = 2;

    public ConexaoDB(@Nullable Context context) {
        super(context, nomeDB, null, versaoDB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            //Cria a tabela Ocorrencia no sqlite
            db.execSQL("CREATE TABLE ocorrencia (id integer primary key autoincrement not null, " +
                    "codigo TEXT(2), descricao TEXT(80))");

            //Cria a tabela Entregador no sqlite
            db.execSQL("CREATE TABLE entregador (id integer primary key autoincrement not null, cnpjcpf TEXT(20), nome TEXT(80), usuario TEXT(25),senha TEXT(25))");

            //Cria a tabela movihawb no sqlite
            db.execSQL("CREATE TABLE movihawb (id integer primary key autoincrement not null, Nhawb TEXT(25), nomedestino TEXT(50), rua TEXT(50),numero TEXT(10),bairro TEXT(50),cidade TEXT(40),recebedor TEXT(40),documento TEXT(30),dtentrega DATE,imagem TEXT(100),hrentrega TEXT(10),ocorrencia TEXT(3),envibase TEXT(1) default 'N', imgBase TEXT(1) default 'N')");
        }catch (SQLException e){
            Log.e("DB_LOG", "onCreate: "+ e.getLocalizedMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
