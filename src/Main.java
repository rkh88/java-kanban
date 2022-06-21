import util.Epic;
import util.Status;
import util.Subtask;
import util.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task();
        Task task2 = new Task();
        taskManager.createTask(task1, "Заняться домашними делами", "описание");
        taskManager.createTask(task2, "Погулять", "описание");
        Epic epic1 = new Epic();
        taskManager.createEpic(epic1,"Сделать покупки", "описание");
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        taskManager.createSubtask(subtask1,"Купить помидоры", "описание", Status.NEW, epic1);
        taskManager.createSubtask(subtask2,"Купить огурцы", "описание", Status.NEW, epic1);
        Epic epic2 = new Epic();
        taskManager.createEpic(epic2,"Позвонить другу", "описание");
        Subtask subtask3 = new Subtask();
        taskManager.createSubtask(subtask3,"Набрать номер", "описание", Status.NEW, epic2);
        System.out.println(taskManager.allTasks);
        System.out.println(taskManager.allEpics);
        System.out.println(taskManager.allSubtasks);
        taskManager.getSubtaskById(4).setStatus(Status.DONE);
        taskManager.getSubtaskById(5).setStatus(Status.DONE);
        System.out.println(taskManager.allEpics);
        System.out.println(taskManager.allSubtasks);
        Task task = new Task();
        taskManager.createTask(task, "Побегать", "описание");
        taskManager.updateTask(task);
        System.out.println(taskManager.allTasks);
        taskManager.deleteTaskById(1);
        System.out.println(taskManager.allTasks);

    }
}
