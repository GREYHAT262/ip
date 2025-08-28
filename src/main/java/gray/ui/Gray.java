package gray.ui;

import gray.command.Command;
import gray.exception.CorruptedFileException;
import gray.task.TaskList;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Gray {
    private final Ui ui;
    private final Storage storage;
    private TaskList tasks;

    public Gray(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(ui, filePath);
        try {
            this.tasks = new TaskList(storage.load());
        } catch (FileNotFoundException e) {
            ui.showNoFile();
        } catch (CorruptedFileException e) {
            ui.showLoadingError(e);
            this.tasks = new TaskList();
        }
    }

    public void run() {
        this.ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String input = ui.readCommand();
                Command c = Parser.parse(input);
                c.execute(tasks, ui, storage);
                this.storage.save(this.tasks);
                isExit = c.isExit();
            } catch (IOException e) {
                this.ui.showWriteFileError();
            }
        }
    }

    public static void main(String[] args) {
        new Gray("./data/gray.txt").run();
    }
}
