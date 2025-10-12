package ku.cs.services.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public final class AlertUtil {
    public AlertUtil() {}

    /**
     * Show an information alert dialog.
     *
     * @param title the title of the alert window
     * @param msg   the message content to display
     */
    public void info(String title, String msg) {
        show(Alert.AlertType.INFORMATION, title, msg);
    }

    /**
     * Show an information async alert dialog.
     *
     * @param title the title of the alert window
     * @param msg   the message content to display
     */
    public void infoAsync(String title, String msg) {
        async(Alert.AlertType.INFORMATION, title, msg);
    }

    /**
     * Show a warning alert dialog.
     *
     * @param title the title of the alert window
     * @param msg   the message content to display
     */
    public void warning(String title, String msg) {
        show(Alert.AlertType.WARNING, title, msg);
    }

    /**
     * Show a warning async alert dialog.
     *
     * @param title the title of the alert window
     * @param msg   the message content to display
     */
    public void warningAsync(String title, String msg) {
        async(Alert.AlertType.WARNING, title, msg);
    }

    /**
     * Show a error alert dialog.
     *
     * @param title the title of the alert window
     * @param msg   the message content to display
     */
    public void error(String title, String msg) {
        show(Alert.AlertType.ERROR, title, msg);
    }

    /**
     * Show a error async alert dialog.
     *
     * @param title the title of the alert window
     * @param msg   the message content to display
     */
    public void errorAsync(String title, String msg) {
        async(Alert.AlertType.ERROR, title, msg);
    }

    /**
     * Show a confirmation alert dialog with OK and Cancel buttons.
     *
     * @param title the title of the alert window
     * @param msg   the message content to display
     */
    public Optional<ButtonType> confirm(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        return a.showAndWait();
    }

    /**
     * Internal helper method for displaying a general alert of a given type.
     *
     * @param t  the type of alert to show (INFORMATION, WARNING, ERROR, etc.)
     * @param title the title of the alert window
     * @param msg   the message content to display
     */
    private void show(Alert.AlertType t, String title, String msg) {
        Alert a = new Alert(t);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    /**
     * Internal helper method for displaying an async alert of a given type.
     *
     * @param t  the type of alert to show (INFORMATION, WARNING, ERROR, etc.)
     * @param title the title of the alert window
     * @param msg   the message content to display
     */
    public void async(Alert.AlertType t, String title, String msg) {
        Platform.runLater(() -> {
            Alert a = new Alert(t);
            a.setTitle(title);
            a.setHeaderText(null);
            a.setContentText(msg);
            a.show();
        });
    }
}
