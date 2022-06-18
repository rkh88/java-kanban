import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Subtask> subtasks;

    public Epic(String name, String description, int id) {
        super(name, description, id);
        this.subtasks = new ArrayList<>();
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
        boolean epicDone = false;
        boolean epicNew = false;
        for(Subtask currentSubtask : this.subtasks) {
            if(currentSubtask.status == Status.DONE) {
                epicDone = true;
            }
        }
        for(Subtask currentSubtask : this.subtasks) {
            if(currentSubtask.status == Status.NEW) {
                epicNew = true;
            }
        }
        if (epicDone || epicNew || this.subtasks.size() == 0){
            if (epicDone) {
                this.setStatus(Status.DONE);
            }
            if (epicNew || this.subtasks.size() == 0) {
                this.setStatus(Status.NEW);
            }
        } else {
            this.setStatus(Status.IN_PROGRESS);
        }
    }



}
