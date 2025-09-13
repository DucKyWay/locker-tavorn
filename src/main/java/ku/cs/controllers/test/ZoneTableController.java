package ku.cs.controllers.test;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.LockerListHardCodeDatasource;

public class ZoneTableController {
    @FXML  private TableView<ZoneList> zonelistTableView;
    @FXML  private HBox backButtonContainer;
    @FXML private HBox lockerListButtonContainer;

    private DefaultButton backButton;
    private DefaultLabel headerLabel;

    private ZoneList zoneList;
    private LockerListHardCodeDatasource datasource;
}
