package gray.ui;

import java.io.FileNotFoundException;
import java.io.IOException;

import gray.command.Command;
import gray.command.InvalidCommand;
import gray.command.InvalidDateCommand;
import gray.command.InvalidDateTimeCommand;
import gray.command.InvalidTaskExceptionCommand;
import gray.command.NoDateCommand;
import gray.command.NoIndexCommand;
import gray.command.UnexpectedDateTimeCommand;
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
     * Provides a response based on user input.
     * @param input User input.
     * @return Chatbot response.
     */
    public String getResponse(String input) {
        try {
            Command c = Parser.parse(input);

            assert tasks != null : "tasks should be initialised";
            assert ui != null : "ui should be initialised";
            assert storage != null : "storage should be initialised";

            String output = c.execute(tasks, ui, storage);
            storage.save(tasks);
            return output;
        } catch (IOException e) {
            return ui.showWriteFileError();
        }
    }

    /**
     * Determines if input results in an error.
     * @param input User input.
     * @return Whether input results in an error.
     */
    public boolean isError(String input) {
        Command c = Parser.parse(input);
        return c instanceof InvalidCommand || c instanceof InvalidDateCommand || c instanceof InvalidDateTimeCommand
                || c instanceof InvalidTaskExceptionCommand || c instanceof NoDateCommand
                || c instanceof NoIndexCommand || c instanceof UnexpectedDateTimeCommand;
    }
}
