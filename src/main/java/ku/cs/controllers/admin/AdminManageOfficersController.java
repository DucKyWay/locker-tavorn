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
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.FilledButton;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.components.button.IconButton;
import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.models.comparator.FullNameComparator;
import ku.cs.services.FXRouter;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.OfficerListFileDatasource;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.TableColumnFactory;

import java.io.IOException;
import java.util.Collections;

public class AdminManageOfficersController extends BaseAdminController {
    private final AlertUtil alertUtil = new AlertUtil();

    private static final int PROFILE_SIZE = 40;

    @FXML private TableView<Officer> officersTableView;
    @FXML private HBox parentHBoxFilled;

    private Button addNewOfficerFilledButton;

    private OfficerList officers;
    private Datasource<OfficerList> datasource;

    @Override
    protected void initDatasource() {
        datasource = new OfficerListFileDatasource("data", "test-officer-data.json");
        officers = datasource.readData();
        Collections.sort(officers.getOfficers(), new FullNameComparator());
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
        addNewOfficerFilledButton = new FilledButton("เพิ่มพนักงานใหม่");

        LabelStyle.TITLE_LARGE.applyTo(headerLabel);
        LabelStyle.TITLE_SMALL.applyTo(descriptionLabel);

        vBox.getChildren().addAll(headerLabel, descriptionLabel);
        parentHBoxFilled.getChildren().addAll(vBox, region, addNewOfficerFilledButton);

        showTable(officers);

        // hover effect
        officersTableView.setRowFactory(tv -> {
            TableRow<Officer> row = new TableRow<>();
            row.setOnMouseEntered(e -> {
                if (!row.isEmpty()) row.setCursor(javafx.scene.Cursor.HAND);
            });
            row.setOnMouseExited(e -> row.setCursor(javafx.scene.Cursor.DEFAULT));
            return row;
        });

        // click
        officersTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Officer>() {
            @Override
            public void changed(ObservableValue<? extends Officer> observableValue, Officer curOfficer, Officer newOfficer) {
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
        addNewOfficerFilledButton.setOnAction(e -> onAddNewOfficerButtonClick());
    }

    private void showTable(OfficerList officers) {
        officersTableView.getColumns().setAll(
                TableColumnFactory.createProfileColumn(PROFILE_SIZE),
                TableColumnFactory.createTextColumn("ชื่อผู้ใช้", "username"),
                TableColumnFactory.createTextColumn("ชื่อ", "fullName"),
                createDefaultPasswordColumn(),
                createCopyPasswordColumn(),
                TableColumnFactory.createTextColumn("ตำแหน่ง", "role", 0, "-fx-alignment: CENTER;"),
                createActionColumn()
        );

        officersTableView.getItems().setAll(officers.getOfficers());
        officersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private TableColumn<Officer, String> createDefaultPasswordColumn() {
        TableColumn<Officer, String> defaultPasswordColumn = new TableColumn<>("รหัสผ่าน");
        defaultPasswordColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String password, boolean empty) {
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    return;
                }

                Officer officer = getTableRow().getItem();
                if (!officer.isStatus()) {
                    setText(officer.getDefaultPassword());
                } else {
                    setText("-");
                }
            }
        });

        defaultPasswordColumn.setStyle("-fx-alignment: CENTER;");
        defaultPasswordColumn.setPrefWidth(50);
        return defaultPasswordColumn;
    }

    private TableColumn<Officer, Void> createCopyPasswordColumn() {
        TableColumn<Officer, Void> copyPasswordColumn = new TableColumn<>();
        copyPasswordColumn.setCellFactory(col -> new TableCell<>() {
            private final IconButton copyPasswordBtn = new IconButton(new Icon(Icons.COPY));

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }

                Officer officer = getTableRow().getItem();
                copyPasswordBtn.setOnAction(e -> onCopyPasswordButtonClick(officer));
                copyPasswordBtn.setDisable(officer.isStatus());

                setGraphic(copyPasswordBtn);
            }
        });

        copyPasswordColumn.setPrefWidth(20);
        return copyPasswordColumn;
    }

    private TableColumn<Officer, Void> createActionColumn() {
        return TableColumnFactory.createActionColumn("จัดการ", officer -> {
            FilledButtonWithIcon editBtn = FilledButtonWithIcon.small("แก้ไข", Icons.EDIT);
            FilledButtonWithIcon deleteBtn = FilledButtonWithIcon.small("ลบ", Icons.DELETE);

            editBtn.setOnAction(e -> editOfficer(officer));
            deleteBtn.setOnAction(e -> deleteOfficer(officer));

            return new Button[]{editBtn, deleteBtn};
        });
    }

    private void onCopyPasswordButtonClick(Officer officer) {
        if (!officer.isStatus()) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(officer.getDefaultPassword());
            clipboard.setContent(content);
        }
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
                .ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        officers.removeOfficer(officer);
                        datasource.writeData(officers);
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
