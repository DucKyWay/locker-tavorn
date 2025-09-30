package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import ku.cs.components.*;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.models.account.Account;
import ku.cs.services.AppContext;
import ku.cs.services.datasources.AdminFileDatasource;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.SessionManager;

import java.io.IOException;

public class AdminLoginController {
    private final SessionManager sessionManager = AppContext.getSessionManager();
    private final AlertUtil alertUtil = new AlertUtil();

    @FXML private Button backButton;

    @FXML private Label displayLabel;

    @FXML private VBox usernameTextFieldVBox;
    @FXML private Label usernameLabel;
    @FXML private TextField usernameTextField;
    @FXML private Label usernameErrorLabel;

    @FXML private VBox passwordTextFieldVBox;
    @FXML private Label passwordLabel;
    @FXML private PasswordField passwordPasswordField;
    @FXML private Label passwordErrorLabel;

    @FXML private Button loginButton;


    private Datasource<Account> datasource;
    private Account admin;

    @FXML
    public void initialize() {
        initDatasource();
        initUserInterface();
        initEvents();
    }

    private void initDatasource() {
        datasource = new AdminFileDatasource("data", "admin-data.json");
        admin = datasource.readData();

    }

    private void initUserInterface() {
        usernameErrorLabel.setText("");
        passwordErrorLabel.setText("");

        ElevatedButtonWithIcon.SMALL.mask(backButton, Icons.ARROW_LEFT);
        FilledButtonWithIcon.mask(loginButton, null, Icons.ARROW_RIGHT);
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
            if (admin == null) {
                throw new IllegalStateException("ยังไม่ได้ตั้งค่าบัญชีผู้ดูแลระบบ");
            }
            sessionManager.authenticate(admin, password);
            sessionManager.login(admin);

        } catch (IllegalArgumentException | IllegalStateException e) {
            alertUtil.error("เข้าสู่ระบบล้มเหลว", e.getMessage());
        }
    }

    protected void onBackButtonClick() {
        try {
            FXRouter.goTo("user-login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}