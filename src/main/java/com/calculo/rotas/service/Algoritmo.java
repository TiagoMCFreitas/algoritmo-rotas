package com.calculo.rotas.service;

import com.calculo.rotas.Dtos.EntregadoresDto;
import com.calculo.rotas.EnvioDtos.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class Algoritmo {
    private static final int INF = Integer.MAX_VALUE / 2; // Prevenir overflow ao somar valores
    private DividirRotas dividirRotas(double[] path, int maxCapacity, int entregadoresDisponiveis, double[][] graph, double timeWindow) throws Exception {
        DividirRotas dividir = new DividirRotas();
        List<List<Integer>> entregas = new ArrayList<>();
        List<HorasEntregador> horarios = new ArrayList<>();

        List<Integer> rotaAtual = new ArrayList<>();
        int entregadorIndex = 0;

        double tempoTotalEntregador = 0;
        System.out.println("Entregadores Disponiveis: " + entregadoresDisponiveis);

        // Adiciona sempre a origem (ponto 0) no começo de cada rota
        rotaAtual.add(0);

        // Loop para percorrer todos os pontos de entrega
        for (int i = 1; i < path.length - 1; i++) {
            int from = (int) path[i];
            int to = (int) path[i + 1];
            double tempoDeViagem = graph[from][to];

            // Verifica se adicionar esse ponto à rota não ultrapassa a capacidade ou o tempo do entregador atual
            if (tempoTotalEntregador + tempoDeViagem <= timeWindow && rotaAtual.size() < maxCapacity) {
                // Adiciona o ponto à rota
                rotaAtual.add((int) path[i]);
                tempoTotalEntregador += tempoDeViagem;
            } else {
                // Verifica se o tempo de entrega ultrapassa o limite do entregador atual
                if (tempoTotalEntregador + tempoDeViagem > timeWindow) {
                    // O tempo ultrapassou o limite de tempo para esse entregador
                    if (entregadorIndex + 1 >= entregadoresDisponiveis) {
                        System.out.printf("Index entregador: " + entregadorIndex);
                        throw new Exception("Erro: Não há entregadores suficientes para a quantidade de locais.");
                    }

                    // Adiciona a rota atual ao entregador
                    entregas.add(new ArrayList<>(rotaAtual));
                    System.out.println("Entregador " + (entregadorIndex + 1) + " tempo total: " + tempoTotalEntregador + " minutos.");
                    HorasEntregador horasEntregador = new HorasEntregador();
                    horasEntregador.setEntregador(entregadorIndex); // Define o índice (ID) do entregador
                    horasEntregador.setHorasTotais(tempoTotalEntregador); // Define o total de horas
                    horarios.add(horasEntregador); // Adiciona às horas do entregador na lista final

                    // Reinicia a rota e o tempo do entregador
                    rotaAtual.clear();
                    tempoTotalEntregador = 0;

                    // Adiciona a origem e o primeiro ponto ao próximo entregador
                    rotaAtual.add(0); // A origem deve ser sempre o ponto de partida
                    rotaAtual.add((int) path[i]);
                    tempoTotalEntregador += tempoDeViagem;
                    entregadorIndex++;
                } else {
                    // Se o tempo de viagem não ultrapassar o limite, continua com o mesmo entregador
                    rotaAtual.add((int) path[i]);
                    tempoTotalEntregador += tempoDeViagem;
                }
            }
        }

        // Adiciona a última rota
        if (!rotaAtual.isEmpty()) {
            HorasEntregador horasEntregador = new HorasEntregador();
            horasEntregador.setEntregador(entregadorIndex); // Define o índice (ID) do entregador
            horasEntregador.setHorasTotais(tempoTotalEntregador); // Define o total de horas
            horarios.add(horasEntregador); // Adiciona às horas do entregador na lista final

            entregas.add(rotaAtual); // Adiciona as entregas realizadas à lista principal
            System.out.println("Entregador " + (entregadorIndex + 1)
                    + " tempo total: " + tempoTotalEntregador + " minutos.");
        }


        // Validação de tempo para garantir que todos os entregadores possam realizar as rotas dentro do tempo

        // Garantir que todas as rotas começam com 0 (origem)
        for (List<Integer> rota : entregas) {
            if (rota.get(0) != 0) {
                rota.add(0, 0); // Insere a origem no começo, se necessário
            }
            if (rota.get(rota.size() - 1) != 0) {
                rota.add(0); // Adiciona a origem no final, se necessário
            }
        }
        double tempoTotal = 0.0;
        for (HorasEntregador horario : horarios) {
            tempoTotal+=horario.getHorasTotais();
        }
        dividir.setEntregas(entregas);
        dividir.setHoras(horarios);
        dividir.setTempoTotal(tempoTotal);

        return dividir;
    }


    public EnvioRotasDto tsp(double[][] graph, int maxCapacity, List<EntregadoresDto> entregadores, double timeWindow, List<String> addresses) throws Exception {
        int n = graph.length;
        double[][] dp = new double[1 << n][n];
        double[][] parent = new double[1 << n][n];

        for (int i = 0; i < (1 << n); i++) {
            Arrays.fill(dp[i], Double.POSITIVE_INFINITY);
            Arrays.fill(parent[i], -1);
        }
        dp[1][0] = 0.0;

        for (int mask = 1; mask < (1 << n); mask++) {
            for (int u = 0; u < n; u++) {
                if ((mask & (1 << u)) == 0) continue;

                for (int v = 0; v < n; v++) {
                    if ((mask & (1 << v)) != 0 || graph[u][v] == INF) continue;

                    double newCost = dp[mask][u] + graph[u][v];
                    if (newCost < dp[mask | (1 << v)][v]) {
                        dp[mask | (1 << v)][v] = newCost;
                        parent[mask | (1 << v)][v] = u;
                    }
                }
            }
        }

        double minCost = INF;
        double lastCity = -1;
        double finalMask = (1 << n) - 1;

        for (int u = 1; u < n; u++) {
            if (graph[u][0] != INF) {
                double cost = dp[(int) finalMask][u] + graph[u][0];
                if (cost < minCost) {
                    minCost = cost;
                    lastCity = u;
                }
            }
        }

        double[] path = new double[n + 1];
        double index = n;
        path[(int) index--] = 0;
        int currentMask = (int) finalMask;
        double currentCity = lastCity;

        while (currentCity != -1) {
            path[(int) index--] = currentCity;
            double prevCity = parent[(int) currentMask][(int) currentCity];
            currentMask ^= (1 << (int) currentCity);
            currentCity = prevCity;
        }

        System.out.println("O custo mínimo do caminho é: " + minCost);
        System.out.print("O caminho percorrido é: ");
        for (double p : path) {
            System.out.print(p + " ");
        }
        System.out.println();

        // Dividir as rotas entre os entregadores
        DividirRotas entregas = dividirRotas(path, maxCapacity, entregadores.size(), graph, timeWindow);
        System.out.println("Entregas: " + entregas.toString());
        System.out.println("\nAlocação de entregadores e rotas:");
        List<RotasDto> listaRotas = new ArrayList<>();
        for (int i = 0; i < entregas.getEntregas().size(); i++) {
            RotasDto rotasDto = new RotasDto();
            rotasDto.setEntregador(i);

            for (int j = 0; j < entregas.getEntregas().get(i).size(); j++) {

                RotasDto rotaComEndereco = new RotasDto();
                rotaComEndereco.setEntregador(rotasDto.getEntregador());
                rotaComEndereco.setEndereco(addresses.get(entregas.getEntregas().get(i).get(j)));
                System.out.println(addresses.get(entregas.getEntregas().get(i).get(j)));

                listaRotas.add(rotaComEndereco);
            }

            System.out.println("Entregador " + (i + 1) + ": " + entregas.getEntregas().get(i));
        }


        System.out.println(entregas.getHoras().toString());

        EnvioRotasDto envioDeRotas = new EnvioRotasDto();
        envioDeRotas.setRotas(listaRotas);
        envioDeRotas.setTempoTotal(entregas.getTempoTotal());
        envioDeRotas.setHorasEntregadores(entregas.getHoras());

        return envioDeRotas;
    }

    public double[][] getDistanceMatrix(List<String> addresses, String API_KEY) throws IOException {
        OkHttpClient client = new OkHttpClient();
        StringBuilder origins = new StringBuilder();
        StringBuilder destinations = new StringBuilder();

        for (String address : addresses) {
            origins.append(address.replace(" ", "+")).append("|");
            destinations.append(address.replace(" ", "+")).append("|");
        }

        // Remover o último "|"
        origins.setLength(origins.length() - 1);
        destinations.setLength(destinations.length() - 1);

        String url = String.format(
                "https://maps.googleapis.com/maps/api/distancematrix/json?origins=%s&destinations=%s&key=%s",
                origins, destinations, API_KEY
        );

        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro na API: " + response);
            }
            String jsonResponse = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonResponse);

            JSONArray rows = jsonObject.getJSONArray("rows");
            double[][] distances = new double[rows.length()][rows.length()];
            double[][] times = new double[rows.length()][rows.length()]; // Matriz para armazenar os tempos

            for (int i = 0; i < rows.length(); i++) {
                JSONArray elements = rows.getJSONObject(i).getJSONArray("elements");
                for (int j = 0; j < elements.length(); j++) {
                    JSONObject element = elements.getJSONObject(j);
                    if (element.getString("status").equals("OK")) {
                        int distanceInMeters = element.getJSONObject("distance").getInt("value");
                        int durationInSeconds = element.getJSONObject("duration").getInt("value");

                        // Distâncias em quilômetros
                        distances[i][j] = distanceInMeters / 1000.0;

                        // Tempos em minutos
                        times[i][j] = durationInSeconds / 60.0;
                    } else {
                        distances[i][j] = -1; // Não conectado
                        times[i][j] = -1; // Não conectado
                    }
                }
            }
            return times;
        }
    }
}
