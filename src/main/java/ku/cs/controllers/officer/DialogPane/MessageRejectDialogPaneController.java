package ku.cs.controllers.officer.DialogPane;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import ku.cs.components.Icons;
import ku.cs.components.button.ElevatedButton;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.components.button.FilledButton;
import ku.cs.models.account.Officer;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.request.RequestType;
import ku.cs.models.zone.Zone;
import ku.cs.services.datasources.provider.RequestDatasourceProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.session.SessionManager;
import ku.cs.services.zone.ZoneService;
import ku.cs.services.utils.AlertUtil;

import java.io.IOException;
import java.time.LocalDateTime;

public class MessageRejectDialogPaneController {
    private final SessionManager sessionManager = (SessionManager) FXRouter.getService("session");
    private final RequestDatasourceProvider requestsProvider = new RequestDatasourceProvider();
    private final AlertUtil alertUtil = new AlertUtil();

    @FXML private Label requestUidLabel;
    @FXML private Label lockerUidLabel;
    @FXML private Label userNameLabel;

    @FXML private VBox messageRejectDialogPane;

    @FXML TextField messageTextField;

    @FXML private Button cancelButton;
    @FXML private Button confirmButton;
    @FXML private Button rejectNameButton;

    private RequestList requestList;
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
    }

    private void initEvents() {
        cancelButton.setOnAction(e -> onCancelButtonClick());
        confirmButton.setOnAction(e -> onConfirmButtonClick());
    }
    private void initUserInterface() {
        requestUidLabel.setText(request.getRequestUid());
        lockerUidLabel.setText("ไอดีล็อกเกอร์: " + request.getLockerUid());
        userNameLabel.setText("ผู้ยื่นขอ " + request.getUserUsername());

        ElevatedButtonWithIcon.SMALL.mask(rejectNameButton, Icons.LOCATION);
        rejectNameButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);;

        ElevatedButton.mask(cancelButton);
        FilledButton.mask(confirmButton);
    }
    private void onCancelButtonClick(){
        if (messageRejectDialogPane != null && messageRejectDialogPane.getScene() != null) {
            messageRejectDialogPane.getScene().getWindow().hide();
        }
    }
    private void onConfirmButtonClick(){
        String message = messageTextField.getText();
        if(message!=null && !message.isEmpty()){
            request.setMessage(message);
            request.setRequestTime(LocalDateTime.now());
            request.setRequestType(RequestType.REJECT);
            request.setOfficerUsername(officer.getUsername());
            requestsProvider.saveCollection(zone.getZoneUid(), requestList);
            alertUtil.info("ปฎิเสธสำเร็จ", "ได้ทำการปฎิเสธคำขอของ "+request.getUserUsername()+" สำเร็จ");
            try {
                FXRouter.goTo("officer-zone-request");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Window window = messageRejectDialogPane.getScene().getWindow();
            window.hide();
        }
        else{
            alertUtil.error("เกิดข้อผิดพลาด", "กรุณากรอกข้อมูลให้ครบถ้วน");
        }
    }


}
