package ku.cs.controllers.user;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import ku.cs.components.*;
import ku.cs.components.button.ElevatedButton;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.models.account.*;
import ku.cs.models.account.OfficerList;
import ku.cs.models.account.User;
import ku.cs.models.account.UserForm;
import ku.cs.models.account.UserList;
import ku.cs.services.session.SessionManager;
import ku.cs.services.accounts.strategy.OfficerAccountProvider;
import ku.cs.services.accounts.strategy.UserAccountProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.AccountValidator;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.PasswordUtil;

import java.io.IOException;

public class UserRegisterController {
    private final SessionManager sessionManager = (SessionManager) FXRouter.getService("session");
    private final PasswordUtil passwordUtil = new PasswordUtil();
    private final OfficerAccountProvider officersProvider = new OfficerAccountProvider();
    private final UserAccountProvider usersProvider = new UserAccountProvider();
    private final AccountValidator accountValidator = new AccountValidator();

    @FXML private TextField usernameTextField;
    @FXML private Label usernameErrorLabel;
    @FXML private PasswordField passwordPasswordField;
    @FXML private Label passwordErrorLabel;

    @FXML private PasswordField confirmPasswordPasswordField;
    @FXML private Label confirmPasswordErrorLabel;

    @FXML private TextField fullNameTextField;
    @FXML private Label fullNameErrorLabel;

    // --- Email ---
    @FXML private TextField emailTextField;
    @FXML private Label emailErrorLabel;

    // --- Phone number ---
    @FXML private TextField numberTextField;
    @FXML private Label numberErrorLabel;

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
            FilledButtonWithIcon.mask(registerButton, Icons.USER_PLUS, Icons.NULL);
        if (goToUserRegisterButton  != null)
            ElevatedButtonWithIcon.SMALL.mask(goToUserRegisterButton, Icons.ARROW_LEFT);
        if (goToUserRegister2Button  != null)
            FilledButtonWithIcon.mask(goToUserRegister2Button, Icons.USER_PLUS, Icons.ARROW_RIGHT);
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
        String firstname = "";
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

            String firstnameValidate = accountValidator.validateFirstname(firstname);
            if  (firstnameValidate != null) {
                fullNameErrorLabel.setText(firstnameValidate);
                return;
            }

            String lastnameValidate = accountValidator.validateLastname(parts.length > 1 ? parts[parts.length - 1] : null);
            if  (lastnameValidate != null) {
                fullNameErrorLabel.setText(lastnameValidate);
                return;
            }
            fullNameErrorLabel.setText("");
        }

        String emailValidate = accountValidator.validateEmail(email);
        if (emailValidate != null) {
            emailErrorLabel.setText(emailValidate);
            return;
        } else {
            emailErrorLabel.setText("");
        }

        String numberValidate = accountValidator.validatePhone(number);
        if (numberValidate != null) {
            numberErrorLabel.setText(numberValidate);
            return;
        } else {
            numberErrorLabel.setText("");
        }

        String hashedPassword = passwordUtil.hashPassword(password);
        System.out.println("hashed password: " + number);
        userList.addUser(username, hashedPassword, firstname, lastname, email, number);
        usersProvider.saveCollection(userList);

        try {
            User user = userList.findByUsername(username);
            sessionManager.authenticate(user, password);
        } catch (IllegalArgumentException | IllegalStateException e) {
            new AlertUtil().error("เข้าสู่ระบบล้มเหลว", e.getMessage());
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

        String usernameValidate = accountValidator.validateUsername(username);
        if (usernameValidate != null) {
            usernameErrorLabel.setText(usernameValidate);
            return;
        } else {
            usernameErrorLabel.setText("");
        }

        if (userList.findByUsername(username) != null || officerList.canFindByUsername(username) || username.equals("admin")) {
            usernameErrorLabel.setText("ชื่อผู้ใช้นี้ถูกใช้ไปแล้ว");
            return;
        } else {
            usernameErrorLabel.setText("");
        }

        String passwordValidate = accountValidator.validatePassword(password);
        if (passwordValidate != null) {
            passwordErrorLabel.setText(passwordValidate);
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
