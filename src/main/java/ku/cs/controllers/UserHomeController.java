package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import ku.cs.components.DefaultLabel;

public class UserHomeController {
    @FXML private VBox userHoneLabelContainer;

    @FXML
    public void initialize() {
        DefaultLabel userHomeLabel = DefaultLabel.h2("Home");
        userHoneLabelContainer.getChildren().add(userHomeLabel);
    }
}
