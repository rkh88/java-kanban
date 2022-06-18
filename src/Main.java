public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = taskManager.createTask("Заняться домашними делами", "описание");
        Task task2 = taskManager.createTask("Погулять", "описание");
        Epic epic1 = taskManager.createEpic("Сделать покупки", "описание");
        taskManager.createSubtask("Купить помидоры", "описание", Status.NEW, epic1 );
        taskManager.createSubtask("Купить огурцы", "описание", Status.NEW, epic1 );
        Epic epic2 = taskManager.createEpic("Позвонить другу", "описание");
        taskManager.createSubtask("Набрать номер", "описание", Status.NEW, epic2 );
        System.out.println(taskManager.allTasks);
        System.out.println(taskManager.allEpics);
        System.out.println(taskManager.allSubtasks);
        taskManager.getSubtaskById(4).setStatus(Status.DONE);
        taskManager.getSubtaskById(5).setStatus(Status.DONE);
        System.out.println(taskManager.allEpics);
        System.out.println(taskManager.allSubtasks);
        Task task = new Task("Побегать", "описание", 2);
        taskManager.updateTask(task);
        System.out.println(taskManager.allTasks);
        taskManager.deleteTaskById(1);
        System.out.println(taskManager.allTasks);

    }
}
