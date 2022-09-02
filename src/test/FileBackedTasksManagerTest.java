package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackedTasksManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.time.Duration;

class FileBackedTasksManagerTest { //у меня абсолютно все тесты успешно отрабатывают. Прошу дать более подробные комментарии

    private static File file = new File("history.csv");
    private static FileBackedTasksManager fb1;
    private static FileBackedTasksManager fb2;

    @BeforeEach
    public void setUp() {
        file.delete();
        file = new File("history.csv");
        fb1 = new FileBackedTasksManager(file);
        Task task = new Task("Task 1", "description Task 1", Duration.ofMinutes(30));
        Epic epic = new Epic("Epic 1", "description Epic 1");
        Subtask subtask = new Subtask("Subtask 1", "description Subtask 1", Duration.ofMinutes(15), epic);
        fb1.createTask(task);
        fb1.createEpic(epic);
        fb1.createSubtask(subtask);
        fb1.getTaskById(1);
        fb1.getEpicById(2);
        fb2 = FileBackedTasksManager.loadFromFile(file);
    }

    @Test
    public void allTasksNotNull() {
        Assertions.assertNotNull(fb2.getAllTasks());
    }

    @Test
    public void historyNotNull() {
        Assertions.assertNotNull(fb2.getHistoryManager().getHistory());
    }

    @Test
    public void taskHashMapNotNull() {
        Assertions.assertNotNull(fb2.getHistoryManager().getTaskHashMap());
    }

    @Test
    public void task1Equality() {
        Assertions.assertEquals("Task 1",fb2.getAllTasks().get(1).getName());
    }
}
