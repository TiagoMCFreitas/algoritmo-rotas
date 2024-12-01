package com.calculo.rotas.EnvioDtos;

import java.util.List;

public class EnvioRotasDto {
    private List<RotasDto> rotas;
    private double tempoTotal;;
    private List<HorasEntregador> horasEntregadores;

    public EnvioRotasDto() {
    }

    public EnvioRotasDto(List<RotasDto> rotas, double tempoTotal, List<HorasEntregador> horasEntregadores) {
        this.rotas = rotas;
        this.tempoTotal = tempoTotal;
        this.horasEntregadores = horasEntregadores;
    }

    public List<RotasDto> getRotas() {
        return rotas;
    }

    public void setRotas(List<RotasDto> rotas) {
        this.rotas = rotas;
    }

    public double getTempoTotal() {
        return tempoTotal;
    }

    public void setTempoTotal(double tempoTotal) {
        this.tempoTotal = tempoTotal;
    }

    public List<HorasEntregador> getHorasEntregadores() {
        return horasEntregadores;
    }

    public void setHorasEntregadores(List<HorasEntregador> horasEntregadores) {
        this.horasEntregadores = horasEntregadores;
    }

    @Override
    public String toString() {
        return "EnvioRotasDto{" +
                "rotas=" + rotas +
                ", tempoTotal=" + tempoTotal +
                ", horasEntregadores=" + horasEntregadores +
                '}';
    }
}
