package ku.cs.controllers.user;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ku.cs.components.*;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.components.button.IconButton;
import ku.cs.models.key.KeyList;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.provider.KeyDatasourceProvider;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.SearchService;
import ku.cs.services.utils.TableColumnFactory;

import java.io.IOException;
import java.util.List;

public class UserSelectLockerController extends BaseUserController {
    private final LockerDatasourceProvider lockersProvider = new LockerDatasourceProvider();
    private final ZoneList zones = new ZoneDatasourceProvider().loadCollection();
    private final SearchService<Locker> searchService = new SearchService<>();
    protected final TableColumnFactory tableColumnFactory = new TableColumnFactory();

    @FXML private TableView<Locker> lockersTableView;

    @FXML private VBox selectLockerTypeDropdown;
    @FXML private VBox addLockerZoneDropdown;
    @FXML private Button backButton;
    @FXML private Label headerLabel;
    @FXML private Label descriptionLabel;

    @FXML private TextField searchTextField;
    @FXML private Button searchButton;

    private LockerList lockers;

    @FXML public void initialize() {
        currentZone = (Zone) FXRouter.getData();
        System.out.println("zoneUid: " + currentZone.getZoneUid());
        super.initialize();
    }

    @Override
    protected void initDatasource() {
        lockers = lockersProvider.loadCollection(currentZone.getZoneUid());
    }

    @Override
    protected void initUserInterfaces() {
        backButton.setText("ย้อนกลับ");
        ElevatedButtonWithIcon.SMALL.mask(backButton, Icons.ARROW_LEFT);

        headerLabel.setText("รายการล็อคเกอร์ภายในจุดให้บริการ");
        LabelStyle.TITLE_LARGE.applyTo(headerLabel);
        descriptionLabel.setText("พื้นที่ให้บริการ " + currentZone.getZoneName());

        IconButton.mask(searchButton, new Icon(Icons.MAGNIFYING_GLASS));
        searchTextField.setPromptText("ค้นหาจากบางส่วนของล็อคเกอร์");

        showTable(lockers);
    }

    @Override
    protected void initEvents() {
        backButton.setOnAction(e -> backButtonOnclick());

        searchTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            onSearch();
        });
        searchButton.setOnAction(e -> onSearch());

        lockersTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Locker>() {
            @Override
            public void changed(ObservableValue<? extends Locker> observableValue, Locker oldLocker, Locker newLocker) {
                if(newLocker !=null){
                    if(newLocker.isAvailable() && newLocker.isStatus()) {
                        try {
                            FXRouter.loadDialogStage("locker-reserve", newLocker);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else if(!newLocker.isAvailable()) {
                        new AlertUtil().error("ล็อกเกอร์ไม่พร้อมใช้งาน","ล็อกเกอร์ถูกใช้งานแล้ว");
                    }
                    else{
                        new AlertUtil().error("ล็อกเกอร์ไม่พร้อมใช้งาน","ล็อกเกอร์ชำรุด");
                    }
                }
            }
        });
    }

    private void showTable(LockerList lockers) {
        lockersTableView.getColumns().clear();
        lockersTableView.getColumns().setAll(
                tableColumnFactory.createZoneNameColumn("จุดให้บริการ", "zoneUid", zones),
                tableColumnFactory.createTextColumn("ล็อคเกอร์", "lockerUid", "-fx-alignment: CENTER"),
                tableColumnFactory.createEnumStatusColumn("ประเภทล็อคเกอร์", "lockerType", 0),
                tableColumnFactory.createTextColumn("ขนาดล็อคเกอร์","lockerSizeType"),
                tableColumnFactory.createLockerStatusColumn("สถานะล็อคเกอร์", "lockerUid", lockers)
        );
        lockersTableView.getItems().setAll(lockers.getLockers());
        lockersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private void onSearch() {
        String keyword = searchTextField.getText();

        List<Locker> filtered = searchService.search(
                lockers.getLockers(),
                keyword,
                l -> l.getLockerType().getDescription(),
                Locker::getLockerSizeTypeString
        );
        LockerList filteredList = new LockerList();
        filtered.forEach(filteredList::addLocker);

        showTable(filteredList);
    }

    protected void backButtonOnclick() {
        try {
            FXRouter.goTo("user-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
