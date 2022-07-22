package tasks;

import java.util.Objects;

public class Subtask extends Task {
    protected Epic epic;

    public Subtask(String name, String description, Epic epic) {
        super(name, description);
        this.epic = epic;
        this.status = Status.NEW;
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public String toString() {
        return "tasks.Subtask{" +
                "epic=" + epic +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(epic, subtask.epic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epic);
    }
}
