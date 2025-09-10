package ku.cs.components.button;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.TextAlignment;

import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.StyleMasker;

public class FilledButtonWithIcon extends CustomButtonWithIcon{
    public static final StyleMasker DEFAULT = new StyleMasker("filled-button-with-icon");
    public static final StyleMasker MEDIUM = new StyleMasker("filled-button-with-icon", "medium");
    public static final StyleMasker SMALL  = new StyleMasker("filled-button-with-icon", "small");

    public FilledButtonWithIcon(String label) {
        this(label, null, null);
    }

    public FilledButtonWithIcon(String label, Icons iconLeft) {
        this(label, iconLeft, null);
    }

    public FilledButtonWithIcon(String label, Icons iconLeft, Icons iconRight) {
        super(label, iconLeft, iconRight);
    }

    @Override
    protected void initializeStyle() {
        getStyleClass().add("filled-button-with-icon");
    }

    public static FilledButtonWithIcon small(String label) {
        return small(label, null, null);
    }

    public static FilledButtonWithIcon small(String label, Icons icon) {
        return small(label, icon, null);
    }

    public static FilledButtonWithIcon small(String label, Icons iconLeft, Icons iconRight) {
        FilledButtonWithIcon b = new FilledButtonWithIcon(label, iconLeft, iconRight);
        b.getStyleClass().add("small");
        return b;
    }

    public static FilledButtonWithIcon medium(String label) {
        return small(label, null, null);
    }

    public static FilledButtonWithIcon medium(String label, Icons icon) {
        return small(label, icon, null);
    }

    public static FilledButtonWithIcon medium(String label, Icons iconLeft, Icons iconRight) {
        FilledButtonWithIcon b = new FilledButtonWithIcon(label, iconLeft, iconRight);
        b.getStyleClass().add("medium");
        return b;
    }

    public static void mask(Button button, Icons iconLeft, Icons iconRight) {
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

        DEFAULT.mask(button);
        button.setGraphic(content);
    }
}
