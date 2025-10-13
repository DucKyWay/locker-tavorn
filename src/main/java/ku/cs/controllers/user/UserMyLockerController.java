package ku.cs.controllers.user;

import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.button.*;
import ku.cs.models.comparator.TimestampComparator;
import ku.cs.models.dialog.DialogData;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.request.RequestType;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.provider.RequestDatasourceProvider;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.session.SelectedDayService;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.request.RequestService;
import ku.cs.services.utils.SearchService;
import ku.cs.services.utils.TableColumnFactory;
import ku.cs.services.utils.TimeFormatUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class UserMyLockerController extends BaseUserController {
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final RequestDatasourceProvider requestsProvider = new RequestDatasourceProvider();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();
    private final SelectedDayService selectedDayService = new SelectedDayService();
    private final SearchService<Request> searchService = new SearchService<>();

    @FXML private TableView<Request> requestListTableView;

    @FXML private Button userMyLockerRouteLabelButton;
    @FXML private TextField searchTextField;
    @FXML private Button searchButton;
    @FXML private Button reserveLockerButton;

    private ZoneList zoneList;
    private RequestList currentRequestList;
    RequestService requestService = new RequestService();
    TimeFormatUtil timeFormatUtil = new TimeFormatUtil();
    @FXML
    public void initialize() {
        super.initialize();
    }

    @Override
    protected void initDatasource() {
        requestService.updateData();

        zoneList = zonesProvider.loadCollection();
        currentRequestList = new RequestList();

        for(Zone zone : zoneList.getZones()){
            RequestList requestList = requestsProvider.loadCollection(zone.getZoneUid());
            for(Request request : requestList.getRequestList()){
                if(current.getUsername().equals(request.getUserUsername())){
                    currentRequestList.addRequest(request);
                }
            }
        }

        currentRequestList.getRequestList().sort(new TimestampComparator<>());
    }

    @Override
    protected void initUserInterfaces() {
        IconButton.mask(searchButton, new Icon(Icons.MAGNIFYING_GLASS, 20));
        FilledButtonWithIcon.SMALL.mask(reserveLockerButton, Icons.LOCKER);
        ElevatedButtonWithIcon.LABEL.mask(userMyLockerRouteLabelButton, Icons.TAG);

        showTable(currentRequestList);
    }

    @Override
    protected void initEvents() {
        searchTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            onSearch();
        });
        searchButton.setOnAction(e -> onSearch());
        reserveLockerButton.setOnAction(e -> {onReserveLockerButtonClick();});
        requestListTableView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldRequest, newRequest) -> {
                    if (newRequest != null) {
                        try {
                            FXRouter.loadDialogStage("locker-dialog", new DialogData(newRequest,current));
                            showTable(currentRequestList);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
    }

    private void showTable(RequestList currentRequestList) {
        if (requestListTableView != null) requestListTableView.getSelectionModel().clearSelection();
        requestListTableView.getColumns().clear();
        requestListTableView.getItems().clear();
        requestListTableView.getColumns().setAll(
                tableColumnFactory.createTextColumn("รหัสจอง", "requestUid", 78),
                tableColumnFactory.createTextColumn("ผู้จอง", "userUsername", 111),
                tableColumnFactory.createZoneNameColumn("จุดให้บริการ", "zoneUid", zoneList),
                tableColumnFactory.createTextColumn("เริ่มการจอง", "startDate", 115),
                tableColumnFactory.createTextColumn("สิ้นสุดการใช้งาน", "endDate", 115),
                tableColumnFactory.createEnumStatusColumn("สถานะการจอง", "requestType", 159),
                createRequestTimeColumn()
        );

        requestListTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        for (Request req : currentRequestList.getRequestList()) {
            if (selectedDayService.isBooked(req.getStartDate(), req.getEndDate()) || req.getRequestType().equals(RequestType.LATE)) {
                requestListTableView.getItems().add(req);
            }
        }
    }

    private TableColumn<Request, LocalDateTime> createRequestTimeColumn() {
        TableColumn<Request, LocalDateTime> requestTimeColumn = new TableColumn<>("ใช้งานล่าสุด");
        requestTimeColumn.setPrefWidth(112);
        requestTimeColumn.setMinWidth(112);
        requestTimeColumn.setMaxWidth(112);
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
        requestTimeColumn.setStyle("-fx-alignment: CENTER_LEFT; -fx-padding: 0 16");
        return requestTimeColumn;
    }

    private void onSearch() {
        String keyword = searchTextField.getText();
        List<Request> filtered = searchService.search(
                currentRequestList.getRequestList(),
                keyword,
                Request::getRequestUid,
                Request::getZoneUid,
                Request::getLockerUid,
                r -> zoneList.findZoneByUid(r.getZoneUid()).getZoneName()
        );
        RequestList filteredList = new RequestList();
        filtered.forEach(filteredList::addRequest);

        showTable(filteredList);
    }

    private void onReserveLockerButtonClick() {
        try {
            FXRouter.goTo("user-zone");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
