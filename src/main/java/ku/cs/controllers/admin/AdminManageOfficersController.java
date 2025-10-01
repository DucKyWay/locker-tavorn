package ku.cs.controllers.admin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.LabelStyle;
import ku.cs.components.Toast;
import ku.cs.components.button.FilledButton;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.components.button.IconButton;
import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.models.comparator.FullNameComparator;
import ku.cs.services.AppContext;
import ku.cs.services.FXRouter;
import ku.cs.services.strategy.account.OfficerAccountProvider;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.TableColumnFactory;

import java.io.IOException;

public class AdminManageOfficersController extends BaseAdminController {
    protected final TableColumnFactory tableColumnFactory = AppContext.getTableColumnFactory();

    private final AlertUtil alertUtil = new AlertUtil();

    private static final int PROFILE_SIZE = 32;

    @FXML private TableView<Officer> officersTableView;
    @FXML private HBox parentHBoxFilled;

    private Button addNewOfficerButton;
    private Stage stage;

    private final OfficerAccountProvider provider = new OfficerAccountProvider();
    private OfficerList officers;

    @Override
    protected void initDatasource() {
        officers = provider.loadCollection();
        officers.getOfficers().sort(new FullNameComparator());
        showTable(officers);
    }

    @Override
    protected void initUserInterfaces() {
        Region region = new Region();
        VBox vBox = new VBox();

        parentHBoxFilled.setSpacing(4);
        region.setPrefSize(410, 50);

        if (footerNavBarButton != null) {
            footerNavBarButton.setText("ย้อนกลับ");
        }

        Label headerLabel = new Label("จัดการพนักงาน");
        Label descriptionLabel = new Label("คลิกที่รายชื่อพนักงานเพื่อตรวจสอบจุดพื้นที่รับผิดชอบ");
        addNewOfficerButton = new FilledButton("เพิ่มพนักงานใหม่");

        LabelStyle.TITLE_LARGE.applyTo(headerLabel);
        LabelStyle.TITLE_SMALL.applyTo(descriptionLabel);

        vBox.getChildren().addAll(headerLabel, descriptionLabel);
        parentHBoxFilled.getChildren().addAll(vBox, region, addNewOfficerButton);

        // hover effect
        officersTableView.setRowFactory(tv -> {
            TableRow<Officer> row = new TableRow<>();
            row.setOnMouseEntered(e -> {
                if (!row.isEmpty()) row.setCursor(javafx.scene.Cursor.HAND);
            });
            row.setOnMouseExited(e -> row.setCursor(javafx.scene.Cursor.DEFAULT));
            return row;
        });

        // click row -> display zones
        officersTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Officer>() {
            @Override
            public void changed(ObservableValue<? extends Officer> obs, Officer oldOfficer, Officer newOfficer) {
                if (newOfficer != null) {
                    try {
                        FXRouter.goTo("admin-display-officer-zones", newOfficer);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    @Override
    protected void initEvents() {
        if (footerNavBarButton != null) {
            footerNavBarButton.setOnAction(e -> onBackButtonClick());
        }
        addNewOfficerButton.setOnAction(e -> onAddNewOfficerButtonClick());
    }

    private void showTable(OfficerList officers) {
        officersTableView.getItems().clear();
        officersTableView.getColumns().setAll(
                tableColumnFactory.createProfileColumn(PROFILE_SIZE),
                tableColumnFactory.createTextColumn("ชื่อผู้ใช้", "username"),
                tableColumnFactory.createTextColumn("ชื่อ", "fullName"),
                createDefaultPasswordColumn(),
                createCopyPasswordColumn(),
                tableColumnFactory.createStatusColumn("สถานะ", "status", "ปกติ", "ถูกระงับ"),
                createActionColumn()
        );

        officersTableView.getItems().setAll(officers.getOfficers());
        officersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private TableColumn<Officer, String> createDefaultPasswordColumn() {
        TableColumn<Officer, String> col = new TableColumn<>("รหัสผ่าน");
        col.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(String pwd, boolean empty) {
                super.updateItem(pwd, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    return;
                }
                Officer officer = getTableRow().getItem();
                setText(officer.isFirstTime() ? "-" : officer.getDefaultPassword());
            }
        });
        col.setStyle("-fx-alignment: CENTER;");
        col.setPrefWidth(50);
        return col;
    }

    private TableColumn<Officer, Void> createCopyPasswordColumn() {
        TableColumn<Officer, Void> col = new TableColumn<>();
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
        col.setPrefWidth(20);
        return col;
    }

    private TableColumn<Officer, Void> createActionColumn() {
        return tableColumnFactory.createActionColumn("จัดการ", officer -> {
            FilledButtonWithIcon statusBtn = FilledButtonWithIcon.small("เปลี่ยนสถานะ", Icons.SUSPEND);
            IconButton editBtn = new IconButton(new Icon(Icons.EDIT));
            IconButton deleteBtn = IconButton.error(new Icon(Icons.DELETE));

            statusBtn.setOnAction(e -> toggleStatus(officer));
            editBtn.setOnAction(e -> editOfficer(officer));
            deleteBtn.setOnAction(e -> deleteOfficer(officer));


            return new Button[]{statusBtn, editBtn, deleteBtn};
        }, 130);
    }

    private void onCopyPasswordButtonClick(Officer officer) {
        if (officer.isFirstTime()) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(officer.getDefaultPassword());
            clipboard.setContent(content);
        }
    }

    private void toggleStatus(Officer officer) {
        officer.toggleStatus();
        provider.saveCollection(officers);
        stage = (Stage) parentHBoxFilled.getScene().getWindow();
        Toast.show(stage, "เปลี่ยนสถานะให้ " + officer.getUsername(), 500);
        showTable(officers);
    }

    private void editOfficer(Officer officer) {
        try {
            FXRouter.goTo("admin-manage-officer-details", officer.getUsername());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteOfficer(Officer officer) {
        alertUtil.confirm("Warning", "Do you want to remove " + officer.getUsername() + "?")
                .ifPresent(res -> {
                    if (res == ButtonType.OK) {
                        officers.removeOfficer(officer);
                        provider.saveCollection(officers); // บันทึกผ่าน Provider
                        showTable(officers);
                    }
                });
    }

    private void onBackButtonClick() {
        try {
            FXRouter.goTo("admin-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void onAddNewOfficerButtonClick() {
        try {
            FXRouter.goTo("admin-manage-new-officer");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
