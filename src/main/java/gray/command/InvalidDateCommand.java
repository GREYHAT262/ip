package gray.command;

import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;

/**
 * Alerts user that the date is not in the required format.
 */
public class InvalidDateCommand extends Command {
    @Override
    public String execute(TaskList taskList, Ui ui, Storage storage) {
        return ui.showInvalidDate();
    }
}
