package ku.cs.controllers.officer;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.components.button.IconButton;
import ku.cs.models.comparator.TimestampComparator;
import ku.cs.models.dialog.DialogData;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.locker.LockerType;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.request.RequestType;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;
import ku.cs.services.datasources.provider.RequestDatasourceProvider;
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

public class OfficerTableRequestHistoryController extends BaseOfficerController{
    private final RequestDatasourceProvider requestsProvider = new RequestDatasourceProvider();
    private final LockerDatasourceProvider lockersProvider = new LockerDatasourceProvider();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();
    private final TimeFormatUtil timeFormatUtil = new TimeFormatUtil();
    private final SearchService<Request> searchService = new SearchService<>();

    private final SelectedDayService selectedDayService = new SelectedDayService();
    RequestService requestService = new RequestService();
    RequestList requestList;
    LockerList lockerList;

    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;

    @FXML private TableView<Request> requestTableView;

    @FXML private TextField searchTextField;
    @FXML private Button searchButton;

    @FXML private Button officerZoneRouteLabelButton;
    @FXML private Button officerRequestHistoryRouteLabelButton;

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
        Collections.sort(requestList.getRequestList(), new TimestampComparator<>());
    }

    @Override
    protected void initUserInterfaces() {
        ElevatedButtonWithIcon.LABEL.mask(officerZoneRouteLabelButton, Icons.LOCATION);
        ElevatedButtonWithIcon.LABEL.mask(officerRequestHistoryRouteLabelButton, Icons.TAG);
        IconButton.mask(searchButton, new Icon(Icons.MAGNIFYING_GLASS));

        officerZoneRouteLabelButton.setText(currentZone.getZoneName());

        showTable(requestList);
    }

    @Override
    protected void initEvents() {
        officerZoneRouteLabelButton.setOnAction(e -> onBackButtonClick());
        searchTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            onSearch();
        });
    }

    private void showTable(RequestList requestList) {
        requestTableView.getColumns().clear();
        requestTableView.getColumns().setAll(
                tableColumnFactory.createTextColumn("เลขที่คำร้อง", "requestUid"),
                tableColumnFactory.createEnumStatusColumn("สถานะการจอง", "requestType", -1),
                createLockerUidColumn(),
                createLockerTypeColumn(),
                tableColumnFactory.createTextColumn("เริ่มการจอง", "startDate"),
                tableColumnFactory.createTextColumn("สิ้นสุดการจอง", "endDate"),
                tableColumnFactory.createTextColumn("ชื่อผู้จอง", "userUsername"),

                tableColumnFactory.createTextColumn("จุดให้บริการ", "zoneName"),
                createLastTimeColumn(),
                createActionColumn()
        );

        for (Request req : requestList.getRequestList()) {
            if (!selectedDayService.isBooked(req.getStartDate(), req.getEndDate())) {
                requestTableView.getItems().add(req);
            }
        }

        requestTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    }

    private TableColumn<Request,String> createLockerTypeColumn() {
        TableColumn<Request, String> TypeLockerColumn = new TableColumn<>("เลขประจำล็อคเกอร์");
        TypeLockerColumn.setCellValueFactory(new PropertyValueFactory<>("lockerUid"));
        TypeLockerColumn.setCellValueFactory((TableColumn.CellDataFeatures<Request, String> col) -> {
            Request request = col.getValue();
            for (Locker l : lockerList.getLockers()) {
                if (l.getLockerUid().equals(request.getLockerUid())) {
                    return new SimpleStringProperty(l.getLockerType().toString());
                }
            }
            return new SimpleStringProperty("ไม่พบล็อคเกอร์");
        });
        return TypeLockerColumn;
    }

    private TableColumn<Request, String> createLockerUidColumn() {
        TableColumn<Request, String> lockerUid = new TableColumn<>("เลขประจำล็อคเกอร์");
        lockerUid.setCellValueFactory(new PropertyValueFactory<>("lockerUid"));
        lockerUid.setCellValueFactory((TableColumn.CellDataFeatures<Request, String> col) -> {
            Request request = col.getValue();

            for (Locker l : lockerList.getLockers()) {
                if (l.getLockerUid().equals(request.getLockerUid())) {
                    return new SimpleStringProperty(l.getLockerUid());
                }
            }
            return new SimpleStringProperty("ไม่พบล็อคเกอร์");
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
            FilledButtonWithIcon approveBtn;
            FilledButtonWithIcon rejectBtn = FilledButtonWithIcon.small("ปฏิเสธ", Icons.REJECT);
            RequestType type = request.getRequestType();

            switch (type) {
                case SUCCESS:
                case LATE:
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
            FXRouter.loadDialogStage("locker-dialog", new DialogData(request,current));
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
        }
    }

    private void onRejectButtonClick(Request request) {
        try {
            FXRouter.goTo("officer-manage-reject", request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void onSearch() {
        String keyword = searchTextField.getText();
        List<Request> filtered = searchService.search(
                requestList.getRequestList(),
                keyword,
                Request::getRequestUid,
                Request::getZoneUid,
                Request::getLockerUid
        );
        RequestList filteredList = new RequestList();
        filtered.forEach(filteredList::addRequest);

        showTable(filteredList);
    }

    @FXML
    protected void onBackButtonClick(){
        try {
            FXRouter.goTo("officer-select-zone");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
