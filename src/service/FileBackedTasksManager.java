package service;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
        return subtask;
    }

    public Task createWithoutSaveTask (Task task) {
        super.createTask(task);
        return task;
    }

    public Epic createWithoutSaveEpic (Epic epic) {
        super.createEpic(epic);
        return epic;
    }

    public Task createWithoutSaveSubtask (Subtask subtask) {
        super.createSubtask(subtask);
        return subtask;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        return subtask;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic,duration,startTime,endTime");
            writer.newLine();
            for (Map.Entry<Integer, Task> entry : getAllTasks().entrySet()) {
                final Task task = entry.getValue();
                writer.write(taskToString(task));
                writer.newLine();
            }
            for (Map.Entry<Integer, Epic> entry : getAllEpics().entrySet()) {
                final Epic epic = entry.getValue();
                writer.write(taskToString(epic));
                writer.newLine();
            }
            for (Map.Entry<Integer, Subtask> entry : getAllSubtasks().entrySet()) {
                final Subtask subtask = entry.getValue();
                writer.write(taskToString(subtask));
                writer.newLine();
            }
            writer.newLine();
            writer.write(historyToString(getHistoryManager()));
            writer.newLine();
        } catch (IOException e) {
            throw new ManagerSaveException("Can't save to file: " + file.getName(), e);
        }
    }

    public static String taskToString(Task task) {
        String result;
        result = task.getId() + "," + task.typeToString() + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + "," + (task instanceof Subtask ? ((Subtask) task).getEpic().getId() : "no epic") + "," + task.getDuration() + "," + task.getStartTime() + "," + task.getEndTime();
        return result;
    }

    public static Task taskFromString(String[] value, TaskManager taskManager) {
        if (TaskType.valueOf(value[1]).equals(TaskType.TASK)) {
            Task task = new Task(Integer.parseInt(value[0]), value[2], Status.valueOf(value[3]), value[4], Duration.parse(value[6]), LocalDateTime.parse(value[7]), LocalDateTime.parse(value[8]));
            return task;
        }
        if (TaskType.valueOf(value[1]).equals(TaskType.SUBTASK)) {
            Subtask subtask = new Subtask(Integer.parseInt(value[0]), value[2], Status.valueOf(value[3]), value[4], taskManager.getAllEpics().get(Integer.parseInt(value[5])), Duration.parse(value[6]), LocalDateTime.parse(value[7]), LocalDateTime.parse(value[8]));
            return subtask;
        }
        if (TaskType.valueOf(value[1]).equals(TaskType.EPIC)) {
            Epic epic = new Epic(Integer.parseInt(value[0]), value[2], Status.valueOf(value[3]), value[4], Duration.parse(value[6]), LocalDateTime.parse(value[7]), LocalDateTime.parse(value[8]));
            return epic;
        }
        return null;
    }

    public static String historyToString(HistoryManager manager) {
        final List<Task> history = manager.getHistory();
        if (history.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(history.get(0).getId());
        for (int i = 1; i < history.size(); i++) {
            Task task = history.get(i);
            sb.append(",");
            sb.append(task.getId());
        }
        return sb.toString();
    }

    public static List<Integer> historyFromString(String value) throws IOException {
        String[] values = value.split(",");
        List<Integer> history = new ArrayList<>(values.length);
        for (int i = 0; i < values.length; i++) {
            history.add(Integer.parseInt(values[i]));
        }
        return history;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fb = new FileBackedTasksManager(file);
        try (BufferedReader br = new BufferedReader(new FileReader("history.csv"))) {
            br.readLine();
            while (br.ready()) {
                String[] values = br.readLine().split(",");
                if (values.length > 1) {
                    if (values[1].equals(TaskType.TASK.toString())) {
                        fb.createWithoutSaveTask(FileBackedTasksManager.taskFromString(values, fb));
                    }
                    if (values[1].equals(TaskType.EPIC.toString())) {
                        fb.createWithoutSaveEpic((Epic) FileBackedTasksManager.taskFromString(values, fb));
                    }
                    if (values[1].equals(TaskType.SUBTASK.toString())) {
                        fb.createWithoutSaveSubtask((Subtask) FileBackedTasksManager.taskFromString(values, fb));
                    }
                }
                if (values.length == 1) {
                    String idString = br.readLine();
                    List<Integer> idList = historyFromString(idString);
                    for (int i = 0; i < idList.size(); i++) {
                        if (fb.getAllTasks().containsKey(idList.get(i))) {
                            fb.getTaskById(idList.get(i));
                        }
                        if (fb.getAllSubtasks().containsKey(idList.get(i))) {
                            fb.getSubtaskById(idList.get(i));
                        }
                        if (fb.getAllEpics().containsKey(idList.get(i))) {
                            fb.getEpicById(idList.get(i));
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Can't read form file: " + file.getName(), e);
        }
        return fb;
    }

    public static void main(String[] args) {
        File file = new File("history.csv");
        FileBackedTasksManager fb1 = new FileBackedTasksManager(file);
        Task task = new Task("Task 1", "description Task 1", Duration.ofMinutes(30));
        Epic epic = new Epic("Epic 1", "description Epic 1", Duration.ofMinutes(0));
        Subtask subtask = new Subtask("Subtask 1", "description Subtask 1", Duration.ofMinutes(15), epic);
        fb1.createTask(task);
        fb1.createEpic(epic);
        fb1.createSubtask(subtask);
        System.out.println("Check 0: " + fb1.getAllTasks() + " / " + fb1.getAllEpics() + " / " + fb1.getAllSubtasks());
        fb1.getTaskById(1);
        fb1.getEpicById(2);
        System.out.println("Check 1: ");
        printAllTasks(fb1);
        System.out.println("Check 2: " + fb1.historyToString(fb1.getHistoryManager()));
        System.out.println("Check 3: " + fb1.getHistoryManager().getTaskHashMap().toString());
       /* fb1.setCounter(1);*/
        FileBackedTasksManager fb2 = loadFromFile(file);
        System.out.println(fb2.getHistoryManager().getTaskHashMap());
        System.out.println("Check 4: ");
        printAllTasks(fb2);
    }

    public static void printAllTasks(FileBackedTasksManager fb) {
        for (Integer key : fb.getAllTasks().keySet()) {
            System.out.println(fb.getAllTasks().get(key).toString());
        }
        for (Integer key : fb.getAllEpics().keySet()) {
            System.out.println(fb.getAllEpics().get(key).toString());
        }
        for (Integer key : fb.getAllSubtasks().keySet()) {
            System.out.println(fb.getAllSubtasks().get(key).toString());
        }

    }
}
