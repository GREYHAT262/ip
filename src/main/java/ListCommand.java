public class ListCommand extends Command {
    private final boolean isValid;

    public ListCommand(boolean isValid) {
        this.isValid = isValid;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        if (this.isValid) {
            ui.showTasks(taskList);
        } else {
            ui.showInvalidInstruction();
        }
    }
}
