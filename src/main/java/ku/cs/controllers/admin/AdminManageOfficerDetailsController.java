package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.FilledButton;
import ku.cs.models.account.Account;
import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.OfficerListFileDatasource;

import java.io.FileNotFoundException;
import java.io.IOException;

public class AdminManageOfficerDetailsController {

    @FXML private HBox parentHBoxFilled;
    @FXML private Button backFilledButton;
    @FXML private VBox testVbox;
    @FXML private Label officerUsernameLabel;
    @FXML private Label officerNameLabel;
    @FXML private Label officerEmailLabel;
    @FXML private Label officerPhoneLabel;
    @FXML private Label officerRoleLabel;
    @FXML private Label officerImagePathLabel;

    private OfficerList officers;
    private Datasource<OfficerList> datasource;

    private Account current;
    private Officer officer;

    public void initialize() throws FileNotFoundException {
        SessionManager.requireAdminLogin();
        current = SessionManager.getCurrentAccount();

        initDatasource();
        initUserInterfaces();
        initEvents();
    }

    public void initDatasource() throws FileNotFoundException {
        datasource = new OfficerListFileDatasource("data", "test-officer-data.json");
        officers = datasource.readData();

        officer = (Officer) FXRouter.getData();
    }

    public void initUserInterfaces() throws FileNotFoundException {
        Region region = new Region();

        parentHBoxFilled.setSpacing(4);
        region.setPrefSize(850, 50);

        backFilledButton = new FilledButton("ย้อนกลับ");

        parentHBoxFilled.getChildren().addAll(backFilledButton, region);

        showOfficer(officer);
    }

    public void initEvents() throws FileNotFoundException {
        backFilledButton.setOnAction(e -> onBackButtonClick());
    }

    private void showOfficer(Officer officer) {
        officerUsernameLabel = new Label(officer.getUsername());
        officerNameLabel = new Label(officer.getName());
        officerEmailLabel = new Label(officer.getEmail());
        officerPhoneLabel = new Label(officer.getTelphone());
        officerRoleLabel = new Label(String.valueOf(officer.getRole()));
        officerImagePathLabel = new Label(officer.getImagePath());

        LabelStyle.LABEL_MEDIUM.applyTo(officerUsernameLabel);
        LabelStyle.LABEL_MEDIUM.applyTo(officerNameLabel);
        LabelStyle.LABEL_MEDIUM.applyTo(officerEmailLabel);
        LabelStyle.LABEL_MEDIUM.applyTo(officerPhoneLabel);
        LabelStyle.LABEL_MEDIUM.applyTo(officerRoleLabel);
        LabelStyle.LABEL_MEDIUM.applyTo(officerImagePathLabel);

        testVbox.getChildren().addAll(officerUsernameLabel, officerNameLabel, officerEmailLabel, officerPhoneLabel, officerRoleLabel, officerImagePathLabel);
    }

    protected void onBackButtonClick() {
        try {
            FXRouter.goTo("admin-manage-officers", current);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
