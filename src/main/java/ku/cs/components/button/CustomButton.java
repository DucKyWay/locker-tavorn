package ku.cs.components.button;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import ku.cs.components.ReplaceableInParent;

public class CustomButton extends Button implements ReplaceableInParent<CustomButton> {
    public CustomButton() {
        super("CustomButton");
        initializeStyle();
    }

    public CustomButton(String label) {
        super(label);
        initializeStyle();
    }

    public CustomButton(String label, int width, int height) {
        super(label);
        initializeStyle();
        setPrefSize(width, height);
    }

    public CustomButton(String label, double width, double height) {
        super(label);
        initializeStyle();
        setPrefSize(width, height);
    }

    protected void initializeStyle() {
        ensureStyleClassPresent(this,  "custom-button");
    }

    protected static void ensureStyleClassPresent(Button button, String cls) {
        ObservableList<String> sc = button.getStyleClass();
        sc.remove(cls);
        sc.add(0, cls);
    }


    public void setLabel(String label) { setText(label); }
    public String getLabel() { return getText(); }

    @Override
    public CustomButton self() { return this; }

}
