package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.components.button.IconButton;
import ku.cs.models.account.*;
import ku.cs.models.comparator.LoginTimeComparator;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.accounts.strategy.UserAccountProvider;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.SearchService;
import ku.cs.services.utils.TableColumnFactory;
import ku.cs.services.utils.TimeFormatUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class AdminManageUsersController extends BaseAdminController {
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();
    private final UserAccountProvider usersProvider = new UserAccountProvider();
    private final SearchService<User> searchService = new SearchService<>();
    private final AlertUtil alertUtil = new AlertUtil();
    private final TimeFormatUtil timeFormatUtil = new TimeFormatUtil();

    private static final int PROFILE_SIZE = 36;

    @FXML private TableView<User> userlistTableView;

    @FXML private TextField searchTextField;
    @FXML private Button searchButton;

    private UserList userlist;

    @Override
    protected void initDatasource() {
        userlist = usersProvider.loadCollection();
        Collections.sort(userlist.getAccounts(), new LoginTimeComparator());
    }

    @Override
    protected void initUserInterfaces() {
        IconButton.mask(searchButton, new Icon(Icons.MAGNIFYING_GLASS, 20));

        showTable(userlist);
    }

    @Override
    protected void initEvents() {
        searchTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            onSearch();
        });
        searchButton.setOnAction(e -> onSearch());
    }

    private void showTable(UserList userlist) {
        userlistTableView.getColumns().setAll(
                tableColumnFactory.createProfileColumn(),
                tableColumnFactory.createTextColumn("ชื่อผู้ใช้", "username", 128, "-fx-padding: 0 16"),
                tableColumnFactory.createTextColumn("ชื่อ", "fullName", 214, "-fx-padding: 0 16"),
                tableColumnFactory.createTextColumn("เบอร์มือถือ", "phone", 124, "-fx-padding: 0 16"),
                createLastLoginColumn(),
                tableColumnFactory.createStatusColumn("สถานะ", "status", "ปกติ", "ถูกระงับ"),
                createActionColumn()
        );

        userlistTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        userlistTableView.getItems().setAll(userlist.getAccounts());
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
                    setText(timeFormatUtil.localDateTimeToString(time));
                }
            }
        });
        col.setStyle("-fx-alignment: center-left;");
        return col;
    }

    private TableColumn<User, Void> createActionColumn() {
        return tableColumnFactory.createActionColumn("จัดการ", user -> {
            IconButton suspendBtn = new IconButton(new Icon(Icons.SUSPEND , 20));
//            IconButton infoBtn = new IconButton(new Icon(Icons.EYE));
            IconButton deleteBtn = IconButton.error(new Icon(Icons.DELETE));

            suspendBtn.setOnAction(e -> toggleStatus(user));
//            infoBtn.setOnAction(e -> userInfo(user));
            deleteBtn.setOnAction(e -> deleteUser(user));

//            infoBtn.setDisable(true);

            return new Button[]{suspendBtn, deleteBtn};
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
                        userlist.removeAccount(user);
                        usersProvider.saveCollection(userlist);
                        showTable(userlist);
                    }
                });
    }

    private String formatStatus(boolean status) {
        return (status ? "ปกติ" : "ถูกระงับ");
    }

    private void onSearch() {
        String keyword = searchTextField.getText();
        List<User> filtered = searchService.search(
                userlist.getAccounts(),
                keyword,
                Account::getUsername,
                Account::getFullName
        );
        UserList filteredList = new UserList();
        filtered.forEach(filteredList::addAccount);

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
