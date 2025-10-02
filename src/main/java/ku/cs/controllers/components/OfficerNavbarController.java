package ku.cs.controllers.components;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import ku.cs.components.Icons;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.services.context.AppContext;
import ku.cs.services.session.SessionManager;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.AlertUtil;

import java.io.IOException;

public class OfficerNavbarController {
    private final SessionManager sessionManager = AppContext.getSessionManager();
    private final AlertUtil alertUtil = new AlertUtil();

    @FXML private Button addLockerButton;
    @FXML private Button lockerHistoryButton;
    @FXML private Button logoutButton;

    @FXML public void initialize() {
        initUserInterfaces();
        initEvents();
    }

    private void initUserInterfaces() {
        System.out.println("current Route: " + FXRouter.getCurrentRouteLabel());
        switch (FXRouter.getCurrentRouteLabel()) {
            case "officer-locker-add":
                addLockerButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
                break;
            case "officer-locker-history":
                lockerHistoryButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
                break;
            default:
                break;
        }
        ElevatedButtonWithIcon.SMALL.mask(addLockerButton, Icons.VAULT);
        ElevatedButtonWithIcon.SMALL.mask(lockerHistoryButton, Icons.HISTORY);
        FilledButtonWithIcon.SMALL.mask(logoutButton, Icons.SIGN_OUT);
    }

    private void initEvents() {
        addLockerButton.setOnAction(e -> onAddLockerButtonClick());
        lockerHistoryButton.setOnAction(e -> onLockerHistoryButtonClick());
        logoutButton.setOnAction(e -> onLogoutButtonClick());
    }

    public Button getFooterNavButton() {
        return logoutButton;
    }

    private void onAddLockerButtonClick() {
        try {
            FXRouter.goTo("officer-locker-add");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void onLockerHistoryButtonClick() {
        try {
            FXRouter.goTo("officer-locker-history");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void onLogoutButtonClick() {
        alertUtil.confirm("ยืนยันการออกจากระบบ", "คุณต้องการออกจากระบบหรือไม่?")
                .ifPresent(btn -> {
                    if (btn == ButtonType.OK) {
                        sessionManager.logout();
                    }
                });
    }
}
