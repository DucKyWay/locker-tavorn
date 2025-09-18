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
import ku.cs.models.locker.*;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.UpdateZoneService;
import ku.cs.services.datasources.*;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;

import java.io.IOException;
import java.time.LocalDateTime;

public class OfficerHomeController {
    @FXML private VBox officerHomeLabelContainer;

    @FXML private SettingDropdownController settingsContainerController;
    @FXML private HBox backButtonContainer;
    @FXML private TextField zoneTextFieldContainer;
    @FXML private TextField lockerTextFieldContainer;
    private DefaultLabel officerHomeLabel;
    private DefaultButton lockerListButton;
    //test DateList
    private Datasource<DateList> datasourceDateList;
    private DateList dateList;



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
        initialDatasourceDateList();
    }
    private void initialDatasourceDateList(){
        datasourceDateList = new DateListFileDatasource("data", "test-date-list-data.json");
        dateList = datasourceDateList.readData();
    }

    private void initialDatasourceZone(){
        datasourceZone = new ZoneListFileDatasource("data", "test-zone-data.json");
        zoneList = datasourceZone.readData();
        UpdateZoneService.setLockerToZone(zoneList);
    }


    private void initialDatasourceOfficerList(){
        datasourceOfficer = new OfficerListFileDatasource("data", "test-officer-data.json");
        officerList = datasourceOfficer.readData();
        officer = officerList.findOfficerByUsername(account.getUsername());
    }
    private void initialDatasourceLockerList(){
        datasourceLocker =
                new LockerListFileDatasource(
                        "data/lockers",
                        "zone-" + zoneList.findZoneByName(officer.getServiceZone()).getIdZone() + ".json"
                );
        lockerList = datasourceLocker.readData();

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
        datasourceZone.writeData(zoneList);
        System.out.println("Current Zone List: " + zoneList.getZones());
    }
    @FXML
    protected void onAddLockerManual(){
        Locker locker = new Locker(KeyType.MANUAL,officer.getServiceZone());
        lockerList.addLocker(locker);
        Date date = new Date(locker.getUuid());
        dateList.addDateList(date);

        datasourceDateList.writeData(dateList);
        datasourceLocker.writeData(lockerList);
        UpdateZoneService.setLockerToZone(zoneList);
    }
    @FXML
    protected void onAddLockerChain(){
        Locker locker = new Locker(KeyType.CHAIN,officer.getServiceZone());
        lockerList.addLocker(locker);
        Date date = new Date(locker.getUuid());
        dateList.addDateList(date);

        datasourceDateList.writeData(dateList);
        datasourceLocker.writeData(lockerList);
        UpdateZoneService.setLockerToZone(zoneList);
    }
    @FXML
    protected void onAddLockerDigital(){
        Locker locker = new Locker(KeyType.DIGITAL,officer.getServiceZone());
        lockerList.addLocker(locker);
        Date date = new Date(locker.getUuid());
        dateList.addDateList(date);

        datasourceDateList.writeData(dateList);
        datasourceLocker.writeData(lockerList);
        UpdateZoneService.setLockerToZone(zoneList);
    }
}
