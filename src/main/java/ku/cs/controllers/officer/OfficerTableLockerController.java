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
import ku.cs.models.zone.Zone;
import ku.cs.services.FXRouter;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.LockerListFileDatasource;
import ku.cs.services.datasources.RequestListFileDatasource;

import java.io.IOException;

public class OfficerTableLockerController{
    @FXML private TableView<Locker> lockersTableView;

    @FXML private VBox selectLockerTypeDropdown;
    @FXML private VBox addLockerZoneDropdown;
    @FXML private HBox backButtonContainer;
    @FXML private HBox headerLabelContainer;

    private DefaultButton backButton;
    private DefaultLabel headerLabel;
    private Datasource<RequestList> datasourceRequest;
    private RequestList requestList;

    private LockerList lockers;
    private LockerListFileDatasource datasourceLocker;
    Zone zone;
    @FXML public void initialize() {
        zone = (Zone) FXRouter.getData();
        initDatasource();
        initUserInterface();
        initEvents();
        showTable(lockers);

        lockersTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Locker>() {
            @Override
            public void changed(ObservableValue<? extends Locker> observableValue, Locker oldLocker, Locker newLocker) {
                if(newLocker !=null){
                    try {
                        FXRouter.loadDialogStage("locker-reserve",newLocker);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void initDatasource() {
        datasourceLocker =
                new LockerListFileDatasource(
                        "data/lockers",
                        "zone-" + zone.getZoneUid() + ".json"
                );

        lockers = datasourceLocker.readData();
    }

    private void initUserInterface() {
        backButton = DefaultButton.warning("Back");
        headerLabel = DefaultLabel.h1("Locker List");

        backButtonContainer.getChildren().add(backButton);
        headerLabelContainer.getChildren().addAll(headerLabel);
    }

    private void initEvents() {
        backButton.setOnAction(e -> backButtonOnclick());
    }

    private void showTable(LockerList lockers) {
        TableColumn<Locker, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Locker, String> typeColumn = new TableColumn<>("lockerType");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("lockerType"));
        typeColumn.setCellValueFactory(column -> new SimpleStringProperty(column.getValue().getLockerType().toString()));

        TableColumn<Locker, String> zoneColumn = new TableColumn<>("Zone");
        zoneColumn.setCellValueFactory(new PropertyValueFactory<>("zone"));

        TableColumn<Locker, String> availableColumn = new TableColumn<>("Available");
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));

        TableColumn<Locker, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));


        TableColumn<Locker, Void> actionColumn = new TableColumn<>("จัดการ");
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final FilledButtonWithIcon infoBtn = FilledButtonWithIcon.small("ข้อมูลเพิ่มเติม", Icons.EDIT);
            private final FilledButtonWithIcon deleteBtn = FilledButtonWithIcon.small("ลบ", Icons.DELETE);
            {
                infoBtn.setOnAction(e -> infoLocker(getTableView().getItems().get(getIndex())));
                deleteBtn.setOnAction(e -> deleteLocker(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(5, infoBtn, deleteBtn));
            }
        });
        actionColumn.setPrefWidth(180);
        actionColumn.setStyle("-fx-alignment: CENTER;");

        lockersTableView.getColumns().clear();
        lockersTableView.getColumns().add(idColumn);
        lockersTableView.getColumns().add(typeColumn);
        lockersTableView.getColumns().add(zoneColumn);
        lockersTableView.getColumns().add(availableColumn);
        lockersTableView.getColumns().add(statusColumn);
        lockersTableView.getColumns().add(actionColumn);


        lockersTableView.getItems().clear();
        lockersTableView.getItems().addAll(lockers.getLockers());
        lockersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    private void infoLocker(Locker locker){
        datasourceRequest = new RequestListFileDatasource("data/requests","zone-"+zone.getZoneUid()+".json");
        requestList = datasourceRequest.readData();
        Request request = requestList.findRequestbyIdLocker(locker.getUuid());
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
            FXRouter.goTo("officer-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}