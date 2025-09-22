package ku.cs.controllers.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ku.cs.components.*;
import ku.cs.models.account.User;
import ku.cs.models.account.UserList;
import ku.cs.services.*;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.UserListFileDatasource;
import ku.cs.services.utils.PasswordUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;

public class UserLoginController {
    @FXML private HBox navbarHBox;
    @FXML private HBox navbarLeftHBox;
    @FXML private Button backButton;
    @FXML private HBox navbarRightHBox;
    @FXML private Button aboutUsButton;
    @FXML private Button goToAdminLoginButton;
    @FXML private Button changeThemeButton;

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

    @FXML private Button registerButton;
    @FXML private Button loginButton;

    @FXML private Button goToOfficerLoginButton;

    @FXML private Label footerLabel;

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
        changeThemeButton.setGraphic(new Icon(Icons.SMILEY, 24));
    }

    private void initEvents() {
        loginButton.setOnAction(e -> loginHandler());
        registerButton.setOnAction(e -> onRegisterButtonClick());
        backButton.setOnAction(e -> onBackButtonClick());
        goToOfficerLoginButton.setOnAction(e -> onGoToOfficerLoginButtonClick());
        goToAdminLoginButton.setOnAction(e -> onGoToAdminLoginButtonClick());
        aboutUsButton.setOnAction(e -> onAboutUsButtonClick());
    }

    private void loginHandler() {
        String username = usernameTextField.getText().trim();
        String password = passwordPasswordField.getText().trim();

        // Required all field
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login failed", "Please enter username and password.");
            return;
        }

        // find user
        User user = userList.findUserByUsername(username);
        if (user == null) {
            showAlert(Alert.AlertType.ERROR, "Login failed", "User not found.");
            return;
        }

        // check hash
        String inputHashed = PasswordUtil.hashPassword(password);
        String storedHashed = user.getPassword();

        if (!inputHashed.equalsIgnoreCase(storedHashed)) {
            showAlert(Alert.AlertType.ERROR, "Login failed", "Incorrect password.");
            return;
        }

        // success
        user.setLogintime(LocalDateTime.now());
        usersDatasource.writeData(userList);

        SessionManager.login(user);
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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
