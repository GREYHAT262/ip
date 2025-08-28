package gray.ui;

import gray.exception.CorruptedFileException;
import gray.exception.InvalidTaskException;
import gray.task.Task;
import gray.task.TaskList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Prints chatbot responses onto the screen.
 */
public class Ui {
    private final Scanner scanner;

    /**
     * Creates a new Ui.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Appends a line above and below chatbot response.
     *
     * @param input Gray response.
     */
    private static void respond(String input) {
        String horizontalLine = "-------------------------------------------------------------------";
        System.out.println(horizontalLine + "\n" + input + "\n" + horizontalLine);
    }

    /**
     * Prints welcome message.
     */
    public void showWelcome() {
        Ui.respond("""
                Hi! I'm Gray, your personal assistant chatbot!
                What can I do for you?""");
    }

    /**
     * Prints goodbye message.
     */
    public void showGoodbye() {
        Ui.respond("Bye and see you soon!");
    }

    /**
     * Prints invalid instruction error message.
     */
    public void showInvalidInstruction() {
        Ui.respond("""
                I don't understand what you mean.
                Please enter a valid instruction.""");
    }

    /**
     * Prints the task that was added and the number of tasks left.
     */
    public void showAddTask(Task task, int noOfTasks) {
        if (noOfTasks == 1) {
            Ui.respond("I've added this task:\n  " + task + "\n"
                    + "You have 1 task in your list. All the best!");
        } else {
            Ui.respond("I've added this task:\n  " + task + "\n" + "You have "
                    + noOfTasks + " tasks in your list. All the best!");
        }
    }

    /**
     * Prints the task that was deleted and the number of tasks left.
     */
    public void showDeleteTask(Task task, int noOfTasks) {
        if (noOfTasks == 0) {
            Ui.respond("I've deleted this task:\n  " + task + "\n"
                    + "You have no more tasks left!");
        } else if (noOfTasks == 1) {
            Ui.respond("I've deleted this task:\n  " + task + "\n"
                    + "You have 1 task in your list. All the best!");
        } else {
            Ui.respond("I've deleted this task:\n  " + task + "\n" + "You have "
                    + noOfTasks + " tasks in your list. All the best!");
        }
    }

    /**
     * Prints all tasks in taskList.
     * Prints a separate message if there are no tasks left.
     */
    public void showTasks(TaskList taskList) {
        if (taskList.size() == 0) {
            Ui.respond("Nice! You don't have any tasks left!");
        } else {
            Ui.respond("Here are your tasks:\n" + taskList);
        }
    }

    /**
     * Prints the task that was marked.
     */
    public void showMarkTask(Task task) {
        Ui.respond("I have marked this task as done:\n  " + task);
    }

    /**
     * Prints the task that was marked.
     */
    public void showUnmarkTask(Task task) {
        Ui.respond("I have marked this task as not done:\n  " + task);
    }

    /**
     * Prints task not found error message.
     */
    public void showTaskNotFound() {
        Ui.respond("This task cannot be found!");
    }

    /**
     * Prints no task index error message.
     */
    public void showNoIndex() {
        Ui.respond("Please give the index of the task.");
    }

    /**
     * Prints invalid date and time error message.
     */
    public void showInvalidDateAndTime() {
        Ui.respond("""
                Invalid date and time!
                Please use the format yyyy-MM-dd HHmm.""");
    }

    /**
     * Prints invalid date error message.
     */
    public void showInvalidDate() {
        Ui.respond("Invalid date! Please use the format yyyy-MM-dd.");
    }

    /**
     * Prints no date error message.
     */
    public void showNoDate() {
        Ui.respond("Please give a date.");
    }

    /**
     * Prints tasks occurring on a specified date.
     */
    public void showTasksOnDate(TaskList taskList, LocalDate date) {
        if (taskList.size() == 0) {
            Ui.respond("There are no tasks on this date.");
        } else {
            String taskString = "Here are the tasks found on "
                    + date.format(DateTimeFormatter.ofPattern("MMM d yyyy")) + ":\n" + taskList;
            Ui.respond(taskString);
        }
    }

    /**
     * Prints file creation error message.
     */
    public void showFileCreationError() {
        Ui.respond("Sorry! I'm not able to create the file to store your tasks!");
    }

    /**
     * Prints no file error message.
     */
    public void showNoFile() {
        Ui.respond("This file is not present.");
    }

    /**
     * Prints write file error message.
     */
    public void showWriteFileError() {
        Ui.respond("Sorry! I couldn't write to your tasks file!");
    }

    /**
     * Prints loading error message.
     */
    public void showLoadingError(CorruptedFileException e) {
        Ui.respond(e.getMessage());
    }

    /**
     * Prints invalid task error message.
     */
    public void showInvalidTaskException(InvalidTaskException e) {
        Ui.respond(e.getMessage());
    }

    /**
     * Reads user command one line at a time.
     *
     * @return User command.
     */
    public String readCommand() {
        return this.scanner.nextLine();
    }
}
