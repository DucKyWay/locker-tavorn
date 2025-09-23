package ku.cs.controllers.components;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ku.cs.components.Icons;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.models.account.Account;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;

import java.io.IOException;

public class HeaderController {
    @FXML private Button lockerTavornButton;

    @FXML public void initialize() {
        initUserInterfaces();
        initEvents();
    }

    private void initUserInterfaces() {
        ElevatedButtonWithIcon.SMALL.mask(lockerTavornButton, Icons.LOCK);
    }

    private void initEvents() {
        lockerTavornButton.setOnAction(e -> onLogoButtonClick());
    }

    private void onLogoButtonClick() {
        Account acc = SessionManager.getCurrentAccount();
        try {
            FXRouter.goTo(acc.getRole().toString().toLowerCase() + "-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
