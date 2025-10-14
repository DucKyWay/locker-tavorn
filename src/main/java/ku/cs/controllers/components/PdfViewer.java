package ku.cs.controllers.components;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;

public class PdfViewer {
    public static ImageView renderPdfToImageView(String pdfPath) throws Exception {
        File file = new File(pdfPath);
        PDDocument document = PDDocument.load(file);
        PDFRenderer renderer = new PDFRenderer(document);

        BufferedImage image = renderer.renderImageWithDPI(0, 150); // หน้าแรก
        Image fxImage = SwingFXUtils.toFXImage(image, null);

        ImageView imageView = new ImageView(fxImage);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(800);

        document.close();
        return imageView;
    }
}
