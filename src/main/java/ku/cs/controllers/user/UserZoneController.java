package ku.cs.controllers.user;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.components.LabelStyle;
import ku.cs.models.account.Account;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.models.zone.ZoneStatus;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.ZoneService;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.ZoneListFileDatasource;
import ku.cs.services.utils.AlertUtil;

import java.io.IOException;

public class UserZoneController {

    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;

    @FXML private TableView<Zone> zoneListTable;

    private ZoneList zoneList;
    private Datasource<ZoneList> datasource;
    private Account current;

//    Account current = SessionManager.getCurrentAccount();

    @FXML
    public void initialize() {
        SessionManager.requireUserLogin();
        current = SessionManager.getCurrentAccount();

        initialDatasourceZone();
        initUserInterface();
        initEvents();
        showTable(zoneList);
        zoneListTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Zone>() {
            @Override
            public void changed(ObservableValue<? extends Zone> observableValue, Zone oldzone, Zone newzone) {
                if (newzone != null) {
                    switch (newzone.getStatus()) {
                        case ZoneStatus.ACTIVE:
                            try {
                                FXRouter.goTo("locker-list", newzone.getIdZone());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case ZoneStatus.INACTIVE:
                            showAlert(Alert.AlertType.ERROR, "Zone Not Active", "Please try again later.");
                            break;
                        case ZoneStatus.FULL:
                            AlertUtil.error("Zone Full", "Please try again later.");
                            break;
                    }
                }
            }
        });
    }

    private void initialDatasourceZone() {
        datasource = new ZoneListFileDatasource("data", "test-zone-data.json");
        zoneList = datasource.readData();

        ZoneService.setLockerToZone(zoneList);
    }

    private void initUserInterface() {
        LabelStyle.BODY_LARGE.applyTo(titleLabel);
        LabelStyle.BODY_MEDIUM.applyTo(descriptionLabel);
    }

    private void initEvents() {
    }


    private void showTable(ZoneList zoneList) {
        zoneListTable.getColumns().clear();

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

        zoneListTable.getColumns().clear();
        zoneListTable.getColumns().add(idColumn);
        zoneListTable.getColumns().add(zoneColumn);
        zoneListTable.getColumns().add(totalLockerColumn);
        zoneListTable.getColumns().add(totalAvailableNowColumn);
        zoneListTable.getColumns().add(totalAvailableColumn);
        zoneListTable.getColumns().add(statusColumn);

        zoneListTable.getItems().clear();
        zoneListTable.getItems().addAll(zoneList.getZones());
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
