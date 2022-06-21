package tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    protected ArrayList<Subtask> subtasks;

    public Epic() {

    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public void addSubtask(Subtask subtask) {
        this.subtasks.add(subtask);
    }

    public void checkEpicStatus () {
        int epicDone = 0;
        int epicNew = 0;
        for(Subtask currentSubtask : this.subtasks) {
            if(currentSubtask.status == Status.DONE) {
                epicDone++;
            }
        }
        for(Subtask currentSubtask : this.subtasks) {
            if(currentSubtask.status == Status.NEW) {
                epicNew++;
            }
        }
        if (epicDone == this.subtasks.size() || epicNew == this.subtasks.size() || this.subtasks.size() == 0){
            if (epicDone == this.subtasks.size()) {
                this.setStatus(Status.DONE);
            }
            if (epicNew == this.subtasks.size() || this.subtasks.size() == 0) {
                this.setStatus(Status.NEW);
            }
        } else {
            this.setStatus(Status.IN_PROGRESS);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasks, epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }
}
