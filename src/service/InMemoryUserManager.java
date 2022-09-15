package service;

import tasks.Task;
import tasks.User;

import java.util.HashMap;
import java.util.List;

public class InMemoryUserManager implements UserManager {

    private final HashMap<Integer, User> users = new HashMap<>();
    private final TaskManager taskManager = Managers.getDefault();
    private int generatedId = 0;

    @Override
    public int add(User user) {
        int id = generateId();
        user.setId(id);
        users.put(id, user);
        return id;
    }

    @Override
    public void update(User user) {
        int id = user.getId();
    }

    @Override
    public User getById(int id) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public List<Task> getUserTasks() {
        return null;
    }

    @Override
    public TaskManager getTaskManager() {
        return null;
    }

    private int generateId() {
        return ++generatedId;
    }
}
