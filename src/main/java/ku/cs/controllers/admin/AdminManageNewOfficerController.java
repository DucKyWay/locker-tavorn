package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.FilledButton;
import ku.cs.components.button.IconButton;
import ku.cs.controllers.components.AdminNavbarController;
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
import ku.cs.services.utils.PasswordUtil;
import ku.cs.services.utils.UuidUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminManageNewOfficerController {
    private final SessionManager sessionManager = AppContext.getSessionManager();
    private final AlertUtil alertUtil = new AlertUtil();

    @FXML private VBox headingVBox;
    @FXML private VBox parentOfficerVBox;
    private TextField officerUsernameTextField;
    private TextField officerFirstnameTextField;
    private TextField officerLastnameTextField;
    private TextField officerPasswordTextField;
    private Button copyPaaswordButton;
    private TextField officerEmailTextField;
    private TextField officerPhoneTextField;
    private FlowPane zoneCheckboxFlowPane;

    private List<CheckBox> zoneCheckBoxes = new ArrayList<>();

    @FXML private Button addNewOfficerFilledButton;

    @FXML private VBox errorAddNewOfficerVBox;

    @FXML private AdminNavbarController adminNavbarController;
    private Button footerNavBarButton;

    private OfficerList officers;
    private Datasource<OfficerList> datasource;
    private ZoneList zones;
    private Datasource<ZoneList> zonesDatasource;

    public void initialize() throws FileNotFoundException {
        sessionManager.requireAdminLogin();

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

    public void initUserInterfaces() {
        footerNavBarButton = adminNavbarController.getFooterNavButton();

        Label headerLabel = new Label("เพิ่มพนักงานใหม่");
        Label descriptionLabel = new Label("กรุณากรอกข้อมูลให้ครบ");
        Region region = new Region();
        Region zoneRegion = new Region();

        region.setPrefSize(850, 50);
        zoneRegion.setPrefSize(850, 20);

        footerNavBarButton.setText("ย้อนกลับ");

        LabelStyle.TITLE_LARGE.applyTo(headerLabel);
        LabelStyle.TITLE_SMALL.applyTo(descriptionLabel);

        GridPane formGridPane = new GridPane();
        VBox zoneVBox = new VBox();
        zoneCheckboxFlowPane = new FlowPane();
        HBox buttonHBox = new HBox();

        formGridPane.setHgap(10);
        formGridPane.setVgap(10);
        zoneCheckboxFlowPane.setHgap(10);
        zoneCheckboxFlowPane.setVgap(10);
        zoneVBox.setSpacing(10);
        buttonHBox.setSpacing(4);

        zoneVBox.setAlignment(Pos.CENTER);
        zoneCheckboxFlowPane.setAlignment(Pos.TOP_CENTER);
        buttonHBox.setAlignment(Pos.TOP_CENTER);

        formGridPane.setPadding(new Insets(10, 10, 10, 30));
        zoneVBox.setPadding(new Insets(10, 0, 10, 0));
        zoneCheckboxFlowPane.setPadding(new Insets(10, 70, 10, 70));
        buttonHBox.setPadding(new Insets(50, 0, 10, 0));


        Label officerUsernameLabel = new Label("ชื่อผู้ใช้");
        officerUsernameTextField = new TextField();

        Label officerFirstnameLabel = new Label("ชื่อจริงพนักงาน");
        officerFirstnameTextField = new TextField();

        Label officerLastnameLabel = new Label("นามสกุลพนักงาน");
        officerLastnameTextField = new TextField();

        Label officerPasswordLabel = new Label("รหัสผ่าน");
        officerPasswordTextField = new TextField();
        officerPasswordTextField.setText(UuidUtil.generateShort());
        officerPasswordTextField.setDisable(true);
        copyPaaswordButton = new IconButton(new Icon(Icons.COPY));

        Label officerEmailLabel = new Label("อีเมล");
        officerEmailTextField = new TextField();

        Label officerPhoneLabel = new Label("เบอร์มือถือ");
        officerPhoneTextField = new TextField();

        Label officerZoneLabel = new Label("พื้นที่รับผิดชอบ");
        LabelStyle.BODY_LARGE.applyTo(officerZoneLabel);

        addNewOfficerFilledButton = new FilledButton("เพิ่มพนักงานใหม่");

        LabelStyle.LABEL_LARGE.applyTo(officerUsernameLabel);
        LabelStyle.LABEL_LARGE.applyTo(officerFirstnameLabel);
        LabelStyle.LABEL_LARGE.applyTo(officerLastnameLabel);
        LabelStyle.LABEL_LARGE.applyTo(officerPasswordLabel);
        LabelStyle.LABEL_LARGE.applyTo(officerEmailLabel);
        LabelStyle.LABEL_LARGE.applyTo(officerPhoneLabel);
        LabelStyle.LABEL_LARGE.applyTo(officerZoneLabel);

        loadZoneCheckboxes();

        ColumnConstraints col0 = new ColumnConstraints();
        col0.setMinWidth(120);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setMinWidth(300);

        formGridPane.getColumnConstraints().addAll(col0, col1);

        int row = 0;
        formGridPane.add(officerUsernameLabel, 0, row);
        formGridPane.add(officerUsernameTextField, 1, row++);

        formGridPane.add(officerFirstnameLabel, 0, row);
        formGridPane.add(officerFirstnameTextField, 1, row++);

        formGridPane.add(officerLastnameLabel, 0, row);
        formGridPane.add(officerLastnameTextField, 1, row++);

        formGridPane.add(officerPasswordLabel, 0, row);
        formGridPane.add(officerPasswordTextField, 1, row);
        formGridPane.add(copyPaaswordButton, 2, row++);

        formGridPane.add(officerEmailLabel, 0, row);
        formGridPane.add(officerEmailTextField, 1, row++);

        formGridPane.add(officerPhoneLabel, 0, row);
        formGridPane.add(officerPhoneTextField, 1, row);

        zoneVBox.getChildren().addAll(zoneRegion, officerZoneLabel, zoneCheckboxFlowPane);

        headingVBox.getChildren().addAll(headerLabel, descriptionLabel);
        parentOfficerVBox.getChildren().addAll(formGridPane, zoneVBox, addNewOfficerFilledButton);
    }

    public void initEvents() {
        footerNavBarButton.setOnAction(e -> onBackButtonClick());
        copyPaaswordButton.setOnAction(e -> onCopyPasswordButtonClick());
        addNewOfficerFilledButton.setOnAction(e -> addNewOfficerHandler());
    }

    protected void addNewOfficerHandler() {

        errorAddNewOfficerVBox.getChildren().clear();

        String username = officerUsernameTextField.getText();
        String firstname = officerFirstnameTextField.getText();
        String lastname = officerLastnameTextField.getText();
        String password = officerPasswordTextField.getText();
        String email = officerEmailTextField.getText();
        String phone = officerPhoneTextField.getText();

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
            showError("กรุณากรอกนามสกุลพนักงาน");
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

        alertUtil.info("สร้างพนักงานใหม่สำเร็จ", "ชื่อผู้ใช้ " + username + "\nรหัสผ่าน " + password);
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
            CheckBox checkBox = new CheckBox(zone.getZoneName());
            checkBox.setUserData(zone.getZoneUid());
            checkBox.setStyle("-fx-font-size: 14");
            zoneCheckBoxes.add(checkBox);
            zoneCheckboxFlowPane.getChildren().add(checkBox);
        }
    }

    protected void onCopyPasswordButtonClick() {
        String text = officerPasswordTextField.getText();
        if (text != null && !text.isEmpty()) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(text);
            clipboard.setContent(content);
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
