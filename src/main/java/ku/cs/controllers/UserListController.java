package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ku.cs.models.User;
import ku.cs.models.UserList;
import javafx.scene.control.TextField;
import ku.cs.services.Datasource;
import ku.cs.services.UserListFileDatasource;

public class UserListController {
    @FXML TextField registerUsernameTextfield;
    @FXML TextField registerPasswordTextfield;
    @FXML TextField registerNameTextfield;
    @FXML TextField registerEmailTextfield;
    @FXML TextField registerConfirmPasswordTextField;
    @FXML TextField registerTelephoneTextField;
    @FXML Label errorUsernameLabel;
    @FXML Label errorPasswordLabel;
    @FXML Label errorNameLabel;
    @FXML Label errorEmailLabel;
    @FXML Label errorTelephoneLabel;
    @FXML Label errorConfirmPasswordLabel;

    @FXML TextField loginUsernameTextfield;
    @FXML TextField loginPasswordTextfield;
    @FXML Label errorLoginUsernameLabel;
    @FXML Label errorLoginPasswordLabel;


    User user = null;
    private Datasource<UserList> datasource;
    private UserList userList;

    @FXML
    public void initialize(){
        clearErrorLabel();
        clearTextfield();
        datasource = new UserListFileDatasource("data","test-user-data.csv");
        userList = datasource.readData();
        showList(userList);
    }

    private void showList(UserList userList) {
        System.out.println(userList.getUsers());
    }

    @FXML
    public void registerAction(){
        clearErrorLabel();
        String usernameText = registerUsernameTextfield.getText();
        String passwordText = registerPasswordTextfield.getText();
        String confirmPasswordText = registerConfirmPasswordTextField.getText();
        String nameText = registerNameTextfield.getText();
        String emailText = registerEmailTextfield.getText();
        String telephoneText = registerTelephoneTextField.getText();
        boolean isError = false;
        if(usernameText.isEmpty()){
            errorUsernameLabel.setText("Username is empty");
            isError = true;
        }
        if(passwordText.isEmpty()){
            errorPasswordLabel.setText("Password is empty");
            isError = true;
        }
        if(confirmPasswordText.isEmpty()){
            errorConfirmPasswordLabel.setText("Confirm Password is empty");
            isError = true;
        }
        if(nameText.isEmpty()){
            errorNameLabel.setText("Name is empty");
            isError = true;
        }
        if(emailText.isEmpty()){
            errorEmailLabel.setText("Email is empty");
            isError = true;
        }
        if(telephoneText.isEmpty()){
            errorTelephoneLabel.setText("Telephone is empty");
            isError = true;
        }
        user = userList.findUserByUsername(usernameText);
        if(user != null){
            errorUsernameLabel.setText("This Username have taken");
            isError = true;
        }
        if(passwordText.length() < 8){
            errorPasswordLabel.setText("Password length less than 8");
            isError = true;
        }
        else{
            if(!passwordText.equals(confirmPasswordText)){
                errorPasswordLabel.setText("Passwords do not match");
                isError = true;
            }
        }
        if(!emailText.contains("@")&&(emailText.length() - emailText.replace("@", "").length() != 1) && emailText.contains(".")){
            errorEmailLabel.setText("Format email wrong");
            isError = true;
        }
        if((telephoneText.length() != 10) || (telephoneText.charAt(0)!= '0')|| (telephoneText.charAt(1) =='0') || (telephoneText.charAt(1) =='1') ){
            errorTelephoneLabel.setText("Format telephone number wrong");
            isError = true;
        }
        if(!isError){
            userList.addUser(usernameText,passwordText,nameText,emailText,telephoneText);
            errorUsernameLabel.setText("SUCCESS");
            System.out.println(userList.getUsers());
            datasource.writeData(userList);
            clearTextfield();
        }
    }
    public void clearTextfield(){
        registerNameTextfield.setText("");
        registerPasswordTextfield.setText("");
        registerEmailTextfield.setText("");
        registerConfirmPasswordTextField.setText("");
        registerUsernameTextfield.setText("");
        registerTelephoneTextField.setText("");
        loginUsernameTextfield.setText("");
        loginPasswordTextfield.setText("");


    }
    public void clearErrorLabel(){
        errorUsernameLabel.setText("");
        errorPasswordLabel.setText("");
        errorNameLabel.setText("");
        errorEmailLabel.setText("");
        errorTelephoneLabel.setText("");
        errorConfirmPasswordLabel.setText("");
        errorLoginUsernameLabel.setText("");
        errorLoginPasswordLabel.setText("");
    }
    @FXML
    public void loginAction(){
        String usernameText = loginUsernameTextfield.getText();
        String passwordText = loginPasswordTextfield.getText();
        boolean isError = false;
        if(usernameText.isEmpty()){
            errorLoginUsernameLabel.setText("Username is empty");
            isError = true;
        }
        if(passwordText.isEmpty()){
            errorLoginPasswordLabel.setText("Password is empty");
            isError = true;
        }
        boolean checkLogin = false;
        if(!isError) {
            for (User user : userList.getUsers()) {
                System.out.println(user);
                if (usernameText.equals(user.getUsername()) && user.getPassword().equals(passwordText)) {
                    errorLoginUsernameLabel.setText("Login Successful");
                    checkLogin = true;
                }
            }
            if(!checkLogin){
                errorLoginUsernameLabel.setText("Login Failed");
            }
        }
    }
}
