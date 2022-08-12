package service;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {

    File file;

    List<Integer> idCsvList = new ArrayList<>(); //это поле нужно для того, чтобы при вызове save не добавлялись по новой повторки

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

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

    public void save() throws ManagerSaveException {

        try (FileWriter fw = new FileWriter("history.csv")) {
            StringBuilder sb = new StringBuilder();
            for(Integer taskId : super.getAllTasks().keySet()) {
                if(!idCsvList.contains(taskId)) {
                    idCsvList.add(taskId);
                    Task task = super.getAllTasks().get(taskId);
                    sb.append(taskToString(task));
                    sb.append('\n');
                    System.out.println("Task was added to CSV file");
                }
            }
            sb.append('\n');
            for(Integer key : super.getHistoryManager().getTaskHashMap().keySet()) { // идем по всей истории просмотров
                Task taskViewed = super.getHistoryManager().getTaskHashMap().get(key).getData();//получаем из истории просмотренный таск
                sb.append(taskViewed.getId()); //добавляем айдишник просмотренного таска в стрингбилдер
                fw.write(sb.toString());//записываем стрингбилдер в файл
                System.out.println("History added to CSV file");
            }
            fw.write(sb.toString());
            fw.flush();
            fw.close();
            for (Integer taskId : idCsvList) {
                System.out.println("idCsvList " + taskId);
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Can't save to file: " + file.getName(), e);
        }
    }

    public static String taskToString(Task task) {
        String result = task.getId() + "," + task.typeToString() + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription();
        return result;
    }

    public Task taskFromString(String value) throws IOException {//Нужно ли здесь разделять на три случая?
        String[] taskInfo = value.split(",");
        if(TaskType.valueOf(taskInfo[1]).equals(TaskType.TASK)) {
        Task task = new Task(Integer.parseInt(taskInfo[0]), taskInfo[2], Status.valueOf(taskInfo[3]), taskInfo[4]);
        return task;
        }
        if(TaskType.valueOf(taskInfo[1]).equals(TaskType.SUBTASK)) {
            Subtask subtask = new Subtask(Integer.parseInt(taskInfo[0]), taskInfo[2], Status.valueOf(taskInfo[3]), taskInfo[4]);
            return subtask;
        }

        if(TaskType.valueOf(taskInfo[1]).equals(TaskType.EPIC)) {
            Epic epic = new Epic(Integer.parseInt(taskInfo[0]), taskInfo[2], Status.valueOf(taskInfo[3]), taskInfo[4]);
            return epic;
        }

        if(TaskType.valueOf(taskInfo[0]).equals(TaskType.HISTORY)) {
            String[] values = value.split(",");
            List<Integer> idList = new ArrayList<>(historyFromString(value));

            for (Integer id : idList) {
                super.createTask(getHistoryManager().getTaskHashMap().get(id).getData());
            }

            //надо считать всю строку истории и добавить в менеджер из мапы хистори менеджера таски, по которыми считаны айдишники
        }

        System.out.println("File is empty, nothing to return");
        return null;
    }

    public String historyToString(HistoryManager manager) {//Правильно ли здесь применение стринг буффера?
       StringBuffer historyBuffer = new StringBuffer();
        for(Integer taskId : manager.getTaskHashMap().keySet()) {
            Task task = manager.getTaskHashMap().get(taskId).getData();
            historyBuffer.append(task.getId() + "," + task.typeToString() + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription());
            historyBuffer.append("\n");
        }
        return historyBuffer.toString();
    }

    public static List<Integer> historyFromString(String value) throws IOException {
        String[] values = value.split(",");
        List<Integer> history = new ArrayList<>(values.length);
        for (int i = 0; i < values.length; i++) {
            history.add(Integer.parseInt(values[i]));
        }
        return history;
    }

    public static FileBackedTasksManager loadFromFile(File file) throws IOException {
        FileBackedTasksManager fb = new FileBackedTasksManager(file);
        try (BufferedReader br = new BufferedReader(new FileReader("history.csv"))) {
            while (br.ready()) {
                Task task = taskFromString(br.readLine());
                fb.createTask(task);//вот эта строчка вроде и означает заполнение taskManager и historyManager. Чем плох мой метод?
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
        System.out.println("Check 1: " + printAllTasks(fb1));
        System.out.println("Check 2: " + fb1.historyToString(fb1.getHistoryManager()));

        FileBackedTasksManager fb2 = loadFromFile(file);
        System.out.println("Check 3: " + printAllTasks(fb2));
        /*fb.getTaskById(task.getId()); //На данный момент вот здесь получаю NPE. Он ловит его на методе add, который находится в InMemoryHistoryManager. Не пойму, как заставить работать здесь вызов по айди
        */

    }

    public static boolean printAllTasks (FileBackedTasksManager fb) {
        for(Integer key : fb.getAllTasks().keySet()) {
            System.out.println(fb.getAllTasks().get(key).toString());
        }
        return true;
    }

}
