import java.io.FileNotFoundException;
import java.io.IOException;

public class Gray {
    private final Ui ui;
    private final Storage storage;
    private TaskList tasks;

    public Gray(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
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
                Parser.parse(this.tasks, input);
                this.storage.save(this.tasks);
                isExit = Parser.isExit;
            } catch (IOException e) {
                this.ui.showWriteFileError();
            }
        }
    }

    public static void main(String[] args) {
        new Gray("./data/gray.txt").run();
    }
}
