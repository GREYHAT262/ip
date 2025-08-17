public class Gray {
    public static void respond(String input) {
        String horizontalLine = "----------------------------------------------------------";
        System.out.println(horizontalLine + "\n" + input + "\n" + horizontalLine);
    }

    public static void main(String[] args) {
        Gray.respond("""
                Hello! I'm Gray.
                What can I do for you?""");
    }
}
