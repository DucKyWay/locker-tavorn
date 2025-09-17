package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import ku.cs.models.account.Account;
import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.OfficerListFileDatasource;

import java.io.FileNotFoundException;
import java.io.IOException;

public class AdminManageOfficerDatailsController {

    private OfficerList officers;
    private Datasource<OfficerList> datasource;

    private Object[] data;
    private Account current;
    private String officerUsername;

    private Officer officer;

    public void initialize() throws FileNotFoundException {

        data =  (Object[]) FXRouter.getData();
        current = (Account) data[0];
        officerUsername = (String) data[1];

        SessionManager.requireAdminLogin();
        current = SessionManager.getCurrentAccount();

        initialDatasource();

        officer = officers.findOfficerByUsername(officerUsername);
    }

    public void initialDatasource() throws FileNotFoundException {
        datasource = new OfficerListFileDatasource("data", "officer-data.json");
        officers = datasource.readData();
    }

    private void showOfficer(Officer officer) {
        //officerUsernameText.setText...
        //...
        //...
    }

    @FXML protected void onBackButtonClick() {
        try {
            FXRouter.goTo("admin-manage-officers", current);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
