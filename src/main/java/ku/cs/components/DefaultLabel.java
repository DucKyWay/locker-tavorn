package ku.cs.components;

import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class DefaultLabel extends Label {
    public DefaultLabel(String text) {
        super(text);
        setFont(Font.font("Bai Jamjuree", 14));
        setStyle("-fx-text-fill: #000;");
    }

    public static DefaultLabel title(String text) {
        DefaultLabel label = new DefaultLabel(text);
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold");
        return label;
    }

    public static DefaultLabel header(String text) {
        DefaultLabel label = new DefaultLabel(text);
        label.setStyle("-fx-font-size: 24px;");
        return label;
    }
}
