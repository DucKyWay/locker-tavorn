package ku.cs.components;

import javafx.scene.control.Label;

public class Icon extends Label {
    private Icons iconType;

    public Icon() {
        super("");
        setFont(Icons.getIconFont());
        this.initializeStyle();
    }

    public Icon(String color) {
        super("");
        setFont(Icons.getIconFont());
        setTextFill(javafx.scene.paint.Paint.valueOf(color));
        this.initializeStyle();
    }

    public Icon(double size) {
        super("");
        setFont(Icons.getIconFont(size));
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
        setMouseTransparent(false);
    }

    public void setIcon(Icons icon) {
        if (icon != null)
        {
            this.iconType = icon;
            setText(icon.getUnicode());
        }
    }

    public Icons getIcon() {
        return this.iconType;
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

    @Override
    public String toString() {
        return iconType.getUnicode();
    }
}