package com.calculo.rotas;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TravelingSalesmanDP {
    private static final int INF = Integer.MAX_VALUE / 2; // Prevenir overflow ao somar valores
    private static final String API_KEY = "AIzaSyCex0CPAzMpv91y6vM5eusrCcwOnDTHa_k";
    public static List<List<Integer>> dividirEntregaPorTempo(List<Integer> entrega, double[][] graph, double timeWindow) throws Exception {
        List<List<Integer>> partes = new ArrayList<>();
        List<Integer> parteAtual = new ArrayList<>();
        double tempoTotal = 0;

        for (int i = 0; i < entrega.size() - 1; i++) {
            int from = entrega.get(i);
            int to = entrega.get(i + 1);
            tempoTotal += graph[from][to];

            if (tempoTotal > timeWindow) {
                if (!parteAtual.isEmpty()) {
                    partes.add(new ArrayList<>(parteAtual));
                    parteAtual.clear();
                }
                tempoTotal = 0;
            }

            parteAtual.add(from);
        }
        parteAtual.add(entrega.get(entrega.size() - 1));
        if (!parteAtual.isEmpty()) {
            partes.add(parteAtual);
        }
        System.out.println(tempoTotal);

        return partes;
    }
    public static List<List<String>> dividirLotes(List<String> enderecos, int loteTamanho) {
        List<List<String>> lotes = new ArrayList<>();
        for (int i = 0; i < enderecos.size(); i += loteTamanho) {
            lotes.add(enderecos.subList(i, Math.min(i + loteTamanho, enderecos.size())));
        }
        return lotes;
    }
    // Função para calcular o TSP e dividir as rotas entre os entregadores

    public static double[] tspTestes(double[][] graph, int maxCapacity, List<Integer> entregadores, double timeWindow) throws Exception {
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

        System.out.println("Saída esperada: O custo mínimo do caminho é: " + minCost);
        System.out.print("Saída esperada: O caminho percorrido é: ");
        for (double p : path) {
            System.out.print(p + " ");
        }
        System.out.println();

        // Dividir as rotas entre os entregadores
        List<List<Integer>> entregas = dividirRotas(path, maxCapacity, entregadores.size(), graph, timeWindow);
        System.out.println("Saída esperada:\nAlocação de entregadores e rotas:");
        for (int i = 0; i < entregas.size(); i++) {
            System.out.println("Saída esperada: Entregador " + (i + 1) + ": " + entregas.get(i));
        }

        return path;
    }
    public static double[] tsp(double[][] graph, int maxCapacity, List<Integer> entregadores, double timeWindow) throws Exception {
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
        List<List<Integer>> entregas = dividirRotas(path, maxCapacity, entregadores.size(), graph, timeWindow);
        System.out.println("\nAlocação de entregadores e rotas:");
        for (int i = 0; i < entregas.size(); i++) {
            System.out.println("Entregador " + (i + 1) + ": " + entregas.get(i));
        }

        return path;
    }

    // Função para dividir as rotas entre os entregadores
    public static List<List<Integer>> dividirRotas(double[] path, int maxCapacity, int entregadoresDisponiveis, double[][] graph, double timeWindow) throws Exception {
        List<List<Integer>> entregas = new ArrayList<>();
        List<Integer> rotaAtual = new ArrayList<>();
        int entregadorIndex = 0;
        double tempoTotalEntregador = 0;

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
                        throw new Exception("Erro: Não há entregadores suficientes para a quantidade de locais.");
                    }

                    // Adiciona a rota atual ao entregador
                    entregas.add(new ArrayList<>(rotaAtual));
                    System.out.println("Entregador " + (entregadorIndex + 1) + " tempo total: " + tempoTotalEntregador + " minutos.");

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
            entregas.add(rotaAtual);
            System.out.println("Entregador " + (entregadorIndex + 1) + " tempo total: " + tempoTotalEntregador + " minutos.");
        }

        // Validação de tempo para garantir que todos os entregadores possam realizar as rotas dentro do tempo

        // Garantir que todas as rotas começam com 0 (origem)
        for (List<Integer> rota : entregas) {
            if (rota.get(0) != 0) {
                rota.add(0, 0); // Insere a origem no começo, se necessário
            }
        }

        return entregas;
    }






    // Função para obter a Matriz de Distâncias e Tempo usando a API do Google
    public static double[][] getDistanceMatrix(List<String> addresses, String API_KEY) throws IOException {
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

    public static void main(String[] args) {
        try {
            String API_KEY = "AIzaSyCex0CPAzMpv91y6vM5eusrCcwOnDTHa_k";
            // Suponhamos que temos a matriz de distâncias e tempos
            List<String> listaEnderecos  = new ArrayList<>();
            listaEnderecos.add("Rua Assuncao, Jardim novo mundo");
            listaEnderecos.add("Estadio Serra Dourada");
            System.out.println("Entrada: " + "\n");
            for(String enderecos : listaEnderecos){
                System.out.println("Endereço: "  + enderecos);
            }


            double timeWindow = 240; // Janela de tempo de 60 minutos
            int capacity = 12;
            System.out.println("Janela de tempo: " + timeWindow);
            System.out.println("Capacidade: " + capacity);
            System.out.println();

                double[][] distances = new double[listaEnderecos.size()][listaEnderecos.size()]; // A matriz final para armazenar as distâncias
            int totalEnderecos = listaEnderecos.size();

            // Inicializar a matriz com valores de distâncias como "máximo", ou outro valor adequado
            for (int i = 0; i < totalEnderecos; i++) {
                Arrays.fill(distances[i], Double.MAX_VALUE); // ou algum valor inicial como "infinito"
            }

            // Processa a lista em lotes de 10
            for (int i = 0; i < totalEnderecos; i += 10) {
                // Sublista de 10 itens (ou menos se for o último lote)
                List<String> sublist = listaEnderecos.subList(i, Math.min(i + 10, totalEnderecos));

                // Chama a função para obter a matriz de distâncias
                double[][] partialDistances = getDistanceMatrix(sublist, API_KEY);

                // Agora, faça a junção da matriz de distâncias parcial com a matriz de distâncias final
                for (int j = 0; j < sublist.size(); j++) {
                    for (int k = 0; k < sublist.size(); k++) {
                        distances[i + j][i + k] = partialDistances[j][k];
                    }
                }
            }

            // Exemplo de entrega e janela de tempo
            List<Integer> entregadores = Arrays.asList(1,2); // 2 entregadores disponíveis
            tspTestes(distances, capacity, entregadores, timeWindow);
            System.out.println("\n");
            tsp(distances, capacity, entregadores, timeWindow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}