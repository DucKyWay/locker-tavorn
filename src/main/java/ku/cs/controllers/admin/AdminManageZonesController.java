package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.FilledButton;
import ku.cs.controllers.components.AdminNavbarController;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
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

    Datasource<ZoneList> zoneListDatasource;
    private ZoneList zoneList;

    @FXML public void initialize() {
        SessionManager.requireAdminLogin();

        initDatasource();
        initUserInterfaces();
        initEvents();

        showTable(zoneList);
    }

    private void initDatasource(){
        zoneListDatasource = new ZoneListFileDatasource("data", "test-zone-data.json");
        zoneList = zoneListDatasource.readData();
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
        TableColumn<Zone, Integer> totalAvailableColumn = new TableColumn<>("ล็อกเกอร์ที่ใช้งานได้");
        TableColumn<Zone, String> statusColumn = new TableColumn<>("สถานะ");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("idZone"));
        zoneColumn.setCellValueFactory(new PropertyValueFactory<>("zone"));
        totalLockerColumn.setCellValueFactory(new PropertyValueFactory<>("totalLocker"));
        totalAvailableNowColumn.setCellValueFactory(new PropertyValueFactory<>("totalAvailableNow"));
        totalAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("totalAvailable"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        idColumn.setPrefWidth(20);

        idColumn.setStyle("-fx-alignment: TOP_CENTER;");
        totalLockerColumn.setStyle("-fx-alignment: TOP_CENTER;");
        totalAvailableNowColumn.setStyle("-fx-alignment: TOP_CENTER;");
        totalAvailableColumn.setStyle("-fx-alignment: TOP_CENTER;");
        statusColumn.setStyle("-fx-alignment: TOP_CENTER;");

        zoneListTableView.getColumns().clear();
        zoneListTableView.getColumns().add(idColumn);
        zoneListTableView.getColumns().add(zoneColumn);
        zoneListTableView.getColumns().add(totalLockerColumn);
        zoneListTableView.getColumns().add(totalAvailableNowColumn);
        zoneListTableView.getColumns().add(totalAvailableColumn);
        zoneListTableView.getColumns().add(statusColumn);
        zoneListTableView.getItems().setAll(zoneList.getZones());

        zoneListTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    protected void onAddNewZoneButtonClick() {
        AlertUtil.info("ยังไม่ได้ทำงะ", "เดี๋ยวทำนะ");
    }

    protected void onBackButtonClick() {
        try {
            FXRouter.goTo("admin-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
