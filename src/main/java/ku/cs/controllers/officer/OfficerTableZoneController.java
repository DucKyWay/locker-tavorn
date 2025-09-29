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
import ku.cs.services.AppContext;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.ZoneListFileDatasource;

import java.io.IOException;

public class OfficerTableZoneController {
    private final SessionManager sessionManager = AppContext.getSessionManager();;

    private Officer current;
    @FXML private TableView<Zone> zoneListTableView;

    private Datasource<ZoneList> zoneListDatasource;
    private ZoneList zoneList;

    @FXML
    public void initialize() {
        current = sessionManager.getOfficer();

        initialDatasource();

        System.out.println("officer: " + current.getUsername());
        System.out.println("zoneUids: " + current.getZoneUids());

        getCurrentZoneList(zoneList);

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

    private void initialDatasource() {
        zoneListDatasource = new ZoneListFileDatasource("data", "test-zone-data.json");
        zoneList = zoneListDatasource.readData();
    }

    private void getCurrentZoneList(ZoneList zoneList) {
        zoneListTableView.getItems().clear();
        showTable();

        for (String uid : current.getZoneUids()) {
            Zone zone = zoneList.findZoneByUid(uid);
            if (zone != null) {
                System.out.println("Found zone: " + zone.getZoneUid());
                zoneListTableView.getItems().add(zone);
            } else {
                System.out.println("Zone not found for uid: " + uid);
            }
        }
    }

    private void showTable() {
        zoneListTableView.getColumns().clear();

        zoneListTableView.getColumns().setAll(
            createTextColumn("ID", "idZone"),
            createTextColumn("ชื่อโซน", "zone"),
            createTextColumn("จำนวนล็อกเกอร์ทั้งหมด",  "totalLocker"),
            createTextColumn("จำนวนล็อกเกอร์ว่างในตอนนี้", "totalAvailableNow"),
            createTextColumn("จำนวนล็อกเกอร์ที่สามารถใช้งานได้", "totalAvailable"),
            createTextColumn("สถานะ", "status")
        );
        zoneListTableView.getItems().setAll(zoneList.getZones());
        zoneListTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private <T> TableColumn<Zone, T> createTextColumn(String title, String property) {
        TableColumn<Zone, T> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        col.setStyle("-fx-alignment: TOP_CENTER;");
        return col;
    }
}
