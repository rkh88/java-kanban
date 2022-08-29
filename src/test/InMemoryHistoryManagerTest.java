package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.InMemoryHistoryManager;
import service.Managers;
import service.TaskManager;
import tasks.Task;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryHistoryManagerTest {

    private static final TaskManager tm = Managers.getDefault();
    private static final HistoryManager hm = tm.getHistoryManager();

        @BeforeEach
        public void beforeAll() {
        tm.getAllTasks().clear();
        tm.setCounter(1);
        hm.getTaskHashMap().clear();
        hm.getTaskCustomLinkedList().removeAll();
        Task testTask1 = new Task("TestTask 1", "TestTask 1 description", Duration.ofMinutes(15));
        Task testTask2 = new Task("TestTask 2", "TestTask 2 description", Duration.ofMinutes(15));
        Task testTask3 = new Task("TestTask 3", "TestTask 3 description", Duration.ofMinutes(15));
        tm.createTask(testTask1);
        tm.createTask(testTask2);
        tm.createTask(testTask3);
        hm.add(testTask1);
        hm.add(testTask2);
        hm.add(testTask3);
    }

    @Test
    public void add() {
        Task testTask4 = new Task("TestTask 4", "TestTask 4 description", Duration.ofMinutes(15));
        tm.createTask(testTask4);
        hm.add(testTask4);
        final List<Task> history = hm.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(4, history.size(), "История не пустая.");
    }

    @Test
    public void removeBegining() {
        hm.remove(1);
        final List<Task> history = hm.getHistory();
        assertEquals(2, history.size(), "История не пустая.");
    }

    @Test
    public void removeMiddle() {
        hm.remove(2);
        final List<Task> history = hm.getHistory();
        assertEquals(2, history.size(), "История не пустая.");
    }

    @Test
    public void removeEnd() {
        hm.remove(3);
        final List<Task> history = hm.getHistory();
        assertEquals(2, history.size(), "История не пустая.");
    }

    @Test
    public void doubleTask() {
        tm.setCounter(1);
        Task doubleTask = new Task("TestTask 1", "TestTask 1 description", Duration.ofMinutes(15));
        hm.add(doubleTask);
        final List<Task> history = hm.getHistory();
        assertEquals(4, history.size());
        assertEquals(4, hm.getTaskHashMap().size());

    }

    @Test
    public void emptyHistory() {
        tm.getAllTasks().clear();
        hm.getTaskHashMap().clear();
        hm.getTaskCustomLinkedList().removeAll();
        assertEquals(0, tm.getAllTasks().size());
        assertEquals(0, hm.getTaskHashMap().size());
        Assertions.assertNull(hm.getTaskCustomLinkedList().getHead());
    }

    @Test
    public void getHistory() {
        hm.add(new Task("TestTask", "TestTask description", Duration.ofMinutes(15)));
        final List<Task> history = hm.getHistory();
        assertNotNull(history, "История не пустая.");
    }

    @Test
    public void getTaskHashMap() {
        hm.add(new Task("TestTask", "TestTask description", Duration.ofMinutes(15)));
        final HashMap<Integer, InMemoryHistoryManager.Node> taskHashMap = hm.getTaskHashMap();
        assertNotNull(taskHashMap, "История не пустая.");
    }


}


