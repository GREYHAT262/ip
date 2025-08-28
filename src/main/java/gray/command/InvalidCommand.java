package gray.command;

import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;

public class InvalidCommand extends Command {
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showInvalidInstruction();
    }
}
