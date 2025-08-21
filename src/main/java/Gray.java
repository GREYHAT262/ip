import java.util.Scanner;
import java.util.ArrayList;

public class Gray {
    private static final ArrayList<Task> tasks = new ArrayList<>();

    private static void respond(String input) {
        String horizontalLine = "-------------------------------------------------------------------";
        System.out.println(horizontalLine + "\n" + input + "\n" + horizontalLine);
    }

    private static void addTask(Task task) {
        Gray.tasks.add(task);
        if (Gray.tasks.size() == 1) {
            Gray.respond("I've added this task:\n  " + task + "\n"
                    + "You have 1 task in your list. All the best!");
        } else {
            Gray.respond("I've added this task:\n  " + task + "\n" + "You have "
                    + Gray.tasks.size() + " tasks in your list. All the best!");
        }
    }
    
    private static String inBetween(String str1, String str2, String target) {
        String[] firstSplit = target.split(str1, 2);
        if (firstSplit.length == 2) {
            return firstSplit[1].split(str2, 2)[0].trim();
        } else {
            return "";
        }
    }

    private static void checkEvent(String description, String start, String end) throws InvalidTaskException {
        boolean noDescription = description.isEmpty();
        boolean noStart = start.isEmpty();
        boolean noEnd = end.isEmpty();
        if (noDescription && noStart && noEnd) {
            throw new InvalidTaskException("event", "description, start and end date/time");
        } else if (noDescription && noStart) {
            throw new InvalidTaskException("event", "description and start date/time");
        } else if (noDescription && noEnd) {
            throw new InvalidTaskException("event", "description and end date/time");
        } else if (noStart && noEnd) {
            throw new InvalidTaskException("event", "start and end date/time");
        } else if (noDescription) {
            throw new InvalidTaskException("event", "description");
        } else if (noStart) {
            throw new InvalidTaskException("event", "start date/time");
        } else if (noEnd) {
            throw new InvalidTaskException("event", "end date/time");
        }
    }

    public static void main(String[] args) {
        Gray.respond("""
                Hi! I'm Gray, your personal assistant chatbot!
                What can I do for you?""");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                Gray.respond("Bye and see you soon!");
                break;
            }
            String[] inputParts = input.split(" ", 2);
            String command = inputParts[0];
            switch (command) {
                case "list" -> {
                    if (inputParts.length != 1 && !(inputParts[1].trim().isEmpty())) {
                        Gray.respond("""
                                I don't understand what you mean.
                                Please enter a valid instruction.""");
                        break;
                    }
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
                        Gray.respond("Please give the index of the task to be marked.");
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
                        Gray.respond("Please give the index of the task to be unmarked.");
                    }
                }
                case "todo" -> {
                    String description;
                    try {
                        if (inputParts.length != 2 || inputParts[1].trim().isEmpty()) {
                            throw new InvalidTaskException("todo", "description");
                        }
                        description = inputParts[1];
                        Todo todo = new Todo(description);
                        Gray.addTask(todo);
                    } catch (InvalidTaskException e) {
                        Gray.respond(e.getMessage());
                    }
                }
                case "deadline" -> {
                    String description;
                    String by;
                    try {
                        if (inputParts.length != 2 || inputParts[1].trim().isEmpty()) {
                            throw new InvalidTaskException("deadline",
                                    "description and due date/time");
                        } else if (inputParts[1].trim().startsWith("/by")) {
                            if (inputParts[1].split("/by", 2)[1].isEmpty()) {
                                throw new InvalidTaskException("deadline",
                                        "description and due date/time");
                            }
                            throw new InvalidTaskException("deadline", "description");
                        }
                        inputParts = inputParts[1].split("/by", 2);
                        if (inputParts.length != 2 || inputParts[1].trim().isEmpty()) {
                            throw new InvalidTaskException("deadline", "due date/time");
                        }
                        description = inputParts[0].trim();
                        by = inputParts[1].trim();
                        Deadline deadline = new Deadline(description, by);
                        Gray.addTask(deadline);
                    } catch (InvalidTaskException e) {
                        Gray.respond(e.getMessage());
                    }
                }
                case "event" -> {
                    try {
                        String description = Gray.inBetween(" ", "/from", input);
                        if (description.startsWith("/to")) {
                            description = "";
                        }
                        String start = Gray.inBetween("/from", "/to", input);
                        String[] temp = input.split("/to", 2);
                        String end;
                        if (temp.length == 2) {
                            end = temp[1].trim();
                        } else {
                            end = "";
                        }
                        Gray.checkEvent(description, start, end);
                        if (description.contains("/to")) {
                            throw new InvalidTaskException("event", "correct ordering of information");
                        }
                        Event event = new Event(description, start, end);
                        Gray.addTask(event);
                    } catch (InvalidTaskException e) {
                        Gray.respond(e.getMessage());
                    }
                }
                case "delete" -> {
                    if (inputParts.length == 2 && inputParts[1].matches("\\d+")) {
                        try {
                            Task task = Gray.tasks.get(Integer.parseInt(inputParts[1]) - 1);
                            Gray.tasks.remove(Integer.parseInt(inputParts[1]) - 1);
                            if (Gray.tasks.isEmpty()) {
                                Gray.respond("I've deleted this task:\n  " + task + "\n"
                                        + "You have no more tasks left!");
                            } else if (Gray.tasks.size() == 1) {
                                Gray.respond("I've deleted this task:\n  " + task + "\n"
                                        + "You have 1 task in your list. All the best!");
                            } else {
                                Gray.respond("I've deleted this task:\n  " + task + "\n" + "You have "
                                        + Gray.tasks.size() + " tasks in your list. All the best!");
                            }
                        } catch (IndexOutOfBoundsException e) {
                            Gray.respond("This task cannot be found!");
                        }
                    } else {
                        Gray.respond("Please give the index of the task to be deleted.");
                    }
                }
                default -> Gray.respond("""
                        I don't understand what you mean.
                        Please enter a valid instruction.""");
            }
        }
    }
}
