package ku.cs.controllers.officer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.components.button.FilledButton;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.components.button.IconButton;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.locker.LockerSizeType;
import ku.cs.models.locker.LockerType;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;
import ku.cs.services.datasources.provider.RequestDatasourceProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.SearchService;
import ku.cs.services.utils.TableColumnFactory;

import java.io.IOException;
import java.util.List;

public class OfficerManageLockersController extends BaseOfficerController{

    private final RequestDatasourceProvider requestsProvider = new RequestDatasourceProvider();
    private final LockerDatasourceProvider lockersProvider = new LockerDatasourceProvider();
    private final SearchService<Locker> searchService = new SearchService<>();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();

    private LockerList lockerlist;

    @FXML private Button backButton;
    @FXML private Label headerLabel;
    @FXML private Label descriptionLabel;
    @FXML private TextField searchTextField;
    @FXML private Button searchButton;
    @FXML private TableView<Locker> lockersTableView;
    @FXML private Button addlockerManualButton;
    @FXML private Button addlockerDigitalButton;

    @Override
    protected void initDatasource() {
        lockerlist = lockersProvider.loadCollection(currentZone.getZoneUid());
    }

    @Override
    protected void initUserInterfaces() {
        headerLabel.setText("รายการล็อคเกอร์");
        descriptionLabel.setText("จุดให้บริการ " + currentZone.getZoneName() + " [" + currentZone.getZoneUid() + "]");

        ElevatedButtonWithIcon.SMALL.mask(backButton, Icons.ARROW_LEFT);
        IconButton.mask(searchButton, new Icon(Icons.MAGNIFYING_GLASS));
        FilledButton.SMALL.mask(addlockerManualButton);
        FilledButton.SMALL.mask(addlockerDigitalButton);
        showTable(lockerlist);
    }

    @Override
    protected void initEvents() {
        backButton.setOnAction(e -> onBackButtonClick());
        addlockerManualButton.setOnAction(e -> onAddlockerManualButtonClick());
        addlockerDigitalButton.setOnAction(e -> onAddlockerDigitalButtonClick());
        searchTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            onSearch();
        });
        searchButton.setOnAction(e -> onSearch());

        lockersTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Locker>() {
            @Override
            public void changed(ObservableValue<? extends Locker> observableValue, Locker oldLocker, Locker newLocker) {
                if(newLocker !=null){
                    infoLocker(newLocker);
                }
            }
        });
    }

    private void showTable(LockerList lockersTable) {
        lockersTableView.getColumns().clear();
        lockersTableView.getItems().clear();

        lockersTableView.getColumns().setAll(
                tableColumnFactory.createTextColumn("ที่", "lockerId", 60),
                tableColumnFactory.createTextColumn("เลขล็อคเกอร์", "lockerUid", 105),
                tableColumnFactory.createEnumStatusColumn("ประเภทล็อคเกอร์", "lockerType",0),
                tableColumnFactory.createEnumStatusColumn("ขนาดล็อคเกอร์", "lockerSizeType", 0),
                tableColumnFactory.createStatusColumn("สถานะ", "available", "พร้อมใช้งาน", "ใช้งานอยู่"),
                tableColumnFactory.createStatusColumn("ชำรุด", "status", "ใช้งานได้", "ชำรุด"),
                createActionColumn()
        );

        lockersTableView.getItems().setAll(lockersTable.getLockers());
    }
    private void onAddlockerManualButtonClick(){
        Locker newLocker = new Locker(LockerType.MANUAL, LockerSizeType.MEDIUM, currentZone.getZoneUid());
        lockerlist.addLocker(newLocker);
        lockersProvider.saveCollection(currentZone.getZoneUid(), lockerlist);
        showTable(lockerlist);

    }
    private void onAddlockerDigitalButtonClick(){
        Locker newLocker = new Locker(LockerType.DIGITAL, LockerSizeType.MEDIUM,currentZone.getZoneUid());
        lockerlist.addLocker(newLocker);
        lockersProvider.saveCollection(currentZone.getZoneUid(), lockerlist);
        showTable(lockerlist);
        
    }
    private TableColumn<Locker, Void> createActionColumn() {
        return tableColumnFactory.createActionColumn("จัดการ", locker -> {
            FilledButtonWithIcon infoBtn = FilledButtonWithIcon.small("ข้อมูล", Icons.EDIT);
            FilledButtonWithIcon historyBtn = FilledButtonWithIcon.small("ประวัติ", Icons.HISTORY);

            infoBtn.setOnAction(e -> infoLocker(locker));
            historyBtn.setOnAction(e -> deleteLocker(locker));

            return new Button[]{infoBtn, historyBtn};
        });
    }

    private void infoLocker(Locker locker){
        RequestList requests = requestsProvider.loadCollection(currentZone.getZoneUid());
        Request request = requests.findRequestByLockerUid(locker.getLockerUid());
        try {
            FXRouter.loadDialogStage("officer-locker-dialog", locker);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        showTable(lockerlist);
    }

    private void deleteLocker(Locker locker){
        try {
            FXRouter.loadDialogStage("officer-display-locker-history", locker);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void onSearch() {
        String keyword = searchTextField.getText();

        if(keyword.isEmpty()) {
            showTable(lockerlist);
        }

        List<Locker> filtered = searchService.search(
                lockerlist.getLockers(),
                keyword,
                Locker::getLockerUid,
                l -> String.valueOf(l.getLockerId()),
                l -> l.getLockerType().getDescription(), // lambda ไม่ต้องทำ getLockerTypeString
                Locker::getLockerSizeTypeString
        );

        LockerList filteredList = new LockerList();
        filtered.forEach(filteredList::addLocker);

        showTable(filteredList);
    }

    private void onBackButtonClick(){
        try {
            FXRouter.goTo("officer-home", currentZone);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
