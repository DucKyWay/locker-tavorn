package ku.cs.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.TextAlignment;

import java.util.Arrays;
import java.util.List;

public class StyleMasker {
    private final List<String> classes;

    public StyleMasker(String... styles) {
        this.classes = Arrays.asList(styles);
    }

    public void mask(Button button) {
        for (String style : classes) {
            if (!button.getStyleClass().contains(style)) {
                button.getStyleClass().add(style);
            }
        }
    }

    public void mask(Button button, Icons iconLeft) {
        mask(button, iconLeft, null);
    }

    public void mask(Button button, Icons iconLeft, Icons iconRight) {
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER);

        if (iconLeft != null) {
            Icon left = new Icon(iconLeft);
            Region spacerL = new Region(); spacerL.setPrefWidth(12);
            content.getChildren().addAll(left, spacerL);
        }

        if (button.getText() != null && !button.getText().isEmpty()) {
            Label lbl = new Label(button.getText());
            button.setText(null);
            lbl.setMaxWidth(Double.MAX_VALUE);
            lbl.setAlignment(Pos.CENTER);
            lbl.setTextAlignment(TextAlignment.CENTER);
            HBox.setHgrow(lbl, Priority.ALWAYS);
            content.getChildren().add(lbl);
        }

        if (iconRight != null) {
            Region spacerR = new Region(); spacerR.setPrefWidth(12);
            Icon right = new Icon(iconRight);
            content.getChildren().addAll(spacerR, right);
        }

        for (String style : classes) {
            if (!button.getStyleClass().contains(style)) {
                button.getStyleClass().add(style);
            }
        }

        button.setGraphic(content);
    }

    public void mask(Button button, Icon icon)
    {
        button.setContentDisplay(ContentDisplay.CENTER);
        button.setAlignment(Pos.CENTER);
        button.setTextAlignment(TextAlignment.CENTER);
        button.setPadding(Insets.EMPTY);
        mask(button);
        button.setText(icon.toString());
    }
}