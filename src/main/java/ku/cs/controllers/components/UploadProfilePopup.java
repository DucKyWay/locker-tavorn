package ku.cs.controllers.components;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import ku.cs.models.account.Account;
import ku.cs.services.accounts.AccountService;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.ImageUploadUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class UploadProfilePopup {
    private final ImageUploadUtil imageUploadUtil = new ImageUploadUtil();
    private final AlertUtil alertUtil = new AlertUtil();
    private static final long MAX_FILE_SIZE_BYTES = 30 * 1024 * 1024; // 30MB

    public UploadProfilePopup() {}

    public void run(Account current) {
        if (current == null) {
            alertUtil.error("ไม่พบผู้ใช้", "กรุณาเข้าสู่ระบบใหม่");
            try { FXRouter.goTo("home"); } catch (IOException e) { throw new RuntimeException(e); }
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("จัดการรูปภาพโปรไฟล์");
        dialog.setHeaderText("นำเข้ารูปใหม่ของคุณ");

        ButtonType uploadBtnType = new ButtonType("บันทึก", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(uploadBtnType, ButtonType.CANCEL);

        // ui
        Button chooseBtn = new Button("Choose image…");
        Label fileLabel = new Label("No file selected");
        ImageView preview = new ImageView();
        preview.setPreserveRatio(true);
        preview.setFitWidth(180);
        preview.setFitHeight(180);

        // set initial image
        if (current.getImagePath() != null && !current.getImagePath().isBlank()) {
            preview.setImage(new Image("file:" + Paths.get(current.getImagePath()).toAbsolutePath()));
            fileLabel.setText("Current: " + current.getImagePath());
        } else {
            preview.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource("/ku/cs/images/default_profile.png")).toExternalForm()
            ));
        }

        // layout
        HBox row = new HBox(10, chooseBtn, fileLabel);
        row.setPadding(new Insets(10));
        HBox.setHgrow(fileLabel, Priority.ALWAYS);

        VBox vbox = new VBox(12, row, preview);
        vbox.setPadding(new Insets(15));
        vbox.setPrefWidth(400);
        vbox.setPrefHeight(300);

        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().setMinWidth(420);
        dialog.getDialogPane().setMinHeight(350);

        final Node saveBtn = dialog.getDialogPane().lookupButton(uploadBtnType);
        saveBtn.setDisable(true);

        final ImageUploadUtil.PickResult[] staged = new ImageUploadUtil.PickResult[1];

        // show old image label
        try {
            String existingName = current.getImagePath();
            if (existingName != null && !existingName.isBlank()) {
                Path existingPath = Paths.get(existingName);
                if (Files.exists(existingPath)) {
                    try (FileInputStream in = new FileInputStream(existingPath.toFile())) {
                        preview.setImage(new Image(in));
                    }
                    fileLabel.setText("Current: " + existingName);
                }
            }
        } catch (Exception ex) {
            fileLabel.setText("Current image not found");
        }

        // choose new file
        chooseBtn.setOnAction(e -> {
            try {
                Window owner = dialog.getDialogPane().getScene().getWindow();
                Path destDir = Paths.get("images", "profiles");

                ImageUploadUtil.PickResult res = imageUploadUtil.pickAndSaveImage(
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
                alertUtil.error("เกิดข้อผิดพลาด", "ไม่สามารถอัปโหลดรูปภาพได้: " + ex.getMessage());
            }
        });

        // save new image
        saveBtn.addEventFilter(javafx.event.ActionEvent.ACTION, ev -> {
            if (staged[0] == null) {
                alertUtil.error("ยังไม่ได้เลือกไฟล์", "กรุณาเลือกไฟล์รูปภาพก่อน");
                ev.consume();
                return;
            }
            try {
                AccountService accountService = new AccountService(current);
                accountService.updateProfileImage(staged[0].savedName());

                // sync
                current.setImagePath("images/profiles/" + staged[0].savedName());

                alertUtil.info("อัปเดตรูปโปรไฟล์สำเร็จ", "บันทึกรูปโปรไฟล์ใหม่เรียบร้อย");
            } catch (Exception ex) {
                alertUtil.error("เกิดข้อผิดพลาด", "ไม่สามารถบันทึกรูปภาพได้: " + ex.getMessage());
                ev.consume();
            }
        });

        dialog.showAndWait();
    }
}
