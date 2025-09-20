package gray.gui;

import gray.ui.Gray;
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

    //Image from https://www.flaticon.com/free-icon/user_1077114
    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/user.png"));

    //Image from https://www.flaticon.com/free-icon/bot_4712109
    private final Image grayImage = new Image(this.getClass().getResourceAsStream("/images/gray.png"));

    /**
     * Initialises the GUI.
     * Shows a welcome message from the chatbot.
     */
    @FXML
    public void initialize() {
        assert userImage != null : "userImage not found";
        assert grayImage != null : "grayImage not found";

        //@@author salmonkarp-reused
        //Reused from https://github.com/nus-cs2103-AY2526S1/forum/issues/171
        dialogContainer.heightProperty().addListener((obs, oldVal, newVal) -> {
            scrollPane.setVvalue(1.0);
        });
        showWelcome();
        //@@author
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
                    DialogBox.getErrorDialog(response, grayImage)
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
