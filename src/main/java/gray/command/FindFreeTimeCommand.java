package gray.command;

import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * Find the nearest x-hours time block that is available.
 */
public class FindFreeTimeCommand extends Command {
    private final int hours;

    /**
     * Creates a new FindFreeTimeCommand.
     *
     * @param hours The number of hours of the time block.
     */
    public FindFreeTimeCommand(int hours) {
        this.hours = hours;
    }

    public String execute(TaskList taskList, Ui ui, Storage storage) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Singapore"));
        LocalDateTime start = now.plusHours(1).truncatedTo(ChronoUnit.HOURS).toLocalDateTime();
        LocalDateTime end = now.plusHours(hours + 1).truncatedTo(ChronoUnit.HOURS).toLocalDateTime();
        while (!taskList.isAvailable(start, end)) {
            start = start.plusHours(1);
            end = end.plusHours(1);
        }
        return ui.showFreeTime(start, end);
    }
}
