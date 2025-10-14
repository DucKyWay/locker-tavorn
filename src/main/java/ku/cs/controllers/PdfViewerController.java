package ku.cs.controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;

public class PdfViewerController {

    @FXML
    private ScrollPane scrollPane;

    private VBox pdfContainer;

    @FXML
    public void initialize() {
        String pdfPath = "data/doc/01418211_rod-f-211_docs.pdf";
        pdfContainer = new VBox(20); // ระยะห่างระหว่างหน้ากระดาษ
        scrollPane.setContent(pdfContainer);
        scrollPane.setFitToWidth(true);

        loadPdf(pdfPath);
    }

    private void loadPdf(String pdfPath) {
        try (PDDocument document = PDDocument.load(new File(pdfPath))) {
            PDFRenderer renderer = new PDFRenderer(document);

            int pageCount = document.getNumberOfPages();
            System.out.println("PDF has " + pageCount + " pages");

            for (int i = 0; i < pageCount; i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, 120); // 120 DPI = ความละเอียดปานกลาง
                ImageView imageView = new ImageView(SwingFXUtils.toFXImage(image, null));
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(800); // ปรับขนาดตามต้องการ

                pdfContainer.getChildren().add(imageView);
            }

            System.out.println("PDF loaded successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
