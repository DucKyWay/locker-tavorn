package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class UserHomeController {
    @FXML private VBox userHomeLabelContainer;
    @FXML private VBox lockerListButtonContainer;

    @FXML
    public void initialize() {
        DefaultLabel userHomeLabel = DefaultLabel.h2("Home");
        userHomeLabelContainer.getChildren().add(userHomeLabel);

        DefaultButton lockerListButton = DefaultButton.primary("Locker List");
        lockerListButtonContainer.getChildren().add(lockerListButton);
        lockerListButton.setOnAction(e -> {
            try {
                FXRouter.goTo("locker-list");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
