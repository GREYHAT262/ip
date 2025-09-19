package gray.command;

import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;

/**
 * Alerts user that their start date/time is not before their end date/time.
 */
public class UnexpectedDateTimeCommand extends Command {
    @Override
    public String execute(TaskList taskList, Ui ui, Storage storage) {
        return ui.showUnexpectedDateAndTime();
    }
}
