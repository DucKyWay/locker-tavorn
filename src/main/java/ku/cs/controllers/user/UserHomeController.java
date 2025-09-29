package ku.cs.controllers.user;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.account.Account;
import ku.cs.models.account.Role;
import ku.cs.models.comparator.RequestTimeComparator;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.request.RequestType;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.AppContext;
import ku.cs.services.FXRouter;
import ku.cs.services.RequestService;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.RequestListFileDatasource;
import ku.cs.services.datasources.ZoneListFileDatasource;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;

public class UserHomeController {
    private final SessionManager sessionManager = AppContext.getSessionManager();

    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;
    @FXML private TableView<Request> requestListTableView;
    private Datasource<RequestList> requestListDatasource;
    private RequestList requestList;
    private Datasource<ZoneList> zoneListDatasource;
    private ZoneList zoneList;
    private RequestList currentRequestList;
    Account current;
    RequestService requestService = new RequestService();

    @FXML
    public void initialize() {
        // Auth Guard
        sessionManager.requireUserLogin();
        current = sessionManager.getCurrentAccount();
        requestService.updateData();
        initialDatasourceZone();
        initUserInterface();
        initEvents();
        showTable(requestList);

        requestListTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Request>() {
            @Override
            public void changed(ObservableValue<? extends Request> observableValue, Request oldRequest, Request newRequest) {
                if(newRequest !=null){
                    try {
                        FXRouter.loadDialogStage("locker-dialog", newRequest);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println("Hello");
            }
        });
    }

    private void initialDatasourceZone(){
        zoneListDatasource = new ZoneListFileDatasource("data","test-zone-data.json");
        zoneList = zoneListDatasource.readData();
        currentRequestList = new RequestList();

        for(Zone zone : zoneList.getZones()){
            requestListDatasource =  new RequestListFileDatasource("data/requests","zone-"+zone.getZoneUid()+".json");
            requestList = requestListDatasource.readData();
            for(Request request : requestList.getRequestList()){
                if(current.getUsername().equals(request.getUserUsername())){
                    currentRequestList.addRequest(request);
                }
            }
        }
        Collections.sort(currentRequestList.getRequestList(),new RequestTimeComparator());
    }

    private void showTable(RequestList requestList) {
        TableColumn<Request, String> uuidColumn = new TableColumn<>("uuid");
        uuidColumn.setMinWidth(55);
        TableColumn<Request, RequestType> requestTypeColumn = new TableColumn<>("สถานะการจอง");
        requestTypeColumn.setMinWidth(120);
        TableColumn<Request, String> startDateColumn = new TableColumn<>("เริ่มการจอง");
        startDateColumn.setMinWidth(112);
        TableColumn<Request, String> endDateColumn = new TableColumn<>("สิ้นสุดการจอง");
        endDateColumn.setMinWidth(112);
        TableColumn<Request, Role> userNameColumn = new TableColumn<>("ผู้จอง");
        endDateColumn.setMinWidth(77);
        TableColumn<Request, String> zoneColumn = new TableColumn<>("โซน");
        zoneColumn.setMinWidth(57);
        TableColumn<Request, LocalDateTime> requestTimeColumn = new TableColumn<>("เวลาเข้าถึงล่าสุด");
        requestTimeColumn.setMinWidth(200);

        uuidColumn.setCellValueFactory(new PropertyValueFactory<>("zoneUid"));
        requestTypeColumn.setCellValueFactory(new PropertyValueFactory<>("requestType"));
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
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userUsername"));
        zoneColumn.setCellValueFactory(new PropertyValueFactory<>("zoneName"));
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

    }

    private void initEvents() {

    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
