package com.calculo.rotas.latitudeElongitude;

import java.util.Objects;

public class LatitudeLongitudeDto {
    private String nomeEndereco;
    private Double latitude;
    private Double longitude;

    public LatitudeLongitudeDto(String nomeEndereco, Double latitude, Double longitude) {
        this.nomeEndereco = nomeEndereco;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LatitudeLongitudeDto() {
    }

    public String getNomeEndereco() {
        return nomeEndereco;
    }

    public void setNomeEndereco(String nomeEndereco) {
        this.nomeEndereco = nomeEndereco;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LatitudeLongitudeDto that = (LatitudeLongitudeDto) o;
        return Objects.equals(nomeEndereco, that.nomeEndereco) && Objects.equals(latitude, that.latitude) && Objects.equals(longitude, that.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeEndereco, latitude, longitude);
    }

    @Override
    public String toString() {
        return "LatitudeLongitudeDto{" +
                "nomeEndereco='" + nomeEndereco + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
