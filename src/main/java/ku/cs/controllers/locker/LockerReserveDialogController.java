package ku.cs.controllers.locker;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import ku.cs.components.button.ElevatedButton;
import ku.cs.components.button.FilledButton;
import ku.cs.models.account.Account;
import ku.cs.models.locker.Locker;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.AppContext;
import ku.cs.services.FXRouter;
import ku.cs.services.SelectedDayService;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.RequestListFileDatasource;
import ku.cs.services.datasources.ZoneListFileDatasource;
import ku.cs.services.utils.UuidUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LockerReserveDialogController {
    private final SessionManager sessionManager = AppContext.getSessionManager();

    @FXML private AnchorPane lockerReserveDialogPane;

    @FXML private ImageView lockerImage;

    @FXML private Label lockerNumberLabel;
    @FXML private Label lockerIdLabel;
    @FXML private Label lockerZoneLabel;
    @FXML private Label lockerTypeLabel;

    @FXML private Label StartDateTextField;

    @FXML private ComboBox<String> endDateComboBox;


    @FXML private Button cancelButton;
    @FXML private Button confirmButton;

    private Datasource<RequestList> requestListDatasource;
    private RequestList requestList;
    private Datasource<ZoneList> zoneListDatasource;
    private ZoneList zoneList;
    private Zone zone;
    private final SelectedDayService selectedDayService = new SelectedDayService();
    private LocalDate startDate = LocalDate.parse(LocalDate.now().format(selectedDayService.FORMATTER));
    private LocalDate endDate;
    ObservableList<String> availableDatesStart;
    private Locker locker;
    Account current = sessionManager.getCurrentAccount();
    @FXML
    private void initialize() {
        locker = (Locker) FXRouter.getData();
        initUserInterface();
        initializeDatasource();
        initEvents();
        System.out.println("locker: " + locker.getUuid());
        ObservableList<String> availableDatesEnd = selectedDayService.populateEndDateComboBox();
        if (endDateComboBox != null) {
            endDateComboBox.setItems(availableDatesEnd);
        }
        endDateComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldTime, String newTime) {
                if(newTime != null) {
                    endDate = LocalDate.parse(newTime);
                }
            }
        });
    }

    private void initializeDatasource() {
        zoneListDatasource = new ZoneListFileDatasource("data", "test-zone-data.json");
        zoneList = zoneListDatasource.readData();
        zone = zoneList.findZoneByName(locker.getZone());

        requestListDatasource =new RequestListFileDatasource("data/requests","zone-"+zone.getZoneUid()+".json");
        requestList = requestListDatasource.readData();
    }

    private void initUserInterface() {
        ElevatedButton.MEDIUM.mask(cancelButton);
        FilledButton.MEDIUM.mask(confirmButton);
        StartDateTextField.setText(startDate.toString());
    }

    private void initEvents() {

        cancelButton.setOnAction(e -> onCancelButtonClick());
        confirmButton.setOnAction(e -> onConfirmButtonClick());
    }

    private void onCancelButtonClick(){
        if (lockerReserveDialogPane != null && lockerReserveDialogPane.getScene() != null && lockerReserveDialogPane.getScene().getWindow() != null) {
            lockerReserveDialogPane.getScene().getWindow().hide();
        }
    }
    private void onConfirmButtonClick(){
        Request request =new Request(locker.getUuid(),startDate,endDate,current.getUsername(),locker.getZone(),"", LocalDateTime.now());
        if(request.getUuid() == null || request.getUuid().isEmpty()){
            request.setUuid(UuidUtil.generateShort());
        }

        requestList.addRequest(request);
        requestListDatasource.writeData(requestList);
        showAlert(Alert.AlertType.INFORMATION, "Request Successfully Saved", "Please Check Your Request");
        onCancelButtonClick();
    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
