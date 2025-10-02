package ku.cs.controllers.components.navbar;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ku.cs.components.Icons;

public class AdminNavbarController extends BaseNavbarController {
    @FXML private Button displayAccountsButton;
    @FXML private Button manageOfficersButton;
    @FXML private Button manageUsersButton;
    @FXML private Button manageLockerZonesButton;
    @FXML private Button footerNavButton;

    @FXML public void initialize() {
        applyIcon(displayAccountsButton, Icons.USER, false);
        applyIcon(manageOfficersButton, Icons.EDIT, false);
        applyIcon(manageUsersButton, Icons.EDIT, false);
        applyIcon(manageLockerZonesButton, Icons.LOCATION, false);

        routeOnClick(displayAccountsButton, "admin-display-accounts");
        routeOnClick(manageOfficersButton, "admin-manage-officers");
        routeOnClick(manageUsersButton, "admin-manage-users");
        routeOnClick(manageLockerZonesButton, "admin-manage-zones");
    }

    @Override
    public Button getFooterNavButton() {
        return footerNavButton;
    }
}
