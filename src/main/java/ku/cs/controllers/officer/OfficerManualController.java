package ku.cs.controllers.officer;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ku.cs.services.pdf.PdfLoaderService;

import java.io.IOException;
import java.util.List;

public class OfficerManualController extends BaseOfficerController{

    @FXML
    private ScrollPane scrollPane;

    private final PdfLoaderService pdfLoaderService = new PdfLoaderService();



    @Override
    protected void initDatasource() {

        String pdfPath = "/ku/cs/document/officer-manual.pdf";

        VBox pdfContainer = new VBox(20);
        pdfContainer.setAlignment(Pos.CENTER);
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

    @Override
    protected void initUserInterfaces() {

    }

    @Override
    protected void initEvents() {

    }
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
