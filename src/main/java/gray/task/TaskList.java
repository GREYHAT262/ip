package gray.task;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Represents a list of tasks.
 */
public class TaskList {
    private final ArrayList<Task> taskList;

    /**
     * Creates a new list containing all the tasks in taskList.
     */
    public TaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }

    /**
     * Creates a new list with no tasks.
     */
    public TaskList() {
        taskList = new ArrayList<>();
    }

    /**
     * Retrieves a task of a given index.
     */
    public Task get(int index) {
        return taskList.get(index);
    }

    /**
     * Retrieves the number of tasks.
     */
    public int size() {
        return taskList.size();
    }

    /**
     * Adds a task to taskList.
     */
    public void add(Task task) {
        taskList.add(task);
    }

    /**
     * Removes a task of a given index from taskList.
     */
    public Task delete(int index) {
        return taskList.remove(index);
    }

    /**
     * Mark a task of a given index as done.
     */
    public void mark(int index) {
        taskList.get(index).markAsDone();
    }

    /**
     * Mark a task of a given index as not done.
     */
    public void unmark(int index) {
        taskList.get(index).markAsNotDone();
    }

    /**
     * Returns TaskList object containing only tasks occurring on the specified date.
     */
    public TaskList filterByDate(LocalDate date) {
        TaskList filtered = new TaskList();
        for (Task task : taskList) {
            if (task instanceof Deadline deadline) {
                if (deadline.correctDate(date)) {
                    filtered.add(deadline);
                }
            } else if (task instanceof Event event) {
                if (event.correctDate(date)) {
                    filtered.add(event);
                }
            }
        }
        return filtered;
    }

    /**
     * Converts tasks to strings of the format required for storage in file.
     */
    public String toStorage() {
        StringBuilder taskString = new StringBuilder();
        for (Task task : taskList) {
            taskString.append(task.toStorage()).append("\n");
        }
        return taskString.toString();
    }

    @Override
    public String toString() {
        StringBuilder taskString = new StringBuilder();
        for (int i = 0; i < taskList.size(); i++) {
            if (i != 0) {
                taskString.append("\n");
            }
            Task task = taskList.get(i);
            taskString.append(i + 1).append(".").append(task);
        }
        return taskString.toString();
    }
}
