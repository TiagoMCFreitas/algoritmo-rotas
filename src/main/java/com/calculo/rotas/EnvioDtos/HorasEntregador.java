package com.calculo.rotas.EnvioDtos;

public class HorasEntregador {
    private Integer entregador;
    private double horasTotais;

    public HorasEntregador(Integer entregador, double horasTotais) {
        this.entregador = entregador;
        this.horasTotais = horasTotais;
    }
    public HorasEntregador(){}

    public Integer getEntregador() {
        return entregador;
    }

    public void setEntregador(Integer entregador) {
        this.entregador = entregador;
    }

    public double getHorasTotais() {
        return horasTotais;
    }

    public void setHorasTotais(double horasTotais) {
        this.horasTotais = horasTotais;
    }

    @Override
    public String toString() {
        return "HorasEntregador{" +
                "entregador=" + entregador +
                ", horasTotais=" + horasTotais +
                '}';
    }
}
