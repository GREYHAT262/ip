package gray.task;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a generic task with a description and completion status.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a task with the specified description.
     * The task is initialised to not done.
     *
     * @param description Description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    private String getStatusIcon() {
        return (isDone) ? "X" : " ";
    }

    /**
     * Marks the task as done.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks the task as not done.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    public boolean isCorrectDate(LocalDate date) {
        return false;
    }

    public boolean isWithinRange(LocalDateTime start, LocalDateTime end) {
        return false;
    }

    public boolean matchDescription(String description) {
        return this.description.contains(description);
    }

    @Override
    public String toString() {
        return "[" + this.getStatusIcon() + "] " + description;
    }

    /**
     * Converts task to a string in the format used for storage.
     */
    public String toStorage() {
        return " | " + (isDone ? "1" : "0") + " | " + description;
    }
}
