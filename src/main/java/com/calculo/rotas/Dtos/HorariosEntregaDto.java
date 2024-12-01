package com.calculo.rotas.Dtos;

public class HorariosEntregaDto {
    private Integer inicio;
    private Integer fim;

    public HorariosEntregaDto(Integer inicio, Integer fim) {
        this.inicio = inicio;
        this.fim = fim;
    }

    public HorariosEntregaDto() {
    }

    public Integer getInicio() {
        return inicio;
    }

    public void setInicio(Integer inicio) {
        this.inicio = inicio;
    }

    public Integer getFim() {
        return fim;
    }

    public void setFim(Integer fim) {
        this.fim = fim;
    }
}
