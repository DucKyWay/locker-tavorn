package ku.cs.controllers.officer.DialogPane;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.FilledButton;
import ku.cs.models.account.OfficerList;
import ku.cs.models.comparator.TimestampComparator;
import ku.cs.models.locker.Locker;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.services.accounts.strategy.OfficerAccountProvider;
import ku.cs.services.datasources.provider.RequestDatasourceProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.TableColumnFactory;
import ku.cs.services.utils.TimeFormatUtil;

import java.nio.file.Paths;
import java.time.LocalDateTime;

public class OfficerDisplayLockerHistoryDialogController {
    private final RequestList requests = new RequestDatasourceProvider().loadAllCollections();
    private final OfficerList officers = new OfficerAccountProvider().loadCollection();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();

    private Locker locker;

    @FXML AnchorPane historyDialogPane;

    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;
    @FXML private TableView<Request> historyTableView;
    @FXML private Button closeButton;

    @FXML private ImageView historyImageView;

    @FXML private Label requestUidLabel;
    @FXML private Label showRequestUidLabel;
    @FXML private Label userUsernameLabel;
    @FXML private Label showUserUsernameLabel;
    @FXML private Label startDateLabel;
    @FXML private Label showStartDateLabel;
    @FXML private Label endDateLabel;
    @FXML private Label showEndDateLabel;
    @FXML private Label statusLabel;
    @FXML private Label showStatusLabel;

    @FXML private Label requestTimeLabel;
    @FXML private Label showRequestTimeLabel;
    @FXML private Label officerUsernameLabel;
    @FXML private Label showOfficerUsernameLabel;

    @FXML private Label requestPricePerDayLabel;
    @FXML private Label showRequestPricePerDayLabel;
    @FXML private Label requestFinePerDayLabel;
    @FXML private Label showRequestFinePerDayLabel;
    @FXML private Label requestTotalPriceLabel;
    @FXML private Label showRequestTotalPriceLabel;

    @FXML private Label bahtPricePerDayLabel;
    @FXML private Label bahtFinePerDayLabel;
    @FXML private Label bahtTotalLabel;

    @FXML public void initialize() {
        locker = (Locker) FXRouter.getData();
        initUserInterfaces();
        initEvents();
    }

    private void initUserInterfaces() {

        if (locker != null) {
            filterRequestsByLockerUid(locker.getLockerUid());
            titleLabel.setText("ประวัติการใช้งานของล็อคเกอร์ " + locker.getLockerUid());
            descriptionLabel.setText("เลือกคำร้องเพื่อแสดงรายละเอียดเพิ่มเติม");
        } else {
            titleLabel.setText("ไม่พบข้อมูลล็อคเกอร์ " + locker.getLockerUid());
        }

        LabelStyle.LABEL_LARGE.applyTo(requestUidLabel);
        LabelStyle.LABEL_LARGE.applyTo(showRequestUidLabel);
        LabelStyle.LABEL_LARGE.applyTo(userUsernameLabel);
        LabelStyle.LABEL_LARGE.applyTo(showUserUsernameLabel);
        LabelStyle.LABEL_LARGE.applyTo(startDateLabel);
        LabelStyle.LABEL_LARGE.applyTo(showStartDateLabel);
        LabelStyle.LABEL_LARGE.applyTo(endDateLabel);
        LabelStyle.LABEL_LARGE.applyTo(showEndDateLabel);
        LabelStyle.LABEL_LARGE.applyTo(statusLabel);
        LabelStyle.LABEL_LARGE.applyTo(showStatusLabel);
        LabelStyle.LABEL_LARGE.applyTo(requestTimeLabel);
        LabelStyle.LABEL_LARGE.applyTo(showRequestTimeLabel);
        LabelStyle.LABEL_LARGE.applyTo(officerUsernameLabel);
        LabelStyle.LABEL_LARGE.applyTo(showOfficerUsernameLabel);
        LabelStyle.LABEL_LARGE.applyTo(requestPricePerDayLabel);
        LabelStyle.LABEL_LARGE.applyTo(showRequestPricePerDayLabel);
        LabelStyle.LABEL_LARGE.applyTo(requestFinePerDayLabel);
        LabelStyle.LABEL_LARGE.applyTo(showRequestFinePerDayLabel);
        LabelStyle.LABEL_LARGE.applyTo(requestTotalPriceLabel);
        LabelStyle.LABEL_LARGE.applyTo(showRequestTotalPriceLabel);

        FilledButton.MEDIUM.mask(closeButton);

        requestUidLabel.setText("");
        userUsernameLabel.setText("");
        startDateLabel.setText("");
        endDateLabel.setText("");
        statusLabel.setText("");
        requestTimeLabel.setText("");
        officerUsernameLabel.setText("");
        requestPricePerDayLabel.setText("");
        requestFinePerDayLabel.setText("");
        requestTotalPriceLabel.setText("");
        bahtPricePerDayLabel.setText("");
        bahtFinePerDayLabel.setText("");
        bahtTotalLabel.setText("");
    }

