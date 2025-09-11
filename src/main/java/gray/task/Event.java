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
     *
     * @param description Description of event.
     * @param start Start date and time of event.
     * @param end End date and time of event.
     */
    public Event(String description, LocalDateTime start, LocalDateTime end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    /**
     * Checks if event occurs on the specified date.
     *
     * @param date Date to compare against.
     */
    @Override
    public boolean isCorrectDate(LocalDate date) {
        return start.getDayOfMonth() == date.getDayOfMonth()
                || end.getDayOfMonth() == date.getDayOfMonth();
    }

    @Override
    public boolean isWithinRange(LocalDateTime start, LocalDateTime end) {
        if (this.start.isBefore(start)) {
            return this.end.isAfter(start);
        } else {
            return this.start.isBefore(end);
        }
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
