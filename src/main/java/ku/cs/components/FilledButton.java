package ku.cs.components;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class FilledButton extends Button {
    private final HBox content = new HBox();
    private final Label textLabel = new Label();

    private final ObjectProperty<Node> leftGraphic  = new SimpleObjectProperty<>();
    private final ObjectProperty<Node> rightGraphic = new SimpleObjectProperty<>();
    private final DoubleProperty gap = new SimpleDoubleProperty(8);

    public FilledButton(String text) {
        this(text, null, null);
    }

    /** text + LeftIcon */
    public FilledButton(String text, Icon leftIcon) {
        this(text, leftIcon, null);
    }

    /** text + LeftIcon + RightIcon */
    public FilledButton(String text, Icon leftIcon, Icon rightIcon) {
        super(text);
        initializeStyle();

        // Layout ภายใน
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(getGap());
        gap.addListener((o, ov, nv) -> content.setSpacing(nv.doubleValue()));

        // ให้ปุ่มแสดง "graphic only" แล้วเราคุมข้อความเองใน HBox
        setPrefHeight(40);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        // ผูกข้อความในปุ่มเดิม ไปยัง label ภายใน
        textLabel.textProperty().bind(textProperty());

        // ติดตั้ง content เป็น graphic เดียวของปุ่ม
        setGraphic(content);

        // ตั้งไอคอนเริ่มต้น
        setLeftGraphic(leftIcon);
        setRightGraphic(rightIcon);

        // เมื่อไอคอนเปลี่ยน → ประกอบ content ใหม่
        leftGraphic.addListener((o, ov, nv) -> rebuildContent());
        rightGraphic.addListener((o, ov, nv) -> rebuildContent());

        rebuildContent();
    }

    private void initializeStyle() {
        getStyleClass().add("filled-button");
    }

    /** เรียกทุกครั้งเมื่อ left/right/text เปลี่ยน เพื่อประกอบ children ใหม่ */
    private void rebuildContent() {
        content.getChildren().setAll(FXCollections.observableArrayList());
        if (getLeftGraphic() != null)  content.getChildren().add(getLeftGraphic());
        // แสดง label เฉพาะเมื่อมีข้อความจริง ๆ
        if (getText() != null && !getText().isEmpty()) content.getChildren().add(textLabel);
        if (getRightGraphic() != null) content.getChildren().add(getRightGraphic());
    }

    // --------- API สาธารณะ ----------
    public Node getLeftGraphic() { return leftGraphic.get(); }
    public void setLeftGraphic(Node n) { leftGraphic.set(n); }
    public ObjectProperty<Node> leftGraphicProperty() { return leftGraphic; }

    public Node getRightGraphic() { return rightGraphic.get(); }
    public void setRightGraphic(Node n) { rightGraphic.set(n); }
    public ObjectProperty<Node> rightGraphicProperty() { return rightGraphic; }

    public double getGap() { return gap.get(); }
    public void setGap(double v) { gap.set(v); }
    public DoubleProperty gapProperty() { return gap; }

    // --------- size helper ----------
    public static FilledButton small(String text) {
        FilledButton b = new FilledButton(text);
        b.getStyleClass().add("small");
        return b;
    }

    // (opt) reset ปุ่มธรรมดาให้ใช้สไตล์ filled
    public static void mask(Button button) {
        button.setText("");
        button.getStyleClass().add("filled-button");
    }

}