package ku.cs.controllers.admin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import ku.cs.components.Icons;
import ku.cs.components.button.FilledButton;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.models.account.Account;
import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.models.account.Role;

import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.OfficerListFileDatasource;

import java.io.FileNotFoundException;
import java.io.IOException;

public class AdminManageOfficersController {
    @FXML private TableView<Officer> officersTableView;

    @FXML private HBox parentHBoxFilled;
    @FXML private Button backFilledButton;
    @FXML private Button addNewOfficerFilledButton;

    OfficerList officers;
    Datasource<OfficerList> datasource;

    Account current;

    @FXML public void initialize() throws FileNotFoundException {

        SessionManager.requireAdminLogin();
        current = SessionManager.getCurrentAccount();

        initDatasources();
        initUserInterfaces();
        initEvents();

        showTable(officers);

        officersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        officersTableView.getSelectionModel().selectedItemProperty().addListener((new ChangeListener<Officer>() {

            @Override


            public void changed(ObservableValue<? extends Officer> observable, Officer oldValue, Officer newValue) {
                if (newValue != null) {
                    try {
                        FXRouter.goTo("admin-manage-officer-details", newValue.getUsername());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }));
    }

    private void initDatasources() throws FileNotFoundException {
        datasource = new OfficerListFileDatasource("data", "test-officer-data.json");
        officers = datasource.readData();
    }

    private void initUserInterfaces() throws FileNotFoundException {
        Region region = new Region();

        parentHBoxFilled.setSpacing(4);
        region.setPrefSize(850, 50);

        backFilledButton = new FilledButton("ย้อนกลับ");
        addNewOfficerFilledButton = new FilledButton("เพิ่มพนักงานใหม่");

        parentHBoxFilled.getChildren().addAll(backFilledButton, region, addNewOfficerFilledButton);
    }

    private void initEvents() throws FileNotFoundException {
        backFilledButton.setOnAction(e -> onBackButtonClick());
        addNewOfficerFilledButton.setOnAction(e -> onAddNewOfficerButtonClick());
    }

    private void showTable(OfficerList officers) {
        TableColumn<Officer, String> usernameColumn = new TableColumn<>("ชื่อผู้ใช้");
        TableColumn<Officer, String> nameColumn = new TableColumn<>("ชื่อ");
        TableColumn<Officer, String> emailColumn = new TableColumn<>("อีเมล");
        TableColumn<Officer, String> telphoneColumn = new TableColumn<>("เบอร์มือถือ");
        TableColumn<Officer, Role> roleColumn = new TableColumn<>("ตำแหน่ง");
        TableColumn<Officer, String> imagePathColumn = new TableColumn<>("ที่อยู่รูปโปรไฟล์");

        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        telphoneColumn.setCellValueFactory(new PropertyValueFactory<>("telphone"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        imagePathColumn.setCellValueFactory(new PropertyValueFactory<>("imagePath"));

        TableColumn<Officer, Void> actionCol = new TableColumn<>("จัดการ");

        Callback<TableColumn<Officer, Void>, TableCell<Officer, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Officer, Void> call(final TableColumn<Officer, Void> param) {
                return new TableCell<>() {
                    private final FilledButtonWithIcon editBtn = FilledButtonWithIcon.small("แก้ไข", Icons.EDIT);
                    private final FilledButtonWithIcon deleteBtn = FilledButtonWithIcon.small("ลบ", Icons.DELETE);

                    {
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
                            HBox hbox = new HBox(5, editBtn, deleteBtn);
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
        officersTableView.getColumns().add(imagePathColumn);
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
