package ku.cs.controllers.user;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.LockerBox;
import ku.cs.components.button.ElevatedButton;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.components.button.IconButton;
import ku.cs.models.comparator.Locker.LockerAvailableComparator;
import ku.cs.models.comparator.Locker.LockerIdComparator;
import ku.cs.models.comparator.Locker.LockerSizeTypeComparator;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.SearchService;
import ku.cs.services.utils.TableColumnFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class UserSelectLockerController extends BaseUserController {
    private final LockerDatasourceProvider lockersProvider = new LockerDatasourceProvider();
    private final ZoneList zones = new ZoneDatasourceProvider().loadCollection();
    private final SearchService<Locker> searchService = new SearchService<>();
    protected final TableColumnFactory tableColumnFactory = new TableColumnFactory();

    @FXML private TableView<Locker> lockersTableView;
    @FXML private ScrollPane lockersScrollPane;
    @FXML private FlowPane lockersFlowPane;

    @FXML private Button userZoneRouteLabelButton;
    @FXML private Button zoneRouteLabelButton;
    @FXML private Button rowButton;
    @FXML private Button gridButton;

    @FXML private Label titleLabel;

    @FXML private TextField searchTextField;
    @FXML private Button searchButton;

    @FXML private ComboBox<String> FilterComboBox;
    @FXML private Label filterIconLabel;

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
        filterIconLabel.setGraphic(new Icon(Icons.FILTER, 20));

        titleLabel.setText(currentZone.getZoneName());
        zoneRouteLabelButton.setText(currentZone.getZoneName());

        showTable(lockers);
    }

    private void updateView(boolean layout) {
        gridButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), layout);
        rowButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), !layout);

        lockersScrollPane.setVisible(layout);
        lockersScrollPane.setManaged(layout);

        lockersTableView.setVisible(!layout);
        lockersTableView.setManaged(!layout);

        if (layout) {
            showFlow(lockers);
        } else {
            showTable(lockers);
        }
    }


    @Override
    protected void initEvents() {
        rowButton.setOnAction(event -> {
            layout = false;
            updateView(false);
        });
        gridButton.setOnAction(event -> {
            layout = true;
            updateView(true);
        });
        userZoneRouteLabelButton.setOnAction(e -> backButtonOnclick());
        searchTextField.textProperty().addListener((obs, oldValue, newValue) -> onSearch());
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

        //filter
        ObservableList<String> filters = FXCollections.observableArrayList();
        filters.add("ตามหมายเลขตู้");
        filters.add("ขนาดตู้ เล็ก-ใหญ่");
        filters.add("ขนาดตู้ ใหญ่-เล็ก");
        filters.add("ตามตู้ว่าง");
        FilterComboBox.setItems(filters);
        FilterComboBox.valueProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue != null){
                    switch (newValue) {
                        case "ตามหมายเลขตู้" -> filterLockerbyId();
                        case "ขนาดตู้ เล็ก-ใหญ่" -> filterLockerbySize(true);
                        case "ขนาดตู้ ใหญ่-เล็ก" -> filterLockerbySize(false);
                        case "ตามตู้ว่าง" -> filterLockerbyAvailability();
                    }
                    showTable(lockers);

                    showFlow(lockers);
                }
            }
        });
    }
    private void filterLockerbyId(){
        Collections.sort(lockers.getLockers(), new LockerIdComparator());
    }
    private void filterLockerbySize(boolean reverse){
        filterLockerbyId();
        if(reverse){
            Collections.sort(lockers.getLockers(), new LockerSizeTypeComparator().reversed());
        }else{
            Collections.sort(lockers.getLockers(), new LockerSizeTypeComparator());
        }
    }

    private void filterLockerbyAvailability(){
        Collections.sort(lockers.getLockers(), new LockerAvailableComparator());
    }


    private void showTable(LockerList lockers) {
        lockersTableView.getColumns().clear();
        lockersTableView.getColumns().setAll(
                createLockerIdColumn(),
                tableColumnFactory.createTextColumn("รหัสล็อคเกอร์", "lockerUid",100, "-fx-alignment: CENTER; -fx-padding: 0 16"),
                tableColumnFactory.createZoneNameColumn("จุดให้บริการ", "zoneUid", zones),
                tableColumnFactory.createEnumStatusColumn("ประเภทล็อคเกอร์", "lockerType", 120),
                tableColumnFactory.createTextColumn("ขนาดล็อคเกอร์","lockerSizeType", 106, "-fx-alignment: CENTER; -fx-padding: 0 16"),
                tableColumnFactory.createLockerStatusColumn("สถานะล็อคเกอร์", "lockerUid", lockers)
        );
        lockersTableView.getItems().setAll(lockers.getLockers());
        lockersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    TableColumn<Locker, String> createLockerIdColumn() {
        TableColumn<Locker, String> col = new TableColumn<>("NO.");
        col.setMinWidth(36);
        col.setPrefWidth(36);
        col.setMaxWidth(36);
        col.setStyle("-fx-padding: 0 8; -fx-alignment: CENTER;");
        col.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    return;
                }
                Locker locker = getTableRow().getItem();
                setText(String.valueOf(locker.getLockerId()));
            }
        });
        return col;
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
