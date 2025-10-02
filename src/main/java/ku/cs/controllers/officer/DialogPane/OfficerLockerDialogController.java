package ku.cs.controllers.officer.DialogPane;

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
import ku.cs.models.request.RequestList;
import ku.cs.models.request.RequestType;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.provider.KeyDatasourceProvider;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;
import ku.cs.services.datasources.provider.RequestDatasourceProvider;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.datasources.*;

import java.io.IOException;

public class OfficerLockerDialogController {
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final RequestDatasourceProvider requestsProvider = new RequestDatasourceProvider();
    private final LockerDatasourceProvider lockersProvider = new LockerDatasourceProvider();
    private final KeyDatasourceProvider keysProvider = new KeyDatasourceProvider();

    @FXML private AnchorPane lockerDialogPane;
    @FXML private ImageView itemImage;

    @FXML private Label lockerNumberLabel;

    @FXML private Label statusLabel;
    @FXML private Label priceLabel;

    @FXML private Label lockerIdLabel;
    @FXML private Label lockerZoneLabel;
    @FXML private Label lockerTypeLabel;
    @FXML private Label lockerKeyTypeLabel;
    @FXML private Label usernameLabel;
    @FXML private Label startDateLabel;
    @FXML private Label endDateLabel;

    @FXML private HBox containerHBox;

    @FXML private Button setAvalibleButton;
    @FXML private Button setStatusButton;
    @FXML private Button removeLockerButton;
    @FXML private Button closeLockerButton;

    Request request;
    LockerList lockerList;
    Locker inputLocker;
    Locker locker;

    RequestList requestList;

    KeyList keyList;
    Key key;

    ZoneList zoneList;
    Zone zone;
    @FXML
    private void initialize() {
        inputLocker = (Locker) FXRouter.getData();
        System.out.println(inputLocker);
        initializeDatasource();
        initUserInterface();
        initEvents();
        if(request != null) {
            refreshContainerUI();
        }
    }


    private void initializeDatasource() {
        zoneList = zonesProvider.loadCollection();
        zone = zoneList.findZoneByName(inputLocker.getZoneName());

        lockerList = lockersProvider.loadCollection(zone.getZoneUid());
        locker = lockerList.findLockerByUuid(inputLocker.getUid());

        requestList = requestsProvider.loadCollection(zone.getZoneUid());
        for (Request r : requestList.getRequestList()) {
            if (r.getLockerUid().equals(locker.getUid()) && r.getRequestType() != RequestType.PENDING) {
                request = r;
                break;
            }
        }

        if (request == null) {
            System.out.println("No request data found.");
            lockerNumberLabel.setText("ไม่มีข้อมูล");
            statusLabel.setText("ไม่มีข้อมูล");
        }else {
            lockerNumberLabel.setText(request.getLockerUid());
            statusLabel.setText(request.getRequestType().toString());
            lockerIdLabel.setText(request.getLockerUid());
            lockerZoneLabel.setText(request.getZoneUid());
            lockerTypeLabel.setText(locker.getLockerType().toString());
            startDateLabel.setText(request.getStartDate().toString());
            endDateLabel.setText(request.getEndDate().toString());
            usernameLabel.setText(request.getUserUsername());
        }
        if (locker.isStatus()) {
            setStatusButton.setText("ล็อกเกอร์ชำรุด");
        } else {
            setStatusButton.setText("ล็อกเกอร์พร้อมใช้งาน");
        }
        if (locker.isStatus()) {
            if (locker.isAvailable()) {
                setAvalibleButton.setText("ล็อกเกอร์ไม่ว่าง");
            } else {
                setAvalibleButton.setDisable(true);
            }
        } else {
            // ถ้าล็อกเกอร์พร้อมใช้งาน ให้ disable ปุ่ม
            setAvalibleButton.setDisable(true);
        }
    }

    private void initUserInterface() {
        FilledButton.MEDIUM.mask(closeLockerButton);
        ElevatedButton.MEDIUM.mask(setAvalibleButton);
        FilledButton.MEDIUM.mask(setStatusButton);
        OutlinedButton.MEDIUM.mask(removeLockerButton);
    }

    private void initEvents() {
        if (setAvalibleButton != null) {
            setAvalibleButton.setOnAction(e -> onSetAvalibleButtonClick());
        }
        if (removeLockerButton != null) {
            removeLockerButton.setOnAction(e -> onRemoveLockerButtonClick());
        }
        if (setStatusButton != null) {
            setStatusButton.setOnAction(e -> onSetStatusButtonClick());
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
                switch (locker.getLockerType()) {
                    case LockerType.DIGITAL:
                        renderApproveDigital();
                        break;
                    case LockerType.MANUAL:
                        keyList = keysProvider.loadCollection(zone.getZoneUid());
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


    private void onSetAvalibleButtonClick() {
        locker.setAvailable(!locker.isAvailable());
        lockersProvider.saveCollection(zone.getZoneUid(), lockerList);
        showAlert(Alert.AlertType.CONFIRMATION,"สถานะล็อกเกอร์","สถานะล็อกเกอร์เปลี่ยนแปลงถูกเปลี่ยนแปลงแล้ว");
    }

    private void onRemoveLockerButtonClick() {
        lockerList.deleteLocker(locker);
        lockersProvider.saveCollection(zone.getZoneUid(), lockerList);
        showAlert(Alert.AlertType.CONFIRMATION,"สถานะล็อกเกอร์","ล็อกเกอร์ถูกลบแล้ว");
        onCloseButtonClick();
    }

    private void onSetStatusButtonClick() {
        locker.setStatus(!locker.isStatus());
        lockersProvider.saveCollection(zone.getZoneUid(), lockerList);
        showAlert(Alert.AlertType.CONFIRMATION,"สถานะล็อกเกอร์","สถานะล็อกเกอร์เปลี่ยนแปลงถูกเปลี่ยนแปลงแล้ว");
    }

    private void onCloseButtonClick() {
        if (lockerDialogPane != null && lockerDialogPane.getScene() != null && lockerDialogPane.getScene().getWindow() != null) {
            lockerDialogPane.getScene().getWindow().hide();
        }
        try {
            FXRouter.goTo("officer-locker",zone);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
