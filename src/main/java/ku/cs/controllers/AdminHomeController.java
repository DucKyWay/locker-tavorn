package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import ku.cs.components.DefaultLabel;
import ku.cs.controllers.components.SettingDropdownController;
import ku.cs.models.Account;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;

public class AdminHomeController {
    @FXML private VBox adminHomeLabelContainer;

    @FXML private SettingDropdownController settingsContainerController;

    private DefaultLabel adminHomeLabel;

    private Account account;

    @FXML
    public void initialize() {
        // Auth Guard
        SessionManager.requireAdminLogin();
        account = SessionManager.getCurrentAccount();

        initialDatasourceZone();
        initUserInterface();
        initEvents();
    }
    private void initialDatasourceZone(){
        // datasource
    }

    private void initUserInterface() {
        adminHomeLabel = DefaultLabel.h2("Home | Super Admin | " + account.getUsername());

        adminHomeLabelContainer.getChildren().add(adminHomeLabel);
    }

    private void initEvents() {
        // events action
    }
}
