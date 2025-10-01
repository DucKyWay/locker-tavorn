package ku.cs.controllers.user;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ku.cs.components.*;
import ku.cs.components.button.ElevatedButton;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.models.account.OfficerList;
import ku.cs.models.account.User;
import ku.cs.models.account.UserList;
import ku.cs.services.*;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.OfficerListFileDatasource;
import ku.cs.services.datasources.UserListFileDatasource;
import ku.cs.services.strategy.account.OfficerAccountProvider;
import ku.cs.services.strategy.account.UserAccountProvider;
import ku.cs.services.utils.PasswordUtil;

import java.io.IOException;

public class UserRegisterController {
    private final SessionManager sessionManager = AppContext.getSessionManager();
    protected final OfficerAccountProvider officersProvider = new OfficerAccountProvider();
    protected final UserAccountProvider usersProvider = new UserAccountProvider();

    @FXML private VBox contentVBox;
    @FXML private Label displayLabel;
    @FXML private Label subDisplayLabel;

    @FXML private VBox usernameTextFieldVBox;
    @FXML private Label usernameLabel;
    @FXML private TextField usernameTextField;
    @FXML private Label usernameErrorLabel;

    @FXML private VBox passwordTextFieldVBox;
    @FXML private Label passwordLabel;
    @FXML private PasswordField passwordPasswordField;
    @FXML private Label passwordErrorLabel;

    @FXML private VBox confirmPasswordTextFieldVBox;
    @FXML private Label confirmPasswordLabel;
    @FXML private PasswordField confirmPasswordPasswordField;
    @FXML private Label confirmPasswordErrorLabel;

    @FXML private VBox  fullNameTextFieldVBox;
    @FXML private Label fullNameLabel;
    @FXML private TextField fullNameTextField;
    @FXML private Label fullNameErrorLabel;

    // --- Email ---
    @FXML private VBox  emailTextFieldVBox;
    @FXML private Label emailLabel;
    @FXML private TextField emailTextField;
    @FXML private Label emailErrorLabel;

    // --- Phone number ---
    @FXML private VBox  numberTextFieldVBox;
    @FXML private Label numberLabel;
    @FXML private TextField numberTextField;
    @FXML private Label numberErrorLabel;


    @FXML private HBox navbarHBox;
    @FXML private HBox navbarLeftHBox;
    @FXML private HBox navbarRightHBox;

    @FXML private Button registerButton;
    @FXML private Button goToUserRegisterButton;
    @FXML private Button goToUserRegister2Button;
    @FXML private Button goToUserLoginButton;
    @FXML private Button goToAdminLoginButton;
    @FXML private Button backButton;

    private UserList userList;
    private OfficerList officerList;

    @FXML
    public void initialize() {
        initDatasource();
        initUserInterface();
        initEvents();
    }

    private void initDatasource() {
        userList = usersProvider.loadCollection();
        officerList = officersProvider.loadCollection();
    }

    private void initUserInterface() {
        if (registerButton != null)
            FilledButtonWithIcon.mask(registerButton, Icons.USER_PLUS, Icons.ARROW_RIGHT);
        if (goToUserRegisterButton  != null)
            ElevatedButtonWithIcon.SMALL.mask(goToUserRegisterButton, Icons.ARROW_LEFT);
        if (goToUserRegister2Button  != null)
            FilledButtonWithIcon.mask(goToUserRegister2Button, Icons.USER_PLUS);
        if (goToUserLoginButton  != null)
            ElevatedButton.SMALL.mask(goToUserLoginButton);
        if (goToAdminLoginButton  != null)
            ElevatedButton.SMALL.mask(goToAdminLoginButton);
        if (backButton != null)
            ElevatedButtonWithIcon.SMALL.mask(backButton, Icons.ARROW_LEFT);
    }

    private void initEvents() {
        if (registerButton != null)
            registerButton.setOnAction(e -> registerHandler());
        if (goToUserRegisterButton != null)
            goToUserRegisterButton.setOnAction(event -> onGoToUserRegisterClick());
        if (goToUserRegister2Button != null)
            goToUserRegister2Button.setOnAction(event -> onGoToUserRegister2Click());
        if (goToUserLoginButton != null)
            goToUserLoginButton.setOnAction(e -> onGoToUserLoginButtonClick());
        if (goToAdminLoginButton != null)
            goToAdminLoginButton.setOnAction(e -> goToAdminLoginButtonClick());
        if (backButton != null)
            backButton.setOnAction(e -> onBackButtonClick());
    }

    public void registerHandler() {
        String[] data = (String[]) FXRouter.getData();
        String username = data[0];
        String password = data[1];
        String fullName = fullNameTextField.getText();
        String firstname;
        String lastname = "";
        String email = emailTextField.getText();
        String number = numberTextField.getText();

        if (fullName.isEmpty()) {
            fullNameErrorLabel.setText("กรุณากรอกชื่อ–นามสกุล");
            return;
        } else {
            String[] parts = fullName.split("\\s+");
            firstname = parts[0];
            if (parts.length > 1) {
                lastname = parts[parts.length - 1];
            }
            fullNameErrorLabel.setText("");
        }

        if (email.isEmpty()) {
            emailErrorLabel.setText("กรุณากรอกอีเมล");
            return;
        } else {
            emailErrorLabel.setText("");
        }

        if (number.isEmpty()) {
            numberErrorLabel.setText("กรุณากรอกเบอร์โทรศัพท์");
            return;
        } else {
            numberErrorLabel.setText("");
        }

        String hashedPassword = PasswordUtil.hashPassword(password);

        userList.addUser(username, hashedPassword, firstname, lastname, email, number);
        usersProvider.saveCollection(userList);
        User user = userList.findUserByUsername(username);
        try {
            sessionManager.login(user);
            FXRouter.goTo("user-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void onGoToUserRegisterClick() {
        try {
            FXRouter.goTo("user-register");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void onGoToUserRegister2Click() {
        String username = usernameTextField.getText();
        String password = passwordPasswordField.getText();
        String confirmPassword = confirmPasswordPasswordField.getText();

        if (username.isEmpty()) {
            usernameErrorLabel.setText("กรุณากรอกชื่อผู้ใช้");
            return;
        } else {
            usernameErrorLabel.setText("");
        }

        if (userList.findUserByUsername(username) != null || officerList.canFindOfficerByUsername(username) || username.equals("admin")) {
            usernameErrorLabel.setText("ชื่อผู้ใช้นี้ถูกใช้ไปแล้ว");
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

        if (confirmPassword.isEmpty()) {
            confirmPasswordErrorLabel.setText("กรุณากรอกยืนยันรหัสผ่าน");
            return;
        } else {
            confirmPasswordErrorLabel.setText("");
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordErrorLabel.setText("รหัสผ่านไม่ตรงกัน");
            return;
        }

        String[] data = {username, confirmPassword};
        try {
            FXRouter.goTo("user-register-2", data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void onGoToUserLoginButtonClick() {
        try {
            FXRouter.goTo("user-login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void goToAdminLoginButtonClick() {
        try {
            FXRouter.goTo("admin-login");
        } catch (IOException e) {
            throw new RuntimeException(e);
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
