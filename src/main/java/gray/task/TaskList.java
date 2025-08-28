package gray.task;

import java.time.LocalDate;
import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> taskList;

    public TaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }

    public TaskList() {
        taskList = new ArrayList<>();
    }

    public Task get(int index) {
        return taskList.get(index);
    }

    public int size() {
        return taskList.size();
    }

    public void add(Task task) {
        taskList.add(task);
    }

    public Task delete(int index) {
        return taskList.remove(index);
    }

    public void mark(int index) {
        taskList.get(index).markAsDone();
    }

    public void unmark(int index) {
        taskList.get(index).markAsNotDone();
    }

    public TaskList filterByDate(LocalDate date) {
        TaskList filtered = new TaskList();
        for (Task task : taskList) {
            if (task instanceof Deadline deadline) {
                if (deadline.correctDateTime(date)) {
                    filtered.add(deadline);
                }
            } else if (task instanceof Event event) {
                if (event.correctDateTime(date)) {
                    filtered.add(event);
                }
            }
        }
        return filtered;
    }

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
