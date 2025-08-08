package ku.cs.services;

import javafx.scene.text.Font;

import java.net.URL;
import java.util.Objects;

public class FontLoader {
    public static String baiJamjureePath = "/ku/cs/fonts/BaiJamjuree";
    public static String phosphorPath = "/ku/cs/icons/Phosphor";

    public static void loadBaiJamjureeFonts() {
        String[] weights = {
                "Regular", "Bold", "BoldItalic", "Light", "LightItalic",
                "Medium", "MediumItalic", "SemiBold", "SemiBoldItalic", "ExtraLight", "ExtraLightItalic", "Italic"
        };

        for (String weight : weights) {
            URL url = FontLoader.class.getResource(baiJamjureePath + "-" + weight + ".ttf");
            if (url == null) {
                throw new RuntimeException("Font not found in: " + baiJamjureePath + "-" + weight + ".ttf");
            }
            Font.loadFont(
                    Objects.requireNonNull(FontLoader.class.getResource(baiJamjureePath + "-" + weight + ".ttf")).toExternalForm(), 14
            );
            System.out.println("Service Font: " + url + "Success.");
        }
    }

    public static void loadPhosphorIcons() {
        String[] weights = {"Regular"};

        for (String weight : weights) {
            URL url = FontLoader.class.getResource( phosphorPath + "-" + weight + ".ttf");
            if (url == null) {
                throw new RuntimeException("Icon not found in: " + phosphorPath + "-" + weight + ".ttf");
            }
            Font.loadFont(
                    Objects.requireNonNull(FontLoader.class.getResource(phosphorPath + "-" + weight + ".ttf")).toExternalForm(), 14
            );
            System.out.println("Service Icon: " + url + " Success.");
        }
    }
}