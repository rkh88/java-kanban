package service;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        Managers managers1 = new Managers(inMemoryTaskManager, inMemoryHistoryManager);
        Task task1 = new Task("Заняться домашними делами", "описание");
        Task task2 = new Task("Погулять", "описание");
        managers1.inMemoryTaskManager.createTask(task1);
        managers1.inMemoryTaskManager.createTask(task2);
        Epic epic1 = new Epic("Сделать покупки", "описание");
        managers1.inMemoryTaskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Купить помидоры", "описание", epic1);
        Subtask subtask2 = new Subtask("Купить огурцы", "описание", epic1);
        managers1.inMemoryTaskManager.createSubtask(subtask1);
        managers1.inMemoryTaskManager.createSubtask(subtask2);
        Epic epic2 = new Epic("Позвонить другу", "описание");
        managers1.inMemoryTaskManager.createEpic(epic2);
        Subtask subtask3 = new Subtask("Набрать номер", "описание", epic2);
        managers1.inMemoryTaskManager.createSubtask(subtask3);
        System.out.println("Check 1: " + inMemoryTaskManager.getAllTasks());
        System.out.println("Check 2: " + inMemoryTaskManager.getAllEpics());
        System.out.println("Check 3: " + inMemoryTaskManager.getAllSubtasks());
        managers1.inMemoryTaskManager.getTaskById(1);
        managers1.inMemoryTaskManager.getTaskById(2);
        managers1.inMemoryTaskManager.getEpicById(3);
        managers1.inMemoryHistoryManager.printTaskHistory();
        managers1.inMemoryTaskManager.setSubtaskStatus(managers1.inMemoryTaskManager.getSubtaskById(4), Status.DONE);
        managers1.inMemoryTaskManager.setSubtaskStatus(managers1.inMemoryTaskManager.getSubtaskById(5), Status.DONE);
        System.out.println("Check 4: " +  managers1.inMemoryTaskManager.getAllEpics());
        System.out.println("Check 5: " +  managers1.inMemoryTaskManager.getAllSubtasks());
        Task task = new Task("Побегать", "описание");
        managers1.inMemoryTaskManager.createTask(task);
        managers1.inMemoryTaskManager.updateTask(task);
        System.out.println("Check 6: " +  managers1.inMemoryTaskManager.getAllTasks());
        managers1.inMemoryTaskManager.deleteTaskById(1);
        System.out.println("Check 7: " +  managers1.inMemoryTaskManager.getAllTasks());
        managers1.inMemoryHistoryManager.printTaskHistory();


    }
}
