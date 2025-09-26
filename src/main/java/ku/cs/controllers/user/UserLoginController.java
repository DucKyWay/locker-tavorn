package ku.cs.controllers.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ku.cs.components.*;
import ku.cs.components.button.ElevatedButton;
import ku.cs.components.button.FilledButton;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.components.button.OutlinedButton;
import ku.cs.models.account.User;
import ku.cs.models.account.UserList;
import ku.cs.services.*;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.UserListFileDatasource;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.PasswordUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;

public class UserLoginController {

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

        String title = "Login | Customer";

        displayLabel.setText(title);
        LabelStyle.DISPLAY_LARGE.applyTo(displayLabel);
        LabelStyle.BODY_LARGE.applyTo(subDisplayLabel);
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

        try {
            User user = userList.findUserByUsername(username);
            SessionManager.authenticate(user, password);
            usersDatasource.writeData(userList);
            SessionManager.login(user);
        } catch (IllegalArgumentException | IllegalStateException e) {
            AlertUtil.error("Login failed", e.getMessage());
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
