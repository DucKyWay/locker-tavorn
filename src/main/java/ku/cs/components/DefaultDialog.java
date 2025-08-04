package ku.cs.components;

import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font;

public class DefaultDialog {

    private static final String DEFAULT_FONT = "Bai Jamjuree";
    private static final int DEFAULT_FONT_SIZE = 14;

    private static void applyFontStyle(DialogPane pane) {
        pane.setStyle(String.format("-fx-font-family: '%s'; -fx-font-size: %dpx;", DEFAULT_FONT, DEFAULT_FONT_SIZE));
    }

    public static void info(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        applyFontStyle(alert.getDialogPane());
        alert.showAndWait();
    }

    public static void error(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR, message, ButtonType.CLOSE);
        alert.setTitle(title);
        alert.setHeaderText(null);
        applyFontStyle(alert.getDialogPane());
        alert.showAndWait();
    }

    public static boolean confirm(String title, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        alert.setHeaderText(null);
        applyFontStyle(alert.getDialogPane());
        return alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
    }

    public static void custom(String title, String message, AlertType type, ButtonType... buttons) {
        Alert alert = new Alert(type, message, buttons);
        alert.setTitle(title);
        alert.setHeaderText(null);
        applyFontStyle(alert.getDialogPane());
        alert.showAndWait();
    }
}
