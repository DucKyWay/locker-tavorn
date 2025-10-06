package ku.cs.controllers.user;

import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.button.ElevatedButton;
import ku.cs.components.button.FilledButton;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.components.button.IconButton;
import ku.cs.models.comparator.RequestTimeComparator;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.provider.RequestDatasourceProvider;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.session.SelectedDayService;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.request.RequestService;
import ku.cs.services.utils.TableColumnFactory;
import ku.cs.services.utils.TimeFormatUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

public class UserMyLockerController extends BaseUserController {
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final RequestDatasourceProvider requestsProvider = new RequestDatasourceProvider();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();
    private final SelectedDayService selectedDayService = new SelectedDayService();

    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;

    @FXML private TableView<Request> requestListTableView;
    @FXML private VBox parentVBox;

    @FXML private Button userMyLockerRouteLabelButton;
    @FXML private TextField searchTextField;
    @FXML private Button searchButton;
    @FXML private Button reserveLockerButton;

    private RequestList requestList;
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
        IconButton.mask(searchButton, new Icon(Icons.MAGNIFYING_GLASS, 20));
        ElevatedButton.LABEL.mask(userMyLockerRouteLabelButton);
        FilledButtonWithIcon.SMALL.mask(reserveLockerButton, Icons.LOCKER);

        showTable();
    }

    @Override
    protected void initEvents() {
        requestListTableView.getSelectionModel().selectedItemProperty().addListener(
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
        requestListTableView.getColumns().clear();
        requestListTableView.getColumns().setAll(
                tableColumnFactory.createTextColumn("รหัสจอง", "requestUid", 78),
                tableColumnFactory.createTextColumn("ผู้จอง", "userUsername", 111),
                tableColumnFactory.createTextColumn("จุดให้บริการ", "zoneName"),
                tableColumnFactory.createTextColumn("เริ่มการจอง", "startDate", 115),
                tableColumnFactory.createTextColumn("สิ้นสุดการใช้งาน", "endDate", 115),
                tableColumnFactory.createEnumStatusColumn("สถานะการจอง", "requestType", 159),
                createRequestTimeColumn()
        );

        for (Request req : currentRequestList.getRequestList()) {
            if (selectedDayService.isBooked(req.getStartDate(), req.getEndDate())) {
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
}
