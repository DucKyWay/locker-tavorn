package ku.cs.controllers.user;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.models.account.Account;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;

import java.io.IOException;

public class UserHomeController {
    @FXML private VBox userHomeLabelContainer;
    @FXML private VBox lockerListButtonContainer;
    @FXML private VBox zoneListButtonContainer;
    private DefaultLabel userHomeLabel;
    private DefaultButton lockerListButton;
    private DefaultButton zoneListButton;
    private Datasource<ZoneList> datasourceZone;
    private ZoneList zoneList;

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
        userHomeLabel = DefaultLabel.h2("Home | " + current.getUsername());
        lockerListButton = DefaultButton.primary("Locker List");
        zoneListButton = DefaultButton.primary(("Zone List"));
        userHomeLabelContainer.getChildren().add(userHomeLabel);
        zoneListButtonContainer.getChildren().add(zoneListButton);
        lockerListButtonContainer.getChildren().add(lockerListButton);
    }

    private void initEvents() {

        lockerListButton.setOnAction(e -> onLockerTableButtonClick());
        zoneListButton.setOnAction(e ->onZonelistTableButtonClick());
    }

    protected void onLockerTableButtonClick() {
        try {
            FXRouter.goTo("locker-list");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    protected void onZonelistTableButtonClick() {
        try {
            FXRouter.goTo("test-zonelist");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
