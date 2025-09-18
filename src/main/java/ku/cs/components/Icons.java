package ku.cs.components;

import javafx.scene.text.Font;

public enum Icons {
    ARROW_DOWN("\uE03E"),
    ARROW_LEFT("\uE058"),
    ARROW_RIGHT("\uE06C"),
    ARROW_UP("\uE08E"),
    EYE("\uE220"),
    EYE_CLOSED("\uE222"),
    EYE_SLASH("\uE224"),
    GEAR("\uE270"),
    GITHUB("\uE576"),
    LINE_VERTICAL("\uED70"),
    MOON("\uE330"),
    SIGN_IN("\uE428"),
    SIGN_OUT("\uE42A"),
    SMILEY("\uE436"),
    SMILEY_MELTING("\uEE56"),
    SMILEY_MEH("\uE43A"),
    SMILEY_NERVOUS("\uE43C"),
    SMILEY_SAD("\uE43E"),
    SUN("\uE472"),
    USER("\uE4C2"),
    USER_CIRCLE("\uE4C4"),
    USER_CIRCLE_CHECK("\uEC38"),
    USER_CIRCLE_GEAR("\uE4C6"),
    USER_CIRCLE_MINUS("\uE4C8"),
    USER_CIRCLE_PLUS("\uE4CA"),
    USER_CHECK("\uEAFA"),
    USER_GEAR("\uE4CC"),
    USER_MINUS("\uE4CE"),
    USER_PLUS("\uE4D0"),
    WARNING_CIRCLE("\uE4E2"),
    EDIT("\uE3B2"),
    DELETE("\uE4A6"),
    HOME("\uE2C2"),
    LOCATION("\uEE3A"),
    HISTORY("\uE1A0"),
    LOCK("\uE2FE"),
    SUSPEND("\uE4E4");
    ;

    private final String unicode;
    private static final double defaultSize = Size.md;
    private static final Font iconFont = Font.font("Phosphor", defaultSize);

    Icons(String unicode) {
        this.unicode = unicode;
    }

    public String getUnicode() {
        return this.unicode;
    }

    public static Font getIconFont() {
        return iconFont;
    }

    public static Font getIconFont(double size) {
        return Font.font(iconFont.getFamily(), size);
    }

    @Override
    public String toString() {
        return this.unicode;
    }

    public static class Size {
        public static double sm = 16;
        public static double md = 20;
        public static double lg = 24;
        public static double xl = 28;
        public static double xxl = 32;
    }

}
