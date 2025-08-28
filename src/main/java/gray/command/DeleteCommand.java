package gray.command;

import java.io.IOException;

import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;

public class DeleteCommand extends Command {
    private int index;
    private final boolean isValid;

    public DeleteCommand(int index) {
        this.index = index;
        isValid = true;
    }

    public DeleteCommand() {
        isValid = false;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws IOException {
        if (!isValid) {
            ui.showNoIndex();
            return;
        }
        try {
            ui.showDeleteTask(taskList.delete(index), taskList.size());
            storage.save(taskList);
        } catch (IndexOutOfBoundsException e) {
            ui.showTaskNotFound();
        }
    }
}
