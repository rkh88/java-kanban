package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.TaskValidationException;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void setUp() {
        tm = new InMemoryTaskManager();
        initTasks();
    }

    @Test
    public void prioritizedTasksCheck() {
        Task taskWithoutTime = new Task("TestTaskWithoutTime", "TestTaskWithoutTime description");
        tm.createTask(taskWithoutTime);
        taskWithoutTime.setStartTime(null);
        Task taskNew = new Task("TestTaskNew", "TestTaskNew description", LocalDateTime.now().plusMinutes(30), Duration.ofMinutes(15));
        tm.createTask(taskNew);
        List<Task> taskList = new ArrayList<>(tm.getPrioritizedTasks());
        Assertions.assertEquals(tm.getAllTasks().get(1), taskList.get(0));
        Assertions.assertEquals(tm.getAllSubtasks().get(3), taskList.get(1));
        Assertions.assertEquals(tm.getAllEpics().get(2), taskList.get(2));
        Assertions.assertEquals(tm.getAllTasks().get(5), taskList.get(3));
        Assertions.assertEquals(tm.getAllTasks().get(4), taskList.get(4));// проверка на то, что таск без времени лежит в конце списка
        Assertions.assertEquals(5, taskList.size());
    }

    @Test
    public void taskCrossingTimeCheck() {
        Task crossingTask = new Task("CrossingTask", "CrossingTask description", tm.getAllTasks().get(1).getStartTime(), Duration.ofMinutes(15));
        Assertions.assertThrows(TaskValidationException.class, () -> tm.createTask(crossingTask));//Это что? Не проверка на исключение разве?
    }

    @Test
    public void subtaskCrossingTimeCheck() {
        Subtask crossingSubtask = new Subtask("CrossingSubtask", "CrossingSubtask description", tm.getAllSubtasks().get(3).getStartTime(), Duration.ofMinutes(15), tm.getAllSubtasks().get(3).getEpic());
        Assertions.assertThrows(TaskValidationException.class, () -> tm.createSubtask(crossingSubtask));
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




