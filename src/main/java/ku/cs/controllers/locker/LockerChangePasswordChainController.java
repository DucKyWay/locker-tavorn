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
import ku.cs.models.request.Request;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.provider.KeyDatasourceProvider;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.ui.FXRouter;

import java.io.IOException;


public class LockerChangePasswordChainController {
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final KeyDatasourceProvider keysProvider = new KeyDatasourceProvider();

    @FXML
    private VBox lockerChangePasswordDialog;
    @FXML private TextField lockerPasswordTextField;
    @FXML private Label lockerPasswordErrorLabel;
    @FXML private Label lockerIdLabel;
    @FXML private Button lockerChangePasswordButton;

    @FXML private Button submitButton;
    @FXML private Button closeButton;

    private Request request;

    KeyList keyList;
    Key key;

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

        keyList = keysProvider.loadCollection(zone.getZoneUid());
        key = keyList.findKeyByUid(request.getLockerKeyUid());
    }

    private void initUserInterface() {
        lockerIdLabel.setText(key.getLockerUid());
        lockerPasswordTextField.setPromptText(key.getPasskey());


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

        key.setPasskey(lockerPass);
        keysProvider.saveCollection(zone.getZoneUid(), keyList);

        onCloseButtonClick();
    }

    private void onCloseButtonClick() {
        if (lockerChangePasswordDialog != null && lockerChangePasswordDialog.getScene() != null) {
            try {
                FXRouter.loadDialogStage(FXRouter.getCurrentBeforeRouteLabel());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                lockerChangePasswordDialog.getScene().getWindow().hide();
            }

        }
    }
}
