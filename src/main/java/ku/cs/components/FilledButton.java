package ku.cs.components;

import javafx.scene.control.Button;
import java.util.Objects;

public class FilledButton extends Button implements ReplaceableInParent<FilledButton> {

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

    private void initializeStyle() {
        getStyleClass().add("filled-button");
    }

    public void setLabel(String label) { setText(label); }

    public String getLabel() { return getText(); }

    @Override
    public FilledButton self() {
        return this;
    }

    public static FilledButton small(String label)
    {
        FilledButton filledButton = new FilledButton(label);
        filledButton.getStyleClass().add("small");
        return filledButton;
    }

    public static FilledButton from(Button button) {
        Objects.requireNonNull(button, "JavaFx Button cannot be null");

        FilledButton filledButton = new FilledButton(button.getText());

        ReplaceableInParent.adoptButtonProperties(button, filledButton);

        if (!filledButton.getStyleClass().contains("filled-button")) {
            filledButton.getStyleClass().addFirst("filled-button");
        }

        return filledButton;
    }

    public static FilledButton fromAndReplace(Button oldBtn) {
        FilledButton filledButton = from(oldBtn);
        filledButton.replaceInParentOf(oldBtn);
        return filledButton;
    }
}
