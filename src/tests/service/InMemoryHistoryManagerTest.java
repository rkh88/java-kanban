package service;

import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.InMemoryHistoryManager;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class InMemoryHistoryManagerTest {

    private HistoryManager<HistoryManager> historyManager;

    @Test
    void add(Task task) {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void remove() {
    }

    @Test
    void getHistory() {
    }

    @Test
    void getTaskHashMap() {
    }


}
