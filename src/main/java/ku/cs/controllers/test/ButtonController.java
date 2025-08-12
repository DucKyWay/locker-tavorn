package ku.cs.controllers.test;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import ku.cs.components.FilledButton;
import ku.cs.components.Icon;
import ku.cs.components.Icons;

public class ButtonController {
    @FXML private HBox parentVBox;
    @FXML private Button buttonS;

    @FXML
    private void initialize() {
        // 1) Text only
        FilledButton filledButton = new FilledButton("Normal");
        FilledButton filledButtonSmall = FilledButton.small("Small");
        FilledButton filledButtonIcon = new FilledButton("Small", new Icon(Icons.SMILEY, 24));

        FilledButton.mask(buttonS);

        parentVBox.setSpacing(10);

        parentVBox.getChildren().addAll(filledButton,  filledButtonSmall, filledButtonIcon);
    }
}
