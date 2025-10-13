package ku.cs.controllers.components;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import ku.cs.models.account.Account;
import ku.cs.services.accounts.AccountService;
import ku.cs.services.utils.AlertUtil;
//for account
public class ChangePasswordPopup {

    public void run(Account current) {
        final AlertUtil alertUtil = new AlertUtil();

        if (current == null) {
            alertUtil.error("ไม่พบผู้ใช้", "กรุณาเข้าสู่ระบบใหม่");
            return;
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("จัดการรหัสผ่าน");
        dialog.setHeaderText("เปลี่ยนรหัสผ่าน");

        ButtonType changeButtonType = new ButtonType("เปลี่ยนรหัสผ่าน", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(changeButtonType, ButtonType.CANCEL);

        PasswordField oldPasswordField = new PasswordField();
        oldPasswordField.setPromptText("รหัสผ่านเดิม");

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("รหัสผ่านใหม่");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("ยืนยันรหัสผ่านใหม่");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("รหัสผ่านเดิม:"), 0, 0);
        grid.add(oldPasswordField, 1, 0);
        grid.add(new Label("รหัสผ่านใหม่:"), 0, 1);
        grid.add(newPasswordField, 1, 1);
        grid.add(new Label("ยืนยันรหัสผ่านใหม่:"), 0, 2);
        grid.add(confirmPasswordField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        oldPasswordField.requestFocus();

        Node changeBtnNode = dialog.getDialogPane().lookupButton(changeButtonType);
        changeBtnNode.setDisable(true);

        Runnable validate = () -> {
            boolean filled = !oldPasswordField.getText().isEmpty()
                    && !newPasswordField.getText().isEmpty()
                    && !confirmPasswordField.getText().isEmpty();
            boolean match = newPasswordField.getText().equals(confirmPasswordField.getText());
            changeBtnNode.setDisable(!(filled && match));
        };

        oldPasswordField.textProperty().addListener((o, a, b) -> validate.run());
        newPasswordField.textProperty().addListener((o, a, b) -> validate.run());
        confirmPasswordField.textProperty().addListener((o, a, b) -> validate.run());

        dialog.setResultConverter(button -> button == changeButtonType ? button : null);

        dialog.showAndWait().ifPresent(btn -> {
            String oldPass = oldPasswordField.getText();
            String newPass = newPasswordField.getText();
            String confirmPass = confirmPasswordField.getText();

            if (!newPass.equals(confirmPass)) {
                alertUtil.error("รหัสผ่านใหม่ไม่ตรงกัน", "กรุณาตรวจสอบ รหัสผ่านและยืนยันรหัสผ่านของท่าน");
                return;
            }

            try {
                AccountService accountService = new AccountService(current);
                accountService.changePassword(oldPass, newPass);
                alertUtil.info("สำเร็จ", "เปลี่ยนรหัสผ่านเรียบร้อยแล้ว");
            } catch (IllegalArgumentException | IllegalStateException ex) {
                alertUtil.error("ไม่สามารถเปลี่ยนรหัสผ่าน", ex.getMessage());
            } catch (RuntimeException ex) {
                alertUtil.error("เกิดข้อผิดพลาด", ex.getMessage());
            }
        });
    }
}
