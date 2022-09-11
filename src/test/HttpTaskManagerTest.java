package test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import http.HttpTaskManager;
import server.KVServer;
import service.Managers;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    private KVServer kvServer;
    private HttpTaskManager taskManager;

    @BeforeEach
    public void setUp() throws IOException {
        kvServer = Managers.getDefaultKVServer();
        taskManager = new HttpTaskManager(KVServer.PORT);
        initTasks();
    }

    @AfterEach
    protected void tearDown() {
        kvServer.stop();
    }

    @Test
    public void loadFromHttpServer() {
        taskManager.getTaskById(task.getId());
        taskManager.getSubtaskById(subtask.getId());
        taskManager.getEpicById(epic.getId());

        HttpTaskManager taskManager = new HttpTaskManager(KVServer.PORT, true);

        final HashMap<Integer, Task> tasks = taskManager.getAllTasks();
        Assertions.assertNotNull(tasks, "Возвращает не пустой список задач");
        assertEquals(1, tasks.size(), "Возвращает не пустой список задач");

        final HashMap<Integer, Epic> epics = taskManager.getAllEpics();
        assertNotNull(epics, "Возвращает не пустой список эпиков");
        assertEquals(1, epics.size(), "Возвращает не пустой список эпиков");

        final HashMap<Integer, Subtask> subtasks = taskManager.getAllSubtasks();
        assertNotNull(subtasks, "Возвращает не пустой список подзадач");
        assertEquals(1, subtasks.size(), "Возвращает не пустой список подзадач");

        final List<Task> history = taskManager.getHistoryManager().getHistory();
        assertNotNull(history, "Возвращает не пустой список истории");
        assertEquals(3, history.size(), "Возвращает не пустой список истории");
        assertEquals(subtask.getId(), taskManager.getCounter(), "Не установлено значение generatorId");
    }
}