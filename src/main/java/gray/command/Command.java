package gray.command;

import java.io.IOException;

import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;

/**
 * Represents a generic user command.
 */
public abstract class Command {
    /**
     * Performs the function of the command.
     *
     * @throws IOException If Storage object fails to save taskList.
     */
    public abstract void execute(TaskList taskList, Ui ui, Storage storage) throws IOException;

    /**
     * Returns whether chatbot should exit.
     */
    public boolean isExit() {
        return false;
    }
}
