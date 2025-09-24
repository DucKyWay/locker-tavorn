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
//    public static final StyleMasker MEDIUM = new StyleMasker("icon-button", "medium");
//    public static final StyleMasker SMALL  = new StyleMasker("icon-button", "small");

    public IconButton(Icon icon) {
        super("");
        this.setGraphic(icon);
        initializeStyle();
    }

    public IconButton(Icon icon, int size) {
        super("");
        this.setGraphic(icon);
        setPrefSize(size, size);
    }

    @Override
    protected void initializeStyle() {
        this.setContentDisplay(ContentDisplay.CENTER);
        this.setAlignment(Pos.CENTER);
        this.setTextAlignment(TextAlignment.CENTER);
        this.setPadding(Insets.EMPTY);
        ensureStyleClassPresent(this,  "icon-button");
    }

    @Override
    public IconButton self() { return this; }

//    public static IconButton medium(Icon icon) {
//        IconButton b = new IconButton(icon);
//        b.getStyleClass().add("medium");
//        return b;
//    }
//
//    public static IconButton small(Icon icon) {
//        IconButton b = new IconButton(icon);
//        b.getStyleClass().add("small");
//        return b;
//    }

    public static void mask(Button button, Icon icon) {
        DEFAULT.mask(button, icon);
    }
}
