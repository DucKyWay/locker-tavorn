package ku.cs.controllers.user;

import javafx.fxml.FXML;

import javafx.scene.control.*;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.components.button.IconButton;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.SearchService;
import ku.cs.services.utils.TableColumnFactory;

import java.io.IOException;
import java.util.List;

public class UserHomeController extends BaseUserController {
    private final LockerDatasourceProvider lockerProvider = new LockerDatasourceProvider();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();
    private final SearchService<Locker> searchService = new SearchService<>();

    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;
    @FXML private TextField searchTextField;
    @FXML private Button searchButton;

    @FXML private TableView<Locker> lockersTableView;
    private List<LockerList> lockers;
    private LockerList allLockers = new LockerList();

    @Override
    protected void initDatasource() {
        lockers = lockerProvider.loadAllCollectionsList();

        allLockers.addLocker((lockers.stream()
                .flatMap(lockerList -> lockerList.getLockers().stream())
                .toList()));
    }

    @Override
    protected void initUserInterfaces() {
        LabelStyle.TITLE_LARGE.applyTo(titleLabel);
        LabelStyle.TITLE_SMALL.applyTo(descriptionLabel);
        IconButton.mask(searchButton, new Icon(Icons.MAGNIFYING_GLASS, 20));
        showTable(allLockers);
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
        );
    }

    private void showTable(LockerList lockerList) {
        lockersTableView.getColumns().clear();
        lockersTableView.getColumns().setAll(
            tableColumnFactory.createTextColumn("ที่", "lockerId", 45, "-fx-alignment: CENTER; -fx-padding: 0 16"),
            tableColumnFactory.createTextColumn("จุดให้บริการ", "zoneName", 200, "-fx-alignment: CENTER; -fx-padding: 0 16"),
            tableColumnFactory.createTextColumn("เลขล็อคเกอร์", "lockerUid", 100, "-fx-alignment: CENTER; -fx-padding: 0 16"),
            tableColumnFactory.createEnumStatusColumn("ขนาดล็อคเกอร์", "lockerSizeType", 100),
            tableColumnFactory.createEnumStatusColumn("ประเภทล็อคเกอร์", "lockerType", 100),
            tableColumnFactory.createStatusColumn("สถานะ", "available", 140, "ใช้งานได้", "ถูกใช้งานอยู่")
        );

        lockersTableView.getItems().clear();
        lockersTableView.getItems().setAll(lockerList.getLockers());
    }

    private void onSearch() {
        String keyword = searchTextField.getText();

        if (keyword.isEmpty()) {
            showTable(allLockers);
            return;
        }

        List<Locker> filtered = searchService.search(
            allLockers.getLockers(),
            keyword,
            l -> String.valueOf(l.getLockerId()),
            Locker::getZoneName,
            Locker::getLockerUid,
            Locker::getLockerSizeTypeString,
            l -> l.getLockerType().getDescription(),
            l -> String.valueOf(l.isStatus())
        );
        LockerList filteredlist = new LockerList();
        filtered.forEach(filteredlist::addLocker);

        showTable(filteredlist);
    }
}
