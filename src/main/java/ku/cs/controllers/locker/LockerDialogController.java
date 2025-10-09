package ku.cs.controllers.locker;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import ku.cs.components.button.ElevatedButton;
import ku.cs.components.button.FilledButton;
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
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.ImageUploadUtil;
import ku.cs.services.utils.QrCodeGenerator;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LockerDialogController {
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final LockerDatasourceProvider lockersProvider = new LockerDatasourceProvider();
    private final KeyDatasourceProvider keysProvider = new KeyDatasourceProvider();
    private final RequestDatasourceProvider requestsProvider = new RequestDatasourceProvider();
    private final ImageUploadUtil imageUploadUtil = new ImageUploadUtil();
    @FXML private AnchorPane lockerDialogPane;
    @FXML private ImageView itemImage;
    @FXML private Label lockerNumberLabel;
    @FXML private Label lockerSizeTypeLabel;
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

    @FXML private VBox qrCodeVBox;
    @FXML private ImageView qrImageView;
    @FXML private Label qrCodeLabel;

    @FXML private Button returnLockerButton;
    @FXML private Button closeLockerButton;
    RequestList requestList;
    Request request;
    LockerList lockerList;
    Locker locker;
    String RELATIVE_PATH ;

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
        generateQrCode();
    }

    private void initializeDatasource() {
        zoneList = zonesProvider.loadCollection();
        zone = zoneList.findZoneByUid(request.getZoneUid());

        lockerList = lockersProvider.loadCollection(zone.getZoneUid());
        locker = lockerList.findLockerByUid(request.getLockerUid());

        requestList =  requestsProvider.loadCollection(zone.getZoneUid());
        request = requestList.findRequestByUid(request.getRequestUid());

        lockerNumberLabel.setText(request.getLockerUid());
        statusLabel.setText(request.getRequestType().toString());
        lockerIdLabel.setText(request.getLockerUid());
        lockerZoneLabel.setText(zoneList.findZoneByUid(request.getZoneUid()).getZoneName());
        lockerSizeTypeLabel.setText(locker.getLockerSizeTypeString());
        lockerTypeLabel.setText(locker.getLockerType().toString());
        startDateLabel.setText(request.getStartDate().toString());
        endDateLabel.setText(request.getEndDate().toString());
        priceLabel.setText(String.valueOf(request.getPrice()));
    }

    private void initUserInterface() {
        if(!request.getImagePath().isBlank() && request.getImagePath()!=null) {
            Image image = new Image("file:" + request.getImagePath(), 230, 230, true, true);
            itemImage.setImage(image);
            RELATIVE_PATH =  request.getImagePath();
        }

        FilledButton.MEDIUM.mask(closeLockerButton);
        ElevatedButton.MEDIUM.mask(returnLockerButton);
        FilledButton.MEDIUM.mask(addItemButton);
        addItemButton.setDisable(true);
    }

    private void initEvents() {
        if (addItemButton != null) {
            addItemButton.setOnAction(e -> onAddItemButtonClick());
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
                switch (locker.getLockerType()) {
                    case LockerType.DIGITAL:
                        renderApproveDigitalOrChain();
                        break;
                    case LockerType.MANUAL:
                        keyList = keysProvider.loadCollection(zone.getZoneUid());
                        key = keyList.findKeyByUid(request.getLockerKeyUid());
                        KeyType keyType = key.getKeyType();

                        switch (keyType) {
                            case KeyType.MANUAL:
                                lockerKeyTypeLabel.setText(keyType.toString());
                                renderApproveManual();
                                break;
                            case KeyType.CHAIN:
                                lockerKeyTypeLabel.setText(keyType.toString());
                                renderApproveDigitalOrChain();
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
                renderPending();
                break;
            case RequestType.LATE:
                renderLate();
                break;
            case RequestType.SUCCESS:
                renderSuccess();
                break;
            default:
                containerHBox.getChildren().add(new Text("Unknown status"));
        }
    }

    private void generateQrCode() {
        if (locker == null) return;

        String qrContent = "LOCKER:" + locker.getLockerUid();
        qrCodeVBox.getChildren().clear();

        ImageView qrImage = new ImageView(new QrCodeGenerator().generate(qrContent, 100));
        qrImage.setFitWidth(100);
        qrImage.setFitHeight(100);

        Label label = new Label("QR: " + locker.getLockerUid());
        label.getStyleClass().addAll("label-small", "text-on-surface");

        qrCodeVBox.getChildren().addAll(qrImage, label);
        qrCodeVBox.setAlignment(Pos.CENTER);
    }

    private void renderApproveDigitalOrChain() {
        VBox box = new VBox(6);
        box.setFillWidth(true);

        HBox hBox = new HBox();
        TextField codeField = new TextField();
        FilledButton setBtn = FilledButton.small("Set Code");
        codeField.setPrefColumnCount(10);
        Label title = new Label("Set digital code");
        if(locker.getLockerType().equals(LockerType.DIGITAL)) {
            title = new Label("Set digital code");
            codeField.setPromptText(locker.getPassword());

            setBtn.setOnAction(e -> {
                String val = codeField.getText();
                if (val == null || val.isBlank() || !val.matches("\\d{5}")) {
                    new AlertUtil().error("Invalid Code", "รหัสต้องเป็นตัวเลข 5 หลัก");
                    return;
                }
                locker.setPassword(val);
                lockersProvider.saveCollection(zone.getZoneUid(), lockerList);
                refreshContainerUI();
                generateQrCode();
            });
        }else{
            title = new Label("Set Chain code");
            codeField.setPromptText(key.getPasskey());
            setBtn.setOnAction(e -> {
                String val = codeField.getText();
                if (val == null || val.isBlank() || !val.matches("\\d{5}")) {
                    new AlertUtil().error("Invalid Code", "Please enter a valid code.");
                    return;
                }
                key.setPasskey(val);
                keysProvider.saveCollection(zone.getZoneUid(),keyList);
                refreshContainerUI();
                generateQrCode();
            });
        }
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
    private void renderLate(){
        VBox box = new VBox(4);
        Label status = new Label("Status: LATE");
        Label reason = new Label("Reason: เข้าใช้บริการล็อกเกอร์เกินวันที่จอง กรุณาชำระเงินหน้าเคาเตอร์");
        reason.setWrapText(true);
        box.getChildren().addAll(status, reason);
        containerHBox.getChildren().add(box);
        returnLockerButton.setDisable(true);
    }
    private void renderSuccess() {
        VBox box = new VBox(4);
        Label status = new Label("Status: SUCCESS");
        Label reason = new Label("Reason: "+ (request.getMessage() == null ? "-" : request.getMessage()));
        reason.setWrapText(true);
        box.getChildren().addAll(status, reason);
        containerHBox.getChildren().add(box);
        returnLockerButton.setDisable(true);
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
        returnLockerButton.setDisable(true);
    }

    private void renderPending() {
        Label l = new Label("Status: PENDING");
        containerHBox.getChildren().add(l);
        returnLockerButton.setDisable(true);
    }


    private void onAddItemButtonClick() {
        // TODO: เลือก/อัปโหลดรูป หรือเปิด dialog ใส่ของ
        try {
            Path destDir = Paths.get("images","requests",request.getZoneUid());
            ImageUploadUtil.PickResult res = imageUploadUtil.pickAndSaveImage(
                    addItemButton.getScene().getWindow(),
                    destDir,
                    request.getRequestUid(),
                    30 * 1024 * 1024
            );
            if (res == null) return;
            try (FileInputStream in = new FileInputStream(res.savedPath().toFile())) {
                itemImage.setImage(new Image(in));
                RELATIVE_PATH = res.savedPath()
                        .toString()
                        .replace("\\", "/");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void onReturnLockerButtonClick() {
        // TODO: ยืนยันการสิ้นสุดบริการ -> เปลี่ยนสถานะ, รีเซ็ตกุญแจ/โค้ด, ทำตู้ให้ว่าง, ปิด dialog ถ้าจบ flow
        request.setRequestType(RequestType.SUCCESS);
        request.setMessage("เข้าใช้บริการล็อกเกอร์ครบวันที่จองแล้ว");
        if(locker.getLockerType()!=LockerType.DIGITAL){
            key.setAvailable(true);
            keysProvider.saveCollection(zone.getZoneUid(), keyList);
        }
        locker.setAvailable(true);
        requestsProvider.saveCollection(zone.getZoneUid(),requestList);
        lockersProvider.saveCollection(zone.getZoneUid(), lockerList);
        lockerDialogPane.getScene().getWindow().hide();
        try {
            FXRouter.goTo("user-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void onCloseButtonClick() {
        if (lockerDialogPane != null && lockerDialogPane.getScene() != null && lockerDialogPane.getScene().getWindow() != null) {
            if(!RELATIVE_PATH.equals(request.getImagePath())){
                try {
                    Path path = Paths.get(request.getImagePath()).toAbsolutePath();
                    boolean deleted = java.nio.file.Files.deleteIfExists(path);
                    System.out.println("Delete old image " + path + " result=" + deleted);
                } catch (IOException e) {
                    System.err.println("Warning: ลบรูปเก่าไม่ได้ " + request.getImagePath());
                }
            }
            request.setImagePath(RELATIVE_PATH);
            requestsProvider.saveCollection(zone.getZoneUid(),requestList);
            lockerDialogPane.getScene().getWindow().hide();
        }

    }
}