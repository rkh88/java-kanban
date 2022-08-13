package service;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {

    protected File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    @Override
    public Task createTask(Task task){
        super.createTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
        return task;
    }

    @Override
    public Epic createEpic(Epic epic){
        super.createTask(epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask){
        super.createTask(subtask);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
        return subtask;
    }

    @Override
    public Task getTaskById(int id){
        Task task = null;
        task = super.getTaskById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
        return task;
    }

    @Override
    public Subtask getSubtaskById(int id){
        Subtask subtask = super.getSubtaskById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
        return subtask;
    }

    @Override
    public Epic getEpicById(int id){
        Epic epic = super.getEpicById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
        return epic;
    }

    @Override
    public void deleteTaskById(int id){
        try {
            super.deleteTaskById(id);
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
        try {
            save();
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteSubtaskById(int id){
        try {
            super.deleteSubtaskById(id);
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
        try {
            save();
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteEpicById(int id){
        try {
            super.deleteEpicById(id);
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
        try {
            save();
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
    }

    public void save() throws ManagerSaveException {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic");
            writer.newLine();
            for (Map.Entry<Integer, Task> entry : getAllTasks().entrySet()) {
                final Task task = entry.getValue();
                writer.write(taskToString(task));
                writer.newLine();
            }
            for (Map.Entry<Integer, Subtask> entry : getAllSubtasks().entrySet()) {
                final Subtask subtask = entry.getValue();
                writer.write(subtaskToString(subtask));
                writer.newLine();
            }
            for (Map.Entry<Integer, Epic> entry : getAllEpics().entrySet()) {
                final Epic epic = entry.getValue();
                writer.write(taskToString(epic));
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
        if(task.getClass().equals(Task.class) || task.getClass().equals(Epic.class)) {
            String result = task.getId() + "," + task.typeToString() + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription();
            return result;
        }
        if(task.getClass().equals(Subtask.class)) {
            String result = task.getId() + "," + task.typeToString() + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + ((Subtask) task).getEpic().getId();
            return result;
        }
        return "No task";
    }

    public static String subtaskToString(Subtask subtask) {
        String result = subtask.getId() + "," + subtask.typeToString() + "," + subtask.getName() + "," + subtask.getStatus() + "," + subtask.getDescription() + subtask.getEpic().getId();
        return result;
    }

    public Task taskFromString(String[] value) throws IOException {
        if(TaskType.valueOf(value[1]).equals(TaskType.TASK)) {
        Task task = new Task(Integer.parseInt(value[0]), value[2], Status.valueOf(value[3]), value[4]);
        return task;
        }
        if(TaskType.valueOf(value[1]).equals(TaskType.SUBTASK)) {
            Subtask subtask = new Subtask(Integer.parseInt(value[0]), value[2], Status.valueOf(value[3]), value[4], super.getAllEpics().get(value[5]));
            return subtask;
        }

        if(TaskType.valueOf(value[1]).equals(TaskType.EPIC)) {
            Epic epic = new Epic(Integer.parseInt(value[0]), value[2], Status.valueOf(value[3]), value[4]);
            return epic;
        }

        System.out.println("File is empty, nothing to return");
        return null;
    }

    public String historyToString(HistoryManager manager) {

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

    public FileBackedTasksManager loadFromFile(File file) throws IOException {
        FileBackedTasksManager fb = new FileBackedTasksManager(file);
        try (BufferedReader br = new BufferedReader(new FileReader("history.csv"))) {
            br.readLine();
            while (br.ready()) {
                String[] values = br.readLine().split(",");
                if(values.length > 1) {

                    if(TaskType.valueOf(values[1]).equals(TaskType.TASK)) {
                        fb.createTask(taskFromString(values));
                    }
                    if(TaskType.valueOf(values[1]).equals(TaskType.EPIC)) {
                        fb.createEpic((Epic) taskFromString(values));
                    }
                    if(TaskType.valueOf(values[1]).equals(TaskType.SUBTASK)) {
                        fb.createSubtask((Subtask) taskFromString(values));
                    }
                    System.out.println("values[0] : " + values[0] + " values[1] : " + values[1] + " values[2] : " + values[2]);
                    System.out.println(values.length);
                }

                if(values.length == 1) {
                    System.out.println(values[0]);
                    String idString = br.readLine();
                    System.out.println("idString: " + idString);
                    List<Integer> idList = historyFromString(idString);
                    for (int i = 0; i < idList.size(); i++) {
                        if(super.getAllTasks().containsKey(idList.get(i))) {
                            super.getTaskById(idList.get(i));
                        }
                        if(super.getAllSubtasks().containsKey(idList.get(i))) {
                            super.getSubtaskById(idList.get(i));
                        }
                        if(super.getAllEpics().containsKey(idList.get(i))) {
                            super.getEpicById(idList.get(i));
                        }
                    }
                }
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Can't read form file: " + file.getName(), e);
        }
        return fb;
    }



    public static void main(String[] args) throws IOException {
        File file = new File("history.csv");
        FileBackedTasksManager fb1 = new FileBackedTasksManager(file);
        Task task = new Task("Task 1", "description Task 1");
        Epic epic = new Epic("Epic 1", "description Epic 1");
        Subtask subtask = new Subtask("Subtask 1", "description Subtask 1", epic);
        fb1.createTask(task);
        fb1.createEpic(epic);
        fb1.createSubtask(subtask);
        fb1.getTaskById(1);
        fb1.getTaskById(2);
        System.out.println("Check 1: ");
        printAllTasks(fb1);
        System.out.println("Check 2: " + fb1.historyToString(fb1.getHistoryManager()));
        System.out.println("Check 3: " + fb1.getHistoryManager().getTaskHashMap().toString());

        FileBackedTasksManager fb2 = new FileBackedTasksManager(file);
        fb2.loadFromFile(file);
        System.out.println(fb2.getAllTasks());//почему-то не создаются таски при подгрузке loadFromFile. Не пойму, почему
        System.out.println(fb2.getHistoryManager().getTaskHashMap());
        System.out.println("Check 4: ");
        printAllTasks(fb2);


    }

    public static void printAllTasks (FileBackedTasksManager fb) {
        for(Integer key : fb.getAllTasks().keySet()) {
            System.out.println(fb.getAllTasks().get(key).toString());
        }
    }

}
