package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.Toast;
import ku.cs.components.button.*;
import ku.cs.models.account.Account;
import ku.cs.models.account.AccountList;
import ku.cs.models.account.Officer;
import ku.cs.models.account.User;
import ku.cs.models.comparator.TimestampComparator;
import ku.cs.services.accounts.strategy.*;
import ku.cs.services.request.RequestService;
import ku.cs.services.utils.SearchService;
import ku.cs.services.utils.TableColumnFactory;
import ku.cs.services.utils.TimeFormatUtil;

import java.time.LocalDateTime;
import java.util.List;

public class AdminHomeController extends BaseAdminController {
    private final AccountProvider provider = AccountProviderFactory.create(AccountProviderType.ALL);
    private final UserAccountProvider userProvider = new UserAccountProvider();
    private final OfficerAccountProvider officerProvider = new OfficerAccountProvider();
    private final SearchService<Account> searchService = new SearchService<>();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();
    private final RequestService requestService = new RequestService();
    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;

    @FXML private TableView<Account> accountListTableView;
    @FXML private VBox parentVBox;

    @FXML private TextField searchTextField;
    @FXML private Button searchButton;

    private List<Account> accounts;

    @Override
    protected void initDatasource() {
        accounts = provider.loadAccounts();
        accounts.sort(new TimestampComparator<>());
    }

    @Override
    protected void initUserInterfaces() {
        IconButton.mask(searchButton, new Icon(Icons.MAGNIFYING_GLASS));
        long officerCount = accounts.stream()
                .filter(a -> a.getRole().toString().equals("OFFICER"))
                .count();

        long userCount = accounts.stream()
                .filter(a -> a.getRole().toString().equals("USER"))
                .count();

        titleLabel.setText("รายชื่อบัญชีผู้ใช้ทั้งหมด");
        descriptionLabel.setText("เจ้าหน้าที่ " + officerCount + " บัญชี | ผู้ใช้ " + userCount + " บัญชี");

        showTable(accounts);
    }

    @Override
    protected void initEvents() {
        requestService.updateData();
        searchTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            onSearch();
        });
        searchButton.setOnAction(e -> onSearch());
    }

    private void showTable(List<Account> accounts) {
        accountListTableView.getColumns().clear();
        accountListTableView.getColumns().setAll(
                tableColumnFactory.createProfileColumn(),
                tableColumnFactory.createTextColumn("ชื่อผู้ใช้", "username", 128),
                tableColumnFactory.createTextColumn("ชื่อ-นามสกุล", "fullName"),
                createLastLoginColumn(),
                tableColumnFactory.createEnumStatusColumn("บทบาท", "role", 110),
                tableColumnFactory.createStatusColumn("สถานะ", "status", 113, "ปกติ", "ถูกระงับ"),
                createActionColumn()
        );

        accountListTableView.getItems().setAll(accounts);
        accountListTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private TableColumn<Account, LocalDateTime> createLastLoginColumn() {
        TableColumn<Account, LocalDateTime> col = new TableColumn<>("ใช้งานล่าสุด");
        col.setPrefWidth(112);
        col.setMinWidth(112);
        col.setMaxWidth(112);
        col.setCellValueFactory(new PropertyValueFactory<>("loginTime"));
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime time, boolean empty) {
                super.updateItem(time, empty);
                if (empty || time == null) {
                    setText(null);
                } else {
                    setText(new TimeFormatUtil().localDateTimeToString(time));
                }
            }
        });
        col.setStyle("-fx-alignment: center-left; -fx-padding: 0 16");
        return col;
    }

    private TableColumn<Account, Void> createActionColumn() {
        return tableColumnFactory.createActionColumn("", 44,account -> {
            IconButton suspendBtn = new IconButton(new Icon(Icons.SUSPEND));
            suspendBtn.setOnAction(e -> toggleStatus(account));
            return new Button[]{suspendBtn};
        });
    }

    private void toggleStatus(Account account) {
        account.toggleStatus();

        if (account instanceof User user) {
            updateAndSave(user, userProvider);
        } else if (account instanceof Officer officer) {
            updateAndSave(officer, officerProvider);
        }

        Toast.show((Stage) parentVBox.getScene().getWindow(), "เปลี่ยนแปลงสถานะ " + account.getUsername() + " สำเร็จ", 1300);
        accountListTableView.refresh();
    }


    private <T extends Account, C extends AccountList<T>> void updateAndSave(T account, AccountProvider<T, C> provider) {
        C collection = provider.loadCollection();
        for (T a : collection.getAccounts()) {
            if (a.getUsername().equals(account.getUsername())) {
                a.setStatus(account.getStatus()); // sync
                break;
            }
        }
        provider.saveCollection(collection);
    }

    private void onSearch() {
        String keyword = searchTextField.getText();
        List<Account> filtered = searchService.search(
                accounts,
                keyword,
                Account::getUsername,
                Account::getFullName
        );
        showTable(filtered);
    }
}
