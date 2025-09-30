package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.FilledButton;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.components.button.IconButton;
import ku.cs.controllers.components.AddNewZonePopup;
import ku.cs.controllers.components.EditZoneNamePopup;
import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.AppContext;
import ku.cs.services.FXRouter;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.OfficerListFileDatasource;
import ku.cs.services.datasources.ZoneListFileDatasource;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.TableColumnFactory;

import java.io.IOException;

public class AdminManageZonesController extends BaseAdminController {
    protected final TableColumnFactory tableColumnFactory = AppContext.getTableColumnFactory();

    private final AlertUtil alertUtil = new AlertUtil();

    @FXML private TableView<Zone> zonesTableView;
    @FXML private HBox parentHBoxFilled;

    private Button addNewZoneFilledButton;
    private Datasource<ZoneList> zonesDatasource;
    private ZoneList zones;
    private Datasource<OfficerList> officersDatasource;
    private OfficerList officers;

    @Override
    protected void initDatasource() {
        zonesDatasource = new ZoneListFileDatasource("data", "test-zone-data.json");
        zones = zonesDatasource.readData();

        officersDatasource = new OfficerListFileDatasource("data", "test-officer-data.json");
        officers = officersDatasource.readData();
    }

    @Override
    protected void initUserInterfaces() {
        Region region = new Region();
        VBox vBox = new VBox();

        parentHBoxFilled.setSpacing(4);
        region.setPrefSize(455, 50);

        if (footerNavBarButton != null) {
            footerNavBarButton.setText("ย้อนกลับ");
        }

        Label headerLabel = new Label("จัดการจุดให้บริการตู้ล็อกเกอร์");
        Label descriptionLabel = new Label("รายชื่อของสถานที่ให้บริการทั้งหมด");
        addNewZoneFilledButton = new FilledButton("เพิ่มจุดให้บริการใหม่");

        LabelStyle.TITLE_LARGE.applyTo(headerLabel);
        LabelStyle.TITLE_SMALL.applyTo(descriptionLabel);

        vBox.getChildren().addAll(headerLabel, descriptionLabel);
        parentHBoxFilled.getChildren().addAll(vBox, region, addNewZoneFilledButton);

        showTable(zones);
    }

    @Override
    protected void initEvents() {
        if (footerNavBarButton != null) {
            footerNavBarButton.setOnAction(e -> onBackButtonClick());
        }
        addNewZoneFilledButton.setOnAction(e -> onAddNewZoneButtonClick());
    }

    private void showTable(ZoneList zones) {
        zonesTableView.getColumns().clear();
        zonesTableView.getColumns().setAll(
                tableColumnFactory.createTextColumn("ID", "zoneId", 30, "-fx-alignment: CENTER_LEFT;"),
                tableColumnFactory.createTextColumn("ชื่อโซน", "zoneName", 0, "-fx-alignment: CENTER_LEFT;"),
                tableColumnFactory.createTextColumn("ล็อกเกอร์ทั้งหมด", "totalLocker", 0, "-fx-alignment: CENTER;"),
                tableColumnFactory.createTextColumn("ว่างอยู่", "totalAvailableNow", 0, "-fx-alignment: CENTER;"),
                tableColumnFactory.createTextColumn("ไม่ว่าง", "totalUnavailable", 0, "-fx-alignment: CENTER;"),
                tableColumnFactory.createEnumStatusColumn("สถานะ", "status", 0),
                createActionColumn()
        );

        zonesTableView.getItems().setAll(zones.getZones());
        zonesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private TableColumn<Zone, Void> createActionColumn() {
        return tableColumnFactory.createActionColumn("จัดการ", zone -> {
            FilledButtonWithIcon statusBtn = FilledButtonWithIcon.small("เปลี่ยนสถานะ", Icons.SUSPEND);
            IconButton editBtn = new IconButton(new Icon(Icons.EDIT));
            IconButton deleteBtn = IconButton.error(new Icon(Icons.DELETE));

            statusBtn.setOnAction(e -> toggleStatus(zone));
            editBtn.setOnAction(e -> editInfo(zone));
            deleteBtn.setOnAction(e -> deleteZone(zone));

            return new Button[]{statusBtn, editBtn, deleteBtn};
        });
    }

    private void toggleStatus(Zone zone) {
        zone.toggleStatus();
        zonesDatasource.writeData(zones);
        showTable(zones);
    }

    private void editInfo(Zone zone) {
        new EditZoneNamePopup().run(zone);
        showTable(zones);
    }

    private void deleteZone(Zone zone) {
        alertUtil.confirm("Warning", "Do you want to remove [" + zone.getZoneId() + "] " + zone.getZoneName() + "?")
                .ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        if (zone.getTotalUnavailable() <= 0) {
                            zones.removeZone(zone);
                            zonesDatasource.writeData(zones);

                            for(Officer officer : officers.getOfficers()) {
                                if(officer.getZoneUids().contains(zone.getZoneUid())) {
                                    officer.removeZoneUid(zone.getZoneUid());
                                }
                            }
                            officersDatasource.writeData(officers);

                            try {
                                FXRouter.goTo("admin-manage-zones");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            alertUtil.error("Error",
                                    "ยังไม่สามารถลบจุดให้บริการได้ โปรดรอให้จุดให้บริการไม่มีการใช้งานก่อน หรือระงับล็อกเกอร์ในจุดให้บริการ");
                        }
                    }
                });
    }

    private void onAddNewZoneButtonClick() {
        new AddNewZonePopup().run();
    }

    private void onBackButtonClick() {
        try {
            FXRouter.goTo("admin-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
