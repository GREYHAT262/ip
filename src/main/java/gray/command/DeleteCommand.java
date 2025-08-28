package gray.command;

import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;
import java.io.IOException;

/**
 * Deletes a task from a list of tasks.
 */
public class DeleteCommand extends Command {
    private int index;
    private final boolean isValid;

    /**
     * Creates a new DeleteCommand.
     * If index is valid.
     */
    public DeleteCommand(int index) {
        this.index = index;
        this.isValid = true;
    }

    /**
     * Creates a new DeleteCommand.
     * If index is invalid.
     */
    public DeleteCommand() {
        this.isValid = false;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws IOException {
        if (!this.isValid) {
            ui.showNoIndex();
            return;
        }
        try {
            ui.showDeleteTask(taskList.delete(index), taskList.size());
            storage.save(taskList);
        } catch (IndexOutOfBoundsException e) {
            ui.showTaskNotFound();
        }
    }
}
