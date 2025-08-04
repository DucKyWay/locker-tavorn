package ku.cs.services;

import javafx.scene.text.Font;

import java.net.URL;

public class FontLoader {

    public static void loadBaiJamjureeFonts() {
        String[] weights = {
                "Regular", "Bold", "BoldItalic", "Light", "LightItalic",
                "Medium", "MediumItalic", "SemiBold", "SemiBoldItalic", "ExtraLight", "ExtraLightItalic", "Italic"
        };

        for (String weight : weights) {
            URL url = FontLoader.class.getResource("/ku/cs/fonts/BaiJamjuree-" + weight + ".ttf");
            System.out.println("Service Font: " + url + "Success.");
            if (url == null) {
                throw new RuntimeException("Font not found in: /ku/cs/fonts/BaiJamjuree-" + weight + ".ttf");
            }
            Font.loadFont(
                    FontLoader.class.getResource("/ku/cs/fonts/BaiJamjuree-" + weight + ".ttf").toExternalForm(), 14
            );
        }
    }
}