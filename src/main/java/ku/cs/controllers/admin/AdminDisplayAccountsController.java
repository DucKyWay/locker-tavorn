package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.LabelStyle;
import ku.cs.components.Toast;
import ku.cs.components.button.*;
import ku.cs.models.account.Account;
import ku.cs.models.account.AccountList;
import ku.cs.models.account.Officer;
import ku.cs.models.account.User;
import ku.cs.services.accounts.strategy.*;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.SearchService;
import ku.cs.services.utils.TableColumnFactory;
import ku.cs.services.utils.TimeFormatUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class AdminDisplayAccountsController extends BaseAdminController {
    private final AccountProvider provider = AccountProviderFactory.create(AccountProviderType.ALL);
    private final UserAccountProvider userProvider = new UserAccountProvider();
    private final OfficerAccountProvider officerProvider = new OfficerAccountProvider();
    private final SearchService<Account> searchService = new SearchService<>();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();
    private final TimeFormatUtil timeFormatUtil = new TimeFormatUtil();

    private static final int PROFILE_SIZE = 40;

    @FXML private HBox parentHBoxFilled;
    @FXML private Button backButton;
    @FXML private TableView<Account> accountListTableView;

    private TextField searchTextField;
    private Button searchButton;
    private Stage stage;

    private List<Account> accounts;

    @Override
    protected void initDatasource() {
        accounts = provider.loadAccounts();
    }

    @Override
    protected void initUserInterfaces() {
        Region region = new Region();
        VBox titleVBox = new VBox();
        HBox searchBarHBox = new HBox();

        parentHBoxFilled.setSpacing(4);
        region.setPrefSize(360, 50);

        long officerCount = accounts.stream()
                .filter(a -> a.getRole().toString().equals("OFFICER"))
                .count();

        long userCount = accounts.stream()
                .filter(a -> a.getRole().toString().equals("USER"))
                .count();

        Label headerLabel = new Label("รายชื่อบัญชีผู้ใช้ทั้งหมด");
        Label descriptionLabel = new Label("พนักงาน " + officerCount + " บัญชี | ผู้ใช้ " + userCount + " บัญชี");
        ElevatedButton.MEDIUM.mask(backButton, Icons.ARROW_LEFT);
        searchTextField = new TextField();
        searchButton = new IconButton(new Icon(Icons.MAGNIFYING_GLASS));

        LabelStyle.TITLE_LARGE.applyTo(headerLabel);
        LabelStyle.TITLE_SMALL.applyTo(descriptionLabel);
        searchTextField.setPromptText("ค้นหาจากบางส่วนของชื่อ");
        searchTextField.setPrefWidth(300);

        titleVBox.getChildren().addAll(headerLabel, descriptionLabel);
        searchBarHBox.getChildren().addAll(searchTextField, searchButton);
        parentHBoxFilled.getChildren().addAll(titleVBox, region, searchBarHBox);

        if (footerNavBarButton != null) {
            footerNavBarButton.setText("ย้อนกลับ");
        }

        showTable(accounts);
    }

    @Override
    protected void initEvents() {
        backButton.setOnAction(e -> onBackButtonClick());

        searchTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            onSearch();
        });
        searchButton.setOnAction(e -> onSearch());
    }

    private void showTable(List<Account> accounts) {
        accountListTableView.getColumns().clear();
        accountListTableView.getColumns().setAll(
                tableColumnFactory.createProfileColumn(),
                tableColumnFactory.createTextColumn("ชื่อผู้ใช้", "username"),
                tableColumnFactory.createTextColumn("ชื่อ-นามสกุล", "fullName"),
                tableColumnFactory.createEnumStatusColumn("บทบาท", "role", 0),
                tableColumnFactory.createStatusColumn("สถานะ", "status", "ปกติ", "ถูกระงับ"),
                createLastLoginColumn(),
                createActionColumn()
        );

        accountListTableView.getItems().setAll(accounts);
        accountListTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private TableColumn<Account, LocalDateTime> createLastLoginColumn() {
        TableColumn<Account, LocalDateTime> col = new TableColumn<>("ใช้งานล่าสุด");
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
        col.setStyle("-fx-alignment: CENTER;");
        return col;
    }

    private TableColumn<Account, Void> createActionColumn() {
        return tableColumnFactory.createActionColumn("จัดการ", account -> {
            FilledButtonWithIcon statusBtn = FilledButtonWithIcon.small("เปลี่ยนสถานะ", Icons.SUSPEND);
            statusBtn.setOnAction(e -> toggleStatus(account));
            return new Button[]{statusBtn};
        });
    }


    private void toggleStatus(Account account) {
        System.out.println("Toggle: " + account.getUsername());
        account.toggleStatus();

        if (account instanceof User user) {
            updateAndSave(user, userProvider);
        } else if (account instanceof Officer officer) {
            updateAndSave(officer, officerProvider);
        }

        stage = (Stage) parentHBoxFilled.getScene().getWindow();
        Toast.show(stage, "เปลี่ยนแปลงสถานะ " + account.getUsername() + " สำเร็จ", 500);

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

    private void onBackButtonClick() {
        try {
            FXRouter.goTo("admin-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
