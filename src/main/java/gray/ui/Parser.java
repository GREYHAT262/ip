package gray.ui;

import gray.exception.InvalidTaskException;
import gray.command.AddCommand;
import gray.command.ByeCommand;
import gray.command.Command;
import gray.command.DateCommand;
import gray.command.DeleteCommand;
import gray.command.InvalidCommand;
import gray.command.ListCommand;
import gray.command.MarkCommand;
import gray.command.UnmarkCommand;
import gray.task.Deadline;
import gray.task.Event;
import gray.task.Todo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parses user input into different command types.
 */
public class Parser {
    /**
     * Represents possible commands from a user.
     */
    public enum CommandType {
        LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, DATE, INVALID;
    }

    /**
     * Represents 3 types of tasks: todo, deadline and event.
     */
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

    /**
     * Represents possible combinations of missing arguments during task instantiation.
     */
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

    /**
     * Returns string between two regex.
     *
     * @param str1 First regex.
     * @param str2 Second regex.
     * @param target String to be split.
     */
    private static String inBetween(String str1, String str2, String target) {
        String[] firstSplit = target.split(str1, 2);
        if (firstSplit.length == 2) {
            return firstSplit[1].split(str2, 2)[0].trim();
        } else {
            return "";
        }
    }

    /**
     * Checks if all required arguments to create an Event object are present.
     *
     * @param description Description of event.
     * @param start Start time of event.
     * @param end End time of event.
     * @throws InvalidTaskException If any one of description, start and end is empty.
     */
    private static void checkEvent(String description, String start, String end) throws InvalidTaskException {
        boolean noDescription = description.isEmpty();
        boolean noStart = start.isEmpty();
        boolean noEnd = end.isEmpty();
        if (noDescription && noStart && noEnd) {
            throw new InvalidTaskException(Parser.TaskType.EVENT, Parser.MissingInfo.DESCRIPTION_START_END);
        } else if (noDescription && noStart) {
            throw new InvalidTaskException(Parser.TaskType.EVENT, Parser.MissingInfo.DESCRIPTION_START);
        } else if (noDescription && noEnd) {
            throw new InvalidTaskException(Parser.TaskType.EVENT, Parser.MissingInfo.DESCRIPTION_END);
        } else if (noStart && noEnd) {
            throw new InvalidTaskException(Parser.TaskType.EVENT, Parser.MissingInfo.START_END);
        } else if (noDescription) {
            throw new InvalidTaskException(Parser.TaskType.EVENT, Parser.MissingInfo.DESCRIPTION);
        } else if (noStart) {
            throw new InvalidTaskException(Parser.TaskType.EVENT, Parser.MissingInfo.START);
        } else if (noEnd) {
            throw new InvalidTaskException(Parser.TaskType.EVENT, Parser.MissingInfo.END);
        }
    }

    private static Command list(String[] inputParts) {
        if (inputParts.length == 1 || inputParts[1].trim().isEmpty()) {
            return new ListCommand();
        } else {
            return new InvalidCommand();
        }
    }

    private static MarkCommand mark(String[] inputParts) {
        if (inputParts.length == 2 && inputParts[1].matches("\\d+")) {
            int index = Integer.parseInt(inputParts[1]) - 1;
            return new MarkCommand(index);
        } else {
            return new MarkCommand();
        }
    }

    private static UnmarkCommand unmark(String[] inputParts) {
        if (inputParts.length == 2 && inputParts[1].matches("\\d+")) {
            int index = Integer.parseInt(inputParts[1]) - 1;
            return new UnmarkCommand(index);
        } else {
            return new UnmarkCommand();
        }
    }

    private static DeleteCommand delete(String[] inputParts) {
        if (inputParts.length == 2 && inputParts[1].matches("\\d+")) {
            int index = Integer.parseInt(inputParts[1]) - 1;
            return new DeleteCommand(index);
        } else {
            return new DeleteCommand();
        }
    }

