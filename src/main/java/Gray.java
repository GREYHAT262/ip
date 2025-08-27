import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.ArrayList;

public class Gray {
    public enum Command {
        LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, DATE, INVALID;
    }

    public enum TaskType {
        TODO("todo"),
        DEADLINE("deadline"),
        EVENT("event");
        private final String taskType;
        TaskType(String taskType) {
            this.taskType = taskType;
        }
        public String getTaskType() {
            return this.taskType;
        }
    }

    public enum MissingInfo {
        DESCRIPTION("description"),
        DUE("due date/time"),
        START("start date/time"),
        END("end date/time"),
        DESCRIPTION_DUE("description and due date/time"),
        DESCRIPTION_START("description and start date/time"),
        DESCRIPTION_END("description and end date/time"),
        START_END("start and end date/time"),
        DESCRIPTION_START_END("description, start and end date/time"),
        WRONG_ORDER("correct ordering of information");
        private final String missingInfo;
        MissingInfo(String missingInfo) {
            this.missingInfo = missingInfo;
        }
        public String getMissingInfo() {
            return this.missingInfo;
        }
    }

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
            throw new InvalidTaskException(TaskType.EVENT, MissingInfo.DESCRIPTION_START_END);
        } else if (noDescription && noStart) {
            throw new InvalidTaskException(TaskType.EVENT, MissingInfo.DESCRIPTION_START);
        } else if (noDescription && noEnd) {
            throw new InvalidTaskException(TaskType.EVENT, MissingInfo.DESCRIPTION_END);
        } else if (noStart && noEnd) {
            throw new InvalidTaskException(TaskType.EVENT, MissingInfo.START_END);
        } else if (noDescription) {
            throw new InvalidTaskException(TaskType.EVENT, MissingInfo.DESCRIPTION);
        } else if (noStart) {
            throw new InvalidTaskException(TaskType.EVENT, MissingInfo.START);
        } else if (noEnd) {
            throw new InvalidTaskException(TaskType.EVENT, MissingInfo.END);
        }
    }

    private static void list(String[] inputParts) {
        if (inputParts.length != 1 && !(inputParts[1].trim().isEmpty())) {
            Gray.respond("""
                                I don't understand what you mean.
                                Please enter a valid instruction.""");
        } else {
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
    }

    private static void mark(String[] inputParts) {
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

    private static void unmark(String[] inputParts) {
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

    private static void createTodo(String[] inputParts) {
        String description;
        try {
            if (inputParts.length != 2 || inputParts[1].trim().isEmpty()) {
                throw new InvalidTaskException(TaskType.TODO, MissingInfo.DESCRIPTION);
            }
            description = inputParts[1];
            Todo todo = new Todo(description);
            Gray.addTask(todo);
        } catch (InvalidTaskException e) {
            Gray.respond(e.getMessage());
        }
    }

    private static void createDeadline(String[] inputParts) {
        try {
            if (inputParts.length != 2 || inputParts[1].trim().isEmpty()) {
                throw new InvalidTaskException(TaskType.DEADLINE, MissingInfo.DESCRIPTION_DUE);
            } else if (inputParts[1].trim().startsWith("/by")) {
                if (inputParts[1].split("/by", 2)[1].isEmpty()) {
                    throw new InvalidTaskException(TaskType.DEADLINE, MissingInfo.DESCRIPTION_DUE);
                }
                throw new InvalidTaskException(TaskType.DEADLINE, MissingInfo.DESCRIPTION);
            }
            inputParts = inputParts[1].split("/by", 2);
            if (inputParts.length != 2 || inputParts[1].trim().isEmpty()) {
                throw new InvalidTaskException(TaskType.DEADLINE, MissingInfo.DUE);
            }
            String description = inputParts[0].trim();
            LocalDateTime by;
            try {
                by = LocalDateTime.parse(inputParts[1].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
                Deadline deadline = new Deadline(description, by);
                Gray.addTask(deadline);
            } catch (DateTimeParseException e) {
                Gray.respond("""
                            Invalid due date and time!
                            Please use the format yyyy-MM-dd HHmm.""");
            }
        } catch (InvalidTaskException e) {
            Gray.respond(e.getMessage());
        }
    }

    private static void createEvent(String input) {
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
                throw new InvalidTaskException(TaskType.EVENT, MissingInfo.WRONG_ORDER);
            }
            LocalDateTime startDate;
            LocalDateTime endDate;
            try {
                startDate = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
                endDate = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
                Event event = new Event(description, startDate, endDate);
                Gray.addTask(event);
            } catch (DateTimeParseException e) {
                Gray.respond("""
                            Invalid due date and time!
                            Please use the format yyyy-MM-dd HHmm.""");
            }
        } catch (InvalidTaskException e) {
            Gray.respond(e.getMessage());
        }
    }

    public static void delete(String[] inputParts) {
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

    public static void getTasksOn(String[] inputParts) {
        if (inputParts.length != 2 || inputParts[1].trim().isEmpty()) {
            Gray.respond("Please give a date.");
            return;
        }
        LocalDate date;
        try {
            date = LocalDate.parse(inputParts[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            ArrayList<Task> tasksFound = new ArrayList<>();
            for (Task task : Gray.tasks) {
                if (task instanceof Deadline deadline) {
                    if (deadline.correctDateTime(date)) {
                        tasksFound.add(deadline);
                    }
                } else if (task instanceof Event event) {
                    if (event.correctDateTime(date)) {
                        tasksFound.add(event);
                    }
                }
            }
            if (tasksFound.isEmpty()) {
                Gray.respond("There are no tasks on this date.");
            } else {
                StringBuilder taskList = new StringBuilder("Here are the tasks found on "
                        + date.format(DateTimeFormatter.ofPattern("MMM d yyyy")) + ":\n");
                for (int i = 0; i < tasksFound.size(); i++) {
                    if (i != 0) {
                        taskList.append("\n");
                    }
                    Task task = tasksFound.get(i);
                    taskList.append(i + 1).append(".").append(task);
                }
                Gray.respond(taskList.toString());
            }
        } catch (DateTimeParseException e) {
            Gray.respond("Invalid date! Please use the format yyyy-MM-dd.");
        }
    }

    public static void main(String[] args) {
        File dataDir = new File("./data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
        File tasksFile = new File("./data/gray.txt");
        if (!tasksFile.exists()) {
            try {
                tasksFile.createNewFile();
            } catch (IOException e) {
                Gray.respond("Sorry! I'm not able to create the file to store your tasks!");
            }
        }
        try {
            Scanner tasksFileScanner = new Scanner(tasksFile);
            while (tasksFileScanner.hasNextLine()) {
                String entry = tasksFileScanner.nextLine();
                String[] parts = entry.split("\\|");
                if (parts.length < 2) {
                    throw new CorruptedFileException();
                }
                String type = parts[0].trim();
                String mark = parts[1].trim();
                if (!(mark.equals("0") || mark.equals("1"))) {
                    throw new CorruptedFileException();
                }
                switch (type) {
                    case "T" -> {
                        if (parts.length != 3) {
                            throw new CorruptedFileException();
                        }
                        String description = parts[2].trim();
                        Todo todo = new Todo(description);
                        Gray.tasks.add(todo);
                        if (mark.equals("1")) {
                            todo.markAsDone();
                        }
                    }
                    case "D" -> {
                        if (parts.length != 4) {
                            throw new CorruptedFileException();
                        }
                        String description = parts[2].trim();
                        LocalDateTime by;
                        try {
                            by = LocalDateTime.parse(parts[3].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
                        } catch (DateTimeParseException e) {
                            throw new CorruptedFileException();
                        }
                        Deadline deadline = new Deadline(description, by);
                        Gray.tasks.add(deadline);
                        if (mark.equals("1")) {
                            deadline.markAsDone();
                        }
                    }
                    case "E" -> {
                        if (parts.length != 5) {
                            throw new CorruptedFileException();
                        }
                        String description = parts[2].trim();
                        LocalDateTime start;
                        LocalDateTime end;
                        try {
                            start = LocalDateTime.parse(parts[3].trim(),
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
                            end = LocalDateTime.parse(parts[4].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
                        } catch (DateTimeParseException e) {
                            throw new CorruptedFileException();
                        }
                        Event event = new Event(description, start, end);
                        Gray.tasks.add(event);
                        if (mark.equals("1")) {
                            event.markAsDone();
                        }
                    }
                    default -> throw new CorruptedFileException();
                }
            }
        } catch (FileNotFoundException e) {
            Gray.respond("This file is not present.");
        } catch (CorruptedFileException e) {
            Gray.respond(e.getMessage());
            Gray.tasks.clear();
        }
        try {
            FileWriter fileWriter = new FileWriter(tasksFile);
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
                Command command;
                try {
                    command = Command.valueOf(inputParts[0].toUpperCase());
                } catch (IllegalArgumentException e) {
                    command = Command.INVALID;
                }
                switch (command) {
                    case LIST -> Gray.list(inputParts);
                    case MARK -> Gray.mark(inputParts);
                    case UNMARK -> Gray.unmark(inputParts);
                    case TODO -> Gray.createTodo(inputParts);
                    case DEADLINE -> Gray.createDeadline(inputParts);
                    case EVENT -> Gray.createEvent(input);
                    case DELETE -> Gray.delete(inputParts);
                    case DATE -> Gray.getTasksOn(inputParts);
                    default -> Gray.respond("""
                            I don't understand what you mean.
                            Please enter a valid instruction.""");
                }
            }
            for (Task task : Gray.tasks) {
                fileWriter.write(task.toStorage() + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            Gray.respond("Sorry! I couldn't write to your tasks file!");
        }
    }
}
