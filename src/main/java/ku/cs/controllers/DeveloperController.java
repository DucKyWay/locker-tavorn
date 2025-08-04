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

    @FXML private VBox backButtonContainer;

    //public void init
    @FXML
    private void initialize(){

        DefaultButton backButton = DefaultButton.info("ย้อนกลับ");
        backButton.setOnAction(event -> onBackButtonClick());
        backButtonContainer.getChildren().add(backButton);

        String imagePath = "/images/developer/" + "Developer1" + ".png";
        // สมมติไฟล์อยู่ใน resources/image/cars/
        // โหลดจาก classpath
        Image image = new Image(getClass().getResource(imagePath).toExternalForm());
        imageView1.setImage(image);
    }
    public void showTeamCar(String teamName) {
        String formattedTeamName = teamName.toLowerCase().replaceAll("\\s+", "");
        // 2) สร้าง path รูป
        String imagePath = "/images/developer/" + formattedTeamName + ".png";
        // สมมติไฟล์อยู่ใน resources/image/cars/
        // โหลดจาก classpath
        Image image = new Image(getClass().getResource(imagePath).toExternalForm());
        imageView1.setImage(image);
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
