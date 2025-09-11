package ku.cs.components;

import javafx.scene.control.Button;

import java.util.Arrays;
import java.util.List;

public class StyleMasker {
    private final List<String> classes;

    public StyleMasker(String... styles) {
        this.classes = Arrays.asList(styles);
    }

    public void mask(Button target) {
        for (String style : classes) {
            if (!target.getStyleClass().contains(style)) {
                target.getStyleClass().add(style);
            }
        }
    }
}