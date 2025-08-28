package gray.command;

import gray.exception.InvalidTaskException;
import gray.task.Task;
import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;
import java.io.IOException;

/**
 * Adds a task to a list of tasks.
 */
public class AddCommand extends Command {
    private Task task;
    private final boolean isValid;
    private final boolean isValidDateTime;
    private InvalidTaskException e;

    /**
     * Creates a new AddCommand.
     * If task can be instantiated.
     */
    public AddCommand(Task task) {
        this.task = task;
        this.isValid = true;
        this.isValidDateTime = true;
    }

    /**
     * Creates a new AddCommand.
     * If there are missing arguments leading to an invalid task.
     */
    public AddCommand(InvalidTaskException e) {
        this.isValid = false;
        this.isValidDateTime = true;
        this.e = e;
    }

    /**
     * Creates a new AddCommand.
     * If the date and time is invalid.
     */
    public AddCommand() {
        this.isValid = false;
        this.isValidDateTime = false;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws IOException {
        if (!isValid) {
            if (!isValidDateTime) {
                ui.showInvalidDateAndTime();
            } else {
                ui.showInvalidTaskException(this.e);
            }
            return;
        }
        taskList.add(task);
        storage.save(taskList);
        ui.showAddTask(task, taskList.size());
    }
}
