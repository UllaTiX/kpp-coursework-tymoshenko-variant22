package ua.edu.nupp.kpp.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class HttpAutoServiceClient {
    private static final String BASE = "http://localhost:8080/car-service-server";
    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    public HttpAutoServiceClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    public void addCar(String licensePlate, String brand, String model, int year, String clientName, String clientPhone) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("licensePlate", licensePlate);
        params.put("brand", brand);
        params.put("model", model);
        params.put("year", String.valueOf(year));
        params.put("clientName", clientName);
        params.put("clientPhone", clientPhone);

        send("/car", params);
    }

    private String encode(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }

    private String send(String endpoint, Map<String, String> params) throws IOException, InterruptedException {
        StringBuilder body = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!first) {
                body.append("&");
            }
            body.append(encode(entry.getKey()))
                    .append("=")
                    .append(encode(entry.getValue()));
            first = false;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + endpoint))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 400) {
            throw new RuntimeException("HTTP Error: " + response.statusCode() + " -> " + response.body());
        }
        return response.body();
    }

    private String get(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + path))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 400) {
            throw new RuntimeException("HTTP Error: " + response.statusCode() + " -> " + response.body());
        }
        return response.body();
    }

    // --- Методи отримання списків (GET) ---
    public List<Client> getClients() throws IOException, InterruptedException {
        String json = get("/clients");
        return mapper.readValue(json, new TypeReference<List<Client>>() {});
    }

    public List<Car> getCars() throws IOException, InterruptedException {
        String json = get("/cars");
        return mapper.readValue(json, new TypeReference<List<Car>>() {});
    }

    public List<ServiceOrder> getOrders() throws IOException, InterruptedException {
        String json = get("/orders");
        return mapper.readValue(json, new TypeReference<List<ServiceOrder>>() {});
    }

    public List<WorkItem> getWorkItems() throws IOException, InterruptedException {
        String json = get("/workitems");
        return mapper.readValue(json, new TypeReference<List<WorkItem>>() {});
    }

    // --- Додавання клієнта ---
    public void addClient(String name, String phone, String email) throws IOException, InterruptedException {
        Map<String, String> params = Map.of(
                "action", "add",
                "name", name,
                "phone", phone,
                "email", email
        );
        send("/clients", params);
    }
    // --- Видалення клієнта ---
    public void deleteClient(Long id) throws IOException, InterruptedException {
        Map<String, String> params = Map.of(
                "action", "delete",
                "id", String.valueOf(id)
        );
        send("/clients", params);
    }
    // --- Зміна статусу замовлення (предметна операція за аналогією до методички) ---
    public void changeOrderState(Long id, ServiceOrderState state) throws IOException, InterruptedException {
        Map<String, String> params = Map.of(
                "action", "transition",
                "id", String.valueOf(id),
                "state", state.name()
        );
        send("/orders", params);
    }
}