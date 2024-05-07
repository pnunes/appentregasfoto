package com.example.appentregasfoto;

import androidx.annotation.NonNull;

public class ModeloHawb {
    Integer id;
    String Nhawb;
    String nomeDestino;
    String rua;
    String numero;
    String bairro;
    String cidade;
    String recebedor;
    String documento;
    String ocorrencia;
    String DtEntrega;
    String hrEntrega;
    String envibase;
    String imgBase;

    public ModeloHawb(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNhawb() {
        return Nhawb;
    }

    public String setNhawb(String nhawb) {this.Nhawb = nhawb;
        return nhawb;
    }

    public String getNomeDestino() {
        return nomeDestino;
    }

    public void setNomeDestino(String nomeDestino) {
        this.nomeDestino = nomeDestino;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getRecebedor() {
        return recebedor;
    }

    public String getHrEntrega() {
        return hrEntrega;
    }

    public void setHrEntrega(String hrEntrega) {
        this.hrEntrega = hrEntrega;
    }

    public String getEnvibase() {
        return envibase;
    }

    public void setEnvibase(String envibase) {
        this.envibase = envibase;
    }

    public String getImgBase() {
        return imgBase;
    }

    public void setImgBase(String envibase) {
        this.imgBase = imgBase;
    }
    public String setRecebedor(String recebedor) {
        this.recebedor = recebedor;
        return recebedor;
    }

    public String getDocumento() {
        return documento;
    }

    public String setDocumento(String documento) {
        this.documento = documento;
        return documento;
    }

    public String getDtEntrega() {
        return DtEntrega;
    }

    public String setDtEntrega(String dtEntrega) {
        DtEntrega = dtEntrega;
        return dtEntrega;
    }

    public String getOcorrencia() {
        return ocorrencia;
    }

    public String setOcorrencia(String ocorrencia) {
        this.ocorrencia = ocorrencia;
        return ocorrencia;
    }

    @NonNull
    public String toString(){
        return "HAWB :"+Nhawb;
    }
}
