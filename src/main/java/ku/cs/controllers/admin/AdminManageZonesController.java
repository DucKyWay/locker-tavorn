package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.Toast;
import ku.cs.components.button.*;
import ku.cs.controllers.components.AddNewZonePopup;
import ku.cs.controllers.components.EditZoneNamePopup;
import ku.cs.models.account.OfficerList;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.accounts.strategy.OfficerAccountProvider;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.SearchService;
import ku.cs.services.utils.TableColumnFactory;
import ku.cs.services.zone.ZoneService;

import java.util.List;

public class AdminManageZonesController extends BaseAdminController {
    private final OfficerAccountProvider officersProvider = new OfficerAccountProvider();
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final SearchService<Zone> searchService = new SearchService<>();

    private final ZoneService zoneService = new ZoneService();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();

    @FXML private TableView<Zone> zoneListTableView;
    @FXML private VBox parentVBox;

    @FXML private TextField searchTextField;
    @FXML private Button searchButton;
    @FXML private Button addNewZoneButton;
    @FXML private Button adminManageZoneRouteLabelButton;

    private ZoneList zones;
    private OfficerList officers;

    @Override
    protected void initDatasource() {
        zones = zonesProvider.loadCollection();
        officers = officersProvider.loadCollection();
    }

    @Override
    protected void initUserInterfaces() {
        FilledButtonWithIcon.SMALL.mask(addNewZoneButton, null, Icons.LOCATION);
        IconButton.mask(searchButton, new Icon(Icons.MAGNIFYING_GLASS, 20));
        ElevatedButton.LABEL.mask(adminManageZoneRouteLabelButton);

        showTable(zones);
    }

    @Override
    protected void initEvents() {
        searchTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            onSearch();
        });
        searchButton.setOnAction(e -> onSearch());

        addNewZoneButton.setOnAction(e -> onAddNewZoneButtonClick());
    }

    private void showTable(ZoneList zones) {
        zoneListTableView.getColumns().clear();
        zoneListTableView.getColumns().setAll(
                tableColumnFactory.createTextColumn("ID", "zoneId", 36, "-fx-alignment: CENTER; -fx-padding: 0 12" ),
                tableColumnFactory.createTextColumn("ชื่อโซน", "zoneName"),
                tableColumnFactory.createTextColumn("ล็อกเกอร์", "totalLocker", 78,"-fx-alignment: CENTER; -fx-padding: 0 16" ),
                tableColumnFactory.createTextColumn("ว่างอยู่", "totalAvailableNow", 78,"-fx-alignment: CENTER; -fx-padding: 0 16"),
                tableColumnFactory.createTextColumn("ไม่ว่าง", "totalUnavailable", 78,"-fx-alignment: CENTER; -fx-padding: 0 16"),
                tableColumnFactory.createEnumStatusColumn("สถานะ", "status", 146),
                createActionColumn()
        );

        zoneListTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        zoneListTableView.getItems().setAll(zones.getZones());
    }

    private TableColumn<Zone, Void> createActionColumn() {
        return tableColumnFactory.createActionColumn("", 124, zone -> {
            IconButton statusBtn = new IconButton(new Icon(Icons.SUSPEND));
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
        Toast.show((Stage)parentVBox.getScene().getWindow(), "เปลี่ยนสถานะให้ " + zone.getZoneName(), 1300);
        showTable(zones);
    }

    private void editInfo(Zone zone) {
        new EditZoneNamePopup().run(zone);
        showTable(zones);
    }

    private void deleteZone(Zone zone) {
        new AlertUtil().confirm("Warning", "Do you want to remove [" + zone.getZoneId() + "] " + zone.getZoneName() + "?")
                .ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        if (zone.getTotalUnavailable() <= 0) {
                            zoneService.deleteZoneAndFiles(zone, zones, officers);

                            showTable(zones);
                        } else {
                            new AlertUtil().error("Error",
                                    "ยังไม่สามารถลบจุดให้บริการได้ โปรดรอให้จุดให้บริการไม่มีการใช้งานก่อน หรือระงับล็อกเกอร์ในจุดให้บริการ");
                        }
                    }
                });
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

    private void onAddNewZoneButtonClick() {
        new AddNewZonePopup().run();
    }
}
