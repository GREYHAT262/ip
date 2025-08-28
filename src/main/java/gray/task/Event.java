package gray.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    private final LocalDateTime start;
    private final LocalDateTime end;

    public Event(String description, LocalDateTime start, LocalDateTime end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    public boolean correctDateTime(LocalDate dateTime) {
        return this.start.getDayOfMonth() == dateTime.getDayOfMonth()
                || this.end.getDayOfMonth() == dateTime.getDayOfMonth();
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " +
                this.start.format(DateTimeFormatter.ofPattern("HHmm, MMM d yyyy")) + " to: "
                + this.end.format(DateTimeFormatter.ofPattern("HHmm, MMM d yyyy")) + ")";
    }

    @Override
    public String toStorage() {
        return "E" + super.toStorage() + " | " + this.start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"))
                + " | " + this.end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
    }
}
