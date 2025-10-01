package ku.cs.controllers.components;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.button.IconButton;
import ku.cs.services.ui.Theme;
import ku.cs.services.ui.ThemeProvider;

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
                IconButton.mask(toggleThemeButton, new Icon(Icons.MOON, ICON_SIZE));
                break;
            case DARK:
                IconButton.mask(toggleThemeButton, new Icon(Icons.SUN, ICON_SIZE));
                break;
        }
    }
}
