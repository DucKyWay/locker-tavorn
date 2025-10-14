package ku.cs.controllers.user;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.button.ElevatedButtonWithIcon;
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

public class UserSelectZoneController extends BaseUserController{
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final SearchService<Zone> searchService = new SearchService<>();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();

    private final AlertUtil alertUtil = new AlertUtil();

    @FXML private TableView<Zone> zoneListTable;
    @FXML private TextField searchTextField;
    @FXML private Button searchButton;

    @FXML private Button userZoneRouteLabelButton;

    private ZoneList zoneList;
    private Datasource<ZoneList> datasource;
    private final ZoneService zoneService =  new ZoneService();

    @Override
    protected void initDatasource() {
        zoneList = zonesProvider.loadCollection();

        zoneList = zonesProvider.loadCollection();
        zoneService.updateLockersToZone(zoneList);
    }

    @Override
    protected void initUserInterfaces() {
        ElevatedButtonWithIcon.LABEL.mask(userZoneRouteLabelButton, Icons.TAG);

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
                handleGoToLocker(newzone);
            }
        });
    }
    // ในคลาส UserSelectZoneController
    private void handleGoToLocker(Zone zone) {
        if (zone != null) {
            switch (zone.getStatus()) {
                case ZoneStatus.ACTIVE:
                    try {
                        FXRouter.goTo("user-select-locker", zone);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } finally {
                        // Async clear
                        Platform.runLater(() -> zoneListTable.getSelectionModel().clearSelection());
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
    private void showTable(ZoneList zoneList) {
        zoneListTable.getColumns().clear();
        zoneListTable.getItems().clear();

        zoneListTable.getColumns().setAll(
                tableColumnFactory.createTextColumn("", "zoneId", 36, "-fx-alignment: CENTER; -fx-padding: 0 12"),
                tableColumnFactory.createTextColumn("จุดให้บริการ", "zoneName"),
                tableColumnFactory.createTextColumn("ล็อคเกอร์", "totalLocker", 78,"-fx-alignment: CENTER; -fx-padding: 0 16"),
                tableColumnFactory.createTextColumn("ว่างอยู่", "totalAvailableNow", 78, "-fx-alignment: CENTER; -fx-padding: 0 22.5"),
                tableColumnFactory.createTextColumn("ใช้งานได้", "totalAvailable", 90, "-fx-alignment: CENTER; -fx-padding: 0 23"),
                tableColumnFactory.createEnumStatusColumn("สถานะ", "status", 156),
                tableColumnFactory.createActionColumn("", 122, zone -> {
                    ElevatedButtonWithIcon gotoLockerButton = ElevatedButtonWithIcon.label("ดูล็อคเกอร์", null, Icons.ARROW_RIGHT);
                    gotoLockerButton.setOnAction(event -> {
                        handleGoToLocker(zone);
                    });
                    return  new Button[]{gotoLockerButton};
                })
        );
        zoneListTable.getItems().addAll(zoneList.getZones());

        zoneListTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Zone zone, boolean empty) {
                super.updateItem(zone, empty);
                if (empty || zone == null) {
                    setOpacity(1.0);
                } else {
                    ZoneStatus zoneStatus = zone.getStatus();
                    if (zoneStatus == ZoneStatus.INACTIVE) {
                        setOpacity(0.5);
                    } else {
                        setOpacity(1.0);
                    }
                }
            }
        });
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
