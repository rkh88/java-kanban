public class Subtask extends Task {
    protected Epic epic;

    public Subtask(String name, String description, int id, Status status, Epic epic) {
        super(name, description, id);
        this.status = status;
        this.epic = epic;
    }


    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    @Override
    public void setStatus(Status status) {
        super.setStatus(status);
        this.epic.checkEpicStatus();
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epic=" + epic +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
