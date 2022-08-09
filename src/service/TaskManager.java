package service;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {

    HashMap<Integer, Task> getAllTasks();

    HashMap<Integer, Subtask> getAllSubtasks();

    HashMap<Integer, Epic> getAllEpics();

    HistoryManager getHistoryManager();

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    Task getTaskById(int id) throws FileBackedTasksManager.ManagerSaveException;

    Subtask getSubtaskById(int id) throws FileBackedTasksManager.ManagerSaveException;

    Epic getEpicById(int id) throws FileBackedTasksManager.ManagerSaveException;

    Task createTask(Task task) throws FileBackedTasksManager.ManagerSaveException;

    Epic createEpic(Epic epic) throws FileBackedTasksManager.ManagerSaveException;

    Subtask createSubtask(Subtask subtask) throws FileBackedTasksManager.ManagerSaveException;

    void checkEpicStatus (Epic epic);

    void setSubtaskStatus(Subtask subtask, Status status);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    void deleteTaskById(int id) throws FileBackedTasksManager.ManagerSaveException;

    void deleteSubtaskById(int id) throws FileBackedTasksManager.ManagerSaveException;

    void deleteEpicById(int id) throws FileBackedTasksManager.ManagerSaveException;

    ArrayList<Subtask> getSubtasksListByEpic(Epic epic);
}
