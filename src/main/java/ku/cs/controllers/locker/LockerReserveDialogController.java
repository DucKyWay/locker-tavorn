package ku.cs.controllers.locker;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.components.button.ElevatedButton;
import ku.cs.components.button.FilledButton;

public class LockerReserveDialogController {

    @FXML private DialogPane lockerReserveDialogPane;

    @FXML private ImageView lockerImage;

    @FXML private Label lockerNumberLabel;
    @FXML private Label lockerIdLabel;
    @FXML private Label lockerZoneLabel;
    @FXML private Label lockerTypeLabel;

    @FXML private VBox usernameTextFieldVBox;
    @FXML private Label usernameLabel;
    @FXML private ChoiceBox<String> startDateChoiceBox;

    @FXML private VBox usernameTextFieldVBox1;
    @FXML private Label usernameLabel1;
    @FXML private ChoiceBox<String> endDateChoiceBox;

    @FXML private Button cancelButton;
    @FXML private Button confirmButton;

    @FXML
    private void initialize() {
        initUserInterface();
        initEvents();
    }

    private void initUserInterface() {
        ElevatedButton.MEDIUM.mask(cancelButton);
        FilledButton.MEDIUM.mask(confirmButton);
        lockerReserveDialogPane.getButtonTypes().clear();
    }

    private void initEvents() {
        cancelButton.setOnAction(e -> onCancelButtonClick());
    }

    private void onCancelButtonClick(){
            Window window = lockerReserveDialogPane.getScene().getWindow();
            window.hide();
    }

}
