package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.components.button.IconButton;
import ku.cs.models.account.User;
import ku.cs.models.account.UserList;
import ku.cs.models.comparator.LoginTimeComparator;
import ku.cs.services.FXRouter;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.UserListFileDatasource;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.TableColumnFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;

public class AdminManageUsersController extends BaseAdminController {
    private final AlertUtil alertUtil = new AlertUtil();

    private static final int PROFILE_SIZE = 40;

    @FXML private HBox parentHBoxFilled;
    @FXML private TableView<User> userlistTableView;

    private Label headerLabel;
    private Label descriptionLabel;

    private Datasource<UserList> userdatasource;
    private UserList userlist;

    @Override
    protected void initDatasource() {
        userdatasource = new UserListFileDatasource("data", "test-user-data.json");
        userlist = userdatasource.readData();
        Collections.sort(userlist.getUsers(), new LoginTimeComparator());
    }

    @Override
    protected void initUserInterfaces() {
        Region region = new Region();
        VBox vBox = new VBox();

        parentHBoxFilled.setSpacing(4);
        region.setPrefSize(620, 50);

        headerLabel = new Label("จัดการผู้ใช้งาน");
        descriptionLabel = new Label("ด้วย " + current.getUsername());

        LabelStyle.TITLE_LARGE.applyTo(headerLabel);
        LabelStyle.TITLE_SMALL.applyTo(descriptionLabel);

        vBox.getChildren().addAll(headerLabel, descriptionLabel);
        parentHBoxFilled.getChildren().addAll(vBox, region);

        if (footerNavBarButton != null) {
            footerNavBarButton.setText("ย้อนกลับ");
        }

        showTable(userlist);
    }

    @Override
    protected void initEvents() {
        if (footerNavBarButton != null) {
            footerNavBarButton.setOnAction(e -> onBackButtonClick());
        }
    }

    private void showTable(UserList userlist) {
        userlistTableView.getColumns().setAll(
                TableColumnFactory.createProfileColumn(PROFILE_SIZE),
                TableColumnFactory.createTextColumn("ชื่อผู้ใช้", "username"),
                TableColumnFactory.createTextColumn("ชื่อ", "fullName"),
                TableColumnFactory.createTextColumn("เบอร์มือถือ", "phone"),
                createSuspendColumn(),
                createLastLoginColumn(),
                createActionColumn()
        );

        userlistTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        userlistTableView.getItems().setAll(userlist.getUsers());
    }

    private TableColumn<User, Boolean> createSuspendColumn() {
        TableColumn<User, Boolean> col = new TableColumn<>("สถานะ");
        col.setCellValueFactory(new PropertyValueFactory<>("suspend"));
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean suspended, boolean empty) {
                super.updateItem(suspended, empty);
                if (empty || suspended == null) {
                    setText(null);
                } else {
                    setText(suspended ? "ถูกระงับ" : "ปกติ");
                }
            }
        });
        return col;
    }

    private TableColumn<User, LocalDateTime> createLastLoginColumn() {
        TableColumn<User, LocalDateTime> col = new TableColumn<>("ใช้งานล่าสุด");
        col.setCellValueFactory(new PropertyValueFactory<>("loginTime"));
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime time, boolean empty) {
                super.updateItem(time, empty);
                if (empty || time == null) {
                    setText(null);
                } else {
                    setText(formatLastLogin(time));
                }
            }
        });
        return col;
    }

    private TableColumn<User, Void> createActionColumn() {
        return TableColumnFactory.createActionColumn("จัดการ", user -> {
            FilledButtonWithIcon suspendBtn = FilledButtonWithIcon.small("เปลี่ยนสถานะ", Icons.SUSPEND);
            IconButton infoBtn = new IconButton(new Icon(Icons.EYE));
            IconButton deleteBtn = IconButton.error(new Icon(Icons.DELETE));

            suspendBtn.setOnAction(e -> toggleSuspend(user));
            infoBtn.setOnAction(e -> userInfo(user));
            deleteBtn.setOnAction(e -> deleteUser(user));

            infoBtn.setDisable(true);

            return new Button[]{suspendBtn, infoBtn, deleteBtn};
        });
    }


    private void toggleSuspend(User user) {
        user.toggleSuspend();
        userdatasource.writeData(userlist);
        alertUtil.info("เปลี่ยนแปลงสถานะสำเร็จ",
                user.getUsername() + " ได้เปลี่ยนสถานะเป็น " + formatSuspended(user.getSuspend()));
        showTable(userlist);
    }

    private void userInfo(User user) {
        try {
            FXRouter.goTo("admin-manage-user-details", user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteUser(User user) {
        alertUtil.confirm("Warning", "Do you want to remove " + user.getUsername() + "?")
                .ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        userlist.removeUser(user);
                        userdatasource.writeData(userlist);
                        showTable(userlist);
                    }
                });
    }

    private String formatSuspended(boolean suspended) {
        return (suspended ? "ถูกระงับ" : "ปกติ");
    }

    private String formatLastLogin(LocalDateTime time) {
        Duration duration = Duration.between(time, LocalDateTime.now());
        long seconds = duration.getSeconds();

        if (seconds < 60) return seconds + " วินาทีที่แล้ว";
        if (seconds < 3600) return (seconds / 60) + " นาทีที่แล้ว";
        if (seconds < 86400) return (seconds / 3600) + " ชั่วโมงที่แล้ว";
        return (seconds / 86400) + " วันที่แล้ว";
    }

    private void onBackButtonClick() {
        try {
            FXRouter.goTo("admin-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
