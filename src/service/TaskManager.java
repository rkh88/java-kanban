package service;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private HashMap<Integer, Task> allTasks;
    private HashMap<Integer, Subtask> allSubtasks;
    private HashMap<Integer, Epic> allEpics;
    private int counter = 1;

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

    public Task createTask(Task task, String name, String description) {
        task.setName(name);
        task.setDescription(description);
        task.setId(counter);
        task.setStatus(Status.NEW);
        getAllTasks().put(counter, task);
        counter++;
        return task;
    }

    public Epic createEpic(Epic epic, String name, String description) {
        epic.setName(name);
        epic.setDescription(description);
        epic.setId(counter);
        epic.setStatus(Status.NEW);
        epic.setSubtasks(new ArrayList<Subtask>());
        getAllEpics().put(counter, epic);
        counter++;
        return epic;
    }

    public Subtask createSubtask(Subtask subtask, String name, String description, Status status, Epic epic) {
        subtask.setName(name);
        subtask.setDescription(description);
        subtask.setEpic(epic);
        subtask.setStatus(status);
        getAllSubtasks().put(counter, subtask);
        epic.addSubtask(subtask);
        checkEpicStatus(epic);
        counter++;
        return subtask;
    }

    public void checkEpicStatus (Epic epic) {
        int epicDone = 0;
        int epicNew = 0;
        for(Subtask currentSubtask : epic.getSubtasks()) {
            if(currentSubtask.getStatus() == Status.DONE) {
                epicDone++;
            }
        }
        for(Subtask currentSubtask : epic.getSubtasks()) {
            if(currentSubtask.getStatus() == Status.NEW) {
                epicNew++;
            }
        }
        if (epicDone == epic.getSubtasks().size() || epicNew == epic.getSubtasks().size() || epic.getSubtasks().size() == 0){
            if (epicDone == epic.getSubtasks().size()) {
                epic.setStatus(Status.DONE);
            }
            if (epicNew == epic.getSubtasks().size() || epic.getSubtasks().size() == 0) {
                epic.setStatus(Status.NEW);
            }
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    public void updateTask(Task task) {
        getAllTasks().put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask) {
        getAllSubtasks().put(subtask.getId(), subtask);
        checkEpicStatus(getAllSubtasks().get(subtask.getId()).getEpic());
    }

    public void updateEpic(Epic epic) {
        getAllEpics().put(epic.getId(), epic);
    }

    public void deleteTaskById(int id) {
        getAllTasks().remove(id);
    }

    public void deleteSubtaskById(int id) {
        getAllSubtasks().remove(id);
        checkEpicStatus(getAllSubtasks().get(id).getEpic());
    }

    public void deleteEpicById(int id) {
        getAllEpics().remove(id);
    }

    public ArrayList<Subtask> getSubtasksListByEpic(Epic epic) {
        return epic.getSubtasks();
    }
}
