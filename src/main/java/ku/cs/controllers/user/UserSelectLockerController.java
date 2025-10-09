package ku.cs.controllers.user;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import ku.cs.components.*;
import ku.cs.components.button.ElevatedButton;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.components.button.FilledButton;
import ku.cs.components.button.IconButton;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.ui.Theme;
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
    @FXML private ScrollPane lockersScrollPane;
    @FXML private FlowPane lockersFlowPane;

    @FXML private VBox selectLockerTypeDropdown;
    @FXML private VBox addLockerZoneDropdown;
    @FXML private Button userZoneRouteLabelButton;
    @FXML private Button zoneRouteLabelButton;
    @FXML private Button rowButton;
    @FXML private Button gridButton;
    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;

    @FXML private TextField searchTextField;
    @FXML private Button searchButton;

    private LockerList lockers;
    private boolean layout = true;

    @FXML public void initialize() {
        currentZone = (Zone) FXRouter.getData();
        System.out.println("zoneUid: " + currentZone.getZoneUid());
        super.initialize();
    }

    @Override
    protected void initDatasource() {
        lockers = lockersProvider.loadCollection(currentZone.getZoneUid());
        updateView(layout);
    }

    @Override
    protected void initUserInterfaces() {
        ElevatedButtonWithIcon.LABEL.mask(userZoneRouteLabelButton, Icons.ARROW_LEFT);
        ElevatedButton.LABEL.mask(zoneRouteLabelButton);
        IconButton.mask(searchButton, new Icon(Icons.MAGNIFYING_GLASS));
        IconButton.mask(rowButton, new Icon(Icons.ROW));
        IconButton.mask(gridButton, new Icon(Icons.GRID));

        titleLabel.setText(currentZone.getZoneName());
        zoneRouteLabelButton.setText(currentZone.getZoneName());

        showTable(lockers);
    }

    private void updateView(boolean layout) {
        gridButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), layout);
        rowButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), !layout);

        boolean showFlow = layout;
        lockersScrollPane.setVisible(showFlow);
        lockersScrollPane.setManaged(showFlow);

        lockersTableView.setVisible(!showFlow);
        lockersTableView.setManaged(!showFlow);

        if (showFlow) {
            showFlow(lockers);          // ใช้ข้อมูลทั้งหมดตามปกติ
        } else {
            showTable(lockers);
        }
    }


    @Override
    protected void initEvents() {
        rowButton.setOnAction(event -> {
            layout = false;
            updateView(layout);
        });
        gridButton.setOnAction(event -> {
            layout = true;
            updateView(layout);
        });
        userZoneRouteLabelButton.setOnAction(e -> backButtonOnclick());
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

    private void showFlow(LockerList lockers){
        lockersFlowPane.getChildren().clear();
        for (Locker locker : lockers.getLockers()){
            LockerBox box = new LockerBox(locker);
            box.setOnAction(event -> {
                if(locker !=null){
                    if(locker.isAvailable() && locker.isStatus()) {
                        try {
                            FXRouter.loadDialogStage("locker-reserve", locker);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else if(!locker.isAvailable()) {
                        new AlertUtil().error("ล็อกเกอร์ไม่พร้อมใช้งาน","ล็อกเกอร์ถูกใช้งานแล้ว");
                    }
                    else{
                        new AlertUtil().error("ล็อกเกอร์ไม่พร้อมใช้งาน","ล็อกเกอร์ชำรุด");
                    }
                }
            });
            lockersFlowPane.getChildren().add(box);
        }
    }

    private void onSearch() {
        String keyword = searchTextField.getText();

        List<Locker> filtered = searchService.search(
                lockers.getLockers(),
                keyword,
                Locker::getLockerUid,
                l -> l.getLockerType().getDescription(),
                Locker::getLockerSizeTypeString
        );
        LockerList filteredList = new LockerList();
        filtered.forEach(filteredList::addLocker);

        if (layout) {
            showFlow(filteredList);
        } else {
            showTable(filteredList);
        }
    }

    protected void backButtonOnclick() {
        try {
            FXRouter.goTo("user-zone");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
