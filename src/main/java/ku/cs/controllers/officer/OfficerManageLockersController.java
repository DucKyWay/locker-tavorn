package ku.cs.controllers.officer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.components.button.CustomButton;
import ku.cs.models.account.Officer;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.locker.LockerSizeType;
import ku.cs.models.locker.LockerType;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.accounts.strategy.OfficerAccountProvider;
import ku.cs.services.context.AppContext;
import ku.cs.services.datasources.provider.KeyDatasourceProvider;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;
import ku.cs.services.datasources.provider.RequestDatasourceProvider;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.session.SelectedDayService;
import ku.cs.services.session.SessionManager;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.zone.ZoneService;

import java.io.IOException;

public class OfficerManageLockersController extends BaseOfficerController{
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final LockerDatasourceProvider lockersProvider = new LockerDatasourceProvider();
    private final KeyDatasourceProvider keysProvider = new KeyDatasourceProvider();
    private final ZoneService zoneService = new ZoneService();

    @FXML private VBox headerVBoxContainer;

    private Button lockerListButton;

    private ZoneList zones;
    private LockerList lockers;

    @Override
    protected void initDatasource() {
        zones = zonesProvider.loadCollection();
        lockers = lockersProvider.loadCollection(currentZone.getZoneUid());
    }

    @Override
    protected void initUserInterfaces() {
        Label officerHomeLabel = DefaultLabel.h2("Home | Officer " + current.getUsername());
        lockerListButton = new CustomButton("Locker List");
        headerVBoxContainer.getChildren().addAll(officerHomeLabel, lockerListButton);
    }

    @Override
    protected void initEvents() {
        lockerListButton.setOnAction(e -> onLockerTableButtonClick());
    }

    protected void onLockerTableButtonClick() {
        try {
            FXRouter.goTo("officer-locker", currentZone);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onAddLockerManual(){
        Zone zone = zones.findZoneByUid(currentZone.getZoneUid());
        Locker locker = new Locker(LockerType.MANUAL, LockerSizeType.MEDIUM, zone.getZoneName());
        lockers.addLocker(locker);

        lockersProvider.saveCollection(zone.getZoneUid(), lockers);
        zoneService.setLockerToZone(zones);
    }

    @FXML
    protected void onAddKeyChain(){
        try {
            FXRouter.goTo("officer-key-list", current);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onAddLockerDigital(){
        Zone zone = zones.findZoneByUid(currentZone.getZoneUid());
        Locker locker = new Locker(LockerType.DIGITAL, LockerSizeType.MEDIUM, zone.getZoneName());
        lockers.addLocker(locker);

        lockersProvider.saveCollection(zone.getZoneUid(), lockers);
        zoneService.setLockerToZone(zones);
    }

    @FXML
    protected void onLockerClick(){
        try {
            FXRouter.goTo("officer-locker", currentZone);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onBackClick(){
        try {
            FXRouter.goTo("officer-zone-list");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
