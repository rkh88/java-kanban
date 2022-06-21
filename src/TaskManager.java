import util.Epic;
import util.Status;
import util.Subtask;
import util.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    protected HashMap<Integer, Task> allTasks;
    protected HashMap<Integer, Subtask> allSubtasks;
    protected HashMap<Integer, Epic> allEpics;
    protected int counter = 1;

    public HashMap<Integer, Task> getAllTasks() {
        return allTasks;
    }

    public HashMap<Integer, Subtask> getAllSubtasks() {
        return allSubtasks;
    }

    public HashMap<Integer, Epic> getAllEpics() {
        return allEpics;
    }

    public TaskManager() {
        this.allTasks = new HashMap<>();
        this.allSubtasks = new HashMap<>();
        this.allEpics = new HashMap<>();
    }

    public void deleteAllTasks() { getAllTasks().clear(); }

    public void deleteAllEpics() { getAllEpics().clear(); }

    public void deleteAllSubtasks() { getAllSubtasks().clear(); }

    public Task getTaskById(int id) {
        return getAllTasks().get(id);
    }

    public Task getSubtaskById(int id) {
        return getAllSubtasks().get(id);
    }

    public Task getEpicById(int id) {
        return getAllEpics().get(id);
    }

    public Task createTask(String name, String description) {
        Task task = new Task(name, description, counter);
        getAllTasks().put(counter, task);
        counter++;
        return task;
    }

    public Epic createEpic(String name, String description) {
        Epic epic = new Epic(name, description, counter);
        getAllEpics().put(counter, epic);
        counter++;
        return epic;
    }

    public Subtask createSubtask(String name, String description, Status status, Epic epic) {
        Subtask subtask = new Subtask(name, description, counter, status, epic);
        getAllSubtasks().put(counter, subtask);
        epic.addSubtask(subtask);
        epic.checkEpicStatus();
        counter++;
        return subtask;
    }

    public void updateTask(Task task) {
        getAllTasks().put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask) {
        getAllSubtasks().put(subtask.getId(), subtask);
        getAllSubtasks().get(subtask.getId()).getEpic().checkEpicStatus();
    }

    public void updateEpic(Epic epic) {
        getAllEpics().put(epic.getId(), epic);
    }

    public void deleteTaskById(int id) {
        getAllTasks().remove(id);
    }

    public void deleteSubtaskById(int id) {
        getAllSubtasks().remove(id);
        getAllSubtasks().get(id).getEpic().checkEpicStatus();
    }

    public void deleteEpicById(int id) {
        getAllEpics().remove(id);
    }

    public ArrayList<Subtask> getSubtasksListByEpic(Epic epic) {
        return epic.getSubtasks();
    }
}
