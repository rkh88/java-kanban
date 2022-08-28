package test;

import org.junit.jupiter.api.Test;
import service.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T tm;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    protected void initTasks() {
        tm.setCounter(1);
        task = new Task("TestTask", "TestTask description", Duration.ofMinutes(15));
        tm.createTask(task);
        epic = new Epic("TestEpic", "TestEpic description", Duration.ofMinutes(0));
        tm.createEpic(epic);
        subtask = new Subtask("Test Subtask", "Test description", Duration.ofMinutes(30), epic);
        tm.createSubtask(subtask);

    }

    @Test
    void getTasks() {
        final HashMap<Integer, Task> tasks = tm.getAllTasks();
        final HashMap<Integer, Epic> epics = tm.getAllEpics();
        final HashMap<Integer, Subtask> subtasks = tm.getAllSubtasks();

        assertNotNull(tasks, "Задачи на возвращаются");
        assertNotNull(epics, "Задачи на возвращаются");
        assertNotNull(subtasks, "Задачи на возвращаются");
        assertEquals(1, tasks.size(), "Не верное количество задач");
        assertEquals(1, epics.size(), "Не верное количество задач");
        assertEquals(1, subtasks.size(), "Не верное количество задач");
        assertEquals(task, tasks.get(1), "Задачи не совпадают");
        assertEquals(epic, epics.get(2), "Задачи не совпадают");
        assertEquals(subtask, subtasks.get(3), "Задачи не совпадают");
    }
}


