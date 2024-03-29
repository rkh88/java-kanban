package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import service.Managers;
import service.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class HttpTaskServer {

    private TaskManager taskManager;
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static Gson gson = new Gson();
    public static HttpServer server;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::TasksHandler);
        server.createContext("/tasks/subtasks", this::SubtasksHandler);
        server.createContext("/tasks/epics", this::EpicsHandler);
        server.createContext("/tasks/history", this::HistoryHandler);
    }

    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }

    public void start() {
        System.out.println("Started TaskServer " + PORT);
        System.out.println("http://localhost:" + PORT + "/tasks/task");
        System.out.println("http://localhost:" + PORT + "/tasks/subtask");
        System.out.println("http://localhost:" + PORT + "/tasks/epic");
        System.out.println("http://localhost:" + PORT + "/tasks/history");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    public void TasksHandler(HttpExchange httpExchange) throws IOException {
        final String query = httpExchange.getRequestURI().getQuery();
        switch (httpExchange.getRequestMethod()) {
            case "GET":
                if (query == null) {
                    final HashMap<Integer, Task> tasks = taskManager.getAllTasks();
                    final String response = gson.toJson(tasks);
                    System.out.println("Задачи получены");
                    sendText(httpExchange, response);
                    return;
                }

                String idIndex = query.substring(3);
                final int id = Integer.parseInt(idIndex);
                final Task task = taskManager.getTaskById(id);
                final String response = gson.toJson(task);
                System.out.println("Получили задачу с id = " + id);
                sendText(httpExchange, response);

            case "DELETE":
                if (query == null) {
                    taskManager.deleteAllTasks();
                    System.out.println("Удалили все задачи");
                    httpExchange.sendResponseHeaders(200, 0);
                    return;
                }
            case "POST":
                String json = readText(httpExchange);
                if (json.isEmpty()) {
                    System.out.println("Тело запроса пусто");
                    httpExchange.sendResponseHeaders(400, 0);
                    return;
                }
                final Task taskToPost = gson.fromJson(json, Task.class);
                final Integer idToPost = taskToPost.getId();
                if (idToPost != null) {
                    taskManager.updateTask(taskToPost);
                    System.out.println("Обновили задачу с id = " + idToPost);
                    httpExchange.sendResponseHeaders(200, 0);
                } else {
                    taskManager.createTask(taskToPost);
                    System.out.println("Создали задачу с id = " + idToPost);
                    final String responseToPost = gson.toJson(taskToPost);
                    sendText(httpExchange, responseToPost);
                }
            default:
                System.out.println("/task получен " + httpExchange.getRequestMethod());
                httpExchange.sendResponseHeaders(405, 0);
        }


    }

    public void SubtasksHandler(HttpExchange httpExchange) throws IOException {
        final String query = httpExchange.getRequestURI().getQuery();
        switch (httpExchange.getRequestMethod()) {
            case "GET":
                if (query == null) {
                    final HashMap<Integer, Subtask> subtasks = taskManager.getAllSubtasks();
                    final String response = gson.toJson(subtasks);
                    System.out.println("Подзадачи получены");
                    sendText(httpExchange, response);
                    return;
                }

                String idIndex = query.substring(3);
                final int id = Integer.parseInt(idIndex);
                final Subtask subtask = taskManager.getSubtaskById(id);
                final String response = gson.toJson(subtask);
                System.out.println("Получили подзадачу с id = " + id);
                sendText(httpExchange, response);

            case "DELETE":
                if (query == null) {
                    taskManager.deleteAllSubtasks();
                    System.out.println("Удалили все задачи");
                    httpExchange.sendResponseHeaders(200, 0);
                    return;
                }
            case "POST":
                String json = readText(httpExchange);
                if (json.isEmpty()) {
                    System.out.println("Тело запроса пусто");
                    httpExchange.sendResponseHeaders(400, 0);
                    return;
                }
                final Subtask subtaskToPost = gson.fromJson(json, Subtask.class);
                final Integer idToPost = subtaskToPost.getId();
                if (idToPost != null) {
                    taskManager.updateSubtask(subtaskToPost);
                    System.out.println("Обновили подзадачу с id = " + idToPost);
                    httpExchange.sendResponseHeaders(200, 0);
                } else {
                    taskManager.createSubtask(subtaskToPost);
                    System.out.println("Создали подзадачу с id = " + idToPost);
                    final String responseToPost = gson.toJson(subtaskToPost);
                    sendText(httpExchange, responseToPost);
                }
            default:
                System.out.println("/subtask получен " + httpExchange.getRequestMethod());
                httpExchange.sendResponseHeaders(405, 0);
        }
    }

    public void EpicsHandler(HttpExchange httpExchange) throws IOException {
        final String query = httpExchange.getRequestURI().getQuery();
        switch (httpExchange.getRequestMethod()) {
            case "GET":
                if (query == null) {
                    final HashMap<Integer, Epic> epics = taskManager.getAllEpics();
                    final String response = gson.toJson(epics);
                    System.out.println("Эпики получены");
                    sendText(httpExchange, response);
                    return;
                }

                String idIndex = query.substring(3);
                final int id = Integer.parseInt(idIndex);
                final Epic epic = taskManager.getEpicById(id);
                final String response = gson.toJson(epic);
                System.out.println("Получили эпик с id = " + id);
                sendText(httpExchange, response);

            case "DELETE":
                if (query == null) {
                    taskManager.deleteAllEpics();
                    System.out.println("Удалили все задачи");
                    httpExchange.sendResponseHeaders(200, 0);
                    return;
                }
            case "POST":
                String json = readText(httpExchange);
                if (json.isEmpty()) {
                    System.out.println("Тело запроса пусто");
                    httpExchange.sendResponseHeaders(400, 0);
                    return;
                }
                final Epic epicToPost = gson.fromJson(json, Epic.class);
                final Integer idToPost = epicToPost.getId();
                if (idToPost != null) {
                    taskManager.updateEpic(epicToPost);
                    System.out.println("Обновили эпик с id = " + idToPost);
                    httpExchange.sendResponseHeaders(200, 0);
                } else {
                    taskManager.createEpic(epicToPost);
                    System.out.println("Создали эпик с id = " + idToPost);
                    final String responseToPost = gson.toJson(epicToPost);
                    sendText(httpExchange, responseToPost);
                }
            default:
                System.out.println("/epic получен " + httpExchange.getRequestMethod());
                httpExchange.sendResponseHeaders(405, 0);
        }
    }

    public void HistoryHandler(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        URI responseURI = httpExchange.getRequestURI();
        String path = responseURI.getPath();
        String[] splitStrings = path.split("/");
        if (path.endsWith("/history")) {
            String historyJson = gson.toJson(taskManager.getHistoryManager().getHistory());
            httpExchange.sendResponseHeaders(200, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(historyJson.getBytes());
            }
        } else if (path.endsWith("/tasks")) {
            String prTasksJson = gson.toJson(taskManager.getPrioritizedTasks());
            httpExchange.sendResponseHeaders(200, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(prTasksJson.getBytes(StandardCharsets.UTF_8));
            }
            httpExchange.close();
        } else {
            httpExchange.sendResponseHeaders(404, 0);
            httpExchange.close();
        }
    }


    private String readText(HttpExchange httpExchange) throws IOException {
        InputStream is = httpExchange.getRequestBody();
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int length; (length = is.read(buffer)) != -1; ) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }

    private void sendText(HttpExchange httpExchange, String response) throws IOException {
        String responseJson = gson.toJson(response);
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(responseJson.getBytes(StandardCharsets.UTF_8));
        }
        httpExchange.close();
    }
}
