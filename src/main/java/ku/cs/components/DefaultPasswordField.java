package ku.cs.components;

import javafx.scene.control.PasswordField;
import javafx.scene.text.Font;

public class DefaultPasswordField extends PasswordField {
    public DefaultPasswordField (String placeholder) {
        super();
        setPromptText(placeholder);
        setFont(Font.font("Bai Jamjuree", 14));
        setPrefWidth(200);
        setStyle("-fx-border-color: #ccc; -fx-background-color: #fff;");
    }
}
