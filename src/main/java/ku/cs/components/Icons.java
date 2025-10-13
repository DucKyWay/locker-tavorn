package ku.cs.components;

import javafx.scene.text.Font;

public enum Icons {
    ARROW_DOWN("\uE03E"),
    ARROW_LEFT("\uE058"),
    ARROW_RIGHT("\uE06C"),
    ARROW_UP("\uE08E"),
    EXPORT("\uEAF0"),
    EYE("\uE220"),
    EYE_CLOSED("\uE222"),
    EYE_SLASH("\uE224"),
    GEAR("\uE270"),
    GITHUB("\uE576"),
    LINE_VERTICAL("\uED70"),
    LIST_PLUS("\uE2F8"),
    LOCKER("\uECB8"),
    MAGNIFYING_GLASS("\ue30c"),
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
    DELETE("\uE4A8"),
    HOME("\uE2C2"),
    LOCATION("\uEE3A"),
    VAULT("\uE76E"),
    HISTORY("\uE1A0"),
    LOCK("\uE2FE"),
    KEY("\uE2D6"),
    APPROVE("\uE712"),
    REJECT("\uE10C"),
    SUSPEND("\uE4E4"),
    COPY("\uE1CA"),
    PLUS("\uE3D4"),
    MINUS("\uE32A"),
    DETAIL("\uE0A8"),
    TAG("\uE47A"),
    GRID("\uE464"),
    ROW("\uE5A2"),
    PASSWORD("\uE752"),
    FILTER("\uE268"),
    BELL("\uE0D0"),
    NULL(" ")
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
