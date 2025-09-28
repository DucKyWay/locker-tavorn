package ku.cs.controllers.components;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import ku.cs.components.Icons;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.models.account.Account;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;

import java.io.IOException;

public class HeaderController {
    @FXML private Button lockerTavornButton;
    @FXML private HBox headerHBox;

    private Account acc;

    @FXML public void initialize() {
        acc = SessionManager.getCurrentAccount();
        initUserInterfaces();
        initEvents();
    }

    private void initUserInterfaces() {
        ElevatedButtonWithIcon.SMALL.mask(lockerTavornButton, Icons.LOCK);

        Label userLabel = new Label("Hello, " + acc.getFirstname() + "!");
        LabelStyle.BODY_MEDIUM.applyTo(userLabel);

        headerHBox.setPadding(new Insets(0, 10, 0, 0));
        headerHBox.getChildren().add(userLabel);
    }

    private void initEvents() {
        lockerTavornButton.setOnAction(e -> onLogoButtonClick());
    }

    private void onLogoButtonClick() {
        try {
            FXRouter.goTo(acc.getRole().toString().toLowerCase() + "-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
