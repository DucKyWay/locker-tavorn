package ku.cs.controllers.officer;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.components.Icons;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.models.comparator.RequestTimeComparator;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.locker.LockerType;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.request.RequestType;
//import ku.cs.services.*;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;
import ku.cs.services.datasources.provider.RequestDatasourceProvider;
import ku.cs.services.request.RequestService;
import ku.cs.services.session.SelectedDayService;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.TableColumnFactory;
import ku.cs.services.utils.TimeFormatUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

public class OfficerTableLockerHistoryController extends BaseOfficerController{
    private final RequestDatasourceProvider requestsProvider = new RequestDatasourceProvider();
    private final LockerDatasourceProvider lockersProvider = new LockerDatasourceProvider();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();
    private final TimeFormatUtil timeFormatUtil = new TimeFormatUtil();

    private SelectedDayService selectedDayService = new SelectedDayService();
    RequestService requestService = new RequestService();
    RequestList requestList;
    LockerList lockerList;

    @FXML private TableView<Request> requestTableView;

    @FXML
    public void initialize() {
        super.initialize();
        requestService.updateData();
    }

    @Override
    protected void initDatasource() {
        /* ========== Locker ========== */;
        lockerList = lockersProvider.loadCollection(currentZone.getZoneUid());

        /* ========== Request ========== */
        requestList = requestsProvider.loadCollection(currentZone.getZoneUid());
        Collections.sort(requestList.getRequestList(), new RequestTimeComparator());
    }

    @Override
    protected void initUserInterfaces() {
        showTable(requestList);
    }

    @Override
    protected void initEvents() {

    }

    private void showTable(RequestList requestList) {
        requestTableView.getColumns().clear();
        requestTableView.getColumns().setAll(
                tableColumnFactory.createTextColumn("เลขที่คำร้อง", "requestUid"),
                tableColumnFactory.createEnumStatusColumn("สถานะการจอง", "requestType", 0),
                createLockerUidColumn(),
                createLockerTypeColumn(),
                tableColumnFactory.createTextColumn("เริ่มการจอง", "startDate"),
                tableColumnFactory.createTextColumn("สิ้นสุดการจอง", "endDate"),
                tableColumnFactory.createTextColumn("ชื่อผู้จอง", "userUsername"),

                tableColumnFactory.createTextColumn("โซน", "zoneName"),
                createLastTimeColumn(),
                createActionColumn()
        );

        for (Request req : requestList.getRequestList()) {
            if (!selectedDayService.isBooked(req.getStartDate(), req.getEndDate())) {
                requestTableView.getItems().add(req);
            }
        }
    }

    private TableColumn<Request,String> createLockerTypeColumn() {
        TableColumn<Request, String> TypeLockerColumn = new TableColumn<>("เลขประจำล็อกเกอร์");
        TypeLockerColumn.setCellValueFactory(new PropertyValueFactory<>("lockerUid"));
        TypeLockerColumn.setCellValueFactory((TableColumn.CellDataFeatures<Request, String> col) -> {
            Request request = col.getValue();
            for (Locker l : lockerList.getLockers()) {
                if (l.getLockerUid().equals(request.getLockerUid())) {
                    return new SimpleStringProperty(l.getLockerType().toString());
                }
            }
            return new SimpleStringProperty("ไม่พบล็อกเกอร์");
        });
        return TypeLockerColumn;
    }

    private TableColumn<Request, String> createLockerUidColumn() {
        TableColumn<Request, String> lockerUid = new TableColumn<>("เลขประจำล็อกเกอร์");
        lockerUid.setCellValueFactory(new PropertyValueFactory<>("lockerUid"));
        lockerUid.setCellValueFactory((TableColumn.CellDataFeatures<Request, String> col) -> {
            Request request = col.getValue();

            for (Locker l : lockerList.getLockers()) {
                if (l.getLockerUid().equals(request.getLockerUid())) {
                    return new SimpleStringProperty(l.getLockerUid());
                }
            }
            return new SimpleStringProperty("ไม่พบล็อกเกอร์");
        });
        return lockerUid;
    }

    private TableColumn<Request, LocalDateTime> createLastTimeColumn() {
        TableColumn<Request, LocalDateTime> col = new TableColumn<>("เข้าถึงล่าสุด");
        col.setCellValueFactory(new PropertyValueFactory<>("requestTime"));
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime time, boolean empty) {
                super.updateItem(time, empty);
                if (empty || time == null) {
                    setText(null);
                } else {
                    setText(timeFormatUtil.localDateTimeToString(time));
                }
            }
        });
        col.setStyle("-fx-alignment: CENTER;");
        return col;
    }

    private TableColumn<Request, Void> createActionColumn() {
        return tableColumnFactory.createActionColumn("จัดการ", request -> {
            final FilledButtonWithIcon approveBtn = FilledButtonWithIcon.small("อนุมัติ", Icons.APPROVE);
            final FilledButtonWithIcon RejectBtn = FilledButtonWithIcon.small("ปฎิเสธ", Icons.REJECT);

            if (request.getRequestType() != RequestType.PENDING) {
                approveBtn.setDisable(true);
                RejectBtn.setDisable(true);
            } else {
                approveBtn.setDisable(false);
                RejectBtn.setDisable(false);
            }

            approveBtn.setOnAction(e -> onApproveButtonClick(request));
            RejectBtn.setOnAction(e -> onRejectButtonClick(request));

            return new Button[]{approveBtn, RejectBtn};
        });
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
            FXRouter.goTo("officer-manage-reject", request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onBackButton(){
        try {
            FXRouter.goTo("officer-home", currentZone);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
