package ku.cs.controllers.user;

import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.IconButton;
import ku.cs.models.key.KeyList;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.provider.KeyDatasourceProvider;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;
import ku.cs.services.datasources.provider.RequestDatasourceProvider;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.request.RequestService;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.SearchService;
import ku.cs.services.utils.TableColumnFactory;
import ku.cs.services.utils.TimeFormatUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.locks.Lock;

public class UserHomeController extends BaseUserController {
    private final RequestList requests = new RequestDatasourceProvider().loadAllCollections();
    private final ZoneList zones = new ZoneDatasourceProvider().loadCollection();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();
    private final SearchService<Locker> searchService = new SearchService<>();
    private final RequestService requestService = new RequestService();
    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;
    @FXML private TextField searchTextField;
    @FXML private Button searchButton;

    @FXML private TableView<Locker> lockersTableView;
    private LockerList lockers;

    @Override
    protected void initDatasource() {
        lockers = new LockerDatasourceProvider().loadAllCollections();
    }

    @Override
    protected void initUserInterfaces() {
        LabelStyle.TITLE_LARGE.applyTo(titleLabel);
        LabelStyle.TITLE_SMALL.applyTo(descriptionLabel);
        IconButton.mask(searchButton, new Icon(Icons.MAGNIFYING_GLASS, 20));
        showTable(lockers);
    }

    @Override
    protected void initEvents() {
        requestService.updateData();
        searchTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            onSearch();
        });
        searchButton.setOnAction(e -> onSearch());

        lockersTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldLocker, newLocker) -> {
            if (newLocker == null) return;                       // empty space click
            if (lockersTableView.getItems().isEmpty()) return;   // empty list click

            try {
                boolean available = newLocker.isAvailable(); // ว่างให้จอง?
                boolean serviceable = newLocker.isStatus();  // สภาพใช้งานได้?

                if (!serviceable) {
                    new AlertUtil().error("ล็อคเกอร์ไม่พร้อมใช้งาน", "ล็อคเกอร์ชำรุด");
                    return;
                }

                if (available) {
                    FXRouter.loadDialogStage("locker-reserve", newLocker);
                    return;
                }

                Request request = requests.findLatestRequestByLockerUid(newLocker.getLockerUid());
                if (request == null) {
                    new AlertUtil().error("ล็อคเกอร์ไม่พร้อมใช้งาน", "ไม่พบข้อมูลคำขอของล็อคเกอร์นี้");
                    return;
                }

                if (request.getUserUsername().equals(current.getUsername())) {
                    LocalDate now   = LocalDate.now();
                    LocalDate start = request.getStartDate();
                    LocalDate end   = request.getEndDate();
                    boolean inRange = (!now.isBefore(start) && !now.isAfter(end)); // start <= now <= end

                    if (inRange) {
                        FXRouter.loadDialogStage("locker-dialog", request);
                    } else {
                        System.out.println(request.getEndDate() + " and now " + now);
                        new AlertUtil().error(
                                "ล็อคเกอร์ไม่พร้อมใช้งาน",
                                "หมดระยะเวลาการใช้บริการเมื่อ " + new TimeFormatUtil().formatFull(end)
                        );
                    }
                } else {
                    new AlertUtil().error(
                            "ล็อคเกอร์ไม่พร้อมใช้งาน",
                            "ล็อคเกอร์นี้จะใช้งานได้หลังจากวันที่ " + new TimeFormatUtil().formatFull(request.getEndDate())
                    );
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                new AlertUtil().error("เกิดข้อผิดพลาด", String.valueOf(ex.getMessage()));
            } finally {
                // Async clear
                Platform.runLater(() -> lockersTableView.getSelectionModel().clearSelection());
            }
        });

    }

    private void showTable(LockerList lockerList) {
        lockersTableView.getColumns().clear();
        lockersTableView.getColumns().setAll(
            tableColumnFactory.createTextColumn("ที่", "lockerId", 45, "-fx-alignment: CENTER; -fx-padding: 0 16"),
            tableColumnFactory.createZoneNameColumn("จุดให้บริการ", "zoneUid", zones),
            tableColumnFactory.createTextColumn("เลขล็อคเกอร์", "lockerUid", 100, "-fx-alignment: CENTER; -fx-padding: 0 16"),
            tableColumnFactory.createEnumStatusColumn("ขนาดล็อคเกอร์", "lockerSizeType", 100),
            tableColumnFactory.createEnumStatusColumn("ประเภทล็อคเกอร์", "lockerType", 100),
            tableColumnFactory.createLockerStatusColumn("สถานะล็อคเกอร์", "lockerUid", lockers)

        );
        lockersTableView.getItems().clear();
        lockersTableView.getItems().setAll(lockerList.getLockers());
    }

    private void onSearch() {
        String keyword = searchTextField.getText();

        KeyList keys = new KeyDatasourceProvider().loadAllCollection();

        if (keyword.isEmpty()) {
            showTable(lockers);
            return;
        }

        List<Locker> filtered = searchService.search(
            lockers.getLockers(),
            keyword,
            l -> String.valueOf(l.getLockerId()),
            l -> {
                try {
                    return zones.findZoneByUid(l.getZoneUid()).getZoneName();
                } catch (Exception e) {
                    return "-";
                }
            },
            Locker::getLockerUid,
            Locker::getLockerSizeTypeString,
            l -> l.getLockerType().getDescription(),
            l -> String.valueOf(l.isStatus()),
            l -> "LOCKER:" + l.getLockerUid() // Locker Qrcode Template
        );
        LockerList filteredlist = new LockerList();
        filtered.forEach(filteredlist::addLocker);

        showTable(filteredlist);
    }
}
