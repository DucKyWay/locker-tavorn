package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.components.DefaultPasswordField;
import ku.cs.components.DefaultTextField;
import ku.cs.models.Admin;
import ku.cs.services.AdminFileDatasource;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.PasswordUtil;
import ku.cs.services.SessionManager;

import java.io.FileNotFoundException;
import java.io.IOException;

public class AdminLoginController {

    @FXML private VBox titleLabelContainer;
    @FXML private VBox usernameLabelContainer;
    @FXML private VBox usernameTextFieldContainer;
    @FXML private VBox passwordLabelContainer;
    @FXML private VBox passwordPasswordFieldContainer;
    @FXML private VBox submitButtonContainer;
    @FXML private VBox backButtonContainer;

    private Datasource<Admin> datasource;
    private Admin admin;

    private DefaultTextField usernameField;
    private DefaultPasswordField passwordField;
    private DefaultButton loginButton;
    private DefaultButton backButton;

    @FXML
    public void initialize() {
        initDatasource();
        initUserInterface();
        initEvents();
    }

    private void initDatasource() {
        datasource = new AdminFileDatasource("data", "test-admin-data.json");
        try {
            admin = datasource.readData();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void initUserInterface() {
        DefaultLabel title = DefaultLabel.h2("Login | Admin");
        DefaultLabel usernameLabel = new DefaultLabel("Username");
        DefaultLabel passwordLabel = new DefaultLabel("Password");

        loginButton = DefaultButton.primary("Sign in");
        backButton = DefaultButton.outline("Back");

        usernameField = new DefaultTextField("username");
        passwordField = new DefaultPasswordField("********");

        titleLabelContainer.getChildren().add(title);
        usernameLabelContainer.getChildren().add(usernameLabel);
        usernameTextFieldContainer.getChildren().add(usernameField);
        passwordLabelContainer.getChildren().add(passwordLabel);
        passwordPasswordFieldContainer.getChildren().add(passwordField);
        submitButtonContainer.getChildren().add(loginButton);
        backButtonContainer.getChildren().add(backButton);
    }

    private void initEvents() {
        loginButton.setOnAction(e -> loginHandler());
        backButton.setOnAction(e -> onBackButtonClick());
    }

    private void loginHandler() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login failed", "Please enter username and password.");
            return;
        }

        if (admin == null || admin.getUsername() == null || admin.getUsername().isBlank()) {
            showAlert(Alert.AlertType.ERROR, "Login failed", "Admin is not set up.");
            return;
        }

        if (!username.equals(admin.getUsername())) {
            showAlert(Alert.AlertType.ERROR, "Login failed", "Incorrect username or password.");
            return;
        }

        String storedHash = admin.getPassword();
        if (storedHash == null || storedHash.isBlank() ||
                !PasswordUtil.matches(password, storedHash)) {
            showAlert(Alert.AlertType.ERROR, "Login failed", "Incorrect username or password.");
            return;
        }

        // success
        showAlert(Alert.AlertType.INFORMATION, "Welcome", "Login successful!");
        try {
            SessionManager.login(admin);
            FXRouter.goTo("user-home", admin);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void onBackButtonClick() {
        try {
            FXRouter.goTo("officer-login");
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
