package ku.cs.controllers.officer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.components.Icons;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;
import ku.cs.services.datasources.provider.RequestDatasourceProvider;
import ku.cs.services.ui.FXRouter;

import java.io.IOException;

public class OfficerTableLockerController extends BaseOfficerController{
    private final RequestDatasourceProvider requestsProvider = new RequestDatasourceProvider();
    private final LockerDatasourceProvider lockersProvider = new LockerDatasourceProvider();

    @FXML private TableView<Locker> lockersTableView;

    @FXML private VBox selectLockerTypeDropdown;
    @FXML private VBox addLockerZoneDropdown;
    @FXML private HBox backButtonContainer;
    @FXML private HBox headerLabelContainer;

    private DefaultButton backButton;
    private DefaultLabel headerLabel;
    private RequestList requestList;

    private LockerList lockers;

    @Override
    protected void initDatasource() {
        lockers = lockersProvider.loadCollection(currentZone.getZoneUid());
    }

    @Override
    protected void initUserInterfaces() {
        backButton = DefaultButton.warning("Back");
        headerLabel = DefaultLabel.h1("Locker List");

        backButtonContainer.getChildren().add(backButton);
        headerLabelContainer.getChildren().addAll(headerLabel);

        showTable(lockers);
    }

    @Override
    protected void initEvents() {
        backButton.setOnAction(e -> backButtonOnclick());

        lockersTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Locker>() {
            @Override
            public void changed(ObservableValue<? extends Locker> observableValue, Locker oldLocker, Locker newLocker) {
                if(newLocker !=null){
                    infoLocker(newLocker);
                }
            }
        });
    }

    private void showTable(LockerList lockers) {
        TableColumn<Locker, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("lockerId"));

        TableColumn<Locker, String> uidColumn = new TableColumn<>("UID");
        uidColumn.setCellValueFactory(new PropertyValueFactory<>("lockerUid"));

        TableColumn<Locker, String> typeColumn = new TableColumn<>("lockerType");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("lockerType"));
        typeColumn.setCellValueFactory(column -> new SimpleStringProperty(column.getValue().getLockerType().toString()));

        TableColumn<Locker, String> zoneColumn = new TableColumn<>("Zone");
        zoneColumn.setCellValueFactory(new PropertyValueFactory<>("zoneName"));

        TableColumn<Locker, String> lockerSizeTypeColumn = new TableColumn<>("Size");
        lockerSizeTypeColumn.setCellValueFactory(new PropertyValueFactory<>("lockerSizeType"));

        TableColumn<Locker, String> availableColumn = new TableColumn<>("Available");
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));

        TableColumn<Locker, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));


        TableColumn<Locker, Void> actionColumn = new TableColumn<>("จัดการ");
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final FilledButtonWithIcon infoBtn = FilledButtonWithIcon.small("ข้อมูลเพิ่มเติม", Icons.EDIT);
            private final FilledButtonWithIcon deleteBtn = FilledButtonWithIcon.small("ลบ", Icons.DELETE);

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                Locker locker = getTableRow().getItem();

                infoBtn.setOnAction(e -> infoLocker(locker));
                deleteBtn.setOnAction(e -> deleteLocker(locker));

                setGraphic(empty ? null : new HBox(5, infoBtn, deleteBtn));
            }
        });
        actionColumn.setPrefWidth(180);
        actionColumn.setStyle("-fx-alignment: CENTER;");

        lockersTableView.getColumns().clear();
        lockersTableView.getColumns().add(idColumn);
        lockersTableView.getColumns().add(uidColumn);
        lockersTableView.getColumns().add(typeColumn);
        lockersTableView.getColumns().add(zoneColumn);
        lockersTableView.getColumns().add(lockerSizeTypeColumn);
        lockersTableView.getColumns().add(availableColumn);
        lockersTableView.getColumns().add(statusColumn);
        lockersTableView.getColumns().add(actionColumn);


        lockersTableView.getItems().clear();
        lockersTableView.getItems().addAll(lockers.getLockers());
        lockersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }
    private void infoLocker(Locker locker){
        requestList = requestsProvider.loadCollection(currentZone.getZoneUid());
        Request request = requestList.findRequestbyIdLocker(locker.getLockerUid());
        try {
            FXRouter.loadDialogStage("officer-locker-dialog",locker);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void deleteLocker(Locker locker){
    }
    protected void backButtonOnclick() {
        try {
            FXRouter.goTo("officer-home", currentZone);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}