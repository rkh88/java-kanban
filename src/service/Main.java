package service;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        Task task1 = new Task("Заняться домашними делами", "описание");
        Task task2 = new Task("Погулять", "описание");
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        Epic epic1 = new Epic("Сделать покупки", "описание");
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Купить помидоры", "описание", epic1);
        Subtask subtask2 = new Subtask("Купить огурцы", "описание", epic1);
        inMemoryTaskManager.createSubtask(subtask1);
        inMemoryTaskManager.createSubtask(subtask2);
        Epic epic2 = new Epic("Позвонить другу", "описание");
        inMemoryTaskManager.createEpic(epic2);
        Subtask subtask3 = new Subtask("Набрать номер", "описание", epic2);
        inMemoryTaskManager.createSubtask(subtask3);
        System.out.println("Check 1: " + inMemoryTaskManager.getAllTasks());
        System.out.println("Check 2: " + inMemoryTaskManager.getAllEpics());
        System.out.println("Check 3: " + inMemoryTaskManager.getAllSubtasks());
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getTaskById(2);
        inMemoryTaskManager.getEpicById(3);
        inMemoryTaskManager.printTaskHistory();
        inMemoryTaskManager.setSubtaskStatus(inMemoryTaskManager.getSubtaskById(4), Status.DONE);
        inMemoryTaskManager.setSubtaskStatus(inMemoryTaskManager.getSubtaskById(5), Status.DONE);
        System.out.println("Check 4: " + inMemoryTaskManager.getAllEpics());
        System.out.println("Check 5: " + inMemoryTaskManager.getAllSubtasks());
        Task task = new Task("Побегать", "описание");
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.updateTask(task);
        System.out.println("Check 6: " + inMemoryTaskManager.getAllTasks());
        inMemoryTaskManager.deleteTaskById(1);
        System.out.println("Check 7: " + inMemoryTaskManager.getAllTasks());
        inMemoryTaskManager.printTaskHistory();


    }
}
