package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import ku.cs.components.*;
import ku.cs.models.Account;
import ku.cs.models.Admin;
import ku.cs.services.AdminFileDatasource;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.PasswordUtil;
import ku.cs.services.SessionManager;

import java.io.FileNotFoundException;
import java.io.IOException;

public class AdminLoginController {
    @FXML private HBox navbarHBox;
    @FXML private HBox navbarLeftHBox;
    @FXML private Button backButton;
    @FXML private HBox navbarRightHBox;
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

    @FXML private Button loginButton;

    @FXML private Label footerLabel;

    private Datasource<Admin> datasource;
    private Account account;

    @FXML
    public void initialize() {
        initDatasource();
        initUserInterface();
        initEvents();
    }

    private void initDatasource() {
        datasource = new AdminFileDatasource("data", "test-admin-data.json");
        try {
            account = datasource.readData();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void initUserInterface() {
        String title = "Login | Admin (" + account.getUsername() + ")";

        displayLabel.setText(title);
        LabelStyle.DISPLAY_LARGE.applyTo(displayLabel);
        changeThemeButton.setGraphic(new Icon(Icons.SMILEY, 24));
    }

    private void initEvents() {
        loginButton.setOnAction(e -> loginHandler());
        backButton.setOnAction(e -> onBackButtonClick());
    }

    private void loginHandler() {
        String username = usernameTextField.getText().trim();
        String password = passwordPasswordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login failed", "Please enter username and password.");
            return;
        }

        if (account == null || account.getUsername() == null || account.getUsername().isBlank()) {
            showAlert(Alert.AlertType.ERROR, "Login failed", "Admin is not set up.");
            return;
        }

        if (!username.equals(account.getUsername())) {
            showAlert(Alert.AlertType.ERROR, "Login failed", "Incorrect username or password.");
            return;
        }

        String storedHash = account.getPassword();
        if (storedHash == null || storedHash.isBlank() ||
                !PasswordUtil.matches(password, storedHash)) {
            showAlert(Alert.AlertType.ERROR, "Login failed", "Incorrect username or password.");
            return;
        }

        // success
        showAlert(Alert.AlertType.INFORMATION, "Welcome", "Login successful!");
        try {
            SessionManager.login(account);
            FXRouter.goTo("admin-home", account);
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
