package service;

import tasks.Task;
import tasks.User;

import java.util.List;

public interface UserManager {

    int add(User user);

    void update(User user);

    User getById(int id);

    List<User> getAll();

    List<Task> getUserTasks();

    TaskManager getTaskManager();
}
