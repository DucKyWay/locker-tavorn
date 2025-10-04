package ku.cs.controllers.components.navbar;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ku.cs.components.Icons;

import java.util.Map;

public class AdminNavbarController extends BaseNavbarController {
    @FXML private Button displayAccountsButton;
    @FXML private Button manageOfficersButton;
    @FXML private Button manageUsersButton;
    @FXML private Button manageLockerZonesButton;
    @FXML private Button logoutButton;

    @FXML public void initialize() {
        applyIcon(displayAccountsButton, Icons.USER, false);
        applyIcon(manageOfficersButton, Icons.EDIT, false);
        applyIcon(manageUsersButton, Icons.EDIT, false);
        applyIcon(manageLockerZonesButton, Icons.LOCATION, false);
        bindLogout(logoutButton);

        routeOnClick(displayAccountsButton, "admin-display-accounts");
        routeOnClick(manageOfficersButton, "admin-manage-officers");
        routeOnClick(manageUsersButton, "admin-manage-users");
        routeOnClick(manageLockerZonesButton, "admin-manage-zones");

        highlightCurrentRoute(Map.of(
                "admin-display-accounts", displayAccountsButton,
                "admin-manage-officers", manageOfficersButton,
                "admin-manage-users", manageUsersButton,
                "admin-manage-zones", manageLockerZonesButton
        ));
    }

    @Override
    public Button getFooterNavButton() {
        return logoutButton;
    }
}
