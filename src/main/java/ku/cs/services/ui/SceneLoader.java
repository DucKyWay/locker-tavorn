package ku.cs.services.ui;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.net.URL;
import java.util.List;
import java.util.Objects;

public class SceneLoader {

    public static final String CSS_ROOT = "/ku/cs/styles";
    public static final String GLOBAL = "global.css";

    public static Scene loadScene(Parent root, double w, double h) {
        Scene scene = new Scene(root, w, h);
        attachGlobalThemeFont(scene);
        return scene;
    }

    public static Scene loadScene(Parent root) {
        Scene scene = new Scene(root);
        attachGlobalThemeFont(scene);
        return scene;
    }

    private static void attachGlobalThemeFont(Scene scene) {
        applyGlobalStyles(scene.getStylesheets());

        ThemeProvider tp = ThemeProvider.getInstance();
        setTheme(scene, tp.getTheme());
        tp.themeProperty().addListener((obs, oldT, newT) -> {
            setTheme(scene, newT);
            forceCss(scene.getRoot());
        });

        setFontFamilyClass(scene.getRoot(), tp.getFontFamily());
        tp.fontFamilyProperty().addListener((obs, oldF, newF) -> {
            setFontFamilyClass(scene.getRoot(), newF);
            forceCss(scene.getRoot());
        });

        setFontScaleClass(scene.getRoot(), tp.getFontScale());
        tp.fontScaleProperty().addListener((obs, oldS, newS) -> {
            setFontScaleClass(scene.getRoot(), newS);
            forceCss(scene.getRoot());
        });
    }

    private static void applyGlobalStyles(List<String> stylesheets) {
        stylesheets.clear();
        stylesheets.add(
                urlOrThrow(GLOBAL)
        );
    }

    private static void setTheme(Scene scene, Theme theme) {
        String KEY_THEME_URL = "themeStylesheetUrl";
        Object prevUrl = scene.getProperties().get(KEY_THEME_URL);
        if (prevUrl instanceof String) {
            scene.getStylesheets().remove((String) prevUrl);
        }
        String themeUrl = urlOrThrow(theme.getCssFile());
        scene.getStylesheets().add(themeUrl);
        scene.getProperties().put(KEY_THEME_URL, themeUrl);
    }

    private static void setFontFamilyClass(Parent root, FontFamily family) {
        String KEY_FONT_FAMILY_CLASS = "rootFontFamilyClass";
        Object prev = root.getProperties().get(KEY_FONT_FAMILY_CLASS);
        if (prev instanceof String) {
            root.getStyleClass().remove((String) prev);
        }
        String cssClass = (family == FontFamily.BAI_JAMJUREE) ? "font-bai" : "font-sarabun";
        if (!root.getStyleClass().contains(cssClass)) {
            root.getStyleClass().add(cssClass);
        }
        root.getProperties().put(KEY_FONT_FAMILY_CLASS, cssClass);
    }

    private static void setFontScaleClass(Parent root, FontScale scale) {
        String KEY_FONT_SCALE_CLASS = "rootFontScaleClass";
        Object prev = root.getProperties().get(KEY_FONT_SCALE_CLASS);
        if (prev instanceof String) {
            root.getStyleClass().remove((String) prev);
        }
        String cssClass = (scale == FontScale.REGULAR) ? "scale-regular" : "scale-large";
        if (!root.getStyleClass().contains(cssClass)) {
            root.getStyleClass().add(cssClass);
        }
        root.getProperties().put(KEY_FONT_SCALE_CLASS, cssClass);
    }

    private static String urlOrThrow(String file) {
        URL url = SceneLoader.class.getResource(CSS_ROOT + "/" + file);
        return Objects.requireNonNull(url, "Missing CSS: " + CSS_ROOT + "/" + file).toExternalForm();
    }

    private static void forceCss(Parent root) {
        root.applyCss();
        root.requestLayout();
        Platform.runLater(() -> {
            root.applyCss();
            root.requestLayout();
        });
    }

    public static void debugResourcePaths() {
        System.out.println("Checking CSS resource paths:");
        for (String f : new String[]{ GLOBAL, Theme.LIGHT.getCssFile(), Theme.DARK.getCssFile() }) {
            String path = CSS_ROOT + "/" + f;
            URL url = SceneLoader.class.getResource(path);
            System.out.println(path + " -> " + (url != null ? "✓ Found" : "✗ Not Found"));
        }
    }
}
