package ku.cs.services.utils;

import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;

public class ImageUploadUtil {

    private ImageUploadUtil() {}

    public record PickResult(Path savedPath, String savedName) {}

    public static PickResult pickAndSaveImage(Window owner, Path destDir, String filenamePrefix, long maxSizeBytes) throws IOException {
        FileChooser fc = new FileChooser();
        fc.setTitle("Select image");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image files", "*.png", "*.jpg", "*.jpeg"));
        File chosen = fc.showOpenDialog(owner);
        if (chosen == null) return null; // user cancel

        if (maxSizeBytes > 0 && chosen.length() > maxSizeBytes) {
            throw new IOException("File too large");
        }

        // file is image
        try (FileInputStream in = new FileInputStream(chosen)) {
            Image img = new Image(in);
            if (img.isError() || img.getWidth() <= 0 || img.getHeight() <= 0) {
                throw new IOException("Invalid image");
            }
        }

        Files.createDirectories(destDir);

        String ext = getLowercaseExtension(chosen.getName());
        if (!isAllowedExt(ext)) {
            throw new IOException("Unsupported extension: " + ext);
        }

        String safePrefix = (filenamePrefix == null ? "" : filenamePrefix).replaceAll("[^a-zA-Z0-9-_]", "_");
        String newName = (safePrefix.isEmpty() ? "" : safePrefix + "-") + Instant.now().toEpochMilli() + "." + ext;

        Path target = destDir.resolve(newName);
        Files.copy(chosen.toPath(), target, StandardCopyOption.REPLACE_EXISTING);

        return new PickResult(target, newName);
    }

    private static String getLowercaseExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        if (dot == -1 || dot == filename.length() - 1) return "";
        return filename.substring(dot + 1).toLowerCase();
    }

    private static boolean isAllowedExt(String ext) {
        return ext.equals("png") || ext.equals("jpg") || ext.equals("jpeg");
    }
}
