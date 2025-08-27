import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Ui {
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
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

    public void showTasks(ArrayList<Task> tasks) {
        StringBuilder taskList = new StringBuilder("Here are your tasks:\n");
        if (tasks.isEmpty()) {
            Ui.respond("Nice! You don't have any tasks left!");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                if (i != 0) {
                    taskList.append("\n");
                }
                Task task = tasks.get(i);
                taskList.append(i + 1).append(".").append(task);
            }
            Ui.respond(taskList.toString());
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

    public void showTasksOnDate(ArrayList<Task> tasks, LocalDate date) {
        if (tasks.isEmpty()) {
            Ui.respond("There are no tasks on this date.");
        } else {
            StringBuilder taskList = new StringBuilder("Here are the tasks found on "
                    + date.format(DateTimeFormatter.ofPattern("MMM d yyyy")) + ":\n");
            for (int i = 0; i < tasks.size(); i++) {
                if (i != 0) {
                    taskList.append("\n");
                }
                Task task = tasks.get(i);
                taskList.append(i + 1).append(".").append(task);
            }
            Ui.respond(taskList.toString());
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
        return this.scanner.nextLine();
    }
}
