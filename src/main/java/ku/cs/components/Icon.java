package ku.cs.components;

import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.text.Font;

public class Icon extends Label {
    private Icons iconType;

    public Icon() {
        this.initializeStyle();
    }

    public Icon(Icons icon) {
        super(icon.getUnicode());
        this.iconType = icon;
        setFont(Icons.getIconFont());
        initializeStyle();
    }

    public Icon(Icons icon, double size) {
        super(icon.getUnicode());
        this.iconType = icon;
        setFont(Icons.getIconFont(size));
        initializeStyle();
    }

    public Icon(Icons icon, double size, String color) {
        super(icon.getUnicode());
        this.iconType = icon;
        setFont(Icons.getIconFont(size));
        setTextFill(javafx.scene.paint.Paint.valueOf(color));
        initializeStyle();
    }

    public Icon(Icons icon, String style) {
        super(icon.getUnicode());
        this.iconType = icon;
        setFont(Icons.getIconFont());
        setStyle(style);
        initializeStyle();
    }

    private void initializeStyle() {
        getStyleClass().add("icon");
        setMouseTransparent(false); // ให้รับ mouse events ได้
    }

    public void setIcon(Icons icon) {
        this.iconType = icon;
        setText(icon.getUnicode());
    }

    public Icons getIcon() {
        return iconType;
    }

    public void setIconSize(double size) {
        setFont(Icons.getIconFont(size));
    }

    public void setIconColor(String color) {
        setTextFill(javafx.scene.paint.Paint.valueOf(color));
    }

    public void setIconColor(javafx.scene.paint.Paint color) {
        setTextFill(color);
    }

    public static void setIconToLabel(Label label, Icons icon) {
        label.setText("");
        label.setGraphic(new Icon(icon));
    }

    public static void setIconToLabel(Label label, Icons icon, double size) {
        label.setText("");
        label.setGraphic(new Icon(icon, size));
    }

    public static void setIconToLabel(Label label, Icons icon, double size, String color) {
        label.setText("");
        label.setGraphic(new Icon(icon, size, color));
    }
}