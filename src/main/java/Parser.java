import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Parser {
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

    public static boolean isExit = false;

    private static void add(TaskList taskList, Task task) {
        taskList.add(task);
        Ui ui = new Ui();
        ui.showAddTask(task, taskList.size());
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

    private static void list(TaskList taskList, String[] inputParts) {
        Ui ui = new Ui();
        if (inputParts.length != 1 && !(inputParts[1].trim().isEmpty())) {
            ui.showInvalidInstruction();
        } else {
            ui.showTasks(taskList);
        }
    }

    private static void mark(TaskList taskList, String[] inputParts) {
        Ui ui = new Ui();
        if (inputParts.length == 2 && inputParts[1].matches("\\d+")) {
            try {
                int index = Integer.parseInt(inputParts[1]) - 1;
                taskList.mark(index);
                ui.showMarkTask(taskList.get(index));
            } catch (IndexOutOfBoundsException e) {
                ui.showTaskNotFound();
            }
        } else {
            ui.showNoIndex();
        }
    }

    private static void unmark(TaskList taskList, String[] inputParts) {
        Ui ui = new Ui();
        if (inputParts.length == 2 && inputParts[1].matches("\\d+")) {
            try {
                int index = Integer.parseInt(inputParts[1]) - 1;
                taskList.unmark(index);
                ui.showUnmarkTask(taskList.get(index));
            } catch (IndexOutOfBoundsException e) {
                ui.showTaskNotFound();
            }
        } else {
            ui.showNoIndex();
        }
    }

    private static void createTodo(TaskList taskList, String[] inputParts) {
        Ui ui = new Ui();
        String description;
        try {
            if (inputParts.length != 2 || inputParts[1].trim().isEmpty()) {
                throw new InvalidTaskException(Parser.TaskType.TODO, Parser.MissingInfo.DESCRIPTION);
            }
            description = inputParts[1];
            Todo todo = new Todo(description);
            Parser.add(taskList, todo);
        } catch (InvalidTaskException e) {
            ui.showInvalidTaskException(e);
        }
    }

    private static void createDeadline(TaskList taskList, String[] inputParts) {
        Ui ui = new Ui();
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
            LocalDateTime by;
            try {
                by = LocalDateTime.parse(inputParts[1].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
                Deadline deadline = new Deadline(description, by);
                Parser.add(taskList, deadline);
            } catch (DateTimeParseException e) {
                ui.showInvalidDateAndTime();
            }
        } catch (InvalidTaskException e) {
            ui.showInvalidTaskException(e);
        }
    }

    private static void createEvent(TaskList taskList, String input) {
        Ui ui = new Ui();
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
            LocalDateTime startDate;
            LocalDateTime endDate;
            try {
                startDate = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
                endDate = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
                Event event = new Event(description, startDate, endDate);
                Parser.add(taskList, event);
            } catch (DateTimeParseException e) {
                ui.showInvalidDateAndTime();
            }
        } catch (InvalidTaskException e) {
            ui.showInvalidTaskException(e);
        }
    }

    private static void delete(TaskList taskList, String[] inputParts) {
        Ui ui = new Ui();
        if (inputParts.length == 2 && inputParts[1].matches("\\d+")) {
            try {
                int index = Integer.parseInt(inputParts[1]) - 1;
                ui.showDeleteTask(taskList.delete(index), taskList.size());
            } catch (IndexOutOfBoundsException e) {
                ui.showTaskNotFound();
            }
        } else {
            ui.showNoIndex();
        }
    }

    private static void getTasksOn(TaskList taskList, String[] inputParts) {
        Ui ui = new Ui();
        if (inputParts.length != 2 || inputParts[1].trim().isEmpty()) {
            ui.showNoDate();
            return;
        }
        LocalDate date;
        try {
            date = LocalDate.parse(inputParts[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            ui.showTasksOnDate(taskList.filterByDate(date), date);
        } catch (DateTimeParseException e) {
            ui.showInvalidDate();
        }
    }

    public static void parse(TaskList taskList, String input) {
        Ui ui = new Ui();
        if (input.equals("bye")) {
            ui.showGoodbye();
            Parser.isExit = true;
            return;
        }
        String[] inputParts = input.split(" ", 2);
        Parser.Command command;
        try {
            command = Parser.Command.valueOf(inputParts[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            command = Parser.Command.INVALID;
        }
        switch (command) {
            case LIST -> Parser.list(taskList, inputParts);
            case MARK -> Parser.mark(taskList, inputParts);
            case UNMARK -> Parser.unmark(taskList, inputParts);
            case TODO -> Parser.createTodo(taskList, inputParts);
            case DEADLINE -> Parser.createDeadline(taskList, inputParts);
            case EVENT -> Parser.createEvent(taskList, input);
            case DELETE -> Parser.delete(taskList, inputParts);
            case DATE -> Parser.getTasksOn(taskList, inputParts);
            default -> ui.showInvalidInstruction();
        }
    }
}
