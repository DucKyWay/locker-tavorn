package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.CustomButtonWithIcon;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.components.button.IconButton;
import ku.cs.controllers.components.AdminNavbarController;
import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.AppContext;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.OfficerListFileDatasource;
import ku.cs.services.datasources.ZoneListFileDatasource;
import ku.cs.services.utils.AlertUtil;

import java.io.IOException;

public class AdminDisplayOfficerZonesController {
    private final SessionManager sessionManager = AppContext.getSessionManager();
    private final AlertUtil alertUtil = new AlertUtil();

    @FXML private VBox parentVBoxFilled;
    @FXML private TableView<Zone> officerZonesTableView;

    @FXML private AdminNavbarController adminNavbarController;
    private Button footerNavBarButton;

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
        officerZonesTableView.getColumns().setAll(
                createNumberColumn(),
                createTextColumn("ชื่อโซน", "zone"),
                createTextColumn("ล็อกเกอร์", "totalLocker", "-fx-alignment: TOP_CENTER;"),
                createTextColumn("ล็อกเกอร์ที่ว่าง", "totalAvailableNow", "-fx-alignment: TOP_CENTER;"),
                createTextColumn("ล็อกเกอร์ที่ใช้งานได้", "totalAvailable", "-fx-alignment: TOP_CENTER;"),
                createTextColumn("ล็อกเกอร์ที่ไม่ว่าง", "totalUnavailable", "-fx-alignment: TOP_CENTER;"),
                createTextColumn("สถานะ", "status", 60, "-fx-alignment: TOP_CENTER;"),
                createActionColumn()
        );

        officerZonesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
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

    private TableColumn<Zone, Void> createActionColumn() {
        TableColumn<Zone, Void> actionColumn = new TableColumn<>("จัดการ");
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final FilledButtonWithIcon statusBtn = FilledButtonWithIcon.small("เปลี่ยนสถานะ", Icons.SUSPEND);
            private final IconButton deleteBtn = IconButton.error(new Icon(Icons.USER_MINUS));


            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                Zone zone = getTableRow().getItem();

                statusBtn.setOnAction(e -> toggleZoneStatus(zone));
                deleteBtn.setOnAction(e -> deleteOfficerZone(zone));

                setGraphic(empty ? null : new HBox(5, statusBtn, deleteBtn));
            }
        });

        actionColumn.setPrefWidth(120);
        actionColumn.setStyle("-fx-alignment: CENTER;");
        return actionColumn;
    }

    private void toggleZoneStatus(Zone zone) {
        zones.zoneToggleStatus(zone);
        zonesDatasource.writeData(zones);
        alertUtil.info("สำเร็จ", "คุณเปลี่ยนสถานะจุดให้บริการ " + zone.getZone() + " เรียบร้อยแล้ว");
    }

    private void deleteOfficerZone(Zone zone) {
        alertUtil.confirm("Confirmation",
                "คุณแน่ใจหรือไม่ที่จะนำ " + zone.getZone() + " ออกจากจุดให้บริการภายใต้พนักงาน " + officer.getFirstname() + "?"
        ).ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                officer.removeZoneUid(zone.getZoneUid());
                officersDatasource.writeData(officers);
                try {
                    FXRouter.goTo("admin-display-officer-zones", officer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
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
