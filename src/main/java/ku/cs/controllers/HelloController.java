package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Start your project.\nYou can delete hello-view.fxml");
        welcomeText.setStyle("-fx-text-fill: #418211");
    }
}