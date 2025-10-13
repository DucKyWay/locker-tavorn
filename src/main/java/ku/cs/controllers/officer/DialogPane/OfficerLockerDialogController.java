package ku.cs.controllers.officer.DialogPane;

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
import ku.cs.components.button.OutlinedButton;
import ku.cs.models.key.KeyList;
import ku.cs.models.key.Key;
import ku.cs.models.key.KeyType;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
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
import ku.cs.services.utils.GenerateNumberUtil;

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
    @FXML private AnchorPane lockerDialogPane;
    @FXML private ImageView itemImage;

    @FXML private Label lockerNumberLabel;

    @FXML private Label statusLabel;
    @FXML private Label lockerSizeTypeLabel;
    @FXML private Label priceLabel;
    @FXML private Label totalpriceLabel;
    @FXML private Label fineLabel;

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
    @FXML private Button removeKeyLockerButton;

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
        zone = zoneList.findZoneByUid(inputLocker.getZoneUid());

        lockerList = lockersProvider.loadCollection(zone.getZoneUid());
        locker = lockerList.findLockerByUid(inputLocker.getLockerUid());

        requestList = requestsProvider.loadCollection(zone.getZoneUid());
        for (Request r : requestList.getRequestList()) {
            if (r.getLockerUid().equals(locker.getLockerUid()) && r.getRequestType() ==RequestType.APPROVE) {
                request = r;
                break;
            }
        }
        if(request == null) {
            for (Request r : requestList.getRequestList()) {
                if (r.getLockerUid().equals(locker.getLockerUid()) && r.getRequestType() ==RequestType.LATE) {
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
        statusLabel.setText(request.getRequestType().toString());
        lockerIdLabel.setText(request.getLockerUid());
        lockerZoneLabel.setText(request.getZoneUid());
        lockerTypeLabel.setText(locker.getLockerType().toString());
        usernameLabel.setText(request.getUserUsername());
        startDateLabel.setText(request.getStartDate().toString());
        endDateLabel.setText(request.getEndDate().toString());
        lockerSizeTypeLabel.setText(locker.getLockerSizeTypeString());
        int basePrice = calculateBasePrice();
        int fine = calculateFine(basePrice);

        priceLabel.setText(String.valueOf(basePrice));
        totalpriceLabel.setText(String.valueOf(request.getPrice()));
        fineLabel.setText(String.valueOf(fine));
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

        setStatusButton.setText(isLockerActive ? "ล็อกเกอร์ชำรุด" : "ล็อกเกอร์พร้อมใช้งาน");

        setAvalibleButton.setDisable(!isLockerActive);

        if (isLockerActive && isRequestApproved) {
            setStatusButton.setDisable(true);
            setAvalibleButton.setDisable(true);
            removeLockerButton.setDisable(true);
            removeKeyLockerButton.setDisable(true);
        } else {
            setAvalibleButton.setText(locker.isAvailable() ? "ล็อกเกอร์ไม่ว่าง" : "ล็อกเกอร์ว่าง");
        }
    }

    private void displayNoRequestInfo() {
        lockerNumberLabel.setText("ไม่มีข้อมูล");
        statusLabel.setText("ไม่มีข้อมูล");
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
        FilledButton.MEDIUM.mask(closeLockerButton);
        ElevatedButton.MEDIUM.mask(setAvalibleButton);
        FilledButton.MEDIUM.mask(setStatusButton);
        OutlinedButton.MEDIUM.mask(removeKeyLockerButton);
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
        if (removeKeyLockerButton != null) {
            removeKeyLockerButton.setOnAction(e -> onRemoveKeyButtonClick());
        }
    }
    private void refreshContainerUI() {
        containerHBox.getChildren().clear();
        RequestType status = request.getRequestType();

        switch (status) {
            case APPROVE:
            case LATE:
                handleActiveRequestStatus();
                break;
            case PENDING:
                renderPending();
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
        removeKeyLockerButton.setDisable(key.isAvailable());
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
        containerHBox.getChildren().add(new Text("Unknown status"));
    }

    private void renderUnknownKeyType() {
        containerHBox.getChildren().add(new Text("Unknown key type"));
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
                containerHBox.getChildren().add(box);
            }
    }
    private void renderApproveDigitalApprove() {
        VBox box = new VBox(6);
        box.setFillWidth(true);
        Label passkey = new Label("");
        Label title = new Label("Digital code");
        passkey.setText(locker.getPassword());
        box.getChildren().addAll(title,passkey);
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

    private void renderPending() {
        Label l = new Label("Status: PENDING");
        containerHBox.getChildren().add(l);
    }


    private void onSetAvalibleButtonClick() {
        if(request !=null && request.getRequestType().equals(RequestType.LATE)){
            request.setRequestType(RequestType.SUCCESS);
            requestsProvider.saveCollection(zone.getZoneUid(),requestList);
            deleteLockerImageFile();
            if(key!=null){
                onRemoveKeyButtonClick();
            }
        }
        locker.setAvailable(!locker.isAvailable());
        lockersProvider.saveCollection(zone.getZoneUid(), lockerList);
        new AlertUtil().confirm("สถานะล็อกเกอร์","สถานะล็อกเกอร์เปลี่ยนแปลงถูกเปลี่ยนแปลงแล้ว");
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
        lockerList.deleteLocker(locker);
        lockersProvider.saveCollection(zone.getZoneUid(), lockerList);
        new AlertUtil().confirm("สถานะล็อกเกอร์","ล็อกเกอร์ถูกลบแล้ว");
        onCloseButtonClick();
    }

    private void onSetStatusButtonClick() {
        locker.setStatus(!locker.isStatus());
        lockersProvider.saveCollection(zone.getZoneUid(), lockerList);
        new AlertUtil().confirm("สถานะล็อกเกอร์","สถานะล็อกเกอร์เปลี่ยนแปลงถูกเปลี่ยนแปลงแล้ว");
        onCloseButtonClick();
    }

    private void onCloseButtonClick() {
        if (lockerDialogPane != null && lockerDialogPane.getScene() != null && lockerDialogPane.getScene().getWindow() != null) {
            lockerDialogPane.getScene().getWindow().hide();
        }
    }
}
