package gray.command;

import java.io.IOException;

import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;

/**
 * Marks a task as done.
 */
public class MarkCommand extends Command {
    private final int index;

    /**
     * Creates a new MarkCommand.
     * If index is valid.
     *
     * @param index Index of task to be marked.
     */
    public MarkCommand(int index) {
        this.index = index;
    }

    @Override
    public String execute(TaskList taskList, Ui ui, Storage storage) throws IOException {
        try {
            taskList.mark(index);
            String output = ui.showMarkTask(taskList.get(index));
            storage.save(taskList);
            return output;
        } catch (IndexOutOfBoundsException e) {
            return ui.showTaskNotFound();
        }
    }
}
