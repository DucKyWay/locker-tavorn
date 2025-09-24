package ku.cs.controllers.officer.DialogPane;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.ZoneService;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.RequestListFileDatasource;
import ku.cs.services.utils.AlertUtil;

import java.io.IOException;
import java.time.LocalDateTime;

public class MessageRejectDialogPaneController {
    @FXML
    private DialogPane messageRejectDialogPane;
    @FXML TextField messageTextField;
    @FXML private Button cancelButton;
    @FXML private Button confirmButton;

    private Datasource<RequestList> requestListDatasource;
    private RequestList requestList;
    private Officer officer;
    private Request request;
    private Zone zone;
    @FXML
    public void initialize() {
        officer = SessionManager.getOfficer();
        Object data = FXRouter.getData();
        if (data instanceof Request) {
            request = (Request) data;
            zone = ZoneService.findZoneByName(request.getZone());
        } else {
            System.out.println("Error: Data is not an Request");
        }
        initialDatasource();
        initEvents();
        initUserInterface();
    }
    private void initialDatasource(){
        requestListDatasource = new RequestListFileDatasource("data/requests","zone-"+zone.getIdZone()+".json");
        requestList = requestListDatasource.readData();
        request = requestList.findRequestByUuid(request.getUuid());
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
        System.out.println("message: "+message);
        if(message!=null && !message.isEmpty()){
            request.setMessenger(message);
            request.setRequestTime(LocalDateTime.now());
            request.setRequestType(RequestType.REJECT);
            request.setOfficerName(officer.getUsername());
            requestListDatasource.writeData(requestList);
            AlertUtil.info("ปฎิเสธสำเร็จ", "ได้ทำการปฎิเสธคำขอของ "+request.getUserName()+" สำเร็จ");
            try {
                FXRouter.goTo("officer-home");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            AlertUtil.error("เกิดข้อผิดพลาด", "กรุณากรอกข้อมูลให้ครบถ้วน");
        }
    }


}
