package service;

public class Managers {

    static public InMemoryTaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    static public InMemoryHistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}

