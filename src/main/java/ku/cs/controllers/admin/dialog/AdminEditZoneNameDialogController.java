package ku.cs.controllers.admin.dialog;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import ku.cs.components.Icons;
import ku.cs.components.button.ElevatedButton;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.components.button.FilledButton;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.session.SessionManager;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.zone.ZoneService;

import java.io.IOException;

public class AdminEditZoneNameDialogController {
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final ZoneService zoneService = new ZoneService();
    private final SessionManager sessionManager = (SessionManager) FXRouter.getService("session");
    private final AlertUtil alertUtil = new AlertUtil();

    @FXML private VBox editZoneNameDialog;
    @FXML private TextField zoneNameTextField;
    @FXML private Label zoneNameErrorLabel;
    @FXML private Label zoneIdLabel;
    @FXML private Button zoneNameButton;

    @FXML private Button submitButton;
    @FXML private Button closeButton;

    private Zone zone;

    @FXML public void initialize() {
        sessionManager.requireAdminLogin();
        zone = (Zone) FXRouter.getData();
        zoneNameTextField.setText(zone.getZoneName());
        zoneIdLabel.setText(zone.getZoneUid());

        ElevatedButtonWithIcon.SMALL.mask(zoneNameButton, Icons.LOCATION);
        zoneNameButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);;

        FilledButton.mask(submitButton);
        submitButton.setOnAction(e -> onSubmitButtonClick());

        ElevatedButton.mask(closeButton);
        closeButton.setOnAction(e -> onCloseButtonClick());
    }

    private void onSubmitButtonClick() {
        String zoneName = zoneNameTextField.getText().trim();

        if (zoneName.isEmpty()) {
            zoneNameErrorLabel.setText("ยังไม่ได้กรอกชื่อจุดให้บริการ");
            return;
        }else
            zoneNameButton.setText("");

        zone.setZoneName(zoneName);
        zoneService.update(zone);

        alertUtil.info("เปลี่ยนชื่อจุดให้บริการสำเร็จ", "จุดให้บริการ \"" + zoneName + "\" ได้ถูกแก้ไขแล้ว!");
        if(editZoneNameDialog != null && editZoneNameDialog.getScene() != null && editZoneNameDialog.getScene().getWindow() != null) {
            onCloseButtonClick();
        }
        try {
            FXRouter.goTo("admin-manage-zones");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void onCloseButtonClick() {
        if (editZoneNameDialog != null && editZoneNameDialog.getScene() != null) {
            editZoneNameDialog.getScene().getWindow().hide();
        }
    }
}
