package gray.ui;

import gray.exception.CorruptedFileException;
import gray.task.Deadline;
import gray.task.Event;
import gray.task.Task;
import gray.task.TaskList;
import gray.task.Todo;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Stores and loads tasks entered by user in previous sessions.
 */
public class Storage {
    File file;
    FileWriter fileWriter;

    /**
     * Creates a new storage.
     *
     * @param ui Ui used for printing chatbot responses.
     * @param filePath Location of file used for storage.
     */
    public Storage(Ui ui, String filePath) {
        int idx = filePath.lastIndexOf("/");
        String directoryPath = filePath.substring(0, idx);
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdir();
        }
        this.file = new File(filePath);
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                ui.showFileCreationError();
            }
        }
    }

    /**
     * Load tasks stored in file into chatbot.
     *
     * @return ArrayList of Task objects.
     * @throws FileNotFoundException If file used for storage cannot be found.
     * @throws CorruptedFileException If contents of file is not in the required format.
     */
    public ArrayList<Task> load() throws FileNotFoundException, CorruptedFileException {
        Scanner scanner = new Scanner(this.file);
        ArrayList<Task> tasks = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String entry = scanner.nextLine();
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
                    tasks.add(todo);
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
                    tasks.add(deadline);
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
                    tasks.add(event);
                    if (mark.equals("1")) {
                        event.markAsDone();
                    }
                }
                default -> throw new CorruptedFileException();
            }
        }
        return tasks;
    }

    /**
     * Saves tasks in taskList to the file.
     *
     * @throws IOException If FileWriter object fails to write to the file.
     */
    public void save(TaskList taskList) throws IOException {
        this.fileWriter = new FileWriter(this.file);
        this.fileWriter.write(taskList.toStorage());
        this.fileWriter.close();
    }
}
