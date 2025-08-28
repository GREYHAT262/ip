package gray.command;

import java.io.IOException;

import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;

public class MarkCommand extends Command {
    private int index;
    private final boolean isValid;

    public MarkCommand(int index) {
        this.index = index;
        isValid = true;
    }

    public MarkCommand() {
        isValid = false;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws IOException {
        if (!isValid) {
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
