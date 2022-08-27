package service;

public class Managers {

    static public TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    static public HistoryManager<HistoryManager> getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}

