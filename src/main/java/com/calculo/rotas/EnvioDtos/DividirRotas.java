package com.calculo.rotas.EnvioDtos;

import java.util.List;

public class DividirRotas {
    private List<List<Integer>> entregas;
    private List<HorasEntregador> horas;
    private double tempoTotal;
    public DividirRotas(List<List<Integer>> entregas, List<HorasEntregador> horas, double tempoTotal) {
        this.entregas = entregas;
        this.horas = horas;
        this.tempoTotal = tempoTotal;
    }

    public DividirRotas() {
    }

    public double getTempoTotal() {
        return tempoTotal;
    }

    public void setTempoTotal(double tempoTotal) {
        this.tempoTotal = tempoTotal;
    }

    public List<List<Integer>> getEntregas() {
        return entregas;
    }

    public void setEntregas(List<List<Integer>> entregas) {
        this.entregas = entregas;
    }

    public List<HorasEntregador> getHoras() {
        return horas;
    }

    public void setHoras(List<HorasEntregador> horas) {
        this.horas = horas;
    }

    @Override
    public String
    toString() {
        return "DividirRotas{" +
                "entregas=" + entregas +
                ", horas=" + horas +
                ", tempoTotal=" + tempoTotal +
                '}';
    }
}
