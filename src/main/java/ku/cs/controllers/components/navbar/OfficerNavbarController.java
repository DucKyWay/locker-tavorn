package ku.cs.controllers.components.navbar;

import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ku.cs.components.Icons;
import ku.cs.models.zone.Zone;
import ku.cs.services.ui.FXRouter;

public class OfficerNavbarController extends BaseNavbarController {
    @FXML private Button zoneRequestButton;
    @FXML private Button addLockerButton;
    @FXML private Button lockerHistoryButton;
    @FXML private Button logoutButton;

    private Zone currentZone;

    @FXML public void initialize() {
        currentZone = (Zone) FXRouter.getData();

        applyIcon(zoneRequestButton, Icons.GEAR, false);
        applyIcon(addLockerButton, Icons.LOCKER, false);
        applyIcon(lockerHistoryButton, Icons.HISTORY, false);
        bindLogout(logoutButton);

        routeOnClick(zoneRequestButton, "officer-zone-request", currentZone);
        routeOnClick(addLockerButton, "officer-manage-lockers", currentZone);
        routeOnClick(lockerHistoryButton, "officer-history-request", currentZone);

        highlightCurrentRoute(Map.of(
                "officer-zone-request", zoneRequestButton,
                "officer-manage-lockers", addLockerButton,
                "officer-history-request", lockerHistoryButton
        ));
    }

    @Override
    public Button getFooterNavButton() {
        return logoutButton;
    }
}