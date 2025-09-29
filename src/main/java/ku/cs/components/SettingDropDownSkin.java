package ku.cs.components;

import javafx.application.Platform;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class SettingDropDownSkin<T> extends ComboBoxListViewSkin<T> {

    public SettingDropDownSkin(ComboBox<T> comboBox) {
        super(comboBox);
        Platform.runLater(this::customizeArrowButton);
    }

    private void customizeArrowButton() {
        getSkinnable().lookupAll(".arrow-button").forEach(node -> {
            if (node instanceof StackPane pane) {
                pane.setVisible(false);
                pane.setManaged(false);
            }
        });
    }
}