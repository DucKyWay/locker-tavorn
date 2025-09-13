package ku.cs.controllers.officer;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.controllers.components.SettingDropdownController;
import ku.cs.models.account.Account;
import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.models.locker.KeyType;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.LockerListFileDatasource;
import ku.cs.services.datasources.OfficerListFileDatasource;
import ku.cs.services.datasources.ZoneListFileDatasource;

import java.io.FileNotFoundException;
import java.io.IOException;

public class OfficerHomeController {
    @FXML private VBox officerHomeLabelContainer;

    @FXML private SettingDropdownController settingsContainerController;
    @FXML private HBox backButtonContainer;
    @FXML private TextField zoneTextFieldContainer;
    @FXML private TextField lockerTextFieldContainer;
    private DefaultLabel officerHomeLabel;
    private DefaultButton lockerListButton;
    //test intitial Zone
    private Datasource<ZoneList> datasourceZone;
    private ZoneList zoneList;
    private Datasource<LockerList> datasourceLocker;
    private Datasource<OfficerList> datasourceOfficer;
    private Account account;
    private OfficerList officerList;
    private LockerList lockerList;
    private Officer officer;
    @FXML
    public void initialize() {
        // Auth Guard
        SessionManager.requireOfficerLogin();
        account = SessionManager.getCurrentAccount();
        initialDatasourceZone();
        initUserInterface();
        initEvents();
        initialDatasourceOfficerList();
        initialDatasourceLockerList();
    }
    private void initialDatasourceZone(){
        datasourceZone = new ZoneListFileDatasource("data", "test-zone-data.json");
        try {
            zoneList = datasourceZone.readData();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private void initialDatasourceOfficerList(){
        datasourceOfficer = new OfficerListFileDatasource("data", "test-officer-data.json");
        try {
            officerList = datasourceOfficer.readData();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        officer = officerList.findOfficerByUsername(account.getUsername());
    }
    private void initialDatasourceLockerList(){
        datasourceLocker = new LockerListFileDatasource("data/lockers", "zone-"+Integer.toString( ( ( zoneList.findZoneByName( officer.getServiceZone() ) ).getIdZone()) )+".json");
        try {
            lockerList = datasourceLocker.readData();

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
    @FXML
    protected void onAddLockerManual(){
        Locker locker = new Locker(KeyType.MANUAL,officer.getServiceZone());
        lockerList.addLocker(locker);
        try {
            datasourceLocker.writeData(lockerList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void onAddLockerChain(){
        Locker locker = new Locker(KeyType.CHAIN,officer.getServiceZone());
        lockerList.addLocker(locker);
        try {
            datasourceLocker.writeData(lockerList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void onAddLockerDigital(){
        Locker locker = new Locker(KeyType.DIGITAL,officer.getServiceZone());
        lockerList.addLocker(locker);
        try {
            datasourceLocker.writeData(lockerList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
