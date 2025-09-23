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
import ku.cs.models.account.Account;
import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.models.account.Role;

import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.OfficerListFileDatasource;
import ku.cs.services.utils.AlertUtil;

import java.io.FileNotFoundException;
import java.io.IOException;

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

        officersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
        TableColumn<Officer, String> telphoneColumn = new TableColumn<>("เบอร์มือถือ");
        TableColumn<Officer, Role> roleColumn = new TableColumn<>("ตำแหน่ง");
        TableColumn<Officer, Void> actionCol = new TableColumn<>("จัดการ");

        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        telphoneColumn.setCellValueFactory(new PropertyValueFactory<>("telphone"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        Callback<TableColumn<Officer, Void>, TableCell<Officer, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Officer, Void> call(final TableColumn<Officer, Void> param) {
                return new TableCell<>() {
                    // TODO: passBtn future use icon button
                    private final FilledButtonWithIcon passBtn = new FilledButtonWithIcon("", Icons.KEY);
                    private final FilledButtonWithIcon editBtn = FilledButtonWithIcon.small("แก้ไข", Icons.EDIT);
                    private final FilledButtonWithIcon deleteBtn = FilledButtonWithIcon.small("ลบ", Icons.DELETE);

                    {
                        passBtn.setOnAction(e -> {
                           Officer officer = getTableView().getItems().get(getIndex());
                           if(!officer.isStatus()) {
                               AlertUtil.info("Default Password",
                                       officer.getUsername() + " hasn't change password." + "\n" +
                                               "Default Password is "+ officer.getDefaultPassword());
                           } else {
                               AlertUtil.info("Default Password", officer.getUsername() + " password has changed.");
                           }
                        });

                        editBtn.setOnAction(event -> {
                            Officer officer = getTableView().getItems().get(getIndex());
                            try {
                                FXRouter.goTo("admin-manage-officer-details", officer);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });

                        deleteBtn.setOnAction(event -> {
                            // TODO: delete officer
//                            Officer officer = getTableView().getItems().get(getIndex());
//                            officers.removeOfficer(officer);
//                            datasource.writeData(officers);
//                            officersTableView.getItems().remove(officer);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox hbox = new HBox(5, passBtn, editBtn, deleteBtn);
                            setGraphic(hbox);
                        }
                    }
                };
            }
        };

        actionCol.setCellFactory(cellFactory);

        officersTableView.getColumns().clear();
        officersTableView.getColumns().add(usernameColumn);
        officersTableView.getColumns().add(nameColumn);
        officersTableView.getColumns().add(emailColumn);
        officersTableView.getColumns().add(telphoneColumn);
        officersTableView.getColumns().add(roleColumn);
        officersTableView.getColumns().add(actionCol);

        officersTableView.getItems().setAll(officers.getOfficers());
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
