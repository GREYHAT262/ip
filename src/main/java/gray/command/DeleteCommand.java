package gray.command;

import java.io.IOException;

import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;

/**
 * Deletes a task from a list of tasks.
 */
public class DeleteCommand extends Command {
    private int index;
    private final boolean isValid;

    /**
     * Creates a new DeleteCommand.
     * If index is valid.
     *
     * @param index Index of the task to be deleted.
     */
    public DeleteCommand(int index) {
        this.index = index;
        isValid = true;
    }

    /**
     * Creates a new DeleteCommand.
     * If index is invalid.
     */
    public DeleteCommand() {
        isValid = false;
    }

    @Override
    public String execute(TaskList taskList, Ui ui, Storage storage) throws IOException {
        if (!isValid) {
            return ui.showNoIndex();
        }
        try {
            String output = ui.showDeleteTask(taskList.delete(index), taskList.size());
            storage.save(taskList);
            return output;
        } catch (IndexOutOfBoundsException e) {
            return ui.showTaskNotFound();
        }
    }
}
