package ku.cs.controllers.officer.DialogPane;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
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
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.ZoneService;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.LockerListFileDatasource;
import ku.cs.services.datasources.RequestListFileDatasource;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.GenerateNumberUtil;

import java.io.IOException;
import java.time.LocalDateTime;

public class PasskeyDigitalDialogPaneController {
    @FXML private DialogPane passkeyDigitalDialogPane;
    @FXML private TextField passKeyTextField;
    @FXML private Button cancelButton;
    @FXML private Button confirmButton;
    @FXML private Button generateButton;

    private Datasource<RequestList> requestListDatasource;
    private RequestList requestList;

    private Datasource<LockerList> lockerListDatasource;
    private LockerList lockerList;
    private Locker locker;

    private Officer officer;
    private Request request;
    private Zone zone;
    private ZoneService zoneService =  new ZoneService();

    @FXML
    public void initialize() {
        officer = SessionManager.getOfficer();
        Object data = FXRouter.getData();
        if (data instanceof Request) {
            request = (Request) data;
            zone = zoneService.findZoneByName(request.getZone());
        } else {
            System.out.println("Error: Data is not an Request");
        }
        initialDatasource();
        initEvents();
        initUserInterface();
    }
    private void initialDatasource(){
        requestListDatasource = new RequestListFileDatasource("data/requests","zone-"+zone.getZoneUid()+".json");
        requestList = requestListDatasource.readData();
        request = requestList.findRequestByUuid(request.getUuid());

        lockerListDatasource = new LockerListFileDatasource("data/lockers","zone-"+zone.getZoneUid()+".json");
        lockerList = lockerListDatasource.readData();
        locker = lockerList.findLockerByUuid(request.getUuidLocker());

    }

    private void initEvents() {
        cancelButton.setOnAction(e -> onCancelButtonClick());
        confirmButton.setOnAction(e -> onConfirmButtonClick());
        generateButton.setOnAction(e -> onGenerateButtonClick());
    }
    private void initUserInterface() {
        passKeyTextField.setText(locker.getPassword());
        ElevatedButton.MEDIUM.mask(cancelButton);
        FilledButton.MEDIUM.mask(confirmButton);
        passkeyDigitalDialogPane.getButtonTypes().clear();
    }
    private void onCancelButtonClick(){
        Window window = passkeyDigitalDialogPane.getScene().getWindow();
        window.hide();
    }
    private void  onConfirmButtonClick(){
        String passKey = passKeyTextField.getText();
        if(passKey.isEmpty() || passKey.length() !=5){
            AlertUtil.error("เกิดข้อผิดพลาด", "กรุณากรอกรหัสผ่านให้ครบ 5 หลัก");
        }
        else if(passKey.matches("\\d{5}")){
            request.setRequestType(RequestType.APPROVE);
            request.setRequestTime(LocalDateTime.now());
            request.setOfficerName(officer.getUsername());
            request.setUuidKeyLocker("");
            locker.setPassword(passKey);

            // update locker date

            requestListDatasource.writeData(requestList);
            lockerListDatasource.writeData(lockerList);
            AlertUtil.info("ยืนยันสำเร็จ", request.getUserName() + " ได้ทำการจองสำเร็จ ");
            try {
                FXRouter.goTo("officer-home");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Window window = passkeyDigitalDialogPane.getScene().getWindow();
            window.hide();
        }
        else{
            AlertUtil.error("เกิดข้อผิดพลาด", "กรุณากรอกรหัสผ่านเป็นตัวเลข");
        }
    }
    private void onGenerateButtonClick(){
        passKeyTextField.setText(GenerateNumberUtil.generateNumberShort());
    }
}
