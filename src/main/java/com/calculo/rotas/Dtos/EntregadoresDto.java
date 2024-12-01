package com.calculo.rotas.Dtos;

public class EntregadoresDto {
    private String nome;
    private Integer id;


    public EntregadoresDto() {
    }

    public EntregadoresDto(String nome, Integer id) {
        this.nome = nome;
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
