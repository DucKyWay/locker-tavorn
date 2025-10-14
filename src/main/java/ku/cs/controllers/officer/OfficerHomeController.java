package ku.cs.controllers.officer;

import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.components.button.IconButton;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.SearchService;
import ku.cs.services.utils.TableColumnFactory;

import java.io.IOException;
import java.util.List;

public class OfficerHomeController extends BaseOfficerController {
    private final LockerList lockers = new LockerDatasourceProvider().loadAllCollections();
    private final ZoneList zones = new ZoneDatasourceProvider().loadCollection();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();
    private final SearchService<Locker> searchService = new SearchService<>();

    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;

    @FXML private TableView<Locker> lockersTableView;

    @FXML private TextField searchTextField;
    @FXML private Button searchButton;

    @FXML private Button officerWelcomeRouteLabelButton;
    private LockerList lockersOnOfficer = new LockerList();

    @Override
    protected void initDatasource() {
        lockersOnOfficer = lockers.filterByZoneUids(current.getZoneUids());
    }

    @Override
    protected void initUserInterfaces() {
        ElevatedButtonWithIcon.LABEL.mask(officerWelcomeRouteLabelButton, Icons.TAG);
        IconButton.mask(searchButton, new Icon(Icons.MAGNIFYING_GLASS));

        showTable(lockersOnOfficer);
    }

    @Override
    protected void initEvents() {
        searchTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            onSearch();
        });
        searchButton.setOnAction(e -> onSearch());

        lockersTableView.getSelectionModel().selectedItemProperty().addListener(
            (observableValue, oldLocker, newLocker) -> {
                if(newLocker !=null){
                    try {
                        FXRouter.loadDialogStage("officer-display-locker-history", newLocker);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } finally {
                        // Async clear
                        Platform.runLater(() -> lockersTableView.getSelectionModel().clearSelection());
                    }
                }
            }
        );
    }

    private void showTable(LockerList lockerList) {
        lockersTableView.getColumns().clear();
        lockersTableView.getColumns().setAll(
                tableColumnFactory.createNumberColumn(),
                tableColumnFactory.createZoneNameColumn("จุดให้บริการ", "zoneUid", zones),
                tableColumnFactory.createTextColumn("ไอดีล็อคเกอร์", "lockerUid"),
                tableColumnFactory.createEnumStatusColumn("ขนาดล็อคเกอร์", "lockerSizeType", 106),
                tableColumnFactory.createEnumStatusColumn("ประเภทล็อคเกอร์", "lockerType", 120),
                tableColumnFactory.createLockerStatusColumn("สถานะล็อคเกอร์", "lockerUid", lockers),
                tableColumnFactory.createActionColumn("",locker -> {
                    ElevatedButtonWithIcon infoBtn = ElevatedButtonWithIcon.small("แก้ไข", Icons.EDIT);
                    infoBtn.setOnAction(e -> infoLocker(locker));

                    return new Button[]{infoBtn};
                })
        );

        lockersTableView.getItems().clear();
        lockersTableView.getItems().setAll(lockerList.getLockers());
        lockersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private TableColumn<Locker, Void> createActionColumn() {
        return tableColumnFactory.createActionColumn("จัดการ",locker -> {
            ElevatedButtonWithIcon infoBtn = ElevatedButtonWithIcon.small("แก้ไข", Icons.EDIT);
            infoBtn.setOnAction(e -> infoLocker(locker));

            return new Button[]{infoBtn};
        });
    }

    private void infoLocker(Locker locker){
        try {
            FXRouter.loadDialogStage("officer-locker-dialog", locker);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        showTable(lockersOnOfficer);
    }

    private void onSearch() {
        String keyword = searchTextField.getText();
        if (keyword.isEmpty()) {
            showTable(lockersOnOfficer);
            return;
        }

        List<Locker> filtered = searchService.search(
                lockersOnOfficer.getLockers(),
                keyword,
                l -> String.valueOf(l.getLockerId()),
                Locker::getZoneUid,
                l -> zones.findZoneByUid(l.getZoneUid()).getZoneName(),
                Locker::getLockerUid,
                Locker::getLockerSizeTypeString,
                l -> l.getLockerType().getDescription(),
                l -> String.valueOf(l.isStatus()),
                l -> "LOCKER:" + l.getLockerUid()
        );
        LockerList filteredList = new LockerList();
        filtered.forEach(filteredList::addLocker);

        showTable(filteredList);
    }
}
