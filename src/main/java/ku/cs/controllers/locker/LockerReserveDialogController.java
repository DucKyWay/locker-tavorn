package ku.cs.controllers.locker;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
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
import ku.cs.services.FXRouter;
import ku.cs.services.SelectedDayService;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.RequestListFileDatasource;
import ku.cs.services.datasources.ZoneListFileDatasource;
import ku.cs.services.utils.UuidUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LockerReserveDialogController {

    @FXML private DialogPane lockerReserveDialogPane;

    @FXML private ImageView lockerImage;

    @FXML private Label lockerNumberLabel;
    @FXML private Label lockerIdLabel;
    @FXML private Label lockerZoneLabel;
    @FXML private Label lockerTypeLabel;

    @FXML private VBox usernameTextFieldVBox;
    @FXML private Label usernameLabel;
    @FXML private ComboBox<String> startDateComboBox;

    @FXML private VBox usernameTextFieldVBox1;
    @FXML private Label usernameLabel1;
    @FXML private ComboBox<String> endDateComboBox;


    @FXML private Button cancelButton;
    @FXML private Button confirmButton;

    private Datasource<RequestList> requestListDatasource;
    private RequestList requestList;
    private Datasource<ZoneList> zoneListDatasource;
    private ZoneList zoneList;
    private Zone zone;
    private LocalDate startDate;
    private LocalDate endDate;
    ObservableList<String> availableDatesStart;
    ObservableList<String> availableDatesEnd;
    private Locker locker;
    Account current = SessionManager.getCurrentAccount();
    @FXML
    private void initialize() {
        locker = (Locker) FXRouter.getData();
        initUserInterface();
        initializeDatasource();
        initEvents();
        System.out.println("locker: " + locker.getUuid());
        ObservableList<String> availableDatesStart = SelectedDayService.populateStartDateComboBox(zone.getIdZone(), locker.getUuid());
        if (startDateComboBox != null) {
            startDateComboBox.setItems(availableDatesStart);
        }
        endDateComboBox.setDisable(true);
        startDateComboBox.valueProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldTime, String newTime) {
                if(newTime != null) {
                    startDate = LocalDate.parse(newTime);
                    endDateComboBox.setDisable(false);
                    availableDatesEnd = SelectedDayService.populateEndDateComboBox(zone.getIdZone(),locker.getUuid(),startDate);
                    endDateComboBox.setItems(availableDatesEnd);
                }
            }
        });
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

        requestListDatasource =new RequestListFileDatasource("data/requests","zone-"+zone.getIdZone()+".json");
        requestList = requestListDatasource.readData();
    }

    private void initUserInterface() {
        ElevatedButton.MEDIUM.mask(cancelButton);
        FilledButton.MEDIUM.mask(confirmButton);
        lockerReserveDialogPane.getButtonTypes().clear();
        startDateComboBox.setItems(availableDatesStart);
    }

    private void initEvents() {

        cancelButton.setOnAction(e -> onCancelButtonClick());
        confirmButton.setOnAction(e -> onConfirmButtonClick());
    }

    private void onCancelButtonClick(){
        Window window = lockerReserveDialogPane.getScene().getWindow();
        window.hide();
    }
    private void onConfirmButtonClick(){
        Request request =new Request(locker.getUuid(),startDate,endDate,current.getUsername(),locker.getZone(),"", LocalDateTime.now());
        if(request.getUuid() == null || request.getUuid().isEmpty()){
            request.setUuid(UuidUtil.generateShort());
        }

        requestList.addRequest(request);
        requestListDatasource.writeData(requestList);
        showAlert(Alert.AlertType.INFORMATION, "Request Successfully Saved", "Please Check Your Request");
    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
