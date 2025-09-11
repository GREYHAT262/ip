package gray.command;

import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;

/**
 * Alerts user that no date is given.
 */
public class NoDateCommand extends Command {
    @Override
    public String execute(TaskList taskList, Ui ui, Storage storage) {
        return ui.showNoDate();
    }
}
