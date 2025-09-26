package ku.cs.components.button;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.text.TextAlignment;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.ReplaceableInParent;
import ku.cs.components.StyleMasker;

import javax.swing.*;
import java.util.Objects;

public class IconButton extends CustomButton {
    public static final StyleMasker DEFAULT = new StyleMasker("icon-button");
    public static final StyleMasker ERROR = new StyleMasker("icon-button", "error");
//    public static final StyleMasker SMALL  = new StyleMasker("icon-button", "small");

    public IconButton(Icon icon) {
        super(icon.toString());
        initializeStyle();
    }

    @Override
    protected void initializeStyle() {
        ensureStyleClassPresent(this,  "icon-button");
    }

    @Override
    public IconButton self() { return this; }

    public static IconButton error(Icon icon) {
        IconButton b = new IconButton(icon);
        b.getStyleClass().add("error");
        return b;
    }

    public static void mask(Button button, Icon icon) {
        DEFAULT.mask(button, icon);
    }
}
