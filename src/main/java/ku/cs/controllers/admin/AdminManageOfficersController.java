package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import ku.cs.components.Icons;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.FilledButton;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.components.button.IconButton;
import ku.cs.controllers.components.AdminNavbarController;
import ku.cs.models.account.*;

import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.OfficerListFileDatasource;
import ku.cs.services.utils.AlertUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class AdminManageOfficersController {
    @FXML private TableView<Officer> officersTableView;

    @FXML private HBox parentHBoxFilled;
    private Label headerLabel;
    private Label descriptionLabel;
    private Button addNewOfficerFilledButton;

    @FXML private AdminNavbarController adminNavbarController;
    private Button footerNavBarButton;

    OfficerList officers;
    Datasource<OfficerList> datasource;

    Account current;

    @FXML public void initialize() throws FileNotFoundException {

        SessionManager.requireAdminLogin();
        current = SessionManager.getCurrentAccount();

        footerNavBarButton = adminNavbarController.getFooterNavButton();

        initDatasources();
        initUserInterfaces();
        initEvents();

        showTable(officers);
    }

    private void initDatasources() {
        datasource = new OfficerListFileDatasource("data", "test-officer-data.json");
        officers = datasource.readData();
    }

    private void initUserInterfaces() {
        Region region = new Region();
        VBox vBox = new VBox();

        parentHBoxFilled.setSpacing(4);
        region.setPrefSize(620, 50);

        footerNavBarButton.setText("ย้อนกลับ");

        headerLabel = new Label("จัดการพนักงาน");
        descriptionLabel = new Label("ด้วย " + current.getUsername());
        addNewOfficerFilledButton = new FilledButton("เพิ่มพนักงานใหม่");

        LabelStyle.TITLE_LARGE.applyTo(headerLabel);
        LabelStyle.TITLE_SMALL.applyTo(descriptionLabel);

        vBox.getChildren().addAll(headerLabel, descriptionLabel);

        parentHBoxFilled.getChildren().addAll(vBox, region, addNewOfficerFilledButton);
    }

    private void initEvents() {
        footerNavBarButton.setOnAction(e -> onBackButtonClick());
        addNewOfficerFilledButton.setOnAction(e -> onAddNewOfficerButtonClick());
    }

    private void showTable(OfficerList officers) {
        TableColumn<Officer, String> usernameColumn = new TableColumn<>("ชื่อผู้ใช้");
        TableColumn<Officer, String> nameColumn = new TableColumn<>("ชื่อ");
        TableColumn<Officer, String> emailColumn = new TableColumn<>("อีเมล");
        TableColumn<Officer, String> phoneColumn = new TableColumn<>("เบอร์มือถือ");
        TableColumn<Officer, Role> roleColumn = new TableColumn<>("ตำแหน่ง");
        TableColumn<Officer, Void> actionColumn = new TableColumn<>("จัดการ");

        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        actionColumn.setCellFactory(createActionCellFactory());

        phoneColumn.setStyle("-fx-alignment: CENTER;");
        roleColumn.setStyle("-fx-alignment: CENTER;");
        actionColumn.setStyle("-fx-alignment: CENTER;");

        emailColumn.setMinWidth(200);
        actionColumn.setPrefWidth(180);

        officersTableView.getColumns().clear();
        officersTableView.getColumns().addAll(usernameColumn, nameColumn,
                emailColumn, phoneColumn, roleColumn, actionColumn);

        officersTableView.getItems().setAll(officers.getOfficers());
        officersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private Callback<TableColumn<Officer, Void>, TableCell<Officer, Void>> createActionCellFactory() {
        return col -> new TableCell<>() {
            // TODO: passBtn future use icon button
            private final FilledButtonWithIcon passBtn = new FilledButtonWithIcon("", Icons.KEY);
            private final FilledButtonWithIcon editBtn = FilledButtonWithIcon.small("แก้ไข", Icons.EDIT);
            private final FilledButtonWithIcon deleteBtn = FilledButtonWithIcon.small("ลบ", Icons.DELETE);

            {
                passBtn.setOnAction(e -> showDefaultPassword(getTableView().getItems().get(getIndex())));
                editBtn.setOnAction(e -> editOfficer(getTableView().getItems().get(getIndex())));
                deleteBtn.setOnAction(e -> deleteOfficer(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(5, passBtn, editBtn, deleteBtn));
            }
        };
    }

    private void showDefaultPassword(Officer officer) {
        if(!officer.isStatus()) {
            AlertUtil.info("Default Password",
                    officer.getUsername() + " hasn't change password." + "\n" +
                            "Default Password is "+ officer.getDefaultPassword());
        } else {
            AlertUtil.info("Default Password", officer.getUsername() + " password has changed.");
        }
    }

    private void editOfficer(Officer officer) {
        try {
            FXRouter.goTo("admin-manage-officer-details", officer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteOfficer(Officer officer) {
        AlertUtil.confirm(
                "Warning",
                "Do you want to remove " + officer.getUsername() + "?"
        ).ifPresent(response -> {
            if (response == ButtonType.OK) {
                officers.removeOfficer(officer);
                datasource.writeData(officers);
                showTable(officers);
            }
        });
    }

    protected void onBackButtonClick() {
        try {
            FXRouter.goTo("admin-home", current);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void onAddNewOfficerButtonClick() {
        try {
            FXRouter.goTo("admin-manage-new-officer", current);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
