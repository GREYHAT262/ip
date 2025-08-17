import java.util.Scanner;

public class Gray {
    public static void respond(String input) {
        String horizontalLine = "----------------------------------------------------------";
        System.out.println(horizontalLine + "\n" + input + "\n" + horizontalLine);
    }

    public static void main(String[] args) {
        Gray.respond("""
                Hello! I'm Gray.
                What can I do for you?""");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                Gray.respond("Bye. Hope to see you again soon!");
                break;
            }
            Gray.respond(input);
        }
    }
}
