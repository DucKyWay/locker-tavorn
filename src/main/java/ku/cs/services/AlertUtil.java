package ku.cs.services;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public final class AlertUtil {

    private AlertUtil() {}
    public static void info(String title, String msg) {
        show(Alert.AlertType.INFORMATION, title, msg);
    }
    public static void error(String title, String msg) {
        show(Alert.AlertType.ERROR, title, msg);
    }
    public static Optional<ButtonType> confirm(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        return a.showAndWait();
    }
    private static void show(Alert.AlertType t, String title, String msg) {
        Alert a = new Alert(t);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}