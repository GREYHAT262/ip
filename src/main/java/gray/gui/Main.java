package gray.gui;

import java.io.IOException;

import gray.ui.Gray;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Duke using FXML.
 */
public class Main extends Application {

    private final Gray gray = new Gray("./data/gray.txt");

    /**
     * Starts the GUI.
     * @param stage Desktop window.
     */
    @Override
    public void start(Stage stage) {
        try {
            stage.setTitle("GrayPro");
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/gray.png")));
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setGray(gray); // inject the Gray instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
