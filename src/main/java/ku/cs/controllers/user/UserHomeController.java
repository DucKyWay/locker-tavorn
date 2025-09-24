package ku.cs.controllers.user;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ku.cs.components.Icons;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.models.account.Account;
import ku.cs.models.account.Role;
import ku.cs.models.comparator.RequestTimeComparator;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.request.RequestType;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.RequestListFileDatasource;
import ku.cs.services.datasources.ZoneListFileDatasource;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;

public class UserHomeController {

    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;
    @FXML private TableView requestListTableView;
    private Datasource<RequestList> requestListDatasource;
    private RequestList requestList;
    private Datasource<ZoneList> zoneListDatasource;
    private ZoneList zoneList;
    private RequestList currentRequestList;
    Account current;

    @FXML
    public void initialize() {
        // Auth Guard
        SessionManager.requireUserLogin();
        current = SessionManager.getCurrentAccount();

        initialDatasourceZone();
        initUserInterface();
        initEvents();
        showTable(requestList);
    }
    private void initialDatasourceZone(){
        zoneListDatasource = new ZoneListFileDatasource("data","test-zone-data.json");
        zoneList = zoneListDatasource.readData();
        currentRequestList = new RequestList();

        for(Zone zone : zoneList.getZones()){
            requestListDatasource =  new RequestListFileDatasource("data/requests","zone-"+zone.getIdZone()+".json");
            requestList = requestListDatasource.readData();
            for(Request request : requestList.getRequestList()){
                if(current.getUsername().equals(request.getUserName())){
                    currentRequestList.addRequest(request);
                }
            }
        }
        Collections.sort(currentRequestList.getRequestList(),new RequestTimeComparator());
    }

    private void showTable(RequestList requestList) {
        TableColumn<Request, String> uuidColumn = new TableColumn<>("uuid");
        TableColumn<Request, RequestType> requestTypeColumn = new TableColumn<>("สถานะการจอง");

        TableColumn<Request, String> startDateColumn = new TableColumn<>("เริ่มการจอง");
        TableColumn<Request, String> endDateColumn = new TableColumn<>("สิ้นสุดการจอง");
        TableColumn<Request, Role> userNameColumn = new TableColumn<>("ชื่อผู้จอง");
        TableColumn<Request, String> zoneColumn = new TableColumn<>("โซน");
        TableColumn<Request, LocalDateTime> requestTimeColumn = new TableColumn<>("เวลาเข้าถึงล่าสุด");

        uuidColumn.setCellValueFactory(new PropertyValueFactory<>("uuid"));
        requestTypeColumn.setCellFactory(column -> new TableCell<Request, RequestType>() {
            @Override
            protected void updateItem(RequestType item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    switch (item) {
                        case APPROVE -> setText("คำขออนุมัติ");
                        case PENDING -> setText("คำขอรออนุมัติ");
                        case REJECT -> setText("คำขอถูกปฏิเสธ");
                    }
                }
            }
        });
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        zoneColumn.setCellValueFactory(new PropertyValueFactory<>("zone"));
        requestTimeColumn.setCellValueFactory(new PropertyValueFactory<>("requestTime"));
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
        requestListTableView.getColumns().clear();
        requestListTableView.getColumns().addAll(uuidColumn, requestTypeColumn, startDateColumn, endDateColumn, userNameColumn, zoneColumn, requestTimeColumn);
        requestListTableView.getItems().clear();
        requestListTableView.getItems().addAll(currentRequestList.getRequestList());
    }

    private void initUserInterface() {
//        LabelStyle._LARGE.applyTo(titleLabel);
//        LabelStyle.BODY_MEDIUM.applyTo(descriptionLabel);
    }

    private void initEvents() {

    }

    protected void onHomeButtonClick() {
        try {
            FXRouter.goTo("test-zonelist");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
