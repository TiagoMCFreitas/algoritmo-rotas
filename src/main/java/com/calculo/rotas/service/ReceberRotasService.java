package com.calculo.rotas.service;

import com.calculo.rotas.Dtos.ReceberRotasDto;
import com.calculo.rotas.EnvioDtos.EnvioRotasDto;
import com.calculo.rotas.EnvioDtos.HorasEntregador;
import com.calculo.rotas.EnvioDtos.RotasDto;
import com.calculo.rotas.latitudeElongitude.LatitudeLongitudeDto;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReceberRotasService {
    private static final String APIKEY = "AIzaSyCex0CPAzMpv91y6vM5eusrCcwOnDTHa_k";
    @Autowired
    private Algoritmo algoritmo;

    public EnvioRotasDto receberRotas(ReceberRotasDto rotas) throws Exception {
        double[][] distances = algoritmo.getDistanceMatrix(rotas.getEnderecos(), APIKEY);
        int janelaTempo = (rotas.getHorariosEntrega().getFim() - rotas.getHorariosEntrega().getInicio()) * 60;
        var recebimento = algoritmo.tsp(distances, rotas.getCapacidade(), rotas.getEntregadores(), janelaTempo, rotas.getEnderecos());
        var enderecosLatiLong = geocodificarEnderecoGoogle(rotas.getEnderecos(), APIKEY);

        for (int i = 0; i < recebimento.getRotas().size(); i++) {
            RotasDto rotasDto = new RotasDto();
            int posicaoEntregador = recebimento.getRotas().get(i).getEntregador();
            Integer idEntregador = rotas.getEntregadores().get(posicaoEntregador).getId();
            rotasDto.setEntregador(idEntregador);
            recebimento.getRotas().get(i).setEntregador(rotasDto.getEntregador());
            for(LatitudeLongitudeDto latlong: enderecosLatiLong){
                if(latlong.getNomeEndereco().equals(recebimento.getRotas().get(i).getEndereco())){
                        recebimento.getRotas().get(i).setLatitude(latlong.getLatitude());
                        recebimento.getRotas().get(i).setLongitude(latlong.getLongitude());
                }
            }
        }
        for (int i = 0; i < recebimento.getHorasEntregadores().size(); i++) {
            HorasEntregador horasEntregador = new HorasEntregador();
            int posicaoEntregador = recebimento.getHorasEntregadores().get(i).getEntregador();
            Integer idEntregador = rotas.getEntregadores().get(posicaoEntregador).getId();
            horasEntregador.setEntregador(idEntregador);
            recebimento.getHorasEntregadores().get(i).setEntregador(horasEntregador.getEntregador());
        }

        return recebimento;
    }


    public List<LatitudeLongitudeDto> geocodificarEnderecoGoogle(List<String> enderecoLista, String apiKey) throws Exception {
        List<LatitudeLongitudeDto> listaLatLong = new ArrayList<>();
        for (String endereco : enderecoLista) {
            String urlString = String.format(
                    "https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s",
                    java.net.URLEncoder.encode(endereco, "UTF-8"),
                    apiKey
            );

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new Exception("Erro na requisição: HTTP " + responseCode);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                JSONObject jsonResponse = new JSONObject(response.toString());
                String status = jsonResponse.getString("status");

                if ("OK".equals(status)) {
                    JSONArray results = jsonResponse.getJSONArray("results");
                    JSONObject location = results.getJSONObject(0)
                            .getJSONObject("geometry")
                            .getJSONObject("location");
                    double latitude = location.getDouble("lat");
                    double longitude = location.getDouble("lng");
                    LatitudeLongitudeDto latitudeLongitudeDto = new LatitudeLongitudeDto();
                    latitudeLongitudeDto.setLatitude(latitude);
                    latitudeLongitudeDto.setLongitude(longitude);
                    latitudeLongitudeDto.setNomeEndereco(endereco);
                    listaLatLong.add(latitudeLongitudeDto);
                } else {
                    throw new Exception("Erro ao geocodificar o endereço: " + status);
                }
            }
        }

        return listaLatLong;
    }
}
