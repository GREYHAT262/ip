package gray.command;

import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;
import java.io.IOException;

/**
 * Marks a task as done.
 */
public class MarkCommand extends Command {
    private int index;
    private final boolean isValid;

    /**
     * Creates a new MarkCommand.
     * If index is valid.
     */
    public MarkCommand(int index) {
        this.index = index;
        this.isValid = true;
    }

    /**
     * Creates a new MarkCommand.
     * If index is invalid.
     */
    public MarkCommand() {
        this.isValid = false;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws IOException {
        if (!this.isValid) {
            ui.showNoIndex();
            return;
        }
        try {
            taskList.mark(index);
            ui.showMarkTask(taskList.get(index));
            storage.save(taskList);
        } catch (IndexOutOfBoundsException e) {
            ui.showTaskNotFound();
        }
    }
}
