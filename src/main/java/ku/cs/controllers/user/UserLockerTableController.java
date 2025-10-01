package ku.cs.controllers.user;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.services.context.AppContext;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.datasources.LockerListFileDatasource;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.TableColumnFactory;

import java.io.IOException;

public class UserLockerTableController extends BaseUserController{
    protected final TableColumnFactory tableColumnFactory = AppContext.getTableColumnFactory();

    private final AlertUtil alertUtil = new AlertUtil();

    @FXML private TableView<Locker> lockersTableView;

    @FXML private VBox selectLockerTypeDropdown;
    @FXML private VBox addLockerZoneDropdown;
    @FXML private HBox backButtonContainer;
    @FXML private HBox headerLabelContainer;

    private DefaultButton backButton;
    private DefaultLabel headerLabel;

    private LockerList lockers;
    private LockerListFileDatasource datasourceLocker;
    String zoneUid;

    @FXML public void initialize() {
        zoneUid = (String)FXRouter.getData();
        System.out.println("zoneUid: " + zoneUid);

        super.initialize();

        showTable();

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
        datasourceLocker =
                new LockerListFileDatasource(
                        "data/lockers",
                        "zone-" + zoneUid + ".json"
                );

        lockers = datasourceLocker.readData();
    }

    @Override
    protected void initUserInterfaces() {
        backButton = DefaultButton.warning("Back");
        headerLabel = DefaultLabel.h1("Locker List");

        backButtonContainer.getChildren().add(backButton);
        headerLabelContainer.getChildren().addAll(headerLabel);
    }

    @Override
    protected void initEvents() {
        backButton.setOnAction(e -> backButtonOnclick());
    }

    private void showTable() {
        lockersTableView.getColumns().clear();
        lockersTableView.getColumns().setAll(
                tableColumnFactory.createTextColumn("ID", "id", "-fx-alignment: CENTER"),
                tableColumnFactory.createEnumStatusColumn("Locker Type", "lockerType", 0),
                tableColumnFactory.createTextColumn("Zone", "zoneName"),
                tableColumnFactory.createTextColumn("Available", "available"),
                tableColumnFactory.createStatusColumn("Status", "status")

        );
        lockersTableView.getItems().setAll(lockers.getLockers());

        lockersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    protected void backButtonOnclick() {
        try {
            FXRouter.goTo("user-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
