package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    @Override
    public String typeToString() {
        return "Subtask";
    }

    protected Epic epic;


    @Override
    public String toString() {
        return "Subtask{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status + "epic=" + epic +
                ", duration=" + duration.toMinutes() +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", durationFormatter=" + durationFormatter +
                '}';
    }

    public Subtask(String name, String description, Duration duration, Epic epic) {
        super(name, description, duration);
        this.epic = epic;
        this.status = Status.NEW;
        this.epic = epic;

    }

/*    public Subtask(int id, String name, Status status, String description) {
        super(id, name, status, description);
    }*/

    public Subtask(int id, String name, Status status, String description, Epic epic, Duration duration, LocalDateTime startTime, LocalDateTime endTime) {
        super(id, name, status, description, duration, startTime, endTime);
        this.epic = epic;
    }



    public Epic getEpic() {
        return epic;
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
