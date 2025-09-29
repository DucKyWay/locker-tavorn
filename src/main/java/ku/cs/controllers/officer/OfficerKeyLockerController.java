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
import ku.cs.models.key.Key;
import ku.cs.models.key.KeyType;
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
    @FXML private TableView<Key> keylockerTableView;

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
        String zoneName = (currentZone != null) ? currentZone.getZoneName() : "Unknown";
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
        keylockerTableView.getColumns().setAll(
            createTextColumn("ประเภทกุญแจ", "keyType"),
            createTextColumn("เลขประจำกุญแจ", "keyUid"),
            createTextColumn("รหัสกุญแจ", "passkey"),
            createTextColumn("สถานะกุญแจ", "available"),
            createTextColumn("uuidLocker", "lockerUid")
        );

        keylockerTableView.getItems().addAll(keyList.getKeys());
    }

    private <T> TableColumn<Key, T> createTextColumn(String title, String property) {
        TableColumn<Key, T> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        col.setStyle("-fx-alignment: TOP_CENTER;");
        return col;
    }

    @FXML
    protected void onAddKeyChain() {
        if (currentZone == null) return;
        Key key = new Key(KeyType.CHAIN, currentZone.getZoneName());
        keyList.addKey(key);
        keyListDatasource.writeData(keyList);
        showTable(keyList);
    }

    @FXML
    protected void onAddKeyManual() {
        if (currentZone == null) return;
        Key key = new Key(KeyType.MANUAL, currentZone.getZoneName());
        keyList.addKey(key);
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
