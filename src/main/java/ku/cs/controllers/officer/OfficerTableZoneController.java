package ku.cs.controllers.officer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.account.Officer;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.session.SessionManager;
import ku.cs.services.utils.TableColumnFactory;
import ku.cs.services.zone.ZoneService;

import java.io.IOException;

public class OfficerTableZoneController {
    protected final SessionManager sessionManager = (SessionManager) FXRouter.getService("session");
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final ZoneService zoneService = new ZoneService();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();

    private Officer current;
    @FXML private TableView<Zone> zoneListTableView;

    private ZoneList zoneList;

    @FXML
    public void initialize() {
        current = sessionManager.getOfficer();
        initialDatasource();
        initUserInterfaces();
        initEvents();

        System.out.println("officer: " + current.getUsername());
        System.out.println("zoneUids: " + current.getZoneUids());
    }

    private void initialDatasource() {
        zoneList = zonesProvider.loadCollection();
    }

    private void initUserInterfaces() {
        showTable();
        getCurrentZoneList(zoneList);
        zoneService.updateLockersToZone(zoneList);
    }

    private void initEvents() {
        zoneListTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Zone>() {
            @Override
            public void changed(ObservableValue<? extends Zone> observableValue, Zone oldzone, Zone newzone) {
                if (newzone != null) {
                    try {
                        FXRouter.goTo("officer-home", newzone);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void getCurrentZoneList(ZoneList zoneList) {
        zoneListTableView.getItems().clear();

        for (String uid : current.getZoneUids()) {
            Zone zone = zoneList.findZoneByUid(uid);
            if (zone != null) {
                zoneListTableView.getItems().add(zone);
            }
        }
    }

    private void showTable() {
        zoneListTableView.getColumns().clear();

        zoneService.reloadZoneStatus();

        zoneListTableView.getColumns().setAll(
                tableColumnFactory.createTextColumn("ไอดีจุดให้บริการ", "zoneUid" ),
                tableColumnFactory.createTextColumn("จุดให้บริการ", "zoneName"),
                tableColumnFactory.createTextColumn("ล็อกเกอร์", "totalLocker", 78,"-fx-alignment: CENTER; -fx-padding: 0 16" ),
                tableColumnFactory.createTextColumn("ว่างอยู่", "totalAvailableNow", 78, "-fx-alignment: CENTER; -fx-padding: 0 16"),
                tableColumnFactory.createTextColumn("ใช้งานได้", "totalAvailable", 90, "-fx-alignment: CENTER; -fx-padding: 0 23"),
                tableColumnFactory.createEnumStatusColumn("สถานะ", "status", 158)
        );
        zoneListTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

}
