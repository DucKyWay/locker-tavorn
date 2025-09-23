package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.CustomButton;
import ku.cs.controllers.components.AdminNavbarController;
import ku.cs.models.account.Account;
import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.OfficerListFileDatasource;
import ku.cs.services.utils.AlertUtil;

import java.io.FileNotFoundException;
import java.io.IOException;

public class AdminManageOfficerDetailsController {

    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;
    @FXML private VBox contentVBox;
    private Label officerUsernameLabel;
    private TextField officerUsernameTextField;
    private Label officerNameLabel;
    private TextField officerNameTextField;
    private Label officerEmailLabel;
    private TextField officerEmailTextField;
    private Label officerPhoneLabel;
    private TextField officerPhoneTextField;
    private Label officerRoleLabel;
    private Label officerRoleString;
    private Label officerImagePathLabel;
    private Label officerImagePathString;
    private Button editOfficerButton;

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

    public void initDatasource() {
        datasource = new OfficerListFileDatasource("data", "test-officer-data.json");
        officers = datasource.readData();

        String officerUsername = (String) FXRouter.getData();
        officer = officers.findOfficerByUsername(officerUsername);
    }

    public void initUserInterfaces() {
        footerNavBarButton.setText("ย้อนกลับ");

        titleLabel.setText("จัดการพนักงาน");
        descriptionLabel.setText("Officer " + officer.getUsername());

        editOfficerButton = new CustomButton("บันทึก");

        contentVBox.setSpacing(10);

        LabelStyle.TITLE_LARGE.applyTo(titleLabel);
        LabelStyle.TITLE_SMALL.applyTo(descriptionLabel);

        showOfficer(officer);
    }

    public void initEvents() throws FileNotFoundException {
        footerNavBarButton.setOnAction(e -> onBackButtonClick());
        editOfficerButton.setOnAction(e -> onEditOfficerButtonClick());
    }

    private void showOfficer(Officer officer) {
        HBox usernameHBox = new HBox();
        HBox nameHBox = new HBox();
        HBox emailHBox = new HBox();
        HBox phoneHBox = new HBox();
        HBox roleHBox = new HBox();
        HBox imagePathHBox = new HBox();
        Region region = new Region();

        officerUsernameLabel = new Label("ชื่อผู้ใช้");
        officerNameLabel = new Label("ชื่อจริง");
        officerEmailLabel = new Label("อีเมล");
        officerPhoneLabel = new Label("เบอร์มือถือ");
        officerRoleLabel = new Label("ตำแหน่ง");
        officerImagePathLabel = new Label("รุปโปรไฟล์");

        officerUsernameTextField = new TextField(officer.getUsername());
        officerNameTextField = new TextField(officer.getName());
        officerEmailTextField = new TextField(officer.getEmail());
        officerPhoneTextField = new TextField(officer.getPhone());
        officerRoleString = new Label(String.valueOf(officer.getRole()));
        officerImagePathString = new Label(officer.getImagePath());

        LabelStyle.LABEL_LARGE.applyTo(officerUsernameLabel);
        LabelStyle.LABEL_LARGE.applyTo(officerNameLabel);
        LabelStyle.LABEL_LARGE.applyTo(officerEmailLabel);
        LabelStyle.LABEL_LARGE.applyTo(officerPhoneLabel);
        LabelStyle.LABEL_LARGE.applyTo(officerRoleLabel);
        LabelStyle.LABEL_LARGE.applyTo(officerImagePathLabel);

        region.setPrefSize(600, 50);

        usernameHBox.getChildren().clear();
        nameHBox.getChildren().clear();
        emailHBox.getChildren().clear();
        phoneHBox.getChildren().clear();
        roleHBox.getChildren().clear();
        imagePathHBox.getChildren().clear();
        contentVBox.getChildren().clear();

        usernameHBox.getChildren().addAll(officerUsernameLabel, officerUsernameTextField);
        nameHBox.getChildren().addAll(officerNameLabel, officerNameTextField);
        emailHBox.getChildren().addAll(officerEmailLabel, officerEmailTextField);
        phoneHBox.getChildren().addAll(officerPhoneLabel, officerPhoneTextField);
        roleHBox.getChildren().addAll(officerRoleLabel, officerRoleString);
        imagePathHBox.getChildren().addAll(officerImagePathLabel, officerImagePathString);
        contentVBox.getChildren().addAll(usernameHBox, nameHBox, emailHBox, phoneHBox, roleHBox, imagePathHBox, region, editOfficerButton);
    }

    protected void onEditOfficerButtonClick() {
        String username = officerUsernameTextField.getText();
        String name = officerNameTextField.getText();
        String email = officerEmailTextField.getText();
        String phone = officerPhoneTextField.getText();

        boolean hasError = false;
        String error = "";

        if(username.isEmpty()){
            username = officer.getUsername();
        }

        if (name.isEmpty()) {
            name = officer.getName();
        }

        if (email.isEmpty()) {
            email = officer.getEmail();
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            error = error + "รูปแบบอีเมลไม่ถูกต้อง\n";
            hasError = true;
        }

        if (phone.isEmpty()) {
            phone = officer.getPhone();
        } else if (!phone.matches("^\\d+$")) {
            error =  error + "เบอร์มือถือไม่ถูกต้อง ต้องมี 9-10 หลัก และเป็นตัวเลขเท่านั้น";
            hasError = true;
        }

        if(!hasError){
            String finalUsername = username;
            String finalName = name;
            String finalEmail = email;
            String finalPhone = phone;

            AlertUtil.confirm("Confirmation", "Do you want to change " + officer.getUsername() + " details?")
                    .ifPresent(btn -> {
                        if (btn == ButtonType.OK) {
                            officer.setUsername(finalUsername);
                            officer.setName(finalName);
                            officer.setEmail(finalEmail);
                            officer.setPhone(finalPhone);
                            datasource.writeData(officers);
                            showOfficer(officer);
                            AlertUtil.info("Success", "เปลี่ยนข้อมูล" + officer.getUsername() + "สำเร็จ");
                        }
                    });
        } else {
            AlertUtil.error("Error", error);
        }
    }

    protected void onBackButtonClick() {
        try {
            FXRouter.goTo("admin-manage-officers", current);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
