package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;

import java.io.IOException;

public class UserHomeController {
    @FXML private VBox userHomeLabelContainer;
    @FXML private VBox lockerListButtonContainer;
    @FXML private VBox logoutButtonContainer;

    private DefaultLabel userHomeLabel;
    private DefaultButton lockerListButton;
    private DefaultButton logoutButton;

    @FXML
    public void initialize() {
        // Auth Guard
        SessionManager.requireLogin();
        
        initUserInterface();
        initEvents();
    }

    private void initUserInterface() {
        userHomeLabel = DefaultLabel.h2("Home");
        lockerListButton = DefaultButton.primary("Locker List");
        logoutButton = DefaultButton.primary("Logout");

        userHomeLabelContainer.getChildren().add(userHomeLabel);
        lockerListButtonContainer.getChildren().add(lockerListButton);
        logoutButtonContainer.getChildren().add(logoutButton);
    }

    private void initEvents() {
        lockerListButton.setOnAction(e -> onLockerListButtonClick());
        logoutButton.setOnAction(e -> onLogoutButtonClick());
    }

    protected void onLockerListButtonClick() {
        try {
            FXRouter.goTo("locker-list");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void onLogoutButtonClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Logout");
        alert.setHeaderText(null);
        alert.setContentText("คุณต้องการออกจากระบบหรือไม่?");
        alert.showAndWait().ifPresent(btn -> {
            SessionManager.logout();

            try {
                FXRouter.goTo("user-login");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    protected void redirectToLogin() {
        try {
            FXRouter.goTo("user-login");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
