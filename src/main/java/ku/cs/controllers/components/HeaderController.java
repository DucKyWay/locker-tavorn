package ku.cs.controllers.components;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ku.cs.models.account.Account;
import ku.cs.services.AccountService;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;

import java.io.IOException;

public class HeaderController {
    @FXML private Button lockerTavornButton;
    @FXML private Button changeThemeButton;

    @FXML public void initialize() {
        initEvents();
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
