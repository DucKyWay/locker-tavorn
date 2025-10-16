package ku.cs.components;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Toast {
    public static void show(Stage ownerStage, String message, int millis) {
        Popup popup = new Popup();

        Label label = new Label(message);
        label.setStyle(
                "-fx-padding: 10px 20px; "
                + "-fx-background-radius: 8; ");
        label.getStyleClass().addAll("label-large", "text-background");
        StackPane root = new StackPane(label);
        root.setStyle("-fx-background-radius: 8; ");
        root.getStyleClass().add("bg-on-background");
        Scene scene = new Scene(root);
        scene.setFill(null);

        popup.getContent().add(root);
        popup.setAutoFix(true);
        popup.setAutoHide(true);

        // show popup
        popup.show(ownerStage,
                ownerStage.getX() + (ownerStage.getWidth() / 2) - 100,
                ownerStage.getY() + ownerStage.getHeight() - 100); // bottom 100

        // fade ทำได้เท่านี้แหละ สวยละ
        PauseTransition delay = new PauseTransition(Duration.millis(millis));
        delay.setOnFinished(event -> {
            FadeTransition fade = new FadeTransition(Duration.millis(500), root);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(e -> popup.hide());
            fade.play();
        });
        delay.play();
    }
}
