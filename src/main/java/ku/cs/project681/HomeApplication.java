package ku.cs.project681;

import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ku.cs.services.FXRouter;
import ku.cs.services.FontLoader;

import java.io.IOException;

public class HomeApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FontLoader.loadBaiJamjureeFonts();
        FontLoader.loadPhosphorIcons();

        configRoutes();

        FXRouter.bind(this, stage, "Project 2568/1", 1024, 768);
        stage.setResizable(false);
        FXRouter.goTo("home");
    }

    private void configRoutes() {
        String viewPath = "ku/cs/views/";
        FXRouter.when("home", viewPath + "home-view.fxml");
        FXRouter.when("developer", viewPath + "developer.fxml");

        FXRouter.when("user-register", viewPath + "user/user-register.fxml");
        FXRouter.when("user-login", viewPath + "user/user-login.fxml");
        FXRouter.when("user-home", viewPath + "user/user-home.fxml");
        FXRouter.when("user-zone", viewPath + "user/user-zone.fxml");
        FXRouter.when("user-history", viewPath + "user/user-history.fxml");

        FXRouter.when("officer-login", viewPath + "officer/officer-login.fxml");
        FXRouter.when("officer-home", viewPath + "officer/officer-home.fxml");
        FXRouter.when("officer-key-list", viewPath + "officer/officer-key-list.fxml");

        FXRouter.when("admin-login", viewPath + "admin/admin-login-view.fxml");
        FXRouter.when("admin-home", viewPath + "admin/admin-home-view.fxml");
        FXRouter.when("admin-manage-officers", viewPath + "admin/admin-manage-officers-view.fxml");
        FXRouter.when("admin-manage-new-officer", viewPath + "admin/admin-manage-new-officer-view.fxml");
        FXRouter.when("admin-manage-officer-details", viewPath + "admin/admin-manage-officer-details-view.fxml");
        FXRouter.when("admin-manage-users",viewPath + "admin/admin-manage-users-view.fxml");

        FXRouter.when("locker-list", viewPath + "test/locker-list-view.fxml");
        FXRouter.when("typo", viewPath + "test/typography-view.fxml");
        FXRouter.when("test-user", viewPath + "test/test-user.fxml");
        FXRouter.when("test-button", viewPath + "test/button-view.fxml");
        FXRouter.when("test-zonelist", viewPath + "test/zone-list-view.fxml");

        // DialogPane
        FXRouter.when("locker-reserve", viewPath + "locker/locker-reserve-dialog-view.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}