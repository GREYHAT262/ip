package gray.command;

import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;
import java.time.LocalDate;

public class DateCommand extends Command {
    private LocalDate date;
    private final boolean hasDate;
    private final boolean isValid;

    public DateCommand(LocalDate date) {
        this.date = date;
        this.hasDate = true;
        this.isValid = true;
    }

    public DateCommand(boolean hasDate) {
        this.hasDate = hasDate;
        this.isValid = !this.hasDate;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        if (!this.hasDate) {
            ui.showNoDate();
            return;
        } else if (!this.isValid) {
            ui.showInvalidDate();
            return;
        }
        ui.showTasksOnDate(taskList.filterByDate(date), date);
    }
}
