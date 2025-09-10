package ku.cs.components.button;

import javafx.scene.control.Button;
import ku.cs.components.Icons;
import ku.cs.components.ReplaceableInParent;
import ku.cs.components.StyleMasker;

import java.util.Objects;

public class OutlinedButton extends CustomButton {

    public static final StyleMasker DEFAULT = new StyleMasker("outlined-button");
    public static final StyleMasker MEDIUM = new StyleMasker("outlined-button", "medium");
    public static final StyleMasker SMALL  = new StyleMasker("outlined-button", "small");

    public OutlinedButton() {
        super("OutlinedButton");
        initializeStyle();
    }

    public OutlinedButton(String label) {
        super(label);
        initializeStyle();
    }

    public OutlinedButton(String label, int width, int height) {
        super(label);
        initializeStyle();
        setPrefSize(width, height);
    }

    public OutlinedButton(String label, double width, double height) {
        super(label);
        initializeStyle();
        setPrefSize(width, height);
    }

    @Override
    protected void initializeStyle() {
        ensureStyleClassPresent(this,  "outlined-button");
    }

    @Override
    public OutlinedButton self() { return this; }

    public static OutlinedButton small() {
        OutlinedButton b = new OutlinedButton();
        b.getStyleClass().add("small");
        return b;
    }

    public static OutlinedButton small(String label) {
        OutlinedButton b = new OutlinedButton(label);
        b.getStyleClass().add("small");
        return b;
    }

    public static OutlinedButtonWithIcon icon(String label) {
        return icon(label, null, null);
    }

    public static OutlinedButtonWithIcon icon(String label, Icons icon) {
        return icon(label, icon, null);
    }

    public static OutlinedButtonWithIcon icon(String label, Icons leftIcon, Icons rightIcon) {
        return new OutlinedButtonWithIcon(label, leftIcon, rightIcon);
    }

    public static OutlinedButton from(Button button) {
        Objects.requireNonNull(button, "JavaFX Button cannot be null");

        OutlinedButton outlinedButton = new OutlinedButton(button.getText());

        ReplaceableInParent.adoptButtonProperties(button, outlinedButton);

        ensureStyleClassPresent(outlinedButton, "outlined-button");

        return outlinedButton;
    }

    public static OutlinedButton fromAndReplace(Button oldBtn) {
        OutlinedButton fb = from(oldBtn);
        fb.replaceInParentOf(oldBtn);
        return fb;
    }

    public static void mask(Button button) {
        DEFAULT.mask(button);
    }
}
