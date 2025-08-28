package gray.command;

import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;

/**
 * Causes chatbot to exit.
 */
public class ByeCommand extends Command {
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
