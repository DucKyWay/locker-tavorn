package ku.cs.services.ui;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;

import java.net.URL;
import java.util.List;
import java.util.Objects;

public class SceneLoader {

    public static final String CSS_ROOT = "/ku/cs/styles";
    public static final String GLOBAL = "global.css";
    public static final String LABEL  = "label-style.css";
    public static final String BUTTON = "button-style.css";

    private static final String KEY_THEME_URL = "themeStylesheetUrl";

    public static Scene loadScene(Parent root, double w, double h) {
        Scene scene = new Scene(root, w, h);
        attachGlobalAndTheme(scene);
        return scene;
    }

    public static Scene loadScene(Parent root) {
        Scene scene = new Scene(root);
        attachGlobalAndTheme(scene);
        return scene;
    }


    public static void attachGlobalAndTheme(DialogPane dialog) {
        applyGlobalStyles(dialog.getStylesheets());
        setTheme(dialog.getStylesheets(), ThemeProvider.getInstance().getTheme(), dialog);

        ThemeProvider.getInstance().themeProperty().addListener((obs, oldT, newT) -> {
            setTheme(dialog.getStylesheets(), newT, dialog);
            forceCss(dialog);
        });
    }


    private static void attachGlobalAndTheme(Scene scene) {
        applyGlobalStyles(scene.getStylesheets());
        setTheme(scene, ThemeProvider.getInstance().getTheme());

        ThemeProvider.getInstance().themeProperty().addListener((obs, oldT, newT) -> {
            setTheme(scene, newT);
            forceCss(scene.getRoot());
        });
    }

    private static void applyGlobalStyles(List<String> stylesheets) {
        stylesheets.clear();
        stylesheets.addAll(List.of(
                urlOrThrow(GLOBAL),
                urlOrThrow(LABEL),
                urlOrThrow(BUTTON)
        ));
    }

    private static void setTheme(Scene scene, Theme theme) {
        Object prevUrl = scene.getProperties().get(KEY_THEME_URL);
        if (prevUrl instanceof String) {
            scene.getStylesheets().remove((String) prevUrl);
        }

        String themeUrl = urlOrThrow(theme.getCssFile());
        scene.getStylesheets().add(themeUrl);
        scene.getProperties().put(KEY_THEME_URL, themeUrl);
    }

    private static void setTheme(List<String> stylesheets, Theme theme, Object ownerToKeepKey) {
        stylesheets.removeIf(s -> s.endsWith("/" + Theme.LIGHT.getCssFile()) || s.endsWith("/" + Theme.DARK.getCssFile()));
        stylesheets.add(urlOrThrow(theme.getCssFile()));
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
        for (String f : new String[]{ GLOBAL, LABEL, BUTTON, Theme.LIGHT.getCssFile(), Theme.DARK.getCssFile() }) {
            String path = CSS_ROOT + "/" + f;
            URL url = SceneLoader.class.getResource(path);
            System.out.println(path + " -> " + (url != null ? "✓ Found" : "✗ Not Found"));
        }
    }
}
