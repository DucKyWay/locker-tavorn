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

public class OutlinedButtonWithIcon extends CustomButtonWithIcon{
    public static final StyleMasker DEFAULT = new StyleMasker("outlined-button-with-icon");
    public static final StyleMasker MEDIUM = new StyleMasker("outlined-button-with-icon", "medium");
    public static final StyleMasker SMALL  = new StyleMasker("outlined-button-with-icon", "small");

    public OutlinedButtonWithIcon(String label) {
        this(label, null, null);
    }

    public OutlinedButtonWithIcon(String label, Icons iconLeft) {
        this(label, iconLeft, null);
    }

    public OutlinedButtonWithIcon(String label, Icons iconLeft, Icons iconRight) {
        super(label, iconLeft, iconRight);
    }

    @Override
    protected void initializeStyle() {
        getStyleClass().add("outlined-button-with-icon");
    }

    public static OutlinedButtonWithIcon small(String label) {
        return small(label, null, null);
    }

    public static OutlinedButtonWithIcon small(String label, Icons icon) {
        return small(label, icon, null);
    }

    public static OutlinedButtonWithIcon small(String label, Icons iconLeft, Icons iconRight) {
        OutlinedButtonWithIcon b = new OutlinedButtonWithIcon(label, iconLeft, iconRight);
        b.getStyleClass().add("small");
        return b;
    }

    public static OutlinedButtonWithIcon medium(String label) {
        return small(label, null, null);
    }

    public static OutlinedButtonWithIcon medium(String label, Icons icon) {
        return small(label, icon, null);
    }

    public static OutlinedButtonWithIcon medium(String label, Icons iconLeft, Icons iconRight) {
        OutlinedButtonWithIcon b = new OutlinedButtonWithIcon(label, iconLeft, iconRight);
        b.getStyleClass().add("medium");
        return b;
    }

    public static void mask(Button button, Icons iconLeft) {
        mask(button, iconLeft, null);
    }

    public static void mask(Button button, Icons iconLeft, Icons iconRight) {
        DEFAULT.mask(button, iconLeft, iconRight);
    }
}
