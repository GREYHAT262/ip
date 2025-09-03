package gray.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a deadline task which must be completed by a specified date.
 */
public class Deadline extends Task {
    private final LocalDateTime byDate;

    /**
     * Creates a new deadline with the specified description and due date and time.
     * The task is initialised to not done.
     *
     * @param description Description of the deadline.
     * @param byDate Due date of the deadline.
     */
    public Deadline(String description, LocalDateTime byDate) {
        super(description);
        this.byDate = byDate;
    }

    /**
     * Checks if deadline occurs on the specified date.
     *
     * @param date Date to be compared to.
     */
    public boolean isCorrectDate(LocalDate date) {
        return byDate.getDayOfMonth() == date.getDayOfMonth();
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: "
                + byDate.format(DateTimeFormatter.ofPattern("HHmm, MMM d yyyy")) + ")";
    }

    @Override
    public String toStorage() {
        return "D" + super.toStorage() + " | " + byDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
    }
}
