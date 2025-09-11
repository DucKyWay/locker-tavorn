package ku.cs.controllers.user;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.components.DefaultPasswordField;
import ku.cs.components.DefaultTextField;
import ku.cs.models.account.User;
import ku.cs.models.account.UserList;
import ku.cs.services.*;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.UserListFileDatasource;
import ku.cs.services.utils.PasswordUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserRegisterController {

    @FXML private VBox registerLabelContainer;
    @FXML private VBox usernameLabelContainer;
    @FXML private VBox usernameTextFieldContainer;
    @FXML private VBox passwordLabelContainer;
    @FXML private VBox passwordPasswordFieldContainer;
    @FXML private VBox confirmPasswordLabelContainer;
    @FXML private VBox confirmPasswordPasswordFieldContainer;
    @FXML private VBox nameLabelContainer;
    @FXML private VBox nameTextFieldContainer;
    @FXML private VBox emailLabelContainer;
    @FXML private VBox emailTextFieldContainer;
    @FXML private VBox telephoneLabelContainer;
    @FXML private VBox telephoneTexTFieldContainer;
    @FXML private VBox submitButtonContainer;
    @FXML private VBox loginButtonContainer;
    @FXML private VBox backButtonContainer;



    Datasource<UserList> datasource;
    UserList userList;

    private DefaultTextField username;
    private DefaultPasswordField password;
    private DefaultPasswordField confirmPassword;
    private DefaultTextField name;
    private DefaultTextField email;
    private DefaultTextField telephoneText;
    private DefaultButton registerButton;
    private DefaultButton loginButton;
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
        DefaultLabel registerLabel = DefaultLabel.h2("Register | Customer");
        DefaultLabel usernameLabel = new DefaultLabel("Username");
        DefaultLabel passwordLabel = new DefaultLabel("Password");
        DefaultLabel confirmPasswordLabel = new DefaultLabel("Confirm Password");
        DefaultLabel nameLabel = new DefaultLabel("Name");
        DefaultLabel emailLabel = new DefaultLabel("Email");
        DefaultLabel telephoneLabel = new DefaultLabel("Tel.");
        registerButton = DefaultButton.primary("Register");
        loginButton = DefaultButton.info("Login");
        backButton = DefaultButton.outline("Back");
        username = new DefaultTextField("username");
        password = new DefaultPasswordField("********");
        confirmPassword = new DefaultPasswordField("********");
        name = new DefaultTextField("name");
        email = new DefaultTextField("email");
        telephoneText = new DefaultTextField("telephone");

        registerLabelContainer.getChildren().add(registerLabel);
        usernameTextFieldContainer.getChildren().add(username);
        usernameLabelContainer.getChildren().add(usernameLabel);
        passwordPasswordFieldContainer.getChildren().add(password);
        passwordLabelContainer.getChildren().add(passwordLabel);
        confirmPasswordPasswordFieldContainer.getChildren().add(confirmPassword);
        confirmPasswordLabelContainer.getChildren().add(confirmPasswordLabel);
        nameTextFieldContainer.getChildren().add(name);
        nameLabelContainer.getChildren().add(nameLabel);
        emailTextFieldContainer.getChildren().add(email);
        emailLabelContainer.getChildren().add(emailLabel);
        telephoneTexTFieldContainer.getChildren().add(telephoneText);
        telephoneLabelContainer.getChildren().add(telephoneLabel);
        submitButtonContainer.getChildren().add(registerButton);
        loginButtonContainer.getChildren().add(loginButton);
        backButtonContainer.getChildren().add(backButton);
    }

    private void initEvents() {
        registerButton.setOnAction(e -> registerHandler());
        loginButton.setOnAction(e -> onLoginButtonClick());
        backButton.setOnAction(e -> onBackButtonClick());
    }

    public void registerHandler() {
        LocalDateTime today = LocalDateTime.now();
        String u = username.getText().trim();
        String p = password.getText().trim();
        String cp = confirmPassword.getText().trim();
        String n = name.getText().trim();
        String em = email.getText().trim();
        String tel = telephoneText.getText().trim();

        // Required all field
        if (u.isEmpty() || p.isEmpty() || cp.isEmpty() || n.isEmpty() || em.isEmpty() || tel.isEmpty()) {
            showAlert("Error", "Please fill in all fields");
            return;
        }

        // Password Correct
        if (!p.equals(cp)) {
            showAlert("Error", "Passwords do not match");
            return;
        }

        // check username existed
        if (userList.findUserByUsername(u) != null) {
            showAlert("Error", "Username already exists");
            return;
        }

        // hash
        String hashedPassword = PasswordUtil.hashPassword(p);

        // add user
        userList.addUser(u, hashedPassword, n, em, tel,today);
        try {
            datasource.writeData(userList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        User user = userList.findUserByUsername(u);

        // alert, go home
        showAlert("Success", "Registration successful!");
        try {
            SessionManager.login(user);
            FXRouter.goTo("user-home");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void onLoginButtonClick() {
        try {
            FXRouter.goTo("user-login");
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

    // alert when success
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
