package com.example.mycookbook.activity.model;

import java.io.Serializable;

public class Receita implements Serializable {
    private int id = 0;
    private String nome;
    private String ingredientes;
    private String modoDePreparo;
    private String porcao;
    private String categoria;

    public Receita(String nome, String ingredientes, String modoDePreparo, String porcao, String categoria) {
        this.nome = nome;
        this.ingredientes = ingredientes;
        this.modoDePreparo = modoDePreparo;
        this.porcao = porcao;
        this.categoria = categoria;
    }

    public Receita() {

    }

    public boolean temIdValido() {
        return id > 0;
    }


    @Override
    public String toString() {
        return this.nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getModoDePreparo() {
        return modoDePreparo;
    }

    public void setModoDePreparo(String modoDePreparo) {
        this.modoDePreparo = modoDePreparo;
    }

    public String getPorcao() {
        return porcao;
    }

    public void setPorcao(String porcao) {
        this.porcao = porcao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

}
