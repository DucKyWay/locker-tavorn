package ku.cs.controllers.user;

import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.components.button.IconButton;
import ku.cs.models.comparator.TimestampComparator;
import ku.cs.models.dialog.DialogData;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.request.RequestType;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.provider.RequestDatasourceProvider;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.request.RequestService;
import ku.cs.services.session.SelectedDayService;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.SearchService;
import ku.cs.services.utils.TableColumnFactory;
import ku.cs.services.utils.TimeFormatUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class UserHistoryController extends BaseUserController {
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final RequestDatasourceProvider requestsProvider = new RequestDatasourceProvider();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();
    private final SelectedDayService selectedDayService = new SelectedDayService();
    private final SearchService<Request> searchService = new SearchService<>();

    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;

    @FXML private TableView<Request> historyListTable;

    @FXML private TextField searchTextField;
    @FXML private Button searchButton;

    @FXML private Button userReserveHistoryRouteLabelButton;

    RequestService requestService = new RequestService();
    private RequestList currentRequestList;
    TimeFormatUtil timeFormatUtil = new TimeFormatUtil();

    @FXML
    public void initialize() {
        super.initialize();
        requestService.updateData();
    }
    @Override
    protected void initDatasource() {

        ZoneList zoneList = zonesProvider.loadCollection();
        currentRequestList = new RequestList();

        for(Zone zone : zoneList.getZones()){
            RequestList requestList = requestsProvider.loadCollection(zone.getZoneUid());
            for(Request request : requestList.getRequestList()){
                if(current.getUsername().equals(request.getUserUsername())){
                    currentRequestList.addRequest(request);
                }
            }
        }
        Collections.sort(currentRequestList.getRequestList(),new TimestampComparator<>());
    }

    @Override
    protected void initUserInterfaces() {
        ElevatedButtonWithIcon.LABEL.mask(userReserveHistoryRouteLabelButton, Icons.TAG);
        IconButton.mask(searchButton, new Icon(Icons.MAGNIFYING_GLASS));

        showTable(currentRequestList);
    }

    @Override
    protected void initEvents() {
        historyListTable.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldRequest, newRequest) -> {
                    if (newRequest != null) {
                        try {
                            FXRouter.loadDialogStage("locker-dialog",  new DialogData(newRequest,current));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } finally {
                            // Async clear
                            Platform.runLater(() -> historyListTable.getSelectionModel().clearSelection());
                        }
                    }
                }
        );
        searchTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            onSearch();
        });
    }
    private void showTable(RequestList requestList) {
        historyListTable.getColumns().clear();
        historyListTable.getColumns().setAll(
                tableColumnFactory.createTextColumn("ไอดีจุดให้บริการ", "zoneUid", 100),
                tableColumnFactory.createEnumStatusColumn("สถานะการจอง", "requestType", 120),
                tableColumnFactory.createTextColumn("เริ่มการจอง", "startDate"),
                tableColumnFactory.createTextColumn("สิ้นสุดการจอง", "endDate"),
                tableColumnFactory.createTextColumn("ผู้กดยืนยันการจอง", "officerUsername", 150),
                tableColumnFactory.createTextColumn("จุดให้บริการ", "zoneName"),
                createRequestTimeColumn()
        );
        for (Request req : requestList.getRequestList()) {
            if (!selectedDayService.isBooked(req.getStartDate(), req.getEndDate()) &&  !req.getRequestType().equals(RequestType.LATE)) {
                historyListTable.getItems().add(req);
            }
        }
        historyListTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }
    private TableColumn<Request, LocalDateTime> createRequestTimeColumn() {
        TableColumn<Request, LocalDateTime> requestTimeColumn = new TableColumn<>("เวลาเข้าถึงล่าสุด");
        requestTimeColumn.setCellValueFactory(new PropertyValueFactory<>("requestTime"));
        requestTimeColumn.setCellFactory(column -> new TableCell<Request, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(timeFormatUtil.localDateTimeToString(item));
                }
            }
        });
        requestTimeColumn.setStyle("-fx-alignment: CENTER;");
        return requestTimeColumn;
    }

    private void onSearch() {
        String keyword = searchTextField.getText();
        List<Request> filtered = searchService.search(
                currentRequestList.getRequestList(),
                keyword,
                Request::getRequestUid,
                Request::getZoneUid,
                Request::getLockerUid
        );
        RequestList filteredList = new RequestList();
        filtered.forEach(filteredList::addRequest);

        showTable(filteredList);
    }
}
