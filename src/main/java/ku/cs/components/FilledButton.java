package ku.cs.components;

import javafx.scene.control.Button;

public class FilledButton extends Button {
    public FilledButton(String text) {
        super(text);
        initializeStyle();
    }

    public FilledButton(String text, String color) {
        super(text);
        initializeStyle();
    }

    private void initializeStyle() {
        getStyleClass().add("filled-button");
//        setOnMouseEntered(event -> {});
//        setMouseTransparent(false);
    }

    public static void mask(Button button) {
        button.getStyleClass().add("filled-button");
    }
}



//import javafx.beans.property.*;
//import javafx.scene.Node;
//import javafx.scene.control.Button;
//import javafx.scene.control.ContentDisplay;
//import javafx.scene.paint.Color;
//import javafx.scene.paint.Paint;
//
//public class FilledButton extends Button {
//
//    // Looked-up color properties (ตั้งจาก Java → ใช้ใน CSS ทุก state)
//    private final ObjectProperty<Paint> backgroundColor       = new SimpleObjectProperty<>(Color.web("#3b82f6"));
//    private final ObjectProperty<Paint> backgroundHoverColor  = new SimpleObjectProperty<>(Color.web("#2563eb"));
//    private final ObjectProperty<Paint> backgroundPressedColor= new SimpleObjectProperty<>(Color.web("#1e40af"));
//    private final ObjectProperty<Paint> backgroundDisabledColor=new SimpleObjectProperty<>(Color.web("#d1d5db"));
//
//    private final ObjectProperty<Paint> textColor             = new SimpleObjectProperty<>(Color.WHITE);
//    private final ObjectProperty<Paint> textDisabledColor     = new SimpleObjectProperty<>(Color.web("#9ca3af"));
//
//    private final DoubleProperty cornerRadius = new SimpleDoubleProperty(10);
//    private final DoubleProperty paddingV     = new SimpleDoubleProperty(8);
//    private final DoubleProperty paddingH     = new SimpleDoubleProperty(14);
//    private final DoubleProperty gap          = new SimpleDoubleProperty(8);
//
//    public FilledButton() {
//        this(null, null);
//    }
//
//    public FilledButton(String text) {
//        this(text, null);
//    }
//
//    public FilledButton(Node icon) {
//        this(null, icon);
//    }
//
//    public FilledButton(String text, Node icon) {
//        super(text);
//        if (icon != null) setGraphic(icon);
//        commonInit();
//    }
//
//
//    private void commonInit() {
//        getStyleClass().add("filled-button");
//        setContentDisplay(ContentDisplay.LEFT);
//        setGraphicTextGap(getGap());
//        gap.addListener((o, ov, nv) -> setGraphicTextGap(nv.doubleValue()));
//
//        // เมื่อค่าเปลี่ยน → อัปเดต looked-up variables บนปุ่ม
//        InvalidationListener refresh = obs -> updateInlineVariables();
//        backgroundColor.addListener(refresh);
//        backgroundHoverColor.addListener(refresh);
//        backgroundPressedColor.addListener(refresh);
//        backgroundDisabledColor.addListener(refresh);
//        textColor.addListener(refresh);
//        textDisabledColor.addListener(refresh);
//        cornerRadius.addListener(refresh);
//        paddingV.addListener(refresh);
//        paddingH.addListener(refresh);
//
//        updateInlineVariables();
//    }
//
//    private void updateInlineVariables() {
//        // inject looked-up variables และค่าพื้นฐาน (radius/padding) เข้าสู่ style ของปุ่ม
//        String style = String.join("; ",
//                "-filled-btn-bg: "            + toCss(backgroundColor.get()),
//                "-filled-btn-bg-hover: "      + toCss(backgroundHoverColor.get()),
//                "-filled-btn-bg-pressed: "    + toCss(backgroundPressedColor.get()),
//                "-filled-btn-bg-disabled: "   + toCss(backgroundDisabledColor.get()),
//                "-filled-btn-fg: "            + toCss(textColor.get()),
//                "-filled-btn-fg-disabled: "   + toCss(textDisabledColor.get()),
//                "-filled-btn-radius: "        + fmt(cornerRadius.get()) + "px",
//                "-filled-btn-pad-v: "         + fmt(paddingV.get()) + "px",
//                "-filled-btn-pad-h: "         + fmt(paddingH.get()) + "px;"
//        );
//        System.out.println(style);
//        setStyle(style);
//    }
//
//    private static String toCss(Paint p) {
//        if (p instanceof Color c) {
//            int r = (int)Math.round(c.getRed()*255);
//            int g = (int)Math.round(c.getGreen()*255);
//            int b = (int)Math.round(c.getBlue()*255);
//            String a = String.format("%.3f", c.getOpacity());
//            return "rgba(" + r + "," + g + "," + b + "," + a + ")";
//        }
//        // รองรับ Gradient/อื่น ๆ
//        return p.toString();
//    }
//
//    private static String fmt(double d) { return String.format("%.0f", d); }
//
//
//    /** ใส่ไอคอนได้ทุก Node เช่น Icon (extends Label), ImageView, SVG ฯลฯ */
//    public void setIcon(Node icon) { setGraphic(icon); }
//
//    /** LEFT/RIGHT/TOP/BOTTOM เหมือนปุ่มปกติ */
//    public void setIconPosition(ContentDisplay pos) { setContentDisplay(pos); }
//
//    /** ระยะ icon-text */
//    public double getGap() { return gap.get(); }
//    public void setGap(double value) { gap.set(value); }
//    public DoubleProperty gapProperty() { return gap; }
//
//    /* -------------------- Properties สำหรับ Theme Runtime -------------------- */
//
//    public Paint getBackgroundColor() { return backgroundColor.get(); }
//    public void setBackgroundColor(Paint p) { backgroundColor.set(p); }
//    public ObjectProperty<Paint> backgroundColorProperty() { return backgroundColor; }
//
//    public Paint getBackgroundHoverColor() { return backgroundHoverColor.get(); }
//    public void setBackgroundHoverColor(Paint p) { backgroundHoverColor.set(p); }
//    public ObjectProperty<Paint> backgroundHoverColorProperty() { return backgroundHoverColor; }
//
//    public Paint getBackgroundPressedColor() { return backgroundPressedColor.get(); }
//    public void setBackgroundPressedColor(Paint p) { backgroundPressedColor.set(p); }
//    public ObjectProperty<Paint> backgroundPressedColorProperty() { return backgroundPressedColor; }
//
//    public Paint getBackgroundDisabledColor() { return backgroundDisabledColor.get(); }
//    public void setBackgroundDisabledColor(Paint p) { backgroundDisabledColor.set(p); }
//    public ObjectProperty<Paint> backgroundDisabledColorProperty() { return backgroundDisabledColor; }
//
//    public Paint getTextColor() { return textColor.get(); }
//    public void setTextColor(Paint p) { textColor.set(p); }
//    public ObjectProperty<Paint> textColorProperty() { return textColor; }
//
//    public Paint getTextDisabledColor() { return textDisabledColor.get(); }
//    public void setTextDisabledColor(Paint p) { textDisabledColor.set(p); }
//    public ObjectProperty<Paint> textDisabledColorProperty() { return textDisabledColor; }
//
//    public double getCornerRadius() { return cornerRadius.get(); }
//    public void setCornerRadius(double v) { cornerRadius.set(v); }
//    public DoubleProperty cornerRadiusProperty() { return cornerRadius; }
//
//    public double getPaddingV() { return paddingV.get(); }
//    public void setPaddingV(double v) { paddingV.set(v); }
//    public DoubleProperty paddingVProperty() { return paddingV; }
//
//    public double getPaddingH() { return paddingH.get(); }
//    public void setPaddingH(double v) { paddingH.set(v); }
//    public DoubleProperty paddingHProperty() { return paddingH; }
//}
//
