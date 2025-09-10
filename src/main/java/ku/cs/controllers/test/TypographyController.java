package ku.cs.controllers.test;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ku.cs.components.LabelStyle;

public class TypographyController {
    @FXML private VBox parentVBox;

    @FXML private Label titleMedium;

    @FXML private Label titleLarge;

    @FXML
    private void initialize() {
        // วิธีใช้ที่ 1
        titleMedium.getStyleClass().add(LabelStyle.TITLE_MEDIUM.getStyleClass());

        // วิธีใช้ที่ 2
        titleLarge.setText("titleLarge วิธีใช้ที่ 2");
        LabelStyle.TITLE_LARGE.applyTo(titleLarge);

        // วิธีใช้ที่ 3
        Label headSmall = new Label("ทดสอบ HeadLine วิธีใช้ที่ 3");
        headSmall.getStyleClass().add(LabelStyle.HEADLINE_SMALL.getStyleClass());

        parentVBox.getChildren().addAll(headSmall);

        for (LabelStyle style : LabelStyle.values()) {
            Label label = new Label(style.name().replace("_", " "));
            label.getStyleClass().add(style.getStyleClass());
            label.setMinWidth(500);
            parentVBox.getChildren().add(label);
        }

    }
}
