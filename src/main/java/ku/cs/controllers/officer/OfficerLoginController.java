package ku.cs.controllers.officer;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import ku.cs.components.Icons;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.ElevatedButton;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.session.SessionManager;
import ku.cs.services.accounts.strategy.OfficerAccountProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.AlertUtil;

import java.io.IOException;

public class OfficerLoginController {
    private final SessionManager sessionManager = (SessionManager) FXRouter.getService("session");
    protected final OfficerAccountProvider officersProvider = new OfficerAccountProvider();
    private final AlertUtil alertUtil = new AlertUtil();


    @FXML private Button backButton;

    @FXML private Label displayLabel;
    @FXML private Label subDisplayLabel;

    @FXML private TextField usernameTextField;
    @FXML private Label usernameErrorLabel;

    @FXML private PasswordField passwordPasswordField;
    @FXML private Label passwordErrorLabel;

    @FXML private Button loginButton;
    @FXML private Button goToUserLoginButton;
    @FXML private Button goToAdminLoginButton;
    private OfficerList officerList;

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
        officerList = officersProvider.loadCollection();
    }

    private void initUserInterface() {
        LabelStyle.DISPLAY_LARGE.applyTo(displayLabel);
        LabelStyle.BODY_LARGE.applyTo(subDisplayLabel);
        ElevatedButtonWithIcon.SMALL.mask(backButton, Icons.ARROW_LEFT);
        ElevatedButton.SMALL.mask(goToAdminLoginButton);
        ElevatedButton.SMALL.mask(goToUserLoginButton);
        FilledButtonWithIcon.mask(loginButton, Icons.USER_CHECK, Icons.ARROW_RIGHT);
    }

    private void initEvents() {
        backButton.setOnAction(event -> {onBackButtonClick();});
        goToUserLoginButton.setOnAction(event -> {onBackButtonClick();});
        goToAdminLoginButton.setOnAction(event -> {onAdminLoginButtonClick();});
        loginButton.setOnAction(event -> {onLoginButtonClick();});
    }

    @FXML
    protected void onLoginButtonClick() {
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
            Officer officer = officerList.findByUsername(username);
            sessionManager.authenticate(officer, password);
        } catch (IllegalArgumentException | IllegalStateException e) {
            alertUtil.error("เข้าสู่ระบบล้มเหลว", e.getMessage());
        }
    }

    @FXML protected void onAdminLoginButtonClick() {
        try {
            FXRouter.goTo("admin-login", new Object[] {
                    usernameTextField.getText(),
                    passwordPasswordField.getText()
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML protected void onBackButtonClick() {
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
