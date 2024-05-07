package com.example.appentregasfoto;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class ModeloEntregador implements Serializable {

    Integer id;
    String cnpjcpf;
    String nome;
    String usuario;
    String senha;

    public ModeloEntregador(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCnpjcpf() {
        return cnpjcpf;
    }

    public void setCnpjcpf(String cnpjcpf) {
        this.cnpjcpf = cnpjcpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @NonNull
    public String toString(){
        return cnpjcpf + " - " + nome;
    }
}
