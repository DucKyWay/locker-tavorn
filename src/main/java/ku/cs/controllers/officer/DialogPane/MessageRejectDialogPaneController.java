package ku.cs.controllers.officer.DialogPane;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import ku.cs.components.button.ElevatedButton;
import ku.cs.components.button.FilledButton;
import ku.cs.models.account.Officer;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.request.RequestType;
import ku.cs.models.zone.Zone;
import ku.cs.services.context.AppContext;
import ku.cs.services.datasources.provider.RequestDatasourceProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.session.SessionManager;
import ku.cs.services.zone.ZoneService;
import ku.cs.services.datasources.RequestListFileDatasource;
import ku.cs.services.utils.AlertUtil;

import java.io.IOException;
import java.time.LocalDateTime;

public class MessageRejectDialogPaneController {
    private final SessionManager sessionManager = AppContext.getSessionManager();
    private final RequestDatasourceProvider requestsProvider = new RequestDatasourceProvider();
    private final AlertUtil alertUtil = new AlertUtil();

    @FXML
    private DialogPane messageRejectDialogPane;
    @FXML TextField messageTextField;
    @FXML private Button cancelButton;
    @FXML private Button confirmButton;

    private RequestList requestList;
    private Officer officer;
    private Request request;
    private Zone zone;
    private ZoneService zoneService =  new ZoneService();
    @FXML
    public void initialize() {
        officer = sessionManager.getOfficer();
        Object data = FXRouter.getData();
        if (data instanceof Request) {
            request = (Request) data;
            zone = zoneService.findZoneByName(request.getZoneName());
        } else {
            System.out.println("Error: Data is not an Request");
        }
        initialDatasource();
        initEvents();
        initUserInterface();
    }
    private void initialDatasource(){
        requestList = requestsProvider.loadCollection(zone.getZoneUid());
        request = requestList.findRequestByUuid(request.getRequestUid());
    }

    private void initEvents() {
        cancelButton.setOnAction(e -> onCancelButtonClick());
        confirmButton.setOnAction(e -> onConfirmButtonClick());
    }
    private void initUserInterface() {
        ElevatedButton.MEDIUM.mask(cancelButton);
        FilledButton.MEDIUM.mask(confirmButton);
        messageRejectDialogPane.getButtonTypes().clear();
    }
    private void onCancelButtonClick(){
        Window window = messageRejectDialogPane.getScene().getWindow();
        window.hide();
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
                FXRouter.goTo("officer-home");
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
