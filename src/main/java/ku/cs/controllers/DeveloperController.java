package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ku.cs.components.DefaultButton;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class DeveloperController {
    @FXML private Label nameLabel;
    @FXML private ImageView imageView1;
    @FXML private ImageView imageView2;
    @FXML private ImageView imageView3;
//    @FXML private ImageView imageblurView2;
    @FXML private VBox backButtonContainer;

    //public void init
    @FXML
    private void initialize(){

        DefaultButton backButton = DefaultButton.info("ย้อนกลับ");
        backButton.setOnAction(event -> onBackButtonClick());
        backButtonContainer.getChildren().add(backButton);

        String imageRoot = "/images/developer/";
        String imageManusPath = imageRoot + "Developer-Manus" + ".png";
        // NIJI Edit here
        String imageNijiPath = imageRoot + "Developer-Manus" + ".png";
        String imageAthiPath = imageRoot + "Developer-Athi" + ".png";

        Image imageManus = new Image(getClass().getResource(imageManusPath).toExternalForm());
        Image imageNiji = new Image(getClass().getResource(imageNijiPath).toExternalForm());
        Image imageAthi = new Image(getClass().getResource(imageAthiPath).toExternalForm());

        imageView1.setImage(imageNiji);
        imageView2.setImage(imageManus);
        imageView3.setImage(imageAthi);
    }

    @FXML
    protected void onBackButtonClick() {
        try {
            FXRouter.goTo("home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
