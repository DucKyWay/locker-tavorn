package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.components.DefaultPasswordField;
import ku.cs.components.DefaultTextField;
import ku.cs.models.UserList;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.PasswordUtil;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;

public class UserRegisterController {

    @FXML private VBox registerLabelContainer;
    @FXML private VBox usernameTextFieldContainer;
    @FXML private VBox passwordPasswordFieldContainer;
    @FXML private VBox confirmPasswordPasswordFieldContainer;
    @FXML private VBox nameTextFieldContainer;
    @FXML private VBox emailTextFieldContainer;
    @FXML private VBox telephoneTexTFieldContainer;
    @FXML private VBox submitButtonContainer;
    @FXML private VBox loginButtonContainer;

    Datasource<UserList> datasource;
    UserList userList;

    private DefaultTextField username;
    private DefaultPasswordField password;
    private DefaultPasswordField confirmPassword;
    private DefaultTextField name;
    private DefaultTextField email;
    private DefaultTextField telephoneText;

    @FXML
    public void initialize() {

        datasource = new UserListFileDatasource("data", "test-user-data.csv");
        userList = datasource.readData();

        DefaultLabel registerLabel = DefaultLabel.h2("Register");

        username = new DefaultTextField("username");
        password = new DefaultPasswordField("********");
        confirmPassword = new DefaultPasswordField("********");
        name = new DefaultTextField("name");
        email = new DefaultTextField("email");
        telephoneText = new DefaultTextField("telephone");

        DefaultButton register = DefaultButton.primary("Register");
        register.setOnAction(e -> registerHandler());

        DefaultButton login = DefaultButton.info("Login");
        login.setOnAction(e -> onLoginButtonClick());

        registerLabelContainer.getChildren().add(registerLabel);
        usernameTextFieldContainer.getChildren().add(username);
        passwordPasswordFieldContainer.getChildren().add(password);
        confirmPasswordPasswordFieldContainer.getChildren().add(confirmPassword);
        nameTextFieldContainer.getChildren().add(name);
        emailTextFieldContainer.getChildren().add(email);
        telephoneTexTFieldContainer.getChildren().add(telephoneText);
        submitButtonContainer.getChildren().add(register);
        loginButtonContainer.getChildren().add(login);
    }

    public void registerHandler() {
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
        userList.addUser(u, hashedPassword, n, em, tel);
        datasource.writeData(userList);

        // alert, go home
        showAlert("Success", "Registration successful!");
        try {
            FXRouter.goTo("home");
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

    // alert when success
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
