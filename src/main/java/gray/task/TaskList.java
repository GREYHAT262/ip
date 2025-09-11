package gray.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Represents a list of tasks.
 */
public class TaskList {
    private final ArrayList<Task> taskList;

    /**
     * Creates a new list containing all the tasks in taskList.
     *
     * @param taskList A list of tasks.
     */
    public TaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }


    /**
     * Creates a new list using a variable number of tasks.
     *
     * @param tasks A variable number of tasks.
     */
    public TaskList(Task... tasks) {
        taskList = new ArrayList<>();
        taskList.addAll(Arrays.asList(tasks));
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
     * Returns task list containing only tasks occurring on the specified date.
     */
    public TaskList filterByDate(LocalDate date) {
        ArrayList<Task> tasks = taskList
                .stream()
                .filter(task -> task.isCorrectDate(date))
                .collect(Collectors.toCollection(ArrayList::new));
        return new TaskList(tasks);
    }

    /**
     * Returns task list containing only tasks with matching descriptions.
     */
    public TaskList filterByDescription(String description) {
        ArrayList<Task> tasks = taskList
                .stream()
                .filter(task -> task.matchDescription(description))
                .collect(Collectors.toCollection(ArrayList::new));
        return new TaskList(tasks);
    }

    /**
     * Checks if a block of time is available.
     *
     * @param start Start of time block.
     * @param end End of time block.
     * @return Availability of time block
     */
    public boolean isAvailable(LocalDateTime start, LocalDateTime end) {
        return taskList
                .stream()
                .noneMatch(task -> task.isWithinRange(start, end));
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
