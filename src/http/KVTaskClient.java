package http;

import server.KVServer;
import service.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    int port;
    private String url;

    public KVTaskClient(int port) {
        this.port = port;
    }

    private String register(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "register"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, handler);
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Не удалось" + response.statusCode());
            }
            return response.body();
        } catch (ManagerSaveException | InterruptedException | IOException e) {
            throw new RuntimeException("Не удалось");
        }
    }

    public String load(String key) throws ManagerSaveException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            String apiToken = KVServer.generateApiToken();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "load/" + key + "?API_TOKEN=" + apiToken))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, handler);

            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Не удалось" + response.statusCode());
            }
            return response.body();
        } catch (ManagerSaveException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void put(String key, String jsonTasks) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String apiToken = KVServer.generateApiToken();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonTasks))
                    .build();
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Не удалось :(" + response.statusCode());
            }
        } catch (ManagerSaveException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
