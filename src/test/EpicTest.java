package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;

import java.time.Duration;



class EpicTest {

    protected static TaskManager tm = Managers.getDefault();

    @BeforeEach

    public void beforeEach() {
        tm.getAllEpics().clear();
        tm.getAllSubtasks().clear();
        tm.setCounter(1);
        Epic epic1 = new Epic("Epic 1", "description Epic 1", Duration.ofMinutes(0));
        tm.createEpic(epic1);
        Subtask subtask1 = new Subtask("Test Subtask 1", "Test description", Duration.ofMinutes(30), epic1);
        tm.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Test Subtask 2", "Test description", Duration.ofMinutes(30), epic1);
        tm.createSubtask(subtask2);
        Subtask subtask3 = new Subtask("Test Subtask 3", "Test description", Duration.ofMinutes(30), epic1);
        tm.createSubtask(subtask3);
    }

    @Test

    public void emptySubtasksListStatusCheck() {
        tm.getAllSubtasks().clear();
        Assertions.assertEquals("NEW", tm.getAllEpics().get(1).getStatus().toString());
    }

    @Test

    public void allSubtasksAreNewStatusCheck() {
        Assertions.assertEquals(Status.NEW, tm.getAllEpics().get(1).getStatus());
    }

    @Test

    public void allSubtasksAreDoneStatusCheck() {
        for(Integer key : tm.getAllSubtasks().keySet()) {
            tm.getAllSubtasks().get(key).setStatus(Status.DONE);
        }
        tm.checkEpicStatus(tm.getEpicById(1));
        Assertions.assertEquals(Status.DONE, tm.getAllEpics().get(1).getStatus());
    }

    @Test

    public void someSubtasksAreDoneSomeNewStatusCheck() {
        tm.getAllSubtasks().get(2).setStatus(Status.DONE);
        tm.checkEpicStatus(tm.getEpicById(1));
        Assertions.assertEquals(Status.IN_PROGRESS, tm.getAllEpics().get(1).getStatus());
    }

    @Test

    public void allSubtasksAreInProgressStatusCheck() {
        for(Integer key : tm.getAllSubtasks().keySet()) {
            tm.getAllSubtasks().get(key).setStatus(Status.IN_PROGRESS);
        }
        tm.checkEpicStatus(tm.getEpicById(1));
        Assertions.assertEquals(Status.IN_PROGRESS, tm.getAllEpics().get(1).getStatus());
    }


}


