package ku.cs.controllers.user;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import ku.cs.components.*;
import ku.cs.components.button.ElevatedButton;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.components.button.OutlinedButton;
import ku.cs.models.account.User;
import ku.cs.models.account.UserList;
import ku.cs.services.*;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.UserListFileDatasource;
import ku.cs.services.utils.AlertUtil;

import java.io.IOException;

public class UserLoginController {
    private final AlertUtil alertUtil = new AlertUtil();

    @FXML
    private Button aboutUsButton;
    @FXML
    private Button goToAdminLoginButton;

    @FXML
    private Label displayLabel;
    @FXML
    private Label subDisplayLabel;

    @FXML
    private VBox usernameTextFieldVBox;
    @FXML
    private Label usernameLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private Label usernameErrorLabel;

    @FXML
    private VBox passwordTextFieldVBox;
    @FXML
    private Label passwordLabel;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private Label passwordErrorLabel;

    @FXML
    private Button registerButton;
    @FXML
    private Button loginButton;

    @FXML
    private Button goToOfficerLoginButton;

    private Datasource<UserList> usersDatasource;
    private UserList userList;

    private final SessionManager sessionManager = AppContext.getSessionManager();;

    @FXML
    public void initialize() {
        initDatasource();
        initUserInterface();
        initEvents();
    }

    private void initDatasource() {
        usersDatasource = new UserListFileDatasource("data", "test-user-data.json");
        userList = usersDatasource.readData();
    }

    private void initUserInterface() {
        FilledButtonWithIcon.MEDIUM.mask(loginButton, null, Icons.ARROW_RIGHT);
        ElevatedButton.MEDIUM.mask(registerButton);
        OutlinedButton.MEDIUM.mask(goToOfficerLoginButton);
        ElevatedButton.SMALL.mask(aboutUsButton);
        ElevatedButton.SMALL.mask(goToAdminLoginButton);
    }

    private void initEvents() {
        loginButton.setOnAction(e -> loginHandler());
        registerButton.setOnAction(e -> onRegisterButtonClick());
        goToOfficerLoginButton.setOnAction(e -> onGoToOfficerLoginButtonClick());
        goToAdminLoginButton.setOnAction(e -> onGoToAdminLoginButtonClick());
        aboutUsButton.setOnAction(e -> onAboutUsButtonClick());
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
            User user = userList.findUserByUsername(username);
            sessionManager.authenticate(user, password);
            usersDatasource.writeData(userList);
            sessionManager.login(user);
        } catch (IllegalArgumentException | IllegalStateException e) {
            alertUtil.error("เข้าสู่ระบบล้มเหลว", e.getMessage());
        }
    }

    protected void onRegisterButtonClick() {
        try {
            FXRouter.goTo("user-register");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void onGoToOfficerLoginButtonClick() {
        try {
            FXRouter.goTo("officer-login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void onGoToAdminLoginButtonClick() {
        try {
            FXRouter.goTo("admin-login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void onAboutUsButtonClick() {
        try {
            FXRouter.goTo("developer");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void onBackButtonClick() {
        try {
            FXRouter.goTo("home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
