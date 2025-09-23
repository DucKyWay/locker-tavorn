package ku.cs.controllers.officer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.account.Officer;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.ZoneListFileDatasource;

import java.io.IOException;

public class OfficerTableZoneController {
    private Officer officer;
    @FXML TableView<Zone> zonelistTableView;

    Datasource<ZoneList> zoneListDatasource;
    private ZoneList zoneList;
    @FXML
    public void initialize() {
        officer = SessionManager.getOfficer();
        initialDatasource();
    }
    private void initialDatasource(){
        zoneListDatasource = new ZoneListFileDatasource("data", "test-zone-data.json");
        zoneList = zoneListDatasource.readData();
        showTable();
        getCurrentZoneList(zoneList);

        zonelistTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Zone>() {
            @Override
            public void changed(ObservableValue<? extends Zone> observableValue, Zone oldzone, Zone newzone) {
                if(newzone != null){
                    try{
                        FXRouter.goTo("officer-home",newzone);
                    }
                    catch (IOException e){
                        throw new RuntimeException(e);
                    }

                }
            }
        });

    }
    private void getCurrentZoneList(ZoneList zoneList){
        boolean check =false;
        for(String s : officer.getServiceZoneArray()){
            for(Zone zone : zoneList.getZones()){
                if(s.equals(zone.getZone())){
                    check =true;
                    zonelistTableView.getItems().add(zone);
                }
            }
        }
        if(!check){
            zonelistTableView.getItems().add(zoneList.findZoneByName(officer.getServiceZone()));
        }
    }
    private void showTable() {
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
    }

}
