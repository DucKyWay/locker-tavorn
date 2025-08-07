package ku.cs.components;

import javafx.scene.text.Font;

public enum Icons {
    SMILEY("\uE436"),
    ARROW_LEFT("\uE058"),
    ARROW_RIGHT("\uE06C"),
    ARROW_UP("\uE08E"),
    ARROW_DOWN("\uE03E");

    private final String unicode;
    private static Font iconFont = Font.font("Phosphor");
    private final static double defaultSize = 16;

    Icons(String unicode) {
        this.unicode = unicode;
    }

    public String getUnicode() {
        return unicode;
    }

    public static Font getIconFont() {
        if (iconFont == null) {
            iconFont = Font.font("Phosphor", defaultSize);
        }
        return iconFont;
    }

    public static Font getIconFont(double size) {
        if (iconFont == null) {
            return Font.font("Phosphor", size);
        }
        return Font.font(iconFont.getFamily(), size);
    }

    @Override
    public String toString() {
        return unicode;
    }
}
