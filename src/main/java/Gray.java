import java.util.Scanner;
import java.util.ArrayList;

public class Gray {
    private static final ArrayList<Task> tasks = new ArrayList<>();

    public static void respond(String input) {
        String horizontalLine = "----------------------------------------------------------";
        System.out.println(horizontalLine + "\n" + input + "\n" + horizontalLine);
    }

    public static void main(String[] args) {
        Gray.respond("""
                Hi! I'm Gray, your personal assistant chatbot!
                What can I do for you?""");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                Gray.respond("Bye and see you soon!");
                break;
            }
            String[] inputParts = input.split(" ");
            String command = inputParts[0];
            switch (command) {
                case "list" -> {
                    StringBuilder taskList = new StringBuilder("Here are your tasks:\n");
                    if (Gray.tasks.isEmpty()) {
                        Gray.respond("Nice! You don't have any tasks left!");
                    } else {
                        for (int i = 0; i < Gray.tasks.size(); i++) {
                            if (i != 0) {
                                taskList.append("\n");
                            }
                            Task task = Gray.tasks.get(i);
                            taskList.append(i + 1).append(".").append(task);
                        }
                        Gray.respond(taskList.toString());
                    }
                }
                case "mark" -> {
                    if (inputParts.length == 2 && inputParts[1].matches("\\d+")) {
                        try {
                            Task task = Gray.tasks.get(Integer.parseInt(inputParts[1]) - 1);
                            task.markAsDone();
                            Gray.respond("I have marked this task as done:\n  " + task);
                        } catch (IndexOutOfBoundsException e) {
                            Gray.respond("This task cannot be found!");
                        }
                    } else {
                        Gray.respond("""
                                Invalid instruction.
                                Please give the index of the task to be marked.""");
                    }
                }
                case "unmark" -> {
                    if (inputParts.length == 2 && inputParts[1].matches("\\d+")) {
                        try {
                            Task task = Gray.tasks.get(Integer.parseInt(inputParts[1]) - 1);
                            task.markAsNotDone();
                            Gray.respond("I have marked this task as not done:\n  " + task);
                        } catch (IndexOutOfBoundsException e) {
                            Gray.respond("This task cannot be found!");
                        }
                    } else {
                        Gray.respond("""
                                Invalid instruction.
                                Please give the index of the task to be unmarked.""");
                    }
                }
                case "todo" -> {
                    String description = input.split(" ", 2)[1];
                    Todo todo = new Todo(description);
                    Gray.tasks.add(todo);
                    if (Gray.tasks.size() == 1) {
                        Gray.respond("I've added this task:\n  " + todo + "\n" + "You have "
                                + Gray.tasks.size() + " more task to complete. All the best!");
                    } else {
                        Gray.respond("I've added this task:\n  " + todo + "\n" + "You have "
                                + Gray.tasks.size() + " more tasks to complete. All the best!");
                    }
                }
                case "deadline" -> {
                    String[] info = input.split(" ", 2)[1].split("/by");
                    Deadline deadline = new Deadline(info[0].trim(), info[1].trim());
                    Gray.tasks.add(deadline);
                    if (Gray.tasks.size() == 1) {
                        Gray.respond("I've added this task:\n  " + deadline + "\n" + "You have "
                                + Gray.tasks.size() + " more task to complete. All the best!");
                    } else {
                        Gray.respond("I've added this task:\n  " + deadline + "\n" + "You have "
                                + Gray.tasks.size() + " more tasks to complete. All the best!");
                    }
                }
                case "event" -> {
                    String[] info = input.split(" ", 2)[1].split("/from");
                    String[] times = info[1].split("/to");
                    Event event = new Event(info[0].trim(), times[0].trim(), times[1].trim());
                    Gray.tasks.add(event);
                    if (Gray.tasks.size() == 1) {
                        Gray.respond("I've added this task:\n  " + event + "\n" + "You have "
                                + Gray.tasks.size() + " more task to complete. All the best!");
                    } else {
                        Gray.respond("I've added this task:\n  " + event + "\n" + "You have "
                                + Gray.tasks.size() + " more tasks to complete. All the best!");
                    }
                }
                default -> Gray.respond("""
                        I don't understand what you mean.
                        Please enter a valid instruction.""");
            }
        }
    }
}
