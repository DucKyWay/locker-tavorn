package ku.cs.controllers.officer.DialogPane;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Window;
import ku.cs.components.button.ElevatedButton;
import ku.cs.components.button.FilledButton;
import ku.cs.models.account.Officer;
import ku.cs.models.key.KeyList;
import ku.cs.models.key.Key;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.request.RequestType;
import ku.cs.models.zone.Zone;
import ku.cs.services.datasources.provider.KeyDatasourceProvider;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;
import ku.cs.services.datasources.provider.RequestDatasourceProvider;
import ku.cs.services.request.RequestService;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.session.SessionManager;
import ku.cs.services.utils.TableColumnFactory;
import ku.cs.services.zone.ZoneService;
import ku.cs.services.utils.AlertUtil;

import java.io.IOException;
import java.time.LocalDateTime;

public class SelectKeyDialogPaneController {
    private final SessionManager sessionManager = (SessionManager) FXRouter.getService("session");

    private final RequestDatasourceProvider requestsProvider = new RequestDatasourceProvider();
    private final LockerDatasourceProvider lockersProvider = new LockerDatasourceProvider();
    private final KeyDatasourceProvider keysProvider = new KeyDatasourceProvider();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();
    private final RequestService requestService = new RequestService();
    @FXML
    private DialogPane selectKeyDialogPane;
    @FXML private TableView<Key> keylockerTableView;
    @FXML private Button cancelButton;
    @FXML private Button confirmButton;
    @FXML Label requestUidLabel;
    @FXML Label lockerUidLabel;
    @FXML Label userNameLabel;
    private KeyList keyList;
    private RequestList requestList;
    private LockerList lockerList;
    private Request request;
    private Zone zone;
    private Key currentKey;
    private Locker currentLocker;
    private Officer officer;
    private final ZoneService zoneService = new ZoneService();
    @FXML
    public void initialize() {

        officer = sessionManager.getOfficer();
        request = (Request) FXRouter.getData();

        zone = zoneService.findZoneByUid(request.getZoneUid());
        initialDatasource();
        initUserInterface();
        initEvents();
        showTable(keyList);

        keylockerTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                currentKey = newValue;
            }
        });
    }
    private void initialDatasource() {
        keyList = keysProvider.loadCollection(zone.getZoneUid());

        requestList = requestsProvider.loadCollection(zone.getZoneUid());

        lockerList = lockersProvider.loadCollection(zone.getZoneUid());
        currentLocker = lockerList.findLockerByUid(request.getLockerUid());

    }

    private void showTable(KeyList keyList) {
        keylockerTableView.getColumns().clear();
        keylockerTableView.getItems().clear();

        keylockerTableView.getColumns().setAll(
                tableColumnFactory.createEnumStatusColumn("ประเภทกุญแจ", "keyType", 0),
                tableColumnFactory.createTextColumn("เลขกุญแจ", "keyUid"),
                tableColumnFactory.createTextColumn("รหัสกุญแจ", "passkey"),
                tableColumnFactory.createTextColumn("เลขล็อคเกอร์", "lockerUid"),
                tableColumnFactory.createTextColumn("สถานะกุญแจ", "available")

        );

        keylockerTableView.getItems().addAll(
                keyList.getKeys().stream()
                        .filter(Key::isAvailable) // เฉพาะที่ available == true
                        .toList()
        );
    }

    private void initEvents() {
        cancelButton.setOnAction(e -> onCancelButtonClick());
        confirmButton.setOnAction(e -> onConfirmButtonClick());
    }
    private void initUserInterface() {
        ElevatedButton.MEDIUM.mask(cancelButton);
        FilledButton.MEDIUM.mask(confirmButton);
        selectKeyDialogPane.getButtonTypes().clear();
        requestUidLabel.setText(request.getRequestUid());
        lockerUidLabel.setText(request.getLockerUid());
        userNameLabel.setText(request.getUserUsername());
    }
    private void onCancelButtonClick(){
        Window window = selectKeyDialogPane.getScene().getWindow();}
    private void onConfirmButtonClick(){
        Request oldRequest = requestList.findRequestByUid(request.getRequestUid());
        currentKey = keyList.findKeyByUid(currentKey.getKeyUid());
        currentKey.setAvailable(false);
        currentKey.setLockerUid(request.getLockerUid());

        currentLocker.setAvailable(false);
        oldRequest.setRequestType(RequestType.APPROVE);
        oldRequest.setRequestTime(LocalDateTime.now());
        oldRequest.setOfficerUsername(officer.getUsername());
        oldRequest.setLockerKeyUid(currentKey.getKeyUid());
        requestList = requestService.checkIsBooked(oldRequest,requestList);
        requestsProvider.saveCollection(zone.getZoneUid(), requestList);

        keysProvider.saveCollection(zone.getZoneUid(), keyList);
        lockersProvider.saveCollection(zone.getZoneUid(), lockerList);

        new AlertUtil().info("ยืนยันสำเร็จ", request.getUserUsername() + " ได้ทำการจองสำเร็จ ");
        try {
            FXRouter.goTo("officer-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Window window = selectKeyDialogPane.getScene().getWindow();
        window.hide();
    }
}
