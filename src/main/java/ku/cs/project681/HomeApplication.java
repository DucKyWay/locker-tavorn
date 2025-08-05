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

        configRoutes();

        FXRouter.bind(this, stage, "Project 2568/1", 1024, 768);
        stage.setResizable(false);
        FXRouter.goTo("home");
    }

    private void configRoutes() {
        String viewPath = "ku/cs/views/";
        FXRouter.when("home", viewPath + "home-view.fxml");
        FXRouter.when("developer", viewPath + "developer.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}