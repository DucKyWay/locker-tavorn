package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.FilledButton;
import ku.cs.controllers.components.AdminNavbarController;
import ku.cs.models.account.Account;
import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.OfficerListFileDatasource;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.PasswordUtil;
import ku.cs.services.utils.UuidUtil;

import java.io.FileNotFoundException;
import java.io.IOException;

public class AdminManageNewOfficerController {

    @FXML private VBox parentOfficerVBox;
    private HBox usernameHBox;
    @FXML private Label officerUsernameLabel;
    @FXML private TextField officerUsernameTextField;
    private HBox nameHBox;
    @FXML private Label officerNameLabel;
    @FXML private TextField officerNameTextField;
    private HBox passwordHBox;
    @FXML private Label officerPasswordLabel;
    @FXML private TextField officerPasswordTextField;
    private HBox zoneHBox;
    @FXML private Label officerZoneLabel;
    @FXML private TextField officerZoneTextField;
    private HBox emailHBox;
    @FXML private Label officerEmailLabel;
    @FXML private TextField officerEmailTextField;
    private HBox phoneHBox;
    @FXML private Label officerPhoneLabel;
    @FXML private TextField officerPhoneTextField;
    private HBox buttonHBox;
    @FXML private Button addNewOfficerFilledButton;

    @FXML private VBox errorAddNewOfficerVBox;

    @FXML private AdminNavbarController adminNavbarController;
    private Button footerNavBarButton;

    private OfficerList officers;
    private Datasource<OfficerList> datasource;

    private Account current;
    private Officer officer;

    public void initialize() throws FileNotFoundException {
        SessionManager.requireAdminLogin();
        current = SessionManager.getCurrentAccount();

        footerNavBarButton = adminNavbarController.getFooterNavButton();

        initDatasource();
        initUserInterfaces();
        initEvents();
    }

    public void initDatasource() throws FileNotFoundException {
        datasource = new OfficerListFileDatasource("data", "test-officer-data.json");
        officers = datasource.readData();
    }

    public void initUserInterfaces(){
        Region region = new Region();

        region.setPrefSize(850, 50);

        footerNavBarButton.setText("ย้อนกลับ");

        usernameHBox = new HBox();
        nameHBox = new HBox();
        passwordHBox = new HBox();
        zoneHBox = new HBox();
        emailHBox = new HBox();
        phoneHBox = new HBox();
        buttonHBox = new HBox();

        usernameHBox.setSpacing(4);
        nameHBox.setSpacing(4);
        passwordHBox.setSpacing(4);
        zoneHBox.setSpacing(4);
        emailHBox.setSpacing(4);
        phoneHBox.setSpacing(4);
        buttonHBox.setSpacing(4);

        usernameHBox.setAlignment(Pos.TOP_CENTER);
        nameHBox.setAlignment(Pos.TOP_CENTER);
        passwordHBox.setAlignment(Pos.TOP_CENTER);
        zoneHBox.setAlignment(Pos.TOP_CENTER);
        emailHBox.setAlignment(Pos.TOP_CENTER);
        phoneHBox.setAlignment(Pos.TOP_CENTER);
        buttonHBox.setAlignment(Pos.TOP_CENTER);

        usernameHBox.setPadding(new Insets(10, 0, 10, 0));
        nameHBox.setPadding(new Insets(10, 0, 10, 0));
        passwordHBox.setPadding(new Insets(10, 0, 10, 0));
        zoneHBox.setPadding(new Insets(10, 0, 10, 0));
        emailHBox.setPadding(new Insets(10, 0, 10, 0));
        phoneHBox.setPadding(new Insets(10, 0, 10, 0));
        buttonHBox.setPadding(new Insets(50, 0, 10, 0));

        officerUsernameLabel = new Label("ชื่อผู้ใช้");
        officerUsernameTextField = new TextField();

        officerNameLabel = new Label("ชื่อพนักงาน");
        officerNameTextField = new TextField();

        officerPasswordLabel = new Label("รหัสผ่าน");
        officerPasswordTextField = new TextField();
        officerPasswordTextField.setText(UuidUtil.generateShort());
        officerPasswordTextField.setDisable(true);

        officerZoneLabel = new Label("โซนที่อยู่");
        officerZoneTextField = new TextField(); // TODO: Dropdown Select Zone

        officerEmailLabel = new Label("อีเมล");
        officerEmailTextField = new TextField();

        officerPhoneLabel = new Label("เบอร์มือถือ");
        officerPhoneTextField = new TextField();

        addNewOfficerFilledButton = new FilledButton("เพิ่มพนักงานใหม่");

        LabelStyle.LABEL_MEDIUM.applyTo(officerUsernameLabel);
        LabelStyle.LABEL_MEDIUM.applyTo(officerNameLabel);
        LabelStyle.LABEL_MEDIUM.applyTo(officerPasswordLabel);
        LabelStyle.LABEL_MEDIUM.applyTo(officerZoneLabel);
        LabelStyle.LABEL_MEDIUM.applyTo(officerEmailLabel);
        LabelStyle.LABEL_MEDIUM.applyTo(officerPhoneLabel);

        usernameHBox.getChildren().addAll(officerUsernameLabel, officerUsernameTextField);
        nameHBox.getChildren().addAll(officerNameLabel, officerNameTextField);
        passwordHBox.getChildren().addAll(officerPasswordLabel, officerPasswordTextField);
        zoneHBox.getChildren().addAll(officerZoneLabel, officerZoneTextField);
        emailHBox.getChildren().addAll(officerEmailLabel, officerEmailTextField);
        phoneHBox.getChildren().addAll(officerPhoneLabel, officerPhoneTextField);
        buttonHBox.getChildren().addAll(addNewOfficerFilledButton);

        parentOfficerVBox.getChildren().addAll(usernameHBox, nameHBox, passwordHBox, zoneHBox, emailHBox, phoneHBox, buttonHBox);
    }

