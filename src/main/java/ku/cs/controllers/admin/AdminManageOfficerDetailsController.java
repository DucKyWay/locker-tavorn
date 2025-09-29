package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.CustomButton;
import ku.cs.controllers.components.AdminNavbarController;
import ku.cs.models.account.Account;
import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.AppContext;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.OfficerListFileDatasource;
import ku.cs.services.datasources.ZoneListFileDatasource;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.ImageUploadUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdminManageOfficerDetailsController {
    private final SessionManager sessionManager = AppContext.getSessionManager();
    private final AlertUtil alertUtil = new AlertUtil();

    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;
    @FXML private VBox contentVBox;

    @FXML private GridPane formGridPane;
    @FXML private ImageView profileImageView;
    @FXML private Label chooseFileLabel;
    @FXML private Button chooseFileButton;

    private TextField officerUsernameTextField;
    private TextField officerFirstnameTextField;
    private TextField officerLastnameTextField;
    private TextField officerEmailTextField;
    private TextField officerPhoneTextField;

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
        sessionManager.requireAdminLogin();
        current = sessionManager.getCurrentAccount();

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

    public void initEvents() {
        footerNavBarButton.setOnAction(e -> onBackButtonClick());
        editOfficerButton.setOnAction(e -> onEditOfficerButtonClick());
        chooseFileButton.setOnAction(e -> onChooseFileClick());
    }

    private void showOfficer(Officer officer) {
        formGridPane.getChildren().clear();
        formGridPane.getColumnConstraints().clear();

        ColumnConstraints col0 = new ColumnConstraints();
        col0.setMinWidth(120);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setMinWidth(250);
        col1.setPrefWidth(250);

        formGridPane.getColumnConstraints().addAll(col0, col1);

        Label officerUsernameLabel = new Label("ชื่อผู้ใช้ ");
        LabelStyle.LABEL_LARGE.applyTo(officerUsernameLabel);
        officerUsernameTextField = new TextField(officer.getUsername());

        Label officerFirstnameLabel = new Label("ชื่อจริง ");
        LabelStyle.LABEL_LARGE.applyTo(officerFirstnameLabel);
        officerFirstnameTextField = new TextField(officer.getFirstname());

        Label officerLastnameLabel = new Label("นามสกุล ");
        LabelStyle.LABEL_LARGE.applyTo(officerLastnameLabel);
        officerLastnameTextField = new TextField(officer.getLastname());

        Label officerEmailLabel = new Label("อีเมล ");
        LabelStyle.LABEL_LARGE.applyTo(officerEmailLabel);
        officerEmailTextField = new TextField(officer.getEmail());

        Label officerPhoneLabel = new Label("เบอร์มือถือ ");
        LabelStyle.LABEL_LARGE.applyTo(officerPhoneLabel);
        officerPhoneTextField = new TextField(officer.getPhone());

        Label officerRoleLabel = new Label("ตำแหน่ง ");
        LabelStyle.LABEL_LARGE.applyTo(officerRoleLabel);
        Label officerRoleString = new Label(String.valueOf(officer.getRole()));

        Label zoneLabel = new Label("พื้นที่รับผิดชอบ");
        LabelStyle.LABEL_LARGE.applyTo(zoneLabel);
        FlowPane zoneFlowPane = new FlowPane(10, 5);

        for (Zone zone : zones.getZones()) {
            CheckBox cb = new CheckBox(zone.getZoneName());
            cb.setSelected(officer.getZoneUids().contains(zone.getZoneUid()));
            cb.setUserData(zone.getZoneUid());
            cb.setStyle("-fx-font-size: 14");
            zoneFlowPane.getChildren().add(cb);
            zoneCheckBoxes.add(cb);
        }

        int row = 0;
        formGridPane.add(officerUsernameLabel, 0, row);
        formGridPane.add(officerUsernameTextField, 1, row++);

        formGridPane.add(officerFirstnameLabel, 0, row);
        formGridPane.add(officerFirstnameTextField, 1, row++);

        formGridPane.add(officerLastnameLabel, 0, row);
        formGridPane.add(officerLastnameTextField, 1, row++);

        formGridPane.add(officerEmailLabel, 0, row);
        formGridPane.add(officerEmailTextField, 1, row++);

        formGridPane.add(officerPhoneLabel, 0, row);
        formGridPane.add(officerPhoneTextField, 1, row++);

        formGridPane.add(officerRoleLabel, 0, row);
        formGridPane.add(officerRoleString, 1, row++);

        formGridPane.add(zoneLabel, 0, row);
        formGridPane.add(zoneFlowPane, 1, row);

        GridPane.setValignment(zoneLabel, javafx.geometry.VPos.TOP);
        GridPane.setHalignment(zoneLabel, javafx.geometry.HPos.LEFT);

        if (officer.getImagePath() != null && !officer.getImagePath().isBlank()) {
            profileImageView.setImage(new Image("file:" + officer.getImagePath()));
        } else {
            profileImageView.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource("/ku/cs/images/default_profile.png")).toExternalForm()
            ));
        }


        contentVBox.getChildren().remove(editOfficerButton);
        contentVBox.getChildren().add(editOfficerButton);
    }

    private void onChooseFileClick() {
        try {
            Path destDir = Paths.get("images", "profiles");

            // File chooser
            ImageUploadUtil.PickResult res = ImageUploadUtil.pickAndSaveImage(
                    chooseFileButton.getScene().getWindow(),
                    destDir,
                    officer.getUsername(),
                    30 * 1024 * 1024 // 30 MB
            );

            if (res == null) return; // cancel

            // new preview
            try (FileInputStream in = new FileInputStream(res.savedPath().toFile())) {
                profileImageView.setImage(new Image(in));
                chooseFileLabel.setText(res.savedPath().toString());
            }

            officer.setImagePath(res.savedPath().toString());
            officersDatasource.writeData(officers);
            alertUtil.info("Success", "เปลี่ยนรูปภาพสำเร็จ");

        } catch (IOException ex) {
            ex.printStackTrace();
            alertUtil.error("เกิดข้อผิดพลาด", "ไม่สามารถอัปโหลดรูปภาพได้: " + ex.getMessage());
        }
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

            List<String> selectedZoneUids = new ArrayList<>();
            for (CheckBox cb : zoneCheckBoxes) {
                if (cb.isSelected()) {
                    selectedZoneUids.add((String) cb.getUserData());
                }
            }

            alertUtil.confirm("Confirmation", "Do you want to change " + officer.getUsername() + " details?")
                .ifPresent(btn -> {
                    if (btn == ButtonType.OK) {
                        officer.setUsername(finalUsername);
                        officer.setFirstname(finalFirstname);
                        officer.setLastname(finalLastname);
                        officer.setEmail(finalEmail);
                        officer.setPhone(finalPhone);

                        officer.setZoneUids(selectedZoneUids);

                        officersDatasource.writeData(officers);
                        showOfficer(officer);
                        alertUtil.info("Success", "เปลี่ยนข้อมูล " + officer.getUsername() + " สำเร็จ");
                        try {
                            FXRouter.goTo("admin-manage-officers");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
        } else {
            alertUtil.error("Error", error);
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
