package ku.cs.controllers.user;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.button.IconButton;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.models.zone.ZoneStatus;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.SearchService;
import ku.cs.services.zone.ZoneService;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.TableColumnFactory;

import java.io.IOException;
import java.util.List;

public class UserZoneTableController extends BaseUserController{
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final SearchService<Zone> searchService = new SearchService<>();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();

    private final AlertUtil alertUtil = new AlertUtil();

    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;

    @FXML private TableView<Zone> zoneListTable;
    @FXML private TextField searchTextField;
    @FXML private Button searchButton;

    private ZoneList zoneList;
    private Datasource<ZoneList> datasource;
    private ZoneService zoneService =  new ZoneService();

    @Override
    protected void initDatasource() {
        zoneList = zonesProvider.loadCollection();
        zoneService.setLockerToZone(zoneList);
    }

    @Override
    protected void initUserInterfaces() {
        IconButton.mask(searchButton, new Icon(Icons.MAGNIFYING_GLASS));

        searchTextField.setPromptText("ค้นหาจากบางส่วนของชื่อ");
        searchTextField.setPrefWidth(300);

        showTable(zoneList);
    }

    @Override
    protected void initEvents() {
        searchTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            onSearch();
        });
        searchButton.setOnAction(e -> onSearch());

        zoneListTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Zone>() {
            @Override
            public void changed(ObservableValue<? extends Zone> observableValue, Zone oldzone, Zone newzone) {
                if (newzone != null) {
                    switch (newzone.getStatus()) {
                        case ZoneStatus.ACTIVE:
                            try {
                                FXRouter.goTo("user-locker", newzone.getZoneUid());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case ZoneStatus.INACTIVE:
                            alertUtil.error("Zone Not Active", "Please try again later.");
                            break;
                        case ZoneStatus.FULL:
                            alertUtil.error("Zone Full", "Please try again later.");
                            break;
                    }
                }
            }
        });
    }

    private void showTable(ZoneList zones) {
        zoneListTable.getColumns().clear();

        zoneListTable.getColumns().setAll(
                tableColumnFactory.createTextColumn("ID", "zoneId", "-fx-alignment: CENTER"),
                tableColumnFactory.createTextColumn("ชื่อโซน", "zoneName"),
                tableColumnFactory.createTextColumn("ล็อกเกอร์ทั้งหมด", "totalLocker", "-fx-alignment: CENTER"),
                tableColumnFactory.createTextColumn("ล็อกเกอร์ว่าง", "totalAvailableNow", "-fx-alignment: CENTER"),
                tableColumnFactory.createTextColumn("ล็อกเกอร์ที่ใช้งานได้", "totalAvailable", "-fx-alignment: CENTER"),
                tableColumnFactory.createEnumStatusColumn("สถานะ", "status", 0)
        );
        zoneListTable.getItems().setAll(zones.getZones());
    }

    private void onSearch() {
        String keyword = searchTextField.getText();
        List<Zone> filtered = searchService.search(
                zoneList.getZones(),
                keyword,
                Zone::getZoneName
        );
        ZoneList filteredList = new ZoneList();
        filtered.forEach(filteredList::addZone);

        showTable(filteredList);
    }
}
