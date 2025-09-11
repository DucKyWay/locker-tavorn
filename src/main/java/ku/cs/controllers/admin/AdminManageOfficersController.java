package ku.cs.controllers.admin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

    OfficerList officers;
    Datasource<OfficerList> datasource;

    Account current;

    @FXML public void initialize() throws FileNotFoundException {

        SessionManager.requireAdminLogin();
        current = SessionManager.getCurrentAccount();

        initialDatasource();
        showTable(officers);

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

    private void initialDatasource() throws FileNotFoundException {
        datasource = new OfficerListFileDatasource("data", "test-officer-data.json");
        officers = datasource.readData();
    }

    private void showTable(OfficerList officers) {
        TableColumn<Officer, String> usernameColumn = new TableColumn<>("ชื่อผู้ใช้");
        TableColumn<Officer, String> nameColumn = new TableColumn<>("ชื่อ");
        TableColumn<Officer, String> emailColumn = new TableColumn<>("อีเมล");
        TableColumn<Officer, String> telphoneColumn = new TableColumn<>("เบอร์มือถือ");
        TableColumn<Officer, Role> roleColumn = new TableColumn<>("ตำแหน่ง");
        TableColumn<Officer, String> imagePathColumn = new TableColumn<>("ที่อยู่รุปโปรไฟล์");

        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        telphoneColumn.setCellValueFactory(new PropertyValueFactory<>("telphone"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        imagePathColumn.setCellValueFactory(new PropertyValueFactory<>("imagePath"));

        officersTableView.getColumns().clear();
        officersTableView.getColumns().addAll(usernameColumn, nameColumn, emailColumn, telphoneColumn, roleColumn, imagePathColumn);

        officersTableView.getItems().clear();
        officersTableView.getItems().addAll(officers.getOfficers());
    }

    @FXML protected void onBackButtonClick() {
        try {
            FXRouter.goTo("admin-home", current);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML protected void onAddNewOfficerButtonClick() {
        try {
            FXRouter.goTo("admin-manage-officer-details", current);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
