package gray.command;

import java.io.IOException;

import gray.task.Task;
import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;

/**
 * Adds a task to a list of tasks.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates a new AddCommand.
     *
     * @param task Task to be added.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public String execute(TaskList taskList, Ui ui, Storage storage) throws IOException {
        taskList.add(task);
        storage.save(taskList);
        return ui.showAddTask(task, taskList.size());
    }
}
