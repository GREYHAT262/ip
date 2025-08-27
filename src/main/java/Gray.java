import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Gray {
    private final Ui ui;
    private final Storage storage;
    public static ArrayList<Task> tasks;

    public Gray(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        try {
            Gray.tasks = storage.load();
        } catch (FileNotFoundException e) {
            ui.showNoFile();
        } catch (CorruptedFileException e) {
            ui.showLoadingError(e);
            Gray.tasks.clear();
        }
    }

    public void run() {
        this.ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String input = ui.readCommand();
                Parser.parse(input);
                this.storage.save(Gray.tasks);
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
