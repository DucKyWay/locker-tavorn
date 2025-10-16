package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import ku.cs.components.Icons;
import ku.cs.components.button.CustomButton;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerForm;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.accounts.AccountService;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.accounts.OfficerService;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.ImageUploadUtil;
import ku.cs.services.utils.AccountValidator;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class AdminManageOfficerDetailsController extends BaseAdminController {
    private final OfficerService officerService = new OfficerService();
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final ImageUploadUtil imageUploadUtil = new ImageUploadUtil();

    @FXML private VBox contentVBox;
    @FXML private ImageView profileImageView;
    @FXML private Label chooseFileLabel;
    @FXML private Button chooseFileButton;
    @FXML private Button backButton;

    @FXML private Label officerDisplayUsernameLabel;
    @FXML private TextField officerFirstnameTextField;
    @FXML private TextField officerLastnameTextField;
    @FXML private TextField officerEmailTextField;
    @FXML private TextField officerPhoneTextField;
    @FXML private FlowPane zoneFlowPane;
    @FXML private Label officerRoleLabel;

    @FXML private Button editOfficerButton;

    private List<CheckBox> zoneCheckBoxes = new ArrayList<>();
    private ZoneList zones;
    private Officer officer;

    @Override
    protected void initDatasource() {
        zones = zonesProvider.loadCollection();
        String officerUsername = (String) FXRouter.getData();
        officer = officerService.findByUsername(officerUsername);
    }

    @Override
    protected void initUserInterfaces() {
        ElevatedButtonWithIcon.SMALL.mask(backButton, Icons.ARROW_LEFT);

        editOfficerButton = new CustomButton("บันทึก");
        contentVBox.setSpacing(10);

        showOfficer(officer);
    }

    @Override
    protected void initEvents() {
        backButton.setOnAction(e -> onBackButtonClick());
        editOfficerButton.setOnAction(e -> onEditOfficerButtonClick());
        chooseFileButton.setOnAction(e -> onChooseFileClick());
    }

    private void showOfficer(Officer officer) {
        officerDisplayUsernameLabel.setText(officer.getUsername());
        officerFirstnameTextField.setText(officer.getFirstname());
        officerLastnameTextField.setText(officer.getLastname());
        officerEmailTextField.setText(officer.getEmail());
        officerPhoneTextField.setText(officer.getPhone());
        officerRoleLabel.setText(officer.getRole().getDescription());

        zoneCheckBoxes.clear();
        for (Zone zone : zones.getZones()) {
            CheckBox cb = new CheckBox(zone.getZoneName());
            cb.setSelected(officer.getZoneUids().contains(zone.getZoneUid()));
            cb.setUserData(zone.getZoneUid());
            cb.setStyle("-fx-font-size: 14");
            zoneFlowPane.getChildren().add(cb);
            zoneCheckBoxes.add(cb);
        }

        if (officer.getImagePath() != null && !officer.getImagePath().isBlank()) {
            profileImageView.setImage(new Image("file:" + Paths.get(officer.getImagePath()).toAbsolutePath()));
        } else {
            profileImageView.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource("/ku/cs/images/default_profile.png")).toExternalForm()
            ));
        }

        if (!contentVBox.getChildren().contains(editOfficerButton)) {
            contentVBox.getChildren().add(editOfficerButton);
        }
    }

    private void onChooseFileClick() {
        try {
            Path destDir = Paths.get("images", "profiles");

            ImageUploadUtil.PickResult res = imageUploadUtil.pickAndSaveImage(
                    chooseFileButton.getScene().getWindow(),
                    destDir,
                    officer.getUsername(),
                    30 * 1024 * 1024
            );

            if (res == null) return;

            try (FileInputStream in = new FileInputStream(res.savedPath().toFile())) {
                profileImageView.setImage(new Image(in));
                chooseFileLabel.setText(res.savedPath().toString());
            }

            AccountService accountService = new AccountService(officer);
            accountService.updateProfileImage(res.savedPath().getFileName().toString());

            officer.setImagePath("images/profiles/" + res.savedPath().getFileName().toString());

            new AlertUtil().info("Success", "เปลี่ยนรูปภาพสำเร็จ");

        } catch (IOException ex) {
            new AlertUtil().error("เกิดข้อผิดพลาด", "ไม่สามารถอัปโหลดรูปภาพได้: " + ex.getMessage());
        }
    }

    private void onEditOfficerButtonClick() {
        List<String> selectedZoneUids = new ArrayList<>();
        for (CheckBox cb : zoneCheckBoxes) {
            if (cb.isSelected()) {
                selectedZoneUids.add((String) cb.getUserData());
            }
        }

        OfficerForm form = new OfficerForm(
                officer.getUsername(),
                officerFirstnameTextField.getText().isBlank() ? officer.getFirstname() : officerFirstnameTextField.getText(),
                officerLastnameTextField.getText().isBlank() ? officer.getLastname() : officerLastnameTextField.getText(),
                null,
                officerEmailTextField.getText().isBlank() ? officer.getEmail() : officerEmailTextField.getText(),
                officerPhoneTextField.getText().isBlank() ? officer.getPhone() : officerPhoneTextField.getText(),
                selectedZoneUids
        );

        var errors = new AccountValidator().validateEditOfficer(form, officer);
        if (!errors.isEmpty()) {
            new AlertUtil().error("Error", String.join("\n", errors));
            return;
        }

        new AlertUtil().confirm("Confirmation", "Do you want to change " + officer.getUsername() + " details?")
                .ifPresent(btn -> {
                    if (btn == ButtonType.OK) {
                        officerService.updateOfficer(officer, form);
                        new AlertUtil().info("Success", "เปลี่ยนข้อมูล " + officer.getUsername() + " สำเร็จ");
                        try {
                            FXRouter.goTo("admin-manage-officers");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    private void onBackButtonClick() {
        try {
            FXRouter.goTo("admin-manage-officers");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
