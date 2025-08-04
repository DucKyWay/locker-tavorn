package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DeveloperController {
    @FXML
    private Label nameLabel;
    @FXML
    private ImageView imageView1;
    //public void init
    @FXML
    private void initialize(){
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
}
