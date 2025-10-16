package ku.cs.controllers.officer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import ku.cs.components.Icons;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.ElevatedButton;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.components.button.FilledButton;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.models.account.Officer;
import ku.cs.services.accounts.AccountService;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.session.SessionManager;
import ku.cs.services.utils.AccountValidator;
import ku.cs.services.utils.AlertUtil;

public class OfficerFirstLoginController {
    private final SessionManager sessionManager = (SessionManager) FXRouter.getService("session");

    @FXML private Label displayLabel;
    @FXML private Label subDisplayLabel;

    @FXML private PasswordField newPasswordPasswordField;
    @FXML private Label newPasswordErrorLabel;
    @FXML private PasswordField confirmPasswordPasswordField;
    @FXML private Label confirmPasswordErrorLabel;

    @FXML private Button changePasswordButton;

    @FXML private Button backButton;

    private Officer current;

    @FXML public void initialize() {
        current = sessionManager.getOfficer();
        initUserInterfaces();
        initEvents();
    }

    private void initUserInterfaces() {
        displayLabel.setText("ยินดีต้อนรับ! " + current.getFirstname());

        LabelStyle.DISPLAY_LARGE.applyTo(displayLabel);
        LabelStyle.BODY_LARGE.applyTo(subDisplayLabel);
        ElevatedButtonWithIcon.SMALL.mask(backButton, Icons.ARROW_LEFT);
        FilledButtonWithIcon.mask(changePasswordButton, Icons.USER_CHECK, Icons.ARROW_RIGHT);
    }

    private void initEvents() {
        changePasswordButton.setOnAction(e -> onChangePasswordButtonClick());
        backButton.setOnAction(e -> onBackButtonClick());
    }

    protected void onChangePasswordButtonClick() {
        AccountValidator validator = new AccountValidator();
        String newPassword = newPasswordPasswordField.getText().trim();
        String confirmPassword = confirmPasswordPasswordField.getText().trim();

        String passwordValidate = validator.validatePassword(newPassword);
        if (passwordValidate != null) {
            newPasswordErrorLabel.setText(passwordValidate);
            return;
        }

        passwordValidate = validator.validatePassword(confirmPassword);
        if (passwordValidate != null) {
            confirmPasswordErrorLabel.setText(passwordValidate);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            new AlertUtil().error("รหัสผ่านใหม่ไม่ตรงกัน", "กรุณาตรวจสอบรหัสอีกครั้ง");
            return;
        }

        try {
            AccountService accountService = new AccountService(current);
            accountService.changePasswordFirstOfficer(newPassword);
            new AlertUtil().info("สำเร็จ", "เปลี่ยนรหัสผ่านเรียบร้อยแล้ว");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            new AlertUtil().error("ไม่สามารถเปลี่ยนรหัสผ่าน", ex.getMessage());
        } catch (RuntimeException ex) {
            new AlertUtil().error("เกิดข้อผิดพลาด", ex.getMessage());
        }
    }

    protected void onBackButtonClick() {
        sessionManager.logout();
    }
}
