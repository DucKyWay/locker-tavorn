package ku.cs.controllers.officer;

import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.controllers.components.SettingDropdownController;
import ku.cs.models.account.*;
import ku.cs.models.key.KeyList;
import ku.cs.models.locker.*;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.request.RequestType;
import ku.cs.models.request.date.LockerDate;
import ku.cs.models.request.date.LockerDateList;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.UpdateZoneService;
import ku.cs.services.datasources.*;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class OfficerHomeController {
    @FXML private VBox officerHomeLabelContainer;

    @FXML private SettingDropdownController settingsContainerController;
    @FXML private HBox backButtonContainer;
    @FXML private TextField zoneTextFieldContainer;
    @FXML private TextField lockerTextFieldContainer;
    private DefaultLabel officerHomeLabel;
    private DefaultButton lockerListButton;
    @FXML private TableView requestTableView;
    //test DateList
    private Datasource<LockerDateList> datasourceDateList;
    private LockerDateList dateList;

    private Datasource<KeyList> datasourceKeyList;
    private KeyList keyList;

    //test intitial Zone
    private Datasource<ZoneList> datasourceZone;
    private ZoneList zoneList;

    private Datasource<RequestList> datasourceRequest;
    private RequestList requestList;

    private Datasource<LockerList> datasourceLocker;
    private Datasource<OfficerList> datasourceOfficer;
    private Account account;
    private OfficerList officerList;
    private LockerList lockerList;
    private Officer officer;
    @FXML
    public void initialize() {
        // Auth Guard
        SessionManager.requireOfficerLogin();
        account = SessionManager.getCurrentAccount();
        initialDatasourceZone();
        initUserInterface();
        initEvents();
        initialDatasourceOfficerList();
        initialDatasourceLockerList();
        initialDatasourceDateList();
        initialDatasourceKeyList();
        initialDatasourceRequestList();
        showTable(requestList);
    }
    private void initialDatasourceDateList(){
        datasourceDateList = new DateListFileDatasource("data", "test-date-list-data.json");
        dateList = datasourceDateList.readData();
    }

    private void initialDatasourceZone(){
        datasourceZone = new ZoneListFileDatasource("data", "test-zone-data.json");
        zoneList = datasourceZone.readData();
        UpdateZoneService.setLockerToZone(zoneList);
    }

    private void initialDatasourceKeyList(){
        datasourceKeyList =
                new KeyListFileDatasource(
                        "data/keys",
                        "zone-" + zoneList.findZoneByName(officer.getServiceZone()).getIdZone() + ".json"
                );
        keyList = datasourceKeyList.readData();

    }
    private void initialDatasourceOfficerList(){
        datasourceOfficer = new OfficerListFileDatasource("data", "test-officer-data.json");
        officerList = datasourceOfficer.readData();
        officer = officerList.findOfficerByUsername(account.getUsername());
    }
    private void initialDatasourceLockerList(){
        datasourceLocker =
                new LockerListFileDatasource(
                        "data/lockers",
                        "zone-" + zoneList.findZoneByName(officer.getServiceZone()).getIdZone() + ".json"
                );
        lockerList = datasourceLocker.readData();

    }
    private void initialDatasourceRequestList(){
        datasourceRequest = new RequestListFileDatasource(
                "data/requests",
                "zone-" + zoneList.findZoneByName(officer.getServiceZone()).getIdZone() + ".json"
        );
        requestList = datasourceRequest.readData();
        List<Request> requests = requestList.getRequestList();
        Collections.sort(requests, (o1, o2) -> {
            LocalDateTime l1 = o1.getRequestTime();
            LocalDateTime l2 = o1.getRequestTime();

            // ถ้า null ให้ใช้เวลาที่ไกลมาก หรือเวลาปัจจุบันตามที่ต้องการ
            if (l1 == null && l2 == null) return 0;
            if (l1 == null) return 1; // คนไม่มี logintime ไปหลังสุด
            if (l2 == null) return -1; // คนไม่มี logintime ไปหลังสุด

            Duration duration1 = Duration.between(l1, LocalDateTime.now());
            Duration duration2 = Duration.between(l2, LocalDateTime.now());
            return Long.compare(duration1.getSeconds(), duration2.getSeconds());
        });
        requestList = new RequestList();
        for (Request r : requests) {
            requestList.addRequest(r);
        }

    }
    private void initUserInterface() {
        officerHomeLabel = DefaultLabel.h2("Home | Officer " + account.getUsername());
        lockerListButton = DefaultButton.primary("Locker List");
        officerHomeLabelContainer.getChildren().add(officerHomeLabel);
    }

    private void initEvents() {
        lockerListButton.setOnAction(e -> onLockerTableButtonClick());
    }

    protected void onLockerTableButtonClick() {
        try {
            FXRouter.goTo("locker-list");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void showTable(RequestList requestList) {
        TableColumn<Request, String> uuidColumn = new TableColumn<>("uuid");
        TableColumn<Request, RequestType> requestTypeColumn = new TableColumn<>("สถานะการจอง");
        TableColumn<Request, String> uuidLocker = new TableColumn<>("เลขประจำล็อกเกอร์");
        TableColumn<Request, String> startDateColumn = new TableColumn<>("เริ่มการจอง");
        TableColumn<Request, String> endDateColumn = new TableColumn<>("สิ้นสุดการจอง");
        TableColumn<Request, Role> userNameColumn = new TableColumn<>("ชื่อผู้จอง");
        TableColumn<Request, String> officerName = new TableColumn<>("ชื่อผู้จัดการรีเควส");
        TableColumn<Request, String> zoneColumn = new TableColumn<>("โซน");
        TableColumn<Request, LocalDateTime> requestTimeColumn = new TableColumn<>("เวลาเข้าถึงล่าสุด");


        uuidColumn.setCellValueFactory(new PropertyValueFactory<>("uuid"));
        requestTypeColumn.setCellValueFactory(new PropertyValueFactory<>("requestType"));
        uuidLocker.setCellValueFactory(new PropertyValueFactory<>("uuidLocker"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        officerName.setCellValueFactory(new PropertyValueFactory<>("officerName"));
        zoneColumn.setCellValueFactory(new PropertyValueFactory<>("zone"));
        requestTimeColumn.setCellValueFactory(new PropertyValueFactory<>("requestTime"));
        requestTimeColumn.setCellFactory(column -> new TableCell<Request, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // คำนวณระยะเวลาจากปัจจุบัน
                    Duration duration = Duration.between(item, LocalDateTime.now());

                    long seconds = duration.getSeconds();
                    String text;
                    if (seconds < 60) {
                        text = "ใช้งานล่าสุดเมื่อ " + seconds + " วินาทีที่แล้ว";
                    } else if (seconds < 3600) {
                        long minutes = seconds / 60;
                        text = "ใช้งานล่าสุดเมื่อ " + minutes + " นาทีที่แล้ว";
                    } else if (seconds < 86400) {
                        long hours = seconds / 3600;
                        text = "ใช้งานล่าสุดเมื่อ " + hours + " ชั่วโมงที่แล้ว";
                    } else {
                        long days = seconds / 86400;
                        text = "ใช้งานล่าสุดเมื่อ " + days + " วันที่แล้ว";
                    }
                    setText(text);
                }
            }
        });
        requestTableView.getColumns().clear();
        requestTableView.getColumns().addAll(uuidColumn, requestTypeColumn, uuidLocker, startDateColumn, endDateColumn, userNameColumn, officerName, zoneColumn, requestTimeColumn);
        requestTableView.getItems().addAll(requestList.getRequestList());


    }

    @FXML
    protected void onAddZoneClick(){
        String zone_string = zoneTextFieldContainer.getText();
        zoneList.addZone(zone_string);
        datasourceZone.writeData(zoneList);
        System.out.println("Current Zone List: " + zoneList.getZones());
    }
    @FXML
    protected void onAddLockerManual(){
        Locker locker = new Locker(KeyType.MANUAL,officer.getServiceZone());
        lockerList.addLocker(locker);
        LockerDate date = new LockerDate(locker.getUuid());
        dateList.addDateList(date);

        datasourceDateList.writeData(dateList);
        datasourceLocker.writeData(lockerList);
        UpdateZoneService.setLockerToZone(zoneList);
    }
    @FXML
    protected void onAddLockerChain(){
        Locker locker = new Locker(KeyType.CHAIN,officer.getServiceZone());
        lockerList.addLocker(locker);
        LockerDate date = new LockerDate(locker.getUuid());
        dateList.addDateList(date);

        datasourceDateList.writeData(dateList);
        datasourceLocker.writeData(lockerList);
        UpdateZoneService.setLockerToZone(zoneList);
    }
    @FXML
    protected void onAddLockerDigital(){
        Locker locker = new Locker(KeyType.DIGITAL,officer.getServiceZone());
        lockerList.addLocker(locker);
        LockerDate date = new LockerDate(locker.getUuid());
        dateList.addDateList(date);

        datasourceDateList.writeData(dateList);
        datasourceLocker.writeData(lockerList);
        UpdateZoneService.setLockerToZone(zoneList);
    }
    @FXML
    protected void onAddKeyChain(){
        try {
            FXRouter.goTo("officer-key-list",officer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onUserListButton(){
        try {
            FXRouter.goTo("user-list");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
