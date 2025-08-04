package ku.cs.components;

import javafx.scene.control.Button;
import javafx.scene.text.Font;

public class DefaultButton extends Button {

    private DefaultButton(String text) {
        super(text);
        setFont(Font.font("Bai Jamjuree", 14));
        setPrefWidth(120);
        setPrefHeight(35);
    }

    public static DefaultButton primary(String text) {
        DefaultButton btn = new DefaultButton(text);
        btn.setStyle("-fx-background-color: #0d6efd; -fx-text-fill: white;");
        return btn;
    }

    public static DefaultButton success(String text) {
        DefaultButton btn = new DefaultButton(text);
        btn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        return btn;
    }

    public static DefaultButton warning(String text) {
        DefaultButton btn = new DefaultButton(text);
        btn.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black;");
        return btn;
    }

    public static DefaultButton danger(String text) {
        DefaultButton btn = new DefaultButton(text);
        btn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
        return btn;
    }

    public static DefaultButton info(String text) {
        DefaultButton btn = new DefaultButton(text);
        btn.setStyle("-fx-background-color: #0dcaf0; -fx-text-fill: black;");
        return btn;
    }

    public static DefaultButton outline(String text) {
        DefaultButton btn = new DefaultButton(text);
        btn.setStyle("-fx-background-color: transparent; -fx-border-color: #333; -fx-text-fill: #333;");
        return btn;
    }
}