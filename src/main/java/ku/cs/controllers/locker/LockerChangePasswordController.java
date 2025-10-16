package ku.cs.controllers.locker;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ku.cs.components.Icons;
import ku.cs.components.button.ElevatedButton;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.components.button.FilledButton;
import ku.cs.models.key.Key;
import ku.cs.models.key.KeyList;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.request.Request;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.provider.KeyDatasourceProvider;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.ui.FXRouter;

import java.io.IOException;


public class LockerChangePasswordController {
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final LockerDatasourceProvider lockersProvider = new LockerDatasourceProvider();

    @FXML
    private VBox lockerChangePasswordDialog;
    @FXML private TextField lockerPasswordTextField;
    @FXML private Label lockerPasswordErrorLabel;
    @FXML private Label lockerIdLabel;
    @FXML private Button lockerChangePasswordButton;

    @FXML private Button submitButton;
    @FXML private Button closeButton;

    private Request request;

    LockerList lockerList;
    Locker locker;

    ZoneList zoneList;
    Zone zone;

    @FXML public void initialize() {
        request = (Request) FXRouter.getData();
        initializeDatasource();
        initEvents();
        initUserInterface();
    }

    private void initializeDatasource() {
        zoneList = zonesProvider.loadCollection();
        zone = zoneList.findZoneByUid(request.getZoneUid());

        lockerList = lockersProvider.loadCollection(zone.getZoneUid());
        locker = lockerList.findLockerByUid(request.getLockerUid());
    }

    private void initUserInterface() {
        lockerIdLabel.setText(locker.getLockerUid());
        lockerPasswordTextField.setPromptText(locker.getPassword());

        ElevatedButtonWithIcon.SMALL.mask(lockerChangePasswordButton, Icons.EDIT);
        lockerChangePasswordButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);;

        FilledButton.mask(submitButton);
        submitButton.setOnAction(e -> onSubmitButtonClick());

        ElevatedButton.mask(closeButton);
        closeButton.setOnAction(e -> onCloseButtonClick());
    }

    private void initEvents() {}

    private void onSubmitButtonClick() {
        String lockerPass = lockerPasswordTextField.getText().trim();

        if (lockerPass.isBlank() || !lockerPass.matches("\\d{5}")) {
           lockerPasswordErrorLabel.setText("รหัสต้องเป็นตัวเลข 5 หลัก");
           return;
        }else
            lockerPasswordErrorLabel.setText("");

        locker.setPassword(lockerPass);
        lockersProvider.saveCollection(zone.getZoneUid(), lockerList);

        onCloseButtonClick();
    }

    private void onCloseButtonClick() {
        if (lockerChangePasswordDialog != null && lockerChangePasswordDialog.getScene() != null) {
            try {
                System.out.println(FXRouter.getCurrentBeforeRouteLabel());
                FXRouter.loadDialogStage(FXRouter.getCurrentBeforeRouteLabel());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                lockerChangePasswordDialog.getScene().getWindow().hide();
            }

        }
    }
}
