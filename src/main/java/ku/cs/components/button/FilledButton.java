package ku.cs.components.button;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import ku.cs.components.Icons;
import ku.cs.components.ReplaceableInParent;
import ku.cs.components.StyleMasker;

import java.util.Objects;

public class FilledButton extends CustomButton {

    public static final StyleMasker DEFAULT = new StyleMasker("filled-button");
    public static final StyleMasker MEDIUM = new StyleMasker("filled-button", "medium");
    public static final StyleMasker SMALL  = new StyleMasker("filled-button", "small");

    public FilledButton() {
        super("FilledButton");
        initializeStyle();
    }

    public FilledButton(String label) {
        super(label);
        initializeStyle();
    }

    public FilledButton(String label, int width, int height) {
        super(label);
        initializeStyle();
        setPrefSize(width, height);
    }

    public FilledButton(String label, double width, double height) {
        super(label);
        initializeStyle();
        setPrefSize(width, height);
    }

    @Override
    protected void initializeStyle() {
        ensureStyleClassPresent(this,  "filled-button");
    }

    @Override
    public FilledButton self() { return this; }

    public static FilledButton small() {
        FilledButton b = new FilledButton();
        b.getStyleClass().add("small");
        return b;
    }

    public static FilledButton small(String label) {
        FilledButton b = new FilledButton(label);
        b.getStyleClass().add("small");
        return b;
    }

    public static FilledButtonWithIcon icon(String label) {
        return icon(label, null, null);
    }

    public static FilledButtonWithIcon icon(String label, Icons icon) {
        return icon(label, icon, null);
    }

    public static FilledButtonWithIcon icon(String label, Icons leftIcon, Icons rightIcon) {
        return new FilledButtonWithIcon(label, leftIcon, rightIcon);
    }

    public static FilledButton from(Button button) {
        Objects.requireNonNull(button, "JavaFX Button cannot be null");

        FilledButton filledButton = new FilledButton(button.getText());

        ReplaceableInParent.adoptButtonProperties(button, filledButton);

        ensureStyleClassPresent(filledButton, "filled-button");

        return filledButton;
    }

    public static FilledButton fromAndReplace(Button oldBtn) {
        FilledButton fb = from(oldBtn);
        fb.replaceInParentOf(oldBtn);
        return fb;
    }

    public static void mask(Button button) {
        DEFAULT.mask(button);
    }
}
