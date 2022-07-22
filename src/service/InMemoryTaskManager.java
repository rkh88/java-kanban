package service;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {

    private HashMap<Integer, Task> allTasks = new HashMap<>();
    private HashMap<Integer, Subtask> allSubtasks = new HashMap<>();
    private HashMap<Integer, Epic> allEpics = new HashMap<>();
    private int counter = 1;
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public HashMap<Integer, Task> getAllTasks() {
        return allTasks;
    }

    @Override
    public HashMap<Integer, Subtask> getAllSubtasks() {
        return allSubtasks;
    }

    @Override
    public HashMap<Integer, Epic> getAllEpics() {
        return allEpics;
    }

    @Override
    public void deleteAllTasks() { getAllTasks().clear(); }

    @Override
    public void deleteAllEpics() { getAllEpics().clear(); }

    @Override
    public void deleteAllSubtasks() { getAllSubtasks().clear(); }

    @Override
    public Task getTaskById(int id) {
        if(getAllTasks().containsKey(id)){
            Task task = getAllTasks().get(id);
            historyManager.add(task);
            return task;
        } else {
            return new Task("Random task", "description"); // Вот тут вопрос. Что вернуть, если таска с указанным айдишником нет? А если вообще нет тасков? Тот же вопрос для двух других аналогичных методов
        }
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (getAllSubtasks().containsKey(id)) {
            Subtask subtask = getAllSubtasks().get(id);
            historyManager.add(subtask);
            return subtask;
        } else {
            return new Subtask("Random task", "description", new Epic("Random Epic", "description"));
        }
    }

    @Override
    public Epic getEpicById(int id) {
        if (getAllEpics().containsKey(id)) {
            Epic epic = getAllEpics().get(id);
            historyManager.add(epic);
            return epic;
        } else {
            return new Epic("Random epic", "description");
        }
    }

    @Override
    public Task createTask(Task task) {
        task.setId(counter);
        getAllTasks().put(counter, task);
        counter++;
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(counter);
        getAllEpics().put(counter, epic);
        counter++;
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(counter);
        getAllSubtasks().put(counter, subtask);
        subtask.getEpic().addSubtask(subtask);
        checkEpicStatus(subtask.getEpic());
        counter++;
        return subtask;
    }

    @Override
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

    @Override
    public void setSubtaskStatus(Subtask subtask, Status status) { //Если метод checkEpicStatus писать в TaskManager, а не в самом Epic, то при вызове метода setStatus() на сервисе у сабтаска не будет виден метод checkStatus(), а это каждый раз необходимо. В этом была моя идея
        subtask.setStatus(status);
        checkEpicStatus(subtask.getEpic());
    }

    @Override
    public void updateTask(Task task) {
        if(getAllTasks().containsKey(task.getId())) {
            getAllTasks().put(task.getId(), task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if(getAllSubtasks().containsKey(subtask.getId())) {
            getAllSubtasks().put(subtask.getId(), subtask);
            checkEpicStatus(getAllSubtasks().get(subtask.getId()).getEpic());
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if(getAllEpics().containsKey(epic.getId())) {
            getAllEpics().put(epic.getId(), epic);
        }
    }

    @Override
    public void deleteTaskById(int id) {
        if(historyManager.getHistory().contains(getAllTasks().get(id))) {
            historyManager.remove(id);
            System.out.println("Task with id " + id + " was removed from history");
        }
        if(getAllTasks().containsKey(id)) {
            getAllTasks().remove(id);
            System.out.println("Task with id " + id + " was removed from initial TaskHashMap");
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        if(historyManager.getHistory().contains(getAllSubtasks().get(id))) {
            historyManager.remove(id);
            System.out.println("Subtask with id " + id + " was removed from history");
        }
        if(getAllSubtasks().containsKey(id)) {
            getAllSubtasks().remove(id);
            checkEpicStatus(getAllSubtasks().get(id).getEpic());
            System.out.println("Subtask with id " + id + " was removed from initial TaskHashMap");
        }
    }

    @Override
    public void deleteEpicById(int id) { //тут надо по DRY все сделать, пока думаю как
        if(historyManager.getHistory().size() != 0 && historyManager.getHistory().contains(getAllEpics().get(id))) {
            historyManager.remove(id);
            getAllEpics().remove(id);
            System.out.println("Epic with id " + id + " was removed from history and from initial TaskHashMap");
            ArrayList<Integer> keysToDelete = new ArrayList<>();
            for(Integer key : getAllSubtasks().keySet()) { // если удаляется эпик, сначала надо удалить все его сабтаски
                if(getAllSubtasks().get(key).getEpic().equals(getAllEpics().get(id))) {
                    keysToDelete.add(key);// избегаю concurrent modification exception
                }
            }
            for(Integer key : keysToDelete){
                historyManager.remove(key);
                getAllSubtasks().remove(key);
            }
            System.out.println("All epic's subtasks were removed from history and from initial TaskHashMap");
        }

        if(getAllEpics().containsKey(id)) {
            ArrayList<Integer> keysToDelete = new ArrayList<>();
            for(Integer key : getAllSubtasks().keySet()) { // если удаляется эпик, сначала надо удалить все его сабтаски
                if(getAllSubtasks().get(key).getEpic().equals(getAllEpics().get(id))) {
                    keysToDelete.add(key);// избегаю concurrent modification exception
                }
            }
            for(Integer key : keysToDelete){
                getAllSubtasks().remove(key);
            }
            getAllEpics().remove(id);
            System.out.println("Epic with id " + id + " and all its subtasks were removed from initial TaskHashMap");
        }

    }

    @Override
    public ArrayList<Subtask> getSubtasksListByEpic(Epic epic) {
        return epic.getSubtasks();
    }

}
