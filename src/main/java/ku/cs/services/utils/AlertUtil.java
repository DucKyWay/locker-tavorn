package ku.cs.services.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public final class AlertUtil {
    private static boolean headless = false;

    public static void setHeadless(boolean h) {
        headless = h;
    }

    private AlertUtil() {}

    public static void info(String title, String msg) {
        show(Alert.AlertType.INFORMATION, title, msg);
    }

    public static void error(String title, String msg) {
        show(Alert.AlertType.ERROR, title, msg);
    }

    public static Optional<ButtonType> confirm(String title, String msg) {
        if (headless) {
            System.out.println("[CONFIRM] " + title + ": " + msg + " -> YES");
            return Optional.of(ButtonType.OK); // auto confirm for headless
        }
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        return a.showAndWait();
    }

    private static void show(Alert.AlertType t, String title, String msg) {
        if (headless) {
            System.out.println("[" + t + "] " + title + ": " + msg);
            return;
        }
        Alert a = new Alert(t);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
