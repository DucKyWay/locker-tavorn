package ku.cs.controllers.officer.DialogPane;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Window;
import ku.cs.components.button.ElevatedButton;
import ku.cs.components.button.FilledButton;
import ku.cs.models.account.Officer;
import ku.cs.models.key.KeyList;
import ku.cs.models.key.Key;
import ku.cs.models.key.KeyType;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.request.RequestType;
import ku.cs.models.zone.Zone;
import ku.cs.services.context.AppContext;
import ku.cs.services.datasources.provider.KeyDatasourceProvider;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;
import ku.cs.services.datasources.provider.RequestDatasourceProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.session.SessionManager;
import ku.cs.services.zone.ZoneService;
import ku.cs.services.datasources.*;
import ku.cs.services.utils.AlertUtil;

import java.io.IOException;
import java.time.LocalDateTime;

public class SelectKeyDialogPaneController {
    private final SessionManager sessionManager = AppContext.getSessionManager();
    private final RequestDatasourceProvider requestsProvider = new RequestDatasourceProvider();
    private final LockerDatasourceProvider lockersProvider = new LockerDatasourceProvider();
    private final KeyDatasourceProvider keysProvider = new KeyDatasourceProvider();
    private final AlertUtil alertUtil = new AlertUtil();

    @FXML
    private DialogPane selectKeyDialogPane;
    @FXML private TableView<Key> keylockerTableView;
    @FXML private Button cancelButton;
    @FXML private Button confirmButton;
    private KeyList keyList;
    private RequestList requestList;
    private LockerList lockerList;
    private Request request;
    private Zone zone;
    private Key currentKey;
    private Locker currentLocker;
    private Officer officer;
    private ZoneService zoneService = new ZoneService();
    @FXML
    public void initialize() {
        officer = sessionManager.getOfficer();
        Object data = FXRouter.getData();
        if (data instanceof Request) {
            request = (Request) data;
        } else {
            System.out.println("Error: Data is not an Request");
        }
        zone = zoneService.findZoneByName(request.getZoneName());
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
        //keyList.removeUnavailableKeys();

        requestList = requestsProvider.loadCollection(zone.getZoneUid());

        lockerList = lockersProvider.loadCollection(zone.getZoneUid());
        currentLocker = lockerList.findLockerByUuid(request.getLockerUid());

    }

    private void showTable(KeyList keyList) {
        keylockerTableView.getColumns().clear();

        TableColumn<Key, KeyType> keyTypeColumn = new TableColumn<>("ประเภทกุญแจ");
        keyTypeColumn.setCellValueFactory(new PropertyValueFactory<>("keyType"));

        TableColumn<Key, String> uuidColumn = new TableColumn<>("เลขประจำกุญแจ");
        uuidColumn.setCellValueFactory(new PropertyValueFactory<>("uuid"));

        TableColumn<Key, String> passKeyColumn = new TableColumn<>("รหัสกุญแจ");
        passKeyColumn.setCellValueFactory(new PropertyValueFactory<>("passkey"));

        TableColumn<Key, Boolean> availableColumn = new TableColumn<>("สถานะกุญแจ");
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));

        TableColumn<Key, String> uuidLockerColumn = new TableColumn<>("uuidLocker");
        uuidLockerColumn.setCellValueFactory(new PropertyValueFactory<>("uuidLocker"));
        keylockerTableView.getColumns().addAll(keyTypeColumn, uuidColumn, passKeyColumn, availableColumn, uuidLockerColumn);

        keylockerTableView.getItems().clear();
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
    }
    private void onCancelButtonClick(){
        Window window = selectKeyDialogPane.getScene().getWindow();}
    private void onConfirmButtonClick(){
        Request oldRequest = requestList.findRequestByUuid(request.getRequestUid());
        currentKey.setAvailable(false);
        currentLocker.setAvailable(false);
        oldRequest.setRequestType(RequestType.APPROVE);
        oldRequest.setRequestTime(LocalDateTime.now());
        oldRequest.setOfficerUsername(officer.getUsername());
        oldRequest.setLockerKeyUid(currentKey.getKeyUid());

        requestsProvider.saveCollection(zone.getZoneUid(), requestList);

        keysProvider.saveCollection(zone.getZoneUid(), keyList);
        lockersProvider.saveCollection(zone.getZoneUid(), lockerList);

        alertUtil.info("ยืนยันสำเร็จ", request.getUserUsername() + " ได้ทำการจองสำเร็จ ");
        try {
            FXRouter.goTo("officer-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Window window = selectKeyDialogPane.getScene().getWindow();
        window.hide();
    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
