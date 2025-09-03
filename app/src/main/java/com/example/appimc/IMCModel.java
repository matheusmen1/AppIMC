package com.example.appimc;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class IMCModel implements Serializable
{
    private String nome, sexo, data, condicao;
    private double altura, imc;
    private int peso;

    public IMCModel(String nome, String sexo, String data, String condicao, double altura, double imc, int peso) {
        this.nome = nome;
        this.sexo = sexo;
        this.data = data;
        this.condicao = condicao;
        this.altura = altura;
        this.imc = imc;
        this.peso = peso;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setCondicao(String condicao) {
        this.condicao = condicao;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public void setImc(double imc) {
        this.imc = imc;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }
    @NonNull
    @Override
    public String toString() {
        return String.format(
                        "Nome: %-15s\n" +
                        "Sexo: %-10s\n" +
                        "Peso: %.2f kg\n" +
                        "Altura: %.2f m\n" +
                        "IMC: %.2f\n" +
                        "Condição: %-15s\n" +
                        "Data: %s",
                nome, sexo, (double)peso, altura, imc, condicao, data
        );
    }

}
