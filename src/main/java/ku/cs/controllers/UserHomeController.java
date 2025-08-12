package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import ku.cs.components.DefaultLabel;

public class UserHomeController {
    @FXML private VBox userHomeLabelContainer;

    @FXML
    public void initialize() {
        DefaultLabel userHomeLabel = DefaultLabel.h2("Home");
        userHomeLabelContainer.getChildren().add(userHomeLabel);
    }
}
