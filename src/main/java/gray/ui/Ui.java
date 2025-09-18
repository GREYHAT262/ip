package gray.ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import gray.exception.CorruptedFileException;
import gray.exception.InvalidTaskException;
import gray.task.Task;
import gray.task.TaskList;

/**
 * Prints chatbot responses onto the screen.
 */
public class Ui {
    /**
     * Returns goodbye message.
     */
    public String showGoodbye() {
        return "Bye and see you soon!";
    }

    /**
     * Returns invalid instruction error message.
     */
    public String showInvalidInstruction() {
        return """
                I don't understand what you mean.
                Please enter a valid instruction.""";
    }

    /**
     * Returns the task that was added and the number of tasks left.
     */
    public String showAddTask(Task task, int noOfTasks) {
        if (noOfTasks == 1) {
            return "I've added this task:\n  " + task + "\n"
                    + "You have 1 task in your list. All the best!";
        } else {
            return "I've added this task:\n  " + task + "\n" + "You have "
                    + noOfTasks + " tasks in your list. All the best!";
        }
    }

    /**
     * Returns the task that was deleted and the number of tasks left.
     */
    public String showDeleteTask(Task task, int noOfTasks) {
        if (noOfTasks == 0) {
            return "I've deleted this task:\n  " + task + "\n"
                    + "You have no more tasks left!";
        } else if (noOfTasks == 1) {
            return "I've deleted this task:\n  " + task + "\n"
                    + "You have 1 task in your list. All the best!";
        } else {
            return "I've deleted this task:\n  " + task + "\n" + "You have "
                    + noOfTasks + " tasks in your list. All the best!";
        }
    }

    /**
     * Returns all tasks in taskList.
     * Returns a separate message if there are no tasks left.
     */
    public String showTasks(TaskList taskList) {
        if (taskList.size() == 0) {
            return "Nice! You don't have any tasks left!";
        } else {
            return "Here are your tasks:\n" + taskList;
        }
    }

    /**
     * Returns the task that was marked.
     */
    public String showMarkTask(Task task) {
        return "I have marked this task as done:\n  " + task;
    }

    /**
     * Returns the task that was marked.
     */
    public String showUnmarkTask(Task task) {
        return "I have marked this task as not done:\n  " + task;
    }

    /**
     * Returns task not found error message.
     */
    public String showTaskNotFound() {
        return "This task cannot be found!";
    }

    /**
     * Returns no task index error message.
     */
    public String showNoIndex() {
        return "Please give the index of the task.";
    }

    /**
     * Returns invalid date and time error message.
     */
    public String showInvalidDateAndTime() {
        return """
                Invalid date and time!
                Please use the format yyyy-MM-dd HHmm.""";
    }

    /**
     * Returns invalid date error message.
     */
    public String showInvalidDate() {
        return "Invalid date! Please use the format yyyy-MM-dd.";
    }

    /**
     * Returns no date error message.
     */
    public String showNoDate() {
        return "Please give a date.";
    }

    /**
     * Returns tasks occurring on a specified date.
     */
    public String showTasksOnDate(TaskList taskList, LocalDate date) {
        if (taskList.size() == 0) {
            return "There are no tasks on this date.";
        } else {
            String taskString = "Here are the tasks found on "
                    + date.format(DateTimeFormatter.ofPattern("MMM d yyyy")) + ":\n" + taskList;
            return taskString;
        }
    }

    /**
     * Returns tasks with matching descriptions.
     */
    public String showFindTasks(TaskList taskList) {
        if (taskList.size() == 0) {
            return "No matching tasks can be found!";
        } else {
            return "Here are the matching tasks in your list:\n" + taskList;
        }
    }

    /**
     * Returns nearest available time block.
     */
    public String showFreeTime(LocalDateTime start, LocalDateTime end) {
        return start.format(DateTimeFormatter.ofPattern("HHmm, MMM d yyyy")) + " - "
                + end.format(DateTimeFormatter.ofPattern("HHmm, MMM d yyyy"));
    }

    /**
     * Returns file creation error message.
     */
    public String showFileCreationError() {
        return "Sorry! I'm not able to create the file to store your tasks!";
    }

    /**
     * Returns no file error message.
     */
    public String showNoFile() {
        return "This file is not present.";
    }

    /**
     * Returns write file error message.
     */
    public String showWriteFileError() {
        return "Sorry! I couldn't write to your tasks file!";
    }

    /**
     * Returns loading error message.
     */
    public String showLoadingError(CorruptedFileException e) {
        return e.getMessage();
    }

    /**
     * Returns invalid task error message.
     */
    public String showInvalidTaskException(InvalidTaskException e) {
        return e.getMessage();
    }
}
