package ku.cs.components.button;

import javafx.scene.control.Button;
import ku.cs.components.Icon;
import ku.cs.components.StyleMasker;

public class IconButton extends CustomButton {
    public static final StyleMasker DEFAULT = new StyleMasker("icon-button");
    public static final StyleMasker ERROR = new StyleMasker("icon-button", "error");
    public static final StyleMasker SUCCESS = new StyleMasker("icon-button", "success");

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

    public static IconButton success(Icon icon) {
        IconButton b = new IconButton(icon);
        b.getStyleClass().add("success");
        return b;
    }

    public static void mask(Button button, Icon icon) {
        DEFAULT.mask(button, icon);
    }
}
