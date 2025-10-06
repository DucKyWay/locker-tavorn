package ku.cs.controllers.officer.DialogPane;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.components.LabelStyle;
import ku.cs.models.account.User;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;
import ku.cs.services.datasources.provider.RequestDatasourceProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.TableColumnFactory;
import ku.cs.services.utils.TimeFormatUtil;

import java.time.LocalDateTime;

public class OfficerDisplayLockerHistoryDialogController {
    private final RequestList requests = new RequestDatasourceProvider().loadAllCollections();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();

    private Locker locker;
    private RequestList filteredRequests;

    @FXML private Label titleLabel;
    @FXML private TableView<Request> historyTableView;

    @FXML public void initialize() {
        locker = (Locker) FXRouter.getData();

        if (locker != null) {
            filterRequestsByLockerUid(locker.getLockerUid());
            titleLabel.setText("ประวัติการใช้งานของล็อกเกอร์ " + locker.getLockerUid());
        } else {
            titleLabel.setText("ไม่พบข้อมูลล็อกเกอร์ " + locker.getLockerUid());
        }
        LabelStyle.TITLE_MEDIUM.applyTo(titleLabel);
    }

    private void showTable(RequestList requests) {
        historyTableView.getColumns().clear();
        historyTableView.getItems().clear();

        historyTableView.getColumns().setAll(
            tableColumnFactory.createNumberColumn(),
            tableColumnFactory.createTextColumn("เลขที่คำร้อง", "requestUid"),
                tableColumnFactory.createTextColumn("วันที่เริ่ม", "startDate"),
                tableColumnFactory.createTextColumn("วันที่สิ้นสุด", "endDate"),
                tableColumnFactory.createTextColumn("โดย", "officerUsername"),
                tableColumnFactory.createTextColumn("ผู้ใช้บริการ", "userUsername"),
                tableColumnFactory.createTextColumn("รหัสเปิดล็อคเกอร์", "lockerKeyUid"),
                createRequestTimeColumn(),
                tableColumnFactory.createEnumStatusColumn("สถานะ", "requestType", 0)
        );

        historyTableView.getItems().setAll(requests.getRequests());
        historyTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_LAST_COLUMN);
    }

    private TableColumn<Request, LocalDateTime> createRequestTimeColumn() {
        TableColumn<Request, LocalDateTime> col = new TableColumn<>("วันที่ยื่นคำร้อง");
        col.setPrefWidth(150);
        col.setMinWidth(150);
        col.setMaxWidth(150);
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

    private void filterRequestsByLockerUid(String lockerUid) {
        filteredRequests = new RequestList();

        for (Request req : requests.getRequests()) {
            if (req.getLockerUid() != null && req.getLockerUid().equals(lockerUid)) {
                filteredRequests.addRequest(req);
            }
        }

        showTable(filteredRequests);
    }
}
