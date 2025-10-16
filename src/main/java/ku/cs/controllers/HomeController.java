package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ku.cs.components.Icons;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.FilledButton;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.services.ui.FXRouter;

import java.io.IOException;

public class HomeController {
    @FXML private VBox headerLabelContainer;
    @FXML private VBox helloButtonContainer;
    @FXML private VBox welcomeLabelContainer;
    @FXML private VBox userLoginButtonContainer;
    @FXML private VBox officerLoginButtonContainer;
    @FXML private VBox infoButtonContainer;

    private FilledButtonWithIcon helloButton;
    private FilledButton userLoginButton;
    private FilledButton officerLoginButton;
    private FilledButton infoButton;
    private Label headerLabel;

    @FXML
    public void initialize() {
        initUserInterface();
        initEvents();
    }

    private void initUserInterface() {

        headerLabel = new Label("Locker Tavorn");
        LabelStyle.DISPLAY_LARGE.applyTo(headerLabel);
        helloButton = new FilledButtonWithIcon(
                "สวัสดีโลก!", Icons.HOME
        );
        userLoginButton = new FilledButton("เข้าสู่ระบบผู้ใช้");
        officerLoginButton = new FilledButton("เข้าสู่ระบบเจ้าหน้าที่");
        infoButton = new FilledButton("About Team");

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
        Label welcomeLabel = new Label("สวัสดี Project ที่สดใส!");
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