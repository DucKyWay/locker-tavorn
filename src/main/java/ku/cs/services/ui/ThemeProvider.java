package ku.cs.services.ui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ThemeProvider {

    private static final ThemeProvider instance = new ThemeProvider();

    private final ObjectProperty<Theme> currentTheme = new SimpleObjectProperty<>(Theme.LIGHT);

    private ThemeProvider() {}

    public static ThemeProvider getInstance() {
        return instance;
    }

    public ObjectProperty<Theme> themeProperty() {
        return currentTheme;
    }

    public Theme getTheme() {
        return currentTheme.get();
    }

    public void setTheme(Theme theme) {
        currentTheme.set(theme);
    }

    public void toggleTheme() {
        setTheme(getTheme() == Theme.LIGHT ? Theme.DARK : Theme.LIGHT);
    }
}
