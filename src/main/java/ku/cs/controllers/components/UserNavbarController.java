package ku.cs.controllers.components;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import ku.cs.services.utils.AlertUtil;
import ku.cs.components.Icons;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.services.AppContext;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;

import java.io.IOException;

public class UserNavbarController {
    private final SessionManager sessionManager = AppContext.getSessionManager();
    private final AlertUtil alertUtil = new AlertUtil();

    @FXML private Button lockerPageButton;
    @FXML private Button zonePageButton;
    @FXML private Button historyPageButton;
    @FXML private Button logoutButton;

    @FXML private void initialize() {
        initUserInterfaces();
        initEvents();
    };

    private void initUserInterfaces() {
        System.out.println("current Route: " + FXRouter.getCurrentRouteLabel());
        switch (FXRouter.getCurrentRouteLabel()){
            case "user-home":
                lockerPageButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
                break;
            case "user-zone":
                zonePageButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
                break;
            case "user-history":
                historyPageButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
                break;
            default:
                break;
        }
        ElevatedButtonWithIcon.SMALL.mask(lockerPageButton, Icons.HOME);
        ElevatedButtonWithIcon.SMALL.mask(zonePageButton, Icons.LOCATION);
        ElevatedButtonWithIcon.SMALL.mask(historyPageButton, Icons.HISTORY);
        FilledButtonWithIcon.SMALL.mask(logoutButton, Icons.SIGN_OUT);
    }

    protected void initEvents() {
        lockerPageButton.setOnAction(e-> onLockerButtonClick());
        zonePageButton.setOnAction(e -> onZoneButtonClick());
        historyPageButton.setOnAction(e -> onHistoryButtonClick());
        logoutButton.setOnAction(e -> onLogoutButtonClick());
    }

    public Button getFooterNavButton() {
        return logoutButton;
    }

    protected void onLockerButtonClick() {
        try {
            FXRouter.goTo("user-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void onLockerPageButtonClick() {
        try {
            FXRouter.goTo("user-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void onZoneButtonClick() {
        try {
            FXRouter.goTo("user-zone");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void onHistoryButtonClick() {
        try {
            FXRouter.goTo("user-history");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void onLogoutButtonClick() {
        alertUtil.confirm("Confirm Logout", "คุณต้องการออกจากระบบหรือไม่?")
                .ifPresent(btn -> {
                    if (btn == ButtonType.OK) {
                        sessionManager.logout();
                    }
                });
    }
}
