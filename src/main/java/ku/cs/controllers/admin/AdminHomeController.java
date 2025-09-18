package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import ku.cs.components.DefaultLabel;
import ku.cs.components.button.FilledButton;
import ku.cs.controllers.components.SettingDropdownController;
import ku.cs.models.account.Account;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;

import java.io.IOException;

public class AdminHomeController {
    @FXML private VBox adminHomeLabelContainer;
    @FXML private SettingDropdownController settingsContainerController;

    @FXML private VBox manageVBox;
    @FXML private Button adminManageOfficerButton;
    @FXML private Button adminManageUserButton;

    private DefaultLabel adminHomeLabel;

    private Account current;

    @FXML
    public void initialize() {
        // Auth Guard
        SessionManager.requireAdminLogin();
        current = SessionManager.getCurrentAccount();

        initUserInterface();
        initEvents();
    }

    private void initUserInterface() {
        Region region = new Region();
        region.setPrefSize(0, 20);

        adminHomeLabel = DefaultLabel.h2("Home | Super Admin | " + current.getUsername());
        adminManageOfficerButton = new FilledButton("จัดการพนักงาน");
        adminManageUserButton = new FilledButton("จัดการผู้ใช้");

        adminHomeLabelContainer.getChildren().add(adminHomeLabel);

        manageVBox.getChildren().addAll(adminManageOfficerButton, region, adminManageUserButton);
    }

    private void initEvents() {
        adminManageOfficerButton.setOnAction(event -> onAdminManageOfficerButtonClick());
        adminManageUserButton.setOnAction(event -> onAdminManageUserButtonClick());
    }

    protected void onAdminManageOfficerButtonClick() {
        try {
            FXRouter.goTo("admin-manage-officers");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void onAdminManageUserButtonClick() {
        try {
            FXRouter.goTo("admin-manage-users");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
