package service;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    protected void initTasks() {
        task = new Task("TestTask", "TestTask description", Duration.ofMinutes(15));
        taskManager.createTask(task);
        epic = new Epic("TestEpic", "TestEpic description", Duration.ofMinutes(0));
        taskManager.createEpic(epic);
        subtask = new Subtask("Test Subtask", "Test description", Duration.ofMinutes(30), epic);
        taskManager.createSubtask(subtask);

    }

    @Test
    void getTasks() {
        final HashMap<Integer, Task> tasks = taskManager.getAllTasks();
        final HashMap<Integer, Epic> epics = taskManager.getAllEpics();
        final HashMap<Integer, Subtask> subtasks = taskManager.getAllSubtasks();

        assertNotNull(tasks, "Задачи на возвращаются");
        assertNotNull(epics, "Задачи на возвращаются");
        assertNotNull(subtasks, "Задачи на возвращаются");
        assertEquals(1, tasks.size(), "Не верное количество задач");
        assertEquals(1, epics.size(), "Не верное количество задач");
        assertEquals(1, subtasks.size(), "Не верное количество задач");
        assertEquals(task, tasks.get(0), "Задачи не совпадают");
        assertEquals(epic, epics.get(0), "Задачи не совпадают");
        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают");
    }
}
