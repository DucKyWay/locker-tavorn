package ku.cs.controllers.officer;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.models.key.KeyList;
import ku.cs.models.key.Key;
import ku.cs.models.key.KeyType;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.context.AppContext;
import ku.cs.services.datasources.provider.KeyDatasourceProvider;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.session.SessionManager;

import java.io.IOException;

public class OfficerKeyLockerController extends BaseOfficerController{
    private final SessionManager sessionManager = AppContext.getSessionManager();
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final KeyDatasourceProvider keysProvider = new KeyDatasourceProvider();

    @FXML private HBox headerLabelContainer;
    @FXML private HBox backButtonContainer;
    @FXML private TableView<Key> keylockerTableView;

    private DefaultButton backButton;
    private DefaultLabel headerLabel;

    private KeyList keyList;

    private ZoneList zoneList;
    private Zone currentZone;

    @Override
    protected void initDatasource() {
        // โหลด ZoneList และหา Zone ของ officer จาก zoneUid
        zoneList = zonesProvider.loadCollection();
        if (!current.getZoneUids().isEmpty()) {
            currentZone = zoneList.findZoneByUid(current.getZoneUids().get(0));
        }

        if (currentZone == null) {
            throw new RuntimeException("Officer has no valid zoneUid");
        }

        keyList = keysProvider.loadCollection(currentZone.getZoneUid());

    }

    @Override
    protected void initUserInterfaces() {
        String zoneName = (currentZone != null) ? currentZone.getZoneName() : "Unknown";
        headerLabel = DefaultLabel.h2("Key List Zone : " + zoneName);

        backButton = DefaultButton.primary("Back");
        backButtonContainer.getChildren().add(backButton);
        headerLabelContainer.getChildren().add(headerLabel);

        showTable(keyList);
    }

    @Override
    protected void initEvents() {
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
            createLockerColumn()
        );
        keylockerTableView.getItems().clear();
        keylockerTableView.getItems().addAll(keyList.getKeys());
    }
    private TableColumn<Key, String> createLockerColumn() {
        TableColumn<Key, String> col = new TableColumn<>("uuidLocker");
        col.setCellValueFactory(cellData -> {
            Key key = cellData.getValue();
            if (key.isAvailable() && key.getLockerUid() != null && !key.getLockerUid().isBlank()) {
                return new javafx.beans.property.SimpleStringProperty("ว่าง");
            } else {
                return new javafx.beans.property.SimpleStringProperty(
                        key.getLockerUid() != null ? key.getLockerUid() : ""
                );
            }
        });
        col.setStyle("-fx-alignment: TOP_CENTER;");
        return col;
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
        keysProvider.saveCollection(currentZone.getZoneUid(), keyList);
        showTable(keyList);
    }

    @FXML
    protected void onAddKeyManual() {
        if (currentZone == null) return;
        Key key = new Key(KeyType.MANUAL, currentZone.getZoneName());
        keyList.addKey(key);
        keysProvider.saveCollection(currentZone.getZoneUid(), keyList);
        showTable(keyList);
    }

    @FXML
    protected void onResetKeyList() {
        keyList.resetKeyList();
        showTable(keyList);
    }
}
