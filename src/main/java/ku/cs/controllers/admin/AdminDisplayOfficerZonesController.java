package ku.cs.controllers.admin;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.Toast;
import ku.cs.components.button.ElevatedButton;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.components.button.IconButton;
import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.models.comparator.OfficerZoneComparator;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.accounts.strategy.OfficerAccountProvider;
import ku.cs.services.utils.SearchService;
import ku.cs.services.utils.TableColumnFactory;
import ku.cs.services.zone.ZoneService;

import java.io.IOException;
import java.util.List;

public class AdminDisplayOfficerZonesController extends BaseAdminController {
    private final OfficerAccountProvider officersProvider = new OfficerAccountProvider();
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final ZoneService zoneService = new ZoneService();
    protected final TableColumnFactory tableColumnFactory = new TableColumnFactory();
    private final SearchService<Zone> searchService = new SearchService<>();

    @FXML
    private Label titleLabel;
    @FXML
    private Label descriptionLabel;

    @FXML
    private TableView<Zone> officerZonesTableView;
    @FXML
    private VBox parentVBox;

    @FXML
    private TextField searchTextField;
    @FXML
    private Button searchButton;
    @FXML
    private Button adminManageOfficerRouteLabelButton;
    @FXML
    private Button adminDisplayZoneRouteLabelButton;

    private Officer officer;
    private OfficerList officers;
    private ZoneList zones;

    @Override
    protected void initDatasource() {
        officer = (Officer) FXRouter.getData();
        officers = officersProvider.loadCollection();
        officer = officers.findByUsername(officer.getUsername());
        zones = zonesProvider.loadCollection();

        zones.getZones().sort(new OfficerZoneComparator(officer));
    }

    @Override
    protected void initUserInterfaces() {
        titleLabel.setText("รายการจุดให้บริการ ของเจ้าหน้าที่: " + officer.getUsername());
        descriptionLabel.setText(officer.getFullName() + " มีจุดให้บริการทั้งหมด " + officer.getZoneUids().size() + " จุด");
        IconButton.mask(searchButton, new Icon(Icons.MAGNIFYING_GLASS));
        ElevatedButtonWithIcon.LABEL.mask(adminManageOfficerRouteLabelButton, Icons.ARROW_LEFT);
        ElevatedButton.LABEL.mask(adminDisplayZoneRouteLabelButton);

        showTable(zones);
    }

    @Override
    protected void initEvents() {
        searchTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            onSearch();
        });
        searchButton.setOnAction(e -> onSearch());

        adminManageOfficerRouteLabelButton.setOnAction(event -> onAdminManageOfficerRouteLabelButton());
    }

    private void showTable(ZoneList zones) {
        officerZonesTableView.getColumns().clear();
        officerZonesTableView.getColumns().setAll(
                tableColumnFactory.createTextColumn("ID", "zoneId", 36, "-fx-alignment: CENTER; -fx-padding: 0 12"),
                tableColumnFactory.createTextColumn("จุดให้บริการ", "zoneName"),
                tableColumnFactory.createTextColumn("ล็อคเกอร์", "totalLocker", 78, "-fx-alignment: CENTER; -fx-padding: 0 16"),
                tableColumnFactory.createTextColumn("ว่างอยู่", "totalAvailableNow", 78, "-fx-alignment: CENTER; -fx-padding: 0 16"),
                tableColumnFactory.createTextColumn("ไม่ว่าง", "totalUnavailable", 78, "-fx-alignment: CENTER; -fx-padding: 0 16"),
                tableColumnFactory.createEnumStatusColumn("สถานะ", "status", 146),
                tableColumnFactory.createActionColumn("", 88, zone -> {
                    IconButton suspendBtn = new IconButton(new Icon(Icons.SUSPEND));
                    suspendBtn.setOnAction(e -> toggleZoneStatus(zone));

                    if (officer.getZoneUids().contains(zone.getZoneUid())) {
                        IconButton deleteBtn = IconButton.error(new Icon(Icons.MINUS));
                        deleteBtn.setOnAction(e -> deleteZoneToOfficer(zone));
                        return new Button[]{suspendBtn, deleteBtn};
                    } else {
                        IconButton addBtn = IconButton.success(new Icon(Icons.PLUS));
                        addBtn.setOnAction(e -> addZoneToOfficer(zone));
                        return new Button[]{suspendBtn, addBtn};
                    }
                })
        );

        officerZonesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        officerZonesTableView.getItems().setAll(zones.getZones());
        if (searchTextField.getText().isEmpty()) {
            officerZonesTableView.getItems().sort(new OfficerZoneComparator(officer));
        }
        officerZonesTableView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Zone zone, boolean empty) {
                super.updateItem(zone, empty);
                if (empty || zone == null) {
                    setOpacity(1.0);
                } else {
                    boolean hasAddButton = !officer.getZoneUids().contains(zone.getZoneUid());
                    if (hasAddButton) {
                        setOpacity(0.5);
                    } else {
                        setOpacity(1.0);
                    }
                }
            }
        });
    }

    private void toggleZoneStatus(Zone zone) {
        zone.toggleStatus();
        zoneService.update(zone);
        Toast.show((Stage) parentVBox.getScene().getWindow(), "เปลี่ยนสถานะให้ " + zone.getZoneName(), 500);
        showTable(zones);
    }

    private void deleteZoneToOfficer(Zone zone) {
        officer.removeZoneUid(zone.getZoneUid());
        officersProvider.saveCollection(officers);
        Toast.show((Stage) parentVBox.getScene().getWindow(), "นำ " + zone.getZoneName() + " ออกจาก " + officer.getFirstname(), 500);
        showTable(zones);
    }

    private void addZoneToOfficer(Zone zone) {
        officer.addZoneUid(zone.getZoneUid());
        officersProvider.saveCollection(officers);
        Toast.show((Stage) parentVBox.getScene().getWindow(), "เพิ่ม " + zone.getZoneName() + " ให้ " + officer.getFirstname(), 500);
        showTable(zones);
    }

    private void onSearch() {
        String keyword = searchTextField.getText();
        List<Zone> filtered = searchService.search(
                zones.getZones(),
                keyword,
                Zone::getZoneName
        );
        ZoneList filteredList = new ZoneList();
        filtered.forEach(filteredList::addZone);

        showTable(filteredList);
    }

    protected void onAdminManageOfficerRouteLabelButton() {
        try {
            FXRouter.goTo("admin-manage-officers");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // Async clear
            Platform.runLater(() -> officerZonesTableView.getSelectionModel().clearSelection());
        }
    }
}
