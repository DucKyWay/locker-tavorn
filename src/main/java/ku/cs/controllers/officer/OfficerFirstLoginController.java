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
import ku.cs.components.button.CustomButton;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.models.account.Officer;
import ku.cs.services.accounts.AccountService;
import ku.cs.services.context.AppContext;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.session.SessionManager;
import ku.cs.services.utils.AlertUtil;

public class OfficerFirstLoginController {
    private final SessionManager sessionManager = AppContext.getSessionManager();
    private final AlertUtil alertUtil = new AlertUtil();

    // Interfaces
    @FXML private VBox parentVBox;
    @FXML private HBox newPasswordHBox;
    @FXML private HBox confirmPasswordHBox;
    @FXML private HBox changePasswordHBox;

    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label newPasswordLabel;
    @FXML private Label confirmPasswordLabel;
    private PasswordField newPasswordPasswordField;
    private PasswordField confirmPasswordPasswordField;
    private Button changePasswordButton;

    private Button backButton;

    // Controller

    protected Officer current = (Officer) FXRouter.getData();

    @FXML public void initialize() {
        initUserInterfaces();
        initEvents();
    }

    private void initUserInterfaces() {
        Region region = new Region();

        backButton = new ElevatedButtonWithIcon("ย้อนกลับ", Icons.ARROW_LEFT);
        titleLabel.setText("ยินดีต้อนรับ! Officer " + current.getFirstname());
        descriptionLabel.setText("เนื่องจากผู้ใช้ " + current.getUsername() + " ได้เข้าสู่ระบบครั้งแรก จึงต้องเปลี่ยนรหัสผ่านก่อนถึงจะสามารถใช้งานระบบได้");

        newPasswordLabel.setText("New Password: ");
        newPasswordPasswordField = new PasswordField();
        newPasswordPasswordField.setPromptText("New password");
        confirmPasswordLabel.setText("Confirm Password: ");
        confirmPasswordPasswordField = new PasswordField();
        confirmPasswordPasswordField.setPromptText("Confirm password");

        changePasswordButton = new CustomButton("Change Password");

        // Style
        LabelStyle.TITLE_LARGE.applyTo(titleLabel);
        LabelStyle.TITLE_SMALL.applyTo(descriptionLabel);
        LabelStyle.BODY_MEDIUM.applyTo(newPasswordLabel);
        LabelStyle.BODY_MEDIUM.applyTo(confirmPasswordLabel);

        parentVBox.setSpacing(40);

        region.setPrefSize(10, 10);

        newPasswordHBox.getChildren().addAll(newPasswordPasswordField);
        confirmPasswordHBox.getChildren().addAll(confirmPasswordPasswordField);
        changePasswordHBox.getChildren().addAll(backButton, region, changePasswordButton);
    }

    private void initEvents() {
        changePasswordButton.setOnAction(e -> onChangePasswordButtonClick());
        backButton.setOnAction(e -> onBackButtonClick());
    }

    protected void onChangePasswordButtonClick() {

        String newPassword = newPasswordPasswordField.getText().trim();
        String confirmPassword = confirmPasswordPasswordField.getText().trim();

        if (!newPassword.equals(confirmPassword)) {
            alertUtil.error("รหัสผ่านใหม่ไม่ตรงกัน", "กรุณาตรวจสอบ New/Confirm Password");
            return;
        }

        try {

            AccountService accountService = new AccountService(current);
            accountService.changePasswordFirstOfficer(newPassword);
            alertUtil.info("สำเร็จ", "เปลี่ยนรหัสผ่านเรียบร้อยแล้ว");

            sessionManager.login(current);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            alertUtil.error("ไม่สามารถเปลี่ยนรหัสผ่าน", ex.getMessage());
        } catch (RuntimeException ex) {
            alertUtil.error("เกิดข้อผิดพลาด", ex.getMessage());
        }
    }

    protected void onBackButtonClick() {
        sessionManager.logout();
    }
}
