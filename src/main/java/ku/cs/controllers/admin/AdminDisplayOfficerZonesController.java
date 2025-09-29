package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.LabelStyle;
import ku.cs.components.Toast;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.components.button.IconButton;
import ku.cs.controllers.components.AdminNavbarController;
import ku.cs.controllers.officer.OfficerTableZoneController;
import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.models.account.User;
import ku.cs.models.comparator.OfficerZoneComparator;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.models.zone.ZoneStatus;
import ku.cs.services.AppContext;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.OfficerListFileDatasource;
import ku.cs.services.datasources.ZoneListFileDatasource;
import ku.cs.services.utils.AlertUtil;

import java.io.IOException;
import java.util.Collections;

public class AdminDisplayOfficerZonesController {
    private final SessionManager sessionManager = AppContext.getSessionManager();
    private final AlertUtil alertUtil = new AlertUtil();

    @FXML private VBox parentVBoxFilled;
    @FXML private TableView<Zone> officerZonesTableView;

    @FXML private AdminNavbarController adminNavbarController;
    private Button footerNavBarButton;
    private Stage stage;

    private Datasource<OfficerList> officersDatasource;
    private Datasource<ZoneList> zonesDatasource;
    private OfficerList officers;
    private ZoneList zones;

    private Officer officer;

    @FXML public void initialize() {
        sessionManager.requireAdminLogin();
        officer = (Officer) FXRouter.getData();

        initDatasource();
        officer = officers.findOfficerByUsername(officer.getUsername());
        System.out.println(officer.getUsername() + ": " + officer.getZoneUids());

        getCurrentOfficerZonesList(zones);

        initUserInterfaces();
        initEvents();
    }

    private void initDatasource() {
        officersDatasource = new OfficerListFileDatasource("data", "test-officer-data.json");
        officers = officersDatasource.readData();

        zonesDatasource = new ZoneListFileDatasource("data", "test-zone-data.json");
        zones = zonesDatasource.readData();
        Collections.sort(zones.getZones(), new OfficerZoneComparator(officer));
    }

    private void initUserInterfaces() {
        footerNavBarButton = adminNavbarController.getFooterNavButton();

        footerNavBarButton.setText("ย้อนกลับ");

        Label titleLabel = new Label("รายการจุดให้บริการ ของเจ้าหน้าที่: " + officer.getUsername());
        Label descriptionLabel = new Label(officer.getFullName() + " มีจุดให้บริการทั้งหมด " + officer.getZoneUids().size() + " จุด");

        LabelStyle.TITLE_LARGE.applyTo(titleLabel);
        LabelStyle.TITLE_SMALL.applyTo(descriptionLabel);

        parentVBoxFilled.getChildren().addAll(titleLabel, descriptionLabel);
    }

    private void initEvents() {
        footerNavBarButton.setOnAction(e -> onBackButtonClick());
    }

    private void showTable() {
        officerZonesTableView.getColumns().clear();
        officerZonesTableView.getColumns().setAll(
                createNumberColumn(),
                createTextColumn("ชื่อโซน", "zoneName"),
                    createTextColumn("ล็อกเกอร์ทั้งหมด", "totalLocker", 130,true),
                createTextColumn("ว่างอยู่", "totalAvailableNow", 75,true),
                createTextColumn("ใช้งานได้", "totalAvailable", 75,true),
                createTextColumn("ไม่ว่าง", "totalUnavailable", 75, true),
                createStatusColumn(),
                createActionColumn()
        );

        officerZonesTableView.getItems().sort(new OfficerZoneComparator(officer));
        officerZonesTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    }

    private <T> TableColumn<Zone, T> createTextColumn(String title, String property) {
        TableColumn<Zone, T> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        return col;
    }

    private <T> TableColumn<Zone, T> createTextColumn(String title, String property, double prefWidth, boolean center) {
        TableColumn<Zone, T> col = createTextColumn(title, property);
        if (prefWidth > 0) col.setPrefWidth(prefWidth);
        if (center) col.setStyle("-fx-alignment: TOP_CENTER;");
        return col;
    }

    private TableColumn<Zone, Void> createNumberColumn() {
        TableColumn<Zone, Void> col = new TableColumn<>("ที่");
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(getIndex() + 1));
                }
            }
        });

        col.setStyle("-fx-alignment: CENTER;");
        col.setMaxWidth(30);

        col.setSortable(false);
        return col;
    }

    private TableColumn<Zone, ZoneStatus> createStatusColumn() {
        TableColumn<Zone, ZoneStatus> col = new TableColumn<>("สถานะ");
        col.setCellValueFactory(new PropertyValueFactory<>("status"));
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(ZoneStatus status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                } else {
                    setText(status.getDescription());
                }
            }
        });

        col.setPrefWidth(80);
        return col;
    }

    private TableColumn<Zone, Void> createActionColumn() {
        TableColumn<Zone, Void> actionColumn = new TableColumn<>("จัดการ");
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final FilledButtonWithIcon statusBtn = FilledButtonWithIcon.small("เปลี่ยนสถานะ", Icons.SUSPEND);
            private final IconButton deleteBtn = IconButton.error(new Icon(Icons.USER_MINUS));
            private final IconButton addBtn = IconButton.success(new Icon(Icons.USER_PLUS));

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                Zone zone = getTableRow().getItem();
                if (empty || zone == null) {
                    setGraphic(null);
                    return;
                }

                HBox box = new HBox(6);
                Region region = new Region();

                region.setPrefWidth(5);
                box.getChildren().add(region);

                statusBtn.setOnAction(e -> toggleZoneStatus(zone));
                box.getChildren().add(statusBtn);

                if (officer.getZoneUids().contains(zone.getZoneUid())) {
                    // officer zone
                    deleteBtn.setOnAction(e -> deleteOfficerZone(zone));
                    box.getChildren().add(deleteBtn);
                } else {
                    // not officer zone
                    addBtn.setOnAction(e -> addOfficerZone(zone));
                    box.getChildren().add(addBtn);
                }

                setGraphic(box);
            }
        });

        actionColumn.setPrefWidth(190);
        actionColumn.setStyle("-fx-alignment: CENTER;");
        return actionColumn;
    }

    private void toggleZoneStatus(Zone zone) {
        zone.toggleStatus();
        zonesDatasource.writeData(zones);

        stage = (Stage) parentVBoxFilled.getScene().getWindow();
        Toast.show(stage, "เปลี่ยนสถานะให้ " + zone.getZoneName(), 500);
        showTable();
    }

    private void deleteOfficerZone(Zone zone) {
        officer.removeZoneUid(zone.getZoneUid());
        officersDatasource.writeData(officers);

        stage = (Stage) parentVBoxFilled.getScene().getWindow();
        Toast.show(stage, "นำ " + zone.getZoneName() + " ออกจาก " + officer.getFirstname(), 500);
        showTable();
    }

    private void addOfficerZone(Zone zone) {
        officer.addZoneUid(zone.getZoneUid());
        officersDatasource.writeData(officers);

        stage = (Stage) parentVBoxFilled.getScene().getWindow();
        Toast.show(stage, "เพิ่ม " + zone.getZoneName() + " ให้ " + officer.getFirstname(), 500);
        showTable();
    }

    private void getCurrentOfficerZonesList(ZoneList zones) {
        officerZonesTableView.getItems().clear();
        showTable();

        for (Zone zone : zones.getZones()) {
            officerZonesTableView.getItems().add(zone);
        }
    }

    protected void onBackButtonClick() {
        try {
            FXRouter.goTo("admin-manage-officers");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
