package ku.cs.components;

import javafx.scene.control.TextField;
import javafx.scene.text.Font;

public class DefaultTextField extends TextField {

    public DefaultTextField(String placeholder) {
        super(placeholder);
        setFont(Font.font("Bai Jamjuree", 14));
        setPromptText("กรอกข้อความ...");
        setPrefWidth(200);
    }
}