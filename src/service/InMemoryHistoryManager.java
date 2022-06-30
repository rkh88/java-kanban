package service;

import tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    static ArrayList<Task> taskHistory = new ArrayList<>();

    @Override
    public void add(Task task) {
        checkTaskHistorySize();
        taskHistory.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return taskHistory;
    }

    @Override
    public void printTaskHistory() {
        for(Task task : getHistory()) {
            System.out.println(task.toString());
        }
    }

    public static void checkTaskHistorySize() {
        if (taskHistory.size() == 10) {
            taskHistory.remove(0);
        }
    }
}
