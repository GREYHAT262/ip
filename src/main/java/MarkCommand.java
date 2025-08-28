import java.io.IOException;

public class MarkCommand extends Command {
    private int index;
    private final boolean isValid;

    public MarkCommand(int index) {
        this.index = index;
        this.isValid = true;
    }

    public MarkCommand() {
        this.isValid = false;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws IOException {
        if (!this.isValid) {
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
