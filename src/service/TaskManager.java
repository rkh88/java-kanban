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

    public Subtask getSubtaskById(int id) {
        return getAllSubtasks().get(id);
    }

    public Epic getEpicById(int id) {
        return getAllEpics().get(id);
    }

    public Task createTask(Task task) {
        task.setId(counter);
        getAllTasks().put(counter, task);
        counter++;
        return task;
    }

    public Epic createEpic(Epic epic) {
        epic.setId(counter);
        getAllEpics().put(counter, epic);
        counter++;
        return epic;
    }

    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(counter);
        getAllSubtasks().put(counter, subtask);
        subtask.getEpic().addSubtask(subtask);
        checkEpicStatus(subtask.getEpic());
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

    public void setSubtaskStatus(Subtask subtask, Status status) { //Если метод checkEpicStatus писать в TaskManager, а не в самом Epic, то при вызове метода setStatus() на сервисе у сабтаска не будет виден метод checkStatus(), а это каждый раз необходимо. В этом была моя идея
        subtask.setStatus(status);
        checkEpicStatus(subtask.getEpic());
    }

    public void updateTask(Task task) {
        if(getAllTasks().containsKey(task.getId())) {
            getAllTasks().put(task.getId(), task);
        }
    }

    public void updateSubtask(Subtask subtask) {
        if(getAllSubtasks().containsKey(subtask.getId())) {
            getAllSubtasks().put(subtask.getId(), subtask);
            checkEpicStatus(getAllSubtasks().get(subtask.getId()).getEpic());
        }
    }

    public void updateEpic(Epic epic) {
        if(getAllEpics().containsKey(epic.getId())) {
            getAllEpics().put(epic.getId(), epic);
        }
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
