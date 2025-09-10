package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.controllers.components.SettingDropdownController;
import ku.cs.models.account.Account;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;

import java.io.IOException;

public class AdminHomeController {
    @FXML private VBox adminHomeLabelContainer;
    @FXML private VBox adminManageOfficerButtonContainer;

    @FXML private SettingDropdownController settingsContainerController;

    private DefaultLabel adminHomeLabel;
    private DefaultButton adminManageOfficerButton;

    private Account current;

    @FXML
    public void initialize() {
        // Auth Guard
        SessionManager.requireAdminLogin();
        current = SessionManager.getCurrentAccount();

        initialDatasourceZone();
        initUserInterface();
        initEvents();
    }
    private void initialDatasourceZone(){
        // datasource
    }

    private void initUserInterface() {
        adminHomeLabel = DefaultLabel.h2("Home | Super Admin | " + current.getUsername());
        adminManageOfficerButton = DefaultButton.warning("จัดการพนักงาน");

        adminHomeLabelContainer.getChildren().add(adminHomeLabel);
        adminManageOfficerButtonContainer.getChildren().add(adminManageOfficerButton);
    }

    private void initEvents() {
        adminManageOfficerButton.setOnAction(event -> onadminManageOfficerButtonClick());
    }

    protected void onadminManageOfficerButtonClick() {
        try {
            FXRouter.goTo("admin-manage-officer-details", current.getUsername());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
