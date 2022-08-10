package service;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    @Override
    public Task createTask(Task task) throws ManagerSaveException {
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) throws ManagerSaveException {
        super.createTask(epic);
        save();
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) throws ManagerSaveException {
        super.createTask(subtask);
        save();
        return subtask;
    }

    @Override
    public Task getTaskById(int id) throws ManagerSaveException {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Subtask getSubtaskById(int id) throws ManagerSaveException {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public Epic getEpicById(int id) throws ManagerSaveException {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public void deleteTaskById(int id) throws ManagerSaveException {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) throws ManagerSaveException {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) throws ManagerSaveException {
        super.deleteEpicById(id);
        save();
    }

    public class ManagerSaveException extends IOException {

        public ManagerSaveException(String message) {
            super(message);
        }
    }

    public void save() throws ManagerSaveException {
        try (FileOutputStream fos = new FileOutputStream("history.csv")) {
            OutputStreamWriter oWriter = new OutputStreamWriter (fos, Charset.forName (String.valueOf(StandardCharsets.UTF_8)));
            BufferedWriter bw = new BufferedWriter(oWriter);
            for(Integer taskId : super.getAllTasks().keySet()) {
                Task task = super.getAllTasks().get(taskId);
                bw.newLine();
                bw.write(toString(task));
            }
            for(Integer subtaskId : super.getAllSubtasks().keySet()) {
                Subtask subtask = super.getAllSubtasks().get(subtaskId);
                bw.newLine();
                bw.write(toString(subtask));
            }
            for(Integer epicId : super.getAllEpics().keySet()) {
                Epic epic = super.getAllEpics().get(epicId);
                bw.newLine();
                bw.write(toString(epic));
            }
           /* fos.close();
            oWriter.close();
            bw.close();*/
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toString(Task task) {
        String result = String.format(task.getId() + "," + task.typeToString() + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription());
        return result;
    }

    public Task fromString(String value) {
        String[] taskInfo = value.split(",");
        Task task = new Task(Integer.parseInt(taskInfo[0]), taskInfo[2], Status.valueOf(taskInfo[3]), taskInfo[4]);
        return task;
    }

    public String historyToString(HistoryManager manager) {
       StringBuffer historyBuffer = new StringBuffer();
        for(Integer taskId : manager.getTaskHashMap().keySet()) {
            Task task = manager.getTaskHashMap().get(taskId).getData();
            historyBuffer.append("\n" + task.getId() + "," + task.typeToString() + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription());
        }
        return historyBuffer.toString();
    }

    public List<Integer> historyFromString(String value) throws IOException {
        List<Integer> history = new ArrayList<>();
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("history.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.add(Arrays.asList(values));
            }
        }
        for(List<String> record : records) {
            history.add(Integer.parseInt(record.get(0)));
        }
        return history;
    }

    public FileBackedTasksManager loadFromFile(File file) throws IOException {// Я не могу понять, как писать этот метод. Прошу дать каки-то комментарии
        FileBackedTasksManager fb = new FileBackedTasksManager();
        try (BufferedReader br = new BufferedReader(new FileReader("history.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                Task task = fromString(line);
                fb.createTask(task);
            }
        }
        return fb;
    }



    public static void main(String[] args) throws ManagerSaveException {// Здесь я не могу понять, как написать требуемую проверку
        FileBackedTasksManager fb = new FileBackedTasksManager();
        Task task = new Task("Task 1", "description Task 1");
        Epic epic = new Epic("Epic 1", "description Epic 1");
        Subtask subtask = new Subtask("Subtask 1", "description Subtask 1", epic);
        fb.createTask(task);
        fb.createEpic(epic);
        fb.createSubtask(subtask);
        fb.getTaskById(task.getId()); //На данный момент вот здесь получаю NPE
        fb.getEpicById(epic.getId());
        fb.getSubtaskById(subtask.getId());
        System.out.println("Check 1: " + printAllTasks(fb));
        System.out.println(fb.historyToString(fb.getHistoryManager()));

    }

    public static boolean printAllTasks (FileBackedTasksManager fb) {
        for(Integer key : fb.getAllTasks().keySet()) {
            System.out.println(fb.getAllTasks().get(key).toString());
        }
        return true;
    }

}
