package gray.ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import gray.command.*;
import gray.exception.InvalidTaskException;
import gray.task.Deadline;
import gray.task.Event;
import gray.task.Todo;

/**
 * Parses user input into different command types.
 */
public class Parser {
    /**
     * Represents possible commands from a user.
     */
    public enum CommandType {
        LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, DATE, FIND, INVALID;
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
            return taskType;
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
            return missingInfo;
        }
    }

    /**
     * Returns string between two regex.
     *
     * @param str1 First regex.
     * @param str2 Second regex.
     * @param target String to be split.
     */
    private static String getStringBetweenRegexes(String str1, String str2, String target) {
        String[] firstSplit = target.split(str1, 2);
        if (firstSplit.length != 2) {
            return "";
        }
        String result = firstSplit[1].split(str2, 2)[0];
        return result.trim();
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
        boolean invalidDescription = description.isEmpty() || description.startsWith("/to");
        boolean noStart = start.isEmpty();
        boolean noEnd = end.isEmpty();

        if (invalidDescription && noStart && noEnd) {
            throw new InvalidTaskException(Parser.TaskType.EVENT, Parser.MissingInfo.DESCRIPTION_START_END);
        } else if (invalidDescription && noStart) {
            throw new InvalidTaskException(Parser.TaskType.EVENT, Parser.MissingInfo.DESCRIPTION_START);
        } else if (invalidDescription && noEnd) {
            throw new InvalidTaskException(Parser.TaskType.EVENT, Parser.MissingInfo.DESCRIPTION_END);
        } else if (noStart && noEnd) {
            throw new InvalidTaskException(Parser.TaskType.EVENT, Parser.MissingInfo.START_END);
        } else if (invalidDescription) {
            throw new InvalidTaskException(Parser.TaskType.EVENT, Parser.MissingInfo.DESCRIPTION);
        } else if (noStart) {
            throw new InvalidTaskException(Parser.TaskType.EVENT, Parser.MissingInfo.START);
        } else if (noEnd) {
            throw new InvalidTaskException(Parser.TaskType.EVENT, Parser.MissingInfo.END);
        } else if (description.contains("/to")) {
            throw new InvalidTaskException(Parser.TaskType.EVENT, Parser.MissingInfo.WRONG_ORDER);
        }
    }

    private static Command list(String[] inputParts) {
        if (inputParts.length != 1 && !inputParts[1].trim().isEmpty()) {
            return new InvalidCommand();
        }
        return new ListCommand();
    }

    private static Command mark(String[] inputParts) {
        if (inputParts.length != 2 || !inputParts[1].matches("\\d+")) {
            return new NoIndexCommand();
        }
        int index = Integer.parseInt(inputParts[1]) - 1;
        return new MarkCommand(index);
    }

    private static Command unmark(String[] inputParts) {
        if (inputParts.length != 2 || !inputParts[1].matches("\\d+")) {
            return new NoIndexCommand();
        }
        int index = Integer.parseInt(inputParts[1]) - 1;
        return new UnmarkCommand(index);
    }

    private static Command delete(String[] inputParts) {
        if (inputParts.length != 2 || !inputParts[1].matches("\\d+")) {
            return new NoIndexCommand();
        }
        int index = Integer.parseInt(inputParts[1]) - 1;
        return new DeleteCommand(index);
    }

    private static Command addTodo(String[] inputParts) {
        try {
            if (inputParts.length != 2 || inputParts[1].trim().isEmpty()) {
                throw new InvalidTaskException(Parser.TaskType.TODO, Parser.MissingInfo.DESCRIPTION);
            }
            String description = inputParts[1].trim();
            Todo todo = new Todo(description);
            return new AddCommand(todo);
        } catch (InvalidTaskException e) {
            return new InvalidTaskExceptionCommand(e);
        }
    }

    private static void checkDeadline(String[] inputParts) throws InvalidTaskException {
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
    }

    private static Deadline createDeadline(String[] inputParts) {
        inputParts = inputParts[1].split("/by", 2);
        String description = inputParts[0].trim();
        String dateTime = inputParts[1].trim();
        LocalDateTime by = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        return new Deadline(description, by);
    }

    private static Command addDeadline(String[] inputParts) {
        try {
            checkDeadline(inputParts);
            Deadline deadline = createDeadline(inputParts);
            return new AddCommand(deadline);
        } catch (InvalidTaskException e) {
            return new InvalidTaskExceptionCommand(e);
        } catch (DateTimeParseException e) {
            return new InvalidDateTimeCommand();
        }
    }

    private static String getEnd(String input) {
        String[] temp = input.split("/to", 2);
        String end;
        if (temp.length == 2) {
            end = temp[1].trim();
        } else {
            end = "";
        }
        return end;
    }

    private static Event createEvent(String description, String start, String end) {
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        return new Event(description, startDate, endDate);
    }

    private static Command addEvent(String input) {
        try {
            String description = Parser.getStringBetweenRegexes(" ", "/from", input);
            String start = Parser.getStringBetweenRegexes("/from", "/to", input);
            String end = getEnd(input);

            Parser.checkEvent(description, start, end);

            Event event = createEvent(description, start, end);
            return new AddCommand(event);
        } catch (InvalidTaskException e) {
            return new InvalidTaskExceptionCommand(e);
        } catch (DateTimeParseException e) {
            return new InvalidDateTimeCommand();
        }
    }

    private static Command getTasksOn(String[] inputParts) {
        if (inputParts.length != 2 || inputParts[1].trim().isEmpty()) {
            return new NoDateCommand();
        }

        try {
            LocalDate date = LocalDate.parse(inputParts[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return new DateCommand(date);
        } catch (DateTimeParseException e) {
            return new InvalidDateCommand();
        }
    }

    private static Command find(String[] inputParts) {
        if (inputParts.length != 2 || inputParts[1].trim().isEmpty()) {
            return new InvalidCommand();
        }
        return new FindCommand(inputParts[1].trim());
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

        //CHECKSTYLE.OFF: Indentation
        return switch (command) {
            case LIST -> Parser.list(inputParts);
            case MARK -> Parser.mark(inputParts);
            case UNMARK -> Parser.unmark(inputParts);
            case TODO -> Parser.addTodo(inputParts);
            case DEADLINE -> Parser.addDeadline(inputParts);
            case EVENT -> Parser.addEvent(input);
            case DELETE -> Parser.delete(inputParts);
            case DATE -> Parser.getTasksOn(inputParts);
            case FIND -> Parser.find(inputParts);
            default -> new InvalidCommand();
        };
        //CHECKSTYLE.ON: Indentation
    }
}