    private static AddCommand createTodo(String[] inputParts) {
        try {
            if (inputParts.length != 2 || inputParts[1].trim().isEmpty()) {
                throw new InvalidTaskException(Parser.TaskType.TODO, Parser.MissingInfo.DESCRIPTION);
            }
            String description = inputParts[1].trim();
            Todo todo = new Todo(description);
            return new AddCommand(todo);
        } catch (InvalidTaskException e) {
            return new AddCommand(e);
        }
    }

    private static AddCommand createDeadline(String[] inputParts) {
        try {
            if (inputParts.length != 2 || inputParts[1].trim().isEmpty()) {
                throw new InvalidTaskException(Parser.TaskType.DEADLINE, Parser.MissingInfo.DESCRIPTION_DUE);
            } else if (inputParts[1].trim().startsWith("/by")) {
                if (inputParts[1].split("/by", 2)[1].isEmpty()) {
                    throw new InvalidTaskException(Parser.TaskType.DEADLINE, Parser.MissingInfo.DESCRIPTION_DUE);
                }
                throw new InvalidTaskException(Parser.TaskType.DEADLINE, Parser.MissingInfo.DESCRIPTION);
            }
            inputParts = inputParts[1].split("/by", 2);
            if (inputParts.length != 2 || inputParts[1].trim().isEmpty()) {
                throw new InvalidTaskException(Parser.TaskType.DEADLINE, Parser.MissingInfo.DUE);
            }
            String description = inputParts[0].trim();
            LocalDateTime by = LocalDateTime.parse(inputParts[1].trim(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            Deadline deadline = new Deadline(description, by);
            return new AddCommand(deadline);
        } catch (InvalidTaskException e) {
            return new AddCommand(e);
        } catch (DateTimeParseException e) {
            return new AddCommand();
        }
    }

    private static AddCommand createEvent(String input) {
        try {
            String description = Parser.inBetween(" ", "/from", input);
            if (description.startsWith("/to")) {
                description = "";
            }
            String start = Parser.inBetween("/from", "/to", input);
            String[] temp = input.split("/to", 2);
            String end;
            if (temp.length == 2) {
                end = temp[1].trim();
            } else {
                end = "";
            }
            Parser.checkEvent(description, start, end);
            if (description.contains("/to")) {
                throw new InvalidTaskException(Parser.TaskType.EVENT, Parser.MissingInfo.WRONG_ORDER);
            }
            LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            Event event = new Event(description, startDate, endDate);
            return new AddCommand(event);
        } catch (InvalidTaskException e) {
            return new AddCommand(e);
        } catch (DateTimeParseException e) {
            return new AddCommand();
        }
    }

    private static DateCommand getTasksOn(String[] inputParts) {
        if (inputParts.length != 2 || inputParts[1].trim().isEmpty()) {
            return new DateCommand(false);
        }
        try {
            LocalDate date = LocalDate.parse(inputParts[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return new DateCommand(date);
        } catch (DateTimeParseException e) {
            return new DateCommand(true);
        }
    }

    /**
     * Returns a Command object to be executed.
     * The subtype of Command is determined by user input.
     *
     * @param input User input.
     */
    public static Command parse(String input) {
        if (input.trim().equals("bye")) {
            return new ByeCommand();
        }
        String[] inputParts = input.split(" ", 2);
        Parser.CommandType command;
        try {
            command = Parser.CommandType.valueOf(inputParts[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            command = Parser.CommandType.INVALID;
        }
        return switch (command) {
            case LIST -> Parser.list(inputParts);
            case MARK -> Parser.mark(inputParts);
            case UNMARK -> Parser.unmark(inputParts);
            case TODO -> Parser.createTodo(inputParts);
            case DEADLINE -> Parser.createDeadline(inputParts);
            case EVENT -> Parser.createEvent(input);
            case DELETE -> Parser.delete(inputParts);
            case DATE -> Parser.getTasksOn(inputParts);
            default -> new InvalidCommand();
        };
    }
}
