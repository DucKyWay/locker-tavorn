package ku.cs.controllers.user;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.button.IconButton;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.datasources.LockerListFileDatasource;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.SearchService;
import ku.cs.services.utils.TableColumnFactory;

import java.io.IOException;
import java.util.List;

public class UserLockerTableController extends BaseUserController{
    private final LockerDatasourceProvider lockersProvider = new LockerDatasourceProvider();
    private final ZoneList zones = new ZoneDatasourceProvider().loadCollection();
    private final SearchService<Locker> searchService = new SearchService<>();
    protected final TableColumnFactory tableColumnFactory = new TableColumnFactory();

    private final AlertUtil alertUtil = new AlertUtil();

    @FXML private TableView<Locker> lockersTableView;

    @FXML private VBox selectLockerTypeDropdown;
    @FXML private VBox addLockerZoneDropdown;
    @FXML private HBox backButtonContainer;
    @FXML private HBox headerLabelContainer;

    private DefaultButton backButton;
    private DefaultLabel headerLabel;
    private TextField searchTextField;
    private Button searchButton;

    private LockerList lockers;
    String zoneUid;

    @FXML public void initialize() {
        zoneUid = (String)FXRouter.getData();
        System.out.println("zoneUid: " + zoneUid);

        super.initialize();

        showTable(lockers);

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
                        alertUtil.error("ล็อกเกอร์ไม่พร้อมใช้งาน","ล็อกเกอร์ถูกใช้งานแล้ว");
                    }
                    else{
                        alertUtil.error("ล็อกเกอร์ไม่พร้อมใช้งาน","ล็อกเกอร์ชำรุด");
                    }
                }
            }
        });
    }

    @Override
    protected void initDatasource() {
        lockers = lockersProvider.loadCollection(zoneUid);
    }

    @Override
    protected void initUserInterfaces() {
        HBox searchBarHBox = new HBox();

        backButton = DefaultButton.warning("Back");
        headerLabel = DefaultLabel.h1("Locker List");
        searchTextField = new TextField();
        searchButton = new IconButton(new Icon(Icons.MAGNIFYING_GLASS));

        searchTextField.setPromptText("ค้นหาจากบางส่วนของชื่อ");
        searchTextField.setPrefWidth(300);

        searchBarHBox.getChildren().addAll(searchTextField, searchButton);

        backButtonContainer.getChildren().add(backButton);
        headerLabelContainer.getChildren().addAll(headerLabel, searchBarHBox);
    }

    @Override
    protected void initEvents() {
        backButton.setOnAction(e -> backButtonOnclick());

        searchTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            onSearch();
        });
        searchButton.setOnAction(e -> onSearch());
    }

    private void showTable(LockerList lockers) {
        lockersTableView.getColumns().clear();
        lockersTableView.getColumns().setAll(
                tableColumnFactory.createTextColumn("ID", "lockerUid", "-fx-alignment: CENTER"),
                tableColumnFactory.createEnumStatusColumn("Locker Type", "lockerType", 0),
                tableColumnFactory.createTextColumn("Zone", "zoneName"),
                tableColumnFactory.createTextColumn("Size","lockerSizeType"),
                tableColumnFactory.createTextColumn("Available", "available"),
                tableColumnFactory.createStatusColumn("Status", "status")

        );
        lockersTableView.getItems().setAll(lockers.getLockers());
        lockersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private void onSearch() {
        String keyword = searchTextField.getText();
        List<Locker> filtered = searchService.search(
                lockers.getLockers(),
                keyword,
                l -> l.getLockerType().getDescription(), // เปลี่ยนเป็น lambda จะได้ไม่ต้องทำ getLockerTypeString
                Locker::getLockerSizeTypeString,
                Locker::getZoneUid,
                l -> zones.findZoneByUid(zoneUid).getZoneName()
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
