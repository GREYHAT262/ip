package gray.command;

import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;
import java.io.IOException;

/**
 * Marks a task as not done.
 */
public class UnmarkCommand extends Command {
    private int index;
    private final boolean isValid;

    /**
     * Creates a new UnmarkCommand.
     * If index is valid.
     */
    public UnmarkCommand(int index) {
        this.index = index;
        this.isValid = true;
    }

    /**
     * Creates a new UnmarkCommand.
     * If index is invalid.
     */
    public UnmarkCommand() {
        this.isValid = false;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws IOException {
        if (!this.isValid) {
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

