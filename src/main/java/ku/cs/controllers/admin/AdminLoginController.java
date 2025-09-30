package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ku.cs.components.*;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.models.account.Account;
import ku.cs.services.AppContext;
import ku.cs.services.datasources.AdminFileDatasource;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.PasswordUtil;
import ku.cs.services.SessionManager;

import java.io.FileNotFoundException;
import java.io.IOException;

public class AdminLoginController {
    private final SessionManager sessionManager = AppContext.getSessionManager();
    private final AlertUtil alertUtil = new AlertUtil();

    @FXML private Button backButton;

    @FXML private Label displayLabel;

    @FXML private VBox usernameTextFieldVBox;
    @FXML private Label usernameLabel;
    @FXML private TextField usernameTextField;
    @FXML private Label usernameErrorLabel;

    @FXML private VBox passwordTextFieldVBox;
    @FXML private Label passwordLabel;
    @FXML private PasswordField passwordPasswordField;
    @FXML private Label passwordErrorLabel;

    @FXML private Button loginButton;


    private Datasource<Account> datasource;
    private Account admin;

    @FXML
    public void initialize() {
        initDatasource();
        initUserInterface();
        initEvents();
    }

    private void initDatasource() {
        datasource = new AdminFileDatasource("data", "admin-data.json");
        admin = datasource.readData();

    }

    private void initUserInterface() {
        String title = "Login | Admin (" + admin.getUsername() + ")";
        usernameErrorLabel.setText("");
        passwordErrorLabel.setText("");

        ElevatedButtonWithIcon.SMALL.mask(backButton, Icons.ARROW_LEFT);
        FilledButtonWithIcon.mask(loginButton, null, Icons.ARROW_RIGHT);
    }

    private void initEvents() {
        loginButton.setOnAction(e -> loginHandler());
        backButton.setOnAction(e -> onBackButtonClick());
    }

    private void loginHandler() {
        String username = usernameTextField.getText().trim();
        String password = passwordPasswordField.getText().trim();

        try {

            if (admin == null) {
                throw new IllegalStateException("Admin account is not set up.");
            }

            if (!username.equals(admin.getUsername())) {
                passwordErrorLabel.setText("Incorrect username or password.");
                throw new IllegalArgumentException("Incorrect username or password.");
            }

            sessionManager.authenticate(admin, password);
            sessionManager.login(admin);

        } catch (IllegalArgumentException | IllegalStateException e) {
            alertUtil.error("Login failed", e.getMessage());
        }
    }

    protected void onBackButtonClick() {
        try {
            FXRouter.goTo("user-login");
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