package ku.cs.controllers.components;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import ku.cs.components.Icons;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.models.account.Account;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.session.SessionManager;

import java.io.IOException;

public class HeaderController {
    private final SessionManager sessionManager = (SessionManager) FXRouter.getService("session");
    @FXML private Button lockerTavornButton;
    @FXML private HBox headerHBox;

    private Account acc;

    @FXML public void initialize() {
        acc = sessionManager.getCurrentAccount();
        initUserInterfaces();
        initEvents();
    }

    private void initUserInterfaces() {
        FilledButtonWithIcon.SMALL.mask(lockerTavornButton, Icons.LOCK);
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
