package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ku.cs.components.*;
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




    private Datasource<Account> datasource;
    private Account admin;

    private final SessionManager sessionManager = AppContext.getSessionManager();

    @FXML
    public void initialize() {
        initDatasource();
        initUserInterface();
        initEvents();
    }

    private void initDatasource() {
        datasource = new AdminFileDatasource("data", "test-admin-data.json");
        admin = datasource.readData();

    }

    private void initUserInterface() {
        String title = "Login | Admin (" + admin.getUsername() + ")";

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

        try {

            if (admin == null) {
                throw new IllegalStateException("Admin account is not set up.");
            }

            if (!username.equals(admin.getUsername())) {
                throw new IllegalArgumentException("Incorrect username or password.");
            }

            sessionManager.authenticate(admin, password);
            sessionManager.login(admin);

        } catch (IllegalArgumentException | IllegalStateException e) {
            AlertUtil.error("Login failed", e.getMessage());
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