package gray.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event task which occurs over a specific period of time.
 */
public class Event extends Task {
    private final LocalDateTime start;
    private final LocalDateTime end;

    /**
     * Creates a new event with the specified description, start and end date and time.
     * The task is initialised to not done.
     */
    public Event(String description, LocalDateTime start, LocalDateTime end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    /**
     * Checks if event occurs on the specified date.
     */
    public boolean correctDate(LocalDate date) {
        return start.getDayOfMonth() == date.getDayOfMonth()
                || end.getDayOfMonth() == date.getDayOfMonth();
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: "
                + start.format(DateTimeFormatter.ofPattern("HHmm, MMM d yyyy")) + " to: "
                + end.format(DateTimeFormatter.ofPattern("HHmm, MMM d yyyy")) + ")";
    }

    @Override
    public String toStorage() {
        return "E" + super.toStorage() + " | " + start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"))
                + " | " + end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
    }
}
