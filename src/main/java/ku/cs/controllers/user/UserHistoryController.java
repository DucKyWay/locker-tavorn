package ku.cs.controllers.user;

import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.comparator.RequestTimeComparator;
import ku.cs.models.locker.LockerList;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;
import ku.cs.services.datasources.provider.RequestDatasourceProvider;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.request.RequestService;
import ku.cs.services.session.SelectedDayService;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.TableColumnFactory;
import ku.cs.services.utils.TimeFormatUtil;
import ku.cs.services.zone.ZoneService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

public class UserHistoryController extends BaseUserController {
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final RequestDatasourceProvider requestsProvider = new RequestDatasourceProvider();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();
    private final SelectedDayService selectedDayService = new SelectedDayService();
    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;

    @FXML private TableView<Request> historyListTable;
    RequestService requestService = new RequestService();
    private ZoneList zoneList;
    private RequestList requestList;
    private RequestList currentRequestList;
    TimeFormatUtil timeFormatUtil = new TimeFormatUtil();
    @FXML
    public void initialize() {
        super.initialize();
        requestService.updateData();
        initEvents();
        showTable();
    }
    @Override
    protected void initDatasource() {

        zoneList = zonesProvider.loadCollection();
        currentRequestList = new RequestList();

        for(Zone zone : zoneList.getZones()){
            requestList = requestsProvider.loadCollection(zone.getZoneUid());
            for(Request request : requestList.getRequestList()){
                if(current.getUsername().equals(request.getUserUsername())){
                    currentRequestList.addRequest(request);
                }
            }
        }

        Collections.sort(currentRequestList.getRequestList(),new RequestTimeComparator());
    }

    @Override
    protected void initUserInterfaces() {

    }

    @Override
    protected void initEvents() {
        historyListTable.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldRequest, newRequest) -> {
                    if (newRequest != null) {
                        try {
                            FXRouter.loadDialogStage("locker-dialog", newRequest);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
    }
    private void showTable() {
        historyListTable.getColumns().clear();
        historyListTable.getColumns().setAll(
                tableColumnFactory.createTextColumn("เลขที่", "zoneUid", 55),
                tableColumnFactory.createEnumStatusColumn("สถานะการจอง", "requestType", 120),
                tableColumnFactory.createTextColumn("เริ่มการจอง", "startDate", 112),
                tableColumnFactory.createTextColumn("สิ้นสุดการจอง", "endDate", 77),
                tableColumnFactory.createTextColumn("ผู้จอง", "userUsername", 57),
                tableColumnFactory.createTextColumn("จุดให้บริการ", "zoneName"),
                createRequestTimeColumn()
        );
        for (Request req : currentRequestList.getRequestList()) {
            if (!selectedDayService.isBooked(req.getStartDate(), req.getEndDate())) {
                historyListTable.getItems().add(req);
            }
        }
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
}
