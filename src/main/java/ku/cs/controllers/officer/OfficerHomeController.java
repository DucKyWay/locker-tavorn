package ku.cs.controllers.officer;

import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.components.Icons;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.models.account.*;
import ku.cs.models.comparator.RequestTimeComparator;
import ku.cs.models.key.KeyList;
import ku.cs.models.locker.*;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.request.RequestType;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.context.AppContext;
import ku.cs.services.datasources.provider.KeyDatasourceProvider;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;
import ku.cs.services.datasources.provider.RequestDatasourceProvider;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.request.RequestService;
import ku.cs.services.session.SelectedDayService;
import ku.cs.services.session.SessionManager;
import ku.cs.services.accounts.strategy.OfficerAccountProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.zone.ZoneService;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;

public class OfficerHomeController {
    private final SessionManager sessionManager = AppContext.getSessionManager();
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final RequestDatasourceProvider requestsProvider = new RequestDatasourceProvider();
    private final LockerDatasourceProvider lockersProvider = new LockerDatasourceProvider();
    private final KeyDatasourceProvider keysProvider = new KeyDatasourceProvider();
    protected final OfficerAccountProvider officersProvider = new OfficerAccountProvider();
    private final SelectedDayService selectedDayService = new SelectedDayService();
    @FXML private VBox officerHomeLabelContainer;

    private DefaultLabel officerHomeLabel;
    private DefaultButton lockerListButton;
    @FXML private TableView requestTableView;
    private final AlertUtil alertUtil = new AlertUtil();
    //test DateList
    RequestService requestService = new RequestService();
    private KeyList keyList;

    //test intitial Zone
    private ZoneList zoneList;
    private RequestList requestList;

    private Account account;
    private OfficerList officerList;
    private LockerList lockerList;
    private Officer officer;

    private final ZoneService zoneService = new ZoneService();
    private Zone currentzone;

    @FXML
    public void initialize() {
        // Auth Guard
        sessionManager.requireOfficerLogin();
        officer = sessionManager.getOfficer();
        currentzone = (Zone) FXRouter.getData();
        requestService.updateData();
        initialDatasource();
        initUserInterface();
        initEvents();
        showTable(requestList);
    }

    private void initialDatasource(){
        /* ========== Zone ========== */
        zoneList = zonesProvider.loadCollection();
        zoneService.setLockerToZone(zoneList);

        Zone officerZone = zoneList.findZoneByUid(officer.getZoneUids().get(0));

        keyList = keysProvider.loadCollection(currentzone.getZoneUid());
        lockerList = lockersProvider.loadCollection(currentzone.getZoneUid());

        /* ========== Request ========== */
        requestList = requestsProvider.loadCollection(currentzone.getZoneUid());
        Collections.sort(requestList.getRequestList(), new RequestTimeComparator());

        /* ========== Locker Date ========== */
    }

    private void initUserInterface() {
        officerHomeLabel = DefaultLabel.h2("Home | Officer " + officer.getUsername());
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
        TableColumn<Request, String> idLocker = new TableColumn<>("เลขประจำล็อกเกอร์");
        TableColumn<Request, String> startDateColumn = new TableColumn<>("เริ่มการจอง");
        TableColumn<Request, String> endDateColumn = new TableColumn<>("สิ้นสุดการจอง");
        TableColumn<Request, Role> userNameColumn = new TableColumn<>("ชื่อผู้จอง");
        TableColumn<Request, String> TypeLockerColumn = new TableColumn<>("ประเภทล็อกเกอร์");
        TableColumn<Request, String> zoneColumn = new TableColumn<>("โซน");
        TableColumn<Request, LocalDateTime> requestTimeColumn = new TableColumn<>("เวลาเข้าถึงล่าสุด");
        TableColumn<Request, Void> actionColumn = new TableColumn<>("จัดการ");

        uuidColumn.setCellValueFactory(new PropertyValueFactory<>("requestUid"));

        requestTypeColumn.setCellValueFactory(new PropertyValueFactory<>("requestType"));
        requestTypeColumn.setCellFactory(column -> new TableCell<Request, RequestType>() {
            @Override
            protected void updateItem(RequestType item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDescription()); //use enum description
                }
            }
        });



        idLocker.setCellValueFactory(cellData -> {
            Request request = cellData.getValue();
            String lockerId = "ไม่พบล็อกเกอร์";

            for (Locker l : lockerList.getLockers()) {
                if (l.getUid().equals(request.getLockerUid())) {
                    lockerId = String.valueOf(l.getId()); // แปลง int เป็น String
                    break;
                }
            }

            return new javafx.beans.property.SimpleStringProperty(lockerId);
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
        Callback<TableColumn<Request, Void>, TableCell<Request, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Request, Void> call(final TableColumn<Request, Void> param) {
                return new TableCell<>() {
                    private final FilledButtonWithIcon approveBtn = FilledButtonWithIcon.small("อนุมัติ", Icons.APPROVE);
                    private final FilledButtonWithIcon RejectBtn = FilledButtonWithIcon.small("ปฎิเสธ", Icons.REJECT);
                    {
                        approveBtn.setOnAction(e -> {
                            Request request = getTableView().getItems().get(getIndex());
                            Locker locker = lockerList.findLockerByUuid(request.getLockerUid());
                            if(locker.isAvailable()) {
                                if (locker.getLockerType() == LockerType.MANUAL) {
                                    try {
                                        FXRouter.loadDialogStage("officer-select-key-list", request);
                                    } catch (IOException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                } else {
                                    try {
                                        FXRouter.loadDialogStage("officer-passkey-digital", request);
                                    } catch (IOException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                }
                                requestTableView.refresh();
                            }else{
                                //ต้องบอก request ว่าล็อกเกอร์ไม่ว่างแล้ว แล้วต้องสร้าง messenger บอกว่า ตู้ถูกจองไปแล้ว โดยอัตโนมัติ
                            }
                        });
                        RejectBtn.setOnAction(event -> {
                            Request request = getTableView().getItems().get(getIndex());
                            try {
                                FXRouter.loadDialogStage("officer-message-reject",request);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        });

                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Request request = getTableView().getItems().get(getIndex());
                            if (request.getRequestType() != RequestType.PENDING) {
                                approveBtn.setDisable(true);
                                RejectBtn.setDisable(true);
                            } else {
                                approveBtn.setDisable(false);
                                RejectBtn.setDisable(false);
                            }

                            HBox hbox = new HBox(5, approveBtn, RejectBtn);
                            setGraphic(hbox);
                        }
                    }

                };
            }
        };

        actionColumn.setCellFactory(cellFactory);
        requestTableView.getColumns().clear();
        requestTableView.getColumns().addAll(uuidColumn, requestTypeColumn, idLocker,TypeLockerColumn, startDateColumn, endDateColumn, userNameColumn, zoneColumn, requestTimeColumn,actionColumn);
        requestTableView.getItems().clear();
        for (Request req : requestList.getRequestList()) {
            if (selectedDayService.isBooked(req.getStartDate(), req.getEndDate())) {
                requestTableView.getItems().add(req);
            }
        }

    }

    @FXML
    protected void onBackClick(){
        try {
            FXRouter.goTo("officer-zone-list");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
