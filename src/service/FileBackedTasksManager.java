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
            String result = task.getId() + "," + task.typeToString() + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription();
            return result;
    }

    public static String subtaskToString(Subtask subtask) {
        String result = subtask.getId() + "," + subtask.typeToString() + "," + subtask.getName() + "," + subtask.getStatus() + "," + subtask.getDescription() + subtask.getEpic().getId();
        return result;
    }

    public Task taskFromString(String value) throws IOException {
        String[] taskInfo = value.split(",");
        if(TaskType.valueOf(taskInfo[1]).equals(TaskType.TASK)) {
        Task task = new Task(Integer.parseInt(taskInfo[0]), taskInfo[2], Status.valueOf(taskInfo[3]), taskInfo[4]);
        return task;
        }
        if(TaskType.valueOf(taskInfo[1]).equals(TaskType.SUBTASK)) {
            Subtask subtask = new Subtask(Integer.parseInt(taskInfo[0]), taskInfo[2], Status.valueOf(taskInfo[3]), taskInfo[4], super.getAllEpics().get(taskInfo[5]));
            return subtask;
        }

        if(TaskType.valueOf(taskInfo[1]).equals(TaskType.EPIC)) {
            Epic epic = new Epic(Integer.parseInt(taskInfo[0]), taskInfo[2], Status.valueOf(taskInfo[3]), taskInfo[4]);
            return epic;
        }

        if(!TaskType.valueOf(taskInfo[1]).equals(TaskType.TASK) && !TaskType.valueOf(taskInfo[1]).equals(TaskType.SUBTASK) && !TaskType.valueOf(taskInfo[1]).equals(TaskType.EPIC) && !taskInfo[0].isEmpty()) {

            for (int i = 0; i < taskInfo.length; i++) {
                if(super.getAllTasks().containsKey(Integer.parseInt(taskInfo[i]))) {
                    super.getTaskById(Integer.parseInt(taskInfo[i]));
                }
                if(super.getAllSubtasks().containsKey(Integer.parseInt(taskInfo[i]))) {
                    super.getSubtaskById(Integer.parseInt(taskInfo[i]));
                }
                if(super.getAllEpics().containsKey(Integer.parseInt(taskInfo[i]))) {
                    super.getEpicById(Integer.parseInt(taskInfo[i]));
                }
            }

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
            while (br.ready()) {
                String[] values = br.readLine().split(",");
                if(TaskType.valueOf(values[1]).equals(TaskType.TASK) || TaskType.valueOf(values[1]).equals(TaskType.SUBTASK) || TaskType.valueOf(values[1]).equals(TaskType.EPIC)) {// Я вот тут получаю IllegalArgumentExpression из=за проблем со сравнением TaskType, как я понимаю
                    Task task = taskFromString(br.readLine());
                    fb.createTask(task);
                }
                if(values.length == 0) {
                    br.readLine();
                    String[] idList = br.readLine().split(",");
                    for (int i = 0; i < idList.length; i++) {
                        historyFromString(idList[i]);
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
        fb1.getTaskById(task.getId(1));
        fb1.getTaskById(task.getId(2));
        System.out.println("Check 1: " + printAllTasks(fb1));
        System.out.println("Check 2: " + fb1.historyToString(fb1.getHistoryManager()));
        System.out.println("Check 3: " + fb1.getHistoryManager().getTaskHashMap().toString());

        FileBackedTasksManager fb2 = new FileBackedTasksManager(file);
        fb2.loadFromFile(file);
        System.out.println("Check 4: " + printAllTasks(fb2));
        fb1.getTaskById(task.getId());


    }

    public static boolean printAllTasks (FileBackedTasksManager fb) {
        for(Integer key : fb.getAllTasks().keySet()) {
            System.out.println(fb.getAllTasks().get(key).toString());
        }
        return true;
    }

}
