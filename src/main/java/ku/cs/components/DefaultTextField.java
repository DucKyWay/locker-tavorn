package ku.cs.components;

import javafx.scene.control.TextField;
import javafx.scene.text.Font;

public class DefaultTextField extends TextField {

    public DefaultTextField(String placeholder) {
        super();
        setFont(Font.font("Bai Jamjuree", 14));
        setPromptText(placeholder);
        setPrefWidth(200);
    }
}