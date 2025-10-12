package ku.cs.controllers.locker;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import ku.cs.components.button.ElevatedButton;
import ku.cs.components.button.FilledButton;
import ku.cs.models.account.Account;
import ku.cs.models.locker.Locker;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.context.AppContext;
import ku.cs.services.datasources.provider.RequestDatasourceProvider;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.session.SelectedDayService;
import ku.cs.services.session.SessionManager;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.UuidUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LockerReserveDialogController {
    private final SessionManager sessionManager = AppContext.getSessionManager();
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final RequestDatasourceProvider requestsProvider = new RequestDatasourceProvider();

    @FXML private AnchorPane lockerReserveDialogPane;

    @FXML private ImageView lockerImage;
    @FXML private Label priceLabel;
    @FXML private Label fineLabel;
    @FXML private Label lockerNumberLabel;
    @FXML private Label lockerSizeTypeLabel;
    @FXML private Label lockerUidLabel;
    @FXML private Label lockerZoneLabel;
    @FXML private Label lockerTypeLabel;

    @FXML private Label StartDateTextField;

    @FXML private ComboBox<String> endDateComboBox;


    @FXML private Button cancelButton;
    @FXML private Button confirmButton;

    private RequestList requestList;
    private ZoneList zoneList;
    private Zone zone;
    private int price;
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
        System.out.println("locker: " + locker.getLockerUid());
        ObservableList<String> availableDatesEnd = selectedDayService.populateEndDateComboBox();
        if (endDateComboBox != null) {
            endDateComboBox.setItems(availableDatesEnd);
        }
        endDateComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldTime, String newTime) {
                if(newTime != null) {
                    endDate = LocalDate.parse(newTime);
                    price = (selectedDayService.getDaysBetween(startDate, endDate)+1)*locker.getLockerSizeType().getPrice();
                    priceLabel.setText(String.valueOf(price));
                }
            }
        });
    }

    private void initializeDatasource() {
        zoneList = zonesProvider.loadCollection();
        zone = zoneList.findZoneByUid(locker.getZoneUid());

        requestList = requestsProvider.loadCollection(zone.getZoneUid());
    }

    private void initUserInterface() {
        lockerNumberLabel.setText(String.valueOf(locker.getLockerId()));
        lockerSizeTypeLabel.setText(locker.getLockerSizeType().getDescription());
        lockerUidLabel.setText(locker.getLockerUid());
        lockerZoneLabel.setText(locker.getZoneUid());
        lockerTypeLabel.setText(locker.getLockerType().getDescription());
        ElevatedButton.MEDIUM.mask(cancelButton);
        FilledButton.MEDIUM.mask(confirmButton);
        StartDateTextField.setText(startDate.toString());
        fineLabel.setText(String.valueOf(locker.getLockerSizeType().getFine()));
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
        Request request = new Request(locker.getLockerUid(), startDate, endDate, current.getUsername(), zone.getZoneUid(), LocalDateTime.now(),price);
        if(request.getRequestUid() == null || request.getRequestUid().isEmpty()){
            request.setRequestUid(new UuidUtil().generateShort());
        }

        requestList.addRequest(request);
        requestsProvider.saveCollection(zone.getZoneUid(), requestList);
        new AlertUtil().info("Request Successfully Saved", "Please Check Your Request");
        onCancelButtonClick();
    }
}
