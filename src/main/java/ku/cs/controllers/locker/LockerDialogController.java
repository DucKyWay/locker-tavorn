package ku.cs.controllers.locker;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import ku.cs.components.Icons;
import ku.cs.components.LockerBox;
import ku.cs.components.button.*;
import ku.cs.models.account.Account;
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

    @FXML private VBox lockerVBox;

    @FXML private AnchorPane lockerDialogPane;
    @FXML private ImageView itemImage;

    @FXML private Label lockerNumberLabel;
    @FXML private Label lockerZoneLabel;
    @FXML private Label lockerTypeLabel;
    @FXML private Label lockerSizeTypeLabel;

    @FXML private Label statusLabel;
    @FXML private Label priceLabel;
    @FXML private Label fineLabel;
    @FXML private Label priceTotalLabel;

    @FXML private Label lockerOwerLabel;
    @FXML private Label lockerKeyTypeLabel;

    @FXML private VBox keyContainerVBox;
    @FXML private HBox keyContainerHBox;
    @FXML private Label keyLockerLabel;
    @FXML private Label lockerPasswordLabel;

    @FXML private ComboBox startDateCombo;
    @FXML private ComboBox endDateCombo;

    @FXML private VBox containerVBox;
    @FXML private VBox qrCodeVBox;
    @FXML private ImageView qrImageView;
    @FXML private Label qrCodeLabel;

    @FXML private Button returnLockerButton;

    @FXML private Button addItemButton;
    @FXML private Button removeItemButton;

    @FXML private Button closeLockerButton;

    @FXML private Button lockerDialog;


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
        FilledButtonWithIcon.SMALL.mask(lockerDialog, Icons.LOCKER);
        OutlinedButton.MEDIUM.mask(returnLockerButton);
        FilledButton.MEDIUM.mask(addItemButton);
        FilledButton.MEDIUM.mask(removeItemButton);
        FilledButton.MEDIUM.mask(closeLockerButton);

        if(!locker.getImagePath().isBlank() && locker.getImagePath()!=null) {
            Image image = new Image("file:" + locker.getImagePath(), 250, 252, true, true);

            Rectangle clip = new Rectangle(250, 252);
            clip.setArcWidth(8);
            clip.setArcHeight(8);
            itemImage.setClip(clip);
            itemImage.setImage(image);
            RELATIVE_PATH =  locker.getImagePath();
        }
        lockerNumberLabel.setText(request.getLockerUid());
        lockerZoneLabel.setText(zoneList.findZoneByUid(request.getZoneUid()).getZoneName());
        lockerTypeLabel.setText(locker.getLockerType().toString());
        lockerSizeTypeLabel.setText(locker.getLockerSizeType().getDescription());

        statusLabel.setText(request.getRequestType().toString());
        int price = (selectedDayService.getDaysBetween(request.getStartDate(), request.getEndDate())+1)*locker.getLockerSizeType().getPrice();
        priceLabel.setText(String.valueOf(price));
        fineLabel.setText(String.valueOf(
                Math.max(request.getPrice() - price, 0)
        ));
        priceTotalLabel.setText(String.valueOf(request.getPrice()));

        startDateCombo.setValue(request.getStartDate().toString());
        endDateCombo.setValue(request.getEndDate().toString());

        if(current instanceof User) {
            addItemButton.setDisable(true);
            removeItemButton.setDisable(true);
        }
        else{
            removeItemButton.setDisable(true);
            addItemButton.setDisable(true);
            returnLockerButton.setDisable(true);
        }

        LockerBox lockerBox = new LockerBox(locker);
        lockerVBox.getChildren().add(lockerBox);
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
        lockerOwerLabel.setText(request.getUserUsername());

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
        containerVBox.getChildren().clear();
        containerVBox.getChildren().add(new Text("ไม่สามารถตรวจสอบสถานะได้"));
        containerVBox.getStyleClass().add("text-error");
    }

    private void renderUnknownLockerType() {
        containerVBox.getChildren().clear();
        containerVBox.getChildren().add(new Text("ไม่พบประเภทล็อคเกอร์"));
        containerVBox.getStyleClass().add("text-error");
    }
    private void generateQrCode() {
        if (locker == null) return;

        String qrContent = "LOCKER:" + locker.getLockerUid();
        Rectangle clip = new Rectangle(50, 50);
        clip.setArcWidth(8);
        clip.setArcHeight(8);
        clip.setTranslateX(5);
        clip.setTranslateY(5);
        qrImageView.setClip(clip);
        qrImageView.setImage(new QrCodeGenerator().generate(qrContent, 60));
        qrImageView.setFitWidth(60);
        qrImageView.setFitHeight(60);
        qrCodeLabel.setText("QR: " + locker.getLockerUid());
        System.out.println(locker.getLockerUid());
    }

    private void handleChainKey() {
        if (current instanceof User) {
            renderApproveDigitalOrChainOfUser();
        } else {
            renderApproveDigitalOrChainOfOfficer();
        }
    }

    private void renderApproveDigitalOrChainOfUser() {
        if (!keyContainerHBox.getChildren().isEmpty() && keyContainerHBox.getChildren().size() > 2) {
            keyContainerHBox.getChildren().removeLast();
        }

        if (locker.getPassword() != null)
            lockerPasswordLabel.setText(locker.getPassword());
        else if (key.getPasskey() != null)
            lockerPasswordLabel.setText(key.getPasskey());

        Label lockerChangePasswordButton = new Label("เปลี่ยนรหัส");
        lockerChangePasswordButton.setMinWidth(72);
        lockerChangePasswordButton.setUnderline(true);
        lockerChangePasswordButton.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        lockerChangePasswordButton.setContentDisplay(javafx.scene.control.ContentDisplay.RIGHT);
        lockerChangePasswordButton.getStyleClass().addAll("text-variant", "label-medium");
        lockerChangePasswordButton.setStyle("-fx-cursor: hand;");

        lockerChangePasswordButton.setOnMouseClicked(event -> {
            System.out.println("เปลี่ยนรหัสล็อกเกอร์!");
            if (locker.getLockerType().equals(LockerType.DIGITAL)) {
                try {
                    FXRouter.loadDialogStage("locker-change-password", request);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                finally {
                    onCloseButtonClick();
                }
            }
            else {
                try {
                    FXRouter.loadDialogStage("locker-change-password-chain", request);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                finally {
                    onCloseButtonClick();
                }
            }
        });
        keyContainerHBox.getChildren().add(lockerChangePasswordButton);
    }

    private void renderApproveDigitalOrChainOfOfficer() {
        if(locker.getLockerType().equals(LockerType.DIGITAL)) {
            keyLockerLabel.setText("รหัสดิจิตอล");
            lockerPasswordLabel.setText(locker.getPassword());
        }else{
            keyLockerLabel.setText("รหัสสายรัด");
            lockerPasswordLabel.setText(key.getPasskey());
        }
    }

    private void renderApproveManual() {
        if (!keyContainerVBox.getChildren().isEmpty() && keyContainerVBox.getChildren().size() > 3) {
            keyContainerVBox.getChildren().removeLast();
        }
        keyLockerLabel.setText("รหัสกุญแจ:");
        lockerPasswordLabel.setText(key.getPasskey());

        Label lockerLabel = new Label("หมายเลขกุญแจ:");
        lockerLabel.setMinWidth(81);
        lockerLabel.getStyleClass().addAll("label-medium", "text-variant");

        Label lockerPasswordLabel = new Label(key.getKeyUid());
        lockerPasswordLabel.setMinWidth(52);
        lockerPasswordLabel.getStyleClass().addAll("body-small", "text-on-surface");

        HBox hbox = new HBox(8, lockerLabel, lockerPasswordLabel);
        hbox.setAlignment(javafx.geometry.Pos.BOTTOM_LEFT);

        keyContainerVBox.getChildren().add(hbox);
    }

    private void renderLate(){
        VBox box = new VBox(4);
        Label status = new Label("สถานะ: เกินกำหนด");
        Label reason = new Label("หมายเหตุ: เข้าใช้บริการล็อคเกอร์เกินวันที่จอง กรุณาชำระเงินหน้าเคาเตอร์");
        reason.setWrapText(true);
        box.getChildren().addAll(status, reason);
        containerVBox.getChildren().add(box);
        returnLockerButton.setDisable(true);
    }
    private void renderSuccess() {
        VBox box = new VBox(4);
        Label status = new Label("สถานะ: สำเร็จ");
        Label reason = new Label("หมายเหตุ: "+ (request.getMessage() == null ? "-" : request.getMessage()));
        reason.setWrapText(true);
        box.getChildren().addAll(status, reason);
        containerVBox.getChildren().add(box);
        returnLockerButton.setDisable(true);
    }

    private void renderReject() {
        VBox box = new VBox(4);
        Label status = new Label("สถานะ: ไม่สำเร็จ");
        Label reason = new Label("หมายเหตุ: " + (request.getMessage() == null ? "-" : request.getMessage()));
        reason.setWrapText(true);
        box.getChildren().addAll(status, reason);
        containerVBox.getChildren().add(box);
        returnLockerButton.setDisable(true);
    }
    private void renderPending() {
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
            new AlertUtil().info("เพิ่มรูปภาพสำเร็จ", "เพิ่มรูปภาพสิ่งของในล็อคเกอร์แล้ว");

        } catch (IOException e) {
            new AlertUtil().error("เพิ่มรูปภาพไม่สำเร็จ", "ไม่สามารถอัปโหลดรูปภาพได้: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void onRemoveItemButtonClick() {
        if (locker.getImagePath() == null || locker.getImagePath().isEmpty()) {
            new AlertUtil().error("ไม่สำเร็จ", "ไม่พบสิ่งของในล็อคเกอร์");
            return;
        }

        deleteLockerImageFile();
        locker.setImagePath("");
        itemImage.setImage(null);

        lockersProvider.saveCollection(zone.getZoneUid(), lockerList);
        new AlertUtil().info("สำเร็จ", "นำสิ่งของออกจากล็อคเกอร์แล้ว");
    }

    private void onReturnLockerButtonClick() {
        if (locker.getImagePath() == null || locker.getImagePath().isEmpty()) {
            request.setRequestType(RequestType.SUCCESS);
            request.setMessage("เข้าใช้บริการล็อคเกอร์ครบวันที่จองแล้ว");
            locker.setAvailable(true);
            locker.setImagePath("");

            if (locker.getLockerType() != LockerType.DIGITAL && key != null) {
                key.setAvailable(true);
                key.setLockerUid("");
                keysProvider.saveCollection(zone.getZoneUid(), keyList); // Save key state
            }

            requestsProvider.saveCollection(zone.getZoneUid(), requestList);
            lockersProvider.saveCollection(zone.getZoneUid(), lockerList);

            new AlertUtil().info("สำเร็จ", "คืนล็อคเกอร์เรียบร้อยแล้ว");

            lockerDialogPane.getScene().getWindow().hide();
            try {
                FXRouter.goTo("user-home");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            new AlertUtil().error("พบสิ่งของในล็อคเกอร์", "กรุณานำสิ่งของออกจากล็อคเกอร์ ก่อนคืนล็อคเกอร์");
        }
    }


    private void onCloseButtonClick() {
        if (lockerDialogPane != null && lockerDialogPane.getScene() != null) {
            Platform.runLater(() -> lockerDialogPane.getScene().getWindow().hide());
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