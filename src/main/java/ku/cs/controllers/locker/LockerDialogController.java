package ku.cs.controllers.locker;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import ku.cs.components.button.ElevatedButton;
import ku.cs.components.button.FilledButton;
import ku.cs.components.button.OutlinedButton;
import ku.cs.models.key.KeyList;
import ku.cs.models.key.Key;
import ku.cs.models.key.KeyType;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.locker.LockerType;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestType;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.datasources.*;

public class LockerDialogController {
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();

    @FXML private AnchorPane lockerDialogPane;
    @FXML private ImageView itemImage;

    @FXML private Label lockerNumberLabel;

    @FXML private Label statusLabel;
    @FXML private Label priceLabel;

    @FXML private Label lockerIdLabel;
    @FXML private Label lockerZoneLabel;
    @FXML private Label lockerTypeLabel;
    @FXML private Label lockerKeyTypeLabel;

    @FXML private Label startDateLabel;
    @FXML private Label endDateLabel;

    @FXML private HBox containerHBox;

    @FXML private Button addItemButton;
    @FXML private Button removeItemButton;
    @FXML private Button returnLockerButton;
    @FXML private Button closeLockerButton;

    Request request;
    Datasource<LockerList> lockerListDatasource;
    LockerList lockerList;
    Locker locker;

    Datasource<KeyList> keyListDatasource;
    KeyList keyList;
    Key key;

    ZoneList zoneList;
    Zone zone;

    @FXML
    private void initialize() {
        request = (Request) FXRouter.getData();

        initializeDatasource();
        initUserInterface();
        initEvents();
        refreshContainerUI();
    }

    private void initializeDatasource() {
        zoneList = zonesProvider.loadCollection();
        zone = zoneList.findZoneByName(request.getZoneName());

        lockerListDatasource =  new LockerListFileDatasource("data/lockers","zone-"+zone.getZoneUid() +".json");
        lockerList = lockerListDatasource.readData();
        locker = lockerList.findLockerByUuid(request.getLockerUid());

        System.out.println("locker: " + request.getLockerUid() );
        System.out.println("data/lockers" + "/zone-"+zone.getZoneUid()+ ".json");

        lockerNumberLabel.setText(request.getLockerUid());
        statusLabel.setText(request.getRequestType().toString());
        lockerIdLabel.setText(request.getLockerUid());
        lockerZoneLabel.setText(request.getZoneUid());
        lockerTypeLabel.setText(locker.getLockerType().toString());
        startDateLabel.setText(request.getStartDate().toString());
        endDateLabel.setText(request.getEndDate().toString());
    }

    private void initUserInterface() {
        FilledButton.MEDIUM.mask(closeLockerButton);
        ElevatedButton.MEDIUM.mask(returnLockerButton);
        FilledButton.MEDIUM.mask(addItemButton);
        OutlinedButton.MEDIUM.mask(removeItemButton);
        addItemButton.setDisable(true);
        removeItemButton.setDisable(true);
    }

    private void initEvents() {
        if (addItemButton != null) {
            addItemButton.setOnAction(e -> onAddItemButtonClick());
        }
        if (removeItemButton != null) {
            removeItemButton.setOnAction(e -> onRemoveItemButtonClick());
        }
        if (returnLockerButton != null) {
            returnLockerButton.setOnAction(e -> onReturnLockerButtonClick());
        }
        if (closeLockerButton != null) {
            closeLockerButton.setOnAction(e -> onCloseButtonClick());
        }
    }

