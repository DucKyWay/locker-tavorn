package ku.cs.controllers.officer;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.services.*;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.OfficerListFileDatasource;
import ku.cs.services.utils.PasswordUtil;

import java.io.IOException;
import java.time.LocalDateTime;

public class OfficerLoginController {
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
    @FXML private Button goToUserLoginButton;


    @FXML private Label footerLabel;

    private Datasource<OfficerList> datasource;
    private OfficerList officerList;
    @FXML
    public void initialize() {
        initDatasource();
    }
    private void initDatasource() {
        datasource = new OfficerListFileDatasource("data","test-officer-data.json");
        officerList = datasource.readData();

    }
    @FXML
    protected void onloginButton() {
        String username = usernameTextField.getText().trim();
        String password = passwordPasswordField.getText().trim();

        // Required all field
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login failed", "Please enter username and password.");
            return;
        }

        // find officer
        Officer officer = officerList.findOfficerByUsername(username);
        if (officer == null) {
            showAlert(Alert.AlertType.ERROR, "Login failed", "User not found.");
            return;
        }

        // suspend


        // check hash
        String inputHashed = PasswordUtil.hashPassword(password);
        String storedHashed = officer.getPassword();
        System.out.println("stored password: " + storedHashed);
        System.out.println("input password: " + inputHashed);
        if (!inputHashed.equalsIgnoreCase(storedHashed)) {
            showAlert(Alert.AlertType.ERROR, "Login failed", "Incorrect password.");
            return;
        }

        // success
        officer.setLogintime(LocalDateTime.now());
        datasource.writeData(officerList);

        showAlert(Alert.AlertType.INFORMATION, "Welcome", "Login successful!");
        SessionManager.login(officer);
    }

    @FXML protected void onAdminLoginButtonClick() {
        try {
            FXRouter.goTo("admin-login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML protected void onBackButtonClick() {
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
