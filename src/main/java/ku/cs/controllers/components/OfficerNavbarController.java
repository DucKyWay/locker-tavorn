package ku.cs.controllers.components;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import ku.cs.components.Icons;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.models.zone.Zone;
import ku.cs.services.context.AppContext;
import ku.cs.services.session.SessionManager;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.AlertUtil;

import java.io.IOException;

public class OfficerNavbarController {
    private final SessionManager sessionManager = AppContext.getSessionManager();
    private final AlertUtil alertUtil = new AlertUtil();

    private Zone currentZone;

    @FXML private Button addLockerButton;
    @FXML private Button lockerHistoryButton;
    @FXML private Button logoutButton;

    @FXML public void initialize() {
        currentZone = (Zone) FXRouter.getData();
        initUserInterfaces();
        initEvents();
    }

    private void initUserInterfaces() {
        System.out.println("current Route: " + FXRouter.getCurrentRouteLabel());
        switch (FXRouter.getCurrentRouteLabel()) {
            case "officer-manage-lockers":
                addLockerButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
                break;
            case "officer-history-request":
                lockerHistoryButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
                break;
            default:
                break;
        }
        ElevatedButtonWithIcon.SMALL.mask(addLockerButton, Icons.LOCKER);
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
            FXRouter.goTo("officer-manage-lockers", currentZone);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void onLockerHistoryButtonClick() {
        try {
            FXRouter.goTo("officer-history-request", currentZone);
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
