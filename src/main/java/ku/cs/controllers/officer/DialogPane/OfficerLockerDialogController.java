package ku.cs.controllers.officer.DialogPane;

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
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import ku.cs.components.Icons;
import ku.cs.components.LockerBox;
import ku.cs.components.button.ElevatedButton;
import ku.cs.components.button.FilledButton;
import ku.cs.components.button.FilledButtonWithIcon;
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
import ku.cs.services.request.RequestService;
import ku.cs.services.session.SelectedDayService;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.GenerateNumberUtil;
import ku.cs.services.utils.QrCodeGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OfficerLockerDialogController {
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final RequestDatasourceProvider requestsProvider = new RequestDatasourceProvider();
    private final LockerDatasourceProvider lockersProvider = new LockerDatasourceProvider();
    private final KeyDatasourceProvider keysProvider = new KeyDatasourceProvider();
    private final GenerateNumberUtil generateNumberUtil = new GenerateNumberUtil();
    private final SelectedDayService selectedDayService = new SelectedDayService();

    @FXML
    private VBox lockerVBox;

    @FXML
    private AnchorPane lockerDialogPane;
    @FXML
    private ImageView itemImage;

    @FXML
    private Label lockerNumberLabel;
    @FXML
    private Label lockerZoneLabel;
    @FXML
    private Label lockerTypeLabel;
    @FXML
    private Label lockerSizeTypeLabel;

    @FXML
    private Label statusLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label fineLabel;
    @FXML
    private Label priceTotalLabel;

    @FXML
    private Label lockerOwerLabel;
    @FXML
    private Label lockerKeyTypeLabel;

    @FXML
    private VBox keyContainerVBox;
    @FXML
    private HBox keyContainerHBox;
    @FXML
    private Label keyLockerLabel;
    @FXML
    private Label lockerPasswordLabel;

    @FXML
    private ComboBox startDateCombo;
    @FXML
    private ComboBox endDateCombo;

    @FXML
    private VBox containerVBox;

    @FXML
    private Button setAvalibleButton;
    @FXML
    private Button setStatusButton;

    @FXML
    private Button removeLockerButton;
    @FXML
    private Button removeKeyLockerButton;

    @FXML
    private Button closeLockerButton;

    @FXML
    private Button lockerDialog;

    @FXML private VBox qrCodeVBox;
    @FXML private ImageView qrImageView;
    @FXML private Label qrCodeLabel;

    RequestService requestService = new RequestService();
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
        initializeDatasource();
        initUserInterface();
        initEvents();
        generateQrCode();
        if (request != null) {
            refreshContainerUI();
        }
    }


    private void initializeDatasource() {
        zoneList = zonesProvider.loadCollection();
        zone = zoneList.findZoneByUid(inputLocker.getZoneUid());

        lockerList = lockersProvider.loadCollection(zone.getZoneUid());
        locker = lockerList.findLockerByUid(inputLocker.getLockerUid());
        requestList = requestsProvider.loadCollection(zone.getZoneUid());
        for (Request r : requestList.getRequestList()) {
            if (r.getLockerUid().equals(locker.getLockerUid()) && r.getRequestType() == RequestType.APPROVE) {
                request = r;
                break;
            }
        }
        if (request == null) {
            for (Request r : requestList.getRequestList()) {
                if (r.getLockerUid().equals(locker.getLockerUid()) && r.getRequestType() == RequestType.LATE) {
                    request = r;
                    break;
                }
            }
        }
    }

    private void setupUI() {
        if (request == null || locker.isAvailable()) {
            displayNoRequestInfo();
            return;
        }
        populateInfoLabels();
        displayLockerImage();
        configureButtonStates();
    }

    private void populateInfoLabels() {
        lockerNumberLabel.setText(request.getLockerUid());
        lockerZoneLabel.setText(zoneList.findZoneByUid(request.getZoneUid()).getZoneName());
        lockerTypeLabel.setText(locker.getLockerType().toString());
        lockerSizeTypeLabel.setText(locker.getLockerSizeType().getDescription());

        statusLabel.setText(request.getRequestType().toString());
        lockerOwerLabel.setText(request.getUserUsername());

        startDateCombo.setValue(request.getStartDate().toString());
        endDateCombo.setValue(request.getEndDate().toString());

        int basePrice = calculateBasePrice();
        int fine = calculateFine(basePrice);

        priceLabel.setText(String.valueOf(basePrice));
        priceTotalLabel.setText(String.valueOf(request.getPrice()));
        fineLabel.setText(String.valueOf(fine));

        LockerBox lockerBox = new LockerBox(locker);
        lockerVBox.getChildren().add(lockerBox);
    }

    private void displayLockerImage() {
        if (locker.getImagePath() != null && !locker.getImagePath().isBlank()) {
            Image image = new Image("file:" + locker.getImagePath(), 230, 230, true, true);
            itemImage.setImage(image);
        }
    }

    private void configureButtonStates() {
        boolean isLockerActive = locker.isStatus();
        boolean isRequestApproved = request.getRequestType().equals(RequestType.APPROVE);

        setStatusButton.setText(isLockerActive ? "ชำรุด" : "ใช้ได้");

        setAvalibleButton.setDisable(!isLockerActive);

        if (isLockerActive && isRequestApproved) {
            setStatusButton.setDisable(true);
            setAvalibleButton.setDisable(true);
            removeLockerButton.setDisable(true);
            removeKeyLockerButton.setDisable(true);
        } else {
            if(request != null&& request.getRequestType().equals(RequestType.LATE)){
                removeLockerButton.setDisable(true);
            }
            setAvalibleButton.setText(locker.isAvailable() ? "ไม่ว่าง" : "ว่าง");
        }
    }

    private void displayNoRequestInfo() {
        lockerNumberLabel.setText("ไม่มีข้อมูล");
        statusLabel.setText("ไม่มีข้อมูล");
        removeKeyLockerButton.setDisable(true);
    }

    private int calculateBasePrice() {
        long days = selectedDayService.getDaysBetween(request.getStartDate(), request.getEndDate()) + 1;
        return (int) (days * locker.getLockerSizeType().getPrice());
    }

    private int calculateFine(int basePrice) {
        return Math.max(request.getPrice() - basePrice, 0);
    }

    private void initUserInterface() {
        setupUI();
        FilledButtonWithIcon.SMALL.mask(lockerDialog, Icons.LOCKER);
        OutlinedButton.MEDIUM.mask(setAvalibleButton);
        OutlinedButton.MEDIUM.mask(setStatusButton);

        FilledButton.MEDIUM.mask(removeLockerButton);
        OutlinedButton.MEDIUM.mask(removeKeyLockerButton);

        FilledButton.MEDIUM.mask(closeLockerButton);
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
        if (removeKeyLockerButton != null) {
            removeKeyLockerButton.setOnAction(e -> onRemoveKeyButtonClick());
        }
    }

    private void refreshContainerUI() {
        RequestType status = request.getRequestType();

        switch (status) {
            case APPROVE:
            case LATE:
                handleActiveRequestStatus();
                break;
            case PENDING:
                break;
            default:
                renderUnknownStatus();
        }
    }

    private void handleActiveRequestStatus() {
        switch (locker.getLockerType()) {
            case DIGITAL:
                handleDigitalLocker();
                break;
            case MANUAL:
                handleManualLocker();
                break;
            default:
                renderUnknownKeyType();
        }
    }

    private void handleDigitalLocker() {
        renderApproveDigital();
        removeKeyLockerButton.setDisable(true);
    }

    private void handleManualLocker() {
        keyList = keysProvider.loadCollection(zone.getZoneUid());
        key = keyList.findKeyByUid(request.getLockerKeyUid());

        if (key == null) {
            renderUnknownKeyType();
            return;
        }
        removeKeyLockerButton.setDisable(!key.isAvailable());
        lockerKeyTypeLabel.setText(key.getKeyType().toString());
        switch (key.getKeyType()) {
            case MANUAL:
                renderApproveManual();
                break;
            case CHAIN:
                renderApproveChain();
                break;
        }
    }

    private void renderUnknownStatus() {
        containerVBox.getChildren().clear();
        containerVBox.getChildren().add(new Text("ไม่สามารถตรวจสอบสถานะได้"));
        containerVBox.getStyleClass().add("text-error");
    }

    private void renderUnknownKeyType() {
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

    private void renderApproveDigital() {
            if(request.getRequestType().equals(RequestType.APPROVE)) {
                renderApproveDigitalApprove();
            }else {
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
                    if (val == null || val.isBlank() || !val.matches("\\d{5}")) {
                        new AlertUtil().error("รหัสผิดพลาด", "กรุณาใส่รหัสตัวเลขให้ครบ 5 ตัว");
                        return;
                    }
                    refreshContainerUI();
                });

                hBox.getChildren().addAll(codeField, setBtn);
                box.getChildren().addAll(title, hBox);
                containerVBox.getChildren().add(box);
            }
    }

    private void renderApproveDigitalApprove() {
        if(locker.getLockerType().equals(LockerType.DIGITAL)) {
            keyLockerLabel.setText("รหัสดิจิตอล");
            lockerPasswordLabel.setText(locker.getPassword());
        }
    }

    private void renderApproveManual() {
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

    private void renderApproveChain() {
        if (!keyContainerVBox.getChildren().isEmpty() && keyContainerVBox.getChildren().size() > 3) {
            keyContainerVBox.getChildren().removeLast();
        }
        keyLockerLabel.setText("รหัสกุญแจสายรัด:");
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
    private void onRemoveKeyButtonClick(){
        key = keyList.findKeyByUid(request.getLockerKeyUid());
        key.setLockerUid("");
        if(key.getKeyType().equals(KeyType.CHAIN)) {
            key.setPasskey(generateNumberUtil.generateNumberShort());
        }
        key.setAvailable(true);
        keysProvider.saveCollection(zone.getZoneUid(),keyList);
        new AlertUtil().confirm("สถานะกุญแจ","สถานะกุญแจเปลี่ยนแปลงถูกเปลี่ยนแปลงให้ว่างแล้ว");
        onCloseButtonClick();
    }


    void onSetAvalibleButtonClick() {
        if(request !=null && request.getRequestType().equals(RequestType.LATE)){
            request.setRequestType(RequestType.SUCCESS);
            deleteLockerImageFile();
            locker.setImagePath("");
            requestsProvider.saveCollection(zone.getZoneUid(),requestList);
            if(key!=null){
                onRemoveKeyButtonClick();
            }
        }
        locker.setAvailable(!locker.isAvailable());
        if(!locker.isAvailable()){
            requestService.checkIsLockerAvailable(locker);
        }
        lockersProvider.saveCollection(zone.getZoneUid(), lockerList);
        new AlertUtil().confirm("สถานะล็อคเกอร์","สถานะล็อคเกอร์เปลี่ยนแปลงถูกเปลี่ยนแปลงแล้ว");
        onCloseButtonClick();
    }

    private void deleteLockerImageFile() {
        if (locker.getImagePath() != null && !locker.getImagePath().isBlank()) {
            try {
                Path path = Paths.get(locker.getImagePath());
                Files.deleteIfExists(path);
            } catch (IOException e) {
                System.err.println("Warning: ไม่สามารถลบรูปภาพเก่าได้ " + locker.getImagePath());
            }
        }
    }

    private void onRemoveLockerButtonClick() {
        requestService.deleteLocker(locker);
        lockerList.deleteLocker(locker);
        lockersProvider.saveCollection(zone.getZoneUid(), lockerList);
        new AlertUtil().confirm("สถานะล็อคเกอร์","ล็อคเกอร์ถูกลบแล้ว");
        onCloseButtonClick();
    }

    private void onSetStatusButtonClick() {
        locker.setStatus(!locker.isStatus());
        if(!locker.isStatus()){
            requestService.checkIsLockerAvailable(locker);
        }
        lockersProvider.saveCollection(zone.getZoneUid(), lockerList);
        new AlertUtil().confirm("สถานะล็อคเกอร์","สถานะล็อคเกอร์เปลี่ยนแปลงถูกเปลี่ยนแปลงแล้ว");
        onCloseButtonClick();
    }

    private void onCloseButtonClick() {
        if (lockerDialogPane != null && lockerDialogPane.getScene() != null && lockerDialogPane.getScene().getWindow() != null) {
            lockerDialogPane.getScene().getWindow().hide();
        }
        try {
            FXRouter.goTo("officer-manage-lockers");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
