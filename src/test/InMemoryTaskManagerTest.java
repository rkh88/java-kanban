package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void setUp() {
        tm = new InMemoryTaskManager();
        initTasks();
    }

    @Test
    public void prioritizedTasksCheck() {
        List<Task> taskList = new ArrayList<>(tm.getPrioritizedTasks());
        Assertions.assertEquals(tm.getAllTasks().get(1), taskList.get(0));
        Assertions.assertEquals(tm.getAllSubtasks().get(3), taskList.get(1));
    }

    @Test
    public void taskCrossingCheck() {
        Task crossingTask = new Task("CrossingTask", "CrossingTask description", Duration.ofMinutes(15));
        crossingTask.setStartTime(tm.getAllTasks().get(1).getStartTime());
        tm.createTask(crossingTask);
        Assertions.assertEquals(1, tm.getAllTasks().size());
    }

    @Test
    public void subtaskCrossingCheck() {
        Subtask crossingSubtask = new Subtask("CrossingSubtask", "CrossingSubtask description", Duration.ofMinutes(15), tm.getAllSubtasks().get(3).getEpic());
        crossingSubtask.setStartTime(tm.getAllSubtasks().get(3).getStartTime());
        tm.createSubtask(subtask);
        Assertions.assertEquals(1, tm.getAllSubtasks().size());
    }

    @Test
    void createTask() {
        tm.getAllTasks().clear();
        tm.getAllEpics().clear();
        tm.getAllSubtasks().clear();
        tm.setCounter(1);
        final int taskId = tm.createTask(task).getId();
        final Task savedTask = tm.getTaskById(taskId);

        Assertions.assertNotNull(savedTask, "Задача не найдена.");
        Assertions.assertEquals(task, savedTask, "Задачи не совпадают.");

        final HashMap<Integer, Task> tasks = tm.getAllTasks();

        Assertions.assertNotNull(tasks, "Задачи на возвращаются.");
        Assertions.assertEquals(1, tasks.size(), "Неверное количество задач.");
        Assertions.assertEquals(task, tasks.get(1), "Задачи не совпадают.");
    }

    @Test
    void createEpic() {
        tm.getAllTasks().clear();
        tm.getAllEpics().clear();
        tm.getAllSubtasks().clear();
        tm.setCounter(1);
        final int epicId = tm.createEpic(epic).getId();
        final Epic savedEpic = tm.getEpicById(epicId);

        Assertions.assertNotNull(savedEpic, "Задача не найдена.");
        Assertions.assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final HashMap<Integer, Epic> epics = tm.getAllEpics();

        Assertions.assertNotNull(epics, "Задачи на возвращаются.");
        Assertions.assertEquals(1, epics.size(), "Неверное количество задач.");
        Assertions.assertEquals(epic, epics.get(1), "Задачи не совпадают.");
    }

    @Test
    void createSubtask() {
        tm.getAllTasks().clear();
        tm.getAllEpics().clear();
        tm.getAllSubtasks().clear();
        tm.setCounter(1);
        final int subtaskId = tm.createSubtask(subtask).getId();
        final Subtask savedSubtask = tm.getSubtaskById(subtaskId);

        Assertions.assertNotNull(subtask, "Задача не найдена.");
        Assertions.assertEquals(subtask, savedSubtask, "Задачи не совпадают.");

        final HashMap<Integer, Subtask> subtasks = tm.getAllSubtasks();

        Assertions.assertNotNull(subtasks, "Задачи на возвращаются.");
        Assertions.assertEquals(1, subtasks.size(), "Неверное количество задач.");
        Assertions.assertEquals(subtask, subtasks.get(1), "Задачи не совпадают.");
    }

    @Test
    void deleteTaskById() {
        tm.deleteTaskById(1);
        Assertions.assertEquals(0, tm.getAllTasks().size());
    }

    @Test
    void deleteTaskByIdEmptyList() {
        tm.deleteTaskById(1);
        tm.deleteTaskById(1);
        Assertions.assertEquals(0, tm.getAllTasks().size());
    }

    @Test
    void deleteTaskByIdWrongId() {
        tm.deleteTaskById(5);
        Assertions.assertEquals(1, tm.getAllTasks().size());
    }

    @Test
    void deleteAllTasks() {
        tm.deleteAllTasks();
        Assertions.assertEquals(0, tm.getAllTasks().size());
    }

    @Test
    void deleteAllTasksEmptyList() {
        tm.deleteAllTasks();
        tm.deleteAllTasks();
        Assertions.assertEquals(0, tm.getAllTasks().size());
    }

    @Test
    void updateTask() {
        Task newTask = new Task(1, "New Task", Status.NEW,"desc", Duration.ofMinutes(30), LocalDateTime.now(), LocalDateTime.now().plus(Duration.ofMinutes(30)));
        tm.updateTask(newTask);
        Assertions.assertEquals(newTask, tm.getAllTasks().get(newTask.getId()));
    }

}




