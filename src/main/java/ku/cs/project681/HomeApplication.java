package ku.cs.project681;

import javafx.application.Application;
import javafx.stage.Stage;
import ku.cs.services.session.SessionManager;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.ui.FontLoader;

import java.awt.*;
import java.io.IOException;

public class HomeApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        SessionManager sm = new SessionManager();
        FXRouter.bindService("session", sm);

        FontLoader.loadBaiJamjureeFonts();
        FontLoader.loadSarabunFonts();
        FontLoader.loadPhosphorIcons();

        configRoutes();

        FXRouter.bind(this, stage, "Project 2568/1", 1024, 768);
        stage.setResizable(false);
        FXRouter.goTo("user-login");
    }

    private void configRoutes() {
        String viewPath = "ku/cs/views/";

    /*
     *  Main
     *  ====================================================================
     */
        FXRouter.when("home", viewPath + "home-view.fxml");
        FXRouter.when("developer", viewPath + "developer-view.fxml");
        FXRouter.when("read-pdf", viewPath + "test-pdf.fxml");
    /*
     *  User
     *  ====================================================================
     */
        FXRouter.when("user-register", viewPath + "user/user-register-view.fxml");
        FXRouter.when("user-register-2", viewPath + "user/user-register-2-view.fxml");
        FXRouter.when("user-login", viewPath + "user/user-login-view.fxml");
        FXRouter.when("user-home", viewPath + "user/user-home-view.fxml");

        // User Menu
        FXRouter.when("user-my-locker", viewPath + "user/user-my-locker-view.fxml");
        FXRouter.when("user-zone", viewPath + "user/user-select-zone-view.fxml");
        FXRouter.when("user-history", viewPath + "user/user-history-view.fxml");

        // User Select Locker for Request
        FXRouter.when("user-select-locker", viewPath + "user/user-select-locker-view.fxml");

    /*
     *  Officer
     *  ====================================================================
     */
        FXRouter.when("officer-login", viewPath + "officer/officer-login-view.fxml");
        FXRouter.when("officer-first-login", viewPath + "officer/officer-first-login-view.fxml");
        FXRouter.when("officer-select-zone", viewPath+ "officer/officer-zone-list-view.fxml");
        FXRouter.when("officer-home", viewPath + "officer/officer-home-view.fxml");

        // Officer Manage Lockers
        FXRouter.when("officer-manage-lockers", viewPath + "officer/officer-manage-lockers-view.fxml");
        FXRouter.when("officer-locker-dialog", viewPath + "officer/officer-locker-dialog-view.fxml");

        // Officer Manage Keys
        FXRouter.when("officer-manage-keys", viewPath + "officer/officer-manage-keys-view.fxml");
        FXRouter.when("officer-select-key-list", viewPath + "officer/officer-select-key-list-view.fxml");
        FXRouter.when("officer-passkey-digital", viewPath + "officer/officer-passkey-digital-view.fxml");

        // Officer Manage Request
        FXRouter.when("officer-zone-request", viewPath + "officer/officer-zone-request-view.fxml");
        FXRouter.when("officer-message-reject", viewPath + "officer/officer-message-reject-view.fxml");
        FXRouter.when("officer-history-request", viewPath + "officer/officer-request-history.fxml");

        // Officer Locker Dialog
        FXRouter.when("officer-display-locker-history", viewPath + "officer/dialog/officer-display-locker-history-view.fxml");

        // Officer Manual PDF
        FXRouter.when("officer-manual", viewPath + "officer/officer-manual.fxml");
    /*
     *  Admin
     *  ====================================================================
     */
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

        // Admin Dialog
        FXRouter.when("admin-add-new-zone", viewPath + "admin/dialog/admin-add-new-zone-dialog.fxml");
        FXRouter.when("admin-edit-zone-name", viewPath + "admin/dialog/admin-edit-zone-name-dialog.fxml");

    /*
     *  Test
     *  ====================================================================
     */
        FXRouter.when("typo", viewPath + "test/typography-view.fxml");
        FXRouter.when("test-button", viewPath + "test/button-view.fxml");

    /*
     *  DialogPane
     *  ====================================================================
     */
        FXRouter.when("locker-reserve", viewPath + "locker/locker-reserve-dialog-view.fxml");
        FXRouter.when("locker-dialog", viewPath + "locker/locker-dialog-view.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}