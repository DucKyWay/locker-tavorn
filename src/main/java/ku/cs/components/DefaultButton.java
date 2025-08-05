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
        String normalStyle = "-fx-background-color: #0d6efd; -fx-text-fill: white;";
        String hoverStyle = "-fx-background-color: rgba(13,110,253,0.50);";

        btn.setStyle(normalStyle);
        btn.hoverProperty().addListener((observable, oldValue, isHovering) -> {
            if (isHovering) {
                btn.setStyle(hoverStyle);
            } else {
                btn.setStyle(normalStyle);
            }
        });

        return btn;
    }

    public static DefaultButton success(String text) {
        DefaultButton btn = new DefaultButton(text);
        String normalStyle = "-fx-background-color: #28a745; -fx-text-fill: white;";
        String hoverStyle = "-fx-background-color: rgba(40,167,69,0.5);";

        btn.setStyle(normalStyle);
        btn.hoverProperty().addListener((observable, oldValue, isHovering) -> {
            if (isHovering) {
                btn.setStyle(hoverStyle);
            } else {
                btn.setStyle(normalStyle);
            }
        });

        return btn;
    }

    public static DefaultButton warning(String text) {
        DefaultButton btn = new DefaultButton(text);
        String normalStyle = "-fx-background-color: #ffc107; -fx-text-fill: white;";
        String hoverStyle = "-fx-background-color: rgba(255,193,7,0.5);";

        btn.setStyle(normalStyle);
        btn.hoverProperty().addListener((observable, oldValue, isHovering) -> {
            if (isHovering) {
                btn.setStyle(hoverStyle);
            } else {
                btn.setStyle(normalStyle);
            }
        });

        return btn;
    }

    public static DefaultButton danger(String text) {
        DefaultButton btn = new DefaultButton(text);
        String normalStyle = "-fx-background-color: #dc3545; -fx-text-fill: white;";
        String hoverStyle = "-fx-background-color: rgba(220,53,69,0.5);";

        btn.setStyle(normalStyle);
        btn.hoverProperty().addListener((observable, oldValue, isHovering) -> {
            if (isHovering) {
                btn.setStyle(hoverStyle);
            } else {
                btn.setStyle(normalStyle);
            }
        });

        return btn;
    }

    public static DefaultButton info(String text) {
        DefaultButton btn = new DefaultButton(text);
        String normalStyle = "-fx-background-color: #0dcaf0; -fx-text-fill: black;";
        String hoverStyle = "-fx-background-color: rgba(13,202,240,0.5);";

        btn.setStyle(normalStyle);
        btn.hoverProperty().addListener((observable, oldValue, isHovering) -> {
            if (isHovering) {
                btn.setStyle(hoverStyle);
            } else {
                btn.setStyle(normalStyle);
            }
        });

        return btn;
    }

    public static DefaultButton outline(String text) {
        DefaultButton btn = new DefaultButton(text);
        String normalStyle = "-fx-background-color: transparent; -fx-border-color: #333; -fx-text-fill: #333;";
        String hoverStyle = "-fx-background-color: rgb(129,129,129); -fx-border-color: #333;";

        btn.setStyle(normalStyle);
        btn.hoverProperty().addListener((observable, oldValue, isHovering) -> {
            if (isHovering) {
                btn.setStyle(hoverStyle);
            } else {
                btn.setStyle(normalStyle);
            }
        });

        return btn;
    }
}