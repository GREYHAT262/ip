package gray.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

import gray.exception.CorruptedFileException;
import gray.task.Deadline;
import gray.task.Event;
import gray.task.Task;
import gray.task.TaskList;
import gray.task.Todo;

/**
 * Stores and loads tasks entered by user in previous sessions.
 */
public class Storage {
    private final File file;

    /**
     * Creates a new storage.
     *
     * @param ui Ui used for printing chatbot responses.
     * @param filePath Location of file used for storage.
     */
    public Storage(Ui ui, String filePath) {
        // Solution below adapted from ChatGPT
        int idx = filePath.lastIndexOf("/");
        String directoryPath = filePath.substring(0, idx);
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdir();
        }

        file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                ui.showFileCreationError();
            }
        }
    }

    private void checkLength(String[] parts, int length) throws CorruptedFileException {
        if (parts.length != length) {
            throw new CorruptedFileException();
        }
    }

    private void checkMark(String mark) throws CorruptedFileException {
        if (!(mark.equals("0") || mark.equals("1"))) {
            throw new CorruptedFileException();
        }
    }

    private void addStatus(Task task, String mark) {
        if (mark.equals("1")) {
            task.markAsDone();
        }
    }

    private Todo loadTodo(String[] parts, ArrayList<Task> tasks, String mark) {
        String description = parts[2].trim();
        Todo todo = new Todo(description);
        tasks.add(todo);
        return todo;
    }

    private Deadline loadDeadline(String[] parts, ArrayList<Task> tasks, String mark) {
        String description = parts[2].trim();
        LocalDateTime by;

        try {
            by = LocalDateTime.parse(parts[3].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        } catch (DateTimeParseException e) {
            throw new CorruptedFileException();
        }

        Deadline deadline = new Deadline(description, by);
        tasks.add(deadline);
        return deadline;
    }

    private Event loadEvent(String[] parts, ArrayList<Task> tasks, String mark) {
        String description = parts[2].trim();
        LocalDateTime start;
        LocalDateTime end;

        try {
            start = LocalDateTime.parse(parts[3].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            end = LocalDateTime.parse(parts[4].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        } catch (DateTimeParseException e) {
            throw new CorruptedFileException();
        }

        Event event = new Event(description, start, end);
        tasks.add(event);
        return event;
    }

    /**
     * Loads tasks stored in file into chatbot.
     *
     * @return ArrayList of Task objects.
     * @throws FileNotFoundException If file used for storage cannot be found.
     * @throws CorruptedFileException If contents of file is not in the required format.
     */
    public ArrayList<Task> load() throws FileNotFoundException, CorruptedFileException {
        Scanner scanner = new Scanner(file);
        ArrayList<Task> tasks = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String entry = scanner.nextLine();
            String[] parts = entry.split("\\|");

            if (parts.length < 2) {
                throw new CorruptedFileException();
            }

            String type = parts[0].trim();
            String mark = parts[1].trim();

            checkMark(mark);

            //CHECKSTYLE.OFF: Indentation
            switch (type) {
                case "T" -> {
                    checkLength(parts, 3);
                    Todo todo = loadTodo(parts, tasks, mark);
                    addStatus(todo, mark);
                }
                case "D" -> {
                    checkLength(parts, 4);
                    Deadline deadline = loadDeadline(parts, tasks, mark);
                    addStatus(deadline, mark);
                }
                case "E" -> {
                    checkLength(parts, 5);
                    Event event = loadEvent(parts, tasks, mark);
                    addStatus(event, mark);
                }
                default -> throw new CorruptedFileException();
            }
            //CHECKSTYLE.ON: Indentation
        }
        return tasks;
    }

    /**
     * Saves tasks in taskList to the file.
     *
     * @param taskList A list of tasks the user wants to save.
     * @throws IOException If FileWriter object fails to write to the file.
     */
    public void save(TaskList taskList) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(taskList.toStorage());
        fileWriter.close();
    }
}
