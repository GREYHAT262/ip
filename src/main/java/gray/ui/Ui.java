package gray.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import gray.exception.CorruptedFileException;
import gray.exception.InvalidTaskException;
import gray.task.Task;
import gray.task.TaskList;

public class Ui {
    private final Scanner scanner;

    public Ui() {
        scanner = new Scanner(System.in);
    }

    private static void respond(String input) {
        String horizontalLine = "-------------------------------------------------------------------";
        System.out.println(horizontalLine + "\n" + input + "\n" + horizontalLine);
    }

    public void showWelcome() {
        Ui.respond("""
                Hi! I'm Gray, your personal assistant chatbot!
                What can I do for you?""");
    }

    public void showGoodbye() {
        Ui.respond("Bye and see you soon!");
    }

    public void showInvalidInstruction() {
        Ui.respond("""
                I don't understand what you mean.
                Please enter a valid instruction.""");
    }

    public void showAddTask(Task task, int noOfTasks) {
        if (noOfTasks == 1) {
            Ui.respond("I've added this task:\n  " + task + "\n"
                    + "You have 1 task in your list. All the best!");
        } else {
            Ui.respond("I've added this task:\n  " + task + "\n" + "You have "
                    + noOfTasks + " tasks in your list. All the best!");
        }
    }

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

    public void showTasks(TaskList taskList) {
        if (taskList.size() == 0) {
            Ui.respond("Nice! You don't have any tasks left!");
        } else {
            Ui.respond("Here are your tasks:\n" + taskList);
        }
    }

    public void showMarkTask(Task task) {
        Ui.respond("I have marked this task as done:\n  " + task);
    }

    public void showUnmarkTask(Task task) {
        Ui.respond("I have marked this task as not done:\n  " + task);
    }

    public void showTaskNotFound() {
        Ui.respond("This task cannot be found!");
    }

    public void showNoIndex() {
        Ui.respond("Please give the index of the task.");
    }

    public void showInvalidDateAndTime() {
        Ui.respond("""
                Invalid date and time!
                Please use the format yyyy-MM-dd HHmm.""");
    }

    public void showInvalidDate() {
        Ui.respond("Invalid date! Please use the format yyyy-MM-dd.");
    }

    public void showNoDate() {
        Ui.respond("Please give a date.");
    }

    public void showTasksOnDate(TaskList taskList, LocalDate date) {
        if (taskList.size() == 0) {
            Ui.respond("There are no tasks on this date.");
        } else {
            String taskString = "Here are the tasks found on "
                    + date.format(DateTimeFormatter.ofPattern("MMM d yyyy")) + ":\n" + taskList;
            Ui.respond(taskString);
        }
    }

    public void showFileCreationError() {
        Ui.respond("Sorry! I'm not able to create the file to store your tasks!");
    }

    public void showNoFile() {
        Ui.respond("This file is not present.");
    }

    public void showWriteFileError() {
        Ui.respond("Sorry! I couldn't write to your tasks file!");
    }

    public void showLoadingError(CorruptedFileException e) {
        Ui.respond(e.getMessage());
    }

    public void showInvalidTaskException(InvalidTaskException e) {
        Ui.respond(e.getMessage());
    }

    public String readCommand() {
        return scanner.nextLine();
    }
}
