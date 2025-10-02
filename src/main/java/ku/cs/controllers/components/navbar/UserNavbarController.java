package ku.cs.controllers.components.navbar;

import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ku.cs.components.Icons;

public class UserNavbarController extends BaseNavbarController {
    @FXML private Button lockerPageButton;
    @FXML private Button zonePageButton;
    @FXML private Button historyPageButton;
    @FXML private Button logoutButton;

    @FXML private void initialize() {
        applyIcon(lockerPageButton, Icons.HOME, false);
        applyIcon(zonePageButton, Icons.LOCATION, false);
        applyIcon(historyPageButton, Icons.HISTORY, false);
        bindLogout(logoutButton);

        routeOnClick(lockerPageButton, "user-home");
        routeOnClick(zonePageButton, "user-zone");
        routeOnClick(historyPageButton, "user-history");

        highlightCurrentRoute(Map.of(
                "user-home", lockerPageButton,
                "user-zone", zonePageButton,
                "user-history", historyPageButton
        ));
    }

    @Override
    public Button getFooterNavButton() {
        return logoutButton;
    }
}
