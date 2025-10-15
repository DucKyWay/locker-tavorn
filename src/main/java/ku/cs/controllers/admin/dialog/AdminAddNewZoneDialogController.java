package ku.cs.controllers.admin.dialog;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.session.SessionManager;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.zone.ZoneService;

import java.io.IOException;
public class AdminAddNewZoneDialogController {
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final ZoneService zoneService = new ZoneService();
    private final SessionManager sessionManager = (SessionManager) FXRouter.getService("session");
    private final AlertUtil alertUtil = new AlertUtil();

    @FXML private AnchorPane addNewZoneDialog;
    @FXML private TextField zoneNameTextField;
    @FXML private Button submitButton;

    private ZoneList zones;

    @FXML public void initialize() {
        sessionManager.requireAdminLogin();
        zones = zonesProvider.loadCollection();

        submitButton.setOnAction(e -> onSubmitButtonClick());
    }

    private void onSubmitButtonClick() {
        String zoneName = zoneNameTextField.getText().trim();

        if (zoneName.isEmpty()) {
            alertUtil.error("เพ่ิมจุดให้บริการไม่สำเร็จ", "ยังไม่ได้กรอกชื่อจุดให้บริการ");
            return;
        }

        // Add New Zone
        zones.addZone(zoneName);
        zonesProvider.saveCollection(zones);

        alertUtil.info("เพิ่มจุดให้บริการสำเร็จ", "จุดให้บริการ \"" + zoneName + "\" ได้ถูกเพิ่มเรียบร้อย!");
        if(addNewZoneDialog != null && addNewZoneDialog.getScene() != null && addNewZoneDialog.getScene().getWindow() != null) {
            addNewZoneDialog.getScene().getWindow().hide();
        }
        try {
            FXRouter.goTo("admin-manage-zones");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}