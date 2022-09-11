package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import http.HttpTaskManager;
import server.KVServer;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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

    public static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

        @Override
        public void write(final JsonWriter jsonWriter, final LocalDateTime localDate) throws IOException {
            jsonWriter.value(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
    }

    public static class DurationAdapter extends TypeAdapter<Duration> {

        @Override
        public void write(final JsonWriter jsonWriter, final Duration localDate) throws IOException {
            jsonWriter.value(localDate.getSeconds());
        }

        @Override
        public Duration read(final JsonReader jsonReader) throws IOException {
            String s = jsonReader.nextString();
            long seconds = Long.parseLong(s);
            Duration d = Duration.of(seconds, ChronoUnit.SECONDS);
            return d;
        }
    }
}

