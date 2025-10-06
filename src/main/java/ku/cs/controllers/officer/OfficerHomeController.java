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
import ku.cs.models.locker.*;
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
import ku.cs.services.accounts.strategy.OfficerAccountProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.TableColumnFactory;
import ku.cs.services.utils.TimeFormatUtil;
import ku.cs.services.zone.ZoneService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

public class OfficerHomeController extends BaseOfficerController{
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final RequestDatasourceProvider requestsProvider = new RequestDatasourceProvider();
    private final LockerDatasourceProvider lockersProvider = new LockerDatasourceProvider();
    private final KeyDatasourceProvider keysProvider = new KeyDatasourceProvider();
    private final OfficerAccountProvider officersProvider = new OfficerAccountProvider();
    private final SelectedDayService selectedDayService = new SelectedDayService();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();
    private final TimeFormatUtil timeFormatUtil = new TimeFormatUtil();

    @FXML private VBox officerHomeLabelContainer;
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
        TableColumn<Request, String> uuidColumn = tableColumnFactory.createTextColumn("uuid", "requestUid");
        TableColumn<Request, RequestType> requestTypeColumn = tableColumnFactory.createEnumStatusColumn("สถานะการจอง", "requestType", 0);

        TableColumn<Request, String> idLocker = new TableColumn<>("เลขประจำล็อกเกอร์");
        idLocker.setCellValueFactory(cellData -> {
            Request request = cellData.getValue();
            String lockerId = "ไม่พบล็อกเกอร์";

            for (Locker l : lockerList.getLockers()) {
                if (l.getLockerUid().equals(request.getLockerUid())) {
                    lockerId = String.valueOf(l.getLockerId()); // แปลง int เป็น String
                    break;
                }
            }

            return new javafx.beans.property.SimpleStringProperty(lockerId);
        });

        TableColumn<Request, String> startDateColumn = tableColumnFactory.createTextColumn("เริ่มการจอง", "startDate");
        TableColumn<Request, String> endDateColumn = tableColumnFactory.createTextColumn("สิ้นสุดการจอง", "endDate");
        TableColumn<Request, String> userNameColumn = tableColumnFactory.createTextColumn("ชื่อผู้จอง", "userUsername");
        TableColumn<Request, String> TypeLockerColumn = new TableColumn<>("ประเภทล็อกเกอร์");
        TypeLockerColumn.setCellValueFactory(cellData-> {
            Request request = cellData.getValue();
            String typeLockerColumn = "ไม่ระบุ";
            for (Locker l : lockerList.getLockers()) {
                if (l.getUid().equals(request.getLockerUid())) {
                    typeLockerColumn = l.getLockerType().toString();
                    break;
                }
            }
            return  new javafx.beans.property.SimpleStringProperty(typeLockerColumn);
        });
        TableColumn<Request, String> zoneColumn = tableColumnFactory.createTextColumn("โซน", "zoneName");
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

        TableColumn<Request, Void> actionColumn = createActionColumn();

        requestTableView.getColumns().clear();
        requestTableView.getColumns().addAll(uuidColumn, requestTypeColumn, idLocker,TypeLockerColumn, startDateColumn, endDateColumn, userNameColumn, zoneColumn, requestTimeColumn,actionColumn);
        requestTableView.getItems().clear();
        for (Request req : requestList.getRequestList()) {
            if (selectedDayService.isBooked(req.getStartDate(), req.getEndDate())) {
                requestTableView.getItems().add(req);
            }
        }

    }

    private TableColumn<Request, Void> createActionColumn() {
        return tableColumnFactory.createActionColumn("จัดการ", request -> {
            FilledButtonWithIcon approveBtn;
            if(request.getRequestType() == RequestType.APPROVE){
                approveBtn = FilledButtonWithIcon.small("รายละเอียด", Icons.DETAIL);
            }
            else {
                approveBtn = FilledButtonWithIcon.small("อนุมัติ", Icons.APPROVE);
            }
            final FilledButtonWithIcon RejectBtn = FilledButtonWithIcon.small("ปฎิเสธ", Icons.REJECT);

            if (request.getRequestType() != RequestType.PENDING && request.getRequestType() != RequestType.APPROVE) {
                approveBtn.setDisable(true);
                RejectBtn.setDisable(true);
            } else {
                RejectBtn.setDisable(true);
            }
            if(request.getRequestType() == RequestType.APPROVE){
                approveBtn.setOnAction(e -> onInfoLockerButtonClick(request));
            }else if(request.getRequestType() == RequestType.PENDING){
                approveBtn.setOnAction(e -> onApproveButtonClick(request));
            }
            RejectBtn.setOnAction(e -> onRejectButtonClick(request));

            return new Button[]{approveBtn, RejectBtn};
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
        Locker locker = lockerList.findLockerByUuid(request.getLockerUid());
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
            FXRouter.goTo("officer-manage-reject", request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onBackClick(){
        try {
            FXRouter.goTo("officer-zone-list");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