    public void initEvents() {
        addNewOfficerFilledButton.setOnAction(e -> addNewOfficerHandler());
        footerNavBarButton.setOnAction(e -> onBackButtonClick());
    }

    protected void addNewOfficerHandler() {

        errorAddNewOfficerVBox.getChildren().clear();

        String username = officerUsernameTextField.getText().trim();
        String name = officerNameTextField.getText().trim();
        String password = officerPasswordTextField.getText().trim();
        String zone = officerZoneTextField.getText().trim();
        String email = officerEmailTextField.getText().trim();
        String phone = officerPhoneTextField.getText().trim();

        boolean hasError = false;

        if(username.isEmpty()){
            showError("กรุณากรอกชื่อผู้ใช้");
            hasError = true;
        }

        if (name.isEmpty()) {
            showError("กรุณากรอกชื่อพนักงาน");
            hasError = true;
        }

        if (password.isEmpty()) {
            showError("กรุณากรอกรหัสผ่าน");
            hasError = true;
        } else if (password.length() < 4) {
            showError("รหัสผ่านต้องมีอย่างน้อย 4 ตัวอักษร");
            hasError = true;
        }

        if (zone.isEmpty()) {
            showError("กรุณากรอกโซนที่อยู่");
            hasError = true;
        }

        if (email.isEmpty()) {
            showError("กรุณากรอกอีเมล");
            hasError = true;
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showError("รูปแบบอีเมลไม่ถูกต้อง");
            hasError = true;
        }

        if (phone.isEmpty()) {
            showError("กรุณากรอกเบอร์มือถือ");
            hasError = true;
        } else if (!phone.matches("^\\d+$")) {
            showError("เบอร์มือถือไม่ถูกต้อง ต้องมี 9-10 หลัก และเป็นตัวเลขเท่านั้น");
            hasError = true;
        }

        if (hasError) return;

        String hashPassword = PasswordUtil.hashPassword(password);
        int zoneId = Integer.parseInt(zone);

        officers.addOfficer(username, name, hashPassword, password, zoneId, email, phone);
        datasource.writeData(officers);

        String officerUsername = Officer.createUsername(zoneId, username);
        officer = officers.findOfficerByUsername(officerUsername);
        AlertUtil.info("สร้างพนักงานใหม่สำเร็จ", "ชื่อผู้ใช้ " + officerUsername + "\nรหัสผ่าน " + password);
        try {
            FXRouter.goTo("admin-manage-officers");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void onBackButtonClick() {
        try {
            FXRouter.goTo("admin-manage-officers");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showError(String message) {
        Label errorLabel = new Label(message);
        errorLabel.setStyle("-fx-text-fill: red;");
        LabelStyle.LABEL_MEDIUM.applyTo(errorLabel);
        errorAddNewOfficerVBox.getChildren().add(errorLabel);
    }
}
