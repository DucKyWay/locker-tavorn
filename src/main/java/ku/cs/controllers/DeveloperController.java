package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class DeveloperController {
    @FXML private ImageView nijiImageView;
    @FXML private VBox nijiLabelContainer;
    @FXML private VBox nijiIdLabelContainer;
    @FXML private ImageView manusImageView;
    @FXML private VBox manusLabelContainer;
    @FXML private VBox manusIdLabelContainer;
    @FXML private ImageView athiImageView;
    @FXML private VBox athiLabelContainer;
    @FXML private VBox athiIdLabelContainer;

    @FXML private VBox backButtonContainer;
    @FXML private VBox titleLabelContainer;
    @FXML private VBox copyRightLabelContainer;

    //public void init
    @FXML
    private void initialize(){

        DefaultButton backButton = DefaultButton.info("ย้อนกลับ");
        backButton.setOnAction(event -> onBackButtonClick());
        backButtonContainer.getChildren().add(backButton);

        DefaultLabel titleLabel = DefaultLabel.custom("ROD F 211", 40, FontWeight.BLACK, false, FontPosture.REGULAR, "black", "transparent");
        titleLabelContainer.getChildren().add(titleLabel);

        DefaultLabel copyrightLabel = DefaultLabel.copyright();
        copyRightLabelContainer.getChildren().add(copyrightLabel);

        DefaultLabel nijiLabel = DefaultLabel.devName("NIJI");
        DefaultLabel nijiIdLabel = DefaultLabel.devId("6710405541");
        nijiLabelContainer.getChildren().add(nijiLabel);
        nijiIdLabelContainer.getChildren().add(nijiIdLabel);

        DefaultLabel manusLabel = DefaultLabel.devName("MANUS");
        DefaultLabel manusIdLabel = DefaultLabel.devId("6710405451");
        manusLabelContainer.getChildren().add(manusLabel);
        manusIdLabelContainer.getChildren().add(manusIdLabel);

        DefaultLabel athiLabel = DefaultLabel.devName("ATHI");
        DefaultLabel athiIdLabel = DefaultLabel.devId("6710405559");
        athiLabelContainer.getChildren().add(athiLabel);
        athiIdLabelContainer.getChildren().add(athiIdLabel);

        String imageRoot = "/images/developer/";

        String imageNijiPath = imageRoot + "Developer-Niji" + ".png";
        String imageManusPath = imageRoot + "Developer-Manus" + ".png";
        String imageAthiPath = imageRoot + "Developer-Athi" + ".png";

        Image imageNiji = new Image(getClass().getResource(imageNijiPath).toExternalForm());
        Image imageManus = new Image(getClass().getResource(imageManusPath).toExternalForm());
        Image imageAthi = new Image(getClass().getResource(imageAthiPath).toExternalForm());

        nijiImageView.setImage(imageNiji);
        manusImageView.setImage(imageManus);
        athiImageView.setImage(imageAthi);

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
