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

    @FXML public void initialize() {
        applyIcon(displayAccountsButton, Icons.HOME, false);
        applyIcon(manageLockerZonesButton, Icons.LOCATION, false);
        applyIcon(manageOfficersButton, Icons.USER_CHECK, false);
        applyIcon(manageUsersButton, Icons.USER, false);
        bindLogout(logoutButton);

        routeOnClick(displayAccountsButton, "admin-home");
        routeOnClick(manageLockerZonesButton, "admin-manage-zones");
        routeOnClick(manageOfficersButton, "admin-manage-officers");
        routeOnClick(manageUsersButton, "admin-manage-users");

        highlightCurrentRoute(Map.of(
                "admin-home", displayAccountsButton,
                "admin-manage-zones", manageLockerZonesButton,
                "admin-display-officer-zones", manageOfficersButton,
                "admin-manage-officers", manageOfficersButton,
                "admin-manage-users", manageUsersButton
        ));
    }

    @Override
    public Button getFooterNavButton() {
        return logoutButton;
    }
}
