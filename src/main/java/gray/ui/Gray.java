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
    private Ui ui;
    private Storage storage;
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
     * Provides a response based on user input.
     * @param input User input.
     * @return Chatbot response.
     */
    public String getResponse(String input) {
        try {
            Command c = Parser.parse(input);
            String output = c.execute(tasks, ui, storage);
            storage.save(tasks);
            return output;
        } catch (IOException e) {
            return ui.showWriteFileError();
        }
    }
}
