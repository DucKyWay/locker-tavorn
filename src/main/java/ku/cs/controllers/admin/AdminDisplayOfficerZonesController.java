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
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.components.button.IconButton;
import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.models.comparator.OfficerZoneComparator;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.context.AppContext;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.accounts.strategy.OfficerAccountProvider;
import ku.cs.services.utils.TableColumnFactory;

import java.io.IOException;
import java.util.Collections;

public class AdminDisplayOfficerZonesController extends BaseAdminController {
    private final OfficerAccountProvider officersProvider = new OfficerAccountProvider();
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    protected final TableColumnFactory tableColumnFactory = new TableColumnFactory();

    @FXML private VBox parentVBoxFilled;
    @FXML private TableView<Zone> officerZonesTableView;
    @FXML private Button backButton;

    private OfficerList officers;
    private ZoneList zones;

    private Officer officer;
    private Stage stage;

    @Override
    protected void initDatasource() {
        officer = (Officer) FXRouter.getData();

        officers = officersProvider.loadCollection();
        officer = officers.findByUsername(officer.getUsername());

        zones = zonesProvider.loadCollection();

        Collections.sort(zones.getZones(), new OfficerZoneComparator(officer));
    }

    @Override
    protected void initUserInterfaces() {
        Label titleLabel = new Label("รายการจุดให้บริการ ของเจ้าหน้าที่: " + officer.getUsername());
        Label descriptionLabel = new Label(officer.getFullName() + " มีจุดให้บริการทั้งหมด " + officer.getZoneUids().size() + " จุด");
        ElevatedButtonWithIcon.MEDIUM.mask(backButton, Icons.ARROW_LEFT);

        LabelStyle.TITLE_LARGE.applyTo(titleLabel);
        LabelStyle.TITLE_SMALL.applyTo(descriptionLabel);

        parentVBoxFilled.getChildren().addAll(titleLabel, descriptionLabel);

        showTable();
        officerZonesTableView.getItems().setAll(zones.getZones());
    }

    @Override
    protected void initEvents() {
        backButton.setOnAction(e -> onBackButtonClick());
    }

    private void showTable() {
        officerZonesTableView.getColumns().clear();
        officerZonesTableView.getColumns().setAll(
                tableColumnFactory.createNumberColumn(),
                tableColumnFactory.createTextColumn("ชื่อโซน", "zoneName"),
                tableColumnFactory.createTextColumn("ล็อกเกอร์ทั้งหมด", "totalLocker", 130, "-fx-alignment: TOP_CENTER;"),
                tableColumnFactory.createTextColumn("ว่างอยู่", "totalAvailableNow", 75, "-fx-alignment: TOP_CENTER;"),
                tableColumnFactory.createTextColumn("ใช้งานได้", "totalAvailable", 75, "-fx-alignment: TOP_CENTER;"),
                tableColumnFactory.createTextColumn("ไม่ว่าง", "totalUnavailable", 75, "-fx-alignment: TOP_CENTER;"),
                tableColumnFactory.createEnumStatusColumn("สถานะ", "status", 0),
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
        zonesProvider.saveCollection(zones);

        stage = (Stage) parentVBoxFilled.getScene().getWindow();
        Toast.show(stage, "เปลี่ยนสถานะให้ " + zone.getZoneName(), 500);
        officerZonesTableView.refresh();
    }

    private void deleteZoneToOfficer(Zone zone) {
        officer.removeZoneUid(zone.getZoneUid());
        zonesProvider.saveCollection(zones);

        stage = (Stage) parentVBoxFilled.getScene().getWindow();
        Toast.show(stage, "นำ " + zone.getZoneName() + " ออกจาก " + officer.getFirstname(), 500);
        officerZonesTableView.refresh();
    }

    private void addZoneToOfficer(Zone zone) {
        officer.addZoneUid(zone.getZoneUid());
        zonesProvider.saveCollection(zones);

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
