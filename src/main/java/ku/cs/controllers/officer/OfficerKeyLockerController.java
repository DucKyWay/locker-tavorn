package ku.cs.controllers.officer;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.models.account.Account;
import ku.cs.models.account.Officer;
import ku.cs.models.key.KeyList;
import ku.cs.models.key.KeyLocker;
import ku.cs.models.locker.KeyType;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.AppContext;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.KeyListFileDatasource;
import ku.cs.services.datasources.ZoneListFileDatasource;

import java.io.IOException;

public class OfficerKeyLockerController {
    private final SessionManager sessionManager = AppContext.getSessionManager();

    @FXML private HBox headerLabelContainer;
    @FXML private HBox backButtonContainer;
    @FXML private TableView<KeyLocker> keylockerTableView;

    private DefaultButton backButton;
    private DefaultLabel headerLabel;

    private Officer officer;
    private Datasource<KeyList> keyListDatasource;
    private KeyList keyList;
    private Account current;

    private ZoneList zoneList;
    private Zone currentZone;

    @FXML
    public void initialize() {
        sessionManager.requireOfficerLogin();
        current = sessionManager.getCurrentAccount();
        Object data = FXRouter.getData();
        if (data instanceof Officer) {
            officer = (Officer) data;
        } else {
            System.out.println("Error: Data is not an Officer");
        }

        // โหลด ZoneList และหา Zone ของ officer จาก zoneUid
        zoneList = new ZoneListFileDatasource("data", "test-zone-data.json").readData();
        if (!officer.getZoneUids().isEmpty()) {
            currentZone = zoneList.findZoneByUid(officer.getZoneUids().get(0));
        }

        initKeyListDatasource();
        initUserInterface();
        initEvents();
        showTable(keyList);
    }

    public void initKeyListDatasource() {
        if (currentZone == null) {
            throw new RuntimeException("Officer has no valid zoneUid");
        }

        keyListDatasource = new KeyListFileDatasource(
                "data/keys",
                "zone-" + currentZone.getZoneUid() + ".json"
        );
        keyList = keyListDatasource.readData();
    }

    private void initUserInterface() {
        String zoneName = (currentZone != null) ? currentZone.getZone() : "Unknown";
        headerLabel = DefaultLabel.h2("Key List Zone : " + zoneName);

        backButton = DefaultButton.primary("Back");
        backButtonContainer.getChildren().add(backButton);
        headerLabelContainer.getChildren().add(headerLabel);
    }

    private void initEvents() {
        backButton.setOnAction(e -> onBackButtonClick());
    }

    private void onBackButtonClick() {
        try {
            FXRouter.goTo("officer-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

        keylockerTableView.getColumns().addAll(
                keyTypeColumn, uuidColumn, passKeyColumn, availableColumn, uuidLockerColumn
        );

        keylockerTableView.getItems().clear();
        keylockerTableView.getItems().addAll(keyList.getKeys());
    }

    @FXML
    protected void onAddKeyChain() {
        if (currentZone == null) return;
        KeyLocker keyLocker = new KeyLocker(KeyType.CHAIN, currentZone.getZone());
        keyList.addKey(keyLocker);
        keyListDatasource.writeData(keyList);
        showTable(keyList);
    }

    @FXML
    protected void onAddKeyManual() {
        if (currentZone == null) return;
        KeyLocker keyLocker = new KeyLocker(KeyType.MANUAL, currentZone.getZone());
        keyList.addKey(keyLocker);
        keyListDatasource.writeData(keyList);
        showTable(keyList);
    }

    @FXML
    protected void onResetKeyList() {
        keyList.resetKeyList();
        keyListDatasource.writeData(keyList);
        showTable(keyList);
    }
}
