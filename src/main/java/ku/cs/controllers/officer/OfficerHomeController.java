package ku.cs.controllers.officer;

import javafx.fxml.FXML;

import javafx.scene.control.*;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.ElevatedButton;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.components.button.IconButton;
import ku.cs.models.account.User;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.request.Request;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.SearchService;
import ku.cs.services.utils.TableColumnFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.Lock;

public class OfficerHomeController extends BaseOfficerController {
    private final LockerDatasourceProvider lockerProvider = new LockerDatasourceProvider();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();
    private final SearchService<Locker> searchService = new SearchService<>();

    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;
    @FXML private TextField searchTextField;
    @FXML private Button searchButton;
    @FXML private Button backButton;

    @FXML private TableView<Locker> lockersTableView;
    private List<LockerList> lockers;
    private LockerList allLockers = new LockerList();

    @Override
    protected void initDatasource() {
        lockers = lockerProvider.loadAllCollections();

        allLockers.addLocker((lockers.stream()
                .flatMap(lockerList -> lockerList.getLockers().stream())
                .toList()));
    }

    @Override
    protected void initUserInterfaces() {
        backButton.setText("กลับไปเลือกจุดให้บริการ");
        FilledButtonWithIcon.mask(backButton, null, Icons.ARROW_LEFT);

        LabelStyle.TITLE_LARGE.applyTo(titleLabel);
        LabelStyle.TITLE_SMALL.applyTo(descriptionLabel);
        FilledButtonWithIcon.mask(searchButton, Icons.MAGNIFYING_GLASS);
        showTable(allLockers);
    }

    @Override
    protected void initEvents() {
        backButton.setOnAction(e -> onBackButtonClick());

        searchTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            onSearch();
        });
        searchButton.setOnAction(e -> onSearch());

        lockersTableView.getSelectionModel().selectedItemProperty().addListener(
            (observableValue, oldLocker, newLocker) -> {
                if(newLocker !=null){
                    try {
                        FXRouter.goTo("officer-history-request", currentZone);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
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
                tableColumnFactory.createTextColumn("เลขล็อคเกอร์", "lockerUid", 90, "-fx-alignment: CENTER; -fx-padding: 0 16"),
                tableColumnFactory.createEnumStatusColumn("ขนาดล็อคเกอร์", "lockerSizeType", 90),
                tableColumnFactory.createEnumStatusColumn("ประเภทล็อคเกอร์", "lockerType", 100),
                tableColumnFactory.createStatusColumn("สถานะ", "available", 120, "ใช้งานได้", "ถูกใช้งานอยู่"),
                createActionColumn()
        );

        lockersTableView.getItems().clear();
        lockersTableView.getItems().setAll(lockerList.getLockers());
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

    private void onBackButtonClick() {
        try {
            FXRouter.goTo("officer-zone-list");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
