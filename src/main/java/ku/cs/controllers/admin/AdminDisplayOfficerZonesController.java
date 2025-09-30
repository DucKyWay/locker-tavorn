package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
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
import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.models.comparator.OfficerZoneComparator;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.FXRouter;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.OfficerListFileDatasource;
import ku.cs.services.datasources.ZoneListFileDatasource;
import ku.cs.services.utils.TableColumnFactory;

import java.io.IOException;
import java.util.Collections;

public class AdminDisplayOfficerZonesController extends BaseAdminController {

    @FXML private VBox parentVBoxFilled;
    @FXML private TableView<Zone> officerZonesTableView;

    private Datasource<OfficerList> officersDatasource;
    private OfficerList officers;
    private Datasource<ZoneList> zonesDatasource;
    private ZoneList zones;

    private Officer officer;
    private Stage stage;

    @Override
    protected void initDatasource() {
        officer = (Officer) FXRouter.getData();

        officersDatasource = new OfficerListFileDatasource("data", "test-officer-data.json");
        officers = officersDatasource.readData();
        officer = officers.findOfficerByUsername(officer.getUsername());

        zonesDatasource = new ZoneListFileDatasource("data", "test-zone-data.json");
        zones = zonesDatasource.readData();

        Collections.sort(zones.getZones(), new OfficerZoneComparator(officer));
    }

    @Override
    protected void initUserInterfaces() {
        if (footerNavBarButton != null) {
            footerNavBarButton.setText("ย้อนกลับ");
        }

        Label titleLabel = new Label("รายการจุดให้บริการ ของเจ้าหน้าที่: " + officer.getUsername());
        Label descriptionLabel = new Label(officer.getFullName() + " มีจุดให้บริการทั้งหมด " + officer.getZoneUids().size() + " จุด");

        LabelStyle.TITLE_LARGE.applyTo(titleLabel);
        LabelStyle.TITLE_SMALL.applyTo(descriptionLabel);

        parentVBoxFilled.getChildren().addAll(titleLabel, descriptionLabel);

        showTable();
        officerZonesTableView.getItems().setAll(zones.getZones());
    }

    @Override
    protected void initEvents() {
        if (footerNavBarButton != null) {
            footerNavBarButton.setOnAction(e -> onBackButtonClick());
        }
    }

    private void showTable() {
        officerZonesTableView.getColumns().clear();
        officerZonesTableView.getColumns().setAll(
                TableColumnFactory.createNumberColumn(),
                TableColumnFactory.createTextColumn("ชื่อโซน", "zoneName"),
                TableColumnFactory.createTextColumn("ล็อกเกอร์ทั้งหมด", "totalLocker", 130, "-fx-alignment: TOP_CENTER;"),
                TableColumnFactory.createTextColumn("ว่างอยู่", "totalAvailableNow", 75, "-fx-alignment: TOP_CENTER;"),
                TableColumnFactory.createTextColumn("ใช้งานได้", "totalAvailable", 75, "-fx-alignment: TOP_CENTER;"),
                TableColumnFactory.createTextColumn("ไม่ว่าง", "totalUnavailable", 75, "-fx-alignment: TOP_CENTER;"),
                TableColumnFactory.createEnumStatusColumn("สถานะ", "status"),
                createActionColumn()
        );

        officerZonesTableView.getItems().sort(new OfficerZoneComparator(officer));
        officerZonesTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
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
                    deleteBtn.setOnAction(e -> deleteZoneToOfficer(zone));
                    box.getChildren().add(deleteBtn);
                } else {
                    addBtn.setOnAction(e -> addZoneToOfficer(zone));
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
        officerZonesTableView.refresh();
    }

    private void deleteZoneToOfficer(Zone zone) {
        officer.removeZoneUid(zone.getZoneUid());
        officersDatasource.writeData(officers);

        stage = (Stage) parentVBoxFilled.getScene().getWindow();
        Toast.show(stage, "นำ " + zone.getZoneName() + " ออกจาก " + officer.getFirstname(), 500);
        officerZonesTableView.refresh();
    }

    private void addZoneToOfficer(Zone zone) {
        officer.addZoneUid(zone.getZoneUid());
        officersDatasource.writeData(officers);

        stage = (Stage) parentVBoxFilled.getScene().getWindow();
        Toast.show(stage, "เพิ่ม " + zone.getZoneName() + " ให้ " + officer.getFirstname(), 500);
        officerZonesTableView.refresh();
    }

    protected void onBackButtonClick() {
        try {
            FXRouter.goTo("admin-manage-officers");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
