package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
        initTasks();
    }

    @Test
    void createTask() {
        final int taskId = taskManager.createTask(task).getId();

        final Task savedTask = taskManager.getTaskById(taskId);

        Assertions.assertNotNull(savedTask, "Задача не найдена.");
        Assertions.assertEquals(task, savedTask, "Задачи не совпадают.");

        final HashMap<Integer, Task> tasks = taskManager.getAllTasks();

        Assertions.assertNotNull(tasks, "Задачи на возвращаются.");
        Assertions.assertEquals(1, tasks.size(), "Неверное количество задач.");
        Assertions.assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void createEpic() {
        final int epicId = taskManager.createEpic(epic).getId();

        final Epic savedEpic = taskManager.getEpicById(epicId);

        Assertions.assertNotNull(savedEpic, "Задача не найдена.");
        Assertions.assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final HashMap<Integer, Epic> epics = taskManager.getAllEpics();

        Assertions.assertNotNull(epics, "Задачи на возвращаются.");
        Assertions.assertEquals(1, epics.size(), "Неверное количество задач.");
        Assertions.assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void createSubtask() {
        final int subtaskId = taskManager.createSubtask(subtask).getId();

        final Subtask savedSubtask = taskManager.getSubtaskById(subtaskId);

        Assertions.assertNotNull(subtask, "Задача не найдена.");
        Assertions.assertEquals(subtask, savedSubtask, "Задачи не совпадают.");

        final HashMap<Integer, Subtask> subtasks = taskManager.getAllSubtasks();

        Assertions.assertNotNull(subtasks, "Задачи на возвращаются.");
        Assertions.assertEquals(1, subtasks.size(), "Неверное количество задач.");
        Assertions.assertEquals(subtask, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void deleteTaskById() {
        taskManager.deleteTaskById(1);
        Assertions.assertEquals(0, taskManager.getAllTasks().size());
    }

    @Test
    void deleteTaskByIdEmptyList() {
        taskManager.deleteTaskById(1);
        taskManager.deleteTaskById(1);
        Assertions.assertEquals(0, taskManager.getAllTasks().size());
    }

    @Test
    void deleteTaskByIdWrongId() {
        taskManager.deleteTaskById(5);
        Assertions.assertEquals(1, taskManager.getAllTasks().size());
    }

    @Test
    void deleteAllTasks() {
        taskManager.deleteAllTasks();
        Assertions.assertEquals(0, taskManager.getAllTasks().size());
    }

    @Test
    void deleteAllTasksEmptyList() {
        taskManager.deleteAllTasks();
        taskManager.deleteAllTasks();
        Assertions.assertEquals(0, taskManager.getAllTasks().size());
    }

    @Test
    void updateTask() {
        Task newTask = new Task(1, "New Task", Status.NEW,"desc", Duration.ofMinutes(30), LocalDateTime.now(), LocalDateTime.now().plus(Duration.ofMinutes(30)));
        taskManager.updateTask(newTask);
        Assertions.assertEquals(newTask, taskManager.getAllTasks().get(newTask.getId()));
    }

}


