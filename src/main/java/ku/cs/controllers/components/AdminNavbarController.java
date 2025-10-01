package ku.cs.controllers.components;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import ku.cs.components.Icons;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.services.ui.FXRouter;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.services.context.AppContext;
import ku.cs.services.session.SessionManager;
import ku.cs.services.utils.AlertUtil;

import java.io.IOException;

public class AdminNavbarController {
    private final SessionManager sessionManager = AppContext.getSessionManager();
    private final AlertUtil alertUtil = new AlertUtil();

    @FXML private Button displayAccountsButton;
    @FXML private Button manageOfficersButton;
    @FXML private Button manageUsersButton;
    @FXML private Button manageLockerZonesButton;
    @FXML private Button logoutButton;

    @FXML public void initialize() {
        initUserInterfaces();
        initEvents();
    }

    private void initUserInterfaces() {
        System.out.println("current Route: " + FXRouter.getCurrentRouteLabel());
        switch (FXRouter.getCurrentRouteLabel()){
            case "admin-display-accounts":
                displayAccountsButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
                break;
            case "admin-manage-zones":
                manageLockerZonesButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
                break;
            case "admin-manage-officers":
                manageOfficersButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
                break;
            case "admin-manage-users":
                manageUsersButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
                break;
            default:
                break;
        }
        ElevatedButtonWithIcon.SMALL.mask(displayAccountsButton, Icons.HOME);
        ElevatedButtonWithIcon.SMALL.mask(manageLockerZonesButton, Icons.LOCATION);
        ElevatedButtonWithIcon.SMALL.mask(manageOfficersButton, Icons.USER_CHECK);
        ElevatedButtonWithIcon.SMALL.mask(manageUsersButton, Icons.USER);
        FilledButtonWithIcon.SMALL.mask(logoutButton, Icons.SIGN_OUT , Icons.NULL);
    }

    private void initEvents() {
        displayAccountsButton.setOnAction(e -> onDisplayAccountsButtonClick());
        manageOfficersButton.setOnAction(e -> onManageOfficersButtonClick());
        manageUsersButton.setOnAction(e -> onManageUsersButtonClick());
        manageLockerZonesButton.setOnAction(e -> onManageLockerZonesButtonClick());
        logoutButton.setOnAction(e -> onLogoutButtonClick());

    }

    protected void onDisplayAccountsButtonClick() {
        try {
            FXRouter.goTo("admin-display-accounts");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void onManageOfficersButtonClick() {
        try {
            FXRouter.goTo("admin-manage-officers");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void onManageUsersButtonClick() {
        try {
            FXRouter.goTo("admin-manage-users");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void onManageLockerZonesButtonClick() {
        try {
            FXRouter.goTo("admin-manage-zones");
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
