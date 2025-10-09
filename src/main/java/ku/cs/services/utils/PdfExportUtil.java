package ku.cs.services.utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import ku.cs.models.account.Account;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.zone.Zone;
import ku.cs.models.locker.LockerType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PdfExportUtil {

    public PdfExportUtil() {}

    /**
     * สร้าง PDF แสดง QR code ของตู้ล็อกเกอร์ทั้งหมดใน Zone
     * @param zone โซนที่ต้องการ export
     * @param lockers รายการล็อกเกอร์
     * @param outputFile ไฟล์ปลายทาง
     * @param account ผู้ export
     */
    public void exportLockerQrGrid(Zone zone, LockerList lockers, File outputFile, Account account) throws IOException {
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        doc.addPage(page);
        PDPageContentStream content = new PDPageContentStream(doc, page);

        InputStream fontStream = PdfExportUtil.class.getResourceAsStream("/ku/cs/fonts/BaiJamjuree-Regular.ttf");
        if (fontStream == null) {
            throw new IOException("ไม่พบไฟล์ฟอนต์ BaiJamjuree-Regular.ttf ใน resources/fonts/");
        }
        PDFont thaiFont = PDType0Font.load(doc, fontStream);

        int cols = 3, rows = 3;
        float startX = 60, startY = 750;
        float cellWidth = 160, cellHeight = 220;
        int count = 0;

        // Set BaiJamjuree
        content.setFont(thaiFont, 16);
        content.beginText();
        content.newLineAtOffset(60, 800);
        content.showText("จุดให้บริการ: " + zone.getZoneName());
        content.endText();

        for (Locker locker : lockers.getLockers()) {
            int col = count % cols;
            int row = count / cols;

            if (row == rows) {
                content.close();
                page = new PDPage(PDRectangle.A4);
                doc.addPage(page);
                content = new PDPageContentStream(doc, page);
                row = 0;
                count = 0;

                content.setFont(thaiFont, 16);
                content.beginText();
                content.newLineAtOffset(60, 800);
                content.showText("จุดให้บริการ: " + zone.getZoneName());
                content.endText();
            }

            float x = startX + (col * cellWidth);
            float y = startY - (row * cellHeight);

            String password = locker.getLockerType().equals(LockerType.DIGITAL)
                    ? locker.getPassword() : "00000";

            String qrText = "LOCKER:" + locker.getLockerUid() + ":" + password;

            Image fxImage = new QrCodeGenerator().generate(qrText, 140);
            BufferedImage qrImage = SwingFXUtils.fromFXImage(fxImage, null);

            PDImageXObject pdImage = PDImageXObject.createFromByteArray(doc, imageToBytes(qrImage), locker.getLockerUid());
            content.drawImage(pdImage, x, y - 120, 100, 100);

            // Set BaiJamjuree
            content.setFont(thaiFont, 12);

            content.beginText();
            content.newLineAtOffset(x, y);
            content.showText("ตู้ล็อกเกอร์: " + locker.getLockerUid());
            content.endText();

            content.beginText();
            content.newLineAtOffset(x, y - 20);
            content.showText("รหัสผ่าน: " + password);
            content.endText();

            count++;
        }

        drawFooter(content, thaiFont, account.getFullName());

        content.close();
        outputFile.getParentFile().mkdirs();
        doc.save(outputFile);
        doc.close();
    }

    private void drawFooter(PDPageContentStream content, PDFont font, String exportedBy) throws IOException {
        content.setFont(font, 10);
        content.beginText();
        content.newLineAtOffset(60, 40);
        String dateTime = new TimeFormatUtil().formatFull(LocalDateTime.now());
        content.showText("นำออกโดย: " + exportedBy + "   (" + dateTime + ")");
        content.endText();
    }

    private byte[] imageToBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }
}
