package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.components.button.IconButton;
import ku.cs.controllers.components.AdminNavbarController;
import ku.cs.models.account.Account;
import ku.cs.models.account.User;
import ku.cs.models.account.UserList;
import ku.cs.models.comparator.LoginTimeComparator;
import ku.cs.services.AppContext;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.UserListFileDatasource;
import ku.cs.services.utils.AlertUtil;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;

public class AdminManageUsersController {
    private final SessionManager sessionManager = AppContext.getSessionManager();
    private final AlertUtil alertUtil = new AlertUtil();

    private static final int PROFILE_SIZE = 40;
    private static final String DEFAULT_AVATAR = "/ku/cs/images/default_profile.png";

    @FXML private HBox parentHBoxFilled;
    private Label headerLabel;
    private Label descriptionLabel;

    @FXML private TableView<User> userlistTableView;

    @FXML private AdminNavbarController adminNavbarController;
    private Button footerNavBarButton;

    private Datasource<UserList> userdatasource;
    private UserList userlist;
    private Account current;

    @FXML
    public void initialize() {
        sessionManager.requireAdminLogin();
        current = sessionManager.getCurrentAccount();

        footerNavBarButton = adminNavbarController.getFooterNavButton();

        initDatasource();
        initUserInterface();
        initEvents();
    }

    private void initDatasource() {
        userdatasource = new UserListFileDatasource("data", "test-user-data.json");
        userlist = userdatasource.readData(); // ให้ return เป็น List<User> จริง ๆ
        Collections.sort(userlist.getUsers(), new LoginTimeComparator());
    }

    private void initUserInterface() {
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

        showTable(userlist);
    }

    private void initEvents() {
        footerNavBarButton.setOnAction(e -> onBackButtonClick());
    }

    private void showTable(UserList userlist) {

        userlistTableView.getColumns().setAll(
                createProfileColumn(),
                createTextColumn("ชื่อผู้ใช้", "username"),
                createTextColumn("ชื่อ", "fullName"),
                createTextColumn("เบอร์มือถือ", "phone"),
                createSuspendColumn(),
                createLastLoginColumn(),
                createActionColumn()
        );

        userlistTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        userlistTableView.getItems().setAll(userlist.getUsers());
    }

    private <T> TableColumn<User, T> createTextColumn(String title, String property) {
        TableColumn<User, T> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        return col;
    }

    private <T> TableColumn<User, T> createTextColumn(String title, String property, double minWidth, String style) {
        TableColumn<User, T> col = createTextColumn(title, property);
        if (minWidth > 0) col.setMinWidth(minWidth);
        if (style != null) col.setStyle(style);
        return col;
    }

    private TableColumn<User, String> createProfileColumn() {
        TableColumn<User, String> profileColumn = new TableColumn<>();
        profileColumn.setCellValueFactory(new PropertyValueFactory<>("imagePath"));

        profileColumn.setCellFactory(col -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);

                if (empty) {
                    setGraphic(null);
                    return;
                }

                Image image;
                try {
                    if (imagePath != null && !imagePath.isBlank()) {
                        image = new Image("file:" + imagePath, PROFILE_SIZE, PROFILE_SIZE, true, true);
                        if (image.isError()) throw new Exception("Invalid image");
                    } else {
                        throw new Exception("No imagePath");
                    }
                } catch (Exception e) {
                    // Default
                    image = new Image(
                            getClass().getResource(DEFAULT_AVATAR).toExternalForm(),
                            PROFILE_SIZE, PROFILE_SIZE, true, true
                    );
                }

                imageView.setImage(image);
                imageView.setFitWidth(PROFILE_SIZE);
                imageView.setFitHeight(PROFILE_SIZE);

                // clip เป็นวงกลม
                Circle clip = new Circle(PROFILE_SIZE / 2.0, PROFILE_SIZE / 2.0, PROFILE_SIZE / 2.0);
                imageView.setClip(clip);

                setGraphic(imageView);
            }
        });

        profileColumn.setPrefWidth(60);
        profileColumn.setStyle("-fx-alignment: CENTER;");
        return profileColumn;
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
        col.setCellValueFactory(new PropertyValueFactory<>("logintime"));
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
        TableColumn<User, Void> actionColumn = new TableColumn<>("จัดการ");
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final FilledButtonWithIcon suspendBtn = FilledButtonWithIcon.small("เปลี่ยนสถานะ", Icons.SUSPEND);
            private final IconButton infoBtn = new IconButton(new Icon(Icons.EYE));
            private final IconButton deleteBtn = IconButton.error(new Icon(Icons.DELETE));

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                User user = getTableRow().getItem();

                suspendBtn.setOnAction(e -> toggleSuspend(user));
                infoBtn.setOnAction(e -> userInfo(user));
                deleteBtn.setOnAction(e -> deleteUser(user));

                infoBtn.setDisable(true);

                setGraphic(empty ? null : new HBox(5, suspendBtn, infoBtn, deleteBtn));
            }
        });

        actionColumn.setMinWidth(210);
        actionColumn.setStyle("-fx-alignment: CENTER;");
        return actionColumn;
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
        alertUtil.confirm(
                "Warning",
                "Do you want to remove " + user.getUsername() + "?"
        ).ifPresent(response -> {
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
