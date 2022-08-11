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

    Task getTaskById(int id) throws ManagerSaveException;

    Subtask getSubtaskById(int id) throws ManagerSaveException;

    Epic getEpicById(int id) throws ManagerSaveException;

    Task createTask(Task task) throws ManagerSaveException;

    Epic createEpic(Epic epic) throws ManagerSaveException;

    Subtask createSubtask(Subtask subtask) throws ManagerSaveException;

    void checkEpicStatus (Epic epic);

    void setSubtaskStatus(Subtask subtask, Status status);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    void deleteTaskById(int id) throws ManagerSaveException;

    void deleteSubtaskById(int id) throws ManagerSaveException;

    void deleteEpicById(int id) throws ManagerSaveException;

    ArrayList<Subtask> getSubtasksListByEpic(Epic epic);
}
