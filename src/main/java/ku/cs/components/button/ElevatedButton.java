package ku.cs.components.button;

import javafx.scene.control.Button;
import ku.cs.components.Icons;
import ku.cs.components.ReplaceableInParent;
import ku.cs.components.StyleMasker;

import java.util.Objects;

public class ElevatedButton extends CustomButton {

    public static final StyleMasker DEFAULT = new StyleMasker("elevated-button");
    public static final StyleMasker MEDIUM = new StyleMasker("elevated-button", "medium");
    public static final StyleMasker SMALL  = new StyleMasker("elevated-button", "small");

    public ElevatedButton() {
        super("ElevatedButton");
        initializeStyle();
    }

    public ElevatedButton(String label) {
        super(label);
        initializeStyle();
    }

    public ElevatedButton(String label, int width, int height) {
        super(label);
        initializeStyle();
        setPrefSize(width, height);
    }

    public ElevatedButton(String label, double width, double height) {
        super(label);
        initializeStyle();
        setPrefSize(width, height);
    }
    @Override
    protected void initializeStyle() {
        ensureStyleClassPresent(this,  "elevated-button");
    }

    @Override
    public ElevatedButton self() { return this; }

    public static ElevatedButton small() {
        ElevatedButton b = new ElevatedButton();
        b.getStyleClass().add("small");
        return b;
    }

    public static ElevatedButton small(String label) {
        ElevatedButton b = new ElevatedButton(label);
        b.getStyleClass().add("small");
        return b;
    }

    public static ElevatedButtonWithIcon icon(String label) {
        return icon(label, null, null);
    }

    public static ElevatedButtonWithIcon icon(String label, Icons icon) {
        return icon(label, icon, null);
    }

    public static ElevatedButtonWithIcon icon(String label, Icons leftIcon, Icons rightIcon) {
        return new ElevatedButtonWithIcon(label, leftIcon, rightIcon);
    }

    public static ElevatedButton from(Button button) {
        Objects.requireNonNull(button, "JavaFX Button cannot be null");

        ElevatedButton elevatedButton = new ElevatedButton(button.getText());

        ReplaceableInParent.adoptButtonProperties(button, elevatedButton);

        ensureStyleClassPresent(elevatedButton, "elevated-button");

        return elevatedButton;
    }

    public static ElevatedButton fromAndReplace(Button oldBtn) {
        ElevatedButton fb = from(oldBtn);
        fb.replaceInParentOf(oldBtn);
        return fb;
    }

    public static void mask(Button button) {
        DEFAULT.mask(button);
    }
}
