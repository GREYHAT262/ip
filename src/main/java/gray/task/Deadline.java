package gray.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a deadline task which must be completed by a specified date.
 */
public class Deadline extends Task {
    private final LocalDateTime by;

    /**
     * Creates a new deadline with the specified description and due date and time.
     * The task is initialised to not done.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Checks if deadline occurs on the specified date.
     */
    public boolean correctDate(LocalDate date) {
        return by.getDayOfMonth() == date.getDayOfMonth();
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: "
                + by.format(DateTimeFormatter.ofPattern("HHmm, MMM d yyyy")) + ")";
    }

    @Override
    public String toStorage() {
        return "D" + super.toStorage() + " | " + by.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
    }
}
