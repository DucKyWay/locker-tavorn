package ku.cs.controllers.user;

import javafx.fxml.FXML;

import javafx.scene.control.*;
import ku.cs.models.comparator.RequestTimeComparator;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.provider.RequestDatasourceProvider;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.request.RequestService;
import ku.cs.services.utils.TableColumnFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;

public class UserMyLockerController extends BaseUserController {
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final RequestDatasourceProvider requestsProvider = new RequestDatasourceProvider();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();

    @FXML
    private Label titleLabel;
    @FXML private Label descriptionLabel;

    @FXML private TableView<Request> requestListTableView;
    private RequestList requestList;
    private ZoneList zoneList;
    private RequestList currentRequestList;
    RequestService requestService = new RequestService();

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
                tableColumnFactory.createTextColumn("เลขที่", "zoneUid", 55),
                tableColumnFactory.createEnumStatusColumn("สถานะการจอง", "requestType", 120),
                tableColumnFactory.createTextColumn("เริ่มการจอง", "startDate", 112),
                tableColumnFactory.createTextColumn("สิ้นสุดการจอง", "endDate", 77),
                tableColumnFactory.createTextColumn("ผู้จอง", "userUsername", 57),
                tableColumnFactory.createTextColumn("จุดให้บริการ", "zoneName"),
                createRequestTimeColumn()
        );

        requestListTableView.getItems().setAll(currentRequestList.getRequestList());
    }

    private TableColumn<Request, LocalDateTime> createRequestTimeColumn() {
        TableColumn<Request, LocalDateTime> requestTimeColumn = new TableColumn<>("เวลาเข้าถึงล่าสุด");
        requestTimeColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // คำนวณระยะเวลาจากปัจจุบัน
                    Duration duration = Duration.between(item, LocalDateTime.now());

                    long seconds = duration.getSeconds();
                    String text;
                    if (seconds < 60) {
                        text = "ใช้งานล่าสุดเมื่อ " + seconds + " วินาทีที่แล้ว";
                    } else if (seconds < 3600) {
                        long minutes = seconds / 60;
                        text = "ใช้งานล่าสุดเมื่อ " + minutes + " นาทีที่แล้ว";
                    } else if (seconds < 86400) {
                        long hours = seconds / 3600;
                        text = "ใช้งานล่าสุดเมื่อ " + hours + " ชั่วโมงที่แล้ว";
                    } else {
                        long days = seconds / 86400;
                        text = "ใช้งานล่าสุดเมื่อ " + days + " วันที่แล้ว";
                    }
                    setText(text);
                }
            }
        });

        requestTimeColumn.setStyle("-fx-alignment: CENTER;");
        requestTimeColumn.setPrefWidth(500);
        return requestTimeColumn;
    }
}
