package ku.cs.controllers.components;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.context.AppContext;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.session.SessionManager;
import ku.cs.services.utils.AlertUtil;

import java.io.IOException;

public class AddNewZonePopup {
    public void run() {
        final SessionManager sessionManager = AppContext.getSessionManager();
        final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
        final AlertUtil alertUtil = new AlertUtil();

        sessionManager.requireAdminLogin();

        ZoneList zones = zonesProvider.loadCollection();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("เพิ่มจุดให้บริการใหม่");
        dialog.setHeaderText("โปรดกรอกชื่อจุดใ้ห้บริการใหม่");

        ButtonType addNewZoneButtonType = new ButtonType("เพิ่มจุดให้บริการ", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addNewZoneButtonType, ButtonType.CANCEL);

        VBox vBox = new VBox();
        vBox.setSpacing(10);

        Label zoneNameLabel = new Label("ชื่อจุดให้บริการ:");
        TextField zoneNameTextField = new TextField();
        zoneNameTextField.setPromptText("ชื่อจุดให้บริการ");

        vBox.getChildren().addAll(zoneNameLabel, zoneNameTextField);

        dialog.getDialogPane().setContent(vBox);

        // Not Empty
        Node addButton = dialog.getDialogPane().lookupButton(addNewZoneButtonType);
        addButton.setDisable(true);

        zoneNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty());
        });

        // Wait Result on dialog
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addNewZoneButtonType) {
                return addNewZoneButtonType;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            if (result == addNewZoneButtonType) {
                String zoneName = zoneNameTextField.getText().trim();

                if (zoneName.isEmpty()) {
                    alertUtil.error("เพ่ิมจุดให้บริการไม่สำเร็จ", "ยังไม่ได้กรอกชื่อจุดให้บริการ");
                    return;
                }

                // Add New Zone
                zones.addZone(zoneName);
                zonesProvider.saveCollection(zones);

                alertUtil.info("เพิ่มจุดให้บริการสำเร็จ", "จุดให้บริการ \"" + zoneName + "\" ได้ถูกเพิ่มเรียบร้อย!");
                try {
                    FXRouter.goTo("admin-manage-zones");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
