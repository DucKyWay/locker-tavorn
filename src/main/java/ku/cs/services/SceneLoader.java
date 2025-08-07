package ku.cs.services;

import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class SceneLoader {

    // Corrected paths based on your project structure
    public static String CSS_ROOT = "/ku/cs/styles";  // Note the leading slash and correct path
    public static String GLOBAL = "global.css";
    public static String GLOBAL_JFX = "global.jfx.css";
    public static String TYPOGRAPHY = "typography.jfx.css";

    public static Scene loadScene(Parent root, double sceneWidth, double sceneHeight) throws IOException {
        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        applyGlobalStyles(scene);
        return scene;
    }

    private static void applyGlobalStyles(Scene scene) {
        try {
            scene.getStylesheets().clear();
            scene.getStylesheets().addAll(
                    Objects.requireNonNull(SceneLoader.class.getResource(CSS_ROOT + "/" + GLOBAL)).toExternalForm(),
//                    Objects.requireNonNull(SceneLoader.class.getResource(CSS_ROOT + "/" + GLOBAL_JFX)).toExternalForm(),
                    Objects.requireNonNull(SceneLoader.class.getResource(CSS_ROOT + "/" + TYPOGRAPHY)).toExternalForm(),
                    Objects.requireNonNull(SceneLoader.class.getResource(CSS_ROOT + "/themes/" + ThemeProvider.getInstance().getTheme().getCssFile())).toExternalForm()
            );
            debugResourcePaths();
        } catch (NullPointerException e) {
            System.err.println("Failed to load CSS resources: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void applyTheme(Scene scene) {
        try {
            scene.getStylesheets().clear();
            scene.getStylesheets().addAll(
//                    Objects.requireNonNull(SceneLoader.class.getResource(CSS_ROOT + "/" + GLOBAL)).toExternalForm(),
                    Objects.requireNonNull(SceneLoader.class.getResource(CSS_ROOT + "/" + GLOBAL_JFX)).toExternalForm(),
                    Objects.requireNonNull(SceneLoader.class.getResource(CSS_ROOT + "/" + TYPOGRAPHY)).toExternalForm(),
                    Objects.requireNonNull(SceneLoader.class.getResource(CSS_ROOT + "/themes/" + ThemeProvider.getInstance().getTheme().getCssFile())).toExternalForm()
            );
            debugResourcePaths();
        } catch (NullPointerException e) {
            System.err.println("Failed to apply theme: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void debugResourcePaths() {
        System.out.println("Checking CSS resource paths:");

        String[] resources = {
                CSS_ROOT + "/" + GLOBAL,
                CSS_ROOT + "/" + GLOBAL_JFX,
                CSS_ROOT + "/" + TYPOGRAPHY,
                CSS_ROOT + "/themes/" + ThemeProvider.getInstance().getTheme().getCssFile()
        };

        for (String resource : resources) {
            var url = SceneLoader.class.getResource(resource);
            System.out.println(resource + " -> " + (url != null ? "✓ Found" : "✗ Not Found"));
        }
    }
}