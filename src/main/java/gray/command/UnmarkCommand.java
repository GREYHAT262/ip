package gray.command;

import java.io.IOException;

import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;

/**
 * Marks a task as not done.
 */
public class UnmarkCommand extends Command {
    private final int index;

    /**
     * Creates a new UnmarkCommand.
     * If index is valid.
     *
     * @param index Index of task to be unmarked.
     */
    public UnmarkCommand(int index) {
        this.index = index;
    }

    @Override
    public String execute(TaskList taskList, Ui ui, Storage storage) throws IOException {
        try {
            taskList.unmark(index);
            String output = ui.showUnmarkTask(taskList.get(index));
            storage.save(taskList);
            return output;
        } catch (IndexOutOfBoundsException e) {
            return ui.showTaskNotFound();
        }
    }
}

