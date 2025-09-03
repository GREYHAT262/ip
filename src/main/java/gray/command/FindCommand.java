package gray.command;

import gray.task.TaskList;
import gray.ui.Storage;
import gray.ui.Ui;

/**
 * Finds tasks with a certain keyword in their descriptions.
 */
public class FindCommand extends Command {
    private final String description;

    /**
     * Creates a new FindCommand.
     *
     * @param description Keyword used to find relevant tasks.
     */
    public FindCommand(String description) {
        this.description = description;
    }

    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showFindTasks(taskList.filterByDescription(description));
    }
}
