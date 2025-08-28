package gray.command;

import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;

/**
 * Alerts user to the use of an invalid instruction.
 */
public class InvalidCommand extends Command {
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showInvalidInstruction();
    }
}
