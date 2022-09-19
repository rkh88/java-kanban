package test;

import com.google.gson.Gson;
import service.Managers;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientTest {
    private final String url;
    private final Gson gson;
    private final HttpClient client;
    public HttpClientTest (int port) {
        this.url = "http://localhost:" + port + "/";
        client = HttpClient.newHttpClient();
        gson = Managers.getGson();
    }

    public HttpClientTest() {
        url = "";
        gson = new Gson();
        client = null;
    }

    public int addTask(Task task){
        String taskJson = gson.toJson(task);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .uri(URI.create(url + "tasks/task"))
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        System.out.println("Sending addTask, json: "+ taskJson);
        try {
            HttpResponse<String> response = client.send(request, handler);
            return response.statusCode();
        } catch (IOException | InterruptedException e) {
            return 500;
        }

    }

    public Task getTask(int id){

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "tasks/task" + "?id=" + id))
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        System.out.println("getting a task, id: "+ id);
        try {
            HttpResponse<String> response = client.send(request, handler);
            String body = response.body();
            System.out.println("got task, json: " + body);
            return gson.fromJson(body, Task.class);

        } catch (IOException | InterruptedException e) {
            return null;
        }

    }
}
