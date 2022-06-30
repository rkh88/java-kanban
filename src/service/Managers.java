package service;

public class Managers {

    static InMemoryTaskManager inMemoryTaskManager;
    static InMemoryHistoryManager inMemoryHistoryManager;

    public Managers(InMemoryTaskManager inMemoryTaskManager, InMemoryHistoryManager inMemoryHistoryManager) {
        this.inMemoryTaskManager = inMemoryTaskManager;
        this.inMemoryHistoryManager = inMemoryHistoryManager;
    }

    static InMemoryTaskManager getDefault(){
        return inMemoryTaskManager;
    }

    static InMemoryHistoryManager getDefaultHistory(){
        return inMemoryHistoryManager;
    }
}

