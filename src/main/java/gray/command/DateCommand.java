package gray.command;

import java.time.LocalDate;

import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;

/**
 * Finds tasks of a specified date.
 */
public class DateCommand extends Command {
    private final LocalDate date;

    /**
     * Creates a new DateCommand.
     *
     * @param date Date to search for.
     */
    public DateCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public String execute(TaskList taskList, Ui ui, Storage storage) {
        return ui.showTasksOnDate(taskList.filterByDate(date), date);
    }
}
