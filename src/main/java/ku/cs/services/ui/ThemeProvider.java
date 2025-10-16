package ku.cs.services.ui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ThemeProvider {

    private static final ThemeProvider instance = new ThemeProvider();

    private final ObjectProperty<Theme> currentTheme = new SimpleObjectProperty<>(Theme.LIGHT);

    private final ObjectProperty<FontFamily> currentFontFamily =
            new SimpleObjectProperty<>(FontFamily.BAI_JAMJUREE);

    private final ObjectProperty<FontScale> currentFontScale =
            new SimpleObjectProperty<>(FontScale.REGULAR);

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

    public ObjectProperty<FontFamily> fontFamilyProperty() {
        return currentFontFamily;
    }

    public FontFamily getFontFamily() {
        return currentFontFamily.get();
    }

    public void setFontFamily(FontFamily family) {
        currentFontFamily.set(family);
    }

    public void toggleFontFamily() {
        setFontFamily(
                getFontFamily() == FontFamily.BAI_JAMJUREE
                        ? FontFamily.SARABUN
                        : FontFamily.BAI_JAMJUREE
        );
    }

    public ObjectProperty<FontScale> fontScaleProperty() {
        return currentFontScale;
    }

    public FontScale getFontScale() {
        return currentFontScale.get();
    }

    public void setFontScale(FontScale scale) {
        currentFontScale.set(scale);
    }

    public void toggleFontScale() {
        setFontScale(
                getFontScale() == FontScale.REGULAR
                        ? FontScale.LARGE
                        : FontScale.REGULAR
        );
    }
}
