package ku.cs.controllers.officer;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.*;
import ku.cs.models.key.KeyList;
import ku.cs.models.key.Key;
import ku.cs.models.key.KeyType;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.request.RequestType;
import ku.cs.services.datasources.provider.KeyDatasourceProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.SearchService;
import ku.cs.services.utils.TableColumnFactory;

import java.io.IOException;
import java.util.List;

public class OfficerManageKeysController extends BaseOfficerController{
    private final KeyDatasourceProvider keysProvider = new KeyDatasourceProvider();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();
    private final SearchService<Key> searchService = new SearchService<>();

    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;

    @FXML private TableView<Key> keysTableView;

    @FXML private TextField searchTextField;
    @FXML private Button searchButton;

    @FXML private Button addKeyChainButton;
    @FXML private Button addKeyManualButton;
    @FXML private Button resetKeysButton;

    @FXML private Button officerZoneRouteLabelButton;
    @FXML private Button officerManageKeyRouteLabelButton;
    private final AlertUtil alertUtil = new AlertUtil();
    private KeyList keyList;

    @Override
    protected void initDatasource() {
        if (currentZone == null) {
            throw new RuntimeException("Officer has no valid zoneUid");
        }

        keyList = keysProvider.loadCollection(currentZone.getZoneUid());
    }

    @Override
    protected void initUserInterfaces() {
        ElevatedButtonWithIcon.LABEL.mask(officerZoneRouteLabelButton, Icons.LOCATION);
        ElevatedButtonWithIcon.LABEL.mask(officerManageKeyRouteLabelButton, Icons.TAG);
        IconButton.mask(searchButton, new Icon(Icons.MAGNIFYING_GLASS));

        FilledButton.SMALL.mask(addKeyChainButton);
        FilledButton.SMALL.mask(addKeyManualButton);
        OutlinedButtonWithIcon.SMALL.mask(resetKeysButton, null,  Icons.DELETE);

        officerZoneRouteLabelButton.setText(currentZone.getZoneName());

        showTable(keyList);
    }

    @Override
    protected void initEvents() {
        officerZoneRouteLabelButton.setOnAction(e -> onBackButtonClick());

        addKeyChainButton.setOnAction(e -> onAddKeyChainButtonClick());
        addKeyManualButton.setOnAction(e -> onAddKeyManualButtonClick());
        resetKeysButton.setOnAction(e -> onResetKeyListButtonClick());

        searchTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            onSearch();
        });
    }

    private void showTable(KeyList keyList) {
        keysTableView.getColumns().clear();
        keysTableView.getColumns().setAll(
            tableColumnFactory.createTextColumn("ประเภทกุญแจ", "keyType"),
            tableColumnFactory.createTextColumn("ไอดีกุญแจ", "keyUid"),
            tableColumnFactory.createTextColumn("รหัสกุญแจ", "passkey"),
            tableColumnFactory.createStatusColumn("สถานะกุญแจ", "available","ใช้งานได้", "ถูกใช้งานอยู่"),
            createLockerColumn(),
                createActionColumn()

        );
        keysTableView.getItems().clear();
        keysTableView.getItems().addAll(keyList.getKeys());

        keysTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

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
        col.setStyle("-fx-alignment: CENTER_LEFT;");
        return col;
    }
    private TableColumn<Key, Void> createActionColumn() {
        return tableColumnFactory.createActionColumn("จัดการ", key -> {
            IconButton deleteBtn = new IconButton(new Icon( Icons.DELETE));
            deleteBtn.setDisable(true);
            if(key.isAvailable()) {
                deleteBtn.setDisable(false);
                deleteBtn.setOnAction(e -> deleteKey(key));
            }

            return new Button[]{deleteBtn};
        });
    }
    private void deleteKey(Key key) {
        keyList.removeKeybyUid(key.getKeyUid());
        keysProvider.saveCollection(currentZone.getZoneUid(),keyList);
        new AlertUtil().info("ลบกุญแจสำเร็จ","ได้ทำการลบกุญแจ "+key.getKeyUid() +" สำเร็จแล้ว");
        showTable(keyList);

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
        boolean check = true;
        for(Key key : keyList.getKeys()) {
            if(!key.isAvailable()) {
                new AlertUtil().error("ลบกุญแจไม่สำเร็จ","ไม่สามารถรีเซ็ทกุญแจในโซนนี้ได้เพราะ มีกุญแจถูกใช้งานอยู่");
                check =false;
                break;
            }
        }
        if(check) {
            keyList.resetKeyList();
            new AlertUtil().info("ลบกุญแจสำเร็จ","ได้ทำการลบกุญแจในโซนสำเร็จแล้ว");
            showTable(keyList);
        }
    }

    private void onSearch() {
        String keyword = searchTextField.getText();
        List<Key> filtered = searchService.search(
                keyList.getKeys(),
                keyword,
                Key::getKeyUid
        );
        KeyList filteredList = new KeyList();
        filtered.forEach(filteredList::addKey);

        showTable(filteredList);
    }

    private void onBackButtonClick() {
        try {
            FXRouter.goTo("officer-select-zone");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
