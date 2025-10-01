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
import ku.cs.models.account.*;
import ku.cs.models.comparator.LoginTimeComparator;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.accounts.strategy.UserAccountProvider;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.SearchService;
import ku.cs.services.utils.TableColumnFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class AdminManageUsersController extends BaseAdminController {
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();
    private final UserAccountProvider usersProvider = new UserAccountProvider();
    private final SearchService<User> searchService = new SearchService<>();
    private final AlertUtil alertUtil = new AlertUtil();

    private static final int PROFILE_SIZE = 40;

    @FXML private HBox parentHBoxFilled;
    @FXML private TableView<User> userlistTableView;

    private TextField searchTextField;
    private Button searchButton;

    private UserList userlist;

    @Override
    protected void initDatasource() {
        userlist = usersProvider.loadCollection();
        Collections.sort(userlist.getUsers(), new LoginTimeComparator());
    }

    @Override
    protected void initUserInterfaces() {
        Region region = new Region();
        VBox titleVBox = new VBox();
        HBox searchBarHBox = new HBox();

        parentHBoxFilled.setSpacing(4);
        region.setPrefSize(310, 50);

        Label headerLabel = new Label("จัดการผู้ใช้งาน");
        Label descriptionLabel = new Label("ด้วย " + current.getUsername());
        searchTextField = new TextField();
        searchButton = new IconButton(new Icon(Icons.MAGNIFYING_GLASS));

        LabelStyle.TITLE_LARGE.applyTo(headerLabel);
        LabelStyle.TITLE_SMALL.applyTo(descriptionLabel);
        searchTextField.setPromptText("ค้นหาจากบางส่วนของชื่อ");
        searchTextField.setPrefWidth(300);

        searchBarHBox.getChildren().addAll(searchTextField, searchButton);
        titleVBox.getChildren().addAll(headerLabel, descriptionLabel);
        parentHBoxFilled.getChildren().addAll(titleVBox, region, searchBarHBox);

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

        searchTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            onSearch();
        });
        searchButton.setOnAction(e -> onSearch());
    }

    private void showTable(UserList userlist) {
        userlistTableView.getColumns().setAll(
                tableColumnFactory.createProfileColumn(PROFILE_SIZE),
                tableColumnFactory.createTextColumn("ชื่อผู้ใช้", "username"),
                tableColumnFactory.createTextColumn("ชื่อ", "fullName"),
                tableColumnFactory.createTextColumn("เบอร์มือถือ", "phone"),
                tableColumnFactory.createStatusColumn("สถานะ", "status", "ปกติ", "ถูกระงับ"),
                createLastLoginColumn(),
                createActionColumn()
        );

        userlistTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        userlistTableView.getItems().setAll(userlist.getUsers());
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
        col.setStyle("-fx-alignment: CENTER;");
        return col;
    }

    private TableColumn<User, Void> createActionColumn() {
        return tableColumnFactory.createActionColumn("จัดการ", user -> {
            FilledButtonWithIcon suspendBtn = FilledButtonWithIcon.small("เปลี่ยนสถานะ", Icons.SUSPEND);
            IconButton infoBtn = new IconButton(new Icon(Icons.EYE));
            IconButton deleteBtn = IconButton.error(new Icon(Icons.DELETE));

            suspendBtn.setOnAction(e -> toggleStatus(user));
            infoBtn.setOnAction(e -> userInfo(user));
            deleteBtn.setOnAction(e -> deleteUser(user));

            infoBtn.setDisable(true);

            return new Button[]{suspendBtn, infoBtn, deleteBtn};
        });
    }


    private void toggleStatus(User user) {
        user.toggleStatus();
        usersProvider.saveCollection(userlist);
        alertUtil.info("เปลี่ยนแปลงสถานะสำเร็จ",
                user.getUsername() + " ได้เปลี่ยนสถานะเป็น " + formatStatus(user.getStatus()));
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
                        usersProvider.saveCollection(userlist);
                        showTable(userlist);
                    }
                });
    }

    private String formatStatus(boolean status) {
        return (status ? "ปกติ" : "ถูกระงับ");
    }

    private String formatLastLogin(LocalDateTime time) {
        Duration duration = Duration.between(time, LocalDateTime.now());
        long seconds = duration.getSeconds();

        if (seconds < 60) return seconds + " วินาทีที่แล้ว";
        if (seconds < 3600) return (seconds / 60) + " นาทีที่แล้ว";
        if (seconds < 86400) return (seconds / 3600) + " ชั่วโมงที่แล้ว";
        return (seconds / 86400) + " วันที่แล้ว";
    }

    private void onSearch() {
        String keyword = searchTextField.getText();
        List<User> filtered = searchService.search(
                userlist.getUsers(),
                keyword,
                Account::getUsername,
                Account::getFullName
        );
        UserList filteredList = new UserList();
        filtered.forEach(filteredList::addUser);

        showTable(filteredList);
    }

    private void onBackButtonClick() {
        try {
            FXRouter.goTo("admin-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
