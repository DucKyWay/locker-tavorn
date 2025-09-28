package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.FilledButton;
import ku.cs.controllers.components.AdminNavbarController;
import ku.cs.models.account.Account;
import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.OfficerListFileDatasource;
import ku.cs.services.datasources.ZoneListFileDatasource;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.PasswordUtil;
import ku.cs.services.utils.UuidUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminManageNewOfficerController {

    @FXML private VBox headingVBox;
    @FXML private VBox parentOfficerVBox;
    private HBox usernameHBox;
    private Label officerUsernameLabel;
    private TextField officerUsernameTextField;
    private HBox firstnameHBox;
    private Label officerFirstnameLabel;
    private TextField officerFirstnameTextField;
    private HBox lastnameHBox;
    private Label officerLastnameLabel;
    private TextField officerLastnameTextField;
    private HBox passwordHBox;
    private Label officerPasswordLabel;
    private TextField officerPasswordTextField;
    private HBox emailHBox;
    private Label officerEmailLabel;
    private TextField officerEmailTextField;
    private HBox phoneHBox;
    private Label officerPhoneLabel;
    private TextField officerPhoneTextField;
    private FlowPane zoneCheckboxFlowPane;
    private Label officerZoneLabel;
    private HBox buttonHBox;

    private List<CheckBox> zoneCheckBoxes = new ArrayList<>();

    @FXML private Button addNewOfficerFilledButton;

    @FXML private VBox errorAddNewOfficerVBox;

    @FXML private AdminNavbarController adminNavbarController;
    private Button footerNavBarButton;

    private OfficerList officers;
    private Datasource<OfficerList> datasource;
    private ZoneList zones;
    private Datasource<ZoneList> zonesDatasource;

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

        zonesDatasource = new ZoneListFileDatasource("data", "test-zone-data.json");
        zones = zonesDatasource.readData();
    }

    public void initUserInterfaces(){
        Label headerLabel = new Label("เพิ่มพนักงานใหม่");
        Label descriptionLabel = new Label("กรุณากรอกข้อมูลให้ครบ");
        Region region = new Region();

        region.setPrefSize(850, 50);

        footerNavBarButton.setText("ย้อนกลับ");

        LabelStyle.TITLE_LARGE.applyTo(headerLabel);
        LabelStyle.TITLE_SMALL.applyTo(descriptionLabel);

        usernameHBox = new HBox();
        firstnameHBox = new HBox();
        lastnameHBox = new HBox();
        passwordHBox = new HBox();
        emailHBox = new HBox();
        phoneHBox = new HBox();
        zoneCheckboxFlowPane = new FlowPane();
        buttonHBox = new HBox();

        usernameHBox.setSpacing(4);
        firstnameHBox.setSpacing(4);
        lastnameHBox.setSpacing(4);
        passwordHBox.setSpacing(4);
        emailHBox.setSpacing(4);
        phoneHBox.setSpacing(4);
        zoneCheckboxFlowPane.setHgap(10);
        zoneCheckboxFlowPane.setVgap(10);
        buttonHBox.setSpacing(4);

        usernameHBox.setAlignment(Pos.TOP_CENTER);
        firstnameHBox.setAlignment(Pos.TOP_CENTER);
        lastnameHBox.setAlignment(Pos.TOP_CENTER);
        passwordHBox.setAlignment(Pos.TOP_CENTER);
        emailHBox.setAlignment(Pos.TOP_CENTER);
        phoneHBox.setAlignment(Pos.TOP_CENTER);
        zoneCheckboxFlowPane.setAlignment(Pos.TOP_CENTER);
        buttonHBox.setAlignment(Pos.TOP_CENTER);

        usernameHBox.setPadding(new Insets(10, 0, 10, 0));
        firstnameHBox.setPadding(new Insets(10, 0, 10, 0));
        lastnameHBox.setPadding(new Insets(10, 0, 10, 0));
        passwordHBox.setPadding(new Insets(10, 0, 10, 0));
        emailHBox.setPadding(new Insets(10, 0, 10, 0));
        phoneHBox.setPadding(new Insets(10, 0, 10, 0));
        zoneCheckboxFlowPane.setPadding(new Insets(10, 20, 10, 20));
        buttonHBox.setPadding(new Insets(50, 0, 10, 0));


        officerUsernameLabel = new Label("ชื่อผู้ใช้");
        officerUsernameTextField = new TextField();

        officerFirstnameLabel = new Label("ชื่อจริงพนักงาน");
        officerFirstnameTextField = new TextField();

        officerLastnameLabel = new Label("นามสกุลพนักงาน");
        officerLastnameTextField = new TextField();

        officerPasswordLabel = new Label("รหัสผ่าน");
        officerPasswordTextField = new TextField();
        officerPasswordTextField.setText(UuidUtil.generateShort());
        officerPasswordTextField.setDisable(true);

        officerZoneLabel = new Label("พื้นที่ที่รับผิดชอบ");

        officerEmailLabel = new Label("อีเมล");
        officerEmailTextField = new TextField();

        officerPhoneLabel = new Label("เบอร์มือถือ");
        officerPhoneTextField = new TextField();

        officerZoneLabel = new Label("พื้นที่รับผิดชอบ");

        addNewOfficerFilledButton = new FilledButton("เพิ่มพนักงานใหม่");

        LabelStyle.LABEL_MEDIUM.applyTo(officerUsernameLabel);
        LabelStyle.LABEL_MEDIUM.applyTo(officerFirstnameLabel);
        LabelStyle.LABEL_MEDIUM.applyTo(officerLastnameLabel);
        LabelStyle.LABEL_MEDIUM.applyTo(officerPasswordLabel);
        LabelStyle.LABEL_MEDIUM.applyTo(officerEmailLabel);
        LabelStyle.LABEL_MEDIUM.applyTo(officerPhoneLabel);
        LabelStyle.LABEL_MEDIUM.applyTo(officerZoneLabel);

        loadZoneCheckboxes();

        usernameHBox.getChildren().addAll(officerUsernameLabel, officerUsernameTextField);
        firstnameHBox.getChildren().addAll(officerFirstnameLabel, officerFirstnameTextField);
        lastnameHBox.getChildren().addAll(officerLastnameLabel, officerLastnameTextField);
        passwordHBox.getChildren().addAll(officerPasswordLabel, officerPasswordTextField);
        emailHBox.getChildren().addAll(officerEmailLabel, officerEmailTextField);
        phoneHBox.getChildren().addAll(officerPhoneLabel, officerPhoneTextField);
        buttonHBox.getChildren().addAll(addNewOfficerFilledButton);

        headingVBox.getChildren().addAll(headerLabel, descriptionLabel);

        parentOfficerVBox.getChildren().addAll(
                usernameHBox, firstnameHBox, lastnameHBox, passwordHBox,
                emailHBox, phoneHBox,
                zoneCheckboxFlowPane, buttonHBox
        );
    }

    public void initEvents() {
        addNewOfficerFilledButton.setOnAction(e -> addNewOfficerHandler());
        footerNavBarButton.setOnAction(e -> onBackButtonClick());
    }

    protected void addNewOfficerHandler() {

        errorAddNewOfficerVBox.getChildren().clear();

        String username = officerUsernameTextField.getText().trim();
        String firstname = officerFirstnameTextField.getText().trim();
        String lastname = officerLastnameTextField.getText().trim();
        String password = officerPasswordTextField.getText().trim();
        String email = officerEmailTextField.getText().trim();
        String phone = officerPhoneTextField.getText().trim();

        List<String> selectedZoneUids = new ArrayList<>();
        for (CheckBox cb : zoneCheckBoxes) {
            if (cb.isSelected()) {
                selectedZoneUids.add((String) cb.getUserData());
            }
        }

        boolean hasError = false;

        if(username.isEmpty()){
            showError("กรุณากรอกชื่อผู้ใช้");
            hasError = true;
        }

        if (firstname.isEmpty()) {
            showError("กรุณากรอกชื่อพนักงาน");
            hasError = true;
        }

        if (lastname.isEmpty()) {
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

        officers.addOfficer(username, firstname, lastname, hashPassword, password, email, phone, new ArrayList<>(selectedZoneUids));
        datasource.writeData(officers);

        AlertUtil.info("สร้างพนักงานใหม่สำเร็จ", "ชื่อผู้ใช้ " + username + "\nรหัสผ่าน " + password);
        try {
            FXRouter.goTo("admin-manage-officers");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadZoneCheckboxes() {
        zoneCheckboxFlowPane.getChildren().clear();
        zoneCheckBoxes.clear();

        for (Zone zone : zones.getZones()) {
            CheckBox checkBox = new CheckBox(zone.getZone());
            checkBox.setUserData(zone.getZoneUid());
            zoneCheckBoxes.add(checkBox);
            zoneCheckboxFlowPane.getChildren().add(checkBox);
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
