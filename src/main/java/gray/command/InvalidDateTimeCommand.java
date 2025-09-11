package gray.command;

import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;

import java.io.IOException;

/**
 * Alerts user that the date and time is not in the required format.
 */
public class InvalidDateTimeCommand extends Command {
    @Override
    public String execute(TaskList taskList, Ui ui, Storage storage) throws IOException {
        return ui.showInvalidDateAndTime();
    }
}
