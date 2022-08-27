package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    protected ArrayList<Subtask> subtasks = new ArrayList<>();

    public Epic(String name, String description,Duration duration) {
        super(name, description, duration);
        this.status = Status.NEW;

    }

    public Epic(int id, String name, Status status, String description, Duration duration, LocalDateTime startTime, LocalDateTime endTime) {
        super(id, name, status, description, duration, startTime, endTime);

    }

    @Override
    public String typeToString() {
        return "EPIC";
    }

    public Duration getCalcDuration() {
        Duration calcDuration = Duration.ofMinutes(0);
        for(Subtask subtask : this.getSubtasks()) {
            calcDuration.plus(subtask.getDuration());
        }
        return calcDuration;
    }

    public LocalDateTime getCalcStartTime() {
        LocalDateTime startTime = this.getSubtasks().get(0).getStartTime();
        return startTime;
    }

    public LocalDateTime getCalcEndTime() {
        LocalDateTime endTime = this.getSubtasks().get(0).getStartTime().plus(getCalcDuration());
        return endTime;
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Subtask subtask) {
        this.getSubtasks().add(subtask);
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasks, epic.subtasks) && Objects.equals(duration, epic.duration) && Objects.equals(startTime, epic.startTime) && Objects.equals(endTime, epic.endTime);
    }

}
