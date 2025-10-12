package ku.cs.controllers.components;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.context.AppContext;
import ku.cs.services.session.SessionManager;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.utils.AlertUtil;

public class EditZoneNamePopup {
    public void run(Zone zone) {
        final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
        final SessionManager sessionManager = AppContext.getSessionManager();
        final AlertUtil alertUtil = new AlertUtil();

        sessionManager.requireAdminLogin();

        ZoneList zones = zonesProvider.loadCollection();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("จุดการจุดให้บริการ");
        dialog.setHeaderText("กรอกชื่อจุดให้บริการใหม่");

        ButtonType changeZoneNameButtonType = new ButtonType("แก้ไขจุดให้บริการ", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(changeZoneNameButtonType, ButtonType.CANCEL);

        VBox vBox = new VBox();
        vBox.setSpacing(10);

        Label zoneNameLabel = new Label("ชื่อจุดให้บริการ:");
        TextField zoneNameTextField = new TextField();
        zoneNameTextField.setText(zone.getZoneName());
        zoneNameTextField.setPromptText("ชื่อจุดให้บริการ");

        vBox.getChildren().addAll(zoneNameLabel, zoneNameTextField);

        dialog.getDialogPane().setContent(vBox);

        // Not Empty
        Node addButton = dialog.getDialogPane().lookupButton(changeZoneNameButtonType);
        addButton.setDisable(true);

        zoneNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty());
        });

        // Wait Result on dialog
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == changeZoneNameButtonType) {
                return changeZoneNameButtonType;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            if (result == changeZoneNameButtonType) {
                String zoneName = zoneNameTextField.getText().trim();

                if (zoneName.isEmpty()) {
                    alertUtil.error("เปลี่ยนชื่อจุดให้บริการไม่สำเร็จ", "ยังไม่ได้กรอกชื่อจุดให้บริการ");
                    return;
                }

                // Edit Zone
                zone.setZoneName(zoneName);
                zonesProvider.saveCollection(zones);

                alertUtil.info("เปลี่ยนชื่อจุดให้บริการสำเร็จ", "จุดให้บริการ \"" + zoneName + "\" ได้ถูกแก้ไขแล้ว!");
            }
        });
    }
}
