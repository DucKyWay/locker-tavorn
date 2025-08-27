package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import ku.cs.models.Officer;
import ku.cs.models.OfficerList;
import ku.cs.services.*;

import java.io.IOException;

public class OfficerLoginController {
    @FXML
    private Label footerLabel;
    @FXML
    private VBox contentVBox;
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
    private PasswordField passwordTextField;
    @FXML
    private Label passwordErrorLabel;
    @FXML
    private Button loginButton;
    @FXML
    private Button backButton;
    @FXML
    private Button changeThemeButton;
    @FXML
    private TextField login;
    @FXML
    private PasswordField password;
    private Datasource<OfficerList> datasource;
    private OfficerList officerList;
    @FXML
    public void initialize() {
        initDatasource();
    }
    private void initDatasource() {
        officerList = new OfficerListHardCodeDatasource().readdata();
        datasource = new OfficerListFileDatasource("data","test-officer-data.json");
        try {
            datasource.writeData(officerList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onloginButton() {
        String username = usernameTextField.getText().trim();
        String password = passwordTextField.getText().trim();

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

        if (!inputHashed.equalsIgnoreCase(storedHashed)) {
            showAlert(Alert.AlertType.ERROR, "Login failed", "Incorrect password.");
            return;
        }

        // success
        showAlert(Alert.AlertType.INFORMATION, "Welcome", "Login successful!");
        try {
            SessionManager.login(officer);
            FXRouter.goTo("user-home", officer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
