package ku.cs.controllers.officer.DialogPane;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import ku.cs.components.button.ElevatedButton;
import ku.cs.components.button.FilledButton;
import ku.cs.models.account.Officer;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.request.RequestType;
import ku.cs.models.zone.Zone;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;
import ku.cs.services.datasources.provider.RequestDatasourceProvider;
import ku.cs.services.request.RequestService;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.session.SessionManager;
import ku.cs.services.zone.ZoneService;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.GenerateNumberUtil;

import java.io.IOException;
import java.time.LocalDateTime;

public class PasskeyDigitalDialogPaneController {
    private final SessionManager sessionManager  = (SessionManager) FXRouter.getService("session");
    private final RequestDatasourceProvider requestsProvider = new RequestDatasourceProvider();
    private final LockerDatasourceProvider lockersProvider = new LockerDatasourceProvider();
    private final AlertUtil alertUtil = new AlertUtil();
    private final RequestService requestService =  new RequestService();
    private final GenerateNumberUtil generateNumberUtil = new GenerateNumberUtil();
    @FXML private DialogPane passkeyDigitalDialogPane;
    @FXML private TextField passKeyTextField;
    @FXML private Button cancelButton;
    @FXML private Button confirmButton;
    @FXML private Button generateButton;

    @FXML private Label requestUidLabel;
    @FXML private Label lockerUidLabel;
    @FXML private Label userNameLabel;

    private RequestList requestList;
    private LockerList lockerList;
    private Locker locker;

    private Officer officer;
    private Request request;
    private Zone zone;
    private final ZoneService zoneService =  new ZoneService();

    @FXML
    public void initialize() {
        officer = sessionManager.getOfficer();
        request = (Request)FXRouter.getData();
        zone = zoneService.findZoneByUid(request.getZoneUid());



        initialDatasource();
        initEvents();
        initUserInterface();
    }
    private void initialDatasource(){
        requestList = requestsProvider.loadCollection(zone.getZoneUid());
        request = requestList.findRequestByUid(request.getRequestUid());

        lockerList = lockersProvider.loadCollection(zone.getZoneUid());
        locker = lockerList.findLockerByUid(request.getLockerUid());

    }

    private void initEvents() {
        cancelButton.setOnAction(e -> onCancelButtonClick());
        confirmButton.setOnAction(e -> onConfirmButtonClick());
        generateButton.setOnAction(e -> onGenerateButtonClick());
    }
    private void initUserInterface() {
        passKeyTextField.setText(locker.getPassword());
        requestUidLabel.setText(request.getRequestUid());
        lockerUidLabel.setText(locker.getLockerUid());
        userNameLabel.setText(request.getUserUsername());
        ElevatedButton.MEDIUM.mask(cancelButton);
        FilledButton.MEDIUM.mask(confirmButton);
        FilledButton.MEDIUM.mask(generateButton);
        passkeyDigitalDialogPane.getButtonTypes().clear();
    }
    private void onCancelButtonClick(){
        Window window = passkeyDigitalDialogPane.getScene().getWindow();
        window.hide();
    }
    private void  onConfirmButtonClick(){
        String passKey = passKeyTextField.getText();
        if(passKey.length() != 5){
            alertUtil.error("เกิดข้อผิดพลาด", "กรุณากรอกรหัสผ่านให้ครบ 5 หลัก");
        }
        else if(passKey.matches("\\d{5}")){
            request.setRequestType(RequestType.APPROVE);
            request.setRequestTime(LocalDateTime.now());
            request.setOfficerUsername(officer.getUsername());
            request.setLockerKeyUid("");
            locker.setAvailable(false);
            locker.setPassword(passKey);

            // update locker date
            requestList = requestService.checkIsBooked(request,requestList);
            requestsProvider.saveCollection(zone.getZoneUid(), requestList);
            lockersProvider.saveCollection(zone.getZoneUid(), lockerList);

            alertUtil.info("ยืนยันสำเร็จ", request.getUserUsername() + " ได้ทำการจองสำเร็จ ");
            try {
                FXRouter.goTo("officer-home");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Window window = passkeyDigitalDialogPane.getScene().getWindow();
            window.hide();
        }
        else{
            alertUtil.error("เกิดข้อผิดพลาด", "กรุณากรอกรหัสผ่านเป็นตัวเลข");
        }
    }
    private void onGenerateButtonClick(){
        passKeyTextField.setText(generateNumberUtil.generateNumberShort());
    }
}
