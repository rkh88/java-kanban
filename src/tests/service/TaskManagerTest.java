/*
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
        epic = new Epic("TestEpic", "TestEpic description");
        taskManager.createEpic(epic);
        subtask = new Subtask("Test Subtask", "Test description", Duration.ofMinutes(30), epic);
        taskManager.createSubtask(subtask);

    }

    @Test
    void getTasks() {
        final HashMap<Integer, Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются");
        assertEquals(1, tasks.size(), "Не верное количество задач");
        assertEquals(task, tasks.get(0), "Задачи не совпадают");
    }
}
*/
