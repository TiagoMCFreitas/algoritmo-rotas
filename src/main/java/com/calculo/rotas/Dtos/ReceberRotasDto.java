package com.calculo.rotas.Dtos;

import java.util.List;

public class ReceberRotasDto {
    private List<String> enderecos;
    private Integer capacidade;
    private HorariosEntregaDto horariosEntrega;
    private List<EntregadoresDto> entregadores;

    public ReceberRotasDto(List<String> enderecos, Integer capacidade, HorariosEntregaDto horariosEntrega, List<EntregadoresDto> entregadores) {
        this.enderecos = enderecos;
        this.capacidade = capacidade;
        this.horariosEntrega = horariosEntrega;
        this.entregadores = entregadores;
    }

    public ReceberRotasDto() {
    }

    public List<String> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<String> enderecos) {
        this.enderecos = enderecos;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public HorariosEntregaDto getHorariosEntrega() {
        return horariosEntrega;
    }

    public void setHorariosEntrega(HorariosEntregaDto horariosEntrega) {
        this.horariosEntrega = horariosEntrega;
    }

    public List<EntregadoresDto> getEntregadores() {
        return entregadores;
    }

    public void setEntregadores(List<EntregadoresDto> entregadores) {
        this.entregadores = entregadores;
    }

    @Override
    public String toString() {
        return "ReceberRotasDto{" +
                "enderecos=" + enderecos +
                ", capacidade=" + capacidade +
                ", horariosEntrega=" + horariosEntrega +
                ", entregadores=" + entregadores +
                '}';
    }
}
