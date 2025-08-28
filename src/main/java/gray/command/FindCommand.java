package gray.command;

import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;

public class FindCommand extends Command {
    private final String description;

    public FindCommand(String description) {
        this.description = description;
    }

    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showFindTasks(taskList.filterByDescription(description));
    }
}
