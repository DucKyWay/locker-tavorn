package ku.cs.project681;

import javafx.application.Application;
import javafx.stage.Stage;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class HomeApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        configRoutes();
        FXRouter.bind(this, stage, "Project 2568/1", 1024, 768);
        FXRouter.goTo("home");
    }

    private void configRoutes() {
        String viewPath = "ku/cs/views/";
        FXRouter.when("home", viewPath + "home-view.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}