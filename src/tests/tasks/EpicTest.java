package tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.time.Duration;



class EpicTest {

    protected TaskManager tm;

    @BeforeAll

    public void beforeAll() {
        tm = Managers.getDefault();
    }

    @Test

    public void emptySubtasksListStatusCheck() {
        Epic epic1 = new Epic("Epic 1", "description Epic 1", Duration.ofMinutes(0));
        tm.createEpic(epic1);
        Assertions.assertEquals("NEW", epic1.getStatus().toString());
    }

    @Test

    public void allSubtasksAreNewStatusCheck() {
        tm.deleteAllEpics();
        tm.setCounter(1);
        Epic epic1 = new Epic("Epic 1", "description Epic 1", Duration.ofMinutes(0));
        tm.createEpic(epic1);
        Subtask subtask1 = new Subtask("Test Subtask 1", "Test description", Duration.ofMinutes(30), epic1);
        tm.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Test Subtask 2", "Test description", Duration.ofMinutes(30), epic1);
        tm.createSubtask(subtask2);
        Subtask subtask3 = new Subtask("Test Subtask 3", "Test description", Duration.ofMinutes(30), epic1);
        tm.createSubtask(subtask3);
        Assertions.assertEquals("NEW", epic1.getStatus().toString());
    }

    @Test

    public void allSubtasksAreDoneStatusCheck() {
        for(Integer key : tm.getAllSubtasks().keySet()) {
            tm.getAllSubtasks().get(key).setStatus(Status.DONE);
        }
        tm.checkEpicStatus(tm.getEpicById(1));
        Assertions.assertEquals("DONE", tm.getAllEpics().get(0).getStatus().toString());
    }

    @Test

    public void someSubtasksAreDoneSomeNewStatusCheck() {
        tm.getAllSubtasks().get(1).setStatus(Status.DONE);
        tm.checkEpicStatus(tm.getEpicById(1));
        Assertions.assertEquals("IN_PROGRESS", tm.getAllEpics().get(0).getStatus().toString());
    }

    @Test

    public void allSubtasksAreInProgressStatusCheck() {
        for(Integer key : tm.getAllSubtasks().keySet()) {
            tm.getAllSubtasks().get(key).setStatus(Status.IN_PROGRESS);
        }
        tm.checkEpicStatus(tm.getEpicById(1));
        Assertions.assertEquals("IN_PROGRESS", tm.getAllEpics().get(0).getStatus().toString());
    }


}

