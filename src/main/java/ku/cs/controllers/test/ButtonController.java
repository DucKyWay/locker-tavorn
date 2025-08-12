package ku.cs.controllers.test;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import ku.cs.components.FilledButton;
import ku.cs.components.Icon;
import ku.cs.components.Icons;

public class ButtonController {
    @FXML private VBox parentVBox;
    @FXML private Button buttonS;

    @FXML
    private void initialize() {
        // 1) Text only
        FilledButton primary = new FilledButton("Save");

        FilledButton.mask(buttonS);

        parentVBox.getChildren().addAll(primary);
    }
}
