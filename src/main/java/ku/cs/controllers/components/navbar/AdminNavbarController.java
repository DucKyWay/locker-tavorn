package ku.cs.controllers.components.navbar;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ku.cs.components.Icons;
import ku.cs.services.session.SessionManager;
import ku.cs.services.ui.FXRouter;

import java.util.Map;

public class AdminNavbarController extends BaseNavbarController {
    @FXML private Button displayAccountsButton;
    @FXML private Button manageOfficersButton;
    @FXML private Button manageUsersButton;
    @FXML private Button manageLockerZonesButton;
    @FXML private Button logoutButton;
    @FXML private Button manualButton;

    @FXML public void initialize() {
        applyIcon(displayAccountsButton, Icons.HOME, false);
        applyIcon(manageLockerZonesButton, Icons.LOCATION, false);
        applyIcon(manageOfficersButton, Icons.USER_CHECK, false);
        applyIcon(manageUsersButton, Icons.USER, false);
        applyManualIcon(manualButton, Icons.BOOK);
        bindLogout(logoutButton);

        routeOnClick(displayAccountsButton, "admin-home");


        routeOnClick(manageLockerZonesButton, "admin-manage-zones");
        routeOnClick(manageOfficersButton, "admin-manage-officers");
        routeOnClick(manageUsersButton, "admin-manage-users");
        routeOnClick(manualButton, "admin-manual");
        highlightCurrentRoute(Map.of(
                "admin-home", displayAccountsButton,
                "admin-manage-zones", manageLockerZonesButton,
                "admin-display-officer-zones", manageOfficersButton,
                "admin-manage-officers", manageOfficersButton,
                "admin-manage-users", manageUsersButton,
                "admin-manual",manualButton
        ));
    }

    @Override
    public Button getFooterNavButton() {
        return logoutButton;
    }
}
