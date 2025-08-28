package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.AccessibleAction;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.controllers.components.SettingDropdownController;
import ku.cs.models.Account;
import ku.cs.models.Officer;
import ku.cs.models.OfficerList;
import ku.cs.models.ZoneList;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.ZoneListFileDatasource;

import java.io.FileNotFoundException;
import java.io.IOException;

public class OfficerHomeController {
    @FXML private VBox officerHomeLabelContainer;

    @FXML private SettingDropdownController settingsContainerController;

    @FXML private TextField zoneTextFieldContainer;
    private DefaultLabel officerHomeLabel;
    private DefaultButton lockerListButton;
    //test intitial Zone
    private Datasource<ZoneList> datasourceZone;
    private ZoneList zoneList;

    private Account account;

    @FXML
    public void initialize() {
        // Auth Guard
        SessionManager.requireOfficerLogin();
        account = SessionManager.getCurrentAccount();

        initialDatasourceZone();
        initUserInterface();
        initEvents();
    }
    private void initialDatasourceZone(){
        datasourceZone = new ZoneListFileDatasource("data", "test-zone-data.json");
        try {
            zoneList = datasourceZone.readData();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private void initUserInterface() {
        officerHomeLabel = DefaultLabel.h2("Home | Officer " + account.getUsername());
        lockerListButton = DefaultButton.primary("Locker List");

        officerHomeLabelContainer.getChildren().add(officerHomeLabel);
    }

    private void initEvents() {
        lockerListButton.setOnAction(e -> onLockerTableButtonClick());
    }

    protected void onLockerTableButtonClick() {
        try {
            FXRouter.goTo("locker-list");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onAddZoneClick(){
        String zone_string = zoneTextFieldContainer.getText();
        zoneList.addZone(zone_string);
        try {
            datasourceZone.writeData(zoneList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Current Zone List: " + zoneList.getZones());
    }
}
