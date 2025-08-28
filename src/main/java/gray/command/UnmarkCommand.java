package gray.command;

import java.io.IOException;

import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;

public class UnmarkCommand extends Command {
    private int index;
    private final boolean isValid;

    public UnmarkCommand(int index) {
        this.index = index;
        isValid = true;
    }

    public UnmarkCommand() {
        isValid = false;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws IOException {
        if (!isValid) {
            ui.showNoIndex();
            return;
        }
        try {
            taskList.unmark(index);
            ui.showUnmarkTask(taskList.get(index));
            storage.save(taskList);
        } catch (IndexOutOfBoundsException e) {
            ui.showTaskNotFound();
        }
    }
}

