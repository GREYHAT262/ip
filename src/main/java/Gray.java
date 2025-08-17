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
            } else if (input.equals("list")) {
                String taskList = "Here are your tasks:\n";
                for (int i = 0; i < Gray.tasks.size(); i++) {
                    if (i != 0) {
                        taskList += "\n";
                    }
                    taskList += i + 1 + ". " + Gray.tasks.get(i).getDescription();
                }
                Gray.respond(taskList);
            } else {
                Gray.respond("added: " + input);
                Gray.tasks.add(new Task(input));
            }
        }
    }
}
