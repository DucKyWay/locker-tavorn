package ku.cs.controllers.components;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.services.Theme;
import ku.cs.services.ThemeProvider;

public class ToggleThemeButtonController {

    @FXML
    private Button toggleThemeButton;

    private static final double ICON_SIZE = 24;

    @FXML
    public void initialize() {
        updateIcon(ThemeProvider.getInstance().getTheme());
        ThemeProvider.getInstance().themeProperty().addListener((obs, oldTheme, newTheme) -> {
            updateIcon(newTheme);
        });
        toggleThemeButton.setOnAction(e -> ThemeProvider.getInstance().toggleTheme());
    }

    private void updateIcon(Theme theme) {
        switch (theme) {
            case LIGHT:
                // ตอนเป็น LIGHT ให้โชว์ไอคอน MOON (สื่อว่า “กดเพื่อไป Dark”)
                toggleThemeButton.setGraphic(new Icon(Icons.MOON, ICON_SIZE));
                toggleThemeButton.setAccessibleText("Switch to Dark Theme");
                break;
            case DARK:
                // ตอนเป็น DARK ให้โชว์ไอคอน SUN (สื่อว่า “กดเพื่อไป Light”)
                toggleThemeButton.setGraphic(new Icon(Icons.SUN, ICON_SIZE));
                toggleThemeButton.setAccessibleText("Switch to Light Theme");
                break;
        }
    }
}
