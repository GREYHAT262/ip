package gray.gui;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import gray.ui.Gray;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Gray gray;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/user.png"));
    private Image grayImage = new Image(this.getClass().getResourceAsStream("/images/gray.png"));

    /**
     * Initialises the GUI.
     * Shows a welcome message from the chatbot.
     */
    @FXML
    public void initialize() {
        assert userImage != null: "userImage not found";
        assert grayImage != null: "grayImage not found";

        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        showWelcome();
    }

    /** Injects the Gray instance */
    public void setGray(Gray gray) {
        this.gray = gray;
    }

    @FXML
    private void showWelcome() {
        dialogContainer.getChildren().addAll(
                DialogBox.getGrayDialog("""
                Hi! I'm Gray, your personal assistant chatbot!
                What can I do for you?""", grayImage)
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = gray.getResponse(input);
        if (gray.isError(input)) {
            dialogContainer.getChildren().addAll(
                    DialogBox.getUserDialog(input, userImage),
                    DialogBox.getErrorDialog("Error: " + response, grayImage)
            );
        } else {
            dialogContainer.getChildren().addAll(
                    DialogBox.getUserDialog(input, userImage),
                    DialogBox.getGrayDialog(response, grayImage)
            );
        }
        userInput.clear();
        if (input.equals("bye")) {
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> Platform.exit());
            pause.play();
        }
    }
}
