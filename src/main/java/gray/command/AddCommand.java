package gray.command;

import java.io.IOException;

import gray.exception.InvalidTaskException;
import gray.task.Task;
import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;

public class AddCommand extends Command {
    private Task task;
    private final boolean isValid;
    private final boolean isValidDateTime;
    private InvalidTaskException e;

    public AddCommand(Task task) {
        this.task = task;
        isValid = true;
        isValidDateTime = true;
    }

    public AddCommand(InvalidTaskException e) {
        isValid = false;
        isValidDateTime = true;
        this.e = e;
    }

    public AddCommand() {
        isValid = false;
        isValidDateTime = false;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws IOException {
        if (!isValid) {
            if (!isValidDateTime) {
                ui.showInvalidDateAndTime();
            } else {
                ui.showInvalidTaskException(e);
            }
            return;
        }
        taskList.add(task);
        storage.save(taskList);
        ui.showAddTask(task, taskList.size());
    }
}
