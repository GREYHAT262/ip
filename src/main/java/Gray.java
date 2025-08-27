import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    private static ArrayList<Task> tasks = new ArrayList<>();

    private static void addTask(Task task) {
        Gray.tasks.add(task);
        Ui ui = new Ui();
        ui.showAddTask(task, Gray.tasks.size());
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
        Ui ui = new Ui();
        if (inputParts.length != 1 && !(inputParts[1].trim().isEmpty())) {
            ui.showInvalidInstruction();
        } else {
            ui.showTasks(Gray.tasks);
        }
    }

    private static void mark(String[] inputParts) {
        Ui ui = new Ui();
        if (inputParts.length == 2 && inputParts[1].matches("\\d+")) {
            try {
                Task task = Gray.tasks.get(Integer.parseInt(inputParts[1]) - 1);
                task.markAsDone();
                ui.showMarkTask(task);
            } catch (IndexOutOfBoundsException e) {
                ui.showTaskNotFound();
            }
        } else {
            ui.showNoIndex();
        }
    }

    private static void unmark(String[] inputParts) {
        Ui ui = new Ui();
        if (inputParts.length == 2 && inputParts[1].matches("\\d+")) {
            try {
                Task task = Gray.tasks.get(Integer.parseInt(inputParts[1]) - 1);
                task.markAsNotDone();
                ui.showUnmarkTask(task);
            } catch (IndexOutOfBoundsException e) {
                ui.showTaskNotFound();
            }
        } else {
            ui.showNoIndex();
        }
    }

    private static void createTodo(String[] inputParts) {
        Ui ui = new Ui();
        String description;
        try {
            if (inputParts.length != 2 || inputParts[1].trim().isEmpty()) {
                throw new InvalidTaskException(TaskType.TODO, MissingInfo.DESCRIPTION);
            }
            description = inputParts[1];
            Todo todo = new Todo(description);
            Gray.addTask(todo);
        } catch (InvalidTaskException e) {
            ui.showInvalidTaskException(e);
        }
    }

    private static void createDeadline(String[] inputParts) {
        Ui ui = new Ui();
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
                ui.showInvalidDateAndTime();
            }
        } catch (InvalidTaskException e) {
            ui.showInvalidTaskException(e);
        }
    }

    private static void createEvent(String input) {
        Ui ui = new Ui();
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
                ui.showInvalidDateAndTime();
            }
        } catch (InvalidTaskException e) {
            ui.showInvalidTaskException(e);
        }
    }

    public static void delete(String[] inputParts) {
        Ui ui = new Ui();
        if (inputParts.length == 2 && inputParts[1].matches("\\d+")) {
            try {
                Task task = Gray.tasks.get(Integer.parseInt(inputParts[1]) - 1);
                Gray.tasks.remove(Integer.parseInt(inputParts[1]) - 1);
                ui.showDeleteTask(task, Gray.tasks.size());
            } catch (IndexOutOfBoundsException e) {
                ui.showTaskNotFound();
            }
        } else {
            ui.showNoIndex();
        }
    }

    public static void getTasksOn(String[] inputParts) {
        Ui ui = new Ui();
        if (inputParts.length != 2 || inputParts[1].trim().isEmpty()) {
            ui.showNoDate();
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
            ui.showTasksOnDate(tasksFound, date);
        } catch (DateTimeParseException e) {
            ui.showInvalidDate();
        }
    }

    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage("./data/gray.txt");
        try {
            Gray.tasks = storage.load();
            ui.showWelcome();
            while (true) {
                String input = ui.readCommand();
                if (input.equals("bye")) {
                    ui.showGoodbye();
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
                    default -> ui.showInvalidInstruction();
                }
            }
            storage.save(Gray.tasks);
        } catch (FileNotFoundException e) {
            ui.showNoFile();
        } catch (IOException e) {
            ui.showWriteFileError();
        } catch (CorruptedFileException e) {
            ui.showLoadingError(e);
            Gray.tasks.clear();
        }
    }
}
