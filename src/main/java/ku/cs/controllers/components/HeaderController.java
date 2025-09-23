package ku.cs.controllers.components;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ku.cs.components.Icons;
import ku.cs.components.button.ElevatedButtonWithIcon;

public class HeaderController {

    @FXML private Button lockerTavornButton;

    @FXML public void initialize() {
        initUserInterface();
    }

    private void initUserInterface() {
        ElevatedButtonWithIcon.SMALL.mask(lockerTavornButton, Icons.LOCK);
    }
}
