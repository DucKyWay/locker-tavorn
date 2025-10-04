package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.*;
import ku.cs.controllers.components.AddNewZonePopup;
import ku.cs.controllers.components.EditZoneNamePopup;
import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.context.AppContext;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.accounts.strategy.OfficerAccountProvider;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.TableColumnFactory;
import ku.cs.services.zone.ZoneService;

import java.io.IOException;

public class AdminManageZonesController extends BaseAdminController {
    private final OfficerAccountProvider officersProvider = new OfficerAccountProvider();
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final ZoneService zoneService = new ZoneService();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();

    private final AlertUtil alertUtil = new AlertUtil();

    @FXML private TableView<Zone> zoneListTableView;
    @FXML private HBox parentHBoxFilled;
    @FXML private Button backButton;

    private Button addNewZoneFilledButton;
    private ZoneList zones;
    private OfficerList officers;

    @Override
    protected void initDatasource() {
        zones = zonesProvider.loadCollection();
        officers = officersProvider.loadCollection();
    }

    @Override
    protected void initUserInterfaces() {
        Region region = new Region();
        VBox vBox = new VBox();

        parentHBoxFilled.setSpacing(4);
        region.setPrefSize(455, 50);

        Label headerLabel = new Label("จัดการจุดให้บริการตู้ล็อกเกอร์");
        Label descriptionLabel = new Label("รายชื่อของสถานที่ให้บริการทั้งหมด");
        ElevatedButtonWithIcon.MEDIUM.mask(backButton, Icons.ARROW_LEFT);
        addNewZoneFilledButton = new FilledButton("เพิ่มจุดให้บริการใหม่");

        LabelStyle.TITLE_LARGE.applyTo(headerLabel);
        LabelStyle.TITLE_SMALL.applyTo(descriptionLabel);

        vBox.getChildren().addAll(headerLabel, descriptionLabel);
        parentHBoxFilled.getChildren().addAll(vBox, region, addNewZoneFilledButton);

        showTable(zones);
    }

    @Override
    protected void initEvents() {
        backButton.setOnAction(e -> onBackButtonClick());
        addNewZoneFilledButton.setOnAction(e -> onAddNewZoneButtonClick());
    }

    private void showTable(ZoneList zones) {
        zoneListTableView.getColumns().clear();
        zoneListTableView.getColumns().setAll(
                tableColumnFactory.createTextColumn("ID", "zoneId", 30, "-fx-alignment: CENTER_LEFT;"),
                tableColumnFactory.createTextColumn("ชื่อโซน", "zoneName", 0, "-fx-alignment: CENTER_LEFT;"),
                tableColumnFactory.createTextColumn("ล็อกเกอร์ทั้งหมด", "totalLocker", 0, "-fx-alignment: CENTER;"),
                tableColumnFactory.createTextColumn("ว่างอยู่", "totalAvailableNow", 0, "-fx-alignment: CENTER;"),
                tableColumnFactory.createTextColumn("ไม่ว่าง", "totalUnavailable", 0, "-fx-alignment: CENTER;"),
                tableColumnFactory.createEnumStatusColumn("สถานะ", "status", 0),
                createActionColumn()
        );

        zoneListTableView.getItems().setAll(zones.getZones());
        zoneListTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
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
        zonesProvider.saveCollection(zones);
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
                            zoneService.deleteZoneAndFiles(zone, zones, officers);

                            showTable(zones);
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
