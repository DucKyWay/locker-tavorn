package ku.cs.controllers.components;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ku.cs.components.Icons;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class AdminNavbarController {
    @FXML private Button manageOfficersButton;
    @FXML private Button manageUsersButton;
    @FXML private Button footerNavButton;

    @FXML public void initialize() {
        initUserInterfaces();
        initEvents();
    }

    private void initUserInterfaces() {
        ElevatedButtonWithIcon.SMALL.mask(manageOfficersButton, Icons.EDIT);
        ElevatedButtonWithIcon.SMALL.mask(manageUsersButton, Icons.EDIT);
    }

    private void initEvents() {
        manageOfficersButton.setOnAction(e -> onManageOfficersButtonClick());
        manageUsersButton.setOnAction(e -> onManageUsersButtonClick());
    }

    public Button getFooterNavButton() {
        return footerNavButton;
    }

    protected void onManageOfficersButtonClick() {
        try {
            FXRouter.goTo("admin-manage-officers");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void onManageUsersButtonClick() {
        try {
            FXRouter.goTo("admin-manage-users");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
