package http;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import service.FileBackedTasksManager;
import service.Managers;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private final Gson gson;
    private final KVTaskClient client;

    public HttpTaskManager(int port) {
        this(port, false);
    }

    public HttpTaskManager(int port, boolean load) {
        this.client = new KVTaskClient(port, "http://localhost:" + port + "/tasks/task");
        gson = Managers.getGson();
        if (load) {
            load();
        }
    }

    private void addTasks(ArrayList<? extends Task> tasks) {
        for (Task task : tasks) {
            final int id = task.getId();
            if (id > getCounter()) {
                setCounter(id);
            }

            if (task instanceof Task) {
                this.getAllTasks().put(id, task);
                getPrioritizedTasks().add(task);
            } else if (task instanceof Subtask) {
                getAllSubtasks().put(id, (Subtask) task);
                getPrioritizedTasks().add(task);
            } else if (task instanceof Epic) {
                getAllEpics().put(id, (Epic) task);
            }
        }
    }

    private void load() {
        ArrayList<Task> tasks = gson.fromJson(client.load("tasks"), new TypeToken<ArrayList<Task>>(){}.getType());
        addTasks(tasks);
        ArrayList<Epic> epics = gson.fromJson(client.load("epics"), new TypeToken<ArrayList<Epic>>(){}.getType());
        addTasks(epics);
        ArrayList<Subtask> subtasks = gson.fromJson(client.load("subtasks"), new TypeToken<ArrayList<Subtask>>(){}.getType());
        addTasks(subtasks);

        List<Integer> history = gson.fromJson(client.load("history"), new TypeToken<ArrayList<Integer>>(){}.getType());
        for(Integer taskId : history) {
            getHistoryManager().add(getTaskById(taskId));
        }
    }

    @Override
    public void save() {
        String jsonTasks = gson.toJson(getAllTasks());
        client.put("tasks", jsonTasks);
        String jsonEpics = gson.toJson(getAllEpics());
        client.put("epics", jsonEpics);
        String jsonSubtasks = gson.toJson(getAllSubtasks());
        client.put("subtasks", jsonSubtasks);
    }
}