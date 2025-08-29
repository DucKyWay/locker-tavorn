package ku.cs.controllers.components;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Window;
import ku.cs.models.account.Account;
import ku.cs.services.AccountService;
import ku.cs.services.FXRouter;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.ImageUploadUtil;

import javax.imageio.stream.FileImageInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadProfilePopup {

    private static final long MAX_FILE_SIZE_BYTES = 30 * 1024 * 1024; // 30MB

    public UploadProfilePopup() {}

    public void run(Account current) {
        if (current == null) {
            AlertUtil.error("ไม่พบผู้ใช้", "กรุณาเข้าสู่ระบบใหม่");
            try { FXRouter.goTo("home"); } catch (IOException e) { throw new RuntimeException(e); }
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Change Profile");
        dialog.setHeaderText("Please upload new profile.");

        ButtonType uploadBtnType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(uploadBtnType, ButtonType.CANCEL);

        Button chooseBtn = new Button("Choose image…");
        Label fileLabel = new Label("No file selected");
        ImageView preview = new ImageView();
        preview.setPreserveRatio(true);
        preview.setFitWidth(180);
        preview.setFitHeight(180);

        HBox row = new HBox(10, chooseBtn, fileLabel);
        row.setPadding(new Insets(10));
        HBox.setHgrow(fileLabel, Priority.ALWAYS);
        dialog.getDialogPane().setContent(new javafx.scene.layout.VBox(8, row, preview));

        final Node saveBtn = dialog.getDialogPane().lookupButton(uploadBtnType);
        saveBtn.setDisable(true);

        final ImageUploadUtil.PickResult[] staged = new ImageUploadUtil.PickResult[1];

        // show old
        try {
            String existingName = current.getImagePath();
            if (existingName != null && !existingName.isBlank()) {
                Path existingPath = Paths.get(existingName);
                if (Files.exists(existingPath)) {
                    try (FileInputStream in = new FileInputStream(existingPath.toFile())) {
                        preview.setImage(new Image(in));
                    }
                    fileLabel.setText("Current: " + existingName);
                } else {
                    fileLabel.setText("Current image not found");
                    preview.setImage(new Image(getClass().getResource("/images/default_profile.png").toExternalForm()));
                }
            }
        } catch (Exception ignore) {

        }

        chooseBtn.setOnAction(e -> {
            try {
                Window owner = dialog.getDialogPane().getScene().getWindow();
                Path destDir = Paths.get("images", "profiles");

                ImageUploadUtil.PickResult res = ImageUploadUtil.pickAndSaveImage(
                        owner, destDir, current.getUsername(), MAX_FILE_SIZE_BYTES
                );
                if (res == null) return; // user cancel

                try (FileInputStream in = new FileInputStream(res.savedPath().toFile())) {
                    preview.setImage(new Image(in));
                }
                fileLabel.setText(res.savedName());
                staged[0] = res;
                saveBtn.setDisable(false);

            } catch (IOException ex) {
                AlertUtil.error("เกิดข้อผิดพลาด", "ไม่สามารถอัปโหลดรูปภาพได้: " + ex.getMessage());
            }
        });

        ((Button) saveBtn).addEventFilter(javafx.event.ActionEvent.ACTION, ev -> {
            if (staged[0] == null) {
                // ยังไม่ได้เลือกไฟล์ใหม่ แต่ผู้ใช้กด Save
                AlertUtil.error("ยังไม่ได้เลือกไฟล์", "กรุณาเลือกไฟล์รูปภาพก่อน");
                ev.consume();
                return;
            }
            try {
                new AccountService(current).updateProfileImage(staged[0].savedName());
                AlertUtil.info("อัปเดตรูปโปรไฟล์สำเร็จ", "บันทึกรูปโปรไฟล์ใหม่เรียบร้อย");
            } catch (Exception ex) {
                AlertUtil.error("เกิดข้อผิดพลาด", "ไม่สามารถบันทึกรูปภาพได้: " + ex.getMessage());
                ev.consume();
            }
        });

        dialog.showAndWait();
    }
}
