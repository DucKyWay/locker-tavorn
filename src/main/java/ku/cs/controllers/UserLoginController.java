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
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.PasswordUtil;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserLoginController {

    @FXML private VBox titleLabelContainer;
    @FXML private VBox usernameTextFieldContainer;
    @FXML private VBox passwordPasswordFieldContainer;
    @FXML private VBox submitButtonContainer;
    @FXML private VBox registerButtonContainer;

    private Datasource<UserList> datasource;
    private UserList userList;

    private DefaultTextField usernameField;
    private DefaultPasswordField passwordField;

    @FXML
    public void initialize() {

        datasource = new UserListFileDatasource("data", "test-user-data.csv");
        userList = datasource.readData();

        DefaultLabel title = DefaultLabel.h2("Login");

        usernameField = new DefaultTextField("username");
        passwordField = new DefaultPasswordField("********");

        DefaultButton loginBtn = DefaultButton.primary("Sign in");
        loginBtn.setOnAction(e -> loginHandler());

        DefaultButton regisBtn = DefaultButton.info("Sign Up");
        regisBtn.setOnAction(e -> onRegisterButtonClick());

        titleLabelContainer.getChildren().add(title);
        usernameTextFieldContainer.getChildren().add(usernameField);
        passwordPasswordFieldContainer.getChildren().add(passwordField);
        submitButtonContainer.getChildren().add(loginBtn);
        registerButtonContainer.getChildren().add(regisBtn);
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
        try {
            FXRouter.goTo("home", user);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void onRegisterButtonClick() {
        try {
            FXRouter.goTo("user-register");
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
