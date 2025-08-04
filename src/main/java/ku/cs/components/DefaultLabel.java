package ku.cs.components;

import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class DefaultLabel extends Label {
    public DefaultLabel(String text) {
        this(text, false, false); // normal style
    }

    public DefaultLabel(String text, boolean bold, boolean underline) {
        super(text);
        setFont(Font.font("Bai Jamjuree", bold ? FontWeight.BOLD : FontWeight.NORMAL, 14));
        setUnderline(underline);
        setStyle("-fx-text-fill: #333;");
    }

    public static DefaultLabel bold(String text) {
        return new DefaultLabel(text, true, false);
    }

    public static DefaultLabel underline(String text) {
        return new DefaultLabel(text, false, true);
    }

    public static DefaultLabel boldUnderline(String text) {
        return new DefaultLabel(text, true, true);
    }

    public static DefaultLabel title(String text) {
        DefaultLabel label = new DefaultLabel(text);
        label.setStyle("-fx-font-size: 36px; -fx-font-weight: bold");
        return label;
    }

    public static DefaultLabel h1(String text) {
        DefaultLabel label = new DefaultLabel(text);
        label.setFont(Font.font("Bai Jamjuree", FontWeight.BOLD,28));
        return label;
    }

    public static DefaultLabel h2(String text) {
        DefaultLabel label = new DefaultLabel(text);
        label.setFont(Font.font("Bai Jamjuree", FontWeight.BOLD,24));
        return label;
    }

    public static DefaultLabel h3(String text) {
        DefaultLabel label = new DefaultLabel(text);
        label.setFont(Font.font("Bai Jamjuree", FontWeight.BOLD,20));
        return label;
    }

    public static DefaultLabel h4(String text) {
        DefaultLabel label = new DefaultLabel(text);
        label.setStyle("-fx-font-size: 18px;");
        return label;
    }

    public static DefaultLabel link(String text) {
        DefaultLabel label = new DefaultLabel(text);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: #0058b1");
        label.setUnderline(true);
        return label;
    }

    public static DefaultLabel copyright() {
        DefaultLabel label = new DefaultLabel("Copyright © 2025 Rod F 211. All Rights Reserved. ");
        label.setStyle("-fx-text-fill: #fff; -fx-font-size: 12px");
        return label;
    }

    // for developer page
    public static DefaultLabel devName(String text) {
        DefaultLabel label = new DefaultLabel(text);
        label.setFont(Font.font(null, FontWeight.BOLD, 56));
        label.setStyle("-fx-text-fill: #fff;");
        return label;
    }

    public static DefaultLabel devId(String text) {
        DefaultLabel label = new DefaultLabel(text);
        label.setStyle("-fx-text-fill: #fff; -fx-font-size: 20px");
        return label;
    }

    // custom text and style
    public static DefaultLabel custom(String text, double size, FontWeight bold, boolean underline, FontPosture italic, String color, String background) {
        DefaultLabel label = new DefaultLabel(text);
        label.setFont(Font.font("Bai Jamjuree", bold, italic, size));

        label.setStyle(String.format("-fx-text-fill: %s; -fx-background-color: %s;", color, background));
        label.setUnderline(underline);
        return label;
    }
}
