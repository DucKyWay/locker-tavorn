package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import ku.cs.components.button.FilledButton;
import ku.cs.models.account.Account;
import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.OfficerListFileDatasource;

import java.io.FileNotFoundException;
import java.io.IOException;

public class AdminManageNewOfficerController {

    @FXML private HBox parentHBoxFilled;
    @FXML private Button backFilledButton;

    private OfficerList officers;
    private Datasource<OfficerList> datasource;

    private Account current;
    private Officer officer;

    public void initialize() throws FileNotFoundException {
        SessionManager.requireAdminLogin();
        current = SessionManager.getCurrentAccount();

        initDatasource();
        initUserInterfaces();
        initEvents();
    }

    public void initDatasource() throws FileNotFoundException {
        datasource = new OfficerListFileDatasource("data", "test-officer-data.json");
        officers = datasource.readData();
    }

    public void initUserInterfaces(){
        parentHBoxFilled.setSpacing(4);

        backFilledButton = new FilledButton("ย้อนกลับ");

        parentHBoxFilled.getChildren().addAll(backFilledButton);
    }

    public void initEvents() {
        backFilledButton.setOnAction(e -> onBackFilledButtonClick());
    }

    protected void onBackFilledButtonClick() {
        try {
            FXRouter.goTo("admin-manage-officers");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
