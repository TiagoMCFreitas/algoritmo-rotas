//package com.calculo.rotas.service;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.springframework.stereotype.Service;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URI;
//import java.net.URL;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.util.*;
//
//@Service
//public class ReceberRotas {
//    private List<EnviarRotasDto> listaRotas = new ArrayList<>();
//    public void geocodificarEnderecoGoogle(List<String> enderecoLista, String apiKey) throws Exception {
//        listaRotas.clear();
//        for(String endereco : enderecoLista){
//        String urlString = String.format(
//                "https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s",
//                java.net.URLEncoder.encode(endereco, "UTF-8"),
//                apiKey
//        );
//
//        URL url = new URL(urlString);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//
//        int responseCode = conn.getResponseCode();
//        if (responseCode != 200) {
//            throw new Exception("Erro na requisição: HTTP " + responseCode);
//        }
//
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
//            StringBuilder response = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                response.append(line);
//            }
//
//            JSONObject jsonResponse = new JSONObject(response.toString());
//            String status = jsonResponse.getString("status");
//
//            if ("OK".equals(status)) {
//                JSONArray results = jsonResponse.getJSONArray("results");
//                JSONObject location = results.getJSONObject(0)
//                        .getJSONObject("geometry")
//                        .getJSONObject("location");
//                double latitude = location.getDouble("lat");
//                double longitude = location.getDouble("lng");
//                listaRotas.add(
//                        new EnviarRotasDto(
//                                endereco,
//                                latitude,
//                                longitude,
//                                0,
//                                0
//                        )
//                );
//            }
//            else {
//                throw new Exception("Erro ao geocodificar o endereço: " + status);
//            }
//            }
//        }
//    }
//
//    public double calcularDistancia(EnviarRotasDto a, EnviarRotasDto b) {
//        double lat1 = a.getLatitude();
//        double lon1 = a.getLongitude();
//        double lat2 = b.getLatitude();
//        double lon2 = b.getLongitude();
//
//        double radius = 6371; // Raio da Terra em quilômetros
//
//        double dLat = Math.toRadians(lat2 - lat1);
//        double dLon = Math.toRadians(lon2 - lon1);
//
//        double h = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
//                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
//                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
//
//        double distancia = 2 * Math.atan2(Math.sqrt(h), Math.sqrt(1 - h));
//        return radius * distancia; // Retorna a distância em quilômetros
//    }
//
//    // Método para calcular o caminho mais curto (usando força bruta, verificando todas as permutações)
//    public RotasCompleto resolverTSP(ReceberRotasDto localizacoes) throws Exception {
//        geocodificarEnderecoGoogle(localizacoes.getEnderecos(), "AIzaSyCex0CPAzMpv91y6vM5eusrCcwOnDTHa_k");
//
//        if (listaRotas == null || listaRotas.size() < 2) {
//            throw new IllegalArgumentException("É necessário pelo menos dois pontos para calcular a rota.");
//        }
//
//        List<EnviarRotasDto> naoVisitados = new ArrayList<>(listaRotas);
//        List<EnviarRotasDto> rotasCompletas = new ArrayList<>();
//        double tempoTotal = 0;
//
//        // Configuração inicial
//        int motoristaAtual = 0;
//        int capacidadeAtual = localizacoes.getCapacidade();
//
//        // Definir o tempo máximo por motorista (4 horas)
//        double tempoMaximoPorMotorista = 240.0; // Tempo máximo em horas
//
//        // Define o ponto inicial
//        EnviarRotasDto pontoInicial = naoVisitados.get(0);
//        naoVisitados.remove(0);
//
//        while (!naoVisitados.isEmpty()) {
//            EnviarRotasDto pontoAtual = pontoInicial; // Sempre começa do ponto inicial para cada motorista
//            List<EnviarRotasDto> rotaMotorista = new ArrayList<>();
//
//            // Adiciona o ponto inicial à rota do motorista atual
//            EnviarRotasDto origemMotorista = new EnviarRotasDto();
//            origemMotorista.setNomeEndereco(pontoInicial.getNomeEndereco());
//            origemMotorista.setLatitude(pontoInicial.getLatitude());
//            origemMotorista.setLongitude(pontoInicial.getLongitude());
//            origemMotorista.setTempo(0.0); // Definindo o tempo inicial como 0
//
//            origemMotorista.setMotoristaId(localizacoes.getEntregadores().get(motoristaAtual));
//            origemMotorista.setTempo(0); // O ponto inicial não tem tempo acumulado
//            rotasCompletas.add(origemMotorista);
//
//            double tempoMotoristaAtual = 0;  // Tempo total do motorista atual
//
//            while (!naoVisitados.isEmpty() && capacidadeAtual > 0) {
//                EnviarRotasDto proximoPonto = null;
//                double menorDistancia = Double.MAX_VALUE;
//                double menorTempo = Double.MAX_VALUE;
//
//                // Encontra o próximo ponto mais próximo
//                for (EnviarRotasDto ponto : naoVisitados) {
//                    double distancia = calcularDistancia(pontoAtual, ponto);
//                    double tempo = calcularTempoGoogleMaps(pontoAtual, ponto);
//
//                    if (distancia < menorDistancia) {
//                        menorDistancia = distancia;
//                        menorTempo = tempo;
//                        proximoPonto = ponto;
//                    }
//                }
//
//                if (proximoPonto == null) break;
//
//                // Atualiza o motorista e tempo no ponto
//                proximoPonto.setMotoristaId(localizacoes.getEntregadores().get(motoristaAtual));
//                proximoPonto.setTempo(menorTempo);
//
//                // Atualiza as listas
//                rotaMotorista.add(proximoPonto);
//                naoVisitados.remove(proximoPonto);
//
//                // Atualiza a capacidade e o tempo total
//                capacidadeAtual--;
//                tempoMotoristaAtual += menorTempo;
//                tempoTotal += menorTempo;
//
//                // Verifica se o tempo total do motorista atual excedeu a janela de tempo
//                if (tempoMotoristaAtual > tempoMaximoPorMotorista) {
//                    throw new IllegalArgumentException("O motorista " + motoristaAtual + " excedeu a janela de tempo disponível.");
//                }
//
//                // Atualiza o ponto atual
//                pontoAtual = proximoPonto;
//
//                // Adiciona à lista final
//                rotasCompletas.add(proximoPonto);
//            }
//
//            // Prepara para o próximo motorista
//            motoristaAtual++;
//            if (motoristaAtual >= localizacoes.getEntregadores().size() && !naoVisitados.isEmpty()) {
//                throw new IllegalArgumentException("A quantidade de motoristas informada não é suficiente para realizar todas as entregas.");
//            }
//
//            // Reseta a capacidade e o ponto inicial para o próximo motorista
//            capacidadeAtual = localizacoes.getCapacidade();
//
//            // Verifica se o total de tempo das rotas não excede o tempo total disponível
//            double tempoTotalTodasRotas = 0.0;
//            for (EnviarRotasDto rota : rotasCompletas) {
//                tempoTotalTodasRotas += rota.getTempo();
//            }
//
//            if (tempoTotalTodasRotas > tempoMaximoPorMotorista * localizacoes.getEntregadores().size()) {
//                throw new IllegalArgumentException("O total de tempo das rotas excede o tempo disponível para os motoristas.");
//            }
//        }
//
//        // Mapa para rastrear os tempos por motorista
//        List<TempoMotorista> listaMotoristas = new ArrayList<>();
//        Map<Integer, Double> temposPorMotorista = new HashMap<>();
//
//        // Itera pelas rotas para somar os tempos por motorista
//        for (EnviarRotasDto rota : rotasCompletas) {
//            int motoristaId = rota.getMotoristaId();
//            double tempoRota = rota.getTempo();
//
//            // Atualiza o tempo total do motorista no mapa
//            temposPorMotorista.put(motoristaId, temposPorMotorista.getOrDefault(motoristaId, 0.0) + tempoRota);
//        }
//
//        // Converte o mapa em uma lista de objetos TempoMotorista
//        for (Map.Entry<Integer, Double> entry : temposPorMotorista.entrySet()) {
//            TempoMotorista tempoMotorista = new TempoMotorista();
//            tempoMotorista.setId(entry.getKey());
//            tempoMotorista.setTempo(entry.getValue());
//            listaMotoristas.add(tempoMotorista);
//        }
//
//        // Monta o objeto final
//        RotasCompleto rotasCompleto = new RotasCompleto();
//        rotasCompleto.setTempoTotal(tempoTotal);
//        rotasCompleto.setRotas(rotasCompletas);
//        rotasCompleto.setTempoMotoristas(listaMotoristas);
//
//        return rotasCompleto;
//    }
//
//
//
//    private double calcularTempoGoogleMaps(EnviarRotasDto origem, EnviarRotasDto destino) throws IOException, InterruptedException {
//        String url = String.format(
//                "https://maps.googleapis.com/maps/api/distancematrix/json?origins=%f,%f&destinations=%f,%f&key=%s",
//                origem.getLatitude(), origem.getLongitude(), destino.getLatitude(), destino.getLongitude(),
//                "AIzaSyCex0CPAzMpv91y6vM5eusrCcwOnDTHa_k"
//        );
//        System.out.println(url);
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(url))
//                .GET()
//                .build();
//
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        JSONObject jsonResponse = new JSONObject(response.body());
//
//        // Verifica se há dados válidos
//        if (!jsonResponse.has("rows") || jsonResponse.getJSONArray("rows").isEmpty()) {
//            throw new RuntimeException("Resposta inválida da API Google Maps.");
//        }
//
//        return jsonResponse
//                .getJSONArray("rows")
//                .getJSONObject(0)
//                .getJSONArray("elements")
//                .getJSONObject(0)
//                .getJSONObject("duration")
//                .getDouble("value") / 60.0; // Retorna o tempo em minutos
//    }
//
//
//
//
//
//}
