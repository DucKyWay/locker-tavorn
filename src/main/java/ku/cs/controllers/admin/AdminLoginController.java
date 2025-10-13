package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import ku.cs.components.*;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.models.account.Account;
import ku.cs.services.accounts.strategy.AdminAccountProvider;
import ku.cs.services.context.AppContext;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.session.SessionManager;

import java.io.IOException;

public class AdminLoginController {
    private final SessionManager sessionManager = AppContext.getSessionManager();
    private final AdminAccountProvider adminProvider = new AdminAccountProvider();

    @FXML private Button backButton;
    @FXML private TextField usernameTextField;
    @FXML private Label usernameErrorLabel;
    @FXML private PasswordField passwordPasswordField;
    @FXML private Label passwordErrorLabel;

    @FXML private Button loginButton;

    private Account admin;

    @FXML
    public void initialize() {
        Object[] data = (Object[]) FXRouter.getData();
        usernameTextField.setText((String) data[0]);
        passwordPasswordField.setText((String) data[1]);

        initDatasource();
        initUserInterface();
        initEvents();
    }

    private void initDatasource() {
        admin = adminProvider.loadAccount();
    }

    private void initUserInterface() {
        usernameErrorLabel.setText("");
        passwordErrorLabel.setText("");

        ElevatedButtonWithIcon.SMALL.mask(backButton, Icons.ARROW_LEFT);
        FilledButtonWithIcon.mask(loginButton, Icons.NULL, Icons.ARROW_RIGHT);
    }

    private void initEvents() {
        loginButton.setOnAction(e -> loginHandler());
        backButton.setOnAction(e -> onBackButtonClick());
    }

    private void loginHandler() {
        String username = usernameTextField.getText().trim();
        String password = passwordPasswordField.getText().trim();

        if (username.isEmpty()) {
            usernameErrorLabel.setText("กรุณากรอกชื่อผู้ใช้");
            return;
        } else {
            usernameErrorLabel.setText("");
        }

        if (password.isEmpty()) {
            passwordErrorLabel.setText("กรุณากรอกรหัสผ่าน");
            return;
        } else {
            passwordErrorLabel.setText("");
        }

        try {
            if(username.equals(admin.getUsername())) {
                sessionManager.authenticate(admin, password);
            } else {
                new AlertUtil().error("เข้าสู่ระบบล้มเหลว", "ชื่อผู้ใช้ไม่ถูกต้อง");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            new AlertUtil().error("เข้าสู่ระบบล้มเหลว", e.getMessage());
        }
    }

    protected void onBackButtonClick() {
        try {
            FXRouter.goTo("user-login", new Object[] {
                    usernameTextField.getText(),
                    passwordPasswordField.getText()
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}