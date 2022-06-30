package service;

import tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    ArrayList<Task> taskHistory = new ArrayList<>();

    @Override
    public void add(tasks.Task task) {
        checkTaskHistorySize();
        taskHistory.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return taskHistory;
    }

    @Override
    public void printTaskHistory(InMemoryHistoryManager inMemoryHistoryManager) {
        for(Task task : inMemoryHistoryManager.getHistory()) {
            System.out.println(task.toString());
        }
    }

    public void checkTaskHistorySize() {
        if (taskHistory.size() == 10) {
            taskHistory.remove(0);
        }
    }
}
