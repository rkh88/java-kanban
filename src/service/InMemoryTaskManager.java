package service;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {

    private final HashMap<Integer, Task> allTasks = new HashMap<>();
    private final HashMap<Integer, Subtask> allSubtasks = new HashMap<>();
    private final HashMap<Integer, Epic> allEpics = new HashMap<>();
    private int counter = 1;
    private final HistoryManager<HistoryManager> historyManager = Managers.getDefaultHistory();
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>();

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    @Override
    public HistoryManager<HistoryManager> getHistoryManager() {
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
    public void deleteAllTasks() {
        getAllTasks().clear();
    }

    @Override
    public void deleteAllEpics() {
        getAllEpics().clear();
    }

    @Override
    public void deleteAllSubtasks() {
        getAllSubtasks().clear();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = getAllTasks().get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = getAllSubtasks().get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = getAllEpics().get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Task createTask(Task task) {
        for(Map.Entry entry : getAllTasks().entrySet()) {
            Task value = (Task) entry.getValue();
            if(task.getStartTime() != null && value.getStartTime() != null && value.getStartTime().isEqual(task.getStartTime())) {
                throw new TaskValidationException("Task with such start time exists");
            }
        }

        task.setId(counter);
        getAllTasks().put(counter, task);
        prioritizedTasks.add(task);
        counter++;
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        if(epic.getDuration() != null) {
            for(Map.Entry entry : getAllEpics().entrySet()) {
                Epic value = (Epic) entry.getValue();
                if(epic.getStartTime() != null && value.getStartTime() != null && value.getStartTime().isEqual(epic.getStartTime())) {
                    throw new TaskValidationException("Epic with such start time exists");
                }
            }
        }

        epic.setId(counter);
        getAllEpics().put(counter, epic);
        prioritizedTasks.add(epic);
        counter++;
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) throws TaskValidationException {
        for(Map.Entry entry : getAllSubtasks().entrySet()) {
            Subtask value = (Subtask) entry.getValue();
            if(subtask.getStartTime() != null && value.getStartTime() != null && value.getStartTime().isEqual(subtask.getStartTime())) {
                throw new TaskValidationException("Subtask with such start time exists");
            }
        }

        subtask.setId(counter);
        getAllSubtasks().put(counter, subtask);
        Epic epic = subtask.getEpic();
        epic.addSubtask(subtask);
        checkEpicStatus(subtask.getEpic());
        prioritizedTasks.add(subtask);
        counter++;
        epic.setDuration(epic.getCalcDuration());
        epic.setStartTime(epic.getCalcStartTime());
        epic.setEndTime(epic.getCalcEndTime());
        return subtask;
    }

    @Override
    public void checkEpicStatus(Epic epic) {
        int epicDone = 0;
        int epicNew = 0;
        for (Subtask currentSubtask : epic.getSubtasks()) {
            if (currentSubtask.getStatus() == Status.DONE) {
                epicDone++;
            }
        }
        for (Subtask currentSubtask : epic.getSubtasks()) {
            if (currentSubtask.getStatus() == Status.NEW) {
                epicNew++;
            }
        }
        if (epicDone == epic.getSubtasks().size() || epicNew == epic.getSubtasks().size() || epic.getSubtasks().size() == 0) {
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
        if (getAllTasks().containsKey(task.getId())) {
            getAllTasks().put(task.getId(), task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (getAllSubtasks().containsKey(subtask.getId())) {
            getAllSubtasks().put(subtask.getId(), subtask);
            checkEpicStatus(getAllSubtasks().get(subtask.getId()).getEpic());
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (getAllEpics().containsKey(epic.getId())) {
            getAllEpics().put(epic.getId(), epic);
        }
    }

    @Override
    public void deleteTaskById(int id) throws ManagerSaveException {
        if (historyManager.getHistory().contains(getAllTasks().get(id))) {
            historyManager.remove(id);
        }
        if (getAllTasks().containsKey(id)) {
            getPrioritizedTasks().remove(getAllTasks().get(id));
            getAllTasks().remove(id);
        } else {
            System.out.println("No such task");
        }
    }

    @Override
    public void deleteSubtaskById(int id) throws ManagerSaveException {
        if (historyManager.getHistory().contains(getAllSubtasks().get(id))) {
            historyManager.remove(id);
        }
        if (getAllSubtasks().containsKey(id)) {
            getPrioritizedTasks().remove(getAllSubtasks().get(id));
            getAllSubtasks().remove(id);
            checkEpicStatus(getAllSubtasks().get(id).getEpic());
        } else {
            System.out.println("No such subtask");
        }
    }

    @Override
    public void deleteEpicById(int id) throws ManagerSaveException { //тут надо по DRY все сделать, пока думаю как
        if (historyManager.getHistory().size() != 0 && historyManager.getHistory().contains(getAllEpics().get(id))) {
            historyManager.remove(id);
            getPrioritizedTasks().remove(getAllEpics().get(id));
            getAllEpics().remove(id);

            ArrayList<Integer> keysToDelete = new ArrayList<>();
            for (Integer key : getAllSubtasks().keySet()) { // если удаляется эпик, сначала надо удалить все его сабтаски
                if (getAllSubtasks().get(key).getEpic().equals(getAllEpics().get(id))) {
                    keysToDelete.add(key);// избегаю concurrent modification exception
                }
            }
            for (Integer key : keysToDelete) {
                historyManager.remove(key);
                getPrioritizedTasks().remove(getAllSubtasks().get(key));
                getAllSubtasks().remove(key);
            }
        }

        if (getAllEpics().containsKey(id)) {
            ArrayList<Integer> keysToDelete = new ArrayList<>();
            for (Integer key : getAllSubtasks().keySet()) {
                if (getAllSubtasks().get(key).getEpic().equals(getAllEpics().get(id))) {
                    keysToDelete.add(key);
                }
            }
            for (Integer key : keysToDelete) {
                getPrioritizedTasks().remove(getAllSubtasks().get(key));
                getAllSubtasks().remove(key);
            }
            getPrioritizedTasks().remove(getAllEpics().get(id));
            getAllEpics().remove(id);
        } else {
            System.out.println("No such epic");
        }
    }

    @Override
    public ArrayList<Subtask> getSubtasksListByEpic(Epic epic) {
        return epic.getSubtasks();
    }

    @Override
    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }
}
