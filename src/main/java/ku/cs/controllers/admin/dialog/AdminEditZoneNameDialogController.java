package ku.cs.controllers.admin.dialog;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
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

    @FXML private AnchorPane editZoneNameDialog;
    @FXML private TextField zoneNameTextField;
    @FXML private Button submitButton;

    private ZoneList zones;
    private Zone zone;

    @FXML public void initialize() {
        sessionManager.requireAdminLogin();
        zones = zonesProvider.loadCollection();
        zone = (Zone) FXRouter.getData();

        submitButton.setOnAction(e -> onSubmitButtonClick());
    }

    private void onSubmitButtonClick() {
        String zoneName = zoneNameTextField.getText().trim();

        if (zoneName.isEmpty()) {
            alertUtil.error("เปลี่ยนชื่อจุดให้บริการไม่สำเร็จ", "ยังไม่ได้กรอกชื่อจุดให้บริการ");
            return;
        }

        // Edit Zone
        zone.setZoneName(zoneName);
        zoneService.update(zone);

        alertUtil.info("เปลี่ยนชื่อจุดให้บริการสำเร็จ", "จุดให้บริการ \"" + zoneName + "\" ได้ถูกแก้ไขแล้ว!");
        if(editZoneNameDialog != null && editZoneNameDialog.getScene() != null && editZoneNameDialog.getScene().getWindow() != null) {
            editZoneNameDialog.getScene().getWindow().hide();
        }
        try {
            FXRouter.goTo("admin-manage-zones");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
