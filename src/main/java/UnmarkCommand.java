import java.io.IOException;

public class UnmarkCommand extends Command {
    private int index;
    private final boolean isValid;

    public UnmarkCommand(int index) {
        this.index = index;
        this.isValid = true;
    }

    public UnmarkCommand() {
        this.isValid = false;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws IOException {
        if (!this.isValid) {
            ui.showNoIndex();
            return;
        }
        try {
            taskList.unmark(index);
            ui.showUnmarkTask(taskList.get(index));
            storage.save(taskList);
        } catch (IndexOutOfBoundsException e) {
            ui.showTaskNotFound();
        }
    }
}

