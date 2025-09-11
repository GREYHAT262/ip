package gray.command;

import gray.exception.InvalidTaskException;
import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;

import java.io.IOException;

/**
 * Alerts user that the task is invalid.
 */
public class InvalidTaskExceptionCommand extends Command {
    private final InvalidTaskException e;

    public InvalidTaskExceptionCommand(InvalidTaskException e) {
        this.e = e;
    }

    @Override
    public String execute(TaskList taskList, Ui ui, Storage storage) throws IOException {
        return ui.showInvalidTaskException(e);
    }
}
