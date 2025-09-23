package ku.cs.controllers.officer;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ku.cs.components.DefaultPasswordField;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.CustomButton;
import ku.cs.models.account.Account;
import ku.cs.models.account.Officer;
import ku.cs.services.AccountService;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.utils.AlertUtil;

public class OfficerFirstLoginController {

    // Interfaces
    @FXML private VBox parentVBox;

    private VBox passwordVBox;
    private HBox newPasswordHBox;
    private HBox confirmPasswordHBox;

    private Label titleLabel;
    private Label newPasswordLabel;
    private Label confirmPasswordLabel;
    private PasswordField newPasswordPasswordField;
    private PasswordField confirmPasswordPasswordField;
    private Button changePasswordButton;

    // Controller
    protected Officer current = (Officer) FXRouter.getData();

    @FXML public void initialize() {

        initUserInterfaces();
        initEvents();
    }

    private void initUserInterfaces() {

        passwordVBox = new VBox();
        newPasswordHBox = new HBox();
        confirmPasswordHBox = new HBox();
        titleLabel = new Label("เปลี่ยนรหัสผ่าน | " + current.getUsername());
        newPasswordLabel = new Label("New Password: ");
        confirmPasswordLabel = new Label("Confirm Password: ");
        newPasswordPasswordField = new DefaultPasswordField("new Password");
        confirmPasswordPasswordField = new DefaultPasswordField("confirm Password");
        changePasswordButton = new CustomButton("Change Password");

        LabelStyle.TITLE_LARGE.applyTo(titleLabel);
        LabelStyle.BODY_MEDIUM.applyTo(newPasswordLabel);
        LabelStyle.BODY_MEDIUM.applyTo(confirmPasswordLabel);

        newPasswordHBox.setAlignment(Pos.TOP_CENTER);
        confirmPasswordHBox.setAlignment(Pos.TOP_CENTER);
        passwordVBox.setSpacing(10);
        parentVBox.setSpacing(40);

        newPasswordHBox.getChildren().addAll(newPasswordLabel, newPasswordPasswordField);
        confirmPasswordHBox.getChildren().addAll(confirmPasswordLabel, confirmPasswordPasswordField);
        passwordVBox.getChildren().addAll(newPasswordHBox, confirmPasswordHBox);

        parentVBox.getChildren().addAll(titleLabel, passwordVBox, changePasswordButton);
    }

    private void initEvents() {
        changePasswordButton.setOnAction(e -> onChangePasswordButtonClick());
    }

    protected void onChangePasswordButtonClick() {

        String newPassword = newPasswordPasswordField.getText().trim();
        String confirmPassword = confirmPasswordPasswordField.getText().trim();

        if (!newPassword.equals(confirmPassword)) {
            AlertUtil.error("รหัสผ่านใหม่ไม่ตรงกัน", "กรุณาตรวจสอบ New/Confirm Password");
            return;
        }

        try {

            AccountService accountService = new AccountService(current);
            accountService.changePasswordFirstOfficer(newPassword);
            AlertUtil.info("สำเร็จ", "เปลี่ยนรหัสผ่านเรียบร้อยแล้ว");

            SessionManager.login(current);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            AlertUtil.error("ไม่สามารถเปลี่ยนรหัสผ่าน", ex.getMessage());
        } catch (RuntimeException ex) {
            AlertUtil.error("เกิดข้อผิดพลาด", ex.getMessage());
        }
    }
}
