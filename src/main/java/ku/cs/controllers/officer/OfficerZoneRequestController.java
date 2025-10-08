package ku.cs.controllers.officer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.components.Icons;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.models.comparator.RequestTimeComparator;
import ku.cs.models.key.KeyList;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.locker.LockerType;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.request.RequestType;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.provider.KeyDatasourceProvider;
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

public class OfficerZoneRequestController extends BaseOfficerController{
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final RequestDatasourceProvider requestsProvider = new RequestDatasourceProvider();
    private final LockerDatasourceProvider lockersProvider = new LockerDatasourceProvider();
    private final KeyDatasourceProvider keysProvider = new KeyDatasourceProvider();
    private final SelectedDayService selectedDayService = new SelectedDayService();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();
    private final TimeFormatUtil timeFormatUtil = new TimeFormatUtil();

    @FXML
    private VBox officerHomeLabelContainer;
    @FXML private TableView requestTableView;

    private DefaultLabel officerHomeLabel;
    private DefaultButton lockerListButton;
    //test DateList
    RequestService requestService = new RequestService();
    private KeyList keyList;

    //test intitial Zone
    private ZoneList zoneList;
    private RequestList requestList;

    private LockerList lockerList;

    private final ZoneService zoneService = new ZoneService();

    @FXML
    public void initialize() {
        super.initialize();
        requestService.updateData();
        initEvents();
        showTable(requestList);
    }

    @Override
    protected void initDatasource() {
        /* ========== Zone ========== */
        zoneList = zonesProvider.loadCollection();
        zoneService.setLockerToZone(zoneList);

        keyList = keysProvider.loadCollection(currentZone.getZoneUid());
        lockerList = lockersProvider.loadCollection(currentZone.getZoneUid());

        /* ========== Request ========== */
        requestList = requestsProvider.loadCollection(currentZone.getZoneUid());
        Collections.sort(requestList.getRequestList(), new RequestTimeComparator());

        /* ========== Locker Date ========== */
    }

    @Override
    protected void initUserInterfaces() {
        officerHomeLabel = DefaultLabel.h2("จุดให้บริการ " + currentZone.getZoneName() + " | โดยเจ้าหน้าที่ " + current.getUsername());
        lockerListButton = DefaultButton.primary("Locker List");
        officerHomeLabelContainer.getChildren().add(officerHomeLabel);
    }

    @Override protected void initEvents() {
        lockerListButton.setOnAction(e -> onLockerTableButtonClick());
    }

    protected void onLockerTableButtonClick() {
        try {
            FXRouter.goTo("locker-list");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showTable(RequestList requestList) {

        requestTableView.getColumns().clear();
        requestTableView.getItems().clear();

        requestTableView.getColumns().setAll(
                tableColumnFactory.createTextColumn("เลขที่คำร้อง", "requestUid"),
                tableColumnFactory.createEnumStatusColumn("สถานะการจอง", "requestType", 0),
                tableColumnFactory.createTextColumn("เลขประจำล็อคเกอร์", "lockerUid"),
                tableColumnFactory.createShortDateColumn("เริ่มการจอง", "startDate"),
                tableColumnFactory.createShortDateColumn("สิ้นสุดการจอง", "endDate"),
                tableColumnFactory.createTextColumn("ชื่อผู้จอง", "userUsername"),
                createLockerTypeColumn(),
                tableColumnFactory.createZoneNameColumn("จุดให้บริการ", "zoneUid", zoneList),
                createRequestTimeColumn(),
                createActionColumn()
        );

        for (Request req : requestList.getRequestList()) {
            if (selectedDayService.isBooked(req.getStartDate(), req.getEndDate())) {
                requestTableView.getItems().add(req);
            }
        }

    }

    private TableColumn<Request, String> createLockerTypeColumn() {
        TableColumn<Request, String> lockerTypeColumn = new TableColumn<>("ประเภทล็อกเกอร์");
        lockerTypeColumn.setCellValueFactory(cellData-> {
            Request request = cellData.getValue();
            String lockerType = "ไม่ระบุ";
            for (Locker l : lockerList.getLockers()) {
                if (l.getLockerUid().equals(request.getLockerUid())) {
                    lockerType = l.getLockerType().toString();
                    break;
                }
            }
            return  new javafx.beans.property.SimpleStringProperty(lockerType);
        });
        return lockerTypeColumn;
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
        return requestTimeColumn;
    }

    private TableColumn<Request, Void> createActionColumn() {
        return tableColumnFactory.createActionColumn("จัดการ", request -> {
            FilledButtonWithIcon approveBtn;
            FilledButtonWithIcon rejectBtn = FilledButtonWithIcon.small("ปฏิเสธ", Icons.REJECT);
            RequestType type = request.getRequestType();

            switch (type) {
                case APPROVE:
                    approveBtn = FilledButtonWithIcon.small("รายละเอียด", Icons.DETAIL);
                    approveBtn.setOnAction(e -> onInfoLockerButtonClick(request));
                    rejectBtn.setDisable(true); // เมื่ออนุมัติแล้ว ปุ่มปฏิเสธไม่ควรกดได้
                    break;

                case PENDING:
                    approveBtn = FilledButtonWithIcon.small("อนุมัติ", Icons.APPROVE);
                    approveBtn.setOnAction(e -> onApproveButtonClick(request));
                    rejectBtn.setOnAction(e -> onRejectButtonClick(request));
                    break;

                default:
                    approveBtn = FilledButtonWithIcon.small("อนุมัติ", Icons.APPROVE);
                    approveBtn.setDisable(true);
                    rejectBtn.setDisable(true);
                    break;
            }

            return new Button[]{approveBtn, rejectBtn};
        });
    }

    private void onInfoLockerButtonClick(Request request){
        try {
            FXRouter.loadDialogStage("officer-request-info", request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void onApproveButtonClick(Request request) {
        Locker locker = lockerList.findLockerByUid(request.getLockerUid());
        if(locker.isAvailable()) {
            if (locker.getLockerType() == LockerType.MANUAL) {
                try {
                    FXRouter.loadDialogStage("officer-select-key-list", request);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                try {
                    FXRouter.loadDialogStage("officer-passkey-digital", request);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            requestTableView.refresh();
        } else {
            //ต้องบอก request ว่าล็อกเกอร์ไม่ว่างแล้ว แล้วต้องสร้าง messenger บอกว่า ตู้ถูกจองไปแล้ว โดยอัตโนมัติ
        }
    }

    private void onRejectButtonClick(Request request) {
        try {
            FXRouter.loadDialogStage("officer-manage-reject", request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onBackButtonClick(){
        try {
            FXRouter.goTo("officer-home", currentZone);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
