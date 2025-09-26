package ku.cs.controllers.user;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
    String uidzone;
    @FXML public void initialize() {
        uidzone = (String)FXRouter.getData();
        System.out.println("uidzone: " + uidzone);
        initDatasource();
        initUserInterface();
        initEvents();
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
                        showAlert(Alert.AlertType.ERROR, "ล็อกเกอร์ไม่พร้อมใช้งาน","ล็อกเกอร์ถูกใช้งานแล้ว");
                    }
                    else{
                        showAlert(Alert.AlertType.ERROR, "ล็อกเกอร์ไม่พร้อมใช้งาน","ล็อกเกอร์ชำรุด");
                    }
                }
            }
        });
    }

    private void initDatasource() {
        datasourceLocker =
                new LockerListFileDatasource(
                        "data/lockers",
                        "zone-" + uidzone + ".json"
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


        lockersTableView.getColumns().clear();
        lockersTableView.getColumns().add(idColumn);
        lockersTableView.getColumns().add(typeColumn);
        lockersTableView.getColumns().add(zoneColumn);
        lockersTableView.getColumns().add(availableColumn);
        lockersTableView.getColumns().add(statusColumn);


        lockersTableView.getItems().clear();
        lockersTableView.getItems().addAll(lockers.getLockers());
        lockersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    protected void backButtonOnclick() {
        try {
            FXRouter.goTo("user-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
