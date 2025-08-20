package ku.cs.controllers.test;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import ku.cs.components.FilledButton;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.ReplaceableInParent;

public class ButtonController {
    @FXML private HBox parentVBox;
    @FXML private Button buttonStandalone;
    @FXML private Button buttonInHBox;
    @FXML private Button buttonInVBox;

    @FXML
    private void initialize() {
        Icon icon = new Icon(Icons.ARROW_LEFT, 24);
        FilledButton filledButton = new FilledButton("FilledButton");
        FilledButton filledButtonSmall = FilledButton.small("FilledButtonSmall");

        FilledButton.fromAndReplace(buttonStandalone);

        FilledButton newButtonInHBox = FilledButton.from(buttonInHBox);
        newButtonInHBox.replaceInParentOf(buttonInHBox);

        FilledButton newButtonInVBox = FilledButton.fromAndReplace(buttonInVBox);
        buttonInVBox.setStyle("-fx-background-color: Blue;");
        ReplaceableInParent.adoptButtonProperties(buttonInVBox, newButtonInVBox);


        Button normal = new Button("Normal");
        normal.setFont(Font.font("Bai Jamjuree", FontWeight.MEDIUM, 16));
        normal.getStyleClass().add("normal");

        parentVBox.setSpacing(10);
        parentVBox.getChildren().addAll(icon, filledButton, filledButtonSmall, normal);
    }
}
