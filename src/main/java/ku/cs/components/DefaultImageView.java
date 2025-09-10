package ku.cs.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DefaultImageView extends ImageView {

    public DefaultImageView(String imagePath, double width, double height) {
        super();
        try {
            Image image = new Image(imagePath, width, height, true, true);
            setImage(image);
        } catch (Exception e) {
            setImage(new Image(getClass().getResource("/images/placeholder.png").toExternalForm())); // เดี๋ยวค่อยทำรูป
        }
        setFitWidth(width);
        setFitHeight(height);
        setPreserveRatio(true);
        setSmooth(true);
    }
}