package ku.cs.controllers.user;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.models.account.Account;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.UpdateZoneService;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.ZoneListFileDatasource;

import java.io.IOException;

public class ZoneTableController {

    @FXML private TableView<Zone> zonelistTableView;
    @FXML private HBox backButtonContainer;
    @FXML private HBox headerLabelContainer;

    private DefaultButton backButton;
    private DefaultLabel headerLabel;

    private ZoneList zoneList;
    private Datasource<ZoneList> datasource;
    private Account current;

    @FXML
    public void initialize() {
        // Auth Guard
        SessionManager.requireUserLogin();
        current = SessionManager.getCurrentAccount();

        initialDatasourceZone();
        initUserInterface();
        initEvents();
        showTable(zoneList);

        zonelistTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Zone>() {
            @Override
            public void changed(ObservableValue<? extends Zone> observableValue, Zone oldzone, Zone newzone) {
                if(newzone != null){
                    if(newzone.getStatus().equals("Not Active")){
                        showAlert(Alert.AlertType.ERROR, "Zone Not Active", "Please try again later.");
                    }
                    else{
                        try{
                            FXRouter.goTo("locker-list", newzone.getIdZone());
                        }
                        catch (IOException e){
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
    }

    private void initialDatasourceZone() {
        datasource = new ZoneListFileDatasource("data", "test-zone-data.json");
        zoneList = datasource.readData();

        UpdateZoneService.setLockerToZone(zoneList);
    }

    private void initUserInterface() {
        headerLabel = DefaultLabel.h2("Zone List");
        backButton = DefaultButton.primary("Back");
        backButtonContainer.getChildren().add(backButton);
        headerLabelContainer.getChildren().add(headerLabel);
    }

    private void initEvents() {
        backButton.setOnAction(e -> onBackButtonClick());
    }

    private void onBackButtonClick() {
        try {
            FXRouter.goTo("user-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showTable(ZoneList zoneList) {
        zonelistTableView.getColumns().clear();

        TableColumn<Zone, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idZone"));

        TableColumn<Zone, String> zoneColumn = new TableColumn<>("ชื่อโซน");
        zoneColumn.setCellValueFactory(new PropertyValueFactory<>("zone"));

        TableColumn<Zone, Integer> totalLockerColumn = new TableColumn<>("จำนวนล็อกเกอร์ทั้งหมด");
        totalLockerColumn.setCellValueFactory(new PropertyValueFactory<>("totalLocker"));

        TableColumn<Zone, Integer> totalAvailableNowColumn = new TableColumn<>("จำนวนล็อกเกอร์ว่างในตอนนี้");
        totalAvailableNowColumn.setCellValueFactory(new PropertyValueFactory<>("totalAvailableNow"));

        TableColumn<Zone, Integer> totalAvailableColumn = new TableColumn<>("จำนวนล็อกเกอร์ที่สามารถใช้งานได้");
        totalAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("totalAvailable"));

        TableColumn<Zone, String> statusColumn = new TableColumn<>("สถานะ");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        zonelistTableView.getColumns().clear();
        zonelistTableView.getColumns().add(idColumn);
        zonelistTableView.getColumns().add(zoneColumn);
        zonelistTableView.getColumns().add(totalLockerColumn);
        zonelistTableView.getColumns().add(totalAvailableNowColumn);
        zonelistTableView.getColumns().add(totalAvailableColumn);
        zonelistTableView.getColumns().add(statusColumn);

        zonelistTableView.getItems().clear();
        zonelistTableView.getItems().addAll(zoneList.getZones());
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
