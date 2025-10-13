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
import ku.cs.models.account.Account;
import ku.cs.models.account.Officer;
import ku.cs.models.account.User;
import ku.cs.models.dialog.DialogData;
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
import ku.cs.services.session.SelectedDayService;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.ImageUploadUtil;
import ku.cs.services.utils.QrCodeGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LockerDialogController {
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final LockerDatasourceProvider lockersProvider = new LockerDatasourceProvider();
    private final KeyDatasourceProvider keysProvider = new KeyDatasourceProvider();
    private final RequestDatasourceProvider requestsProvider = new RequestDatasourceProvider();
    private final ImageUploadUtil imageUploadUtil = new ImageUploadUtil();
    private final SelectedDayService selectedDayService = new SelectedDayService();
    @FXML private AnchorPane lockerDialogPane;
    @FXML private ImageView itemImage;
    @FXML private Label lockerNumberLabel;
    @FXML private Label lockerSizeTypeLabel;
    @FXML private Label statusLabel;
    @FXML private Label priceLabel;
    @FXML private Label priceTotalLabel;
    @FXML private Label fineLabel;


    @FXML private Label lockerIdLabel;
    @FXML private Label lockerZoneLabel;
    @FXML private Label lockerTypeLabel;
    @FXML private Label lockerKeyTypeLabel;
    @FXML private Label startDateLabel;
    @FXML private Label endDateLabel;

    @FXML private HBox containerHBox;

    @FXML private Button addItemButton;
    @FXML private Button removeItemButton;
    @FXML private VBox qrCodeVBox;

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
    Account current;
    @FXML
    private void initialize() {
        DialogData receivedData = (DialogData) FXRouter.getData();
        request = receivedData.getRequest();
        current = receivedData.getAccount();
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
    }

    private void initUserInterface() {
        if(!locker.getImagePath().isBlank() && locker.getImagePath()!=null) {
            Image image = new Image("file:" + locker.getImagePath(), 230, 230, true, true);
            itemImage.setImage(image);
            RELATIVE_PATH =  locker.getImagePath();
        }
        lockerNumberLabel.setText(request.getLockerUid());
        statusLabel.setText(request.getRequestType().toString());
        lockerIdLabel.setText(request.getLockerUid());
        lockerZoneLabel.setText(zoneList.findZoneByUid(request.getZoneUid()).getZoneName());
        lockerSizeTypeLabel.setText(locker.getLockerSizeTypeString());
        lockerTypeLabel.setText(locker.getLockerType().toString());
        startDateLabel.setText(request.getStartDate().toString());
        endDateLabel.setText(request.getEndDate().toString());
        priceTotalLabel.setText(String.valueOf(request.getPrice()));
        int price = (selectedDayService.getDaysBetween(request.getStartDate(), request.getEndDate())+1)*locker.getLockerSizeType().getPrice();
        priceLabel.setText(String.valueOf(price));
        fineLabel.setText(String.valueOf(
                Math.max(request.getPrice() - price, 0)
        ));
        FilledButton.MEDIUM.mask(closeLockerButton);
        if(current instanceof User) {
            FilledButton.MEDIUM.mask(removeItemButton);
            ElevatedButton.MEDIUM.mask(returnLockerButton);
            FilledButton.MEDIUM.mask(addItemButton);
            addItemButton.setDisable(true);
            removeItemButton.setDisable(true);
        }
        else{
            removeItemButton.setDisable(true);
            addItemButton.setDisable(true);
            returnLockerButton.setDisable(true);
        }
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
        if(removeItemButton != null) {
            removeItemButton.setOnAction(e->onRemoveItemButtonClick());
        }
    }

    private void refreshContainerUI() {
        containerHBox.getChildren().clear();
        RequestType status = request.getRequestType();

        switch (status) {
            case APPROVE:
                handleApproveStatus();
                break;
            case REJECT:
                renderReject();
                break;
            case PENDING:
                renderPending();
                break;
            case LATE:
                renderLate();
                break;
            case SUCCESS:
                renderSuccess();
                break;
            default:
                renderUnknownStatus();
        }
    }

    private void handleApproveStatus() {
        if(current instanceof User){
        addItemButton.setDisable(false);
        removeItemButton.setDisable(false);
        }
        LockerType lockerType = locker.getLockerType();

        switch (lockerType) {
            case DIGITAL:
                handleChainKey();
                break;
            case MANUAL:
                handleApproveManualLocker();
                break;
            default:
                renderUnknownLockerType();
        }
    }

    private void handleApproveManualLocker() {
        keyList = keysProvider.loadCollection(zone.getZoneUid());
        key = keyList.findKeyByUid(request.getLockerKeyUid());
        KeyType keyType = key.getKeyType();

        lockerKeyTypeLabel.setText(keyType.toString());

        switch (keyType) {
            case MANUAL:
                renderApproveManual();
                break;
            case CHAIN:
                handleChainKey();
                break;
        }
    }

    private void renderUnknownStatus() {
        containerHBox.getChildren().add(new Text("ไม่สามารถตรวจสอบสถานะได้"));
    }

    private void renderUnknownLockerType() {
        containerHBox.getChildren().add(new Text("ไม่พบประเภทล็อกเกอร์"));
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
    private void handleChainKey() {
        if (current instanceof User) {
            renderApproveDigitalOrChainOfUser();
        } else {
            renderApproveDigitalOrChainOfOfficer();
        }
    }
    private void renderApproveDigitalOrChainOfUser() {
        VBox box = new VBox(6);
        box.setFillWidth(true);

        HBox hBox = new HBox();
        TextField codeField = new TextField();
        FilledButton setBtn = FilledButton.small("ตั้งค่าความปลอดภัย");
        codeField.setPrefColumnCount(10);
        Label title = new Label();
        if(locker.getLockerType().equals(LockerType.DIGITAL)) {
            title.setText("เปลี่ยนรหัสผ่านตู้ล็อคเกอร์");
            codeField.setPromptText(locker.getPassword());

            setBtn.setOnAction(e -> {
                String val = codeField.getText();
                if (val == null || val.isBlank() || !val.matches("\\d{5}")) {
                    new AlertUtil().error("รูปแบบไม่ถูกต้อง", "รหัสต้องเป็นตัวเลข 5 หลัก");
                    return;
                }
                locker.setPassword(val);
                lockersProvider.saveCollection(zone.getZoneUid(), lockerList);
                refreshContainerUI();
                generateQrCode();
            });
        }else{
            title.setText("เปลี่ยนรหัสผ่านสายล็อค");
            codeField.setPromptText(key.getPasskey());
            setBtn.setOnAction(e -> {
                String val = codeField.getText();
                if (val == null || val.isBlank() || !val.matches("\\d{5}")) {
                    new AlertUtil().error("รูปแบบไม่ถูกต้อง", "Please enter a valid code.");
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
    private void renderApproveDigitalOrChainOfOfficer() {
        VBox box = new VBox(6);
        box.setFillWidth(true);
        Label passkey = new Label("");
        Label title = new Label("Set digital code");
        if(locker.getLockerType().equals(LockerType.DIGITAL)) {
            title = new Label("Digital code: ");
            passkey.setText(locker.getPassword());

        }else{
            title = new Label("Chain code: ");
            passkey.setText(key.getPasskey());
        }
        box.getChildren().addAll(title,passkey);
        containerHBox.getChildren().add(box);
    }

    private void renderApproveManual() {
        VBox box = new VBox(4);
        HBox r1 = new HBox(6);
        HBox r2 = new HBox(6);
        r1.setAlignment(Pos.CENTER_LEFT);
        r2.setAlignment(Pos.CENTER_LEFT);

        r1.getChildren().addAll(new Label("รหัสกุญแจ:"), new Label(key.getPasskey()));
        r2.getChildren().addAll(new Label("หมายเลขกุญแจ:"), new Label(key.getKeyUid()));

        box.getChildren().addAll(r1, r2);
        containerHBox.getChildren().add(box);
    }
    private void renderLate(){
        VBox box = new VBox(4);
        Label status = new Label("สถานะ: เกินกำหนด");
        Label reason = new Label("หมายเหตุ: เข้าใช้บริการล็อกเกอร์เกินวันที่จอง กรุณาชำระเงินหน้าเคาเตอร์");
        reason.setWrapText(true);
        box.getChildren().addAll(status, reason);
        containerHBox.getChildren().add(box);
        returnLockerButton.setDisable(true);
    }
    private void renderSuccess() {
        VBox box = new VBox(4);
        Label status = new Label("สถานะ: สำเร็จ");
        Label reason = new Label("หมายเหตุ: "+ (request.getMessage() == null ? "-" : request.getMessage()));
        reason.setWrapText(true);
        box.getChildren().addAll(status, reason);
        containerHBox.getChildren().add(box);
        returnLockerButton.setDisable(true);
    }

    private void renderReject() {
        VBox box = new VBox(4);
        Label status = new Label("สถานะ: ไม่สำเร็จ");
        Label reason = new Label("หมายเหตุ: " + (request.getMessage() == null ? "-" : request.getMessage()));
        reason.setWrapText(true);
        box.getChildren().addAll(status, reason);
        containerHBox.getChildren().add(box);
        returnLockerButton.setDisable(true);
    }

    private void renderPending() {
        Label l = new Label("สถานะ: ดำเนินการ");
        containerHBox.getChildren().add(l);
        returnLockerButton.setDisable(true);
    }


    private void onAddItemButtonClick() {
        try {
            Path destDir = Paths.get("images", "locker", request.getZoneUid());
            ImageUploadUtil.PickResult res = imageUploadUtil.pickAndSaveImage(
                    addItemButton.getScene().getWindow(), destDir, request.getRequestUid(), 30 * 1024 * 1024
            );

            if (res == null) return;
            deleteLockerImageFile();

            itemImage.setImage(new Image(res.savedPath().toUri().toString()));
            locker.setImagePath(res.savedPath().toString().replace("\\", "/"));

            lockersProvider.saveCollection(zone.getZoneUid(), lockerList);
            new AlertUtil().info("เพิ่มรูปภาพสำเร็จ", "เพิ่มรูปภาพสิ่งของในล็อกเกอร์แล้ว");

        } catch (IOException e) {
            new AlertUtil().error("เพิ่มรูปภาพไม่สำเร็จ", "ไม่สามารถอัปโหลดรูปภาพได้: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void onRemoveItemButtonClick() {
        if (locker.getImagePath() == null || locker.getImagePath().isEmpty()) {
            new AlertUtil().error("ไม่สำเร็จ", "ไม่พบสิ่งของในล็อกเกอร์");
            return;
        }

        deleteLockerImageFile();
        locker.setImagePath("");
        itemImage.setImage(null);

        lockersProvider.saveCollection(zone.getZoneUid(), lockerList);
        new AlertUtil().info("สำเร็จ", "นำสิ่งของออกจากล็อกเกอร์แล้ว");
    }

    private void onReturnLockerButtonClick() {
        if (locker.getImagePath() == null || locker.getImagePath().isEmpty()) {
            request.setRequestType(RequestType.SUCCESS);
            request.setMessage("เข้าใช้บริการล็อกเกอร์ครบวันที่จองแล้ว");
            locker.setAvailable(true);
            locker.setImagePath("");

            if (locker.getLockerType() != LockerType.DIGITAL && key != null) {
                key.setAvailable(true);
                key.setLockerUid("");
                keysProvider.saveCollection(zone.getZoneUid(), keyList); // Save key state
            }

            requestsProvider.saveCollection(zone.getZoneUid(), requestList);
            lockersProvider.saveCollection(zone.getZoneUid(), lockerList);

            new AlertUtil().info("สำเร็จ", "คืนล็อกเกอร์เรียบร้อยแล้ว");

            lockerDialogPane.getScene().getWindow().hide();
            try {
                FXRouter.goTo("user-home");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            new AlertUtil().error("พบสิ่งของในล็อคเกอร์", "กรุณานำสิ่งของออกจากล็อกเกอร์ ก่อนคืนล็อกเกอร์");
        }
    }


    private void onCloseButtonClick() {
        if (lockerDialogPane != null && lockerDialogPane.getScene() != null) {
            lockerDialogPane.getScene().getWindow().hide();
        }
    }


    private void deleteLockerImageFile() {
        if (locker.getImagePath() != null && !locker.getImagePath().isBlank()) {
            try {
                Path path = Paths.get(locker.getImagePath());
                Files.deleteIfExists(path);
            } catch (IOException e) {
                System.err.println("คำเตือน: ไม่สามารถลบรูปภาพเก่าได้ " + locker.getImagePath());
            }
        }
    }
}