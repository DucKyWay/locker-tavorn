package ku.cs.controllers.officer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.components.Icons;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.ElevatedButton;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.components.button.FilledButton;
import ku.cs.models.key.KeyList;
import ku.cs.models.key.Key;
import ku.cs.models.key.KeyType;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.provider.KeyDatasourceProvider;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.TableColumnFactory;

import java.io.IOException;

public class OfficerManageKeysController extends BaseOfficerController{
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final KeyDatasourceProvider keysProvider = new KeyDatasourceProvider();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();

    @FXML private Label headerLabel;
    @FXML private Button backButton;
    @FXML private TableView<Key> keysTableView;

    @FXML private Button addKeyChainButton;
    @FXML private Button addKeyManualButton;
    @FXML private Button resetKeysButton;

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
        headerLabel.setText("Key List Zone : " + zoneName);
        LabelStyle.TITLE_MEDIUM.applyTo(headerLabel);

        backButton.setText("ย้อนกลับ");
        ElevatedButtonWithIcon.SMALL.mask(backButton, Icons.ARROW_LEFT);

        FilledButton.SMALL.mask(addKeyChainButton);
        FilledButton.SMALL.mask(addKeyManualButton);
        ElevatedButtonWithIcon.SMALL.mask(resetKeysButton);

        showTable(keyList);
    }

    @Override
    protected void initEvents() {
        backButton.setOnAction(e -> onBackButtonClick());

        addKeyChainButton.setOnAction(e -> onAddKeyChainButtonClick());
        addKeyManualButton.setOnAction(e -> onAddKeyManualButtonClick());
        resetKeysButton.setOnAction(e -> onResetKeyListButtonClick());
    }

    private void showTable(KeyList keyList) {
        keysTableView.getColumns().clear();
        keysTableView.getColumns().setAll(
            tableColumnFactory.createTextColumn("ประเภทกุญแจ", "keyType"),
            tableColumnFactory.createTextColumn("เลขประจำกุญแจ", "keyUid"),
            tableColumnFactory.createTextColumn("รหัสกุญแจ", "passkey"),
            tableColumnFactory.createTextColumn("สถานะกุญแจ", "available"),
            createLockerColumn()
        );
        keysTableView.getItems().clear();
        keysTableView.getItems().addAll(keyList.getKeys());
    }
    private TableColumn<Key, String> createLockerColumn() {
        TableColumn<Key, String> col = new TableColumn<>("เลขล็อคเกอร์");
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

    private void onAddKeyChainButtonClick() {
        if (currentZone == null) return;
        Key key = new Key(KeyType.CHAIN, currentZone.getZoneName());
        keyList.addKey(key);
        keysProvider.saveCollection(currentZone.getZoneUid(), keyList);
        showTable(keyList);
    }

    private void onAddKeyManualButtonClick() {
        if (currentZone == null) return;
        Key key = new Key(KeyType.MANUAL, currentZone.getZoneName());
        keyList.addKey(key);
        keysProvider.saveCollection(currentZone.getZoneUid(), keyList);
        showTable(keyList);
    }

    private void onResetKeyListButtonClick() {
        keyList.resetKeyList();
        showTable(keyList);
    }

    private void onBackButtonClick() {
        try {
            FXRouter.goTo("officer-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
