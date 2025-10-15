package ku.cs.services.pdf;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Service สำหรับโหลดไฟล์ PDF จาก resources และแปลงเป็นหน้า ๆ เป็น ImageView
 */
public class PdfLoaderService {

    /**
     * โหลด PDF จาก resource path และแปลงแต่ละหน้าเป็น ImageView
     *
     * @param resourcePath path ของ PDF ใน resources (เช่น "/ku/cs/document/example.pdf")
     * @param dpi ความละเอียดในการ render (เช่น 120)
     * @param width ความกว้างของภาพใน ImageView
     * @return รายการ ImageView ของทุกหน้าของ PDF
     * @throws IOException ถ้าโหลดหรือ render PDF ไม่สำเร็จ
     */
    public List<ImageView> loadPdfFromResources(String resourcePath, float dpi, double width) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {

            if (inputStream == null) {
                throw new IOException("Resource not found: " + resourcePath +
                        "\nMake sure the file exists in src/main/resources" + resourcePath);
            }

            try (PDDocument document = PDDocument.load(inputStream)) {
                PDFRenderer renderer = new PDFRenderer(document);
                int pageCount = document.getNumberOfPages();

                List<ImageView> imageViews = new ArrayList<>();
                for (int i = 0; i < pageCount; i++) {
                    BufferedImage image = renderer.renderImageWithDPI(i, dpi);
                    ImageView imageView = new ImageView(SwingFXUtils.toFXImage(image, null));
                    imageView.setPreserveRatio(true);
                    imageView.setFitWidth(width);
                    imageViews.add(imageView);
                }

                return imageViews;
            }
        }
    }
}
