package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.FilledButton;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.components.button.IconButton;
import ku.cs.controllers.components.AddNewZonePopup;
import ku.cs.controllers.components.AdminNavbarController;
import ku.cs.controllers.components.EditZoneNamePopup;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.models.zone.ZoneStatus;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.ZoneListFileDatasource;
import ku.cs.services.utils.AlertUtil;

import java.io.IOException;

public class AdminManageZonesController {
    @FXML TableView<Zone> zoneListTableView;

    @FXML private HBox parentHBoxFilled;
    private Label headerLabel;
    private Label descriptionLabel;
    private Button addNewZoneFilledButton;

    @FXML private AdminNavbarController adminNavbarController;
    private Button footerNavBarButton;

    Datasource<ZoneList> datasource;
    private ZoneList zoneList;

    @FXML public void initialize() {
        SessionManager.requireAdminLogin();

        initDatasource();
        initUserInterfaces();
        initEvents();

        showTable(zoneList);
    }

    private void initDatasource(){
        datasource = new ZoneListFileDatasource("data", "test-zone-data.json");
        zoneList = datasource.readData();
    }

    private void initUserInterfaces(){

        Region region = new Region();
        VBox vBox = new VBox();

        footerNavBarButton = adminNavbarController.getFooterNavButton();

        parentHBoxFilled.setSpacing(4);
        region.setPrefSize(485, 50);

        footerNavBarButton.setText("ย้อนกลับ");

        headerLabel = new Label("จัดการจุดให้บริการตู้ล็อกเกอร์");
        descriptionLabel = new Label("รายชื่อของสถานที่ให้บริการทั้งหมด");
        addNewZoneFilledButton = new FilledButton("เพิ่มล็อกเกอร์ใหม่");

        LabelStyle.TITLE_LARGE.applyTo(headerLabel);
        LabelStyle.TITLE_SMALL.applyTo(descriptionLabel);

        vBox.getChildren().addAll(headerLabel, descriptionLabel);

        parentHBoxFilled.getChildren().addAll(vBox, region, addNewZoneFilledButton);
    }

    private void initEvents() {
        footerNavBarButton.setOnAction(e -> onBackButtonClick());
        addNewZoneFilledButton.setOnAction(e -> onAddNewZoneButtonClick());
    }

    private void showTable(ZoneList zoneList) {

        TableColumn<Zone, Integer> idColumn = new TableColumn<>("ID");
        TableColumn<Zone, String> zoneColumn = new TableColumn<>("ชื่อโซน");
        TableColumn<Zone, Integer> totalLockerColumn = new TableColumn<>("จำนวนล็อกเกอร์");
        TableColumn<Zone, Integer> totalAvailableNowColumn = new TableColumn<>("ล็อกเกอร์ที่ว่างอยู่");
        TableColumn<Zone, Integer> totalUnavailableColumn = new TableColumn<>("ล็อกเกอร์ที่ไม่ว่าง");
        TableColumn<Zone, ZoneStatus> statusColumn = new TableColumn<>("สถานะ");
        TableColumn<Zone, Void> actionColumn = new TableColumn<>("จัดการ");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("idZone"));
        zoneColumn.setCellValueFactory(new PropertyValueFactory<>("zone"));
        totalLockerColumn.setCellValueFactory(new PropertyValueFactory<>("totalLocker"));
        totalAvailableNowColumn.setCellValueFactory(new PropertyValueFactory<>("totalAvailableNow"));

        totalUnavailableColumn.setCellValueFactory(cellData -> null);
        totalUnavailableColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer unused, boolean empty) {
                super.updateItem(unused, empty);
                if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                    setText(null);
                    return;
                }
                Zone zone = getTableView().getItems().get(getIndex());
                setText(String.valueOf(formatUnavailable(zone)));
            }
        });

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        actionColumn.setCellFactory(createAction());

        idColumn.setMaxWidth(30);
        actionColumn.setPrefWidth(190);

        idColumn.setStyle("-fx-alignment: TOP_CENTER;");
        totalLockerColumn.setStyle("-fx-alignment: TOP_CENTER;");
        totalAvailableNowColumn.setStyle("-fx-alignment: TOP_CENTER;");
        totalUnavailableColumn.setStyle("-fx-alignment: TOP_CENTER;");
        statusColumn.setStyle("-fx-alignment: TOP_CENTER;");

        zoneListTableView.getColumns().clear();
        zoneListTableView.getColumns().addAll(idColumn, zoneColumn,
                totalLockerColumn, totalAvailableNowColumn, totalUnavailableColumn,
                statusColumn, actionColumn);
        zoneListTableView.getItems().setAll(zoneList.getZones());

        zoneListTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private Callback<TableColumn<Zone, Void>, TableCell<Zone, Void>> createAction() {
        return col -> new TableCell<>() {

            private final FilledButtonWithIcon statusBtn = FilledButtonWithIcon.small("เปลี่ยนสถานะ", Icons.SUSPEND);
            private final IconButton editBtn = new IconButton(new Icon(Icons.EDIT));
            private final IconButton deleteBtn = new IconButton(new Icon(Icons.DELETE, 24, "#EF4444"));

            {
                statusBtn.setOnAction(e -> toggleStatus(getTableView().getItems().get(getIndex())));
                editBtn.setOnAction(e -> editInfo(getTableView().getItems().get(getIndex())));
                deleteBtn.setOnAction(e -> deleteZone(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(5, statusBtn, editBtn, deleteBtn));
            }
        };
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
        AlertUtil.confirm(
                "Warning",
                "Do you want to remove [" + zone.getIdZone() + "] " + zone.getZone() + "?"
        ).ifPresent(response -> {
            if (response == ButtonType.OK) {
                zoneList.removeZoneById(zone.getIdZone());
                datasource.writeData(zoneList);
                showTable(zoneList);
            }
        });
    }

    private int formatUnavailable(Zone zone) {
        return zone.getTotalLocker() - zone.getTotalAvailableNow();
    }

    protected void onAddNewZoneButtonClick() {
        new AddNewZonePopup().run();
        showTable(zoneList);
    }

    protected void onBackButtonClick() {
        try {
            FXRouter.goTo("admin-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
