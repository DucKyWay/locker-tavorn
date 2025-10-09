package ku.cs.services.utils;

import javafx.embed.swing.SwingFXUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.scene.image.Image;

import java.awt.*;
import java.awt.image.BufferedImage;

public class QrCodeGenerator {

    public QrCodeGenerator() {}

    /**
     * Generate QR Code Image
     * @param text something need to generate
     * @param size width, height
     * @return {@link SwingFXUtils} toFXImage
     */
    public Image generate(String text, int size) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, size, size);

            BufferedImage bufferedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, size, size);
            g.setColor(Color.BLACK);

            // เติม สีดำเข้าไปใน Matrix
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    if (bitMatrix.get(x, y)) {
                        g.fillRect(x, y, 1, 1);
                    }
                }
            }

            return SwingFXUtils.toFXImage(bufferedImage, null);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
