package gray.command;

import java.io.IOException;

import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;

/**
 * Marks a task as not done.
 */
public class UnmarkCommand extends Command {
    private int index;
    private final boolean isValid;

    /**
     * Creates a new UnmarkCommand.
     * If index is valid.
     *
     * @param index Index of task to be unmarked.
     */
    public UnmarkCommand(int index) {
        this.index = index;
        isValid = true;
    }

    /**
     * Creates a new UnmarkCommand.
     * If index is invalid.
     */
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

