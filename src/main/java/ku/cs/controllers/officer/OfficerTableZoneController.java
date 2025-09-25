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
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.ZoneListFileDatasource;

import java.io.IOException;

public class OfficerTableZoneController {
    private Officer current;
    @FXML private TableView<Zone> zonelistTableView;

    private Datasource<ZoneList> zoneListDatasource;
    private ZoneList zoneList;

    @FXML
    public void initialize() {
        current = SessionManager.getOfficer();

        initialDatasource();

        System.out.println("officer: " + current.getUsername());
        System.out.println("zoneUids: " + current.getZoneUids());

        getCurrentZoneList(zoneList);

        zonelistTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Zone>() {
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
        zonelistTableView.getItems().clear();
        showTable();

        for (String uid : current.getZoneUids()) {
            Zone zone = zoneList.findZoneByUid(uid);
            if (zone != null) {
                System.out.println("Found zone: " + zone.getZoneUid());
                zonelistTableView.getItems().add(zone);
            } else {
                System.out.println("Zone not found for uid: " + uid);
            }
        }
    }

    private void showTable() {
        zonelistTableView.getColumns().clear();

        TableColumn<Zone, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idZone"));

        TableColumn<Zone, String> zoneColumn = new TableColumn<>("ชื่อโซน");
        zoneColumn.setCellValueFactory(new PropertyValueFactory<>("zone"));

        TableColumn<Zone, Integer> totalLockerColumn = new TableColumn<>("จำนวนล็อกเกอร์ทั้งหมด");
        totalLockerColumn.setCellValueFactory(new PropertyValueFactory<>("totalLocker"));

        TableColumn<Zone, Integer> totalAvailableNowColumn = new TableColumn<>("จำนวนล็อกเกอร์ว่างในตอนนี้");
        totalAvailableNowColumn.setCellValueFactory(new PropertyValueFactory<>("totalAvailableNow"));

        TableColumn<Zone, Integer> totalAvailableColumn = new TableColumn<>("จำนวนล็อกเกอร์ที่สามารถใช้งานได้");
        totalAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("totalAvailable"));

        TableColumn<Zone, String> statusColumn = new TableColumn<>("สถานะ");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        zonelistTableView.getColumns().addAll(
                idColumn, zoneColumn, totalLockerColumn,
                totalAvailableNowColumn, totalAvailableColumn, statusColumn
        );
    }
}
