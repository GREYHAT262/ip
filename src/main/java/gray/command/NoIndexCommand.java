package gray.command;

import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;

/**
 * Alerts user that no index is given.
 */
public class NoIndexCommand extends Command {
    @Override
    public String execute(TaskList taskList, Ui ui, Storage storage) {
        return ui.showNoIndex();
    }
}
