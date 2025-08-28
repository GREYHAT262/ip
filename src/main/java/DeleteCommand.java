import java.io.IOException;

public class DeleteCommand extends Command {
    private int index;
    private final boolean isValid;

    public DeleteCommand(int index) {
        this.index = index;
        this.isValid = true;
    }

    public DeleteCommand() {
        this.isValid = false;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws IOException {
        if (!this.isValid) {
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