    private void refreshContainerUI() {
        containerHBox.getChildren().clear();

        RequestType status = request.getRequestType();

        switch (status) {
            case RequestType.APPROVE:
                addItemButton.setDisable(false);
                removeItemButton.setDisable(false);
                switch (locker.getLockerType()) {
                    case LockerType.DIGITAL:
                        renderApproveDigital();
                        break;
                    case LockerType.MANUAL:
                        keyListDatasource = new KeyListFileDatasource("data/keys","zone-"+zone.getZoneUid() +".json");
                        keyList = keyListDatasource.readData();
                        key = keyList.findKeyByUuid(request.getLockerKeyUid());
                        KeyType keyType = key.getKeyType();

                        switch (keyType) {
                            case KeyType.MANUAL:
                                lockerKeyTypeLabel.setText(keyType.toString());
                                renderApproveManual();
                                break;
                            case KeyType.CHAIN:
                                lockerKeyTypeLabel.setText(keyType.toString());
                                renderApproveChain();
                                break;
                        }
                        break;
                    default:
                        containerHBox.getChildren().add(new Text("Unknown key type"));
                }
                break;

            case RequestType.REJECT:
                renderReject();
                break;

            case RequestType.PENDING:
                renderPadding();
                break;

            default:
                containerHBox.getChildren().add(new Text("Unknown status"));
        }
    }

    private void renderApproveDigital() {
            VBox box = new VBox(6);
            box.setFillWidth(true);

            Label title = new Label("Set digital code");

            HBox hBox = new HBox();
            TextField codeField = new TextField();
            codeField.setPromptText(locker.getPassword());
            codeField.setPrefColumnCount(10);

            FilledButton setBtn = FilledButton.small("Set Code");
            setBtn.setOnAction(e -> {
                String val = codeField.getText();
                if (val == null || val.isBlank()) {
                    showAlert(Alert.AlertType.ERROR,"Invalid Code", "Please enter a valid code.");
                    return;
                }
                refreshContainerUI();
            });

            hBox.getChildren().addAll(codeField, setBtn);
            box.getChildren().addAll(title, hBox);
            containerHBox.getChildren().add(box);
    }

    private void renderApproveManual() {
        VBox box = new VBox(4);
        HBox r1 = new HBox(6);
        HBox r2 = new HBox(6);
        r1.setAlignment(Pos.CENTER_LEFT);
        r2.setAlignment(Pos.CENTER_LEFT);

        r1.getChildren().addAll(new Label("Key Code:"), new Label(key.getPasskey()));
        r2.getChildren().addAll(new Label("Key UUID:"), new Label(key.getKeyUid()));

        box.getChildren().addAll(r1, r2);
        containerHBox.getChildren().add(box);
    }

    private void renderApproveChain() {
        VBox box = new VBox(4);
        HBox r1 = new HBox(6);
        HBox r2 = new HBox(6);
        r1.setAlignment(Pos.CENTER_LEFT);
        r2.setAlignment(Pos.CENTER_LEFT);

        r1.getChildren().addAll(new Label("Key Code:"), new Label(key.getPasskey()));
        r2.getChildren().addAll(new Label("Key UUID:"), new Label(key.getKeyUid()));

        box.getChildren().addAll(r1, r2);
        containerHBox.getChildren().add(box);
    }

    private void renderReject() {
        VBox box = new VBox(4);
        Label status = new Label("Status: REJECT");
        Label reason = new Label("Reason: " + (request.getMessage() == null ? "-" : request.getMessage()));
        reason.setWrapText(true);
        box.getChildren().addAll(status, reason);
        containerHBox.getChildren().add(box);
    }

    private void renderPadding() {
        Label l = new Label("Status: PADDING");
        containerHBox.getChildren().add(l);
    }


    private void onAddItemButtonClick() {
        // TODO: เลือก/อัปโหลดรูป หรือเปิด dialog ใส่ของ
    }

    private void onRemoveItemButtonClick() {
        // TODO: เอารูปออก / อัปเดตสถานะของในตู้
    }

    private void onReturnLockerButtonClick() {
        // TODO: ยืนยันการสิ้นสุดบริการ -> เปลี่ยนสถานะ, รีเซ็ตกุญแจ/โค้ด, ทำตู้ให้ว่าง, ปิด dialog ถ้าจบ flow
    }

    private void onCloseButtonClick() {
        if (lockerDialogPane != null && lockerDialogPane.getScene() != null && lockerDialogPane.getScene().getWindow() != null) {
            lockerDialogPane.getScene().getWindow().hide();
        }
    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
