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
            if (input.equals("list")) {
                StringBuilder taskList = new StringBuilder("Here are your tasks:\n");
                for (int i = 0; i < Gray.tasks.size(); i++) {
                    if (i != 0) {
                        taskList.append("\n");
                    }
                    Task task = Gray.tasks.get(i);
                    taskList.append(i + 1).append(".").append(task);
                }
                Gray.respond(taskList.toString());
            } else {
                String[] inputParts = input.split(" ");
                if (inputParts.length == 2 && inputParts[0].equals("mark")
                        && inputParts[1].matches("\\d+")) {
                    try {
                        Task task = Gray.tasks.get(Integer.parseInt(inputParts[1]) - 1);
                        task.markAsDone();
                        Gray.respond("Nice work! This task is marked as done:\n  " + task);
                    } catch (IndexOutOfBoundsException e) {
                        Gray.respond("This task cannot be found!");
                    }
                } else if (inputParts.length == 2 && inputParts[0].equals("unmark")
                        && inputParts[1].matches("\\d+")) {
                    try {
                        Task task = Gray.tasks.get(Integer.parseInt(inputParts[1]) - 1);
                        task.markAsNotDone();
                        Gray.respond("I have marked this task as not done:\n  " + task);
                    } catch (IndexOutOfBoundsException e) {
                        Gray.respond("This task cannot be found!");
                    }
                } else {
                    Gray.respond("added: " + input);
                    Gray.tasks.add(new Task(input));
                }
            }
        }
    }
}
