package gray.ui;

import java.io.FileNotFoundException;
import java.io.IOException;

import gray.command.Command;
import gray.exception.CorruptedFileException;
import gray.task.TaskList;

/**
 * Represents a chatbot which manages users' tasks.
 */
public class Gray {
    private final Ui ui;
    private final Storage storage;
    private TaskList tasks;

    /**
     * Creates the chatbot Gray.
     *
     * @param filePath Location of the file where tasks would be loaded from and saved to.
     */
    public Gray(String filePath) {
        ui = new Ui();
        storage = new Storage(ui, filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (FileNotFoundException e) {
            ui.showNoFile();
        } catch (CorruptedFileException e) {
            ui.showLoadingError(e);
            tasks = new TaskList();
        }
    }

    /**
     * Runs the chatbot Gray.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String input = ui.readCommand();
                Command c = Parser.parse(input);
                c.execute(tasks, ui, storage);
                storage.save(tasks);
                isExit = c.isExit();
            } catch (IOException e) {
                ui.showWriteFileError();
            }
        }
    }

    /**
     * Acts as the starting point of the chatbot Gray.
     */
    public static void main(String[] args) {
        new Gray("./data/gray.txt").run();
    }
}
