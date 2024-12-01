package com.calculo.rotas.EnvioDtos;

import java.util.List;
import java.util.Objects;

public class RotasDto {
    private String endereco;
    private Integer entregador;
    private double latitude;
    private double longitude;
    public RotasDto() {
    }

    public RotasDto(String endereco, Integer entregador, double latitude, double longitude) {
        this.endereco = endereco;
        this.entregador = entregador;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Integer getEntregador() {
        return entregador;
    }

    public void setEntregador(Integer entregador) {
        this.entregador = entregador;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "RotasDto{" +
                "endereco='" + endereco + '\'' +
                ", entregador=" + entregador +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RotasDto rotasDto = (RotasDto) o;
        return Double.compare(latitude, rotasDto.latitude) == 0 && Double.compare(longitude, rotasDto.longitude) == 0 && Objects.equals(endereco, rotasDto.endereco) && Objects.equals(entregador, rotasDto.entregador);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endereco, entregador, latitude, longitude);
    }
}
