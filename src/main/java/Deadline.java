import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    private final LocalDateTime by;

    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    public boolean correctDateTime(LocalDate dateTime) {
        return this.by.getDayOfMonth() == dateTime.getDayOfMonth();
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: "
                + this.by.format(DateTimeFormatter.ofPattern("HHmm, MMM d yyyy")) + ")";
    }

    @Override
    public String toStorage() {
        return "D" + super.toStorage() + " | " + this.by.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
    }
}
