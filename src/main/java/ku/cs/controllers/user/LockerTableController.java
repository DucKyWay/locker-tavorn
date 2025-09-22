package ku.cs.controllers.user;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.services.FXRouter;
import ku.cs.services.datasources.LockerListFileDatasource;

import java.io.IOException;

public class LockerTableController {
    @FXML private TableView<Locker> lockersTableView;

    @FXML private VBox selectLockerTypeDropdown;
    @FXML private VBox addLockerZoneDropdown;
    @FXML private HBox backButtonContainer;
    @FXML private HBox headerLabelContainer;

    private DefaultButton backButton;
    private DefaultLabel headerLabel;

    private LockerList lockers;
    private LockerListFileDatasource datasourceLocker;
    Integer idzone;
    @FXML public void initialize() {
        idzone = (Integer) FXRouter.getData();
        System.out.println("idzone: " + idzone);
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
                        "zone-" +idzone+ ".json"
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

        TableColumn<Locker, String> typeColumn = new TableColumn<>("KeyType");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("keyType"));

        TableColumn<Locker, String> zoneColumn = new TableColumn<>("Zone");
        zoneColumn.setCellValueFactory(new PropertyValueFactory<>("zone"));

        TableColumn<Locker, String> availableColumn = new TableColumn<>("Available");
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));

        TableColumn<Locker, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Locker, String> startColumn = new TableColumn<>("Start");
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<Locker, String> stopColumn = new TableColumn<>("End");
        stopColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        lockersTableView.getColumns().clear();
        lockersTableView.getColumns().add(idColumn);
        lockersTableView.getColumns().add(typeColumn);
        lockersTableView.getColumns().add(zoneColumn);
        lockersTableView.getColumns().add(availableColumn);
        lockersTableView.getColumns().add(statusColumn);
        lockersTableView.getColumns().add(startColumn);
        lockersTableView.getColumns().add(stopColumn);

        lockersTableView.getItems().clear();
        lockersTableView.getItems().addAll(lockers.getLockers());
    }

    protected void backButtonOnclick() {
        try {
            FXRouter.goTo("user-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
