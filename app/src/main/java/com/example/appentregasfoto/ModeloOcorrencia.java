package com.example.appentregasfoto;

import androidx.annotation.NonNull;

public class ModeloOcorrencia {
    Integer id;
    String codigo;
    String descricao;

    public ModeloOcorrencia() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @NonNull
    public String toString(){
        return codigo + " - " + descricao;
    }
}
