package ku.cs.controllers.test;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.models.account.Account;
import ku.cs.models.locker.Locker;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.LockerListHardCodeDatasource;
import ku.cs.services.datasources.ZoneListFileDatasource;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ZoneTableController {
    @FXML  private TableView<ZoneList> zonelistTableView;
    @FXML  private HBox backButtonContainer;
    @FXML private HBox headerLabelContainer;

    private DefaultButton backButton;
    private DefaultLabel headerLabel;

    private ZoneList zoneList;
    private Datasource<ZoneList> datasource;
    Account current = SessionManager.getCurrentAccount();
    @FXML
    public void initialize() {
        // Auth Guard
        SessionManager.requireUserLogin();
        current = SessionManager.getCurrentAccount();

        initialDatasourceZone();
        initUserInterface();
        initEvents();
    }
    private void initialDatasourceZone(){
        datasource = new ZoneListFileDatasource("data", "test-zone-data.json");
        zoneList = datasource.readData();
        showTable(zoneList);

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
    void showTable(ZoneList zoneList){
        TableColumn<Locker, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

    }

}

