package ku.cs.controllers;

import javafx.fxml.FXML;
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
    @FXML private VBox officerLoginButtonContainer;
    @FXML private VBox infoButtonContainer;

    private DefaultButton helloButton;
    private DefaultButton userLoginButton;
    private DefaultButton officerLoginButton;
    private DefaultButton infoButton;

    @FXML
    public void initialize() {
        initUserInterface();
        initEvents();
    }

    private void initUserInterface() {

        DefaultLabel headerLabel = DefaultLabel.title("Locker Tavorn");
        helloButton = DefaultButton.iconButton(
                "สวัสดีโลก!",
                "",
                "#ffc107",
                "black"
        );
        userLoginButton = DefaultButton.success("เข้าสู่ระบบผู้ใช้");
        officerLoginButton = DefaultButton.outline("เข้าสู่ระบบพนักงาน");
        infoButton = DefaultButton.outline("About Team");

        headerLabelContainer.getChildren().add(headerLabel);
        helloButtonContainer.getChildren().add(helloButton);
        userLoginButtonContainer.getChildren().add(userLoginButton);
        officerLoginButtonContainer.getChildren().add(officerLoginButton);
        infoButtonContainer.getChildren().add(infoButton);
    }

    private void initEvents() {
        helloButton.setOnAction(event -> onHelloButtonClick());
        userLoginButton.setOnAction(event -> onUserLoginButtonClick());
        officerLoginButton.setOnAction(event -> onOfficerLoginButtonClick());
        infoButton.setOnAction(event -> onInfoButtonClick());
    }

    @FXML
    protected void onHelloButtonClick() {
        DefaultLabel welcomeLabel = new DefaultLabel("สวัสดี Project ที่สดใส!");
        welcomeLabel.setStyle("-fx-text-fill: #418211");
        welcomeLabelContainer.getChildren().add(welcomeLabel);
    }

    @FXML
    protected void onUserLoginButtonClick() {
        try {
            FXRouter.goTo("user-login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onOfficerLoginButtonClick() {
        try {
            FXRouter.goTo("officer-login");
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