    private void initEvents() {
        historyTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Request>() {
            @Override
            public void changed(ObservableValue<? extends Request> observableValue, Request oldRequest, Request newRequest) {
                if(newRequest !=null){
                    historyInfo(newRequest);
                }
            }
        });

        closeButton.setOnAction(e -> onCloseButtonClick());
    }

    private void showTable(RequestList requests) {
        historyTableView.getColumns().clear();
        historyTableView.getItems().clear();

        historyTableView.getColumns().setAll(
            tableColumnFactory.createNumberColumn(),
            tableColumnFactory.createTextColumn("เลขที่คำร้อง", "requestUid"),
                tableColumnFactory.createShortDateColumn("วันที่เริ่ม", "startDate"),
                tableColumnFactory.createShortDateColumn("วันที่สิ้นสุด", "endDate"),
                tableColumnFactory.createTextColumn("อนุมัติโดย", "officerUsername"),
                tableColumnFactory.createTextColumn("ผู้ใช้บริการ", "userUsername"),
                tableColumnFactory.createTextColumn("กุญแจเปิดล็อคเกอร์", "lockerKeyUid"),
                createRequestTimeColumn(),
                tableColumnFactory.createEnumStatusColumn("สถานะ", "requestType", 180)
        );

        historyTableView.getItems().setAll(requests.getRequests());
        historyTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private TableColumn<Request, LocalDateTime> createRequestTimeColumn() {
        TableColumn<Request, LocalDateTime> col = new TableColumn<>("วันที่ยื่นคำร้อง");
        col.setPrefWidth(170);
        col.setMinWidth(170);
        col.setMaxWidth(170);
        col.setCellValueFactory(new PropertyValueFactory<>("requestTime"));
        col.setCellFactory(rt -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime time, boolean empty) {
                super.updateItem(time, empty);
                if (empty || time == null) {
                    setText(null);
                } else {
                    setText(new TimeFormatUtil().formatFull(time));
                }
            }
        });
        col.setStyle("-fx-alignment: center-left; -fx-padding: 0 16");
        return col;
    }

    private void historyInfo(Request request) {

        requestUidLabel.setText("เลขที่คำร้อง");
        userUsernameLabel.setText("ผู้ใช้บริการ");
        startDateLabel.setText("วันที่เริ่มต้น");
        endDateLabel.setText("วันที่สิ้นสุด");
        statusLabel.setText("สถานะ");
        requestTimeLabel.setText("วันที่ยื่นคำร้อง");
        officerUsernameLabel.setText("ผู้ดำเนินการ");
        requestPricePerDayLabel.setText("ราคาล็อคเกอร์ต่อวัน");
        requestFinePerDayLabel.setText("ค่าปรับล่าช้าต่อวัน");
        requestTotalPriceLabel.setText("รวมที่ต้องจ่าย");
        bahtPricePerDayLabel.setText("บาท");
        bahtFinePerDayLabel.setText("บาท");
        bahtTotalLabel.setText("บาท");

        if (locker.getImagePath() != null && !locker.getImagePath().isBlank()) {
            historyImageView.setVisible(true);
            historyImageView.setImage(new Image("file:" + Paths.get(locker.getImagePath()).toAbsolutePath()));
        } else {
            historyImageView.setVisible(false);
            historyImageView.setImage(null);
        }

        showRequestUidLabel.setText(request.getRequestUid());
        showUserUsernameLabel.setText(request.getUserUsername());
        showStartDateLabel.setText(new TimeFormatUtil().formatFull(request.getStartDate()));
        showEndDateLabel.setText(new TimeFormatUtil().formatFull(request.getEndDate()));
        showStatusLabel.setText(request.getRequestType().getDescription());
        showRequestTimeLabel.setText(new TimeFormatUtil().formatFull(request.getRequestTime()));
        showOfficerUsernameLabel.setText(request.getOfficerUsername());

        showRequestPricePerDayLabel.setText(String.valueOf(locker.getLockerSizeType().getPrice()));
        showRequestFinePerDayLabel.setText(String.valueOf(locker.getLockerSizeType().getFine()));
        showRequestTotalPriceLabel.setText(String.valueOf(request.getPrice()));
    }

    private void filterRequestsByLockerUid(String lockerUid) {
        RequestList filteredRequests = new RequestList();

        for (Request req : requests.getRequests()) {
            if (req.getLockerUid() != null && req.getLockerUid().equals(lockerUid)) {
                filteredRequests.addRequest(req);
            }
        }
        filteredRequests.getRequestList().sort(new TimestampComparator<>());
        showTable(filteredRequests);
    }

    private void onCloseButtonClick() {
        if (historyDialogPane != null && historyDialogPane.getScene() != null && historyDialogPane.getScene().getWindow() != null) {
            historyDialogPane.getScene().getWindow().hide();
        }
    }
}
