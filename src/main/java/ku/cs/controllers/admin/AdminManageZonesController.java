package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.models.zone.ZoneStatus;
import ku.cs.services.FXRouter;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.ZoneListFileDatasource;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.TableColumnFactory;

import java.io.IOException;

public class AdminManageZonesController extends BaseAdminController {
    private final AlertUtil alertUtil = new AlertUtil();

    @FXML private TableView<Zone> zoneListTableView;
    @FXML private HBox parentHBoxFilled;

    private Button addNewZoneFilledButton;
    private Datasource<ZoneList> datasource;
    private ZoneList zoneList;

    @Override
    protected void initDatasource() {
        datasource = new ZoneListFileDatasource("data", "test-zone-data.json");
        zoneList = datasource.readData();
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

        showTable(zoneList);
    }

    @Override
    protected void initEvents() {
        if (footerNavBarButton != null) {
            footerNavBarButton.setOnAction(e -> onBackButtonClick());
        }
        addNewZoneFilledButton.setOnAction(e -> onAddNewZoneButtonClick());
    }

    private void showTable(ZoneList zoneList) {
        zoneListTableView.getColumns().setAll(
                TableColumnFactory.createTextColumn("ID", "zoneId", 30, "-fx-alignment: TOP_CENTER;"),
                TableColumnFactory.createTextColumn("ชื่อโซน", "zoneName", 0, "-fx-alignment: TOP_CENTER;"),
                TableColumnFactory.createTextColumn("จำนวนล็อกเกอร์", "totalLocker", 0, "-fx-alignment: TOP_CENTER;"),
                TableColumnFactory.createTextColumn("ล็อกเกอร์ที่ว่างอยู่", "totalAvailableNow", 0, "-fx-alignment: TOP_CENTER;"),
                TableColumnFactory.createTextColumn("ล็อกเกอร์ที่ไม่ว่าง", "totalUnavailable", 0, "-fx-alignment: TOP_CENTER;"),
                createStatusColumn(),
                createActionColumn()
        );

        zoneListTableView.getItems().setAll(zoneList.getZones());
        zoneListTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private TableColumn<Zone, ZoneStatus> createStatusColumn() {
        TableColumn<Zone, ZoneStatus> col = new TableColumn<>("สถานะ");
        col.setCellValueFactory(new PropertyValueFactory<>("status"));
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(ZoneStatus status, boolean empty) {
                super.updateItem(status, empty);
                setText(empty || status == null ? null : status.getDescription());
            }
        });
        col.setPrefWidth(80);
        return col;
    }

    private TableColumn<Zone, Void> createActionColumn() {
        TableColumn<Zone, Void> actionColumn = new TableColumn<>("จัดการ");
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final FilledButtonWithIcon statusBtn = FilledButtonWithIcon.small("เปลี่ยนสถานะ", Icons.SUSPEND);
            private final IconButton editBtn = new IconButton(new Icon(Icons.EDIT));
            private final IconButton deleteBtn = IconButton.error(new Icon(Icons.DELETE));

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                Zone zone = getTableRow().getItem();
                if (empty || zone == null) {
                    setGraphic(null);
                    return;
                }

                statusBtn.setOnAction(e -> toggleStatus(zone));
                editBtn.setOnAction(e -> editInfo(zone));
                deleteBtn.setOnAction(e -> deleteZone(zone));

                setGraphic(new HBox(5, statusBtn, editBtn, deleteBtn));
            }
        });

        actionColumn.setPrefWidth(190);
        actionColumn.setStyle("-fx-alignment: CENTER;");
        return actionColumn;
    }

    private void toggleStatus(Zone zone) {
        zone.toggleStatus();
        datasource.writeData(zoneList);
        showTable(zoneList);
    }

    private void editInfo(Zone zone) {
        new EditZoneNamePopup().run(zone);
        showTable(zoneList);
    }

    private void deleteZone(Zone zone) {
        alertUtil.confirm("Warning", "Do you want to remove [" + zone.getZoneId() + "] " + zone.getZoneName() + "?")
                .ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        if (zone.getTotalUnavailable() <= 0) {
                            zoneList.removeZoneById(zone.getZoneId());
                            datasource.writeData(zoneList);
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
