package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ku.cs.services.pdf.PdfLoaderService;

import java.io.IOException;
import java.util.List;

public class PdfViewerController {

    @FXML
    private ScrollPane scrollPane;

    private final PdfLoaderService pdfLoaderService = new PdfLoaderService();

    @FXML
    public void initialize() {
        String pdfPath = "/ku/cs/document/01418211_rod-f-211_docs.pdf";

        VBox pdfContainer = new VBox(20);
        scrollPane.setContent(pdfContainer);
        scrollPane.setFitToWidth(true);

        try {
            List<ImageView> pages = pdfLoaderService.loadPdfFromResources(pdfPath, 150, 800);
            pdfContainer.getChildren().addAll(pages);
            System.out.println("PDF loaded successfully from resources!");
        } catch (IOException e) {
            showError("PDF Loading Error", e.getMessage());
            e.printStackTrace();
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
