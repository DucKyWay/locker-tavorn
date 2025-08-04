package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class HomeController {
    @FXML private VBox headerLabelContainer;
    @FXML private VBox helloButtonContainer;
    @FXML private VBox welcomeLabelContainer;
    @FXML private VBox infoButtonContainer;

    @FXML
    public void initialize() {
        DefaultLabel headerLabel = DefaultLabel.title("Locker Tavorn");
        headerLabelContainer.getChildren().add(headerLabel);

        DefaultButton helloButton = DefaultButton.primary("สวัสดีโลก!");
        helloButton.setOnAction(event -> onHelloButtonClick());
        helloButtonContainer.getChildren().add(helloButton);

        DefaultButton infoButton = DefaultButton.outline("About Team");
        infoButton.setOnAction(event -> onInfoButtonClick());
        infoButtonContainer.getChildren().add(infoButton);
    }

    @FXML
    protected void onHelloButtonClick() {
        DefaultLabel welcomeLabel = new DefaultLabel("สวัสดี Project ที่สดใส!");
        welcomeLabel.setStyle("-fx-text-fill: #418211");
        welcomeLabelContainer.getChildren().add(welcomeLabel);
    }

    @FXML
    protected void onInfoButtonClick() {
        try {
            FXRouter.goTo("developer");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}