package ku.cs.controllers.user;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ku.cs.components.Icons;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.models.account.Account;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;

import java.io.IOException;

public class UserHomeController {
    @FXML private Button lockerPageButton;
    @FXML private Button zonePageButton;
    @FXML private Button historyPageButton;

    Account current = SessionManager.getCurrentAccount();

    @FXML
    public void initialize() {
        // Auth Guard
        SessionManager.requireUserLogin();
        current = SessionManager.getCurrentAccount();

        initialDatasourceZone();
        initUserInterface();
        initEvents();
    }
    private void initialDatasourceZone(){
        // thinking what to use
    }

    private void initUserInterface() {
        lockerPageButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
        ElevatedButtonWithIcon.SMALL.mask(lockerPageButton, Icons.HOME);
        ElevatedButtonWithIcon.SMALL.mask(zonePageButton, Icons.LOCATION);
        ElevatedButtonWithIcon.SMALL.mask(historyPageButton, Icons.HISTORY);
    }

    private void initEvents() {
        zonePageButton.setOnMouseClicked(e ->onZoneButtonClick());
    }

    protected void onZoneButtonClick() {
        try {
            FXRouter.goTo("test-zonelist");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    protected void onHomeButtonClick() {
        try {
            FXRouter.goTo("test-zonelist");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
