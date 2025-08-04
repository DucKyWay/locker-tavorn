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
    @FXML private ImageView imageblurView2;
    @FXML private VBox backButtonContainer;

    //public void init
    @FXML
    private void initialize(){

        DefaultButton backButton = DefaultButton.info("ย้อนกลับ");
        backButton.setOnAction(event -> onBackButtonClick());
        backButtonContainer.getChildren().add(backButton);

        String imagePath = "/images/developer/" + "Developer1" + ".png";
        Image image = new Image(getClass().getResource(imagePath).toExternalForm());
        String imageblurPath = "/images/developer/" + "Frame_12" + ".png";
        Image imageblur = new Image(getClass().getResource(imageblurPath).toExternalForm());
        imageblurView2.setImage(imageblur);
        imageView1.setImage(image);
        imageView2.setImage(image);
        imageView3.setImage(image);
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
