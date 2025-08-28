package gray.command;

import java.time.LocalDate;

import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;

public class DateCommand extends Command {
    private LocalDate date;
    private final boolean hasDate;
    private final boolean isValid;

    public DateCommand(LocalDate date) {
        this.date = date;
        hasDate = true;
        isValid = true;
    }

    public DateCommand(boolean hasDate) {
        this.hasDate = hasDate;
        isValid = !hasDate;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        if (!hasDate) {
            ui.showNoDate();
            return;
        } else if (!isValid) {
            ui.showInvalidDate();
            return;
        }
        ui.showTasksOnDate(taskList.filterByDate(date), date);
    }
}
