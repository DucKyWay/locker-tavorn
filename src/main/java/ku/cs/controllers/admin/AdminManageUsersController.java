package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import ku.cs.components.Icons;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.FilledButton;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.models.account.Account;
import ku.cs.models.account.User;
import ku.cs.models.account.UserList;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.UserListFileDatasource;
import ku.cs.services.utils.AlertUtil;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class AdminManageUsersController {
    @FXML private HBox navHBox;
    @FXML private Label headerLabel;
    @FXML private Button backButton;
    @FXML private TableView<User> userlistTableView;

    private Datasource<UserList> userdatasource;
    private UserList userlist;
    private Account current;

    @FXML
    public void initialize() {
        SessionManager.requireAdminLogin();
        current = SessionManager.getCurrentAccount();
        initDatasource();
        initUserInterface();
        initEvents();
        showTable(userlist);
    }

    private void initDatasource() {
        userdatasource = new UserListFileDatasource("data", "test-user-data.json");
        userlist = userdatasource.readData(); // ให้ return เป็น List<User> จริง ๆ
        List<User> users = userlist.getUsers();
        Collections.sort(users, (o1, o2) -> {
            LocalDateTime l1 = o1.getLogintime();
            LocalDateTime l2 = o2.getLogintime();

            // ถ้า null ให้ใช้เวลาที่ไกลมาก หรือเวลาปัจจุบันตามที่ต้องการ
            if (l1 == null && l2 == null) return 0;
            if (l1 == null) return 1; // คนไม่มี logintime ไปหลังสุด
            if (l2 == null) return -1; // คนไม่มี logintime ไปหลังสุด

            Duration duration1 = Duration.between(l1, LocalDateTime.now());
            Duration duration2 = Duration.between(l2, LocalDateTime.now());
            return Long.compare(duration1.getSeconds(), duration2.getSeconds());
        });
        userlist = new UserList();
        for (User u : users) {
            userlist.addUser(u);
        }
    }

    private void initUserInterface() {
        Region region = new Region();
        region.setPrefSize(450, 0);

        headerLabel = new Label("User List");
        LabelStyle.TITLE_LARGE.applyTo(headerLabel);

        backButton = new FilledButton("Back");

        navHBox.getChildren().addAll(backButton, region, headerLabel);
    }

    private void initEvents() {
        backButton.setOnAction(e -> onBackButtonClick());
    }

    private void showTable(UserList userlist) {
        userlistTableView.getColumns().clear();

        TableColumn<User,String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User,String> nameColumn = new TableColumn<>("name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<User,String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User,String> telphoneColumn = new TableColumn<>("telphone");
        telphoneColumn.setCellValueFactory(new PropertyValueFactory<>("telphone"));

        TableColumn<User,Boolean> suspendedColumn = new TableColumn<>("suspended");
        suspendedColumn.setCellValueFactory(new PropertyValueFactory<>("suspend"));

        TableColumn<User, Void> actionColumn = new TableColumn<>("จัดการ");

        TableColumn<User, LocalDateTime> logintimeColumn = new TableColumn<>("logintime");
        logintimeColumn.setCellValueFactory(new PropertyValueFactory<>("logintime"));
        logintimeColumn.setCellFactory(column -> new TableCell<User, LocalDateTime>() {
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

        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                return new TableCell<>() {
                    private final FilledButtonWithIcon suspendBtn = FilledButtonWithIcon.small("เปลี่ยนสถานะ", Icons.SUSPEND);
                    private final FilledButtonWithIcon editBtn = FilledButtonWithIcon.small("แก้ไข", Icons.EDIT);
                    private final FilledButtonWithIcon deleteBtn = FilledButtonWithIcon.small("ลบ", Icons.DELETE);

                    {
                        suspendBtn.setOnAction(e -> {
                            User user = getTableView().getItems().get(getIndex());
                            user.toggleSuspend();
                            userdatasource.writeData(userlist);
                            AlertUtil.info("เปลี่ยนแปลงสถานะสำเร็จ", user.getUsername() + " ได้เปลี่ยนสถานะเป็น " + user.getSuspend());
                            showTable(userlist);
                        });

                        editBtn.setOnAction(event -> {
                            User user = getTableView().getItems().get(getIndex());
                            try {
                                FXRouter.goTo("admin-manage-user-details", user);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });

                        deleteBtn.setOnAction(event -> {
                            // TODO: delete user
                        });

                        editBtn.setDisable(true);
                        deleteBtn.setDisable(true);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox hbox = new HBox(5, suspendBtn, editBtn, deleteBtn);
                            setGraphic(hbox);
                        }
                    }
                };
            }
        };

        actionColumn.setCellFactory(cellFactory);

        userlistTableView.getColumns().clear();
        userlistTableView.getColumns().add(usernameColumn);
        userlistTableView.getColumns().add(nameColumn);
        userlistTableView.getColumns().add(emailColumn);
        userlistTableView.getColumns().add(telphoneColumn);
        userlistTableView.getColumns().add(suspendedColumn);
        userlistTableView.getColumns().add(logintimeColumn);
        userlistTableView.getColumns().add(actionColumn);

        userlistTableView.getItems().clear();
        userlistTableView.getItems().addAll(userlist.getUsers());
    }

    private void onBackButtonClick() {
        try {
            FXRouter.goTo("admin-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
