package ku.cs.controllers.components.navbar;

import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ku.cs.components.Icons;
import ku.cs.models.zone.Zone;
import ku.cs.services.ui.FXRouter;

public class OfficerNavbarController extends BaseNavbarController {
    @FXML private Button zoneRequestButton;
    @FXML private Button manageLockerButton;
    @FXML private Button manageKeyButton;
    @FXML private Button lockerHistoryButton;
    @FXML private Button zoneSelectButton;
    @FXML private Button logoutButton;

    private Zone currentZone;

    @FXML public void initialize() {
        currentZone = (Zone) FXRouter.getData();

        applyIcon(zoneRequestButton, Icons.BELL, false);
        applyIcon(manageLockerButton, Icons.LOCKER, false);
        applyIcon(manageKeyButton, Icons.KEY, false);
        applyIcon(lockerHistoryButton, Icons.HISTORY, false);
        applyIcon(zoneSelectButton, Icons.LOCATION, true);
        bindLogout(logoutButton);

        routeOnClick(zoneRequestButton, "officer-zone-request", currentZone);
        routeOnClick(manageLockerButton, "officer-manage-lockers", currentZone);
        routeOnClick(manageKeyButton, "officer-manage-keys", currentZone);
        routeOnClick(lockerHistoryButton, "officer-history-request", currentZone);

        highlightCurrentRoute(Map.of(
                "officer-zone-request", zoneRequestButton,
                "officer-manage-lockers", manageLockerButton,
                "officer-manage-keys", manageKeyButton,
                "officer-history-request", lockerHistoryButton
        ));
    }

    @Override
    public Button getFooterNavButton() {
        return logoutButton;
    }
}