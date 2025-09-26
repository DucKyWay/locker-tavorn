package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import ku.cs.controllers.components.AdminNavbarController;
import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.OfficerListFileDatasource;
import ku.cs.services.datasources.ZoneListFileDatasource;

import java.io.IOException;

public class AdminDisplayOfficerZonesController {
    @FXML private VBox parentVBoxFilled;
    @FXML private TableView<Zone> officerZonesTableView;

    private Label titleLabel;
    private Label descriptionLabel;

    @FXML private AdminNavbarController adminNavbarController;
    private Button footerNavBarButton;

    private Datasource<OfficerList> officersDatasource;
    private Datasource<ZoneList> zonesDatasource;
    private OfficerList officers;
    private ZoneList zones;

    private Officer officer;

    @FXML public void initialize() {
        SessionManager.requireAdminLogin();
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
    }

    private void initUserInterfaces() {
        footerNavBarButton = adminNavbarController.getFooterNavButton();

        footerNavBarButton.setText("ย้อนกลับ");

        titleLabel = new Label("รายการจุดให้บริการ ของเจ้าหน้าที่: " + officer.getFullName() + " [" + officer.getUsername() + "]");
        descriptionLabel = new Label("จุดให้บริการทั้งหมด " + officer.getZoneUids().size() + " จุด");

        parentVBoxFilled.getChildren().addAll(titleLabel, descriptionLabel);
    }

    private void initEvents() {
        footerNavBarButton.setOnAction(e -> onBackButtonClick());
    }

    private void showTable() {
        officerZonesTableView.getColumns().setAll(
                createNumberColumn(),
                createTextColumn("ชื่อโซน", "zone"),
                createTextColumn("ล็อกเกอร์", "totalLocker", "-fx-alignment: TOP_CENTER;"),
                createTextColumn("ล็อกเกอร์ที่ว่าง", "totalAvailableNow", "-fx-alignment: TOP_CENTER;"),
                createTextColumn("ล็อกเกอร์ที่ใช้งานได้", "totalAvailable", "-fx-alignment: TOP_CENTER;"),
                createTextColumn("ล็อกเกอร์ที่ไม่ว่าง", "totalUnavailable", "-fx-alignment: TOP_CENTER;"),
                createTextColumn("สถานะ", "status", 60, "-fx-alignment: TOP_CENTER;")
        );

        officerZonesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private <T> TableColumn<Zone, T> createTextColumn(String title, String property) {
        TableColumn<Zone, T> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        return col;
    }

    private <T> TableColumn<Zone, T> createTextColumn(String title, String property, String style) {
        TableColumn<Zone, T> col = createTextColumn(title, property);
        if (style != null) col.setStyle(style);
        return col;
    }

    private <T> TableColumn<Zone, T> createTextColumn(String title, String property, double prefWidth, String style) {
        TableColumn<Zone, T> col = createTextColumn(title, property);
        if (prefWidth > 0) col.setPrefWidth(prefWidth);
        if (style != null) col.setStyle(style);
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
        col.setPrefWidth(20);

        col.setSortable(false);
        return col;
    }

    private void getCurrentOfficerZonesList(ZoneList zones) {
        officerZonesTableView.getItems().clear();
        showTable();

        for (String uid : officer.getZoneUids()) {
            Zone zone = zones.findZoneByUid(uid);
            if (zone != null) {
                System.out.println("Found zone: " + zone.getZoneUid());
                officerZonesTableView.getItems().add(zone);
            } else {
                System.out.println("Zone not found for uid: " + uid);
            }
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
