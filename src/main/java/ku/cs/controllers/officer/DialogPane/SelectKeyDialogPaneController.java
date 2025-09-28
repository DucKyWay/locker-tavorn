package ku.cs.controllers.officer.DialogPane;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Window;
import ku.cs.components.button.ElevatedButton;
import ku.cs.components.button.FilledButton;
import ku.cs.models.account.Officer;
import ku.cs.models.key.KeyList;
import ku.cs.models.key.KeyLocker;
import ku.cs.models.locker.KeyType;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.request.RequestType;
import ku.cs.models.zone.Zone;
import ku.cs.services.AppContext;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.ZoneService;
import ku.cs.services.datasources.*;
import ku.cs.services.utils.AlertUtil;

import java.io.IOException;
import java.time.LocalDateTime;

public class SelectKeyDialogPaneController {
    private final SessionManager sessionManager = AppContext.getSessionManager();
    private final AlertUtil alertUtil = new AlertUtil();

    @FXML
    private DialogPane selectKeyDialogPane;
    @FXML private TableView<KeyLocker> keylockerTableView;
    @FXML private Button cancelButton;
    @FXML private Button confirmButton;
    private Datasource<KeyList> keyListdatasource;
    private KeyList keyList;
    private Datasource<RequestList> requestListdatasource;
    private RequestList requestList;
    private Datasource<LockerList> lockerListDatasource;
    private LockerList lockerList;
    private Request request;
    private Zone zone;
    private KeyLocker currentKey;
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
        zone = zoneService.findZoneByName(request.getZone());
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
        keyListdatasource = new KeyListFileDatasource("data/keys","zone-"+zone.getZoneUid()+".json");
        keyList = keyListdatasource.readData();
        //keyList.removeUnavailableKeys();

        requestListdatasource = new RequestListFileDatasource("data/requests","zone-"+zone.getZoneUid()+".json");
        requestList = requestListdatasource.readData();

        lockerListDatasource = new LockerListFileDatasource("data/lockers","zone-"+zone.getZoneUid()+".json");
        lockerList = lockerListDatasource.readData();
        currentLocker = lockerList.findLockerByUuid(request.getUuidLocker());

    }

    private void showTable(KeyList keyList) {
        keylockerTableView.getColumns().clear();

        TableColumn<KeyLocker, KeyType> keyTypeColumn = new TableColumn<>("ประเภทกุญแจ");
        keyTypeColumn.setCellValueFactory(new PropertyValueFactory<>("keyType"));

        TableColumn<KeyLocker, String> uuidColumn = new TableColumn<>("เลขประจำกุญแจ");
        uuidColumn.setCellValueFactory(new PropertyValueFactory<>("uuid"));

        TableColumn<KeyLocker, String> passKeyColumn = new TableColumn<>("รหัสกุญแจ");
        passKeyColumn.setCellValueFactory(new PropertyValueFactory<>("passkey"));

        TableColumn<KeyLocker, Boolean> availableColumn = new TableColumn<>("สถานะกุญแจ");
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));

        TableColumn<KeyLocker, String> uuidLockerColumn = new TableColumn<>("uuidLocker");
        uuidLockerColumn.setCellValueFactory(new PropertyValueFactory<>("uuidLocker"));
        keylockerTableView.getColumns().addAll(keyTypeColumn, uuidColumn, passKeyColumn, availableColumn, uuidLockerColumn);

        keylockerTableView.getItems().clear();
        keylockerTableView.getItems().addAll(
                keyList.getKeys().stream()
                        .filter(KeyLocker::isAvailable) // เฉพาะที่ available == true
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
        Request oldRequest = requestList.findRequestByUuid(request.getUuid());
        currentKey.setAvailable(false);
        currentLocker.setAvailable(false);
        oldRequest.setRequestType(RequestType.APPROVE);
        oldRequest.setRequestTime(LocalDateTime.now());
        oldRequest.setOfficerName(officer.getUsername());
        oldRequest.setUuidKeyLocker(currentKey.getUuid());

        requestListdatasource.writeData(requestList);

        keyListdatasource.writeData(keyList);
        lockerListDatasource.writeData(lockerList);

        alertUtil.info("ยืนยันสำเร็จ", request.getUserName() + " ได้ทำการจองสำเร็จ ");
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
