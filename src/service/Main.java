package service;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task("Заняться домашними делами", "описание");
        Task task2 = new Task("Погулять", "описание");
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        Epic epic1 = new Epic("Сделать покупки", "описание");
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Купить помидоры", "описание", epic1);
        Subtask subtask2 = new Subtask("Купить огурцы", "описание", epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        Epic epic2 = new Epic("Позвонить другу", "описание");
        taskManager.createEpic(epic2);
        Subtask subtask3 = new Subtask("Набрать номер", "описание", epic2);
        taskManager.createSubtask(subtask3);
        System.out.println("Check 1: " + taskManager.getAllTasks());
        System.out.println("Check 2: " + taskManager.getAllEpics());
        System.out.println("Check 3: " + taskManager.getAllSubtasks());
        taskManager.deleteEpicById(3);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getHistoryManager().printTaskHistory();
        taskManager.setSubtaskStatus(taskManager.getSubtaskById(4), Status.DONE);
        taskManager.setSubtaskStatus(taskManager.getSubtaskById(5), Status.DONE);
        System.out.println("Check 4: " + taskManager.getAllEpics());
        System.out.println("Check 5: " + taskManager.getAllSubtasks());
        Task task = new Task("Побегать", "описание");
        taskManager.createTask(task);
        taskManager.updateTask(task);
        System.out.println("Check 6: " + taskManager.getAllTasks());
        taskManager.deleteTaskById(1);
        System.out.println("Check 7: " + taskManager.getAllTasks());
       /* taskManager.deleteEpicById(3);
        System.out.println("Check 8.1: " + taskManager.getAllEpics());
        System.out.println("Check 8.2: " + taskManager.getAllSubtasks());
        System.out.println("Check 8.3: ");
        taskManager.getHistoryManager().printTaskHistory();
        System.out.println("Check 8.4: ");
        taskManager.getHistoryManager().printTaskHashMap();*/
        taskManager.getTaskById(2);
        taskManager.getHistoryManager().printTaskHistory();
    }
}
