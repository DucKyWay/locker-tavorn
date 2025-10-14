package ku.cs.controllers.admin;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.Toast;
import ku.cs.components.button.*;
import ku.cs.models.account.Account;
import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.models.comparator.FullNameComparator;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.accounts.strategy.OfficerAccountProvider;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.SearchService;
import ku.cs.services.utils.TableColumnFactory;

import java.io.IOException;
import java.util.List;

public class AdminManageOfficersController extends BaseAdminController {
    private final OfficerAccountProvider provider = new OfficerAccountProvider();
    private final SearchService<Officer> searchService = new SearchService<>();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();

    @FXML private TableView<Officer> officersTableView;
    @FXML private VBox parentVBox;

    @FXML private TextField searchTextField;
    @FXML private Button searchButton;
    @FXML private Button addNewOfficerButton;
    @FXML private Button adminManageOfficerRouteLabelButton;

    private OfficerList officers;

    @Override
    protected void initDatasource() {
        officers = provider.loadCollection();
        officers.getAccounts().sort(new FullNameComparator());
        showTable(officers);
    }

    @Override
    protected void initUserInterfaces() {
        FilledButtonWithIcon.SMALL.mask(addNewOfficerButton, null, Icons.USER_PLUS);
        IconButton.mask(searchButton, new Icon(Icons.MAGNIFYING_GLASS, 20));
        ElevatedButtonWithIcon.LABEL.mask(adminManageOfficerRouteLabelButton, Icons.TAG);

        officersTableView.setRowFactory(tv -> {
            TableRow<Officer> row = new TableRow<>();
            row.setOnMouseEntered(e -> {
                if (!row.isEmpty()) row.setCursor(javafx.scene.Cursor.HAND);
            });
            row.setOnMouseExited(e -> row.setCursor(javafx.scene.Cursor.DEFAULT));
            return row;
        });

        officersTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Officer>() {
            @Override
            public void changed(ObservableValue<? extends Officer> obs, Officer oldOfficer, Officer newOfficer) {
                if (newOfficer != null) {
                    try {
                        FXRouter.goTo("admin-display-officer-zones", newOfficer);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } finally {
                        // Async clear
                        Platform.runLater(() -> officersTableView.getSelectionModel().clearSelection());
                    }
                }
            }
        });
    }

    @Override
    protected void initEvents() {
        searchTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            onSearch();
        });
        searchButton.setOnAction(e -> onSearch());

        addNewOfficerButton.setOnAction(e -> onAddNewOfficerButtonClick());
    }

    private void showTable(OfficerList officers) {
        officersTableView.getItems().clear();
        officersTableView.getColumns().setAll(
                tableColumnFactory.createProfileColumn(),
                tableColumnFactory.createTextColumn("ชื่อผู้ใช้", "username", 128),
                tableColumnFactory.createTextColumn("ชื่อ-นามสกุล", "fullName"),
                createDefaultPasswordColumn(),
                createCopyPasswordColumn(),
                tableColumnFactory.createStatusColumn("สถานะ", "status",113,  "ปกติ", "ถูกระงับ"),
                createActionColumn()
        );

        officersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        officersTableView.getItems().setAll(officers.getAccounts());
    }

    private TableColumn<Officer, String> createDefaultPasswordColumn() {
        TableColumn<Officer, String> col = new TableColumn<>("รหัสผ่าน");
        col.setPrefWidth(78);
        col.setMinWidth(78);
        col.setMaxWidth(78);
        col.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(String pwd, boolean empty) {
                super.updateItem(pwd, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    return;
                }
                Officer officer = getTableRow().getItem();
                setText(officer.isFirstTime() ? officer.getDefaultPassword() : "-");
            }
        });
        col.setStyle("-fx-alignment: CENTER_LEFT; -fx-padding: 0 16;");
        return col;
    }

    private TableColumn<Officer, Void> createCopyPasswordColumn() {
        TableColumn<Officer, Void> col = new TableColumn<>();
        col.setPrefWidth(44);
        col.setMinWidth(44);
        col.setMaxWidth(44);
        col.setCellFactory(c -> new TableCell<>() {
            private final IconButton copyBtn = new IconButton(new Icon(Icons.COPY));
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }
                Officer officer = getTableRow().getItem();
                copyBtn.setOnAction(e -> onCopyPasswordButtonClick(officer));
                copyBtn.setDisable(!officer.isFirstTime());
                setGraphic(copyBtn);
            }
        });
        col.setStyle("-fx-alignment: CENTER_LEFT; -fx-padding: 0 12 0 0");
        return col;
    }

    private TableColumn<Officer, Void> createActionColumn(){
        return tableColumnFactory.createActionColumn("", 130, officer -> {
            IconButton statusBtn = new IconButton(new Icon(Icons.SUSPEND));
            IconButton editBtn = new IconButton(new Icon(Icons.EDIT));
            IconButton deleteBtn = IconButton.error(new Icon(Icons.DELETE));

            statusBtn.setOnAction(e -> toggleStatus(officer));
            editBtn.setOnAction(e -> editOfficer(officer));
            deleteBtn.setOnAction(e -> deleteOfficer(officer));

            return new Button[]{statusBtn, editBtn, deleteBtn};
        });
    }

    private void onCopyPasswordButtonClick(Officer officer){
        if (officer.isFirstTime()) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(officer.getDefaultPassword());
            clipboard.setContent(content);
        }
    }

    private void toggleStatus(Officer officer){
        officer.toggleStatus();
        provider.saveCollection(officers);
        Toast.show((Stage)parentVBox.getScene().getWindow(), "เปลี่ยนสถานะให้ " + officer.getUsername() + formatStatus(officer.getStatus()), 1300);
        showTable(officers);
    }

    private String formatStatus(boolean status) {
        return (status ? "ปกติ" : "ถูกระงับ");
    }


    private void editOfficer(Officer officer) {
        try {
            FXRouter.goTo("admin-manage-officer-details", officer.getUsername());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteOfficer(Officer officer) {
        new AlertUtil().confirm("Warning", "Do you want to remove " + officer.getUsername() + "?")
                .ifPresent(res -> {
                    if (res == ButtonType.OK) {
                        officers.removeAccount(officer);
                        provider.saveCollection(officers); // บันทึกผ่าน Provider
                        showTable(officers);
                    }
                });
    }

    private void onSearch() {
        String keyword = searchTextField.getText();
        List<Officer> filtered = searchService.search(
                officers.getAccounts(),
                keyword,
                Account::getUsername,
                Account::getFullName
        );
        OfficerList filteredList = new OfficerList();
        filtered.forEach(filteredList::addAccount);

        showTable(filteredList);
    }

    private void onAddNewOfficerButtonClick() {
        try {
            FXRouter.goTo("admin-manage-new-officer");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
