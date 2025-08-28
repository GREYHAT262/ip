package gray.command;

import gray.exception.InvalidTaskException;
import gray.task.Task;
import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;
import java.io.IOException;

public class AddCommand extends Command {
    private Task task;
    private final boolean isValid;
    private final boolean isValidDateTime;
    private InvalidTaskException e;

    public AddCommand(Task task) {
        this.task = task;
        this.isValid = true;
        this.isValidDateTime = true;
    }

    public AddCommand(InvalidTaskException e) {
        this.isValid = false;
        this.isValidDateTime = true;
        this.e = e;
    }

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
