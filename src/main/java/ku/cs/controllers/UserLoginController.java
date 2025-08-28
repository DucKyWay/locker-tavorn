package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.components.DefaultPasswordField;
import ku.cs.components.DefaultTextField;
import ku.cs.models.User;
import ku.cs.models.UserList;
import ku.cs.models.ZoneList;
import ku.cs.services.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserLoginController {

    @FXML private VBox titleLabelContainer;
    @FXML private VBox usernameLabelContainer;
    @FXML private VBox usernameTextFieldContainer;
    @FXML private VBox passwordLabelContainer;
    @FXML private VBox passwordPasswordFieldContainer;
    @FXML private VBox submitButtonContainer;
    @FXML private VBox registerButtonContainer;
    @FXML private VBox backButtonContainer;

    private Datasource<UserList> datasource;
    private UserList userList;


    private DefaultTextField usernameField;
    private DefaultPasswordField passwordField;
    private DefaultButton loginButton;
    private DefaultButton registerButton;
    private DefaultButton backButton;

    @FXML
    public void initialize() {
        initDatasource();
        initUserInterface();
        initEvents();
    }

    private void initDatasource() {
        datasource = new UserListFileDatasource("data", "test-user-data.json");
        try {
            userList = datasource.readData();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void initUserInterface() {

        DefaultLabel title = DefaultLabel.h2("Login | Customer");
        DefaultLabel usernameLabel = new DefaultLabel("Username");
        DefaultLabel passwordLabel = new DefaultLabel("Password");
        loginButton = DefaultButton.primary("Sign in");
        registerButton = DefaultButton.info("Sign Up");
        backButton = DefaultButton.outline("Back");
        usernameField = new DefaultTextField("username");
        passwordField = new DefaultPasswordField("********");

        titleLabelContainer.getChildren().add(title);
        usernameLabelContainer.getChildren().add(usernameLabel);
        usernameTextFieldContainer.getChildren().add(usernameField);
        passwordLabelContainer.getChildren().add(passwordLabel);
        passwordPasswordFieldContainer.getChildren().add(passwordField);
        submitButtonContainer.getChildren().add(loginButton);
        registerButtonContainer.getChildren().add(registerButton);
        backButtonContainer.getChildren().add(backButton);
    }

    private void initEvents() {
        loginButton.setOnAction(e -> loginHandler());
        registerButton.setOnAction(e -> onRegisterButtonClick());
        backButton.setOnAction(e -> onBackButtonClick());
    }

    private void loginHandler() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

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

        // suspend
        if (user.getSuspend()) {
            showAlert(Alert.AlertType.ERROR, "Login failed", "Your account is suspended.");
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
        showAlert(Alert.AlertType.INFORMATION, "Welcome", "Login successful!");
        SessionManager.login(user);
    }

    protected void onRegisterButtonClick() {
        try {
            FXRouter.goTo("user-register");
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
