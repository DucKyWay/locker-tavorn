package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.components.button.FilledButton;
import ku.cs.components.button.IconButton;
import ku.cs.models.account.OfficerForm;
import ku.cs.models.account.OfficerList;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.accounts.strategy.OfficerAccountProvider;
import ku.cs.services.utils.AccountValidator;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.PasswordUtil;
import ku.cs.services.utils.UuidUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminManageNewOfficerController extends BaseAdminController {
    private final PasswordUtil passwordUtil = new PasswordUtil();
    private final OfficerAccountProvider officersProvider = new OfficerAccountProvider();
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();

    @FXML private VBox headingVBox;
    @FXML private VBox parentOfficerVBox;
    @FXML private VBox errorAddNewOfficerVBox;

    private TextField officerUsernameTextField;
    private TextField officerFirstnameTextField;
    private TextField officerLastnameTextField;
    private TextField officerPasswordTextField;
    private Button copyPasswordButton;
    private TextField officerEmailTextField;
    private TextField officerPhoneTextField;
    private FlowPane zoneCheckboxFlowPane;

    private final List<CheckBox> zoneCheckBoxes = new ArrayList<>();
    @FXML private Button addNewOfficerFilledButton;
    @FXML private Button backButton;

    private OfficerList officers;
    private ZoneList zones;

    @Override
    protected void initDatasource() {
        officers = officersProvider.loadCollection();
        zones = zonesProvider.loadCollection();
    }

    @Override
    protected void initUserInterfaces() {
        Label headerLabel = new Label("เพิ่มพนักงานใหม่");
        Label descriptionLabel = new Label("กรุณากรอกข้อมูลให้ครบ");
        ElevatedButtonWithIcon.MEDIUM.mask(backButton, Icons.ARROW_LEFT);
        Region region = new Region();
        Region zoneRegion = new Region();
        region.setPrefSize(850, 50);
        zoneRegion.setPrefSize(850, 20);

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
        officerPasswordTextField.setText(new UuidUtil().generateShort());
        officerPasswordTextField.setDisable(true);
        copyPasswordButton = new IconButton(new Icon(Icons.COPY));

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
        formGridPane.add(copyPasswordButton, 2, row++);

        formGridPane.add(officerEmailLabel, 0, row);
        formGridPane.add(officerEmailTextField, 1, row++);

        formGridPane.add(officerPhoneLabel, 0, row);
        formGridPane.add(officerPhoneTextField, 1, row);

        zoneVBox.getChildren().addAll(zoneRegion, officerZoneLabel, zoneCheckboxFlowPane);

        headingVBox.getChildren().addAll(headerLabel, descriptionLabel);
        parentOfficerVBox.getChildren().addAll(formGridPane, zoneVBox, addNewOfficerFilledButton);
    }

    @Override
    protected void initEvents() {
        if (footerNavBarButton != null) {
            footerNavBarButton.setOnAction(e -> onBackButtonClick());
        }
        copyPasswordButton.setOnAction(e -> onCopyPasswordButtonClick());
        addNewOfficerFilledButton.setOnAction(e -> addNewOfficerHandler());
    }

    private void addNewOfficerHandler() {
        errorAddNewOfficerVBox.getChildren().clear();

        OfficerForm form = new OfficerForm(
                officerUsernameTextField.getText(),
                officerFirstnameTextField.getText(),
                officerLastnameTextField.getText(),
                officerPasswordTextField.getText(),
                officerEmailTextField.getText(),
                officerPhoneTextField.getText(),
                collectSelectedZones()
        );

        List<String> errors = new AccountValidator().validateNewOfficer(form);
        if (!errors.isEmpty()) {
            showErrors(errors);
            return;
        }

        String hashedPassword = passwordUtil.hashPassword(form.password());

        officers.addOfficer(
                form.username(), form.firstname(), form.lastname(),
                hashedPassword, form.password(), form.email(), form.phone(),
                new ArrayList<>(form.zoneUids())
        );
        officersProvider.saveCollection(officers);

        new AlertUtil().info("สร้างพนักงานใหม่สำเร็จ",
                "ชื่อผู้ใช้ " + form.username() + "\nรหัสผ่าน " + form.password());

        try {
            FXRouter.goTo("admin-manage-officers");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> collectSelectedZones() {
        List<String> selected = new ArrayList<>();
        for (CheckBox cb : zoneCheckBoxes) {
            if (cb.isSelected()) {
                selected.add((String) cb.getUserData());
            }
        }
        return selected;
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

    private void onCopyPasswordButtonClick() {
        String text = officerPasswordTextField.getText();
        if (text != null && !text.isEmpty()) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(text);
            clipboard.setContent(content);
        }
    }

    private void onBackButtonClick() {
        try {
            FXRouter.goTo("admin-manage-officers");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showErrors(List<String> errors) {
        for (String msg : errors) {
            Label errorLabel = new Label(msg);
            errorLabel.setStyle("-fx-text-fill: red;");
            LabelStyle.LABEL_MEDIUM.applyTo(errorLabel);
            errorAddNewOfficerVBox.getChildren().add(errorLabel);
        }
    }
}
