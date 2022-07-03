package service;

import tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    ArrayList<Task> taskHistory = new ArrayList<>();

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
            System.out.println("Task history: " + task.toString());
        }
    }

    @Override
    public void checkTaskHistorySize() {
        if (taskHistory.size() == 10) {
            taskHistory.remove(0);
        }
    }
}
