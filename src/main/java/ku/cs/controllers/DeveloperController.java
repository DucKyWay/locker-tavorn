package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.components.Icons;
import ku.cs.components.button.ElevatedButton;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.services.ui.FXRouter;

import java.io.IOException;
import java.util.Objects;

public class DeveloperController {

    @FXML private Button backButton;
    @FXML private Button goToAdminLoginButton;

    @FXML private Label displayLabel;
    @FXML private Label subDisplayLabel;

    // NIJI
    @FXML private ImageView nijiImageView;
    @FXML private Label nijiNameLabel;
    @FXML private Label nijiIdLabel;

    // MANUS
    @FXML private ImageView manusImageView;
    @FXML private Label manusNameLabel;
    @FXML private Label manusIdLabel;

    // ATHI
    @FXML private ImageView athiImageView;
    @FXML private Label athiNameLabel;
    @FXML private Label athiIdLabel;

    @FXML
    private void initialize(){
        String imageRoot = "/ku/cs/images/developer/";

        String imageNijiPath = imageRoot + "Developer-Niji" + ".png";
        String imageManusPath = imageRoot + "Developer-Manus" + ".png";
        String imageAthiPath = imageRoot + "Developer-Athi" + ".png";

        Image imageNiji = new Image(Objects.requireNonNull(getClass().getResource(imageNijiPath)).toExternalForm());
        Image imageManus = new Image(Objects.requireNonNull(getClass().getResource(imageManusPath)).toExternalForm());
        Image imageAthi = new Image(Objects.requireNonNull(getClass().getResource(imageAthiPath)).toExternalForm());
        nijiImageView.setImage(imageNiji);
        manusImageView.setImage(imageManus);
        athiImageView.setImage(imageAthi);

        initUserInterface();
        initEvents();
    }

    private void initUserInterface() {
        if (goToAdminLoginButton  != null)
            ElevatedButton.SMALL.mask(goToAdminLoginButton);
        if (backButton != null)
            ElevatedButtonWithIcon.SMALL.mask(backButton, Icons.ARROW_LEFT);
    }

    private void initEvents() {
        goToAdminLoginButton.setOnAction(e -> goToAdminLoginButtonClick());
        backButton.setOnAction(e -> onBackButtonClick());
    }

    protected void goToAdminLoginButtonClick() {
        try {
            FXRouter.goTo("admin-login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onBackButtonClick() {
        try {
            FXRouter.goTo("user-login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
