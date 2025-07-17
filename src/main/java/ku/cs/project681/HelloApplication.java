package ku.cs.project681;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        configRoutes();
        FXRouter.bind(this, stage, "Project 2568/1", 1024, 768);
        FXRouter.goTo("hello");
    }

    private void configRoutes() {
        String viewPath = "ku/cs/views/";
        FXRouter.when("hello", viewPath + "hello-view.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}