package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HomeController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Start your project.\nYou can delete home" + "home-view.fxml");
        welcomeText.setStyle("-fx-text-fill: #418211");
    }
}