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
    @FXML private VBox userLoginButtonContainer;
    @FXML private VBox infoButtonContainer;

    @FXML
    public void initialize() {
        DefaultLabel headerLabel = DefaultLabel.title("Locker Tavorn");
        DefaultButton helloButton = DefaultButton.iconButton(
                "สวัสดีโลก!",
                "/icons/house-solid-full.png",
                "#ffc107",
                "black"
        );
        DefaultButton userLoginButton = DefaultButton.success("เข้าสู่ระบบผู้ใช้");
        DefaultButton infoButton = DefaultButton.outline("About Team");

        helloButton.setOnAction(event -> onHelloButtonClick());
        userLoginButton.setOnAction(event -> onUserButtonClick());
        infoButton.setOnAction(event -> onInfoButtonClick());

        headerLabelContainer.getChildren().add(headerLabel);
        helloButtonContainer.getChildren().add(helloButton);
        userLoginButtonContainer.getChildren().add(userLoginButton);
        infoButtonContainer.getChildren().add(infoButton);
    }

    @FXML
    protected void onHelloButtonClick() {
        DefaultLabel welcomeLabel = new DefaultLabel("สวัสดี Project ที่สดใส!");
        welcomeLabel.setStyle("-fx-text-fill: #418211");
        welcomeLabelContainer.getChildren().add(welcomeLabel);
    }

    @FXML
    protected void onUserButtonClick() {
        try {
            FXRouter.goTo("user-login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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