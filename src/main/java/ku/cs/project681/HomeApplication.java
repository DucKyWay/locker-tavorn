package ku.cs.project681;

import javafx.application.Application;
import javafx.stage.Stage;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.ui.FontLoader;

import java.io.IOException;

public class HomeApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FontLoader.loadBaiJamjureeFonts();
        FontLoader.loadPhosphorIcons();

        configRoutes();

        FXRouter.bind(this, stage, "Project 2568/1", 1024, 768);
        stage.setResizable(false);
        FXRouter.goTo("user-login");
    }

    private void configRoutes() {
        String viewPath = "ku/cs/views/";

        // Main
        FXRouter.when("home", viewPath + "home-view.fxml");
        FXRouter.when("developer", viewPath + "developer-view.fxml");

        // User
        FXRouter.when("user-register", viewPath + "user/user-register-view.fxml");
        FXRouter.when("user-register-2", viewPath + "user/user-register-2-view.fxml");
        FXRouter.when("user-login", viewPath + "user/user-login-view.fxml");
        FXRouter.when("user-home", viewPath + "user/user-home-view.fxml");
        FXRouter.when("user-zone", viewPath + "user/user-zone-view.fxml");
        FXRouter.when("user-history", viewPath + "user/user-history-view.fxml");
        FXRouter.when("user-locker", viewPath + "user/user-locker-view.fxml");

        // Officer
        FXRouter.when("officer-login", viewPath + "officer/officer-login-view.fxml");
        FXRouter.when("officer-first-login", viewPath + "officer/officer-first-login-view.fxml");
        FXRouter.when("officer-home", viewPath + "officer/officer-home-view.fxml");
        FXRouter.when("officer-key-list", viewPath + "officer/officer-key-list-view.fxml");
        FXRouter.when("officer-zone-list", viewPath + "officer/officer-zone-list-view.fxml");
        FXRouter.when("officer-select-key-list", viewPath + "officer/officer-select-key-list-view.fxml");
        FXRouter.when("officer-message-reject", viewPath + "officer/officer-message-reject-view.fxml");
        FXRouter.when("officer-passkey-digital", viewPath + "officer/officer-passkey-digital-view.fxml");
        FXRouter.when("officer-locker", viewPath + "officer/officer-locker-view.fxml");
        FXRouter.when("officer-locker-dialog", viewPath + "officer/officer-locker-dialog-view.fxml");
        FXRouter.when("officer-history-request", viewPath + "officer/officer-request-history.fxml");
        // Admin
        FXRouter.when("admin-login", viewPath + "admin/admin-login-view.fxml");
        FXRouter.when("admin-home", viewPath + "admin/admin-home-view.fxml");
        // Admin manage account
        FXRouter.when("admin-display-accounts", viewPath + "admin/admin-display-accounts-view.fxml");
        FXRouter.when("admin-manage-officers", viewPath + "admin/admin-manage-officers-view.fxml");
        FXRouter.when("admin-manage-new-officer", viewPath + "admin/admin-manage-new-officer-view.fxml");
        FXRouter.when("admin-manage-officer-details", viewPath + "admin/admin-manage-officer-details-view.fxml");
        FXRouter.when("admin-display-officer-zones", viewPath + "admin/admin-display-officer-zones-view.fxml");
        FXRouter.when("admin-manage-users",viewPath + "admin/admin-manage-users-view.fxml");
        // Admin manage system
        FXRouter.when("admin-manage-zones", viewPath + "admin/admin-manage-zones-view.fxml");

        // test
        FXRouter.when("typo", viewPath + "test/typography-view.fxml");
        FXRouter.when("test-user", viewPath + "test/test-user.fxml");
        FXRouter.when("test-button", viewPath + "test/button-view.fxml");
        FXRouter.when("test-zonelist", viewPath + "test/zone-list-view.fxml");

        // DialogPane
        FXRouter.when("locker-reserve", viewPath + "locker/locker-reserve-dialog-view.fxml");
        FXRouter.when("locker-dialog", viewPath + "locker/locker-dialog-view.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}