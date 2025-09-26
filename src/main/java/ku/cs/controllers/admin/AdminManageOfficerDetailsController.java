package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.CustomButton;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminManageOfficerDetailsController {

    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;
    @FXML private VBox contentVBox;
    private Label zoneLabel;
    private VBox zoneCheckboxVBox;
    private Label officerUsernameLabel;
    private TextField officerUsernameTextField;
    private Label officerFirstnameLabel;
    private TextField officerFirstnameTextField;
    private Label officerLastnameLabel;
    private TextField officerLastnameTextField;
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
    private List<CheckBox> zoneCheckBoxes = new ArrayList<>();

    private OfficerList officers;
    private Datasource<OfficerList> officersDatasource;
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

    public void initDatasource() {
        officersDatasource = new OfficerListFileDatasource("data", "test-officer-data.json");
        officers = officersDatasource.readData();
        zonesDatasource = new ZoneListFileDatasource("data", "test-zone-data.json");
        zones = zonesDatasource.readData();

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
        HBox firstnameHBox = new HBox();
        HBox lastnameHBox = new HBox();
        HBox emailHBox = new HBox();
        HBox phoneHBox = new HBox();
        HBox roleHBox = new HBox();
        HBox imagePathHBox = new HBox();
        Region region = new Region();
        zoneCheckboxVBox = new VBox();

        officerUsernameLabel = new Label("ชื่อผู้ใช้ ");
        officerFirstnameLabel = new Label("ชื่อจริง ");
        officerLastnameLabel = new Label("นามสกุล ");
        officerEmailLabel = new Label("อีเมล ");
        officerPhoneLabel = new Label("เบอร์มือถือ ");
        zoneLabel = new Label("พื้นที่ที่รับผิดชอบ ");
        officerRoleLabel = new Label("ตำแหน่ง ");
        officerImagePathLabel = new Label("รุปโปรไฟล์ ");

        officerUsernameTextField = new TextField(officer.getUsername());
        officerFirstnameTextField = new TextField(officer.getFirstname());
        officerLastnameTextField = new TextField(officer.getLastname());
        officerEmailTextField = new TextField(officer.getEmail());
        officerPhoneTextField = new TextField(officer.getPhone());
        officerRoleString = new Label(String.valueOf(officer.getRole()));
        officerImagePathString = new Label(officer.getImagePath());

        LabelStyle.LABEL_LARGE.applyTo(officerUsernameLabel);
        LabelStyle.LABEL_LARGE.applyTo(officerFirstnameLabel);
        LabelStyle.LABEL_LARGE.applyTo(officerLastnameLabel);
        LabelStyle.LABEL_LARGE.applyTo(officerEmailLabel);
        LabelStyle.LABEL_LARGE.applyTo(officerPhoneLabel);
        LabelStyle.LABEL_LARGE.applyTo(zoneLabel);
        LabelStyle.LABEL_LARGE.applyTo(officerRoleLabel);
        LabelStyle.LABEL_LARGE.applyTo(officerImagePathLabel);

        zoneCheckboxVBox.setSpacing(5);
        region.setPrefSize(600, 50);

        usernameHBox.getChildren().clear();
        firstnameHBox.getChildren().clear();
        lastnameHBox.getChildren().clear();
        emailHBox.getChildren().clear();
        phoneHBox.getChildren().clear();
        roleHBox.getChildren().clear();
        imagePathHBox.getChildren().clear();
        contentVBox.getChildren().clear();

        usernameHBox.getChildren().addAll(officerUsernameLabel, officerUsernameTextField);
        firstnameHBox.getChildren().addAll(officerFirstnameLabel, officerFirstnameTextField);
        lastnameHBox.getChildren().addAll(officerLastnameLabel, officerLastnameTextField);
        emailHBox.getChildren().addAll(officerEmailLabel, officerEmailTextField);
        phoneHBox.getChildren().addAll(officerPhoneLabel, officerPhoneTextField);
        roleHBox.getChildren().addAll(officerRoleLabel, officerRoleString);
        imagePathHBox.getChildren().addAll(officerImagePathLabel, officerImagePathString);
        contentVBox.getChildren().addAll(usernameHBox, firstnameHBox, lastnameHBox, emailHBox, phoneHBox, zoneLabel, zoneCheckboxVBox, roleHBox, imagePathHBox, region, editOfficerButton);

        loadZoneCheckboxes(officer);
    }

    protected void onEditOfficerButtonClick() {
        String username = officerUsernameTextField.getText();
        String firstname = officerFirstnameTextField.getText();
        String lastname = officerLastnameTextField.getText();
        String email = officerEmailTextField.getText();
        String phone = officerPhoneTextField.getText();

        boolean hasError = false;
        String error = "";

        if(username.isEmpty()){
            username = officer.getUsername();
        }

        if (firstname.isEmpty()) {
            firstname = officer.getFirstname();
        }

        if (lastname.isEmpty()) {
            lastname = officer.getLastname();
        }

        if (email.isEmpty()) {
            email = officer.getEmail();
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            error += "รูปแบบอีเมลไม่ถูกต้อง\n";
            hasError = true;
        }

        if (phone.isEmpty()) {
            phone = officer.getPhone();
        } else if (!phone.matches("^\\d+$")) {
            error += "เบอร์มือถือไม่ถูกต้อง ต้องมี 9-10 หลัก และเป็นตัวเลขเท่านั้น";
            hasError = true;
        }

        if(!hasError && officer.isStatus()){
            String finalUsername = username;
            String finalFirstname = firstname;
            String finalLastname = lastname;
            String finalEmail = email;
            String finalPhone = phone;

            AlertUtil.confirm("Confirmation", "Do you want to change " + officer.getUsername() + " details?")
                    .ifPresent(btn -> {
                        if (btn == ButtonType.OK) {
                            officer.setUsername(finalUsername);
                            officer.setFirstname(finalFirstname);
                            officer.setLastname(finalLastname);
                            officer.setEmail(finalEmail);
                            officer.setPhone(finalPhone);

                            List<String> selectedUids = new ArrayList<>();
                            for (Node node : zoneCheckboxVBox.getChildren()) {
                                if (node instanceof CheckBox cb && cb.isSelected()) {
                                    selectedUids.add((String) cb.getUserData());
                                }
                            }
                            officer.setZoneUids(selectedUids);

                            officersDatasource.writeData(officers);
                            showOfficer(officer);
                            AlertUtil.info("Success", "เปลี่ยนข้อมูล " + officer.getUsername() + " สำเร็จ");
                            try {
                                FXRouter.goTo("admin-manage-officers");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
        } else {
            AlertUtil.error("Error", error);
        }
    }

    private void loadZoneCheckboxes(Officer officer) {
        zoneCheckboxVBox.getChildren().clear();
        for (Zone zone : zones.getZones()) {
            CheckBox checkBox = new CheckBox(zone.getZone());
            checkBox.setSelected(officer.getZoneUids().contains(zone.getZoneUid()));
            checkBox.setUserData(zone.getZoneUid());
            zoneCheckBoxes.add(checkBox);
            zoneCheckboxVBox.getChildren().add(checkBox);
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