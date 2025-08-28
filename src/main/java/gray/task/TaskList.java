package gray.task;

import java.time.LocalDate;
import java.util.ArrayList;

public class TaskList {
    ArrayList<Task> taskList;

    public TaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }

    public TaskList() {
        this.taskList = new ArrayList<>();
    }

    public Task get(int index) {
        return this.taskList.get(index);
    }

    public int size() {
        return this.taskList.size();
    }

    public void add(Task task) {
        this.taskList.add(task);
    }

    public Task delete(int index) {
        return this.taskList.remove(index);
    }

    public void mark(int index) {
        this.taskList.get(index).markAsDone();
    }

    public void unmark(int index) {
        this.taskList.get(index).markAsNotDone();
    }

    public TaskList filterByDate(LocalDate date) {
        TaskList filtered = new TaskList();
        for (Task task : this.taskList) {
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
        for (Task task : this.taskList) {
            taskString.append(task.toStorage()).append("\n");
        }
        return taskString.toString();
    }

    @Override
    public String toString() {
        StringBuilder taskString = new StringBuilder();
        for (int i = 0; i < this.taskList.size(); i++) {
            if (i != 0) {
                taskString.append("\n");
            }
            Task task = this.taskList.get(i);
            taskString.append(i + 1).append(".").append(task);
        }
        return taskString.toString();
    }
}
