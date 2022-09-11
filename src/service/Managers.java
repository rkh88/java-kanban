package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.HttpTaskManager;
import server.KVServer;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {

    static public TaskManager getDefault() {
        return new HttpTaskManager(KVServer.PORT, true);
    }
    public static KVServer getDefaultKVServer() throws IOException {
        final KVServer kvServer = new KVServer();
        kvServer.start();
        return kvServer;
    }

    static public HistoryManager<HistoryManager> getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());

        return gsonBuilder.create();
    }

    private static class LocalDateTimeAdapter {
    }

    private static class DurationAdapter {
    }
}

