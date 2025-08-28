package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.models.Account;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;

public class AdminHomeController {
    @FXML private VBox adminHomeLabelContainer;
    @FXML private VBox logoutButtonContainer;

    private DefaultLabel adminHomeLabel;
    private DefaultButton logoutButton;

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
        // thinking what to use
    }

    private void initUserInterface() {
        adminHomeLabel = DefaultLabel.h2("Home | Super Admin | " + account.getUsername());
        logoutButton = DefaultButton.primary("Logout");

        adminHomeLabelContainer.getChildren().add(adminHomeLabel);
        logoutButtonContainer.getChildren().add(logoutButton);
    }

    private void initEvents() {
        logoutButton.setOnAction(e -> onLogoutButtonClick());
    }

    protected void onLogoutButtonClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Logout");
        alert.setHeaderText(null);
        alert.setContentText("คุณต้องการออกจากระบบหรือไม่?");
        alert.showAndWait().ifPresent(btn -> {
            SessionManager.logout();
            try {
                FXRouter.goTo("admin-login");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    protected void redirectToLogin() {
        try {
            FXRouter.goTo("admin-login");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
