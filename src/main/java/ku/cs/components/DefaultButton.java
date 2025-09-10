package ku.cs.components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.util.Objects;

public class DefaultButton extends Button {

    private DefaultButton(String text) {
        super(text);
        setFont(Font.font("Bai Jamjuree", 14));
        setPrefWidth(120);
        setPrefHeight(35);
    }

    public static DefaultButton iconButton(String text, String iconPath, String backgroundColor, String textColor) {
        DefaultButton btn = new DefaultButton(""); // ไม่ใส่ text ตรงนี้ เพราะใช้ HBox แทน

        ImageView icon = new ImageView();
        try {
            icon.setImage(new Image(DefaultButton.class.getResource(iconPath).toExternalForm()));
            icon.setFitWidth(18);
            icon.setFitHeight(18); // 1:1
            icon.setPreserveRatio(true);
            icon.setSmooth(true);
        } catch (Exception e) {
            System.out.println("Icon not found: " + iconPath);
        }

        Label label = new Label(text);
        label.setFont(Font.font("Bai Jamjuree", 14));
        label.setStyle("-fx-text-fill: " + textColor + ";");

        HBox content = new HBox(8, icon, label); // ระยะห่างระหว่าง icon กับ text
        content.setAlignment(Pos.CENTER_LEFT);

        btn.setGraphic(content);
        btn.setStyle("-fx-background-color: " + backgroundColor + ";");
        return btn;
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